/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.hash;

import com.google.common.hash.Hashing;

import java.util.Arrays;

/**
 * Accumulates bounded feature contributions while preserving overlap in text and child signatures.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class SimilarityAccumulator {

	/** Accumulated normalized object votes. */
	private final double[] objectVotes = new double[Long.SIZE];

	/** Weighted token votes for the current feature. */
	private final long[] featureVotes = new long[Long.SIZE];

	/** Bits on which the current feature contributes. */
	private long featureBitMask;

	/** Total token weight of the current feature. */
	private long totalTokenWeight;

	/** Lone token retained until a second token requires per-bit feature voting. */
	private long singleTokenHash;

	/** Creates an accumulator for an object or cached feature contribution. */
	SimilarityAccumulator() {
	}

	/**
	 * Starts accumulating a new feature's tokens on its assigned subset of signature bits. The previous
	 * feature must have been finished before starting another.
	 *
	 * @param bitMask
	 *            the bit positions on which this feature is allowed to vote
	 */
	void startFeature(long bitMask) {
		featureBitMask = bitMask;
		totalTokenWeight = 0;
	}

	/**
	 * Adds one token with unit relative weight to the current feature.
	 *
	 * @param tokenHash
	 *            the token's 64-bit hash
	 */
	void addToken(long tokenHash) {
		addToken(tokenHash, 1);
	}

	/**
	 * Adds a weighted token to the current feature, delaying per-bit staging for a lone scalar token.
	 *
	 * @param tokenHash
	 *            the token's 64-bit hash
	 * @param tokenWeight
	 *            the token's relative weight within this feature, before feature-wide normalization
	 */
	private void addToken(long tokenHash, int tokenWeight) {
		// Most stored features are scalar; avoid staging a vector for a single token.
		if (totalTokenWeight == 0 && tokenWeight == 1) {
			singleTokenHash = tokenHash;
			totalTokenWeight = 1;
			return;
		}
		if (totalTokenWeight == 0 || totalTokenWeight == 1) {
			Arrays.fill(featureVotes, 0);
			if (totalTokenWeight == 1) {
				accumulateTokenVotes(singleTokenHash, 1);
			}
		}
		accumulateTokenVotes(tokenHash, tokenWeight);
		totalTokenWeight += tokenWeight;
	}

	/**
	 * Adds weighted positive or negative votes on the current feature's active bit positions.
	 *
	 * @param tokenHash
	 *            the token's 64-bit hash
	 * @param tokenWeight
	 *            its relative vote weight within the current feature
	 */
	private void accumulateTokenVotes(long tokenHash, int tokenWeight) {
		for (long bits = featureBitMask; bits != 0; bits &= bits - 1) {
			int bit = Long.numberOfTrailingZeros(bits);
			int contribution = -tokenWeight;
			if ((tokenHash & (1L << bit)) != 0) {
				contribution = tokenWeight;
			}
			featureVotes[bit] += contribution;
		}
	}

	/**
	 * Adds words, identifier parts and overlapping character triples as evidence for one text value.
	 * Words split at punctuation, digit transitions, camel case and acronym boundaries. Short text uses
	 * its whole value when there are no character triples.
	 *
	 * @param text
	 *            the captured text to tokenize
	 * @param featureHash
	 *            the feature-name hash mixed into word and character-triple hashes
	 */
	void addTextTokens(String text, long featureHash) {
		int wordStart = -1;
		for (int characterIndex = 0; characterIndex <= text.length(); characterIndex++) {
			if (characterIndex == text.length()
					|| !Character.isLetterOrDigit(text.charAt(characterIndex))) {
				if (wordStart >= 0) {
					addWord(text, wordStart, characterIndex, featureHash);
					wordStart = -1;
				}
			} else if (wordStart < 0) {
				wordStart = characterIndex;
			} else if (isWordBoundary(text, characterIndex)) {
				addWord(text, wordStart, characterIndex, featureHash);
				wordStart = characterIndex;
			}
		}
		if (text.length() < 3) {
			addToken(Hashing.murmur3_128().hashUnencodedChars(text).asLong() ^ featureHash);
		} else {
			for (int characterIndex = 0; characterIndex < text.length() - 2; characterIndex++) {
				long characterTriple = (long)text.charAt(characterIndex) << Integer.SIZE
						| (long)text.charAt(characterIndex + 1) << Character.SIZE
						| text.charAt(characterIndex + 2);
				addToken(Hashing.murmur3_128().hashLong(characterTriple ^ featureHash).asLong());
			}
		}
	}

	/**
	 * Detects digit, camel-case and acronym boundaries within an identifier.
	 *
	 * @param text
	 *            the captured text
	 * @param index
	 *            the character after a possible boundary
	 * @return whether a new identifier word starts at the index
	 */
	private static boolean isWordBoundary(String text, int index) {
		char previous = text.charAt(index - 1);
		char current = text.charAt(index);
		if (Character.isDigit(previous) != Character.isDigit(current)) {
			return true;
		}
		if (Character.isLowerCase(previous) && Character.isUpperCase(current)) {
			return true;
		}
		return Character.isUpperCase(previous) && Character.isUpperCase(current)
				&& index + 1 < text.length() && Character.isLowerCase(text.charAt(index + 1));
	}

	/**
	 * Adds a word with a weight balanced against all character triples in the containing text.
	 *
	 * @param text
	 *            the captured text containing the word
	 * @param wordStart
	 *            the inclusive character index of the word
	 * @param wordEnd
	 *            the exclusive character index of the word
	 * @param featureHash
	 *            the feature-name hash mixed into the word's hash
	 */
	private void addWord(String text, int wordStart, int wordEnd, long featureHash) {
		// Each word or identifier part carries as much evidence as all character fragments together.
		addToken(Hashing.murmur3_128().hashUnencodedChars(text.substring(wordStart, wordEnd)).asLong()
				^ featureHash, Math.max(1, text.length() - 2));
	}

	/**
	 * Normalizes the feature's token votes to its fixed weight and adds them to the object's votes. This
	 * keeps long text and large collections from overwhelming the contributions of other features.
	 *
	 * @param featureWeight
	 *            the feature's overall vote budget, independent of token count
	 */
	void finishFeature(int featureWeight) {
		if (totalTokenWeight == 0) {
			return;
		}
		// A feature has a fixed vote budget, regardless of text length or collection size.
		double normalizationScale = (double)featureWeight / totalTokenWeight;
		for (long bits = featureBitMask; bits != 0; bits &= bits - 1) {
			int bit = Long.numberOfTrailingZeros(bits);
			double contribution;
			if (totalTokenWeight == 1) {
				contribution = -featureWeight;
				if ((singleTokenHash & (1L << bit)) != 0) {
					contribution = featureWeight;
				}
			} else {
				contribution = normalizationScale * featureVotes[bit];
			}
			objectVotes[bit] += contribution;
		}
	}

	/**
	 * Returns the completed feature vote vector for caching without copying it. The caller must finish
	 * the feature first and never modify this accumulator after sharing its vector.
	 *
	 * @return the normalized per-bit contribution, which callers must keep immutable
	 */
	double[] finishedFeatureContribution() {
		return objectVotes;
	}

	/**
	 * Adds a previously normalized, immutable feature contribution to the object's accumulated votes.
	 *
	 * @param contribution
	 *            the cached per-bit votes for one feature value
	 */
	void addCachedFeatureContribution(double[] contribution) {
		for (int bit = 0; bit < Long.SIZE; bit++) {
			objectVotes[bit] += contribution[bit];
		}
	}

	/**
	 * Builds the signature from bits whose accumulated votes are positive.
	 *
	 * @return the 64-bit signature
	 */
	long signature() {
		long signatureBits = 0;
		for (int bit = 0; bit < Long.SIZE; bit++) {
			if (objectVotes[bit] > 0) {
				signatureBits |= 1L << bit;
			}
		}
		return signatureBits;
	}
}

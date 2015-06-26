/*******************************************************************************
 * Copyright (c) 2015 EclipseSource GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Borkowski - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import org.eclipse.emf.compare.internal.dmp.LineBasedDiff;
import org.eclipse.emf.compare.internal.dmp.diff_match_patch;
import org.eclipse.emf.compare.internal.dmp.diff_match_patch.Diff;

/**
 * This class is responsible for computing similarities between two text contents and deciding whether they
 * are close enough to be considered a rename.
 * 
 * @author Michael Borkowski <mborkowski@eclipsesource.com>
 */
public final class SimilarityComputer {
	/**
	 * The minimum length both sides must have to not be ignored (short text contents might seem similar and
	 * cause false negatives).
	 */
	public static final int MINIMUM_LENGTH = 1024;

	/**
	 * The maximum percentage of differing lines contained in the content for files to be considered a rename.
	 */
	public static final double THRESHOLD = 0.3;

	/**
	 * Private constructor to prevent instantiation.
	 */
	private SimilarityComputer() {
	}

	/**
	 * Decides whether two input streams are similar. This methods calls
	 * {@link #computeDifference(InputStream, InputStream)} and compares the value with {@link #THRESHOLD}.
	 * 
	 * @param a
	 *            the first input stream
	 * @param b
	 *            the second input stream
	 * @return <code>true</code> if the input streams are similar
	 * @throws IOException
	 *             if reading of one of the input streams fails
	 */
	public static boolean isSimilar(InputStream a, InputStream b) throws IOException {
		return computeDifference(a, b) < THRESHOLD;
	}

	/**
	 * Computes the difference between two {@link InputStream} instances. The returned value is a ratio of
	 * changed lines to total lines, where total lines is denoted by the maximum of the line counts of both
	 * input streams. This method returns {@link Double#MAX_VALUE} if one or both of the streams are
	 * <code>null</code> or if the content is too short to be compared (shorter than {@link #MINIMUM_LENGTH}).
	 * 
	 * @param a
	 *            the first input stream
	 * @param b
	 *            the second input stream
	 * @return how different the two streams are
	 * @throws IOException
	 *             if reading of one of the input streams fails
	 */
	public static double computeDifference(InputStream a, InputStream b) throws IOException {
		if (a == null || b == null) {
			return Double.MAX_VALUE;
		}

		try {
			LineFile fileA;
			LineFile fileB;

			// even though the file might not be encoded in UTF-8, decoding both in UTF-8 should not harm the
			// similarity function
			fileA = readUtf8(a);
			fileB = readUtf8(b);

			return internalCalculateSimilarity(fileA, fileB);
		} finally {
			try {
				a.close();
			} catch (IOException ignored) {
				// ignore
			}
			try {
				b.close();
			} catch (IOException ignored) {
				// ignore
			}
		}
	}

	/**
	 * Internal method for calculating similarity, without checks.
	 * 
	 * @param a
	 *            the first file
	 * @param b
	 *            the second file
	 * @return the similarity
	 */
	private static double internalCalculateSimilarity(LineFile a, LineFile b) {
		if (a.characterCount < MINIMUM_LENGTH || b.characterCount < MINIMUM_LENGTH) {
			return Double.MAX_VALUE;
		}

		final LineBasedDiff lineBasedDiff = new LineBasedDiff();

		long differences = 0;

		final LinkedList<Diff> diffs = lineBasedDiff.diff_main(a.content, b.content, false);

		for (Diff diff : diffs) {
			if (diff.operation != diff_match_patch.Operation.EQUAL) {
				differences++;
			}
		}

		return (double)differences / Math.max(a.lineCount, b.lineCount);
	}

	/**
	 * Reads the stream as a UTF-8 encoded stream.
	 * 
	 * @param stream
	 *            the stream to read from
	 * @return the resulting string
	 * @throws IOException
	 *             if reading fails
	 */
	private static LineFile readUtf8(InputStream stream) throws IOException {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8")); //$NON-NLS-1$
		final StringBuilder builder = new StringBuilder();
		final LineFile result = new LineFile();

		String rd;
		while ((rd = reader.readLine()) != null) {
			builder.append(rd);
			builder.append('\n');
			result.lineCount++;
			result.characterCount += rd.length() + 1;
		}

		result.content = builder.toString();
		return result;
	}

	/**
	 * An auxiliary data structure for internally representing a file.
	 * 
	 * @author mborkowski
	 */
	private static class LineFile {
		/**
		 * The character count of the file.
		 */
		long characterCount;

		/**
		 * The line count of the file.
		 */
		long lineCount;

		/**
		 * The contents of the file.
		 */
		String content;
	}
}

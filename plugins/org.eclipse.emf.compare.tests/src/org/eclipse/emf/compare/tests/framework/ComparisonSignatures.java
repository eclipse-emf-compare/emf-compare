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
package org.eclipse.emf.compare.tests.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.FeatureMapChange;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * Describes matcher results for diagnostics without choosing test expectations. Resource-backed objects use
 * URI keys so independently loaded fixture copies can be compared. Detached objects are kept as object
 * values. Each map counts repeated signatures so duplicate matches or differences cannot disappear during
 * comparison.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ComparisonSignatures {
	/** Prevents instantiation. */
	private ComparisonSignatures() {
	}

	/**
	 * Counts signatures containing the left, right and ancestor object keys. A nested match also includes its
	 * parent's three partner keys, so different match hierarchies remain distinguishable.
	 *
	 * @param comparison
	 *            the comparison in progress
	 * @return the counted match signatures
	 */
	public static Map<List<Object>, Integer> matches(Comparison comparison) {
		Map<List<Object>, Integer> signatureCounts = new HashMap<>();
		for (Match rootMatch : comparison.getMatches()) {
			countMatchSignature(signatureCounts, rootMatch);
			rootMatch.getAllSubmatches().forEach(match -> countMatchSignature(signatureCounts, match));
		}
		return signatureCounts;
	}

	/**
	 * Counts a match together with its parent correspondence.
	 *
	 * @param signatureCounts
	 *            the accumulator of signature occurrence counts
	 * @param match
	 *            the partial object match
	 */
	private static void countMatchSignature(Map<List<Object>, Integer> signatureCounts, Match match) {
		List<Object> signatureComponents = new ArrayList<>(partnerKeys(match));
		if (match.eContainer() instanceof Match) {
			signatureComponents.addAll(partnerKeys((Match)match.eContainer()));
		}
		signatureCounts.merge(signatureComponents, 1, Integer::sum);
	}

	/**
	 * Returns the left, right and ancestor partners using diagnostic keys.
	 *
	 * @param match
	 *            the partial object match
	 * @return the three partner keys, including nulls for absent sides
	 */
	private static List<Object> partnerKeys(Match match) {
		return Arrays.asList(signatureValue(match.getLeft()), signatureValue(match.getRight()),
				signatureValue(match.getOrigin()));
	}

	/**
	 * Counts signatures containing object partners, difference type, kind, source and affected feature/value.
	 * Resource attachments retain their resource URI; feature-map values retain both the entry's structural
	 * feature and its value.
	 *
	 * @param comparison
	 *            the comparison in progress
	 * @return the counted difference signatures
	 */
	public static Map<List<Object>, Integer> differences(Comparison comparison) {
		Map<List<Object>, Integer> signatureCounts = new HashMap<>();
		for (Diff diff : comparison.getDifferences()) {
			List<Object> signatureComponents = new ArrayList<>(partnerKeys(diff.getMatch()));
			Collections.addAll(signatureComponents, signatureValue(diff.eClass()), diff.getKind(),
					diff.getSource());
			if (diff instanceof AttributeChange) {
				AttributeChange change = (AttributeChange)diff;
				Collections.addAll(signatureComponents, signatureValue(change.getAttribute()),
						signatureValue(change.getValue()));
			} else if (diff instanceof ReferenceChange) {
				ReferenceChange change = (ReferenceChange)diff;
				Collections.addAll(signatureComponents, signatureValue(change.getReference()),
						signatureValue(change.getValue()));
			} else if (diff instanceof ResourceAttachmentChange) {
				signatureComponents.add(((ResourceAttachmentChange)diff).getResourceURI());
			} else if (diff instanceof FeatureMapChange) {
				FeatureMapChange change = (FeatureMapChange)diff;
				Collections.addAll(signatureComponents, signatureValue(change.getAttribute()),
						signatureValue(change.getValue()));
			} else {
				throw new AssertionError("Unhandled difference type: " + diff.eClass().getName());
			}
			signatureCounts.merge(signatureComponents, 1, Integer::sum);
		}
		return signatureCounts;
	}

	/**
	 * Gives independently loaded copies of a fixture the same diagnostic key.
	 *
	 * @param component
	 *            the captured signature component
	 * @return its URI when resource-backed, a feature/value pair for a feature-map entry, or the value itself
	 */
	private static Object signatureValue(Object component) {
		if (component instanceof EObject && ((EObject)component).eResource() != null) {
			return EcoreUtil.getURI((EObject)component);
		}
		if (component instanceof FeatureMap.Entry) {
			FeatureMap.Entry entry = (FeatureMap.Entry)component;
			return Arrays.asList(signatureValue(entry.getEStructuralFeature()),
					signatureValue(entry.getValue()));
		}
		return component;
	}
}

/*******************************************************************************
 * Copyright (c) 2014, 2019 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Christian W. Damus - bug 522080
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ComparisonCanceledException;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.FeatureFilter;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.uml2.internal.postprocessor.extension.stereotype.UMLStereotypedElementChangeFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.util.UMLCompareUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.uml2.uml.Element;

/**
 * Post processor that creates the {@link org.eclipse.emf.compare.uml2.internal.StereotypedElementChange} in
 * the comparison model.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class StereotypedElementChangePostProcessor implements IPostProcessor {

	/**
	 * {@link UMLStereotypedElementChangeFactory}.
	 */
	private UMLStereotypedElementChangeFactory factory = new UMLStereotypedElementChangeFactory();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postMatch(Comparison , Monitor)
	 */
	public void postMatch(Comparison comparison, Monitor monitor) {
		// Re-match applications and unapplications of the same stereotypes to/from
		// the same base elements
		Map<Match, Map<URI, Match>> leftApplications = new HashMap<>();
		Map<Match, Map<URI, Match>> rightApplications = new HashMap<>();
		mapStereotypeApplications(comparison, leftApplications, rightApplications);

		for (Map.Entry<Match, Map<URI, Match>> left : leftApplications.entrySet()) {
			EObject base = left.getKey();
			Map<URI, Match> right = rightApplications.get(base);
			if (right != null) {
				for (Map.Entry<URI, Match> next : left.getValue().entrySet()) {
					Match rightMatch = right.get(next.getKey());
					if (rightMatch != null) {
						// Complete the left match
						next.getValue().setRight(rightMatch.getRight());
						// Drop the right match from the comparison
						EcoreUtil.remove(rightMatch);
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postDiff(Comparison , Monitor)
	 */
	public void postDiff(final Comparison comparison, Monitor monitor) {
		// We only need to do this for three-way comparisons to check for conflicts
		// in stereotypes applied to the same elemnt on both sides but with different
		// values
		if (comparison.isThreeWay()) {
			AttributeDiffEngine engine = new AttributeDiffEngine();
			for (Match next : comparison.getMatches()) {
				if (next.getOrigin() == null && next.getLeft() != null && next.getRight() != null) {
					// Is it an add-add of a stereotype application?
					if (isStereotypeApplication(next.getLeft())) {
						engine.checkForDifferences(next, monitor);
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postRequirements(Comparison, Monitor)
	 */
	public void postRequirements(Comparison comparison, Monitor monitor) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postEquivalences(Comparison, Monitor)
	 */
	public void postEquivalences(Comparison comparison, Monitor monitor) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postConflicts(Comparison, Monitor)
	 */
	public void postConflicts(Comparison comparison, Monitor monitor) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postComparison(Comparison, Monitor)
	 */
	public void postComparison(Comparison comparison, Monitor monitor) {
		for (Diff difference : comparison.getDifferences()) {
			if (factory.handles(difference)) {
				Diff stereotypedElementChange = factory.create(difference);
				if (!stereotypedElementChange.getRefinedBy().isEmpty()) {
					final Match match = factory.getParentMatch(difference);
					// FIXME: why the match may be null ? (see AddAssociation2Test.testMergeLtRA30UseCase)
					if (match != null) {
						match.getDifferences().add(stereotypedElementChange);
					}
				}
			}
		}
	}

	/**
	 * Compute maps of left- and right-side dangling matches of stereotype applications and unapplications.
	 * These map the {@link Match} for a base UML element to mappings of stereotypes (by {@link URI} of the
	 * definition) to matches for the stereotype application, itself.
	 * 
	 * @param comparison
	 *            the comparison in which to map stereotype application matches
	 * @param leftApplications
	 *            the map of left-side matches to populate
	 * @param rightApplications
	 *            the map of right-side matches to populate
	 */
	protected void mapStereotypeApplications(Comparison comparison,
			Map<Match, Map<URI, Match>> leftApplications, Map<Match, Map<URI, Match>> rightApplications) {
		for (Match next : comparison.getMatches()) {
			if (next.getLeft() != null && next.getRight() == null) {
				// Unmatched left. Is it a stereotype application?
				Match base = comparison.getMatch(getBaseElement(next.getLeft()));
				if (base != null) {
					// Map it
					put(leftApplications, base, getStereotypeURI(next.getLeft()), next);
				}
			} else if (next.getRight() != null && next.getLeft() == null) {
				// Unmatched right. Is it a stereotype application?
				Match base = comparison.getMatch(getBaseElement(next.getRight()));
				if (base != null) {
					// Map it
					put(rightApplications, base, getStereotypeURI(next.getRight()), next);
				}
			}
		}
	}

	/**
	 * Obtains the URI of a stereotype application's type, for comparison independent of resource set context.
	 * 
	 * @param stereotypeApplication
	 *            a stereotype applicatkion
	 * @return the URI of its defining (stereo)type
	 */
	private static URI getStereotypeURI(EObject stereotypeApplication) {
		return EcoreUtil.getURI(stereotypeApplication.eClass());
	}

	/**
	 * Puts a value into a two-level map of maps.
	 * 
	 * @param mapOfMaps
	 *            the map
	 * @param key1
	 *            the first level key
	 * @param key2
	 *            the second level key
	 * @param value
	 *            the value
	 * @return the previous value for these keys, if or {@code null} if there was none
	 * @param <K>
	 *            the top level key type
	 * @param <L>
	 *            the second level key type
	 * @param <V>
	 *            the value type
	 */
	private static <K, L, V> V put(Map<K, Map<L, V>> mapOfMaps, K key1, L key2, V value) {
		Map<L, V> map = mapOfMaps.get(key1);
		if (map == null) {
			map = new HashMap<>();
			mapOfMaps.put(key1, map);
		}
		return map.put(key2, value);
	}

	/**
	 * Queries whether an {@code object} is a stereotype application.
	 * 
	 * @param object
	 *            an object
	 * @return {@code true} if it is a stereotype application; {@code false}, otherwise
	 */
	protected boolean isStereotypeApplication(EObject object) {
		return getBaseElement(object) != null;
	}

	/**
	 * Gets the base element of an {@code object} if that a stereotype application.
	 * 
	 * @param object
	 *            an object
	 * @return its base element if it is a stereotype application; {@code null}, otherwise
	 */
	protected Element getBaseElement(EObject object) {
		Element result = null;

		if (object.eContainer() == null) {
			result = UMLCompareUtil.getBaseElement(object);
		}

		return result;
	}

	//
	// Nested types
	//

	/**
	 * <p>
	 * Explicit diff engine for attributes of added stereotype applications.
	 * </p>
	 * <p>
	 * The default diff engine doesn't attempt to compare attributes of added elements. However, stereotype
	 * applications are special because they are extensions of other elements that are not added (because they
	 * would not be matched if the base elements were added, which in turn would not have been matched).
	 * </p>
	 */
	protected class AttributeDiffEngine extends DefaultDiffEngine {
		/**
		 * Initializes me.
		 */
		public AttributeDiffEngine() {
			super();
		}

		@Override
		protected void checkForDifferences(Match match, Monitor monitor) {
			if (monitor.isCanceled()) {
				throw new ComparisonCanceledException();
			}

			final FeatureFilter featureFilter = createFeatureFilter();
			final Iterator<EAttribute> attributes = featureFilter.getAttributesToCheck(match);
			while (attributes.hasNext()) {
				final EAttribute attribute = attributes.next();
				final boolean considerOrdering = featureFilter.checkForOrderingChanges(attribute);
				computeDifferences(match, attribute, considerOrdering);
			}

			for (Match submatch : match.getSubmatches()) {
				checkForDifferences(submatch, monitor);
			}
		}

		@Override
		protected void computeDifferences(Match match, EAttribute attribute, boolean checkOrdering) {
			// We *do* care about "attribute" changes on added stereotype applications,
			// but not removed
			boolean shortcut = match.getLeft() == null || match.getRight() == null;

			// Do not shortcut when manyvalued FeatureMaps are affected to keep their ordering intact
			if (shortcut && FeatureMapUtil.isFeatureMap(attribute)) {
				if (FeatureMapUtil.isMany(match.getLeft(), attribute)) {
					shortcut = false;
				}
			}

			if (shortcut) {
				return;
			}

			if (attribute.isMany()) {
				if (match.getComparison().isThreeWay()) {
					computeMultiValuedFeatureDifferencesThreeWay(match, attribute, checkOrdering);
				} else {
					computeMultiValuedFeatureDifferencesTwoWay(match, attribute, checkOrdering);
				}
			} else {
				computeSingleValuedAttributeDifferences(match, attribute);
			}
		}
	}

}

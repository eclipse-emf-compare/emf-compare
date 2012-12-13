/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.eobject;

import com.google.common.base.Predicate;
import com.google.common.cache.Cache;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.FeatureFilter;
import org.eclipse.emf.compare.diff.IDiffProcessor;
import org.eclipse.emf.compare.match.DefaultComparisonFactory;
import org.eclipse.emf.compare.match.DefaultEqualityHelperFactory;
import org.eclipse.emf.compare.match.IEqualityHelperFactory;
import org.eclipse.emf.compare.match.eobject.ProximityEObjectMatcher.DistanceFunction;
import org.eclipse.emf.compare.utils.DiffUtil;
import org.eclipse.emf.compare.utils.EqualityHelper;
import org.eclipse.emf.compare.utils.IEqualityHelper;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * This distance function implementation will actually compare the given EObject.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class EditionDistance implements DistanceFunction {
	/**
	 * It's the percentage of similarity we consider as being the limit on a maximum potential distance of 1.
	 */
	private static final double MAX_DIST_RATIO = 0.75d;

	/**
	 * Weight coefficient of a change on a reference.
	 */
	private int referenceChangeCoef = 3;

	/**
	 * Weight coefficient of a change on an attribute.
	 */
	private int attributeChangeCoef = 10;

	/**
	 * Weight coefficient of a change of location (uri).
	 */
	private int locationChangeCoef = 1;

	/**
	 * Weight coefficient of a change of order within a reference.
	 */
	private int orderChangeCoef = 2;

	/**
	 * The instance used to compare location of EObjects.
	 */
	private URIDistance uriDistance = new URIDistance();

	/**
	 * The fake comparison is used to make the diff engine super class happy. We are reusing the same instance
	 * which we are updating because of the cost of adding even a single Match in it (and subsequent growing
	 * of list) which gets very significant considering how much we are calling this during a single
	 * comparison.
	 */
	private Comparison fakeComparison;

	/**
	 * instance providing the weight for each feature.
	 */
	private WeightProvider weights = new ReflectiveWeightProvider();

	/**
	 * Instantiate a new Edition Distance.
	 */
	public EditionDistance() {
		IEqualityHelperFactory fakeEqualityHelperFactory = new DefaultEqualityHelperFactory() {
			@Override
			public IEqualityHelper createEqualityHelper() {
				final Cache<EObject, URI> cache = EqualityHelper.createDefaultCache(getCacheBuilder());
				return new EqualityHelper(cache) {
					@Override
					protected boolean matchingURIs(EObject object1, EObject object2) {
						return uriDistance.proximity(object1, object2) == 0;
					}

				};
			}
		};
		this.fakeComparison = new DefaultComparisonFactory(fakeEqualityHelperFactory).createComparison();
	}

	/**
	 * {@inheritDoc}
	 */
	public int distance(EObject a, EObject b) {
		int maxDist = Math.max(getMaxDistance(a), getMaxDistance(b));
		int measuredDist = new CountingDiffEngine(maxDist, this.fakeComparison).measureDifferences(a, b);
		if (measuredDist >= maxDist) {
			return Integer.MAX_VALUE;
		}
		return measuredDist;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean areIdentic(EObject a, EObject b) {
		return new CountingDiffEngine(0, this.fakeComparison).measureDifferences(a, b) == 0;
	}

	/**
	 * Create a new builder to instantiate and configure an EditionDistance.
	 * 
	 * @return a configuration builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder class to configure an EditionDistance instance.
	 */
	public static class Builder {
		/**
		 * The EditionDistance built by the builder.
		 */
		private EditionDistance toBeBuilt;

		/**
		 * Create the builder.
		 */
		protected Builder() {
			this.toBeBuilt = new EditionDistance();
		}

		/**
		 * Specify the weight of any change of uri between two instances.
		 * 
		 * @param weight
		 *            the new weight.
		 * @return the current builder instance.
		 */
		public Builder uri(int weight) {
			this.toBeBuilt.locationChangeCoef = weight;
			return this;
		}

		/**
		 * Specify the weight of any change of reference order between two instances.
		 * 
		 * @param weight
		 *            the new weight.
		 * @return the current builder instance.
		 */

		public Builder order(int weight) {
			this.toBeBuilt.orderChangeCoef = weight;
			return this;
		}

		/**
		 * Specify the weight of any change of attribute value between two instances.
		 * 
		 * @param weight
		 *            the new weight.
		 * @return the current builder instance.
		 */

		public Builder attribute(int weight) {
			this.toBeBuilt.attributeChangeCoef = weight;
			return this;
		}

		/**
		 * Specify the weight of any change of reference between two instances.
		 * 
		 * @param weight
		 *            the new weight.
		 * @return the current builder instance.
		 */

		public Builder reference(int weight) {
			this.toBeBuilt.referenceChangeCoef = weight;
			return this;
		}

		/**
		 * Configure custom weight provider.
		 * 
		 * @param provider
		 *            the weight provider to use.
		 * @return the current builder instance.
		 */
		public Builder weightProvider(WeightProvider provider) {
			this.toBeBuilt.weights = provider;
			return this;
		}

		/**
		 * return the configured instance.
		 * 
		 * @return the configured instance.
		 */
		public EditionDistance build() {
			return toBeBuilt;
		}
	}

	/**
	 * This class is an implementation of a {@link IDiffProcessor} which counts the number of differences to
	 * given an overall distance between two objects.
	 */
	class CountingDiffProcessor implements IDiffProcessor {
		/**
		 * Keeps track of features which have already been detected as changed so that we can apply different
		 * weight in those cases.
		 */
		private Set<EStructuralFeature> alreadyChanged = Sets.newLinkedHashSet();

		/**
		 * The current distance.
		 */
		private int distance;

		/**
		 * {@inheritDoc}
		 */
		public void referenceChange(Match match, EReference reference, EObject value, DifferenceKind kind,
				DifferenceSource source) {
			if (!alreadyChanged.contains(reference)) {
				switch (kind) {
					case MOVE:
						distance += weights.getWeight(reference) * orderChangeCoef;
						break;
					case ADD:
					case DELETE:
					case CHANGE:
						distance += weights.getWeight(reference) * referenceChangeCoef;
						break;
					default:
						break;
				}
				alreadyChanged.add(reference);
			} else {
				distance += 1;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void attributeChange(Match match, EAttribute attribute, Object value, DifferenceKind kind,
				DifferenceSource source) {
			if (!alreadyChanged.contains(attribute)) {
				Object aValue = ReferenceUtil.safeEGet(match.getLeft(), attribute);
				Object bValue = ReferenceUtil.safeEGet(match.getRight(), attribute);
				switch (kind) {
					case MOVE:
						distance += weights.getWeight(attribute) * orderChangeCoef;
						break;
					case ADD:
					case DELETE:
					case CHANGE:
						if (aValue instanceof String && bValue instanceof String) {
							distance += weights.getWeight(attribute)
									* (1 - DiffUtil.diceCoefficient((String)aValue, (String)bValue))
									* attributeChangeCoef;
						} else {
							distance += weights.getWeight(attribute) * attributeChangeCoef;
						}
						break;
					default:
						break;
				}
				alreadyChanged.add(attribute);
			} else {
				distance += 1;
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diff.IDiffProcessor#resourceAttachmentChange(org.eclipse.emf.compare.Match,
		 *      java.lang.String, org.eclipse.emf.compare.DifferenceKind,
		 *      org.eclipse.emf.compare.DifferenceSource)
		 */
		public void resourceAttachmentChange(Match match, String uri, DifferenceKind kind,
				DifferenceSource source) {
			// Not important for the distance computation
		}

		/**
		 * return the computed distance.
		 * 
		 * @return the computed distance.
		 */
		public int getComputedDistance() {
			return distance;
		}

		/**
		 * Clear the diff processor state so that it's ready for the next computation.
		 */
		public void reset() {
			this.alreadyChanged.clear();
		}

	}

	/**
	 * An implementation of a diff engine which count and measure the detected changes.
	 */
	class CountingDiffEngine extends DefaultDiffEngine {
		/**
		 * The maximum distance until which we just have to stop.
		 */
		private int maxDistance;

		/** The comparison for which this engine will detect differences. */
		private final Comparison comparison;

		/**
		 * Create the diff engine.
		 * 
		 * @param maxDistance
		 *            the maximum distance we might reach.
		 * @param fakeComparison
		 *            the comparison instance to use while measuring the differences between the two objects.
		 */
		public CountingDiffEngine(int maxDistance, Comparison fakeComparison) {
			super(new CountingDiffProcessor());
			this.maxDistance = maxDistance;
			// will always return the same instance.

			this.comparison = fakeComparison;

		}

		@Override
		protected void computeDifferences(Match match, EAttribute attribute, boolean checkOrdering) {
			if (getCounter().getComputedDistance() <= maxDistance) {
				super.computeDifferences(match, attribute, checkOrdering);
			}
		}

		@Override
		protected void computeDifferences(Match match, EReference reference, boolean checkOrdering) {
			if (getCounter().getComputedDistance() <= maxDistance) {
				super.computeDifferences(match, reference, checkOrdering);
			}
		}

		/**
		 * Measure the difference between two objects and return a distance value.
		 * 
		 * @param a
		 *            first object.
		 * @param b
		 *            second object.
		 * @return the distance between them computed using the number of changes required to change a to b.
		 */
		public int measureDifferences(EObject a, EObject b) {
			Match fakeMatch = createOrUpdateFakeMatch(a, b);
			getCounter().reset();
			int changes = 0;
			int dist = uriDistance.proximity(a, b);
			changes += dist * locationChangeCoef;
			if (changes <= maxDistance) {
				checkForDifferences(fakeMatch, new BasicMonitor());
				changes += getCounter().getComputedDistance();
			}
			// System.err.println(changes + ":max=>" + maxDistance + ":" + a + ":" + b);
			return changes;

		}

		/**
		 * Create a mock {@link Match} between the two given EObjects so that we can use the exposed
		 * {@link #checkForDifferences(Match, org.eclipse.emf.common.util.Monitor)} method to check for
		 * differences.
		 * 
		 * @param a
		 *            First of the two EObjects for which we want to force a comparison.
		 * @param b
		 *            Second of the two EObjects for which we want to force a comparison.
		 * @return The created Match.
		 */
		private Match createOrUpdateFakeMatch(EObject a, EObject b) {
			if (!comparison.getMatches().iterator().hasNext()) {
				Match fakeMatch = CompareFactory.eINSTANCE.createMatch();
				((InternalEList<Match>)comparison.getMatches()).addUnique(fakeMatch);
			}
			Match fakeMatch = comparison.getMatches().get(0);
			fakeMatch.setLeft(a);
			fakeMatch.setRight(b);
			return fakeMatch;
		}

		protected CountingDiffProcessor getCounter() {
			return (CountingDiffProcessor)getDiffProcessor();
		}

		@Override
		protected FeatureFilter createFeatureFilter() {
			return new FeatureFilter() {

				@Override
				public Iterator<EReference> getReferencesToCheck(Match match) {
					return Iterators.filter(super.getReferencesToCheck(match), new Predicate<EReference>() {

						public boolean apply(EReference input) {
							return weights.getWeight(input) != 0;

						}
					});
				}

				@Override
				public Iterator<EAttribute> getAttributesToCheck(Match match) {
					return Iterators.filter(super.getAttributesToCheck(match), new Predicate<EAttribute>() {

						public boolean apply(EAttribute input) {
							return weights.getWeight(input) != 0;
						}
					});
				}

			};
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public int getMaxDistance(EObject eObj) {

		Predicate<EStructuralFeature> featureFilter = new Predicate<EStructuralFeature>() {

			public boolean apply(EStructuralFeature feat) {
				return weights.getWeight(feat) != 0;
			}
		};
		// When can you safely says these are not the same EObjects *at all* ?
		// lets consider every feature which is set, and add this in the max distance.
		// and then tweak the max value adding half a location change
		// thats very empirical... and might be wrong in the end, but it gives pretty good results with
		// Ecore so I'll try to gather as much as test data I can and add the corresponding test to be able to
		// assess the quality of further changes.
		int max = 0;
		for (EReference feat : Iterables.filter(eObj.eClass().getEAllReferences(), featureFilter)) {
			if (eObj.eIsSet(feat)) {
				max += weights.getWeight(feat) * referenceChangeCoef;
			}
		}
		for (EAttribute feat : Iterables.filter(eObj.eClass().getEAllAttributes(), featureFilter)) {
			if (eObj.eIsSet(feat)) {
				max += weights.getWeight(feat) * attributeChangeCoef;
			}
		}
		// 10 is the maximum distance for an URIDistance.
		max = max + locationChangeCoef * uriDistance.getUpperBoundDistance();
		return Double.valueOf(max * MAX_DIST_RATIO).intValue();
	}

}

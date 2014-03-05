/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo and others.
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
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.FeatureFilter;
import org.eclipse.emf.compare.diff.IDiffProcessor;
import org.eclipse.emf.compare.internal.spec.ComparisonSpec;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.match.DefaultEqualityHelperFactory;
import org.eclipse.emf.compare.match.IEqualityHelperFactory;
import org.eclipse.emf.compare.match.eobject.ProximityEObjectMatcher.DistanceFunction;
import org.eclipse.emf.compare.match.eobject.internal.WeightProviderDescriptorImpl;
import org.eclipse.emf.compare.utils.EqualityHelper;
import org.eclipse.emf.compare.utils.IEqualityHelper;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * This distance function implementation will actually compare the given EObject.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class EditionDistance implements DistanceFunction {

	/**
	 * Weight coefficient of a change of location (uri).
	 */
	private int locationChangeCoef = 1;

	/**
	 * Weight coefficient of a change of order within a reference.
	 */
	private int orderChangeCoef = 1;

	/**
	 * The instance used to compare location of EObjects.
	 */
	private URIDistance uriDistance = new URIDistance();

	/**
	 * a thresholds ratio discrete function per the number of features.
	 */
	// CHECKSTYLE:OFF we know these are magic numbers, so be it, they happens to have the same value but there
	// is no semantic.
	private double[] thresholds = {0d, 0.6d, 0.6d, 0.55d, 0.465d };

	// CHECKSTYLE:ON
	/**
	 * The fake comparison is used to make the diff engine super class happy. We are reusing the same instance
	 * which we are updating because of the cost of adding even a single Match in it (and subsequent growing
	 * of list) which gets very significant considering how much we are calling this during a single
	 * comparison.
	 */
	private Comparison fakeComparison;

	/** The registry of Weight Providers to use in this Edition Distance. */
	private WeightProvider.Descriptor.Registry weightProviderRegistry;

	/**
	 * Instantiate a new Edition Distance that will use
	 * {@link WeightProviderDescriptorRegistryImpl#createStandaloneInstance()}.
	 */
	public EditionDistance() {
		this(WeightProviderDescriptorRegistryImpl.createStandaloneInstance());
	}

	/**
	 * Instantiate a new Edition Distance.
	 * 
	 * @param registry
	 *            The registry of weight providers to use in this Edition Distance.
	 */
	public EditionDistance(WeightProvider.Descriptor.Registry registry) {
		weightProviderRegistry = registry;

		IEqualityHelperFactory fakeEqualityHelperFactory = new DefaultEqualityHelperFactory() {
			@Override
			public IEqualityHelper createEqualityHelper() {
				final LoadingCache<EObject, URI> cache = EqualityHelper.createDefaultCache(getCacheBuilder());
				return new EqualityHelper(cache) {
					@Override
					protected boolean matchingURIs(EObject object1, EObject object2) {
						/*
						 * we might trying to compare children of the objects under scrutinity right now, it
						 * might happen if a containment reference is seen as "relevant" for the matching
						 * process. In those cases, we don't want to compare the whole uris and instead want
						 * to compare just the current fragment. This has very important performance
						 * implications.
						 */
						if (object1.eContainer() != null && object2.eContainer() != null
								&& fakeComparison.getMatch(object1.eContainer()) != null) {
							return uriDistance.retrieveFragment(object1).equals(
									uriDistance.retrieveFragment(object2));
						}
						return uriDistance.proximity(object1, object2) == 0;
					}

				};
			}
		};

		this.fakeComparison = new ComparisonSpec() {

			/*
			 * We did override this method to avoid the extra cost of maintaining a cross referencer for such
			 * a fake comparison.
			 */
			@Override
			public Match getMatch(EObject element) {
				for (Match m : getMatches()) {
					if (m.getLeft() == element || m.getRight() == element || m.getOrigin() == element) {
						return m;
					}
				}
				return null;
			}

		};
		Match fakeMatch = CompareFactory.eINSTANCE.createMatch();
		((InternalEList<Match>)fakeComparison.getMatches()).addUnique(fakeMatch);

		IEqualityHelper equalityHelper = fakeEqualityHelperFactory.createEqualityHelper();

		fakeComparison.eAdapters().add(equalityHelper);
		equalityHelper.setTarget(fakeComparison);
	}

	/**
	 * {@inheritDoc}
	 */
	public double distance(Comparison inProgress, EObject a, EObject b) {
		this.uriDistance.setComparison(inProgress);
		double maxDist = Math.max(getThresholdAmount(a), getThresholdAmount(b));
		double measuredDist = new CountingDiffEngine(maxDist, this.fakeComparison).measureDifferences(
				inProgress, a, b);
		if (measuredDist > maxDist) {
			return Double.MAX_VALUE;
		}
		return measuredDist;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean areIdentic(Comparison inProgress, EObject a, EObject b) {
		return new CountingDiffEngine(0, this.fakeComparison).measureDifferences(inProgress, a, b) == 0;
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
		 * Configure custom weight provider.
		 * 
		 * @param provider
		 *            the weight provider to use.
		 * @return the current builder instance.
		 */
		public Builder weightProvider(WeightProvider provider) {
			final WeightProvider.Descriptor descriptor = new WeightProviderDescriptorImpl(provider, 101,
					Pattern.compile(".*")); //$NON-NLS-1$
			this.toBeBuilt.weightProviderRegistry.put(descriptor.getNsURI().toString(), descriptor);
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
		private double distance;

		/**
		 * {@inheritDoc}
		 */
		public void referenceChange(Match match, EReference reference, EObject value, DifferenceKind kind,
				DifferenceSource source) {
			if (!alreadyChanged.contains(reference)) {
				switch (kind) {
					case MOVE:
						distance += weightProviderRegistry.getHighestRankingWeightProvider(
								reference.eClass().getEPackage()).getWeight(reference)
								* orderChangeCoef;
						break;
					case ADD:
					case DELETE:
					case CHANGE:
						distance += weightProviderRegistry.getHighestRankingWeightProvider(
								reference.eClass().getEPackage()).getWeight(reference);
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
						distance += weightProviderRegistry.getHighestRankingWeightProvider(
								attribute.eClass().getEPackage()).getWeight(attribute)
								* orderChangeCoef;
						break;
					case ADD:
					case DELETE:
					case CHANGE:
						if (aValue instanceof String && bValue instanceof String) {
							distance += weightProviderRegistry.getHighestRankingWeightProvider(
									attribute.eClass().getEPackage()).getWeight(attribute)
									* (1 - DiffUtil.diceCoefficient((String)aValue, (String)bValue));
						} else {
							distance += weightProviderRegistry.getHighestRankingWeightProvider(
									attribute.eClass().getEPackage()).getWeight(attribute);
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
		public double getComputedDistance() {
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
		private double maxDistance;

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
		public CountingDiffEngine(double maxDistance, Comparison fakeComparison) {
			super(new CountingDiffProcessor());
			this.maxDistance = maxDistance;
			// will always return the same instance.

			this.comparison = fakeComparison;

		}

		@Override
		protected void checkResourceAttachment(Match match, Monitor monitor) {
			/*
			 * we really don't want to check that...
			 */
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
		 * @param comparisonInProgress
		 *            the comparison which is currently being matched.
		 * @param a
		 *            first object.
		 * @param b
		 *            second object.
		 * @return the distance between them computed using the number of changes required to change a to b.
		 */
		public double measureDifferences(Comparison comparisonInProgress, EObject a, EObject b) {
			Match fakeMatch = createOrUpdateFakeMatch(a, b);
			getCounter().reset();
			double changes = 0;
			if (!haveSameContainer(comparisonInProgress, a, b)) {
				changes += locationChangeCoef
						* weightProviderRegistry.getHighestRankingWeightProvider(a.eClass().getEPackage())
								.getParentWeight(a);
			} else {
				int aIndex = getContainmentIndex(a);
				int bIndex = getContainmentIndex(b);
				if (aIndex != bIndex) {
					/*
					 * we just want to pick the same positioned object if two exactly similar objects are
					 * candidates in the same container.
					 */
					changes += 5;
				}

			}
			if (a.eContainingFeature() != b.eContainingFeature()) {
				changes += Math.max(weightProviderRegistry.getHighestRankingWeightProvider(
						a.eClass().getEPackage()).getContainingFeatureWeight(a), weightProviderRegistry
						.getHighestRankingWeightProvider(b.eClass().getEPackage())
						.getContainingFeatureWeight(b));
			}
			if (changes <= maxDistance) {
				checkForDifferences(fakeMatch, new BasicMonitor());
				changes += getCounter().getComputedDistance();
			}
			return changes;

		}

		/**
		 * return the position in which an Object is contained in its parent list.
		 * 
		 * @param a
		 *            any EObject
		 * @return the position in which an Object is contained in its parent list, 0 if there is no container
		 *         or if the reference is single valued.
		 */
		private int getContainmentIndex(EObject a) {
			EStructuralFeature feat = a.eContainingFeature();
			EObject container = a.eContainer();
			int position = 0;
			if (container != null) {
				if (feat instanceof EAttribute) {
					position = indexFromFeatureMap(a, feat, container);
				} else if (feat != null) {
					if (feat.isMany()) {
						EList<?> eList = (EList<?>)container.eGet(feat, false);
						position = eList.indexOf(a);
					}
				}
			}
			return position;
		}

		/**
		 * the position of the {@link EObject} a in its container featureMap.
		 * 
		 * @param a
		 *            the {@link EObject}.
		 * @param feat
		 *            the containing feature.
		 * @param container
		 *            the containing EObject.
		 * @return the position of the {@link EObject} a in its container featureMap.
		 */
		private int indexFromFeatureMap(EObject a, EStructuralFeature feat, EObject container) {
			FeatureMap featureMap = (FeatureMap)container.eGet(feat, false);
			for (int i = 0, size = featureMap.size(); i < size; ++i) {
				if (featureMap.getValue(i) == a) {
					EStructuralFeature entryFeature = featureMap.getEStructuralFeature(i);
					if (entryFeature instanceof EReference && ((EReference)entryFeature).isContainment()) {
						return i;
					}
				}
			}
			return 0;
		}

		/**
		 * Check whether two {@link EObject} have the same containers or not.
		 * 
		 * @param inProgress
		 *            the comparison currently being matched.
		 * @param a
		 *            any {@link EObject}
		 * @param b
		 *            any other {@link EObject}
		 * @return true if they have the same container. If the containers have been matched their match will
		 *         be used, on the contrary the URI will be indirectly use through the EqualityHelper.
		 */
		private boolean haveSameContainer(Comparison inProgress, EObject a, EObject b) {
			EObject aContainer = a.eContainer();
			EObject bContainer = b.eContainer();
			if ((aContainer == null && bContainer != null) || (aContainer != null && bContainer == null)) {
				return false;
			}
			/*
			 * we consider two null containers as being the "same".
			 */
			boolean matching = aContainer == null && bContainer == null;
			if (!matching) {
				Match mA = inProgress.getMatch(aContainer);
				Match mB = inProgress.getMatch(bContainer);
				if (mA == null && mB == null) {
					/*
					 * The Objects have to be out of scope then.
					 */
					matching = fakeComparison.getEqualityHelper().matchingValues(aContainer, bContainer);
				} else {
					matching = isReferencedByTheMatch(bContainer, mA)
							|| isReferencedByTheMatch(aContainer, mB);
				}
			}
			return matching;
		}

		/**
		 * Return true if the given {@link EObject} is referenced by the left/right or origin match reference.
		 * 
		 * @param eObj
		 *            any {@link EObject}.
		 * @param match
		 *            any Match.
		 * @return true if the given {@link EObject} is referenced by the left/right or origin match
		 *         reference.
		 */
		private boolean isReferencedByTheMatch(EObject eObj, Match match) {
			return match != null
					&& (match.getRight() == eObj || match.getLeft() == eObj || match.getOrigin() == eObj);
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
							return weightProviderRegistry.getHighestRankingWeightProvider(
									input.eClass().getEPackage()).getWeight(input) != 0;

						}
					});
				}

				@Override
				public Iterator<EAttribute> getAttributesToCheck(Match match) {
					return Iterators.filter(super.getAttributesToCheck(match), new Predicate<EAttribute>() {
						public boolean apply(EAttribute input) {
							return weightProviderRegistry.getHighestRankingWeightProvider(
									input.eClass().getEPackage()).getWeight(input) != 0;
						}
					});
				}

			};
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public double getThresholdAmount(EObject eObj) {
		// When can you safely says these are not the same EObjects *at all* ?
		// lets consider every feature which is set, and add this in the max distance.
		// and then tweak the max value adding half a location change
		// thats very empirical... and might be wrong in the end, but it gives pretty good results with
		// Ecore so I'll try to gather as much as test data I can and add the corresponding test to be able to
		// assess the quality of further changes.
		int max = 0;
		int nbFeatures = 0;

		for (EStructuralFeature feat : eObj.eClass().getEAllStructuralFeatures()) {
			int featureWeight = weightProviderRegistry.getHighestRankingWeightProvider(
					feat.eClass().getEPackage()).getWeight(feat);
			if (featureWeight != 0 && eObj.eIsSet(feat)) {
				max += featureWeight;
				nbFeatures++;
			}
		}

		// max = max + (locationChangeCoef *
		// weightProviderRegistry.getHighestRankingWeightProvider(eObj.eClass().getEPackage()).getParentWeight(eObj));
		max = max
				+ weightProviderRegistry.getHighestRankingWeightProvider(eObj.eClass().getEPackage())
						.getContainingFeatureWeight(eObj);

		return max * getThresholdRatio(nbFeatures);
	}

	/**
	 * return a ratio to apply on the amount of maximum un-similarity amount depending on the number of
	 * features which are considered.
	 * 
	 * @param nbFeatures
	 *            the nb of features which should be considerd to compute the amount.
	 * @return a ratio to appli on the amount of maximum un-similarity amount depending on the number of
	 *         features which are considered.
	 */
	protected double getThresholdRatio(int nbFeatures) {
		if (nbFeatures >= thresholds.length) {
			// CHECKSTYLE:OFF
			return 0.465d;
			// CHECKSTYLE:ON
		}
		return thresholds[nbFeatures];
	}

}

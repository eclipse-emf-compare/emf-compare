/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Victor Roldan Betancort - bug 352727, bug 360939
 *******************************************************************************/
package org.eclipse.emf.compare.match.engine.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.match.engine.AbstractSimilarityChecker;
import org.eclipse.emf.compare.match.internal.statistic.NameSimilarity;
import org.eclipse.emf.compare.match.internal.statistic.StructureSimilarity;
import org.eclipse.emf.compare.match.statistic.MetamodelFilter;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * A similarity checker using heuristics.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class StatisticBasedSimilarityChecker extends AbstractSimilarityChecker {
	/** Used while computing similarity, this defines the general threshold. */
	private static final double GENERAL_THRESHOLD = 0.96d;

	/**
	 * Minimal number of attributes an element must have for content comparison.
	 */
	private static final int MIN_ATTRIBUTES_COUNT = 5;

	/**
	 * This map is used to cache the name similarity. <code>Pair(Element1, Element2) => nameSimilarity</code>.
	 * 
	 * @since 1.3
	 */
	protected Map<String, Double> nameSimilarityCache;

	/**
	 * This map is used to cache the value similarity.
	 * <code>Pair(Element1, Element2) => valueSimilarity</code>.
	 * 
	 * @since 1.3
	 */
	private Map<String, Double> valueSimilarityCache;

	/**
	 * This map is used to cache the relation similarity.
	 * <code>Pair(Element1, Element2) => relationSimilarity</code>.
	 * 
	 * @since 1.3
	 */
	private Map<String, Double> relationSimilarityCache;

	/**
	 * This map is used to cache the type similarity. <code>Pair(Element1, Element2) => typeSimilarity</code>.
	 * 
	 * @since 1.3
	 */
	private Map<String, Double> typeSimilarityCache;

	/**
	 * This map will allow us to cache the number of non-null features a given instance of EObject has.
	 */
	private final Map<EObject, Integer> nonNullFeatureCounts = new HashMap<EObject, Integer>(20);

	/** We'll use this map to cache the uri fragments computed for each objects. */
	private final Map<EObject, String> uriFragmentCache = new WeakHashMap<EObject, String>(20);

	/**
	 * This field is needed to keep the same behavior for clients having subclassed the generic match engine.
	 */
	@Deprecated
	private GenericMatchEngineToCheckerBridge matchToCheckerBridge;

	/**
	 * Create a new checker.
	 * 
	 * @param filter
	 *            a metamodel filter the checker can use to know whether a feature alwaas has the same value
	 *            or not in the models.
	 * @param bridge
	 *            utility class to keep API compatibility.
	 */
	public StatisticBasedSimilarityChecker(MetamodelFilter filter, GenericMatchEngineToCheckerBridge bridge) {
		super(filter);
		this.matchToCheckerBridge = bridge;
		initMetricsCaches();
	}

	/**
	 * Initializes the different metrics caches.
	 * 
	 * @since 1.3
	 */
	protected void initMetricsCaches() {
		nameSimilarityCache = new HashMap<String, Double>();
		valueSimilarityCache = new HashMap<String, Double>();
		relationSimilarityCache = new HashMap<String, Double>();
		typeSimilarityCache = new HashMap<String, Double>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSimilar(EObject obj1, EObject obj2) throws FactoryException {
		boolean similar = false;
		final double almostEquals = 0.999999d;

		// Defines threshold constants to assume objects' similarity
		final double nameOnlyMetricThreshold = 0.7d;
		final double fewerAttributesNameThreshold = 0.8d;
		final double relationsThreshold = 0.9d;
		final double nameThreshold = 0.2d;
		final double contentThreshold = 0.9d;
		final double triWayThreshold = 0.9d;
		final double generalThreshold = GENERAL_THRESHOLD;

		// Computes some of the required metrics
		final double nameSimilarity = nameSimilarity(obj1, obj2);
		final boolean hasSameUri = hasSameUri(obj1, obj2);
		final int obj1NonNullFeatures = nonNullFeaturesCount(obj1);
		final int obj2NonNullFeatures = nonNullFeaturesCount(obj2);

		if (obj1 instanceof EGenericType || obj2 instanceof EGenericType) {
			similar = isSimilar(obj1.eContainer(), obj2.eContainer());
		} else if (nameSimilarity > almostEquals && hasSameUri) {
			similar = true;
			// softer tests if we don't have enough attributes to compare the
			// objects
		} else if (obj1NonNullFeatures == 1 && obj2NonNullFeatures == 1) {
			similar = nameSimilarity > nameOnlyMetricThreshold;
		} else if (nameSimilarity > fewerAttributesNameThreshold
				&& obj1NonNullFeatures <= MIN_ATTRIBUTES_COUNT && obj2NonNullFeatures <= MIN_ATTRIBUTES_COUNT
				&& typeSimilarity(obj1, obj2) > generalThreshold) {
			similar = true;
		} else {
			final double contentSimilarity = contentSimilarity(obj1, obj2);
			final double relationsSimilarity = relationsSimilarity(obj1, obj2);

			if (relationsSimilarity > almostEquals && hasSameUri && nameSimilarity > nameThreshold) {
				similar = true;
			} else if (contentSimilarity > almostEquals && relationsSimilarity > almostEquals) {
				similar = true;
			} else if (contentSimilarity > generalThreshold && relationsSimilarity > relationsThreshold
					&& nameSimilarity > nameThreshold) {
				similar = true;
			} else if (relationsSimilarity > generalThreshold && contentSimilarity > contentThreshold) {
				similar = true;
			} else if (contentSimilarity > triWayThreshold && nameSimilarity > triWayThreshold
					&& relationsSimilarity > triWayThreshold) {
				similar = true;
			} else if (contentSimilarity > generalThreshold && nameSimilarity > generalThreshold
					&& typeSimilarity(obj1, obj2) > generalThreshold) {
				similar = true;
			}
		}
		return similar;
	}

	/**
	 * This will compute the similarity between two {@link EObject}s' names.
	 * 
	 * @param obj1
	 *            First of the two {@link EObject}s.
	 * @param obj2
	 *            Second of the two {@link EObject}s.
	 * @return <code>double</code> representing the similarity between the two {@link EObject}s' names. 0 &lt;
	 *         value &lt; 1.
	 * @see NameSimilarity#nameSimilarityMetric(String, String)
	 */
	protected double nameSimilarity(EObject obj1, EObject obj2) {
		double similarity = 0d;
		final Double value = getSimilarityFromCache(obj1, obj2, SimilarityKind.NAME);
		if (value != null) {
			similarity = value;
		} else {
			similarity = matchToCheckerBridge.nameSimilarity(obj1, obj2);
			setSimilarityInCache(obj1, obj2, SimilarityKind.NAME, similarity);
		}
		// fails silently, will return a similarity of 0d
		return similarity;
	}

	/**
	 * Returns an absolute comparison metric between the two given {@link EObject}s.
	 * 
	 * @param obj1
	 *            The first {@link EObject} to compare.
	 * @param obj2
	 *            Second of the {@link EObject}s to compare.
	 * @return An absolute comparison metric. 0 &lt; value &lt; 1.
	 * @throws FactoryException
	 *             Thrown if we cannot compute the content similarity.
	 */
	@Override
	public double absoluteMetric(EObject obj1, EObject obj2) throws FactoryException {
		final double nameSimilarity = nameSimilarity(obj1, obj2);
		final double relationsSimilarity = relationsSimilarity(obj1, obj2);
		double sameUri = 0d;
		if (hasSameUri(obj1, obj2)) {
			sameUri = 1d;
		}
		final double positionSimilarity = relationsSimilarity / 2d + sameUri / 2d;

		final double contentWeight = 0.5d;

		if (nonNullFeaturesCount(obj1) > MIN_ATTRIBUTES_COUNT
				&& nonNullFeaturesCount(obj2) > MIN_ATTRIBUTES_COUNT) {
			final double nameWeight = 0.4d;
			final double positionWeight = 0.4d;
			final double contentSimilarity = contentSimilarity(obj1, obj2);
			// Computing type similarity really is time expensive
			// double typeSimilarity = typeSimilarity(obj1, obj2);
			return (contentSimilarity * contentWeight + nameSimilarity * nameWeight + positionSimilarity
					* positionWeight)
					/ (contentWeight + nameWeight + positionWeight);
		}
		// we didn't have enough features to compute an accurate metric
		final double nameWeight = 0.8d;
		final double positionWeight = 0.2d;

		return (nameSimilarity * nameWeight + positionSimilarity * positionWeight)
				/ (nameWeight + positionWeight);
	}

	/**
	 * This will compute the similarity between two {@link EObject}s' types.
	 * 
	 * @param obj1
	 *            First of the two {@link EObject}s.
	 * @param obj2
	 *            Second of the two {@link EObject}s.
	 * @return <code>double</code> representing the similarity between the two {@link EObject}s' types. 0 &lt;
	 *         value &lt; 1.
	 * @throws FactoryException
	 *             Thrown if we cannot compute the type similarity metrics.
	 * @see StructureSimilarity#typeSimilarityMetric(EObject, EObject)
	 */
	private double typeSimilarity(EObject obj1, EObject obj2) throws FactoryException {
		double similarity = 0d;
		final Double value = getSimilarityFromCache(obj1, obj2, SimilarityKind.TYPE);
		if (value != null) {
			similarity = value;
		} else {
			similarity = StructureSimilarity.typeSimilarityMetric(obj1, obj2);
			setSimilarityInCache(obj1, obj2, SimilarityKind.TYPE, similarity);
		}
		return similarity;
	}

	/**
	 * This will compute the similarity between two {@link EObject}s' contents.
	 * 
	 * @param obj1
	 *            First of the two {@link EObject}s.
	 * @param obj2
	 *            Second of the two {@link EObject}s.
	 * @return <code>double</code> representing the similarity between the two {@link EObject}s' contents. 0
	 *         &lt; value &lt; 1.
	 * @throws FactoryException
	 *             Thrown if we cannot compute the {@link EObject}s' contents similarity metrics.
	 */
	protected double contentSimilarity(EObject obj1, EObject obj2) throws FactoryException {
		double similarity = 0d;
		Double value = getSimilarityFromCache(obj1, obj2, SimilarityKind.VALUE);
		// This might be the counter check, invert the two
		if (value == null) {
			value = getSimilarityFromCache(obj2, obj1, SimilarityKind.VALUE);
		}
		if (value != null) {
			similarity = value;
		} else if (filter.getFilteredFeatures(obj1).size() < MIN_ATTRIBUTES_COUNT
				|| filter.getFilteredFeatures(obj2).size() < MIN_ATTRIBUTES_COUNT) {
			similarity = matchToCheckerBridge.contentSimilarity(obj1, obj2);
			setSimilarityInCache(obj1, obj2, SimilarityKind.VALUE, similarity);
		} else {
			similarity = NameSimilarity.nameSimilarityMetric(NameSimilarity.contentValue(obj1, filter),
					NameSimilarity.contentValue(obj2, filter));
			setSimilarityInCache(obj1, obj2, SimilarityKind.VALUE, similarity);
		}
		return similarity;
	}

	/**
	 * Counts all the {@link EStructuralFeature features} of the given {@link EObject} that are
	 * <code>null</code> or initialized to the empty {@link String} &quot;&quot;.
	 * 
	 * @param eobj
	 *            {@link EObject} we need to count the empty features of.
	 * @return The number of features not initialized to <code>null</code> or the empty String.
	 */
	private int nonNullFeaturesCount(EObject eobj) {
		Integer nonNullFeatures = nonNullFeatureCounts.get(eobj);
		if (nonNullFeatures == null) {
			final int count = countNonNullFeatures(eobj);
			nonNullFeatures = Integer.valueOf(count);
			nonNullFeatureCounts.put(eobj, nonNullFeatures);
		}
		return nonNullFeatures.intValue();
	}

	/**
	 * Count non null features in an EObject.
	 * 
	 * @param eobj
	 *            the EObject.
	 * @return the number of filtered non null features.
	 */
	private int countNonNullFeatures(EObject eobj) {
		int count = 0;
		final Iterator<EStructuralFeature> features = filter.getFilteredFeatures(eobj).iterator();
		while (features.hasNext()) {
			final EStructuralFeature feature = features.next();
			if (!feature.isDerived()) {
				final Object value = eobj.eGet(feature);
				if (feature.isMany()) {
					if (((Collection<?>)value).size() > 0) {
						count++;
					}
				} else {
					if (value != null && !"".equals(value.toString())) { //$NON-NLS-1$
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * Computes an unique key between to {@link EObject}s to store their similarity in cache.
	 * <p>
	 * <code>similarityKind</code> must be one of
	 * <ul>
	 * <li>{@link #NAME_SIMILARITY}</li>
	 * <li>{@link #TYPE_SIMILARITY}</li>
	 * <li>{@link #VALUE_SIMILARITY}</li>
	 * <li>{@link #RELATION_SIMILARITY}</li>
	 * </ul>
	 * </p>
	 * 
	 * @param obj1
	 *            First of the two {@link EObject}s.
	 * @param obj2
	 *            Second of the two {@link EObject}s.
	 * @param similarityKind
	 *            Kind of similarity this key will represent in cache.
	 * @return Unique key for the similarity cache.
	 * @since 1.3
	 */
	protected static String pairHashCode(EObject obj1, EObject obj2) {
		final StringBuilder hash = new StringBuilder();

		final int obj1Hash = obj1.hashCode();
		final int obj2Hash = obj2.hashCode();
		if (obj1Hash < obj2Hash) {
			hash.append(String.valueOf(obj1Hash)).append(String.valueOf(obj2Hash));
		} else {
			hash.append(String.valueOf(obj2Hash)).append(String.valueOf(obj1Hash));
		}

		return hash.toString();
	}

	/**
	 * Returns the given similarity between the two given {@link EObject}s as it is stored in cache.<br/>
	 * <p>
	 * <code>similarityKind</code> must be one of
	 * <ul>
	 * <li>{@link #NAME_SIMILARITY}</li>
	 * <li>{@link #TYPE_SIMILARITY}</li>
	 * <li>{@link #VALUE_SIMILARITY}</li>
	 * <li>{@link #RELATION_SIMILARITY}</li>
	 * </ul>
	 * </p>
	 * 
	 * @param obj1
	 *            First of the two {@link EObject}s we seek the similarity for.
	 * @param obj2
	 *            Second of the two {@link EObject}s we seek the similarity for.
	 * @param similarityKind
	 *            Kind of similarity to get.
	 * @return The similarity as described by <code>similarityKind</code> as it is stored in cache for the two
	 *         given {@link EObject}s.
	 * @since 1.3
	 */
	protected Double getSimilarityFromCache(EObject obj1, EObject obj2, SimilarityKind similarityKind) {
		final Map<String, Double> cache = getCache(similarityKind);

		return cache.get(pairHashCode(obj1, obj2));
	}

	/**
	 * Returns the cache for the given similarity kind.
	 * 
	 * @param kind
	 *            The kind of similarity for which we need the cache.
	 * @return The cache for the given similarity kind.
	 * @since 1.3
	 */
	protected Map<String, Double> getCache(SimilarityKind kind) {
		final Map<String, Double> cache;
		if (kind == SimilarityKind.NAME) {
			cache = nameSimilarityCache;
		} else if (kind == SimilarityKind.RELATION) {
			cache = relationSimilarityCache;
		} else if (kind == SimilarityKind.TYPE) {
			cache = typeSimilarityCache;
		} else {
			cache = valueSimilarityCache;
		}
		return cache;
	}

	/**
	 * Checks whether the two given {@link EObject} have the same URI.
	 * 
	 * @param obj1
	 *            First of the two {@link EObject} we're comparing.
	 * @param obj2
	 *            Second {@link EObject} we're comparing.
	 * @return <code>True</code> if the {@link EObject}s have the same URI, <code>False</code> otherwise.
	 */
	protected boolean hasSameUri(EObject obj1, EObject obj2) {
		if (obj1.eResource() != null && obj2.eResource() != null) {
			String obj1URIFragment = uriFragmentCache.get(obj1);
			if (obj1URIFragment == null) {
				obj1URIFragment = obj1.eResource().getURIFragment(obj1);
				uriFragmentCache.put(obj1, obj1URIFragment);
			}
			String obj2URIFragment = uriFragmentCache.get(obj2);
			if (obj2URIFragment == null) {
				obj2URIFragment = obj2.eResource().getURIFragment(obj2);
				uriFragmentCache.put(obj2, obj2URIFragment);
			}
			return obj1URIFragment.equals(obj2URIFragment);
		}
		return false;
	}

	/**
	 * This will compute the similarity between two {@link EObject}s' relations.
	 * 
	 * @param obj1
	 *            First of the two {@link EObject}s.
	 * @param obj2
	 *            Second of the two {@link EObject}s.
	 * @return <code>double</code> representing the similarity between the two {@link EObject}s' relations. 0
	 *         &lt; value &lt; 1.
	 * @throws FactoryException
	 *             Thrown if we cannot compute the relations' similarity metrics.
	 * @see StructureSimilarity#relationsSimilarityMetric(EObject, EObject, MetamodelFilter)
	 */
	private double relationsSimilarity(EObject obj1, EObject obj2) throws FactoryException {
		double similarity = 0d;
		final Double value = getSimilarityFromCache(obj1, obj2, SimilarityKind.RELATION);
		if (value != null) {
			similarity = value;
		} else {
			similarity = StructureSimilarity.relationsSimilarityMetric(obj1, obj2, filter);
			setSimilarityInCache(obj1, obj2, SimilarityKind.RELATION, similarity);
		}
		return similarity;
	}

	/**
	 * Stores in cache the given similarity between the two given {@link EObject}s.<br/>
	 * <p>
	 * <code>similarityKind</code> must be one of
	 * <ul>
	 * <li>{@link #NAME_SIMILARITY}</li>
	 * <li>{@link #TYPE_SIMILARITY}</li>
	 * <li>{@link #VALUE_SIMILARITY}</li>
	 * <li>{@link #RELATION_SIMILARITY}</li>
	 * </ul>
	 * </p>
	 * 
	 * @param obj1
	 *            First of the two {@link EObject}s we're setting the similarity for.
	 * @param obj2
	 *            Second of the two {@link EObject}s we're setting the similarity for.
	 * @param similarityKind
	 *            Kind of similarity to set.
	 * @param similarity
	 *            Value of the similarity between the two {@link EObject}s.
	 * @since 1.3
	 */
	protected void setSimilarityInCache(EObject obj1, EObject obj2, SimilarityKind similarityKind,
			double similarity) {
		final Map<String, Double> cache = getCache(similarityKind);

		cache.put(pairHashCode(obj1, obj2), Double.valueOf(similarity));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(EObject leftObject, EObject rightObject) throws FactoryException {
		// this similarity checker needs no initialization.

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(Resource leftResource, Resource rightResource) throws FactoryException {
		// this similarity checker needs no initialization.
	}

	/**
	 * Converts the legacy strings into a regular enumeration.
	 * 
	 * @author Victor Roldan Betancort
	 * @since 1.3
	 */
	protected static enum SimilarityKind {
		/** This constant is used as key for the buffering of type similarity. */
		TYPE,

		/** This constant is used as key for the buffering of name similarity. */
		NAME,

		/** This constant is used as key for the buffering of relations similarity. */
		RELATION,

		/** This constant is used as key for the buffering of value similarity. */
		VALUE
	}
}

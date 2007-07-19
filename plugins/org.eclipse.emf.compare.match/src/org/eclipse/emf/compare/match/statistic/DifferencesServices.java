/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.statistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.match.api.MatchEngine;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.MatchFactory;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.UnMatchElement;
import org.eclipse.emf.compare.match.statistic.similarity.NameSimilarity;
import org.eclipse.emf.compare.match.statistic.similarity.StructureSimilarity;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.ETools;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * These services are useful when one wants to compare models more precisely using the method modelDiff. Two
 * distinct strategy can be considered for the matching :
 * <ul>
 * <li>{@link #STRONG_STRATEGY Strong} will only consider Add, Remove or Change operations.</li>
 * <li>{@link #SOFT_STRATEGY Soft} will consider Add, Remove, Change, Move and Rename operations.</li>
 * </ul>
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class DifferencesServices implements MatchEngine {
	/** Strong strategy considers only ADD, REMOVE or CHANGE operations. */
	public static final int STRONG_STRATEGY = 0;

	/** Soft strategy considers ADD, REMOVE, CHANGE, MOVE and RENAME operations. */
	public static final int SOFT_STRATEGY = 1;

	/** Default value for the search window. Will be used if the GUI hasn't been loaded. */
	private static final int DEFAULT_SEARCH_WINDOW = 100;

	private static final int MIN_ATTRIBUTES_COUNT = 5;

	private static final double THRESHOLD = 0.96d;

	private static final double STRONGER_THRESHOLD = 0.96d;

	private static final String SUBMATCH_ELEMENT_NAME = "subMatchElements"; //$NON-NLS-1$

	private static final String UNMATCH_ELEMENT_NAME = "unMatchedElements"; //$NON-NLS-1$

	private static final String NAME_SIMILARITY = "n"; //$NON-NLS-1$

	private static final String TYPE_SIMILARITY = "t"; //$NON-NLS-1$

	private static final String VALUE_SIMILARITY = "v"; //$NON-NLS-1$

	private static final String RELATION_SIMILARITY = "r"; //$NON-NLS-1$
	
	/** This map allows us memorize the {@link EObject} we've been able to match thanks to their XMI ID. */
	private final HashMap<EObject, EObject> matchedByID = new HashMap<EObject, EObject>();

	protected MetamodelFilter filter;

	private int currentStrategy = STRONG_STRATEGY;

	private List<EObject> stillToFindFromModel1 = new ArrayList<EObject>();

	private List<EObject> stillToFindFromModel2 = new ArrayList<EObject>();

	/**
	 * This map is used to cache the comparison results Pair(Element1, Element2) => [nameSimilarity,
	 * valueSimilarity, relationSimilarity, TypeSimilarity].
	 */
	private Map<String, Double> metricsCache = new WeakHashMap<String, Double>();

	/**
	 * Set a different kind of strategy, must be one of {@link #STRONG_STRATEGY} or {@link #SOFT_STRATEGY}. Default is SOFT_STRATEGY.
	 * 
	 * @param strategyId
	 *            Matching strategy to consider for the matching.
	 */
	public void setStrategy(int strategyId) {
		currentStrategy = strategyId;
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
	 */
	private String pairHashCode(EObject obj1, EObject obj2, String similarityKind) {
		if (!(similarityKind.equals(NAME_SIMILARITY) || similarityKind.equals(TYPE_SIMILARITY) || similarityKind.equals(VALUE_SIMILARITY) || similarityKind.equals(RELATION_SIMILARITY)))
			throw new IllegalArgumentException("Similarity kind cannot be" + similarityKind); //$NON-NLS-1$
		return similarityKind + obj1.hashCode() + obj2.hashCode();
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
	 * @return The similarity as described by <code>similarityKind</code> as it is stored in cache for the two given {@link EObject}s.
	 */
	private Double getSimilarityFromCache(EObject obj1, EObject obj2, String similarityKind) {
		return metricsCache.get(pairHashCode(obj1, obj2, similarityKind));
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
	 */
	private void setSimilarityInCache(EObject obj1, EObject obj2, String similarityKind, double similarity) {
		metricsCache.put(pairHashCode(obj1, obj2, similarityKind), new Double(similarity));
	}

	/**
	 * This will compute the similarity between two {@link EObject}s' names.
	 * 
	 * @param obj1
	 *            First of the two {@link EObject}s.
	 * @param obj2
	 *            Second of the two {@link EObject}s.
	 * @return <code>double</code> representing the similarity between the two {@link EObject}s' names. 0 &lt; value &lt; 1.
	 * @see NameSimilarity#nameSimilarityMetric(String, String)
	 */
	private double nameSimilarity(EObject obj1, EObject obj2) {
		double similarity = 0d;
		try {
			final Double value = getSimilarityFromCache(obj1, obj2, NAME_SIMILARITY);
			if (value != null) {
				similarity = value;
			} else {
				similarity = NameSimilarity.nameSimilarityMetric(NameSimilarity.findName(obj1), NameSimilarity.findName(obj2));
				setSimilarityInCache(obj1, obj2, NAME_SIMILARITY, similarity);
			}
		} catch (FactoryException e) {
			// fails silently, will return 0d
		}
		return similarity;
	}

	/**
	 * This will compute the similarity between two {@link EObject}s' types.
	 * 
	 * @param obj1
	 *            First of the two {@link EObject}s.
	 * @param obj2
	 *            Second of the two {@link EObject}s.
	 * @return <code>double</code> representing the similarity between the two {@link EObject}s' types. 0 &lt; value &lt; 1.
	 * @throws FactoryException
	 *             Thrown if we cannot compute the type similarity metrics.
	 * @see StructureSimilarity#typeSimilarityMetric(EObject, EObject)
	 */
	private double typeSimilarity(EObject obj1, EObject obj2) throws FactoryException {
		double similarity = 0d;
		final Double value = getSimilarityFromCache(obj1, obj2, TYPE_SIMILARITY);
		if (value != null) {
			similarity = value;
		} else {
			similarity = StructureSimilarity.typeSimilarityMetric(obj1, obj2);
			setSimilarityInCache(obj1, obj2, TYPE_SIMILARITY, similarity);
		}
		return similarity;
	}

	/**
	 * This will compute the similarity between two {@link EObject}s' relations.
	 * 
	 * @param obj1
	 *            First of the two {@link EObject}s.
	 * @param obj2
	 *            Second of the two {@link EObject}s.
	 * @return <code>double</code> representing the similarity between the two {@link EObject}s' relations. 0 &lt; value &lt; 1.
	 * @throws FactoryException
	 *             Thrown if we cannot compute the relations' similarity metrics.
	 * @see StructureSimilarity#relationsSimilarityMetric(EObject, EObject, MetamodelFilter)
	 */
	private double relationsSimilarity(EObject obj1, EObject obj2) throws FactoryException {
		double similarity = 0d;
		final Double value = getSimilarityFromCache(obj1, obj2, RELATION_SIMILARITY);
		if (value != null) {
			similarity = value;
		} else {
			similarity = StructureSimilarity.relationsSimilarityMetric(obj1, obj2, filter);
			setSimilarityInCache(obj1, obj2, RELATION_SIMILARITY, similarity);
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
	 * @return <code>double</code> representing the similarity between the two {@link EObject}s' contents. 0 &lt; value &lt; 1.
	 * @throws FactoryException
	 *             Thrown if we cannot compute the {@link EObject}s' contents similarity metrics.
	 * @see NameSimilarity#contentValue(EObject, MetamodelFilter)
	 */
	private double contentSimilarity(EObject obj1, EObject obj2) throws FactoryException {
		double similarity = 0d;
		final Double value = getSimilarityFromCache(obj1, obj2, VALUE_SIMILARITY);
		if (value != null) {
			similarity = value;
		} else {
			similarity = NameSimilarity.nameSimilarityMetric(NameSimilarity.contentValue(obj1, filter), NameSimilarity.contentValue(obj2, filter));
			setSimilarityInCache(obj1, obj2, VALUE_SIMILARITY, similarity);
		}
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
	private double absoluteMetric(EObject obj1, EObject obj2) throws FactoryException {
		final double nameSimilarity = nameSimilarity(obj1, obj2);
		final double relationsSimilarity = relationsSimilarity(obj1, obj2);
		double sameUri = 0d;
		if (hasSameUri(obj1, obj2))
			sameUri = 1;
		final double positionSimilarity = relationsSimilarity / 2d + sameUri / 2d;
		final double contentSimilarity = contentSimilarity(obj1, obj2);
		// Computing type similarity really is time expensive
		// double typeSimilarity = typeSimilarity(obj1, obj2);

		final double contentWeight = 0.5d;
		final double nameWeight = 0.4d;
		final double positionWeight = 0.4d;

		return (contentSimilarity * contentWeight + nameSimilarity * nameWeight + positionSimilarity * positionWeight) / (contentWeight + nameWeight + positionWeight);
	}

	/**
	 * Returns <code>True</code> if the 2 given {@link EObject}s are considered similar.
	 * 
	 * @param obj1
	 *            The first {@link EObject} to compare.
	 * @param obj2
	 *            Second of the {@link EObject}s to compare.
	 * @return <code>True</code> if both elements have the same serialization ID, <code>False</code>
	 *         otherwise.
	 * @throws FactoryException
	 *             Thrown if we cannot compute one of the needed similarity.
	 */
	private boolean isSimilar(EObject obj1, EObject obj2) throws FactoryException {
		boolean similar = false;

		final double fewerAttributesNameThreshold = 0.8d;
		final double relationsThreshold = 0.9d;
		final double nameThreshold = 0.2d;
		final double softContentThreshold = 0.59d;
		final double strongContentThreshold = 0.9d;
		final double softTriWayThreshold = 0.8d;
		final double strongTriWayThreshold = 0.9d;

		double contentThreshold = softContentThreshold;
		double triWayThreshold = softTriWayThreshold;
		double generalThreshold = THRESHOLD;
		if (currentStrategy == STRONG_STRATEGY) {
			contentThreshold = strongContentThreshold;
			triWayThreshold = strongTriWayThreshold;
			generalThreshold = STRONGER_THRESHOLD;
		}

		if (haveDistinctXMIID(obj1, obj2)) {
			similar = false;
		} else {
			final double nameSimilarity = nameSimilarity(obj1, obj2);
			final boolean hasSameUri = hasSameUri(obj1, obj2);

			if (nameSimilarity == 1 && hasSameUri) {
				similar = true;
				// softer test if we don't have enough attributes to compare the objects
			} else if (nameSimilarity > fewerAttributesNameThreshold
					&& nonNullFeaturesCount(obj1) <= MIN_ATTRIBUTES_COUNT
					&& nonNullFeaturesCount(obj2) <= MIN_ATTRIBUTES_COUNT
					&& typeSimilarity(obj1, obj2) > generalThreshold) {
				similar = true;
			} else {
				final double contentSimilarity = contentSimilarity(obj1, obj2);
				final double relationsSimilarity = relationsSimilarity(obj1, obj2);

				if (relationsSimilarity == 1 && hasSameUri && nameSimilarity > nameThreshold) {
					similar = true;
				} else if (contentSimilarity == 1 && relationsSimilarity == 1) {
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
		}
		return similar;
	}

	/**
	 * Checks wether the two given {@link EObject} have the same URI.
	 * 
	 * @param obj1
	 *            First of the two {@link EObject} we're comparing.
	 * @param obj2
	 *            Second {@link EObject} we're comparing.
	 * @return <code>True</code> if the {@link EObject}s have the same URI, <code>False</code> otherwise.
	 */
	private boolean hasSameUri(EObject obj1, EObject obj2) {
		return ETools.getURI(obj1).equals(ETools.getURI(obj2));
	}

	/**
	 * This method is an indirection for adding Mappings in the current MappingGroup.
	 * 
	 * @param object
	 *            {@link EObject} to add a feature value to.
	 * @param name
	 *            Name of the feature to consider.
	 * @param value
	 *            Value to add to the feature <code>name</code> of <code>object</code>.
	 * @throws FactoryException
	 *             Thrown if the value's affectation fails.
	 */
	private void redirectedAdd(EObject object, String name, Object value) throws FactoryException {
		EFactory.eAdd(object, name, value);
	}

	/**
	 * Returns THe search window corresponding to the number of siblings to consider while matching. Reducing this number (on the preferences page)
	 * considerably improve performances while reducing precision.
	 * 
	 * @return An <code>int</code> representing the number of siblings to consider for matching.
	 */
	private int getSearchWindow() {
		int searchWindow = DEFAULT_SEARCH_WINDOW;
		if (EMFComparePlugin.getDefault() != null && EMFComparePlugin.getDefault().getPluginPreferences().getInt("emfcompare.search.window") > 0) //$NON-NLS-1$
			searchWindow = EMFComparePlugin.getDefault().getPluginPreferences().getInt("emfcompare.search.window"); //$NON-NLS-1$
		return searchWindow;
	}

	/**
	 * Returns a mapping model between the two other models. Basically the difference is computed this way :
	 * <ul>
	 * <li>Both models are browsed and compared, Mappings are created when two nodes are considered as similar</li>
	 * <li>Nodes which haven't been mapped are compared with each other in order to map them.</li>
	 * <li>The mapping tree is browsed in order to determine the modification log.</li>
	 * <li>The modification log (an EMF model) is then returned.</li>
	 * </ul>
	 * 
	 * @param root1
	 *            Left model of the comparison.
	 * @param root2
	 *            Right model of the comparison.
	 * @param monitor
	 *            {@link IProgressMonitor Progress monitor} to display while the comparison lasts.
	 * @return Mapping model of the two given models.
	 * @throws InterruptedException
	 *             Thrown if the matching process is interrupted somehow.
	 * @see MatchEngine#modelMatch(EObject, EObject, IProgressMonitor)
	 */
	@SuppressWarnings("unchecked")
	public MatchModel modelMatch(EObject root1, EObject root2, IProgressMonitor monitor) throws InterruptedException {
		final MatchModel root = MatchFactory.eINSTANCE.createMatchModel();
		launchMonitor(monitor, root1);

		// filtering unused features
		filter = new MetamodelFilter();
		filter.analyseModel(root1);
		filter.analyseModel(root2);
		// end of filtering

		final Resource leftResource = root1.eResource();
		final Resource rightResource = root2.eResource();
		if (leftResource instanceof XMIResource && rightResource instanceof XMIResource)
			matchByID((XMIResource)leftResource, (XMIResource)rightResource);

		// navigate through both models at the same time and realize mappings..
		try {
			monitor.subTask("Matching roots"); //$NON-NLS-1$
			final List<Match2Elements> matchedRoots = mapLists(root1.eResource().getContents(), root2.eResource().getContents(), getSearchWindow(), monitor);
			stillToFindFromModel1.clear();
			stillToFindFromModel2.clear();
			final List<EObject> unMatchedLeftRoots = new ArrayList(root1.eResource().getContents());
			final List<EObject> unMatchedRightRoots = new ArrayList(root2.eResource().getContents());

			Match2Elements matchModelRoot = MatchFactory.eINSTANCE.createMatch2Elements();
			// We haven't found any similar roots, we then consider the firsts to be similar
			if (matchedRoots.size() == 0) {
				final Match2Elements rootMapping = MatchFactory.eINSTANCE.createMatch2Elements();
				rootMapping.setLeftElement(root1);
				rootMapping.setRightElement(findMostSimilar(root1, unMatchedRightRoots));
				matchedRoots.add(rootMapping);
			}
			for (Match2Elements matchedRoot : matchedRoots) {
				monitor.subTask("Processing matched roots' contents"); //$NON-NLS-1$
				final Match2Elements rootMapping = recursiveMappings(matchedRoot.getLeftElement(), matchedRoot.getRightElement(), monitor);
				// this is the first passage
				if (matchModelRoot.getLeftElement() == null) {
					matchModelRoot = rootMapping;
					redirectedAdd(root, "matchedElements", matchModelRoot); //$NON-NLS-1$
				} else {
					redirectedAdd(matchModelRoot, SUBMATCH_ELEMENT_NAME, rootMapping);
				}

				// Keep current lists in a corner and init the objects list we still have to map
				final List<EObject> still1 = new ArrayList<EObject>(stillToFindFromModel1);
				final List<EObject> still2 = new ArrayList<EObject>(stillToFindFromModel2);

				createSubMatchElements(rootMapping, still1, still2, monitor);
				// now the other elements won't be mapped, keep them in the model
				createUnMatchElements(root, stillToFindFromModel1);
				createUnMatchElements(root, stillToFindFromModel2);

				unMatchedLeftRoots.remove(matchedRoot.getLeftElement());
				unMatchedRightRoots.remove(matchedRoot.getRightElement());
			}
			// We'll iterate through the unMatchedRoots all contents
			monitor.subTask("processing unmatched roots"); //$NON-NLS-1$
			createSubMatchElements(matchModelRoot, unMatchedLeftRoots, unMatchedRightRoots, monitor);
			createUnMatchElements(root, stillToFindFromModel1);
			createUnMatchElements(root, stillToFindFromModel2);
		} catch (FactoryException e) {
			EMFComparePlugin.log(e, false);
		}
		return root;
	}

	/**
	 * Sizes the given {@link IProgressMonitor monitor} and launches its main task for model comparison.
	 * 
	 * @param monitor
	 *            Progress monitor to display while the operation lasts.
	 * @param root
	 *            Root of the first model on which the comparison will be launched.
	 */
	private void launchMonitor(IProgressMonitor monitor, EObject root) {
		int size = 1;
		final Iterator sizeit = root.eAllContents();
		while (sizeit.hasNext()) {
			sizeit.next();
			size++;
		}

		monitor.beginTask("Comparing model", size); //$NON-NLS-1$
		monitor.subTask("Browsing model"); //$NON-NLS-1$
	}

	/**
	 * We consider here <code>current1</code> and <code>current2</code> are similar. This method creates the mapping for the objects
	 * <code>current1</code> and <code>current2</code>, Then submappings for these two elements' contents.
	 * 
	 * @param current1
	 *            First element of the two elements mapping.
	 * @param current2
	 *            Second of the two elements mapping.
	 * @param monitor
	 *            {@link IProgressMonitor Progress monitor} to display while the comparison lasts.
	 * @return The mapping for <code>current1</code> and <code>current2</code> and their content.
	 * @throws FactoryException
	 *             Thrown when the metrics cannot be computed for <code>current1</code> and <code>current2</code>.
	 * @throws InterruptedException
	 *             Thrown if the matching process is interrupted somehow.
	 */
	@SuppressWarnings("unchecked")
	private Match2Elements recursiveMappings(EObject current1, EObject current2, IProgressMonitor monitor) throws FactoryException, InterruptedException {
		Match2Elements mapping = null;
		mapping = MatchFactory.eINSTANCE.createMatch2Elements();
		mapping.setLeftElement(current1);
		mapping.setRightElement(current2);
		mapping.setSimilarity(absoluteMetric(current1, current2));
		final List<Match2Elements> mapList = mapLists(current1.eContents(), current2.eContents(), getSearchWindow(), monitor);
		// We can map other elements with mapLists; we iterate through them.
		final Iterator<Match2Elements> it = mapList.iterator();
		while (it.hasNext()) {
			final Match2Elements subMapping = it.next();
			// As we know source and target are similars, we call recursive mappings onto these objects
			EFactory.eAdd(mapping, SUBMATCH_ELEMENT_NAME, recursiveMappings(subMapping.getLeftElement(), subMapping.getRightElement(), monitor));
		}
		return mapping;
	}

	/**
	 * This will iterate through the given {@link List} and return its element which is most similar (as given by
	 * {@link #absoluteMetric(EObject, EObject)}) to the given {@link EObject}.
	 * 
	 * @param eObj
	 *            {@link EObject} we're searching a similar item for in the list.
	 * @param list
	 *            {@link List} in which we are to find an object similar to <code>eObj</code>.
	 * @return The element from <code>list</code> which is the most similar to <code>eObj</code>.
	 * @throws FactoryException
	 *             Thrown if we cannot compute the {@link #absoluteMetric(EObject, EObject) absolute metric} between <code>eObj</code> and one of
	 *             the list's objects.
	 */
	private EObject findMostSimilar(EObject eObj, List list) throws FactoryException {
		double max = 0d;
		EObject resultObject = null;
		final Iterator it = list.iterator();
		while (it.hasNext()) {
			final EObject next = (EObject)it.next();
			final double similarity = absoluteMetric(eObj, next);
			if (similarity > max) {
				max = similarity;
				resultObject = next;
			}
		}
		return resultObject;
	}

	/**
	 * Returns a list containing mappings of the nodes of both given {@link List}s.
	 * 
	 * @param list1
	 *            First of the lists from which we need to map the elements
	 * @param list2
	 *            Second list to map the elements from.
	 * @param window
	 *            Number of siblings to consider for the matching.
	 * @param monitor
	 *            {@link IProgressMonitor Progress monitor} to display while the comparison lasts.
	 * @return A {@link List} containing mappings of the nodes of both given {@link List}s.
	 * @throws FactoryException
	 *             Thrown if the metrics cannot be computed.
	 * @throws InterruptedException
	 *             Thrown if the matching process is interrupted somehow.
	 */
	private List<Match2Elements> mapLists(List<EObject> list1, List<EObject> list2, int window,
			IProgressMonitor monitor) throws FactoryException, InterruptedException {
		final List<Match2Elements> result = new ArrayList<Match2Elements>();
		int curIndex = 0 - window / 2;
		final List<EObject> notFoundList1 = new ArrayList<EObject>(list1);
		final List<EObject> notFoundList2 = new ArrayList<EObject>(list2);

		final Iterator it1 = list1.iterator();
		// then iterate over the 2 lists and compare the elements
		while (it1.hasNext() && list2.size() > 0) {
			final EObject obj1 = (EObject)it1.next();
			EObject obj2 = matchedByID.get(obj1);

			if (obj2 == null) {
				final int end = Math.min(curIndex + window, list2.size());
				final int index = Math.min(Math.max(curIndex, 0), end);

				obj2 = findMostSimilar(obj1, list2.subList(index, end));
				// checks if the most similar to obj2 is obj1
				final EObject obj1Check = findMostSimilar(obj2, notFoundList1);
				if (obj1Check != obj1 && isSimilar(obj1Check, obj2)) {
					continue;
				}
			}

			if (notFoundList1.contains(obj1) && notFoundList2.contains(obj2) && isSimilar(obj1, obj2)) {
				final Match2Elements mapping = MatchFactory.eINSTANCE.createMatch2Elements();
				final double metric = absoluteMetric(obj1, obj2);

				mapping.setLeftElement(obj1);
				mapping.setRightElement(obj2);
				mapping.setSimilarity(metric);
				result.add(mapping);
				notFoundList2.remove(obj2);
				notFoundList1.remove(obj1);
			}
			curIndex += 1;
			monitor.worked(1);
			if (monitor.isCanceled())
				throw new InterruptedException();
		}

		// now putting the not found elements aside for later
		stillToFindFromModel2.addAll(notFoundList2);
		stillToFindFromModel1.addAll(notFoundList1);
		return result;
	}

	/**
	 * Creates the {@link Match2Elements submatch elements} corresponding to the mapping of objects from the two given {@link List}s.
	 * 
	 * @param root
	 *            Root of the {@link MatchModel} where to insert all these mappings.
	 * @param list1
	 *            First of the lists used to compute mapping.
	 * @param list2
	 *            Second of the lists used to compute mapping.
	 * @param monitor
	 *            {@link IProgressMonitor progress monitor} to display while the comparison lasts.
	 * @throws FactoryException
	 *             Thrown if we cannot match the elements of the two lists or add submatch elements to <code>root</code>.
	 * @throws InterruptedException
	 *             Thrown if the operation is cancelled or fails somehow.
	 */
	private void createSubMatchElements(EObject root, List<EObject> list1, List<EObject> list2, IProgressMonitor monitor) throws FactoryException, InterruptedException {
		stillToFindFromModel1.clear();
		stillToFindFromModel2.clear();
		final List<Match2Elements> mappings = mapLists(list1, list2, getSearchWindow(), monitor);

		final Iterator<Match2Elements> it = mappings.iterator();
		while (it.hasNext()) {
			final Match2Elements map = it.next();
			final Match2Elements match = recursiveMappings(map.getLeftElement(), map.getRightElement(), monitor);
			redirectedAdd(root, SUBMATCH_ELEMENT_NAME, match);
		}
	}

	/**
	 * Creates {@link UnMatchElement}s wrapped around all the elements of the given {@link List}.
	 * 
	 * @param root
	 *            Root of the {@link MatchModel} under which to insert all these {@link UnMatchElement}s.
	 * @param unMatchedElements
	 *            {@link List} containing all the elements we haven't been able to match.
	 * @throws FactoryException
	 *             Thrown if we cannot add elements under the given {@link MatchModel root}.
	 */
	private void createUnMatchElements(MatchModel root, List<EObject> unMatchedElements) throws FactoryException {
		for (EObject element : unMatchedElements) {
			final UnMatchElement unMap = MatchFactory.eINSTANCE.createUnMatchElement();
			unMap.setElement(element);
			redirectedAdd(root, UNMATCH_ELEMENT_NAME, unMap);
		}
		unMatchedElements.clear();
	}

	/**
	 * Counts all the {@link EStructuralFeature features} of the given {@link EObject} that are <code>null</code> or initialized to the empty
	 * {@link String} &quot;&quot;.
	 * 
	 * @param eobj
	 *            {@link EObject} we need to count the empty features of.
	 * @return The number of features initialized to <code>null</code> or the empty String.
	 */
	@SuppressWarnings("unchecked")
	private int nonNullFeaturesCount(EObject eobj) {
		int nonNullFeatures = 0;
		final Iterator<EStructuralFeature> features = eobj.eClass().getEAllStructuralFeatures().iterator();
		while (features.hasNext()) {
			final EStructuralFeature feature = features.next();
			if (eobj.eGet(feature) != null && !eobj.eGet(feature).toString().equals("")) //$NON-NLS-1$
				nonNullFeatures++;
		}
		return nonNullFeatures;
	}
	
	/**
	 * This will compare two objects to see if they have ID and in that case, if these IDs are distinct.
	 * 
	 * @param left
	 *            Left of the two objects to compare.
	 * @param right
	 *            right of the two objects to compare.
	 * @return <code>True</code> if only one of the two objects has an ID or the two are distinct, <code>False</code> otherwise.
	 */
	private boolean haveDistinctXMIID(EObject left, EObject right) {
		boolean result = false;
		String item1ID = null;
		String item2ID = null;
		if (left.eResource() != null && left.eResource() instanceof XMIResource)
			item1ID = ((XMIResource)left.eResource()).getID(left);
		if (right.eResource() != null && right.eResource() instanceof XMIResource)
			item2ID = ((XMIResource)right.eResource()).getID(right);
		if (item1ID != null) {
			result = !item1ID.equals(item2ID);
		} else {
			result = item2ID != null;
		}
		return result;
	}

	/**
	 * Iterates through both of the given {@link XMIResource resources} to find all the elements that can be matched by their XMI ID, then populates
	 * {@link #matchedByID} with those mappings.
	 * 
	 * @param left
	 *            First of the two {@link XMIResource resources} to visit.
	 * @param right
	 *            Second of the {@link XMIResource resources} to visit.
	 */
	private void matchByID(XMIResource left, XMIResource right) {
		matchedByID.clear();
		final Iterator leftIterator = left.getAllContents();

		while (leftIterator.hasNext()) {
			final EObject item1 = (EObject)leftIterator.next();
			final String item1ID = left.getID(item1);
			if (item1ID != null) {
				final EObject item2 = right.getEObject(item1ID);
				if (item2 != null) {
					matchedByID.put(item1, item2);
				}
			}
		}
	}
}

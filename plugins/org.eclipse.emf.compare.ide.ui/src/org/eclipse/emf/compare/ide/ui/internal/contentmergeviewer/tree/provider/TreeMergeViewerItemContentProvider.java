/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Alexandra Buzila, Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree.provider;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newArrayListWithCapacity;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.CONTAINMENT_REFERENCE_CHANGE;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.anyRefining;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.graph.IGraphView;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.match.impl.NotLoadedFragmentMatch;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.MergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.ResourceAttachmentChangeMergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.util.MergeViewerUtil;
import org.eclipse.emf.compare.rcp.ui.internal.util.ResourceUIUtil;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.provider.IMergeViewerItemContentProvider;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.provider.IMergeViewerItemProviderConfiguration;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.edit.provider.FeatureMapEntryWrapperItemProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * ContentProvider for {@link IMergeViewerItem}s which uses their 'left', 'right' and 'ancestor' sides in
 * combination with the given {@link AdapterFactory} to create the children and parent
 * {@link IMergeViewerItem}s.
 * 
 * @author Alexandra Buzila
 * @author Stefan Dirix
 */
public class TreeMergeViewerItemContentProvider implements IMergeViewerItemContentProvider {

	/**
	 * {@inheritDoc}
	 */
	public boolean canHandle(Object object) {
		return object instanceof IMergeViewerItem;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getParent(Object object, IMergeViewerItemProviderConfiguration configuration) {
		IMergeViewerItem mergeViewerItem = (IMergeViewerItem)object;
		IMergeViewerItem ret = null;
		if (mergeViewerItem.getDiff() instanceof ResourceAttachmentChange) {
			ret = createBasicContainer((ResourceAttachmentChange)mergeViewerItem.getDiff(), mergeViewerItem,
					configuration.getAdapterFactory());
		} else {
			Object sideValue = getBestSideValue(mergeViewerItem, configuration.getSide());
			Object parent = null;
			ITreeItemContentProvider treeItemContentProvider = (ITreeItemContentProvider)configuration
					.getAdapterFactory().adapt(sideValue, ITreeItemContentProvider.class);
			if (treeItemContentProvider != null) {
				parent = treeItemContentProvider.getParent(sideValue);
			}
			if (parent instanceof EObject) {
				ret = createBasicMergeViewerItem((EObject)parent, mergeViewerItem.getSide(), configuration);
			} else if (parent instanceof Resource) {
				ret = createParent(mergeViewerItem, (Resource)parent, configuration);
			} else if (sideValue instanceof NotLoadedFragmentMatch) {
				ret = createParent(mergeViewerItem, (NotLoadedFragmentMatch)sideValue, configuration);
			}
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasChildren(Object object, IMergeViewerItemProviderConfiguration configuration) {
		IMergeViewerItem mergeViewerItem = (IMergeViewerItem)object;
		if (mergeViewerItem.getLeft() instanceof NotLoadedFragmentMatch) {
			final NotLoadedFragmentMatch notLoadedFragmentMatch = (NotLoadedFragmentMatch)mergeViewerItem
					.getLeft();
			return !notLoadedFragmentMatch.getChildren().isEmpty();
		}

		final Object sideValue = getSideValue(mergeViewerItem, mergeViewerItem.getSide());
		final List<Object> children = getChildrenFromContentProvider(sideValue,
				configuration.getAdapterFactory());
		final List<Object> otherSideChildren = getChildrenFromContentProvider(
				getSideValue(mergeViewerItem, mergeViewerItem.getSide().opposite()),
				configuration.getAdapterFactory());

		final List<? extends Diff> differences = collectAndFilterDifferences(otherSideChildren,
				configuration);
		if (hasChildren(mergeViewerItem, differences, children, configuration)) {
			return true;
		}

		final EObject bestSideValue = (EObject)getBestSideValue(mergeViewerItem, configuration.getSide());
		final Match match = configuration.getComparison().getMatch(bestSideValue);

		return hasNotLoadedFragmentsItems(match, mergeViewerItem.getSide());
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] getChildren(Object object, IMergeViewerItemProviderConfiguration configuration) {
		ArrayList<Object> ret = Lists.newArrayList();
		IMergeViewerItem mergeViewerItem = (IMergeViewerItem)object;

		if (mergeViewerItem.getLeft() instanceof NotLoadedFragmentMatch) {
			ret.addAll(createChildren(mergeViewerItem, (NotLoadedFragmentMatch)mergeViewerItem.getLeft(),
					configuration));
		} else {
			Object sideValue = getSideValue(mergeViewerItem, mergeViewerItem.getSide());
			Object otherSideValue = getSideValue(mergeViewerItem, mergeViewerItem.getSide().opposite());
			final List<Object> children = getChildrenFromContentProvider(sideValue,
					configuration.getAdapterFactory());
			final List<Object> otherSideChildren = getChildrenFromContentProvider(otherSideValue,
					configuration.getAdapterFactory());

			List<? extends Diff> differences = collectAndFilterDifferences(otherSideChildren, configuration);
			ret.addAll(createChildren(children, mergeViewerItem, differences, configuration));

			// Add not loaded fragment match if needed
			final EObject bestSideValue = (EObject)getBestSideValue(mergeViewerItem, configuration.getSide());
			final Match match = configuration.getComparison().getMatch(bestSideValue);
			ret.addAll(getNotLoadedFragmentsItems(configuration.getComparison(), match,
					mergeViewerItem.getSide(), configuration.getAdapterFactory()));
		}
		return ret.toArray();
	}

	/**
	 * Determines the differences related to the given objects and filters them according to the
	 * {@link #visibleContainmentDiffPredicate(IMergeViewerItemProviderConfiguration)}.
	 * 
	 * @param objects
	 *            the objects.
	 * @param configuration
	 *            the {@link IMergeViewerItemProviderConfiguration}.
	 * @return the determined differences.
	 */
	protected List<? extends Diff> collectAndFilterDifferences(Iterable<Object> objects,
			IMergeViewerItemProviderConfiguration configuration) {
		final List<? extends Diff> differences = collectDifferences(configuration.getComparison(), objects);
		return Lists.newArrayList(filter(differences, visibleContainmentDiffPredicate(configuration)));
	}

	/**
	 * Indicates whether the given {@link IMergeViewerItem} parent has real or insertion point children.
	 * 
	 * @param parent
	 *            the {@link IMergeViewerItem} for which the children are to be determined.
	 * @param differences
	 *            the list of relevant diffs.
	 * @param children
	 *            the list of possible children candidates.
	 * @param configuration
	 *            the {@link IMergeViewerItemConfiguration}.
	 * @return {@code true} if the given {@code parent} should have children {@link IMergeViewerItem}s,
	 *         {@code false} otherwise.
	 */
	private boolean hasChildren(IMergeViewerItem parent, Iterable<? extends Diff> differences,
			List<Object> children, IMergeViewerItemProviderConfiguration configuration) {
		if (yieldsMergeViewerItem(configuration.getComparison(), parent.getDiff(), children)) {
			return true;
		}

		if (parent.getSide() != MergeViewerSide.ANCESTOR) {
			return yieldsInsertionPoint(parent, differences, configuration);
		}

		return true;
	}

	/**
	 * Get the children of the MergeViewerItem in case the MergeViewerItem is a
	 * {@link org.eclipse.emf.compare.match.impl.NotLoadedFragmentMatch}.
	 * 
	 * @param parent
	 *            the parent {@link IMergeViewerItem}
	 * @param nlfm
	 *            the NotLoadedFragmentMatch for which we want the children (as MergeViewerItems)
	 * @param configuration
	 *            the {@link IMergeViewerItemProviderConfiguration}.
	 * @return the children (a list of MergeViewerItems) of the given element.
	 */
	private List<IMergeViewerItem> createChildren(IMergeViewerItem parent, NotLoadedFragmentMatch nlfm,
			IMergeViewerItemProviderConfiguration configuration) {
		final List<IMergeViewerItem> ret = newArrayList();
		final Collection<Match> matches = nlfm.getChildren();
		for (Match match : matches) {
			final IMergeViewerItem container;
			if (match instanceof NotLoadedFragmentMatch) {
				container = createMergeViewerItem(configuration.getComparison(), parent.getDiff(), match,
						match, match, parent.getSide(), configuration.getAdapterFactory());
			} else {
				container = createMergeViewerItem(configuration.getComparison(), parent.getDiff(),
						match.getLeft(), match.getRight(), match.getOrigin(), parent.getSide(),
						configuration.getAdapterFactory());
			}
			ret.add(container);
		}
		return ret;
	}

	/**
	 * Creates the {@link IMergeViewerItem}s and insertion points for the given children.
	 * 
	 * @param children
	 *            the children for which {@link IMergeViewerItem}s shall be created.
	 * @param parent
	 *            the parent of the children to create.
	 * @param differences
	 *            list of relevant differences.
	 * @param configuration
	 *            the {@link IMergeViewerItemProviderConfiguration}.
	 * @return the list of created {@link IMergeViewerItem}s.
	 */
	private List<IMergeViewerItem> createChildren(Collection<Object> children, IMergeViewerItem parent,
			final List<? extends Diff> differences, IMergeViewerItemProviderConfiguration configuration) {
		final List<IMergeViewerItem> ret = Lists.newArrayList();

		final List<IMergeViewerItem> realChildren = createMergeViewerItemsFrom(children, parent,
				configuration);
		if (parent.getSide() != MergeViewerSide.ANCESTOR) {
			ret.addAll(createInsertionPoints(parent, realChildren, differences, configuration));
		} else {
			ret.addAll(realChildren);
		}
		return ret;
	}

	/**
	 * Creates and inserts the insertion points for the given {@code values}.
	 * 
	 * @param parent
	 *            the {@link IMergeViewerItem} parent.
	 * @param values
	 *            the list of {@link IMergeViewerItem} children for which insertion points are to be created.
	 * @param differences
	 *            the list of relevant differences.
	 * @param configuration
	 *            the {@link IMergeViewerItemProviderConfiguration}.
	 * @return the list of {@code values} containing the created insertion points.
	 */
	protected List<IMergeViewerItem> createInsertionPoints(IMergeViewerItem parent,
			final List<IMergeViewerItem> values, List<? extends Diff> differences,
			IMergeViewerItemProviderConfiguration configuration) {
		MergeViewerSide side = parent.getSide();
		AdapterFactory adapterFactory = configuration.getAdapterFactory();
		List<Object> sideContent = getChildrenFromContentProvider(getSideValue(parent, side), adapterFactory);
		List<Object> oppositeContent = getChildrenFromContentProvider(getSideValue(parent, side.opposite()),
				adapterFactory);
		List<Object> ancestorContent = getChildrenFromContentProvider(
				getSideValue(parent, MergeViewerSide.ANCESTOR), adapterFactory);

		return createInsertionPoints(parent, sideContent, oppositeContent, ancestorContent, values,
				differences, configuration);
	}

	/**
	 * Creates the insertion points for the given {@code values} based on the children of each side.
	 * 
	 * @param parent
	 *            the {@link IMergeViewerItem} parent.
	 * @param sideContent
	 *            the object for 'this' side.
	 * @param oppositeContent
	 *            the object for the 'other' side.
	 * @param ancestorContent
	 *            the objects for the 'origin' side-
	 * @param values
	 *            the {@link IMergeViewerItem}s for which the insertion points are to be created.
	 * @param differences
	 *            the list of relevant differences.
	 * @param configuration
	 *            the {@link IMergeViewerItemProviderConfiguration}.
	 * @return the list of {@code values} containing the created insertion points.
	 */
	protected List<IMergeViewerItem> createInsertionPoints(IMergeViewerItem parent,
			final List<Object> sideContent, final List<Object> oppositeContent,
			final List<Object> ancestorContent, final List<? extends IMergeViewerItem> values,
			List<? extends Diff> differences, IMergeViewerItemProviderConfiguration configuration) {
		final List<IMergeViewerItem> ret = newArrayList(values);
		if (differences.isEmpty()) {
			return ret;
		}

		if (sideContent.isEmpty() && oppositeContent.isEmpty()) {
			return ret;
		}

		Comparison comparison = configuration.getComparison();
		AdapterFactory adapterFactory = configuration.getAdapterFactory();
		MergeViewerSide side = parent.getSide();

		for (Diff diff : Lists.reverse(differences)) {
			EObject value = (EObject)getDiffValue(diff);
			Match match = comparison.getMatch(value);
			if (!isPseudoAddConflict(diff) && (isAddOnOppositeSide(diff, side)
					|| isDeleteOnSameSide(diff, side) || isInsertOnBothSides(diff, match))) {
				if (match == null && diff.getState() == DifferenceState.MERGED) {
					EObject bestSideValue = (EObject)getBestSideValue(parent, configuration.getSide());
					match = comparison.getMatch(bestSideValue);
					match = getMatchWithNullValues(match);
				}

				if (match != null && !isRealAddConflict(diff, match, side)) {
					IMergeViewerItem insertionPoint = createMergeViewerItem(comparison, diff, match.getLeft(),
							match.getRight(), match.getOrigin(), side, adapterFactory);

					final int insertionIndex;
					if (match.getLeft() == null && match.getRight() == null && diff.getConflict() != null
							&& diff.getConflict().getKind() == ConflictKind.PSEUDO) {
						// pseudo conflict delete...
						insertionIndex = ancestorContent.indexOf(value);
					} else {
						insertionIndex = Math.min(
								DiffUtil.findInsertionIndex(comparison, oppositeContent, sideContent, value),
								ret.size());
					}

					// offset the insertion by the number of previous insertion points in the list
					// Cannot be improved by keeping the number of created insertion points because the given
					// "values" parameter may already contains some insertion points.
					int realIndex = 0;
					for (int index = 0; index < insertionIndex && realIndex < ret.size(); realIndex++) {
						if (!ret.get(realIndex).isInsertionPoint()) {
							index++;
						}
					}

					ret.add(realIndex, insertionPoint);
				}
			}
		}
		return ret;
	}

	/**
	 * Determines the value of the given diff. If the diff has no "value", its prime refining or refined-by
	 * counterparts are also checked.
	 * 
	 * @param diff
	 *            the {@link Diff} to check.
	 * @return the value of the given diff. If no value exists {@code null} will be returned.
	 */
	protected Object getDiffValue(Diff diff) {
		final Object diffValue = MergeViewerUtil.getDiffValue(diff);
		if (diffValue != null) {
			return diffValue;
		}

		final Set<Diff> allRefiningDiffs = DiffUtil.getAllRefiningDiffs(diff);
		final Iterable<Diff> containmentRefs = filter(allRefiningDiffs, CONTAINMENT_REFERENCE_CHANGE);
		return getFirstValue(containmentRefs);
	}

	/**
	 * Determines the first non-null value of the given diffs.
	 * 
	 * @param diffs
	 *            the {@link Diff}s to check.
	 * @return the first non-null value if it exists, {@code null} otherwise.
	 */
	protected Object getFirstValue(Iterable<Diff> diffs) {
		final Iterable<Object> values = filter(Iterables.transform(diffs, new Function<Diff, Object>() {
			public Object apply(Diff input) {
				return MergeViewerUtil.getDiffValue(input);
			}
		}), Predicates.notNull());
		if (values.iterator().hasNext()) {
			return values.iterator().next();
		}
		return null;
	}

	/**
	 * Collect the differences of the given objects.
	 * 
	 * @param objects
	 *            The objects whose differences will be returned
	 * @param comparison
	 *            the Comparison
	 * @return An {@link Iterable} over all collected Differences.
	 */
	private List<Diff> collectDifferences(Comparison comparison, Iterable<Object> objects) {
		Iterable<Diff> differences = ImmutableList.of();
		for (Object object : objects) {
			if (EObject.class.isInstance(object)) {
				EList<Diff> objectDifferences = comparison.getDifferences(EObject.class.cast(object));
				differences = Iterables.concat(differences, objectDifferences);
			}
		}
		return Lists.newArrayList(differences);
	}

	/**
	 * Adapts to {@link ITreeItemMergeViewerContentProvider} or {@link ITreeItemContentProvider} and calls
	 * getChildren. Also unwraps {@link FeatureMap.Entry}.
	 * 
	 * @param object
	 *            The object for which the children are to be determined.
	 * @return A list of all children of the given {@code object}.
	 */
	protected List<Object> getChildrenFromContentProvider(Object object, AdapterFactory adapterFactory) {
		if (object == null) {
			return Collections.emptyList();
		}
		ITreeItemContentProvider treeItemContentProvider = (ITreeItemContentProvider)adapterFactory
				.adapt(object, ITreeItemContentProvider.class);
		if (treeItemContentProvider != null) {
			final List<Object> children = new ArrayList<Object>(treeItemContentProvider.getChildren(object));
			return unwrapFeatureMapEntryProviders(children);
		}
		return Collections.emptyList();
	}

	/**
	 * Unwraps {@link FeatureMapEntryWrapperItemProvider} from the given list of {@code values}.
	 * 
	 * @param values
	 *            the values possibly containing {@link FeatureMapEntryWrapperItemProvider}s.
	 * @return list of objects containing the original non-FeatureMapEntryWrapperItemProvider and the
	 *         unwrapped objects.
	 */
	private List<Object> unwrapFeatureMapEntryProviders(List<Object> values) {
		final List<Object> result = new ArrayList<Object>(values.size());
		for (Object value : values) {
			if (FeatureMapEntryWrapperItemProvider.class.isInstance(value)) {
				final FeatureMapEntryWrapperItemProvider featureMapEntryProvider = FeatureMapEntryWrapperItemProvider.class
						.cast(value);
				final Object featureMapEntry = featureMapEntryProvider.getValue();
				if (FeatureMap.Entry.class.isInstance(featureMapEntry)) {
					final Object featureMapValue = FeatureMap.Entry.class.cast(featureMapEntry).getValue();
					result.add(featureMapValue);
				}
			} else {
				result.add(value);
			}
		}
		return result;
	}

	/**
	 * Indicates whether for the given diffs at least one {@link IMergeViewerItem} insertion point should be
	 * created.
	 * 
	 * @param comparison
	 *            the {@link Comparison}.
	 * @param diffs
	 *            the {@link Diff}s.
	 * @param configuration
	 *            the {@link IMergeViewerItemProviderConfiguration}.
	 * @return {@code true} if at least one {@link IMergeViewerItem} should be created, {@code false}
	 *         otherwise.
	 */
	protected boolean yieldsInsertionPoint(final IMergeViewerItem parent, Iterable<? extends Diff> diffs,
			final IMergeViewerItemProviderConfiguration configuration) {
		final MergeViewerSide side = parent.getSide();
		final Comparison comparison = configuration.getComparison();
		final AdapterFactory adapterFactory = configuration.getAdapterFactory();
		List<Object> sideContent = getChildrenFromContentProvider(getSideValue(parent, side), adapterFactory);
		List<Object> oppositeContent = getChildrenFromContentProvider(getSideValue(parent, side.opposite()),
				adapterFactory);
		if (sideContent.isEmpty() && oppositeContent.isEmpty()) {
			return false;
		}
		return Iterables.any(diffs, new Predicate<Diff>() {
			public boolean apply(Diff diff) {
				if (isPseudoAddConflict(diff)) {
					return false;
				}

				EObject value = (EObject)MergeViewerUtil.getDiffValue(diff);
				Match match = comparison.getMatch(value);
				if (isAddOnOppositeSide(diff, side) || isDeleteOnSameSide(diff, side)
						|| isInsertOnBothSides(diff, match)) {
					if (match == null && diff.getState() == DifferenceState.MERGED) {
						EObject bestSideValue = (EObject)getBestSideValue(parent, configuration.getSide());
						match = comparison.getMatch(bestSideValue);
						match = getMatchWithNullValues(match);
					}

					return match != null && !isRealAddConflict(diff, match, side);
				}

				return false;
			}
		});
	}

	/**
	 * Indicates whether for the given values at least one {@link IMergeViewerItem} should be created.
	 * 
	 * @param comparison
	 *            the {@link Comparison}.
	 * @param diff
	 *            the {@link Diff}.
	 * @param values
	 *            the values.
	 * @return {@code true} if at least one {@link IMergeViewerItem} should be created, {@code false}
	 *         otherwise.
	 */
	protected boolean yieldsMergeViewerItem(Comparison comparison, Diff diff, Collection<?> values) {
		Iterable<EObject> elements = filter(values, EObject.class);
		if (diff != null && !Iterables.isEmpty(elements)) {
			return true;
		}

		for (EObject element : elements) {
			Match match = comparison.getMatch(element);
			if (match != null && !isMatchWithAllProxyData(match)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Indicates whether the match has not loaded fragments.
	 * 
	 * @param match
	 *            the {@link Match} to check.
	 * @param side
	 *            the {@link MergeViewerSide} to check.
	 * @return {@code true} if there are not loaded fragments, {@code false} otherwise.
	 */
	private boolean hasNotLoadedFragmentsItems(Match match, MergeViewerSide side) {
		Collection<Match> childrenMatches = ResourceUIUtil
				.getChildrenMatchWithNotLoadedParent(match.getComparison(), match, side);
		return !childrenMatches.isEmpty();
	}

	/**
	 * Get the parent (as MergeViewerItem) of the MergeViewerItem in case the MergeViewerItem is a
	 * {@link org.eclipse.emf.ecore.resource.Resource}.
	 * 
	 * @param mergeViewerItem
	 *            the {@link IMergeViewerItem} for which the parent is created.
	 * @param resource
	 *            the Resource for which we want the parent (as MergeViewerItem).
	 * @param configuration
	 *            the {@link IMergeViewerItemProviderConfiguration}.
	 * @return the parent (a MergeViewerItem) of the given element.
	 */
	private IMergeViewerItem createParent(IMergeViewerItem mergeViewerItem, Resource resource,
			IMergeViewerItemProviderConfiguration configuration) {
		final IMergeViewerItem parent;
		URI uri = resource.getURI();
		if (ResourceUIUtil.isFragment(uri)) {
			final Object object = getBestSideValue(mergeViewerItem, configuration.getSide());
			final Match matchOfValue = configuration.getComparison().getMatch((EObject)object);
			final NotLoadedFragmentMatch notLoadedFragmentMatch = new NotLoadedFragmentMatch(matchOfValue);
			parent = createMergeViewerItem(configuration.getComparison(), mergeViewerItem.getDiff(),
					notLoadedFragmentMatch, notLoadedFragmentMatch, notLoadedFragmentMatch,
					mergeViewerItem.getSide(), configuration.getAdapterFactory());
		} else {
			parent = null;
		}
		return parent;
	}

	/**
	 * Get the parent of the MergeViewerItem in case the MergeViewerItem is a
	 * {@link org.eclipse.emf.compare.match.impl.NotLoadedFragmentMatch}.
	 * 
	 * @param mergeViewerItem
	 *            the {@link IMergeViewerItem}.
	 * @param nlfm
	 *            the NotLoadedFragmentMatch for which we want the parent (as MergeViewerItem)
	 * @param configuration
	 *            the {@link IMergeViewerItemProviderConfiguration}.l
	 * @return the parent (@link IMergeViewerItem) of the given element.
	 */
	private IMergeViewerItem createParent(IMergeViewerItem mergeViewerItem, NotLoadedFragmentMatch nlfm,
			IMergeViewerItemProviderConfiguration configuration) {
		IMergeViewerItem parent = null;
		MergeViewerSide side = mergeViewerItem.getSide();
		final Collection<? extends Match> children = nlfm.getChildren();
		for (Match match : children) {
			URI uri = ResourceUIUtil.getDataURI(match, side);
			if (uri != null) {
				IGraphView<URI> graph = ResourceUIUtil.getResourcesURIGraph();
				URI parentData = graph.getParentData(uri);
				ResourceSet rs = ResourceUIUtil.getDataResourceSet(match, side);
				Resource resourceParent = ResourceUIUtil.getParent(rs, uri);
				while (resourceParent == null && parentData != null) {
					resourceParent = ResourceUIUtil.getParent(rs, parentData.trimFragment());
					parentData = graph.getParentData(parentData.trimFragment());
				}
				if (resourceParent != null && parentData != null) {
					EObject eObjectParent = resourceParent.getEObject(parentData.fragment());
					if (eObjectParent != null) {
						parent = createBasicMergeViewerItem(eObjectParent, side, configuration);
						break;
					}
				} else {
					parent = createNotLoadedFragmentContainer(rs, uri, configuration.getComparison(),
							mergeViewerItem.getDiff(), side, configuration.getAdapterFactory());
					if (parent != null) {
						break;
					}
				}
			}
		}
		return parent;
	}

	/**
	 * Create an IMergeViewerItem.Container that holds NotLoadedFragmentMatches.
	 * 
	 * @param uri
	 *            the URI of the Match element for which we want to create a container.
	 * @param side
	 * @param diff
	 * @return an IMergeViewerItem.Container.
	 */
	private IMergeViewerItem createNotLoadedFragmentContainer(ResourceSet rs, URI uri, Comparison comparison,
			Diff diff, MergeViewerSide side, AdapterFactory adapterFactory) {
		final IMergeViewerItem parent;
		URI parentURI = ResourceUIUtil.getParentResourceURI(rs, uri);
		URI rootResourceURI = ResourceUIUtil.getRootResourceURI(uri);
		if (parentURI == null) {
			parentURI = rootResourceURI;
		}
		Collection<Match> notLoadedFragmentMatches = Lists.newArrayList();
		Collection<Match> rootMatches = comparison.getMatches();
		Collection<URI> uris = ResourceUIUtil.getDataURIs(rootMatches, side);
		for (Match rootMatch : rootMatches) {
			URI rootMatchDataURI = ResourceUIUtil.getDataURI(rootMatch, side);
			if (!rootResourceURI.equals(rootMatchDataURI) && !parentURI.equals(rootMatchDataURI)
					&& ResourceUIUtil.isChildOf(rootMatchDataURI, ImmutableSet.of(parentURI))
					&& !ResourceUIUtil.isChildOf(rootMatchDataURI, uris)) {
				notLoadedFragmentMatches.add(new NotLoadedFragmentMatch(rootMatch));
			}
		}
		if (notLoadedFragmentMatches.size() > 1) {
			final NotLoadedFragmentMatch notLoadedFragmentMatch = new NotLoadedFragmentMatch(
					notLoadedFragmentMatches);
			parent = createMergeViewerItem(comparison, diff, notLoadedFragmentMatch, notLoadedFragmentMatch,
					notLoadedFragmentMatch, side, adapterFactory);
		} else {
			parent = null;
		}
		return parent;
	}

	/**
	 * Creates a 'basic' {@link IMergeViewerItem}.
	 * 
	 * @param eObject
	 *            the {@link EObject} for which to create an {@link IMergeViewerItem}.
	 * @param side
	 *            the {@link MergeViewerSide}.
	 * @param configuration
	 *            the {@link IMergeViewerItemProviderConfiguration}.
	 * @return the created {@link IMergeViewerItem}.
	 */
	private IMergeViewerItem createBasicMergeViewerItem(EObject eObject, MergeViewerSide side,
			IMergeViewerItemProviderConfiguration configuration) {
		IMergeViewerItem ret = null;
		Comparison comparison = configuration.getComparison();
		Match match = comparison.getMatch(eObject);
		if (match == null) {
			return null;
		}
		EObject expectedValue = MergeViewerUtil.getEObject(match, side);
		if (expectedValue != null) {
			Iterable<? extends Diff> diffs = getDiffsWithValue(expectedValue, side, match, configuration);
			Diff diff = getFirst(diffs, null);
			ret = createMergeViewerItem(comparison, diff, match, side, configuration.getAdapterFactory());

		} else {
			expectedValue = MergeViewerUtil.getEObject(match, side.opposite());
			Iterable<? extends Diff> diffs = Lists.newArrayList();
			if (expectedValue != null) {
				diffs = getDiffsWithValue(expectedValue, side, match, configuration);
			}
			if (isEmpty(diffs)) {
				expectedValue = MergeViewerUtil.getEObject(match, MergeViewerSide.ANCESTOR);
				if (expectedValue != null) {
					diffs = getDiffsWithValue(expectedValue, side, match, configuration);
				}
			}

			if (!isEmpty(diffs)) {
				Diff diff = getFirst(diffs, null);
				if (diff instanceof ResourceAttachmentChange) {
					ret = createMergeViewerItem(comparison, diff, match, side,
							configuration.getAdapterFactory());
				} else {
					ret = createInsertionPoint(comparison, diff, side, configuration.getAdapterFactory());
				}
			}
		}
		return ret;
	}

	/**
	 * Creates an insertion point.
	 * 
	 * @param comparison
	 *            the {@link Comparison}.
	 * @param diff
	 *            the {@link Diff}.
	 * @param side
	 *            the {@link MergeViewerSide}.
	 * @param adapterFactory
	 *            the {@link AdapterFactory}.
	 * @return the newly created insertion point.
	 */
	private IMergeViewerItem createInsertionPoint(Comparison comparison, Diff diff, MergeViewerSide side,
			AdapterFactory adapterFactory) {
		Object left = MergeViewerUtil.getValueFromDiff(diff, MergeViewerSide.LEFT);
		Object right = MergeViewerUtil.getValueFromDiff(diff, MergeViewerSide.RIGHT);

		IMergeViewerItem insertionPoint = null;
		if (left == null && right == null) {
			// Do not display anything
		} else {
			final boolean leftEmptyBox = side == MergeViewerSide.LEFT
					&& (left == null || !MergeViewerUtil.getValues(diff, side).contains(left));
			final boolean rightEmptyBox = side == MergeViewerSide.RIGHT
					&& (right == null || !MergeViewerUtil.getValues(diff, side).contains(right));
			if (leftEmptyBox || rightEmptyBox) {
				Object ancestor = MergeViewerUtil.getValueFromDiff(diff, MergeViewerSide.ANCESTOR);

				insertionPoint = createMergeViewerItem(comparison, diff, left, right, ancestor, side,
						adapterFactory);
			}
		}
		return insertionPoint;
	}

	/**
	 * After merging a diff which will lead to have an insertion point on both sides, the match associated
	 * with this diff will be unreacheable because its left and right sides will be null. This method will
	 * find this match.
	 * 
	 * @param match
	 *            the given match.
	 * @return the match associated with the given merged diff.
	 */
	private Match getMatchWithNullValues(Match match) {
		for (Match subMatch : match.getSubmatches()) {
			if (subMatch.getLeft() == null && subMatch.getRight() == null) {
				return subMatch;
			}
		}
		return null;
	}

	/**
	 * Indicates whether the diff is merged and the match does not match anymore.
	 * 
	 * @param diff
	 *            the {@link Diff} to check.
	 * @param match
	 *            {@link Match} to check.
	 * @return {@code true} if the diff is merged and the match does not exist or has no more left and right,
	 *         {@code false} otherwise.
	 */
	private boolean isInsertOnBothSides(Diff diff, Match match) {
		return diff.getState() == DifferenceState.MERGED
				&& (match == null || (match.getLeft() == null && match.getRight() == null));
	}

	/**
	 * Indicates whether the diff is of kind ADD in a pseudo conflict.
	 * 
	 * @param diff
	 *            the {@link Diff} to check.
	 * @return {@code true} if the diff is of kind ADD in a pseudo conflict, {@code false} otherwise.
	 */
	private boolean isPseudoAddConflict(Diff diff) {
		Conflict conflict = diff.getConflict();
		return conflict != null && conflict.getKind() == ConflictKind.PSEUDO
				&& diff.getKind() == DifferenceKind.ADD;
	}

	/**
	 * Indicates whether the given diff is in real conflict.
	 * 
	 * @param diff
	 *            the {@link Diff} to check.
	 * @param match
	 *            the {@link Match} the diff operates on.
	 * @param side
	 *            the {@link MergeViewerSide}.
	 * @return {@code true} if the diff is a real conflict on the match, {@code false} otherwise.
	 */
	private boolean isRealAddConflict(Diff diff, Match match, MergeViewerSide side) {
		// Real add conflict (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=442898)
		return match.getRight() != null && match.getLeft() != null && isAddOnOppositeSide(diff, side);
	}

	/**
	 * Indicates whether the diff is an add on the opposite side as the given side.
	 * 
	 * @param diff
	 *            the {@link Diff}.
	 * @param side
	 *            the {@link MergeViewerSide}.
	 * @return {@code true} if the diff is an add on the opposite side as the given side, {@code false}
	 *         otherwise.
	 */
	private boolean isAddOnOppositeSide(Diff diff, MergeViewerSide side) {
		if (diff.getState() != DifferenceState.MERGED && diff.getKind() == DifferenceKind.ADD) {
			DifferenceSource source = diff.getSource();
			return (source == DifferenceSource.LEFT && side == MergeViewerSide.RIGHT)
					|| (source == DifferenceSource.RIGHT && side == MergeViewerSide.LEFT);
		}
		return false;
	}

	/**
	 * Indicates whether the diff is a delete on the same side as the given one.
	 * 
	 * @param diff
	 *            the {@link Diff}.
	 * @param side
	 *            the {@link MergeViewerSide}.
	 * @return {@code true} if the diff is a delete on the same side as the given side, {@code false}
	 *         otherwise.
	 */
	private boolean isDeleteOnSameSide(Diff diff, MergeViewerSide side) {
		if (diff.getState() != DifferenceState.MERGED && diff.getKind() == DifferenceKind.DELETE) {
			DifferenceSource source = diff.getSource();
			return (source == DifferenceSource.LEFT && side == MergeViewerSide.LEFT)
					|| (source == DifferenceSource.RIGHT && side == MergeViewerSide.RIGHT);
		}

		return false;
	}

	/**
	 * Return an Iterable of {@link Diff} which are visible and linked to the given expectedValue. Try to get
	 * containment reference changes first, then if empty, try to get resource attachment changes.
	 * 
	 * @param expectedValue
	 *            the expected value
	 * @param side
	 *            {@link MergeViewerSide}.
	 * @param parentMatch
	 *            the {@link Match} parent.
	 * @param configuration
	 *            the {@link IMergeViewerItemProviderConfiguration}.
	 * @return an iterable of diffs.
	 */
	private Iterable<? extends Diff> getDiffsWithValue(EObject expectedValue, MergeViewerSide side,
			Match parentMatch, IMergeViewerItemProviderConfiguration configuration) {
		Iterable<? extends Diff> diffs = getVisibleContainmentDiffs(expectedValue, configuration);
		if (size(diffs) > 1 && side != MergeViewerSide.ANCESTOR) {
			diffs = filter(diffs, fromSide(side.convertToDifferenceSource()));
		}

		Diff diff = getFirst(diffs, null);
		if (diff == null) {
			diffs = filter(parentMatch.getDifferences(), instanceOf(ResourceAttachmentChange.class));
		}
		return diffs;
	}

	/**
	 * Determines all differences regarding the given {@code object} which are related to a containment
	 * reference change and are visible in the viewer.
	 * 
	 * @param object
	 *            the {@link EObject}.
	 * @param configuration
	 *            the {@link IMergeViewerItemProviderConfiguration}.
	 * @return all visible diffs regarding {@code object} related to containment differences.
	 */
	protected Iterable<? extends Diff> getVisibleContainmentDiffs(EObject object,
			IMergeViewerItemProviderConfiguration configuration) {
		List<Diff> differences = configuration.getComparison().getDifferences(object);
		return filter(differences, visibleContainmentDiffPredicate(configuration));
	}

	/**
	 * Get a non-null side of the the given {@link IMergeViewerItem}, preferring but not limited to the given
	 * side.
	 * 
	 * @param mergeViewerItem
	 *            the {@link IMergeViewerItem}.
	 * @param side
	 *            {@link MergeViewerSide}
	 * @return the side object. May be null if all sides are null.
	 */
	protected Object getBestSideValue(IMergeViewerItem mergeViewerItem, MergeViewerSide side) {
		Object sideValue;
		if (side != MergeViewerSide.ANCESTOR) {
			sideValue = getSideValue(mergeViewerItem, side);
			if (sideValue == null) {
				sideValue = getSideValue(mergeViewerItem, side.opposite());
				if (sideValue == null) {
					sideValue = getSideValue(mergeViewerItem, MergeViewerSide.ANCESTOR);
				}
			}
		} else {
			sideValue = getSideValue(mergeViewerItem, MergeViewerSide.ANCESTOR);
			if (sideValue == null) {
				sideValue = getSideValue(mergeViewerItem, MergeViewerSide.LEFT);
				if (sideValue == null) {
					sideValue = getSideValue(mergeViewerItem, MergeViewerSide.RIGHT);
				}
			}
		}
		return sideValue;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param mergeViewerItem
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.item.ide.ui.internal.contentmergeviewer.IMergeViewerItem#getSideValue(org.eclipse.emf.compare.rcp.ui.mergeviewer.ide.ui.internal.contentmergeviewer.IMergeViewer.MergeViewerSide)
	 */
	protected Object getSideValue(IMergeViewerItem mergeViewerItem, MergeViewerSide side) {
		switch (side) {
			case LEFT:
				return mergeViewerItem.getLeft();
			case RIGHT:
				return mergeViewerItem.getRight();
			case ANCESTOR:
				return mergeViewerItem.getAncestor();
			default:
				throw new IllegalStateException(); // happy compiler :)
		}
	}

	/**
	 * Create an IMergeViewerItem for the parent of the given {@link ResourceAttachmentChange}.
	 * 
	 * @param diff
	 *            the given {@link ResourceAttachmentChange}.
	 * @return an IMergeViewerItem.
	 */
	protected IMergeViewerItem createBasicContainer(ResourceAttachmentChange diff,
			IMergeViewerItem mergeViewerItem, AdapterFactory adapterFactory) {
		final Comparison comparison = diff.getMatch().getComparison();
		MergeViewerSide side = mergeViewerItem.getSide();
		Resource left = MergeViewerUtil.getResource(comparison, MergeViewerSide.LEFT, diff);
		Resource right = MergeViewerUtil.getResource(comparison, MergeViewerSide.RIGHT, diff);
		Resource ancestor = MergeViewerUtil.getResource(comparison, MergeViewerSide.ANCESTOR, diff);
		IMergeViewerItem ret = new ResourceAttachmentChangeMergeViewerItem(comparison, null, left, right,
				ancestor, side, adapterFactory);
		return ret;
	}

	/**
	 * Return potential NotLoadedFragment children items (as MergeViewerItem) of the MergeViewerItem in case
	 * the MergeViewerItem is a {@link org.eclipse.emf.compare.match.Match}.
	 * 
	 * @param match
	 *            the Match for which we want the potential NotLoadedFragment children items (as
	 *            MergeViewerItems)
	 * @return the NotLoadedFragment children items (a list of MergeViewerItems) of the given element.
	 */
	private List<IMergeViewerItem> getNotLoadedFragmentsItems(Comparison comparison, Match match,
			MergeViewerSide side, AdapterFactory adapterFactory) {
		final List<IMergeViewerItem> ret = newArrayList();
		final Collection<Match> childrenMatches = ResourceUIUtil
				.getChildrenMatchWithNotLoadedParent(comparison, match, side);
		if (childrenMatches.size() > 0) {
			boolean setNames = childrenMatches.size() > 1;
			for (Match child : childrenMatches) {
				NotLoadedFragmentMatch notLoadedFragmentMatch = new NotLoadedFragmentMatch(child);
				if (setNames) {
					notLoadedFragmentMatch.setName(ResourceUIUtil.getResourceName(notLoadedFragmentMatch));
				}
				IMergeViewerItem notLoadedFragmentItem = createMergeViewerItem(comparison, null,
						notLoadedFragmentMatch, notLoadedFragmentMatch, notLoadedFragmentMatch, side,
						adapterFactory);
				ret.add(notLoadedFragmentItem);
			}
		}
		return ret;
	}

	/**
	 * Predicate for checking if the given {@code diff} is visible and itself (or any of its 'refining' diffs)
	 * is a containment reference change.
	 * 
	 * @param configuration
	 *            the {@link IMergeViewerItemProviderConfiguration}.
	 * @return the {@link Predicate}.
	 */
	protected Predicate<Diff> visibleContainmentDiffPredicate(
			IMergeViewerItemProviderConfiguration configuration) {
		return and(or(CONTAINMENT_REFERENCE_CHANGE, anyRefining(CONTAINMENT_REFERENCE_CHANGE)),
				visibleInMergeViewerPredicate(configuration.getDifferenceFilterPredicate(),
						configuration.getDifferenceGroupProvider()));
	}

	/**
	 * Predicate for checking if the given {@code diff} is visible in the merge viewer.
	 * 
	 * @param predicate
	 *            the filtering {@link Predicate}.
	 * @param groupProvider
	 *            the active {@link IDifferenceGroupProvider}.
	 * @return the {@link Predicate}.
	 */
	protected Predicate<Diff> visibleInMergeViewerPredicate(final Predicate<? super EObject> predicate,
			final IDifferenceGroupProvider groupProvider) {
		if (predicate == null) {
			return Predicates.alwaysTrue();
		}

		// checks whether the diff itself or at least one of its refinements is visible
		return new Predicate<Diff>() {
			public boolean apply(Diff diff) {
				return MergeViewerUtil.isVisibleInMergeViewer(diff, groupProvider, predicate);
			}
		};
	}

	/**
	 * Creates the Merge Viewer Items for the given {@code values}.
	 * 
	 * @param values
	 *            the object for which {@link IMergeViewerItem}s shall be created.
	 * @param parent
	 *            the {@link IMergeViewerItem} parent of the children to create
	 * @param configuration
	 *            the {@link IMergeViewerItemProviderConfiguration}.
	 * @return
	 */
	protected List<IMergeViewerItem> createMergeViewerItemsFrom(Collection<?> values, IMergeViewerItem parent,
			IMergeViewerItemProviderConfiguration configuration) {
		List<IMergeViewerItem> ret = newArrayListWithCapacity(values.size());
		for (EObject value : filter(values, EObject.class)) {
			Match match = configuration.getComparison().getMatch(value);
			if ((match != null && !isMatchWithAllProxyData(match))) {
				IMergeViewerItem valueToAdd = createMergeViewerItemFrom(value, parent, configuration);
				if (valueToAdd != null) {
					ret.add(valueToAdd);
				}
			}
		}
		return ret;
	}

	/**
	 * Check if the given match holds a proxy on each side.
	 * 
	 * @param match
	 *            the given match.
	 * @return true if the given match holds a proxy on each side, false otherwise.
	 */
	private boolean isMatchWithAllProxyData(Match match) {
		boolean proxy = false;
		EObject left = match.getLeft();
		EObject right = match.getRight();
		EObject origin = match.getOrigin();
		if (left != null && right != null) {
			if (left.eIsProxy() && right.eIsProxy()) {
				proxy = true;
			}
		}
		if (proxy == true && match.getComparison().isThreeWay() && (origin == null || !origin.eIsProxy())) {
			proxy = false;
		}
		return proxy;
	}

	/**
	 * Creates an IMergeViewerItem from an EObject.
	 * 
	 * @param eObject
	 *            the given eObject.
	 * @param parent
	 *            the {@link IMergeViewerItem} parent of the child to create.
	 * @param configuration
	 *            the {@link IMergeViewerItemProviderConfiguration}.
	 * @return an IMergeViewerItem.
	 */
	protected IMergeViewerItem createMergeViewerItemFrom(EObject eObject, IMergeViewerItem parent,
			IMergeViewerItemProviderConfiguration configuration) {
		final Comparison comparison = configuration.getComparison();
		final AdapterFactory adapterFactory = configuration.getAdapterFactory();
		final Match match = comparison.getMatch(eObject);
		final MergeViewerSide side = parent.getSide();
		final Diff diff = getFirst(getVisibleContainmentDiffs(eObject, configuration), null);
		if (match != null) {
			return createMergeViewerItem(comparison, diff, match, side, adapterFactory);
		} else {
			switch (side) {
				case LEFT:
					return createMergeViewerItem(comparison, diff, eObject, null, null, side, adapterFactory);
				case RIGHT:
					return createMergeViewerItem(comparison, diff, null, eObject, null, side, adapterFactory);
				case ANCESTOR:
					return createMergeViewerItem(comparison, diff, null, null, eObject, side, adapterFactory);
				default:
					throw new IllegalStateException();
			}
		}
	}

	/**
	 * Creates the {@link IMergeViewerItem} from the given data.
	 * 
	 * @param comparison
	 *            the {@link Comparison}.
	 * @param diff
	 *            the {@link Diff}. May be null.
	 * @param left
	 *            the left object. May be null. (One of left, right, ancestor should be non-null).
	 * @param right
	 *            the right object. May be null. (One of left, right, ancestor should be non-null).
	 * @param ancestor
	 *            the ancestor object. May be null. (One of left, right, ancestor should be non-null).
	 * @param side
	 *            the {@link MergeViewerSide}.
	 * @param adapterFactory
	 *            the {@link AdapterFactory}.
	 * @return the created {@link IMergeViewerItem}.
	 */
	protected IMergeViewerItem createMergeViewerItem(Comparison comparison, Diff diff, Object left,
			Object right, Object ancestor, MergeViewerSide side, AdapterFactory adapterFactory) {
		return new MergeViewerItem(comparison, diff, left, right, ancestor, side, adapterFactory);
	}

	/**
	 * Creates the {@link IMergeViewerItem} from the given data.
	 * 
	 * @param comparison
	 *            the {@link Comparison}.
	 * @param diff
	 *            the {@link Diff}. May be null.
	 * @param match
	 *            the {@link Match}.
	 * @param side
	 *            the {@link MergeViewerSide}.
	 * @param adapterFactory
	 *            the {@link AdapterFactory}.
	 * @return the created {@link IMergeViewerItem}.
	 */
	protected IMergeViewerItem createMergeViewerItem(Comparison comparison, Diff diff, Match match,
			MergeViewerSide side, AdapterFactory adapterFactory) {
		return createMergeViewerItem(comparison, diff, match.getLeft(), match.getRight(), match.getOrigin(),
				side, adapterFactory);
	}
}

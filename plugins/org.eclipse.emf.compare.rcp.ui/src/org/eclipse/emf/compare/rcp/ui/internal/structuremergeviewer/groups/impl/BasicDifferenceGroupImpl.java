/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Iterators.any;
import static com.google.common.collect.Iterators.concat;
import static com.google.common.collect.Iterators.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.CONTAINMENT_REFERENCE_CHANGE;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasState;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
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
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.match.impl.NotLoadedFragmentMatch;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.emf.compare.rcp.ui.internal.util.ResourceUIUtil;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.extender.IDifferenceGroupExtender;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.edit.tree.TreeFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.swt.graphics.Image;

/**
 * This implementation of a {@link IDifferenceGroup} uses a predicate to filter the whole list of differences.
 * <p>
 * This can be subclasses or used directly instead of {@link IDifferenceGroup}.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 4.0
 */
public class BasicDifferenceGroupImpl extends AdapterImpl implements IDifferenceGroup {

	/**
	 * Function that returns all contents of the given EObject.
	 */
	protected static final Function<EObject, Iterator<EObject>> E_ALL_CONTENTS = new Function<EObject, Iterator<EObject>>() {
		public Iterator<EObject> apply(EObject eObject) {
			return eObject.eAllContents();
		}
	};

	/** The filter we'll use in order to filter the differences that are part of this group. */
	protected final Predicate<? super Diff> filter;

	/** The name that the EMF Compare UI will display for this group. */
	protected final String name;

	/** The icon that the EMF Compare UI will display for this group. */
	protected final Image image;

	/** The list of children of this group. */
	protected List<TreeNode> children;

	/** The list of already processed refined diffs. */
	protected Set<Diff> extensionDiffProcessed;

	/** The comparison that is the parent of this group. */
	private final Comparison comparison;

	/** The registry of difference group extenders. */
	private final IDifferenceGroupExtender.Registry registry = EMFCompareRCPUIPlugin.getDefault()
			.getDifferenceGroupExtenderRegistry();

	/** The cross reference adapter that will be added to this group's children. */
	private final ECrossReferenceAdapter crossReferenceAdapter;

	/**
	 * Instantiates this group given the comparison and filter that should be used in order to determine its
	 * list of differences.
	 * <p>
	 * This will use the default name and icon for the group.
	 * </p>
	 * 
	 * @param comparison
	 *            The comparison that is the parent of this group.
	 * @param filter
	 *            The filter we'll use in order to filter the differences that are part of this group.
	 * @param crossReferenceAdapter
	 *            The cross reference adapter that will be added to this group's children.
	 */
	public BasicDifferenceGroupImpl(Comparison comparison, Predicate<? super Diff> filter,
			ECrossReferenceAdapter crossReferenceAdapter) {
		this(comparison, filter,
				EMFCompareRCPUIMessages.getString("BasicDifferenceGroup.name"), EMFCompareRCPUIPlugin //$NON-NLS-1$
						.getImage("icons/full/toolb16/group.gif"), crossReferenceAdapter); //$NON-NLS-1$
	}

	/**
	 * Instantiates this group given the comparison and filter that should be used in order to determine its
	 * list of differences. It will be displayed in the UI with the default icon and the given name.
	 * 
	 * @param comparison
	 *            The comparison that is the parent of this group.
	 * @param filter
	 *            The filter we'll use in order to filter the differences that are part of this group.
	 * @param name
	 *            The name that the EMF Compare UI will display for this group.
	 * @param crossReferenceAdapter
	 *            The cross reference adapter that will be added to this group's children.
	 */
	public BasicDifferenceGroupImpl(Comparison comparison, Predicate<? super Diff> filter, String name,
			ECrossReferenceAdapter crossReferenceAdapter) {
		this(comparison, filter, name, EMFCompareRCPUIPlugin.getImage("icons/full/toolb16/group.gif"), //$NON-NLS-1$
				crossReferenceAdapter);
	}

	/**
	 * Instantiates this group given the comparison and filter that should be used in order to determine its
	 * list of differences. It will be displayed in the UI with the given icon and name.
	 * 
	 * @param comparison
	 *            The comparison that is the parent of this group.
	 * @param filter
	 *            The filter we'll use in order to filter the differences that are part of this group.
	 * @param name
	 *            The name that the EMF Compare UI will display for this group.
	 * @param image
	 *            The icon that the EMF Compare UI will display for this group.
	 * @param crossReferenceAdapter
	 *            Updated upstream The cross reference adapter that will be added to this group's children.
	 */
	public BasicDifferenceGroupImpl(Comparison comparison, Predicate<? super Diff> filter, String name,
			Image image, ECrossReferenceAdapter crossReferenceAdapter) {
		this.comparison = comparison;
		this.filter = filter;
		this.name = name;
		this.image = image;
		this.crossReferenceAdapter = crossReferenceAdapter;
	}

	/**
	 * Returns the comparison object.
	 * 
	 * @return the comparison object.
	 */
	protected final Comparison getComparison() {
		return comparison;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#isAdapterForType(java.lang.Object)
	 */
	@Override
	public boolean isAdapterForType(Object type) {
		return type == IDifferenceGroup.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup#getStyledName()
	 */
	public IStyledString.IComposedStyledString getStyledName() {
		final IStyledString.IComposedStyledString ret = new ComposedStyledString();
		Iterator<EObject> eAllContents = concat(transform(getChildren().iterator(), E_ALL_CONTENTS));
		Iterator<EObject> eAllData = transform(eAllContents, TREE_NODE_DATA);
		boolean unresolvedDiffs = any(Iterators.filter(eAllData, Diff.class),
				hasState(DifferenceState.UNRESOLVED));
		if (unresolvedDiffs) {
			ret.append("> ", Style.DECORATIONS_STYLER); //$NON-NLS-1$
		}
		ret.append(getName());
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup#getImage()
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup#getChildren()
	 */
	public List<? extends TreeNode> getChildren() {
		if (children == null) {
			buildSubTree();
		}
		return children;
	}

	/**
	 * Registers the CrossReferenceAdapter to all given notifiers.
	 * 
	 * @param notifiers
	 *            the list of notifiers.
	 */
	protected final void registerCrossReferenceAdapter(List<? extends Notifier> notifiers) {
		for (Notifier notifier : notifiers) {
			// this cross referencer has to live as long as the objects on which it is installed.
			notifier.eAdapters().add(crossReferenceAdapter);
		}
	}

	/**
	 * Unregisters the CrossReferenceAdapter from all given notifiers.
	 * 
	 * @param notifiers
	 *            the list of notifiers.
	 */
	protected final void unregisterCrossReferenceAdapter(List<? extends Notifier> notifiers) {
		for (Notifier notifier : notifiers) {
			// this cross referencer has to live as long as the objects on which it is installed.
			notifier.eAdapters().remove(crossReferenceAdapter);
		}
	}

	/**
	 * Build the sub tree of the given {@link MatchResource}.
	 * 
	 * @param matchResource
	 *            the given MatchResource.
	 * @return the sub tree of the given MatchResource.
	 */
	protected TreeNode buildSubTree(MatchResource matchResource,
			Set<ResourceAttachmentChange> attachmentChanges) {
		TreeNode treeNode = wrap(matchResource);
		Collection<ResourceAttachmentChange> filteredChanges = filter(attachmentChanges, filter);
		for (ResourceAttachmentChange attachmentChange : filteredChanges) {
			treeNode.getChildren().add(wrap(attachmentChange));
		}
		return treeNode;
	}

	/**
	 * Build the sub tree of the given {@link Match}.
	 * 
	 * @param parentMatch
	 *            the parent of the given Match.
	 * @param match
	 *            the given Match.
	 * @return the sub tree of the given Match.
	 */
	public List<TreeNode> buildSubTree(Match parentMatch, Match match) {
		return buildSubTree(match, false, ChildrenSide.BOTH);
	}

	public List<TreeNode> buildContainmentSubTree(Match match) {
		return buildSubTree(match, true, ChildrenSide.BOTH);
	}

	/**
	 * Build the sub tree of the given {@link Match}.
	 * 
	 * @param match
	 *            the given Match.
	 * @param containment
	 *            true if the current level represents a containment diff, false otherwise.
	 * @param side
	 *            the accepted side(s) for children of current level.
	 * @return the sub tree of the given Match.
	 */
	protected List<TreeNode> buildSubTree(Match match, boolean containment, ChildrenSide side) {
		final List<TreeNode> ret = Lists.newArrayList();
		final Set<TreeNode> nodeChildren = newLinkedHashSet();
		final Set<Match> matchOfValues = newLinkedHashSet();
		final TreeNode matchTreeNode = wrap(match);

		if (!containment) {
			ret.add(matchTreeNode);
		}

		boolean hasDiff = false;
		for (Diff diff : filter(match.getDifferences(), and(filter, compatibleSide(side)))) {
			if (CONTAINMENT_REFERENCE_CHANGE.apply(diff)) {
				hasDiff = true;
				final TreeNode node;
				if (containment) {
					node = wrap(diff);
					ret.add(node);
				} else {
					node = buildSubTree(diff);
					nodeChildren.add(node);
				}
				Match matchOfValue = match.getComparison().getMatch(((ReferenceChange)diff).getValue());
				if (matchOfValue != null) {
					matchOfValues.add(matchOfValue);
					node.getChildren().addAll(buildSubTree(matchOfValue, true, DIFF_TO_SIDE.apply(diff)));
				}
				if (containment) {
					ret.addAll(manageRefines(diff, side));
				} else {
					nodeChildren.addAll(manageRefines(diff, side));
				}
			} else if (!(diff instanceof ResourceAttachmentChange)) {
				if (diff.getPrimeRefining() != null && extensionDiffProcessed.contains(diff)) {
					continue;
				}
				hasDiff = true;
				if (containment) {
					ret.add(wrap(diff));
				} else {
					nodeChildren.add(buildSubTree(diff));
				}
			}
		}

		Collection<TreeNode> toRemove = Lists.newArrayList();
		for (TreeNode treeNode : ret) {
			boolean hasNonEmptySubMatch = false;
			// SubMatches first
			for (Match subMatch : Sets.difference(newLinkedHashSet(match.getSubmatches()), matchOfValues)) {
				List<TreeNode> buildSubTree = buildSubTree(subMatch, containment, ChildrenSide.BOTH);
				if (!buildSubTree.isEmpty()) {
					hasNonEmptySubMatch = true;
					treeNode.getChildren().addAll(buildSubTree);
				}
			}
			// Differences last
			treeNode.getChildren().addAll(nodeChildren);
			if (!(containment || hasDiff || hasNonEmptySubMatch || filter.equals(Predicates.alwaysTrue()))) {
				toRemove.add(treeNode);
			} else if (!containment && isMatchWithOnlyResourceAttachmentChanges(match)) {
				toRemove.add(treeNode);
			} else if (isMatchWithProxyData(match)) {
				toRemove.add(treeNode);
			} else {
				for (IDifferenceGroupExtender ext : registry.getExtenders()) {
					if (ext.handle(treeNode)) {
						ext.addChildren(treeNode);
					}
				}
			}
		}

		ret.removeAll(toRemove);

		return ret;
	}

	/**
	 * Check if the given match holds a proxy.
	 * 
	 * @param match
	 *            the given match
	 * @return true if the given match holds a proxy, false otherwise.
	 */
	private boolean isMatchWithProxyData(Match match) {
		boolean proxy = false;
		if (match.getLeft() != null) {
			if (match.getLeft().eIsProxy()) {
				proxy = true;
			}
		} else if (match.getRight() != null) {
			if (match.getRight().eIsProxy()) {
				proxy = true;
			}
		} else if (match.getOrigin() != null) {
			if (match.getOrigin().eIsProxy()) {
				proxy = true;
			}
		}
		return proxy;
	}

	/**
	 * Manage the addition of refines diffs of the given Diff.
	 * 
	 * @param diff
	 *            the given Diff.
	 * @param side
	 *            the accepted side(s) for children of current level.
	 * @return the sub tree of refines diffs.
	 */
	private List<TreeNode> manageRefines(Diff diff, ChildrenSide side) {
		final List<TreeNode> ret = Lists.newArrayList();
		final EList<Diff> refines = diff.getRefines();
		for (Diff refine : refines) {
			Diff mainDiff = refine.getPrimeRefining();
			if (mainDiff != null && mainDiff == diff && and(filter, compatibleSide(side)).apply(refine)) {
				TreeNode refineSubTree = buildSubTree(refine);
				ret.add(refineSubTree);
				extensionDiffProcessed.add(refine);
			}
		}
		return ret;
	}

	/**
	 * Build the sub tree for the given {@link Diff}.
	 * 
	 * @param diff
	 *            the given diff.
	 * @return the sub tree of the given diff.
	 */
	private TreeNode buildSubTree(Diff diff) {
		TreeNode treeNode = wrap(diff);
		for (IDifferenceGroupExtender ext : registry.getExtenders()) {
			if (ext.handle(treeNode)) {
				ext.addChildren(treeNode);
			}
		}
		return treeNode;
	}

	/**
	 * Creates a TreeNode form the given EObject.
	 * 
	 * @param data
	 *            the given EObject.
	 * @return a TreeNode.
	 */
	protected TreeNode wrap(EObject data) {
		TreeNode treeNode = TreeFactory.eINSTANCE.createTreeNode();
		treeNode.setData(data);
		treeNode.eAdapters().add(this);
		return treeNode;
	}

	/**
	 * Checks if the given {@link Match} contains only differences of type {@link ResourceAttachmentChange}.
	 * 
	 * @param match
	 *            the given Match.
	 * @return true, if the given Match contains only differences of type ResourceAttachmentChange.
	 */
	private boolean isMatchWithOnlyResourceAttachmentChanges(Match match) {
		boolean ret = false;
		Iterable<Diff> allDifferences = match.getAllDifferences();
		if (Iterables.isEmpty(allDifferences)) {
			ret = false;
		} else if (Iterables.all(allDifferences, instanceOf(ResourceAttachmentChange.class))) {
			if (match.getSubmatches() == null || match.getSubmatches().isEmpty()) {
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup#dispose()
	 */
	public void dispose() {
		if (children != null) {
			unregisterCrossReferenceAdapter(children);
			children = null;
		}
	}

	/**
	 * An enum that represents, for a given diff, the accepted side(s) for its children and provides utilty
	 * methods to manage sides.
	 * 
	 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
	 * @since 4.0
	 */
	protected enum ChildrenSide {
		/** Values: both sides, left side. */
		BOTH, LEFT, RIGHT;

		public static ChildrenSide getValueFrom(DifferenceSource source) {
			switch (source) {
				case LEFT:
					return LEFT;
				case RIGHT:
					return RIGHT;
				default:
					return BOTH;
			}
		}
	}

	/**
	 * Get the accepted side(s) for children of a given Diff.
	 */
	private static final Function<Diff, ChildrenSide> DIFF_TO_SIDE = new Function<Diff, ChildrenSide>() {
		public ChildrenSide apply(Diff diff) {
			final ChildrenSide side;
			if (diff != null) {
				final Conflict c = diff.getConflict();
				if (c != null
						&& or(hasConflict(ConflictKind.PSEUDO),
								and(hasConflict(ConflictKind.REAL), ofKind(DifferenceKind.ADD))).apply(diff)) {
					side = ChildrenSide.getValueFrom(diff.getSource());
				} else {
					side = ChildrenSide.BOTH;
				}
			} else {
				side = ChildrenSide.BOTH;
			}
			return side;
		}
	};

	/**
	 * This can be used to check that a given Diff is compatible with the given side.
	 * 
	 * @param source
	 *            The side for which we accept the given Diff.
	 * @return The created predicate.
	 */
	private static Predicate<? super Diff> compatibleSide(final ChildrenSide side) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				if (input != null && side != ChildrenSide.BOTH) {
					return side == ChildrenSide.getValueFrom(input.getSource());
				} else {
					return side == ChildrenSide.BOTH;
				}
			}
		};
	}

	/**
	 * Builds the sub tree for this group.
	 */
	public void buildSubTree() {
		children = newArrayList();
		extensionDiffProcessed = newLinkedHashSet();
		children.addAll(buildMatchSubTrees());
		children.addAll(buildMatchResourceSubTrees());
		registerCrossReferenceAdapter(children);
	}

	protected List<TreeNode> buildMatchSubTrees() {
		final List<TreeNode> matchSubTrees = new ArrayList<TreeNode>();

		for (Match match : comparison.getMatches()) {
			List<? extends TreeNode> buildSubTree = buildSubTree((Match)null, match);
			if (buildSubTree != null) {
				matchSubTrees.addAll(buildSubTree);
			}
		}

		final List<TreeNode> rootNodes = addNotLoadedFragmentNodes(matchSubTrees);

		return rootNodes;
	}

	protected List<TreeNode> buildMatchResourceSubTrees() {
		final List<TreeNode> matchResourceSubTrees = new ArrayList<TreeNode>();
		if (comparison.getMatchedResources().isEmpty()) {
			return matchResourceSubTrees;
		}

		final Iterable<ResourceAttachmentChange> attachmentChanges = Iterables.filter(comparison
				.getDifferences(), ResourceAttachmentChange.class);

		final Multimap<String, ResourceAttachmentChange> uriToRAC = LinkedHashMultimap.create();
		for (ResourceAttachmentChange attachmentChange : attachmentChanges) {
			uriToRAC.put(attachmentChange.getResourceURI(), attachmentChange);
		}
		for (MatchResource matchResource : comparison.getMatchedResources()) {
			final Collection<ResourceAttachmentChange> leftRAC = uriToRAC.get(matchResource.getLeftURI());
			final Collection<ResourceAttachmentChange> rightRAC = uriToRAC.get(matchResource.getRightURI());
			final Collection<ResourceAttachmentChange> originRAC = uriToRAC.get(matchResource.getOriginURI());
			final LinkedHashSet<ResourceAttachmentChange> racForMatchResource = Sets.newLinkedHashSet();
			racForMatchResource.addAll(leftRAC);
			racForMatchResource.addAll(rightRAC);
			racForMatchResource.addAll(originRAC);

			TreeNode buildSubTree = buildSubTree(matchResource, racForMatchResource);
			if (buildSubTree != null) {
				matchResourceSubTrees.add(buildSubTree);
			}

		}
		return matchResourceSubTrees;
	}

	/**
	 * When a model is split into fragments, and only some of them have changes, the structure merge viewer
	 * (SMV) and the content merge viewers (CMV) display the models involved in the comparison but don’t
	 * display the fragments that have no changes.
	 * <p>
	 * If a change (x) is detected in a fragment (B), and this fragment is a child of another fragment (A)
	 * that has no changes, then (A) won't appear in the SMV and the CMV's. As a result, users will think (B)
	 * is the root of the global model.
	 * </p>
	 * <p>
	 * To avoid this, the idea is to display intermediate node(s) (a.k.a NotLoadedFragmentNodes) in order to
	 * show to users that it exists something (fragments, i.e. a parts of models) between/above the changes.
	 * </p>
	 * This method add these NotLoadedFragmentNodes in the given list of root TreeNodes.
	 * 
	 * @param rootNodes
	 *            the given list of root TreeNodes.
	 * @return a new list of root TreeNodes, completed with NotLoadedFragmentNodes if needed.
	 */
	private List<TreeNode> addNotLoadedFragmentNodes(List<TreeNode> rootNodes) {
		final List<TreeNode> newRootNodes = new ArrayList<TreeNode>(rootNodes);
		for (TreeNode treeNode : rootNodes) {
			EObject data = TREE_NODE_DATA.apply(treeNode);
			if (data instanceof Match) {
				URI uri = ResourceUIUtil.getDataURI((Match)data);
				if (ResourceUIUtil.isFragment(uri)) {
					newRootNodes.remove(treeNode);
					TreeNode notLoadedFragment = addNotLoadedFragment(rootNodes, treeNode, (Match)data, uri);
					if (notLoadedFragment != null) {
						newRootNodes.add(notLoadedFragment);
					}
				}
			}
		}
		// if several root nodes are NotLoadedFragment nodes, add new parent node for these
		// NotLoadedFragmentNodes.
		if (ResourceUIUtil.containsNotLoadedFragmentNodes(newRootNodes)) {
			Collection<TreeNode> nodes = encapsulateNotLoadedFragmentNodes(newRootNodes);
			newRootNodes.clear();
			newRootNodes.addAll(nodes);
		}

		return newRootNodes;
	}

	/**
	 * Encapsulate the given TreeNode under a new NotLoadedFragmentNode.
	 * 
	 * @param rootNodes
	 *            the given list of root TreeNodes.
	 * @param treeNode
	 *            the given TreeNode.
	 * @param match
	 *            the match associated to the given TreeNode.
	 * @param uri
	 *            the data resource's URI of the given match.
	 * @return
	 */
	private TreeNode addNotLoadedFragment(final List<TreeNode> rootNodes, TreeNode treeNode, Match match,
			URI uri) {
		TreeNode newRootNode = null;
		TreeNode notLoadedFragmentNode = createNotLoadedFragmentMatchNode(treeNode, match);
		if (ResourceUIUtil.isFirstLevelFragment(uri)) {
			URI rootURI = ResourceUIUtil.getRootResourceURI(uri);
			if (rootURI != null) {
				// if root uri matches a tree node's data resource, the current treeNode has to be
				// moved under this tree node.
				TreeNode realParent = ResourceUIUtil.getTreeNodeFromURI(rootNodes, rootURI);
				if (realParent != null) {
					realParent.getChildren().add(notLoadedFragmentNode);
				} else {
					newRootNode = notLoadedFragmentNode;
				}
			}
		} else { // Get the first loaded parent object
			ResourceSet rs = ResourceUIUtil.getDataResourceSet(match);
			EObject eObject = ResourceUIUtil.getEObjectParent(rs, uri);
			if (eObject != null) {
				Match newParentMatch = getComparison().getMatch(eObject);
				TreeNode newParentNode = ResourceUIUtil.getTreeNode(rootNodes, newParentMatch);
				if (newParentNode != null) {
					EList<TreeNode> newParentNodeChildren = newParentNode.getChildren();
					newParentNodeChildren.add(notLoadedFragmentNode);
					setNotLoadedFragmentNodesName(newParentNodeChildren);
				} else {
					newRootNode = notLoadedFragmentNode;
				}
			} else {
				newRootNode = notLoadedFragmentNode;
			}
		}
		return newRootNode;
	}

	/**
	 * If the given list of nodes contains at least two nodes with NotLoadedFragmentMatches, then it set the
	 * name of these NotLoadedFragmentMatches.
	 * 
	 * @param nodes
	 *            the given list of nodes.
	 */
	private void setNotLoadedFragmentNodesName(Collection<TreeNode> nodes) {
		if (ResourceUIUtil.containsNotLoadedFragmentNodes(nodes)) {
			for (TreeNode node : nodes) {
				EObject data = TREE_NODE_DATA.apply(node);
				if (data instanceof NotLoadedFragmentMatch) {
					((NotLoadedFragmentMatch)data).setName(ResourceUIUtil
							.getResourceName((NotLoadedFragmentMatch)data));
				}
			}
		}
	}

	/**
	 * Creates a TreeNode that holds a {@link org.eclipse.emf.compare.match.impl.NotLoadedFragmentMatch}. The
	 * holding NotLoadedFragmentMatch will be created by this method and will contains the given Match as a
	 * child. The newly created TreeNode will be the parent of the given TreeNode. The given match must
	 * correspond to the given TreeNode's data.
	 * 
	 * @param node
	 *            the child of the newly created TreeNode.
	 * @param match
	 *            the match that will be the child of the newly created NotLoadedFragmentMatch.
	 * @return the newly created TreeNode.
	 */
	private TreeNode createNotLoadedFragmentMatchNode(TreeNode node, Match match) {
		TreeNode notLoadedFragmentNode = TreeFactory.eINSTANCE.createTreeNode();
		NotLoadedFragmentMatch notLoadedFragmentMatch = new NotLoadedFragmentMatch(match);
		notLoadedFragmentNode.setData(notLoadedFragmentMatch);
		notLoadedFragmentNode.eAdapters().add(this);
		notLoadedFragmentNode.getChildren().add(node);
		return notLoadedFragmentNode;
	}

	/**
	 * For the given list of TreeNodes, encapsulates under a new TreeNode container all TreeNode holding
	 * NotLoadingFragmentMatches.
	 * 
	 * @param nodes
	 *            the initial TreeNodes.
	 * @return the modified TreeNodes.
	 */
	private Collection<TreeNode> encapsulateNotLoadedFragmentNodes(Collection<TreeNode> nodes) {
		final Collection<TreeNode> newNodes = Lists.newArrayList(nodes);
		final Collection<TreeNode> fragmentNodes = Lists.newArrayList();
		TreeNode notLoadedFragmentNode = TreeFactory.eINSTANCE.createTreeNode();
		Collection<Match> matches = new ArrayList<Match>();
		for (TreeNode node : nodes) {
			EObject data = TREE_NODE_DATA.apply(node);
			if (data instanceof NotLoadedFragmentMatch) {
				matches.add((Match)data);
				((NotLoadedFragmentMatch)data).setName(ResourceUIUtil
						.getResourceName((NotLoadedFragmentMatch)data));
				newNodes.remove(node);
				fragmentNodes.add(node);
			}
		}
		NotLoadedFragmentMatch notLoadedFragmentMatch = new NotLoadedFragmentMatch(matches);
		notLoadedFragmentNode.setData(notLoadedFragmentMatch);
		notLoadedFragmentNode.eAdapters().add(this);
		notLoadedFragmentNode.getChildren().addAll(fragmentNodes);
		newNodes.add(notLoadedFragmentNode);
		return newNodes;
	}
}

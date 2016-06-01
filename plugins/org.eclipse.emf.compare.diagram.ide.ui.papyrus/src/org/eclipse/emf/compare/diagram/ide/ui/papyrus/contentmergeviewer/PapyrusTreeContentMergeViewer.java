/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *     Philip Langer - introduce caching
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.papyrus.contentmergeviewer;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.any;
import static java.util.Arrays.asList;

import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.eclipse.emf.compare.diagram.ide.ui.papyrus.contentmergeviewer.item.PapyrusContentProviderMergeViewerItem;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.contentmergeviewer.provider.PapyrusTreeContentMergeViewerItemContentProvider;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.contentmergeviewer.provider.PapyrusTreeContentMergeViewerItemLabelProvider;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree.TreeContentMergeViewer;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.ICompareAccessor;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.AbstractMergeViewer;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.AbstractTableOrTreeMergeViewer.ElementComparer;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.TreeMergeViewer;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * Specialized Tree Content Merge Viewer for Papyrus.
 * 
 * @author Stefan Dirix
 */
@SuppressWarnings("restriction")
public class PapyrusTreeContentMergeViewer extends TreeContentMergeViewer {

	/**
	 * Since Papyrus trees could in theory be infinite, we need a maximum search level.
	 */
	private static final int MAX_SEARCH_LEVEL = 20;

	/**
	 * Map of objects to {@link PapyrusContentProviderMergeViewerItem Papyrus merge viewer items } used when
	 * changing the selection in order to find the merge viewer item to be selected when a specific object
	 * (model element or diff) is to be revealed.
	 */
	private Map<Object, IMergeViewerItem> cachedMapForSelection;

	/**
	 * Constructor.
	 * 
	 * @param style
	 *            the style parameter
	 * @param bundle
	 *            the {@link ResourceBundle}
	 * @param parent
	 *            the {@link Composite} parent
	 * @param config
	 *            the {@link EMFCompareConfiguration}
	 */
	public PapyrusTreeContentMergeViewer(int style, ResourceBundle bundle, Composite parent,
			EMFCompareConfiguration config) {
		super(style, bundle, parent, config);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#createMergeViewer(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected AbstractMergeViewer createMergeViewer(final Composite parent, final MergeViewerSide side) {
		final TreeMergeViewer mergeTreeViewer = new TreeMergeViewer(parent, side, this,
				getCompareConfiguration());
		final IContentProvider contentProvider = new PapyrusTreeContentMergeViewerItemContentProvider(
				getAdapterFactory(), getCompareConfiguration(), getDifferenceGroupProvider(),
				getDifferenceFilterPredicate());
		mergeTreeViewer.setContentProvider(contentProvider);

		final IBaseLabelProvider labelProvider = new PapyrusTreeContentMergeViewerItemLabelProvider(
				getResourceBundle(), getAdapterFactory(), side);
		mergeTreeViewer.setLabelProvider(labelProvider);

		hookListeners(mergeTreeViewer);

		return mergeTreeViewer;
	}

	/**
	 * Check whether the given input is an instance of {@link ICompareAccessor}.
	 * 
	 * @param input
	 *            the given input to check
	 * @return {@code true}, if the input is an instance of {@link ICompareAccessor}, {@code false} otherwise
	 */
	private static boolean isCompareAccessor(Object input) {
		return input instanceof ICompareAccessor;
	}

	/**
	 * Check whether the current input of the given side differs from the given one.
	 * 
	 * @param side
	 *            the side to be checked, either {@link MergeViewerSide#LEFT} or {@link MergeViewerSide#RIGHT}
	 * @param input
	 *            the input to check against
	 * @return {@code true}, if the input is different, {@code false} otherwise
	 */
	private boolean isDifferentInput(MergeViewerSide side, Object input) {
		TreeMergeViewer viewer = getMergeViewer(side);
		if (!isCompareAccessor(input) || !isCompareAccessor(viewer.getInput())) {
			return true;
		}
		ImmutableList<? extends IMergeViewerItem> inputItems = ICompareAccessor.class.cast(input).getItems();
		ImmutableList<? extends IMergeViewerItem> vieweritems = ICompareAccessor.class.cast(viewer.getInput())
				.getItems();
		if (inputItems.size() != vieweritems.size()) {
			return true;
		}
		ElementComparer comparer = new ElementComparer();
		for (int i = 0; i < vieweritems.size(); i++) {
			if (!comparer.equals(inputItems.get(i), vieweritems.get(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the {@link TreeMergeViewer} of the given side.
	 * 
	 * @param side
	 *            the side for which to return the {@link TreeMergeViewer}
	 * @return the {@link TreeMergeViewer} of the respective side
	 */
	public TreeMergeViewer getMergeViewer(MergeViewerSide side) {
		if (side == MergeViewerSide.LEFT) {
			return getLeftMergeViewer();
		} else if (side == MergeViewerSide.RIGHT) {
			return getRightMergeViewer();
		}
		return getAncestorMergeViewer();
	}

	@Override
	protected void updateContent(Object ancestor, Object left, Object right) {
		// Modify selection so it works with the Papyrus Merge Viewer Items

		// first check whether the input on any side has changed
		if (isDifferentInput(MergeViewerSide.LEFT, left) || isDifferentInput(MergeViewerSide.RIGHT, right)) {
			getAncestorMergeViewer().setInput(ancestor);
			getLeftMergeViewer().setInput(left);
			getRightMergeViewer().setInput(right);
		}

		IMergeViewerItem leftInitialItem = null;
		if (left instanceof ICompareAccessor) {
			leftInitialItem = ((ICompareAccessor)left).getInitialItem();
		}

		// Bug 458818: In some cases, the left initial item is null because
		// the item that should be selected has been deleted on the right
		// and this delete is part of a conflict
		if (leftInitialItem == null || leftInitialItem.getLeft() == null) {
			if (right instanceof ICompareAccessor) {
				IMergeViewerItem rightInitialItem = ((ICompareAccessor)right).getInitialItem();
				if (rightInitialItem == null) {
					getLeftMergeViewer().setSelection(StructuredSelection.EMPTY, true);
				} else {
					setSelection((ICompareAccessor)right, getRightMergeViewer());
				}
			} else {
				// Strange case: left is an ICompareAccessor but right is not?
				getLeftMergeViewer().setSelection(StructuredSelection.EMPTY, true);
			}
		} else {
			// others will synchronize on this one :)
			setSelection((ICompareAccessor)left, getLeftMergeViewer());
		}
		redrawCenterControl();
	}

	/**
	 * Caches the tree viewer content given by the objects <code>left</code> and <code>right</code>, if we
	 * haven't built a cache yet.
	 * <p>
	 * The caching builds a {@link #cachedMapForSelection map} of objects to be objects of the tree and their
	 * {@link IMergeViewerItem} that represent those objects.
	 * </p>
	 * 
	 * @param left
	 *            the left object, which must be a {@link ICompareAccessor}.
	 * @param right
	 *            the right object, which must be a {@link ICompareAccessor}.
	 */
	private void cacheTreeViewerContentIfNecessary(Object left, Object right) {
		if (cachedMapForSelection != null || notICompareAccessor(left, right)) {
			// we already have a cache or can't build one anyway
			return;
		}

		cachedMapForSelection = new HashMap<Object, IMergeViewerItem>();

		cacheTreeViewerContent((ICompareAccessor)left, getLeftMergeViewer(), MergeViewerSide.LEFT);
		cacheTreeViewerContent((ICompareAccessor)right, getRightMergeViewer(), MergeViewerSide.RIGHT);
	}

	/**
	 * Specifies whether the given <code>objects</code> are all instances of {@link ICompareAccessor}.
	 * 
	 * @param objects
	 *            The objects to check.
	 * @return <code>true</code> if they all are instances of {@link ICompareAccessor}, <code>false</code>
	 *         otherwise.
	 */
	private boolean notICompareAccessor(Object... objects) {
		return any(asList(objects), not(instanceOf(ICompareAccessor.class)));
	}

	/**
	 * Traverses all {@link ITreeContentProvider#getElements(Object) elements} of the given
	 * <code>accessor</code> and caches its content for the given <code>side</code>.
	 * <p>
	 * Note that this may be an expensive method, if the model is very large.
	 * </p>
	 * 
	 * @param accessor
	 *            The accessor representing the content to be cached.
	 * @param viewer
	 *            The viewer for obtaining the content provider from.
	 * @param side
	 *            The side of the viewer.
	 */
	private void cacheTreeViewerContent(ICompareAccessor accessor, TreeMergeViewer viewer,
			MergeViewerSide side) {
		final ITreeContentProvider provider = ITreeContentProvider.class.cast(viewer.getContentProvider());
		for (Object element : provider.getElements(accessor)) {
			if (element instanceof IMergeViewerItem) {
				final IMergeViewerItem item = IMergeViewerItem.class.cast(element);
				cacheTreeViewerContent(item, provider, side, MAX_SEARCH_LEVEL);
			}
		}
	}

	/**
	 * Caches the given <code>item</code> and its children determined by the given <code>provider</code>.
	 * 
	 * @param item
	 *            The item to be cached.
	 * @param provider
	 *            The content provider for determining the children of <code>item</code>.
	 * @param side
	 *            The merge viewer side.
	 * @param maxSearchLevel
	 *            The maximum search level.
	 */
	private void cacheTreeViewerContent(IMergeViewerItem item, ITreeContentProvider provider,
			MergeViewerSide side, int maxSearchLevel) {
		if (maxSearchLevel == 0) {
			return;
		}

		cacheItem(item, side);

		for (Object child : provider.getChildren(item)) {
			if (child instanceof IMergeViewerItem) {
				final IMergeViewerItem childItem = (IMergeViewerItem)child;
				cacheTreeViewerContent(childItem, provider, side, maxSearchLevel - 1);
			}
		}
	}

	/**
	 * Caches the given <code>item</code> for the given side.
	 * 
	 * @param item
	 *            The item to cache.
	 * @param side
	 *            The side.
	 */
	private void cacheItem(IMergeViewerItem item, MergeViewerSide side) {
		if (MergeViewerSide.LEFT.equals(side) && item.getLeft() != null) {
			cachedMapForSelection.put(item.getLeft(), item);
		} else if (MergeViewerSide.RIGHT.equals(side) && item.getRight() != null) {
			cachedMapForSelection.put(item.getRight(), item);
		}
	}

	/**
	 * Sets the selection according to the accessor.
	 * 
	 * @param accessor
	 *            The {@link ICompareAccessor} which contains the root tree elements and the initial
	 *            selection.
	 * @param viewer
	 *            The {@ink TreeMergeViewer} for which the selection is to be set.
	 */
	private void setSelection(ICompareAccessor accessor, TreeMergeViewer viewer) {
		// First try to set the initial item directly
		final IMergeViewerItem initialItem = accessor.getInitialItem();
		viewer.setSelection(new StructuredSelection(initialItem), true);

		// if that didn't work (empty selection), use cache to find correct merge viewer item
		if (viewer.getSelection().isEmpty()) {
			// init cache, if necessary
			cacheTreeViewerContentIfNecessary(getLeftMergeViewer().getInput(),
					getRightMergeViewer().getInput());
			final IMergeViewerItem itemToBeSelected = getItemToBeSelectedFromCache(initialItem);
			if (itemToBeSelected != null) {
				viewer.setSelection(new StructuredSelection(itemToBeSelected), true);
			} else {
				viewer.setSelection(new StructuredSelection(), true);
			}
		}
	}

	/**
	 * Obtains the item for the selection in the tree viewers for the given <code>item</code>.
	 * 
	 * @param item
	 *            The item to be selected.
	 * @return The item that can be used for selection in the merge viewer trees.
	 */
	private IMergeViewerItem getItemToBeSelectedFromCache(IMergeViewerItem item) {
		IMergeViewerItem itemToBeSelected = null;
		if (MergeViewerSide.LEFT.equals(item.getSide()) && item.getLeft() != null) {
			itemToBeSelected = cachedMapForSelection.get(item.getLeft());
		} else if (MergeViewerSide.RIGHT.equals(item.getSide()) && item.getRight() != null) {
			itemToBeSelected = cachedMapForSelection.get(item.getRight());
		}
		return itemToBeSelected;
	}

	@Override
	protected void handleDispose(DisposeEvent event) {
		if (cachedMapForSelection != null) {
			this.cachedMapForSelection.clear();
			this.cachedMapForSelection = null;
		}
		super.handleDispose(event);
	}
}

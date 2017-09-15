/*******************************************************************************
 * Copyright (c) 2013 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 522372
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.WrappableTreeViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class ExpandAllModelAction extends Action {

	private final AbstractTreeViewer treeViewer;

	public ExpandAllModelAction(AbstractTreeViewer treeViewer) {
		this.treeViewer = treeViewer;
		setToolTipText(EMFCompareIDEUIMessages.getString("expand.all.tooltip")); //$NON-NLS-1$
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(EMFCompareIDEUIPlugin.PLUGIN_ID,
				"icons/full/toolb16/expand_all.gif")); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		// If we can do special handling, i.e., for a wrappable tree viewer...
		if (treeViewer instanceof WrappableTreeViewer) {
			final WrappableTreeViewer wrappableTreeViewer = (WrappableTreeViewer)treeViewer;
			final Tree tree = wrappableTreeViewer.getTree();
			final TreeItem[] selection = tree.getSelection();
			final TreeItem topItem = tree.getTopItem();

			// We're most interested in expanding the selection if there is one.
			// Failing that, it makes the most sense to start expanding to top most visible element.
			final TreeItem[] itemsOfInterest;
			if (selection.length == 0 && topItem != null) {
				itemsOfInterest = new TreeItem[] {topItem };
			} else {
				itemsOfInterest = selection;
			}

			final Set<TreeItem> itemsToBeExpanded = Sets.newLinkedHashSet();
			final List<List<TreeItem>> prioritizedItems = getPrioritizedExpansionList(itemsOfInterest, tree);
			for (List<TreeItem> list : prioritizedItems) {
				itemsToBeExpanded.addAll(list);
			}

			// Do the expansion while showing a busy cursor.
			BusyIndicator.showWhile(wrappableTreeViewer.getControl().getDisplay(),
					new TimeboxedExpandItemsRunnable(wrappableTreeViewer, itemsToBeExpanded));
		} else {
			treeViewer.expandToLevel(256);
		}
	}

	/**
	 * Computes and returns a prioritized list of items to be expanded.
	 * <p>
	 * The result is a list of lists reflecting the priority. First we want to expand all the selected
	 * elements, then their closest siblings, then the closes siblings of the parent, and so on. The first
	 * list in this list will be for the items of interest directly.
	 * </p>
	 * 
	 * @param itemsOfInterest
	 *            The items of interest, such as the selection.
	 * @param tree
	 *            The tree to obtain the selection for.
	 * @return list of lists reflecting the priority of items to be expanded.
	 */
	private List<List<TreeItem>> getPrioritizedExpansionList(final TreeItem[] itemsOfInterest,
			final Tree tree) {
		List<List<TreeItem>> prioritizedItems = Lists.newArrayList();
		prioritizedItems.add(Lists.<TreeItem> newArrayList());

		for (TreeItem treeItem : itemsOfInterest) {
			prioritizedItems.get(0).add(treeItem);
			populateExpansionList(treeItem, prioritizedItems, tree);
		}

		return prioritizedItems;
	}

	/**
	 * Populate the list of each parent for the given <code>treeItem</code>, walking up the <code>tree</code>.
	 * 
	 * @param treeItem
	 *            The tree item for which to pupulate the given <code>prioritizedItems</code>.
	 * @param prioritizedItems
	 *            The prioritized list to populate.
	 * @param tree
	 *            The tree.
	 */
	private void populateExpansionList(final TreeItem treeItem, final List<List<TreeItem>> prioritizedItems,
			final Tree tree) {
		// The count keeps track of which list to populate during each iteration to an parent.
		int count = 1;
		for (TreeItem parentItem = treeItem
				.getParentItem(), childItem = treeItem;; childItem = parentItem, parentItem = parentItem
						.getParentItem(), ++count) {

			// If there is no list at the count yet, add one, otherwise reuse it.
			List<TreeItem> currentItemList;
			if (count >= prioritizedItems.size()) {
				currentItemList = Lists.newArrayList();
				prioritizedItems.add(currentItemList);
			} else {
				currentItemList = prioritizedItems.get(count);
			}

			// The siblings are either the items of the parent, or the root items.
			List<TreeItem> siblingItems;
			if (parentItem != null) {
				siblingItems = Arrays.asList(parentItem.getItems());
			} else {
				siblingItems = Arrays.asList(tree.getItems());
			}

			// Add all the siblings after the child item to the list.
			int index = siblingItems.indexOf(childItem);
			for (int i = index + 1, size = siblingItems.size(); i < size; ++i) {
				// Ignore siblings that don't have items (aren't expandable).
				TreeItem siblingItem = siblingItems.get(i);
				if (siblingItem.getItemCount() > 0) {
					currentItemList.add(siblingItem);
				}
			}

			// Add all the siblings before the child item to the list in reverse order.
			for (int i = index - 1; i >= 0; --i) {
				// Ignore siblings that don't have items (aren't expandable).
				TreeItem siblingItem = siblingItems.get(i);
				if (siblingItem.getItemCount() > 0) {
					currentItemList.add(siblingItem);
				}
			}

			// When we've reached the root siblings, so we're done.
			if (parentItem == null) {
				return;
			}
		}
	}

	public static final class TimeboxedExpandItemsRunnable implements Runnable {

		private final WrappableTreeViewer wrappableTreeViewer;

		private final Set<TreeItem> itemsToBeExpanded;

		private final long timeout;

		public TimeboxedExpandItemsRunnable(WrappableTreeViewer wrappableTreeViewer,
				Set<TreeItem> itemsToBeExpanded) {
			this(wrappableTreeViewer, itemsToBeExpanded, EMFCompareIDEUIPlugin.getDefault()
					.getPreferenceStore().getInt(EMFCompareUIPreferences.EDITOR_TREE_EXPAND_TIMEOUT) * 1000L);
		}

		public TimeboxedExpandItemsRunnable(WrappableTreeViewer wrappableTreeViewer,
				Set<TreeItem> itemsToBeExpanded, long timeout) {
			this.wrappableTreeViewer = wrappableTreeViewer;
			this.itemsToBeExpanded = itemsToBeExpanded;
			this.timeout = timeout;
		}

		public void run() {
			final Control control = wrappableTreeViewer.getControl();
			final Tree tree = wrappableTreeViewer.getTree();
			final TreeItem[] selection = tree.getSelection();
			final TreeItem topItem = tree.getTopItem();
			try {
				control.setRedraw(false);

				// Create a queue for prioritized processing.
				LinkedList<TreeItem> queue = Lists.newLinkedList(itemsToBeExpanded);

				// Only process for a fixed period of time.
				long start = System.currentTimeMillis();
				while (!queue.isEmpty()) {
					TreeItem treeItem = queue.poll();

					// Force the creation of children, and then expand the item,
					// if it's not already expanded.
					if (!treeItem.getExpanded()) {
						wrappableTreeViewer.createChildren(treeItem);
						treeItem.setExpanded(true);
					}

					// Add the children in reverse order for processing in the queue.
					TreeItem[] childItems = treeItem.getItems();
					for (int i = childItems.length - 1; i >= 0; --i) {
						TreeItem childItem = childItems[i];
						// If it's not a placeholder item and we haven't already added this item
						// to the queue, then add it at the front for high priority processing.
						if (childItem.getData() != null && itemsToBeExpanded.add(childItem)) {
							queue.addFirst(childItem);
						}
					}

					// Stop expanding if time is up.
					if (System.currentTimeMillis() - start > timeout) {
						return;
					}
				}
			} finally {
				// Scroll to the previous top item.
				tree.setTopItem(topItem);
				if (selection.length != 0) {
					// Force the selection to scroll into view.
					tree.setSelection(selection);
				}
				// Enable drawing again.
				control.setRedraw(true);
			}
		}
	}
}

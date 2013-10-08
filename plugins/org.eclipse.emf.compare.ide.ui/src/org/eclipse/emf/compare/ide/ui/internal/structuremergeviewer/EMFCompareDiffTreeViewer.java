/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import static com.google.common.collect.Sets.newHashSet;

import com.google.common.base.Predicate;
import com.google.common.eventbus.Subscribe;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import org.eclipse.compare.CompareViewerSwitchingPane;
import org.eclipse.compare.structuremergeviewer.DiffTreeViewer;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.util.JFaceUtil;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IComparisonAndScopeChange;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IMergePreviewModeChange;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilterChange;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProviderChange;
import org.eclipse.emf.compare.rcp.ui.internal.util.SWTUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareDiffTreeViewer extends DiffTreeViewer {

	public static final String REQUIRED_DIFF_COLOR = "RequiredDiffColor"; //$NON-NLS-1$

	public static final String REQUIRED_DIFF_BORDER_COLOR = "RequiredDiffBorderColor"; //$NON-NLS-1$

	public static final String UNMERGEABLE_DIFF_COLOR = "UnmergeableDiffColor"; //$NON-NLS-1$

	public static final String UNMERGEABLE_DIFF_BORDER_COLOR = "UnmergeableDiffBorderColor"; //$NON-NLS-1$

	private final Color requiredDiffColor;

	private final Color unmergeableDiffColor;

	private final CompareViewerSwitchingPane fParent;

	private Listener fEraseItemListener;

	private AdapterFactory adapterFactory;

	private ISelectionChangedListener selectionChangeListener;

	/**
	 * @param parent
	 * @param adapterFactory
	 * @param configuration
	 */
	public EMFCompareDiffTreeViewer(Composite parent, final AdapterFactory adapterFactory,
			EMFCompareConfiguration configuration) {
		super(parent, configuration);
		this.adapterFactory = adapterFactory;

		getCompareConfiguration().getEventBus().register(this);

		if (parent.getParent() instanceof CompareViewerSwitchingPane) {
			fParent = (CompareViewerSwitchingPane)parent.getParent();
		} else {
			fParent = null;
		}

		fEraseItemListener = new Listener() {
			public void handleEvent(Event event) {
				handleEraseItemEvent(event);
			}
		};
		getControl().addListener(SWT.EraseItem, fEraseItemListener);

		selectionChangeListener = new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				getControl().redraw();
			}
		};
		addSelectionChangedListener(selectionChangeListener);

		JFaceResources.getColorRegistry().put(REQUIRED_DIFF_COLOR, new RGB(215, 255, 200));
		JFaceResources.getColorRegistry().put(REQUIRED_DIFF_BORDER_COLOR, new RGB(195, 235, 180));
		JFaceResources.getColorRegistry().put(UNMERGEABLE_DIFF_COLOR, new RGB(255, 205, 180));
		JFaceResources.getColorRegistry().put(UNMERGEABLE_DIFF_BORDER_COLOR, new RGB(235, 185, 160));

		requiredDiffColor = JFaceResources.getColorRegistry().get(REQUIRED_DIFF_COLOR);
		unmergeableDiffColor = JFaceResources.getColorRegistry().get(UNMERGEABLE_DIFF_COLOR);

		setUseHashlookup(true);
	}

	/**
	 * A predicate that checks if the given input is a TreeNode that contains a diff.
	 * 
	 * @return true, if the given input is a TreeNode that contains a diff, false otherwise.
	 */
	static Predicate<EObject> IS_DIFF_TREE_NODE = new Predicate<EObject>() {
		public boolean apply(EObject t) {
			return t instanceof TreeNode && ((TreeNode)t).getData() instanceof Diff;
		}
	};

	@Subscribe
	public void selectedDifferenceFiltersChange(IDifferenceFilterChange event) {
		SWTUtil.safeAsyncExec(new Runnable() {
			public void run() {
				getTree().redraw();
				refreshTitle();
			}
		});
	}

	@Subscribe
	public void handleDifferenceGroupProviderChange(IDifferenceGroupProviderChange event) {
		SWTUtil.safeAsyncExec(new Runnable() {
			public void run() {
				refreshTitle();
			}
		});
	}

	@Subscribe
	public void mergePreviewModeChange(IMergePreviewModeChange event) {
		SWTUtil.safeRedraw(getTree(), true);
	}

	@Subscribe
	public void comparisonChange(IComparisonAndScopeChange event) {
		SWTUtil.safeRefresh(this, true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getComparator()
	 */
	@Override
	public ViewerComparator getComparator() {
		return null;
	}

	public void createChildrenSilently(Object o) {
		if (o instanceof Tree) {
			createChildren((Widget)o);
			for (TreeItem item : ((Tree)o).getItems()) {
				createChildrenSilently(item);
			}
		} else if (o instanceof TreeItem) {
			createChildren((Widget)o);
			for (TreeItem item : ((TreeItem)o).getItems()) {
				createChildrenSilently(item);
			}
		}
	}

	@Override
	public void initialSelection() {
		super.initialSelection();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#isExpandable(java.lang.Object)
	 */
	@Override
	public boolean isExpandable(Object parent) {
		if (hasFilters()) {
			// workaround for 65762
			return getFilteredChildren(parent).length > 0;
		}
		return super.isExpandable(parent);
	}

	public void refreshAfterDiff(Object root) {
		if (getControl().isDisposed()) {
			return;
		}

		refresh(root);
	}

	protected void refreshTitle() {
		if (fParent != null) {
			ITreeContentProvider contentProvider = (ITreeContentProvider)getContentProvider();
			int displayedDiff = getMatchCount(contentProvider, contentProvider.getElements(getRoot()));
			Comparison comparison = getCompareConfiguration().getComparison();
			int computedDiff = comparison.getDifferences().size();
			int filteredDiff = computedDiff - displayedDiff;
			fParent.setTitleArgument(computedDiff + " differences – " + filteredDiff
					+ " differences filtered from view");
		}
	}

	private int getMatchCount(ITreeContentProvider cp, Object[] elements) {
		Set<Diff> diffs = newHashSet();
		getMatchCount(cp, elements, diffs);
		return diffs.size();
	}

	/**
	 * @param cp
	 * @param children
	 * @param diffs
	 * @return
	 */
	private void getMatchCount(ITreeContentProvider cp, Object[] elements, Set<Diff> diffs) {
		for (int j = 0; j < elements.length; j++) {
			Object element = elements[j];
			if (!JFaceUtil.isFiltered(this, element, null) && element instanceof Adapter) {
				Notifier target = ((Adapter)element).getTarget();
				if (target instanceof TreeNode) {
					TreeNode treeNode = (TreeNode)target;
					EObject data = treeNode.getData();
					if (data instanceof Diff) {
						diffs.add((Diff)data);
					}
				}
			}
			Object[] children = cp.getChildren(element);
			getMatchCount(cp, children, diffs);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.DiffTreeViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		getControl().removeListener(SWT.EraseItem, fEraseItemListener);
		removeSelectionChangedListener(selectionChangeListener);
		getCompareConfiguration().getEventBus().unregister(this);
		super.handleDispose(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#getSortedChildren(java.lang.Object)
	 */
	@Override
	protected Object[] getSortedChildren(Object parentElementOrTreePath) {
		Object[] result = super.getSortedChildren(parentElementOrTreePath);
		if (parentElementOrTreePath instanceof Adapter) {
			Notifier target = ((Adapter)parentElementOrTreePath).getTarget();
			if (target instanceof TreeNode) {
				EObject data = ((TreeNode)target).getData();
				if (data instanceof Conflict) {
					Collections.sort(Arrays.asList(result), new Comparator<Object>() {
						public int compare(Object o1, Object o2) {
							return getValue(o1) - getValue(o2);
						}

						public int getValue(Object o) {
							int value = 0;
							if (o instanceof Adapter) {
								Notifier n = ((Adapter)o).getTarget();
								if (n instanceof TreeNode) {
									EObject d = ((TreeNode)n).getData();
									if (d instanceof Diff) {
										if (((Diff)d).getSource() == DifferenceSource.LEFT) {
											value = 1;
										} else {
											value = 2;
										}
									}
								}
							}
							return value;
						}
					});
				}
			}
		}

		return result;
	}

	/**
	 * Handle the erase item event. When select a difference in the structure merge viewer, highlight required
	 * differences with a specific color, and highlight unmergeable differences with another color.
	 * 
	 * @param event
	 *            the erase item event.
	 */
	protected void handleEraseItemEvent(Event event) {
		ISelection selection = getSelection();
		Object firstElement = ((IStructuredSelection)selection).getFirstElement();
		if (firstElement instanceof Adapter) {
			Notifier target = ((Adapter)firstElement).getTarget();
			if (target instanceof TreeNode) {
				EObject selectionData = ((TreeNode)target).getData();
				if (selectionData instanceof Diff) {
					TreeItem item = (TreeItem)event.item;
					Object dataTreeItem = item.getData();
					if (dataTreeItem instanceof Adapter) {
						Notifier targetItem = ((Adapter)dataTreeItem).getTarget();
						if (targetItem instanceof TreeNode) {
							EObject dataItem = ((TreeNode)targetItem).getData();
							MergeMode mergePreviewMode = getCompareConfiguration().getMergePreviewMode();
							boolean leftEditable = getCompareConfiguration().isLeftEditable();
							boolean rightEditable = getCompareConfiguration().isRightEditable();
							Diff selectedDiff = (Diff)selectionData;
							boolean leftToRight = mergePreviewMode.isLeftToRight(leftEditable, rightEditable);
							final Set<Diff> requires = DiffUtil.getRequires(selectedDiff, leftToRight);
							final Set<Diff> unmergeables = DiffUtil
									.getUnmergeables(selectedDiff, leftToRight);
							final GC g = event.gc;
							if (requires.contains(dataItem)) {
								paintItemBackground(g, item, requiredDiffColor);
							} else if (unmergeables.contains(dataItem)) {
								paintItemBackground(g, item, unmergeableDiffColor);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Paint the background of the given item with the given color.
	 * 
	 * @param g
	 *            the GC associated to the item.
	 * @param item
	 *            the given item.
	 * @param color
	 *            the given color.
	 */
	private void paintItemBackground(GC g, TreeItem item, Color color) {
		Rectangle itemBounds = item.getBounds();
		Tree tree = item.getParent();
		Rectangle areaBounds = tree.getClientArea();
		g.setClipping(areaBounds.x, itemBounds.y, areaBounds.width, itemBounds.height);
		g.setBackground(color);
		g.fillRectangle(areaBounds.x, itemBounds.y, areaBounds.width, itemBounds.height);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.DiffTreeViewer#getCompareConfiguration()
	 */
	@Override
	public EMFCompareConfiguration getCompareConfiguration() {
		return (EMFCompareConfiguration)super.getCompareConfiguration();
	}
}

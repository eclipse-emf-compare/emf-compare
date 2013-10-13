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

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.resource.JFaceResources;
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

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareDiffTreeViewer extends WrappableTreeViewer {

	public static final String REQUIRED_DIFF_COLOR = "RequiredDiffColor"; //$NON-NLS-1$

	public static final String REQUIRED_DIFF_BORDER_COLOR = "RequiredDiffBorderColor"; //$NON-NLS-1$

	public static final String UNMERGEABLE_DIFF_COLOR = "UnmergeableDiffColor"; //$NON-NLS-1$

	public static final String UNMERGEABLE_DIFF_BORDER_COLOR = "UnmergeableDiffBorderColor"; //$NON-NLS-1$

	private final Color requiredDiffColor;

	private final Color unmergeableDiffColor;

	private final Listener fEraseItemListener;

	private final DependencyData dependencyData;

	/**
	 * @param parent
	 * @param adapterFactory
	 * @param configuration
	 */
	public EMFCompareDiffTreeViewer(Composite parent, DependencyData dependencyData) {
		super(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		this.dependencyData = dependencyData;

		fEraseItemListener = new Listener() {
			public void handleEvent(Event event) {
				handleEraseItemEvent(event);
			}
		};
		getControl().addListener(SWT.EraseItem, fEraseItemListener);

		JFaceResources.getColorRegistry().put(REQUIRED_DIFF_COLOR, new RGB(215, 255, 200));
		JFaceResources.getColorRegistry().put(REQUIRED_DIFF_BORDER_COLOR, new RGB(195, 235, 180));
		JFaceResources.getColorRegistry().put(UNMERGEABLE_DIFF_COLOR, new RGB(255, 205, 180));
		JFaceResources.getColorRegistry().put(UNMERGEABLE_DIFF_BORDER_COLOR, new RGB(235, 185, 160));

		requiredDiffColor = JFaceResources.getColorRegistry().get(REQUIRED_DIFF_COLOR);
		unmergeableDiffColor = JFaceResources.getColorRegistry().get(UNMERGEABLE_DIFF_COLOR);

		setUseHashlookup(true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#isExpandable(java.lang.Object)
	 */
	@Override
	public boolean isExpandable(Object parent) {
		if (hasFilters()) {
			// workaround for 65762
			return getFilteredChildren(parent).length > 0;
		}
		return super.isExpandable(parent);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.DiffTreeViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	public void handleDispose(DisposeEvent event) {
		getControl().removeListener(SWT.EraseItem, fEraseItemListener);
		super.handleDispose(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#getSortedChildren(java.lang.Object)
	 */
	@Override
	public Object[] getSortedChildren(Object parentElementOrTreePath) {
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
		TreeItem item = (TreeItem)event.item;
		EObject dataItem = EMFCompareStructureMergeViewer.getDataOfTreeNodeOfAdapter(item.getData());
		if (dataItem != null) {
			final Set<Diff> requires = dependencyData.getRequires();
			final Set<Diff> unmergeables = dependencyData.getUnmergeables();
			final GC g = event.gc;
			if (requires.contains(dataItem)) {
				paintItemBackground(g, item, requiredDiffColor);
			} else if (unmergeables.contains(dataItem)) {
				paintItemBackground(g, item, unmergeableDiffColor);
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

}

/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.property;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAction;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.TreeMergeViewer;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.ICompareColor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * A specialized {@link TreeMergeViewer tree merge viewer} that can display {@link PropertyItem property
 * items}.
 */
class PropertyTreeMergeViewer extends TreeMergeViewer {

	/**
	 * The handler for resizing columns to their packed size.
	 */
	final ColumnResizer.Handler columnResizer;

	/**
	 * The root property item input for this viewer.
	 */
	private PropertyItem rootPropertyItem;

	/**
	 * Creates an instance with the specified parent, for the specified side, with specified color provider,
	 * for the specified compare configuration.
	 * 
	 * @param parent
	 *            the parent composite for this viewer's control.
	 * @param side
	 *            the side of this viewer.
	 * @param colorProvider
	 *            the color provider for drawing/highlighting property items with an associated diff.
	 * @param configuration
	 *            the compare configuration
	 */
	PropertyTreeMergeViewer(Composite parent, MergeViewerSide side, ICompareColor.Provider colorProvider,
			final EMFCompareConfiguration configuration) {
		super(parent, side, colorProvider, configuration);

		// Create a simple content provider from the adapter factory of the configuration.
		// Note that we want to ignore notifications because a property item will not update based on
		// notifications.
		AdapterFactory adapterFactory = configuration.getAdapterFactory();
		AdapterFactoryContentProvider adapterFactoryContentProvider = new AdapterFactoryContentProvider(
				adapterFactory) {
			@Override
			public void notifyChanged(Notification notification) {
			}
		};
		setContentProvider(adapterFactoryContentProvider);

		// Create a label provider supporting fonts because we draw modified property items with a bold font.
		setLabelProvider(new AdapterFactoryLabelProvider.FontProvider(adapterFactory, this));

		// Limit expansion the expansion to 10 levels. It's unlikely any properties nest deeper than this.
		TreeViewer treeViewer = getStructuredViewer();
		treeViewer.setAutoExpandLevel(10);

		// Set up the two columns, Property and Value, for the tree.
		final Tree tree = treeViewer.getTree();
		TreeColumn propColumn = new TreeColumn(tree, SWT.LEFT, 0);
		propColumn.setText(EMFCompareIDEUIMessages.getString("PropertyContentMergeViewer.property.label")); //$NON-NLS-1$
		TreeColumn valueColumn = new TreeColumn(tree, SWT.LEFT, 1);
		valueColumn.setText(EMFCompareIDEUIMessages.getString("PropertyContentMergeViewer.value.label")); //$NON-NLS-1$

		// Show the header of the columns and make the lines of the "table" visible.
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);

		// Attached the resize handler for packing columns to their minimal size.
		columnResizer = ColumnResizer.addColumnResizer(tree);

		// Listen for expand and collapse events so that we can update tree item.
		// This is important for list-type properties which should show the size of the list when
		// collapsed but be blank when expanded.
		tree.addTreeListener(new TreeListener() {
			public void treeExpanded(TreeEvent event) {
				update((TreeItem)event.item, true);
			}

			public void treeCollapsed(TreeEvent event) {
				update((TreeItem)event.item, false);
			}

			private void update(TreeItem treeItem, boolean expanded) {
				PropertyItem propertyItem = (PropertyItem)treeItem.getData();
				propertyItem.update(treeItem, expanded);
			}
		});

		// Listen for double click events in order to expand and collapse property items via double click.
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent event) {
				TreeItem treeItem = tree.getItem(new Point(event.x, event.y));
				if (treeItem != null && treeItem.getItemCount() > 0) {
					boolean expanded = !treeItem.getExpanded();
					treeItem.setExpanded(expanded);
					PropertyItem propertyItem = (PropertyItem)treeItem.getData();
					propertyItem.update(treeItem, expanded);
				}
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation creates an {@link MergeAction} with the specified diff as the selection..
	 * </p>
	 */
	@Override
	protected IAction createAction(MergeMode mode, Diff diff) {
		return new MergeAction(getCompareConfiguration(),
				EMFCompareRCPPlugin.getDefault().getMergerRegistry(), mode, null,
				new StructuredSelection(diff));
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation fetches the {@link PropertyAccessor#getRootPropertyItem() root property item} of
	 * the input, which must be an instance of {@link PropertyAccessor} and not be {@code null}, using that as
	 * the input for the {@linked #getStructuredViewer() structured viewer}. It then {@link #columnResizer
	 * packs} the columns.
	 * </p>
	 */
	@Override
	protected void inputChanged(Object input, Object oldInput) {
		rootPropertyItem = ((PropertyAccessor)input).getRootPropertyItem();

		TreeViewer treeViewer = getStructuredViewer();
		treeViewer.setSelection(null);
		treeViewer.setInput(rootPropertyItem);

		columnResizer.resizeColumns();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * </p>
	 */
	@Override
	protected TreeViewer createTreeViewer(Composite parent) {
		return new PropertyTreeViewer(parent, getRootPropertyItem(), SWT.FULL_SELECTION | SWT.SINGLE
				| SWT.HIDE_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
	}

	/**
	 * Returns the root property item.
	 * 
	 * @return the root property item.
	 */
	public PropertyItem getRootPropertyItem() {
		return rootPropertyItem;
	}
}

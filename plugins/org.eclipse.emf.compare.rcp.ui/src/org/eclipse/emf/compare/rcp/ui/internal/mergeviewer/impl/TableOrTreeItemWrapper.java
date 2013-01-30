/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
abstract class TableOrTreeItemWrapper {

	public static TableOrTreeItemWrapper create(Item item) {
		if (item instanceof TreeItem) {
			return new TreeItemWrapper((TreeItem)item);
		} else if (item instanceof TableItem) {
			return new TableItemWrapper((TableItem)item);
		} else if (item == null) {
			return null;
		} else {
			throw new IllegalArgumentException("Item must be instance of TreeItem or TableItem"); //$NON-NLS-1$
		}
	}

	protected abstract Item getItem();

	public abstract Rectangle getBounds();

	public abstract Scrollable getParent();

	public abstract int getParentColumnCount();

	public abstract Rectangle getImageBounds(int index);

	public abstract Rectangle getTextBounds(int index);

	/**
	 * @param index
	 * @return
	 */
	public abstract String getText(int index);

	public abstract Image getImage(int index);

	public abstract TableOrTreeItemWrapper getParentItem();

	public abstract int getParentItemHeight();

	public Object getData() {
		return getItem().getData();
	}

	private static class TreeItemWrapper extends TableOrTreeItemWrapper {

		private final TreeItem fItem;

		/**
		 * @param item
		 */
		public TreeItemWrapper(TreeItem item) {
			fItem = item;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.impl.util.TableOrTreeItemWrapper#getItem()
		 */
		@Override
		protected Item getItem() {
			return fItem;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.impl.util.TableOrTreeItemWrapper#getBounds()
		 */
		@Override
		public Rectangle getBounds() {
			return fItem.getBounds();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.impl.util.TableOrTreeItemWrapper#getParent()
		 */
		@Override
		public Scrollable getParent() {
			return fItem.getParent();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.impl.util.TableOrTreeItemWrapper#getParentColumnCount()
		 */
		@Override
		public int getParentColumnCount() {
			return fItem.getParent().getColumnCount();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.impl.util.TableOrTreeItemWrapper#getImageBounds(int)
		 */
		@Override
		public Rectangle getImageBounds(int index) {
			return fItem.getImageBounds(index);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.impl.util.TableOrTreeItemWrapper#getParentItem()
		 */
		@Override
		public TableOrTreeItemWrapper getParentItem() {
			return TableOrTreeItemWrapper.create(fItem.getParentItem());
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.impl.util.TableOrTreeItemWrapper#getParentItemHeight()
		 */
		@Override
		public int getParentItemHeight() {
			return fItem.getParent().getItemHeight();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.TableOrTreeItemWrapper#getTextBounds(int)
		 */
		@Override
		public Rectangle getTextBounds(int index) {
			return fItem.getTextBounds(index);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.TableOrTreeItemWrapper#getText(int)
		 */
		@Override
		public String getText(int index) {
			return fItem.getText(index);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.TableOrTreeItemWrapper#getImage(int)
		 */
		@Override
		public Image getImage(int index) {
			return fItem.getImage(index);
		}

	}

	private static class TableItemWrapper extends TableOrTreeItemWrapper {

		private final TableItem fItem;

		/**
		 * @param item
		 */
		public TableItemWrapper(TableItem item) {
			fItem = item;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.impl.util.TableOrTreeItemWrapper#getItem()
		 */
		@Override
		protected Item getItem() {
			return fItem;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.impl.util.TableOrTreeItemWrapper#getBounds()
		 */
		@Override
		public Rectangle getBounds() {
			return fItem.getBounds();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.impl.util.TableOrTreeItemWrapper#getParent()
		 */
		@Override
		public Scrollable getParent() {
			return fItem.getParent();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.impl.util.TableOrTreeItemWrapper#getParentColumnCount()
		 */
		@Override
		public int getParentColumnCount() {
			return fItem.getParent().getColumnCount();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.impl.util.TableOrTreeItemWrapper#getImageBounds(int)
		 */
		@Override
		public Rectangle getImageBounds(int index) {
			return fItem.getImageBounds(index);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.impl.util.TableOrTreeItemWrapper#getParentItem()
		 */
		@Override
		public TableOrTreeItemWrapper getParentItem() {
			return null;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.impl.util.TableOrTreeItemWrapper#getParentItemHeight()
		 */
		@Override
		public int getParentItemHeight() {
			return fItem.getParent().getItemHeight();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.TableOrTreeItemWrapper#getTextBounds(int)
		 */
		@Override
		public Rectangle getTextBounds(int index) {
			return fItem.getTextBounds(index);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.TableOrTreeItemWrapper#getText(int)
		 */
		@Override
		public String getText(int index) {
			return fItem.getText(index);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.TableOrTreeItemWrapper#getImage(int)
		 */
		@Override
		public Image getImage(int index) {
			return fItem.getImage(index);
		}
	}
}

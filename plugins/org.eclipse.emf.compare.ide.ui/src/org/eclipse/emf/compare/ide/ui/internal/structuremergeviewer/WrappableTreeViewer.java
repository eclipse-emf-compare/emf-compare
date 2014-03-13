/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import java.util.List;

import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerLabel;
import org.eclipse.jface.viewers.ViewerRow;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
class WrappableTreeViewer extends TreeViewer implements IWrappableStructuredViewer {

	/**
	 * @param parent
	 * @param style
	 */
	public WrappableTreeViewer(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @param parent
	 */
	public WrappableTreeViewer(Composite parent) {
		super(parent);
	}

	/**
	 * @param tree
	 */
	public WrappableTreeViewer(Tree tree) {
		super(tree);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#addTreeListener(org.eclipse.swt.widgets.Control,
	 *      org.eclipse.swt.events.TreeListener)
	 */
	@Override
	public void addTreeListener(Control c, TreeListener listener) {
		super.addTreeListener(c, listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#getColumnViewerOwner(int)
	 */
	@Override
	public Widget getColumnViewerOwner(int columnIndex) {
		return super.getColumnViewerOwner(columnIndex);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#getChildren(org.eclipse.swt.widgets.Widget)
	 */
	@Override
	public Item[] getChildren(Widget o) {
		return super.getChildren(o);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#getExpanded(org.eclipse.swt.widgets.Item)
	 */
	@Override
	public boolean getExpanded(Item item) {
		return super.getExpanded(item);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#getItemAt(org.eclipse.swt.graphics.Point)
	 */
	@Override
	public Item getItemAt(Point p) {
		return super.getItemAt(p);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#getItemCount(org.eclipse.swt.widgets.Control)
	 */
	@Override
	public int getItemCount(Control widget) {
		return super.getItemCount(widget);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#getItemCount(org.eclipse.swt.widgets.Item)
	 */
	@Override
	public int getItemCount(Item item) {
		return super.getItemCount(item);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#getItems(org.eclipse.swt.widgets.Item)
	 */
	@Override
	public Item[] getItems(Item item) {
		return super.getItems(item);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#getParentItem(org.eclipse.swt.widgets.Item)
	 */
	@Override
	public Item getParentItem(Item item) {
		return super.getParentItem(item);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#getSelection(org.eclipse.swt.widgets.Control)
	 */
	@Override
	public Item[] getSelection(Control widget) {
		return super.getSelection(widget);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#hookControl(org.eclipse.swt.widgets.Control)
	 */
	@Override
	public void hookControl(Control control) {
		super.hookControl(control);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#createViewerEditor()
	 */
	@Override
	public ColumnViewerEditor createViewerEditor() {
		return super.createViewerEditor();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#newItem(org.eclipse.swt.widgets.Widget, int, int)
	 */
	@Override
	public Item newItem(Widget parent, int flags, int ix) {
		return super.newItem(parent, flags, ix);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#removeAll(org.eclipse.swt.widgets.Control)
	 */
	@Override
	public void removeAll(Control widget) {
		super.removeAll(widget);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#setExpanded(org.eclipse.swt.widgets.Item, boolean)
	 */
	@Override
	public void setExpanded(Item node, boolean expand) {
		super.setExpanded(node, expand);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#setSelection(java.util.List)
	 */
	// Suppressing warning since super is raw
	@SuppressWarnings("rawtypes")
	@Override
	public void setSelection(List items) {
		super.setSelection(items);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#showItem(org.eclipse.swt.widgets.Item)
	 */
	@Override
	public void showItem(Item item) {
		super.showItem(item);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#getChild(org.eclipse.swt.widgets.Widget, int)
	 */
	@Override
	public Item getChild(Widget widget, int index) {
		return super.getChild(widget, index);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#assertContentProviderType(org.eclipse.jface.viewers.IContentProvider)
	 */
	@Override
	public void assertContentProviderType(IContentProvider provider) {
		super.assertContentProviderType(provider);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#getRawChildren(java.lang.Object)
	 */
	@Override
	public Object[] getRawChildren(Object parent) {
		return super.getRawChildren(parent);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#getParentElement(java.lang.Object)
	 */
	@Override
	public Object getParentElement(Object element) {
		return super.getParentElement(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#internalAdd(org.eclipse.swt.widgets.Widget, java.lang.Object,
	 *      java.lang.Object[])
	 */
	@Override
	public void internalAdd(Widget widget, Object parentElement, Object[] childElements) {
		super.internalAdd(widget, parentElement, childElements);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#internalRefreshStruct(org.eclipse.swt.widgets.Widget,
	 *      java.lang.Object, boolean)
	 */
	@Override
	public void internalRefreshStruct(Widget widget, Object element, boolean updateLabels) {
		super.internalRefreshStruct(widget, element, updateLabels);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#mapElement(java.lang.Object, org.eclipse.swt.widgets.Widget)
	 */
	@Override
	public void mapElement(Object element, Widget item) {
		super.mapElement(element, item);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#getViewerRowFromItem(org.eclipse.swt.widgets.Widget)
	 */
	@Override
	public ViewerRow getViewerRowFromItem(Widget item) {
		return super.getViewerRowFromItem(item);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#internalInitializeTree(org.eclipse.swt.widgets.Control)
	 */
	@Override
	public void internalInitializeTree(Control widget) {
		super.internalInitializeTree(widget);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#updatePlus(org.eclipse.swt.widgets.Item, java.lang.Object)
	 */
	@Override
	public void updatePlus(Item item, Object element) {
		super.updatePlus(item, element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#handleTreeExpand(org.eclipse.swt.events.TreeEvent)
	 */
	@Override
	public void handleTreeExpand(TreeEvent event) {
		super.handleTreeExpand(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#handleTreeCollapse(org.eclipse.swt.events.TreeEvent)
	 */
	@Override
	public void handleTreeCollapse(TreeEvent event) {
		super.handleTreeCollapse(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#disassociate(org.eclipse.swt.widgets.Item)
	 */
	@Override
	public void disassociate(Item item) {
		super.disassociate(item);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.TreeViewer#doGetColumnCount()
	 */
	@Override
	public int doGetColumnCount() {
		return super.doGetColumnCount();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#indexForElement(org.eclipse.swt.widgets.Widget,
	 *      java.lang.Object)
	 */
	@Override
	public int indexForElement(Widget parent, Object element) {
		return super.indexForElement(parent, element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#getSortedChildren(java.lang.Object)
	 */
	@Override
	public Object[] getSortedChildren(Object parentElementOrTreePath) {
		return super.getSortedChildren(parentElementOrTreePath);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#addSelectionListener(org.eclipse.swt.widgets.Control,
	 *      org.eclipse.swt.events.SelectionListener)
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void addSelectionListener(Control control, SelectionListener listener) {
		super.addSelectionListener(control, listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#associate(java.lang.Object,
	 *      org.eclipse.swt.widgets.Item)
	 */
	@Override
	public void associate(Object element, Item item) {
		super.associate(element, item);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#createChildren(org.eclipse.swt.widgets.Widget)
	 */
	@Override
	public void createChildren(Widget widget) {
		super.createChildren(widget);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#createTreeItem(org.eclipse.swt.widgets.Widget,
	 *      java.lang.Object, int)
	 */
	@Override
	public void createTreeItem(Widget parent, Object element, int index) {
		super.createTreeItem(parent, element, index);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#doFindInputItem(java.lang.Object)
	 */
	@Override
	public Widget doFindInputItem(Object element) {
		return super.doFindInputItem(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#doFindItem(java.lang.Object)
	 */
	@Override
	public Widget doFindItem(Object element) {
		return super.doFindItem(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#doUpdateItem(org.eclipse.swt.widgets.Item,
	 *      java.lang.Object)
	 */
	@Override
	public void doUpdateItem(Item item, Object element) {
		super.doUpdateItem(item, element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#isSameSelection(java.util.List,
	 *      org.eclipse.swt.widgets.Item[])
	 */
	// Suppressing warning since super is raw
	@SuppressWarnings("rawtypes")
	@Override
	public boolean isSameSelection(List items, Item[] current) {
		return super.isSameSelection(items, current);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#doUpdateItem(org.eclipse.swt.widgets.Widget,
	 *      java.lang.Object, boolean)
	 */
	@Override
	public void doUpdateItem(Widget widget, Object element, boolean fullMap) {
		super.doUpdateItem(widget, element, fullMap);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#fireTreeCollapsed(org.eclipse.jface.viewers.TreeExpansionEvent)
	 */
	@Override
	public void fireTreeCollapsed(TreeExpansionEvent event) {
		super.fireTreeCollapsed(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#fireTreeExpanded(org.eclipse.jface.viewers.TreeExpansionEvent)
	 */
	@Override
	public void fireTreeExpanded(TreeExpansionEvent event) {
		super.fireTreeExpanded(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#getNextItem(org.eclipse.swt.widgets.Item, boolean)
	 */
	@Override
	public Item getNextItem(Item item, boolean includeChildren) {
		return super.getNextItem(item, includeChildren);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#getPreviousItem(org.eclipse.swt.widgets.Item)
	 */
	@Override
	public Item getPreviousItem(Item item) {
		return super.getPreviousItem(item);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#getSelectionFromWidget()
	 */
	// Suppressing warning since super is raw
	@SuppressWarnings("rawtypes")
	@Override
	public List getSelectionFromWidget() {
		return super.getSelectionFromWidget();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#handleDoubleSelect(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void handleDoubleSelect(SelectionEvent event) {
		super.handleDoubleSelect(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#inputChanged(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Object input, Object oldInput) {
		super.inputChanged(input, oldInput);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#internalCollapseToLevel(org.eclipse.swt.widgets.Widget,
	 *      int)
	 */
	@Override
	public void internalCollapseToLevel(Widget widget, int level) {
		super.internalCollapseToLevel(widget, level);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#internalExpand(java.lang.Object, boolean)
	 */
	@Override
	public Widget internalExpand(Object elementOrPath, boolean expand) {
		return super.internalExpand(elementOrPath, expand);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#internalGetWidgetToSelect(java.lang.Object)
	 */
	@Override
	public Widget internalGetWidgetToSelect(Object elementOrTreePath) {
		return super.internalGetWidgetToSelect(elementOrTreePath);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#internalExpandToLevel(org.eclipse.swt.widgets.Widget,
	 *      int)
	 */
	@Override
	public void internalExpandToLevel(Widget widget, int level) {
		super.internalExpandToLevel(widget, level);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#internalRefresh(java.lang.Object)
	 */
	@Override
	public void internalRefresh(Object element) {
		super.internalRefresh(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#internalRefresh(java.lang.Object, boolean)
	 */
	@Override
	public void internalRefresh(Object element, boolean updateLabels) {
		super.internalRefresh(element, updateLabels);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#internalRefresh(org.eclipse.swt.widgets.Widget,
	 *      java.lang.Object, boolean, boolean)
	 */
	@Override
	public void internalRefresh(Widget widget, Object element, boolean doStruct, boolean updateLabels) {
		super.internalRefresh(widget, element, doStruct, updateLabels);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#internalRemove(java.lang.Object[])
	 */
	@Override
	public void internalRemove(Object[] elementsOrPaths) {
		super.internalRemove(elementsOrPaths);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#internalRemove(java.lang.Object, java.lang.Object[])
	 */
	@Override
	public void internalRemove(Object parent, Object[] elements) {
		super.internalRemove(parent, elements);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#labelProviderChanged()
	 */
	@Override
	public void labelProviderChanged() {
		super.labelProviderChanged();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#setSelectionToWidget(java.util.List, boolean)
	 */
	// Suppressing warning since super is raw
	@SuppressWarnings("rawtypes")
	@Override
	public void setSelectionToWidget(List v, boolean reveal) {
		super.setSelectionToWidget(v, reveal);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#updateChildren(org.eclipse.swt.widgets.Widget,
	 *      java.lang.Object, java.lang.Object[])
	 * @deprecated
	 */
	@Override
	@Deprecated
	public void updateChildren(Widget widget, Object parent, Object[] elementChildren) {
		super.updateChildren(widget, parent, elementChildren);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#getTreePathFromItem(org.eclipse.swt.widgets.Item)
	 */
	@Override
	public TreePath getTreePathFromItem(Item item) {
		return super.getTreePathFromItem(item);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#setSelectionToWidget(org.eclipse.jface.viewers.ISelection,
	 *      boolean)
	 */
	@Override
	public void setSelectionToWidget(ISelection selection, boolean reveal) {
		super.setSelectionToWidget(selection, reveal);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#buildLabel(org.eclipse.jface.viewers.ViewerLabel,
	 *      java.lang.Object)
	 */
	@Override
	public void buildLabel(ViewerLabel updateLabel, Object elementOrPath) {
		super.buildLabel(updateLabel, elementOrPath);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ColumnViewer#hookEditingSupport(org.eclipse.swt.widgets.Control)
	 */
	@Override
	public void hookEditingSupport(Control control) {
		super.hookEditingSupport(control);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ColumnViewer#getViewerRow(org.eclipse.swt.graphics.Point)
	 */
	@Override
	public ViewerRow getViewerRow(Point point) {
		return super.getViewerRow(point);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ColumnViewer#getItem(int, int)
	 */
	@Override
	public Item getItem(int x, int y) {
		return super.getItem(x, y);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ColumnViewer#applyEditorValue()
	 */
	@Override
	public void applyEditorValue() {
		super.applyEditorValue();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ColumnViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	public void handleDispose(DisposeEvent event) {
		super.handleDispose(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ColumnViewer#triggerEditorActivationEvent(org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent)
	 */
	@Override
	public void triggerEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
		super.triggerEditorActivationEvent(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ColumnViewer#checkBusy()
	 */
	@Override
	public boolean checkBusy() {
		return super.checkBusy();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ColumnViewer#setBusy(boolean)
	 */
	@Override
	public void setBusy(boolean busy) {
		super.setBusy(busy);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#assertElementsNotNull(java.lang.Object[])
	 */
	@Override
	public void assertElementsNotNull(Object[] elements) {
		super.assertElementsNotNull(elements);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#equals(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean equals(Object elementA, Object elementB) {
		return super.equals(elementA, elementB);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#filter(java.lang.Object[])
	 */
	@Override
	public Object[] filter(Object[] elements) {
		return super.filter(elements);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#fireDoubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
	 */
	@Override
	public void fireDoubleClick(DoubleClickEvent event) {
		super.fireDoubleClick(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#fireOpen(org.eclipse.jface.viewers.OpenEvent)
	 */
	@Override
	public void fireOpen(OpenEvent event) {
		super.fireOpen(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#firePostSelectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void firePostSelectionChanged(SelectionChangedEvent event) {
		super.firePostSelectionChanged(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getFilteredChildren(java.lang.Object)
	 */
	@Override
	public Object[] getFilteredChildren(Object parent) {
		return super.getFilteredChildren(parent);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getRoot()
	 */
	@Override
	public Object getRoot() {
		return super.getRoot();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#handleOpen(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void handleOpen(SelectionEvent event) {
		super.handleOpen(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#handleInvalidSelection(org.eclipse.jface.viewers.ISelection,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void handleInvalidSelection(ISelection invalidSelection, ISelection newSelection) {
		super.handleInvalidSelection(invalidSelection, newSelection);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#handleLabelProviderChanged(org.eclipse.jface.viewers.LabelProviderChangedEvent)
	 */
	@Override
	public void handleLabelProviderChanged(LabelProviderChangedEvent event) {
		super.handleLabelProviderChanged(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#handleSelect(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void handleSelect(SelectionEvent event) {
		super.handleSelect(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#handlePostSelect(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void handlePostSelect(SelectionEvent e) {
		super.handlePostSelect(e);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#hasFilters()
	 */
	@Override
	public boolean hasFilters() {
		return super.hasFilters();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#needsRefilter(java.lang.Object, java.lang.String)
	 */
	@Override
	public boolean needsRefilter(Object element, String property) {
		return super.needsRefilter(element, property);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#preservingSelection(java.lang.Runnable)
	 */
	@Override
	public void preservingSelection(Runnable updateCode) {
		super.preservingSelection(updateCode);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#unmapAllElements()
	 */
	@Override
	public void unmapAllElements() {
		super.unmapAllElements();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#unmapElement(java.lang.Object)
	 */
	@Override
	public void unmapElement(Object element) {
		super.unmapElement(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#unmapElement(java.lang.Object,
	 *      org.eclipse.swt.widgets.Widget)
	 */
	@Override
	public void unmapElement(Object element, Widget item) {
		super.unmapElement(element, item);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#internalUpdate(org.eclipse.swt.widgets.Widget,
	 *      java.lang.Object, java.lang.String[])
	 */
	@Override
	public void internalUpdate(Widget widget, Object element, String[] properties) {
		super.internalUpdate(widget, element, properties);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#updateSelection(org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void updateSelection(ISelection selection) {
		super.updateSelection(selection);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#usingElementMap()
	 */
	@Override
	public boolean usingElementMap() {
		return super.usingElementMap();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getColorAndFontCollector()
	 */
	@Override
	public ColorAndFontCollector getColorAndFontCollector() {
		return super.getColorAndFontCollector();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#fireHelpRequested(org.eclipse.swt.events.HelpEvent)
	 */
	@Override
	public void fireHelpRequested(HelpEvent event) {
		super.fireHelpRequested(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#fireSelectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void fireSelectionChanged(SelectionChangedEvent event) {
		super.fireSelectionChanged(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#handleHelpRequest(org.eclipse.swt.events.HelpEvent)
	 */
	@Override
	public void handleHelpRequest(HelpEvent event) {
		super.handleHelpRequest(event);
	}
}

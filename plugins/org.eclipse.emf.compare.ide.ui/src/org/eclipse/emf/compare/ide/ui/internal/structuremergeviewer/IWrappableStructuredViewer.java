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

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerLabel;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Widget;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
interface IWrappableStructuredViewer {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#addDoubleClickListener(org.eclipse.jface.viewers.IDoubleClickListener)
	 */
	void addDoubleClickListener(IDoubleClickListener listener);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#addOpenListener(org.eclipse.jface.viewers.IOpenListener)
	 */
	void addOpenListener(IOpenListener listener);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#addPostSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	void addPostSelectionChangedListener(ISelectionChangedListener listener);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#addDragSupport(int, org.eclipse.swt.dnd.Transfer[],
	 *      org.eclipse.swt.dnd.DragSourceListener)
	 */
	void addDragSupport(int operations, Transfer[] transferTypes, DragSourceListener listener);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#addDropSupport(int, org.eclipse.swt.dnd.Transfer[],
	 *      org.eclipse.swt.dnd.DropTargetListener)
	 */
	void addDropSupport(int operations, Transfer[] transferTypes, DropTargetListener listener);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#addFilter(org.eclipse.jface.viewers.ViewerFilter)
	 */
	void addFilter(ViewerFilter filter);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#assertElementsNotNull(java.lang.Object[])
	 */
	void assertElementsNotNull(Object[] elements);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#associate(java.lang.Object,
	 *      org.eclipse.swt.widgets.Item)
	 */
	void associate(Object element, Item item);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#disassociate(org.eclipse.swt.widgets.Item)
	 */
	void disassociate(Item item);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#doFindInputItem(java.lang.Object)
	 */
	Widget doFindInputItem(Object element);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#doFindItem(java.lang.Object)
	 */
	Widget doFindItem(Object element);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#doUpdateItem(org.eclipse.swt.widgets.Widget,
	 *      java.lang.Object, boolean)
	 */
	void doUpdateItem(Widget item, Object element, boolean fullMap);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#equals(java.lang.Object, java.lang.Object)
	 */
	boolean equals(Object elementA, Object elementB);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#filter(java.lang.Object[])
	 */
	Object[] filter(Object[] elements);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#fireDoubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
	 */
	void fireDoubleClick(DoubleClickEvent event);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#fireOpen(org.eclipse.jface.viewers.OpenEvent)
	 */
	void fireOpen(OpenEvent event);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#firePostSelectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	void firePostSelectionChanged(SelectionChangedEvent event);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getComparer()
	 */
	IElementComparer getComparer();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getFilteredChildren(java.lang.Object)
	 */
	Object[] getFilteredChildren(Object parent);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getFilters()
	 */
	ViewerFilter[] getFilters();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getItem(int, int)
	 */
	Item getItem(int x, int y);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getRawChildren(java.lang.Object)
	 */
	Object[] getRawChildren(Object parent);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getRoot()
	 */
	Object getRoot();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getSelection()
	 */
	ISelection getSelection();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getSelectionFromWidget()
	 */
	// Suppressing warning since super is raw
	@SuppressWarnings("rawtypes")
	List getSelectionFromWidget();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getSortedChildren(java.lang.Object)
	 */
	Object[] getSortedChildren(Object parent);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getSorter()
	 */
	ViewerSorter getSorter();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getComparator()
	 */
	ViewerComparator getComparator();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#handleDoubleSelect(org.eclipse.swt.events.SelectionEvent)
	 */
	void handleDoubleSelect(SelectionEvent event);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#handleOpen(org.eclipse.swt.events.SelectionEvent)
	 */
	void handleOpen(SelectionEvent event);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#handleInvalidSelection(org.eclipse.jface.viewers.ISelection,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	void handleInvalidSelection(ISelection invalidSelection, ISelection newSelection);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#handleLabelProviderChanged(org.eclipse.jface.viewers.LabelProviderChangedEvent)
	 */
	void handleLabelProviderChanged(LabelProviderChangedEvent event);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#handleSelect(org.eclipse.swt.events.SelectionEvent)
	 */
	void handleSelect(SelectionEvent event);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#handlePostSelect(org.eclipse.swt.events.SelectionEvent)
	 */
	void handlePostSelect(SelectionEvent e);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#hookControl(org.eclipse.swt.widgets.Control)
	 */
	void hookControl(Control control);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#hasFilters()
	 */
	boolean hasFilters();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#internalRefresh(java.lang.Object)
	 */
	void internalRefresh(Object element);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#internalRefresh(java.lang.Object, boolean)
	 */
	void internalRefresh(Object element, boolean updateLabels);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#mapElement(java.lang.Object,
	 *      org.eclipse.swt.widgets.Widget)
	 */
	void mapElement(Object element, Widget item);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#needsRefilter(java.lang.Object, java.lang.String)
	 */
	boolean needsRefilter(Object element, String property);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#preservingSelection(java.lang.Runnable)
	 */
	void preservingSelection(Runnable updateCode);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#refresh()
	 */
	void refresh();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#refresh(boolean)
	 */
	void refresh(boolean updateLabels);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#refresh(java.lang.Object)
	 */
	void refresh(Object element);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#refresh(java.lang.Object, boolean)
	 */
	void refresh(Object element, boolean updateLabels);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#removeOpenListener(org.eclipse.jface.viewers.IOpenListener)
	 */
	void removeOpenListener(IOpenListener listener);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#removePostSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	void removePostSelectionChangedListener(ISelectionChangedListener listener);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#removeDoubleClickListener(org.eclipse.jface.viewers.IDoubleClickListener)
	 */
	void removeDoubleClickListener(IDoubleClickListener listener);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#removeFilter(org.eclipse.jface.viewers.ViewerFilter)
	 */
	void removeFilter(ViewerFilter filter);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setFilters(org.eclipse.jface.viewers.ViewerFilter[])
	 */
	void setFilters(ViewerFilter[] filters);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#resetFilters()
	 */
	void resetFilters();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#reveal(java.lang.Object)
	 */
	void reveal(Object element);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setContentProvider(org.eclipse.jface.viewers.IContentProvider)
	 */
	void setContentProvider(IContentProvider provider);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#assertContentProviderType(org.eclipse.jface.viewers.IContentProvider)
	 */
	void assertContentProviderType(IContentProvider provider);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setSelection(org.eclipse.jface.viewers.ISelection,
	 *      boolean)
	 */
	void setSelection(ISelection selection, boolean reveal);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setSelectionToWidget(java.util.List, boolean)
	 */
	@SuppressWarnings("rawtypes")
	// Suppressing warning since super is raw
	void setSelectionToWidget(List l, boolean reveal);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setSelectionToWidget(org.eclipse.jface.viewers.ISelection,
	 *      boolean)
	 */
	void setSelectionToWidget(ISelection selection, boolean reveal);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setSorter(org.eclipse.jface.viewers.ViewerSorter)
	 */
	void setSorter(ViewerSorter sorter);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setComparator(org.eclipse.jface.viewers.ViewerComparator)
	 */
	void setComparator(ViewerComparator comparator);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setUseHashlookup(boolean)
	 */
	void setUseHashlookup(boolean enable);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setComparer(org.eclipse.jface.viewers.IElementComparer)
	 */
	void setComparer(IElementComparer comparer);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#testFindItem(java.lang.Object)
	 */
	Widget testFindItem(Object element);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#testFindItems(java.lang.Object)
	 */
	Widget[] testFindItems(Object element);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#unmapAllElements()
	 */
	void unmapAllElements();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#unmapElement(java.lang.Object)
	 */
	void unmapElement(Object element);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#unmapElement(java.lang.Object,
	 *      org.eclipse.swt.widgets.Widget)
	 */
	void unmapElement(Object element, Widget item);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#update(java.lang.Object[], java.lang.String[])
	 */
	void update(Object[] elements, String[] properties);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#update(java.lang.Object, java.lang.String[])
	 */
	void update(Object element, String[] properties);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#internalUpdate(org.eclipse.swt.widgets.Widget,
	 *      java.lang.Object, java.lang.String[])
	 */
	void internalUpdate(Widget widget, Object element, String[] properties);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#updateSelection(org.eclipse.jface.viewers.ISelection)
	 */
	void updateSelection(ISelection selection);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#usingElementMap()
	 */
	boolean usingElementMap();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setLabelProvider(org.eclipse.jface.viewers.IBaseLabelProvider)
	 */
	void setLabelProvider(IBaseLabelProvider labelProvider);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#buildLabel(org.eclipse.jface.viewers.ViewerLabel,
	 *      java.lang.Object)
	 */
	void buildLabel(ViewerLabel updateLabel, Object element);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	void handleDispose(DisposeEvent event);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ContentViewer#getContentProvider()
	 */
	IContentProvider getContentProvider();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ContentViewer#getInput()
	 */
	Object getInput();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ContentViewer#getLabelProvider()
	 */
	IBaseLabelProvider getLabelProvider();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ContentViewer#labelProviderChanged()
	 */
	void labelProviderChanged();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#addHelpListener(org.eclipse.swt.events.HelpListener)
	 */
	void addHelpListener(HelpListener listener);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	void addSelectionChangedListener(ISelectionChangedListener listener);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#fireHelpRequested(org.eclipse.swt.events.HelpEvent)
	 */
	void fireHelpRequested(HelpEvent event);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#fireSelectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	void fireSelectionChanged(SelectionChangedEvent event);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getControl()
	 */
	Control getControl();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getData(java.lang.String)
	 */
	Object getData(String key);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#handleHelpRequest(org.eclipse.swt.events.HelpEvent)
	 */
	void handleHelpRequest(HelpEvent event);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#inputChanged(java.lang.Object, java.lang.Object)
	 */
	void inputChanged(Object input, Object oldInput);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#removeHelpListener(org.eclipse.swt.events.HelpListener)
	 */
	void removeHelpListener(HelpListener listener);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	void removeSelectionChangedListener(ISelectionChangedListener listener);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#scrollDown(int, int)
	 */
	Item scrollDown(int x, int y);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#scrollUp(int, int)
	 */
	Item scrollUp(int x, int y);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#setData(java.lang.String, java.lang.Object)
	 */
	void setData(String key, Object value);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	void setSelection(ISelection selection);

}

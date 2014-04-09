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

import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractStructuredViewerWrapper<C extends Control, V extends IWrappableStructuredViewer> extends StructuredViewer {

	/** The wrapped Viewer. */
	private final V fViewer;

	/** A composite control that will contains all sub-control of this wrapper. */
	private final C fControl;

	/** The compare configuration object. */
	private final EMFCompareConfiguration fConfiguration;

	/** The selection changed listener of the wrapped viewer. */
	private final ISelectionChangedListener fWrappedViewerSelectionChangedListener;

	/** The help listener of the wrapped viewer. */
	private final HelpListener fWrappedViewerHelpListener;

	private final IDoubleClickListener fWrappedViewerDoubleClickListener;

	private final ISelectionChangedListener fWrappedViewerPostSelectionChangedListener;

	private final IOpenListener fWrappedViewerOpenListener;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            the SWT parent control under which to create the viewer's SWT control.
	 */
	public AbstractStructuredViewerWrapper(Composite parent, EMFCompareConfiguration compareConfiguration) {
		fConfiguration = compareConfiguration;

		preHookCreateControlAndViewer();
		ControlAndViewer<C, V> cv = createControlAndViewer(parent);
		fControl = cv.getControl();
		fViewer = cv.getViewer();
		hookControl(fControl);

		fWrappedViewerSelectionChangedListener = new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				fireSelectionChanged(new SelectionChangedEvent(AbstractStructuredViewerWrapper.this, event
						.getSelection()));
			}
		};
		fViewer.addSelectionChangedListener(fWrappedViewerSelectionChangedListener);

		fWrappedViewerHelpListener = new HelpListener() {
			public void helpRequested(HelpEvent e) {
				Event event = new Event();
				event.widget = fControl;
				event.display = e.display;
				event.widget = e.widget;
				event.time = e.time;
				event.data = e.data;
				fireHelpRequested(new HelpEvent(event));
			}
		};
		fViewer.addHelpListener(fWrappedViewerHelpListener);

		fWrappedViewerDoubleClickListener = new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				fireDoubleClick(new DoubleClickEvent(AbstractStructuredViewerWrapper.this, event
						.getSelection()));
			}
		};
		fViewer.addDoubleClickListener(fWrappedViewerDoubleClickListener);

		fWrappedViewerPostSelectionChangedListener = new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				firePostSelectionChanged(new SelectionChangedEvent(AbstractStructuredViewerWrapper.this,
						event.getSelection()));
			}
		};
		fViewer.addPostSelectionChangedListener(fWrappedViewerPostSelectionChangedListener);

		fWrappedViewerOpenListener = new IOpenListener() {
			public void open(OpenEvent event) {
				fireOpen(new OpenEvent(AbstractStructuredViewerWrapper.this, event.getSelection()));
			}
		};
		fViewer.addOpenListener(fWrappedViewerOpenListener);

	}

	/**
	 * Get the compare configuration object.
	 * 
	 * @return the compare configuration object.
	 */
	protected final EMFCompareConfiguration getCompareConfiguration() {
		return fConfiguration;
	}

	/**
	 * 
	 */
	protected void preHookCreateControlAndViewer() {

	}

	/**
	 * Should call {@link #setViewer(org.eclipse.jface.viewers.Viewer)}.
	 * 
	 * @param parent
	 *            the SWT control under which to create the viewer.
	 * @return a composite control that will contains all sub-control of this wrapper.
	 */
	protected abstract ControlAndViewer<C, V> createControlAndViewer(Composite parent);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getControl()
	 */
	@Override
	public final C getControl() {
		return fControl;
	}

	/**
	 * Returns the {@link StructuredViewer} associated with this wrapper.
	 * 
	 * @return a StructuredViewer.
	 */
	protected final V getViewer() {
		return fViewer;
	}

	@Override
	protected void handleDispose(DisposeEvent event) {
		fViewer.removeOpenListener(fWrappedViewerOpenListener);
		fViewer.removePostSelectionChangedListener(fWrappedViewerPostSelectionChangedListener);
		fViewer.removeDoubleClickListener(fWrappedViewerDoubleClickListener);
		fViewer.removeSelectionChangedListener(fWrappedViewerSelectionChangedListener);
		fViewer.removeHelpListener(fWrappedViewerHelpListener);
		super.handleDispose(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setContentProvider(org.eclipse.jface.viewers.IContentProvider)
	 */
	@Override
	public final void setContentProvider(IContentProvider provider) {
		fViewer.setContentProvider(provider);
		super.setContentProvider(provider);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setLabelProvider(org.eclipse.jface.viewers.IBaseLabelProvider)
	 */
	@Override
	public final void setLabelProvider(IBaseLabelProvider labelProvider) {
		fViewer.setLabelProvider(labelProvider);
		super.setLabelProvider(labelProvider);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#addDragSupport(int, org.eclipse.swt.dnd.Transfer[],
	 *      org.eclipse.swt.dnd.DragSourceListener)
	 */
	@Override
	public final void addDragSupport(int operations, Transfer[] transferTypes, DragSourceListener listener) {
		fViewer.addDragSupport(operations, transferTypes, listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#addDropSupport(int, org.eclipse.swt.dnd.Transfer[],
	 *      org.eclipse.swt.dnd.DropTargetListener)
	 */
	@Override
	public final void addDropSupport(int operations, Transfer[] transferTypes, DropTargetListener listener) {
		fViewer.addDropSupport(operations, transferTypes, listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#doFindInputItem(java.lang.Object)
	 */
	@Override
	protected final Widget doFindInputItem(Object element) {
		return fViewer.doFindInputItem(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#doFindItem(java.lang.Object)
	 */
	@Override
	protected final Widget doFindItem(Object element) {
		return fViewer.doFindInputItem(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#doUpdateItem(org.eclipse.swt.widgets.Widget,
	 *      java.lang.Object, boolean)
	 */
	@Override
	protected final void doUpdateItem(Widget item, Object element, boolean fullMap) {
		fViewer.doUpdateItem(item, element, fullMap);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getComparer()
	 */
	@Override
	public final IElementComparer getComparer() {
		return fViewer.getComparer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#addFilter(org.eclipse.jface.viewers.ViewerFilter)
	 */
	@Override
	public final void addFilter(ViewerFilter filter) {
		fViewer.addFilter(filter);
		super.addFilter(filter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getFilters()
	 */
	@Override
	public final ViewerFilter[] getFilters() {
		return fViewer.getFilters();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#removeFilter(org.eclipse.jface.viewers.ViewerFilter)
	 */
	@Override
	public final void removeFilter(ViewerFilter filter) {
		fViewer.removeFilter(filter);
		super.removeFilter(filter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setFilters(org.eclipse.jface.viewers.ViewerFilter[])
	 */
	@Override
	public final void setFilters(ViewerFilter[] filters) {
		fViewer.setFilters(filters);
		super.setFilters(filters);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#resetFilters()
	 */
	@Override
	public final void resetFilters() {
		fViewer.resetFilters();
		super.resetFilters();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#reveal(java.lang.Object)
	 */
	@Override
	public final void reveal(Object element) {
		fViewer.reveal(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getSelectionFromWidget()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected final List getSelectionFromWidget() {
		return fViewer.getSelectionFromWidget();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getSorter()
	 */
	@Override
	public final ViewerSorter getSorter() {
		return fViewer.getSorter();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getComparator()
	 */
	@Override
	public final ViewerComparator getComparator() {
		return fViewer.getComparator();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setSelectionToWidget(java.util.List, boolean)
	 */
	// Suppressing warning : super is raw
	@SuppressWarnings("rawtypes")
	@Override
	protected final void setSelectionToWidget(List l, boolean reveal) {
		fViewer.setSelectionToWidget(l, reveal);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#handleInvalidSelection(org.eclipse.jface.viewers.ISelection,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	protected final void handleInvalidSelection(ISelection invalidSelection, ISelection newSelection) {
		fViewer.handleInvalidSelection(invalidSelection, newSelection);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setSorter(org.eclipse.jface.viewers.ViewerSorter)
	 */
	@Override
	public final void setSorter(ViewerSorter sorter) {
		fViewer.setSorter(sorter);
		super.setSorter(sorter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setComparator(org.eclipse.jface.viewers.ViewerComparator)
	 */
	@Override
	public final void setComparator(ViewerComparator comparator) {
		fViewer.setComparator(comparator);
		super.setComparator(comparator);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setUseHashlookup(boolean)
	 */
	@Override
	public final void setUseHashlookup(boolean enable) {
		fViewer.setUseHashlookup(enable);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setComparer(org.eclipse.jface.viewers.IElementComparer)
	 */
	@Override
	public final void setComparer(IElementComparer comparer) {
		fViewer.setComparer(comparer);
	}

	public static final class ControlAndViewer<C extends Control, V extends IWrappableStructuredViewer> {
		private final C control;

		private final V viewer;

		private ControlAndViewer(C control, V viewer) {
			this.control = control;
			this.viewer = viewer;
		}

		/**
		 * @return the c
		 */
		public C getControl() {
			return control;
		}

		/**
		 * @return the v
		 */
		public V getViewer() {
			return viewer;
		}

		public static <C extends Control, V extends IWrappableStructuredViewer> ControlAndViewer<C, V> create(
				C control, V viewer) {
			return new ControlAndViewer<C, V>(control, viewer);
		}
	}

}

/*******************************************************************************
 * Copyright (c) 2013 Obeo.
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

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * A control that wrapped a StructuredViewer and another control.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public abstract class AbstractViewerWrapper extends StructuredViewer {

	/** The StructuredViewer associated with this wrapper. */
	private StructuredViewer fViewer;

	/** A composite control that will contains all sub-control of this wrapper. */
	private final Control fControl;

	/** The selection changed listener. */
	private ISelectionChangedListener fWrappedViewerListener;

	/** The compare configuration object. */
	private final CompareConfiguration fConfiguration;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            the SWT parent control under which to create the viewer's SWT control.
	 * @param config
	 *            a compare configuration the newly created viewer might want to use.
	 */
	public AbstractViewerWrapper(Composite parent, CompareConfiguration config) {
		fConfiguration = config;
		fControl = createControl(parent, config);
		hookControl(fControl);
		fWrappedViewerListener = new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				fireSelectionChanged(new SelectionChangedEvent(AbstractViewerWrapper.this, event
						.getSelection()));
			}
		};
		fViewer.addSelectionChangedListener(fWrappedViewerListener);

		setLabelProvider(fViewer.getLabelProvider());
		setContentProvider(fViewer.getContentProvider());
	}

	/**
	 * Should call {@link #setViewer(org.eclipse.jface.viewers.Viewer)}.
	 * 
	 * @param parent
	 *            the SWT control under which to create the viewer.
	 * @param config
	 *            the compare configuration object.
	 * @return a composite control that will contains all sub-control of this wrapper.
	 */
	protected abstract Control createControl(Composite parent, CompareConfiguration config);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#addOpenListener(IOpenListener)
	 */
	@Override
	public void addOpenListener(IOpenListener listener) {
		fViewer.addOpenListener(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#removeOpenListener(IOpenListener)
	 */
	@Override
	public void removeOpenListener(IOpenListener listener) {
		fViewer.removeOpenListener(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#update(Object, String[])
	 */
	@Override
	public void update(Object element, String[] properties) {
		fViewer.update(element, properties);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getControl()
	 */
	@Override
	public Control getControl() {
		return fControl;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ContentViewer#getInput()
	 */
	@Override
	public Object getInput() {
		return super.getInput();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getSelection()
	 */
	@Override
	public ISelection getSelection() {
		return fViewer.getSelection();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#refresh()
	 */
	@Override
	public void refresh() {
		fViewer.refresh();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#refresh(boolean)
	 */
	@Override
	public void refresh(boolean updateLabels) {
		fViewer.refresh(updateLabels);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setSelection(ISelection, boolean)
	 */
	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		fViewer.setSelection(selection, reveal);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#reveal(Object)
	 */
	@Override
	public void reveal(Object element) {
		fViewer.reveal(element);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ContentViewer#getContentProvider()
	 */
	@Override
	public IContentProvider getContentProvider() {
		return super.getContentProvider();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ContentViewer#setContentProvider(IContentProvider)
	 */
	@Override
	public void setContentProvider(IContentProvider provider) {
		super.setContentProvider(provider);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ContentViewer#getLabelProvider()
	 */
	@Override
	public IBaseLabelProvider getLabelProvider() {
		return super.getLabelProvider();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ContentViewer#setLabelProvider(IBaseLabelProvider)
	 */
	@Override
	public void setLabelProvider(IBaseLabelProvider labelProvider) {
		super.setLabelProvider(labelProvider);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#doFindInputItem(Object)
	 */
	@Override
	protected Widget doFindInputItem(Object element) {
		/*
		 * Nothing to do here. The method doFindInputItem(Object element) is only called by
		 * StructuredViewer#update(Object element, String[] properties), which is override in this class.
		 */
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#doFindItem(Object)
	 */
	@Override
	protected Widget doFindItem(Object element) {
		/*
		 * Nothing to do here. The method doFindItem(Object element) is only called by
		 * StructuredViewer#update(Object element, String[] properties), which is override in this class.
		 */
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#doUpdateItem(Widget, Object, boolean)
	 */
	@Override
	protected void doUpdateItem(Widget item, Object element, boolean fullMap) {
		/*
		 * Nothing to do here. The method doUpdateItem(Widget item, Object element, boolean fullMap) is only
		 * called by StructuredViewer#update(Object element, String[] properties), which is override in this
		 * class.
		 */
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getSelectionFromWidget()
	 */
	@Override
	protected List getSelectionFromWidget() {
		/*
		 * Nothing to do here. The method getSelectionFromWidget() is only called by
		 * StructuredViewer#getSelection(), which is override in this class.
		 */
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#internalRefresh(Object)
	 */
	@Override
	protected void internalRefresh(Object element) {
		/*
		 * Nothing to do here. The method internalRefresh(Object element) is only called by
		 * StructuredViewer#internalRefresh(Object element, boolean updateLabels), which is override in
		 * AbstractTreeViewer.
		 */
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setSelectionToWidget(List, boolean)
	 */
	@Override
	protected void setSelectionToWidget(List l, boolean reveal) {
		/*
		 * Nothing to do here. The method setSelectionToWidget(List l, boolean reveal) is only called by
		 * StructuredViewer#setSelectionToWidget(ISelection selection, boolean reveal), which is override in
		 * AbstractTreeViewer.
		 */
	}

	/**
	 * Adds event listener hooks to the given control.
	 * <p>
	 * All subclasses must call this method when their control is first established.
	 * </p>
	 * <p>
	 * The <code>ContentViewer</code> implementation of this method hooks dispose events for the given
	 * control. Subclasses may override if they need to add other control hooks; however,
	 * <code>super.hookControl</code> must be invoked.
	 * </p>
	 * 
	 * @param control
	 *            the control
	 */
	@Override
	protected void hookControl(Control control) {
		control.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				handleDispose(event);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#handleDispose(DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		fViewer.removeSelectionChangedListener(fWrappedViewerListener);
	}

	/**
	 * Returns the {@link StructuredViewer} associated with this wrapper.
	 * 
	 * @return a StructuredViewer.
	 */
	protected StructuredViewer getViewer() {
		return fViewer;
	}

	/**
	 * Set the {@link StructuredViewer} of this wrapper.
	 * 
	 * @param fViewer
	 *            a StructuredViewer.
	 */
	protected void setViewer(StructuredViewer viewer) {
		fViewer = viewer;
	}

	/**
	 * Get the compare configuration object.
	 * 
	 * @return the compare configuration object.
	 */
	public CompareConfiguration getCompareConfiguration() {
		return fConfiguration;
	}

}

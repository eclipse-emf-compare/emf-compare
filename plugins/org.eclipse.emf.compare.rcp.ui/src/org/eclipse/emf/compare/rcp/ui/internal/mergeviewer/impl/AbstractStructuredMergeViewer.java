/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl;

import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A specific {@link AbstractMergeViewer} for the EMF Compare Editor.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 4.0
 */
public abstract class AbstractStructuredMergeViewer extends AbstractMergeViewer {

	/** The primary control associated with this viewer. */
	private final Control fControl;

	/** A listener which is notified when a viewer's selection changes. */
	private final ISelectionChangedListener fForwardingSelectionListener;

	/**
	 * Default constructor.
	 * 
	 * @param parent
	 *            the parent widget.
	 * @param side
	 *            the side of the viewer.
	 * @param compareConfiguration
	 *            the compare configuration object used by this viewer.
	 */
	public AbstractStructuredMergeViewer(Composite parent, MergeViewerSide side,
			IEMFCompareConfiguration compareConfiguration) {
		super(side, compareConfiguration);

		fControl = createControl(parent);
		hookControl(fControl);

		fForwardingSelectionListener = new ForwardingViewerSelectionListener();
		getStructuredViewer().addSelectionChangedListener(fForwardingSelectionListener);
	}

	/**
	 * Creates the primary control associated with this viewer.
	 * 
	 * @param parent
	 *            the parent widget of this viewer.
	 * @return the created primary control associated with this viewer.
	 */
	protected abstract Control createControl(Composite parent);

	/**
	 * Returns the wrapped {@link StructuredViewer}.
	 * 
	 * @return the wrapped {@link StructuredViewer}.
	 */
	protected abstract StructuredViewer getStructuredViewer();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getControl()
	 */
	@Override
	public Control getControl() {
		return fControl;
	}

	@Override
	protected void handleDispose(DisposeEvent event) {
		getStructuredViewer().removeSelectionChangedListener(fForwardingSelectionListener);
		hookDispose();
		super.handleDispose(event);
	}

	protected abstract void hookDispose();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	@Override
	public ISelection getSelection() {
		return getStructuredViewer().getSelection();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#setSelection(org.eclipse.jface.viewers.ISelection, boolean)
	 */
	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		getStructuredViewer().setSelection(selection, reveal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setContentProvider(IContentProvider contentProvider) {
		super.setContentProvider(contentProvider);
		getStructuredViewer().setContentProvider(contentProvider);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLabelProvider(IBaseLabelProvider labelProvider) {
		super.setLabelProvider(labelProvider);
		getStructuredViewer().setLabelProvider(labelProvider);
	}

	/**
	 * A specific implementation of {@link ISelectionChangedListener} for the AbstractStructuredMergeViewer.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 * @since 4.0
	 */
	private class ForwardingViewerSelectionListener implements ISelectionChangedListener {
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
		 */
		public void selectionChanged(SelectionChangedEvent event) {
			fireSelectionChanged(
					new SelectionChangedEvent(AbstractStructuredMergeViewer.this, event.getSelection()));
		}

	}
}

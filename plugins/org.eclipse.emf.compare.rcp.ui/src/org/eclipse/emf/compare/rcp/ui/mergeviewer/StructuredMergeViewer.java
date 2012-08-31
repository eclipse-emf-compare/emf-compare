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
package org.eclipse.emf.compare.rcp.ui.mergeviewer;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class StructuredMergeViewer extends MergeViewer {

	private final Control fControl;

	private final ISelectionChangedListener fForwardingSelectionListener;

	/**
	 * @param parent
	 * @param side
	 */
	public StructuredMergeViewer(Composite parent, MergeViewerSide side) {
		super(side);

		fControl = createControl(parent);
		hookControl(fControl);

		fForwardingSelectionListener = new ForwardingViewerSelectionListener();
		getStructuredViewer().addSelectionChangedListener(fForwardingSelectionListener);
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

	@Override
	protected void handleDispose(DisposeEvent event) {
		getStructuredViewer().removeSelectionChangedListener(fForwardingSelectionListener);
		super.handleDispose(event);
	}

	protected abstract Control createControl(Composite parent);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.MergeViewer#getStructuredViewer()
	 */
	@Override
	protected abstract StructuredViewer getStructuredViewer();

	private class ForwardingViewerSelectionListener implements ISelectionChangedListener {
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
		 */
		public void selectionChanged(SelectionChangedEvent event) {
			fireSelectionChanged(new SelectionChangedEvent(StructuredMergeViewer.this, event.getSelection()));
		}

	}
}

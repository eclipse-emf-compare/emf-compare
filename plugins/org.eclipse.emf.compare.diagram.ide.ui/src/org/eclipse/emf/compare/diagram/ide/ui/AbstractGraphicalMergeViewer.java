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
package org.eclipse.emf.compare.diagram.ide.ui;

import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.gef.ui.parts.AbstractEditPartViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * Abstract graphical merge viewer for comparison of graphical elements.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 3.0
 */
public abstract class AbstractGraphicalMergeViewer extends AbstractEditPartMergeViewer {

	/** The diagram edit domain. */
	protected DiagramEditDomain editDomain;

	/** Listener for forwarding selection. */
	private final ISelectionChangedListener fForwardingSelectionListener;

	/**
	 * The constructor.
	 * 
	 * @param parent
	 *            The parent composite.
	 * @param side
	 *            The side having to be handle.
	 */
	public AbstractGraphicalMergeViewer(Composite parent, MergeViewerSide side) {
		super(side);

		editDomain = new DiagramEditDomain(null);
		editDomain.setCommandStack(new DiagramCommandStack(editDomain));

		setControl(createControl(parent));
		hookControl();

		fForwardingSelectionListener = new ForwardingViewerSelectionListener();
		// getGraphicalViewer().addSelectionChangedListener(fForwardingSelectionListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		// getGraphicalViewer().removeSelectionChangedListener(fForwardingSelectionListener);
		super.handleDispose(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.AbstractEditPartMergeViewer#getGraphicalViewer()
	 */
	@Override
	protected abstract AbstractEditPartViewer getGraphicalViewer();

	/**
	 * Listener for forwarding selection.
	 */
	private class ForwardingViewerSelectionListener implements ISelectionChangedListener {
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
		 */
		public void selectionChanged(SelectionChangedEvent event) {
			fireSelectionChanged();
		}

	}
}

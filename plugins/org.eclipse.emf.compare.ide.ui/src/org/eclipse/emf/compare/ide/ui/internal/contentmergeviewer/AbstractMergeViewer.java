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
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractMergeViewer<C extends Composite> implements IMergeViewer<C> {

	private final ListenerList fSelectionChangedListeners;

	private final MergeViewerSide fSide;

	private final StructuredViewer fStructuredViewer;

	private final ForwardingViewerSelectionListener fForwardingSelectionListener;

	/**
	 * 
	 */
	public AbstractMergeViewer(Composite parent, MergeViewerSide side) {
		fStructuredViewer = createStructuredViewer(parent);
		fSide = side;
		fSelectionChangedListeners = new ListenerList();

		fForwardingSelectionListener = new ForwardingViewerSelectionListener();
		fStructuredViewer.addSelectionChangedListener(fForwardingSelectionListener);
	}

	protected abstract StructuredViewer createStructuredViewer(Composite parent);

	protected StructuredViewer getStructuredViewer() {
		return fStructuredViewer;
	}

	public void dispose() {
		fStructuredViewer.removeSelectionChangedListener(fForwardingSelectionListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer#getSide()
	 */
	public MergeViewerSide getSide() {
		return fSide;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		fSelectionChangedListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	public ISelection getSelection() {
		return getStructuredViewer().getSelection();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		fSelectionChangedListeners.remove(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	public void setSelection(ISelection selection) {
		Control control = getControl();
		if (control == null || control.isDisposed()) {
			return;
		}
		getStructuredViewer().setSelection(selection, true);
		ISelection sel = getSelection();
		fireSelectionChanged(new SelectionChangedEvent(this, sel));
	}

	/**
	 * @param selectionChangedEvent
	 */
	private void fireSelectionChanged(final SelectionChangedEvent event) {
		Object[] listeners = fSelectionChangedListeners.getListeners();
		for (int i = 0; i < listeners.length; ++i) {
			final ISelectionChangedListener l = (ISelectionChangedListener)listeners[i];
			SafeRunnable.run(new SafeRunnable() {
				public void run() {
					l.selectionChanged(event);
				}
			});
		}
	}

	private class ForwardingViewerSelectionListener implements ISelectionChangedListener {
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
		 */
		public void selectionChanged(SelectionChangedEvent event) {
			fireSelectionChanged(new SelectionChangedEvent(AbstractMergeViewer.this, event.getSelection()));
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer#setContentProvider(org.eclipse.jface.viewers.IContentProvider)
	 */
	public void setContentProvider(IContentProvider contentProvider) {
		getStructuredViewer().setContentProvider(contentProvider);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer#setLabelProvider(org.eclipse.jface.viewers.ILabelProvider)
	 */
	public void setLabelProvider(ILabelProvider labelProvider) {
		getStructuredViewer().setLabelProvider(labelProvider);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IInputProvider#getInput()
	 */
	public Object getInput() {
		return getStructuredViewer().getInput();
	}
}

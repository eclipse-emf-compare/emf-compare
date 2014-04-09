/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.IViewerCreator;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Creates an empty viewer with a single centered label which say to wait for the comparison to finish.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class WaitViewerCreator implements IViewerCreator {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.IViewerCreator#createViewer(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.compare.CompareConfiguration)
	 */
	public Viewer createViewer(final Composite parent, CompareConfiguration config) {
		return new WaitViewer(parent);
	}

	/**
	 * A dummy viewer that display a single label.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private final class WaitViewer extends Viewer {
	
		/** The Control as returned by {@link #getControl()}. */
		private final Composite control;
	
		/**
		 * Creates a new viewer and its controls.
		 * 
		 * @param parent
		 *            the parent of the {@link #getControl() control} of this viewer.
		 */
		private WaitViewer(Composite parent) {
			control = new Composite(parent, SWT.NONE);
			GridLayout layout = new GridLayout(1, true);
			control.setLayout(layout);
			Label label = new Label(control, SWT.NONE);
			label.setText(EMFCompareIDEUIMessages.getString("wait.viewer.desc")); //$NON-NLS-1$
			label.setFont(JFaceResources.getBannerFont());
			label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
			control.setData(CompareUI.COMPARE_VIEWER_TITLE, EMFCompareIDEUIMessages
					.getString("wait.viewer.title")); //$NON-NLS-1$
	
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.Viewer#setSelection(org.eclipse.jface.viewers.ISelection, boolean)
		 */
		@Override
		public void setSelection(ISelection selection, boolean reveal) {
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.Viewer#setInput(java.lang.Object)
		 */
		@Override
		public void setInput(Object input) {
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.Viewer#refresh()
		 */
		@Override
		public void refresh() {
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.Viewer#getSelection()
		 */
		@Override
		public ISelection getSelection() {
			return StructuredSelection.EMPTY;
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.Viewer#getInput()
		 */
		@Override
		public Object getInput() {
			return null;
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.viewers.Viewer#getControl()
		 */
		@Override
		public Control getControl() {
			return control;
		}
	}

}

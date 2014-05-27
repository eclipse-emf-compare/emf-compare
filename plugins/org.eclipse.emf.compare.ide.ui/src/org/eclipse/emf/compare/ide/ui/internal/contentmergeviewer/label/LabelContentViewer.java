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
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label;

import org.eclipse.compare.CompareUI;
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
 * A dummy viewer that displays a single label.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class LabelContentViewer extends Viewer {

	/** The Control as returned by {@link #getControl()}. */
	private final Composite control;

	private Object input;

	/**
	 * Creates a new viewer and its controls.
	 * 
	 * @param parent
	 *            the parent of the {@link #getControl() control} of this viewer.
	 */
	public LabelContentViewer(Composite parent, String title, String message) {
		control = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		control.setLayout(layout);
		Label label = new Label(control, SWT.NONE);
		label.setText(message);
		label.setFont(JFaceResources.getBannerFont());
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		control.setData(CompareUI.COMPARE_VIEWER_TITLE, title);

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
		this.input = input;
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
		return input;
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

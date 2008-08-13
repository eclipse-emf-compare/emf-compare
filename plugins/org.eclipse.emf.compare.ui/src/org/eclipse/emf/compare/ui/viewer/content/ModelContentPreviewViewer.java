/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Moritz Eysholdt <Moritz@Eysholdt.de> - initial API and implementation

 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.content;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.emf.compare.ui.IModelCompareInputProvider;
import org.eclipse.ltk.ui.refactoring.ChangePreviewViewerInput;
import org.eclipse.ltk.ui.refactoring.IChangePreviewViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * This PreviewVierwer integrates itself into the LTK and thereby makes the {@link ModelContentMergeViewer}
 * available to visualize diffs within the LTK's refactoring wizard.
 * 
 * @author Moritz Eysholdt
 */
public class ModelContentPreviewViewer implements IChangePreviewViewer {
    /** This will hold a reference to the view itself. */
	private ModelContentMergeViewer view;

	/** This will be used to display the preview. */
	private CompareViewerPane pane;

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ltk.ui.refactoring.IChangePreviewViewer#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		final CompareConfiguration conf = new CompareConfiguration();
		pane = new CompareViewerPane(parent, SWT.NONE);
		view = new ModelContentMergeViewer(pane, conf);
		pane.setContent(view.getControl());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ltk.ui.refactoring.IChangePreviewViewer#getControl()
	 */
	public Control getControl() {
		return pane;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ltk.ui.refactoring.IChangePreviewViewer#setInput(org.eclipse.ltk.ui.refactoring.ChangePreviewViewerInput)
	 */
	public void setInput(ChangePreviewViewerInput in) {
		final IModelCompareInputProvider p = (IModelCompareInputProvider)in.getChange();
		view.setInput(p.getModelCompareInput());
	}

}

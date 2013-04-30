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
package org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl;

import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.ICompareAccessor;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.ICompareColor;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.IMergeViewerItem.Container;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class TreeMergeViewer extends TableOrTreeMergeViewer {

	private Object fInput;

	private TreeViewer fTreeViewer;

	/**
	 * @param parent
	 */
	public TreeMergeViewer(Composite parent, MergeViewerSide side, ICompareColor.Provider colorProvider) {
		super(parent, side, colorProvider);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.impl.AbstractMergeViewer#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginLeft = -1;
		layout.marginRight = -1;
		layout.marginTop = -1;
		layout.marginBottom = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);

		fTreeViewer = new TreeViewer(composite);
		fTreeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		return composite;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.AbstractMergeViewer#getStructuredViewer()
	 */
	@Override
	public TreeViewer getStructuredViewer() {
		return fTreeViewer;
	}

	public void setExpandedState(Object elementOrTreePath, boolean expanded) {
		getStructuredViewer().setExpandedState(elementOrTreePath, expanded);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#inputChanged(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void inputChanged(Object input, Object oldInput) {
		if (input instanceof ICompareAccessor) {
			fInput = input;
			getStructuredViewer().setInput(input);
			IMergeViewerItem initialItem = ((ICompareAccessor)input).getInitialItem();
			if (initialItem != null) {
				if (oldInput instanceof ICompareAccessor) {
					IMergeViewerItem initialOldItem = ((ICompareAccessor)oldInput).getInitialItem();
					if (initialOldItem != null) {
						Container parent = initialOldItem.getParent();
						if (parent != null) {
							setExpandedState(parent, false);
						}
					}
				}
				getStructuredViewer().setSelection(new StructuredSelection(initialItem), true);
			}
		} else {
			getStructuredViewer().setInput(null);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IInputProvider#getInput()
	 */
	@Override
	public Object getInput() {
		return fInput;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#refresh()
	 */
	@Override
	public void refresh() {
		fTreeViewer.refresh();
	}
}

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

import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor.IStructuralFeatureAccessor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MergeViewerInfoViewer extends ContentViewer {

	private final MergeViewerSide fSide;

	private final Composite fControl;

	private final Label fEObjectIcon;

	private final Label fEObjectLabel;

	private final Label fFeatureIcon;

	private final Label fFeatureLabel;

	private ISelection fSelection;

	private Object fInput;

	/**
	 * 
	 */
	public MergeViewerInfoViewer(Composite parent, MergeViewerSide side) {
		this.fControl = new Composite(parent, SWT.BORDER);
		this.fSide = side;

		fControl.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		GridLayout layout = new GridLayout(3, false);
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		layout.marginLeft = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.marginBottom = 0;
		fControl.setLayout(layout);

		Composite eObjectComposite = new Composite(fControl, SWT.NONE);
		GridLayout eObjectCompositelayout = new GridLayout(2, false);
		eObjectCompositelayout.verticalSpacing = 0;
		eObjectCompositelayout.horizontalSpacing = 0;
		eObjectCompositelayout.marginLeft = 0;
		eObjectCompositelayout.marginHeight = 0;
		eObjectCompositelayout.marginWidth = 0;
		eObjectCompositelayout.marginBottom = 0;
		eObjectComposite.setLayout(eObjectCompositelayout);
		fEObjectIcon = new Label(eObjectComposite, SWT.NONE);
		fEObjectIcon.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		fEObjectLabel = new Label(eObjectComposite, SWT.NONE);
		fEObjectLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		eObjectComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true, 3, 1));

		Label lblIn = new Label(fControl, SWT.NONE);
		lblIn.setText("    "); //$NON-NLS-1$

		fFeatureIcon = new Label(fControl, SWT.NONE);
		fFeatureIcon.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		fFeatureLabel = new Label(fControl, SWT.NONE);
		fFeatureLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		hookControl(fControl);
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
	 * @see org.eclipse.jface.viewers.Viewer#inputChanged(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void inputChanged(Object input, Object oldInput) {
		fInput = input;
		fControl.setRedraw(false);
		try {
			refresh();
		} finally {
			fControl.setRedraw(true);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getSelection()
	 */
	@Override
	public ISelection getSelection() {
		return fSelection;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#refresh()
	 */
	@Override
	public void refresh() {
		if (fInput instanceof IStructuralFeatureAccessor) {
			IStructuralFeatureAccessor featureAccessor = (IStructuralFeatureAccessor)fInput;

			EObject eObject = featureAccessor.getEObject(fSide);
			if (eObject == null) {
				if (fSide != MergeViewerSide.ANCESTOR) {
					eObject = featureAccessor.getEObject(MergeViewerSide.ANCESTOR);
					if (eObject == null) {
						eObject = featureAccessor.getEObject(fSide.opposite());
					}
				} else {
					eObject = featureAccessor.getEObject(MergeViewerSide.LEFT);
					if (eObject == null) {
						eObject = featureAccessor.getEObject(MergeViewerSide.RIGHT);
					}
				}
			}
			EStructuralFeature structuralFeature = featureAccessor.getStructuralFeature();

			if (getLabelProvider() instanceof ILabelProvider) {
				ILabelProvider labelProvider = (ILabelProvider)getLabelProvider();
				fFeatureIcon.setImage(labelProvider.getImage(structuralFeature));
				fFeatureLabel.setText(labelProvider.getText(structuralFeature));

				fEObjectIcon.setImage(labelProvider.getImage(eObject));
				fEObjectLabel.setText(labelProvider.getText(eObject));
			}

			fControl.layout(true);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#setSelection(org.eclipse.jface.viewers.ISelection, boolean)
	 */
	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		fSelection = selection;
	}

}

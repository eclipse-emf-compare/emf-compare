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

import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IStructuralFeatureAccessor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MergeViewerInfoComposite extends Composite {

	private final Label featureLabel;

	private final Label eObjectLabel;

	private final Label featureIcon;

	private final Label eObjectIcon;

	private ILabelProvider fLabelProvider;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public MergeViewerInfoComposite(Composite parent) {
		super(parent, SWT.BORDER);
		setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		GridLayout layout = new GridLayout(6, false);
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		layout.marginLeft = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.marginBottom = 0;
		setLayout(layout);

		Label changesLabel = new Label(this, SWT.NONE);
		changesLabel.setText("Changes in"); //$NON-NLS-1$

		featureIcon = new Label(this, SWT.NONE);
		featureIcon.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		featureLabel = new Label(this, SWT.NONE);
		featureLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Label lblIn = new Label(this, SWT.NONE);
		lblIn.setText("of"); //$NON-NLS-1$

		eObjectIcon = new Label(this, SWT.NONE);
		eObjectIcon.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));

		eObjectLabel = new Label(this, SWT.NONE);
		eObjectLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	}

	public void setInput(IStructuralFeatureAccessor featureAccessor, MergeViewerSide side) {
		EStructuralFeature structuralFeature = featureAccessor.getStructuralFeature();
		featureIcon.setImage(fLabelProvider.getImage(structuralFeature));
		featureLabel.setText(structuralFeature.getName());

		EObject eObject = featureAccessor.getEObject(side);
		if (eObject == null) {
			eObject = featureAccessor.getEObject(side.opposite());
		}
		eObjectIcon.setImage(fLabelProvider.getImage(eObject));
		eObjectLabel.setText(fLabelProvider.getText(eObject));

		getParent().layout();
		layout();
	}

	/**
	 * @param labelProvider
	 */
	public void setLabelProvider(ILabelProvider labelProvider) {
		this.fLabelProvider = labelProvider;
	}
}

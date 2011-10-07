/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Obeo - initial API and implementation
 *******************************************************************************/

package org.eclipse.emf.compare.diagram.ui.mergeviewer;

import org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer;
import org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab;
import org.eclipse.emf.compare.ui.viewer.content.part.ParameterizedContentMergeTabFolder;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Define a ModelContentMergeTabFolder for GMF comparison.
 * 
 * @author <a href="mailto:stephane.bouchet@obeo.fr">Stephane Bouchet</a>
 */
public class GMFContentMergeTabFolder extends ParameterizedContentMergeTabFolder {

	/** the CTabItem used to display the gmfContentMergeViewerTab. */
	protected CTabItem gmfTab;

	/** the GMF Tab to display graphical informations. */
	private IModelContentMergeViewerTab gmfContentMergeViewerTab;

	/**
	 * Constructor.
	 * 
	 * @param viewer
	 *            Parent viewer of this viewer part.
	 * @param composite
	 *            Parent {@link Composite} for this part.
	 * @param side
	 *            Comparison side of this part.
	 */
	public GMFContentMergeTabFolder(ModelContentMergeViewer viewer, Composite composite, int side) {
		super(viewer, composite, side);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabFolder#createTabFolder(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected CTabFolder createTabFolder(Composite parent) {
		// create default tabs
		tabFolder = super.createTabFolder(parent);
		// create gmf viewer tab
		gmfTab = new CTabItem(tabFolder, SWT.NONE);
		gmfTab.setText("Graphical Differences");
		final Composite gmfPanel = new Composite(tabFolder, SWT.NONE);
		gmfPanel.setLayout(new GridLayout());
		gmfPanel.setLayoutData(new GridData(GridData.FILL_BOTH));
		gmfPanel.setFont(parent.getFont());
		gmfContentMergeViewerTab = createGmfPart(gmfPanel);
		gmfTab.setControl(gmfPanel);
		tabs.add(gmfContentMergeViewerTab);

		return tabFolder;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabFolder#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createContents(Composite composite) {
		super.createContents(composite);
		// select the graphical viewer by default
		tabFolder.setSelection(gmfTab);
	}

	/**
	 * Handles the creation of the gmf tab of this viewer part given the parent {@link Composite} under which
	 * to create it.
	 * 
	 * @param parent
	 *            Parent {@link Composite} of the graphical viewer to create.
	 * @return The graphical part displayed by this viewer part's graphical tab.
	 */
	protected IModelContentMergeViewerTab createGmfPart(Composite parent) {
		return new GMFContentMergeViewerTab(parent, partSide, this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabFolder#dispose()
	 */
	@Override
	public void dispose() {
		gmfContentMergeViewerTab.dispose();
		super.dispose();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabFolder#findMatchFromElement(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public EObject findMatchFromElement(EObject element) {
		// used to find the ancestor.
		return super.findMatchFromElement(element);
	}

	/**
	 * returns the graphicalPart.
	 * 
	 * @return the graphical part.
	 */
	public IModelContentMergeViewerTab getGmfPart() {
		return gmfContentMergeViewerTab;
	}

}

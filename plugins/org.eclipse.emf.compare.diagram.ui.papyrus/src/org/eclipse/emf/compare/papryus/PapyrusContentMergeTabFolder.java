/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Obeo - initial API and implementation
 *******************************************************************************/

package org.eclipse.emf.compare.papryus;

import org.eclipse.emf.compare.diagram.ui.mergeviewer.GMFContentMergeTabFolder;
import org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer;
import org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab;
import org.eclipse.swt.widgets.Composite;

/**
 * Define a ModelContentMergeTabFolder for Papyrus comparison.
 * @author Mickael Barbero <a href="mailto:mickael.barbero@obeo.fr">mickael.barbero@obeo.fr</a>
 */
public class PapyrusContentMergeTabFolder extends GMFContentMergeTabFolder {

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
	public PapyrusContentMergeTabFolder(ModelContentMergeViewer viewer, Composite composite, int side) {
		super(viewer, composite, side);
	}


	/**
	 * Handles the creation of the gmf tab of this viewer part given the parent {@link Composite} under which
	 * to create it.
	 * 
	 * @param parent
	 *            Parent {@link Composite} of the graphical viewer to create.
	 * @return The graphical part displayed by this viewer part's graphical tab.
	 */
	@Override
	protected IModelContentMergeViewerTab createGmfPart(Composite parent) {
		final IModelContentMergeViewerTab gmfPart = new PapyrusContentMergeViewerTab(parent, partSide, this);
		return gmfPart;
	}

}

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
package org.eclipse.emf.compare.papryus;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.compare.diagram.ui.mergeviewer.GMFContentMergeViewer;
import org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabFolder;
import org.eclipse.swt.widgets.Composite;

/**
 * GMFContentMergeViewer for Papyrus.
 * 
 * @author Mickael Barbero <a href="mailto:mickael.barbero@obeo.fr">mickael.barbero@obeo.fr</a>
 */
public class PapyrusContentMergeViewer extends GMFContentMergeViewer {

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            The parent composite
	 * @param config
	 *            The compare configuration.
	 */
	public PapyrusContentMergeViewer(Composite parent, CompareConfiguration config) {
		super(parent, config);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ui.mergeviewer.GMFContentMergeViewer#getTitle()
	 */
	@Override
	public String getTitle() {
		// FIXME Externalize this
		return "Papyrus differences"; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.ParameterizedContentMergeViewer#createModelContentMergeTabFolder(org.eclipse.swt.widgets.Composite,
	 *      int)
	 */
	@Override
	protected ModelContentMergeTabFolder createModelContentMergeTabFolder(Composite composite, int side) {
		return new PapyrusContentMergeTabFolder(this, composite, side);
	}
}

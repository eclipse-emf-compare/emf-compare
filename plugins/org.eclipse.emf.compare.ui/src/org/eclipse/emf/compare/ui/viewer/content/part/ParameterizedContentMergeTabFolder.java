/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.content.part;

import org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer;
import org.eclipse.emf.compare.ui.viewer.content.part.diff.ParameterizedContentMergeDiffTab;
import org.eclipse.swt.widgets.Composite;

/**
 * Describes a part of a {@link ModelContentMergeViewer}.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.2
 */
public class ParameterizedContentMergeTabFolder extends ModelContentMergeTabFolder {
	/**
	 * Enhances visibility of this constructor.
	 * 
	 * @param viewer
	 *            Parent viewer of this viewer part.
	 * @param composite
	 *            Parent {@link Composite} for this part.
	 * @param side
	 *            Comparison side of this part.
	 */
	public ParameterizedContentMergeTabFolder(ModelContentMergeViewer viewer, Composite composite, int side) {
		super(viewer, composite, side);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabFolder#createModelContentMergeDiffTab(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected IModelContentMergeViewerTab createModelContentMergeDiffTab(Composite parent) {
		return new ParameterizedContentMergeDiffTab(parent, partSide, this);
	}
}

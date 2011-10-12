/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
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
 */
public class ParameterizedContentMergeTabFolder extends ModelContentMergeTabFolder {

	/**
	 * Constructor.
	 * 
	 * @param viewer
	 *            The content merge viewer.
	 * @param composite
	 *            The container composite.
	 * @param side
	 *            The side of the viewer.
	 */
	public ParameterizedContentMergeTabFolder(ModelContentMergeViewer viewer, Composite composite, int side) {
		super(viewer, composite, side);
	}

	@Override
	protected IModelContentMergeViewerTab createModelContentMergeDiffTab(Composite parent) {
		return new ParameterizedContentMergeDiffTab(parent, partSide, this);
	}

}

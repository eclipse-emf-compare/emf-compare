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
package org.eclipse.emf.compare.ui.viewer.structure;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

/**
 * A {@link ModelStructureMergeViewerCreator} which creates a {@link ParameterizedStructureMergeViewer}.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.2
 */
public class ParameterizedStructureMergeViewerCreator extends ModelStructureMergeViewerCreator {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.structure.ModelStructureMergeViewerCreator#createViewer(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.compare.CompareConfiguration)
	 */
	@Override
	public Viewer createViewer(Composite parent, CompareConfiguration config) {
		return new ParameterizedStructureMergeViewer(parent, config);
	}
}

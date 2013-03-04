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
package org.eclipse.emf.compare.diagram.ide.ui.ecoretools.internal;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.IViewerCreator;
import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.DiagramContentMergeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

/**
 * A {@link IViewerCreator} that creates {@link DiagramContentMergeViewer}.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramContentMergeViewerCreator implements IViewerCreator {

	/**
	 * ID of the contribution to the org.eclipse.compare.contentMergeViewers extension point.
	 */
	public static final String EOBJECT_CONTENT_MERGE_VIEWER_ID = "org.eclipse.emf.compare.ide.ui.internal.ViewContentMergeViewer"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.IViewerCreator#createViewer(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.compare.CompareConfiguration)
	 */
	public Viewer createViewer(Composite parent, CompareConfiguration config) {
		return new DiagramContentMergeViewer(parent, config);
	}

}

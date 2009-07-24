/*******************************************************************************
 * Copyright (c) 2009 Tobias Jaehnel.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Tobias Jaehnel - Bug#241385
 *******************************************************************************/

package org.eclipse.emf.compare.ui.gmf.mergeviewer;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.IViewerCreator;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author Tobias Jaehnel <tjaehnel@gmail.com>
 */
public class StructureMergeViewerCreator implements IViewerCreator {

	public StructureMergeViewerCreator() {
		// TODO Auto-generated constructor stub
	}

	public Viewer createViewer(Composite parent, CompareConfiguration config) {
		return new StructureMergeViewer(parent, config);
	}

}

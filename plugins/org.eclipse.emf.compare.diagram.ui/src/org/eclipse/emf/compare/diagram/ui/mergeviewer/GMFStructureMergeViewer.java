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

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.compare.ui.viewer.structure.ParameterizedStructureMergeViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * The structure merger for gmf comparison.
 * 
 * @author <a href="mailto:stephane.bouchet@obeo.fr">Stephane Bouchet</a>
 */
public class GMFStructureMergeViewer extends ParameterizedStructureMergeViewer {

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            Parent composite of this viewer.
	 * @param compareConfiguration
	 *            Configuration of the underlying comparison.
	 */
	public GMFStructureMergeViewer(Composite parent, CompareConfiguration compareConfiguration) {
		super(parent, compareConfiguration);
	}

}

/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.merge.sysml;

import java.io.IOException;

import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class DiffInvolvingRefineDiffInputData extends DiagramInputData {

	public Resource getLeft() throws IOException {
		return loadFromClassLoader("left.uml"); //$NON-NLS-1$
	}

	public Resource getRight() throws IOException {
		return loadFromClassLoader("right.uml"); //$NON-NLS-1$
	}
}

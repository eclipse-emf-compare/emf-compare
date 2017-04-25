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
package org.eclipse.emf.compare.diagram.papyrus.tests.comparison.data;

import java.io.IOException;

import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class DiagramTooltipProviderInputData extends DiagramInputData {

	public Resource getLeft() throws IOException {
		return loadFromClassLoader("left.notation");
	}

	public Resource getRight() throws IOException {
		return loadFromClassLoader("right.notation");
	}

	public Resource getOrigin() throws IOException {
		return loadFromClassLoader("ancestor.notation");
	}

}

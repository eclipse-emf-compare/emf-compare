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
package org.eclipse.emf.compare.diagram.papyrus.tests.groups.data.defaultgroup;

import java.io.IOException;

import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class DefaultGroupInputData extends DiagramInputData {

	public Resource getBug480437Left() throws IOException {
		return loadFromClassLoader("bug480437/left/model.notation");
	}

	public Resource getBug480437Origin() throws IOException {
		return loadFromClassLoader("bug480437/origin/model.notation");
	}

	public Resource getBug480437Right() throws IOException {
		return loadFromClassLoader("bug480437/right/model.notation");
	}

}

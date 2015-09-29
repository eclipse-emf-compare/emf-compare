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
package org.eclipse.emf.compare.diagram.papyrus.tests.groups.data.conflictsgroup;

import java.io.IOException;

import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class ConflictsGroupInputData extends DiagramInputData {

	public Resource getBug478539Left() throws IOException {
		return loadFromClassLoader("bug478539/left/model.notation");
	}

	public Resource getBug478539Origin() throws IOException {
		return loadFromClassLoader("bug478539/origin/model.notation");
	}

	public Resource getBug478539Right() throws IOException {
		return loadFromClassLoader("bug478539/right/model.notation");
	}

}

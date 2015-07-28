/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.uriattachment.data;

import java.io.IOException;

import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class URIAttachmentData extends DiagramInputData {

	public Resource getA1Left() throws IOException {
		return loadFromClassLoader("a1/left/model.notation");
	}

	public Resource getA1Right() throws IOException {
		return loadFromClassLoader("a1/right/model.notation");
	}

	public Resource getA1Origin() throws IOException {
		return loadFromClassLoader("a1/origin/model.notation");
	}
}

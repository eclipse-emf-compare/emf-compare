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
package org.eclipse.emf.compare.uml2.ide.ui.tests.comparison.tooltip.data;

import java.io.IOException;

import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class UMLTooltipProviderInputData extends AbstractUMLInputData {

	public Resource getLeft() throws IOException {
		return loadFromClassLoader("left.uml");
	}

	public Resource getRight() throws IOException {
		return loadFromClassLoader("right.uml");
	}

	public Resource getOrigin() throws IOException {
		return loadFromClassLoader("ancestor.uml");
	}

}

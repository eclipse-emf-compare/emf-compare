/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.generalizationSet.data;

import java.io.IOException;

import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class GeneralizationSetInputData extends AbstractUMLInputData {
	public Resource getA1Left() throws IOException {
		return loadFromClassLoader("a1/left.uml"); //$NON-NLS-1$
	}

	public Resource getA1Right() throws IOException {
		return loadFromClassLoader("a1/right.uml"); //$NON-NLS-1$
	}

}

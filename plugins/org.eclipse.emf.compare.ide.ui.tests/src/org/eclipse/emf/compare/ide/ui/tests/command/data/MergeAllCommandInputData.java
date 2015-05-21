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
package org.eclipse.emf.compare.ide.ui.tests.command.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

@SuppressWarnings("nls")
public class MergeAllCommandInputData extends AbstractInputData {
	public Resource getLeftScope() throws IOException {
		return loadFromClassLoader("modelLeft.ecore");
	}

	public Resource getOriginScope() throws IOException {
		return loadFromClassLoader("modelOrigin.ecore");
	}

	public Resource getRightScope() throws IOException {
		return loadFromClassLoader("modelRight.ecore");
	}

}

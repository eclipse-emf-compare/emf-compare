/*******************************************************************************
 * Copyright (c) 2016 Ericsson and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Simon Delisle - bug 486923
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.data.ecore.bug486923;

import java.io.IOException;

import org.eclipse.emf.compare.tests.edit.data.ResourceScopeProvider;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class Bug486923InputData extends AbstractInputData implements ResourceScopeProvider {

	public Resource getLeft() throws IOException {
		return loadFromClassLoader("left.ecore");
	}

	public Resource getRight() throws IOException {
		return loadFromClassLoader("right.ecore");
	}

	public Resource getOrigin() throws IOException {
		return loadFromClassLoader("ancestor.ecore");
	}

}

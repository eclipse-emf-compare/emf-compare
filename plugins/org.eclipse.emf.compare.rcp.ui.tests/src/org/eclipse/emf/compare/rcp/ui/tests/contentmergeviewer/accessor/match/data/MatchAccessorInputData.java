/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.tests.contentmergeviewer.accessor.match.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.edit.data.ResourceScopeProvider;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class MatchAccessorInputData extends AbstractInputData implements ResourceScopeProvider {

	public Resource getLeft() throws IOException {
		return loadFromClassLoader("left.ecore");
	}

	public Resource getRight() throws IOException {
		return loadFromClassLoader("right.ecore");
	}

	public Resource getOrigin() throws IOException {
		return loadFromClassLoader("origin.ecore");
	}
}

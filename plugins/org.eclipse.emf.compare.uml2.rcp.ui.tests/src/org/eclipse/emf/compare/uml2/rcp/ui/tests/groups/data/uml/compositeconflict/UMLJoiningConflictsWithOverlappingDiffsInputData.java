/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Tanja Mayerhofer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.rcp.ui.tests.groups.data.uml.compositeconflict;

import java.io.IOException;

import org.eclipse.emf.compare.tests.edit.data.ResourceScopeProvider;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class UMLJoiningConflictsWithOverlappingDiffsInputData extends AbstractInputData implements ResourceScopeProvider {

	public Resource getLeft() throws IOException {
		return loadFromClassLoader("left.uml"); //$NON-NLS-1$
	}

	public Resource getRight() throws IOException {
		return loadFromClassLoader("right.uml"); //$NON-NLS-1$
	}

	public Resource getOrigin() throws IOException {
		return loadFromClassLoader("origin.uml"); //$NON-NLS-1$
	}
}

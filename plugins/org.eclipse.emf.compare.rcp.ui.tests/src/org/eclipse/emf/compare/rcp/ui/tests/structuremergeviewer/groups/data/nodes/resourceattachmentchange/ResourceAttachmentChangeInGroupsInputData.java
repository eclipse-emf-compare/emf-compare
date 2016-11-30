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
package org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.data.nodes.resourceattachmentchange;

import java.io.IOException;

import org.eclipse.emf.compare.tests.edit.data.ResourceScopeProvider;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class ResourceAttachmentChangeInGroupsInputData extends AbstractInputData implements ResourceScopeProvider {

	public Resource getLeft() throws IOException {
		ResourceSet rSet = new ResourceSetImpl();
		Resource resource = loadFromClassLoader("left/left.nodes");
		rSet.getResources().add(resource);
		return resource;
	}

	public Resource getRight() throws IOException {
		ResourceSet rSet = new ResourceSetImpl();
		Resource resource = loadFromClassLoader("right/right.nodes");
		rSet.getResources().add(resource);
		Resource fragment = loadFromClassLoader("right/fragment.nodes");
		rSet.getResources().add(fragment);
		return resource;
	}

	public Resource getOrigin() throws IOException {
		ResourceSet rSet = new ResourceSetImpl();
		Resource resource = loadFromClassLoader("origin/origin.nodes");
		rSet.getResources().add(resource);
		Resource fragment = loadFromClassLoader("origin/fragment.nodes");
		rSet.getResources().add(fragment);
		return resource;
	}
}

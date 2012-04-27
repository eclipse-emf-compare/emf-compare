/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.tests.unit;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.mapping.IModelProviderDescriptor;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.compare.ide.logical.EMFModelProvider;

public class AbstractLogicalModelTest {
	private static final String TEST_PROJECT_NAME = "testProject"; //$NON-NLS-1$

	private static final String INPUT_FOLDER_NAME = "inputs"; //$NON-NLS-1$

	public IResource getIFile(String path) {
		IProject testProject = ResourcesPlugin.getWorkspace().getRoot().getProject(TEST_PROJECT_NAME);
		IFolder inputFolder = testProject.getFolder(INPUT_FOLDER_NAME);

		IResource file = inputFolder.findMember(path);
		if (!file.exists() || !file.isAccessible()) {
			fail("Couldn't find file '" + INPUT_FOLDER_NAME + '/' + path); //$NON-NLS-1$
		}

		return file;
	}

	public ModelProvider getModelProvider(IResource resource) {
		IModelProviderDescriptor[] descriptors = ModelProvider.getModelProviderDescriptors();
		List<IModelProviderDescriptor> candidates = new ArrayList<IModelProviderDescriptor>();
		for (int i = 0; i < descriptors.length; i++) {
			IModelProviderDescriptor descriptor = descriptors[i];
			try {
				IResource[] resources = descriptor.getMatchingResources(new IResource[] {resource, });
				if (resources.length > 0) {
					candidates.add(descriptor);
				}
			} catch (CoreException e) {
				// ignore
			}
		}

		ModelProvider result = null;
		Iterator<IModelProviderDescriptor> modelIterator = candidates.iterator();
		while (result == null && modelIterator.hasNext()) {
			IModelProviderDescriptor candidate = modelIterator.next();
			if (EMFModelProvider.PROVIDER_ID.equals(candidate.getId())) {
				try {
					result = candidate.getModelProvider();
				} catch (CoreException e) {
					// Try the next
				}
			}
		}

		if (result == null && !candidates.isEmpty()) {
			modelIterator = candidates.iterator();
			while (result == null && modelIterator.hasNext()) {
				try {
					result = modelIterator.next().getModelProvider();
				} catch (CoreException e) {
					// Try the next
				}
			}
		}

		if (result == null) {
			fail("Couldn't retrieve model provider for '" + resource); //$NON-NLS-1$
		}
		return result;
	}

}

/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.logical.tests.unit;

import static org.junit.Assert.fail;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.mapping.IModelProviderDescriptor;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.team.ui.mapping.ISynchronizationCompareAdapter;

@SuppressWarnings("nls")
public class AbstractLogicalModelTest {
	private static final String TEST_PROJECT_NAME = "testProject";

	private static final String INPUT_FOLDER_NAME = "inputs";

	public IResource getIFile(String path) {
		IProject testProject = ResourcesPlugin.getWorkspace().getRoot().getProject(TEST_PROJECT_NAME);
		IFolder inputFolder = testProject.getFolder(INPUT_FOLDER_NAME);

		IResource file = inputFolder.findMember(path);
		if (!file.exists() || !file.isAccessible()) {
			fail("Couldn't find file '" + INPUT_FOLDER_NAME + '/' + path);
		}

		return file;
	}

	public ModelProvider getModelProvider(IResource resource) {
		IModelProviderDescriptor[] descriptors = ModelProvider.getModelProviderDescriptors();
		for (int i = 0; i < descriptors.length; i++) {
			IModelProviderDescriptor descriptor = descriptors[i];
			try {
				IResource[] resources = descriptor.getMatchingResources(new IResource[] {resource,});
				if (resources.length > 0) {
					return descriptor.getModelProvider();
				}
			} catch (CoreException e) {
				// will fail out of the for loop
			}
		}
		fail("Couldn't retrieve model provider for '" + resource);
		return null;
	}

	public ISynchronizationCompareAdapter getCompareAdapter(ModelProvider modelProvider) {
		Object compareAdapter = Platform.getAdapterManager().loadAdapter(modelProvider,
				ISynchronizationCompareAdapter.class.getName());
		if (!(compareAdapter instanceof ISynchronizationCompareAdapter)) {
			compareAdapter = modelProvider.getAdapter(ISynchronizationCompareAdapter.class);
		}

		return (ISynchronizationCompareAdapter)compareAdapter;
	}
}

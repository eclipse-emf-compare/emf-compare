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
package org.eclipse.emf.compare.ide.ui.tests.unit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.resources.mapping.RemoteResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.ide.tests.mock.MockRemoteResourceMappingContext;
import org.eclipse.emf.compare.ide.tests.mock.MockSynchronizationContext;
import org.eclipse.emf.compare.ide.ui.logical.sync.EMFCompareSynchronizationAdapter;
import org.eclipse.emf.compare.ide.ui.tests.utils.TestUtils;
import org.eclipse.team.core.mapping.ISynchronizationContext;
import org.eclipse.team.ui.mapping.ISynchronizationCompareAdapter;
import org.junit.Ignore;
import org.junit.Test;

public class EMFCompareSyncAdapterTest {
	private final String[] ecoreModelPaths = new String[] {"ecore/library.ecore", "ecore/books.ecore", //$NON-NLS-1$ //$NON-NLS-2$
			"ecore/writers.ecore", }; //$NON-NLS-1$

	/**
	 * Makes sure that the EMF compare adapter is properly registered for our model provider.
	 */
	@Test
	public void testCompareAdapter() {
		for (String modelPath : ecoreModelPaths) {
			IResource iResource = TestUtils.getIFile(modelPath);
			ModelProvider modelProvider = TestUtils.getModelProvider(iResource);

			ISynchronizationCompareAdapter compareAdapter = TestUtils.getCompareAdapter(modelProvider);

			assertNotNull(compareAdapter);
			assertTrue(compareAdapter instanceof EMFCompareSynchronizationAdapter);
		}
	}

	/**
	 * Makes sure that we can create a compare input from the ResourceMapping.
	 */
	@Test
	@Ignore
	public void testCompareInput() {
		for (String modelPath : ecoreModelPaths) {
			IResource iResource = TestUtils.getIFile(modelPath);
			ModelProvider modelProvider = TestUtils.getModelProvider(iResource);

			ISynchronizationCompareAdapter compareAdapter = TestUtils.getCompareAdapter(modelProvider);

			ResourceMappingContext[] contexts = new ResourceMappingContext[] {
					new MockRemoteResourceMappingContext(), new MockRemoteResourceMappingContext(true), };
			for (ResourceMappingContext context : contexts) {
				try {
					ResourceMapping[] mappings = modelProvider.getMappings(iResource, context,
							new NullProgressMonitor());

					final boolean isThreeWay = context instanceof RemoteResourceMappingContext
							&& ((RemoteResourceMappingContext)context).isThreeWay();
					final int syncType;
					if (isThreeWay) {
						syncType = ISynchronizationContext.THREE_WAY;
					} else {
						syncType = ISynchronizationContext.TWO_WAY;
					}

					ISynchronizationContext synchronizationContext = new MockSynchronizationContext(mappings,
							context, syncType);

					ICompareInput compareInput = compareAdapter.asCompareInput(synchronizationContext,
							iResource);

					fail("code EMFCompareAdapter#asCompareInput");
				} catch (CoreException e) {
					fail("Couldn't create a compare input for '" + iResource + "' from the ResourceMapping."); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
	}
}

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.resources.mapping.RemoteResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceTraversal;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.logical.EMFModelProvider;
import org.eclipse.emf.compare.ide.logical.EMFResourceMapping;
import org.eclipse.emf.compare.ide.logical.sync.EMFSynchronizationModel;
import org.eclipse.emf.compare.ide.tests.mock.MockRemoteResourceMappingContext;
import org.eclipse.emf.compare.ide.tests.mock.MockSynchronizationContext;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.team.core.mapping.ISynchronizationContext;
import org.junit.Test;

public class ResourceMappingTest extends AbstractLogicalModelTest {
	private final String[] ecoreModelPaths = new String[] {"ecore/library.ecore", "ecore/books.ecore", //$NON-NLS-1$ //$NON-NLS-2$
			"ecore/writers.ecore", }; //$NON-NLS-1$

	/**
	 * Make sure that we can find a model provider for each of the models, and that this model provider is
	 * indeed ours.
	 */
	@Test
	public void testModelProvider() {
		for (String modelPath : ecoreModelPaths) {
			IResource iResource = getIFile(modelPath);
			ModelProvider modelProvider = getModelProvider(iResource);

			assertNotNull(modelProvider);
			assertEquals(EMFModelProvider.PROVIDER_ID, modelProvider.getId());
		}
	}

	/**
	 * Ensures that we have a resource mapping accessible for each of the models, and that these mappings are
	 * ours.
	 */
	@Test
	public void testResourceMapping() {
		for (String modelPath : ecoreModelPaths) {
			IResource iResource = getIFile(modelPath);
			ModelProvider modelProvider = getModelProvider(iResource);

			try {
				ResourceMapping[] mappings = modelProvider.getMappings(iResource,
						ResourceMappingContext.LOCAL_CONTEXT, new NullProgressMonitor());

				assertTrue(1 == mappings.length);
				assertNotNull(mappings);

				for (ResourceMapping mapping : mappings) {
					assertTrue(mapping instanceof EMFResourceMapping);
				}
			} catch (CoreException e) {
				fail("Exception while retrieving ResourceMappings for '" + iResource + "'"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

	/**
	 * Make sure that we manage to load the whole logical model for our 'ecore' resources.
	 */
	@Test
	public void testEcoreTraversals() {
		final IResource libraryResource = getIFile(ecoreModelPaths[0]);
		final IResource bookResource = getIFile(ecoreModelPaths[1]);
		final IResource writerResource = getIFile(ecoreModelPaths[2]);

		final List<IResource> logicalModel = new ArrayList<IResource>(3);
		logicalModel.add(libraryResource);
		logicalModel.add(bookResource);
		logicalModel.add(writerResource);

		// First off, from the "library" resource, we should be able to find all three parts of the model
		testTraversal(logicalModel, libraryResource, 3);

		// The "books" resource only references the "writers" resource and has no backreference to "library"
		testTraversal(logicalModel, bookResource, 2);

		// Likewise, the "writers" resource has only references "books".
		testTraversal(logicalModel, writerResource, 2);
	}

	/**
	 * Make sure that we manage to load the whole logical model (the {@link ResourceSet}) corresponding to our
	 * 'ecore' resources.
	 */
	@Test
	public void testEcoreResourceSets() {
		final IResource libraryResource = getIFile(ecoreModelPaths[0]);
		final IResource bookResource = getIFile(ecoreModelPaths[1]);
		final IResource writerResource = getIFile(ecoreModelPaths[2]);

		// First off, from the "library" resource, we should be able to find all three parts of the model
		testResourceSets(libraryResource, 3);

		// The "books" resource only references the "writers" resource and has no backreference to "library"
		testResourceSets(bookResource, 2);

		// Likewise, the "writers" resource has only references "books".
		testResourceSets(writerResource, 2);
	}

	/**
	 * Ensures that the comparison can be executed through the ResourceMapping.
	 */
	@Test
	public void testCompare() {
		for (String modelPath : ecoreModelPaths) {
			IResource iResource = getIFile(modelPath);
			ModelProvider modelProvider = getModelProvider(iResource);

			ResourceMappingContext[] contexts = new ResourceMappingContext[] {
					new MockRemoteResourceMappingContext(), new MockRemoteResourceMappingContext(true), };
			for (ResourceMappingContext context : contexts) {
				try {
					final ResourceMapping[] mappings = modelProvider.getMappings(iResource, context,
							new NullProgressMonitor());

					final boolean isThreeWay = context instanceof RemoteResourceMappingContext
							&& ((RemoteResourceMappingContext)context).isThreeWay();
					final int syncType;
					if (isThreeWay) {
						syncType = ISynchronizationContext.THREE_WAY;
					} else {
						syncType = ISynchronizationContext.TWO_WAY;
					}

					final ISynchronizationContext synchronizationContext = new MockSynchronizationContext(
							mappings, context, syncType);

					final EMFSynchronizationModel model = EMFSynchronizationModel.createModel(
							synchronizationContext, modelProvider.getId(), new NullProgressMonitor());
					assertNotNull(model);

					// Our mock context provides identical resources for local, remote and ancestor.
					final ResourceSet leftResourceSet = model.getLeftResourceSet();
					final ResourceSet rightResourceSet = model.getRightResourceSet();
					final ResourceSet originResourceSet = model.getOriginResourceSet();

					assertNotNull(leftResourceSet);
					assertNotNull(rightResourceSet);
					if (isThreeWay) {
						assertNotNull(originResourceSet);
					} else {
						assertNull(originResourceSet);
					}

					final EMFResourceMapping mapping = (EMFResourceMapping)mappings[0];
					assertSame(mapping.getLocalResourceSet(), leftResourceSet);
					assertSame(mapping.getRemoteResourceSet(), rightResourceSet);
					assertSame(mapping.getOriginResourceSet(), originResourceSet);

					final ResourceTraversal traversal = mapping.getTraversals(context,
							new NullProgressMonitor())[0];
					assertTrue(traversal.getResources().length == leftResourceSet.getResources().size());
					assertTrue(traversal.getResources().length == rightResourceSet.getResources().size());
					if (isThreeWay) {
						assertTrue(traversal.getResources().length == originResourceSet.getResources().size());
					}

					for (IResource resource : traversal.getResources()) {
						final URI expectedURI = URI.createPlatformResourceURI(((IFile)resource).getFullPath()
								.toString(), true);
						assertContainsResourceWithURI(leftResourceSet, expectedURI);
						assertContainsResourceWithURI(rightResourceSet, expectedURI);
						if (isThreeWay) {
							assertContainsResourceWithURI(originResourceSet, expectedURI);
						}
					}
				} catch (CoreException e) {
					fail("Couldn't compare '" + iResource + "' through the ResourceMapping."); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
	}

	/**
	 * This will be used in order to check that the ResourceMapping for the <em>base</em> {@link IResource}
	 * properly finds <em>reachableResources</em> physical resources for the given <em>logicalModel</em>.
	 * 
	 * @param logicalModel
	 *            The whole logical model of which <em>base</em> is a part.
	 * @param base
	 *            The resource of this <em>logicalModel</em> which {@link ResourceMapping} is to be tested.
	 * @param reachableResources
	 *            Number of {@link IResource}s from the <em>logicalModel</em> that can be reached from
	 *            <em>base</em>.
	 */
	private void testTraversal(List<IResource> logicalModel, IResource base, int reachableResources) {
		ModelProvider modelProvider = getModelProvider(base);
		ResourceMappingContext[] contexts = new ResourceMappingContext[] {
				ResourceMappingContext.LOCAL_CONTEXT, new MockRemoteResourceMappingContext(),
				new MockRemoteResourceMappingContext(true) };

		for (ResourceMappingContext context : contexts) {
			try {
				ResourceMapping[] mappings = modelProvider.getMappings(base, context,
						new NullProgressMonitor());

				ResourceMapping mapping = mappings[0];
				ResourceTraversal[] traversals = mapping.getTraversals(context, new NullProgressMonitor());

				assertNotNull(traversals);
				assertTrue(1 == traversals.length);

				ResourceTraversal traversal = traversals[0];
				IResource[] resources = traversal.getResources();

				assertNotNull(resources);
				assertTrue(reachableResources == resources.length);

				for (IResource resource : resources) {
					assertTrue(logicalModel.contains(resource));
				}
			} catch (CoreException e) {
				final String message;
				if (context instanceof RemoteResourceMappingContext) {
					message = "Exception while retrieving remote ResourceMappings for '" + base + "'"; //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					message = "Exception while retrieving ResourceMappings for '" + base + "'"; //$NON-NLS-1$ //$NON-NLS-2$
				}
				fail(message);
			}
		}
	}

	/**
	 * This will be used in order to check that the ResourceMapping for the <em>base</em> {@link IResource}
	 * properly finds and load <em>logicalResources</em> logical resources.
	 * 
	 * @param base
	 *            The resource of this <em>logicalModel</em> which {@link ResourceMapping} is to be tested.
	 * @param logicalResources
	 *            Number of {@link Resource}s from the <em>logicalModel</em> that can be reached from
	 *            <em>base</em>.
	 */
	private void testResourceSets(IResource base, int logicalResources) {
		ModelProvider modelProvider = getModelProvider(base);
		ResourceMappingContext[] contexts = new ResourceMappingContext[] {
				ResourceMappingContext.LOCAL_CONTEXT, new MockRemoteResourceMappingContext(),
				new MockRemoteResourceMappingContext(true) };

		for (ResourceMappingContext context : contexts) {
			try {
				ResourceMapping[] mappings = modelProvider.getMappings(base, context,
						new NullProgressMonitor());

				// Force resolving of the logical model
				mappings[0].getTraversals(context, new NullProgressMonitor());

				EMFResourceMapping mapping = (EMFResourceMapping)mappings[0];

				ResourceSet localResourceSet = mapping.getLocalResourceSet();
				ResourceSet remoteResourceSet = mapping.getRemoteResourceSet();
				ResourceSet ancestorResourceSet = mapping.getOriginResourceSet();

				if (context instanceof RemoteResourceMappingContext
						&& ((RemoteResourceMappingContext)context).isThreeWay()) {
					assertNotNull(localResourceSet);
					assertNotNull(remoteResourceSet);
					assertNotNull(ancestorResourceSet);

					assertTrue(logicalResources == localResourceSet.getResources().size());
					assertTrue(logicalResources == remoteResourceSet.getResources().size());
					assertTrue(logicalResources == ancestorResourceSet.getResources().size());

					for (Resource localResource : localResourceSet.getResources()) {
						assertNotNull(localResource.getContents());
						assertFalse(localResource.getContents().isEmpty());

						assertContainsResourceWithURI(remoteResourceSet, localResource.getURI());
						assertContainsResourceWithURI(ancestorResourceSet, localResource.getURI());
					}

					for (Resource remoteResource : remoteResourceSet.getResources()) {
						assertNotNull(remoteResource.getContents());
						assertFalse(remoteResource.getContents().isEmpty());

						assertContainsResourceWithURI(localResourceSet, remoteResource.getURI());
						assertContainsResourceWithURI(ancestorResourceSet, remoteResource.getURI());
					}

					for (Resource ancestorResource : ancestorResourceSet.getResources()) {
						assertNotNull(ancestorResource.getContents());
						assertFalse(ancestorResource.getContents().isEmpty());

						assertContainsResourceWithURI(localResourceSet, ancestorResource.getURI());
						assertContainsResourceWithURI(remoteResourceSet, ancestorResource.getURI());
					}
				} else if (context instanceof RemoteResourceMappingContext) {
					assertNotNull(localResourceSet);
					assertNotNull(remoteResourceSet);
					assertNull(ancestorResourceSet);

					assertTrue(logicalResources == localResourceSet.getResources().size());
					assertTrue(logicalResources == remoteResourceSet.getResources().size());

					for (Resource localResource : localResourceSet.getResources()) {
						assertNotNull(localResource.getContents());
						assertFalse(localResource.getContents().isEmpty());

						assertContainsResourceWithURI(remoteResourceSet, localResource.getURI());
					}

					for (Resource remoteResource : remoteResourceSet.getResources()) {
						assertNotNull(remoteResource.getContents());
						assertFalse(remoteResource.getContents().isEmpty());

						assertContainsResourceWithURI(localResourceSet, remoteResource.getURI());
					}
				} else {
					assertNotNull(localResourceSet);
					assertNull(remoteResourceSet);
					assertNull(ancestorResourceSet);

					assertTrue(logicalResources == localResourceSet.getResources().size());
					for (Resource localResource : localResourceSet.getResources()) {
						assertNotNull(localResource.getContents());
						assertFalse(localResource.getContents().isEmpty());
					}
				}
			} catch (CoreException e) {
				final String message;
				if (context instanceof RemoteResourceMappingContext) {
					message = "Exception while resolving logical model for '" + base + "'"; //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					message = "Exception while resolving logical model for '" + base + "'"; //$NON-NLS-1$ //$NON-NLS-2$
				}
				fail(message);
			}
		}
	}

	/**
	 * Asserts that the given {@link ResourceSet set} contains a {@link Resource} with an {@link URI} equal to
	 * <em>uri</em> and will make the test fail otherwise.
	 * 
	 * @param set
	 *            The {@link ResourceSet} in which to look for a particular {@link Resource}.
	 * @param uri
	 *            The {@link URI} for which we seek a corresponding {@link Resource} in the <em>set</em>.
	 */
	private void assertContainsResourceWithURI(ResourceSet set, URI uri) {
		boolean foundMatch = false;
		Iterator<Resource> resourceIterator = set.getResources().iterator();
		while (!foundMatch && resourceIterator.hasNext()) {
			Resource resource = resourceIterator.next();
			if (uri.path().equals(resource.getURI().path())) {
				foundMatch = true;
			}
		}
		assertTrue(foundMatch);
	}
}

/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.logical.modelprovider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.internal.resources.mapping.ModelProviderManager;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.mapping.IModelProviderDescriptor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.logical.EMFModelProvider;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.DefaultImplicitDependencies;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.junit.Test;

/**
 * This test class ensures that the {@link EMFModelProvider} is registered for the files we take care of in
 * EMF Compare.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
@SuppressWarnings({"restriction", "nls" })
public class EMFModelProviderRegistrationTest {

	/** Workspace for creating files. */
	protected static final IWorkspace WORKSPACE = ResourcesPlugin.getWorkspace();

	/** Workspace root. */
	protected static final IWorkspaceRoot WORKSPACE_ROOT = WORKSPACE.getRoot();

	/** Descriptor for the {@link EMFModelProvider} able to match specific resources. */
	protected static final IModelProviderDescriptor EMF_MODEL_PROVIDER = ModelProviderManager.getDefault()
			.getDescriptor(EMFModelProvider.PROVIDER_ID);

	/** Implicit dependency provider. */
	protected static final DefaultImplicitDependencies IMPLICIT_DEPENDENCIES = new DefaultImplicitDependencies();

	/** URI converter that always returns true for the existence check. */
	protected static final URIConverter EXISTENCE_URI_CONVERTER = new ExtensibleURIConverterImpl() {
		@Override
		public boolean exists(URI uri, Map<?, ?> options) {
			return true;
		}
	};

	/**
	 * Tests that the {@link EMFModelProvider} supports the expected file extensions and their dependencies.
	 */
	@Test
	public void testFileExtensions() {
		assertNotNull(EMF_MODEL_PROVIDER);

		// check supported file extensions
		assertMatches(getDependencies(createResourceWithFileExtension("uml")));
		assertMatches(getDependencies(createResourceWithFileExtension("notation")));
		assertMatches(getDependencies(createResourceWithFileExtension("di")));
		assertMatches(getDependencies(createResourceWithFileExtension("sash")));
		assertMatches(getDependencies(createResourceWithFileExtension("ecore")));
		assertMatches(getDependencies(createResourceWithFileExtension("xmi")));
		assertMatches(getDependencies(createResourceWithFileExtension("properties")));
		assertMatches(getDependencies(createResourceWithFileExtension("internationalization")));

		// sanity check: check unsupported file extensions
		assertMatchesNot(createResourceWithFileExtension("unsupported"));
	}

	/**
	 * Creates a new file with the given file extension. The created file does not actually exist.
	 * 
	 * @param fileExtension
	 *            file extension
	 * @return resource with the given file extension
	 */
	protected IResource createResourceWithFileExtension(String fileExtension) {
		return createResourceWithName("resource." + fileExtension);
	}

	/**
	 * Creates a new file with the given name. The created file does not actually exist.
	 * 
	 * @param name
	 *            name
	 * @return resource with the given file extension
	 */
	protected IResource createResourceWithName(String name) {
		IResource resource = WORKSPACE_ROOT.getFile(new Path("NoProject/" + name));
		assertEquals(name, resource.getName());
		return resource;
	}

	/**
	 * Returns a set of dependencies for the given resource, including the resource itself.
	 * 
	 * @param resource
	 *            resource
	 * @return set of dependencies for the given resource
	 */
	protected Set<IResource> getDependencies(IResource resource) {
		Set<URI> implicitDependencies = IMPLICIT_DEPENDENCIES.of(URI.createURI(resource.getName()),
				EXISTENCE_URI_CONVERTER);
		Set<IResource> dependencies = Sets.newHashSet();
		for (URI implicitDependency : implicitDependencies) {
			dependencies.add(createResourceWithName(implicitDependency.toString()));
		}
		assertTrue(dependencies.contains(resource));
		return dependencies;
	}

	/**
	 * Asserts that the given resources are matched by the {@link EMFModelProvider}.
	 * 
	 * @param resources
	 *            resources that needs to match
	 */
	protected void assertMatches(Set<IResource> resources) {
		for (IResource resource : resources) {
			assertMatches(resource);
		}
	}

	/**
	 * Asserts that the given resource is matched by the {@link EMFModelProvider}.
	 * 
	 * @param resource
	 *            resource that needs to match
	 */
	protected void assertMatches(IResource resource) {
		assertNotNull(resource);
		IResource[] matchingResources = new IResource[0];
		try {
			matchingResources = EMF_MODEL_PROVIDER.getMatchingResources(new IResource[] {resource });
		} catch (CoreException e) {
			// ignore, as checked with next statement
		}
		assertEquals("EMF Model Provider does not support " + resource.getName(), 1,
				matchingResources.length);
		assertEquals("EMF Model Provider does not support " + resource.getName(), resource,
				matchingResources[0]);
	}

	/**
	 * Asserts that the given resources are NOT matched by the {@link EMFModelProvider}.
	 * 
	 * @param resources
	 *            resources that should not match
	 */
	protected void assertMatchesNot(Set<IResource> resources) {
		for (IResource resource : resources) {
			assertMatchesNot(resource);
		}
	}

	/**
	 * Asserts that the given resource is NOT matched by the {@link EMFModelProvider}.
	 * 
	 * @param resource
	 *            resource that should not match
	 */
	protected void assertMatchesNot(IResource resource) {
		assertNotNull(resource);
		IResource[] matchingResources = new IResource[0];
		try {
			matchingResources = EMF_MODEL_PROVIDER.getMatchingResources(new IResource[] {resource });
		} catch (CoreException e) {
			// ignore, as checked with next statement
		}
		assertEquals("EMF Model Provider does support " + resource.getName(), 0, matchingResources.length);
	}
}

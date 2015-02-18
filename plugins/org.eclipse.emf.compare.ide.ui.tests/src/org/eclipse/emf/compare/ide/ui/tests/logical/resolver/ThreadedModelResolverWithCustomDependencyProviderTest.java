/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *     Stefan Dirix - handle space in file url
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.logical.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.dependency.ModelDependencyProviderRegistry;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ThreadedModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.ui.tests.CompareTestCase;
import org.eclipse.emf.compare.ide.ui.tests.workspace.TestProject;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.junit.Test;
import org.osgi.framework.Bundle;

/**
 * Tests the integration of the modelDependencyProvider extension in the {@link ThreadedModelResolver}.
 * <p>
 * The integration is tested by having a model set consisting of three files (file1.ecore, file2.ecore, and
 * file3.ecore), which are -- for the sake of the test -- always specified as dependency by a dependency
 * provider. We run the {@link ThreadedModelResolver} with and without this dependency provider and assert
 * that the files aren't or are part of the resulting storage traversal that is resolved.
 * </p>
 * <p>
 * More concretely, file2.ecore has a real dependency to file3.ecore, whereas file1.ecore has no dependency
 * neither to file2.ecore nor file3.ecore. Thus, calling the {@link ThreadedModelResolver} with file1.ecore
 * would not include file2.ecore and file3.ecore without a custom dependency provider. Therefore, we test the
 * integration by adding a dependency provider which always adds file1.ecore and file2.ecore to the
 * dependencies. As a result, calling the {@link ThreadedModelResolver} with the custom dependency provider
 * with each of the files, file1.ecore, file2.ecore, or file3.ecore should always result into a storage
 * traversal of all three files.
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
@SuppressWarnings({"nls", "restriction" })
public class ThreadedModelResolverWithCustomDependencyProviderTest extends CompareTestCase {

	private static final String TEST_BUNDLE = "org.eclipse.emf.compare.ide.ui.tests";

	private static final String TEST_DATA_PATH = "src/org/eclipse/emf/compare/ide/ui/tests/logical/resolver/data/case1/";

	private static final String MODEL_FILE1 = "file1.ecore";

	private static final String MODEL_FILE2 = "file2.ecore";

	private static final String MODEL_FILE3 = "file3.ecore";

	private IProgressMonitor monitor = new NullProgressMonitor();

	@Test
	public void testResolveLocalModelFromFile1WithoutDependencyProvider() throws IOException, CoreException,
			URISyntaxException, InterruptedException {
		final ModelSet modelSet = createProjectWithModelSet();
		final ModelDependencyProviderRegistry emptyRegistry = new ModelDependencyProviderRegistry();
		final ThreadedModelResolver resolver = createModelResolver(emptyRegistry);

		final StorageTraversal traversal = resolver.resolveLocalModel(modelSet.file1, monitor);

		assertEquals(1, traversal.getStorages().size());
		assertContainsFile(traversal, modelSet.file1);
	}

	@Test
	public void testResolveLocalModelFromFile2WithoutDependencyProvider() throws IOException, CoreException,
			URISyntaxException, InterruptedException {
		final ModelSet modelSet = createProjectWithModelSet();
		final ModelDependencyProviderRegistry emptyRegistry = new ModelDependencyProviderRegistry();
		final ThreadedModelResolver resolver = createModelResolver(emptyRegistry);

		final StorageTraversal traversal = resolver.resolveLocalModel(modelSet.file2, monitor);

		assertEquals(2, traversal.getStorages().size());
		assertContainsFile(traversal, modelSet.file2);
		assertContainsFile(traversal, modelSet.file3);
	}

	@Test
	public void testResolveLocalModelFromFile1WithDependencyProvider() throws IOException, CoreException,
			URISyntaxException, InterruptedException {
		final ModelSet modelSet = createProjectWithModelSet();
		final ModelDependencyProviderRegistry registry = createRegistryWithCustomResolver();
		final ThreadedModelResolver resolver = createModelResolver(registry);

		final StorageTraversal traversal = resolver.resolveLocalModel(modelSet.file1, monitor);

		assertContainsAllFiles(traversal, modelSet);
	}

	@Test
	public void testResolveLocalModelFromFile2WithDependencyProvider() throws IOException, CoreException,
			URISyntaxException, InterruptedException {
		final ModelSet modelSet = createProjectWithModelSet();
		final ModelDependencyProviderRegistry registry = createRegistryWithCustomResolver();
		final ThreadedModelResolver resolver = createModelResolver(registry);

		final StorageTraversal traversal = resolver.resolveLocalModel(modelSet.file2, monitor);

		assertContainsAllFiles(traversal, modelSet);
	}

	@Test
	public void testResolveLocalModelsTwoWayFromFile1WithoutDependencyProvider() throws IOException,
			CoreException, URISyntaxException, InterruptedException {
		final ModelSet leftModelSet = createProjectWithModelSet("Left");
		final ModelSet rightModelSet = createProjectWithModelSet("Right");
		final ModelDependencyProviderRegistry emptyRegistry = new ModelDependencyProviderRegistry();
		final ThreadedModelResolver resolver = createModelResolver(emptyRegistry);

		final SynchronizationModel synchronizationModel = resolver.resolveLocalModels(leftModelSet.file1,
				rightModelSet.file1, null, monitor);

		assertEquals(1, synchronizationModel.getLeftTraversal().getStorages().size());
		assertContainsFile(synchronizationModel.getLeftTraversal(), leftModelSet.file1);
		assertEquals(1, synchronizationModel.getRightTraversal().getStorages().size());
		assertContainsFile(synchronizationModel.getRightTraversal(), rightModelSet.file1);
		assertTrue(synchronizationModel.getOriginTraversal().getStorages().isEmpty());
	}

	@Test
	public void testResolveLocalModelsThreeWayFromFile1WithoutDependencyProvider() throws IOException,
			CoreException, URISyntaxException, InterruptedException {
		final ModelSet leftModelSet = createProjectWithModelSet("Left");
		final ModelSet rightModelSet = createProjectWithModelSet("Right");
		final ModelSet originModelSet = createProjectWithModelSet("Origin");
		final ModelDependencyProviderRegistry emptyRegistry = new ModelDependencyProviderRegistry();
		final ThreadedModelResolver resolver = createModelResolver(emptyRegistry);

		final SynchronizationModel synchronizationModel = resolver.resolveLocalModels(leftModelSet.file1,
				rightModelSet.file1, originModelSet.file1, monitor);

		assertEquals(1, synchronizationModel.getLeftTraversal().getStorages().size());
		assertContainsFile(synchronizationModel.getLeftTraversal(), leftModelSet.file1);
		assertEquals(1, synchronizationModel.getRightTraversal().getStorages().size());
		assertContainsFile(synchronizationModel.getRightTraversal(), rightModelSet.file1);
		assertEquals(1, synchronizationModel.getOriginTraversal().getStorages().size());
		assertContainsFile(synchronizationModel.getOriginTraversal(), originModelSet.file1);
	}

	@Test
	public void testResolveLocalModelsTwoWayFromFile1WithDependencyProvider() throws IOException,
			CoreException, URISyntaxException, InterruptedException {
		final ModelSet leftModelSet = createProjectWithModelSet("Left");
		final ModelSet rightModelSet = createProjectWithModelSet("Right");
		final ModelDependencyProviderRegistry registry = createRegistryWithCustomResolver();
		final ThreadedModelResolver resolver = createModelResolver(registry);

		final SynchronizationModel synchronizationModel = resolver.resolveLocalModels(leftModelSet.file1,
				rightModelSet.file1, null, monitor);

		assertContainsAllFiles(synchronizationModel.getLeftTraversal(), leftModelSet);
		assertContainsAllFiles(synchronizationModel.getRightTraversal(), rightModelSet);
		assertTrue(synchronizationModel.getOriginTraversal().getStorages().isEmpty());
	}

	@Test
	public void testResolveLocalModelsThreeWayFromFile1WithDependencyProvider() throws IOException,
			CoreException, URISyntaxException, InterruptedException {
		final ModelSet leftModelSet = createProjectWithModelSet("Left");
		final ModelSet rightModelSet = createProjectWithModelSet("Right");
		final ModelSet originModelSet = createProjectWithModelSet("Origin");
		final ModelDependencyProviderRegistry registry = createRegistryWithCustomResolver();
		final ThreadedModelResolver resolver = createModelResolver(registry);

		final SynchronizationModel synchronizationModel = resolver.resolveLocalModels(leftModelSet.file1,
				rightModelSet.file1, originModelSet.file1, monitor);

		assertContainsAllFiles(synchronizationModel.getLeftTraversal(), leftModelSet);
		assertContainsAllFiles(synchronizationModel.getRightTraversal(), rightModelSet);
		assertContainsAllFiles(synchronizationModel.getOriginTraversal(), originModelSet);
	}

	private void assertContainsAllFiles(StorageTraversal traversal, ModelSet modelSet) {
		assertEquals(3, traversal.getStorages().size());
		assertContainsFile(traversal, modelSet.file1);
		assertContainsFile(traversal, modelSet.file3);
		assertContainsFile(traversal, modelSet.file2);
	}

	private void assertContainsFile(StorageTraversal traversal, final IFile iFile) {
		Iterables.any(traversal.getStorages(), containsFile(iFile));
	}

	private static Predicate<IStorage> containsFile(final IFile iFile) {
		return new Predicate<IStorage>() {
			public boolean apply(IStorage input) {
				return iFile.equals(input.getFullPath());
			}
		};
	}

	private ThreadedModelResolver createModelResolver(final ModelDependencyProviderRegistry reg) {
		ThreadedModelResolver resolver = new ThreadedModelResolver() {
			@Override
			protected ModelDependencyProviderRegistry getModelDependencyProviderRegistry() {
				if (reg == null) {
					return new ModelDependencyProviderRegistry();
				} else {
					return reg;
				}
			}
		};
		resolver.initialize();
		return resolver;
	}

	/**
	 * Creates a registry with a resolver that always adds two files of the model set (file1.ecore and
	 * file2.ecore) to the dependencies. The third file, file3.ecore, will be resolved by the
	 * {@link ThreadedModelResolver}, because file2.ecore has a dependency to file3.ecore.
	 * 
	 * @return The registry containing the fixed model set dependency provider.
	 */
	private ModelDependencyProviderRegistry createRegistryWithCustomResolver() {
		final ModelDependencyProviderRegistry registry = new ModelDependencyProviderRegistry() {
			@Override
			public Set<URI> getDependencies(URI uri) {
				final String uriString = uri.toPlatformString(false);
				final String baseUriString = uriString.substring(0, uriString.lastIndexOf("/"));
				final String file1UriString = baseUriString + "/" + MODEL_FILE1;
				final String file2UriString = baseUriString + "/" + MODEL_FILE2;
				final URI file1Uri = URI.createPlatformResourceURI(file1UriString, false);
				final URI file2Uri = URI.createPlatformResourceURI(file2UriString, false);
				final LinkedHashSet<URI> dependencies = Sets.newLinkedHashSet();
				dependencies.add(file1Uri);
				dependencies.add(file2Uri);
				return dependencies;
			}
		};
		return registry;
	}

	private ModelSet createProjectWithModelSet() throws CoreException, IOException, URISyntaxException {
		return createProjectWithModelSet("Project");
	}

	private ModelSet createProjectWithModelSet(String name) throws CoreException, IOException,
			URISyntaxException {
		final IProject iProject = new TestProject(name).getProject();
		final File file1 = project.getOrCreateFile(iProject, MODEL_FILE1);
		final File file2 = project.getOrCreateFile(iProject, MODEL_FILE2);
		final File file3 = project.getOrCreateFile(iProject, MODEL_FILE3);
		final Bundle bundle = Platform.getBundle(TEST_BUNDLE);
		final URI file1Uri = getFileUri(bundle.getEntry(TEST_DATA_PATH + MODEL_FILE1));
		final URI file2Uri = getFileUri(bundle.getEntry(TEST_DATA_PATH + MODEL_FILE2));
		final URI file3Uri = getFileUri(bundle.getEntry(TEST_DATA_PATH + MODEL_FILE3));

		copyFile(toFile(file1Uri), file1);
		copyFile(toFile(file2Uri), file2);
		copyFile(toFile(file3Uri), file3);

		final IFile iFile1 = project.getIFile(iProject, file1);
		final IFile iFile2 = project.getIFile(iProject, file2);
		final IFile iFile3 = project.getIFile(iProject, file3);

		return new ModelSet(iFile1, iFile2, iFile3);
	}

	private URI getFileUri(final URL bundleUrl) throws IOException {
		URL fileLocation = FileLocator.toFileURL(bundleUrl);
		return URI.createFileURI(fileLocation.getPath());
	}

	private File toFile(final URI fileUri) throws URISyntaxException {
		return new File(fileUri.toFileString());
	}

	private class ModelSet {
		protected IFile file1;

		protected IFile file2;

		protected IFile file3;

		public ModelSet(IFile file1, IFile file2, IFile file3) {
			this.file1 = file1;
			this.file2 = file2;
			this.file3 = file3;
		}
	}

}

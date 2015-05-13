/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.logical.modelprovider;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.mapping.IModelProviderDescriptor;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.resources.mapping.RemoteResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceTraversal;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.EMFModelProvider;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.emf.compare.ide.ui.tests.CompareTestCase;
import org.eclipse.emf.compare.ide.ui.tests.workspace.TestProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.AfterClass;
import org.junit.Test;
import org.osgi.framework.Bundle;

/**
 * Tests the EMFModelProvider to return a minimum number of {@link ResourceMapping}s in cases where it is not
 * straightforward (e.g. resolution scope set to "workspace") but still doable since it has all required input
 * resources.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
@SuppressWarnings({"nls", "restriction" })
public class EMFModelProviderTest extends CompareTestCase {

	/**
	 * Path to the test data.
	 */
	private static final String TEST_DATA_PATH = "src/org/eclipse/emf/compare/ide/ui/tests/logical/modelprovider/data/";

	/**
	 * The bundle containing this test.
	 */
	private static final String TEST_BUNDLE = "org.eclipse.emf.compare.ide.ui.tests";

	/**
	 * Name of test project 1.
	 */
	private static final String PROJECT_NAME_1 = "project1";

	/**
	 * Name of test project 2.
	 */
	private static final String PROJECT_NAME_2 = "project2";

	/**
	 * Name of test project 3.
	 */
	private static final String PROJECT_NAME_3 = "project3";

	/**
	 * Reset the resolution scope preference.
	 */
	@AfterClass
	public static void setDefaultResolutionScope() {
		final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault().getPreferenceStore();
		store.setToDefault(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE);
	}

	/**
	 * Delete created projects.
	 */
	@AfterClass
	public static void deleteProjects() throws CoreException {
		deleteProject(PROJECT_NAME_1);
		deleteProject(PROJECT_NAME_2);
		deleteProject(PROJECT_NAME_3);
	}

	/**
	 * Deletes the project wit the given {@code name}.
	 * 
	 * @param name
	 *            The name of the project which shall be deleted.
	 */
	private static void deleteProject(String name) throws CoreException {
		final IProject project = new TestProject(name).getProject();
		project.delete(true, true, new NullProgressMonitor());
	}

	/**
	 * Sets the resolution scope to the given preference.
	 * 
	 * @param preference
	 *            The value to which the resolution scope preference is set.
	 */
	private void setResolutionScope(String preference) {
		final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault().getPreferenceStore();
		store.setValue(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE, preference);
	}

	/**
	 * Creates the projects and test files for case1.
	 * 
	 * @return The created test files.
	 */
	private IFile[] createProjectFilesCase1() throws CoreException, IOException, URISyntaxException {
		IProject project1 = new TestProject(PROJECT_NAME_1).getProject();
		IProject project2 = new TestProject(PROJECT_NAME_2).getProject();
		IProject project3 = new TestProject(PROJECT_NAME_3).getProject();
		IFile file1 = addToProject(project1, "case1/file1.ecore", "");
		IFile file2 = addToProject(project2, "case1/file2.ecore", "");
		IFile file3 = addToProject(project3, "case1/file3.ecore", "");
		return new IFile[] {file1, file2, file3 };
	}

	/**
	 * Creates the projects and test files for case2.
	 * 
	 * @return The created test files.
	 */
	private IFile[] createProjectFilesCase2() throws CoreException, IOException, URISyntaxException {
		IProject project1 = new TestProject(PROJECT_NAME_1).getProject();
		IProject project2 = new TestProject(PROJECT_NAME_2).getProject();
		IProject project3 = new TestProject(PROJECT_NAME_3).getProject();
		IFile file1 = addToProject(project1, "case2/file1.ecore", "");
		IFile file2 = addToProject(project2, "case2/file2.ecore", "");
		IFile file3 = addToProject(project3, "case2/file3.ecore", "");
		return new IFile[] {file1, file2, file3 };
	}

	/**
	 * Creates the projects and test files for case3.
	 * 
	 * @return The created test files.
	 */
	private IFile[] createProjectFilesCase3() throws CoreException, IOException, URISyntaxException {
		IProject project1 = new TestProject(PROJECT_NAME_1).getProject();
		IFile file1 = addToProject(project1, "case3/file1.ecore", "");
		IFile file2 = addToProject(project1, "case3/file2.ecore", "/a/");
		IFile file3 = addToProject(project1, "case3/file3.ecore", "/a/");
		return new IFile[] {file1, file2, file3 };
	}

	/**
	 * Tests the EMFModelProvider with a requirements row:
	 * {@code project1/file1 -> project2/file2 -> project3/file3} and resolution scope
	 * {@link CrossReferenceResolutionScope#OUTGOING}.
	 */
	@Test
	public void testResourceMappingCase1_Outgoing() throws CoreException, IOException, URISyntaxException {
		setResolutionScope(CrossReferenceResolutionScope.OUTGOING.toString());
		final IFile[] files = createProjectFilesCase1();

		final ResourceMapping[] singleResourceMappings = collectSingleResourceMappings(files);
		// {file1,file2,file3}, {file2,file3}, {file3}
		assertEquals(3, singleResourceMappings.length);

		final ResourceMapping[] combinedResourceMappings = getCombinedResourceMappings(files);
		// EMFModelProvider can optimize to {file1,file2,file3}
		assertEquals(1, combinedResourceMappings.length);
	}

	/**
	 * Tests the EMFModelProvider with a requirements row:
	 * {@code project1/file1 -> project2/file2 -> project3/file3} and resolution scope
	 * {@link CrossReferenceResolutionScope#CONTAINER}.
	 */
	@Test
	public void testResourceMappingCase1_Container() throws CoreException, IOException, URISyntaxException {
		setResolutionScope(CrossReferenceResolutionScope.CONTAINER.toString());
		final IFile[] files = createProjectFilesCase1();

		final ResourceMapping[] singleResourceMappings = collectSingleResourceMappings(files);
		// {file1}, {file2}, {file3}
		assertEquals(3, singleResourceMappings.length);

		final ResourceMapping[] combinedResourceMappings = getCombinedResourceMappings(files);
		// EMFModelProvider can not optimize and returns the same
		assertEquals(3, combinedResourceMappings.length);
	}

	/**
	 * Tests the EMFModelProvider with a requirements row:
	 * {@code project1/file1 -> project2/file2 -> project3/file3} and resolution scope
	 * {@link CrossReferenceResolutionScope#PROJECT}.
	 */
	@Test
	public void testResourceMappingCase1_Project() throws CoreException, IOException, URISyntaxException {
		setResolutionScope(CrossReferenceResolutionScope.PROJECT.toString());
		final IFile[] files = createProjectFilesCase1();

		final ResourceMapping[] singleResourceMappings = collectSingleResourceMappings(files);
		// {file1}, {file2}, {file3}
		assertEquals(3, singleResourceMappings.length);

		final ResourceMapping[] combinedResourceMappings = getCombinedResourceMappings(files);
		// EMFModelProvider can not optimize and returns the same
		assertEquals(3, combinedResourceMappings.length);
	}

	/**
	 * Tests the EMFModelProvider with a requirements row:
	 * {@code project1/file1 -> project2/file2 -> project3/file3} and resolution scope
	 * {@link CrossReferenceResolutionScope#WORKSPACE}.
	 */
	@Test
	public void testResourceMappingCase1_Workspace() throws CoreException, IOException, URISyntaxException {
		setResolutionScope(CrossReferenceResolutionScope.WORKSPACE.toString());
		final IFile[] files = createProjectFilesCase1();

		final ResourceMapping[] singleResourceMappings = collectSingleResourceMappings(files);
		// {file1,file2,file3}
		assertEquals(1, singleResourceMappings.length);

		final ResourceMapping[] combinedResourceMappings = getCombinedResourceMappings(files);
		// The mapping can not be optimized further
		assertEquals(1, combinedResourceMappings.length);
	}

	/**
	 * Tests the EMFModelProvider with a requirements fork: {@code project1/file1 -> project3/file3} &
	 * {@code project2/file2 -> project3/file3} and resolution scope
	 * {@link CrossReferenceResolutionScope#OUTGOING}.
	 */
	@Test
	public void testResourceMappingCase2_Outgoing() throws CoreException, IOException, URISyntaxException {
		setResolutionScope(CrossReferenceResolutionScope.OUTGOING.toString());
		final IFile[] files = createProjectFilesCase2();

		final ResourceMapping[] singleResourceMappings = collectSingleResourceMappings(files);
		// {file1,file2,file3}, {file3}
		assertEquals(2, singleResourceMappings.length);

		final ResourceMapping[] combinedResourceMappings = getCombinedResourceMappings(files);
		// EMFModelProvider can optimize to {file1,file2,file3}
		assertEquals(1, combinedResourceMappings.length);
	}

	/**
	 * Tests the EMFModelProvider with a requirements fork: {@code project1/file1 -> project3/file3} &
	 * {@code project2/file2 -> project3/file3} and resolution scope
	 * {@link CrossReferenceResolutionScope#CONTAINER}.
	 */
	@Test
	public void testResourceMappingCase2_Container() throws CoreException, IOException, URISyntaxException {
		setResolutionScope(CrossReferenceResolutionScope.CONTAINER.toString());
		final IFile[] files = createProjectFilesCase2();

		final ResourceMapping[] singleResourceMappings = collectSingleResourceMappings(files);
		// {file1}, {file2}, {file3}
		assertEquals(3, singleResourceMappings.length);

		final ResourceMapping[] combinedResourceMappings = getCombinedResourceMappings(files);
		// EMFModelProvider can not optimize and returns the same
		assertEquals(3, combinedResourceMappings.length);
	}

	/**
	 * Tests the EMFModelProvider with a requirements fork: {@code project1/file1 -> project3/file3} &
	 * {@code project2/file2 -> project3/file3} and resolution scope
	 * {@link CrossReferenceResolutionScope#PROJECT}.
	 */
	@Test
	public void testResourceMappingCase2_Project() throws CoreException, IOException, URISyntaxException {
		setResolutionScope(CrossReferenceResolutionScope.PROJECT.toString());
		final IFile[] files = createProjectFilesCase2();

		final ResourceMapping[] singleResourceMappings = collectSingleResourceMappings(files);
		// {file1}, {file2}, {file3}
		assertEquals(3, singleResourceMappings.length);

		final ResourceMapping[] combinedResourceMappings = getCombinedResourceMappings(files);
		// EMFModelProvider can not optimize and returns the same
		assertEquals(3, combinedResourceMappings.length);
	}

	/**
	 * Tests the EMFModelProvider with a requirements fork: {@code project1/file1 -> project3/file3} &
	 * {@code project2/file2 -> project3/file3} and resolution scope
	 * {@link CrossReferenceResolutionScope#WORKSPACE}.
	 */
	@Test
	public void testResourceMappingCase2_Workspace() throws CoreException, IOException, URISyntaxException {
		setResolutionScope(CrossReferenceResolutionScope.WORKSPACE.toString());
		final IFile[] files = createProjectFilesCase2();

		final ResourceMapping[] singleResourceMappings = collectSingleResourceMappings(files);
		// {file1,file2,file3}
		assertEquals(1, singleResourceMappings.length);

		final ResourceMapping[] combinedResourceMappings = getCombinedResourceMappings(files);
		// The mapping can not be optimized further
		assertEquals(1, combinedResourceMappings.length);
	}

	/**
	 * Tests the EMFModelProvider with a requirements row: {@code file1 -> a/file2 -> a/file3} and resolution
	 * scope {@link CrossReferenceResolutionScope#OUTGOING}.
	 */
	@Test
	public void testResourceMappingCase3_Outgoing() throws CoreException, IOException, URISyntaxException {
		setResolutionScope(CrossReferenceResolutionScope.OUTGOING.toString());
		final IFile[] files = createProjectFilesCase3();

		final ResourceMapping[] singleResourceMappings = collectSingleResourceMappings(files);
		// {file1,file2,file3}, {file2,file3}, {file3}
		assertEquals(3, singleResourceMappings.length);

		final ResourceMapping[] combinedResourceMappings = getCombinedResourceMappings(files);
		// EMFModelProvider can optimize to {file1,file2,file3}
		assertEquals(1, combinedResourceMappings.length);
	}

	/**
	 * Tests the EMFModelProvider with a requirements row: {@code file1 -> a/file2 -> a/file3} and resolution
	 * scope {@link CrossReferenceResolutionScope#CONTAINER}.
	 */
	@Test
	public void testResourceMappingCase3_Container() throws CoreException, IOException, URISyntaxException {
		setResolutionScope(CrossReferenceResolutionScope.CONTAINER.toString());
		final IFile[] files = createProjectFilesCase3();

		final ResourceMapping[] singleResourceMappings = collectSingleResourceMappings(files);
		// {file1}, {file2,file3}
		assertEquals(2, singleResourceMappings.length);

		final ResourceMapping[] combinedResourceMappings = getCombinedResourceMappings(files);
		// EMFModelProvider can optimize to {file1,file2,file3}
		assertEquals(1, combinedResourceMappings.length);
	}

	/**
	 * Tests the EMFModelProvider with a requirements row: {@code file1 -> a/file2 -> a/file3} and resolution
	 * scope {@link CrossReferenceResolutionScope#PROJECT}.
	 */
	@Test
	public void testResourceMappingCase3_Project() throws CoreException, IOException, URISyntaxException {
		setResolutionScope(CrossReferenceResolutionScope.PROJECT.toString());
		final IFile[] files = createProjectFilesCase3();

		final ResourceMapping[] singleResourceMappings = collectSingleResourceMappings(files);
		// {file1,file2,file3}
		assertEquals(1, singleResourceMappings.length);

		final ResourceMapping[] combinedResourceMappings = getCombinedResourceMappings(files);
		// The mapping can not be optimized further
		assertEquals(1, combinedResourceMappings.length);
	}

	/**
	 * Tests the EMFModelProvider with a requirements row: {@code file1 -> a/file2 -> a/file3} and resolution
	 * scope {@link CrossReferenceResolutionScope#WORKSPACE}.
	 */
	@Test
	public void testResourceMappingCase3_Workspace() throws CoreException, IOException, URISyntaxException {
		setResolutionScope(CrossReferenceResolutionScope.WORKSPACE.toString());
		final IFile[] files = createProjectFilesCase3();

		final ResourceMapping[] singleResourceMappings = collectSingleResourceMappings(files);
		// {file1,file2,file3}
		assertEquals(1, singleResourceMappings.length);

		final ResourceMapping[] combinedResourceMappings = getCombinedResourceMappings(files);
		// The mapping can not be optimized further
		assertEquals(1, combinedResourceMappings.length);
	}

	/**
	 * Collects all resource mappings of the given files by calling
	 * {@link EMFModelProvider#getMappings(IResource, org.eclipse.core.resources.mapping.ResourceMappingContext, IProgressMonitor)}
	 * on each of them and combining the results. This is how the default implementation within the
	 * ModelProvider class is implemented.
	 * 
	 * @param files
	 *            The files for which the resource mappings shall be calculated.
	 * @return The combined result of determining the resource mapping for each given file separately.
	 */
	private ResourceMapping[] collectSingleResourceMappings(IFile... files) throws CoreException {
		final EMFModelProvider emfModelProvider = getEMFModelProvider();
		Set<ResourceMapping> mappings = new HashSet<ResourceMapping>();
		for (int i = 0; i < files.length; i++) {
			IResource resource = files[i];
			ResourceMapping[] resourceMappings = emfModelProvider.getMappings(resource, new StubContext(),
					new NullProgressMonitor());
			if (resourceMappings.length > 0) {
				mappings.addAll(Arrays.asList(resourceMappings));
			}
		}
		return mappings.toArray(new ResourceMapping[mappings.size()]);
	}

	/**
	 * Returns the result of calling
	 * {@link EMFModelProvider#getTraversals(ResourceMapping[], org.eclipse.core.resources.mapping.ResourceMappingContext, IProgressMonitor)}
	 * to determine the ResourceMappings.
	 * 
	 * @param files
	 *            The files for which the resource mappings shall be determined.
	 * @return The resource mappings for the given file when {@link EMFModelProvider} is given a chance to
	 *         optimize.
	 */
	private ResourceMapping[] getCombinedResourceMappings(IFile... files) throws CoreException {
		final EMFModelProvider emfModelProvider = getEMFModelProvider();
		return emfModelProvider.getMappings(files, new StubContext(), new NullProgressMonitor());
	}

	private EMFModelProvider getEMFModelProvider() throws CoreException {
		final IModelProviderDescriptor[] descriptors = ModelProvider.getModelProviderDescriptors();
		for (IModelProviderDescriptor descriptor : descriptors) {
			if (descriptor.getModelProvider() instanceof EMFModelProvider) {
				return (EMFModelProvider)descriptor.getModelProvider();
			}
		}
		return null;
	}

	/**
	 * Copies the file located in {@link #TEST_DATA_PATH} + {@code filePath} to the given
	 * {@code destinationPath} in {@code iProject}.
	 * 
	 * @param iProject
	 *            The {@link IProject} to which the file is added.
	 * @param filePath
	 *            The path relative to {@link #TEST_DATA_PATH} where the file is originally located.
	 * @param destinationPath
	 *            The path in the {@code iProject} to which the file will be copied.
	 * @return The newly created {@link IFile}.
	 */
	private IFile addToProject(IProject iProject, String filePath, String destinationPath)
			throws IOException, URISyntaxException, CoreException {
		final Bundle bundle = Platform.getBundle(TEST_BUNDLE);
		final URI fileUri = getFileUri(bundle.getEntry(TEST_DATA_PATH + filePath));

		final File file = project.getOrCreateFile(iProject, destinationPath + fileUri.lastSegment());

		copyFile(toFile(fileUri), file);

		return project.getIFile(iProject, file);
	}

	private URI getFileUri(final URL bundleUrl) throws IOException {
		URL fileLocation = FileLocator.toFileURL(bundleUrl);
		return URI.createFileURI(fileLocation.getPath());
	}

	private File toFile(final URI fileUri) throws URISyntaxException {
		return new File(fileUri.toFileString());
	}

	/**
	 * Stub for a {@link RemoteResourceMappingContext}. The class is used to trigger the creation of a proper
	 * comparison scope within the {@link EMFModelProvider}.
	 */
	private class StubContext extends RemoteResourceMappingContext {

		@Override
		public IStorage fetchBaseContents(IFile file, IProgressMonitor monitor) throws CoreException {
			return null;
		}

		@Override
		public IResource[] fetchMembers(IContainer container, IProgressMonitor monitor) throws CoreException {
			return new IResource[0];
		}

		@Override
		public IStorage fetchRemoteContents(IFile file, IProgressMonitor monitor) throws CoreException {
			return null;
		}

		@Override
		public IProject[] getProjects() {
			return ResourcesPlugin.getWorkspace().getRoot().getProjects();
		}

		@Override
		public boolean hasLocalChange(IResource resource, IProgressMonitor monitor) throws CoreException {
			return true;
		}

		@Override
		public boolean hasRemoteChange(IResource resource, IProgressMonitor monitor) throws CoreException {
			return true;
		}

		@Override
		public boolean isThreeWay() {
			return false;
		}

		@Override
		public void refresh(ResourceTraversal[] traversals, int flags, IProgressMonitor monitor)
				throws CoreException {
		}

	}
}

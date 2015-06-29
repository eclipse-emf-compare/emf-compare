/*******************************************************************************
 * Copyright (C) 2015 Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.merge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.GitCorePreferences;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.EMFModelProvider;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.emf.compare.ide.ui.tests.CompareTestCase;
import org.eclipse.emf.compare.ide.ui.tests.egit.fixture.GitTestRepository;
import org.eclipse.emf.compare.ide.ui.tests.egit.fixture.MockSystemReader;
import org.eclipse.emf.compare.ide.ui.tests.workspace.TestProject;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.util.FileUtils;
import org.eclipse.jgit.util.SystemReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests with EGit shared models.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings({"restriction", "nls" })
public class RemoteNewProjectTests extends CompareTestCase {

	private static final String MASTER = Constants.R_HEADS + Constants.MASTER;

	private static final String BRANCH_1 = Constants.R_HEADS + "branch1";

	private static final String BRANCH_2 = Constants.R_HEADS + "branch2";

	private static final String BRANCH_3 = Constants.R_HEADS + "branch3";

	protected GitTestRepository repository;

	// The ".git" folder of the test repository
	private File gitDir;

	private static String deafultResolutionScope;

	@BeforeClass
	public static void setUpClass() {
		// suppress auto-ignoring and auto-sharing to avoid interference
		IEclipsePreferences eGitPreferences = InstanceScope.INSTANCE.getNode(Activator.getPluginId());
		eGitPreferences.put(GitCorePreferences.core_preferredMergeStrategy, "model recursive");
		eGitPreferences.putBoolean(GitCorePreferences.core_autoShareProjects, false);
		// This is actually the value of "GitCorePreferences.core_autoIgnoreDerivedResources"... but it was
		// not in Egit 2.1
		eGitPreferences.putBoolean("core_autoIgnoreDerivedResources", false);
		final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault().getPreferenceStore();
		deafultResolutionScope = store.getString(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE);
		store.setValue(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE,
				CrossReferenceResolutionScope.WORKSPACE.name());
	}

	@AfterClass
	public static void tearDownClass() {
		final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault().getPreferenceStore();
		store.setValue(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE, deafultResolutionScope);
	}

	@Override
	@Before
	public void setUp() throws Exception {
		// ensure there are no shared Repository instances left
		// when starting a new test
		Activator.getDefault().getRepositoryCache().clear();
		final MockSystemReader mockSystemReader = new MockSystemReader();
		SystemReader.setInstance(mockSystemReader);
		final IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		String gitRepoPath = workspaceRoot.getRawLocation().toFile() + File.separator + "repo";
		mockSystemReader.setProperty(Constants.GIT_CEILING_DIRECTORIES_KEY, workspaceRoot.getLocation()
				.toFile().getParentFile().getAbsoluteFile().toString());
		gitDir = new File(gitRepoPath, Constants.DOT_GIT);
		repository = new GitTestRepository(gitDir);
		repository.ignore(workspaceRoot.getRawLocation().append(".metadata").toFile());
	}

	@Override
	@After
	public void tearDown() throws Exception {
		final EMFModelProvider emfModelProvider = (EMFModelProvider)ModelProvider.getModelProviderDescriptor(
				EMFModelProvider.PROVIDER_ID).getModelProvider();
		emfModelProvider.clear();
		repository.dispose();
		Activator.getDefault().getRepositoryCache().clear();
		if (gitDir.exists()) {
			File gitRoot = gitDir.getParentFile();
			if (gitRoot.exists()) {
				FileUtils.delete(gitRoot, FileUtils.RECURSIVE | FileUtils.RETRY);
			}
		}
	}

	/**
	 * Merge of a new remote project that contains a new fragment of a model. The projects are at the root of
	 * the git repository. The controlled model is at the root of its project.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCollaborativeModeling_UseCase001_MergeFragmentFromRemoteProject() throws Exception {
		ResourceSetImpl resourceSet = new ResourceSetImpl();
		File workingDirectory = repository.getRepository().getWorkTree();
		TestProject testProject1 = new TestProject("Project1", workingDirectory.getAbsolutePath());
		IProject iProject = testProject1.getProject();
		repository.connect(iProject);
		File file1 = testProject1.getOrCreateFile(iProject, "file1.ecore");
		IFile iFile1 = testProject1.getIFile(iProject, file1);
		Resource resource1 = connectResource(iFile1, resourceSet);

		// 1st commit:
		// One project at the root of the git repository
		// One model with a root package containing another package.
		// The model is at the root of the project.
		EPackage root = createPackage(null, "parent");
		EPackage child = createPackage(root, "child");
		createClass(child, "C1");
		resource1.getContents().add(root);
		save(resource1);
		repository.addAllAndCommit("1st-commit");
		repository.createBranch(MASTER, BRANCH_1);

		TestProject testProject2 = new TestProject("Project2", workingDirectory.getAbsolutePath());
		IProject iProject2 = testProject2.getProject();
		repository.connect(iProject2);
		File file2 = testProject2.getOrCreateFile(iProject2, "file2.ecore");
		IFile iFile2 = testProject2.getIFile(iProject2, file2);
		File file22 = testProject2.getOrCreateFile(iProject2, "file22.txt");
		IFile iFile22 = testProject2.getIFile(iProject2, file22);
		Resource resource2 = connectResource(iFile2, resourceSet);

		// 2nd commit:
		// Control the sub-package in a new project at the root of the git repository.
		// The controlled model is at the root of its project.
		// Also add a simple file in this new project.
		resource2.getContents().add(child);
		resource1.getContents().clear();
		resource1.getContents().add(root);
		save(resource2);
		save(resource1);
		repository.addAllAndCommit("2nd-commit");
		repository.createBranch(MASTER, BRANCH_2);

		// Back to 1st commit
		repository.reset(BRANCH_1, ResetType.HARD);

		// 3rd commit:
		// Add a new Package child2 under Package root.
		resourceSet.getResources().clear();
		resource1 = resourceSet.getResource(URI.createPlatformResourceURI(iFile1.getFullPath().toString(),
				true), true);
		root = (EPackage)resource1.getContents().get(0);
		EPackage child2 = createPackage(root, "child2");
		save(resource1);
		repository.addAllAndCommit("3rd-commit");
		repository.createBranch(MASTER, BRANCH_3);

		// Merge
		repository.mergeLogicalWithNewCommit(BRANCH_2);

		// No conflicts
		assertTrue(repository.status().getConflicting().isEmpty());

		// The project2 is not accessible.
		assertTrue(iProject.isAccessible());
		assertFalse(iProject2.isAccessible());
		// After the merge, the repository should contain all files from 2nd commit.
		assertTrue(iFile1.exists());
		assertFalse(iFile2.exists());
		assertFalse(iFile22.exists());
		assertTrue(file2.exists());
		assertTrue(file22.exists());
		// Check the contents
		resourceSet.getResources().clear();
		resource1 = resourceSet.getResource(URI.createPlatformResourceURI(iFile1.getFullPath().toString(),
				true), true);
		resource2 = resourceSet.getResource(URI.createFileURI(file2.getAbsolutePath()), true);
		// The proxy of child element has the following form : "platform:/resource/..."
		// So we have to update the uri of the resource2
		resource2.setURI(URI.createPlatformResourceURI(iFile2.getFullPath().toString(), true));
		EcoreUtil.resolveAll(resourceSet);
		root = (EPackage)resource1.getContents().get(0);
		assertEquals("parent", root.getName());
		// The sub-packages must be "child", and "child2" from the 3rd commit.
		child = root.getESubpackages().get(0);
		assertEquals("child", child.getName());
		assertSame(resource2, ((InternalEObject)child).eDirectResource());
		EClass testC1 = (EClass)child.getEClassifiers().get(0);
		assertEquals("C1", testC1.getName());
		child2 = root.getESubpackages().get(1);
		assertEquals("child2", child2.getName());

		testProject1.dispose();
		testProject2.dispose();

	}

	/**
	 * Merge of a new remote project that contains a new fragment of a model. The projects are at the root of
	 * the git repository. The controlled model is at the root of its project.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCollaborativeModeling_UseCase002_MergeFragmentContainingRenameFromRemoteProject()
			throws Exception {
		ResourceSetImpl resourceSet = new ResourceSetImpl();
		File workingDirectory = repository.getRepository().getWorkTree();
		TestProject testProject1 = new TestProject("Project1", workingDirectory.getAbsolutePath());
		IProject iProject = testProject1.getProject();
		repository.connect(iProject);
		File file1 = testProject1.getOrCreateFile(iProject, "file1.ecore");
		IFile iFile1 = testProject1.getIFile(iProject, file1);
		Resource resource1 = connectResource(iFile1, resourceSet);

		// 1st commit:
		// One project at the root of the git repository
		// One model with a root package containing another package.
		// The model is at the root of the project.
		EPackage root = createPackage(null, "parent");
		EPackage child = createPackage(root, "child");
		createClass(child, "C1");
		resource1.getContents().add(root);
		save(resource1);
		repository.addAllAndCommit("1st-commit");
		repository.createBranch(MASTER, BRANCH_1);

		TestProject testProject2 = new TestProject("Project2", workingDirectory.getAbsolutePath());
		IProject iProject2 = testProject2.getProject();
		repository.connect(iProject2);
		File file2 = testProject2.getOrCreateFile(iProject2, "file2.ecore");
		IFile iFile2 = testProject2.getIFile(iProject2, file2);
		File file22 = testProject2.getOrCreateFile(iProject2, "file22.txt");
		IFile iFile22 = testProject2.getIFile(iProject2, file22);
		Resource resource2 = connectResource(iFile2, resourceSet);

		// 2nd commit:
		// Control the sub-package in a new project at the root of the git repository.
		// The controlled model is at the root of its project.
		// Also add a simple file in this new project.
		resource2.getContents().add(child);
		resource1.getContents().clear();
		resource1.getContents().add(root);
		save(resource2);
		save(resource1);
		repository.addAllAndCommit("2nd-commit");
		repository.createBranch(MASTER, BRANCH_2);

		// Back to 1st commit
		repository.reset(BRANCH_1, ResetType.HARD);

		// 3rd commit:
		// Change the name of the sub-package.
		resourceSet.getResources().clear();
		resource1 = resourceSet.getResource(URI.createPlatformResourceURI(iFile1.getFullPath().toString(),
				true), true);
		root = (EPackage)resource1.getContents().get(0);
		child = root.getESubpackages().get(0);
		child.setName("newChild");
		save(resource1);
		repository.addAllAndCommit("3rd-commit");
		repository.createBranch(MASTER, BRANCH_3);

		// Merge
		repository.mergeLogicalWithNewCommit(BRANCH_2);

		// No conflicts
		assertTrue(repository.status().getConflicting().isEmpty());

		// The project2 is not accessible.
		assertTrue(iProject.isAccessible());
		assertFalse(iProject2.isAccessible());
		// After the merge, the repository should contain all files from 2nd commit.
		assertTrue(iFile1.exists());
		assertFalse(iFile2.exists());
		assertFalse(iFile22.exists());
		assertTrue(file2.exists());
		assertTrue(file22.exists());
		// Check the contents
		resourceSet.getResources().clear();
		resource1 = resourceSet.getResource(URI.createPlatformResourceURI(iFile1.getFullPath().toString(),
				true), true);
		resource2 = resourceSet.getResource(URI.createFileURI(file2.getAbsolutePath()), true);
		// The proxy of child element has the following form : "platform:/resource/..."
		// So we have to update the uri of the resource2
		resource2.setURI(URI.createPlatformResourceURI(iFile2.getFullPath().toString(), true));
		EcoreUtil.resolveAll(resourceSet);
		root = (EPackage)resource1.getContents().get(0);
		assertEquals("parent", root.getName());
		// The sub-package must be "newChild" from the 3rd commit.
		child = root.getESubpackages().get(0);
		assertEquals("newChild", child.getName());
		assertSame(resource2, ((InternalEObject)child).eDirectResource());
		EClass testC1 = (EClass)child.getEClassifiers().get(0);
		assertEquals("C1", testC1.getName());

		testProject1.dispose();
		testProject2.dispose();

	}

	/**
	 * Merge of a model that reference an object from a project outside of the repository. The project outside
	 * of the repository is in the workspace.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCollaborativeModeling_UseCase003_MergeFragmentFromOutsideRepository() throws Exception {
		ResourceSetImpl resourceSet = new ResourceSetImpl();
		File workingDirectory = repository.getRepository().getWorkTree();
		TestProject testProject1 = new TestProject("Project1", workingDirectory.getAbsolutePath());
		IProject iProject = testProject1.getProject();
		repository.connect(iProject);
		File file1 = testProject1.getOrCreateFile(iProject, "file1.ecore");
		IFile iFile1 = testProject1.getIFile(iProject, file1);
		Resource resource1 = connectResource(iFile1, resourceSet);

		// 1st commit:
		// One project at the root of the git repository
		// One model with a root package containing a class.
		// The model is at the root of the project.
		EPackage root = createPackage(null, "parent");
		resource1.getContents().add(root);
		EClass c1 = createClass(root, "C1");
		save(resource1);
		repository.addAllAndCommit("1st-commit");
		repository.createBranch(MASTER, BRANCH_1);

		TestProject testProject2 = new TestProject("Project2", ResourcesPlugin.getWorkspace().getRoot()
				.getLocation().toString());
		IProject iProject2 = testProject2.getProject();
		File file2 = testProject2.getOrCreateFile(iProject2, "file2.ecore");
		IFile iFile2 = testProject2.getIFile(iProject2, file2);
		File file22 = testProject2.getOrCreateFile(iProject2, "file22.txt");
		IFile iFile22 = testProject2.getIFile(iProject2, file22);
		Resource resource2 = connectResource(iFile2, resourceSet);
		EPackage root2 = createPackage(null, "parent2");
		EClass c2 = createClass(root2, "C2");
		resource2.getContents().add(root2);
		save(resource2);

		// 2nd commit:
		// One project not shared in the git repository.
		// One model with a root package.
		// The model is at the root of the project.
		// Add C2 as a new supertype of C1
		c1.getESuperTypes().add(c2);
		save(resource1);
		repository.addAllAndCommit("2nd-commit");
		repository.createBranch(MASTER, BRANCH_2);

		// Back to 1st commit
		repository.reset(BRANCH_1, ResetType.HARD);

		// 3rd commit:
		// Add a new attribute under C1
		resourceSet.getResources().clear();
		resource1 = resourceSet.getResource(URI.createPlatformResourceURI(iFile1.getFullPath().toString(),
				true), true);
		root = (EPackage)resource1.getContents().get(0);
		c1 = (EClass)root.getEClassifiers().get(0);
		EAttribute att1 = createAttribute(c1, "attribute1", null);
		save(resource1);
		repository.addAllAndCommit("3rd-commit");
		repository.createBranch(MASTER, BRANCH_3);

		// Merge
		repository.mergeLogicalWithNewCommit(BRANCH_2);

		// No conflicts
		assertTrue(repository.status().getConflicting().isEmpty());

		// The project are accessible.
		assertTrue(iProject.isAccessible());
		assertTrue(iProject2.isAccessible());
		// After the merge, the repository should contain all files from 2nd commit.
		assertTrue(iFile1.exists());
		assertTrue(iFile2.exists());
		assertTrue(iFile22.exists());
		assertTrue(file2.exists());
		assertTrue(file22.exists());
		// Check the contents
		resourceSet.getResources().clear();
		resource1 = resourceSet.getResource(URI.createPlatformResourceURI(iFile1.getFullPath().toString(),
				true), true);
		resource2 = resourceSet.getResource(URI.createPlatformResourceURI(iFile2.getFullPath().toString(),
				true), true);
		root = (EPackage)resource1.getContents().get(0);
		assertEquals("parent", root.getName());
		c1 = (EClass)root.getEClassifiers().get(0);
		assertEquals("C1", c1.getName());
		att1 = c1.getEAttributes().get(0);
		assertEquals("attribute1", att1.getName());
		root2 = (EPackage)resource2.getContents().get(0);
		assertEquals("parent2", root2.getName());
		c2 = (EClass)root2.getEClassifiers().get(0);
		assertEquals("C2", c2.getName());
		assertEquals(c2, c1.getESuperTypes().get(0));

		testProject1.dispose();
		testProject2.dispose();

	}

	/**
	 * Merge of a new remote project that contains a new fragment of a model. The projects are NOT at the root
	 * of the git repository. The controlled model is NOT at the root of its project.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCollaborativeModeling_UseCase004_MergeFragmentFromRemoteProject_NotAtRepoRoot()
			throws Exception {
		ResourceSetImpl resourceSet = new ResourceSetImpl();
		File workingDirectory = repository.getRepository().getWorkTree();
		TestProject testProject1 = new TestProject("Project1", workingDirectory.getAbsolutePath()
				+ "/a/b/c/d/e/f/g/");
		IProject iProject = testProject1.getProject();
		repository.connect(iProject);
		File file1 = testProject1.getOrCreateFile(iProject, "folder1/folder2/folder3/file1.ecore");
		IFile iFile1 = testProject1.getIFile(iProject, file1);
		Resource resource1 = connectResource(iFile1, resourceSet);

		// 1st commit:
		// One project at the root of the git repository
		// One model with a root package containing another package.
		// The model is at the root of the project.
		EPackage root = createPackage(null, "parent");
		EPackage child = createPackage(root, "child");
		createClass(child, "C1");
		resource1.getContents().add(root);
		save(resource1);
		repository.addAllAndCommit("1st-commit");
		repository.createBranch(MASTER, BRANCH_1);

		TestProject testProject2 = new TestProject("Project2", workingDirectory.getAbsolutePath() + "/h/i/j/");
		IProject iProject2 = testProject2.getProject();
		repository.connect(iProject2);
		File file2 = testProject2.getOrCreateFile(iProject2, "folder1/folder2/folder3/folder4/file2.ecore");
		IFile iFile2 = testProject2.getIFile(iProject2, file2);
		File file22 = testProject2.getOrCreateFile(iProject2, "folder1/folder2/file22.txt");
		IFile iFile22 = testProject2.getIFile(iProject2, file22);
		Resource resource2 = connectResource(iFile2, resourceSet);

		// 2nd commit:
		// Control the sub-package in a new project at the root of the git repository.
		// The controlled model is at the root of its project.
		// Also add a simple file in this new project.
		resource2.getContents().add(child);
		resource1.getContents().clear();
		resource1.getContents().add(root);
		save(resource2);
		save(resource1);
		repository.addAllAndCommit("2nd-commit");
		repository.createBranch(MASTER, BRANCH_2);

		// Back to 1st commit
		repository.reset(BRANCH_1, ResetType.HARD);

		// 3rd commit:
		// Change the name of the sub-package.
		resourceSet.getResources().clear();
		resource1 = resourceSet.getResource(URI.createFileURI(file1.getAbsolutePath()), true);
		root = (EPackage)resource1.getContents().get(0);
		child = root.getESubpackages().get(0);
		child.setName("newChild");
		save(resource1);
		repository.addAllAndCommit("3rd-commit");
		repository.createBranch(MASTER, BRANCH_3);

		// Merge
		repository.mergeLogicalWithNewCommit(BRANCH_2);

		// No conflicts
		assertTrue(repository.status().getConflicting().isEmpty());

		// The project2 is not accessible.
		assertTrue(iProject.isAccessible());
		assertFalse(iProject2.isAccessible());
		// After the merge, the repository should contain all files from 2nd commit.
		assertTrue(iFile1.exists());
		assertFalse(iFile2.exists());
		assertFalse(iFile22.exists());
		assertTrue(file2.exists());
		assertTrue(file22.exists());
		// Check the contents
		resourceSet.getResources().clear();
		resource1 = resourceSet.getResource(URI.createPlatformResourceURI(iFile1.getFullPath().toString(),
				true), true);
		resource2 = resourceSet.getResource(URI.createFileURI(file2.getAbsolutePath()), true);
		// The proxy of child element has the following form : "platform:/resource/..."
		// So we have to update the uri of the resource2
		resource2.setURI(URI.createPlatformResourceURI(iFile2.getFullPath().toString(), true));
		EcoreUtil.resolveAll(resourceSet);
		root = (EPackage)resource1.getContents().get(0);
		assertEquals("parent", root.getName());
		// The sub-package must be "newChild" from the 3rd commit.
		child = root.getESubpackages().get(0);
		assertEquals("newChild", child.getName());
		assertSame(resource2, ((InternalEObject)child).eDirectResource());
		EClass testC1 = (EClass)child.getEClassifiers().get(0);
		assertEquals("C1", testC1.getName());
	}

	/**
	 * Merge of a new remote project that contains a new fragment of a model. The new remote project is
	 * contained in another project.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCollaborativeModeling_UseCase005_MergeFragmentFromRemoteProjectInsideAnotherProject()
			throws Exception {
		ResourceSetImpl resourceSet = new ResourceSetImpl();
		File workingDirectory = repository.getRepository().getWorkTree();
		TestProject testProject1 = new TestProject("Project1", workingDirectory.getAbsolutePath());
		IProject iProject = testProject1.getProject();
		repository.connect(iProject);
		File file1 = testProject1.getOrCreateFile(iProject, "file1.ecore");
		IFile iFile1 = testProject1.getIFile(iProject, file1);
		Resource resource1 = connectResource(iFile1, resourceSet);

		// 1st commit:
		// One project at the root of the git repository
		// One model with a root package containing another package.
		// The model is at the root of the project.
		EPackage root = createPackage(null, "parent");
		EPackage child = createPackage(root, "child");
		createClass(child, "C1");
		resource1.getContents().add(root);
		save(resource1);
		repository.addAllAndCommit("1st-commit");
		repository.createBranch(MASTER, BRANCH_1);

		TestProject testProject2 = new TestProject("Project2", workingDirectory.getAbsolutePath()
				+ "/Project1/a/");
		IProject iProject2 = testProject2.getProject();
		repository.connect(iProject2);
		File file2 = testProject2.getOrCreateFile(iProject2, "file2.ecore");
		IFile iFile2 = testProject2.getIFile(iProject2, file2);
		File file22 = testProject2.getOrCreateFile(iProject2, "folder1/folder2/file22.txt");
		IFile iFile22 = testProject2.getIFile(iProject2, file22);
		Resource resource2 = connectResource(iFile2, resourceSet);

		// 2nd commit:
		// Control the sub-package in a new project at the root of the git repository.
		// The controlled model is at the root of its project.
		// Also add a simple file in this new project.
		resource2.getContents().add(child);
		resource1.getContents().clear();
		resource1.getContents().add(root);
		save(resource2);
		save(resource1);
		repository.addAllAndCommit("2nd-commit");
		repository.createBranch(MASTER, BRANCH_2);

		// Back to 1st commit
		repository.reset(BRANCH_1, ResetType.HARD);

		// 3rd commit:
		// Change the name of the sub-package.
		resourceSet.getResources().clear();
		resource1 = resourceSet.getResource(URI.createFileURI(file1.getAbsolutePath()), true);
		root = (EPackage)resource1.getContents().get(0);
		child = root.getESubpackages().get(0);
		child.setName("newChild");
		save(resource1);
		repository.addAllAndCommit("3rd-commit");
		repository.createBranch(MASTER, BRANCH_3);

		// Merge
		repository.mergeLogicalWithNewCommit(BRANCH_2);

		// No conflicts
		assertTrue(repository.status().getConflicting().isEmpty());

		// The project2 is not accessible.
		assertTrue(iProject.isAccessible());
		assertFalse(iProject2.isAccessible());
		// After the merge, the repository should contain all files from 2nd commit.
		assertTrue(iFile1.exists());
		assertFalse(iFile2.exists());
		assertFalse(iFile22.exists());
		assertTrue(file2.exists());
		assertTrue(file22.exists());
		// Check the contents
		resourceSet.getResources().clear();
		resource1 = resourceSet.getResource(URI.createPlatformResourceURI(iFile1.getFullPath().toString(),
				true), true);
		resource2 = resourceSet.getResource(URI.createFileURI(file2.getAbsolutePath()), true);
		// The proxy of child element has the following form : "platform:/resource/..."
		// So we have to update the uri of the resource2
		resource2.setURI(URI.createPlatformResourceURI(iFile2.getFullPath().toString(), true));
		EcoreUtil.resolveAll(resourceSet);
		root = (EPackage)resource1.getContents().get(0);
		assertEquals("parent", root.getName());
		// The sub-package must be "newChild" from the 3rd commit.
		child = root.getESubpackages().get(0);
		assertEquals("newChild", child.getName());
		assertSame(resource2, ((InternalEObject)child).eDirectResource());
		EClass testC1 = (EClass)child.getEClassifiers().get(0);
		assertEquals("C1", testC1.getName());
	}

	/**
	 * Merge of a new remote project that contains two new fragments of a model. The projects are at the root
	 * of the git repository. The controlled models (fragments) are at the root of their project. The first
	 * fragment doesn't reference the second fragment.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCollaborativeModeling_UseCase006_MergeMultipleIndependentFragmentsFromRemoteProject()
			throws Exception {
		ResourceSetImpl resourceSet = new ResourceSetImpl();
		File workingDirectory = repository.getRepository().getWorkTree();
		TestProject testProject1 = new TestProject("Project1", workingDirectory.getAbsolutePath());
		IProject iProject = testProject1.getProject();
		repository.connect(iProject);
		File file1 = testProject1.getOrCreateFile(iProject, "file1.ecore");
		IFile iFile1 = testProject1.getIFile(iProject, file1);
		Resource resource1 = connectResource(iFile1, resourceSet);

		// 1st commit:
		// One project at the root of the git repository
		// One model with a root package containing another package.
		// The model is at the root of the project.
		EPackage root = createPackage(null, "parent");
		EPackage child1 = createPackage(root, "child1");
		createClass(child1, "C1");
		EPackage child2 = createPackage(root, "child2");
		createClass(child2, "C2");
		resource1.getContents().add(root);
		save(resource1);
		repository.addAllAndCommit("1st-commit");
		repository.createBranch(MASTER, BRANCH_1);

		TestProject testProject2 = new TestProject("Project2", workingDirectory.getAbsolutePath());
		IProject iProject2 = testProject2.getProject();
		repository.connect(iProject2);
		File file2 = testProject2.getOrCreateFile(iProject2, "file2.ecore");
		IFile iFile2 = testProject2.getIFile(iProject2, file2);
		File file3 = testProject2.getOrCreateFile(iProject2, "file3.ecore");
		IFile iFile3 = testProject2.getIFile(iProject2, file3);
		File file22 = testProject2.getOrCreateFile(iProject2, "file22.txt");
		IFile iFile22 = testProject2.getIFile(iProject2, file22);
		Resource resource2 = connectResource(iFile2, resourceSet);
		Resource resource3 = connectResource(iFile3, resourceSet);

		// 2nd commit:
		// Control the sub-packages in a new project at the root of the git repository.
		// The controlled models are at the root of its project.
		// Also add a simple file in this new project.
		resource2.getContents().add(child1);
		resource3.getContents().add(child2);
		resource1.getContents().clear();
		resource1.getContents().add(root);
		save(resource3);
		save(resource2);
		save(resource1);
		repository.addAllAndCommit("2nd-commit");
		repository.createBranch(MASTER, BRANCH_2);

		// Back to 1st commit
		repository.reset(BRANCH_1, ResetType.HARD);

		// 3rd commit:
		// Change the name of the sub-packages.
		resourceSet.getResources().clear();
		resource1 = resourceSet.getResource(URI.createPlatformResourceURI(iFile1.getFullPath().toString(),
				true), true);
		root = (EPackage)resource1.getContents().get(0);
		child1 = root.getESubpackages().get(0);
		child1.setName("newChild1");
		child2 = root.getESubpackages().get(1);
		child2.setName("newChild2");
		save(resource1);
		repository.addAllAndCommit("3rd-commit");
		repository.createBranch(MASTER, BRANCH_3);

		// Merge
		repository.mergeLogicalWithNewCommit(BRANCH_2);

		// No conflicts
		assertTrue(repository.status().getConflicting().isEmpty());

		// The project2 is not accessible.
		assertTrue(iProject.isAccessible());
		assertFalse(iProject2.isAccessible());
		// After the merge, the repository should contain all files from 2nd commit.
		assertTrue(iFile1.exists());
		assertFalse(iFile2.exists());
		assertFalse(iFile3.exists());
		assertFalse(iFile22.exists());
		assertTrue(file2.exists());
		assertTrue(file3.exists());
		assertTrue(file22.exists());
		// Check the contents
		resourceSet.getResources().clear();
		resource1 = resourceSet.getResource(URI.createPlatformResourceURI(iFile1.getFullPath().toString(),
				true), true);
		resource2 = resourceSet.getResource(URI.createFileURI(file2.getAbsolutePath()), true);
		resource3 = resourceSet.getResource(URI.createFileURI(file3.getAbsolutePath()), true);
		// The proxy of child element has the following form : "platform:/resource/..."
		// So we have to update the uri of the resource2
		resource2.setURI(URI.createPlatformResourceURI(iFile2.getFullPath().toString(), true));
		resource3.setURI(URI.createPlatformResourceURI(iFile3.getFullPath().toString(), true));
		EcoreUtil.resolveAll(resourceSet);
		root = (EPackage)resource1.getContents().get(0);
		assertEquals("parent", root.getName());
		// The first sub-package must be "newChild1" from the 3rd commit.
		child1 = root.getESubpackages().get(0);
		assertEquals("newChild1", child1.getName());
		assertSame(resource2, ((InternalEObject)child1).eDirectResource());
		EClass testC1 = (EClass)child1.getEClassifiers().get(0);
		assertEquals("C1", testC1.getName());
		// The second sub-package must be "newChild2" from the 3rd commit.
		child2 = root.getESubpackages().get(1);
		assertEquals("newChild2", child2.getName());
		assertSame(resource3, ((InternalEObject)child2).eDirectResource());
		EClass testC2 = (EClass)child2.getEClassifiers().get(0);
		assertEquals("C2", testC2.getName());

		testProject1.dispose();
		testProject2.dispose();

	}

	/**
	 * Merge of a new remote project that contains two new fragments of a model. The projects are at the root
	 * of the git repository. The controlled models (fragments) are at the root of their project. The first
	 * fragment reference the second fragment.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCollaborativeModeling_UseCase007_MergeMultipleDependentFragmentsFromRemoteProject()
			throws Exception {
		ResourceSetImpl resourceSet = new ResourceSetImpl();
		File workingDirectory = repository.getRepository().getWorkTree();
		TestProject testProject1 = new TestProject("Project1", workingDirectory.getAbsolutePath());
		IProject iProject = testProject1.getProject();
		repository.connect(iProject);
		File file1 = testProject1.getOrCreateFile(iProject, "file1.ecore");
		IFile iFile1 = testProject1.getIFile(iProject, file1);
		Resource resource1 = connectResource(iFile1, resourceSet);

		// 1st commit:
		// One project at the root of the git repository
		// One model with a root package containing another package.
		// The model is at the root of the project.
		EPackage root = createPackage(null, "parent");
		EPackage child1 = createPackage(root, "child1");
		createClass(child1, "C1");
		EPackage child2 = createPackage(child1, "child1");
		createClass(child2, "C2");
		resource1.getContents().add(root);
		save(resource1);
		repository.addAllAndCommit("1st-commit");
		repository.createBranch(MASTER, BRANCH_1);

		TestProject testProject2 = new TestProject("Project2", workingDirectory.getAbsolutePath());
		IProject iProject2 = testProject2.getProject();
		repository.connect(iProject2);
		File file2 = testProject2.getOrCreateFile(iProject2, "file2.ecore");
		IFile iFile2 = testProject2.getIFile(iProject2, file2);
		File file3 = testProject2.getOrCreateFile(iProject2, "file3.ecore");
		IFile iFile3 = testProject2.getIFile(iProject2, file3);
		File file22 = testProject2.getOrCreateFile(iProject2, "file22.txt");
		IFile iFile22 = testProject2.getIFile(iProject2, file22);
		Resource resource2 = connectResource(iFile2, resourceSet);
		Resource resource3 = connectResource(iFile3, resourceSet);

		// 2nd commit:
		// Control the sub-packages in a new project at the root of the git repository.
		// The controlled models are at the root of its project.
		// Also add a simple file in this new project.
		resource3.getContents().add(child2);
		resource2.getContents().add(child1);
		resource1.getContents().clear();
		resource1.getContents().add(root);
		save(resource3);
		save(resource2);
		save(resource1);
		repository.addAllAndCommit("2nd-commit");
		repository.createBranch(MASTER, BRANCH_2);

		// Back to 1st commit
		repository.reset(BRANCH_1, ResetType.HARD);

		// 3rd commit:
		// Change the name of the sub-packages.
		resourceSet.getResources().clear();
		resource1 = resourceSet.getResource(URI.createPlatformResourceURI(iFile1.getFullPath().toString(),
				true), true);
		root = (EPackage)resource1.getContents().get(0);
		child1 = root.getESubpackages().get(0);
		child1.setName("newChild1");
		child2 = child1.getESubpackages().get(0);
		child2.setName("newChild2");
		save(resource1);
		repository.addAllAndCommit("3rd-commit");
		repository.createBranch(MASTER, BRANCH_3);

		// Merge
		repository.mergeLogicalWithNewCommit(BRANCH_2);

		// No conflicts
		assertTrue(repository.status().getConflicting().isEmpty());

		// The project2 is not accessible.
		assertTrue(iProject.isAccessible());
		assertFalse(iProject2.isAccessible());
		// After the merge, the repository should contain all files from 2nd commit.
		assertTrue(iFile1.exists());
		assertFalse(iFile2.exists());
		assertFalse(iFile3.exists());
		assertFalse(iFile22.exists());
		assertTrue(file2.exists());
		assertTrue(file3.exists());
		assertTrue(file22.exists());
		// Check the contents
		resourceSet.getResources().clear();
		resource3 = resourceSet.getResource(URI.createFileURI(file3.getAbsolutePath()), true);
		resource2 = resourceSet.getResource(URI.createFileURI(file2.getAbsolutePath()), true);
		resource1 = resourceSet.getResource(URI.createPlatformResourceURI(iFile1.getFullPath().toString(),
				true), true);
		// The proxy of child element has the following form : "platform:/resource/..."
		// So we have to update the uri of the resource2
		resource2.setURI(URI.createPlatformResourceURI(iFile2.getFullPath().toString(), true));
		EcoreUtil.resolveAll(resourceSet);
		root = (EPackage)resource1.getContents().get(0);
		assertEquals("parent", root.getName());
		// The sub-package of root must be "newChild1" from the 3rd commit.
		child1 = root.getESubpackages().get(0);
		assertEquals("newChild1", child1.getName());
		assertSame(resource2, ((InternalEObject)child1).eDirectResource());
		EClass testC1 = (EClass)child1.getEClassifiers().get(0);
		assertEquals("C1", testC1.getName());
		// The sub-package of child1 must be "newChild2" from the 3rd commit.
		child2 = child1.getESubpackages().get(0);
		assertEquals("newChild2", child2.getName());
		assertSame(resource3, ((InternalEObject)child2).eDirectResource());
		EClass testC2 = (EClass)child2.getEClassifiers().get(0);
		assertEquals("C2", testC2.getName());

		testProject1.dispose();
		testProject2.dispose();

	}

	/**
	 * Merge a new fragment of a model in the same project. The project is at the root of the git repository.
	 * The controlled model is at the root of its project.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCollaborativeModeling_UseCase008_MergeFragmentFromSameProject() throws Exception {
		ResourceSetImpl resourceSet = new ResourceSetImpl();
		File workingDirectory = repository.getRepository().getWorkTree();
		TestProject testProject1 = new TestProject("Project1", workingDirectory.getAbsolutePath());
		IProject iProject = testProject1.getProject();
		repository.connect(iProject);
		File file1 = testProject1.getOrCreateFile(iProject, "file1.ecore");
		IFile iFile1 = testProject1.getIFile(iProject, file1);
		Resource resource1 = connectResource(iFile1, resourceSet);

		// 1st commit:
		// One project at the root of the git repository
		// One model with a root package containing another package.
		// The model is at the root of the project.
		EPackage root = createPackage(null, "parent");
		EPackage child = createPackage(root, "child");
		createClass(child, "C1");
		resource1.getContents().add(root);
		save(resource1);
		repository.addAllAndCommit("1st-commit");
		repository.createBranch(MASTER, BRANCH_1);

		File file2 = testProject1.getOrCreateFile(iProject, "file2.ecore");
		IFile iFile2 = testProject1.getIFile(iProject, file2);
		File file22 = testProject1.getOrCreateFile(iProject, "file22.txt");
		IFile iFile22 = testProject1.getIFile(iProject, file22);
		Resource resource2 = connectResource(iFile2, resourceSet);

		// 2nd commit:
		// Control the sub-package in a the same project.
		// The controlled model is at the root of its project.
		// Also add a simple file in this new project.
		resource2.getContents().add(child);
		resource1.getContents().clear();
		resource1.getContents().add(root);
		save(resource2);
		save(resource1);
		repository.addAllAndCommit("2nd-commit");
		repository.createBranch(MASTER, BRANCH_2);

		// Back to 1st commit
		repository.reset(BRANCH_1, ResetType.HARD);

		// 3rd commit:
		// Change the name of the sub-package.
		resourceSet.getResources().clear();
		resource1 = resourceSet.getResource(URI.createPlatformResourceURI(iFile1.getFullPath().toString(),
				true), true);
		root = (EPackage)resource1.getContents().get(0);
		child = root.getESubpackages().get(0);
		child.setName("newChild");
		save(resource1);
		repository.addAllAndCommit("3rd-commit");
		repository.createBranch(MASTER, BRANCH_3);

		// Merge
		repository.mergeLogicalWithNewCommit(BRANCH_2);

		// No conflicts
		assertTrue(repository.status().getConflicting().isEmpty());

		// The project is accessible.
		assertTrue(iProject.isAccessible());
		// After the merge, the repository should contain all files from 2nd commit.
		assertTrue(iFile1.exists());
		assertTrue(iFile2.exists());
		assertTrue(iFile22.exists());
		// Check the contents
		resourceSet.getResources().clear();
		resource1 = resourceSet.getResource(URI.createPlatformResourceURI(iFile1.getFullPath().toString(),
				true), true);
		resource2 = resourceSet.getResource(URI.createFileURI(file2.getAbsolutePath()), true);
		// The proxy of child element has the following form : "platform:/resource/..."
		// So we have to update the uri of the resource2
		resource2.setURI(URI.createPlatformResourceURI(iFile2.getFullPath().toString(), true));
		EcoreUtil.resolveAll(resourceSet);
		root = (EPackage)resource1.getContents().get(0);
		assertEquals("parent", root.getName());
		// The sub-package must be "newChild" from the 3rd commit.
		child = root.getESubpackages().get(0);
		assertEquals("newChild", child.getName());
		assertSame(resource2, ((InternalEObject)child).eDirectResource());
		EClass testC1 = (EClass)child.getEClassifiers().get(0);
		assertEquals("C1", testC1.getName());

		testProject1.dispose();

	}

	/**
	 * Merge of a new remote project that contains a new fragment of a model. The projects are at the root of
	 * the git repository. The controlled model is at the root of its project.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCollaborativeModeling_UseCase009_MergeFragmentFromLocalProject() throws Exception {
		ResourceSetImpl resourceSet = new ResourceSetImpl();
		File workingDirectory = repository.getRepository().getWorkTree();
		TestProject testProject1 = new TestProject("Project1", workingDirectory.getAbsolutePath());
		IProject iProject = testProject1.getProject();
		repository.connect(iProject);
		File file1 = testProject1.getOrCreateFile(iProject, "file1.ecore");
		IFile iFile1 = testProject1.getIFile(iProject, file1);
		Resource resource1 = connectResource(iFile1, resourceSet);

		// 1st commit:
		// One project at the root of the git repository
		// One model with a root package containing another package.
		// The model is at the root of the project.
		EPackage root = createPackage(null, "parent");
		EPackage child = createPackage(root, "child");
		createClass(child, "C1");
		resource1.getContents().add(root);
		save(resource1);
		repository.addAllAndCommit("1st-commit");
		repository.createBranch(MASTER, BRANCH_1);

		// 2nd commit:
		// Change the name of the sub-package.
		child.setName("newChild");
		save(resource1);
		repository.addAllAndCommit("2nd-commit");
		repository.createBranch(MASTER, BRANCH_2);

		// Back to 1st commit
		repository.reset(BRANCH_1, ResetType.HARD);

		resourceSet.getResources().clear();

		TestProject testProject2 = new TestProject("Project2", workingDirectory.getAbsolutePath());
		IProject iProject2 = testProject2.getProject();
		repository.connect(iProject2);
		File file2 = testProject2.getOrCreateFile(iProject2, "file2.ecore");
		IFile iFile2 = testProject2.getIFile(iProject2, file2);
		File file22 = testProject2.getOrCreateFile(iProject2, "file22.txt");
		IFile iFile22 = testProject2.getIFile(iProject2, file22);
		Resource resource2 = connectResource(iFile2, resourceSet);

		// 3rd commit:
		// Control the sub-package in a new project at the root of the git repository.
		// The controlled model is at the root of its project.
		// Also add a simple file in this new project.
		child.setName("child");
		resource2.getContents().add(child);
		save(resource2);
		save(resource1);
		repository.addAllAndCommit("3rd-commit");
		repository.createBranch(MASTER, BRANCH_3);

		// Merge
		repository.mergeLogicalWithNewCommit(BRANCH_2);

		// No conflicts
		assertTrue(repository.status().getConflicting().isEmpty());

		// The project2 is accessible.
		assertTrue(iProject.isAccessible());
		assertTrue(iProject2.isAccessible());
		// After the merge, the repository should contain all files from 2nd commit.
		assertTrue(iFile1.exists());
		assertTrue(iFile2.exists());
		assertTrue(iFile22.exists());
		// Check the contents
		resourceSet.getResources().clear();
		resource1 = resourceSet.getResource(URI.createPlatformResourceURI(iFile1.getFullPath().toString(),
				true), true);
		resource2 = resourceSet.getResource(URI.createPlatformResourceURI(iFile2.getFullPath().toString(),
				true), true);
		// The proxy of child element has the following form : "platform:/resource/..."
		// So we have to update the uri of the resource2
		// resource2.setURI(URI.createPlatformResourceURI(iFile2.getFullPath().toString(), true));
		EcoreUtil.resolveAll(resourceSet);
		root = (EPackage)resource1.getContents().get(0);
		assertEquals("parent", root.getName());
		// The sub-package must be "newChild" from the 3rd commit.
		child = root.getESubpackages().get(0);
		assertEquals("newChild", child.getName());
		assertSame(resource2, ((InternalEObject)child).eDirectResource());
		EClass testC1 = (EClass)child.getEClassifiers().get(0);
		assertEquals("C1", testC1.getName());

		testProject1.dispose();
		testProject2.dispose();

	}

}

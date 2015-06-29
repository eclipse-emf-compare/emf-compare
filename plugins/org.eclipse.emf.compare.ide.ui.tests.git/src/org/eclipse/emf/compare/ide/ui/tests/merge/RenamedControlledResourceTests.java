/*******************************************************************************
 * Copyright (C) 2015 Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.merge;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceLocationChange;
import org.eclipse.emf.compare.ide.ui.tests.egit.CompareGitTestCase;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.lib.Constants;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This test case specifies the expected behavior of merge when renamed controlled resources are involved.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@SuppressWarnings("nls")
public class RenamedControlledResourceTests extends CompareGitTestCase {
	private static final String MASTER = Constants.R_HEADS + Constants.MASTER;

	private static final String BRANCH = Constants.R_HEADS + "branch";

	private File file1;

	private File file2;

	private IFile iFile1;

	private IFile iFile2;

	private ResourceSet resourceSet;

	private IProject iProject;

	private Resource resource1;

	private Resource resource2;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		iProject = project.getProject();
		resourceSet = new ResourceSetImpl();

		file1 = project.getOrCreateFile(iProject, "file1.ecore");
		file2 = project.getOrCreateFile(iProject, "file2.ecore");
		iFile1 = project.getIFile(iProject, file1);
		iFile2 = project.getIFile(iProject, file2);

		resource1 = connectResource(iFile1, resourceSet);
		resource2 = connectResource(iFile2, resourceSet);
	}

	@Ignore("Due to Bug 464379, this test no longer works. It needs management of resource renaming via ResourceAttachmentChange.MOVE")
	@Test
	public void testMergeNoConflictRemoteRename() throws Exception {
		setUpRenameNoConflict();

		repository.mergeLogical(BRANCH);
		refreshTestProject();

		assertTrue(repository.status().getConflicting().isEmpty());

		assertTrue(iProject.getFile("file1.ecore").exists());
		assertFalse(iProject.getFile("file2.ecore").exists());
		assertTrue(iProject.getFile("file2_new.ecore").exists());
		// Check the contents
		resourceSet.getResources().clear();
		Resource testRoot = resourceSet.getResource(URI.createPlatformResourceURI("/Project-1/file1.ecore",
				true), true);
		Resource testChild = resourceSet.getResource(URI.createPlatformResourceURI(
				"/Project-1/file2_new.ecore", true), true);
		EPackage testRootPack = (EPackage)testRoot.getContents().get(0);
		assertEquals("parent", testRootPack.getName());
		EPackage testChildPack = testRootPack.getESubpackages().get(0);
		assertEquals("child", testChildPack.getName());
		assertSame(testChild, ((InternalEObject)testChildPack).eDirectResource());
		EClass nonConflictingClass = (EClass)testRootPack.getEClassifiers().get(0);
		assertEquals("NonConflicting", nonConflictingClass.getName());
		EClass testC1 = (EClass)testChildPack.getEClassifiers().get(0);
		assertEquals("C1", testC1.getName());
	}

	@Test
	public void testMergeNoConflictLocalRename() throws Exception {
		setUpRenameNoConflict();

		repository.checkoutBranch(BRANCH);

		repository.mergeLogical(MASTER);
		refreshTestProject();

		assertTrue(repository.status().getConflicting().isEmpty());

		assertTrue(iProject.getFile("file1.ecore").exists());
		assertFalse(iProject.getFile("file2.ecore").exists());
		assertTrue(iProject.getFile("file2_new.ecore").exists());
		// Check the contents
		resourceSet.getResources().clear();
		Resource testRoot = resourceSet.getResource(URI.createPlatformResourceURI("/Project-1/file1.ecore",
				true), true);
		Resource testChild = resourceSet.getResource(URI.createPlatformResourceURI(
				"/Project-1/file2_new.ecore", true), true);
		EPackage testRootPack = (EPackage)testRoot.getContents().get(0);
		assertEquals("parent", testRootPack.getName());
		EPackage testChildPack = testRootPack.getESubpackages().get(0);
		assertEquals("child", testChildPack.getName());
		assertSame(testChild, ((InternalEObject)testChildPack).eDirectResource());
		EClass nonConflictingClass = (EClass)testRootPack.getEClassifiers().get(0);
		assertEquals("NonConflicting", nonConflictingClass.getName());
		EClass testC1 = (EClass)testChildPack.getEClassifiers().get(0);
		assertEquals("C1", testC1.getName());
	}

	@Ignore("Due to Bug 464379, this test no longer works. It needs management of resource renaming via ResourceAttachmentChange.MOVE")
	@Test
	public void testMergeNoConflictRemoteRenameLocalChanges() throws Exception {
		setUpRenameNoConflictLocalChanges();

		repository.mergeLogical(BRANCH);
		refreshTestProject();

		assertTrue(repository.status().getConflicting().isEmpty());

		assertTrue(iProject.getFile("file1.ecore").exists());
		assertFalse(iProject.getFile("file2.ecore").exists());
		assertTrue(iProject.getFile("file2_new.ecore").exists());
		// Check the contents
		resourceSet.getResources().clear();
		Resource testRoot = resourceSet.getResource(URI.createPlatformResourceURI("/Project-1/file1.ecore",
				true), true);
		Resource testChild = resourceSet.getResource(URI.createPlatformResourceURI(
				"/Project-1/file2_new.ecore", true), true);
		EPackage testRootPack = (EPackage)testRoot.getContents().get(0);
		assertEquals("parent", testRootPack.getName());
		EPackage testChildPack = testRootPack.getESubpackages().get(0);
		assertEquals("child", testChildPack.getName());
		assertSame(testChild, ((InternalEObject)testChildPack).eDirectResource());
		EClass nonConflictingClass = (EClass)testRootPack.getEClassifiers().get(0);
		assertEquals("NonConflicting", nonConflictingClass.getName());
		EClass testC1 = (EClass)testChildPack.getEClassifiers().get(0);
		assertEquals("C1", testC1.getName());
		EClass testNewClass = (EClass)testChildPack.getEClassifiers().get(1);
		assertEquals("NewClassInRemotelyRenamedPackage", testNewClass.getName());
	}

	@Ignore("Due to Bug 464379, this test no longer works. It needs management of resource renaming via ResourceAttachmentChange.MOVE")
	@Test
	public void testComparisonNoConflictRemoteRename() throws Exception {
		setUpRenameNoConflict();

		Comparison comparison = compare(MASTER, BRANCH, iFile1);

		assertTrue(comparison.getConflicts().isEmpty());
		assertEquals(0, comparison.getDiagnostic().getCode());
		// 2 resource matches
		assertEquals(2, comparison.getMatchedResources().size());
		// 2 diffs:
		// 1- added class "NotConflicting" in MASTER
		// 2- renamed file2 to file2_new
		assertEquals(2, comparison.getDifferences().size());
		assertEquals(1, size(filter(comparison.getDifferences(), instanceOf(ReferenceChange.class))));
		assertEquals(1, size(filter(comparison.getDifferences(), instanceOf(ResourceLocationChange.class))));
	}

	@Ignore("Due to Bug 464379, this test no longer works. It needs management of resource renaming via ResourceAttachmentChange.MOVE")
	@Test
	public void testComparisonNoConflictLocalRename() throws Exception {
		setUpRenameNoConflict();

		Comparison comparison = compare(BRANCH, MASTER, iFile1);

		assertTrue(comparison.getConflicts().isEmpty());
		assertEquals(0, comparison.getDiagnostic().getCode());
		// 2 resource matches
		assertEquals(2, comparison.getMatchedResources().size());
		// 2 diffs:
		// 1- added class "NotConflicting" in MASTER
		// 2- renamed file2 to file2_new
		assertEquals(2, comparison.getDifferences().size());
		assertEquals(1, size(filter(comparison.getDifferences(), instanceOf(ReferenceChange.class))));
		assertEquals(1, size(filter(comparison.getDifferences(), instanceOf(ResourceLocationChange.class))));
	}

	@Test
	public void testMergeConflict() throws Exception {
		setUpRenameConflict();

		repository.mergeLogical(BRANCH);
		refreshTestProject();

		Set<String> conflicting = repository.status().getConflicting();
		assertEquals(3, conflicting.size());
		assertTrue(conflicting.contains("Project-1/file1.ecore"));
		assertTrue(conflicting.contains("Project-1/file2_new.ecore"));
		assertTrue(conflicting.contains("Project-1/file2_other.ecore"));

		assertTrue(iProject.getFile("file1.ecore").exists());
		assertFalse(iProject.getFile("file2.ecore").exists());
		assertFalse(iProject.getFile("file2_new.ecore").exists());
		assertTrue(iProject.getFile("file2_other.ecore").exists());
	}

	@Test
	public void testComparisonConflict() throws Exception {
		setUpRenameConflict();

		Comparison comparison = compare(MASTER, BRANCH, iFile1);

		assertEquals(1, comparison.getConflicts().size());
		assertEquals(0, comparison.getDiagnostic().getCode());
		// 2 resource matches
		assertEquals(2, comparison.getMatchedResources().size());
		// 2 diffs:
		// 2- renamed file2 to file2_new (remote) and to file_other (local)
		assertEquals(2, comparison.getDifferences().size());
		assertEquals(2, size(filter(comparison.getDifferences(), instanceOf(ResourceLocationChange.class))));
		// 2 diffs in conflict
		assertEquals(2, comparison.getConflicts().get(0).getDifferences().size());
		// Both a re resource location changes
		assertEquals(2, size(filter(comparison.getConflicts().get(0).getDifferences(),
				instanceOf(ResourceLocationChange.class))));
	}

	protected void setUpRenameNoConflict() throws Exception {
		EPackage root = createPackage(null, "parent");
		EPackage child = createPackage(root, "child");
		createClass(child, "C1");
		saveTestResource(resource2, child);
		saveTestResource(resource1, root);
		repository.addAllAndCommit("initial-commit");

		// Branch and checkout branch
		repository.createBranch(MASTER, BRANCH);
		repository.checkoutBranch(BRANCH);
		IPath iFile2_new = iFile2.getParent().getLocation().append("file2_new.ecore");
		iFile2.getLocation().toFile().renameTo(iFile2_new.toFile());
		refreshTestProject();
		resource2 = connectResource(iProject.getFile("file2_new.ecore"), resourceSet);
		saveTestResource(resource2, child);
		saveTestResource(resource1, root);
		// Don't forget to remove former file2.ecore from the index
		repository.removeFromIndex(iFile2);
		repository.addAllAndCommit("Moved file2 to file2_new.");

		// Go back to master and make non-conflicting changes
		repository.checkoutBranch(MASTER);
		repository.reset(MASTER, ResetType.HARD);
		refreshTestProject();
		resourceSet.getResources().clear();
		resource1 = connectResource(iProject.getFile("file1.ecore"), resourceSet);
		resource2 = connectResource(iProject.getFile("file2.ecore"), resourceSet);
		createClass(root, "NonConflicting");
		saveTestResource(resource2, child);
		saveTestResource(resource1, root);
		repository.addAllAndCommit("Added class to root.");
	}

	protected void setUpRenameNoConflictLocalChanges() throws Exception {
		setUpRenameNoConflict();

		EPackage child = (EPackage)resource2.getContents().get(0);
		createClass(child, "NewClassInRemotelyRenamedPackage");
		saveTestResource(resource2, child);
		repository.addAllAndAmend("Added class to root and child.");
	}

	protected void setUpRenameConflict() throws Exception {
		EPackage root = createPackage(null, "parent");
		EPackage child = createPackage(root, "child");
		createClass(child, "C1");
		saveTestResource(resource2, child);
		saveTestResource(resource1, root);
		repository.addAllAndCommit("initial-commit");

		// Branch and checkout branch
		repository.createBranch(MASTER, BRANCH);
		repository.checkoutBranch(BRANCH);
		IPath iFile2_new = iFile2.getParent().getLocation().append("file2_new.ecore");
		iFile2.getLocation().toFile().renameTo(iFile2_new.toFile());
		refreshTestProject();
		resource2 = connectResource(iProject.getFile("file2_new.ecore"), resourceSet);
		saveTestResource(resource2, child);
		saveTestResource(resource1, root);
		// Don't forget to remove former file2.ecore from the index
		repository.removeFromIndex(iFile2);
		repository.addAllAndCommit("Moved file2 to file2_new.");

		// Go back to master and make conflicting rename of sub-unit
		repository.checkoutBranch(MASTER);
		repository.reset(MASTER, ResetType.HARD);
		IPath iFile2_other = iFile2.getParent().getLocation().append("file2_other.ecore");
		iFile2.getLocation().toFile().renameTo(iFile2_other.toFile());
		refreshTestProject();
		resource2 = connectResource(iProject.getFile("file2_other.ecore"), resourceSet);
		saveTestResource(resource2, child);
		saveTestResource(resource1, root);
		// Don't forget to remove former file2.ecore from the index
		repository.removeFromIndex(iFile2);
		repository.addAllAndCommit("Moved file2 to file2_other.");
	}

	protected void refreshTestProject() throws CoreException {
		iProject.refreshLocal(IResource.DEPTH_INFINITE, null);
	}

	protected void saveTestResource(final Resource resource, final EPackage pkg) throws IOException,
			CoreException {
		resource.getContents().clear();
		resource.getContents().add(pkg);
		save(resource);
	}
}

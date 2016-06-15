/*******************************************************************************
 * Copyright (C) 2015 Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.merge;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitTestRunner;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitCompare;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitInput;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitMerge;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Repository;
import org.junit.runner.RunWith;

/**
 * This test case specifies the expected behavior of merge when renamed controlled resources are involved.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@SuppressWarnings({"nls", "unused" })
@RunWith(GitTestRunner.class)
public class RenamedControlledResourceTests {

	@GitMerge(localBranch = "master", remoteBranch = "branch")
	@GitInput("data/renamedControlledResources/renameNoConflicts.zip")
	public void testMergeNoConflictRemoteRename(Status status, Repository repository, List<IProject> projects)
			throws Exception {
		IProject iProject = projects.get(0);

		assertTrue(status.getConflicting().isEmpty());

		assertTrue(iProject.getFile("file1.ecore").exists());
		assertFalse(iProject.getFile("file2.ecore").exists());
		assertTrue(iProject.getFile("file2_new.ecore").exists());
		// Check the contents
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource testRoot = resourceSet
				.getResource(URI.createPlatformResourceURI("/renameNoConflicts/file1.ecore", true), true);
		Resource testChild = resourceSet
				.getResource(URI.createPlatformResourceURI("/renameNoConflicts/file2_new.ecore", true), true);
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

	@GitMerge(localBranch = "branch", remoteBranch = "master")
	@GitInput("data/renamedControlledResources/renameNoConflicts.zip")
	public void testMergeNoConflictLocalRename(Status status, Repository repository, List<IProject> projects)
			throws Exception {
		IProject iProject = projects.get(0);

		assertTrue(status.getConflicting().isEmpty());

		assertTrue(iProject.getFile("file1.ecore").exists());
		assertFalse(iProject.getFile("file2.ecore").exists());
		assertTrue(iProject.getFile("file2_new.ecore").exists());
		// Check the contents
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource testRoot = resourceSet
				.getResource(URI.createPlatformResourceURI("/renameNoConflicts/file1.ecore", true), true);
		Resource testChild = resourceSet
				.getResource(URI.createPlatformResourceURI("/renameNoConflicts/file2_new.ecore", true), true);
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

	@GitMerge(localBranch = "master", remoteBranch = "branch")
	@GitInput("data/renamedControlledResources/renameNoConflictsLocalChanges.zip")
	public void testMergeNoConflictRemoteRenameLocalChanges(Status status, Repository repository,
			List<IProject> projects) throws Exception {
		IProject iProject = projects.get(0);
		assertTrue(status.getConflicting().isEmpty());

		assertTrue(iProject.getFile("file1.ecore").exists());
		assertFalse(iProject.getFile("file2.ecore").exists());
		assertTrue(iProject.getFile("file2_new.ecore").exists());
		// Check the contents
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource testRoot = resourceSet.getResource(
				URI.createPlatformResourceURI("/renameNoConflictsLocalChanges/file1.ecore", true), true);
		Resource testChild = resourceSet.getResource(
				URI.createPlatformResourceURI("/renameNoConflictsLocalChanges/file2_new.ecore", true), true);
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

	@GitCompare(localBranch = "master", remoteBranch = "branch", fileToCompare = "file1.ecore")
	@GitInput("data/renamedControlledResources/renameNoConflicts.zip")
	public void testComparisonNoConflictRemoteRename(Comparison comparison) throws Exception {
		assertTrue(comparison.getConflicts().isEmpty());
		assertEquals(0, comparison.getDiagnostic().getCode());
		// 3 resource matches
		assertEquals(3, comparison.getMatchedResources().size());
		// 2 diffs:
		// 1- added class "NotConflicting" in MASTER
		// 2- renamed file2 to file2_new
		assertEquals(2, comparison.getDifferences().size());
		assertEquals(1, size(filter(comparison.getDifferences(), instanceOf(ReferenceChange.class))));
		assertEquals(1, size(filter(comparison.getDifferences(),
				and(instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.MOVE)))));
	}

	@GitCompare(localBranch = "branch", remoteBranch = "master", fileToCompare = "file1.ecore")
	@GitInput("data/renamedControlledResources/renameNoConflicts.zip")
	public void testComparisonNoConflictLocalRename(Comparison comparison) throws Exception {
		assertTrue(comparison.getConflicts().isEmpty());
		assertEquals(0, comparison.getDiagnostic().getCode());
		// 3 resource matches
		assertEquals(3, comparison.getMatchedResources().size());
		// 2 diffs:
		// 1- added class "NotConflicting" in MASTER
		// 2- renamed file2 to file2_new
		assertEquals(2, comparison.getDifferences().size());
		assertEquals(1, size(filter(comparison.getDifferences(), instanceOf(ReferenceChange.class))));
		assertEquals(1, size(filter(comparison.getDifferences(),
				and(instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.MOVE)))));
	}

	@GitMerge(localBranch = "master", remoteBranch = "branch")
	@GitInput("data/renamedControlledResources/renameConflicts.zip")
	public void testMergeConflict(Status status, Repository repository, List<IProject> projects)
			throws Exception {
		IProject iProject = projects.get(0);
		Set<String> conflicting = status.getConflicting();
		assertEquals(3, conflicting.size());
		assertTrue(conflicting.contains("renameConflicts/file1.ecore"));
		assertTrue(conflicting.contains("renameConflicts/file2_new.ecore"));
		assertTrue(conflicting.contains("renameConflicts/file2_other.ecore"));

		assertTrue(iProject.getFile("file1.ecore").exists());
		assertFalse(iProject.getFile("file2.ecore").exists());
		assertFalse(iProject.getFile("file2_new.ecore").exists());
		assertTrue(iProject.getFile("file2_other.ecore").exists());
	}

	@GitCompare(localBranch = "master", remoteBranch = "branch", fileToCompare = "file1.ecore")
	@GitInput("data/renamedControlledResources/renameConflicts.zip")
	public void testComparisonConflict(Comparison comparison) throws Exception {
		assertEquals(1, comparison.getConflicts().size());
		assertEquals(0, comparison.getDiagnostic().getCode());
		// 4 resource matches
		assertEquals(4, comparison.getMatchedResources().size());
		// 2 diffs:
		// 2- renamed file2 to file2_new (remote) and to file_other (local)
		assertEquals(2, comparison.getDifferences().size());
		assertEquals(2, size(filter(comparison.getDifferences(),
				and(instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.MOVE)))));
		// 2 diffs in conflict
		assertEquals(2, comparison.getConflicts().get(0).getDifferences().size());
		// Both a re resource location changes
		assertEquals(2, size(filter(comparison.getConflicts().get(0).getDifferences(),
				and(instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.MOVE)))));
	}

}

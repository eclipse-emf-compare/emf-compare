/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - extract super class AbstractGitLogicalModelTest
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jgit.api.Status;
import org.junit.Test;

@SuppressWarnings("nls")
public class GitLogicalMergeTest extends AbstractGitLogicalModelTest {

	/**
	 * Sets up the repository so that there are three commits affecting a single file.
	 * <ul>
	 * <li>initial commit : A single file "file1" contains a package P1 which contains the classes C1 and C2.
	 * </li>
	 * <li>master commit : delete C2</li>
	 * <li>branch commit : make C2 a super-class of C1</li>
	 * </ul>
	 * <p>
	 * This is both a textual and a logical conflict. We expect both kind of merges to mark the file as
	 * conflicting, but textual merging should corrupt the model with its conflict markers whereas logical
	 * merge will leave it untouched.
	 * </p>
	 */
	private void setUp001() throws Exception {
		// We don't need the second resource here
		file2.delete();
		resource2 = null;

		final EPackage root = createPackage(null, "P1");
		createClass(root, "C1");
		EClass class2 = createClass(root, "C2");
		resource1.getContents().add(root);
		save(resource1);

		repository.addAllAndCommit("initial-commit");

		// Branch, but stay on master
		repository.createBranch(MASTER, BRANCH);

		root.getEClassifiers().remove(class2);
		save(resource1);
		repository.addAndCommit(project, "master-commit", file1);

		repository.checkoutBranch(BRANCH);
		reload(resource1);

		final EClass class1 = (EClass)findObject(resource1, "C1");
		class2 = (EClass)findObject(resource1, "C2");

		class1.getESuperTypes().add(class2);
		save(resource1);
		repository.addAndCommit(project, "branch-commit", file1);

		repository.checkoutBranch(MASTER);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());

		// We expect one conflict, with one single diff on each side
		compareBothDirectionsAndCheck(iFile1, MASTER, BRANCH, 1, 1, 1);
	}

	/**
	 * Sets up the repository so that there are three commits affecting a single file.
	 * <ul>
	 * <li>initial commit : A single file "file1" contains a package P1 which contains the classes C1 and C2.
	 * </li>
	 * <li>master commit : rename C1 into C10</li>
	 * <li>branch commit : make C2 a super-class of C1</li>
	 * </ul>
	 * <p>
	 * This is a textual conflict, but there are no logical conflict. We expect the textual merge to mark the
	 * file as conflicting, corrupting the model with its markers, but the logical merge should pass without
	 * issues.
	 * </p>
	 * 
	 * @throws Exception
	 */
	private void setUp002() throws Exception {
		// We don't need the second resource here
		file2.delete();
		resource2 = null;

		final EPackage root = createPackage(null, "P1");
		EClass class1 = createClass(root, "C1");
		createClass(root, "C2");
		resource1.getContents().add(root);
		save(resource1);

		repository.addAllAndCommit("initial-commit");

		// Branch, but stay on master
		repository.createBranch(MASTER, BRANCH);

		class1.setName("C10");
		save(resource1);
		repository.addAndCommit(project, "master-commit", file1);

		repository.checkoutBranch(BRANCH);
		reload(resource1);

		class1 = (EClass)findObject(resource1, "C1");
		final EClass class2 = (EClass)findObject(resource1, "C2");

		class1.getESuperTypes().add(class2);
		save(resource1);
		repository.addAndCommit(project, "branch-commit", file1);

		repository.checkoutBranch(MASTER);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());

		// One diff on each side, no conflicts
		compareBothDirectionsAndCheck(iFile1, MASTER, BRANCH, 0, 1, 1);
	}

	/**
	 * Sets up the repository so that there are three commits affecting a single file.
	 * <ul>
	 * <li>initial commit : A single file "file1" contains a package P1 which contains the classes C1 and C2.
	 * </li>
	 * <li>master commit : delete C2</li>
	 * <li>branch commit : delete C2</li>
	 * </ul>
	 * <p>
	 * This is a pseudo conflict at both logical and textual level, which can thus be automatically merged in
	 * both cases.
	 * </p>
	 * 
	 * @throws Exception
	 */
	private void setUp003() throws Exception {
		// We don't need the second resource here
		file2.delete();
		resource2 = null;

		final EPackage root = createPackage(null, "P1");
		createClass(root, "C1");
		EClass class2 = createClass(root, "C2");
		resource1.getContents().add(root);
		save(resource1);

		repository.addAllAndCommit("initial-commit");

		// Branch, but stay on master
		repository.createBranch(MASTER, BRANCH);

		root.getEClassifiers().remove(class2);
		save(resource1);
		repository.addAndCommit(project, "master-commit", file1);

		repository.checkoutBranch(BRANCH);
		reload(resource1);

		class2 = (EClass)findObject(resource1, "C2");
		class2.getEPackage().getEClassifiers().remove(class2);
		save(resource1);
		repository.addAndCommit(project, "branch-commit", file1);

		repository.checkoutBranch(MASTER);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());

		// One diff on each side, one pseudo conflict
		compareBothDirectionsAndCheck(iFile1, MASTER, BRANCH, 1, 1, 1);
	}

	/**
	 * Sets up the repository so that there are three commits affecting two files with no links between them
	 * (not part of a single logical model).
	 * <ul>
	 * <li>initial commit : "file1" contains package P1 and classes C1 and C2. "file2" contains P2 containing
	 * C3 and C4.</li>
	 * <li>master commit : delete C2</li>
	 * <li>branch commit : make C2 a super-class of C1, delete C4</li>
	 * </ul>
	 * <p>
	 * This constitutes a conflict in file1, whereas the merge of file2 should end successfully.
	 * </p>
	 * 
	 * @throws Exception
	 */
	private void setUp004() throws Exception {
		final EPackage root1 = createPackage(null, "P1");
		createClass(root1, "C1");
		EClass class2 = createClass(root1, "C2");
		resource1.getContents().add(root1);

		final EPackage root2 = createPackage(null, "P2");
		createClass(root2, "C3");
		createClass(root2, "C4");
		resource2.getContents().add(root2);

		save(resource1, resource2);
		repository.addAllAndCommit("initial-commit");

		// Branch, but stay on master
		repository.createBranch(MASTER, BRANCH);

		root1.getEClassifiers().remove(class2);
		save(resource1);
		repository.addAndCommit(project, "master-commit", file1);

		repository.checkoutBranch(BRANCH);
		reload(resource1, resource2);

		final EClass class1 = (EClass)findObject(resource1, "C1");
		class2 = (EClass)findObject(resource1, "C2");
		final EClass class4 = (EClass)findObject(resource2, "C4");
		class1.getESuperTypes().add(class2);
		class4.getEPackage().getEClassifiers().remove(class4);

		save(resource1, resource2);
		repository.addAndCommit(project, "branch-commit", file1, file2);

		repository.checkoutBranch(MASTER);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());

		// One diff on each side, one conflict
		compareBothDirectionsAndCheck(iFile1, MASTER, BRANCH, 1, 1, 1);
		// one diff from the right
		compareBothDirectionsAndCheck(iFile2, MASTER, BRANCH, 0, 0, 1);
	}

	/**
	 * Sets up the repository so that there are three commits affecting two files with no links between them
	 * (not part of a single logical model).
	 * <ul>
	 * <li>initial commit : "file1" contains package P1 and classes C1 and C2. "file2" contains P2 containing
	 * C3 and C4.</li>
	 * <li>master commit : delete C2, delete C4</li>
	 * <li>branch commit : make C2 a super-class of C1, make C4 a super-class of C3</li>
	 * </ul>
	 * <p>
	 * This constitutes a conflict in both files for both kind of merge (textual or logical).
	 * </p>
	 * 
	 * @throws Exception
	 */
	private void setUp005() throws Exception {
		final EPackage root1 = createPackage(null, "P1");
		createClass(root1, "C1");
		EClass class2 = createClass(root1, "C2");
		resource1.getContents().add(root1);

		final EPackage root2 = createPackage(null, "P2");
		createClass(root2, "C3");
		EClass class4 = createClass(root2, "C4");
		resource2.getContents().add(root2);

		save(resource1, resource2);
		repository.addAllAndCommit("initial-commit");

		// Branch, but stay on master
		repository.createBranch(MASTER, BRANCH);

		root1.getEClassifiers().remove(class2);
		root2.getEClassifiers().remove(class4);
		save(resource1, resource2);
		repository.addAndCommit(project, "master-commit", file1, file2);

		repository.checkoutBranch(BRANCH);
		reload(resource1, resource2);

		final EClass class1 = (EClass)findObject(resource1, "C1");
		class2 = (EClass)findObject(resource1, "C2");
		final EClass class3 = (EClass)findObject(resource2, "C3");
		class4 = (EClass)findObject(resource2, "C4");
		class1.getESuperTypes().add(class2);
		class3.getESuperTypes().add(class4);

		save(resource1, resource2);
		repository.addAndCommit(project, "branch-commit", file1, file2);

		repository.checkoutBranch(MASTER);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());

		// One diff on each side, one conflict
		compareBothDirectionsAndCheck(iFile1, MASTER, BRANCH, 1, 1, 1);
		// likewise
		compareBothDirectionsAndCheck(iFile2, MASTER, BRANCH, 1, 1, 1);
	}

	/**
	 * Sets up the repository so that there are three commits affecting two files with no links between them
	 * (not part of a single logical model).
	 * <ul>
	 * <li>initial commit : "file1" contains package P1 and classes C1 and C2. "file2" contains P2 containing
	 * C3 and C4.</li>
	 * <li>master commit : renamed C1 into C10, rename C3 into C30</li>
	 * <li>branch commit : make C2 a super-class of C1, make C4 a super-class of C3</li>
	 * </ul>
	 * <p>
	 * This is a textual conflict in both files, but there are no logical conflicts. Textual merge will
	 * corrupt both models on top of being unable to finish the merge, logical merge will end successfully.
	 * </p>
	 * 
	 * @throws Exception
	 */
	private void setUp006() throws Exception {
		final EPackage root1 = createPackage(null, "P1");
		EClass class1 = createClass(root1, "C1");
		createClass(root1, "C2");
		resource1.getContents().add(root1);

		final EPackage root2 = createPackage(null, "P2");
		EClass class3 = createClass(root2, "C3");
		createClass(root2, "C4");
		resource2.getContents().add(root2);

		save(resource1, resource2);
		repository.addAllAndCommit("initial-commit");

		// Branch, but stay on master
		repository.createBranch(MASTER, BRANCH);

		class1.setName("C10");
		class3.setName("C30");
		save(resource1, resource2);
		repository.addAndCommit(project, "master-commit", file1, file2);

		repository.checkoutBranch(BRANCH);
		reload(resource1, resource2);

		class1 = (EClass)findObject(resource1, "C1");
		final EClass class2 = (EClass)findObject(resource1, "C2");
		class3 = (EClass)findObject(resource2, "C3");
		final EClass class4 = (EClass)findObject(resource2, "C4");
		class1.getESuperTypes().add(class2);
		class3.getESuperTypes().add(class4);

		save(resource1, resource2);
		repository.addAndCommit(project, "branch-commit", file1, file2);

		repository.checkoutBranch(MASTER);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());

		// One diff on each side, no conflict
		compareBothDirectionsAndCheck(iFile1, MASTER, BRANCH, 0, 1, 1);
		// likewise
		compareBothDirectionsAndCheck(iFile2, MASTER, BRANCH, 0, 1, 1);
	}

	/**
	 * Sets up the repository so that there are three commits affecting two interlinked files (part of the
	 * same logical model). The files won't be part of the same logical model initially, the link will be
	 * created in one of the commits we'll merge.
	 * <ul>
	 * <li>initial commit : "file1" contains package P1 and classes C1 and C2. "file2" contains P2 containing
	 * C3 and C4.</li>
	 * <li>master commit : delete C3</li>
	 * <li>branch commit : make C3 a super-class of C1</li>
	 * </ul>
	 * <p>
	 * There is no textual conflict in these changes, so a textual merge will end successfully, corrupting the
	 * model by breaking the links between them. However, logical merging will see the conflict and should
	 * mark both files as conflicting, without touching either of them.
	 * </p>
	 * <p>
	 * This case also presents the particularity of having a "single file" locally (file1 is not linked to
	 * file2) whereas the model is comprised of two files in the right. This will allow us to make sure that
	 * we are able to resolve the logical model in such cases.
	 * </p>
	 * 
	 * @throws Exception
	 */
	private void setUp007() throws Exception {
		final EPackage root1 = createPackage(null, "P1");
		createClass(root1, "C1");
		createClass(root1, "C2");
		resource1.getContents().add(root1);

		final EPackage root2 = createPackage(null, "P2");
		EClass class3 = createClass(root2, "C3");
		createClass(root2, "C4");
		resource2.getContents().add(root2);

		save(resource1, resource2);
		repository.addAllAndCommit("initial-commit");

		// Branch, but stay on master
		repository.createBranch(MASTER, BRANCH);

		root2.getEClassifiers().remove(class3);
		save(resource2);
		repository.addAndCommit(project, "master-commit", file2);

		repository.checkoutBranch(BRANCH);
		reload(resource1, resource2);

		final EClass class1 = (EClass)findObject(resource1, "C1");
		class3 = (EClass)findObject(resource2, "C3");
		class1.getESuperTypes().add(class3);

		save(resource1);
		repository.addAndCommit(project, "branch-commit", file1);

		repository.checkoutBranch(MASTER);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());

		/*
		 * The files are unrelated locally, but they are part of the same logical model on the remote. We
		 * expect EMF Compare to detect that when starting from file1 (as the remote file1 references the
		 * remote file2), but not when starting from file2 (since in such a case, we have no way to discover
		 * the cross reference).
		 */
		compareBothDirectionsAndCheck(iFile1, MASTER, BRANCH, 1, 1, 1);
		compareADirectionAndCheck(iFile2, MASTER, BRANCH, 0, 1, 0);
		compareADirectionAndCheck(iFile2, BRANCH, MASTER, 1, 1, 1);
		repository.checkoutBranch(MASTER);
	}

	/**
	 * See {@link #setUp007()} for the description of this use case, except that we reverse the changes made
	 * to file1 and file2 so that the "leaf" of the logical model is found before its root in the tree, making
	 * sure that this case will not fail to merge properly.
	 * 
	 * @see #setUp007()
	 * @throws Exception
	 */
	private void setUp007_reverse() throws Exception {
		final EPackage root1 = createPackage(null, "P1");
		EClass class1 = createClass(root1, "C1");
		createClass(root1, "C2");
		resource1.getContents().add(root1);

		final EPackage root2 = createPackage(null, "P2");
		createClass(root2, "C3");
		createClass(root2, "C4");
		resource2.getContents().add(root2);

		save(resource1, resource2);
		repository.addAllAndCommit("initial-commit");

		// Branch, but stay on master
		repository.createBranch(MASTER, BRANCH);

		root1.getEClassifiers().remove(class1);
		save(resource1);
		repository.addAndCommit(project, "master-commit", file1);

		repository.checkoutBranch(BRANCH);
		reload(resource1, resource2);

		final EClass class3 = (EClass)findObject(resource2, "C3");
		class1 = (EClass)findObject(resource1, "C1");
		class3.getESuperTypes().add(class1);

		save(resource2);
		repository.addAndCommit(project, "branch-commit", file2);

		repository.checkoutBranch(MASTER);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());

		/*
		 * The files are unrelated locally, but they are part of the same logical model on the remote. We
		 * expect EMF Compare to detect that when starting from file2 (as the remote file2 references the
		 * remote file1), but not when starting from file1 (since in such a case, we have no way to discover
		 * the cross reference).
		 */
		compareADirectionAndCheck(iFile1, MASTER, BRANCH, 0, 1, 0);
		compareADirectionAndCheck(iFile1, BRANCH, MASTER, 1, 1, 1);
		compareBothDirectionsAndCheck(iFile2, MASTER, BRANCH, 1, 1, 1);
	}

	/**
	 * Sets up the repository so that there are three commits affecting two interlinked files (part of the
	 * same logical model). The files will be linked together as a single logical model in all three sides of
	 * the comparison.
	 * <ul>
	 * <li>initial commit : "file1" contains package P1 and classes C1 and C2. "file2" contains P2 containing
	 * C3 and C4. C3 is a super-type of C1, linking the two files together.</li>
	 * <li>master commit : renamed C1 into C10</li>
	 * <li>branch commit : replace C3 by C4 as a super-class of C1, rename C4 into C40</li>
	 * </ul>
	 * <p>
	 * There is a textual conflict in the first file, none in the second. We expect the textual merge to
	 * corrupt the model with its textual conflict markers. There are no logical conflicts, so logical merging
	 * should end successfully.
	 * </p>
	 * 
	 * @throws Exception
	 */
	private void setUp008() throws Exception {
		final EPackage root1 = createPackage(null, "P1");
		EClass class1 = createClass(root1, "C1");
		createClass(root1, "C2");
		resource1.getContents().add(root1);

		final EPackage root2 = createPackage(null, "P2");
		EClass class3 = createClass(root2, "C3");
		createClass(root2, "C4");
		resource2.getContents().add(root2);

		class1.getESuperTypes().add(class3);

		save(resource1, resource2);
		repository.addAllAndCommit("initial-commit");

		// Branch, but stay on master
		repository.createBranch(MASTER, BRANCH);

		class1.setName("C10");
		save(resource1);
		repository.addAndCommit(project, "master-commit", file1);

		repository.checkoutBranch(BRANCH);
		reload(resource1, resource2);

		class1 = (EClass)findObject(resource1, "C1");
		final EClass class4 = (EClass)findObject(resource2, "C4");
		class1.getESuperTypes().clear();
		class1.getESuperTypes().add(class4);
		class4.setName("C40");

		save(resource1, resource2);
		repository.addAndCommit(project, "branch-commit", file1, file2);

		repository.checkoutBranch(MASTER);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());

		compareBothDirectionsAndCheck(iFile1, MASTER, BRANCH, 0, 1, 3);
		compareBothDirectionsAndCheck(iFile2, MASTER, BRANCH, 0, 1, 3);
	}

	/**
	 * Sets up the repository so that there are three commits affecting two interlinked files (part of the
	 * same logical model).
	 * <ul>
	 * <li>initial commit : "file1" contains package P1 and classes C1 and C2. "file2" contains P2 containing
	 * C3 and C4.</li>
	 * <li>master commit : renamed C1 into C10</li>
	 * <li>branch commit : renamed C4 into C40</li>
	 * </ul>
	 * <p>
	 * There is no conflict with these changes, neither textual nor logical.
	 * </p>
	 * 
	 * @throws Exception
	 */
	private void setUp009() throws Exception {
		final EPackage root1 = createPackage(null, "P1");
		final EClass class1 = createClass(root1, "C1");
		createClass(root1, "C2");
		resource1.getContents().add(root1);

		final EPackage root2 = createPackage(null, "P2");
		final EClass class3 = createClass(root2, "C3");
		createClass(root2, "C4");
		resource2.getContents().add(root2);

		class1.getESuperTypes().add(class3);

		save(resource1, resource2);
		repository.addAllAndCommit("initial-commit");

		// Branch, but stay on master
		repository.createBranch(MASTER, BRANCH);

		class1.setName("C10");
		save(resource1);
		repository.addAndCommit(project, "master-commit", file1);

		repository.checkoutBranch(BRANCH);
		reload(resource1, resource2);

		final EClass class4 = (EClass)findObject(resource2, "C4");
		class4.setName("C40");

		save(resource2);
		repository.addAndCommit(project, "branch-commit", file2);

		repository.checkoutBranch(MASTER);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());

		compareBothDirectionsAndCheck(iFile1, MASTER, BRANCH, 0, 1, 1);
		compareBothDirectionsAndCheck(iFile2, MASTER, BRANCH, 0, 1, 1);

	}

	/*
	 * Try and merge textually. This has no relation with EMF Compare, but it does check that : A - the models
	 * were properly created by setUp001, and B - the textual merge ends up corrupting the models.
	 */
	@Test
	public void merge001_text() throws Exception {
		setUp001();

		repository.mergeTextual(BRANCH);

		final Status status = repository.status();
		assertTrue(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().contains(repository.getRepoRelativePath(file1)));

		try {
			reload(resource1);
			fail();
		} catch (IOException e) {
			// expected behavior : the file has been corrupted by the textual merge
		}
	}

	/*
	 * Try and merge logically. There is a conflict, but contrarily to merge001_textual, we expect the model
	 * not to be corrupted by this attempt.
	 */
	@Test
	public void merge001_logical() throws Exception {
		setUp001();

		repository.mergeLogical(BRANCH);

		final Status status = repository.status();
		assertTrue(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().contains(repository.getRepoRelativePath(file1)));

		// Make sure we're still in the configuration we had on master
		reload(resource1);
		assertTrue(resource1.getErrors().isEmpty());
		assertNotNull(findObject(resource1, "C1"));
		assertNull(findObject(resource1, "C2"));
	}

	/*
	 * Try and merge textually. This has no relation with EMF Compare, but it does check that : A - the models
	 * were properly created by setUp002, and B - the textual merge ends up corrupting the model with a false
	 * conflict.
	 */
	@Test
	public void merge002_text() throws Exception {
		setUp002();

		repository.mergeTextual(BRANCH);

		final Status status = repository.status();
		assertTrue(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().contains(repository.getRepoRelativePath(file1)));

		try {
			reload(resource1);
			fail();
		} catch (IOException e) {
			// expected behavior : the file has been corrupted by the textual merge
		}
	}

	/*
	 * Try and merge logically. The merge should end without issues, without being affected by the textual
	 * conflict.
	 */
	@Test
	public void merge002_logical() throws Exception {
		setUp002();

		repository.mergeLogical(BRANCH);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().isEmpty());

		// Make sure that the merge actually changed the model
		reload(resource1);
		assertTrue(resource1.getErrors().isEmpty());
		final EPackage pack = (EPackage)findObject(resource1, "P1");
		final EClass class1 = (EClass)findObject(resource1, "C10");
		final EClass class2 = (EClass)findObject(resource1, "C2");
		assertNotNull(pack);
		assertNotNull(class1);
		assertNotNull(class2);
		assertTrue(class1.getESuperTypes().contains(class2));
		assertEquals(2, pack.getEClassifiers().size());
	}

	/*
	 * Try and merge textually. This has no relation with EMF Compare, but it does check that : A - the models
	 * were properly created by setUp003, and B - the textual merge manages to auto-merge the pseudo-conflict
	 * without corrupting the model.
	 */
	@Test
	public void merge003_text() throws Exception {
		setUp003();

		repository.mergeTextual(BRANCH);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().isEmpty());

		reload(resource1);
		assertTrue(resource1.getErrors().isEmpty());
		final EPackage pack = (EPackage)findObject(resource1, "P1");
		final EClass class1 = (EClass)findObject(resource1, "C1");

		assertNotNull(pack);
		assertNotNull(class1);
		assertEquals(1, pack.getEClassifiers().size());
	}

	/*
	 * Try and merge logically. The merge should end successfully, auto-merging the pseudo-conflict as needed.
	 */
	@Test
	public void merge003_logical() throws Exception {
		setUp003();

		repository.mergeLogical(BRANCH);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().isEmpty());

		reload(resource1);
		assertTrue(resource1.getErrors().isEmpty());
		final EPackage pack = (EPackage)findObject(resource1, "P1");
		final EClass class1 = (EClass)findObject(resource1, "C1");

		assertNotNull(pack);
		assertNotNull(class1);
		assertEquals(1, pack.getEClassifiers().size());
	}

	/*
	 * Try and merge textually. This has no relation with EMF Compare, but it does check that : A - the models
	 * were properly created by setUp004, and B - the textual merge corrupts the model in which there was a
	 * conflict.
	 */
	@Test
	public void merge004_text() throws Exception {
		setUp004();

		repository.mergeTextual(BRANCH);

		final Status status = repository.status();
		assertTrue(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().contains(repository.getRepoRelativePath(file1)));
		assertFalse(status.getConflicting().contains(repository.getRepoRelativePath(file2)));

		unload(resource1, resource2);

		try {
			reload(resource1);
			fail();
		} catch (IOException e) {
			// expected behavior : the file has been corrupted by the textual merge
		}

		reload(resource2);
		assertTrue(resource2.getErrors().isEmpty());
		final EPackage pack = (EPackage)findObject(resource2, "P2");
		final EClass class3 = (EClass)findObject(resource2, "C3");

		assertNotNull(pack);
		assertNotNull(class3);
		assertEquals(1, pack.getEClassifiers().size());
	}

	/*
	 * Try and merge logically. The merge should leave file1 as conflicting, leaving it untouched as compared
	 * to master. file2 should be properly merged.
	 */
	@Test
	public void merge004_logical() throws Exception {
		setUp004();

		repository.mergeLogical(BRANCH);

		final Status status = repository.status();
		assertTrue(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().contains(repository.getRepoRelativePath(file1)));
		assertFalse(status.getConflicting().contains(repository.getRepoRelativePath(file2)));

		unload(resource1, resource2);

		reload(resource1);
		assertTrue(resource1.getErrors().isEmpty());
		assertNotNull(findObject(resource1, "C1"));
		assertNull(findObject(resource1, "C2"));

		reload(resource2);
		assertTrue(resource2.getErrors().isEmpty());
		final EPackage pack = (EPackage)findObject(resource2, "P2");
		final EClass class3 = (EClass)findObject(resource2, "C3");

		assertNotNull(pack);
		assertNotNull(class3);
		assertEquals(1, pack.getEClassifiers().size());
	}

	/*
	 * Try and merge textually. This has no relation with EMF Compare, but it does check that : A - the models
	 * were properly created by setUp005, and B - the textual merge corrupts the two models.
	 */
	@Test
	public void merge005_text() throws Exception {
		setUp005();

		repository.mergeTextual(BRANCH);

		final Status status = repository.status();
		assertTrue(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().contains(repository.getRepoRelativePath(file1)));
		assertTrue(status.getConflicting().contains(repository.getRepoRelativePath(file2)));

		unload(resource1, resource2);

		try {
			reload(resource1);
			fail();
		} catch (IOException e) {
			// expected behavior : the file has been corrupted by the textual merge
		}

		try {
			reload(resource2);
			fail();
		} catch (IOException e) {
			// expected behavior : the file has been corrupted by the textual merge
		}
	}

	/*
	 * Try and merge logically. both files should be marked as conflicting, untouched as compared to master.
	 */
	@Test
	public void merge005_logical() throws Exception {
		setUp005();

		repository.mergeLogical(BRANCH);

		final Status status = repository.status();
		assertTrue(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().contains(repository.getRepoRelativePath(file1)));
		assertTrue(status.getConflicting().contains(repository.getRepoRelativePath(file2)));

		unload(resource1, resource2);

		reload(resource1);
		assertTrue(resource1.getErrors().isEmpty());
		assertNotNull(findObject(resource1, "C1"));
		assertNull(findObject(resource1, "C2"));

		reload(resource2);
		assertTrue(resource2.getErrors().isEmpty());
		assertNotNull(findObject(resource2, "C3"));
		assertNull(findObject(resource2, "C4"));
	}

	/*
	 * Try and merge textually. This has no relation with EMF Compare, but it does check that : A - the models
	 * were properly created by setUp006, and B - the textual merge ends up corrupting the models with false
	 * conflicts.
	 */
	@Test
	public void merge006_text() throws Exception {
		setUp006();

		repository.mergeTextual(BRANCH);

		final Status status = repository.status();
		assertTrue(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().contains(repository.getRepoRelativePath(file1)));
		assertTrue(status.getConflicting().contains(repository.getRepoRelativePath(file2)));

		unload(resource1, resource2);

		try {
			reload(resource1);
			fail();
		} catch (IOException e) {
			// expected behavior : the file has been corrupted by the textual merge
		}

		try {
			reload(resource2);
			fail();
		} catch (IOException e) {
			// expected behavior : the file has been corrupted by the textual merge
		}
	}

	/*
	 * Try and merge logically. The merge should end without issues, without being affected by the textual
	 * conflicts.
	 */
	@Test
	public void merge006_logical() throws Exception {
		setUp006();

		repository.mergeLogical(BRANCH);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().isEmpty());

		// Make sure that the merge actually changed the models
		reload(resource1, resource2);

		assertTrue(resource1.getErrors().isEmpty());
		final EPackage pack1 = (EPackage)findObject(resource1, "P1");
		final EClass class1 = (EClass)findObject(resource1, "C10");
		final EClass class2 = (EClass)findObject(resource1, "C2");
		assertNotNull(pack1);
		assertNotNull(class1);
		assertNotNull(class2);
		assertTrue(class1.getESuperTypes().contains(class2));
		assertEquals(2, pack1.getEClassifiers().size());

		assertTrue(resource2.getErrors().isEmpty());
		final EPackage pack2 = (EPackage)findObject(resource2, "P2");
		final EClass class3 = (EClass)findObject(resource2, "C30");
		final EClass class4 = (EClass)findObject(resource2, "C4");
		assertNotNull(pack2);
		assertNotNull(class3);
		assertNotNull(class4);
		assertTrue(class3.getESuperTypes().contains(class4));
		assertEquals(2, pack2.getEClassifiers().size());
	}

	/*
	 * Try and merge textually. This has no relation with EMF Compare, but it does check that : A - the models
	 * were properly created by setUp007, and B - the textual merge ends up corrupting the logical model even
	 * though it says the merge ends successfully.
	 */
	@Test
	public void merge007_text() throws Exception {
		setUp007();

		repository.mergeTextual(BRANCH);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().isEmpty());

		unload(resource1, resource2);

		// This one has a broken reference towards C3
		reload(resource1);
		assertTrue(resource1.getErrors().isEmpty());
		final EClass class1 = (EClass)findObject(resource1, "C1");
		final EClass superType = class1.getESuperTypes().get(0);
		// the super type (what was "class C3") is a proxy
		assertTrue(superType.eIsProxy());
		// even though its resource has been loaded in the resource set
		final Resource res2 = resource1.getResourceSet().getResource(resource2.getURI(), false);
		assertNotNull(res2);
		// and it is unresolveable
		assertSame(superType, EcoreUtil.resolve(superType, resource1.getResourceSet()));

		// But the second should be fine
		reload(resource2);
		final EPackage pack2 = (EPackage)findObject(resource2, "P2");
		final EClass class4 = (EClass)findObject(resource2, "C4");
		assertNotNull(pack2);
		assertNotNull(class4);
		assertEquals(1, pack2.getEClassifiers().size());
	}

	/*
	 * Try and merge logically. There is a logical conflict, so we expect both files to remain untouched and
	 * the conflict marked.
	 */
	@Test
	public void merge007_logical() throws Exception {
		setUp007();

		repository.mergeLogical(BRANCH);

		final Status status = repository.status();
		assertTrue(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().contains(repository.getRepoRelativePath(file1)));
		assertTrue(status.getConflicting().contains(repository.getRepoRelativePath(file2)));

		unload(resource1, resource2);

		reload(resource1);
		assertTrue(resource1.getErrors().isEmpty());
		final EClass class1 = (EClass)findObject(resource1, "C1");
		assertNotNull(class1);
		assertTrue(class1.getESuperTypes().isEmpty());
		assertNotNull(findObject(resource1, "C2"));

		reload(resource2);
		assertTrue(resource2.getErrors().isEmpty());
		assertNull(findObject(resource2, "C3"));
		assertNotNull(findObject(resource2, "C4"));
	}

	/* This should have the reverse effect as merge007_textual. */
	@Test
	public void merge007_reverse_text() throws Exception {
		setUp007_reverse();

		repository.mergeTextual(BRANCH);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().isEmpty());

		unload(resource1, resource2);

		// This one should be fine
		reload(resource1);
		final EPackage pack1 = (EPackage)findObject(resource1, "P1");
		final EClass class2 = (EClass)findObject(resource1, "C2");
		assertNotNull(pack1);
		assertNotNull(class2);
		assertEquals(1, pack1.getEClassifiers().size());

		// But the second one has a broken references towards C1
		reload(resource2);
		assertTrue(resource2.getErrors().isEmpty());
		final EClass class3 = (EClass)findObject(resource2, "C3");
		final EClass superType = class3.getESuperTypes().get(0);
		// the super type (what was "class C1") is a proxy
		assertTrue(superType.eIsProxy());
		// even though its resource has been loaded in the resource set
		final Resource res1 = resource2.getResourceSet().getResource(resource1.getURI(), false);
		assertNotNull(res1);
		// and it is unresolveable
		assertSame(superType, EcoreUtil.resolve(superType, resource2.getResourceSet()));
	}

	/* This should have the exact same effect as merge007_logical. */
	@Test
	public void merge007_reverse_logical() throws Exception {
		setUp007_reverse();

		repository.mergeLogical(BRANCH);

		final Status status = repository.status();
		assertTrue(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().contains(repository.getRepoRelativePath(file1)));
		assertTrue(status.getConflicting().contains(repository.getRepoRelativePath(file2)));

		unload(resource1, resource2);

		reload(resource1);
		assertTrue(resource1.getErrors().isEmpty());
		assertNull(findObject(resource1, "C1"));
		assertNotNull(findObject(resource1, "C2"));

		reload(resource2);
		assertTrue(resource2.getErrors().isEmpty());
		final EClass class3 = (EClass)findObject(resource2, "C3");
		assertNotNull(class3);
		assertTrue(class3.getESuperTypes().isEmpty());
		assertNotNull(findObject(resource2, "C4"));
	}

	/*
	 * Try and merge textually. This has no relation with EMF Compare, but it does check that : A - the models
	 * were properly created by setUp008, and B - the textual merge ends up corrupting the logical model even
	 * though it says the merge ends successfully.
	 */
	@Test
	public void merge008_text() throws Exception {
		setUp008();

		repository.mergeTextual(BRANCH);

		final Status status = repository.status();
		assertTrue(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().contains(repository.getRepoRelativePath(file1)));

		unload(resource1, resource2);

		try {
			reload(resource1);
			fail();
		} catch (IOException e) {
			// expected behavior : the file has been corrupted by the textual merge
		}

		// This file has been merged, so we should find C40 in it.
		reload(resource2);
		assertTrue(resource2.getErrors().isEmpty());
		final EPackage pack2 = (EPackage)findObject(resource2, "P2");
		assertNotNull(findObject(resource2, "C3"));
		assertNotNull(findObject(resource2, "C40"));
		assertEquals(2, pack2.getEClassifiers().size());
	}

	/*
	 * Try and merge logically. There is no logical conflict, so the merge should end successfully.
	 */
	@Test
	public void merge008_logical() throws Exception {
		setUp008();

		repository.mergeLogical(BRANCH);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().isEmpty());

		reload(resource1, resource2);
		assertTrue(resource1.getErrors().isEmpty());
		final EClass class1 = (EClass)findObject(resource1, "C10");
		assertNotNull(class1);
		assertNotNull(findObject(resource1, "C2"));

		assertTrue(resource2.getErrors().isEmpty());
		final EClass class4 = (EClass)findObject(resource2, "C40");
		assertNotNull(findObject(resource2, "C3"));
		assertNotNull(class4);

		assertTrue(class1.getESuperTypes().contains(class4));
		assertEquals(1, class1.getESuperTypes().size());
	}

	/*
	 * Try and merge textually. This has no relation with EMF Compare, but it does check that : A - the models
	 * were properly created by setUp009, and B - the textual merge wouldn't have corrupted the model in this
	 * case.
	 */
	@Test
	public void merge009_text() throws Exception {
		setUp009();

		repository.mergeTextual(BRANCH);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().isEmpty());

		reload(resource1, resource2);

		assertTrue(resource1.getErrors().isEmpty());
		final EPackage pack1 = (EPackage)findObject(resource1, "P1");
		final EClass class1 = (EClass)findObject(resource1, "C10");
		assertNotNull(class1);
		assertNotNull(findObject(resource1, "C2"));
		assertEquals(2, pack1.getEClassifiers().size());

		assertTrue(resource2.getErrors().isEmpty());
		final EPackage pack2 = (EPackage)findObject(resource2, "P2");
		final EClass class3 = (EClass)findObject(resource2, "C3");
		assertNotNull(class3);
		assertNotNull(findObject(resource2, "C40"));
		assertEquals(2, pack2.getEClassifiers().size());

		assertTrue(class1.getESuperTypes().contains(class3));
		assertEquals(1, class1.getESuperTypes().size());
	}

	/*
	 * Try and merge logically. There is no logical conflict, so the merge should end successfully.
	 */
	@Test
	public void merge009_logical() throws Exception {
		setUp009();

		repository.mergeLogical(BRANCH);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());
		assertTrue(status.getConflicting().isEmpty());

		reload(resource1, resource2);

		assertTrue(resource1.getErrors().isEmpty());
		final EPackage pack1 = (EPackage)findObject(resource1, "P1");
		final EClass class1 = (EClass)findObject(resource1, "C10");
		assertNotNull(class1);
		assertNotNull(findObject(resource1, "C2"));
		assertEquals(2, pack1.getEClassifiers().size());

		assertTrue(resource2.getErrors().isEmpty());
		final EPackage pack2 = (EPackage)findObject(resource2, "P2");
		final EClass class3 = (EClass)findObject(resource2, "C3");
		assertNotNull(class3);
		assertNotNull(findObject(resource2, "C40"));
		assertEquals(2, pack2.getEClassifiers().size());

		assertTrue(class1.getESuperTypes().contains(class3));
		assertEquals(1, class1.getESuperTypes().size());
	}

	private void compareADirectionAndCheck(IFile file, String source, String destination,
			int expectedConflicts, int diffsInSource, int diffsInDestination) throws Exception {
		repository.checkoutBranch(source);
		Comparison compareResult = compare(source, destination, file);

		assertEquals(expectedConflicts, compareResult.getConflicts().size());
		assertDiffCount(compareResult.getDifferences(), diffsInSource, diffsInDestination);
	}
}

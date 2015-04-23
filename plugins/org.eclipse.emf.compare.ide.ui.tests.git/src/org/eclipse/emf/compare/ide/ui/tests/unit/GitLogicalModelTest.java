/*******************************************************************************
 * Copyright (c) 2013, 2015 Obeo.
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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("nls")
public class GitLogicalModelTest extends AbstractGitLogicalModelTest {

	private static final String FILE1_SUFFIX = "_file1";

	private static final String FILE2_SUFFIX = "_file2";

	private String[] masterTipSynonyms;

	private String[] branchTipSynonyms;

	/**
	 * We'll have seven commits during this test, roughly looking like this:
	 * 
	 * <pre>
	 * o--o--o--o--o
	 *        \--o--o
	 * </pre>
	 * 
	 * In order within this array:
	 * 
	 * <pre>
	 * 0--1--2--3--4
	 *        \--5--6
	 * </pre>
	 * 
	 * There will be two ecore files within this repository. file1 (F1) references file2 (F2) and conversely
	 * so that the logical model, whatever the starting point, comprises both files. The following shows which
	 * files were modified with which commits:
	 * 
	 * <pre>
	 * (F1+F2)--(F2)--(F2)--(F2)--(F1+F2)
	 *                    \--(F1+F2)--(F2)
	 * </pre>
	 */
	private RevCommit[] commits = new RevCommit[7];

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));

		makeCrossReference(resource1, resource2, CLASS1_NAME_PREFIX, CLASS1_NAME_PREFIX);
		makeCrossReference(resource2, resource1, CLASS2_NAME_PREFIX, CLASS2_NAME_PREFIX);

		save(resource1, resource2);
		commits[0] = repository.addAndCommit(project, "master-commit-1", file1, file2);

		// Second commit: add class in second resource
		EPackage packFile2 = (EPackage)findObject(resource2, PACKAGE_NAME_PREFIX);
		final EClass newClassFile2 = createClass(packFile2, CLASS3_NAME_PREFIX + FILE2_SUFFIX);
		save(resource2);
		commits[1] = repository.addAndCommit(project, "master-commit-2", file2);

		// Third: rename that new class
		newClassFile2.setName(CLASS4_NAME_PREFIX + FILE2_SUFFIX);
		save(resource2);
		commits[2] = repository.addAndCommit(project, "master-commit-3", file2);

		// Branching point, though stay on master for now
		repository.createBranch(MASTER, BRANCH);

		// fourth commit: remove the new class
		packFile2.getEClassifiers().remove(newClassFile2);
		save(resource2);
		commits[3] = repository.addAndCommit(project, "master-commit-4", file2);

		// fifth and last commit on master: rename second class in both files
		final EClass classBFile1 = (EClass)findObject(resource1, CLASS2_NAME_PREFIX);
		final EClass classBFile2 = (EClass)findObject(resource2, CLASS2_NAME_PREFIX);
		classBFile1.setName(CLASS3_NAME_PREFIX + FILE1_SUFFIX);
		classBFile2.setName(CLASS3_NAME_PREFIX + FILE2_SUFFIX);
		save(resource1, resource2);
		commits[4] = repository.addAndCommit(project, "master-commit-5", file1, file2);

		// checkout the branch now
		repository.checkoutBranch(BRANCH);
		// Note that we have to reload our two resources now
		reload(resource1, resource2);

		// first commit of the branch : rename first class in both files
		final EClass classAFile1 = (EClass)findObject(resource1, CLASS1_NAME_PREFIX);
		final EClass classAFile2 = (EClass)findObject(resource2, CLASS1_NAME_PREFIX);
		classAFile1.setName(CLASS3_NAME_PREFIX + FILE1_SUFFIX);
		classAFile2.setName(CLASS3_NAME_PREFIX + FILE2_SUFFIX);
		save(resource1, resource2);
		commits[5] = repository.addAndCommit(project, "branch-commit-1", file1, file2);

		// second commit of the branch : delete the new (third) class
		// We've reloaded the resource, our reference to the class must be reinitialized
		EcoreUtil.remove(findObject(resource2, CLASS4_NAME_PREFIX));
		save(resource2);
		commits[6] = repository.addAndCommit(project, "branch-commit-2", file2);

		masterTipSynonyms = new String[] {MASTER, commits[4].getName(), };
		branchTipSynonyms = new String[] {BRANCH, commits[6].getName(), Constants.HEAD, };
	}

	@Test
	public void testCompareTipsFile1() throws Exception {
		// 1 conflict (deleted third class)
		// 6 diffs total (renamed both classes in both versions)

		for (String masterTip : masterTipSynonyms) {
			for (String branchTip : branchTipSynonyms) {
				compareBothDirectionsAndCheck(iFile1, masterTip, branchTip, 1, 3, 3);
			}
		}
	}

	@Test
	public void testCompareMasterTipWithBranchingPoint() throws Exception {
		final String branchingPoint = commits[2].getName();
		for (String masterTip : masterTipSynonyms) {
			// No conflict : comparing a version with its ancestor
			// 3 diffs : renamed class in both files, deleted the third

			compareBothDirectionsAndCheck(iFile1, masterTip, branchingPoint, 0, 3, 0);
		}
	}

	@Test
	public void testCompareBranchTipWithBranchingPoint() throws Exception {
		final String branchingPoint = commits[2].getName();
		for (String branchTip : branchTipSynonyms) {
			// No conflict : comparing a version with its ancestor
			// 3 diffs : renamed class in both files, deleted the third

			compareBothDirectionsAndCheck(iFile1, branchTip, branchingPoint, 0, 3, 0);
		}
	}

	@Test
	public void testCompareMasterTipWithInitial() throws Exception {
		final String initialCommit = commits[0].getName();
		for (String masterTip : masterTipSynonyms) {
			// No conflict : comparing a version with its ancestor
			// 2 diffs : renamed class in both files

			compareBothDirectionsAndCheck(iFile1, masterTip, initialCommit, 0, 2, 0);
		}
	}

	@Test
	public void testCompareBranchTipWithInitial() throws Exception {
		final String initialCommit = commits[0].getName();
		for (String branchTip : branchTipSynonyms) {
			// No conflict : comparing a version with its ancestor
			// 2 diffs : renamed class in both files

			compareBothDirectionsAndCheck(iFile1, branchTip, initialCommit, 0, 2, 0);
		}
	}

	@Test
	public void testCompareSecondToLastCommitBothBranch() throws Exception {
		// no conflict
		// 3 diffs : renamed two class on branch, removed the third on master
		final String masterCommit = commits[3].getName();
		final String branchCommit = commits[5].getName();

		compareBothDirectionsAndCheck(iFile1, masterCommit, branchCommit, 0, 1, 2);
	}

}

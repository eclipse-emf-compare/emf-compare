/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.merge;

import static com.google.common.collect.Iterables.all;
import static org.eclipse.emf.compare.ConflictKind.PSEUDO;
import static org.eclipse.emf.compare.ConflictKind.REAL;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.conflict.MatchBasedConflictDetector;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.ConflictDetectors;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitMergeStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitTestRunner;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitTestSupport;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitInput;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitMerge;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitMergeStrategy;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Repository;
import org.junit.runner.RunWith;

@RunWith(GitTestRunner.class)
@GitMergeStrategy(GitMergeStrategyID.MODEL_ADDITIVE)
// FIXME DefaultConflictDetector is broken here
@ConflictDetectors(MatchBasedConflictDetector.class)
@SuppressWarnings({"nls" })
public class AdditiveMergeDiagramTests {

	@GitMerge(local = "wave", remote = "wired")
	@GitInput("data/additive/rac.zip")
	public void testAdditiveMergeWithRAC_MergeWiredOnWave(Status status, Repository repository,
			List<IProject> projects, GitTestSupport support) throws Exception {
		assertFalse(status.hasUncommittedChanges());
		assertEquals(0, status.getConflicting().size());

		Comparison comparison = support.compare("wave", "expected", "model.notation");

		assertTrue(all(comparison.getDifferences(), hasConflict(PSEUDO)));
	}

	@GitMerge(local = "wired", remote = "wave")
	@GitInput("data/additive/rac.zip")
	public void testAdditiveMergeWithRAC_WaveOnWired(Status status, Repository repository,
			List<IProject> projects, GitTestSupport support) throws Exception {
		assertFalse(status.hasUncommittedChanges());
		assertEquals(0, status.getConflicting().size());

		Comparison comparison = support.compare("wired", "expected", "model.notation");

		assertTrue(all(comparison.getDifferences(), hasConflict(PSEUDO)));
	}

	/**
	 * Additive Merge of Solvable conflicts between 2 branches.
	 */
	@GitMerge(local = "branch1", remote = "branch2")
	@GitInput("data/additive/solvable.zip")
	public void testAdditiveMergeSolvableConflicts_Branch2OnBranch1(Status status, Repository repository,
			List<IProject> projects, GitTestSupport support) throws Exception {
		assertFalse(status.hasUncommittedChanges());
		assertEquals(0, status.getConflicting().size());

		Comparison comparison = support.compare("branch1", "expected", "model.notation");

		// We can't know for sure the order of the new elements
		// this is due to the fact that new elements are created in the same
		// package on both sides and it's (currently) impossible to guarantee
		// the order in which they will be placed in their parent during a merge
		// Let's just check that all diffs are in conflict
		assertTrue(all(comparison.getDifferences(), hasConflict(PSEUDO, REAL)));
	}

	@GitMerge(local = "branch2", remote = "branch1")
	@GitInput("data/additive/solvable.zip")
	public void testAdditiveMergeSolvableConflicts_Branch1OnBranch2(Status status, Repository repository,
			List<IProject> projects, GitTestSupport support) throws Exception {
		assertFalse(status.hasUncommittedChanges());
		assertEquals(0, status.getConflicting().size());

		Comparison comparison = support.compare("branch2", "expected", "model.notation");

		// All the diffs that are not in pseudo-conflicts should be MOVEs.
		// this is due to the fact that new elements are created in the same
		// package on both sides and it's (currently) impossible to guarantee
		// the order in which they will be placed in their parent during a merge
		// Let's just check that all diffs are in conflict
		assertTrue(all(comparison.getDifferences(), hasConflict(PSEUDO, REAL)));
	}
}

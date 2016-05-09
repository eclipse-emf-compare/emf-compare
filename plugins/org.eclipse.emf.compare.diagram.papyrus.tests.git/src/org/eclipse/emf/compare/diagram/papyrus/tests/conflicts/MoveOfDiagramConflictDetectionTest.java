/*******************************************************************************
 * Copyright (C) 2016 Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.conflicts;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.conflict.DefaultConflictDetector;
import org.eclipse.emf.compare.conflict.MatchBasedConflictDetector;
import org.eclipse.emf.compare.diagram.papyrus.tests.resourceattachmentchange.move.AbstractResourceAttachmentChangeMoveTests;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitTestRunner;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitCompare;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitInput;
import org.eclipse.emf.compare.ide.ui.tests.workspace.TestProject;
import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.ResolutionStrategies;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.junit.Test;
import org.junit.runner.RunWith;

@SuppressWarnings({"nls", "unused" })
@RunWith(GitTestRunner.class)
@ResolutionStrategies(ResolutionStrategyID.WORKSPACE)
public class MoveOfDiagramConflictDetectionTest {

	@GitCompare(localBranch = "branch1", remoteBranch = "branch2", fileToCompare = "model.uml")
	@GitInput("/data/case001.zip")
	public void pseudoConflictsOnResourceRootTest(Comparison comparison) throws Exception {
		assertEquals(16, comparison.getDifferences().size());
		assertEquals(4, comparison.getConflicts().size());

		for (Conflict conflict : comparison.getConflicts()) {
			assertEquals(ConflictKind.PSEUDO, conflict.getKind());
		}
	}

	@GitCompare(localBranch = "branch1", remoteBranch = "branch2", fileToCompare = "model.uml")
	@GitInput("/data/case002.zip")
	public void conflictsOnResourceRootTest(Comparison comparison) throws Exception {
		assertEquals(4, comparison.getDifferences().size());
		assertEquals(2, comparison.getConflicts().size());

		for (Conflict conflict : comparison.getConflicts()) {
			assertEquals(ConflictKind.REAL, conflict.getKind());
		}
	}

}

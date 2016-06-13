/*******************************************************************************
 * Copyright (C) 2016 EclipseSource Munich Gmbh and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.egit;

import static org.junit.Assert.assertTrue;

import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.ResolutionStrategies;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitTestRunner;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitTestSupport;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitInput;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitMerge;
import org.junit.runner.RunWith;

/**
 * Tests that concurrent changes to di-files do not cause conflicts.
 * <dl>
 * <dt>Origin:</dt>
 * <dd>Given is a UML activity diagram with a di-file that contains a page list and sash model windows.</dd>
 * <dt>Left:</dt>
 * <dd>A new diagram is created, which causes the di-file to contain a new sash model window.</dd>
 * <dt>Right:</dt>
 * <dd>Again, a new diagram is created, which causes the di-file to contain a new sash model window.</dd>
 * </dl>
 * <p>
 * Without properly ignoring changes of the di-file, this scenario would lead to a conflict. The requirement,
 * however, dictates that di-file changes should never lead to a conflict (cf. bug 485494).
 * </p>
 *
 * @author Philip Langer <planger@eclipsesource.com>
 */
@SuppressWarnings({"nls" })
@RunWith(GitTestRunner.class)
@ResolutionStrategies(ResolutionStrategyID.WORKSPACE)
public class IgnoreDiFileChangesInGitMergeTest {

	@GitMerge(local = "branch1", remote = "branch2")
	@GitInput("data/ignore-di-file-changes.zip")
	public void testIgnoredDiFileChangesAfterGitMerge(GitTestSupport testSupport) throws Exception {
		assertTrue(testSupport.noConflict());
		assertTrue(testSupport.fileExists("project1/model.di"));
		assertTrue(testSupport.fileExists("project1/model.notation"));
		assertTrue(testSupport.fileExists("project1/model.uml"));
	}

}

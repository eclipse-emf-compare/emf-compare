/*******************************************************************************
 * Copyright (C) 2015 Obeo and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.merge;

import org.eclipse.egit.core.project.RepositoryMapping;
import org.eclipse.emf.compare.ide.ui.tests.workspace.TestProject;
import org.eclipse.jgit.api.Git;
import org.junit.After;
import org.junit.Before;

/**
 * This executes the same tests as its super-class but in a configuration where the project is not at the root
 * of the git repository. It has been introduced after detecting a bug about such "deep" projects in the
 * preliminary implementations.
 */
public class StrategyRecursiveModelWithDeepProjectTest extends StrategyRecursiveModelTest {

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		repository.disconnect(iProject);

		project = new TestProject("a/b/deepProject");
		iProject = project.getProject();
		repository.connect(iProject);
		repo = RepositoryMapping.getMapping(iProject).getRepository();

		// make initial commit
		Git git = new Git(repo);
		try {
			git.commit().setAuthor("JUnit", "junit@jgit.org").setMessage("Initial commit").call();
		} finally {
			git.close();
		}
	}

	@Override
	@After
	public void clearGitResources() throws Exception {
		repository.disconnect(iProject);
		repository.dispose();
		repo = null;

		super.tearDown();
	}
}

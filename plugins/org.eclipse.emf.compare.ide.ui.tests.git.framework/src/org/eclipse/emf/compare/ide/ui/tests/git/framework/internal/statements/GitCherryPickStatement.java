/*******************************************************************************
 * Copyright (c) 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.git.framework.internal.statements;

import org.eclipse.emf.compare.ide.ui.tests.framework.EMFCompareTestConfiguration;
import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitMergeStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitTestSupport;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitCherryPick;
import org.junit.runners.model.FrameworkMethod;

/**
 * This class handle all test related to cherry-pick operations.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class GitCherryPickStatement extends AbstractGitOperationStatement {

	/**
	 * Constructor for Git cherry-pick statements.
	 * 
	 * @param testObject
	 *            The test class
	 * @param test
	 *            The test method
	 * @param resolutionStrategy
	 *            The resolution strategy used for this test
	 * @param configuration
	 *            EMFCompare configuration for this test
	 * @param mergeStrategy
	 *            The merge strategy used for the test
	 * @param path
	 *            The path of the archive containing the repository
	 */
	public GitCherryPickStatement(Object testObject, FrameworkMethod test,
			ResolutionStrategyID resolutionStrategy, EMFCompareTestConfiguration configuration,
			GitMergeStrategyID mergeStrategy, String path) {
		super(testObject, test, resolutionStrategy, configuration, mergeStrategy, path);
	}

	@Override
	protected void callGitOperation(GitTestSupport gitTestsSupport, String localBranch, String remoteBranch)
			throws Exception {
		gitTestsSupport.cherryPick(localBranch, remoteBranch);
	}

	@Override
	protected String getCheckoutedBranch() {
		GitCherryPick cherryPick = test.getAnnotation(GitCherryPick.class);
		return cherryPick.local();
	}

	@Override
	protected String getOtherBranch() {
		GitCherryPick cherryPick = test.getAnnotation(GitCherryPick.class);
		return cherryPick.remote();
	}

}

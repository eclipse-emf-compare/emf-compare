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
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitMerge;
import org.junit.runners.model.FrameworkMethod;

/**
 * This class handle all test related to merge operations.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class GitMergeStatement extends AbstractGitOperationStatement {

	/**
	 * Constructor for Git merge statements.
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
	public GitMergeStatement(Object testObject, FrameworkMethod test, ResolutionStrategyID resolutionStrategy,
			EMFCompareTestConfiguration configuration, GitMergeStrategyID mergeStrategy, String path) {
		super(testObject, test, resolutionStrategy, configuration, mergeStrategy, path);
	}

	@Override
	protected void callGitOperation(GitTestSupport gitTestsSupport, String localBranch, String remoteBranch)
			throws Exception {
		gitTestsSupport.merge(localBranch, remoteBranch);
	}

	@Override
	protected String getCheckoutedBranch() {
		GitMerge merge = test.getAnnotation(GitMerge.class);
		return merge.local();
	}

	@Override
	protected String getOtherBranch() {
		GitMerge merge = test.getAnnotation(GitMerge.class);
		return merge.remote();
	}

}

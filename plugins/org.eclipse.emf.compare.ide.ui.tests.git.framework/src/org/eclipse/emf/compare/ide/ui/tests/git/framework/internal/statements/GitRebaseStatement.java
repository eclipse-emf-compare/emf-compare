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
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitRebase;
import org.junit.runners.model.FrameworkMethod;

/**
 * This class handle all test related to rebase operations.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class GitRebaseStatement extends AbstractGitOperationStatement {

	public GitRebaseStatement(Object testObject, FrameworkMethod test,
			ResolutionStrategyID resolutionStrategy, EMFCompareTestConfiguration configuration,
			GitMergeStrategyID mergeStrategy, String path) {
		super(testObject, test, resolutionStrategy, configuration, mergeStrategy, path);
	}

	@Override
	protected void callGitOperation(GitTestSupport gitTestsSupport, String localBranch, String remoteBranch)
			throws Throwable {
		gitTestsSupport.rebase(localBranch, remoteBranch);
	}

	@Override
	protected String getCheckoutedBranch() {
		GitRebase rebase = test.getAnnotation(GitRebase.class);
		return rebase.local();
	}

	@Override
	protected String getOtherBranch() {
		GitRebase rebase = test.getAnnotation(GitRebase.class);
		return rebase.remote();
	}

}

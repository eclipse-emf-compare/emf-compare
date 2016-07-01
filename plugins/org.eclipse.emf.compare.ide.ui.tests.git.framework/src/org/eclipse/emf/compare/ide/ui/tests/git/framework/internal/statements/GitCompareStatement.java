/*******************************************************************************
 * Copyright (c) 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - support more flexible parameters of test methods
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.git.framework.internal.statements;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ide.ui.tests.framework.EMFCompareTestConfiguration;
import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitTestSupport;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitCompare;
import org.junit.runners.model.FrameworkMethod;

/**
 * This class handle all git related comparison tests.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class GitCompareStatement extends AbstractGitStatement {

	/** The path of the archive containing the repository. */
	private final String path;

	/**
	 * Constructor for the Git comparison statement.
	 * 
	 * @param testObject
	 *            The test class
	 * @param test
	 *            The test method
	 * @param resolutionStrategy
	 *            The resolution strategy used for this test
	 * @param configuration
	 *            EMFCompare configuration for this test
	 * @param path
	 *            The path of the archive containing the repository
	 */
	public GitCompareStatement(Object testObject, FrameworkMethod test,
			ResolutionStrategyID resolutionStrategy, EMFCompareTestConfiguration configuration, String path) {
		super(testObject, test, resolutionStrategy, configuration);
		this.path = normalizePath(path);
	}

	@Override
	public void evaluate() throws Throwable {
		setEMFComparePreferences();
		GitCompare compare = test.getAnnotation(GitCompare.class);
		String localBranch = InternalGitTestSupport.normalizeBranch(compare.local());
		String remoteBranch = InternalGitTestSupport.normalizeBranch(compare.remote());
		String fileToCompare = normalizePath(compare.file());
		String containerProject = compare.containerProject();

		GitTestSupport gitTestsSupport = new GitTestSupport();
		try {
			gitTestsSupport.setup();
			gitTestsSupport.createRepositoryFromPath(test.getMethod().getDeclaringClass(), path);
			Comparison comparison = gitTestsSupport.compare(localBranch, remoteBranch, fileToCompare,
					containerProject);
			Class<?>[] parameters = test.getMethod().getParameterTypes();
			if (parameters[parameters.length - 1].getName().equals(GitTestSupport.class.getName())) {
				test.invokeExplosively(testObject, comparison, gitTestsSupport);
			} else {
				test.invokeExplosively(testObject, comparison);
			}
		} finally {
			restoreEMFComparePreferences();
			gitTestsSupport.tearDown();
		}
	}
}

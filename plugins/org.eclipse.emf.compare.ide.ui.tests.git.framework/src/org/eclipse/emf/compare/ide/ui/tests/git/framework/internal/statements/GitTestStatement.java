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

import org.eclipse.emf.compare.ide.ui.tests.framework.EMFCompareTestConfiguration;
import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitTestSupport;
import org.junit.runners.model.FrameworkMethod;

/**
 * This class is used to unzip a repository from a zip and give access to this repository to the test class.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class GitTestStatement extends AbstractGitStatement {

	private final String path;

	public GitTestStatement(Object testObject, FrameworkMethod test, ResolutionStrategyID resolutionStrategy,
			EMFCompareTestConfiguration configuration, String path) {
		super(testObject, test, resolutionStrategy, configuration);
		this.path = normalizePath(path);
	}

	@Override
	public void evaluate() throws Throwable {
		setEMFComparePreferences();

		GitTestSupport gitTestsSupport = new GitTestSupport();
		try {
			gitTestsSupport.setup();
			gitTestsSupport.createRepositoryFromPath(test.getMethod().getDeclaringClass(), path);
			Object[] parameters = createParameters(test.getMethod(), gitTestsSupport);
			test.invokeExplosively(testObject, parameters);
		} finally {
			restoreEMFComparePreferences();
			gitTestsSupport.tearDown();
		}
	}

}

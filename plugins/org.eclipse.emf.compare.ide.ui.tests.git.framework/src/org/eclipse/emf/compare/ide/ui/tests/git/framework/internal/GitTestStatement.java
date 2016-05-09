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
package org.eclipse.emf.compare.ide.ui.tests.git.framework.internal;

import org.eclipse.emf.compare.ide.ui.tests.framework.AbstractCompareStatement;
import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.junit.runners.model.FrameworkMethod;

/**
 * This class is used to unzip a repository from a zip and give access to this repository to the test class.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class GitTestStatement extends AbstractCompareStatement {

	private final String path;

	public GitTestStatement(Object testObject, FrameworkMethod test, ResolutionStrategyID resolutionStrategy,
			Class<?>[] disabledMatchEngineFactory, Class<?> diffEngine, Class<?> eqEngine,
			Class<?> reqEngine, Class<?> conflictDetector, Class<?>[] disabledPostProcessors, String path) {
		super(testObject, test, resolutionStrategy, disabledMatchEngineFactory, diffEngine, eqEngine,
				reqEngine, conflictDetector, disabledPostProcessors);
		this.path = normalizePath(path);
	}

	@Override
	public void evaluate() throws Throwable {
		setEMFComparePreferences();

		GitTestSupport gitTestsSupport = new GitTestSupport();
		try {
			gitTestsSupport.setup();
			gitTestsSupport.createRepositoryFromPath(test.getMethod().getDeclaringClass(), path);
			Class<?>[] parameters = test.getMethod().getParameterTypes();
			if (parameters[parameters.length - 1].getName().equals(GitTestSupport.class.getName())) {
				test.invokeExplosively(testObject, gitTestsSupport.getStatus(), gitTestsSupport
						.getRepository(), gitTestsSupport.getProjects(), gitTestsSupport);
			} else {
				test.invokeExplosively(testObject, gitTestsSupport.getStatus(), gitTestsSupport
						.getRepository(), gitTestsSupport.getProjects());
			}
		} finally {
			restoreEMFComparePreferences();
			gitTestsSupport.tearDown();
		}
	}

}

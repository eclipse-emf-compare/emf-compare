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
package org.eclipse.emf.compare.ide.ui.tests.git.framework;

import org.eclipse.emf.compare.ide.ui.tests.framework.AbstractCompareTestRunner;
import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.MergeStrategy;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.internal.GitTestCaseJUnitBlock;
import org.junit.runners.model.InitializationError;

/**
 * The compare EGit specific runner.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class GitTestRunner extends AbstractCompareTestRunner {

	/** Default merge strategy if the @MergeStrategy annotation is not used. */
	private static MergeStrategyID defaultMergeStrategy = MergeStrategyID.MODEL_RECURSIVE;

	/**
	 * Constructor.
	 * 
	 * @param testClass
	 *            The class to test
	 * @throws InitializationError
	 */
	public GitTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
	}

	@Override
	public void createRunner(Class<?> testClass, ResolutionStrategyID resolutionStrategy,
			Class<?>[] disabledMatchEngines, Class<?> diffEngine, Class<?> eqEngine, Class<?> reqEngine,
			Class<?> conflictDetector, Class<?>[] disabledPostProcessors) throws InitializationError {
		MergeStrategy mStrategy = getTestClass().getAnnotation(MergeStrategy.class);
		final MergeStrategyID mergeStrategy;
		if (mStrategy == null) {
			mergeStrategy = defaultMergeStrategy;
		} else {
			mergeStrategy = mStrategy.value();
		}
		runners.add(new GitTestCaseJUnitBlock(testClass, resolutionStrategy, disabledMatchEngines,
				diffEngine, eqEngine, reqEngine, conflictDetector, disabledPostProcessors, mergeStrategy));

	}

}

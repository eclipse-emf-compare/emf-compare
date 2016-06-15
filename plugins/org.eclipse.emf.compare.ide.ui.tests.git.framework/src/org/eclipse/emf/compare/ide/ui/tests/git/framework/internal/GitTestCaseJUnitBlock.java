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

import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.emf.compare.ide.ui.tests.framework.AbstractCompareTestCaseJUnitBlock;
import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.MergeStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitCompare;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitInput;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitMerge;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitTest;
import org.junit.Assert;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class GitTestCaseJUnitBlock extends AbstractCompareTestCaseJUnitBlock {

	private MergeStrategyID mergeStrategy;

	public GitTestCaseJUnitBlock(Class<?> klass, ResolutionStrategyID resolutionStrategy,
			Class<?>[] disabledMatchEngines, Class<?> diffEngine, Class<?> eqEngine, Class<?> reqEngine,
			Class<?> conflictDetector, Class<?>[] disabledPostProcessors, MergeStrategyID mergeStrategy)
			throws InitializationError {
		super(klass, resolutionStrategy, disabledMatchEngines, diffEngine, eqEngine, reqEngine,
				conflictDetector, disabledPostProcessors);
		this.mergeStrategy = mergeStrategy;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.junit.runners.BlockJUnit4ClassRunner#computeTestMethods()
	 */
	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		final List<FrameworkMethod> allMethods = Lists
				.newArrayList(getTestClass().getAnnotatedMethods(GitCompare.class));
		allMethods.addAll(getTestClass().getAnnotatedMethods(GitMerge.class));
		allMethods.addAll(getTestClass().getAnnotatedMethods(GitTest.class));
		return allMethods;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.junit.runners.BlockJUnit4ClassRunner#methodBlock(org.junit.runners.model.FrameworkMethod)
	 */
	@Override
	protected Statement methodBlock(FrameworkMethod method) {
		Object testObject = null;
		try {
			testObject = createTest();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

		GitInput input = method.getAnnotation(GitInput.class);

		Statement result = null;
		if (input != null) {
			if (method.getAnnotation(GitCompare.class) != null) {
				result = new GitCompareStatement(testObject, method, resolutionStrategy, disabledMatchEngines,
						diffEngine, eqEngine, reqEngine, conflictDetector, disabledPostProcessors,
						input.value());
			} else if (method.getAnnotation(GitMerge.class) != null) {
				result = new MergeStatement(testObject, method, resolutionStrategy, disabledMatchEngines,
						diffEngine, eqEngine, reqEngine, conflictDetector, disabledPostProcessors,
						mergeStrategy, input.value());
			} else if (method.getAnnotation(GitTest.class) != null) {
				result = new GitTestStatement(testObject, method, resolutionStrategy, disabledMatchEngines,
						diffEngine, eqEngine, reqEngine, conflictDetector, disabledPostProcessors,
						input.value());
			}
		}
		return result;
	}

}

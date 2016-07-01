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
import org.eclipse.emf.compare.ide.ui.tests.framework.EMFCompareTestConfiguration;
import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitMergeStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitCherryPick;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitCompare;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitInput;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitMerge;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitRebase;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitTest;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.internal.statements.GitCherryPickStatement;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.internal.statements.GitCompareStatement;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.internal.statements.GitMergeStatement;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.internal.statements.GitRebaseStatement;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.internal.statements.GitTestStatement;
import org.junit.Assert;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * EMFCompare specific {@link BlockJUnit4ClassRunner} used for Git comparisons.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class GitTestCaseJUnitBlock extends AbstractCompareTestCaseJUnitBlock {

	/** The merge strategy to use for the test. */
	private GitMergeStrategyID mergeStrategy;

	/**
	 * Constructor for the classic (no Git) comparison statement.
	 * 
	 * @param klass
	 *            The test class
	 * @param resolutionStrategy
	 *            The resolution strategy used for this test
	 * @param configuration
	 *            EMFCompare configurations for this test
	 * @param mergeStrategy
	 *            The merge strategy to use for this test.
	 * @throws InitializationError
	 *             If something went wrong during test initialization
	 */
	public GitTestCaseJUnitBlock(Class<?> klass, ResolutionStrategyID resolutionStrategy,
			EMFCompareTestConfiguration configuration, GitMergeStrategyID mergeStrategy)
			throws InitializationError {
		super(klass, resolutionStrategy, configuration);
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
		allMethods.addAll(getTestClass().getAnnotatedMethods(GitCherryPick.class));
		allMethods.addAll(getTestClass().getAnnotatedMethods(GitRebase.class));
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
			// CHECKSTYLE:OFF JUnit createTest() method throws an Exception
		} catch (Exception e) {
			// CHECKSTYLE:ON
			Assert.fail(e.getMessage());
		}

		GitInput input = method.getAnnotation(GitInput.class);

		Statement result = null;
		if (input != null) {
			if (method.getAnnotation(GitCompare.class) != null) {
				result = new GitCompareStatement(testObject, method, resolutionStrategy, configuration,
						input.value());
			} else if (method.getAnnotation(GitMerge.class) != null) {
				result = new GitMergeStatement(testObject, method, resolutionStrategy, configuration,
						mergeStrategy, input.value());
			} else if (method.getAnnotation(GitCherryPick.class) != null) {
				result = new GitCherryPickStatement(testObject, method, resolutionStrategy, configuration,
						mergeStrategy, input.value());
			} else if (method.getAnnotation(GitRebase.class) != null) {
				result = new GitRebaseStatement(testObject, method, resolutionStrategy, configuration,
						mergeStrategy, input.value());
			} else if (method.getAnnotation(GitTest.class) != null) {
				result = new GitTestStatement(testObject, method, resolutionStrategy, configuration,
						input.value());
			}
		}
		return result;
	}

}

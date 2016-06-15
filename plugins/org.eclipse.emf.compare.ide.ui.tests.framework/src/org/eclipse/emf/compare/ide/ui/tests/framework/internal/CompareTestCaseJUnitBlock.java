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
package org.eclipse.emf.compare.ide.ui.tests.framework.internal;

import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.emf.compare.ide.ui.tests.framework.AbstractCompareTestCaseJUnitBlock;
import org.eclipse.emf.compare.ide.ui.tests.framework.EMFCompareTestConfiguration;
import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.Compare;
import org.junit.Assert;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * EMFCompare specific {@link BlockJUnit4ClassRunner} used for simple comparisons.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class CompareTestCaseJUnitBlock extends AbstractCompareTestCaseJUnitBlock {

	/**
	 * Constructor for the classic (no Git) comparison JUnit block.
	 * 
	 * @param klass
	 *            The test class
	 * @param resolutionStrategy
	 *            The resolution strategy used for this test
	 * @param configuration
	 *            EMFComapre configurations for this test
	 * @throws InitializationError
	 *             If something went wrong during test initialization
	 */
	public CompareTestCaseJUnitBlock(Class<?> klass, ResolutionStrategyID resolutionStrategy,
			EMFCompareTestConfiguration configuration) throws InitializationError {
		super(klass, resolutionStrategy, configuration);
	}

	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		final List<FrameworkMethod> allMethods = Lists
				.newArrayList(getTestClass().getAnnotatedMethods(Compare.class));
		return allMethods;
	}

	@Override
	protected Statement methodBlock(FrameworkMethod method) {
		Object testObject = null;
		try {
			testObject = createTest();
			// CHECKSTYLE:OFF JUnit method createTest() throw an Exception
		} catch (Exception e) {
			// CHECKSTYLE:ON
			Assert.fail(e.getMessage());
		}

		Statement result = null;
		if (method.getAnnotation(Compare.class) != null) {
			result = new CompareStatement(testObject, method, resolutionStrategy, configuration);
		}
		return result;
	}
}

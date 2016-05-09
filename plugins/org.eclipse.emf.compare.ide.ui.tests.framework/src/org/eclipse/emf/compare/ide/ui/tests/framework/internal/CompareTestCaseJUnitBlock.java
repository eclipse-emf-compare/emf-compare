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
import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.Compare;
import org.junit.Assert;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class CompareTestCaseJUnitBlock extends AbstractCompareTestCaseJUnitBlock {

	public CompareTestCaseJUnitBlock(Class<?> klass, ResolutionStrategyID resolutionStrategy,
			Class<?>[] disabledMatchEngines, Class<?> diffEngine, Class<?> eqEngine, Class<?> reqEngine,
			Class<?> conflictDetector, Class<?>[] disabledPostProcessors) throws InitializationError {
		super(klass, resolutionStrategy, disabledMatchEngines, diffEngine, eqEngine, reqEngine,
				conflictDetector, disabledPostProcessors);
	}

	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		final List<FrameworkMethod> allMethods = Lists.newArrayList(getTestClass().getAnnotatedMethods(
				Compare.class));
		return allMethods;
	}

	@Override
	protected Statement methodBlock(FrameworkMethod method) {
		Object testObject = null;
		try {
			testObject = createTest();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

		Statement result = null;
		if (method.getAnnotation(Compare.class) != null) {
			result = new CompareStatement(testObject, method, resolutionStrategy, disabledMatchEngines,
					diffEngine, eqEngine, reqEngine, conflictDetector, disabledPostProcessors);
		}
		return result;
	}
}

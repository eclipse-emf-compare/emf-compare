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

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ide.ui.tests.framework.AbstractCompareStatement;
import org.eclipse.emf.compare.ide.ui.tests.framework.EMFCompareTestConfiguration;
import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.Compare;
import org.junit.runners.model.FrameworkMethod;

/**
 * JUnit statement for simple comparison tests.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class CompareStatement extends AbstractCompareStatement {

	/**
	 * Constructor for the classic (no Git) comparison statement.
	 * 
	 * @param testObject
	 *            The test class
	 * @param test
	 *            The test method
	 * @param resolutionStrategy
	 *            The resolution strategy used for this test
	 * @param configuration
	 *            EMFComapre configurations for this test
	 */
	public CompareStatement(Object testObject, FrameworkMethod test, ResolutionStrategyID resolutionStrategy,
			EMFCompareTestConfiguration configuration) {
		super(testObject, test, resolutionStrategy, configuration);
	}

	@Override
	public void evaluate() throws Throwable {
		setEMFComparePreferences();

		Compare compare = test.getAnnotation(Compare.class);
		String left = normalizePath(compare.left());
		String right = normalizePath(compare.right());
		String ancestor = normalizePath(compare.ancestor());

		CompareTestSupport testSupport = new CompareTestSupport();
		try {
			testSupport.loadResources(test.getMethod().getDeclaringClass(), left, right, ancestor);
			Comparison comparison = testSupport.compare();
			Class<?>[] parameters = test.getMethod().getParameterTypes();
			if (parameters[parameters.length - 1].getName().equals(CompareTestSupport.class.getName())) {
				test.invokeExplosively(testObject, comparison, testSupport);
			} else {
				test.invokeExplosively(testObject, comparison);
			}
		} finally {
			restoreEMFComparePreferences();
			testSupport.tearDown();
		}
	}

}

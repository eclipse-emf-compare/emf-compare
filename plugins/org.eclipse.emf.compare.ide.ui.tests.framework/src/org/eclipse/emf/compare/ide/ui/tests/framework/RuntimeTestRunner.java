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
package org.eclipse.emf.compare.ide.ui.tests.framework;

import org.eclipse.emf.compare.ide.ui.tests.framework.internal.CompareTestCaseJUnitBlock;
import org.junit.runners.model.InitializationError;

/**
 * EMFCompare specific runner for full comparison tests without git.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class RuntimeTestRunner extends AbstractCompareTestRunner {

	/**
	 * The constructor.
	 * 
	 * @param testClass
	 *            The given test class
	 * @throws InitializationError
	 *             If the test cannot be created
	 */
	public RuntimeTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
	}

	@Override
	public void createRunner(Class<?> testClass, ResolutionStrategyID resolutionStrategy,
			EMFCompareTestConfiguration configuration) throws InitializationError {
		runners.add(new CompareTestCaseJUnitBlock(testClass, resolutionStrategy, configuration));
	}

}

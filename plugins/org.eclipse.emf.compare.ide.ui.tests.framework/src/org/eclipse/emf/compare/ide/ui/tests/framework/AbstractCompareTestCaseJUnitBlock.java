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

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/**
 * EMFCompare specific junit blocks must extends this class.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public abstract class AbstractCompareTestCaseJUnitBlock extends BlockJUnit4ClassRunner {

	/** Separator used to construct the name of the test. */
	private static final String NAME_SEPARATOR = " - "; //$NON-NLS-1$

	/** The resolution strategy to use for the test. */
	protected final ResolutionStrategyID resolutionStrategy;

	/** Wrapper for the configurations of EMFCompare for the test. */
	protected final EMFCompareTestConfiguration configuration;

	/**
	 * Constructor for the classic (no Git) comparison statement.
	 * 
	 * @param klass
	 *            The test class
	 * @param resolutionStrategy
	 *            The resolution strategy used for this test
	 * @param configuration
	 *            EMFCompare configurations for this test
	 * @throws InitializationError
	 *             If something went wrong during test initialization
	 */
	public AbstractCompareTestCaseJUnitBlock(Class<?> klass, ResolutionStrategyID resolutionStrategy,
			EMFCompareTestConfiguration configuration) throws InitializationError {
		super(klass);
		this.resolutionStrategy = resolutionStrategy;
		this.configuration = configuration;
	}

	@Override
	protected String testName(FrameworkMethod method) {
		final StringBuilder name = new StringBuilder();
		name.append(super.testName(method));
		name.append(NAME_SEPARATOR);
		name.append(resolutionStrategy.name().toLowerCase());
		name.append(NAME_SEPARATOR);
		name.append(configuration.getDiffEngine().getSimpleName());
		name.append(NAME_SEPARATOR);
		name.append(configuration.getEqEngine().getSimpleName());
		name.append(NAME_SEPARATOR);
		name.append(configuration.getReqEngine().getSimpleName());
		name.append(NAME_SEPARATOR);
		name.append(configuration.getConflictDetector().getSimpleName());

		return name.toString();
	}

}

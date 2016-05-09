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

	protected final ResolutionStrategyID resolutionStrategy;

	protected final Class<?>[] disabledMatchEngines;

	protected final Class<?> diffEngine;

	protected final Class<?> eqEngine;

	protected final Class<?> reqEngine;

	protected final Class<?> conflictDetector;

	protected final Class<?>[] disabledPostProcessors;

	public AbstractCompareTestCaseJUnitBlock(Class<?> klass, ResolutionStrategyID resolutionStrategy,
			Class<?>[] disabledMatchEngines, Class<?> diffEngine, Class<?> eqEngine, Class<?> reqEngine,
			Class<?> conflictDetector, Class<?>[] disabledPostProcessors) throws InitializationError {
		super(klass);
		this.resolutionStrategy = resolutionStrategy;
		this.disabledMatchEngines = disabledMatchEngines;
		this.diffEngine = diffEngine;
		this.eqEngine = eqEngine;
		this.reqEngine = reqEngine;
		this.conflictDetector = conflictDetector;
		this.disabledPostProcessors = disabledPostProcessors;
	}

	@Override
	protected String testName(FrameworkMethod method) {
		final StringBuilder name = new StringBuilder();
		name.append(super.testName(method));
		name.append(" - "); //$NON-NLS-1$
		name.append(resolutionStrategy.name().toLowerCase());
		name.append(" - "); //$NON-NLS-1$
		name.append(diffEngine.getSimpleName());
		name.append(" - "); //$NON-NLS-1$
		name.append(eqEngine.getSimpleName());
		name.append(" - "); //$NON-NLS-1$
		name.append(reqEngine.getSimpleName());
		name.append(" - "); //$NON-NLS-1$
		name.append(conflictDetector.getSimpleName());

		return name.toString();
	}

}

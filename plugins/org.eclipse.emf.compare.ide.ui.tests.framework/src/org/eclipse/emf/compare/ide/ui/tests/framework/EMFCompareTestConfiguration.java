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

/**
 * This class is a wrapper for EMFCompare configurations.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class EMFCompareTestConfiguration {

	/** The match engines disabled used for this test. */
	private final Class<?>[] disabledMatchEngines;

	/** The diff engine used for this test. */
	private final Class<?> diffEngine;

	/** The eq engine used for this test. */
	private final Class<?> eqEngine;

	/** The req engine used for this test. */
	private final Class<?> reqEngine;

	/** The conflict detector used for this test. */
	private final Class<?> conflictDetector;

	/** The post-processors disabled for this test. */
	private final Class<?>[] disabledPostProcessors;

	/**
	 * The constructor.
	 * 
	 * @param disabledMatchEngineFactory
	 *            The match engines disabled for the test
	 * @param diffEngine
	 *            The diff engine used for the test
	 * @param eqEngine
	 *            The eq engine used for the test
	 * @param reqEngine
	 *            The req engine used for the test
	 * @param conflictDetector
	 *            The conflict detector used for the test
	 * @param disabledPostProcessors
	 *            The post processors disabled for the test
	 */
	public EMFCompareTestConfiguration(Class<?>[] disabledMatchEngineFactory, Class<?> diffEngine,
			Class<?> eqEngine, Class<?> reqEngine, Class<?> conflictDetector,
			Class<?>[] disabledPostProcessors) {
		this.disabledMatchEngines = disabledMatchEngineFactory;
		this.diffEngine = diffEngine;
		this.eqEngine = eqEngine;
		this.reqEngine = reqEngine;
		this.conflictDetector = conflictDetector;
		this.disabledPostProcessors = disabledPostProcessors;
	}

	public Class<?>[] getDisabledMatchEngines() {
		return disabledMatchEngines;
	}

	public Class<?> getDiffEngine() {
		return diffEngine;
	}

	public Class<?> getEqEngine() {
		return eqEngine;
	}

	public Class<?> getReqEngine() {
		return reqEngine;
	}

	public Class<?> getConflictDetector() {
		return conflictDetector;
	}

	public Class<?>[] getDisabledPostProcessors() {
		return disabledPostProcessors;
	}

}

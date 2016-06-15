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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.conflict.DefaultConflictDetector;
import org.eclipse.emf.compare.conflict.MatchBasedConflictDetector;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.equi.DefaultEquiEngine;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.ConflictDetectors;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.DiffEngines;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.DisabledMatchEngines;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.DisabledPostProcessors;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.EqEngines;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.ReqEngines;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.ResolutionStrategies;
import org.eclipse.emf.compare.req.DefaultReqEngine;
import org.junit.Assert;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

/**
 * EMFCompare specific runners must extends this class.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public abstract class AbstractCompareTestRunner extends ParentRunner<Runner> {

	/** The list of all runners. */
	protected final List<Runner> runners;

	/** Default list of resolution strategies used if the @ResolutionStrategies annotation is not used. */
	private final ResolutionStrategyID[] defaultResolutionStrategies = new ResolutionStrategyID[] {
			ResolutionStrategyID.WORKSPACE };

	/** Default list of match engines disabled if the @MatchEngines annotation is not used. */
	private final Class<?>[] defaultDisabledMatchEngines = new Class<?>[] {};

	/** Default list of diff engines used if the @DiffEngines annotation is not used. */
	private final Class<?>[] defaultDiffEngines = new Class<?>[] {DefaultDiffEngine.class };

	/** Default list of eq engines used if the @EqEngines annotation is not used. */
	private final Class<?>[] defaultEqEngines = new Class<?>[] {DefaultEquiEngine.class };

	/** Default list of req engines used if the @ReqEngines annotation is not used. */
	private final Class<?>[] defaultReqEngines = new Class<?>[] {DefaultReqEngine.class };

	/** Default list of conflict detector used if the @ConflictEngines annotation is not used. */
	private final Class<?>[] defaultConflictDetectors = new Class<?>[] {DefaultConflictDetector.class,
			MatchBasedConflictDetector.class, };

	/** Default list of resolution strategies disabled if the @PostProcessors annotation is not used. */
	private final Class<?>[] defaultDisabledPostProcessors = new Class<?>[] {};

	public AbstractCompareTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);

		runners = new ArrayList<Runner>();
		prepareRunnersForTests();
	}

	/**
	 * Create a runner for each configuration. The configurations are a matrix of all association
	 * possibilities between match, diff, eq, req and conflict engines.
	 */
	private void prepareRunnersForTests() {
		ResolutionStrategies rStrategies = getTestClass().getAnnotation(ResolutionStrategies.class);
		final ResolutionStrategyID[] resolutionStrategies;
		if (rStrategies == null) {
			resolutionStrategies = defaultResolutionStrategies;
		} else {
			resolutionStrategies = rStrategies.value();
		}

		DisabledMatchEngines mEngines = getTestClass().getAnnotation(DisabledMatchEngines.class);
		final Class<?>[] disabledMatchEngines;
		if (mEngines == null) {
			disabledMatchEngines = defaultDisabledMatchEngines;
		} else {
			disabledMatchEngines = mEngines.value();
		}

		DiffEngines dEngines = getTestClass().getAnnotation(DiffEngines.class);
		final Class<?>[] diffEngines;
		if (dEngines == null) {
			diffEngines = defaultDiffEngines;
		} else {
			diffEngines = dEngines.value();
		}

		EqEngines eEngines = getTestClass().getAnnotation(EqEngines.class);
		final Class<?>[] eqEngines;
		if (eEngines == null) {
			eqEngines = defaultEqEngines;
		} else {
			eqEngines = eEngines.value();
		}

		ReqEngines rEngines = getTestClass().getAnnotation(ReqEngines.class);
		final Class<?>[] reqEngines;
		if (rEngines == null) {
			reqEngines = defaultReqEngines;
		} else {
			reqEngines = rEngines.value();
		}

		ConflictDetectors cEngines = getTestClass().getAnnotation(ConflictDetectors.class);
		final Class<?>[] conflictDetectors;
		if (cEngines == null) {
			conflictDetectors = defaultConflictDetectors;
		} else {
			conflictDetectors = cEngines.value();
		}

		DisabledPostProcessors pProcessors = getTestClass().getAnnotation(DisabledPostProcessors.class);
		final Class<?>[] disabledPostProcessors;
		if (pProcessors == null) {
			disabledPostProcessors = defaultDisabledPostProcessors;
		} else {
			disabledPostProcessors = pProcessors.value();
		}

		for (ResolutionStrategyID resolutionStrategy : resolutionStrategies) {
			for (Class<?> diffEngine : diffEngines) {
				for (Class<?> eqEngine : eqEngines) {
					for (Class<?> reqEngine : reqEngines) {
						for (Class<?> conflictDetector : conflictDetectors) {
							try {
								createRunner(getTestClass().getJavaClass(), resolutionStrategy,
										disabledMatchEngines, diffEngine, eqEngine, reqEngine,
										conflictDetector, disabledPostProcessors);
							} catch (InitializationError e) {
								e.printStackTrace();
								Assert.fail(e.getMessage());
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Create the specific runner for the given parameters.
	 * 
	 * @param testClass
	 *            The class to test
	 * @param resolutionStrategy
	 *            The resolution strategy used for this runner
	 * @param disabledMatchEngines
	 *            The match engines disabled for this runner
	 * @param diffEngine
	 *            The diff engine used for this runner
	 * @param eqEngine
	 *            The eq engine used for this runner
	 * @param reqEngine
	 *            The req engine used for this runner
	 * @param conflictDetector
	 *            The conflict detector used for this runner
	 * @param disabledPostProcessors
	 *            The post processors disabled for this runner
	 * @throws InitializationError
	 *             If the creation of the runner goes wrong
	 */
	public abstract void createRunner(Class<?> testClass, ResolutionStrategyID resolutionStrategy,
			Class<?>[] disabledMatchEngines, Class<?> diffEngine, Class<?> eqEngine, Class<?> reqEngine,
			Class<?> conflictDetector, Class<?>[] disabledPostProcessors) throws InitializationError;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.junit.runners.BlockJUnit4ClassRunner#getChildren()
	 */
	@Override
	protected List<Runner> getChildren() {
		return runners;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.junit.runners.ParentRunner#describeChild(java.lang.Object)
	 */
	@Override
	protected Description describeChild(Runner child) {
		return child.getDescription();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.junit.runners.ParentRunner#runChild(java.lang.Object,
	 *      org.junit.runner.notification.RunNotifier)
	 */
	@Override
	protected void runChild(Runner child, RunNotifier notifier) {
		child.run(notifier);
	}

}

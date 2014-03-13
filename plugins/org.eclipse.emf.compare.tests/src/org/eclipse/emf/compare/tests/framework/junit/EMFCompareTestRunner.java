/**
 * Copyright (c) 2011, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.framework.junit;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.tests.framework.junit.annotation.UseCase;
import org.eclipse.emf.compare.tests.framework.junit.internal.UseCaseRunner;
import org.junit.Assert;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/**
 * This will be used to launch various tests on a given set of models.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class EMFCompareTestRunner extends ParentRunner<Runner> {
	/** Instantiates our runner. */
	public EMFCompareTestRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.junit.runners.BlockJUnit4ClassRunner#getChildren()
	 */
	@Override
	protected List<Runner> getChildren() {
		final List<Runner> runners = new ArrayList<Runner>();
		for (FrameworkMethod child : getTestClass().getAnnotatedMethods(UseCase.class)) {
			try {
				runners.add(new UseCaseRunner(getTestClass().getJavaClass(), child));
			} catch (InitializationError e) {
				e.printStackTrace();
				Assert.fail(e.getMessage());
			}
		}
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
	protected void runChild(Runner runner, final RunNotifier notifier) {
		runner.run(notifier);
	}
}

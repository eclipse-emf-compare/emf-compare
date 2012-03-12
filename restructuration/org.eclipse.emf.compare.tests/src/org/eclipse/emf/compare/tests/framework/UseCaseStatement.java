/**
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.framework;

import junit.framework.Assert;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * This will be used to invoke a test method with a particular use case.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class UseCaseStatement extends Statement {
	/** The method we are to invoke in order to retrieve the use case. */
	private FrameworkMethod useCase;

	/** Target of the test. */
	private Object test;

	/** The actual test method to invoke. */
	private FrameworkMethod testMethod;

	/** Indicates the direction of the merging for this runner. */
	private boolean mergeLeftToRight;

	/**
	 * Instantiates our statement given the method we are to invoke in order to retrieve the use case, and the
	 * target of the test.
	 * 
	 * @param useCaseMethod
	 *            The method we are to invoke in order to retrieve the use case.
	 * @param testObject
	 *            Target of the test.
	 * @param method
	 *            The actual test method to invoke.
	 * @param leftToRight
	 *            Indicates the direction of the merging for this runner.
	 */
	public UseCaseStatement(FrameworkMethod useCaseMethod, Object testObject, FrameworkMethod method,
			boolean leftToRight) {
		this.useCase = useCaseMethod;
		this.test = testObject;
		this.testMethod = method;
		this.mergeLeftToRight = leftToRight;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.junit.runners.model.Statement#evaluate()
	 */
	@Override
	public void evaluate() throws Throwable {
		Object couple = useCase.invokeExplosively(test);
		if (couple instanceof EObjectCouple) {
			testMethod.invokeExplosively(test, couple, Boolean.valueOf(mergeLeftToRight));
		} else {
			Assert.fail(useCase.getName() + " did not return an EObjectCouple."); //$NON-NLS-1$
		}
	}
}

/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.framework.junit.internal;

import junit.framework.Assert;

import org.eclipse.emf.compare.tests.framework.NotifierTuple;
import org.eclipse.emf.compare.tests.framework.junit.annotation.UseCase;
import org.junit.runners.model.FrameworkMethod;

/**
 * This will be used to invoke a method annotated with the {@link UseCase}
 * annotation. These are expected to return a {@link NotifierTuple} instance.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class UseCaseStatement extends ResultStatement<NotifierTuple> {
	/** The method we are to invoke in order to retrieve the use case. */
	private FrameworkMethod useCase;

	/** Target of the test. */
	private Object test;

	/** Result of the evaluation of {@link #useCase}. */
	private NotifierTuple result;

	/**
	 * Instantiates our statement given the method we are to invoke in order to
	 * retrieve the use case, and the target of the test.
	 * 
	 * @param useCaseMethod
	 *            The method we are to invoke in order to retrieve the use case.
	 * @param testObject
	 *            Target of the test.
	 */
	public UseCaseStatement(FrameworkMethod useCaseMethod, Object testObject) {
		this.useCase = useCaseMethod;
		this.test = testObject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.junit.runners.model.Statement#evaluate()
	 */
	@Override
	public void evaluate() throws Throwable {
		if (result == null) {
			final Object tuple = useCase.invokeExplosively(test);
			Assert.assertTrue(useCase.getName()
					+ " did not return a NotifierTuple.",
					tuple instanceof NotifierTuple);
			result = (NotifierTuple) tuple;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.tests.framework.junit.internal.ResultStatement#getResult()
	 */
	@Override
	public NotifierTuple getResult() {
		return result;
	}
}

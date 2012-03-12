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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * This particular implementation of a runner will be used to compute the list of methods for each use case.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class UseCaseRunner extends BlockJUnit4ClassRunner {
	/** The method we are to invoke in order to retrieve the use case. */
	private FrameworkMethod useCase;

	/** Indicates the direction of the merging for this runner. */
	private boolean mergeLeftToRight;

	/**
	 * Instantiates our runner.
	 * 
	 * @param clazz
	 *            The java class containing our tests.
	 * @param useCaseMethod
	 *            The method we are to invoke in order to retrieve the use case.
	 * @param leftToRight
	 *            Indicates the direction of the merging for this runner.
	 * @throws InitializationError
	 *             Thrown if the initialization failed somehow.
	 */
	public UseCaseRunner(Class<?> clazz, FrameworkMethod useCaseMethod, boolean leftToRight)
			throws InitializationError {
		super(clazz);
		this.useCase = useCaseMethod;
		this.mergeLeftToRight = leftToRight;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.junit.runners.ParentRunner#getName()
	 */
	@Override
	protected String getName() {
		final StringBuilder name = new StringBuilder();
		name.append(useCase.getAnnotation(UseCase.class).value());
		if (mergeLeftToRight) {
			name.append(" - left to right merging"); //$NON-NLS-1$
		} else {
			name.append(" - right to left merging"); //$NON-NLS-1$
		}
		return name.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.junit.runners.BlockJUnit4ClassRunner#computeTestMethods()
	 */
	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		final List<FrameworkMethod> methods = new ArrayList<FrameworkMethod>();
		methods.addAll(getTestClass().getAnnotatedMethods(MergeTest.class));
		return methods;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.junit.runners.BlockJUnit4ClassRunner#methodBlock(org.junit.runners.model.FrameworkMethod)
	 */
	@Override
	protected Statement methodBlock(FrameworkMethod method) {
		Object testObject = null;
		try {
			testObject = createTest();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

		return new UseCaseStatement(useCase, testObject, method, mergeLeftToRight);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.junit.runners.BlockJUnit4ClassRunner#testName(org.junit.runners.model.FrameworkMethod)
	 */
	@Override
	protected String testName(FrameworkMethod method) {
		final StringBuilder name = new StringBuilder();
		name.append(super.testName(method));
		if (mergeLeftToRight) {
			name.append("LeftToRight"); //$NON-NLS-1$
		} else {
			name.append("RightToLeft"); //$NON-NLS-1$
		}
		return name.toString();
	}
}

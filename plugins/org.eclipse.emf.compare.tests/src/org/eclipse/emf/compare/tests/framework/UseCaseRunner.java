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

import java.util.List;

import junit.framework.Assert;

import org.eclipse.emf.compare.tests.framework.annotation.BeforeMatch;
import org.eclipse.emf.compare.tests.framework.annotation.MatchTest;
import org.eclipse.emf.compare.tests.framework.annotation.UseCase;
import org.eclipse.emf.compare.tests.framework.statement.MatchStatement;
import org.eclipse.emf.compare.tests.framework.statement.ResultStatement;
import org.eclipse.emf.compare.tests.framework.statement.UseCaseStatement;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.google.common.collect.Lists;

/**
 * This particular implementation of a runner will be used to run the tests of a
 * use case.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class UseCaseRunner extends BlockJUnit4ClassRunner {
	/** The method we are to invoke in order to retrieve the use case. */
	private FrameworkMethod useCase;

	/**
	 * Instantiates our runner.
	 * 
	 * @param clazz
	 *            The java class containing our tests.
	 * @param useCaseMethod
	 *            The method we are to invoke in order to retrieve the use case.
	 * @throws InitializationError
	 *             Thrown if the initialization failed somehow.
	 */
	public UseCaseRunner(Class<?> clazz, FrameworkMethod useCaseMethod)
			throws InitializationError {
		super(clazz);
		this.useCase = useCaseMethod;
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
		return name.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.junit.runners.BlockJUnit4ClassRunner#computeTestMethods()
	 */
	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		final List<FrameworkMethod> matchTests = getTestClass()
				.getAnnotatedMethods(MatchTest.class);

		final List<FrameworkMethod> allMethods = Lists.newArrayList(matchTests);
		return allMethods;
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

		final ResultStatement<NotifierTuple> useCaseStatement = new UseCaseStatement(
				useCase, testObject);

		final Statement result;
		if (method.getAnnotation(MatchTest.class) != null) {
			final List<FrameworkMethod> befores = getTestClass()
					.getAnnotatedMethods(BeforeMatch.class);
			result = new MatchStatement(testObject, useCaseStatement, befores,
					method);
		} else {
			// TODO diff test
			result = null;
		}
		return result;
	}
}

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

import java.lang.reflect.Constructor;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.DiffBuilder;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.match.DefaultComparisonFactory;
import org.eclipse.emf.compare.match.DefaultEqualityHelperFactory;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.eobject.EditionDistance;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.IdentifierEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.ProximityEObjectMatcher;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.NotifierTuple;
import org.eclipse.emf.compare.tests.framework.junit.annotation.DiffTest;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * This implementation of a {@link Statement} allows us to call methods annotated with {@link DiffTest} on the
 * result of a Differencing process.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class DiffStatement extends Statement {
	/** Target of the test. */
	private final Object testObject;

	/**
	 * The statement that should be executed to retrieve the {@link NotifierTuple} we are to match.
	 */
	private final ResultStatement<NotifierTuple> tupleStatement;

	/** The actual test method. */
	private final FrameworkMethod test;

	/**
	 * Instantiates our statement given its target object and tuple as well as the test methods.
	 * 
	 * @param testObject
	 *            Target of the test.
	 * @param tupleStatement
	 *            The statement that should be executed to retrieve the {@link NotifierTuple} we are to match
	 *            and diff.
	 * @param test
	 *            The actual test method.
	 */
	public DiffStatement(Object testObject, ResultStatement<NotifierTuple> tupleStatement,
			FrameworkMethod test) {
		this.testObject = testObject;
		this.tupleStatement = tupleStatement;
		this.test = test;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.junit.runners.model.Statement#evaluate()
	 */
	@Override
	public void evaluate() throws Throwable {
		tupleStatement.evaluate();
		final NotifierTuple tuple = tupleStatement.getResult();

		final DiffTest annotation = test.getAnnotation(DiffTest.class);
		final IComparisonScope scope = createComparisonScope(tuple, annotation);
		final IMatchEngine matchEngine = createMatchEngine(annotation);
		final IDiffEngine diffEngine = createDiffEngine(annotation);
		final Comparison comparison = matchEngine.match(scope, new BasicMonitor());
		diffEngine.diff(comparison, new BasicMonitor());

		test.invokeExplosively(testObject, scope, comparison);
	}

	/**
	 * Creates the match engine specified by the given annotation if it has a public no-arg constructor, use
	 * the {@link DefaultMatchEngine default} otherwise.
	 * 
	 * @param annotation
	 *            The annotation on which is defined the match engine we are to use.
	 * @return An instance of the specified match engine if it has a public no-arg constructor; an instance of
	 *         the {@link DefaultMatchEngine} otherwise.
	 */
	private static IMatchEngine createMatchEngine(DiffTest annotation) {
		final Class<? extends IMatchEngine> engineClass = annotation.matchEngine();

		IMatchEngine engine = null;
		try {
			engine = engineClass.newInstance();
		} catch (InstantiationException e) {
			// Swallow : we'll create a default engine instead.
		} catch (IllegalAccessException e) {
			// Swallow : we'll create a default engine instead.
		}
		if (engine == null) {
			final IEObjectMatcher contentMatcher = new ProximityEObjectMatcher(EditionDistance.builder()
					.build());
			final IEObjectMatcher matcher = new IdentifierEObjectMatcher(contentMatcher);
			engine = new DefaultMatchEngine(matcher, new DefaultComparisonFactory(
					new DefaultEqualityHelperFactory()));
		}
		return engine;
	}

	/**
	 * Creates the diff engine specified by the given annotation if it has a public no-arg constructor, use
	 * the {@link DefaultDiffEngine default} otherwise.
	 * 
	 * @param annotation
	 *            The annotation on which is defined the diff engine we are to use.
	 * @return An instance of the specified diff engine if it has a public no-arg constructor; an instance of
	 *         the {@link DefaultDiffEngine} otherwise.
	 */
	private static IDiffEngine createDiffEngine(DiffTest annotation) {
		final Class<? extends IDiffEngine> engineClass = annotation.diffEngine();

		IDiffEngine engine = null;
		try {
			engine = engineClass.newInstance();
		} catch (InstantiationException e) {
			// Swallow : we'll create a default engine instead.
		} catch (IllegalAccessException e) {
			// Swallow : we'll create a default engine instead.
		}
		if (engine == null) {
			engine = new DefaultDiffEngine(new DiffBuilder());
		}
		return engine;
	}

	/**
	 * Creates the comparison scope specified by the given annotation if it has a public constructor taking
	 * three {@link Notifier}s as parameters. We'll return an instance of the {@link DefaultComparisonScope}
	 * otherwise.
	 * 
	 * @param tuple
	 *            The tuple for which we need a comparison scope.
	 * @param annotation
	 *            The annotation on which is specified the desired scope's class.
	 * @return An instance of the specified comparison scope it it has the expected constructor, an instance
	 *         of the {@link DefaultComparisonScope} otherwise.
	 */
	private static IComparisonScope createComparisonScope(NotifierTuple tuple, DiffTest annotation) {
		final Class<? extends IComparisonScope> scopeClass = annotation.scope();

		IComparisonScope scope = null;
		try {
			final Constructor<? extends IComparisonScope> constructor = scopeClass.getConstructor(
					Notifier.class, Notifier.class, Notifier.class);
			scope = constructor.newInstance(tuple.getLeft(), tuple.getRight(), tuple.getOrigin());
			// CHECKSTYLE:OFF invoking a constructor requires 7 catches. Since
			// we're swallowing all exceptions, we simply catch everything.
		} catch (Exception e) {
			// CHECKSTYLE:ON
			// Swallow : we'll create a default engine instead.
		}
		if (scope == null) {
			scope = new DefaultComparisonScope(tuple.getLeft(), tuple.getRight(), tuple.getOrigin());
		}
		return scope;
	}
}

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
package org.eclipse.emf.compare.tests.framework.junit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.junit.EMFCompareTestRunner;

/**
 * This annotation can be used to tell the {@link EMFCompareTestRunner} that a particular method has to be
 * called after executing the matching process on a given NotifierTuple.
 * <p>
 * Methods annotated with {@link MatchTest} should declare two arguments of type {@link IComparisonScope} and
 * {@link Comparison}.
 * </p>
 * <p>
 * This annotation accepts two additional parameters, engine and scope, which allow users to specify the
 * {@link IMatchEngine} and {@link IComparisonScope} classes that are to be used by the framework.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface MatchTest {
	/**
	 * Optionally specifies the match engine we are to use for this test. We're expecting the given class to
	 * have a public no-arg constructor.
	 */
	Class<? extends IMatchEngine> matchEngine() default DefaultMatchEngine.class;

	/**
	 * Optionally specifies the scope we are to use for this test. We're expecting the given class to have a
	 * public constructor taking three {@link Notifier}s as parameters.
	 */
	Class<? extends IComparisonScope> scope() default DefaultComparisonScope.class;
}

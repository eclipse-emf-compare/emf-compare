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

import org.eclipse.emf.compare.tests.framework.NotifierTuple;
import org.eclipse.emf.compare.tests.framework.junit.EMFCompareTestRunner;

/**
 * This annotation can be used to tell the {@link EMFCompareTestRunner} that a
 * particular method has to be called before executing the matching process on a
 * given NotifierTuple.
 * <p>
 * Methods annotated with {@link BeforeMatch} should declare a single argument
 * of type {@link NotifierTuple}.
 * </p>
 * <p>
 * Methods annotated as {@link BeforeMatch} will never be called if there are no
 * methods annotated {@link MatchTest} in the same test class.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface BeforeMatch {
	// Empty implementation
}

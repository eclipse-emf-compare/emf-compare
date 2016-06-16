/*******************************************************************************
 * Copyright (c) 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Martin Fleck - resource set hook extension for bug 495259
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * THIS ANNOTATION IS MANDATORY.
 * 
 * <pre>
 * The paths to the file we want to compare.
 * </pre>
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Compare {

	/**
	 * Path to the file used for the left side.
	 * 
	 * @return the value
	 */
	String left();

	/**
	 * Path to the file used for the right side.
	 * 
	 * @return the value
	 */
	String right();

	/**
	 * Path to the file used for the ancestor side. If set, the comparison will be 3-way, 2-way otherwise.
	 * 
	 * @return the value or an empty String if not used
	 */
	String ancestor() default "";

	/**
	 * Classes of resource set hooks to consider during resource loading.
	 */
	Class<?>[] resourceSetHooks() default {};
}

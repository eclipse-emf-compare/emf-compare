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
package org.eclipse.emf.compare.ide.ui.tests.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to select the post processors disabled for the test class. Each test will be run with the
 * given list as disabled post processors. The other post processors will be activated for the tests.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DisabledPostProcessors {

	/**
	 * The list of post processors disabled for the test. If the annotation is used empty, the default array
	 * will be returned. If the annotation is not used the post processors defined in a default array in the
	 * class EMFCompareGitTestRunner will be used.
	 * 
	 * @return the value or the default array if user specify nothing
	 */
	Class<?>[] value() default {};

}

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
package org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitMergeStrategyID;

/**
 * Annotation used to select the merge strategy used for the test class.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GitMergeStrategy {

	/**
	 * The merge strategy used for all tests in the class. If the annotation is used empty, the default value
	 * will be returned. If the annotation is not used the merge strategy defined in the class
	 * EMFCompareGitTestRunner will be used.
	 */
	GitMergeStrategyID value() default GitMergeStrategyID.MODEL_RECURSIVE;

}

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

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.internal.GitTestSupport;

/**
 * Annotation used to launch a comparison on a git repository.
 * 
 * <pre>
 * The signature of the test method must be:
 * public void doTest({@link Comparison} comparison) {}
 * 
 * If you want to be able to perform extra manipulation on the repository in your 
 * test case (merge, checkout, comparison), the signature of the method must be:
 * public void doTest({@link Comparison} comparison, 
 * 	{@link GitTestSupport} support) {}
 * </pre>
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GitCompare {

	String localBranch();

	String remoteBranch();

	/**
	 * The path of the file to compare. The path must be project relative
	 */
	String fileToCompare();

	/**
	 * This value is optional. If not used, the runner will assume that only one project is contained in the
	 * repository. If used, the value will be used to get the project containing the file to compare.
	 */
	String containerProject() default GitTestSupport.COMPARE_NO_PROJECT_SELECTED;

}

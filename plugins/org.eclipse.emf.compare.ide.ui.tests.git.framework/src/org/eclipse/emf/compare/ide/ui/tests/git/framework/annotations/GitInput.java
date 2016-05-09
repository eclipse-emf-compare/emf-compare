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

/**
 * THIS ANNOTATION IS MANDATORY.
 * 
 * <pre>
 * The path of the archive containing a git repository used for the tests. The repository must contains at
 * least one project.
 * 
 * The preferred structure of the git repository is:
 * - rootFolder/
 *   - .git/
 *   - projectFolder/
 *     - .project
 *     - content of the project
 *     
 * This structure of the git repository is supported to but it is a bad practice:
 * - rootFolder/
 *   - .git/
 *   - .project
 *   - content of the project
 * </pre>
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GitInput {

	/**
	 * The path to the zip archive. The path must be relative to the test class.
	 * 
	 * @return the path
	 */
	String value();

}

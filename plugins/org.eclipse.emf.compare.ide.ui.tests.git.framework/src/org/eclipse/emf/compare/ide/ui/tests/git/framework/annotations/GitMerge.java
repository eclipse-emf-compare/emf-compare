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

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.internal.GitTestSupport;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Repository;

/**
 * Annotation used to test the merge of models.
 * 
 * <pre>
 * The signature of the test method must be:
 * public void doTest({@link Status} status, {@link Repository} repository, 
 * 	List<{@link IProject}> projects) {}
 * 
 * If you want to be able to perform extra manipulation on the repository in your 
 * test case (merge, checkout, comparison), the signature of the method must be:
 * public void doTest({@link Status} status, {@link Repository} repository, 
 * 	List<{@link IProject}> projects, {@link GitTestSupport} support) {}
 * </pre>
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GitMerge {

	String localBranch();

	String remoteBranch();

}

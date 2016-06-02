/*******************************************************************************
 * Copyright (c) 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - support more flexible parameters of test methods
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.internal.GitTestSupport;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Repository;

/**
 * Annotation used to test the merge of models.
 * <p>
 * The test method may take the following arguments in an arbitrary order. Note that you also may only accept
 * a subset of these in the signature of the test method:
 * </p>
 * <ul>
 * <li>{@link Status}</li>
 * <li>{@link Repository}</li>
 * <li>List<{@link IProject}></li>
 * <li>{@link MergeResult}</li>
 * <li>{@link GitTestSupport}</li>
 * </ul>
 * 
 * <pre>
 * For instance, the signature of the test method may be:
 * public void doTest({@link Status} status, {@link Repository} repository, 
 * 	List<{@link IProject}> projects) {}
 * 
 * If you want to be able to perform extra manipulation on the repository in your 
 * test case (merge, checkout, comparison), you can take the {@link GitTestSupport}
 * as a parameter:
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

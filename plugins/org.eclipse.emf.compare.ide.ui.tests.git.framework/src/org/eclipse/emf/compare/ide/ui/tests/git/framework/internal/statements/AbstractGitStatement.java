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
package org.eclipse.emf.compare.ide.ui.tests.git.framework.internal.statements;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.compare.ide.ui.tests.framework.AbstractCompareStatement;
import org.eclipse.emf.compare.ide.ui.tests.framework.EMFCompareTestConfiguration;
import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitTestSupport;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitCherryPick;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitMerge;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitRebase;
import org.eclipse.jgit.api.CherryPickResult;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.RebaseResult;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Repository;
import org.junit.runners.model.FrameworkMethod;

/**
 * EMFCompare specific Git statements must extends this class.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public abstract class AbstractGitStatement extends AbstractCompareStatement {

	/**
	 * Constructor for the classic (no Git) comparison statement.
	 * 
	 * @param testObject
	 *            The test class
	 * @param test
	 *            The test method
	 * @param resolutionStrategy
	 *            The resolution strategy used for this test
	 * @param configuration
	 *            EMFCompare configuration for this test
	 */
	public AbstractGitStatement(Object testObject, FrameworkMethod test,
			ResolutionStrategyID resolutionStrategy, EMFCompareTestConfiguration configuration) {
		super(testObject, test, resolutionStrategy, configuration);
	}

	/**
	 * Compute the list of parameters to give to the test method. The parameters are returned in the correct
	 * order.
	 * 
	 * @param method
	 *            The test method
	 * @param gitTestsSupport
	 *            The support class used for Git operations
	 * @return the list of parameters for the method
	 * @throws Exception
	 *             If the method use an incorrect parameter
	 */
	protected Object[] createParameters(Method method, GitTestSupport gitTestsSupport) throws Exception {
		final Builder<Object> builder = ImmutableList.builder();
		Class<?>[] paramTypes = method.getParameterTypes();
		for (Class<?> paramType : paramTypes) {
			if (paramType.equals(GitTestSupport.class)) {
				builder.add(gitTestsSupport);
			} else if (paramType.equals(MergeResult.class) && method.getAnnotation(GitMerge.class) != null) {
				builder.add(gitTestsSupport.getMergeResult());
			} else if (paramType.equals(RebaseResult.class)
					&& method.getAnnotation(GitRebase.class) != null) {
				builder.add(gitTestsSupport.getRebaseResult());
			} else if (paramType.equals(CherryPickResult.class)
					&& method.getAnnotation(GitCherryPick.class) != null) {
				builder.add(gitTestsSupport.getCherryPickResult());
			} else if (isCollectionCompatible(paramType)) {
				builder.add(gitTestsSupport.getProjects());
			} else if (paramType.equals(Repository.class)) {
				builder.add(gitTestsSupport.getRepository());
			} else if (paramType.equals(Status.class)) {
				builder.add(gitTestsSupport.getStatus());
			} else {
				throw new IllegalArgumentException(
						"Unsupported parameter type " + paramType.getCanonicalName()); //$NON-NLS-1$
			}
		}
		return builder.build().toArray();
	}

	/**
	 * Test if the given type is some kind of Collection.
	 * 
	 * @param paramType
	 *            The Class to test
	 * @return <code>true</code> if the given parameter is a Collection
	 */
	private boolean isCollectionCompatible(Class<?> paramType) {
		return paramType.equals(Collection.class) || paramType.equals(List.class)
				|| paramType.equals(Iterable.class);
	}

}

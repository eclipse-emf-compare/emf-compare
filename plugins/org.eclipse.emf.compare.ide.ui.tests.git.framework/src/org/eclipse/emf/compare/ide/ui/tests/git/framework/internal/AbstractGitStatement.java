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
package org.eclipse.emf.compare.ide.ui.tests.git.framework.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.compare.ide.ui.tests.framework.AbstractCompareStatement;
import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.framework.EMFCompareTestConfiguration;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Repository;
import org.junit.runners.model.FrameworkMethod;

public abstract class AbstractGitStatement extends AbstractCompareStatement {

	public AbstractGitStatement(Object testObject, FrameworkMethod test,
			ResolutionStrategyID resolutionStrategy, EMFCompareTestConfiguration configuration) {
		super(testObject, test, resolutionStrategy, configuration);
	}

	protected Object[] createParameters(Class<?>[] paramTypes, GitTestSupport gitTestsSupport)
			throws Throwable {
		final Builder<Object> builder = ImmutableList.builder();
		for (Class<?> paramType : paramTypes) {
			if (paramType.equals(GitTestSupport.class)) {
				builder.add(gitTestsSupport);
			} else if (paramType.equals(MergeResult.class)) {
				builder.add(gitTestsSupport.getMergeResult());
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

	private boolean isCollectionCompatible(Class<?> paramType) {
		return paramType.equals(Collection.class) || paramType.equals(List.class)
				|| paramType.equals(Iterable.class);
	}

}

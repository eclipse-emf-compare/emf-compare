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

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.GitCorePreferences;
import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.framework.EMFCompareTestConfiguration;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.MergeStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitMerge;
import org.junit.runners.model.FrameworkMethod;

/**
 * This class handle all merge tests.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
@SuppressWarnings("restriction")
public class MergeStatement extends AbstractGitStatement {

	private final MergeStrategyID mergeStrategy;

	private final IEclipsePreferences eGitPreferences;

	private final String path;

	private String defaultMergeStrategy = MergeStrategyID.MODEL_RECURSIVE.getValue();

	public MergeStatement(Object testObject, FrameworkMethod test, ResolutionStrategyID resolutionStrategy,
			EMFCompareTestConfiguration configuration, MergeStrategyID mergeStrategy, String path) {
		super(testObject, test, resolutionStrategy, configuration);
		this.mergeStrategy = mergeStrategy;
		this.eGitPreferences = InstanceScope.INSTANCE.getNode(Activator.getPluginId());
		this.path = normalizePath(path);
	}

	@Override
	public void evaluate() throws Throwable {
		setEMFComparePreferences();
		GitMerge merge = test.getAnnotation(GitMerge.class);
		String localBranch = GitTestSupport.normalizeBranch(merge.localBranch());
		String remoteBranch = GitTestSupport.normalizeBranch(merge.remoteBranch());

		GitTestSupport gitTestsSupport = new GitTestSupport();
		try {
			gitTestsSupport.setup();
			gitTestsSupport.createRepositoryFromPath(test.getMethod().getDeclaringClass(), path);
			gitTestsSupport.merge(localBranch, remoteBranch);
			Class<?>[] paramTypes = test.getMethod().getParameterTypes();
			Object[] parameters = createParameters(paramTypes, gitTestsSupport);
			test.invokeExplosively(testObject, parameters);
		} finally {
			restoreEMFComparePreferences();
			gitTestsSupport.tearDown();
		}
	}

	@Override
	protected void setEMFComparePreferences() {
		super.setEMFComparePreferences();
		setMergeStrategyPreference();
	}

	@Override
	protected void restoreEMFComparePreferences() {
		super.restoreEMFComparePreferences();
		eGitPreferences.put(GitCorePreferences.core_preferredMergeStrategy, defaultMergeStrategy);
	}

	private void setMergeStrategyPreference() {
		defaultMergeStrategy = eGitPreferences.get(GitCorePreferences.core_preferredMergeStrategy,
				defaultMergeStrategy);
		eGitPreferences.put(GitCorePreferences.core_preferredMergeStrategy, mergeStrategy.getValue());
	}

}

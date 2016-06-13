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

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.GitCorePreferences;
import org.eclipse.emf.compare.ide.ui.tests.framework.EMFCompareTestConfiguration;
import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitMergeStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitTestSupport;
import org.junit.runners.model.FrameworkMethod;

/**
 * This class handle all test related to git operations (merge, rebase, cherry-pick).
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
@SuppressWarnings("restriction")
public abstract class AbstractGitOperationStatement extends AbstractGitStatement {

	private final GitMergeStrategyID mergeStrategy;

	private final IEclipsePreferences eGitPreferences;

	protected final String path;

	private String defaultMergeStrategy = GitMergeStrategyID.MODEL_RECURSIVE.getValue();

	public AbstractGitOperationStatement(Object testObject, FrameworkMethod test,
			ResolutionStrategyID resolutionStrategy, EMFCompareTestConfiguration configuration,
			GitMergeStrategyID mergeStrategy, String path) {
		super(testObject, test, resolutionStrategy, configuration);
		this.mergeStrategy = mergeStrategy;
		this.eGitPreferences = InstanceScope.INSTANCE.getNode(Activator.getPluginId());
		this.path = normalizePath(path);
	}

	@Override
	public void evaluate() throws Throwable {
		setEMFComparePreferences();
		String from = InternalGitTestSupport.normalizeBranch(getCheckoutedBranch());
		String to = InternalGitTestSupport.normalizeBranch(getOtherBranch());

		GitTestSupport gitTestsSupport = new GitTestSupport();
		try {
			gitTestsSupport.setup();
			gitTestsSupport.createRepositoryFromPath(test.getMethod().getDeclaringClass(), path);
			callGitOperation(gitTestsSupport, from, to);
			Object[] parameters = createParameters(test.getMethod(), gitTestsSupport);
			test.invokeExplosively(testObject, parameters);
		} finally {
			restoreEMFComparePreferences();
			gitTestsSupport.tearDown();
		}
	}

	protected abstract String getCheckoutedBranch();

	protected abstract String getOtherBranch();

	protected abstract void callGitOperation(GitTestSupport gitTestsSupport, String from, String to)
			throws Throwable;

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

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

import static org.junit.Assert.fail;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.GitCorePreferences;
import org.eclipse.emf.compare.ide.ui.tests.framework.EMFCompareTestConfiguration;
import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitMergeStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitTestSupport;
import org.junit.Test.None;
import org.junit.runners.model.FrameworkMethod;

/**
 * This class handle all test related to git operations (merge, rebase, cherry-pick).
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
@SuppressWarnings("restriction")
public abstract class AbstractGitOperationStatement extends AbstractGitStatement {

	/** The path of the archive containing the repository. */
	protected final String path;

	/** The merge strategy used for the test. */
	private final GitMergeStrategyID mergeStrategy;

	/** The EGit preferences. */
	private final IEclipsePreferences eGitPreferences;

	/** The default merge strategy to use if none is provided by user. */
	private String defaultMergeStrategy = GitMergeStrategyID.MODEL_RECURSIVE.getValue();

	/**
	 * Constructor for Git comparison statements.
	 * 
	 * @param testObject
	 *            The test class
	 * @param test
	 *            The test method
	 * @param resolutionStrategy
	 *            The resolution strategy used for this test
	 * @param configuration
	 *            EMFCompare configuration for this test
	 * @param mergeStrategy
	 *            The merge strategy used for the test
	 * @param path
	 *            The path of the archive containing the repository
	 */
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
		Class<? extends Throwable> expected = getExpectedException();

		GitTestSupport gitTestsSupport = new GitTestSupport();
		try {
			gitTestsSupport.setup();
			gitTestsSupport.createRepositoryFromPath(test.getMethod().getDeclaringClass(), path);
			callGitOperation(gitTestsSupport, from, to);
			Object[] parameters = createParameters(test.getMethod(), gitTestsSupport);
			test.invokeExplosively(testObject, parameters);
			if (expected != None.class) {
				fail("Expected exception : " + expected.getName()); //$NON-NLS-1$
			}
		} catch (Throwable e) {
			if (expected.isAssignableFrom(e.getClass())) {
				// success
			} else {
				throw e;
			}
		} finally {
			restoreEMFComparePreferences();
			gitTestsSupport.tearDown();
		}
	}

	/**
	 * Get the branch that will be used as local branch for the test. This method have to be sub-classed by
	 * clients to properly get the branch.
	 * 
	 * @return the local branch
	 */
	protected abstract String getCheckoutedBranch();

	/**
	 * Get the branch that will be used as remote branch for the test. This method have to be sub-classed by
	 * clients to properly get the branch..
	 * 
	 * @return the remote branch
	 */
	protected abstract String getOtherBranch();

	/**
	 * Returns the exception that is expected to be thrown by this test.
	 * 
	 * @return The exception that is expected to be thrown by this test.
	 */
	protected Class<? extends Throwable> getExpectedException() {
		return None.class;
	}

	/**
	 * Call the Git operation used in for the test. This method have to be sub-classed by clients to call the
	 * correct operation.
	 * 
	 * @param gitTestsSupport
	 *            The support class that allow to perform Git operations
	 * @param from
	 *            The local branch
	 * @param to
	 *            The remote branch
	 * @throws Exception
	 *             Thrown if a Git operation have an issue during execution
	 */
	protected abstract void callGitOperation(GitTestSupport gitTestsSupport, String from, String to)
			throws Exception;

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

	/**
	 * Set the merge strategy preference to run the test in the correct conditions.
	 */
	private void setMergeStrategyPreference() {
		defaultMergeStrategy = eGitPreferences.get(GitCorePreferences.core_preferredMergeStrategy,
				defaultMergeStrategy);
		eGitPreferences.put(GitCorePreferences.core_preferredMergeStrategy, mergeStrategy.getValue());
	}

}

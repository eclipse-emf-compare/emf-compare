/*******************************************************************************
 * Copyright (C) 2016 Obeo and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.merge;

import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Iterables.all;
import static org.eclipse.emf.compare.ConflictKind.PSEUDO;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasDirectOrIndirectConflict;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.isInRealAddAddConflict;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ide.EMFCompareIDEPlugin;
import org.eclipse.emf.compare.ide.logical.IModelInclusionTester;
import org.eclipse.emf.compare.ide.logical.ModelInclusionTesterRegistry;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitMergeStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitTestRunner;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitTestSupport;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitInput;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitMerge;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitMergeStrategy;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Repository;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(GitTestRunner.class)
@GitMergeStrategy(GitMergeStrategyID.MODEL_ADDITIVE)
@SuppressWarnings({"nls", "unused" })
public class AdditiveMergeTests {

	@Before
	public void setupModelInclusionTesterRegistry() {
		EMFCompareIDEPlugin idePlugin = EMFCompareIDEPlugin.getDefault();
		ModelInclusionTesterRegistry registry = idePlugin.getModelInclusionTesterRegistry();
		registry.clear();
		addFileExtensionTester(registry, "ecore");
		addFileExtensionTester(registry, "uml");
	}

	private void addFileExtensionTester(ModelInclusionTesterRegistry registry, final String key) {
		registry.add(key, new IModelInclusionTester() {
			public boolean shouldInclude(IFile file) {
				return key.equals(file.getFileExtension());
			}
		});
	}

	@GitMerge(local = "branch1", remote = "branch2")
	@GitInput("data/additive/ecore.zip")
	public void testAdditiveMergeEcore1(Status status, Repository repository, List<IProject> projects,
			GitTestSupport support) throws Exception {
		assertFalse(status.hasUncommittedChanges());
		assertEquals(0, status.getConflicting().size());

		Comparison comparison = support.compare("branch1", "expected", "network.ecore");

		assertTrue(all(comparison.getDifferences(),
				or(hasDirectOrIndirectConflict(PSEUDO), isInRealAddAddConflict())));
	}

	@GitMerge(local = "branch2", remote = "branch1")
	@GitInput("data/additive/ecore.zip")
	public void testAdditiveMergeEcore2(Status status, Repository repository, List<IProject> projects,
			GitTestSupport support) throws Exception {
		assertFalse(status.hasUncommittedChanges());
		assertEquals(0, status.getConflicting().size());

		Comparison comparison = support.compare("branch2", "expected", "network.ecore");

		assertTrue(all(comparison.getDifferences(),
				or(hasDirectOrIndirectConflict(PSEUDO), isInRealAddAddConflict())));
	}

	@GitMerge(local = "branch1", remote = "branch2")
	@GitInput("data/additive/uml.zip")
	public void testAdditiveMergeUml1(Status status, Repository repository, List<IProject> projects,
			GitTestSupport support) throws Exception {
		assertFalse(status.hasUncommittedChanges());
		assertEquals(0, status.getConflicting().size());

		Comparison comparison = support.compare("branch1", "expected", "network.uml");

		assertTrue(all(comparison.getDifferences(),
				or(hasDirectOrIndirectConflict(PSEUDO), isInRealAddAddConflict())));
	}

	@GitMerge(local = "branch2", remote = "branch1")
	@GitInput("data/additive/uml.zip")
	public void testAdditiveMergeUml2(Status status, Repository repository, List<IProject> projects,
			GitTestSupport support) throws Exception {
		assertFalse(status.hasUncommittedChanges());
		assertEquals(0, status.getConflicting().size());

		Comparison comparison = support.compare("branch2", "expected", "network.uml");

		assertTrue(all(comparison.getDifferences(),
				or(hasDirectOrIndirectConflict(PSEUDO), isInRealAddAddConflict())));
	}
}

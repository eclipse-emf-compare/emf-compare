/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Munich and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexandra Buzila - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jgit.api.Status;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Merge tests for resources from a git repository that are not necessarily part of a logical model.
 * 
 * @author Alexandra Buzila
 */
@SuppressWarnings({"nls", "restriction", })
public class GitMergeTest extends AbstractGitLogicalModelTest {

	private final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault().getPreferenceStore();

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		// The test need to have the pre-merge preference set to true
		store.setValue(EMFCompareUIPreferences.PRE_MERGE_MODELS_WHEN_CONFLICT, true);
	}

	@Override
	@After
	public void tearDown() throws Exception {
		super.tearDown();

		// Reset pre-merge default value to the classic one
		store.setToDefault(EMFCompareUIPreferences.PRE_MERGE_MODELS_WHEN_CONFLICT);
	}

	/**
	 * Sets up a repository with three commits on two branches:
	 * <ul>
	 * <li>initial commit: An ecore containing a package P1 and a class C1.</li>
	 * <li>MASTER commit: updates C1 (name changed to C1new) and adds a new class C2.</li>
	 * <li>BRANCH commit: deletes C1 and adds a new class C3.</li>
	 * </ul>
	 * <p>
	 * Checked out is MASTER and the branch BRANCH is to be merged in MASTER. The merge should not finish due
	 * to conflicts, but the non-conflicting differences should be successfully merged (classes C2 and C3
	 * should both be present in the model after the merge).
	 * </p>
	 * 
	 * @throws Exception
	 *             if something went wrong during the setup.
	 */
	private void setup001() throws Exception {

		// initial commit
		EPackage ePackage = createPackage(null, "P1");
		EClass class1 = createClass(ePackage, "C1");
		resource1.getContents().add(ePackage);
		save(resource1);
		repository.addAllAndCommit("initial-commit");

		// create new branch, but stay on MASTER
		repository.createBranch(MASTER, BRANCH);

		// master commit
		createClass(ePackage, "C2");
		class1.setName("C1new");
		save(resource1);
		repository.addAllAndCommit("update-C1-add-C2");

		// branch commit
		repository.checkoutBranch(BRANCH);
		reload(resource1);
		ePackage = (EPackage)findObject(resource1, "P1");
		class1 = (EClass)findObject(resource1, "C1");
		ePackage.getEClassifiers().remove(class1);
		createClass(ePackage, "C3");
		save(resource1);
		repository.addAllAndCommit("remove-C1-add-C3");

		// back on master
		repository.checkoutBranch(MASTER);
		reload(resource1);
	}

	@Test
	public void merge001() throws Exception {
		setup001();

		repository.mergeLogical(BRANCH);
		reload(resource1);

		Status status = repository.status();
		Set<String> conflicting = status.getConflicting();
		assertEquals(1, conflicting.size());
		assertNotNull(findObject(resource1, "C1new"));
		assertNotNull(findObject(resource1, "C2"));
		assertNotNull(findObject(resource1, "C3"));

	}
}

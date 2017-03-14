/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.merge;

import static org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences.PRE_MERGE_MODELS_WHEN_CONFLICT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.EMFResourceMappingMerger;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitTestRunner;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitInput;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitMerge;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jgit.api.Status;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * Test cases covering the pre-merge scenario of the {@link EMFResourceMappingMerger}.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
@SuppressWarnings({"restriction", "nls" })
@RunWith(GitTestRunner.class)
public class EMFResourceMappingMergerPreMergeTest {

	/** Preference store for the pre-merge setting. */
	protected static final IPreferenceStore EMF_COMPARE_PREFS = EMFCompareIDEUIPlugin.getDefault()
			.getPreferenceStore();

	/** Cached pre-merge setting in the preference store. */
	protected static boolean PRE_MERGE_CONFLICT_SETTING;

	@BeforeClass
	public static void setupClass() {
		// turn pre-merge on
		PRE_MERGE_CONFLICT_SETTING = EMF_COMPARE_PREFS.getBoolean(PRE_MERGE_MODELS_WHEN_CONFLICT);
		EMF_COMPARE_PREFS.setValue(PRE_MERGE_MODELS_WHEN_CONFLICT, true);
	}

	@AfterClass
	public static void tearDownClass() {
		// restore pre-merge setting
		EMFCompareIDEUIPlugin.getDefault().getPreferenceStore().setValue(PRE_MERGE_MODELS_WHEN_CONFLICT,
				PRE_MERGE_CONFLICT_SETTING);
	}

	/**
	 * Tests that we have the expected files exist when performing a pre-merge with a real conflict. The test
	 * model looks as follows:
	 * <ul>
	 * <li>master: One class diagram (model) with a single class 'ClassA'</li>
	 * <li>renameClassA: Rename of existing class to 'ClassB'</li>
	 * <li>renameClassA_addOtherModel: Rename of existing class to 'ClassC' (REAL conflict) and addition of
	 * other model (otherModel) which imports ClassC into the diagram (-> part of logical model)</li>
	 * </ul>
	 * The expected result is:
	 * <ul>
	 * <li>Projects: Only the single project containing the models is involved</li>
	 * <li>Conflicts: model.uml has a rename conflict of 'ClassA' to 'ClassB' or 'ClassC'</li>
	 * <li>PreMerge: otherModel can be merged as there is no conflict</li>
	 * </ul>
	 * 
	 * @param status
	 *            git status
	 * @param projects
	 *            list of involved projects
	 */
	@GitInput("data/premerge/bug_preMergeWithConflict.zip")
	@GitMerge(local = "renameClassA", remote = "renameClassA_addOtherModel")
	public void testPreMergeWithConflictL2R(Status status, List<IProject> projects) {
		IProject project = assertSingleProject(projects);
		assertFilesExist(project, "model.uml", "model.notation", "model.di", "otherModel.uml",
				"otherModel.notation", "otherModel.di");
		assertFilesConflict(status, "PreMergeWithConflict/model.uml");
	}

	/**
	 * Tests that we have the expected files exist when performing a pre-merge with a real conflict. The test
	 * model looks as follows:
	 * <ul>
	 * <li>master: One class diagram (model) with a single class 'ClassA'</li>
	 * <li>renameClassA: Rename of existing class to 'ClassB'</li>
	 * <li>renameClassA_addOtherModel: Rename of existing class to 'ClassC' (REAL conflict) and addition of
	 * other model (otherModel) which imports ClassC into the diagram (-> part of logical model)</li>
	 * </ul>
	 * The expected result is:
	 * <ul>
	 * <li>Projects: Only the single project containing the models is involved</li>
	 * <li>Conflicts: model.uml has a rename conflict of 'ClassA' to 'ClassB' or 'ClassC'</li>
	 * <li>PreMerge: otherModel can be merged as there is no conflict</li>
	 * </ul>
	 * 
	 * @param status
	 *            git status
	 * @param projects
	 *            list of involved projects
	 */
	@GitInput("data/premerge/bug_preMergeWithConflict.zip")
	@GitMerge(local = "renameClassA_addOtherModel", remote = "renameClassA")
	public void testPreMergeWithConflictR2L(Status status, List<IProject> projects) {
		IProject project = assertSingleProject(projects);
		assertFilesExist(project, "model.uml", "model.notation", "model.di", "otherModel.uml",
				"otherModel.notation", "otherModel.di");
		assertFilesConflict(status, "PreMergeWithConflict/model.uml");
	}

	/**
	 * Asserts that there is a single project and returns it.
	 * 
	 * @param projects
	 *            projects
	 * @return single project
	 */
	protected IProject assertSingleProject(List<IProject> projects) {
		assertEquals(1, projects.size());
		return projects.get(0);
	}

	/**
	 * Asserts that all given files exist in the given project.
	 * 
	 * @param project
	 *            project
	 * @param files
	 *            files that need to exist in the project
	 */
	protected void assertFilesExist(IProject project, String... files) {
		for (String file : files) {
			assertTrue(file + " does not exist.", project.getFile(file).exists());
		}
	}

	/**
	 * Asserts that the all and only the given files are conflicting.
	 * 
	 * @param status
	 *            git status
	 * @param files
	 *            files that should be conflicting
	 */
	protected void assertFilesConflict(Status status, String... files) {
		Set<String> conflicting = status.getConflicting();
		assertEquals(files.length, conflicting.size());
		for (String file : files) {
			assertTrue(file + " is not conflicting.", conflicting.contains(file));
		}
	}
}

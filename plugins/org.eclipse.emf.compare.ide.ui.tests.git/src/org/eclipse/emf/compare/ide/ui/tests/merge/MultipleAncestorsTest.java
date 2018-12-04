package org.eclipse.emf.compare.ide.ui.tests.merge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitTestRunner;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitInput;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitMerge;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@RunWith(GitTestRunner.class)
@SuppressWarnings({"nls", })
public class MultipleAncestorsTest {

	/**
	 * Three people modifying two 4 models: 1 model for each person and 1 "index" model that links the 4
	 * together.
	 * <p>
	 * Each person is modifying a single ecore file, everyone on their own branch. However, they're all
	 * merging to and from the same branch. There should never be a conflicting merge, but there will be
	 * multiple ancestors for the merges.
	 * </p>
	 */
	@Ignore
	@GitInput("data/multipleancestors/complex.merge.zip")
	@GitMerge(local = "assemblage", remote = "arthur")
	public void testComplexMerge(Status status, List<IProject> projects) {
		assertEquals(1, projects.size());
		assertFilesExist(projects.get(0), "index.ecore", "Arthur.ecore", "Laurent.ecore", "Stephane.ecore");
		assertTrue(status.getConflicting().isEmpty());
		assertTrue(status.isClean());
	}

	/**
	 * Two people modifying two models in their own branch, merging to and from master.
	 * <p>
	 * The two persons are modifying the same model files, but they're only creating "textual" conflicts (as
	 * opposed to model conflicts). These are automatically mergeable through EMF Compare and this merge
	 * should just finish correctly.
	 * </p>
	 */
	@Ignore
	@GitInput("data/multipleancestors/library.binaryecore.multiple.ancestors.conflicts.zip")
	@GitMerge(local = "master", remote = "Stephane")
	public void testLibraryTextualConflicts(Status status, List<IProject> projects) {
		assertEquals(1, projects.size());
		assertFilesExist(projects.get(0), "library.ecore", "types.ecore");
		assertTrue(status.getConflicting().isEmpty());
		assertTrue(status.isClean());
	}

	/**
	 * Two people modifying two models in their own branch, merging to and from master, and using an
	 * "assembly" branch to test before merging towards master.
	 * <p>
	 * The two persons have created a conflict in a previous merge, and have merged in such a way that there
	 * is now a conflict in the two commits that need to be used to create a base ancestor. JGit cannot handle
	 * that case and the merge is impossible.
	 * </p>
	 */
	@GitInput("data/multipleancestors/conflict.base.ancestor.zip")
	@GitMerge(local = "assemblage", remote = "arthur", expected = JGitInternalException.class)
	public void testModelAndTextualConflictInBaseCommits(Status status, List<IProject> projects) {
		assertEquals(1, projects.size());
		assertFilesExist(projects.get(0), "library.uml", "types.uml");
		assertTrue(status.getConflicting().isEmpty());
		assertTrue(status.isClean());
	}

	/**
	 * Two people modifying two models in their own branch, merging to and from master.
	 * <p>
	 * The two persons are modifying different files and will not have conflictual differences with each
	 * other. This merge will go correctly.
	 * </p>
	 */
	@Ignore
	@GitInput("data/multipleancestors/library.binaryecore.multiple.ancestors.no.conflict.different.files.zip")
	@GitMerge(local = "master", remote = "Stephane")
	public void testLibraryNoConflicts(Status status, List<IProject> projects) {
		assertEquals(1, projects.size());
		assertFilesExist(projects.get(0), "library.ecore", "types.ecore");
		assertTrue(status.getConflicting().isEmpty());
		assertTrue(status.isClean());
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

}

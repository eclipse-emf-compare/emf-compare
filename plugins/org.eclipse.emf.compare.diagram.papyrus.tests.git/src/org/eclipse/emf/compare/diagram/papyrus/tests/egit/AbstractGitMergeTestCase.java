/*******************************************************************************
 * Copyright (C) 2015 EclipseSource Munich Gmbh and Others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.egit;

import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.mapping.IModelProviderDescriptor;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.GitCorePreferences;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diagram.papyrus.tests.egit.fixture.GitTestRepository;
import org.eclipse.emf.compare.diagram.papyrus.tests.egit.fixture.MockSystemReader;
import org.eclipse.emf.compare.ide.EMFCompareIDEPlugin;
import org.eclipse.emf.compare.ide.logical.IModelInclusionTester;
import org.eclipse.emf.compare.ide.logical.ModelInclusionTesterRegistry;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.EMFModelProvider;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.emf.compare.ide.ui.tests.workspace.TestProject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.util.FileUtils;
import org.eclipse.jgit.util.SystemReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

/**
 * Abstract test case to assess the results of merging, rebasing, and cherry-picking of a particular merge
 * scenario.
 * <p>
 * This abstract test case sets up the branches <em>left</em> and <em>right</em> with projects and models of a
 * given directory specified by subclasses of this test case. Then it performs a merge, rebase, and
 * cherry-pick in both directions and, for each case, calls the subclass to validate the result.
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
@SuppressWarnings({"nls", "unused", "restriction" })
public abstract class AbstractGitMergeTestCase {

	protected static final String DEFAULT_PROJECT = "Project1";

	protected static final String TEST_BUNDLE = "org.eclipse.emf.compare.diagram.papyrus.tests.git";

	protected static final String MASTER_BRANCH = Constants.R_HEADS + Constants.MASTER;

	protected static final String BRANCH_LEFT = Constants.R_HEADS + "branch_left";

	protected static final String BRANCH_RIGHT = Constants.R_HEADS + "branch_right";

	private static final Predicate<File> IS_EXISTING_FILE = new Predicate<File>() {
		@Override
		public boolean apply(File input) {
			return input != null && input.exists() && input.isFile();
		}
	};

	protected static String defaultResolutionScope;

	protected GitTestRepository repository;

	protected File gitDir;

	@BeforeClass
	public static void setUpClass() {
		// suppress auto-ignoring and auto-sharing to avoid interference
		final IEclipsePreferences eGitPreferences = InstanceScope.INSTANCE.getNode(Activator.getPluginId());
		eGitPreferences.put(GitCorePreferences.core_preferredMergeStrategy, "model recursive");
		eGitPreferences.putBoolean(GitCorePreferences.core_autoShareProjects, false);
		// This is actually the value of
		// "GitCorePreferences.core_autoIgnoreDerivedResources"... but it was
		// not in Egit 2.1
		eGitPreferences.putBoolean("core_autoIgnoreDerivedResources", false);
		final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault().getPreferenceStore();
		defaultResolutionScope = store.getString(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE);
		store.setValue(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE, getResolutionScope().name());
	}

	@AfterClass
	public static void tearDownClass() {
		final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault().getPreferenceStore();
		store.setValue(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE, defaultResolutionScope);
	}

	@Before
	public void setUp() throws Exception {
		Activator.getDefault().getRepositoryCache().clear();
		final MockSystemReader mockSystemReader = new MockSystemReader();
		SystemReader.setInstance(mockSystemReader);
		final IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		final String gitRepoPath = workspaceRoot.getRawLocation().toFile() + File.separator + "repo";
		mockSystemReader.setProperty(Constants.GIT_CEILING_DIRECTORIES_KEY,
				workspaceRoot.getLocation().toFile().getParentFile().getAbsoluteFile().toString());
		gitDir = new File(gitRepoPath, Constants.DOT_GIT);
		repository = new GitTestRepository(gitDir);
		repository.ignore(workspaceRoot.getRawLocation().append(".metadata").toFile());
		setUpRepository();
	}

	@After
	public void tearDown() throws Exception {
		final IModelProviderDescriptor modelProviderDesc = ModelProvider
				.getModelProviderDescriptor(EMFModelProvider.PROVIDER_ID);
		final EMFModelProvider emfModelProvider = (EMFModelProvider)modelProviderDesc.getModelProvider();
		emfModelProvider.clear();
		repository.dispose();
		Activator.getDefault().getRepositoryCache().clear();
		if (gitDir.exists()) {
			File gitRoot = gitDir.getParentFile();
			if (gitRoot.exists()) {
				FileUtils.delete(gitRoot, FileUtils.RECURSIVE | FileUtils.RETRY);
			}
		}
	}

	protected void setUpRepository() throws Exception {
		final String testScenarioPath = getTestScenarioPath();
		final File testScenarioDir = getTestScenarioFile(testScenarioPath);
		Preconditions.checkState(testScenarioDir.isDirectory(), "Test scenario path must be a directory.");

		final File testScenarioDirOrigin = getTestScenarioFile(testScenarioPath + "origin");
		final File testScenarioDirLeft = getTestScenarioFile(testScenarioPath + "left");
		final File testScenarioDirRight = getTestScenarioFile(testScenarioPath + "right");
		Preconditions.checkState(testScenarioDirOrigin.exists() && testScenarioDirOrigin.isDirectory(),
				"Test scenario directory must contain a directory called \"origin\".");
		Preconditions.checkState(testScenarioDirLeft.exists() && testScenarioDirLeft.isDirectory(),
				"Test scenario directory must contain a directory called \"left\".");
		Preconditions.checkState(testScenarioDirRight.exists() && testScenarioDirRight.isDirectory(),
				"Test scenario directory must contain a directory called \"right\".");

		commitContentFrom(testScenarioDirOrigin, "initial-commit");
		repository.createBranch(MASTER_BRANCH, BRANCH_LEFT);
		repository.createBranch(MASTER_BRANCH, BRANCH_RIGHT);
		repository.checkoutBranch(BRANCH_LEFT);
		commitContentFrom(testScenarioDirLeft, "left-commit");
		repository.checkoutBranch(BRANCH_RIGHT);
		commitContentFrom(testScenarioDirRight, "right-commit");
	}

	private File getTestScenarioFile(String scenarioPath) throws IOException, URISyntaxException {
		final Bundle bundle = Platform.getBundle(TEST_BUNDLE);
		final URI fileUri = getFileUri(bundle.getEntry(scenarioPath));
		return toFile(fileUri);
	}

	private URI getFileUri(URL bundleUrl) throws IOException {
		return URI.createFileURI(FileLocator.toFileURL(bundleUrl).getPath());
	}

	private File toFile(URI fileUri) throws URISyntaxException {
		return new File(fileUri.toFileString());
	}

	private void commitContentFrom(File rootDirectory, String commitMsg) throws Exception {
		// TODO support multiple projects
		final File workingDirectory = repository.getRepository().getWorkTree();
		final TestProject testProject1 = new TestProject(DEFAULT_PROJECT, workingDirectory.getAbsolutePath());
		final IProject iProject = testProject1.getProject();
		final File projectDirectory = new File(iProject.getLocation().toOSString());
		repository.connect(iProject);
		copyDirectoryContents(rootDirectory, projectDirectory);
		repository.addAllAndCommit(commitMsg, true);
	}

	private static void copyDirectoryContents(File rootDirectory, final File workingDirectory)
			throws IOException {
		String[] list = rootDirectory.list();
		if (list != null) {
			for (String child : list) {
				copyDirectory(new File(rootDirectory, child), new File(workingDirectory, child));
			}
		}
	}

	private static void copyDirectory(File source, File destination) throws IOException {
		if (source != null && source.isDirectory()) {
			if (destination != null && !destination.exists()) {
				destination.mkdir();
			}
			String[] list = source.list();
			if (list != null) {
				for (String child : list) {
					copyDirectory(new File(source, child), new File(destination, child));
				}
			}
		} else {
			copyFile(source, destination);
		}
	}

	private static void copyFile(File source, File dest) throws IOException {
		FileChannel sourceChannel = null;
		FileChannel destChannel = null;
		FileInputStream fileInputStream = new FileInputStream(source);
		sourceChannel = fileInputStream.getChannel();
		FileOutputStream fileOutputStream = new FileOutputStream(dest);
		destChannel = fileOutputStream.getChannel();
		destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
		sourceChannel.close();
		destChannel.close();
		fileInputStream.close();
		fileOutputStream.close();
	}

	private Iterable<File> getAllContainedFiles(File workingDirectory) {
		final Builder<File> builder = ImmutableList.builder();
		File[] listFiles = workingDirectory.listFiles();
		if (listFiles != null) {
			for (File containedFile : listFiles) {
				if (containedFile.isFile()) {
					builder.add(containedFile);
				} else if (containedFile.isDirectory()) {
					builder.addAll(getAllContainedFiles(containedFile));
				}
			}
		}
		return builder.build();
	}

	@Before
	public void setupModelInclusionTesterRegistry() {
		EMFCompareIDEPlugin idePlugin = EMFCompareIDEPlugin.getDefault();
		ModelInclusionTesterRegistry registry = idePlugin.getModelInclusionTesterRegistry();
		registry.clear();
		addFileExtensionTester(registry, "di");
		addFileExtensionTester(registry, "uml");
		addFileExtensionTester(registry, "notation");
	}

	private void addFileExtensionTester(ModelInclusionTesterRegistry registry, final String key) {
		registry.add(key, new IModelInclusionTester() {
			@Override
			public boolean shouldInclude(IFile file) {
				return key.equals(file.getFileExtension());
			}
		});
	}

	/**
	 * Tests merging branch <em>left</em> into checked-out branch <em>right</em> and validates the result
	 * based on {@link #validateResult()}.
	 */
	@Test
	public void testMergeLeftIntoRight() throws Exception {
		repository.checkoutBranch(BRANCH_RIGHT);
		repository.mergeLogicalWithNewCommit(BRANCH_LEFT);
		validate();
		validateMergeLeftIntoRight();
	}

	/**
	 * Tests merging branch <em>right</em> into checked-out branch <em>left</em> and validates the result
	 * based on {@link #validateResult()}.
	 */
	@Test
	public void testMergeRightIntoLeft() throws Exception {
		repository.checkoutBranch(BRANCH_LEFT);
		repository.mergeLogicalWithNewCommit(BRANCH_RIGHT);
		validate();
		validateMergeRightIntoLeft();
	}

	/**
	 * Tests rebasing branch <em>left</em> onto checked-out branch <em>right</em> and validates the result
	 * based on {@link #validateResult()}.
	 */
	@Test
	public void testRebaseLeftOntoRight() throws Exception {
		repository.checkoutBranch(BRANCH_RIGHT);
		repository.rebaseLogical(BRANCH_LEFT);
		validate();
		validateRebaseLeftOntoRight();
	}

	/**
	 * Tests rebasing branch <em>right</em> onto checked-out branch <em>left</em> and then validates the
	 * result based on {@link #validateResult()} .
	 */
	@Test
	public void testRebaseRightOntoLeft() throws Exception {
		repository.checkoutBranch(BRANCH_LEFT);
		repository.rebaseLogical(BRANCH_RIGHT);
		validate();
		validateRebaseRightOntoLeft();
	}

	/**
	 * Tests cherry-picking branch <em>left</em> onto checked-out branch <em>right</em> and validates the
	 * result based on {@link #validateResult()}.
	 */
	@Test
	public void testCherryPickLeftOntoRight() throws Exception {
		repository.checkoutBranch(BRANCH_RIGHT);
		repository.cherryPickLogical(BRANCH_LEFT);
		validate();
		validateCherryPickLeftOntoRight();
	}

	/**
	 * Tests cherry-picking branch <em>right</em> onto checked-out branch <em>left</em> and then validates the
	 * result based on {@link #validateResult()} .
	 */
	@Test
	public void testCherryPickRightOntoLeft() throws Exception {
		repository.checkoutBranch(BRANCH_LEFT);
		repository.cherryPickLogical(BRANCH_RIGHT);
		validate();
		validateCherryPickRightOntoLeft();
	}

	protected void validate() throws Exception {
		validateResult();
		validateResources();
	}

	private void validateResources() throws Exception {
		final ResourceSet resourceSet = new ResourceSetImpl();
		final File workingDirectory = repository.getRepository().getWorkTree();
		final Iterable<File> filesOfInterest = filter(getAllContainedFiles(workingDirectory),
				and(IS_EXISTING_FILE, getFileOfInterestFilter()));
		final Iterable<URI> urisOfInterest = transform(filesOfInterest,
				toUri(workingDirectory.getAbsolutePath()));
		for (URI uriOfInterest : urisOfInterest) {
			final Resource resource = resourceSet.getResource(uriOfInterest, true);
			validateResult(resource);
		}
	}

	private Function<File, URI> toUri(String string) {
		return new Function<File, URI>() {
			@Override
			public URI apply(File input) {
				return URI.createPlatformResourceURI(repository.getRepoRelativePath(input), true);
			}
		};
	}

	private Predicate<File> getFileOfInterestFilter() {
		return new Predicate<File>() {
			@Override
			public boolean apply(File input) {
				return !input.getAbsolutePath().startsWith(gitDir.getAbsolutePath()) && shouldValidate(input);
			}
		};
	}

	protected boolean isConflicting() throws Exception {
		return repository.status().getConflicting().size() > 0;
	}

	protected boolean noConflict() throws Exception {
		return !isConflicting();
	}

	protected boolean fileExists(String string) {
		final File workTree = repository.getRepository().getWorkTree();
		final File projectDirectory = new File(workTree, DEFAULT_PROJECT);
		return new File(projectDirectory, string).exists();
	}

	/**
	 * Returns the resolution scope to be used for this test case.
	 * <p>
	 * The default value is {@link CrossReferenceResolutionScope#WORKSPACE}. Subclasses may override this
	 * method to provide a different resolution scope.
	 * </p>
	 * 
	 * @return the resolution scope to be used for this test case.
	 */
	protected static CrossReferenceResolutionScope getResolutionScope() {
		return CrossReferenceResolutionScope.WORKSPACE;
	}

	/**
	 * Returns the path to the data of the test scenario.
	 * 
	 * @return the path to the data of the test scenario.
	 */
	protected abstract String getTestScenarioPath();

	/**
	 * Specifies whether a given {@code file} should be validated in this test.
	 * <p>
	 * Clients may overwrite to include or exclude certain files from being validated with
	 * {@link #validateResult(Resource)}. The default is <code>true</code> for any file.
	 * </p>
	 * 
	 * @param file
	 *            The input in question.
	 * @return <code>true</code> if the given {@code file} should be validated, <code>false</code> otherwise.
	 */
	protected boolean shouldValidate(File file) {
		return true;
	}

	/**
	 * Validates the result after merging, rebasing, or cherry-picking in either direction.
	 * 
	 * @throws Exception
	 *             if something goes wrong during the validation of the assertions.
	 */
	protected abstract void validateResult() throws Exception;

	/**
	 * Validates contents of a single resource after merging, rebasing, or cherry-picking in either direction.
	 * 
	 * @param resource
	 *            The resource to validate.
	 * @throws Exception
	 *             if something goes wrong during the validation of the assertions.
	 */
	protected abstract void validateResult(Resource resource) throws Exception;

	/**
	 * Validates the result of merging branch <em>left</em> into <em>right</em>.
	 * <p>
	 * This method it intended to be overwritten by sub-classes if the specific tests require specific
	 * validation.
	 * </p>
	 */
	protected void validateMergeLeftIntoRight() {
		// no validation by default, can be overwritten by sub-classes
	}

	/**
	 * Validates the result of merging branch <em>right</em> into <em>left</em>.
	 * <p>
	 * This method it intended to be overwritten by sub-classes if the specific tests require specific
	 * validation.
	 * </p>
	 */
	protected void validateMergeRightIntoLeft() {
		// no validation by default, can be overwritten by sub-classes
	}

	/**
	 * Validates the result of rebasing branch <em>left</em> onto <em>right</em>.
	 * <p>
	 * This method it intended to be overwritten by sub-classes if the specific tests require specific
	 * validation.
	 * </p>
	 */
	protected void validateRebaseLeftOntoRight() {
		// no validation by default, can be overwritten by sub-classes
	}

	/**
	 * Validates the result of rebasing branch <em>right</em> onto <em>left</em>.
	 * <p>
	 * This method it intended to be overwritten by sub-classes if the specific tests require specific
	 * validation.
	 * </p>
	 */
	protected void validateRebaseRightOntoLeft() {
		// no validation by default, can be overwritten by sub-classes
	}

	/**
	 * Validates the result of cherry-picking branch <em>left</em> onto <em>right</em>.
	 * <p>
	 * This method it intended to be overwritten by sub-classes if the specific tests require specific
	 * validation.
	 * </p>
	 */
	protected void validateCherryPickLeftOntoRight() {
		// no validation by default, can be overwritten by sub-classes
	}

	/**
	 * Validates the result of cherry-picking branch <em>right</em> onto <em>left</em>.
	 * <p>
	 * This method it intended to be overwritten by sub-classes if the specific tests require specific
	 * validation.
	 * </p>
	 */
	protected void validateCherryPickRightOntoLeft() {
		// no validation by default, can be overwritten by sub-classes
	}
}

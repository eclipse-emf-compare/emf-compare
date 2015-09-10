package org.eclipse.emf.compare.diagram.papyrus.tests.resourceattachmentchange.move;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.GitCorePreferences;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.diagram.papyrus.tests.egit.fixture.GitTestRepository;
import org.eclipse.emf.compare.diagram.papyrus.tests.egit.fixture.MockSystemReader;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.ComparisonScopeBuilder;
import org.eclipse.emf.compare.ide.ui.internal.logical.EMFModelProvider;
import org.eclipse.emf.compare.ide.ui.internal.logical.IdenticalResourceMinimizer;
import org.eclipse.emf.compare.ide.ui.internal.logical.StorageTypedElement;
import org.eclipse.emf.compare.ide.ui.internal.logical.SubscriberStorageAccessor;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ThreadedModelResolver;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProvider;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.tests.CompareTestCase;
import org.eclipse.emf.compare.ide.ui.tests.workspace.TestProject;
import org.eclipse.emf.compare.rcp.internal.extension.impl.EMFCompareBuilderConfigurator;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.util.FileUtils;
import org.eclipse.jgit.util.SystemReader;
import org.eclipse.team.core.subscribers.Subscriber;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.osgi.framework.Bundle;

@SuppressWarnings({"restriction", "nls" })
public class AbstractResourceAttachmentChangeMoveTests extends CompareTestCase {

	/**
	 * Path to the test data.
	 */
	protected static final String TEST_DATA_PATH = "src/org/eclipse/emf/compare/diagram/papyrus/tests/resourceattachmentchange/move/data/";

	/**
	 * The bundle containing this test.
	 */
	protected static final String TEST_BUNDLE = "org.eclipse.emf.compare.diagram.papyrus.tests.git";
	
	protected static final String MASTER = Constants.R_HEADS + Constants.MASTER;

	protected static final String BRANCH_1 = Constants.R_HEADS + "branch1";

	protected static final String BRANCH_2 = Constants.R_HEADS + "branch2";

	protected static final String BRANCH_3 = Constants.R_HEADS + "branch3";

	protected GitTestRepository repository;

	// The ".git" folder of the test repository
	protected File gitDir;

	protected static String deafultResolutionScope;

	@BeforeClass
	public static void setUpClass() {
		// suppress auto-ignoring and auto-sharing to avoid interference
		IEclipsePreferences eGitPreferences = InstanceScope.INSTANCE.getNode(Activator.getPluginId());
		eGitPreferences.put(GitCorePreferences.core_preferredMergeStrategy, "model recursive");
		eGitPreferences.putBoolean(GitCorePreferences.core_autoShareProjects, false);
		// This is actually the value of "GitCorePreferences.core_autoIgnoreDerivedResources"... but it was
		// not in Egit 2.1
		eGitPreferences.putBoolean("core_autoIgnoreDerivedResources", false);
		final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault().getPreferenceStore();
		deafultResolutionScope = store.getString(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE);
		store.setValue(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE,
				CrossReferenceResolutionScope.WORKSPACE.name());
	}

	@AfterClass
	public static void tearDownClass() {
		final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault().getPreferenceStore();
		store.setValue(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE, deafultResolutionScope);
	}

	@Override
	@Before
	public void setUp() throws Exception {
		// ensure there are no shared Repository instances left
		// when starting a new test
		Activator.getDefault().getRepositoryCache().clear();
		final MockSystemReader mockSystemReader = new MockSystemReader();
		SystemReader.setInstance(mockSystemReader);
		final IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		String gitRepoPath = workspaceRoot.getRawLocation().toFile() + File.separator + "repo";
		mockSystemReader.setProperty(Constants.GIT_CEILING_DIRECTORIES_KEY, workspaceRoot.getLocation()
				.toFile().getParentFile().getAbsoluteFile().toString());
		gitDir = new File(gitRepoPath, Constants.DOT_GIT);
		repository = new GitTestRepository(gitDir);
		repository.ignore(workspaceRoot.getRawLocation().append(".metadata").toFile());
	}

	@Override
	@After
	public void tearDown() throws Exception {
		final EMFModelProvider emfModelProvider = (EMFModelProvider)ModelProvider.getModelProviderDescriptor(
				EMFModelProvider.PROVIDER_ID).getModelProvider();
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
	
	/**
	 * Copies the file located in {@link #TEST_DATA_PATH} + {@code filePath} to the given
	 * {@code destinationPath} in {@code iProject}.
	 * 
	 * @param iProject
	 *            The {@link IProject} to which the file is added.
	 * @param filePath
	 *            The path relative to {@link #TEST_DATA_PATH} where the file is originally located.
	 * @param destinationPath
	 *            The path in the {@code iProject} to which the file will be copied.
	 * @return The newly created {@link IFile}.
	 */
	protected IFile addToProject(TestProject project, IProject iProject, String filePath, String destinationPath)
			throws IOException, URISyntaxException, CoreException {
		final Bundle bundle = Platform.getBundle(TEST_BUNDLE);
		final URI fileUri = getFileUri(bundle.getEntry(TEST_DATA_PATH + filePath));

		final File file = project.getOrCreateFile(iProject, destinationPath + fileUri.lastSegment());

		copyFile(toFile(fileUri), file);

		return project.getIFile(iProject, file);
	}

	protected URI getFileUri(final URL bundleUrl) throws IOException {
		URL fileLocation = FileLocator.toFileURL(bundleUrl);
		return URI.createFileURI(fileLocation.getPath());
	}

	protected File toFile(final URI fileUri) throws URISyntaxException {
		return new File(fileUri.toFileString());
	}

	protected Comparison compare(String sourceRev, String targetRev, IFile file) throws Exception {
		final String fullPath = file.getFullPath().toString();
		final Subscriber subscriber = repository.createSubscriberForComparison(sourceRev, targetRev, file,
				false);
		final IStorageProviderAccessor accessor = new SubscriberStorageAccessor(subscriber);
		final IStorageProvider sourceProvider = accessor.getStorageProvider(file,
				IStorageProviderAccessor.DiffSide.SOURCE);
		final IStorageProvider remoteProvider = accessor.getStorageProvider(file,
				IStorageProviderAccessor.DiffSide.REMOTE);
		final IStorageProvider ancestorProvider = accessor.getStorageProvider(file,
				IStorageProviderAccessor.DiffSide.ORIGIN);
		assertNotNull(sourceProvider);
		assertNotNull(remoteProvider);
		assertNotNull(ancestorProvider);

		final IProgressMonitor monitor = new NullProgressMonitor();
		// do we really need to create a new one?
		final IStorageProviderAccessor storageAccessor = new SubscriberStorageAccessor(subscriber);
		final ITypedElement left = new StorageTypedElement(sourceProvider.getStorage(monitor), fullPath);
		final ITypedElement right = new StorageTypedElement(remoteProvider.getStorage(monitor), fullPath);
		final ITypedElement origin = new StorageTypedElement(ancestorProvider.getStorage(monitor), fullPath);
		final ThreadedModelResolver resolver = new ThreadedModelResolver();
		resolver.initialize();
		final ComparisonScopeBuilder scopeBuilder = new ComparisonScopeBuilder(resolver,
				new IdenticalResourceMinimizer(), storageAccessor);
		final IComparisonScope scope = scopeBuilder.build(left, right, origin, monitor);

		final ResourceSet leftResourceSet = (ResourceSet)scope.getLeft();
		final ResourceSet rightResourceSet = (ResourceSet)scope.getRight();
		final ResourceSet originResourceSet = (ResourceSet)scope.getOrigin();

		assertFalse(leftResourceSet.getResources().isEmpty());
		assertFalse(rightResourceSet.getResources().isEmpty());
		assertFalse(originResourceSet.getResources().isEmpty());

		final Builder comparisonBuilder = EMFCompare.builder();
		EMFCompareBuilderConfigurator.createDefault().configure(comparisonBuilder);

		return comparisonBuilder.build().compare(scope, new BasicMonitor());
	}
}

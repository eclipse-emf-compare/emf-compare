/*******************************************************************************
 * Copyright (C) 2013, 2017 Obeo and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.egit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.GitCorePreferences;
import org.eclipse.egit.core.RepositoryCache;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.ComparisonScopeBuilder;
import org.eclipse.emf.compare.ide.ui.internal.logical.StorageTypedElement;
import org.eclipse.emf.compare.ide.ui.internal.logical.SubscriberStorageAccessor;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ThreadedModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProvider;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.tests.CompareTestCase;
import org.eclipse.emf.compare.ide.ui.tests.egit.fixture.GitTestRepository;
import org.eclipse.emf.compare.ide.ui.tests.egit.fixture.MockSystemReader;
import org.eclipse.emf.compare.ide.ui.tests.workspace.TestProject;
import org.eclipse.emf.compare.internal.utils.Graph;
import org.eclipse.emf.compare.rcp.internal.extension.impl.EMFCompareBuilderConfigurator;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.util.SystemReader;
import org.eclipse.team.core.subscribers.Subscriber;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.osgi.framework.Bundle;

/**
 * The set up and tear down of this class were mostly copied from org.eclipse.egit.core.test.GitTestCase.
 */
@SuppressWarnings({"restriction", "nls", })
public class CompareGitTestCase extends CompareTestCase {
	/**
	 * The bundle containing this test.
	 */
	protected static final String TEST_BUNDLE = "org.eclipse.emf.compare.ide.ui.tests.git";

	protected GitTestRepository repository;

	// The ".git" folder of the test repository
	protected File gitDir;

	@BeforeClass
	public static void setUpClass() {
		// suppress auto-ignoring and auto-sharing to avoid interference
		IEclipsePreferences eGitPreferences = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		eGitPreferences.put(GitCorePreferences.core_preferredMergeStrategy, "model recursive");
		eGitPreferences.putBoolean(GitCorePreferences.core_autoShareProjects, false);
		// This is actually the value of "GitCorePreferences.core_autoIgnoreDerivedResources"... but it was
		// not in Egit 2.1
		eGitPreferences.putBoolean("core_autoIgnoreDerivedResources", false);
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		// ensure there are no shared Repository instances left
		// when starting a new test
		RepositoryCache.INSTANCE.clear();
		final MockSystemReader mockSystemReader = new MockSystemReader();
		SystemReader.setInstance(mockSystemReader);
		mockSystemReader.setProperty(Constants.GIT_CEILING_DIRECTORIES_KEY, ResourcesPlugin.getWorkspace()
				.getRoot().getLocation().toFile().getParentFile().getAbsoluteFile().toString());
		final IWorkspaceRoot workspaceRoot = project.getProject().getWorkspace().getRoot();
		gitDir = new File(workspaceRoot.getRawLocation().toFile(), Constants.DOT_GIT);
		repository = new GitTestRepository(gitDir);
		repository.connect(project.getProject());
		File gitIgnoreFile = workspaceRoot.getRawLocation().append(".gitignore").toFile();
		Path gitIgnorePath = gitIgnoreFile.toPath();
		Files.write(gitIgnorePath, Collections.singleton("/.metadata/"), Charset.forName("UTF-8"),
				StandardOpenOption.CREATE);
	}

	@Override
	@After
	public void tearDown() throws Exception {
		repository.dispose();
		RepositoryCache.INSTANCE.clear();
		if (gitDir.exists()) {
			try (Stream<java.nio.file.Path> walk = Files.walk(gitDir.toPath())) {
				walk.sorted(Comparator.reverseOrder()).map(java.nio.file.Path::toFile).forEach(File::delete);
			}
		}
		super.tearDown();
	}

	protected void compareBothDirectionsAndCheck(IFile file, String source, String destination,
			int expectedConflicts, int diffsInSource, int diffsInDestination) throws Exception {
		repository.checkoutBranch(source);
		Comparison compareResult = compare(source, destination, file);

		assertEquals(expectedConflicts, compareResult.getConflicts().size());
		assertDiffCount(compareResult.getDifferences(), diffsInSource, diffsInDestination);

		repository.checkoutBranch(destination);
		compareResult = compare(destination, source, file);

		assertEquals(expectedConflicts, compareResult.getConflicts().size());
		assertDiffCount(compareResult.getDifferences(), diffsInDestination, diffsInSource);

		// Other test call this method. The repository must be on the same branch at the end.
		repository.checkoutBranch(source);
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
		final ITypedElement left = new StorageTypedElement(file, fullPath);
		final ITypedElement right = new StorageTypedElement(remoteProvider.getStorage(monitor), fullPath);
		final ITypedElement origin = new StorageTypedElement(ancestorProvider.getStorage(monitor), fullPath);
		final ThreadedModelResolver resolver = new ThreadedModelResolver();
		resolver.setGraph(new Graph<URI>());
		resolver.initialize();
		final ComparisonScopeBuilder scopeBuilder = new ComparisonScopeBuilder(resolver,
				EMFCompareIDEUIPlugin.getDefault().getModelMinimizerRegistry().getCompoundMinimizer(),
				storageAccessor);
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

	protected IStorageProviderAccessor createAccessorForComparison(String sourceRev, String targetRev,
			boolean includeLocal) throws Exception {
		final Subscriber subscriber = repository.createSubscriberForResolution(sourceRev, targetRev,
				includeLocal);
		return new SubscriberStorageAccessor(subscriber);
	}

	protected IStorageProviderAccessor createRemoteAccessorForComparison(String sourceRev, String targetRev,
			IFile file) throws Exception {
		final Subscriber subscriber = repository.createSubscriberForComparisonWithRemoteMappings(sourceRev,
				targetRev, file);
		return new SubscriberStorageAccessor(subscriber);
	}

	protected static void assertDiffCount(List<Diff> differences, int expectedOutgoing,
			int expectedIncoming) {
		assertEquals(expectedOutgoing + expectedIncoming, differences.size());

		int outgoingCount = 0;
		int incomingCount = 0;
		for (Diff diff : differences) {
			switch (diff.getSource()) {
				case LEFT:
					outgoingCount++;
					break;
				case RIGHT:
					incomingCount++;
					break;
				default:
					break;
			}
		}

		assertEquals(expectedOutgoing, outgoingCount);
		assertEquals(expectedIncoming, incomingCount);
	}

	protected IFile addToProject(String testDataPath, TestProject testProject, IProject testIProject,
			String filePath, String destinationPath) throws IOException, URISyntaxException, CoreException {
		final Bundle bundle = Platform.getBundle(TEST_BUNDLE);
		final URI fileUri = getFileUri(bundle.getEntry(testDataPath + filePath));

		final File file = testProject.getOrCreateFile(testIProject, destinationPath + fileUri.lastSegment());

		copyFile(toFile(fileUri), file);

		return testProject.getIFile(testIProject, file);
	}

	private URI getFileUri(final URL bundleUrl) throws IOException {
		URL fileLocation = FileLocator.toFileURL(bundleUrl);
		return URI.createFileURI(fileLocation.getPath());
	}

	private File toFile(final URI fileUri) throws URISyntaxException {
		return new File(fileUri.toFileString());
	}
}

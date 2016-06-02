/*******************************************************************************
 * Copyright (c) 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - add convenience methods
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.git.framework.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.google.common.collect.Lists;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.mapping.RemoteResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceTraversal;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.internal.util.ResourceUtil;
import org.eclipse.egit.core.op.BranchOperation;
import org.eclipse.egit.core.op.ConnectProviderOperation;
import org.eclipse.egit.core.op.DisconnectProviderOperation;
import org.eclipse.egit.core.op.MergeOperation;
import org.eclipse.egit.core.op.ResetOperation;
import org.eclipse.egit.core.synchronize.GitResourceVariantTreeSubscriber;
import org.eclipse.egit.core.synchronize.GitSubscriberResourceMappingContext;
import org.eclipse.egit.core.synchronize.dto.GitSynchronizeData;
import org.eclipse.egit.core.synchronize.dto.GitSynchronizeDataSet;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.ComparisonScopeBuilder;
import org.eclipse.emf.compare.ide.ui.internal.logical.IdenticalResourceMinimizer;
import org.eclipse.emf.compare.ide.ui.internal.logical.StorageTypedElement;
import org.eclipse.emf.compare.ide.ui.internal.logical.SubscriberStorageAccessor;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProvider;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.rcp.internal.extension.impl.EMFCompareBuilderConfigurator;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jgit.api.CheckoutResult;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.util.FileUtils;
import org.eclipse.team.core.subscribers.SubscriberScopeManager;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;

/**
 * This class contains methods to compare a file between two branchs in an existing git repository.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
@SuppressWarnings({"restriction" })
public class GitTestSupport {

	/**
	 * Size of the buffer to read/write data
	 */
	private static final int BUFFER_SIZE = 4096;

	private final static String GIT_BRANCH_PREFIX = "refs/heads/"; //$NON-NLS-1$

	public final static String COMPARE_NO_PROJECT_SELECTED = "noProject"; //$NON-NLS-1$

	private Repository repository = null;

	private IProject[] projects = null;

	private ArrayList<Runnable> disposers = null;

	private MergeResult mergeResult;

	private IOverwriteQuery overwriteQuery = new IOverwriteQuery() {
		public String queryOverwrite(String file) {
			return ALL;
		}
	};

	protected void createRepositoryFromPath(Class<?> clazz, String path) throws IOException,
			InvocationTargetException, InterruptedException, CoreException {
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		// Delete all projects that can remain in the workspace : prevent errors dues to wrong cleanup of
		// other tests
		root.delete(true, new NullProgressMonitor());
		IPath location = root.getLocation();
		extractArchive(clazz, path, root);
		importProjects(new File(location.toString()));
		connectRepository(new File(location.toString()));
		projects = root.getProjects();
		for (IProject project : projects) {
			connect(project);
		}
	}

	private void connectRepository(File file) throws IOException {
		File gitDir = findGitDir(file);
		this.repository = Activator.getDefault().getRepositoryCache().lookupRepository(gitDir);
		this.disposers = new ArrayList<Runnable>();
	}

	/**
	 * Connect a project to this repository.
	 * 
	 * @param project
	 *            The project to connect
	 */
	private void connect(IProject project) throws CoreException, InterruptedException {
		ConnectProviderOperation op = new ConnectProviderOperation(project, repository.getDirectory());
		op.execute(null);
	}

	private File findGitDir(File file) {
		for (File child : file.listFiles()) {
			if (child.isDirectory() && child.getName().equals(".git")) { //$NON-NLS-1$
				return child;
			} else if (child.isDirectory()) {
				File findGitDir = findGitDir(child);
				if (findGitDir != null) {
					return findGitDir;
				}
			}
		}
		return null;
	}

	private void importProjects(File file) throws InvocationTargetException, InterruptedException,
			CoreException {
		for (File child : file.listFiles()) {
			if (child.isDirectory() && !child.getName().equals(".metadata") //$NON-NLS-1$
					&& !child.getName().equals(".git")) { //$NON-NLS-1$
				importProjects(child);
			} else if (child.getName().equals(".project")) { //$NON-NLS-1$
				importProject(child);
			}
		}
	}

	private void importProject(File file) throws InvocationTargetException, InterruptedException,
			CoreException {
		IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(
				new Path(file.getAbsolutePath()));
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
		project.create(description, new NullProgressMonitor());
		project.open(new NullProgressMonitor());

		ImportOperation importOperation = new ImportOperation(project.getFullPath(), file.getParentFile(),
				FileSystemStructureProvider.INSTANCE, overwriteQuery);
		importOperation.setCreateContainerStructure(false);
		importOperation.run(new NullProgressMonitor());
	}

	private void extractArchive(Class<?> clazz, String path, IWorkspaceRoot root) throws IOException {
		InputStream resourceAsStream = clazz.getResourceAsStream(path);
		ZipInputStream zipIn = new ZipInputStream(resourceAsStream);
		ZipEntry entry = null;
		while ((entry = zipIn.getNextEntry()) != null) {
			String filePath = root.getLocation() + File.separator + entry.getName();
			if (!entry.isDirectory()) {
				extractFile(zipIn, filePath);
			} else {
				File dir = new File(filePath);
				dir.mkdir();
			}
			zipIn.closeEntry();
		}
		zipIn.close();
	}

	private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}

	protected void setup() throws CoreException, IOException {
		final IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] unknownProjects = workspaceRoot.getProjects();
		if (unknownProjects != null && unknownProjects.length > 0) {
			for (IProject iProject : unknownProjects) {
				iProject.delete(true, new NullProgressMonitor());
			}
		}
		Activator.getDefault().getRepositoryCache().clear();

		File file = new File(workspaceRoot.getLocation().toOSString());
		for (File child : file.listFiles()) {
			if (!child.getName().equals(".metadata")) { //$NON-NLS-1$
				FileUtils.delete(child, FileUtils.RECURSIVE | FileUtils.RETRY);
			}
		}
	}

	protected void tearDown() throws CoreException, IOException {
		if (repository != null) {
			repository.close();
			repository = null;
		}
		if (disposers != null) {
			for (Runnable disposer : disposers) {
				disposer.run();
			}
			disposers.clear();
		}

		Activator.getDefault().getRepositoryCache().clear();

		if (projects != null) {
			new DisconnectProviderOperation(Arrays.asList(projects)).execute(null);
			for (IProject iProject : projects) {
				iProject.delete(true, new NullProgressMonitor());
			}
		}

		File file = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString());
		for (File child : file.listFiles()) {
			if (!child.getName().equals(".metadata")) { //$NON-NLS-1$
				FileUtils.delete(child, FileUtils.RECURSIVE | FileUtils.RETRY);
			}
		}
	}

	public Repository getRepository() {
		return repository;
	}

	public List<IProject> getProjects() throws CoreException {
		for (IProject project : projects) {
			project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		}
		return Lists.newArrayList(projects);
	}

	public Status getStatus() throws Exception {
		Git git = new Git(repository);
		try {
			return git.status().call();
		} finally {
			git.close();
		}
	}

	public MergeResult getMergeResult() {
		return mergeResult;
	}

	/**
	 * Merge two branches with the given merge strategy.
	 * 
	 * @param from
	 *            The checkouted branch (for example "master" or "refs/for/master", both syntaxes are
	 *            accepted)
	 * @param to
	 *            The branch to merge with (for example "master" or "refs/for/master", both syntaxes are
	 *            accepted)
	 * @throws CoreException
	 */
	public void merge(String from, String to) throws CoreException, IOException, InterruptedException {
		checkoutBranch(normalizeBranch(from));
		MergeOperation op = new MergeOperation(repository, normalizeBranch(to));
		op.execute(new NullProgressMonitor());
		mergeResult = op.getResult();
		for (IProject iProject : projects) {
			iProject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		}
	}

	/**
	 * <pre>
	 * Compare the file on the given path between the two given branches. This method is attended to be used
	 * when their is only one project in the repository.
	 * 
	 * If their is multiple projects use {@code compare(from, to , fileName, containerName)} with the name of 
	 * the containing project instead.
	 * </pre>
	 * 
	 * @param from
	 *            The branch checkouted (for example "master" or "refs/for/master", both syntaxes are
	 *            accepted)
	 * @param to
	 *            The branch to compare with (for example "master" or "refs/for/master", both syntaxes are
	 *            accepted)
	 * @param fileName
	 *            The file to compare (the relative path to the file from the project)
	 * @return the comparison
	 * @throws IOException
	 * @throws CoreException
	 */
	public Comparison compare(String from, String to, String fileName) throws IOException, CoreException {
		return compare(from, to, fileName, COMPARE_NO_PROJECT_SELECTED);
	}

	/**
	 * <pre>
	 * Compare the file on the given path between the two given branches. This method is attended to be used
	 * when their is several projects in the repository.
	 *  
	 * If their is only one project use {@code compare(from, to , fileName)} instead or give the value
	 * {@code EMFCompareGitTestsSupport.COMPARE_NO_PROJECT_SELECTED} to the parameter containerProject.
	 * </pre>
	 * 
	 * @param from
	 *            The branch checkouted (for example "master" or "refs/for/master", both syntaxes are
	 *            accepted)
	 * @param to
	 *            The branch to compare with (for example "master" or "refs/for/master", both syntaxes are
	 *            accepted)
	 * @param fileName
	 *            The file to compare (the relative path to the file from the project)
	 * @param containerProject
	 *            The project containing the file to compare. If
	 *            {@code EMFCompareGitTestsSupport.COMPARE_NO_PROJECT_SELECTED} is used, the first correct
	 *            file will be used (use this when their is only one project)
	 * @return the comparison
	 * @throws IOException
	 * @throws CoreException
	 */
	public Comparison compare(String from, String to, String fileName, String containerProject)
			throws IOException, CoreException {
		String normalizedFrom = normalizeBranch(from);
		String normalizedTo = normalizeBranch(to);
		IFile file = null;
		for (IProject project : projects) {
			if (!containerProject.equals(COMPARE_NO_PROJECT_SELECTED)) {
				if (project.getName().equals(containerProject)) {
					file = project.getFile(fileName);
					break;
				}
			} else {
				file = project.getFile(fileName);
				break;
			}
		}

		if (file == null || !file.exists()) {
			throw new IllegalArgumentException("Could not find file " + fileName + ": wrong test set-up?"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		checkoutBranch(normalizedFrom);
		final String fullPath = file.getFullPath().toString();
		final GitSynchronizeData data = new GitSynchronizeData(getRepository(), normalizedFrom, normalizedTo,
				true);
		final GitSynchronizeDataSet gsds = new GitSynchronizeDataSet(data);
		final GitResourceVariantTreeSubscriber subscriber = new GitResourceVariantTreeSubscriber(gsds);
		subscriber.init(new NullProgressMonitor());
		final GitSubscriberResourceMappingContext context = new GitSubscriberResourceMappingContext(
				subscriber, gsds);

		final Set<IResource> includedResources = new HashSet<IResource>(Arrays.asList(file));
		final Set<ResourceMapping> allMappings = new HashSet<ResourceMapping>();

		Set<IResource> newResources = new HashSet<IResource>(includedResources);
		do {
			final Set<IResource> copy = newResources;
			newResources = new HashSet<IResource>();
			for (IResource resource : copy) {
				ResourceMapping[] mappings = ResourceUtil.getResourceMappings(resource, context);
				allMappings.addAll(Arrays.asList(mappings));
				newResources.addAll(collectResources(mappings, context));
			}
		} while (includedResources.addAll(newResources));

		// Launch the comparison now that the logical model is computed
		// and can be provided to a new GitSynchronizeData object
		final ResourceMapping[] mappings = allMappings.toArray(new ResourceMapping[allMappings.size()]);
		final GitSynchronizeData gsdThatCoverLogicalModel = new GitSynchronizeData(repository,
				normalizedFrom, normalizedTo, true, includedResources);
		final GitSynchronizeDataSet gsds2 = new GitSynchronizeDataSet(gsdThatCoverLogicalModel);
		final GitResourceVariantTreeSubscriber subscriber2 = new GitResourceVariantTreeSubscriber(gsds2);
		RemoteResourceMappingContext remoteContext = new GitSubscriberResourceMappingContext(subscriber2,
				gsds2);
		final SubscriberScopeManager subscriberScopeManager = new SubscriberScopeManager(subscriber2
				.getName(), mappings, subscriber2, remoteContext, true);
		subscriber2.init(new NullProgressMonitor());
		disposers.add(new Runnable() {
			public void run() {
				subscriber.dispose();
				gsds.dispose();
				subscriber2.dispose();
				gsds2.dispose();
				subscriberScopeManager.dispose();
			}
		});

		final IStorageProviderAccessor accessor = new SubscriberStorageAccessor(subscriber2);
		final IStorageProvider remoteProvider = accessor.getStorageProvider(file,
				IStorageProviderAccessor.DiffSide.REMOTE);
		final IStorageProvider ancestorProvider = accessor.getStorageProvider(file,
				IStorageProviderAccessor.DiffSide.ORIGIN);
		assertNotNull(remoteProvider);
		assertNotNull(ancestorProvider);

		final IProgressMonitor monitor = new NullProgressMonitor();
		// do we really need to create a new one?
		final ITypedElement left = new StorageTypedElement(file, fullPath);
		final ITypedElement right = new StorageTypedElement(remoteProvider.getStorage(monitor), fullPath);
		final ITypedElement origin = new StorageTypedElement(ancestorProvider.getStorage(monitor), fullPath);

		EMFCompareIDEUIPlugin p = EMFCompareIDEUIPlugin.getDefault();
		IModelResolver resolver = p.getModelResolverRegistry().getBestResolverFor(file);

		final ComparisonScopeBuilder scopeBuilder = new ComparisonScopeBuilder(resolver,
				new IdenticalResourceMinimizer(), accessor);
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

	private Set<IResource> collectResources(ResourceMapping[] mappings, ResourceMappingContext context) {
		final Set<IResource> resources = new HashSet<IResource>();
		for (ResourceMapping mapping : mappings) {
			try {
				ResourceTraversal[] traversals = mapping.getTraversals(context, new NullProgressMonitor());
				for (ResourceTraversal traversal : traversals) {
					resources.addAll(Arrays.asList(traversal.getResources()));
				}
			} catch (CoreException e) {
				Activator.logError(e.getMessage(), e);
			}
		}
		return resources;
	}

	/**
	 * Checkout repository to the given branch.
	 * 
	 * @param refName
	 *            The branch to checkout (for example "master" or "refs/heads/master", both syntaxes are
	 *            accepted)
	 * @throws CoreException
	 */
	public void checkoutBranch(String refName) throws CoreException, IOException {
		ResetOperation reset = new ResetOperation(repository, repository.getBranch(), ResetType.HARD);
		reset.execute(null);
		BranchOperation op = new BranchOperation(getRepository(), normalizeBranch(refName));
		op.execute(null);
		CheckoutResult result = op.getResult();
		if (result.getStatus() != CheckoutResult.Status.OK) {
			throw new IllegalStateException("Unable to checkout branch " + refName + " result:" + result); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	protected static String normalizeBranch(String branch) {
		if (branch.startsWith(GIT_BRANCH_PREFIX)) {
			return branch;
		} else {
			return GIT_BRANCH_PREFIX + branch;
		}
	}

	/**
	 * Specifies whether the repository is in conflict state or not.
	 * <p>
	 * This is a convenience method for <code>getStatus().getConflicting().isEmpty()</code>.
	 * </p>
	 * 
	 * @return <code>true</code> if the repository is in a conflict state, <code>false</code> otherwise.
	 * @throws Exception
	 *             if the status of the repository could not be created queried.
	 */
	public boolean noConflict() throws Exception {
		return getStatus().getConflicting().isEmpty();
	}

	/**
	 * Specifies whether the file given in <code>path</code> currently exists in the working tree of the
	 * repository.
	 * 
	 * @param path
	 *            The path to the file in question, such as <code>dir1/dir2/file1.txt</code>.
	 * @return <code>true</code> if the file in the given <code>path</code> exists, <code>false</code>
	 *         otherwise.
	 */
	public boolean fileExists(String path) {
		return new File(repository.getWorkTree(), path).exists();
	}
}

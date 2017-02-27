/*******************************************************************************
 * Copyright (c) 2015, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Martin Fleck - bug 512562
 *******************************************************************************/
package data.models;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.egit.core.Activator;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.conflict.IConflictDetector;
import org.eclipse.emf.compare.conflict.MatchBasedConflictDetector;
import org.eclipse.emf.compare.diagram.internal.CompareDiagramPostProcessor;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.DiffBuilder;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.diff.IDiffProcessor;
import org.eclipse.emf.compare.equi.DefaultEquiEngine;
import org.eclipse.emf.compare.equi.IEquiEngine;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.ComparisonScopeBuilder;
import org.eclipse.emf.compare.ide.ui.internal.logical.StorageTypedElement;
import org.eclipse.emf.compare.ide.ui.internal.logical.SubscriberStorageAccessor;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.registry.ModelResolverRegistry;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProvider;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.req.DefaultReqEngine;
import org.eclipse.emf.compare.req.IReqEngine;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.performance.git.GitUtil;
import org.eclipse.emf.compare.uml2.internal.postprocessor.UMLPostProcessor;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.team.core.subscribers.Subscriber;
import org.osgi.framework.Bundle;

import com.google.common.base.Throwables;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("restriction")
public class DataGit {

	private static final String MASTER = Constants.R_HEADS + Constants.MASTER;

	private static final String MODIFIED = Constants.R_HEADS + "modified";

	private List<Runnable> disposers;

	private List<ResourceSet> resourceSets = newArrayList();

	private IComparisonScope scope;

	private Comparison comparison;

	private File repoFile;

	private Repository repository;

	public DataGit(String zippedRepoLocation, String repoName, String rootProjectName, String modelName) {
		try {
			this.disposers = new ArrayList<Runnable>();
			String systemTmpDir = System.getProperty("java.io.tmpdir");
			Bundle bundle = Platform.getBundle("org.eclipse.emf.compare.tests.performance");
			URL entry = bundle.getEntry(zippedRepoLocation);
			repoFile = new File(systemTmpDir + File.separator + repoName);

			// Delete repo if it already exists
			GitUtil.deleteRepo(repoFile);

			// Unzip repository to temp directory
			GitUtil.unzipRepo(entry, systemTmpDir, new NullProgressMonitor());

			Job importJob = new Job("ImportProjects") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					GitUtil.importProjectsFromRepo(repoFile);
					return Status.OK_STATUS;
				}
			};
			importJob.schedule();
			importJob.join();

			Job connectJob = new Job("ConnectProjects") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						// Connect eclipse projects to egit repository
						File gitDir = new File(repoFile, Constants.DOT_GIT);
						repository = Activator.getDefault().getRepositoryCache().lookupRepository(gitDir);
						IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
						GitUtil.connectProjectsToRepo(repository, Arrays.asList(projects));
					} catch (IOException e) {
						Throwables.propagate(e);
					}
					return Status.OK_STATUS;
				}
			};
			connectJob.schedule();
			connectJob.join();

			IProject rootProject = ResourcesPlugin.getWorkspace().getRoot().getProject(rootProjectName);

			final IFile model = rootProject.getFile(new Path(modelName));
			final String fullPath = model.getFullPath().toString();
			final Subscriber subscriber = GitUtil.createSubscriberForComparison(repository, MASTER, MODIFIED,
					model, disposers);
			final IStorageProviderAccessor accessor = new SubscriberStorageAccessor(subscriber);
			final IStorageProvider sourceProvider = accessor.getStorageProvider(model,
					IStorageProviderAccessor.DiffSide.SOURCE);
			final IStorageProvider remoteProvider = accessor.getStorageProvider(model,
					IStorageProviderAccessor.DiffSide.REMOTE);
			final IStorageProvider ancestorProvider = accessor.getStorageProvider(model,
					IStorageProviderAccessor.DiffSide.ORIGIN);
			assertNotNull(sourceProvider);
			assertNotNull(remoteProvider);
			assertNotNull(ancestorProvider);

			final IProgressMonitor m = new NullProgressMonitor();
			final IStorageProviderAccessor storageAccessor = new SubscriberStorageAccessor(subscriber);
			final ITypedElement left = new StorageTypedElement(sourceProvider.getStorage(m), fullPath);
			final ITypedElement right = new StorageTypedElement(remoteProvider.getStorage(m), fullPath);
			final ITypedElement origin = new StorageTypedElement(ancestorProvider.getStorage(m), fullPath);
			ModelResolverRegistry mrr = EMFCompareIDEUIPlugin.getDefault().getModelResolverRegistry();
			IModelResolver resolver = mrr.getBestResolverFor(sourceProvider.getStorage(m));
			final ComparisonScopeBuilder scopeBuilder = new ComparisonScopeBuilder(resolver,
					EMFCompareIDEUIPlugin.getDefault().getModelMinimizerRegistry().getCompoundMinimizer(),
					storageAccessor);
			scope = scopeBuilder.build(left, right, origin, m);

			resourceSets.add((ResourceSet)scope.getLeft());
			resourceSets.add((ResourceSet)scope.getRight());
			resourceSets.add((ResourceSet)scope.getOrigin());

		} catch (IOException e) {
			Throwables.propagate(e);
		} catch (CoreException e) {
			Throwables.propagate(e);
		} catch (InterruptedException e) {
			Throwables.propagate(e);
		}
	}

	public Comparison match() {
		return match(UseIdentifiers.ONLY);
	}

	public Comparison match(UseIdentifiers useIDs) {
		final IMatchEngine matchEngine = DefaultMatchEngine.create(useIDs);
		comparison = matchEngine.match(scope, new BasicMonitor());
		return comparison;
	}

	public Comparison diff() {
		final IDiffProcessor diffBuilder = new DiffBuilder();
		final IDiffEngine diffEngine = new DefaultDiffEngine(diffBuilder);
		diffEngine.diff(comparison, new BasicMonitor());
		return comparison;
	}

	public void req() {
		final IReqEngine reqEngine = new DefaultReqEngine();
		reqEngine.computeRequirements(comparison, new BasicMonitor());
	}

	public void equi() {
		final IEquiEngine equiEngine = new DefaultEquiEngine();
		equiEngine.computeEquivalences(comparison, new BasicMonitor());
	}

	public void conflict() {
		final IConflictDetector conflictDetector = new MatchBasedConflictDetector();
		conflictDetector.detect(comparison, new BasicMonitor());
	}

	public void compare() {
		EMFCompare.builder().build().compare(scope);
	}

	public void postComparisonGMF() {
		final IPostProcessor postProcessor = new CompareDiagramPostProcessor();
		postProcessor.postComparison(comparison, new BasicMonitor());
	}

	public void postMatchUML() {
		final IPostProcessor postProcessor = new UMLPostProcessor();
		postProcessor.postMatch(comparison, new BasicMonitor());
	}

	public void postComparisonUML() {
		final IPostProcessor postProcessor = new UMLPostProcessor();
		postProcessor.postComparison(comparison, new BasicMonitor());
	}

	public void dispose() {
		comparison = null;
		for (ResourceSet rs : resourceSets) {
			EList<Resource> resources = rs.getResources();
			for (Resource resource : resources) {
				TreeIterator<EObject> allContents = EcoreUtil.getAllProperContents(resource, false);
				while (allContents.hasNext()) {
					final EObject next = allContents.next();
					next.eAdapters().clear();
				}
				resource.eAdapters().clear();
			}

			rs.getResources().clear();
			rs.eAdapters().clear();
		}

		resourceSets = null;

		Job cleanJob = new Job("ClearWorkspace") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					// Close & delete projects from workspace
					IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
					for (IProject project : projects) {
						project.close(new NullProgressMonitor());
						project.delete(false, new NullProgressMonitor());
					}
				} catch (CoreException e) {
					Throwables.propagate(e);
				}
				return Status.OK_STATUS;
			}
		};
		cleanJob.schedule();
		try {
			cleanJob.join();
		} catch (InterruptedException e) {
			Throwables.propagate(e);
		}

		if (repository != null) {
			repository.close();
			repository = null;
		}
		for (Runnable disposer : disposers) {
			disposer.run();
		}
		disposers.clear();

		// Delete repository from temp directory
		GitUtil.deleteRepo(repoFile);
	}
}

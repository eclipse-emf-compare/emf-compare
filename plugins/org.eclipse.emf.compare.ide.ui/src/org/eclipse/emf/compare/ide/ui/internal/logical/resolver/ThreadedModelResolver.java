/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import static com.google.common.collect.Sets.difference;
import static com.google.common.collect.Sets.intersection;
import static org.eclipse.emf.compare.ide.ui.internal.util.PlatformElementUtil.adaptAs;
import static org.eclipse.emf.compare.ide.utils.ResourceUtil.createURIFor;
import static org.eclipse.emf.compare.ide.utils.ResourceUtil.hasModelType;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.emf.compare.ide.ui.internal.util.ThreadSafeProgressMonitor;
import org.eclipse.emf.compare.ide.ui.logical.AbstractModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.ide.utils.StorageURIConverter;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * This implementation of an {@link IModelResolver} will look up all of the models located in a set container
 * level of the "starting point" (by default, the containing project) to construct the graph of dependencies
 * between these models.
 * <p>
 * Once this graph is created for the "local" resource, the right and origin (if any) resources will be
 * inferred from the same traversal of resources, though this time expanded with a "top-down" approach : load
 * all models of the traversal from the remote side, then resolve their containment tree to check whether
 * there are other remote resources in the logical model that do not (or "that no longer) exist locally and
 * thus couldn't be discovered in the first resolution phase.
 * </p>
 * <p>
 * All model loading will happen concurrently. At first, a distinct thread will be launched to resolve every
 * model discovered in the container we're browsing. Then, each thread can and will launch separate threads to
 * resolve the set of dependencies discovered "under" the model they are in charge of resolving.
 * </p>
 * <p>
 * No model will be loaded twice, since this will be aware of what models have already been resolved, thus
 * ignoring duplicate resolving demands.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ThreadedModelResolver extends AbstractModelResolver {

	/** This can be used in order to convert an Iterable of IStorages to an Iterable over the storage's URIs. */
	private static final Function<IStorage, URI> AS_URI = new Function<IStorage, URI>() {
		public URI apply(IStorage input) {
			if (input != null) {
				return createURIFor(input);
			}
			return null;
		}
	};

	/**
	 * Keeps track of the discovered dependency graph for local resources.
	 * <p>
	 * Model resolvers are created from the extension point registry, so we know there will be a single
	 * instance of our resolver for a single run of Eclipse (even across multiple comparisons). We also assume
	 * that this graph won't turn to be a memory hog since we're only keeping track of URIs, and at the worst
	 * no more URIs than there are resources in the user's workspace. We can thus keep this graph around to
	 * avoid multiple crawlings of the same models. Team, as well as the EMFResourceMapping, tend to be
	 * over-enthusiast with the resolution of model traversals. For example, a single
	 * "right-click -> compare with -> commit..." with EGit ends up calling 8 distinct times for the resource
	 * traversal of the selected resource.
	 * </p>
	 */
	private final Graph<URI> dependencyGraph;

	/**
	 * We'll keep track of what's already been resolved to avoid duplicate jobs.
	 */
	private Set<URI> resolvedResources;

	/**
	 * Will keep track of any error or warning that can arise during the loading of the resources.
	 */
	private BasicDiagnostic diagnostic;

	/**
	 * Keeps track of the URIs which we are currently resolving (or which are queued for resolution).
	 * <p>
	 * This along with {@link #resolvedResources} will prevent multiple "duplicate" resolution threads to be
	 * queued. We assume that this will be sufficient to prevent duplicates, and the resolution threads
	 * themselves won't check whether their target has already been resolved before starting.
	 * </p>
	 */
	private final Set<URI> currentlyResolving;

	/** Thread pool for our resolving threads. */
	private ListeningExecutorService resolvingPool;

	/** Thread pool for our unloading threads. */
	private ListeningExecutorService unloadingPool;

	/**
	 * An executor service will be used to shut down the {@link #unloadingPool} and the {@link #resolvingPool}
	 * .
	 */
	private ListeningExecutorService terminator;

	/** Tracks if shutdown of {@link #unloadingPool} and {@link #resolvingPool} is currently in progress. */
	private final AtomicBoolean shutdownInProgress;

	/**
	 * This will lock will prevent concurrent modifications of this resolver's fields. Most notably,
	 * {@link #currentlyResolving}, {@link #resolvedResources} and {@link #diagnostic} must not be accessed
	 * concurrently by two threads at once.
	 */
	private final ReentrantLock lock;

	/**
	 * This resolver will not accept two model resolutions at once.
	 * <p>
	 * Any thread trying to call a model resolution process through either of the three "resolve*" APIs will
	 * have to wait for this condition to be true before starting.
	 * </p>
	 */
	private final Condition notResolving;

	/** Condition to await for all current {@link ResourceResolver} threads to terminate. */
	private final Condition resolutionEnd;

	/**
	 * This resolver will keep a resource listener over the workspace in order to keep its dependencies graph
	 * in sync.
	 * <p>
	 * We're building the dependency graph on-demand and keep it around between invocations. This listener
	 * will allow us to update the graph on-demand by keeping track of the removed and changed resources since
	 * we were last called.
	 * </p>
	 */
	private ModelResourceListener resourceListener;

	/** Default constructor. */
	public ThreadedModelResolver() {
		this.dependencyGraph = new Graph<URI>();
		this.lock = new ReentrantLock(true);
		this.notResolving = lock.newCondition();
		this.resolutionEnd = lock.newCondition();
		this.currentlyResolving = new HashSet<URI>();
		this.shutdownInProgress = new AtomicBoolean(false);
	}

	/**
	 * Creates the thread pools of this resolver. We cannot keep pools between resolving call because in case
	 * of cancellation, we have to shutdown the pool to exit early.
	 */
	private void createThreadPools() {
		final int availableProcessors = Runtime.getRuntime().availableProcessors();
		ThreadFactory resolvingThreadFactory = new ThreadFactoryBuilder().setNameFormat(
				"EMFCompare-ResolvingThread-%d") //$NON-NLS-1$
				.build();
		this.resolvingPool = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(
				availableProcessors, resolvingThreadFactory));
		ThreadFactory unloadingThreadFactory = new ThreadFactoryBuilder().setNameFormat(
				"EMFCompare-UnloadingThread-%d") //$NON-NLS-1$
				.build();
		this.unloadingPool = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(
				availableProcessors, unloadingThreadFactory));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize() {
		this.resourceListener = new ModelResourceListener();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceListener);

		this.terminator = MoreExecutors.listeningDecorator(Executors
				.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat(
						"EMFCompare-ThreadPoolShutdowner-%d").setPriority(Thread.MAX_PRIORITY).build())); //$NON-NLS-1$
	}

	/** {@inheritDoc} */
	@Override
	public void dispose() {
		terminator.shutdown();
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceListener);
		super.dispose();
	}

	/**
	 * Shutdown {@link #resolvingPool} and {@link #unloadingPool} and set these two fields to null.
	 */
	private void shutdownPools() {
		if (!shutdownAndAwaitTermination(resolvingPool) || !shutdownAndAwaitTermination(unloadingPool)) {
			EMFCompareIDEUIPlugin.getDefault().log(IStatus.WARNING,
					"Thread pools have not been properly stopped"); //$NON-NLS-1$
		}
		resolvingPool = null;
		unloadingPool = null;
	}

	/**
	 * Shuts down an {@link ExecutorService} in two phases, first by calling
	 * {@link ExecutorService#shutdown() shutdown} to reject incoming tasks, and then calling
	 * {@link ExecutorService#shutdownNow() shutdownNow}, if necessary, to cancel any lingering tasks. Returns
	 * true if the pool has been properly shutdown, false otherwise.
	 * <p>
	 * Copy/pasted from {@link ExecutorService} javadoc.
	 * 
	 * @param pool
	 *            the pool to shutdown
	 * @return true if the pool has been properly shutdown, false otherwise.
	 */
	private static boolean shutdownAndAwaitTermination(ExecutorService pool) {
		boolean ret = true;
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being canceled
				if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
					ret = false;
				}
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
			ret = false;
		}
		return ret;
	}

	/** {@inheritDoc} */
	public boolean canResolve(IStorage sourceStorage) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Note that no two threads will be able to resolve models at once : all three "resolve*" methods will
	 * lock internally to prevent multiple resolutions at once. Though this shouldn't happen unless the user
	 * calls multiple comparisons one after the other in quick succession, we use this locking to prevent
	 * potential unforeseen interactions.
	 * </p>
	 */
	public StorageTraversal resolveLocalModel(IResource start, IProgressMonitor monitor)
			throws InterruptedException {
		if (!(start instanceof IFile)) {
			return new StorageTraversal(new LinkedHashSet<IStorage>());
		}

		ThreadSafeProgressMonitor subMonitor = null;
		lock.lockInterruptibly();
		try {
			subMonitor = new ThreadSafeProgressMonitor(SubMonitor.convert(monitor, 100));
			while (!currentlyResolving.isEmpty()) {
				notResolving.await();
			}

			setupResolving();

			if (getResolutionScope() != CrossReferenceResolutionScope.SELF) {
				final SynchronizedResourceSet resourceSet = new SynchronizedResourceSet();
				updateDependencies(resourceSet, (IFile)start, subMonitor);
				updateChangedResources(resourceSet, subMonitor);
			}

			while (!currentlyResolving.isEmpty()) {
				resolutionEnd.await();
			}

			if (subMonitor.isCanceled()) {
				throw new OperationCanceledException();
			}

			final Set<IStorage> traversalSet = resolveTraversal((IFile)start, Collections.<URI> emptySet());
			StorageTraversal traversal = new StorageTraversal(traversalSet, diagnostic);

			return traversal;
		} finally {
			try {
				finalizeResolving();

				if (subMonitor != null) {
					subMonitor.setWorkRemaining(0);
				}
			} finally {
				notResolving.signal();
				lock.unlock();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Note that no two threads will be able to resolve models at once : all three "resolve*" methods will
	 * lock internally to prevent multiple resolutions at once. Though this shouldn't happen unless the user
	 * calls multiple comparisons one after the other in quick succession, we use this locking to prevent
	 * potential unforeseen interactions.
	 * </p>
	 */
	public SynchronizationModel resolveLocalModels(IResource left, IResource right, IResource origin,
			IProgressMonitor monitor) throws InterruptedException {

		ThreadSafeProgressMonitor subMonitor = new ThreadSafeProgressMonitor(SubMonitor.convert(monitor, 100));
		try {
			if (!(left instanceof IFile && right instanceof IFile && (origin == null || origin instanceof IFile))) {
				return resolveNonFileLocalModels(left, right, origin, subMonitor);
			} else {
				return resolveFileLocalModel(left, right, origin, subMonitor);
			}
		} finally {
			subMonitor.setWorkRemaining(0);
		}
	}

	private SynchronizationModel resolveNonFileLocalModels(IResource left, IResource right, IResource origin,
			ThreadSafeProgressMonitor subMonitor) throws InterruptedException {
		// Sub-optimal implementation, we'll only try and resolve each side individually
		final StorageTraversal leftTraversal = resolveLocalModel(left, subMonitor);
		final StorageTraversal rightTraversal = resolveLocalModel(right, subMonitor);
		final StorageTraversal originTraversal;
		if (origin != null) {
			originTraversal = resolveLocalModel(origin, subMonitor);
		} else {
			originTraversal = new StorageTraversal(Sets.<IStorage> newLinkedHashSet());
		}

		return new SynchronizationModel(leftTraversal, rightTraversal, originTraversal);
	}

	private SynchronizationModel resolveFileLocalModel(IResource left, IResource right, IResource origin,
			ThreadSafeProgressMonitor monitor) throws InterruptedException {

		lock.lockInterruptibly();
		try {
			while (!currentlyResolving.isEmpty()) {
				notResolving.await();
			}

			setupResolving();

			if (getResolutionScope() != CrossReferenceResolutionScope.SELF) {
				final SynchronizedResourceSet resourceSet = new SynchronizedResourceSet();
				updateDependencies(resourceSet, (IFile)left, monitor);
				updateDependencies(resourceSet, (IFile)right, monitor);
				if (origin instanceof IFile) {
					updateDependencies(resourceSet, (IFile)origin, monitor);
				}
				updateChangedResources(resourceSet, monitor);
			}

			final URI leftURI = createURIFor((IFile)left);
			final URI rightURI = createURIFor((IFile)right);
			final URI originURI;
			final Set<IFile> startingPoints;
			if (origin instanceof IFile) {
				startingPoints = ImmutableSet.of((IFile)left, (IFile)right, (IFile)origin);
				originURI = createURIFor((IFile)origin);
			} else {
				startingPoints = ImmutableSet.of((IFile)left, (IFile)right);
				originURI = null;
			}

			while (!currentlyResolving.isEmpty()) {
				resolutionEnd.await();
			}

			if (monitor.isCanceled()) {
				throw new OperationCanceledException();
			}

			final Set<IStorage> leftTraversal;
			final Set<IStorage> rightTraversal;
			final Set<IStorage> originTraversal;
			if (origin instanceof IFile) {
				leftTraversal = resolveTraversal((IFile)left, ImmutableSet.of(rightURI, originURI));
				rightTraversal = resolveTraversal((IFile)right, ImmutableSet.of(leftURI, originURI));
				originTraversal = resolveTraversal((IFile)origin, ImmutableSet.of(leftURI, rightURI));
			} else {
				leftTraversal = resolveTraversal((IFile)left, Collections.singleton(rightURI));
				rightTraversal = resolveTraversal((IFile)right, Collections.singleton(leftURI));
				originTraversal = Collections.emptySet();
			}

			/*
			 * If one resource of the logical model was pointing to both (or "all three") of our starting
			 * elements, we'll have way too many things in our traversal. We need to remove the intersection
			 * before going any further.
			 */
			Set<IStorage> intersection = intersection(leftTraversal, rightTraversal);
			if (!originTraversal.isEmpty()) {
				intersection = intersection(intersection, originTraversal);
			}
			logCoherenceThreats(Iterables.transform(startingPoints, AS_URI), Iterables.transform(
					intersection, AS_URI));

			final Set<IStorage> actualLeft = new LinkedHashSet<IStorage>(difference(leftTraversal,
					intersection));
			final Set<IStorage> actualRight = new LinkedHashSet<IStorage>(difference(rightTraversal,
					intersection));
			final Set<IStorage> actualOrigin = new LinkedHashSet<IStorage>(difference(originTraversal,
					intersection));
			final SynchronizationModel synchronizationModel = new SynchronizationModel(new StorageTraversal(
					actualLeft), new StorageTraversal(actualRight), new StorageTraversal(actualOrigin),
					diagnostic);

			return synchronizationModel;
		} finally {
			try {
				finalizeResolving();
			} finally {
				notResolving.signal();
				lock.unlock();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Note that no two threads will be able to resolve models at once : all three "resolve*" methods will
	 * lock internally to prevent multiple resolutions at once. Though this shouldn't happen unless the user
	 * calls multiple comparisons one after the other in quick succession, we use this locking to prevent
	 * potential unforeseen interactions.
	 * </p>
	 */
	public SynchronizationModel resolveModels(IStorageProviderAccessor storageAccessor, IStorage left,
			IStorage right, IStorage origin, IProgressMonitor monitor) throws InterruptedException {

		ThreadSafeProgressMonitor subMonitor = null;
		lock.lockInterruptibly();
		try {
			subMonitor = new ThreadSafeProgressMonitor(SubMonitor.convert(monitor, 100));

			while (!currentlyResolving.isEmpty()) {
				notResolving.await();
			}

			setupResolving();

			final IFile leftFile = adaptAs(left, IFile.class);

			/*
			 * If we have a local side to this comparison, resolve it first, then use this result to infer the
			 * remote sides' traversal. Otherwise, resolve all three side in a simple top-down approach.
			 */
			final SynchronizationModel synchronizationModel;
			if (leftFile != null) {
				synchronizationModel = resolveModelsWithLocal(storageAccessor, leftFile, right, origin,
						subMonitor);
			} else {
				synchronizationModel = resolveRemoteModels(storageAccessor, left, right, origin, subMonitor);
			}

			return synchronizationModel;
		} finally {
			try {
				finalizeResolving();

				if (subMonitor != null) {
					subMonitor.setWorkRemaining(0);
				}
			} finally {
				notResolving.signal();
				lock.unlock();
			}
		}
	}

	/**
	 * This should be call before starting any model resolution but it must not be call if another resolution
	 * is already running (i.e. it must be call after an {@link #notResolving}.await()).
	 */
	private void setupResolving() {
		createThreadPools();
		resolvedResources = new LinkedHashSet<URI>();
		diagnostic = new BasicDiagnostic(EMFCompareIDEUIPlugin.PLUGIN_ID, 0, null, new Object[0]);
	}

	/**
	 * This is the counterpart of the {@link #setupResolving()} method. This should be called in a finally
	 * block everywhere {@link #setupResolving()} is called.
	 */
	private void finalizeResolving() {
		if (!shutdownInProgress.get()) {
			shutdownPools();
		}

		if (diagnostic.getSeverity() >= Diagnostic.ERROR) {
			// something bad (or a cancel request) happened during resolution, so we invalidate the
			// dependency graph to avoid weird behavior next time the resolution is called.
			dependencyGraph.clear();
		}

		resolvedResources = null;
		diagnostic = null;
	}

	/**
	 * The 'left' model we've been fed is a local file. We'll assume that the whole 'left' side of this
	 * comparison is local and resolve everything for that side as we would for local comparisons : update the
	 * dependency graph according to our resource listener, lookup for cross-references to/from the left
	 * resource according to the {@link #getResolutionScope() resolution scope}... Once we've resolved the
	 * local traversal, we'll use that as a base to infer the two remote sides, then "augment" it with the
	 * outgoing references of the remote variants of these resources.
	 * 
	 * @param storageAccessor
	 *            The accessor that can be used to retrieve synchronization information between our resources.
	 * @param left
	 *            File corresponding to the left side of this comparison.
	 * @param right
	 *            "starting point" of the traversal to resolve as the right logical model.
	 * @param origin
	 *            "starting point" of the traversal to resolve as the origin logical model (common ancestor of
	 *            left and right). Can be <code>null</code>.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 * @return A traversal corresponding to all resources composing the given file's logical model.
	 * @throws InterruptedException
	 *             Thrown if the resolution is cancelled or interrupted one way or another.
	 */
	private SynchronizationModel resolveModelsWithLocal(IStorageProviderAccessor storageAccessor, IFile left,
			IStorage right, IStorage origin, ThreadSafeProgressMonitor monitor) throws InterruptedException {

		// Update changes and compute dependencies for left
		// Then load the same set of resources for the remote sides, completing it top-down

		if (getResolutionScope() != CrossReferenceResolutionScope.SELF) {
			final SynchronizedResourceSet resourceSet = new SynchronizedResourceSet();
			updateDependencies(resourceSet, left, monitor);
			updateChangedResources(resourceSet, monitor);
		}

		while (!currentlyResolving.isEmpty()) {
			resolutionEnd.await();
		}
		if (monitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		final Set<IStorage> leftTraversal = resolveTraversal(left, Collections.<URI> emptySet());

		final Set<IStorage> rightTraversal = resolveRemoteTraversal(storageAccessor, right, leftTraversal,
				DiffSide.REMOTE, monitor);
		final Set<IStorage> originTraversal;
		if (origin != null) {
			originTraversal = resolveRemoteTraversal(storageAccessor, origin, leftTraversal, DiffSide.ORIGIN,
					monitor);
		} else {
			originTraversal = Collections.emptySet();
		}

		final SynchronizationModel synchronizationModel = new SynchronizationModel(new StorageTraversal(
				leftTraversal), new StorageTraversal(rightTraversal), new StorageTraversal(originTraversal),
				diagnostic);

		return synchronizationModel;
	}

	/**
	 * All three sides we've been fed are remote. We'll resolve all three with a simple a top-down algorithm
	 * (detect only outgoing cross-references).
	 * 
	 * @param storageAccessor
	 *            The accessor that can be used to retrieve synchronization information between our resources.
	 * @param left
	 *            "starting point" of the traversal to resolve as the left logical model.
	 * @param right
	 *            "starting point" of the traversal to resolve as the right logical model.
	 * @param origin
	 *            "starting point" of the traversal to resolve as the origin logical model (common ancestor of
	 *            left and right). Can be <code>null</code>.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 * @return A traversal corresponding to all resources composing the given file's logical model.
	 * @throws InterruptedException
	 *             Thrown if the resolution is cancelled or interrupted one way or another.
	 */
	private SynchronizationModel resolveRemoteModels(IStorageProviderAccessor storageAccessor, IStorage left,
			IStorage right, IStorage origin, ThreadSafeProgressMonitor monitor) throws InterruptedException {

		final Set<IStorage> leftTraversal = resolveRemoteTraversal(storageAccessor, left, Collections
				.<IStorage> emptySet(), DiffSide.SOURCE, monitor);
		final Set<IStorage> rightTraversal = resolveRemoteTraversal(storageAccessor, right, Collections
				.<IStorage> emptySet(), DiffSide.REMOTE, monitor);
		final Set<IStorage> originTraversal;
		if (origin != null) {
			originTraversal = resolveRemoteTraversal(storageAccessor, origin, Collections
					.<IStorage> emptySet(), DiffSide.ORIGIN, monitor);
		} else {
			originTraversal = Collections.emptySet();
		}
		final SynchronizationModel synchronizationModel = new SynchronizationModel(new StorageTraversal(
				leftTraversal), new StorageTraversal(rightTraversal), new StorageTraversal(originTraversal),
				diagnostic);

		return synchronizationModel;
	}

	/**
	 * Checks the current state of our {@link #resourceListener} and updates the dependency graph for all
	 * resources that have been changed since we last checked.
	 * 
	 * @param resourceSet
	 *            The resource set in which to load our temporary resources.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 */
	private void updateChangedResources(SynchronizedResourceSet resourceSet, ThreadSafeProgressMonitor monitor) {
		final Set<URI> removedURIs = difference(resourceListener.popRemovedURIs(), resolvedResources);
		final Set<URI> changedURIs = difference(resourceListener.popChangedURIs(), resolvedResources);

		dependencyGraph.removeAll(removedURIs);

		// We need to re-resolve the changed resources, along with their direct parents
		final Set<URI> recompute = new LinkedHashSet<URI>(changedURIs);
		for (URI changed : changedURIs) {
			if (dependencyGraph.contains(changed)) {
				recompute.addAll(dependencyGraph.getDirectParents(changed));
			}
		}
		dependencyGraph.removeAll(recompute);

		for (URI changed : recompute) {
			demandResolve(resourceSet, changed, monitor);
		}
	}

	/**
	 * Update the dependency graph to make sure that it contains the given file.
	 * <p>
	 * If the graph does not yet contain this file, we'll try and find cross-references outgoing from and/or
	 * incoming to the given file, depending on the current {@link #getResolutionScope() resolution scope}.
	 * </p>
	 * 
	 * @param resourceSet
	 *            The resource set in which to load the temporary resources.
	 * @param file
	 *            The file which we need to be present in the dependency graph.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 */
	private void updateDependencies(SynchronizedResourceSet resourceSet, IFile file,
			ThreadSafeProgressMonitor monitor) {
		final URI expectedURI = createURIFor(file);
		if (!dependencyGraph.contains(expectedURI)) {
			final IResource startingPoint = getResolutionStartingPoint(file);
			final ModelResourceVisitor modelVisitor = new ModelResourceVisitor(resourceSet, monitor);
			try {
				startingPoint.accept(modelVisitor);
			} catch (CoreException e) {
				safeMergeDiagnostic(BasicDiagnostic.toDiagnostic(e));
			}
		}
	}

	/**
	 * Returns the starting point for the resolution of the given file's logical model according to
	 * {@link #getResolutionScope()}.
	 * 
	 * @param file
	 *            The file which logical model we need to add to the current {@link #dependencyGraph}.
	 * @return Starting point for this file's logical model resolution.
	 * @see CrossReferenceResolutionScope
	 */
	private IResource getResolutionStartingPoint(IFile file) {
		final IResource startingPoint;
		switch (getResolutionScope()) {
			case WORKSPACE:
				startingPoint = ResourcesPlugin.getWorkspace().getRoot();
				break;
			case PROJECT:
				startingPoint = file.getProject();
				break;
			case CONTAINER:
				startingPoint = file.getParent();
				break;
			case OUTGOING:
				// fall through, the difference between SELF and OUTGOING will only come later on
			case SELF:
				// fall through
			default:
				startingPoint = file;
				break;
		}
		return startingPoint;
	}

	/**
	 * Tells this resolver how much of the dependency graph should be created at once. Note that this value
	 * may change during a resolution, which sole "visible" effect would be to prevent resolution of further
	 * outgoing references if the new value is "SELF".
	 * 
	 * @return The current resolution scope.
	 */
	private CrossReferenceResolutionScope getResolutionScope() {
		final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault().getPreferenceStore();
		if (store.getBoolean(EMFCompareUIPreferences.DISABLE_RESOLVERS_PREFERENCE)) {
			return CrossReferenceResolutionScope.SELF;
		}
		final String stringValue = store.getString(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE);
		return CrossReferenceResolutionScope.valueOf(stringValue);
	}

	private Set<IStorage> resolveTraversal(IFile file, Set<URI> bounds) {
		final Set<IStorage> traversal = new LinkedHashSet<IStorage>();

		final Iterable<URI> dependencies = getDependenciesOf(file, bounds);
		for (URI uri : dependencies) {
			traversal.add(getFileAt(uri));
		}
		return traversal;
	}

	private Set<IStorage> resolveRemoteTraversal(IStorageProviderAccessor storageAccessor, IStorage start,
			Set<IStorage> localVariants, DiffSide side, ThreadSafeProgressMonitor monitor)
			throws InterruptedException {
		final SynchronizedResourceSet resourceSet = new SynchronizedResourceSet();
		final StorageURIConverter converter = new RevisionedURIConverter(resourceSet.getURIConverter(),
				storageAccessor, side);
		resourceSet.setURIConverter(converter);

		resolvedResources = new LinkedHashSet<URI>();

		for (IStorage local : localVariants) {
			/*
			 * FIXME check that the IResourceVariantTrees support parallel accesses... or make sure that we do
			 * not use multiple thread to resolve the remote variants. For now, we'll use threads.
			 */
			final URI expectedURI = ResourceUtil.createURIFor(local);
			demandRemoteResolve(resourceSet, expectedURI, monitor);
		}

		final URI startURI = ResourceUtil.createURIFor(start);
		demandRemoteResolve(resourceSet, startURI, monitor);

		while (!currentlyResolving.isEmpty()) {
			resolutionEnd.await();
		}

		if (monitor.isCanceled()) {
			throw new OperationCanceledException();
		}

		resolvedResources = null;

		return converter.getLoadedRevisions();
	}

	private Iterable<URI> getDependenciesOf(IFile file, Set<URI> bounds) {
		final URI expectedURI = ResourceUtil.createURIFor(file);

		final Iterable<URI> dependencies;
		switch (getResolutionScope()) {
			case WORKSPACE:
				dependencies = dependencyGraph.getSubgraphContaining(expectedURI, bounds);
				break;
			case PROJECT:
				final Set<URI> allDependencies = dependencyGraph.getSubgraphContaining(expectedURI, bounds);
				final IResource project = file.getProject();
				dependencies = Iterables.filter(allDependencies, isInContainer(project));
				break;
			case CONTAINER:
				final Set<URI> allDependencies1 = dependencyGraph.getSubgraphContaining(expectedURI, bounds);
				final IResource container = file.getParent();
				dependencies = Iterables.filter(allDependencies1, isInContainer(container));
				break;
			case OUTGOING:
				dependencies = dependencyGraph.getTreeFrom(expectedURI, bounds);
				break;
			case SELF:
				// fall through
			default:
				dependencies = Collections.singleton(expectedURI);
				break;
		}
		return dependencies;
	}

	/**
	 * Returns the IFile located at the given URI.
	 * 
	 * @param uri
	 *            URI we need the file for.
	 * @return The IFile located at the given URI.
	 */
	private IFile getFileAt(URI uri) {
		final StringBuilder path = new StringBuilder();
		List<String> segments = uri.segmentsList();
		if (uri.isPlatformResource()) {
			segments = segments.subList(1, segments.size());
		}
		for (String segment : segments) {
			path.append(URI.decode(segment)).append('/');
		}
		return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path.toString()));
	}

	/**
	 * This predicate can be used to check wether a given URI points to a workspace resource contained in the
	 * given container.
	 * 
	 * @param container
	 *            The container in which we need the resources to be contained.
	 * @return A ready to use predicate.
	 */
	private Predicate<URI> isInContainer(final IResource container) {
		return new Predicate<URI>() {
			public boolean apply(URI input) {
				if (input != null) {
					final IFile pointedFile = getFileAt(input);
					if (pointedFile != null) {
						return container.getLocation().isPrefixOf(pointedFile.getLocation());
					}
				}
				return false;
			}
		};
	}

	/**
	 * When executing local comparisons, we resolve the full logical model of both (or "all three of") the
	 * compared files.
	 * <p>
	 * If there is one resource in the scope that references all of these starting points, then we'll have
	 * perfectly identical logical models for all comparison sides. Because of that, we need to constrain the
	 * logical model of each starting point to only parts that are not accessible from other starting points.
	 * This might cause coherence issues as merging could thus "break" references from other files to our
	 * compared ones.
	 * </p>
	 * <p>
	 * This method will be used to browse the files that are removed from the logical model, and log a warning
	 * for the files that are removed even though they are "parents" of one of the starting points.
	 * </p>
	 * 
	 * @param startingPoints
	 *            Starting points of the comparison.
	 * @param removedFromModel
	 *            All files that have been removed from the comparison scope.
	 */
	private void logCoherenceThreats(Iterable<URI> startingPoints, Iterable<URI> removedFromModel) {
		final Set<URI> coherenceThreats = new LinkedHashSet<URI>();
		for (URI start : startingPoints) {
			for (URI removed : removedFromModel) {
				if (dependencyGraph.hasChild(removed, start)) {
					coherenceThreats.add(removed);
				}
			}
		}

		if (!coherenceThreats.isEmpty()) {
			// FIXME: should be added to diagnostic instead
			final String message = EMFCompareIDEUIMessages.getString("ModelResolver.coherenceWarning"); //$NON-NLS-1$
			final String details = Iterables.toString(coherenceThreats);
			EMFCompareIDEUIPlugin.getDefault().getLog().log(
					new Status(IStatus.WARNING, EMFCompareIDEUIPlugin.PLUGIN_ID, message + '\n' + details));
		}
	}

	/**
	 * Allows callers to launch the loading and resolution of the model pointed at by the given URI.
	 * <p>
	 * This will check whether the given storage isn't already being resolved, then submit a job to the
	 * {@link #resolvingPool} to load and resolve the model in a separate thread.
	 * </p>
	 * 
	 * @param resourceSet
	 *            The resource set in which to load the resource.
	 * @param uri
	 *            The uri we are to try and load as a model.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 * @see ResourceResolver
	 */
	protected void demandResolve(SynchronizedResourceSet resourceSet, URI uri,
			final ThreadSafeProgressMonitor monitor) {
		if (isInterruptedOrCanceled(monitor)) {
			demandResolvingAndUnloadingPoolShutdown();
			return;
		}

		lock.lock();
		try {
			if (resolvedResources.add(uri) && currentlyResolving.add(uri)) {
				// Regardless of the amount of progress reported so far, use 0.1% of the space remaining in
				// the monitor to process the next node.
				monitor.setWorkRemaining(1000);
				ListenableFuture<?> future = resolvingPool.submit(new ResourceResolver(resourceSet, uri,
						monitor));
				Futures.addCallback(future, new ResolvingFutureCallback(monitor, uri));
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Allows callers to launch the loading and resolution of the model pointed at by the given URI, without
	 * updating the {@link #dependencyGraph} along the way.
	 * <p>
	 * This will check whether the given storage isn't already being resolved, then submit a job to the
	 * {@link #resolvingPool} to load and resolve the model in a separate thread.
	 * </p>
	 * 
	 * @param resourceSet
	 *            The resource set in which to load the resource.
	 * @param uri
	 *            The uri we are to try and load as a model.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 */
	protected void demandRemoteResolve(SynchronizedResourceSet resourceSet, URI uri,
			final ThreadSafeProgressMonitor monitor) {
		if (isInterruptedOrCanceled(monitor)) {
			demandResolvingAndUnloadingPoolShutdown();
			return;
		}

		lock.lock();
		try {
			if (resolvedResources.add(uri) && currentlyResolving.add(uri)) {
				// Regardless of the amount of progress reported so far, use 0.1% of the space remaining in
				// the monitor to process the next node.
				monitor.setWorkRemaining(1000);
				ListenableFuture<?> future = resolvingPool.submit(new RemoteResourceResolver(resourceSet,
						uri, monitor));
				Futures.addCallback(future, new ResolvingFutureCallback(monitor, uri));
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Allows callers to launch the unloading of the given resource.
	 * <p>
	 * Do note that even though this is called "unload", we won't actually call {@link Resource#unload()} on
	 * the given resource unless we deem it necessary (we only call if for UML because of the CacheAdapter)
	 * for now. This will only remove the resource from its containing resource set so as to allow it to be
	 * garbage collected.
	 * </p>
	 * 
	 * @param resourceSet
	 *            The resource set containing the resource to be unloaded.
	 * @param resource
	 *            The resource to unload.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 * @see ResourceUnloader
	 */
	protected void demandUnload(SynchronizedResourceSet resourceSet, Resource resource,
			final ThreadSafeProgressMonitor monitor) {

		// Regardless of the amount of progress reported so far, use 0.1% of the space remaining in the
		// monitor to process the next node.
		monitor.setWorkRemaining(1000);
		ListenableFuture<?> future = unloadingPool
				.submit(new ResourceUnloader(resourceSet, resource, monitor));
		Futures.addCallback(future, new FutureCallback<Object>() {
			public void onSuccess(Object result) {
				if (!isInterruptedOrCanceled(monitor)) {
					monitor.worked(1);
				}
			}

			public void onFailure(Throwable t) {
				if (!isInterruptedOrCanceled(monitor)) {
					monitor.worked(1);
					safeMergeDiagnostic(BasicDiagnostic.toDiagnostic(t));
				}
			}
		});
	}

	/**
	 * Thread safely merge the given diagnostic to the {@link #diagnostic} field.
	 * 
	 * @param resourceDiagnostic
	 *            the diagnostic to be added to the global diagnostic.
	 */
	private void safeMergeDiagnostic(Diagnostic resourceDiagnostic) {
		lock.lock();
		try {
			diagnostic.merge(resourceDiagnostic);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Checks if the current thread is interrupted or if the given monitor has been canceled.
	 * 
	 * @param monitor
	 *            the monitor to check
	 * @return true if the current thread has been canceled, false otherwise.
	 */
	private boolean isInterruptedOrCanceled(IProgressMonitor monitor) {
		return Thread.currentThread().isInterrupted() || monitor.isCanceled();
	}

	/**
	 * If {@link #shutdownInProgress shutdown has not been requested before}, it submits a new task to
	 * {@link #shutdownPools() shut down} {@link #resolvingPool} and {@link #unloadingPool}. Do nothing if
	 * current thread already is interrupted.
	 */
	private void demandResolvingAndUnloadingPoolShutdown() {
		if (!Thread.currentThread().isInterrupted()) {
			if (shutdownInProgress.compareAndSet(false, true)) {
				Runnable runnable = new Runnable() {
					public void run() {
						shutdownPools();
					}
				};

				ListenableFuture<?> listenableFuture = terminator.submit(runnable);
				Futures.addCallback(listenableFuture, new FutureCallback<Object>() {
					public void onSuccess(Object result) {
						shutdownInProgress.set(false);
					}

					public void onFailure(Throwable t) {
						shutdownInProgress.set(false);
						EMFCompareIDEUIPlugin.getDefault().log(t);
					}
				});
			}
		}
	}

	/**
	 * This will remove the given uri from the {@link #currentlyResolving} set and signal to
	 * {@link #resolutionEnd} if the set is empty afterward. This method must be call by every callback of
	 * resolving tasks.
	 * 
	 * @param uri
	 *            the uri to remove.
	 */
	private void finalizeResolvingTask(URI uri) {
		lock.lock();
		try {
			currentlyResolving.remove(uri);
			if (currentlyResolving.isEmpty()) {
				resolutionEnd.signal();
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * The callback for {@link ResourceResolver} and {@link RemoteResourceResolver} tasks. It will report
	 * progress, log errors and finalize the resolving and as such, possibly signaling the end of the
	 * resolution.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private final class ResolvingFutureCallback implements FutureCallback<Object> {

		/** The monitor to which report progress. */
		private final IProgressMonitor monitor;

		private final URI uri;

		/**
		 * @param monitor
		 */
		private ResolvingFutureCallback(IProgressMonitor monitor, URI uri) {
			this.monitor = monitor;
			this.uri = uri;
		}

		public void onSuccess(Object result) {
			try {
				if (!isInterruptedOrCanceled(monitor)) {
					// do not report progress anymore when the task has been interrupted of canceled. It
					// speeds up the cancellation.
					monitor.worked(1);
				}
			} finally {
				finalizeResolvingTask(uri);
			}
		}

		public void onFailure(Throwable t) {
			try {
				if (!isInterruptedOrCanceled(monitor)) {
					// do not report progress or errors anymore when the task has been interrupted of
					// canceled. It speeds up the cancellation.
					monitor.worked(1);
					safeMergeDiagnostic(BasicDiagnostic.toDiagnostic(t));
				}
			} finally {
				finalizeResolvingTask(uri);
			}
		}
	}

	/**
	 * Implements a runnable that will load the EMF resource pointed at by a given URI, then resolve all of
	 * its cross-referenced resources and update the dependency graph accordingly.
	 * <p>
	 * Once done with the resolution, this thread will spawn an independent job to unload the resource.
	 * </p>
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private class ResourceResolver implements Runnable {
		/** The resource set in which to load the resource. */
		private final SynchronizedResourceSet resourceSet;

		/** URI that needs to be loaded as an EMF model. */
		private final URI uri;

		/** Monitor on which to report progress to the user. */
		private final ThreadSafeProgressMonitor monitor;

		/**
		 * Default constructor.
		 * 
		 * @param resourceSet
		 *            The resource set in which to load the resource.
		 * @param uri
		 *            URI that needs to be loaded as an EMF model.
		 * @param monitor
		 *            Monitor on which to report progress to the user.
		 */
		public ResourceResolver(SynchronizedResourceSet resourceSet, URI uri,
				ThreadSafeProgressMonitor monitor) {
			this.resourceSet = resourceSet;
			this.uri = uri;
			this.monitor = monitor;
		}

		/** {@inheritDoc} */
		public void run() {
			if (isInterruptedOrCanceled(monitor)) {
				demandResolvingAndUnloadingPoolShutdown();
				return;
			}

			final Resource resource = resourceSet.loadResource(uri);
			Diagnostic resourceDiagnostic = EcoreUtil.computeDiagnostic(resource, true);
			if (resourceDiagnostic.getSeverity() >= Diagnostic.WARNING) {
				safeMergeDiagnostic(resourceDiagnostic);
			}
			dependencyGraph.add(uri);
			if (getResolutionScope() != CrossReferenceResolutionScope.SELF) {
				final Set<URI> crossReferencedResources = resourceSet.discoverCrossReferences(resource,
						monitor);
				dependencyGraph.addChildren(uri, crossReferencedResources);
				for (URI crossRef : crossReferencedResources) {
					if (isInterruptedOrCanceled(monitor)) {
						demandResolvingAndUnloadingPoolShutdown();
						// do not return, we want to unload what we've already loaded to avoid leaks.
						break;
					}
					demandResolve(resourceSet, crossRef, monitor);
				}
			}
			demandUnload(resourceSet, resource, monitor);
		}
	}

	/**
	 * Implements a runnable that will load the given EMF resource, then launch the resolution of all
	 * cross-references of this resource in separate threads. This will not update the dependency graph.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private class RemoteResourceResolver implements Runnable {
		/** The resource set in which to load the resource. */
		private final SynchronizedResourceSet resourceSet;

		/** URI that needs to be loaded as an EMF model. */
		private final URI uri;

		/** Monitor on which to report progress to the user. */
		private final ThreadSafeProgressMonitor monitor;

		/**
		 * Constructs a resolver to load a resource from its URI.
		 * 
		 * @param resourceSet
		 *            The resource set in which to load the resource.
		 * @param uri
		 *            The URI that needs to be loaded as an EMF model.
		 * @param monitor
		 *            Monitor on which to report progress to the user.
		 */
		public RemoteResourceResolver(SynchronizedResourceSet resourceSet, URI uri,
				ThreadSafeProgressMonitor monitor) {
			this.resourceSet = resourceSet;
			this.uri = uri;
			this.monitor = monitor;
		}

		/** {@inheritDoc} */
		public void run() {
			if (isInterruptedOrCanceled(monitor)) {
				demandResolvingAndUnloadingPoolShutdown();
				return;
			}

			final Resource resource = resourceSet.loadResource(uri);
			Diagnostic resourceDiagnostic = EcoreUtil.computeDiagnostic(resource, true);
			if (resourceDiagnostic.getSeverity() >= Diagnostic.WARNING) {
				safeMergeDiagnostic(resourceDiagnostic);
			}
			if (getResolutionScope() != CrossReferenceResolutionScope.SELF) {
				final Set<URI> crossReferencedResources = resourceSet.discoverCrossReferences(resource,
						monitor);
				for (URI crossRef : crossReferencedResources) {
					if (isInterruptedOrCanceled(monitor)) {
						demandResolvingAndUnloadingPoolShutdown();
						// do not return, we want to unload what we've already loaded to avoid leaks.
						break;
					}
					demandRemoteResolve(resourceSet, crossRef, monitor);
				}
			}
			demandUnload(resourceSet, resource, monitor);
		}
	}

	/**
	 * Implementation of a Runnable that can be used to unload a given resource and make it
	 * garbage-collectable.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private static class ResourceUnloader implements Runnable {
		/** The resource set from which to unload a resource. */
		private final SynchronizedResourceSet resourceSet;

		/** The resource to unload. */
		private final Resource resource;

		/** Monitor on which to report progress to the user. */
		private final IProgressMonitor monitor;

		/**
		 * Default constructor.
		 * 
		 * @param resourceSet
		 *            The resource set from which to unload a resource.
		 * @param resource
		 *            The resource to unload.
		 * @param monitor
		 *            Monitor on which to report progress to the user.
		 */
		public ResourceUnloader(SynchronizedResourceSet resourceSet, Resource resource,
				IProgressMonitor monitor) {
			this.resourceSet = resourceSet;
			this.resource = resource;
			this.monitor = monitor;
		}

		/** {@inheritDoc} */
		public void run() {
			resourceSet.unload(resource, monitor);
		}
	}

	/**
	 * This implementation of a resource visitor will allow us to browse a given hierarchy and resolve the
	 * models files in contains, as determined by {@link ThreadedModelResolver#MODEL_CONTENT_TYPES}.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
	 * @see ThreadedModelResolver#hasModelType(IFile)
	 */
	private class ModelResourceVisitor implements IResourceVisitor {
		/** Resource set in which to load the model files this visitor will find. */
		private final SynchronizedResourceSet resourceSet;

		/** Monitor on which to report progress to the user. */
		private final ThreadSafeProgressMonitor monitor;

		/**
		 * Default constructor.
		 * 
		 * @param resourceSet
		 *            The resource set in which this visitor will load the model files it finds.
		 * @param monitor
		 *            Monitor on which to report progress to the user.
		 */
		public ModelResourceVisitor(SynchronizedResourceSet resourceSet, ThreadSafeProgressMonitor monitor) {
			this.resourceSet = resourceSet;
			this.monitor = monitor;
		}

		/** {@inheritDoc} */
		public boolean visit(IResource resource) throws CoreException {
			if (isInterruptedOrCanceled(monitor)) {
				demandResolvingAndUnloadingPoolShutdown();
				// cancel the visit
				throw new OperationCanceledException();
			}

			if (resource instanceof IFile) {
				final IFile file = (IFile)resource;
				if (hasModelType(file)) {
					final URI expectedURI = ResourceUtil.createURIFor(file);
					demandResolve(resourceSet, expectedURI, monitor);
				}
				return false;
			}
			return true;
		}
	}

	/**
	 * This will listen to workspace changes and react to all changes on "model" resources as determined by
	 * {@link ThreadedModelResolver#MODEL_CONTENT_TYPES}.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
	 * @see ThreadedModelResolver#hasModelType(IFile)
	 */
	private static class ModelResourceListener implements IResourceChangeListener {
		/** Keeps track of the URIs that need to be reparsed when next we need the dependencies graph . */
		protected final Set<URI> changedURIs;

		/** Tracks the files that have been removed. */
		protected final Set<URI> removedURIs;

		/** Prevents concurrent access to the two internal sets. */
		protected final ReentrantLock internalLock;

		/** Initializes this listener. */
		public ModelResourceListener() {
			this.changedURIs = new LinkedHashSet<URI>();
			this.removedURIs = new LinkedHashSet<URI>();
			this.internalLock = new ReentrantLock();
		}

		/** {@inheritDoc} */
		public void resourceChanged(IResourceChangeEvent event) {
			final IResourceDelta delta = event.getDelta();
			if (delta == null) {
				return;
			}

			/*
			 * We must block any and all threads from using the two internal sets through either
			 * popChangedURIs or popRemovedURIs while we are parsing a resource delta. This particular locking
			 * is here to avoid such misuses.
			 */
			internalLock.lock();
			try {
				delta.accept(new ModelResourceDeltaVisitor());
			} catch (CoreException e) {
				EMFCompareIDEUIPlugin.getDefault().log(e);
			} finally {
				internalLock.unlock();
			}
		}

		/**
		 * Retrieves the set of all changed URIs since we last updated the dependencies graph, and clears it
		 * for subsequent calls.
		 * 
		 * @return The set of all changed URIs since we last updated the dependencies graph.
		 */
		public Set<URI> popChangedURIs() {
			final Set<URI> changed;
			internalLock.lock();
			try {
				changed = ImmutableSet.copyOf(changedURIs);
				changedURIs.clear();
			} finally {
				internalLock.unlock();
			}
			return changed;
		}

		/**
		 * Retrieves the set of all removed URIs since we last updated the dependencies graph, and clears it
		 * for subsequent calls.
		 * 
		 * @return The set of all removed URIs since we last updated the dependencies graph.
		 */
		public Set<URI> popRemovedURIs() {
			final Set<URI> removed;
			internalLock.lock();
			try {
				removed = ImmutableSet.copyOf(removedURIs);
				removedURIs.clear();
			} finally {
				internalLock.unlock();
			}
			return removed;
		}

		/**
		 * Visits a resource delta to collect the changed and removed files' URIs.
		 * 
		 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
		 */
		private class ModelResourceDeltaVisitor implements IResourceDeltaVisitor {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
			 */
			public boolean visit(IResourceDelta delta) throws CoreException {
				/*
				 * Note : the lock (#lock) must be acquired by the current thread _before_ calling #accept()
				 * on this visitor.
				 */
				if (delta.getFlags() == IResourceDelta.MARKERS
						|| delta.getResource().getType() != IResource.FILE) {
					return true;
				}

				final IFile file = (IFile)delta.getResource();
				final URI fileURI = createURIFor(file);
				// We can't check the content type of a removed resource
				if (delta.getKind() == IResourceDelta.REMOVED) {
					removedURIs.add(fileURI);
					changedURIs.remove(fileURI);
				} else if (hasModelType(file)) {
					if ((delta.getKind() & IResourceDelta.CHANGED) != 0) {
						changedURIs.add(fileURI);
						// Probably can't happen, but let's stay on the safe side
						removedURIs.remove(fileURI);
					} else if ((delta.getKind() & IResourceDelta.ADDED) != 0) {
						// If a previously removed resource is changed, we can assume it's been re-added
						if (removedURIs.remove(fileURI)) {
							changedURIs.add(fileURI);
						}
					}
				}

				return true;
			}
		}
	}
}

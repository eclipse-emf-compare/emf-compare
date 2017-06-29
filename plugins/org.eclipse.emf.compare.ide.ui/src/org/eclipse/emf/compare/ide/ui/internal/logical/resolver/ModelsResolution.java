/*******************************************************************************
 * Copyright (c) 2015, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 470268
 *     Martin Fleck - bug 512677
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.transform;
import static org.eclipse.emf.compare.ide.ui.internal.util.PlatformElementUtil.adaptAs;
import static org.eclipse.emf.compare.ide.utils.ResourceUtil.asURI;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.util.ThreadSafeProgressMonitor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.ide.utils.StorageURIConverter;

/**
 * Computation that resolves 2 or 3 storages (left, right and potentially origin).
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ModelsResolution extends AbstractResolution {

	/** Storage Accessor. */
	private final IStorageProviderAccessor storageAccessor;

	/** The left storage. */
	private final IStorage left;

	/** The right storage. */
	private final IStorage right;

	/** The ancestor storage, can be <code>null</code>. */
	private final IStorage origin;

	/** The local resolver. */
	private final IResourceDependencyLocalResolver localResolver;

	/** The remote resolver. */
	private final IResourceDependencyRemoteResolver remoteResolver;

	/**
	 * At least one of {@link #left}, {@link #right} and {@link #origin} must be non-null.
	 * 
	 * @param dependencyProvider
	 *            The dependency provider
	 * @param scheduler
	 *            The muti-thread support to use
	 * @param eventBus
	 *            The event bus
	 * @param monitor
	 *            The progress monitor to use to report progress
	 * @param storageAccessor
	 *            The storage accessor, must not be {@code null}
	 * @param left
	 *            The left storage, can be {@code null}
	 * @param right
	 *            The right storage, can be {@code null}
	 * @param origin
	 *            The ancestor storage, can be {@code null}
	 */
	public ModelsResolution(IResolutionContext context, IProgressMonitor monitor,
			IStorageProviderAccessor storageAccessor, IStorage left, IStorage right, IStorage origin) {
		super(context, monitor);
		this.localResolver = context.getLocalResolver();
		this.remoteResolver = context.getRemoteResolver();
		this.storageAccessor = checkNotNull(storageAccessor);
		this.left = left;
		this.right = right;
		this.origin = origin;
		checkArgument(left != null || right != null || origin != null);
	}

	/**
	 * Executes the resolution.
	 * 
	 * @return The logical model to use to compare the given storages
	 */
	public SynchronizationModel run() {
		if (logger.isDebugEnabled()) {
			logger.debug("run() - START"); //$NON-NLS-1$
		}
		return call(new Callable<SynchronizationModel>() {
			public SynchronizationModel call() throws Exception {
				final IFile leftFile = adaptAs(left, IFile.class);

				final SynchronizationModel synchronizationModel;
				if (leftFile != null) {
					synchronizationModel = resolveModelsWithLocal(leftFile,
							new ThreadSafeProgressMonitor(monitor));
				} else {
					synchronizationModel = resolveRemoteModels(new ThreadSafeProgressMonitor(monitor));
				}
				if (logger.isDebugEnabled()) {
					logger.debug("run() - FINISH"); //$NON-NLS-1$
				}
				return synchronizationModel;
			}
		});
	}

	/**
	 * Overridden to set the work remaining to zero on the progress monitor used.
	 */
	@Override
	protected Runnable getFinalizeResolvingRunnable() {
		return new Runnable() {
			public void run() {
				ModelsResolution.super.getFinalizeResolvingRunnable().run();
				if (monitor != null) {
					monitor.setWorkRemaining(0);
				}
			}
		};
	}

	/**
	 * The 'left' model we've been fed is a local file. We'll assume that the whole 'left' side of this
	 * comparison is local and resolve everything for that side as we would for local comparisons : update the
	 * dependency graph according to our resource listener, lookup for cross-references to/from the left
	 * resource according to the {@link #getResolutionScope() resolution scope}... Once we've resolved the
	 * local traversal, we'll use that as a base to infer the two remote sides, then "augment" it with the
	 * cross-references of the remote variants of these resources.
	 * 
	 * @param leftFile
	 *            File corresponding to the left side of this comparison.
	 * @param tspm
	 *            Monitor on which to report progress to the user.
	 * @return The SynchronizationModel describing the traversals of all three sides of this logical model.
	 * @throws InterruptedException
	 *             Thrown if the resolution is cancelled or interrupted one way or another.
	 */
	private SynchronizationModel resolveModelsWithLocal(final IFile leftFile,
			final ThreadSafeProgressMonitor tspm) throws InterruptedException {
		if (logger.isDebugEnabled()) {
			logger.debug("resolveModelsWithLocal() - updating dependencies"); //$NON-NLS-1$
		}
		// Update changes and compute dependencies for left
		// Then load the same set of resources for the remote sides, completing it top-down
		localResolver.updateDependencies(monitor, diagnostic, leftFile);

		if (tspm.isCanceled()) {
			throw new OperationCanceledException();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("resolveModelsWithLocal() - resolving traversal"); //$NON-NLS-1$
		}
		final Set<IStorage> leftTraversal = resolveTraversal(leftFile, Collections.<URI> emptySet());

		if (logger.isDebugEnabled()) {
			logger.debug("resolveModelsWithLocal() - resolving remote traversal"); //$NON-NLS-1$
		}
		return resolveRemoteTraversals(leftTraversal, tspm);
	}

	/**
	 * All three sides we've been fed are remote. We'll resolve all three with a simple a top-down algorithm
	 * (detect only outgoing cross-references).
	 * 
	 * @param tspm
	 *            Monitor on which to report progress to the user.
	 * @return The SynchronizationModel describing the traversals of all three sides of this logical model.
	 * @throws InterruptedException
	 *             Thrown if the resolution is cancelled or interrupted one way or another.
	 */
	private SynchronizationModel resolveRemoteModels(ThreadSafeProgressMonitor tspm)
			throws InterruptedException {
		if (logger.isDebugEnabled()) {
			logger.debug("resolveRemoteModels() - resolving left remote traversal"); //$NON-NLS-1$
		}
		final Set<IStorage> leftTraversal = resolveRemoteTraversal(left, Collections.<URI> emptySet(),
				DiffSide.SOURCE, tspm);
		if (logger.isDebugEnabled()) {
			logger.debug("resolveRemoteModels() - resolving other remote traversals"); //$NON-NLS-1$
		}
		return resolveRemoteTraversals(leftTraversal, tspm);
	}

	/**
	 * Resolve the remote sides (right and origin, or right alone in case of two-way) of this comparison,
	 * inferring a "starting traversal" from the left side.
	 * <p>
	 * Do note that {@code leftTraversal} <b>will be changed</b> as a result of this call if the right and/or
	 * origin sides contain a reference to another resource that was not found from the left
	 * cross-referencing, yet does exist in the left side.
	 * </p>
	 * 
	 * @param leftTraversal
	 *            The already resolved left traversal, to be augmented if right and/or origin have some new
	 *            resources in their logical model.
	 * @param tspm
	 *            Monitor on which to report progress to the user.
	 * @return The SynchronizationModel describing the traversals of all three sides of this logical model.
	 * @throws InterruptedException
	 *             Thrown if the resolution is cancelled or interrupted one way or another.
	 */
	private SynchronizationModel resolveRemoteTraversals(Set<IStorage> leftTraversal,
			ThreadSafeProgressMonitor tspm) throws InterruptedException {
		if (logger.isDebugEnabled()) {
			logger.debug("resolveRemotetraversals() - START"); //$NON-NLS-1$
		}
		final Set<IStorage> rightTraversal = resolveRemoteTraversal(right,
				Iterables.transform(leftTraversal, asURI()), DiffSide.REMOTE, tspm);
		final Set<IStorage> differenceRightLeft = difference(rightTraversal, asURISet(leftTraversal));
		loadAdditionalRemoteStorages(leftTraversal, rightTraversal, differenceRightLeft, tspm);

		final Set<IStorage> originTraversal;
		if (origin != null) {
			final Set<URI> unionLeftRight = Sets.newLinkedHashSet(
					Iterables.transform(Sets.union(leftTraversal, rightTraversal), asURI()));
			originTraversal = resolveRemoteTraversal(origin, unionLeftRight, DiffSide.ORIGIN, tspm);
			Set<IStorage> differenceOriginLeft = difference(originTraversal, asURISet(leftTraversal));
			Set<IStorage> differenceOriginRight = difference(originTraversal, asURISet(rightTraversal));
			Set<IStorage> additional = symmetricDifference(differenceOriginLeft, differenceOriginRight);
			loadAdditionalRemoteStorages(leftTraversal, rightTraversal, originTraversal, additional, tspm);
		} else {
			originTraversal = Collections.emptySet();
		}
		final SynchronizationModel synchronizationModel = new SynchronizationModel(
				new StorageTraversal(leftTraversal), new StorageTraversal(rightTraversal),
				new StorageTraversal(originTraversal), diagnostic.getDiagnostic());

		if (logger.isDebugEnabled()) {
			logger.debug("resolveRemotetraversals() - FINISH"); //$NON-NLS-1$
		}
		return synchronizationModel;
	}

	/**
	 * If we found some storages in the right traversal that were not part of the left traversal, we need to
	 * check whether they exist in the left, since in such a case they must be considered as part of the same
	 * logical model.
	 * <p>
	 * <b>Important</b> : note that the input {@code left} and {@code right} sets <b>will be modified</b> as a
	 * result of this call if there are any additional storages to load on these sides.
	 * </p>
	 * 
	 * @param leftSet
	 *            Traversal of the left logical model.
	 * @param rightSet
	 *            Traversal of the right logical model.
	 * @param additional
	 *            the addition storages we are to lookup in left.
	 * @param tspm
	 *            Monitor on which to report progress to the user.
	 * @return The set of all additional resources (both on left and right) that have been loaded as a result
	 *         of this call.
	 * @throws InterruptedException
	 *             Thrown if the resolution is cancelled or interrupted one way or another.
	 */
	private Set<IStorage> loadAdditionalRemoteStorages(Set<IStorage> leftSet, Set<IStorage> rightSet,
			Set<IStorage> additional, ThreadSafeProgressMonitor tspm) throws InterruptedException {
		/*
		 * This loop will be extremely costly at best, but we hope the case to be sufficiently rare (and the
		 * new resources well spread when it happens) not to pose an issue in the most frequent cases.
		 */

		// Make sure we properly update the dependency graph if our source side is local
		final boolean sourceIsLocal = Iterables.any(leftSet, new Predicate<IStorage>() {
			public boolean apply(IStorage input) {
				return adaptAs(input, IFile.class) != null;
			}
		});

		final Set<IStorage> additionalStorages = new LinkedHashSet<IStorage>();
		final Set<URI> additionalURIs = new LinkedHashSet<URI>();
		// Have we found new resources in the right as compared to the left?
		Set<IStorage> differenceRightLeft = additional;
		boolean somethingToAdd = !differenceRightLeft.isEmpty();
		while (somethingToAdd) {
			somethingToAdd = false;
			// There's at least one resource in the right that was not found in the left.
			// This might be a new resource added on the right side... but it might also be a cross-reference
			// that's been either removed from left or added in right. In this second case, we need the
			// resource to be present in both traversals to make sure we'll be able to properly detect
			// potential conflicts. However, since this resource could itself be a part of a larger logical
			// model, we need to start the resolving again with it.
			final Set<IStorage> additionalLeft;
			if (sourceIsLocal) {
				additionalLeft = findAdditionalLocalTraversal(leftSet, differenceRightLeft, tspm);
			} else {
				additionalLeft = findAdditionalRemoteTraversal(leftSet, differenceRightLeft, DiffSide.SOURCE,
						tspm);
			}
			if (leftSet.addAll(additionalLeft)) {
				somethingToAdd = true;
				for (IStorage storage : additionalLeft) {
					final URI newURI = asURI().apply(storage);
					if (additionalURIs.add(newURI)) {
						additionalStorages.add(storage);
					}
				}
			}
			// have we only loaded the resources that were present in the right but not in the left, or have
			// we found even more?
			final Set<IStorage> differenceAdditionalLeftRight = difference(additionalLeft,
					asURISet(rightSet));
			// If so, we once more need to augment the right traversal
			final Set<IStorage> additionalRight = findAdditionalRemoteTraversal(rightSet,
					differenceAdditionalLeftRight, DiffSide.REMOTE, tspm);
			if (rightSet.addAll(additionalRight)) {
				somethingToAdd = true;
				for (IStorage storage : additionalRight) {
					final URI newURI = asURI().apply(storage);
					if (additionalURIs.add(newURI)) {
						additionalStorages.add(storage);
					}
				}
			}
			// Start this loop anew if we once again augmented the right further than what we had in
			// left
			if (somethingToAdd) {
				differenceRightLeft = difference(additionalRight, asURISet(leftSet));
			}
		}
		return additionalStorages;
	}

	/**
	 * If we found some storages in the origin traversal that were part of neither the left nor the right
	 * traversals, we need to check whether they exist in them, since in such a case they must be considered
	 * as part of the same logical model.
	 * <p>
	 * <b>Important</b> : note that the input {@code left}, {@code right} and {@code origin} sets <b>will be
	 * modified</b> as a result of this call if there are any additional storages to load on either side.
	 * </p>
	 * 
	 * @param leftSet
	 *            Traversal of the left logical model.
	 * @param rightSet
	 *            Traversal of the right logical model.
	 * @param originSet
	 *            Traversal of the origin logical model.
	 * @param additional
	 *            the set of additional storages we are to lookup in right and left.
	 * @param tspm
	 *            Monitor on which to report progress to the user.
	 * @throws InterruptedException
	 *             Thrown if the resolution is cancelled or interrupted one way or another.
	 */
	private void loadAdditionalRemoteStorages(Set<IStorage> leftSet, Set<IStorage> rightSet,
			Set<IStorage> originSet, Set<IStorage> additional, ThreadSafeProgressMonitor tspm)
			throws InterruptedException {
		// This loop will be extremely costly at best, but we hope the case to be sufficiently rare (and the
		// new resources well spread when it happens) not to pose an issue in the most frequent cases.
		Set<IStorage> additionalStorages = additional;
		while (!additionalStorages.isEmpty()) {
			// There's at least one resource that is in the origin set yet neither in left nor in right.
			final Set<IStorage> additionalLeftRightComparedToOrigin = loadAdditionalRemoteStorages(leftSet,
					rightSet, additionalStorages, tspm);
			// Have we found even more resources to add to the traversal? If so, augment the origin
			// accordingly.
			final Set<IStorage> additionalOrigin = findAdditionalRemoteTraversal(originSet,
					additionalLeftRightComparedToOrigin, DiffSide.ORIGIN, tspm);
			originSet.addAll(additionalOrigin);
			// If we once again found new storages in the origin, restart the loop.
			final Set<IStorage> differenceOriginLeft = difference(additionalOrigin, asURISet(leftSet));
			final Set<IStorage> differenceOriginRight = difference(additionalOrigin, asURISet(rightSet));
			additionalStorages = symmetricDifference(differenceOriginRight, differenceOriginLeft);

			// Differences between left/right and origin could come from resources that are present in
			// the origin, but were deleted in one of the sides. As these resources already exist in
			// the origin, they need to be removed from the additionalStorages
			additionalStorages.removeAll(originSet);
		}
	}

	/**
	 * Tries and resolve the given set of additional storages (as compared to {@code alreadyLoaded}) on the
	 * given side.
	 * <p>
	 * If the storages from {@code additionalStorages} do not (or no longer) exist on the given side, this
	 * will have no effect. Otherwise, they'll be loaded and resolved in order to determine whether they are
	 * part of a larger model. Whether they're part of a larger model or not, they will be returned by this
	 * method as long as they exist on the given side.
	 * </p>
	 * 
	 * @param alreadyLoaded
	 *            All storages that have already been loaded on the given side. This will prevent us from
	 *            resolving the same model more than once.
	 * @param additionalStorages
	 *            The set of additional storages we are to find and resolve on the given side.
	 * @param side
	 *            Side on which we seek to load additional storages in the traversal.
	 * @param tspm
	 *            Monitor on which to report progress to the user.
	 * @return The set of additional storages that are to be added to the traversal of the given side.
	 * @throws InterruptedException
	 *             Thrown if the resolution is cancelled or interrupted one way or another.
	 */
	private Set<IStorage> findAdditionalRemoteTraversal(Set<IStorage> alreadyLoaded,
			Set<IStorage> additionalStorages, DiffSide side, final ThreadSafeProgressMonitor tspm)
			throws InterruptedException {
		if (additionalStorages.isEmpty()) {
			return Collections.emptySet();
		}
		final SynchronizedResourceSet resourceSet = remoteResolver
				.getResourceSetForRemoteResolution(diagnostic, tspm);
		final StorageURIConverter converter = new RevisionedURIConverter(resourceSet.getURIConverter(),
				storageAccessor, side);
		resourceSet.setURIConverter(converter);

		ResourceComputationScheduler<URI> scheduler = context.getScheduler();
		scheduler.setComputedElements(transform(converter.getLoadedRevisions(), asURI()));

		Iterable<URI> urisToResolve = transform(additionalStorages, asURI());
		urisToResolve = Iterables.filter(urisToResolve, new Predicate<URI>() {
			public boolean apply(URI input) {
				return input != null && input.isPlatformResource();
			}
		});
		scheduler.computeAll(transform(urisToResolve, resolveRemoteURI(tspm, resourceSet)));

		resourceSet.dispose();

		if (tspm.isCanceled()) {
			throw new OperationCanceledException();
		}

		scheduler.clearComputedElements();

		return converter.getLoadedRevisions();
	}

	/**
	 * Tries and resolve the given set of additional storages (as compared to {@code alreadyLoaded}) on the
	 * source side. Should only be called when said "source" is local.
	 * 
	 * @param alreadyLoaded
	 *            All storages that have already been loaded on the given side. This will prevent us from
	 *            resolving the same model more than once.
	 * @param additionalStorages
	 *            The set of additional storages we are to find and resolve on the source side.
	 * @param tspm
	 *            Monitor on which to report progress to the user.
	 * @return The set of additional storages that are to be added to the source traversal.
	 * @throws InterruptedException
	 *             Thrown if the resolution is cancelled or interrupted one way or another.
	 */
	private Set<IStorage> findAdditionalLocalTraversal(Set<IStorage> alreadyLoaded,
			Set<IStorage> additionalStorages, final ThreadSafeProgressMonitor tspm)
			throws InterruptedException {
		if (additionalStorages.isEmpty()) {
			return Collections.emptySet();
		}

		final Set<IStorage> traversal = new LinkedHashSet<IStorage>();
		Iterable<URI> urisToResolve = transform(additionalStorages, asURI());
		urisToResolve = Iterables.filter(urisToResolve, new Predicate<URI>() {
			public boolean apply(URI input) {
				return input != null && input.isPlatformResource();
			}
		});
		Set<URI> bounds = Sets.newLinkedHashSet(urisToResolve);
		for (URI resolveMe : urisToResolve) {
			IResource file = ResourceUtil.getResourceFromURI(resolveMe);
			if (file instanceof IFile && !alreadyLoaded.contains(file)) {
				localResolver.updateDependencies(monitor, diagnostic, (IFile)file);
				if (tspm.isCanceled()) {
					throw new OperationCanceledException();
				}
				bounds.remove(resolveMe);
				Set<IStorage> storages = resolveTraversal((IFile)file, bounds);
				for (IStorage storage : storages) {
					URI uri = ResourceUtil.createURIFor(storage);
					bounds.add(uri);
					traversal.add(storage);
				}
				bounds.add(resolveMe);
			}
		}

		return traversal;
	}

	/**
	 * Returns the set of all elements that are contained neither in set1 nor in set2.
	 * 
	 * @param set1
	 *            First of the two sets.
	 * @param set2
	 *            Second of the two sets.
	 * @return The set of all elements that are contained neither in set1 nor in set2.
	 */
	private Set<IStorage> symmetricDifference(Set<IStorage> set1, Set<IStorage> set2) {
		final Set<URI> uris1 = Sets.newLinkedHashSet(Iterables.transform(set1, asURI()));
		final Set<URI> uris2 = Sets.newLinkedHashSet(Iterables.transform(set2, asURI()));

		final Set<IStorage> symmetricDifference = new LinkedHashSet<IStorage>();
		for (IStorage storage1 : set1) {
			if (!uris2.contains(asURI().apply(storage1))) {
				symmetricDifference.add(storage1);
			}
		}
		for (IStorage storage2 : set2) {
			if (!uris1.contains(asURI().apply(storage2))) {
				symmetricDifference.add(storage2);
			}
		}
		return symmetricDifference;
	}

	/**
	 * Returns the set of all elements that are contained in {@code set1} but not in {@code set2}.
	 * 
	 * @param set1
	 *            First of the two sets.
	 * @param set2
	 *            Second of the two sets.
	 * @return The set of all elements that are contained in {@code set1} but not in {@code set2}.
	 */
	private Set<IStorage> difference(Set<IStorage> set1, Set<URI> set2) {
		final Set<IStorage> difference = new LinkedHashSet<IStorage>();
		for (IStorage storage1 : set1) {
			final URI uri = asURI().apply(storage1);
			if (!set2.contains(uri)) {
				difference.add(storage1);
			}
		}
		return difference;
	}

	/**
	 * Resolve the remote traversal of the given storage.
	 * 
	 * @param start
	 *            Storage to resolve
	 * @param knownVariants
	 *            Iterable over all the currently known {@link URI}s that are part of the local logical model
	 *            on any side
	 * @param side
	 *            The side
	 * @param tspm
	 *            Monitor to report progress
	 * @return The traversal of the storage on the given side
	 * @throws InterruptedException
	 *             If interrupted.
	 */
	private Set<IStorage> resolveRemoteTraversal(IStorage start, Iterable<URI> knownVariants, DiffSide side,
			final ThreadSafeProgressMonitor tspm) throws InterruptedException {
		// we can't call ResourceUtil.createURIFor(start) if start is null
		// but the returned Set must be changeable so Collections.emptySet() won't do
		if (start == null) {
			return Sets.newLinkedHashSet();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("resolveRemotetraversal() - START for " + start); //$NON-NLS-1$
		}
		final SynchronizedResourceSet resourceSet = remoteResolver
				.getResourceSetForRemoteResolution(diagnostic, tspm);
		final RevisionedURIConverter converter = new RevisionedURIConverter(resourceSet.getURIConverter(),
				storageAccessor, side);
		resourceSet.setURIConverter(converter);

		ResourceComputationScheduler<URI> scheduler = context.getScheduler();
		scheduler.clearComputedElements();

		final URI startURI = converter.normalize(ResourceUtil.createURIFor(start));
		final Iterable<URI> knownVariantsAndStart = concat(knownVariants, Collections.singleton(startURI));
		final Iterable<URI> urisToResolve = addRenamedUris(knownVariantsAndStart, converter, side);

		scheduler.computeAll(transform(urisToResolve, resolveRemoteURI(tspm, resourceSet)));

		if (tspm.isCanceled()) {
			throw new OperationCanceledException();
		}

		scheduler.clearComputedElements();

		resourceSet.dispose();

		if (logger.isDebugEnabled()) {
			logger.debug("resolveRemotetraversal() - END for " + start); //$NON-NLS-1$
		}
		return converter.getLoadedRevisions();
	}

	/**
	 * Adds the new {@link URI URIs} of resources in {@code resolvedUris} that have been renamed on the given
	 * {@code side}.
	 * <p>
	 * If the side is {@link DiffSide#ORIGIN}, we check whether the given {@link URI URIs}, which have been
	 * obtained from the {@link DiffSide#SOURCE}, have been renamed in the {@link DiffSide#SOURCE} and thus
	 * don't exist in {@link DiffSide#ORIGIN}.
	 * </p>
	 * <p>
	 * If the side is {@link DiffSide#REMOTE}, we check whether the given {@link URI URIs}, which have been
	 * obtained from the {@link DiffSide#SOURCE}, have been renamed at the {@link DiffSide#REMOTE} and
	 * therefore don't exist in {@link DiffSide#REMOTE} or whether they are renamed {@link URI URIs} (i.e.,
	 * they have been renamed in {@link DiffSide#SOURCE}) and thus don't exist in {@link DiffSide#REMOTE}.
	 * </p>
	 * 
	 * @param resolvedUris
	 *            The list of {@link URI URIs} to check for renames.
	 * @param converter
	 *            The converter to be used for obtaining resources from {@link URI URIs}.
	 * @param side
	 *            The side to consider.
	 * @return The given {@link URI URIs} and the renamed {@link URI URIs}.
	 */
	private Iterable<URI> addRenamedUris(Iterable<URI> resolvedUris, RevisionedURIConverter converter,
			DiffSide side) {
		final Set<URI> renamedUris = new HashSet<URI>();
		for (URI resolvedUri : resolvedUris) {
			final IResource iResource = ResourceUtil.getResourceFromURI(resolvedUri);
			if (iResource instanceof IFile) {
				final IFile iFile = (IFile)iResource;
				final Optional<IFile> fileBeforeRename = Optional
						.fromNullable(storageAccessor.getFileBeforeRename(iFile, DiffSide.SOURCE));
				if (DiffSide.ORIGIN.equals(side)) {
					// file name of the origin side is the file name before the rename that may have
					// happened on either side, but since we start from the source, we only consider the
					// renames that may have happened on the source side
					renamedUris.addAll(resolveRenamedUri(fileBeforeRename, converter));
				} else if (DiffSide.REMOTE.equals(side)) {
					// file name of the remote side is the file name after the rename that may have
					// happened on the remote side, or the file name before the renames that may have
					// happened on the opposite side (i.e., source side)
					final Optional<IFile> fileAfterRename = Optional
							.fromNullable(storageAccessor.getFileAfterRename(iFile, DiffSide.REMOTE));
					renamedUris.addAll(resolveRenamedUri(fileAfterRename, converter));
					renamedUris.addAll(resolveRenamedUri(fileBeforeRename, converter));
				}
			}
		}
		final ImmutableList.Builder<URI> builder = new ImmutableList.Builder<URI>();
		return builder.addAll(renamedUris).addAll(resolvedUris).build();
	}

	/**
	 * Creates a list of {@link URI URIs} that contains the given {@code renamedFile} converted into a
	 * {@link URI} as well as its implicit dependencies. If the optional {@code renamedFile} is absent, this
	 * method returns an empty collection.
	 * 
	 * @param renamedFile
	 *            The optional {@code renamedFile}.
	 * @param converter
	 *            The converter to be used for {@link URI URIs} from {@code renamedFile} as well as to get its
	 *            implicit dependencies.
	 * @return The list of resolved {@link URI URIs}
	 */
	private Collection<URI> resolveRenamedUri(Optional<IFile> renamedFile, RevisionedURIConverter converter) {
		final Set<URI> renamedUris = new HashSet<URI>();
		if (renamedFile.isPresent()) {
			final URI unnormalizedRenamedUri = ResourceUtil.createURIFor(renamedFile.get());
			final URI renamedUri = converter.normalize(unnormalizedRenamedUri);
			renamedUris.addAll(getImplicitDependencies().of(renamedUri, converter));
			renamedUris.add(renamedUri);
		}
		return renamedUris;
	}

	/**
	 * Provides a {@link Function} that converts a given URI into a Computation that can be run by a
	 * {@link ResourceComputationScheduler}.
	 * 
	 * @param tspm
	 *            The progress monitor to use
	 * @param resourceSet
	 *            The resource set to use
	 * @return A {@link Function}, never {@code null}, that can be used to remotely resolvea given URI.
	 */
	protected Function<URI, IComputation<URI>> resolveRemoteURI(final ThreadSafeProgressMonitor tspm,
			final SynchronizedResourceSet resourceSet) {
		return new Function<URI, IComputation<URI>>() {
			public IComputation<URI> apply(final URI uri) {
				return remoteResolver.getRemoteResolveComputation(resourceSet, uri, diagnostic, tspm);
			}
		};
	}
}

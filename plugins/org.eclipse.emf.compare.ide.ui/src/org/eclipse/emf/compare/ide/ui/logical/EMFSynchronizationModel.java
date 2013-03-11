/*******************************************************************************
 * Copyright (c) 2011, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.logical;

import static org.eclipse.emf.compare.ide.utils.ResourceUtil.binaryIdentical;
import static org.eclipse.emf.compare.ide.utils.ResourceUtil.createURIFor;

import com.google.common.annotations.Beta;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.compare.IResourceProvider;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.internal.utils.NotLoadingResourceSet;
import org.eclipse.emf.compare.ide.internal.utils.SyncResourceSet;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.ide.utils.StorageURIConverter;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.FilterComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.team.core.diff.IDiff;
import org.eclipse.team.core.diff.IThreeWayDiff;
import org.eclipse.team.core.diff.ITwoWayDiff;
import org.eclipse.team.core.history.IFileRevision;
import org.eclipse.team.core.mapping.provider.ResourceDiff;
import org.eclipse.team.core.subscribers.Subscriber;

/**
 * This class will act as a logical model for EMF. It will hold the necessary logic to be able to determine
 * which "other" files are to be considered dependencies of a "starting point". For example, when trying to
 * compare a "genmodel" file, we cannot compare the genmodel alone, we need to compare the underlying "ecore"
 * file along with it.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@Beta
public final class EMFSynchronizationModel {
	/** The traversal corresponding to the left side. */
	private final StorageTraversal leftTraversal;

	/** The traversal corresponding to the right side. */
	private final StorageTraversal rightTraversal;

	/** The traversal corresponding to the common ancestor of both other side. */
	private final StorageTraversal originTraversal;

	/**
	 * While loading this model, we might find that the left side cannot be edited (i.e. we could not save it
	 * even if we were to edit it). This might notably be the case for comparison with the Git Index : Git
	 * allows modification of the index, but we would not be able to save these modifications (cannot open an
	 * output stream towards the 'index' revisions of all files composing the logical model). This will be
	 * used to alter the compare configuration.
	 */
	private final boolean leftEditable;

	/**
	 * See {@link #leftEditable}.
	 * 
	 * @see #leftEditable
	 */
	private final boolean rightEditable;

	/**
	 * Constructs our logical model given the three traversal for our sides.
	 * 
	 * @param leftTraversal
	 *            The traversal corresponding to the left side.
	 * @param rightTraversal
	 *            The traversal corresponding to the right side.
	 * @param originTraversal
	 *            The traversal corresponding to the common ancestor of both other side. Can be
	 *            <code>null</code>.
	 */
	private EMFSynchronizationModel(StorageTraversal leftTraversal, StorageTraversal rightTraversal,
			StorageTraversal originTraversal, boolean leftEditable, boolean rightEditable) {
		if (leftTraversal == null) {
			this.leftTraversal = new StorageTraversal(Sets.<IStorage> newHashSet());
		} else {
			this.leftTraversal = leftTraversal;
		}

		if (rightTraversal == null) {
			this.rightTraversal = new StorageTraversal(Sets.<IStorage> newHashSet());
		} else {
			this.rightTraversal = rightTraversal;
		}

		if (originTraversal == null) {
			this.originTraversal = new StorageTraversal(Sets.<IStorage> newHashSet());
		} else {
			this.originTraversal = originTraversal;
		}

		this.leftEditable = leftEditable;
		this.rightEditable = rightEditable;
	}

	/**
	 * Constructs a synchronization model by resolving the logical model of the given IFile, using
	 * synchronization information provided by the given Subscriber.
	 * 
	 * @param subscriber
	 *            The subscriber that can be used to retrieve synchronization information between our
	 *            resources. May be <code>null</code>.
	 * @param left
	 *            The resource which model is to be resolved.
	 * @param right
	 *            The right resource if comparing local files.
	 * @param origin
	 *            the origin resource if comparing local files.
	 * @return The resolved synchronization model.
	 */
	public static EMFSynchronizationModel createSynchronizationModel(Subscriber subscriber, IResource left,
			IResource right, IResource origin) {
		// History cannot be edited. Can we determine it here?
		boolean canEditLeft = left != null;
		boolean canEditRight = right != null;

		// FIXME resource can be null if we've been provided three remote revisions.
		// Find a way around that.

		IFileRevision leftRevision = null;
		IFileRevision rightRevision = null;
		IFileRevision originRevision = null;
		if (subscriber != null && left != null) {
			try {
				final IDiff diff = subscriber.getDiff(left);
				if (diff instanceof IThreeWayDiff) {
					final ITwoWayDiff localChange = ((IThreeWayDiff)diff).getLocalChange();
					final ITwoWayDiff remoteChange = ((IThreeWayDiff)diff).getRemoteChange();

					// right and origin found here should match what was passed as input to this method.
					if (localChange instanceof ResourceDiff) {
						leftRevision = ((ResourceDiff)localChange).getAfterState();
						originRevision = ((ResourceDiff)localChange).getBeforeState();
					}
					if (remoteChange instanceof ResourceDiff) {
						rightRevision = ((ResourceDiff)remoteChange).getAfterState();
						// origin should match
					}
				} else if (diff instanceof ResourceDiff) {
					leftRevision = ((ResourceDiff)diff).getAfterState();
					rightRevision = ((ResourceDiff)diff).getBeforeState();
				} else {
					// Can this happen?
				}
			} catch (CoreException e) {
				// FIXME log this
			}
		} else if (subscriber != null) {
			// FIXME we need to find the path of the resource from its ITypedElement
		} else {
			// FIXME can this be the case in a scenario where we need the logical model (i.e. more than one
			// resource)?
		}

		final StorageTraversal leftTraversal;
		final StorageTraversal rightTraversal;
		final StorageTraversal originTraversal;
		if (leftRevision == null) {
			// Load it as a local model
			leftTraversal = resolveTraversal(left);
		} else {
			leftTraversal = resolveTraversal(subscriber, leftRevision, DiffSide.LEFT);
		}
		if (rightRevision == null) {
			// Load it as a local model
			rightTraversal = resolveTraversal(right);
		} else {
			rightTraversal = resolveTraversal(subscriber, rightRevision, DiffSide.RIGHT);
		}
		if (originRevision == null) {
			// Load it as a local model
			originTraversal = resolveTraversal(origin);
		} else {
			originTraversal = resolveTraversal(subscriber, originRevision, DiffSide.ORIGIN);
		}

		return new EMFSynchronizationModel(leftTraversal, rightTraversal, originTraversal, canEditLeft,
				canEditRight);
	}

	/**
	 * Creates a synchronization model by resolving the full logical model of the given elements.
	 * 
	 * @param subscriber
	 *            The subscriber that can be used to retrieve synchronization information between our
	 *            resources. Can be <code>null</code>.
	 * @param left
	 *            The left resource, starting point of the left logical model we are to resolve.
	 * @param right
	 *            The right resource, starting point of the right logical model we are to resolve.
	 * @param origin
	 *            The origin resource, starting point of the logical model we are to resolve as the origin
	 *            one. Can be <code>null</code>.
	 * @return The created synchronization model.
	 */
	public static EMFSynchronizationModel createSynchronizationModel(Subscriber subscriber,
			ITypedElement left, ITypedElement right, ITypedElement origin) {
		final IResource leftResource = findResource(left);
		final IResource rightResource = findResource(right);
		final IResource originResource = findResource(origin);

		return createSynchronizationModel(subscriber, leftResource, rightResource, originResource);
	}

	public IComparisonScope createMinimizedScope() {
		// Minimize the traversals to non-read-only resources with no binary identical counterparts.
		minimize();

		// Create the left, right and origin resource sets.
		final ResourceSet leftResourceSet = new NotLoadingResourceSet(leftTraversal);
		final ResourceSet rightResourceSet = new NotLoadingResourceSet(rightTraversal);
		final ResourceSet originResourceSet;
		if (originTraversal == null || originTraversal.getStorages().isEmpty()) {
			// FIXME why would an empty resource set yield a different result ?
			originResourceSet = null;
		} else {
			originResourceSet = new NotLoadingResourceSet(originTraversal);
		}

		final Set<URI> urisInScope = Sets.newLinkedHashSet();
		for (IStorage left : leftTraversal.getStorages()) {
			urisInScope.add(createURIFor(left));
		}
		for (IStorage right : rightTraversal.getStorages()) {
			urisInScope.add(createURIFor(right));
		}
		for (IStorage origin : originTraversal.getStorages()) {
			urisInScope.add(createURIFor(origin));
		}

		final FilterComparisonScope scope = new DefaultComparisonScope(leftResourceSet, rightResourceSet,
				originResourceSet);
		scope.setResourceSetContentFilter(isInScope(urisInScope));
		return scope;
	}

	/**
	 * This can be called to reduce the number of resources in this model's traversals. Specifically, we'll
	 * remove all resources that can be seen as binary identical (we match resources through exact equality of
	 * their names) or read-only.
	 */
	public void minimize() {
		final boolean threeWay = !originTraversal.getStorages().isEmpty();
		// Copy the sets to update them as we go.
		final Set<IStorage> leftCopy = Sets.newLinkedHashSet(leftTraversal.getStorages());
		final Set<IStorage> rightCopy = Sets.newLinkedHashSet(rightTraversal.getStorages());
		final Set<IStorage> originCopy = Sets.newLinkedHashSet(originTraversal.getStorages());

		for (IStorage left : leftCopy) {
			final IStorage right = removeLikeNamedStorageFrom(left, rightCopy);
			if (right != null && threeWay) {
				final IStorage origin = removeLikeNamedStorageFrom(left, originCopy);

				if (origin != null && binaryIdentical(left, right, origin)) {
					leftTraversal.getStorages().remove(left);
					rightTraversal.getStorages().remove(right);
					originTraversal.getStorages().remove(origin);
				}
			} else if (right != null && binaryIdentical(left, right)) {
				leftTraversal.getStorages().remove(left);
				rightTraversal.getStorages().remove(right);
			} else if (right == null) {
				// This file has no match. remove it if read only
				if (left.isReadOnly()) {
					leftTraversal.getStorages().remove(left);
				}
			}
		}

		for (IStorage right : rightCopy) {
			// These have no match on left. Remove if read only
			if (right.isReadOnly()) {
				rightTraversal.getStorages().remove(right);
			}
		}

		for (IStorage origin : originCopy) {
			// These have no match on left and right. Remove if read only
			if (origin.isReadOnly()) {
				originTraversal.getStorages().remove(origin);
			}
		}
	}

	/**
	 * Looks up into the {@code candidates} set for a storage which name matches that of the {@code reference}
	 * storage, removing it if there is one.
	 * 
	 * @param reference
	 *            The storage for which we'll seek a match into {@code candidates}.
	 * @param candidates
	 *            The set of candidates into which to look up for a match to {@code reference}.
	 * @return The first storage from the set of candidates that matches the {@code reference}, if any.
	 *         <code>null</code> if none match.
	 */
	private IStorage removeLikeNamedStorageFrom(IStorage reference, Set<IStorage> candidates) {
		final String referenceName = reference.getName();
		final Iterator<IStorage> candidatesIterator = candidates.iterator();
		while (candidatesIterator.hasNext()) {
			final IStorage candidate = candidatesIterator.next();
			final String candidateName = candidate.getName();

			if (referenceName.equals(candidateName)) {
				candidatesIterator.remove();
				return candidate;
			}
		}
		return null;
	}

	/**
	 * Clients may call this in order to determine whether the left logical model can be edited.
	 * 
	 * @return <code>true</code> if modifications to the left model should be allowed, <code>false</code>
	 *         otherwise.
	 * @see #leftEditable
	 */
	public boolean isLeftEditable() {
		return leftEditable;
	}

	/**
	 * Clients may call this in order to determine whether the right logical model can be edited.
	 * 
	 * @return <code>true</code> if modifications to the right model should be allowed, <code>false</code>
	 *         otherwise.
	 * @see #leftEditable
	 */
	public boolean isRightEditable() {
		return rightEditable;
	}

	/**
	 * Tries and resolve the resource traversal corresponding to the given starting point.
	 * 
	 * @param start
	 *            The resource that will be considered as the "starting point" of the traversal to resolve.
	 * @return The resource traversal corresponding to the logical model that's been computed from the given
	 *         starting point.
	 */
	// package visibility as this will be used by our model provider
	/* package */static StorageTraversal resolveTraversal(IResource start) {
		if (!(start instanceof IFile)) {
			return new StorageTraversal(Sets.<IFile> newLinkedHashSet());
		}

		/*
		 * TODO make model resolver extension point use IResource instead of EMF resource ... and use it here.
		 * For now, we'll simply load the resource as an EMF model and resolve it all.
		 */
		final SyncResourceSet resourceSet = new SyncResourceSet();
		final StorageURIConverter converter = new StorageURIConverter(resourceSet.getURIConverter());
		resourceSet.setURIConverter(converter);

		if (resourceSet.resolveAll((IFile)start)) {
			final Set<IStorage> storages = Sets.newLinkedHashSet(Sets.union(Collections
					.singleton((IFile)start), converter.getLoadedRevisions()));
			return new StorageTraversal(storages);
		}

		return new StorageTraversal(Collections.singleton((IFile)start));
	}

	/**
	 * Tries and resolve the resource traversal corresponding to the given starting point.
	 * 
	 * @param subscriber
	 *            This will be used to retrieve the remote revisions of referenced files. Can be
	 *            <code>null</code>.
	 * @param start
	 *            The revision that will be considered as the "starting point" of the traversal to resolve.
	 * @param side
	 *            Side we are currently resolving.
	 * @return The resource traversal corresponding to the logical model that's been computed from the given
	 *         starting point.
	 */
	private static StorageTraversal resolveTraversal(Subscriber subscriber, IFileRevision start, DiffSide side) {
		if (start == null) {
			return new StorageTraversal(Sets.<IFile> newLinkedHashSet());
		}

		// TODO how could we make this extensible?
		StorageTraversal traversal = new StorageTraversal(Sets.<IFile> newLinkedHashSet());
		final SyncResourceSet resourceSet = new SyncResourceSet();
		final StorageURIConverter converter = new RevisionedURIConverter(resourceSet.getURIConverter(),
				subscriber, side);
		resourceSet.setURIConverter(converter);
		try {
			final IStorage startStorage = start.getStorage(new NullProgressMonitor());
			if (resourceSet.resolveAll(startStorage)) {
				final Set<IStorage> storages = Sets.newLinkedHashSet();
				storages.add(startStorage);
				final IPath startPath = startStorage.getFullPath();
				for (IStorage loaded : converter.getLoadedRevisions()) {
					if (!startPath.equals(loaded.getFullPath())) {
						storages.add(loaded);
					}
				}
				traversal = new StorageTraversal(storages);
			} else {
				// FIXME log
				// We failed to load the starting point. simply return an empty traversal.
			}
		} catch (CoreException e) {
			// FIXME ignore for now
		}
		return traversal;
	}

	/**
	 * Try and determine the resource of the given element.
	 * 
	 * @param element
	 *            The element for which we need an {@link IResource}.
	 * @return The resource corresponding to the given {@code element} if we could find it, <code>null</code>
	 *         otherwise.
	 */
	private static IResource findResource(ITypedElement element) {
		if (element == null) {
			return null;
		}

		// Can we adapt it directly?
		IResource resource = adaptAs(element, IResource.class);
		if (resource == null) {
			// We know about some types ...
			if (element instanceof IResourceProvider) {
				resource = ((IResourceProvider)element).getResource();
			}
		}

		return resource;
	}

	/**
	 * Tries and adapt the given <em>object</em> to an instance of the given class.
	 * 
	 * @param <T>
	 *            Type to which we need to adapt <em>object</em>.
	 * @param object
	 *            The object we need to coerce to a given {@link Class}.
	 * @param clazz
	 *            Class to which we are to adapt <em>object</em>.
	 * @return <em>object</em> cast to type <em>T</em> if possible, <code>null</code> if not.
	 */
	@SuppressWarnings("unchecked")
	private static <T> T adaptAs(Object object, Class<T> clazz) {
		if (object == null) {
			return null;
		}

		T result = null;
		if (clazz.isInstance(object)) {
			result = (T)object;
		} else if (object instanceof IAdaptable) {
			result = (T)((IAdaptable)object).getAdapter(clazz);
		}

		if (result == null) {
			result = (T)Platform.getAdapterManager().getAdapter(object, clazz);
		}

		return result;
	}

	/**
	 * Returns a predicate that can be applied to {@link Resource}s in order to check if their URI is
	 * contained in the given set.
	 * 
	 * @param uris
	 *            URIs that we consider to be in this scope.
	 * @return A useable Predicate.
	 */
	private static Predicate<Resource> isInScope(final Set<URI> uris) {
		return new Predicate<Resource>() {
			public boolean apply(Resource input) {
				return input != null && uris.contains(input.getURI());
			}
		};
	}
}

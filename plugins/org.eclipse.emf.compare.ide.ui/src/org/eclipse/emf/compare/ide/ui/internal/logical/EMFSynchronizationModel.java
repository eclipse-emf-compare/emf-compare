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
package org.eclipse.emf.compare.ide.ui.internal.logical;

import static org.eclipse.emf.compare.ide.utils.ResourceUtil.binaryIdentical;
import static org.eclipse.emf.compare.ide.utils.ResourceUtil.createURIFor;

import com.google.common.annotations.Beta;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.compare.IResourceProvider;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.internal.utils.NotLoadingResourceSet;
import org.eclipse.emf.compare.ide.internal.utils.SyncResourceSet;
import org.eclipse.emf.compare.ide.ui.internal.logical.IStorageProviderAccessor.DiffSide;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.ide.utils.StorageURIConverter;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.FilterComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
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
	 *            The storage that will be used as the starting point for the "left" logical model.
	 * @param right
	 *            The storage that will be used as the starting point for the "right" logical model.
	 * @param origin
	 *            The storage, if any, that will be used as the starting point for a "common ancestor" logical
	 *            model.
	 * @return The resolved synchronization model.
	 */
	public static EMFSynchronizationModel createSynchronizationModel(Subscriber subscriber, IStorage left,
			IStorage right, IStorage origin) {
		EMFSynchronizationModel syncModel;

		/*
		 * No subscriber here means this comparison has not been launched through the synchronize view. We
		 * assume that there is no need to resolve the logical model in such cases, and will simply load the
		 * given content alone. Any cross-resource reference will be dereferenced towards either workspace
		 * (local) or plugin content. Note that even if we _do_ need to load the model, that would not be
		 * possible here as we have no synchronization information.
		 */
		if (subscriber == null) {
			syncModel = loadSingle(left, right, origin);
		} else {
			// History cannot be edited. Can we determine it here?
			final IStorageProviderAccessor storageAccessor = new SubscriberStorageAccessor(subscriber);

			StorageTraversal leftTraversal = resolveTraversal(storageAccessor, left, DiffSide.SOURCE);
			StorageTraversal rightTraversal = resolveTraversal(storageAccessor, right, DiffSide.REMOTE);
			StorageTraversal originTraversal = null;
			if (origin != null) {
				originTraversal = resolveTraversal(storageAccessor, origin, DiffSide.ORIGIN);
			}

			syncModel = new EMFSynchronizationModel(leftTraversal, rightTraversal, originTraversal, !left
					.isReadOnly(), !right.isReadOnly());
		}

		return syncModel;
	}

	/**
	 * We cannot resolve the logical model of these elements. Load them alone.
	 * 
	 * @param left
	 *            Storage to be loaded as left.
	 * @param right
	 *            Storage to be loaded as right.
	 * @param origin
	 *            Common ancestor of left and right, if any.
	 * @return The resolved synchronization model.
	 */
	private static EMFSynchronizationModel loadSingle(IStorage left, IStorage right, IStorage origin) {
		final StorageTraversal leftTraversal = new StorageTraversal(new LinkedHashSet<IStorage>(Arrays
				.asList(left)));
		final StorageTraversal rightTraversal = new StorageTraversal(new LinkedHashSet<IStorage>(Arrays
				.asList(right)));
		final StorageTraversal originTraversal;
		if (origin != null) {
			originTraversal = new StorageTraversal(new LinkedHashSet<IStorage>(Arrays.asList(origin)));
		} else {
			originTraversal = new StorageTraversal(Sets.<IStorage> newLinkedHashSet());
		}

		return new EMFSynchronizationModel(leftTraversal, rightTraversal, originTraversal,
				!left.isReadOnly(), !right.isReadOnly());
	}

	/**
	 * This will be called in the case of a local comparison.
	 * 
	 * @param left
	 *            Resource to load as the left side.
	 * @param right
	 *            Resource to load as the right side.
	 * @param origin
	 *            Resource to load as common ancestor of left and right, if any.
	 * @return The resolved synchronization model.
	 */
	private static EMFSynchronizationModel loadLocal(IResource left, IResource right, IResource origin) {
		final StorageTraversal leftTraversal = resolveTraversal(left);
		final StorageTraversal rightTraversal = resolveTraversal(right);
		final StorageTraversal originTraversal;
		if (origin != null) {
			originTraversal = resolveTraversal(origin);
		} else {
			originTraversal = new StorageTraversal(Sets.<IStorage> newLinkedHashSet());
		}

		final boolean leftEditable = !left.getResourceAttributes().isReadOnly();
		final boolean rightEditable = !right.getResourceAttributes().isReadOnly();
		return new EMFSynchronizationModel(leftTraversal, rightTraversal, originTraversal, leftEditable,
				rightEditable);
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
		if (subscriber == null) {
			// Is this a local comparison?
			final IResource leftResource = findResource(left);
			final IResource rightResource = findResource(right);

			if (leftResource != null && rightResource != null) {
				// assume origin is local or null
				return loadLocal(leftResource, rightResource, findResource(origin));
			}
		}

		final IStorage leftStorage = StreamAccessorStorage.fromTypedElement(left);
		final IStorage rightStorage = StreamAccessorStorage.fromTypedElement(right);
		final IStorage originStorage;
		if (origin != null) {
			originStorage = StreamAccessorStorage.fromTypedElement(origin);
		} else {
			originStorage = null;
		}

		return createSynchronizationModel(subscriber, leftStorage, rightStorage, originStorage);
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
		if (originTraversal != null) {
			for (IStorage origin : originTraversal.getStorages()) {
				urisInScope.add(createURIFor(origin));
			}
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
			return new StorageTraversal(Sets.<IStorage> newLinkedHashSet());
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

	private static StorageTraversal resolveTraversal(IStorageProviderAccessor storageAccessor,
			IStorage start, DiffSide side) {
		StorageTraversal traversal = new StorageTraversal(Sets.<IStorage> newLinkedHashSet());
		if (start == null) {
			return traversal;
		}

		final SyncResourceSet resourceSet = new SyncResourceSet();
		final StorageURIConverter converter = new RevisionedURIConverter(resourceSet.getURIConverter(),
				storageAccessor, side);
		resourceSet.setURIConverter(converter);

		if (resourceSet.resolveAll(start)) {
			final Set<IStorage> storages = Sets.newLinkedHashSet();
			storages.add(start);
			final IPath startPath = start.getFullPath();
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

		return traversal;
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

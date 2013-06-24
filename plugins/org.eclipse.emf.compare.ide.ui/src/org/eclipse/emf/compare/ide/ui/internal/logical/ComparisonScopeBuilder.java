/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.eclipse.emf.compare.ide.ui.internal.util.PlatformElementUtil.findFile;
import static org.eclipse.emf.compare.ide.utils.ResourceUtil.createURIFor;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.internal.utils.NotLoadingResourceSet;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.logical.IModelMinimizer;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.FilterComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * This will be used by EMF Compare in order to construct its comparison scope given a "starting point".
 * <p>
 * A single file is not always a single EMF model, nor is an EMF model always stored in a single file. This
 * will be used to resolve all physical resources composing the logical model we are to compare, minimize this
 * set to the potential changed candidates, and construct the actual comparison scope.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class ComparisonScopeBuilder {
	/** The instance that will be in charge of resolving our logical models. */
	private final IModelResolver resolver;

	/** The instance that will be in charge of reducing the comparison scope. */
	private final IModelMinimizer minimizer;

	/** The accessor that can be used to retrieve synchronization information between our resources. */
	private final IStorageProviderAccessor storageAccessor;

	/**
	 * Constructs a builder given its model resolver and minimizer.
	 * 
	 * @param resolver
	 *            The resolver we'll use to resolve the logical models of this scope.
	 * @param minimizer
	 *            The minimizer that will be use to reduce the comparison scope before construction.
	 */
	public ComparisonScopeBuilder(IModelResolver resolver, IModelMinimizer minimizer) {
		this(resolver, minimizer, null);
	}

	/**
	 * Constructs a builder given its model resolver and minimizer, along with the storage accessor for remote
	 * data.
	 * 
	 * @param resolver
	 *            The resolver we'll use to resolve the logical models of this scope.
	 * @param minimizer
	 *            The minimizer that will be use to reduce the comparison scope before construction.
	 * @param storageAccessor
	 *            The storage accessor we'll use to access remote resource variants. May be <code>null</code>,
	 *            in which case we'll only use local content.
	 */
	public ComparisonScopeBuilder(IModelResolver resolver, IModelMinimizer minimizer,
			IStorageProviderAccessor storageAccessor) {
		this.resolver = checkNotNull(resolver);
		this.minimizer = checkNotNull(minimizer);
		this.storageAccessor = storageAccessor;
	}

	/**
	 * Builds a comparison scope from the given two starting elements.
	 * 
	 * @param left
	 *            The element that will be used as the starting point to resolve the left logical model.
	 * @param right
	 *            Element that will be used as the starting point to resolve the left logical model.
	 * @param monitor
	 *            The monitor on which to report progress information to the user.
	 * @return The newly created comparison scope.
	 */
	public IComparisonScope build(ITypedElement left, ITypedElement right, IProgressMonitor monitor) {
		return build(left, right, null, monitor);
	}

	/**
	 * Builds a comparison scope from the given starting elements.
	 * 
	 * @param left
	 *            The element that will be used as the starting point to resolve the left logical model.
	 * @param right
	 *            Element that will be used as the starting point to resolve the left logical model.
	 * @param origin
	 *            The origin resource, starting point of the logical model we are to resolve as the origin
	 *            one. Can be <code>null</code>.
	 * @param monitor
	 *            The monitor on which to report progress information to the user.
	 * @return The newly created comparison scope.
	 */
	public IComparisonScope build(ITypedElement left, ITypedElement right, ITypedElement origin,
			IProgressMonitor monitor) {
		final SynchronizationModel syncModel;
		if (storageAccessor != null) {
			syncModel = createSynchronizationModel(storageAccessor, left, right, origin, monitor);
		} else {
			syncModel = createSynchronizationModel(left, right, origin, monitor);
		}
		return createMinimizedScope(syncModel, monitor);
	}

	/**
	 * Creates the synchronization model for the given three elements (left, right, and the common ancestor of
	 * the two). Since this comparison may concern either local or remote resources, all I/O operations should
	 * go through the given storage accessor.
	 * 
	 * @param accessor
	 *            The accessor that can be used to retrieve synchronization information between our resources.
	 * @param left
	 *            Typed element used as the left side of this comparison.
	 * @param right
	 *            Typed element used as the right side of this comparison.
	 * @param origin
	 *            Common ancestor of <code>left</code> and <code>right</code>.
	 * @param monitor
	 *            Monitor to report progress information on.
	 * @return The created synchronization model.
	 */
	private SynchronizationModel createSynchronizationModel(IStorageProviderAccessor accessor,
			ITypedElement left, ITypedElement right, ITypedElement origin, IProgressMonitor monitor) {
		SubMonitor progress = SubMonitor.convert(monitor, EMFCompareIDEUIMessages
				.getString("EMFSynchronizationModel.resolving"), 100); //$NON-NLS-1$

		// Can we find a local file to associate a proper path to our storages?
		final IFile localFile = findFile(left);
		String path = null;
		if (localFile != null) {
			path = localFile.getFullPath().toString();
		}

		final IStorage leftStorage = StreamAccessorStorage.fromTypedElement(path, left);
		final IStorage rightStorage = StreamAccessorStorage.fromTypedElement(path, right);
		final IStorage originStorage;
		if (origin != null) {
			originStorage = StreamAccessorStorage.fromTypedElement(path, origin);
		} else {
			originStorage = null;
		}

		return resolver.resolveModels(accessor, leftStorage, rightStorage, originStorage, progress
				.newChild(100));
	}

	/**
	 * Creates the synchronization model for the given three elements (left, right, and their common
	 * ancestor). Since we have no remote data available, we'll consider that the three given files are either
	 * locally available, or that they have no logical model.
	 * 
	 * @param left
	 *            Typed element used as the left side of this comparison.
	 * @param right
	 *            Typed element used as the right side of this comparison.
	 * @param origin
	 *            Common ancestor of <code>left</code> and <code>right</code>.
	 * @param monitor
	 *            Monitor to report progress information on.
	 * @return The created synchronization model.
	 */
	private SynchronizationModel createSynchronizationModel(ITypedElement left, ITypedElement right,
			ITypedElement origin, IProgressMonitor monitor) {
		SubMonitor progress = SubMonitor.convert(monitor, EMFCompareIDEUIMessages
				.getString("EMFSynchronizationModel.resolving"), 100); //$NON-NLS-1$
		// Is this a local comparison?
		final IFile leftFile = findFile(left);
		final IFile rightFile = findFile(right);
		if (leftFile != null && rightFile != null) {
			// assume origin is local or null
			return resolver.resolveLocalModels(leftFile, rightFile, findFile(origin), progress.newChild(100));
		}

		// This is not a local comparison, and we've got no info on how to load remote revisions.
		final IStorage leftStorage = StreamAccessorStorage.fromTypedElement(left);
		final IStorage rightStorage = StreamAccessorStorage.fromTypedElement(right);
		final IStorage originStorage;
		if (origin != null) {
			originStorage = StreamAccessorStorage.fromTypedElement(origin);
		} else {
			originStorage = null;
		}

		return loadSingleResource(leftStorage, rightStorage, originStorage, progress.newChild(3));
	}

	/**
	 * We cannot resolve the logical model of these elements. Load them as single resources.
	 * 
	 * @param left
	 *            Storage to be loaded as left.
	 * @param right
	 *            Storage to be loaded as right.
	 * @param origin
	 *            Common ancestor of left and right, if any.
	 * @return The resolved synchronization model.
	 */
	private SynchronizationModel loadSingleResource(IStorage left, IStorage right, IStorage origin,
			IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
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

		subMonitor.worked(100);
		return new SynchronizationModel(leftTraversal, rightTraversal, originTraversal);
	}

	/**
	 * Prepare the given synchronization model for use, then create the corresponding comparison scope.
	 * 
	 * @param syncModel
	 *            The synchronization model describing our resource traversals.
	 * @param monitor
	 *            Monitor on which to report progress information to the user.
	 * @return The created comparison scope.
	 */
	private IComparisonScope createMinimizedScope(SynchronizationModel syncModel, IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		// Minimize the traversals to non-read-only resources with no binary identical counterparts.
		minimizer.minimize(syncModel, subMonitor.newChild(10));
		return createScope(syncModel, subMonitor.newChild(90));
	}

	/**
	 * Constructs the actual comparison scope given the storage traversals from which to load the resources.
	 * 
	 * @param syncModel
	 *            The synchronization model describing our resource traversals.
	 * @param monitor
	 *            Monitor on which to report progress information to the user.
	 * @return The created comparison scope.
	 */
	private IComparisonScope createScope(SynchronizationModel syncModel, IProgressMonitor monitor) {
		SubMonitor progress = SubMonitor.convert(monitor, EMFCompareIDEUIMessages
				.getString("EMFSynchronizationModel.creatingScope"), 3); //$NON-NLS-1$

		final StorageTraversal leftTraversal = syncModel.getLeftTraversal();
		final StorageTraversal rightTraversal = syncModel.getRightTraversal();
		final StorageTraversal originTraversal = syncModel.getOriginTraversal();

		final ResourceSet originResourceSet;
		if (originTraversal == null || originTraversal.getStorages().isEmpty()) {
			originResourceSet = null;
			progress.setWorkRemaining(2);
		} else {
			originResourceSet = NotLoadingResourceSet.create(originTraversal, progress.newChild(1));
		}
		final ResourceSet leftResourceSet = NotLoadingResourceSet.create(leftTraversal, progress.newChild(1));
		final ResourceSet rightResourceSet = NotLoadingResourceSet.create(rightTraversal, progress
				.newChild(1));

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

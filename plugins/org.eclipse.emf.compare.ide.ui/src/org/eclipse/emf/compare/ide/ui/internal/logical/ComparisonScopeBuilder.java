/*******************************************************************************
 * Copyright (c) 2013, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - bug 466607
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.eclipse.emf.compare.ide.ui.internal.util.PlatformElementUtil.findFile;
import static org.eclipse.emf.compare.ide.utils.ResourceUtil.createURIFor;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.compare.ICompareContainer;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.ide.EMFCompareIDEPlugin;
import org.eclipse.emf.compare.ide.internal.hook.ResourceSetHookRegistry;
import org.eclipse.emf.compare.ide.internal.utils.NotLoadingResourceSet;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.util.PlatformElementUtil;
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
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.team.core.subscribers.Subscriber;
import org.eclipse.team.core.subscribers.SubscriberMergeContext;
import org.eclipse.team.ui.synchronize.ISynchronizeParticipant;
import org.eclipse.team.ui.synchronize.ModelSynchronizeParticipant;

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

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(ComparisonScopeBuilder.class);

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
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		subMonitor.subTask(EMFCompareIDEUIMessages.getString("EMFSynchronizationModel.resolving")); //$NON-NLS-1$
		try {
			final SynchronizationModel syncModel;
			if (storageAccessor != null) {
				syncModel = createSynchronizationModel(storageAccessor, left, right, origin, subMonitor
						.newChild(60));
			} else {
				syncModel = createSynchronizationModel(left, right, origin, subMonitor.newChild(60));
			}

			return createMinimizedScope(syncModel, subMonitor.newChild(40));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			EmptyComparisonScope scope = new EmptyComparisonScope();
			scope.setDiagnostic(BasicDiagnostic.toDiagnostic(e));
			return scope;
		}
	}

	/**
	 * Resolves and minimizes the logical model for the given three typed element as would be done by
	 * {@link #build(ITypedElement, ITypedElement, ITypedElement, IProgressMonitor)}, but returns directly the
	 * SynchronizationModel DTO instead of the actual IComparisonScope.
	 * <p>
	 * This internal API is only intended for use by the resource mapping mergers.
	 * </p>
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
	 * @return The newly created SynchronizationModel.
	 * @throws InterruptedException
	 *             In case of user interruption.
	 */
	/* package */SynchronizationModel buildSynchronizationModel(ITypedElement left, ITypedElement right,
			ITypedElement origin, IProgressMonitor monitor) throws InterruptedException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("buildSynchronizationModel - START"); //$NON-NLS-1$
		}
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		subMonitor.subTask(EMFCompareIDEUIMessages.getString("EMFSynchronizationModel.resolving")); //$NON-NLS-1$

		final SynchronizationModel syncModel;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("buildSynchronizationModel - Creating sync model"); //$NON-NLS-1$
		}
		if (storageAccessor != null) {
			syncModel = createSynchronizationModel(storageAccessor, left, right, origin, subMonitor
					.newChild(90));
		} else {
			syncModel = createSynchronizationModel(left, right, origin, subMonitor.newChild(90));
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("buildSynchronizationModel - Minimizing model"); //$NON-NLS-1$
		}
		minimizer.minimize(syncModel, subMonitor.newChild(10));
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("buildSynchronizationModel - FINISH NORMALLY"); //$NON-NLS-1$
		}
		return syncModel;
	}

	/**
	 * Constructs the comparison scope corresponding to the given typed elements.
	 * 
	 * @param left
	 *            Left of the compared elements.
	 * @param right
	 *            Right of the compared elements.
	 * @param origin
	 *            Common ancestor of the <code>left</code> and <code>right</code> compared elements.
	 * @param monitor
	 *            Monitor to report progress on.
	 * @return The created comparison scope.
	 */
	public static IComparisonScope create(ICompareContainer container, ITypedElement left,
			ITypedElement right, ITypedElement origin, IProgressMonitor monitor) {
		IStorageProviderAccessor storageAccessor = null;
		Subscriber subscriber = getSubscriber(container);
		if (subscriber != null) {
			storageAccessor = new SubscriberStorageAccessor(subscriber);
		}
		IStorage leftStorage = PlatformElementUtil.findFile(left);
		if (leftStorage == null) {
			leftStorage = StreamAccessorStorage.fromTypedElement(left);
		}
		IModelResolver resolver = EMFCompareIDEUIPlugin.getDefault().getModelResolverRegistry()
				.getBestResolverFor(leftStorage);
		final ComparisonScopeBuilder scopeBuilder = new ComparisonScopeBuilder(resolver,
				new IdenticalResourceMinimizer(), storageAccessor);
		return scopeBuilder.build(left, right, origin, monitor);
	}

	/**
	 * Creates the comparison scope corresponding to the given synchronization model, with no further
	 * operation on it.
	 * <p>
	 * This internal API is only intended for use by the resource mapping mergers and is not meant to be
	 * referenced.
	 * </p>
	 * 
	 * @param synchronizationModel
	 *            The synchronization model describing the traversals for which a comparison scope is needed.
	 * @param monitor
	 *            Monitor on which to report progress information to the user.
	 * @return The created comparison scope.
	 * @throws OperationCanceledException
	 *             if the user cancels (or has already canceled) the operation through the given
	 *             {@code monitor}.
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public static IComparisonScope create(SynchronizationModel synchronizationModel, IProgressMonitor monitor)
			throws OperationCanceledException {
		if (monitor.isCanceled()) {
			throw new OperationCanceledException();
		}

		return createScope(synchronizationModel, monitor);
	}

	/**
	 * Team left us with absolutely no way to determine whether our supplied input is the result of a
	 * synchronization or not.
	 * <p>
	 * In order to properly resolve the logical model of the resource currently being compared we need to know
	 * what "other" resources were part of its logical model, and we need to know the revisions of these
	 * resources we are to load. All of this has already been computed by Team, but it would not let us know.
	 * This method uses discouraged means to get around this "black box" locking from Team.
	 * </p>
	 * <p>
	 * The basic need here is to retrieve the Subscriber from this point. We have a lot of accessible
	 * variables, the two most important being the CompareConfiguration and ICompareInput... I could find no
	 * way around the privileged access to the private ModelCompareEditorInput.participant field. There does
	 * not seem to be any adapter (or Platform.getAdapterManager().getAdapter(...)) that would allow for this,
	 * so I'm taking the long way 'round.
	 * </p>
	 * 
	 * @return The subscriber used for this comparison if any could be found, <code>null</code> otherwise.
	 */
	@SuppressWarnings("restriction")
	private static Subscriber getSubscriber(ICompareContainer container) {
		if (container instanceof org.eclipse.team.internal.ui.mapping.ModelCompareEditorInput) {
			final org.eclipse.team.internal.ui.mapping.ModelCompareEditorInput modelInput = (org.eclipse.team.internal.ui.mapping.ModelCompareEditorInput)container;
			ISynchronizeParticipant participant = null;
			try {
				final Field field = org.eclipse.team.internal.ui.mapping.ModelCompareEditorInput.class
						.getDeclaredField("participant"); //$NON-NLS-1$
				AccessController.doPrivileged(new PrivilegedAction<Object>() {
					public Object run() {
						field.setAccessible(true);
						return null;
					}
				});
				participant = (ISynchronizeParticipant)field.get(modelInput);
			} catch (NoSuchFieldException e) {
				// Swallow this, this private field was there at least from 3.5 to 4.3
			} catch (IllegalArgumentException e) {
				// Cannot happen
			} catch (IllegalAccessException e) {
				// "Should" not happen, but ignore it anyway
			}
			if (participant instanceof ModelSynchronizeParticipant
					&& ((ModelSynchronizeParticipant)participant).getContext() instanceof SubscriberMergeContext) {
				return ((SubscriberMergeContext)((ModelSynchronizeParticipant)participant).getContext())
						.getSubscriber();
			}
		}
		return null;
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
			ITypedElement left, ITypedElement right, ITypedElement origin, IProgressMonitor monitor)
			throws InterruptedException {

		// Can we find a local file to associate a proper path to our storages?
		final IFile localFile = findFile(left);
		String path = null;
		if (localFile != null) {
			path = localFile.getFullPath().toString();
		}

		final IStorage leftStorage = StreamAccessorStorage.fromTypedElement(path, left);
		final IStorage rightStorage;
		if (right instanceof IStreamContentAccessor) {
			rightStorage = StreamAccessorStorage.fromTypedElement(path, right);
		} else {
			rightStorage = null;
		}
		final IStorage originStorage;
		if (origin instanceof IStreamContentAccessor) {
			originStorage = StreamAccessorStorage.fromTypedElement(path, origin);
		} else {
			originStorage = null;
		}

		return resolver.resolveModels(accessor, leftStorage, rightStorage, originStorage, monitor);
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
			ITypedElement origin, IProgressMonitor monitor) throws InterruptedException {
		// Is this a local comparison?
		final IFile leftFile = findFile(left);
		final IFile rightFile = findFile(right);
		if (leftFile != null && rightFile != null) {
			// assume origin is local or null
			return resolver.resolveLocalModels(leftFile, rightFile, findFile(origin), monitor);
		}

		// This is not a local comparison, and we've got no info on how to load remote revisions.
		final IStorage leftStorage = StreamAccessorStorage.fromTypedElement(left);
		Assert.isNotNull(leftStorage);
		final IStorage rightStorage;
		if (right instanceof IStreamContentAccessor) {
			rightStorage = StreamAccessorStorage.fromTypedElement(right);
		} else {
			rightStorage = null;
		}
		final IStorage originStorage;
		if (origin instanceof IStreamContentAccessor) {
			originStorage = StreamAccessorStorage.fromTypedElement(origin);
		} else {
			originStorage = null;
		}

		return loadSingleResource(leftStorage, rightStorage, originStorage);
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
	private SynchronizationModel loadSingleResource(IStorage left, IStorage right, IStorage origin) {
		final StorageTraversal leftTraversal = new StorageTraversal(new LinkedHashSet<IStorage>(Arrays
				.asList(left)));
		final StorageTraversal rightTraversal;
		if (right != null) {
			rightTraversal = new StorageTraversal(new LinkedHashSet<IStorage>(Arrays.asList(right)));
		} else {
			rightTraversal = new StorageTraversal(Sets.<IStorage> newLinkedHashSet());
		}
		final StorageTraversal originTraversal;
		if (origin != null) {
			originTraversal = new StorageTraversal(new LinkedHashSet<IStorage>(Arrays.asList(origin)));
		} else {
			originTraversal = new StorageTraversal(Sets.<IStorage> newLinkedHashSet());
		}

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
		if (monitor.isCanceled()) {
			throw new OperationCanceledException();
		}

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
	private static IComparisonScope createScope(SynchronizationModel syncModel, IProgressMonitor monitor) {
		SubMonitor progress = SubMonitor.convert(monitor, 3);
		progress.subTask(EMFCompareIDEUIMessages.getString("EMFSynchronizationModel.creatingScope")); //$NON-NLS-1$

		final StorageTraversal leftTraversal = syncModel.getLeftTraversal();
		final StorageTraversal rightTraversal = syncModel.getRightTraversal();
		final StorageTraversal originTraversal = syncModel.getOriginTraversal();

		final ResourceSet originResourceSet;
		ResourceSetHookRegistry resourceSetHookRegistry = EMFCompareIDEPlugin.getDefault()
				.getResourceSetHookRegistry();
		if (originTraversal == null || originTraversal.getStorages().isEmpty()) {
			originResourceSet = null;
			progress.setWorkRemaining(2);
		} else {
			originResourceSet = NotLoadingResourceSet.create(originTraversal, progress.newChild(1),
					resourceSetHookRegistry);
		}
		final ResourceSet leftResourceSet = NotLoadingResourceSet.create(leftTraversal, progress.newChild(1),
				resourceSetHookRegistry);
		final ResourceSet rightResourceSet = NotLoadingResourceSet.create(rightTraversal, progress
				.newChild(1), resourceSetHookRegistry);

		final URIConverter converter = new ExtensibleURIConverterImpl();
		final Set<URI> urisInScope = Sets.newLinkedHashSet();
		for (IStorage left : leftTraversal.getStorages()) {
			urisInScope.add(converter.normalize(createURIFor(left)));
		}
		for (IStorage right : rightTraversal.getStorages()) {
			urisInScope.add(converter.normalize(createURIFor(right)));
		}
		if (originTraversal != null) {
			for (IStorage origin : originTraversal.getStorages()) {
				urisInScope.add(converter.normalize(createURIFor(origin)));
			}
		}

		final FilterComparisonScope scope = new DefaultComparisonScope(leftResourceSet, rightResourceSet,
				originResourceSet);
		scope.setResourceSetContentFilter(isInScope(urisInScope));

		Diagnostic syncModelDiagnostic = syncModel.getDiagnostic();
		Diagnostic scopeDiagnostic = computeDiagnostics(originResourceSet, leftResourceSet, rightResourceSet);

		BasicDiagnostic basicDiagnostic = new BasicDiagnostic(Diagnostic.OK, EMFCompareIDEUIPlugin.PLUGIN_ID,
				0, null, new Object[0]);

		basicDiagnostic.add(syncModelDiagnostic);
		basicDiagnostic.add(scopeDiagnostic);

		scope.setDiagnostic(basicDiagnostic);
		return scope;
	}

	private static Diagnostic computeDiagnostics(final ResourceSet originResourceSet,
			final ResourceSet leftResourceSet, final ResourceSet rightResourceSet) {
		final BasicDiagnostic originDiagnostic;
		if (originResourceSet != null) {
			originDiagnostic = getResourceSetDiagnostic(originResourceSet, null, true);
		} else {
			originDiagnostic = null;
		}
		BasicDiagnostic leftDiagnostic = getResourceSetDiagnostic(leftResourceSet, DifferenceSource.LEFT,
				true);
		BasicDiagnostic rightDiagnostic = getResourceSetDiagnostic(rightResourceSet, DifferenceSource.RIGHT,
				true);

		BasicDiagnostic diagnostic = new BasicDiagnostic(
				Diagnostic.OK,
				EMFCompareIDEUIPlugin.PLUGIN_ID,
				0,
				EMFCompareIDEUIMessages.getString("ComparisonScopeBuilder.comparisonScopeDiagnostic"), new Object[0]); //$NON-NLS-1$

		if (originDiagnostic != null) {
			diagnostic.add(originDiagnostic);
		}
		diagnostic.add(leftDiagnostic);
		diagnostic.add(rightDiagnostic);

		return diagnostic;
	}

	/**
	 * Compute the diagnotic on each resource of the given resource set and return a diagnostic composed of
	 * 
	 * @param resourceSet
	 *            the resource set
	 * @param side
	 *            the resource set's side. null means origin
	 * @param includeWarning
	 * @return the composite diagnostic
	 */
	private static BasicDiagnostic getResourceSetDiagnostic(final ResourceSet resourceSet,
			DifferenceSource side, boolean includeWarning) {
		final String sideStr;
		if (side == DifferenceSource.LEFT) {
			sideStr = EMFCompareIDEUIMessages.getString("ComparisonScopeBuilder.left"); //$NON-NLS-1$
		} else if (side == DifferenceSource.RIGHT) {
			sideStr = EMFCompareIDEUIMessages.getString("ComparisonScopeBuilder.right"); //$NON-NLS-1$
		} else {
			sideStr = EMFCompareIDEUIMessages.getString("ComparisonScopeBuilder.ancestor"); //$NON-NLS-1$
		}
		BasicDiagnostic diagnostic = new BasicDiagnostic(
				EMFCompareIDEUIPlugin.PLUGIN_ID,
				0,
				EMFCompareIDEUIMessages.getString("ComparisonScopeBuilder.resourceSetDiagnostic", sideStr), new Object[0]); //$NON-NLS-1$
		for (Resource resource : resourceSet.getResources()) {
			Diagnostic resourceDiagnostic = EcoreUtil.computeDiagnostic(resource, includeWarning);
			diagnostic.merge(resourceDiagnostic);
		}
		return diagnostic;
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
				final boolean result;
				if (input != null) {
					URI inputUri = input.getURI();
					if (uris.contains(inputUri)) {
						result = true;
					} else if (input.getResourceSet() != null
							&& input.getResourceSet().getURIConverter() != null) {
						// Tries to normalize the URI in case it is a pathmap uri that needs to be resolved
						URI normalizedInputUri = input.getResourceSet().getURIConverter().normalize(inputUri);
						result = uris.contains(normalizedInputUri);
					} else {
						result = false;
					}
				} else {
					result = false;
				}
				return result;
			}
		};
	}
}

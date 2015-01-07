/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.internal.utils;

import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.emf.compare.ide.utils.ResourceUtil.createURIFor;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.compare.ide.EMFCompareIDEPlugin;
import org.eclipse.emf.compare.ide.hook.IResourceSetHook;
import org.eclipse.emf.compare.ide.internal.EMFCompareIDEMessages;
import org.eclipse.emf.compare.ide.internal.hook.ResourceSetHookRegistry;
import org.eclipse.emf.compare.ide.internal.utils.ProxyNotifierParserPool.IProxyCreationListener;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.policy.ILoadOnDemandPolicy;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * This implementation of a resource set will be created from a {@link StorageTraversal}, and only those
 * resources that are part of the traversal will be loaded. This will allow us to resolve the proxies between
 * these "traversed" resources.
 * <p>
 * This resource set will prevent loading any resources that is not part of the initial traversal. The only
 * exception to this rule is if one of the registered {@link ILoadOnDemandPolicy} says otherwise. Users should
 * not try to add more resources into this resource set using any of the createResource methods.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@Beta
public final class NotLoadingResourceSet extends ResourceSetImpl implements DisposableResourceSet, IProxyCreationListener {
	/**
	 * All of the storages that this resource set is going to load, mapped to their resource (workspace) URIs.
	 */
	private final Map<URI, IStorage> storageToURI;

	/**
	 * Registry holding {@link IResourceSetHook}s.
	 */
	private final ResourceSetHookRegistry hookRegistry;

	/**
	 * Holds <code>true</code> if the resource set has been disposed.
	 */
	private boolean isDisposed;

	/** Registers the proxies we detect while resolving our resources. */
	private Map<EObject, Set<EStructuralFeature>> proxyMap;

	/**
	 * Constructor.
	 * 
	 * @param storagesToLoad
	 *            see {@link #storageToURI}.
	 * @param hookRegistry
	 *            Registry holding {@link IResourceSetHook}s.
	 */
	private NotLoadingResourceSet(Map<URI, IStorage> storagesToLoad, ResourceSetHookRegistry hookRegistry) {
		this.storageToURI = storagesToLoad;
		this.hookRegistry = hookRegistry;
		this.proxyMap = new ConcurrentHashMap<EObject, Set<EStructuralFeature>>();
	}

	/**
	 * Constructs a resource set to contain the resources described by the given traversals.
	 * 
	 * @param traversals
	 *            All traversals we are to load.
	 * @param monitor
	 *            the monitor to which progress will be reported.
	 * @param resourceSetHookRegistry
	 *            A registry of {@link IResourceSetHook}s that potentialy can hook on the new
	 *            {@link org.eclipse.emf.ecore.resource.ResourceSet} (can be <code>null</code> if none).
	 * @return resource set containing the resources described by the given traversals.
	 */
	public static NotLoadingResourceSet create(final StorageTraversal traversals, IProgressMonitor monitor,
			ResourceSetHookRegistry resourceSetHookRegistry) {
		SubMonitor progress = SubMonitor.convert(monitor, 100);
		progress.subTask(EMFCompareIDEMessages.getString("NotLoadingResourceSet.monitor.resolve")); //$NON-NLS-1$

		final URIConverter converter = new ExtensibleURIConverterImpl();
		final Set<? extends IStorage> storages = traversals.getStorages();
		final Map<URI, IStorage> storageToURI = new LinkedHashMap<URI, IStorage>(storages.size());
		for (IStorage storage : storages) {
			final IStorage old = storageToURI.put(converter.normalize(createURIFor(storage)), storage);
			if (old != null) {
				// FIXME debug and log : duplicate storage URI somehow
			}
		}
		final NotLoadingResourceSet resourceSet = new NotLoadingResourceSet(storageToURI,
				resourceSetHookRegistry);
		resourceSet.setURIConverter(converter);

		resourceSet.setURIResourceMap(new HashMap<URI, Resource>(storages.size() << 1));

		// loading is 60% of the total work?
		final int loadWorkPercentage = 60;
		SubMonitor subMonitor = progress.newChild(loadWorkPercentage).setWorkRemaining(storages.size());

		resourceSet.load(subMonitor);

		final int resolveWorkPercentage = 40;
		subMonitor = progress.newChild(resolveWorkPercentage);

		// Then resolve all proxies between our "loaded" resources.
		resourceSet.resolveProxies(subMonitor);

		return resourceSet;
	}

	/**
	 * Retrieves the hooks that need to be hooked on this resource set.
	 * 
	 * @param urisToLoad
	 *            The collection of uris that will be loaded.
	 * @return {@link Collection} of {@link IResourceSetHook}s that need to be hocked.
	 */
	private Collection<IResourceSetHook> getMatchingHooks(final Collection<URI> urisToLoad) {
		final Collection<IResourceSetHook> hooks;
		if (hookRegistry == null) {
			hooks = Collections.emptyList();
		} else {
			hooks = Collections2.filter(hookRegistry.getResourceSetHooks(),
					new Predicate<IResourceSetHook>() {

						public boolean apply(IResourceSetHook input) {
							return input.isHookFor(urisToLoad);
						}
					});
		}
		return hooks;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#demandLoadHelper(org.eclipse.emf.ecore.resource.Resource)
	 */
	@Override
	protected void demandLoadHelper(Resource resource) {
		// Checks if there is an IStorage that should be used to fill this ressource.
		IStorage storage = getMatchingStorage(resource);
		if (storage != null) {
			try {
				loadFromStorage(resource, storage);
			} catch (IOException e) {
				logLoadingFromStorageFailed(resource, storage, e);
			}
		} else {
			super.demandLoadHelper(resource);
		}
	}

	/**
	 * Logs a error on a {@link Resource} that has failed loading.
	 * 
	 * @param resource
	 *            The {@link Resource} to be loaded.
	 * @param storage
	 *            The {@link IStorage} providing the content of the resource.
	 * @param e
	 *            The raised exception.
	 */
	private void logLoadingFromStorageFailed(Resource resource, IStorage storage, Exception e) {
		Status errorStatus = new Status(IStatus.ERROR, EMFCompareIDEPlugin.PLUGIN_ID, EMFCompareIDEMessages
				.getString("StorageResourceWrapper.failToLoad", resource.getURI().toString(), storage //$NON-NLS-1$
						.getName()), e);
		EMFCompareIDEPlugin.getDefault().getLog().log(errorStatus);
	}

	/**
	 * Gets the {@link IStorage} that should be used to fill the passed {@link Resource}.
	 * 
	 * @param resource
	 *            The {@link Resource} to load.
	 * @return A {@link IStorage} to use for loading the resource or <code>null</code> if there is no
	 *         {@link IStorage} for this {@link Resource}.
	 */
	private IStorage getMatchingStorage(Resource resource) {
		URIConverter theURIConverter = getURIConverter();
		URI resourceNormalizedUri = theURIConverter.normalize(resource.getURI());
		return storageToURI.get(resourceNormalizedUri);
	}

	/**
	 * Loads a {@link Resource} using a the content of {@link IStorage}.
	 * 
	 * @param resource
	 *            The {@link Resource} to be loaded.
	 * @param storage
	 *            The {@link IStorage} providing the content.
	 * @throws IOException
	 *             Exception raised if there is an error with the {@link IStorage}.
	 */
	private void loadFromStorage(Resource resource, IStorage storage) throws IOException {
		InputStream stream = null;
		try {
			stream = storage.getContents();
			resource.load(stream, getLoadOptions());
		} catch (CoreException e) {
			logLoadingFromStorageFailed(resource, storage, e);
		} catch (WrappedException e) {
			logLoadingFromStorageFailed(resource, storage, e);
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#handleDemandLoadException(org.eclipse.emf.ecore.resource.Resource,
	 *      java.io.IOException)
	 */
	@Override
	protected void handleDemandLoadException(Resource resource, IOException exception) {
		try {
			super.handleDemandLoadException(resource, exception);
			// CHECKSTYLE:OFF
		} catch (RuntimeException e) {
			// CHECKSTYLE:ON
			// do nothing. The errors are added to the Resource#getErrors() in super().
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#getResource(org.eclipse.emf.common.util.URI,
	 *      boolean)
	 */
	@Override
	public Resource getResource(URI uri, boolean loadOnDemand) {
		checkNotDisposed();

		// Bypass the load on demand policies when possible
		final Resource cached = getURIResourceMap().get(uri);
		if (cached != null) {
			return cached;
		}

		final Resource resource;
		// If this is a resource we know and want to load, do it
		final IStorage storage = storageToURI.get(uri);
		if (loadOnDemand && storage != null) {
			resource = load(storage, uri, new NullProgressMonitor());
		} else {
			ILoadOnDemandPolicy.Registry registry = EMFCompareRCPPlugin.getDefault()
					.getLoadOnDemandPolicyRegistry();
			if (registry.hasAnyAuthorizingPolicy(uri)) {
				resource = super.getResource(uri, true);
			} else {
				resource = super.getResource(uri, false);
			}
		}
		return resource;
	}

	/**
	 * Resolve the proxies registered when loading our resources (as mapped within {@link #proxyMap}) so that
	 * all links between the resources we want to load have been resolved. Note that this will leave other
	 * proxies (proxies to some resource that is not included in the {@link #storageToURI} map) unresolved.
	 * 
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 */
	private void resolveProxies(SubMonitor monitor) {
		// Isn't this first loop counter-productive?
		int totalWork = 0;
		for (Map.Entry<EObject, Set<EStructuralFeature>> entry : proxyMap.entrySet()) {
			totalWork += entry.getValue().size();
		}
		monitor.setWorkRemaining(totalWork);

		for (Map.Entry<EObject, Set<EStructuralFeature>> proxyEntry : proxyMap.entrySet()) {
			for (EStructuralFeature proxiesOn : proxyEntry.getValue()) {
				Object values = proxyEntry.getKey().eGet(proxiesOn, true);
				if (values instanceof InternalEList<?>) {
					final ListIterator<?> crossRefs = ((InternalEList<?>)values).basicListIterator();
					while (crossRefs.hasNext()) {
						final Object nextValue = crossRefs.next();
						if (nextValue instanceof EObject && ((EObject)nextValue).eIsProxy()) {
							final URI proxyURI = ((InternalEObject)nextValue).eProxyURI();
							final Resource targetRes = getResource(proxyURI.trimFragment(), false);
							if (targetRes != null) {
								// resolve this one
								((InternalEList<?>)values).get(crossRefs.previousIndex());
							}
						}
					}
				}
				monitor.worked(1);
			}
		}

		proxyMap.clear();
	}

	/**
	 * Loads resources known by the {@link #storageToURI} map.
	 * 
	 * @param monitor
	 *            {@link IProgressMonitor} reporting progress.
	 */
	private void load(IProgressMonitor monitor) {
		checkNotDisposed();

		final Set<URI> urisToLoad = ImmutableSet.copyOf(storageToURI.keySet());
		final Collection<IResourceSetHook> hooks = getMatchingHooks(urisToLoad);

		for (IResourceSetHook hook : hooks) {
			hook.preLoadingHook(this, urisToLoad);
		}

		for (Map.Entry<URI, IStorage> entry : storageToURI.entrySet()) {
			load(entry.getValue(), entry.getKey(), monitor);
		}

		for (IResourceSetHook hook : hooks) {
			hook.postLoadingHook(this, urisToLoad);
		}
	}

	/**
	 * Retrieve an already loaded resource with the given URI, or load it using the content from the given
	 * storage.
	 * 
	 * @param storage
	 *            The storage from which to load this resource.
	 * @param uri
	 *            The uri to use for our new resource.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 * @return The loaded resource.
	 */
	private Resource load(IStorage storage, URI uri, IProgressMonitor monitor) {
		if (monitor.isCanceled()) {
			throw new OperationCanceledException();
		}

		final Resource cached = getURIResourceMap().get(uri);
		if (cached != null) {
			return cached;
		}

		final Resource loaded = demandCreateResource(uri);
		getURIResourceMap().put(uri, loaded);
		try {
			loadFromStorage(loaded, storage);
			monitor.worked(1);
		} catch (IOException e) {
			logLoadingFromStorageFailed(loaded, storage, e);
		}
		return loaded;
	}

	/** {@inheritDoc} */
	@Override
	public Map<Object, Object> getLoadOptions() {
		this.loadOptions = super.getLoadOptions();
		final ProxyNotifierParserPool parserPool = new ProxyNotifierParserPool(false);
		parserPool.addProxyListener(this);
		loadOptions.put(XMLResource.OPTION_USE_PARSER_POOL, parserPool);
		loadOptions.put(XMLResource.OPTION_USE_DEPRECATED_METHODS, Boolean.FALSE);
		return loadOptions;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.internal.utils.DisposableResourceSet#dispose()
	 */
	public void dispose() {
		ImmutableList<Resource> currentResources = ImmutableList.copyOf(getResources());
		Collection<URI> resourceSetUris = newArrayList(transform(currentResources,
				new Function<Resource, URI>() {
					public URI apply(Resource input) {
						return input.getURI();
					}
				}));
		for (IResourceSetHook hook : getMatchingHooks(resourceSetUris)) {
			hook.onDispose(currentResources);
		}

		// the properties view retains resources in memory somehow (at least with uml).
		// resource.unload does not unload all resources...
		// removing the uml CacheAdapter isn't enough either
		// we need to get rid of all adapters manually
		for (Resource resource : currentResources) {
			TreeIterator<EObject> allContents = EcoreUtil.getAllProperContents(resource, false);
			while (allContents.hasNext()) {
				final EObject next = allContents.next();
				next.eAdapters().clear();
			}
			resource.eAdapters().clear();
		}

		getResources().clear();
		eAdapters().clear();
		isDisposed = true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#createResource(org.eclipse.emf.common.util.URI,
	 *      java.lang.String)
	 */
	@Override
	public Resource createResource(URI uri, String contentType) {
		checkNotDisposed();
		return super.createResource(uri, contentType);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#getEObject(org.eclipse.emf.common.util.URI,
	 *      boolean)
	 */
	@Override
	public EObject getEObject(URI uri, boolean loadOnDemand) {
		checkNotDisposed();
		return super.getEObject(uri, loadOnDemand);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#getAllContents()
	 */
	@Override
	public TreeIterator<Notifier> getAllContents() {
		checkNotDisposed();
		return super.getAllContents();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#getResources()
	 */
	@Override
	public EList<Resource> getResources() {
		checkNotDisposed();
		return super.getResources();
	}

	/**
	 * Checks that the resource set is not disposed. If it is throws an {@link IllegalStateException}.
	 * 
	 * @throws IllegalStateException
	 *             If the resource set is disposed.
	 */
	private void checkNotDisposed() {
		if (isDisposed) {
			throw new IllegalStateException("The resource set is disposed"); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void proxyCreated(Resource source, EObject eObject, EStructuralFeature eStructuralFeature,
			EObject proxy, int position) {
		Set<EStructuralFeature> proxiesOn = proxyMap.get(eObject);
		if (proxiesOn == null) {
			proxiesOn = new LinkedHashSet<EStructuralFeature>();
			proxyMap.put(eObject, proxiesOn);
		}
		proxiesOn.add(eStructuralFeature);
	}
}

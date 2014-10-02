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

import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.emf.compare.ide.utils.ResourceUtil.createURIFor;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.compare.ide.EMFCompareIDEPlugin;
import org.eclipse.emf.compare.ide.internal.EMFCompareIDEMessages;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.policy.ILoadOnDemandPolicy;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.InternalEList;

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
public final class NotLoadingResourceSet extends ResourceSetImpl {

	/**
	 * Function to transform a {@link IStorage} in an {@link URI}.
	 */
	private static final Function<IStorage, URI> TO_URI = new Function<IStorage, URI>() {

		public URI apply(IStorage input) {
			return createURIFor(input);
		}
	};

	/**
	 * Storage traversal used to load specific resources from {@link IStorage}.
	 */
	private final StorageTraversal storageTranversal;

	/**
	 * Constructor.
	 * 
	 * @param storageTranversal
	 *            see {@link #storageTranversal}.
	 */
	private NotLoadingResourceSet(StorageTraversal storageTranversal) {
		this.storageTranversal = storageTranversal;
	}

	/**
	 * Constructs a resource set to contain the resources described by the given traversals.
	 * 
	 * @param traversals
	 *            All traversals we are to load.
	 * @param monitor
	 *            the monitor to which progress will be reported.
	 * @return resource set containing the resources described by the given traversals.
	 */
	public static NotLoadingResourceSet create(final StorageTraversal traversals, IProgressMonitor monitor) {
		SubMonitor progress = SubMonitor.convert(monitor, 100);
		progress.subTask(EMFCompareIDEMessages.getString("NotLoadingResourceSet.monitor.resolve")); //$NON-NLS-1$
		final NotLoadingResourceSet resourceSet = new NotLoadingResourceSet(traversals);

		resourceSet.setURIResourceMap(new HashMap<URI, Resource>(traversals.getStorages().size() << 1));
		final Set<? extends IStorage> storages = traversals.getStorages();

		// loading is 60% of the total work?
		final int loadWorkPercentage = 60;
		SubMonitor subMonitor = progress.newChild(loadWorkPercentage).setWorkRemaining(
				traversals.getStorages().size());
		for (URI uri : Iterables.transform(storages, TO_URI)) {
			if (monitor.isCanceled()) {
				throw new OperationCanceledException();
			}
			// Forces the loading of the selected resources.
			resourceSet.loadResource(uri);
			subMonitor.worked(1);
		}

		final int resolveWorkPercentage = 40;
		subMonitor = progress.newChild(resolveWorkPercentage).setWorkRemaining(
				resourceSet.getResources().size());
		// Then resolve all proxies between our "loaded" resources.
		List<Resource> resourcesCopy = newArrayList(resourceSet.getResources());
		for (Resource res : resourcesCopy) {
			if (monitor.isCanceled()) {
				throw new OperationCanceledException();
			}

			resourceSet.resolve(res);
			subMonitor.worked(1);
		}

		return resourceSet;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#demandLoadHelper(org.eclipse.emf.ecore.resource.Resource)
	 */
	@Override
	protected void demandLoadHelper(Resource resource) {
		if (storageTranversal != null) {
			// Checks if there is IStorage that should be used to fill this ressource.
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
		for (IStorage storage : storageTranversal.getStorages()) {
			URI storageNormalizedURI = getURIConverter().normalize(createURIFor(storage));
			if (storageNormalizedURI.equals(resourceNormalizedUri)) {
				return storage;
			}
		}
		return null;
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
	 * Loads a resource the way a basic {@link ResourceSetImpl} would do it.
	 * 
	 * @param uri
	 *            same as {@link ResourceSetImpl#getResource(URI, boolean)}.
	 * @return same as {@link ResourceSetImpl#getResource(URI, boolean)}.
	 */
	private Resource loadResource(URI uri) {
		return super.getResource(uri, true);
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
		ILoadOnDemandPolicy.Registry registry = EMFCompareRCPPlugin.getDefault()
				.getLoadOnDemandPolicyRegistry();
		if (registry.hasAnyAuthorizingPolicy(uri)) {
			return super.getResource(uri, true);
		}
		return super.getResource(uri, false);
	}

	/**
	 * This will resolve all cross references of the given resource, then unload it. It will then swap the
	 * loaded resource with a new, empty one with the same URI.
	 * 
	 * @param resource
	 *            The resource for which we are to resolve all cross references.
	 */
	private void resolve(Resource resource) {
		final List<EObject> roots = ((InternalEList<EObject>)resource.getContents()).basicList();
		final Iterator<EObject> resourceContent = roots.iterator();
		while (resourceContent.hasNext()) {
			final EObject eObject = resourceContent.next();
			resolveCrossReferences(eObject);
			resolveChildren(eObject);
		}
		resource.getContents().addAll(roots);
	}

	/**
	 * Recursively resolve all children of the given EObject, including (but stopping at) any proxy.
	 * 
	 * @param eObject
	 *            The eObject which children we are to resolve.
	 */
	private void resolveChildren(EObject eObject) {
		final List<EObject> list = eObject.eContents();
		final ListIterator<EObject> childContent = ((InternalEList<EObject>)list).basicListIterator();
		while (childContent.hasNext()) {
			final EObject child = childContent.next();
			if (child.eIsProxy()) {
				final URI proxyURI = ((InternalEObject)child).eProxyURI();
				final Resource targetRes = getResource(proxyURI.trimFragment(), false);
				if (targetRes != null) {
					// resolve this one
					list.get(childContent.previousIndex());
				}
				resolveCrossReferences(child);
			} else {
				resolveCrossReferences(child);
				resolveChildren(child);
			}
		}
	}

	/**
	 * Resolves the cross references of the given EObject.
	 * 
	 * @param eObject
	 *            The EObject for which we are to resolve the cross references.
	 */
	private void resolveCrossReferences(EObject eObject) {
		final EList<EObject> list = eObject.eCrossReferences();
		final ListIterator<EObject> objectChildren = ((InternalEList<EObject>)list).basicListIterator();
		while (objectChildren.hasNext()) {
			final EObject eObj = objectChildren.next();
			if (eObj.eIsProxy()) {
				final URI proxyURI = ((InternalEObject)eObj).eProxyURI();
				final Resource targetRes = getResource(proxyURI.trimFragment(), false);
				if (targetRes != null) {
					// resolve this one
					list.get(objectChildren.previousIndex());
				}
			}
		}
	}
}

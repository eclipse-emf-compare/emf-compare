/*******************************************************************************
 * Copyright (c) 2012 Obeo.
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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.compare.ide.EMFCompareIDEPlugin;
import org.eclipse.emf.compare.ide.policy.ILoadOnDemandPolicy;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * This implementation of a resource set will be created from a {@link StorageTraversal}, and only those
 * resources that are part of the traversal will be loaded. This will allow us to resolve the proxies between
 * these "traversed" resources.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@Beta
public final class NotLoadingResourceSet extends ResourceSetImpl {
	/**
	 * Constructs a resource set to contain the resources described by the given traversal.
	 * 
	 * @param traversal
	 *            The traversal containing all resources we are to load.
	 */
	public NotLoadingResourceSet(StorageTraversal traversal) {
		final Set<? extends IStorage> storages = traversal.getStorages();
		setURIResourceMap(new HashMap<URI, Resource>(storages.size() << 1));
		for (IStorage storage : storages) {
			loadResource(storage, getLoadOptions());
		}
		// Then resolve all proxies between our "loaded" resources
		List<Resource> resourcesCopy = newArrayList(getResources());
		for (Resource res : resourcesCopy) {
			resolve(res);
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
		ILoadOnDemandPolicy.Registry registry = EMFCompareIDEPlugin.getDefault()
				.getLoadOnDemandPolicyRegistry();
		if (registry.hasAnyAuthorizingPolicy(uri)) {
			return super.getResource(uri, true);
		}
		return super.getResource(uri, false);
	}

	/**
	 * This will try and load the given file as an EMF model, and return the corresponding {@link Resource} if
	 * at all possible.
	 * 
	 * @param storage
	 *            The file we need to try and load as a model.
	 * @param options
	 *            The options to pass to {@link Resource#load(java.util.Map)}.
	 * @return The loaded EMF Resource if {@code file} was a model, {@code null} otherwise.
	 */
	// Suppressing the warning until bug 376938 is fixed
	@SuppressWarnings("resource")
	public Resource loadResource(IStorage storage, Map<?, ?> options) {
		InputStream stream = null;
		Resource resource = null;
		try {
			resource = createResource(createURIFor(storage));
			stream = storage.getContents();
			resource.load(stream, options);
		} catch (IOException e) {
			// return null
		} catch (CoreException e) {
			// return null
		} catch (WrappedException e) {
			// return null
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// Should have been caught by the outer try
				}
			}
		}

		return resource;
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

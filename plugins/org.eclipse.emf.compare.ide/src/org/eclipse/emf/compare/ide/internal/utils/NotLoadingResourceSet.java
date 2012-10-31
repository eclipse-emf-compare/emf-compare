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

import static org.eclipse.emf.compare.ide.utils.ResourceUtil.loadResource;

import com.google.common.annotations.Beta;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.core.resources.IStorage;
import org.eclipse.emf.common.util.AbstractTreeIterator;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
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
			loadResource(storage, this, getLoadOptions());
		}
		// Then resolve all proxies between our "loaded" resources
		for (Resource res : getResources()) {
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
			final TreeIterator<EObject> childContent = basicEAllContents(eObject);
			while (childContent.hasNext()) {
				final EObject child = childContent.next();
				resolveCrossReferences(child);
			}
		}
		resource.getContents().addAll(roots);
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

	/**
	 * An implementation of {@link EObject#eAllContents()} that will not resolve its proxies.
	 * 
	 * @param eObject
	 *            The eobject for which contents we need an iterator.
	 * @return The created {@link TreeIterator}.
	 */
	private TreeIterator<EObject> basicEAllContents(final EObject eObject) {
		return new AbstractTreeIterator<EObject>(eObject, false) {
			/** Generated SUID. */
			private static final long serialVersionUID = 6874121606163401152L;

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractTreeIterator#getChildren(java.lang.Object)
			 */
			@Override
			public Iterator<EObject> getChildren(Object obj) {
				return ((InternalEList<EObject>)((EObject)obj).eContents()).basicIterator();
			}
		};
	}
}

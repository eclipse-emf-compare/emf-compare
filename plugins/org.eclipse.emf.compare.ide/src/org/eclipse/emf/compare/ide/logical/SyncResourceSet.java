/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.logical;

import com.google.common.annotations.Beta;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.internal.utils.PriorityExecutorService;
import org.eclipse.emf.compare.ide.internal.utils.PriorityExecutorService.Priority;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * This implementation of a ResourceSet will avoid loading of any resource from any other mean than
 * {@link #resolveAll()}. Furthermore, it will unload any resource it has loaded as soon as its finished with
 * the cross reference links resolving.
 * <p>
 * This is used from our {@link EMFSynchronizationModel} in order to resolve the traversals without keeping
 * the whole model in-memory (and striving to never have it in-memory as a whole).
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@Beta
public final class SyncResourceSet extends ResourceSetImpl {
	/**
	 * Keeps track of the URIs that have been demanded by the resolving of a resource. This cache will be
	 * invalidated when we start resolving this new set of resources.
	 */
	private Set<URI> demandedURIs = Sets.newLinkedHashSet();

	/** Keeps track of the URIs of all resources that have been loaded by this resource set. */
	private Set<URI> loadedURIs = Sets.newLinkedHashSet();

	/**
	 * This thread pool will be used to launch the loading and unloading of resources in separate threads so
	 * as not to block the resolution of new resources.
	 * <p>
	 * Take note that the unloading threads will take precedence over the loading threads : we need to free
	 * the memory as soon as possible, and we expect "unload" threads to complete faster that "load" ones.
	 * </p>
	 */
	private final PriorityExecutorService pool = new PriorityExecutorService();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#getResource(org.eclipse.emf.common.util.URI,
	 *      boolean)
	 */
	@Override
	public Resource getResource(URI uri, boolean loadOnDemand) {
		// Never load resources from here
		if (!loadedURIs.contains(uri)) {
			synchronized(demandedURIs) {
				demandedURIs.add(uri);
			}
		}
		return null;
	}

	/**
	 * Loads the given URI as an EMF resource.
	 * 
	 * @param uri
	 *            The URI to load as a resource.
	 * @return The loaded Resource.
	 */
	public Resource loadResource(URI uri) {
		return super.getResource(uri, true);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Overridden as we need this synchronized : new resources are added through a call to this.
	 * </p>
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#getResources()
	 */
	@Override
	public synchronized EList<Resource> getResources() {
		return super.getResources();
	}

	/**
	 * Resolve all cross references of the resources currently loaded in this resource set. Take note that
	 * this resource set is only interested in the URIs of the resources, and that it will not keep the loaded
	 * content in memory. No resource will be kept in the {@link #getResources() resources'} list of this set.
	 */
	public void resolveAll() {
		for (Resource resource : Sets.newLinkedHashSet(getResources())) {
			loadedURIs.add(resource.getURI());
			resolve(resource);
			unload(resource);
		}

		Set<URI> newURIs;
		synchronized(demandedURIs) {
			newURIs = demandedURIs;
			demandedURIs = Sets.newLinkedHashSet();
		}
		while (!newURIs.isEmpty()) {
			loadedURIs.addAll(newURIs);
			final Set<Future<Object>> resolveThreads = Sets.newLinkedHashSet();
			for (URI uri : newURIs) {
				resolveThreads.add(pool.submit(new ResourceLoader(uri), Priority.LOW));
			}
			for (Future<Object> future : resolveThreads) {
				try {
					future.get();
				} catch (InterruptedException e) {
					// Ignore
				} catch (ExecutionException e) {
					// FIXME log
				}
			}
			synchronized(demandedURIs) {
				newURIs = demandedURIs;
				demandedURIs = Sets.newLinkedHashSet();
			}
		}
		pool.shutdown();
	}

	/**
	 * Retrieve the set of all URIs that have been loaded by {@link #resolveAll()}.
	 * 
	 * @return The set of all URIs that have been loaded by {@link #resolveAll()}.
	 */
	public Set<URI> getLoadedURIs() {
		return loadedURIs;
	}

	/**
	 * This will resolve all cross references of the given resource, then unload it. It will then swap the
	 * loaded resource with a new, empty one with the same URI.
	 * 
	 * @param resource
	 *            The resource for which we are to resolve all cross references.
	 */
	private void resolve(Resource resource) {
		final Iterator<EObject> resourceContent = resource.getContents().iterator();
		while (resourceContent.hasNext()) {
			final EObject eObject = resourceContent.next();
			resolveCrossReferences(eObject);
			final TreeIterator<EObject> childContent = eObject.eAllContents();
			while (childContent.hasNext()) {
				final EObject child = childContent.next();
				if (child.eResource() != resource) {
					childContent.prune();
				} else {
					resolveCrossReferences(child);
				}
			}
		}
	}

	/**
	 * Unload the given resource.
	 * 
	 * @param resource
	 *            The resource we are to unload.
	 */
	private void unload(final Resource resource) {
		getResources().remove(resource);
		// We still need to unload what we loaded since some (like UML) cross reference everything...
		final Runnable unloader = new Runnable() {
			public void run() {
				resource.unload();
			}
		};
		pool.submit(unloader, Priority.NORMAL);
	}

	/**
	 * Resolves the cross references of the given EObject.
	 * 
	 * @param eObject
	 *            The EObject for which we are to resolve the cross references.
	 */
	private void resolveCrossReferences(EObject eObject) {
		final Iterator<EObject> objectChildren = eObject.eCrossReferences().iterator();
		while (objectChildren.hasNext()) {
			// Resolves cross references by simply visiting them.
			objectChildren.next();
		}
	}

	/**
	 * This implementation of a {@link Runnable} will be used to load a given uri from the disk as an EMF
	 * {@link Resource} and resolve all of its cross-resource links (these will be stored in
	 * {@link #demandedURIs} as we go). Once done, it will spawn another thread in order to unload that
	 * resource out of the memory.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private class ResourceLoader implements Runnable {
		/** The uri of the EMF Resource we are to load from the disk. */
		private URI uri;

		/**
		 * Constructs our loader thread given its target URI.
		 * 
		 * @param uri
		 *            The uri of the EMF Resource this thread is to load from the disk.
		 */
		public ResourceLoader(URI uri) {
			this.uri = uri;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			final Resource newResource = loadResource(uri);
			resolve(newResource);
			unload(newResource);
		}
	}
}

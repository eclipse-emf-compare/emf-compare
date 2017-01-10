/*******************************************************************************
 * Copyright (c) 2011, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Michael Borkowski - public visibility
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import com.google.common.collect.Sets;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.AbstractEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.internal.utils.DisposableResourceSet;
import org.eclipse.emf.compare.ide.internal.utils.INamespaceDeclarationListener;
import org.eclipse.emf.compare.ide.internal.utils.IProxyCreationListener;
import org.eclipse.emf.compare.ide.internal.utils.NoNotificationParserPool;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ContentHandler;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * A thread-safe implementation of a ResourceSet that will prevent loading of resources unless explicitly
 * demanded through {@link #loadResource(URI)}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
// Visible for testing
public class SynchronizedResourceSet extends ResourceSetImpl implements DisposableResourceSet {
	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(SynchronizedResourceSet.class);

	/** Associates URIs with their resources. */
	private final ConcurrentHashMap<URI, Resource> uriCache;

	/**
	 * The list of URIs corresponding to namespaces as declared in the xml files ("xmlns:"). These must not go
	 * through our usual xml parser as they have to be properly (and completely) loaded. They also need to be
	 * kept in this resource set and made available for other resources to find as they might have associated
	 * factories to create objects.
	 */
	private final Set<URI> namespaceURIs;

	/** Keeps track of the packages manually loaded for some of this resource set's resources. */
	private final Set<Resource> loadedPackages;

	/** Never try and load the same package twice. We'll use this lock to prevent that from happening. */
	private final ReentrantLock packageLoadingLock;

	/**
	 * Constructor.
	 * 
	 * @param proxyListener
	 *            The listener to notify of proxy creations.
	 */
	public SynchronizedResourceSet(IProxyCreationListener proxyListener) {
		this.uriCache = new ConcurrentHashMap<URI, Resource>();
		this.resources = new SynchronizedResourcesEList<Resource>();
		this.namespaceURIs = Sets.newSetFromMap(new ConcurrentHashMap<URI, Boolean>());
		this.loadedPackages = Sets.newSetFromMap(new ConcurrentHashMap<Resource, Boolean>());
		this.packageLoadingLock = new ReentrantLock(true);
		this.loadOptions = super.getLoadOptions();
		/*
		 * This resource set is specifically designed to resolve cross resources links, it thus spends a lot
		 * of time loading resources. The following set of options is what seems to give the most significant
		 * boost in loading performances, though I did not fine-tune what's really needed here.
		 */
		final NoNotificationParserPool parserPool = new NoNotificationParserPool(true);
		parserPool.addProxyListener(proxyListener);
		parserPool.addNamespaceDeclarationListener(new INamespaceDeclarationListener() {
			public void schemaLocationDeclared(String key, URI uri) {
				namespaceURIs.add(uri.trimFragment());
			}
		});
		loadOptions.put(XMLResource.OPTION_USE_PARSER_POOL, parserPool);
		loadOptions.put(XMLResource.OPTION_USE_DEPRECATED_METHODS, Boolean.FALSE);

		/*
		 * We don't use XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP whereas it could bring performance
		 * improvements because we are loading the resources concurrently and this map could be used (put and
		 * get) by several threads. Passing a ConcurrentMap here is not an option either as EMF sometimes
		 * needs to put "null" values in there. see https://bugs.eclipse.org/bugs/show_bug.cgi?id=403425 for
		 * more details.
		 */
		/*
		 * Most of the existing options we are not using from here, since none of them seems to have a single
		 * effect on loading performance, whether we're looking at time or memory. See also
		 * https://www.eclipse.org/forums/index.php/t/929918/
		 */
	}

	/**
	 * This will load the given URI as an EMF Resource.
	 * <p>
	 * This is the only entry point within this resource set to load an EMF resource, and it will _only_ load
	 * the resource pointed at by <code>uri</code>, ignoring all cross-referenced resources (including
	 * containment proxies).
	 * </p>
	 * 
	 * @param uri
	 *            The URI to load as a resource.
	 * @return The loaded Resource.
	 */
	public Resource loadResource(URI uri) {
		/*
		 * Don't use super.getResource : we know the resource does not exist yet as there will only be one
		 * "load" call for each given URI. The super implementation iterates over loaded resources before
		 * doing any actual work. That causes some minimal overhead but, more importantly, it can generate
		 * concurrent modification exceptions.
		 */
		final URIConverter theURIConverter = getURIConverter();
		final URI normalizedURI = theURIConverter.normalize(uri);

		Resource result = null;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SRS@" + Integer.toHexString(hashCode()) + ".loadResource for " + normalizedURI); //$NON-NLS-1$ //$NON-NLS-2$
		}
		result = uriCache.get(normalizedURI);
		if (result == null) {
			result = delegatedGetResource(uri, true);
			if (result != null) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("SRS@" + Integer.toHexString(hashCode()) + ".loadResource - caching " //$NON-NLS-1$ //$NON-NLS-2$
							+ normalizedURI);
				}
				Resource former = uriCache.putIfAbsent(normalizedURI, result);
				if (former != null) {
					result = former;
				}
			} else if (namespaceURIs.contains(uri)) {
				// This uri points to an EPackage (or profile) that needs to be loaded completely and
				// normally for its factory to be useable.
				result = demandPackageLoad(uri);
			}
		}

		if (result == null) {
			result = demandCreateResource(uri);
			if (getURIConverter() instanceof RevisionedURIConverter) {
				try {
					if (!((RevisionedURIConverter)getURIConverter()).prefetchStream(uri, getLoadOptions())) {
						// Don't try and load. This resource doesn't exist on that side
						return result;
					}
				} catch (IOException e) {
					// Let EMF handle this one.
				}
			}
			if (result == null) {
				// copy/pasted from super.getResource
				throw new RuntimeException("Cannot create a resource for '" + uri //$NON-NLS-1$
						+ "'; a registered resource factory is needed"); //$NON-NLS-1$
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("SRS@" + Integer.toHexString(hashCode()) + ".loadResource - No caching for " //$NON-NLS-1$ //$NON-NLS-2$
						+ normalizedURI);
			}
			demandLoadHelper(result);
		}
		return result;
	}

	/**
	 * Loads the given URI as an EMF EPackage. We will disable our custom parser pool for this one as it need
	 * to be loaded properly for its factory to be functional.
	 * 
	 * @param uri
	 *            The uri to load.
	 * @param normalized
	 *            the normalized form of this URI.
	 * @return The loaded resource.
	 */
	private Resource loadPackage(URI uri, URI normalized) {
		final URI trimmed = uri.trimFragment();
		final Resource resource = createResource(trimmed, ContentHandler.UNSPECIFIED_CONTENT_TYPE);
		if (resource == null) {
			return null;
		}

		// cache this asap. there might be recursive calls to "getResource" with this package URI (as can be
		// observed with the UML Ecore profile for example) during the loading itself; in which case we need
		// to return that same "currently loading" instance.
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SRS@" + Integer.toHexString(hashCode()) + ".getResource - caching package " + uri); //$NON-NLS-1$ //$NON-NLS-2$
		}
		Resource former = uriCache.putIfAbsent(normalized, resource);
		if (former != null) {
			// There was already a resource cached (multi-threading makes it possible)
			return former;
		}

		try (InputStream stream = getURIConverter().createInputStream(trimmed, null)) {
			resource.load(stream, Collections.emptyMap());
		} catch (IOException e) {
			handleDemandLoadException(resource, e);
		}
		loadedPackages.add(resource);
		return resource;
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
		} catch (RuntimeException e) {
			// do nothing, continue with loading, the exception has been added to the diagnostics of the
			// resource
		}
	}

	/**
	 * Unload the given resource.
	 * 
	 * @param resource
	 *            Resource to unlod
	 * @param monitor
	 *            Progress monito to use (currently unused)
	 */
	public void unload(Resource resource, IProgressMonitor monitor) {
		final URI uri = resource.getURI();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SRS@" + Integer.toHexString(hashCode()) + ".unload " + uri); //$NON-NLS-1$ //$NON-NLS-2$
		}
		uriCache.remove(uri);
		getResources().remove(resource);
		resource.eAdapters().clear();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#getResource(org.eclipse.emf.common.util.URI,
	 *      boolean)
	 */
	@Override
	public Resource getResource(URI uri, boolean loadOnDemand) {
		// Never load resources from here, we only care for the EPackages to prevent the XMLHandler from going
		// into a stackoverflow
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SRS@" + Integer.toHexString(hashCode()) + ".getResource for " + uri); //$NON-NLS-1$ //$NON-NLS-2$
		}
		final URI normalized = getURIConverter().normalize(uri);
		Resource demanded = uriCache.get(normalized);
		if (demanded == null) {
			final EPackage ePackage = getPackageRegistry().getEPackage(uri.toString());
			if (ePackage != null) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("SRS@" + Integer.toHexString(hashCode()) //$NON-NLS-1$
							+ ".getResource - found in package registry : " + uri); //$NON-NLS-1$
				}
				demanded = ePackage.eResource();
				if (demanded != null) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("SRS@" + Integer.toHexString(hashCode()) + ".getResource - caching " //$NON-NLS-1$ //$NON-NLS-2$
								+ uri);
					}
					Resource former = uriCache.putIfAbsent(normalized, demanded);
					if (former != null) {
						demanded = former;
					}
				}
			} else if (namespaceURIs.contains(uri)) {
				// This uri points to an EPackage (or profile) that needs to be loaded completely and
				// normally.
				demanded = demandPackageLoad(uri);
			}
		} else if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SRS@" + Integer.toHexString(hashCode()) + ".getResource - FOUND in cache " + uri); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return demanded;
	}

	/**
	 * This will be used to load the uris matching those from {@link #namespaceURIs} normally.
	 * 
	 * @param uri
	 *            The uri of the package to load.
	 * @return The loaded package's resource.
	 */
	private Resource demandPackageLoad(URI uri) {
		final URI normalized = getURIConverter().normalize(uri);

		packageLoadingLock.lock();
		try {
			Resource demanded = uriCache.get(normalized);
			if (demanded == null) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("SRS@" + Integer.toHexString(hashCode()) //$NON-NLS-1$
							+ ".getResource - loaded package normally : " + uri); //$NON-NLS-1$
				}
				demanded = loadPackage(uri, normalized);
			}
			return demanded;
		} finally {
			packageLoadingLock.unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#createResource(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public synchronized Resource createResource(URI uri) {
		return super.createResource(uri);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#createResource(org.eclipse.emf.common.util.URI,
	 *      java.lang.String)
	 */
	@Override
	public synchronized Resource createResource(URI uri, String contentType) {
		return super.createResource(uri, contentType);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#getResources()
	 */
	@Override
	public EList<Resource> getResources() {
		return resources;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#getLoadOptions()
	 */
	@Override
	public Map<Object, Object> getLoadOptions() {
		return loadOptions;
	}

	/**
	 * {@inheritDoc}
	 */
	public void dispose() {
		// unload these completely instead of using #unload(Resource)
		for (Resource resource : loadedPackages) {
			final URI uri = resource.getURI();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("SRS@" + Integer.toHexString(hashCode()) + ".unload " + uri); //$NON-NLS-1$ //$NON-NLS-2$
			}
			uriCache.remove(uri);
			resource.unload();
			getResources().remove(resource);
		}
	}

	/**
	 * A synchronized implementation of {@link ResourcesEList}.
	 * <p>
	 * Note that this cannot be extracted out of the {@link SynchronizedResourceSet} since the
	 * {@link ResourcesEList} type is not visible.
	 * </p>
	 * 
	 * @param <E>
	 *            Type of this list's contents.
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private class SynchronizedResourcesEList<E extends Resource> extends ResourcesEList<E> {
		/** Generated SUID. */
		private static final long serialVersionUID = 7371376112881960414L;

		/** The lock we'll use for synchronization of the resources list. */
		private final Object lock = new Object();

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.AbstractCollection#containsAll(java.util.Collection)
		 */
		@Override
		public boolean containsAll(Collection<?> c) {
			synchronized(lock) {
				return super.containsAll(c);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.AbstractEList#set(int, java.lang.Object)
		 */
		@Override
		public E set(int index, E object) {
			synchronized(lock) {
				return super.set(index, object);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.impl.NotifyingListImpl#dispatchNotification(org.eclipse.emf.common.notify.Notification)
		 */
		@Override
		protected void dispatchNotification(Notification notification) {
			// do nothing
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.AbstractEList#add(java.lang.Object)
		 */
		@Override
		public boolean add(E object) {
			synchronized(lock) {
				return super.add(object);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.AbstractEList#add(int, java.lang.Object)
		 */
		@Override
		public void add(int index, E object) {
			synchronized(lock) {
				super.add(index, object);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.AbstractEList#addAll(java.util.Collection)
		 */
		@Override
		public boolean addAll(Collection<? extends E> collection) {
			synchronized(lock) {
				return super.addAll(collection);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.AbstractEList#addAll(int, java.util.Collection)
		 */
		@Override
		public boolean addAll(int index, Collection<? extends E> collection) {
			synchronized(lock) {
				return super.addAll(index, collection);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.AbstractEList#remove(java.lang.Object)
		 */
		@Override
		public boolean remove(Object object) {
			synchronized(lock) {
				return super.remove(object);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.AbstractEList#retainAll(java.util.Collection)
		 */
		@Override
		public boolean retainAll(Collection<?> collection) {
			synchronized(lock) {
				return super.retainAll(collection);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.AbstractEList#move(int, java.lang.Object)
		 */
		@Override
		public void move(int index, E object) {
			synchronized(lock) {
				super.move(index, object);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.AbstractEList#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object object) {
			synchronized(lock) {
				return super.equals(object);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.AbstractEList#hashCode()
		 */
		@Override
		public int hashCode() {
			synchronized(lock) {
				return super.hashCode();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.AbstractEList#toString()
		 */
		@Override
		public String toString() {
			synchronized(lock) {
				return super.toString();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.AbstractEList#iterator()
		 */
		@Override
		public Iterator<E> iterator() {
			return new SynchronizedEIterator();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.AbstractEList#listIterator()
		 */
		@Override
		public ListIterator<E> listIterator() {
			return new SynchronizedEListIterator();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.AbstractEList#listIterator(int)
		 */
		@Override
		public ListIterator<E> listIterator(int index) {
			synchronized(lock) {
				int curSize = size();
				if (index < 0 || index > curSize) {
					throw new BasicIndexOutOfBoundsException(index, curSize);
				}
				return new SynchronizedEListIterator(index);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.BasicEList#indexOf(java.lang.Object)
		 */
		@Override
		public int indexOf(Object object) {
			synchronized(lock) {
				return super.indexOf(object);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.BasicEList#lastIndexOf(java.lang.Object)
		 */
		@Override
		public int lastIndexOf(Object object) {
			synchronized(lock) {
				return super.lastIndexOf(object);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.BasicEList#toArray()
		 */
		@Override
		public Object[] toArray() {
			synchronized(lock) {
				return super.toArray();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.BasicEList#toArray(T[])
		 */
		@Override
		public <T> T[] toArray(T[] array) {
			synchronized(lock) {
				return super.toArray(array);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.BasicEList#setData(int, java.lang.Object[])
		 */
		@Override
		public void setData(int size, Object[] data) {
			synchronized(lock) {
				super.setData(size, data);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.BasicEList#get(int)
		 */
		@Override
		public E get(int index) {
			synchronized(lock) {
				return super.get(index);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.BasicEList#basicGet(int)
		 */
		@Override
		public E basicGet(int index) {
			synchronized(lock) {
				return super.basicGet(index);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.BasicEList#shrink()
		 */
		@Override
		public void shrink() {
			synchronized(lock) {
				super.shrink();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.BasicEList#grow(int)
		 */
		@Override
		public void grow(int minimumCapacity) {
			synchronized(lock) {
				super.grow(minimumCapacity);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.BasicEList#clone()
		 */
		// CHECKSTYLE:OFF we're overriding...
		@Override
		public Object clone() {
			// CHECKSTYLE:ON
			synchronized(lock) {
				return super.clone();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.impl.NotifyingListImpl#addUnique(java.lang.Object)
		 */
		@Override
		public void addUnique(E object) {
			synchronized(lock) {
				super.addUnique(object);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.impl.NotifyingListImpl#addUnique(int, java.lang.Object)
		 */
		@Override
		public void addUnique(int index, E object) {
			synchronized(lock) {
				super.addUnique(index, object);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.impl.NotifyingListImpl#addAllUnique(java.util.Collection)
		 */
		@Override
		public boolean addAllUnique(Collection<? extends E> collection) {
			synchronized(lock) {
				return super.addAllUnique(collection);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.impl.NotifyingListImpl#addAllUnique(int, java.util.Collection)
		 */
		@Override
		public boolean addAllUnique(int index, Collection<? extends E> collection) {
			synchronized(lock) {
				return super.addAllUnique(index, collection);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.impl.NotifyingListImpl#addAllUnique(java.lang.Object[], int,
		 *      int)
		 */
		@Override
		public boolean addAllUnique(Object[] objects, int start, int end) {
			synchronized(lock) {
				return super.addAllUnique(objects, start, end);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.impl.NotifyingListImpl#addAllUnique(int, java.lang.Object[],
		 *      int, int)
		 */
		@Override
		public boolean addAllUnique(int index, Object[] objects, int start, int end) {
			synchronized(lock) {
				return super.addAllUnique(index, objects, start, end);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.impl.NotifyingListImpl#basicAdd(java.lang.Object,
		 *      org.eclipse.emf.common.notify.NotificationChain)
		 */
		@Override
		public NotificationChain basicAdd(E object, NotificationChain notifications) {
			synchronized(lock) {
				return super.basicAdd(object, notifications);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.impl.NotifyingListImpl#remove(int)
		 */
		@Override
		public E remove(int index) {
			synchronized(lock) {
				return super.remove(index);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.impl.NotifyingListImpl#removeAll(java.util.Collection)
		 */
		@Override
		public boolean removeAll(Collection<?> collection) {
			synchronized(lock) {
				return super.removeAll(collection);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.impl.NotifyingListImpl#basicRemove(java.lang.Object,
		 *      org.eclipse.emf.common.notify.NotificationChain)
		 */
		@Override
		public NotificationChain basicRemove(Object object, NotificationChain notifications) {
			synchronized(lock) {
				return super.basicRemove(object, notifications);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.impl.NotifyingListImpl#clear()
		 */
		@Override
		public void clear() {
			synchronized(lock) {
				super.clear();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.impl.NotifyingListImpl#setUnique(int, java.lang.Object)
		 */
		@Override
		public E setUnique(int index, E object) {
			synchronized(lock) {
				return super.setUnique(index, object);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.impl.NotifyingListImpl#basicSet(int, java.lang.Object,
		 *      org.eclipse.emf.common.notify.NotificationChain)
		 */
		@Override
		public NotificationChain basicSet(int index, E object, NotificationChain notifications) {
			synchronized(lock) {
				return super.basicSet(index, object, notifications);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.impl.NotifyingListImpl#move(int, int)
		 */
		@Override
		public E move(int targetIndex, int sourceIndex) {
			synchronized(lock) {
				return super.move(targetIndex, sourceIndex);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.util.NotifyingInternalEListImpl#basicList()
		 */
		@Override
		public List<E> basicList() {
			synchronized(lock) {
				return super.basicList();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.util.NotifyingInternalEListImpl#basicIterator()
		 */
		@Override
		public Iterator<E> basicIterator() {
			return new SynchronizedNonResolvingEIterator();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.util.NotifyingInternalEListImpl#basicListIterator()
		 */
		@Override
		public ListIterator<E> basicListIterator() {
			return new SynchronizedNonResolvingEListIterator();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.util.NotifyingInternalEListImpl#basicListIterator(int)
		 */
		@Override
		public ListIterator<E> basicListIterator(int index) {
			synchronized(lock) {
				int curSize = size();
				if (index < 0 || index > curSize) {
					throw new BasicIndexOutOfBoundsException(index, curSize);
				}
				return new SynchronizedNonResolvingEListIterator(index);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl.ResourcesEList#contains(java.lang.Object)
		 */
		@Override
		public boolean contains(Object object) {
			synchronized(lock) {
				return super.contains(object);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.util.NotifyingInternalEListImpl#basicContains(java.lang.Object)
		 */
		@Override
		public boolean basicContains(Object object) {
			synchronized(lock) {
				return super.basicContains(object);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.util.NotifyingInternalEListImpl#basicContainsAll(java.util.Collection)
		 */
		@Override
		public boolean basicContainsAll(Collection<?> collection) {
			synchronized(lock) {
				return super.basicContainsAll(collection);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.util.NotifyingInternalEListImpl#basicIndexOf(java.lang.Object)
		 */

		@Override
		public int basicIndexOf(Object object) {
			synchronized(lock) {
				return super.basicIndexOf(object);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.util.NotifyingInternalEListImpl#basicLastIndexOf(java.lang.Object)
		 */
		@Override
		public int basicLastIndexOf(Object object) {
			synchronized(lock) {
				return super.basicLastIndexOf(object);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.util.NotifyingInternalEListImpl#basicToArray()
		 */
		@Override
		public Object[] basicToArray() {
			synchronized(lock) {
				return super.basicToArray();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.util.NotifyingInternalEListImpl#basicToArray(T[])
		 */
		@Override
		public <T> T[] basicToArray(T[] array) {
			synchronized(lock) {
				return super.basicToArray(array);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.BasicEList#data()
		 */
		@Override
		public Object[] data() {
			synchronized(lock) {
				return super.data();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.notify.impl.NotifyingListImpl#getFeature()
		 */
		@Override
		public Object getFeature() {
			synchronized(lock) {
				return super.getFeature();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl.ResourcesEList#getFeatureID()
		 */
		@Override
		public int getFeatureID() {
			synchronized(lock) {
				return super.getFeatureID();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl.ResourcesEList#getNotifier()
		 */
		@Override
		public Object getNotifier() {
			synchronized(lock) {
				return super.getNotifier();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.BasicEList#isEmpty()
		 */
		@Override
		public boolean isEmpty() {
			synchronized(lock) {
				return super.isEmpty();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.BasicEList#size()
		 */
		@Override
		public int size() {
			synchronized(lock) {
				return super.size();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.AbstractList#subList(int, int)
		 */
		@Override
		public List<E> subList(int fromIndex, int toIndex) {
			synchronized(lock) {
				return super.subList(fromIndex, toIndex);
			}
		}

		/**
		 * A synchronized implementation of the {@link AbstractEList.EIterator}.
		 * 
		 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
		 */
		private class SynchronizedEIterator extends AbstractEList<E>.EIterator<E> {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EIterator#hasNext()
			 */
			@Override
			public boolean hasNext() {
				synchronized(lock) {
					return super.hasNext();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EIterator#next()
			 */
			@Override
			public E next() {
				synchronized(lock) {
					return super.next();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EIterator#remove()
			 */
			@Override
			public void remove() {
				synchronized(lock) {
					super.remove();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.lang.Object#equals(java.lang.Object)
			 */
			@Override
			public boolean equals(Object obj) {
				synchronized(lock) {
					return super.equals(obj);
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.lang.Object#hashCode()
			 */
			@Override
			public int hashCode() {
				synchronized(lock) {
					return super.hashCode();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.lang.Object#toString()
			 */
			@Override
			public String toString() {
				synchronized(lock) {
					return super.toString();
				}
			}
		}

		/**
		 * A synchronized implementation of the {@link AbstractEList.EListIterator}.
		 * 
		 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
		 */
		private class SynchronizedEListIterator extends AbstractEList<E>.EListIterator<E> {
			/**
			 * Delegates to the super constructor.
			 */
			public SynchronizedEListIterator() {
				super();
			}

			/**
			 * Delegates to the super constructor.
			 * 
			 * @param index
			 *            Index at which this list iterator should start.
			 */
			public SynchronizedEListIterator(int index) {
				super(index);
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EListIterator#add(java.lang.Object)
			 */
			@Override
			public void add(E object) {
				synchronized(lock) {
					super.add(object);
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EIterator#hasNext()
			 */
			@Override
			public boolean hasNext() {
				synchronized(lock) {
					return super.hasNext();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EListIterator#hasPrevious()
			 */
			@Override
			public boolean hasPrevious() {
				synchronized(lock) {
					return super.hasPrevious();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EIterator#next()
			 */
			@Override
			public E next() {
				synchronized(lock) {
					return super.next();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EListIterator#previous()
			 */
			@Override
			public E previous() {
				synchronized(lock) {
					return super.previous();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EListIterator#previousIndex()
			 */
			@Override
			public int previousIndex() {
				synchronized(lock) {
					return super.previousIndex();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EIterator#remove()
			 */
			@Override
			public void remove() {
				synchronized(lock) {
					super.remove();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EListIterator#set(java.lang.Object)
			 */
			@Override
			public void set(E object) {
				synchronized(lock) {
					super.set(object);
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.lang.Object#equals(java.lang.Object)
			 */
			@Override
			public boolean equals(Object obj) {
				synchronized(lock) {
					return super.equals(obj);
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.lang.Object#hashCode()
			 */
			@Override
			public int hashCode() {
				synchronized(lock) {
					return super.hashCode();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.lang.Object#toString()
			 */
			@Override
			public String toString() {
				synchronized(lock) {
					return super.toString();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EListIterator#nextIndex()
			 */
			@Override
			public int nextIndex() {
				synchronized(lock) {
					return super.nextIndex();
				}
			}
		}

		/**
		 * A synchronized implementation of the {@link AbstractEList.NonResolvingEIterator}.
		 * 
		 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
		 */
		private class SynchronizedNonResolvingEIterator extends AbstractEList<E>.NonResolvingEIterator<E> {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EIterator#hasNext()
			 */
			@Override
			public boolean hasNext() {
				synchronized(lock) {
					return super.hasNext();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EIterator#next()
			 */
			@Override
			public E next() {
				synchronized(lock) {
					return super.next();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.NonResolvingEIterator#remove()
			 */
			@Override
			public void remove() {
				synchronized(lock) {
					super.remove();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.lang.Object#equals(java.lang.Object)
			 */
			@Override
			public boolean equals(Object obj) {
				synchronized(lock) {
					return super.equals(obj);
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.lang.Object#hashCode()
			 */
			@Override
			public int hashCode() {
				synchronized(lock) {
					return super.hashCode();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.lang.Object#toString()
			 */
			@Override
			public String toString() {
				synchronized(lock) {
					return super.toString();
				}
			}
		}

		/**
		 * A synchronized implementation of the {@link AbstractEList.NonResolvingEListIterator}.
		 * 
		 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
		 */
		private class SynchronizedNonResolvingEListIterator extends AbstractEList<E>.NonResolvingEListIterator<E> {
			/**
			 * Delegates to the super constructor.
			 */
			public SynchronizedNonResolvingEListIterator() {
				super();
			}

			/**
			 * Delegates to the super constructor.
			 * 
			 * @param index
			 *            Index at which the iteration should start.
			 */
			public SynchronizedNonResolvingEListIterator(int index) {
				super(index);
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.NonResolvingEListIterator#add(java.lang.Object)
			 */
			@Override
			public void add(E object) {
				synchronized(lock) {
					super.add(object);
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EIterator#hasNext()
			 */
			@Override
			public boolean hasNext() {
				synchronized(lock) {
					return super.hasNext();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EListIterator#hasPrevious()
			 */
			@Override
			public boolean hasPrevious() {
				synchronized(lock) {
					return super.hasPrevious();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EIterator#next()
			 */
			@Override
			public E next() {
				synchronized(lock) {
					return super.next();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EListIterator#previous()
			 */
			@Override
			public E previous() {
				synchronized(lock) {
					return super.previous();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EListIterator#previousIndex()
			 */
			@Override
			public int previousIndex() {
				synchronized(lock) {
					return super.previousIndex();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.NonResolvingEListIterator#remove()
			 */
			@Override
			public void remove() {
				synchronized(lock) {
					super.remove();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.NonResolvingEListIterator#set(java.lang.Object)
			 */
			@Override
			public void set(E object) {
				synchronized(lock) {
					super.set(object);
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.lang.Object#equals(java.lang.Object)
			 */
			@Override
			public boolean equals(Object obj) {
				synchronized(lock) {
					return super.equals(obj);
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.lang.Object#hashCode()
			 */
			@Override
			public int hashCode() {
				synchronized(lock) {
					return super.hashCode();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.lang.Object#toString()
			 */
			@Override
			public String toString() {
				synchronized(lock) {
					return super.toString();
				}
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.util.AbstractEList.EListIterator#nextIndex()
			 */
			@Override
			public int nextIndex() {
				synchronized(lock) {
					return super.nextIndex();
				}
			}
		}
	}
}

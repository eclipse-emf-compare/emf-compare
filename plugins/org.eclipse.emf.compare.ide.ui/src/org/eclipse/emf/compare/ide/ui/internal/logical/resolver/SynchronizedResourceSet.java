/*******************************************************************************
 * Copyright (c) 2011, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.AbstractEList;
import org.eclipse.emf.common.util.AbstractTreeIterator;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.internal.utils.NoNotificationParserPool;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * A thread-safe implementation of a ResourceSet that will prevent loading of resources unless explicitely
 * demanded through {@link #loadResource(URI)}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
class SynchronizedResourceSet extends ResourceSetImpl {
	/** Associates URIs with their resources. */
	private final ConcurrentHashMap<URI, Resource> uriCache;

	public SynchronizedResourceSet() {
		this.uriCache = new ConcurrentHashMap<URI, Resource>();
		this.resources = new SynchronizedResourcesEList<Resource>();
		this.loadOptions = super.getLoadOptions();
		/*
		 * This resource set is specifically designed to resolve cross resources links, it thus spends a lot
		 * of time loading resources. The following set of options is what seems to give the most significant
		 * boost in loading performances, though I did not fine-tune what's really needed here. Take note that
		 * the use of our own parser pool along with the disabling of notifications is what allows us to
		 * bypass UML's CacheAdapter and the potential dead locks it causes.
		 */
		loadOptions.put(XMLResource.OPTION_USE_PARSER_POOL, new NoNotificationParserPool());
		loadOptions.put(XMLResource.OPTION_USE_DEPRECATED_METHODS, Boolean.FALSE);

		final int bufferSize = 16384;
		final Map<String, Object> parserProperties = Maps.newHashMap();
		parserProperties.put("http://apache.org/xml/properties/input-buffer-size", Integer //$NON-NLS-1$
				.valueOf(bufferSize));
		loadOptions.put(XMLResource.OPTION_PARSER_PROPERTIES, parserProperties);

		/*
		 * We don't use XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP whereas it could bring performance
		 * improvements because we are loading the resources concurrently and this map could be used (put and
		 * get) by several threads. Passing a ConcurrentMap here is not an option either as EMF sometimes
		 * needs to put "null" values in there. see https://bugs.eclipse.org/bugs/show_bug.cgi?id=403425 for
		 * more details.
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
		result = uriCache.get(normalizedURI);
		if (result == null) {
			result = delegatedGetResource(uri, true);
			if (result != null) {
				result = uriCache.putIfAbsent(uri, result);
			}
		}

		if (result == null) {
			result = demandCreateResource(uri);
			if (result == null) {
				// copy/pasted from super.getResource
				throw new RuntimeException("Cannot create a resource for '" + uri //$NON-NLS-1$
						+ "'; a registered resource factory is needed"); //$NON-NLS-1$
			}
			demandLoadHelper(result);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#handleDemandLoadException(org.eclipse.emf.ecore.resource.Resource,
	 *      java.io.IOException)
	 */
	@Override
	protected void handleDemandLoadException(Resource resource, IOException exception)
			throws RuntimeException {
		try {
			super.handleDemandLoadException(resource, exception);
		} catch (RuntimeException e) {
			// do nothing, continue with loading, the exception has been added to the diagnostics of the
			// resource
		}
	}

	/*
	 * TODO this should be made into an extension point : give the user a Resource, and ask for a Set<URI> in
	 * return, representing the cross references of said resource. Warn the client that implementation should
	 * be kept fast and should avoid resolution of proxies.
	 */
	public Set<URI> discoverCrossReferences(Resource resource, IProgressMonitor monitor) {
		resource.eSetDeliver(false);
		final List<EObject> roots = ((InternalEList<EObject>)resource.getContents()).basicList();
		final Iterator<EObject> resourceContent = roots.iterator();

		final Set<URI> crossReferencedResources = new LinkedHashSet<URI>();
		while (resourceContent.hasNext()) {

			if (isInterruptedOrCanceled(monitor)) {
				break;
			}

			final EObject eObject = resourceContent.next();
			crossReferencedResources.addAll(resolveCrossReferences(eObject));
			final TreeIterator<EObject> objectChildren = basicEAllContents(eObject);
			while (objectChildren.hasNext()) {

				if (isInterruptedOrCanceled(monitor)) {
					break;
				}

				final EObject child = objectChildren.next();
				if (child.eIsProxy()) {
					final URI proxyURI = ((InternalEObject)child).eProxyURI().trimFragment();
					crossReferencedResources.add(proxyURI);
				} else {
					crossReferencedResources.addAll(resolveCrossReferences(child));
				}
			}
		}

		if (isInterruptedOrCanceled(monitor)) {
			crossReferencedResources.clear();
		}

		return crossReferencedResources;
	}

	private boolean isInterruptedOrCanceled(IProgressMonitor monitor) {
		return monitor.isCanceled() || Thread.currentThread().isInterrupted();
	}

	public void unload(Resource resource, @SuppressWarnings("unused") IProgressMonitor monitor) {
		final URI uri = resource.getURI();
		uriCache.remove(uri);
		getResources().remove(resource);

		// Only call "unload()" when really needed as this is both a time and memory hog.
		// We can't ignore this call with UML because of the CacheAdapter.
		if (resource.getClass().getSimpleName().startsWith("UMLResource")) { //$NON-NLS-1$
			resource.unload();
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
			private static final long serialVersionUID = -617740251257708686L;

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

	/**
	 * Resolves the cross references of the given EObject, but leaves proxies as-is.
	 * 
	 * @param eObject
	 *            The EObject for which we are to resolve the cross references.
	 */
	private Set<URI> resolveCrossReferences(EObject eObject) {
		final Set<URI> crossReferencedResources = new LinkedHashSet<URI>();
		final Iterator<EObject> objectCrossRefs = ((InternalEList<EObject>)eObject.eCrossReferences())
				.basicIterator();
		while (objectCrossRefs.hasNext()) {
			final EObject crossRef = objectCrossRefs.next();
			if (crossRef.eIsProxy()) {
				final URI proxyURI = ((InternalEObject)crossRef).eProxyURI().trimFragment();
				if (proxyURI.isPlatformResource()) {
					crossReferencedResources.add(proxyURI);
				}
			}
		}
		return crossReferencedResources;
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
		Resource demanded = uriCache.get(uri);
		if (demanded == null) {
			final EPackage ePackage = getPackageRegistry().getEPackage(uri.toString());
			if (ePackage != null) {
				demanded = ePackage.eResource();
				demanded = uriCache.putIfAbsent(uri, demanded);
			} else {
				// simply return null
			}
		}
		return demanded;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#createResource(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public synchronized Resource createResource(URI uri) {
		final Resource created = super.createResource(uri);
		return created;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#createResource(org.eclipse.emf.common.util.URI,
	 *      java.lang.String)
	 */
	@Override
	public synchronized Resource createResource(URI uri, String contentType) {
		final Resource created = super.createResource(uri, contentType);
		return created;
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

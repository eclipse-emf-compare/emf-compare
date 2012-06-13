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
package org.eclipse.emf.compare.scope;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.Iterators;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This implementation of an {@link IComparisonScope} can be provided specific filters to filter out parts of
 * the Notifiers' content lists.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class FilterComparisonScope implements IComparisonScope {
	/**
	 * This will be used in order to determine the filter that should be used to filter the EObjects' content
	 * list of unnecessary values.
	 */
	protected Predicate<? super EObject> eObjectContentFilter = Predicates.alwaysTrue();

	/**
	 * This will be used in order to determine the filter that should be used to filter the Resources' content
	 * list of unnecessary values.
	 */
	protected Predicate<? super EObject> resourceContentFilter = Predicates.alwaysTrue();

	/**
	 * This will be used in order to determine the filter that should be used to filter the ResourceSets'
	 * content list of unnecessary values.
	 */
	protected Predicate<? super Resource> resourceSetContentFilter = Predicates.alwaysTrue();

	/** The left root of this comparison. */
	private final Notifier left;

	/** The right root of this comparison. */
	private final Notifier right;

	/** The common ancestor of {@link #left} and {@link #right}. May be <code>null</code>. */
	private final Notifier origin;

	/** The namespace uris detected in the comparison. */
	private Set<String> nsURIs;

	/** The resource uris detected in the comparison. */
	private Set<String> resourceURIs;

	/**
	 * This will instantiate a scope with left, right and origin Notifiers defined.
	 * 
	 * @param left
	 *            The left root of this comparison.
	 * @param right
	 *            The right root of this comparison.
	 * @param origin
	 *            The common ancestor of <code>left</code> and <code>right</code>. May be <code>null</code>.
	 */
	public FilterComparisonScope(Notifier left, Notifier right, Notifier origin) {
		this.left = left;
		this.right = right;
		this.origin = origin;
		resourceURIs = new HashSet<String>();
		nsURIs = new HashSet<String>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.scope.IComparisonScope#getLeft()
	 */
	public Notifier getLeft() {
		return left;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.scope.IComparisonScope#getRight()
	 */
	public Notifier getRight() {
		return right;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.scope.IComparisonScope#getOrigin()
	 */
	public Notifier getOrigin() {
		return origin;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This default implementation will only return the {@link Resource}s directly contained by
	 * {@link ResourceSet} which match the {@link #resourceSetContentFilter} predicate.
	 * </p>
	 * 
	 * @see org.eclipse.emf.compare.scope.IComparisonScope#getCoveredResources(org.eclipse.emf.ecore.resource.ResourceSet)
	 */
	public Iterator<? extends Resource> getCoveredResources(ResourceSet resourceSet) {
		if (resourceSet == null) {
			return Iterators.emptyIterator();
		}

		final Iterator<Resource> allResources = resourceSet.getResources().iterator();
		final Iterator<Resource> filter = Iterators.filter(allResources, resourceSetContentFilter);

		final Iterator<Resource> uriInitializingIt = new URIInitializingIterator<Resource>(filter);

		return Iterators.unmodifiableIterator(uriInitializingIt);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This default implementation will return all direct and indirect content of the given {@link Resource},
	 * filtering out those {@link EObject}s that do not match {@link #resourceContentFilter}.
	 * </p>
	 * 
	 * @see org.eclipse.emf.compare.scope.IComparisonScope#getCoveredEObjects(org.eclipse.emf.ecore.resource.Resource)
	 */
	public Iterator<? extends EObject> getCoveredEObjects(Resource resource) {
		if (resource == null) {
			return Iterators.emptyIterator();
		}

		final Iterator<EObject> properContent = Iterators.filter(EcoreUtil.getAllProperContents(resource,
				false), EObject.class);
		final Iterator<EObject> filter = Iterators.filter(properContent, resourceContentFilter);

		final Iterator<EObject> uriInitializingIt = new URIInitializingIterator<EObject>(resource, filter);

		return Iterators.unmodifiableIterator(uriInitializingIt);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This default implementation will return all direct and indirect content of the given {@link EObject},
	 * filtering out those {@link EObject}s that do not match {@link #eObjectContentFilter}.
	 * </p>
	 * 
	 * @see org.eclipse.emf.compare.scope.IComparisonScope#getChildren(org.eclipse.emf.ecore.EObject)
	 */
	public Iterator<? extends EObject> getChildren(EObject eObject) {
		if (eObject == null) {
			return Iterators.emptyIterator();
		}

		final Iterator<EObject> properContent = Iterators.filter(EcoreUtil.getAllProperContents(eObject,
				false), EObject.class);
		final Iterator<EObject> filter = Iterators.filter(properContent, eObjectContentFilter);

		final Iterator<EObject> uriInitializingIt = new URIInitializingIterator<EObject>(eObject, filter);

		return Iterators.unmodifiableIterator(uriInitializingIt);
	}

	/**
	 * This can be used to set the filter that should be used to filter the EObjects' content list of
	 * unnecessary values. By default, we will use an "always true" predicate : the list won't be filtered out
	 * unless this is called with a new filter.
	 * 
	 * @param newContentFilter
	 *            The filter that should be used for EObject content filtering.
	 */
	public void setEObjectContentFilter(Predicate<? super EObject> newContentFilter) {
		this.eObjectContentFilter = newContentFilter;
	}

	/**
	 * This can be used to set the filter that should be used to filter the Resources' content list of
	 * unnecessary values. By default, we will return an "always true" predicate : the list won't be filtered
	 * out unless this is called with a new filter.
	 * 
	 * @param resourceContentFilter
	 *            The filter that should be used for Resource content filtering.
	 */
	public void setResourceContentFilter(Predicate<? super EObject> resourceContentFilter) {
		this.resourceContentFilter = resourceContentFilter;
	}

	/**
	 * This can be used to set the filter that should be used to filter the ResourceSets' content list of
	 * unnecessary values. By default, we will return an "always true" predicate : the list won't be filtered
	 * out unless this called with a new filter.
	 * 
	 * @param resourceSetContentFilter
	 *            The filter that should be used for ResourceSet content filtering.
	 */
	public void setResourceSetContentFilter(Predicate<? super Resource> resourceSetContentFilter) {
		this.resourceSetContentFilter = resourceSetContentFilter;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.scope.IComparisonScope#getNsURIs()
	 */
	public Set<String> getNsURIs() {
		return nsURIs;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.scope.IComparisonScope#getResourceURIs()
	 */
	public Set<String> getResourceURIs() {
		return resourceURIs;
	}

	/**
	 * This iterator enables to add in the iteration the initialization of the namespace and resource uris
	 * set.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 * @param <T>
	 *            The kind of object to iterate on.
	 */
	class URIInitializingIterator<T> extends ForwardingIterator<T> {

		/** The origin iterator. */
		private Iterator<T> delegate;

		/**
		 * Constructor.
		 * 
		 * @param delegate
		 *            The origin iterator.
		 */
		public URIInitializingIterator(Iterator<T> delegate) {
			this.delegate = delegate;
		}

		/**
		 * Constructor.
		 * 
		 * @param resource
		 *            The resource containing the elements to iterate on.
		 * @param delegate
		 *            The origin iterator.
		 */
		public URIInitializingIterator(Resource resource, Iterator<T> delegate) {
			this.delegate = delegate;
			addUri(resource);
		}

		/**
		 * Constructor.
		 * 
		 * @param eObject
		 *            The EObject containing the elements to iterate on.
		 * @param delegate
		 *            The origin iterator.
		 */
		public URIInitializingIterator(EObject eObject, Iterator<T> delegate) {
			this.delegate = delegate;
			addUri(eObject);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.google.common.collect.ForwardingIterator#delegate()
		 */
		@Override
		protected Iterator<T> delegate() {
			return delegate;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.google.common.collect.ForwardingIterator#next()
		 */
		@Override
		public T next() {
			T obj = super.next();
			if (obj instanceof EObject) {
				addUri((EObject)obj);
			} else if (obj instanceof Resource) {
				addUri((Resource)obj);
			}
			return obj;
		}

		/**
		 * It registers the namespace and resource URI from the given <code>eObject</code>.
		 * 
		 * @param eObject
		 *            The given <code>eObject</code>.
		 */
		private void addUri(EObject eObject) {
			if (eObject.eResource() != null) {
				resourceURIs.add(eObject.eResource().getURI().toString());
			}
			nsURIs.add(eObject.eClass().getEPackage().getNsURI());
		}

		/**
		 * It registers the resource URI from the given <code>resource</code>.
		 * 
		 * @param resource
		 *            The given <code>resource</code>.
		 */
		private void addUri(Resource resource) {
			resourceURIs.add(resource.getURI().toString());
		}

	}

}

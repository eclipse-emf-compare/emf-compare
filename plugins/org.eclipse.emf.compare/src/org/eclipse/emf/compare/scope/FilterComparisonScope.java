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
import com.google.common.collect.Iterators;

import java.util.Iterator;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This implementation of an {@link IComparisonScope} can be sub-classed in order to filter out parts of the
 * Notifiers' content lists.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class FilterComparisonScope implements IComparisonScope {
	/** The left root of this comparison. */
	private final Notifier left;

	/** The right root of this comparison. */
	private final Notifier right;

	/** The common ancestor of {@link #left} and {@link #right}. May be <code>null</code>. */
	private final Notifier origin;

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
	 * {@link ResourceSet} which match the {@link #getResourceSetChildrenFilter()} predicate.
	 * </p>
	 * 
	 * @see org.eclipse.emf.compare.scope.IComparisonScope#getChildren(org.eclipse.emf.ecore.resource.ResourceSet)
	 */
	public Iterator<? extends Resource> getChildren(ResourceSet resourceSet) {
		if (resourceSet == null) {
			return Iterators.emptyIterator();
		}

		final Iterator<Resource> allResources = resourceSet.getResources().iterator();
		final Iterator<Resource> filter = Iterators.filter(allResources, getResourceSetChildrenFilter());
		return Iterators.unmodifiableIterator(filter);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This default implementation will return all direct and indirect content of the given {@link Resource},
	 * filtering out those {@link EObject}s that do not match {@link #getResourceChildrenFilter()}.
	 * </p>
	 * 
	 * @see org.eclipse.emf.compare.scope.IComparisonScope#getChildren(org.eclipse.emf.ecore.resource.Resource)
	 */
	public Iterator<? extends EObject> getChildren(Resource resource) {
		if (resource == null) {
			return Iterators.emptyIterator();
		}

		final Iterator<EObject> properContent = Iterators.filter(EcoreUtil.getAllProperContents(resource,
				false), EObject.class);
		final Iterator<EObject> filter = Iterators.filter(properContent, getResourceChildrenFilter());
		return Iterators.unmodifiableIterator(filter);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This default implementation will return all direct and indirect content of the given {@link EObject},
	 * filtering out those {@link EObject}s that do not match {@link #getEObjectChildrenFilter()}.
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
		final Iterator<EObject> filter = Iterators.filter(properContent, getEObjectChildrenFilter());
		return Iterators.unmodifiableIterator(filter);
	}

	/**
	 * This will be used in order to determine the filter that should be used to filter the EObjects' content
	 * list of unnecessary values. By default, we will return an "always true" predicate : the list won't be
	 * filtered out unless this is overridden.
	 * 
	 * @return The filter that should be used when retrieving an EObject's content.
	 */
	protected Predicate<? super EObject> getEObjectChildrenFilter() {
		return Predicates.alwaysTrue();
	}

	/**
	 * This will be used in order to determine the filter that should be used to filter the Resources' content
	 * list of unnecessary values. By default, we will return an "always true" predicate : the list won't be
	 * filtered out unless this is overridden.
	 * 
	 * @return The filter that should be used when retrieving an EObject's content.
	 */
	protected Predicate<? super EObject> getResourceChildrenFilter() {
		return Predicates.alwaysTrue();
	}

	/**
	 * This will be used in order to determine the filter that should be used to filter the ResourceSets'
	 * content list of unnecessary values. By default, we will return an "always true" predicate : the list
	 * won't be filtered out unless this is overridden.
	 * 
	 * @return The filter that should be used when retrieving an EObject's content.
	 */
	protected Predicate<? super Resource> getResourceSetChildrenFilter() {
		return Predicates.alwaysTrue();
	}
}

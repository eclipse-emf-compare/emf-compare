/*******************************************************************************
 * Copyright (c) 2010, 2011  itemis AG (http://www.itemis.de)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     itemis AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.engine;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * The default {@link IMatchScope} implementation, which can be constructed with a single {@link Resource} or
 * {@link ResourceSet}, or a single {@link EObject}. In case an instance is constructed by passing in a
 * {@link Resource}, it will regard this resource and all its direct and indirect contents (fragments) to be
 * in scope. If being instantiated with a {@link ResourceSet}, all resources within the set (as well as their
 * direct and indirect content) are included in the scope. In case an instance is constructed with an
 * {@link EObject}, its direct and indirect contents ({@link EObject#eAllContents()}) as well as its resource
 * ({@link EObject#eResource}), if specified, will be regarded to be within scope.
 * 
 * @author <a href="mailto:alexander.nyssen@itemis.de">Alexander Nyssen</a>
 * @since 1.1
 */
public class GenericMatchScope implements IMatchScope {

	/** the list of resources to be included in the scope. */
	private final Set<Resource> resourcesInScope = new LinkedHashSet<Resource>();

	/** the list of objects to be included in the scope. */
	private final Set<EObject> eObjectsInScope = new LinkedHashSet<EObject>();

	/**
	 * Allows to construct a scope based on a single {@link EObject}. The constructed scope will include the
	 * provided {@link EObject}, its direct and indirect contents ({@link EObject#eAllContents()}) as well as
	 * its resource ({@link EObject#eResource}) in case one is specified.
	 * 
	 * @param eObject
	 *            the {@link EObject} used to construct the scope.
	 */
	public GenericMatchScope(EObject eObject) {
		eObjectsInScope.add(eObject);
		final Iterator<EObject> iterator = eObject.eAllContents();
		while (iterator.hasNext()) {
			final EObject next = iterator.next();
			eObjectsInScope.add(next);
			if (next.eResource() != null && !resourcesInScope.contains(next.eResource())) {
				resolveAll(next.eResource().getResourceSet());
				addToScope(next.eResource());
			}
		}
	}

	/**
	 * Allows to construct a scope based on a single {@link Resource}. The constructed scope will include the
	 * {@link Resource} as well as its direct and indirect contents {@link Resource#getAllContents()} of the
	 * provided resource.
	 * 
	 * @param scope
	 *            the {@link Resource} used to construct the scope.
	 */
	public GenericMatchScope(Resource scope) {
		resolveAll(scope.getResourceSet());
		addToScope(scope);
	}

	/**
	 * Allow to construct a scope based on a single {@link ResourceSet}. The constructed scope will include
	 * all resources of the resource set (@link {@link ResourceSet#getResources()}) as well as their content (
	 * {@link Resource#getAllContents()}).
	 * 
	 * @param scope
	 *            the {@link ResourceSet} used to construct the scope.
	 */
	public GenericMatchScope(ResourceSet scope) {
		resolveAll(scope);
		for (Resource res : scope.getResources()) {
			addToScope(res);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IMatchScope#isInScope(org.eclipse.emf.ecore.EObject)
	 */
	public boolean isInScope(EObject eObject) {
		// if eObject is directly contained in the list, we do not have to search resources
		if (eObjectsInScope.contains(eObject)) {
			return true;
		}
		// check its containing resource
		final Resource resource = getContainingResource(eObject);
		if (resource == null) {
			return false;
		}
		final boolean inScope = isInScope(resource);
		if (inScope) {
			eObjectsInScope.add(eObject);
		}
		return inScope;
	}

	/**
	 * Adds the given resource to the set of resources considered in this scope.
	 * 
	 * @param newResource
	 *            The resource that is to be added to this scope.
	 * @since 1.3
	 */
	protected void addToScope(Resource newResource) {
		resourcesInScope.add(newResource);
	}

	/**
	 * Determines the containing resource for the given eObject.
	 * 
	 * @param eObject
	 *            the {@link EObject} whose containing resource has to be found
	 * @return the resource of the containing eObject or <code>null</code> in case the eObject does not
	 *         specify a containing resource and none containing the eObject could be found in the internal
	 *         list of resources in scope either.
	 */
	private Resource getContainingResource(EObject eObject) {
		// stereotype applications may e.g. not specify an eResource but be contained within one.
		if (eObject.eResource() != null) {
			return eObject.eResource();
		}
		for (Resource resourceInScope : resourcesInScope) {
			if (ModelUtils.contains(resourceInScope, eObject)) {
				return resourceInScope;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.engine.IMatchScope#isInScope(org.eclipse.emf.ecore.resource.Resource)
	 */
	public boolean isInScope(Resource resource) {
		if (resourcesInScope.contains(resource)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the list of {@link Resource}s to be included in the scope. This should only be used to allow
	 * the application of {@link org.eclipse.emf.compare.match.filter.IResourceFilter} to restrict the scope
	 * after its construction.
	 * 
	 * @return the list of {@link Resource}s by references.
	 */
	public List<Resource> getResourcesInScope() {
		return new ArrayList<Resource>(resourcesInScope);
	}

	/**
	 * Resolved all proxies within the given resource set.
	 * 
	 * @param resourceSet
	 *            the resource set to resolve.
	 * @since 1.3
	 */
	protected void resolveAll(ResourceSet resourceSet) {
		if (resourceSet == null) {
			return;
		}

		Set<Resource> originalSet = new HashSet<Resource>(resourceSet.getResources());
		resolveAll(originalSet);

		Set<Resource> newSet = new HashSet<Resource>(resourceSet.getResources());
		Set<Resource> delta = Sets.difference(newSet, originalSet);
		while (delta.size() > 0) {
			originalSet = newSet;
			resolveAll(delta);

			newSet = new HashSet<Resource>(resourceSet.getResources());
			delta = Sets.difference(newSet, originalSet);
		}
	}

	/**
	 * Resolves all proxies within the given set of resources.
	 * 
	 * @param resources
	 *            The resources we need to resolve.
	 */
	private void resolveAll(Set<Resource> resources) {
		final Iterator<Resource> resourceIterator = resources.iterator();
		while (resourceIterator.hasNext()) {
			Resource current = resourceIterator.next();

			final Iterator<EObject> resourceContent = current.getContents().iterator();
			while (resourceContent.hasNext()) {
				final EObject eObject = resourceContent.next();
				resolveCrossReferences(eObject);
				final TreeIterator<EObject> childContent = eObject.eAllContents();
				while (childContent.hasNext()) {
					final EObject child = childContent.next();
					if (child.eResource() != current) {
						childContent.prune();
					} else {
						resolveCrossReferences(child);
					}
				}
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
		final Iterator<EObject> objectChildren = eObject.eCrossReferences().iterator();
		while (objectChildren.hasNext()) {
			// Resolves cross references by simply visiting them.
			objectChildren.next();
		}
	}
}

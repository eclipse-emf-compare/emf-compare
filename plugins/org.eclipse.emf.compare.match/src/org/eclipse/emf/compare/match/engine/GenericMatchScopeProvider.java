/*******************************************************************************
 * Copyright (c) 2010, 2011  itemis AG (http://www.itemis.de)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexander Nyssen - itemis AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.engine;

import java.util.List;

import org.eclipse.emf.compare.match.filter.IResourceFilter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * The default implementation of an {@link IMatchScopeProvider}. It will use {@link GenericMatchScope}
 * implementations for all its scopes.
 * 
 * @author <a href="mailto:alexander.nyssen@itemis.de">Alexander Nyssen</a>
 * @since 1.1
 */
public class GenericMatchScopeProvider implements IMatchScopeProvider {

	/** The match scope used for the left side of comparison. */
	private IMatchScope leftScope;

	/** The match scope used for the right side of comparison. */
	private IMatchScope rightScope;

	/** The match scope used for the ancestor side of comparison. */
	private IMatchScope ancestorScope;

	/**
	 * Constructs a left and right {@link GenericMatchScope} with the given {@link EObject}s. No ancestor
	 * scope will be created, so calls to {@link #getAncestorScope()} will return <code>null</code>.
	 * 
	 * @param leftObject
	 *            the {@link EObject}, which will be used to construct the left scope
	 * @param rightObject
	 *            the {@link EObject}, which will be used to construct the right scope
	 */
	public GenericMatchScopeProvider(EObject leftObject, EObject rightObject) {
		this.leftScope = new GenericMatchScope(leftObject);
		this.rightScope = new GenericMatchScope(rightObject);
	}

	/**
	 * Constructs a left, right, and ancestor {@link GenericMatchScope} with the given {@link EObject}s.
	 * 
	 * @param leftObject
	 *            the {@link EObject}, which will be used to construct the left scope
	 * @param rightObject
	 *            the {@link EObject}, which will be used to construct the right scope
	 * @param ancestorObject
	 *            the {@link EObject}, which will be used to construct the ancestor scope
	 */
	public GenericMatchScopeProvider(EObject leftObject, EObject rightObject, EObject ancestorObject) {
		this(leftObject, rightObject);
		this.ancestorScope = new GenericMatchScope(ancestorObject);
	}

	/**
	 * Constructs a left and right {@link GenericMatchScope} with the given {@link Resource}s. No ancestor
	 * scope will be created, so calls to {@link #getAncestorScope()} will return <code>null</code>.
	 * 
	 * @param leftResource
	 *            the {@link Resource}, which will be used to construct the left scope
	 * @param rightResource
	 *            the {@link Resource}, which will be used to construct the right scope
	 */
	public GenericMatchScopeProvider(Resource leftResource, Resource rightResource) {
		this.leftScope = new GenericMatchScope(leftResource);
		this.rightScope = new GenericMatchScope(rightResource);
	}

	/**
	 * Constructs a left, right, and ancestor {@link GenericMatchScope} with the given {@link Resource}s.
	 * 
	 * @param leftResource
	 *            the {@link Resource}, which will be used to construct the left scope
	 * @param rightResource
	 *            the {@link Resource}, which will be used to construct the right scope
	 * @param ancestorResource
	 *            the {@link Resource}, which will be used to construct the ancestor scope
	 */
	public GenericMatchScopeProvider(Resource leftResource, Resource rightResource, Resource ancestorResource) {
		this(leftResource, rightResource);
		this.ancestorScope = new GenericMatchScope(ancestorResource);
	}

	/**
	 * Constructs a left and right {@link GenericMatchScope} with the given {@link ResourceSet}s. No ancestor
	 * scope will be created, so calls to {@link #getAncestorScope()} will return <code>null</code>.
	 * 
	 * @param leftResourceSet
	 *            the {@link ResourceSet}, which will be used to construct the left scope
	 * @param rightResourceSet
	 *            the {@link ResourceSet}, which will be used to construct the right scope
	 */
	public GenericMatchScopeProvider(ResourceSet leftResourceSet, ResourceSet rightResourceSet) {
		this.leftScope = new GenericMatchScope(leftResourceSet);
		this.rightScope = new GenericMatchScope(rightResourceSet);
	}

	/**
	 * Constructs a left, right, and ancestor {@link GenericMatchScope} with the given {@link ResourceSet}s.
	 * 
	 * @param leftResourceSet
	 *            the {@link ResourceSet}, which will be used to construct the left scope
	 * @param rightResourceSet
	 *            the {@link ResourceSet}, which will be used to construct the right scope
	 * @param ancestorResourceSet
	 *            the {@link ResourceSet}, which will be used to construct the ancestor scope
	 */
	public GenericMatchScopeProvider(ResourceSet leftResourceSet, ResourceSet rightResourceSet,
			ResourceSet ancestorResourceSet) {
		this(leftResourceSet, rightResourceSet);
		this.ancestorScope = new GenericMatchScope(ancestorResourceSet);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.engine.IMatchScopeProvider#getAncestorScope()
	 */
	public IMatchScope getAncestorScope() {
		return ancestorScope;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.engine.IMatchScopeProvider#getLeftScope()
	 */
	public IMatchScope getLeftScope() {
		return leftScope;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.engine.IMatchScopeProvider#getRightScope()
	 */
	public IMatchScope getRightScope() {
		return rightScope;
	}

	/**
	 * Allows to apply an {@link IResourceFilter} to the contained scopes, to reduce them respectively. This
	 * will only have an effect, if this scope provider was instantiated using either a {@link Resource} or a
	 * {@link ResourceSet}, but not in case an {@link EObject} was used.
	 * 
	 * @param filter
	 *            the filter to apply to the resources of the left, right and (if provided) ancestor scope.
	 */
	@SuppressWarnings("unchecked")
	public void applyResourceFilter(IResourceFilter filter) {
		if (ancestorScope != null) {
			applyExternalFilter(filter, ((GenericMatchScope)leftScope).getResourcesInScope(),
					((GenericMatchScope)rightScope).getResourcesInScope(), ((GenericMatchScope)ancestorScope)
							.getResourcesInScope());
		} else {
			applyExternalFilter(filter, ((GenericMatchScope)leftScope).getResourcesInScope(),
					((GenericMatchScope)rightScope).getResourcesInScope());
		}
	}

	/**
	 * Applies the given filter to the list of resources.
	 * 
	 * @param filter
	 *            the filter to apply.
	 * @param resources
	 *            the list of resources to be filtered (in place).
	 */
	private static void applyExternalFilter(IResourceFilter filter, List<Resource>... resources) {
		if (resources.length == 2) {
			filter.filter(resources[0], resources[1]);
		} else {
			filter.filter(resources[0], resources[1], resources[2]);
		}
	}

}

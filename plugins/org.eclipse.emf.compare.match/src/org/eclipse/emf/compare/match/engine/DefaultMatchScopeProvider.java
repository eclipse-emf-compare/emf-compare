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
package org.eclipse.emf.compare.match.engine;

import java.util.List;

import org.eclipse.emf.compare.match.filter.IResourceFilter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * This scope provider will be used by default if no other scope has been provided to EMF Compare. It uses
 * {@link DefaultMatchScope} for all scopes.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 1.3
 */
public class DefaultMatchScopeProvider implements IMatchScopeProvider {
	/** The match scope used for the left side of comparison. */
	private IMatchScope leftScope;

	/** The match scope used for the right side of comparison. */
	private IMatchScope rightScope;

	/** The match scope used for the ancestor side of comparison. */
	private IMatchScope ancestorScope;

	/**
	 * Constructs a left and right {@link DefaultMatchScope} with the given {@link EObject}s. No ancestor
	 * scope will be created, so calls to {@link #getAncestorScope()} will return <code>null</code>.
	 * 
	 * @param leftObject
	 *            the {@link EObject}, which will be used to construct the left scope
	 * @param rightObject
	 *            the {@link EObject}, which will be used to construct the right scope
	 */
	public DefaultMatchScopeProvider(EObject leftObject, EObject rightObject) {
		this.leftScope = new DefaultMatchScope(leftObject);
		this.rightScope = new DefaultMatchScope(rightObject);
	}

	/**
	 * Constructs a left, right, and ancestor {@link DefaultMatchScope} with the given {@link EObject}s.
	 * 
	 * @param leftObject
	 *            the {@link EObject}, which will be used to construct the left scope
	 * @param rightObject
	 *            the {@link EObject}, which will be used to construct the right scope
	 * @param ancestorObject
	 *            the {@link EObject}, which will be used to construct the ancestor scope
	 */
	public DefaultMatchScopeProvider(EObject leftObject, EObject rightObject, EObject ancestorObject) {
		this(leftObject, rightObject);
		this.ancestorScope = new DefaultMatchScope(ancestorObject);
	}

	/**
	 * Constructs a left and right {@link DefaultMatchScope} with the given {@link Resource}s. No ancestor
	 * scope will be created, so calls to {@link #getAncestorScope()} will return <code>null</code>.
	 * 
	 * @param leftResource
	 *            the {@link Resource}, which will be used to construct the left scope
	 * @param rightResource
	 *            the {@link Resource}, which will be used to construct the right scope
	 */
	public DefaultMatchScopeProvider(Resource leftResource, Resource rightResource) {
		this.leftScope = new DefaultMatchScope(leftResource);
		this.rightScope = new DefaultMatchScope(rightResource);
	}

	/**
	 * Constructs a left, right, and ancestor {@link DefaultMatchScope} with the given {@link Resource}s.
	 * 
	 * @param leftResource
	 *            the {@link Resource}, which will be used to construct the left scope
	 * @param rightResource
	 *            the {@link Resource}, which will be used to construct the right scope
	 * @param ancestorResource
	 *            the {@link Resource}, which will be used to construct the ancestor scope
	 */
	public DefaultMatchScopeProvider(Resource leftResource, Resource rightResource, Resource ancestorResource) {
		this(leftResource, rightResource);
		this.ancestorScope = new DefaultMatchScope(ancestorResource);
	}

	/**
	 * Constructs a left and right {@link DefaultMatchScope} with the given {@link ResourceSet}s. No ancestor
	 * scope will be created, so calls to {@link #getAncestorScope()} will return <code>null</code>.
	 * 
	 * @param leftResourceSet
	 *            the {@link ResourceSet}, which will be used to construct the left scope
	 * @param rightResourceSet
	 *            the {@link ResourceSet}, which will be used to construct the right scope
	 */
	public DefaultMatchScopeProvider(ResourceSet leftResourceSet, ResourceSet rightResourceSet) {
		this.leftScope = new DefaultMatchScope(leftResourceSet);
		this.rightScope = new DefaultMatchScope(rightResourceSet);
	}

	/**
	 * Constructs a left, right, and ancestor {@link DefaultMatchScope} with the given {@link ResourceSet}s.
	 * 
	 * @param leftResourceSet
	 *            the {@link ResourceSet}, which will be used to construct the left scope
	 * @param rightResourceSet
	 *            the {@link ResourceSet}, which will be used to construct the right scope
	 * @param ancestorResourceSet
	 *            the {@link ResourceSet}, which will be used to construct the ancestor scope
	 */
	public DefaultMatchScopeProvider(ResourceSet leftResourceSet, ResourceSet rightResourceSet,
			ResourceSet ancestorResourceSet) {
		this(leftResourceSet, rightResourceSet);
		this.ancestorScope = new DefaultMatchScope(ancestorResourceSet);
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

	/****
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.engine.IMatchScopeProvider#applyResourceFilter(org.eclipse.emf.compare.match.filter.IResourceFilter)
	 */
	@SuppressWarnings("unchecked")
	public void applyResourceFilter(IResourceFilter filter) {
		if (ancestorScope != null) {
			applyExternalFilter(filter, ((DefaultMatchScope)leftScope).getResourcesInScope(),
					((DefaultMatchScope)rightScope).getResourcesInScope(),
					((DefaultMatchScope)ancestorScope).getResourcesInScope());
		} else {
			applyExternalFilter(filter, ((DefaultMatchScope)leftScope).getResourcesInScope(),
					((DefaultMatchScope)rightScope).getResourcesInScope());
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

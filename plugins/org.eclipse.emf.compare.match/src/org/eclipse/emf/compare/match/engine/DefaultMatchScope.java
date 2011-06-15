/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.engine;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * This implementation of an {@link IMatchScope} will be used when no scope is provided to EMF Compare.
 * <p>
 * If constructed around a {@link ResourceSet}, all of the resources contained by this resource set
 * originally, and all resources fetched by a resolveAll on this resource set will be considered in the scope.
 * </p>
 * <p>
 * If constructed around a {@link Resource}, that resource along with all of its fragments and dependencies
 * will be considered in the scope.
 * </p>
 * <p>
 * Finally, if constructed around an {@link EObject}, all of this EObject's content tree and dependencies will
 * be considered in the scope.
 * </p>
 * <p>
 * That is, for all possible constructors, this scope will consider the whole resolved model when matching.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 1.3
 */
public class DefaultMatchScope extends GenericMatchScope {
	/**
	 * Delegates to {@link GenericMatchScope#GenericMatchScope(ResourceSet)}.
	 * 
	 * @param set
	 *            The resource set on which to instantiate a matching scope.
	 */
	public DefaultMatchScope(ResourceSet set) {
		super(set);
	}

	/**
	 * Delegates to {@link GenericMatchScope#GenericMatchScope(Resource)}.
	 * 
	 * @param res
	 *            The resource on which to instantiate a matching scope.
	 */
	public DefaultMatchScope(Resource res) {
		super(res);
	}

	/**
	 * Delegates to {@link GenericMatchScope#GenericMatchScope(EObject)}.
	 * 
	 * @param object
	 *            EObject on which to instantiate a matching scope.
	 */
	public DefaultMatchScope(EObject object) {
		super(object);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.engine.GenericMatchScope#resolveAll(org.eclipse.emf.ecore.resource.ResourceSet)
	 */
	@Override
	protected void resolveAll(ResourceSet resourceSet) {
		if (resourceSet != null) {
			final List<Resource> resources = resourceSet.getResources();
			for (int i = 0; i < resources.size(); ++i) {
				final Iterator<EObject> resourceContent = resources.get(i).getAllContents();
				while (resourceContent.hasNext()) {
					final EObject eObject = resourceContent.next();
					final Iterator<EObject> objectChildren = eObject.eCrossReferences().iterator();
					while (objectChildren.hasNext()) {
						// Resolves cross references by simply visiting them.
						objectChildren.next();
					}
					addToScope(eObject.eResource());
				}
			}
		}
	}
}

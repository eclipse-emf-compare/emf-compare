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

import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * This class defines the expected contract of EMF Compare scopes.
 * <p>
 * The scope will be called on all three root Notifiers in order to determine the range of a given comparison;
 * only those Notifiers will be matched by EMF Compare.
 * </p>
 * <p>
 * An implementation using Predicates to filter out the children lists can be sub-classed instead, see
 * {@link FilterComparisonScope}.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @see AbstractComparisonScope
 * @see FilterComparisonScope
 */
public interface IComparisonScope {
	/**
	 * This will be used by EMF Compare in order to retrieve the left "root" Notifier of this comparison; i.e
	 * the first object to be considered by the match engine, and from which the iteration over children
	 * should start.
	 * 
	 * @return The left root of this comparison. May not be <code>null</code>.
	 */
	Notifier getLeft();

	/**
	 * This will be used by EMF Compare in order to retrieve the right "root" Notifier of this comparison; i.e
	 * the first object to be considered by the match engine, and from which the iteration over children
	 * should start.
	 * 
	 * @return The right root of this comparison. May not be <code>null</code>.
	 */
	Notifier getRight();

	/**
	 * If EMF Compare should consider a Notifier as being the common ancestor of the "left" and "right"
	 * objects to compare, it should be returned from here.
	 * 
	 * @return The origin root for this comparison. May be <code>null</code>.
	 */
	Notifier getOrigin();

	/**
	 * This will be used by EMF Compare in order to determine the Resources that should be considered part of
	 * the comparison when it is launched on the given resource set.
	 * <p>
	 * Do note that this will only be called once per Resource. We will retrieve the set of EMF resources to
	 * include in the comparison, match them, then use {@link #getCoveredEObjects(Resource)} in order to
	 * determine the actual EObjects to cover during that comparison.
	 * </p>
	 * 
	 * @param resourceSet
	 *            The resource set for which we need to know all resources spanned by the comparison.
	 * @return An iterator over the {@link Resource}s which are part of this scope.
	 */
	Iterator<? extends Resource> getCoveredResources(ResourceSet resourceSet);

	/**
	 * This will be used by EMF Compare in order to determine the EObjects that it should iterate over.
	 * <p>
	 * Do note that this will only be called once per comparison. EMF Compare expects this to return the whole
	 * set of EObjects that should be covered by the comparison when launched on the given Resource.
	 * </p>
	 * 
	 * @param resource
	 *            The resource for which we need to determine all EObjects spanned by the comparison.
	 * @return An iterator over the {@link EObject}s which are part of this scope.
	 */
	Iterator<? extends EObject> getCoveredEObjects(Resource resource);

	/**
	 * This will be used by EMF Compare in order to know which EObjects should be considered to be part of the
	 * comparison scope when it is launched on the given EObject.
	 * <p>
	 * Do note that his will only be called once per comparison on EObjects, and never for comparisons
	 * launched on {@link ResourceSet}s or {@link Resource}s. EMF Compare expects the whole set of EObject
	 * that should be covered by the comparison to be returned by this.
	 * </p>
	 * 
	 * @param eObject
	 *            The EObject for which we need to determine the comparison scope.
	 * @return An iterator over the {@link EObject}s which are part of this scope.
	 */
	Iterator<? extends EObject> getChildren(EObject eObject);

	/**
	 * This will be used by EMF Compare in order to retrieve the namespace uris detected in the scope.
	 * 
	 * @return The namespace uris.
	 */
	Set<String> getNsURIs();

	/**
	 * This will be used by EMF Compare in order to retrieve the resource uris detected in the scope.
	 * 
	 * @return The resource uris.
	 */
	Set<String> getResourceURIs();
}

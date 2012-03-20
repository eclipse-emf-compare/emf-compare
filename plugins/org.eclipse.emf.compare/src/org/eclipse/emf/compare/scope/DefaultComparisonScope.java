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

import com.google.common.collect.Iterators;

import java.util.Iterator;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This implementation of an {@link AbstractComparisonScope} will return all of its Notifiers' contents
 * without any filtering.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class DefaultComparisonScope extends AbstractComparisonScope {
	/**
	 * Simmply delegates to the super constructor.
	 * 
	 * @param left
	 *            Left root of this comparison.
	 * @param right
	 *            Right root of this comparison.
	 * @param origin
	 *            Common ancestor of <code>left</code> and <code>right</code>.
	 */
	public DefaultComparisonScope(Notifier left, Notifier right, Notifier origin) {
		super(left, right, origin);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.scope.AbstractComparisonScope#getChildren(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public Iterator<? extends EObject> getChildren(EObject eObject) {
		final Iterator<EObject> properContent = Iterators.filter(EcoreUtil.getAllProperContents(eObject,
				false), EObject.class);
		return Iterators.unmodifiableIterator(properContent);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.scope.AbstractComparisonScope#getChildren(org.eclipse.emf.ecore.resource.Resource)
	 */
	@Override
	public Iterator<? extends EObject> getChildren(Resource resource) {
		final Iterator<EObject> properContent = Iterators.filter(EcoreUtil.getAllProperContents(resource,
				false), EObject.class);
		return Iterators.unmodifiableIterator(properContent);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.scope.AbstractComparisonScope#getChildren(org.eclipse.emf.ecore.resource.ResourceSet)
	 */
	@Override
	public Iterator<? extends Resource> getChildren(ResourceSet resourceSet) {
		return Iterators.unmodifiableIterator(resourceSet.getResources().iterator());
	}
}

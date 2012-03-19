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

import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

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
	 * @see org.eclipse.emf.compare.scope.AbstractComparisonScope#getChildren(org.eclipse.emf.common.notify.Notifier)
	 */
	@Override
	public Iterable<Notifier> getChildren(Notifier notifier) {
		final List<Notifier> children = Lists.newArrayList();
		if (notifier instanceof ResourceSet) {
			children.addAll(((ResourceSet)notifier).getResources());
		} else if (notifier instanceof Resource) {
			children.addAll(((Resource)notifier).getContents());
		} else if (notifier instanceof EObject) {
			children.addAll(((EObject)notifier).eContents());
		}
		return children;
	}
}

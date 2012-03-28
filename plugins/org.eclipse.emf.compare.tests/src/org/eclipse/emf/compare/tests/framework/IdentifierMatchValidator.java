/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.framework;

import static junit.framework.Assert.assertEquals;

import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * This implementation of a {@link AbstractMatchValidator} expects that all
 * sides of a given {@link Match} share the same identifier according to the
 * semantics of {@link #getID(EObject)}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class IdentifierMatchValidator extends AbstractMatchValidator {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.tests.framework.AbstractMatchValidator#validateMatch(org.eclipse.emf.compare.Match)
	 */
	@Override
	protected void validateMatch(Match match) {
		final EObject left = match.getLeft();
		final EObject right = match.getRight();
		final EObject origin = match.getOrigin();

		if (left != null && right != null && origin != null) {
			final String id = getID(left);
			assertEquals(id, getID(right));
			assertEquals(id, getID(origin));
		} else if (left != null && right != null) {
			assertEquals(getID(left), getID(right));
		} else if (left != null && origin != null) {
			assertEquals(getID(left), getID(origin));
		} else if (right != null && origin != null) {
			assertEquals(getID(right), getID(origin));
		}
	}

	/**
	 * Retrieves the identifier of the given EObject. We'll consider the
	 * attribute ID first, then the XMI ID, and simply return <code>null</code>
	 * if none are set.
	 * 
	 * @param eObject
	 *            The EObject for which we need an identifier.
	 * @return The unique identifier of the given EObject if any,
	 *         <code>null</code> if none.
	 */
	protected String getID(EObject eObject) {
		String identifier = EcoreUtil.getID(eObject);
		if (identifier == null) {
			final Resource eObjectResource = eObject.eResource();
			if (eObjectResource instanceof XMIResource) {
				identifier = ((XMIResource) eObjectResource).getID(eObject);
			}
		}
		return identifier;
	}
}

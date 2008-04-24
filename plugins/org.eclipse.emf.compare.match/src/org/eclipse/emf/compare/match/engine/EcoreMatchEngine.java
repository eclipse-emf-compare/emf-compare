/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.engine;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;

/**
 * This will add ecore-specific behavior to the {@link GenericMatchEngine}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EcoreMatchEngine extends GenericMatchEngine {
	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.match.engine.GenericMatchEngine#isSimilar(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected boolean isSimilar(EObject obj1, EObject obj2) throws FactoryException {
		if (obj1 instanceof EGenericType || obj2 instanceof EGenericType) {
			return isSimilar(obj1.eContainer(), obj2.eContainer());
		}
		return super.isSimilar(obj1, obj2);
	}
}

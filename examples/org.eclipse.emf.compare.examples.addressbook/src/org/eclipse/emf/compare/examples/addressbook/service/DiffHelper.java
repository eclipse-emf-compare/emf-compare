/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.examples.addressbook.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.compare.diff.metamodel.AddModelElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Utility class to retrieve elements from a {@link DiffModel}. At some point it may be included in the EMF
 * compare utility classes.
 * 
 * @author Cedric Brun <cedric.brun@obeo.fr>
 */
public class DiffHelper {

	public static AddModelElement isAdded(EObject instance, DiffModel diff) {
		final EcoreUtil.CrossReferencer referencer = getCrossReferencer(instance, diff);
		final Collection<EStructuralFeature.Setting> references = referencer.get(instance);
		if (references != null && references.size() > 0) {
			final Iterator<EStructuralFeature.Setting> it = references.iterator();
			while (it.hasNext()) {
				final EStructuralFeature.Setting setting = it.next();
				final EObject eObj = setting.getEObject();
				if (eObj instanceof AddModelElement && ((AddModelElement)eObj).getLeftElement() == instance)
					return (AddModelElement)eObj;
			}
		}
		return null;

	}

	public static EcoreUtil.CrossReferencer getCrossReferencer(EObject instance, DiffModel diff) {
		final Collection<EObject> models = new ArrayList<EObject>(2);
		models.add(instance);
		models.add(diff);
		final EcoreUtil.CrossReferencer referencer = new EcoreUtil.CrossReferencer(models) {
			private static final long serialVersionUID = 1L;

			// initializer for this anonymous class
			{
				crossReference();
			}

		};
		return referencer;
	}
}

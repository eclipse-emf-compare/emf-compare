/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.merge;

import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.merge.ReferenceChangeMerger;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * UML does not support "EList#addUnique" correctly, especially with references that are part of a
 * subset/superset relationship. This specific merger will intercept calls to list additions in order to user
 * the under-performing "EList#add" instead.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class UMLReferenceChangeMerger extends ReferenceChangeMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#isMergerFor(org.eclipse.emf.compare.Diff)
	 */
	@Override
	public boolean isMergerFor(Diff target) {
		if (target instanceof ReferenceChange) {
			final EObject container = ((ReferenceChange)target).getReference().eContainer();
			if (container instanceof EClass) {
				return ((EClass)container).getEPackage() instanceof UMLPackage;
			}
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	protected <E> void addAt(List<E> list, E value, int insertionIndex) {
		if (insertionIndex < 0 || insertionIndex > list.size()) {
			list.add(value);
		} else {
			list.add(insertionIndex, value);
		}
	}
}

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
package org.eclipse.emf.compare.internal;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;

/**
 * This implementation of an {@link ECrossReferenceAdapter} will allow us to only attach ourselves to the Diff
 * elements.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class DiffCrossReferencer extends ECrossReferenceAdapter {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#isIncluded(org.eclipse.emf.ecore.EReference)
	 */
	@Override
	protected boolean isIncluded(EReference eReference) {
		if (super.isIncluded(eReference)) {
			EClass eClass = eReference.getEContainingClass();
			return eClass.getEAllSuperTypes().contains(ComparePackage.Literals.DIFF);
		}
		return false;

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#addAdapter(org.eclipse.emf.common.notify.Notifier)
	 */
	@Override
	protected void addAdapter(Notifier notifier) {
		// We only need to install ourselves on Match elements (to listen to new Diffs) and the Diff
		// themselves as they're what we wish to cross reference.
		if (notifier instanceof Match || notifier instanceof Diff) {
			super.addAdapter(notifier);
		}
	}
}

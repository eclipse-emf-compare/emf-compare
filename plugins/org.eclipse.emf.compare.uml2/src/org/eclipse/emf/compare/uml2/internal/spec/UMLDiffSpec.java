/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.spec;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.internal.impl.UMLDiffImpl;

/**
 * This specialization of the {@link UMLDiffImpl} class allows us to define the derived features and
 * operations implementations.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class UMLDiffSpec extends UMLDiffImpl {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.impl.UMLDiffImpl#getPrimeRefining()
	 */
	@Override
	public Diff getPrimeRefining() {
		if (primeRefining == null) {
			for (Diff refBy : this.getRefinedBy()) {
				if (refBy instanceof ReferenceChange) {
					ReferenceChange rc = (ReferenceChange)refBy;
					if (rc.getReference().isContainment() && (this.getDiscriminant() == rc.getValue())) {
						primeRefining = rc;
						break;
					}
				}
			}
		}
		return primeRefining;
	}
}

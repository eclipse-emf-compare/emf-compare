/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.emfdiff2mpatch.generic.impl;

import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.generic.util.QvtlibHelper;
import org.eclipse.emf.compare.mpatch.symrefs.IdEmfReference;
import org.eclipse.emf.compare.mpatch.symrefs.SymrefsFactory;
import org.eclipse.emf.ecore.EObject;

/**
 * This implementation created an {@link IdEmfReference} as symbolic reference for a given model element.
 * 
 * {@link IdEmfReference}s use the {@link URI} fragment of the given model element as the ID. This makes these
 * references generically applicable for all EMF models. The cardinality of those implementations, however, is always
 * <code>[1..1]</code>.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class IdReferenceCreator extends AbstractReferenceCreator {

	/**
	 * Create an id-based symbolic reference.
	 * 
	 * @param self
	 *            The object for which an id-based reference should be created.
	 * @return The created symbolic reference based on the last {@link URI} fragment as id.
	 */
	protected IdEmfReference createSymbolicReference(EObject self) {

		// create the id-based symbolic reference
		final IdEmfReference ref = SymrefsFactory.eINSTANCE.createIdEmfReference();
		ref.setType(self.eClass());
		ref.setUriReference(QvtlibHelper.getUriString(self));
		ref.setLabel(QvtlibHelper.getLabel(self));

		// just use the value we get from the resource ;-)
		ref.setIdAttributeValue(self.eResource().getURIFragment(self));

		return ref;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLabel() {
		return "ID-based";
	}

}

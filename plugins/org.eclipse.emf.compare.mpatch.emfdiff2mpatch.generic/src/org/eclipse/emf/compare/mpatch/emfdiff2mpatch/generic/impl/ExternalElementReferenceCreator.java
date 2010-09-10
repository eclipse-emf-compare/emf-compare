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

import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.generic.util.QvtlibHelper;
import org.eclipse.emf.compare.mpatch.symrefs.ExternalElementReference;
import org.eclipse.emf.compare.mpatch.symrefs.SymrefsFactory;
import org.eclipse.emf.ecore.EObject;


/**
 * This class creates external symbolic references.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class ExternalElementReferenceCreator {

	/**
	 * This creates an instance of {@link ExternalElementReference}, sets the {@link URI}, type, and a label of the
	 * given model element <code>self</code>.
	 * 
	 * @param self
	 *            A model element for which an external symbolic reference should be created.
	 * @return An external symbolic reference.
	 */
	static IElementReference toSymbolicReference(EObject self) {
		final ExternalElementReference ref = SymrefsFactory.eINSTANCE.createExternalElementReference();
		ref.setType(self.eClass());
		ref.setUriReference(QvtlibHelper.getUriString(self));
		ref.setLabel(self.eResource().getURIFragment(self));
		return ref;
	}

}

/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.transform.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.ModelDescriptorReference;
import org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference;
import org.eclipse.emf.compare.mpatch.symrefs.SymrefsPackage;
import org.eclipse.emf.compare.mpatch.util.ExtEcoreUtils;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * Helper class for weakenings.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class WeakeningHelper {

	/**
	 * Collect all weakenable symbolic references.
	 * <ul>
	 * <li> {@link ElementSetReference}
	 * <li> {@link ModelDescriptorReference}
	 * </ul>
	 * 
	 * @param mpatch
	 *            The {@link MPatchModel} for which all weakenable references should be collected.
	 * @return A collection of all weakenable symbolic references.
	 */
	public static Collection<IElementReference> getWeakenableSymbolicReferences(MPatchModel mpatch) {
		// collect all potential symrefs
		final Set<EClass> types = new HashSet<EClass>();
		types.add(SymrefsPackage.eINSTANCE.getElementSetReference());
		types.add(MPatchPackage.eINSTANCE.getModelDescriptorReference());
		final List<EObject> symRefs = ExtEcoreUtils.collectTypedElements(mpatch.getChanges(), types, false);

		// filter and return weakenable references
		final List<IElementReference> result = new ArrayList<IElementReference>();
		for (EObject ref : symRefs) {
			/*
			 * PK: Return all those references! Concrete implementations may do additional filtering..
			 */
			// if (isWeakenable((IElementReference) ref))
			result.add((IElementReference)ref);
		}
		return result;
	}
}

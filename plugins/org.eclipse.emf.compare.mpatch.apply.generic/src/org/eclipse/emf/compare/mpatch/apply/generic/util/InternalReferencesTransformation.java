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
package org.eclipse.emf.compare.mpatch.apply.generic.util;

import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.apply.generic.impl.InternalReferences;


/**
 * Public API for manually calling the transformation that replaces internal symbolic references to added/removed
 * elements with {@link ModelDescriptorReference}s in an {@link MPatchModel}. This is required for the default
 * symbolic reference resolver to work properly in case of refined symbolic references.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class InternalReferencesTransformation {

	/**
	 * Replace all cross-references between {@link IndepChange}s with internal {@link ModelDescriptorReference}s. These
	 * are all references from:
	 * <ul>
	 * <li> {@link IModelDescriptor#getAllCrossReferences()}
	 * <li> {@link IndepMoveElementChange#getOldParent()} and {@link IndepMoveElementChange#getNewParent()}
	 * <li> {@link IndepUpdateReferenceChange#getOldReference()} and {@link IndepUpdateReferenceChange#getNewReference()}
	 * <li> {@link IndepAddRemReferenceChange#getChangedReference()}
	 * </ul>
	 * to an element which is described by an {@link IModelDescriptor}.
	 * 
	 * @param mpatch
	 *            MPatch.
	 * @return The number of replaced references.
	 */
	public static int createInternalReferences(MPatchModel mpatch) {
		return InternalReferences.createInternalReferences(mpatch);
	}
}

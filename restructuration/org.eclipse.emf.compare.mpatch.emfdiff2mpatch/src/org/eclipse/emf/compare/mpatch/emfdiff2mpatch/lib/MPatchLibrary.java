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
package org.eclipse.emf.compare.mpatch.emfdiff2mpatch.lib;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Blackbox library for a QVTo transformation from an emfdiff to an mpatch.<br>
 * It provides three functions:
 * <ul>
 * <li>Creating symbolic references for arbitrary {@link EObject}s
 * <li>Creating self-contained model descriptors for arbitrary {@link EObject}s
 * <li>Extracting a String representation the {@link URI} of a given {@link Resource}
 * </ul>
 * The first two functions are implemented as Eclipse extensions. Default implementations are provided with a low
 * priority in the package <code>org.eclipse.emf.compare.mpatch.qvtlib.impl</code>, so that other plugins may easily
 * override or extend the default behaviour.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class MPatchLibrary {

	/**
	 * Convert the {@link URI} of a given {@link Resource} (the datatype EResource in ECore) to a {@link String}.
	 * 
	 * @param self
	 *            The resource.
	 * @return A {@link String} representation of the resource's {@link URI}.
	 */
	// @Operation(contextual = true)
	public String toUriString(Resource self) {
		if (self == null || self.getURI() == null)
			return null;
		return self.getURI().toString();
	}

	/**
	 * @see {@link ISymbolicReferenceCreator#toSymbolicReference(EObject)}
	 */
	// @Operation(contextual = true)
	public IElementReference toSymbolicReference(EObject self) {
		return MPatchLibraryComponents.getSymbolicReferenceCreator().toSymbolicReference(self);
	}

	/**
	 * @see {@link IModelDescriptorCreator#toModelDescriptor(EObject, boolean, ISymbolicReferenceCreator)}
	 */
	// @Operation(contextual = true)
	public IModelDescriptor toModelDescriptor(EObject self, boolean serializable) {
		return MPatchLibraryComponents.getModelDescriptorCreator().toModelDescriptor(self, serializable,
				MPatchLibraryComponents.getSymbolicReferenceCreator());
	}

}

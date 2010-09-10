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
package org.eclipse.emf.compare.mpatch.extension;

import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.ecore.EObject;

/**
 * Extension for creating model descriptors for arbitrary model elements ({@link EObject}s).
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public interface IModelDescriptorCreator {

	/**
	 * The id of the extension point this interface is used in.
	 */
	String EXTENSION_ID = "org.eclipse.emf.compare.mpatch.modeldescriptor";

	/**
	 * Create a model descriptor for the context.<br>
	 * The implementation stores all attributes and references (as symbolic references using
	 * {@link MPatchLibrary#toSymbolicReference(EObject)}) in an {@link EMFModelDescriptor}.
	 * 
	 * @param self
	 *            The context, an arbitrary {@link EObject}.
	 * @param serializable
	 *            If <code>true</code>, then only non-transient, non-derived, and changeable attributes are stored.
	 *            Otherwise all attributes are stored.
	 * @param symbolicReferenceCreator
	 *            The {@link ISymbolicReferenceCreator} which is used to create symbolic references in the model
	 *            descriptor.
	 * @return A self-contained descriptor of the given context as an {@link IModelDescriptor}.
	 */
	IModelDescriptor toModelDescriptor(EObject self, boolean serializable,
			ISymbolicReferenceCreator symbolicReferenceCreator);

	/**
	 * The label for this creator.<br>
	 * 
	 * @return <b>Note: the label must be <i>unique</i> over all labels of all extensions!</b>
	 */
	String getLabel();

}

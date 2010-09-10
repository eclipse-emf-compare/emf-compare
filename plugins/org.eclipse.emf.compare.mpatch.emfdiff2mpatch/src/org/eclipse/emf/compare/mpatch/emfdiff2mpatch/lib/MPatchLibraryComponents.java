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
package org.eclipse.emf.compare.mpatch.emfdiff2mpatch.lib;

import org.eclipse.emf.compare.mpatch.common.util.ExtensionManager;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;

/**
 * The qvto transformation cannot load {@link MPatchLibrary} if there are public setters and getters for these two
 * variables. This is why they are set here in the helper class.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class MPatchLibraryComponents {

	/** Set the initial symref creator from the properties, if it exists. */
	private static ISymbolicReferenceCreator symbolicReferenceCreator = ExtensionManager
			.getSelectedSymbolicReferenceCreator();

	/** Set the initial model descriptor creator from the properties, if it exists. */
	private static IModelDescriptorCreator modelDescriptorCreator = ExtensionManager
			.getSelectedModelDescriptorCreator();

	/**
	 * Get the symbolic reference creator.
	 * 
	 * @return The symbolic reference creator, if it was set.
	 */
	public static ISymbolicReferenceCreator getSymbolicReferenceCreator() {
		return symbolicReferenceCreator;
	}

	/**
	 * Set the symbolic reference creator.
	 * 
	 * @param symbolicReferenceCreator
	 *            The symbolic reference creator.
	 */
	public static void setSymbolicReferenceCreator(ISymbolicReferenceCreator symbolicReferenceCreator) {
		MPatchLibraryComponents.symbolicReferenceCreator = symbolicReferenceCreator;
	}

	/**
	 * Set the model descriptor creator.
	 * 
	 * @return The model descriptor creator, if it was set.
	 */
	public static IModelDescriptorCreator getModelDescriptorCreator() {
		return modelDescriptorCreator;
	}

	/**
	 * Set the model descriptor creator.
	 * 
	 * @param modelDescriptorCreator
	 *            The model descriptor creator.
	 */
	public static void setModelDescriptorCreator(IModelDescriptorCreator modelDescriptorCreator) {
		MPatchLibraryComponents.modelDescriptorCreator = modelDescriptorCreator;
	}

}

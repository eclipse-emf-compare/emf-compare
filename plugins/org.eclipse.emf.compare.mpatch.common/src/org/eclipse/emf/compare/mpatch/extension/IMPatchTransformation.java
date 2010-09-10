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

import org.eclipse.emf.compare.mpatch.MPatchModel;

/**
 * Additional in-place transformations for {@link MPatchModel}s. Registered extensions automatically show up in the
 * dialog for applications.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public interface IMPatchTransformation {

	/**
	 * The id of the extension point this interface is used in.
	 */
	String EXTENSION_ID = "org.eclipse.emf.compare.mpatch.transform";

	/**
	 * Implementations are supposed to transform the given {@link MPatchModel}. The transformation provided by
	 * <i>org.eclipse.emf.compare.mpatch.emfdiff2mpatch</i> created just a flat and unstructured set of atomic
	 * differences.
	 * 
	 * @param mpatch
	 *            MPatch.
	 * @return <code>0</code>, if the transformation did not change anything, otherwise the number of modifications.
	 */
	int transform(MPatchModel mpatch);

	/**
	 * The label.
	 * 
	 * @return The label for this transformation.<br>
	 *         <b>Note: the label must be <i>unique</i> over all labels of all extensions!</b>
	 */
	String getLabel();

	/**
	 * The description.
	 * 
	 * @return A description for this transformation which is shown in the GUI.
	 */
	String getDescription();

	/**
	 * The preferred priority of this transformation. If there are multiple transformations of the same priority, their
	 * order is random. If the transformation is optional (#isOptional()), then the user may change the order.
	 * 
	 * @return The priority of this transformation.
	 */
	int getPriority();

	/**
	 * Defines whether this transformation is optional. If it is, then the user may select or deselect it. If it is not,
	 * then the priority defines the order amongst all non-optional transformations.
	 * 
	 * <b>Non-optional transformation are always performed before the optional transformations!</b>
	 * 
	 * @return Whether or not this transformation is optional.
	 */
	boolean isOptional();

}

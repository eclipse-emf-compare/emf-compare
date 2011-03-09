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
package org.eclipse.emf.compare.mpatch.transform.util;

import org.eclipse.emf.compare.mpatch.IndepAddAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveAttributeChange;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.transform.impl.ReverseMPatch;

/**
 * Public API for reversing an {@link MPatchModel}.
 * 
 * This reverses the direction of a given MPatch. By default, an MPatch P created from a Model A which is the unchanged
 * version of a Model B can be used to reproduce Model B out of Model A, and not vice versa! So P is directed and cannot
 * be used to create Model A out of Model B.
 * 
 * This transformation reverses P and creates P_r such that P_r applied to Model B yields Model A.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class ReverseTransformation {

	/**
	 * Reverse a given {@link MPatchModel}.
	 * 
	 * Simple reverse all changes, e.g. an {@link IndepAddAttributeChange} becomes an {@link IndepRemoveAttributeChange}, etc.
	 * 
	 * @param mpatch
	 *            Input for the reversing.
	 * @return The number of reversed changes or <code>0</code> if none was reversed.
	 */
	public static int reverse(MPatchModel mpatch) {
		return ReverseMPatch.reverse(mpatch);
	}

}

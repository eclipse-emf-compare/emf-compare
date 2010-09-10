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
import org.eclipse.emf.compare.mpatch.apply.generic.impl.MPatchDependencies;


/**
 * Public API for manually calling the transformation that introduces dependencies between {@link IndepChange}s in an
 * {@link MPatchModel}.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class MPatchDependencyTransformation {

	/**
	 * Calculate a dependency graph between all changes in the given {@link MPatchModel}.
	 * 
	 * @param mpatch
	 *            MPatch.
	 * @return The number of dependencies.
	 */
	public static int calculateDependencies(MPatchModel mpatch) {
		return MPatchDependencies.calculateDependencies(mpatch);
	}
}

/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.equi;

import org.eclipse.emf.compare.Comparison;

/**
 * .
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public interface IEquiEngine {

	/**
	 * This is the entry point of the equivalence computing process.
	 * <p>
	 * It will complete the input <code>comparison</code> by iterating over the
	 * {@link org.eclipse.emf.compare.Diff differences} it contain, filling in the equivalence it can detect
	 * for each distinct Diff.
	 * </p>
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 */
	void computeEquivalences(Comparison comparison);
}

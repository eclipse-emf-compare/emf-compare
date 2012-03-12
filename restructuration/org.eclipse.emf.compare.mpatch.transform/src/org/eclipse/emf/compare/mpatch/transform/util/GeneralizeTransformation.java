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

import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.transform.impl.MergeChanges;
import org.eclipse.emf.compare.mpatch.transform.impl.UnboundSymbolicReferencesWeakening;
import org.eclipse.emf.compare.mpatch.transform.impl.ScopeExpansion;

/**
 * Public API for generalizing {@link MPatchModel}s.
 * 
 * Two transformations are available by default:
 * <ul>
 * <li> {@link GeneralizeTransformation#expandScope(MPatchModel)} replaces the equality check of String attributes in OCL
 * conditions with a 'containsIgnoreCase' operation.
 * <li> {@link GeneralizeTransformation#unboundSymbolicReferences(MPatchModel)} replaces the bounds of weakenable
 * symbolic references with <code>[1..-1]</code> (equals <code>[1..*]</code>).
 * <li> {@link GeneralizeTransformation#mergeChanges(MPatchModel)} replaces similar changes with a merged changes.
 * </ul>
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class GeneralizeTransformation {

	/**
	 * This replaces the equality check of String attributes in OCL conditions with a 'containsIgnoreCase' operation.
	 * 
	 * @return The number of modified OCL conditions.
	 */
	public static int expandScope(MPatchModel mpatch) {
		return ScopeExpansion.weakenOCLConditions(mpatch);
	}

	/**
	 * This replaces the bounds of appropriate symbolic references with <code>[1..-1]</code> (equals <code>[1..*]</code>
	 * ).
	 * 
	 * @return The number of modified symbolic references.
	 */
	public static int unboundSymbolicReferences(MPatchModel mpatch) {
		return UnboundSymbolicReferencesWeakening.weakenBounds(mpatch);
	}

	/**
	 * Merge similar changes that describe the same changes but operate on different model elements.
	 * 
	 * @return The number of newly created merged changes.
	 */
	public static int mergeChanges(MPatchModel mpatch) {
		return MergeChanges.mergeChanges(mpatch);
	}

}

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
package org.eclipse.emf.compare.mpatch.transform.util;

import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.transform.impl.DefaultMPatchGrouping;

/**
 * Public API for performing a transformation introducing {@link ChangeGroup}s in an {@link MPatchModel}.
 * 
 * The grouping is a heuristic algorithm which works as follows.<br>
 * <b>Assumption:</b> each {@link IndepChange} in the {@link MPatchModel} as well as all model elements (defined by
 * their URI) are nodes in a graph, and all {@link IElementReference}s are (undirected) arcs.<br>
 * Then the {@link IndepChange}s of each <b>connected component</b> (no connections to other nodes) are grouped in
 * one {@link ChangeGroup}.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class GroupingTransformation {

	/**
	 * Introduce groups in the given {@link MPatchModel}.
	 * 
	 * The criteria for putting two changes into a group are that their symbolic references have elements in common.
	 * 
	 * @param mpatch
	 *            Input for the grouping.
	 * @return The number of created groups or <code>0</code> if none was created.
	 */
	public static int group(MPatchModel mpatch) {
		return DefaultMPatchGrouping.group(mpatch);
	}

}

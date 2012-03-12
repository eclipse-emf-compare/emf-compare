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
package org.eclipse.emf.compare.mpatch.extension;

/**
 * A callback for the user-interactive way of resolving symbolic references.
 * 
 * @see IMPatchResolution
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public interface IMPatchResolutionHost {

	/**
	 * Notification about refined symbolic references.
	 * 
	 * @param mapping
	 *            This must be the very same object as the parameter in
	 *            {@link IMPatchResolution#refineResolution(ResolvedSymbolicReferences, IMPatchResolutionHost)}!
	 * @return <code>true</code>, if the refinement is accepted (no conflicts prevail); <code>false</code> otherwise.
	 */
	boolean resolved(ResolvedSymbolicReferences mapping);

}

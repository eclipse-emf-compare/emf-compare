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
package org.eclipse.emf.compare.mpatch.transform.actions;

import org.eclipse.emf.compare.mpatch.extension.IMPatchTransformation;
import org.eclipse.emf.compare.mpatch.transform.impl.MergeChanges;


/**
 * The action for {@link MergeChanges}.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class MergeChangesAction extends AbstractGeneralizationAction {

	/**
	 * This action just delegates the call to
	 * {@link MergeChanges#transform(org.eclipse.emf.compare.mpatch.MPatchModel)}.
	 */
	@Override
	protected IMPatchTransformation getTransformation() {
		return new MergeChanges();
	}

}

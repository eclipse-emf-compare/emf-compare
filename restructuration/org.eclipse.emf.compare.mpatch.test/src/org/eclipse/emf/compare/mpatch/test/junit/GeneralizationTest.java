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
package org.eclipse.emf.compare.mpatch.test.junit;

import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.test.util.CommonTestOperations;
import org.eclipse.emf.compare.mpatch.test.util.CompareTestHelper;
import org.eclipse.emf.compare.mpatch.test.util.TestConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Test;


/**
 * The test model is structured as follows:
 * 
 * Entity Container
 *   + Entity A
 *   + Entity B
 *   + Entity C
 *   
 * The differences (multiref.mpatch) describe the adding of a new Entity x which contains reference to all three sub-entities.
 * These references' targets are again all three entities.
 * That is, three Entities should be added including references to each of the entity. 
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class GeneralizationTest {

	/**
	 * See test description here: {@link GeneralizationTest}.
	 */
	@Test
	public void testMultiRef() {
		// load models
		final EObject model = CompareTestHelper.loadModel(TestConstants.MULTI_REF_URI1, new ResourceSetImpl()).get(0);
		final EObject expectedModel = CompareTestHelper.loadModel(TestConstants.MULTI_REF_URI2, new ResourceSetImpl()).get(0);
		
		// load diff
		final MPatchModel mpatch = (MPatchModel)CompareTestHelper.loadModel(TestConstants.MULTI_REF_DIFF_URI, new ResourceSetImpl()).get(0);
		
		// apply dependencies and check against expected model
		CommonTestOperations.createAndApplyMPatch(mpatch, model, expectedModel, null, "multiref");
	}
	
}

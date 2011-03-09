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

import org.eclipse.emf.compare.mpatch.IndepAddAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepAddElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepMoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;
import org.eclipse.emf.compare.mpatch.test.util.CommonTestOperations;
import org.eclipse.emf.compare.mpatch.test.util.TestConstants;
import org.junit.Test;

/**
 * This test checks each change type in an individual test case.
 * It calculates the differences, applies them to the unchanged version, and compares it with the changed version.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class IndividualTest {

	/**
	 * Test applicability of {@link IndepAddElementChange}.
	 */
	@Test
	public void testApplyAddElement() {
		testApply(TestConstants.INDIVIDUAL_ADD_REM_ELEMENT_URI1, TestConstants.INDIVIDUAL_ADD_REM_ELEMENT_URI2);
	}

	/**
	 * Test applicability of {@link IndepRemoveElementChange}.
	 */
	@Test
	public void testApplyRemoveElement() {
		testApply(TestConstants.INDIVIDUAL_ADD_REM_ELEMENT_URI2, TestConstants.INDIVIDUAL_ADD_REM_ELEMENT_URI1);
	}

	/**
	 * Test applicability of {@link IndepMoveElementChange}.
	 */
	@Test
	public void testApplyMoveElement() {
		testApply(TestConstants.INDIVIDUAL_MOVE_ELEMENT_URI1, TestConstants.INDIVIDUAL_MOVE_ELEMENT_URI2);
	}

	/**
	 * Test applicability of {@link IndepAddAttributeChange}.
	 */
	@Test
	public void testApplyAddAttribute() {
		testApply(TestConstants.INDIVIDUAL_ADD_REM_ATTRIBUTE_URI1, TestConstants.INDIVIDUAL_ADD_REM_ATTRIBUTE_URI2);
	}

	/**
	 * Test applicability of {@link IndepRemoveAttributeChange}.
	 */
	@Test
	public void testApplyRemoveAttribute() {
		testApply(TestConstants.INDIVIDUAL_ADD_REM_ATTRIBUTE_URI2, TestConstants.INDIVIDUAL_ADD_REM_ATTRIBUTE_URI1);
	}

	/**
	 * Test applicability of {@link IndepUpdateAttributeChange}.
	 */
	@Test
	public void testApplyUpdateAttribute() {
		testApply(TestConstants.INDIVIDUAL_UPDATE_ATTRIBUTE_URI1, TestConstants.INDIVIDUAL_UPDATE_ATTRIBUTE_URI2);
	}

	/**
	 * Test applicability of {@link IndepAddReferenceChange}.
	 */
	@Test
	public void testApplyAddReference() {
		testApply(TestConstants.INDIVIDUAL_ADD_REM_REFERENCE_URI1, TestConstants.INDIVIDUAL_ADD_REM_REFERENCE_URI2);
	}

	/**
	 * Test applicability of {@link IndepRemoveReferenceChange}.
	 */
	@Test
	public void testApplyRemoveReference() {
		testApply(TestConstants.INDIVIDUAL_ADD_REM_REFERENCE_URI2, TestConstants.INDIVIDUAL_ADD_REM_REFERENCE_URI1);
	}

	/**
	 * Test applicability of {@link IndepUpdateReferenceChange}.
	 */
	@Test
	public void testApplyUpdateReference() {
		testApply(TestConstants.INDIVIDUAL_UPDATE_REFERENCE_URI1, TestConstants.INDIVIDUAL_UPDATE_REFERENCE_URI2);
	}

	/** Same as {@link IndividualTest#testApply}. */ 
	private void testApply(String unchangedUri, String changedUri) {
		for (ISymbolicReferenceCreator symrefCreator : TestConstants.SYM_REF_CREATORS) {
			for (IModelDescriptorCreator descriptorCreator : TestConstants.MODEL_DESCRIPTOR_CREATORS) {
				CommonTestOperations.applyMPatchAndValidate(unchangedUri, changedUri, symrefCreator, descriptorCreator);
			}
		}
	}
}

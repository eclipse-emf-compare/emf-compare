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
package org.eclipse.emf.compare.mpatch.test.junit;

import static org.junit.Assert.assertNotNull;

import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;
import org.eclipse.emf.compare.mpatch.test.util.CommonTestOperations;
import org.eclipse.emf.compare.mpatch.test.util.CompareTestHelper;
import org.eclipse.emf.compare.mpatch.test.util.TestConstants;
import org.junit.Test;


/**
 * Check whether all symbolic references contain valid (parseable) OCL expressions.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class OclExpressionTest {

	@Test
	public void testOCLSimple() {
		testOCL(TestConstants.SIMPLE_URI1, TestConstants.SIMPLE_URI2);
	}

	@Test
	public void testOCLDependency() {
		testOCL(TestConstants.DEPENDENCY_URI1, TestConstants.DEPENDENCY_URI2);
	}

	@Test
	public void testOCLEachonce() {
		testOCL(TestConstants.EACHONCE_URI1, TestConstants.EACHONCE_URI2);
	}

	@Test
	public void testOCLEcore() {
		testOCL(TestConstants.ECORE_URI1, TestConstants.ECORE_URI2);
	}

	@Test
	public void testOCLUml() {
		testOCL(TestConstants.UML_URI1, TestConstants.UML_URI2);
	}

	private void testOCL(String unchangedUri, String changedUri) {
		for (ISymbolicReferenceCreator symrefCreator : TestConstants.SYM_REF_CREATORS) {
			for (IModelDescriptorCreator descriptorCreator : TestConstants.MODEL_DESCRIPTOR_CREATORS) {
				MPatchModel mpatch = CompareTestHelper.getMPatchFromUris(changedUri, unchangedUri, symrefCreator,
						descriptorCreator);
				assertNotNull("Preceeding transformation emfdiff2mpatch failed with symrefCreator: "
						+ symrefCreator.getLabel() + " and descriptorCreator: " + descriptorCreator.getLabel(), mpatch);
				CommonTestOperations.checkOclExpressions(mpatch);
			}
		}
	}
}

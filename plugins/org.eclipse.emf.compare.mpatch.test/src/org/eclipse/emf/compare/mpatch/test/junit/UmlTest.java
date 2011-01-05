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

import org.eclipse.emf.compare.mpatch.common.util.ExtensionManager;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;
import org.eclipse.emf.compare.mpatch.test.util.CommonTestOperations;
import org.eclipse.emf.compare.mpatch.test.util.TestConstants;
import org.junit.Test;


/**
 * Test of difference creation and application for UML models.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 *
 */
public class UmlTest {

	/**
	 * Testing the creation and application of mpatches on uml models.
	 */
	@Test
	public void testApplyDifferencesUml() {
		testApply(TestConstants.UML_URI1, TestConstants.UML_URI2);
	}

	/**
	 * Test of adding a UML association explicitly with ID-based symrefs.
	 * 
	 * FIXME: The problem is that the an element which was created by a model descriptor gets a newly created id and
	 * thus cannot be resolved by any existing symbolic reference. The solution is to keep track of all created elements
	 * for each model-descriptor and its symbolic reference, respectively, and use this information to resolve symrefs
	 * which point to such newly created elements.
	 * 
	 * Distinguish between cross-references within one model descriptor only (1) and those which point to another change (2).
	 * (1) is located in EMFModelDescriptorImpl, (2) in the implementation of DiffApplier.
	 */
	@Test
	public void testTestUmlAssocID() {
		CommonTestOperations.applyMPatchAndValidate(TestConstants.UML_ASSOC_URI1, TestConstants.UML_ASSOC_URI2,
				ExtensionManager.getAllSymbolicReferenceCreators().get("ID-based"),
				TestConstants.MODEL_DESCRIPTOR_CREATORS.iterator().next());
	}

	/**
	 * Test of adding a UML association explicitly with condition-based symrefs.
	 */
	@Test
	public void testTestUmlAssocCondition() {
		CommonTestOperations.applyMPatchAndValidate(TestConstants.UML_ASSOC_URI1, TestConstants.UML_ASSOC_URI2,
				ExtensionManager.getAllSymbolicReferenceCreators().get("Condition-based"),
				TestConstants.MODEL_DESCRIPTOR_CREATORS.iterator().next());
	}

	private void testApply(String unchangedUri, String changedUri) {
		for (ISymbolicReferenceCreator symrefCreator : TestConstants.SYM_REF_CREATORS) {
			for (IModelDescriptorCreator descriptorCreator : TestConstants.MODEL_DESCRIPTOR_CREATORS) {
				CommonTestOperations.applyMPatchAndValidate(unchangedUri, changedUri, symrefCreator, descriptorCreator);
			}
		}
	}

}

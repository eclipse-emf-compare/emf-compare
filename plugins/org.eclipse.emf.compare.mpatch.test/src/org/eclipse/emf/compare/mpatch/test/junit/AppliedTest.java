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

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.mpatch.IndepAddAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepAddElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.IndepMoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences.ValidationResult;
import org.eclipse.emf.compare.mpatch.test.util.CommonTestOperations;
import org.eclipse.emf.compare.mpatch.test.util.CompareTestHelper;
import org.eclipse.emf.compare.mpatch.test.util.TestConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Test;

/**
 * This test first calculates the differences between two models and the resolves them to the target (!) model. This
 * must yield all changes already being applied.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class AppliedTest {

	/**
	 * Testing resolution of changes to applied version of dependency cycle test model.
	 */
	@Test
	public void testDependencyCycle() {
		testApplied(TestConstants.DEP_CYCLE_URI1, TestConstants.DEP_CYCLE_URI2);
	}

	/**
	 * Testing resolution of changes to applied version of simpel test model.
	 */
	@Test
	public void testApplyDifferencesSimple() {
		testApplied(TestConstants.SIMPLE_URI1, TestConstants.SIMPLE_URI2);
	}

	/**
	 * Testing resolution of changes to applied version of dependency test model.
	 */
	@Test
	public void testApplyDifferencesDependency() {
		testApplied(TestConstants.DEPENDENCY_URI1, TestConstants.DEPENDENCY_URI2);
	}

	/**
	 * Testing resolution of changes to applied version of eachonce test model.
	 */
	@Test
	public void testApplyDifferencesEachonce() {
		testApplied(TestConstants.EACHONCE_URI1, TestConstants.EACHONCE_URI2);
	}

	/**
	 * Testing resolution of changes to applied version of uml test model.
	 */
	@Test
	public void testApplyDifferencesUML() {
		testApplied(TestConstants.UML_URI1, TestConstants.UML_URI2);
	}

	/**
	 * Test resolution of applied {@link IndepAddElementChange}.
	 */
	@Test
	public void testAppliedAddElement() {
		testApplied(TestConstants.INDIVIDUAL_ADD_REM_ELEMENT_URI1, TestConstants.INDIVIDUAL_ADD_REM_ELEMENT_URI2);
	}

	/**
	 * Test resolution of applied {@link IndepRemoveElementChange}.
	 */
	@Test
	public void testAppliedRemoveElement() {
		testApplied(TestConstants.INDIVIDUAL_ADD_REM_ELEMENT_URI2, TestConstants.INDIVIDUAL_ADD_REM_ELEMENT_URI1);
	}

	/**
	 * Test resolution of applied {@link IndepMoveElementChange}.
	 */
	@Test
	public void testAppliedMoveElement() {
		testApplied(TestConstants.INDIVIDUAL_MOVE_ELEMENT_URI1, TestConstants.INDIVIDUAL_MOVE_ELEMENT_URI2);
	}

	/**
	 * Test resolution of applied {@link IndepAddAttributeChange}.
	 */
	@Test
	public void testAppliedAddAttribute() {
		testApplied(TestConstants.INDIVIDUAL_ADD_REM_ATTRIBUTE_URI1, TestConstants.INDIVIDUAL_ADD_REM_ATTRIBUTE_URI2);
	}

	/**
	 * Test resolution of applied {@link IndepRemoveAttributeChange}.
	 */
	@Test
	public void testAppliedRemoveAttribute() {
		testApplied(TestConstants.INDIVIDUAL_ADD_REM_ATTRIBUTE_URI2, TestConstants.INDIVIDUAL_ADD_REM_ATTRIBUTE_URI1);
	}

	/**
	 * Test resolution of applied {@link IndepUpdateAttributeChange}.
	 */
	@Test
	public void testAppliedUpdateAttribute() {
		testApplied(TestConstants.INDIVIDUAL_UPDATE_ATTRIBUTE_URI1, TestConstants.INDIVIDUAL_UPDATE_ATTRIBUTE_URI2);
	}

	/**
	 * Test resolution of applied {@link IndepAddReferenceChange}.
	 */
	@Test
	public void testAppliedAddReference() {
		testApplied(TestConstants.INDIVIDUAL_ADD_REM_REFERENCE_URI1, TestConstants.INDIVIDUAL_ADD_REM_REFERENCE_URI2);
	}

	/**
	 * Test resolution of applied {@link IndepRemoveReferenceChange}.
	 */
	@Test
	public void testAppliedRemoveReference() {
		testApplied(TestConstants.INDIVIDUAL_ADD_REM_REFERENCE_URI2, TestConstants.INDIVIDUAL_ADD_REM_REFERENCE_URI1);
	}

	/**
	 * Test resolution of applied {@link IndepUpdateReferenceChange}.
	 */
	@Test
	public void testAppliedUpdateReference() {
		testApplied(TestConstants.INDIVIDUAL_UPDATE_REFERENCE_URI1, TestConstants.INDIVIDUAL_UPDATE_REFERENCE_URI2);
	}

	/**
	 * Here we are only using id-based symrefs for testing whether applied changed can be recognized. The reason is that
	 * condition-based references depend on the context or on attribute values which might have been changed.
	 */
	private void testApplied(String unchangedUri, String changedUri) {
		for (ISymbolicReferenceCreator symrefCreator : TestConstants.SYM_REF_CREATORS) {
			if (!"ID-based".equals(symrefCreator.getLabel()))
				continue;
			for (IModelDescriptorCreator descriptorCreator : TestConstants.MODEL_DESCRIPTOR_CREATORS) {

				final String info = "symrefCreator: " + symrefCreator.getLabel() + ", descriptorCreator: "
						+ descriptorCreator.getLabel();

				// create MPatch
				final MPatchModel mpatch = CommonTestOperations.createMPatch(unchangedUri, changedUri, symrefCreator,
						descriptorCreator, info);
				CommonTestOperations.doTransformations(mpatch, null, info);

				// apply it to the changed version of the model ;-)
				final ResourceSet resourceSet = new ResourceSetImpl(); // new resource to avoid conflicts
				final EObject changedModel = CompareTestHelper.loadModel(changedUri, resourceSet).get(0);
				final ResolvedSymbolicReferences resolved = CommonTestOperations.resolveAndValidate(mpatch,
						changedModel, null, info);

				// now the test: all validations must be STATE_AFTER :-)
				final List<IndepChange> notAfter = new ArrayList<IndepChange>();
				for (IndepChange change : resolved.getValidation().keySet()) {
					if (!ValidationResult.STATE_AFTER.equals(resolved.getValidation().get(change)))
						notAfter.add(change);
				}
				assertTrue("Should be applied but are not: " + notAfter, notAfter.isEmpty());
			}
		}
	}

}

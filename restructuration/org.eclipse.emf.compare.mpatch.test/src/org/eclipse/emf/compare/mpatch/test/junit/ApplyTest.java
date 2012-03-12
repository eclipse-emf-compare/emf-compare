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

import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;
import org.eclipse.emf.compare.mpatch.test.util.CommonTestOperations;
import org.eclipse.emf.compare.mpatch.test.util.TestConstants;
import org.junit.Test;


/**
 * Test the entire difference creation and application scenario for several different models and test cases. The testing
 * process is described here: {@link CommonTestOperations#applyDiffAndValidate(String, String)}. Please see the details
 * of each test case in the javadoc of the test methods.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class ApplyTest {

	/**
	 * Testing dependency the creation and application of changes with dependency cycles.
	 */
	@Test
	public void testDependencyCycle() {
		testApply(TestConstants.DEP_CYCLE_URI1, TestConstants.DEP_CYCLE_URI2);
	}

	/**
	 * The most simple test case of just one changed attribute.
	 */
	@Test
	public void testApplyDifferencesSimple() {
		testApply(TestConstants.SIMPLE_URI1, TestConstants.SIMPLE_URI2);
	}

	/**
	 * Testing the creation and application of different depending changes.
	 */
	@Test
	public void testApplyDifferencesDependency() {
		testApply(TestConstants.DEPENDENCY_URI1, TestConstants.DEPENDENCY_URI2);
	}

	/**
	 * Testing each type of change at least once.
	 * 
	 * FIXME: A known bug is that for the eachonce metamodel the deletion of attributes is not recognized. See
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=280449 for details.
	 */
	@Test
	public void testApplyDifferencesEachonce() {
		testApply(TestConstants.EACHONCE_URI1, TestConstants.EACHONCE_URI2);
	}

	/**
	 * Testing the creation and application of mpatches on ecore models.
	 * 
	 * FIXME: Still issues with datatypes and references to model elements from other resources.
	 */
	@Test
	public void testApplyDifferencesEcore() {
		testApply(TestConstants.ECORE_URI1, TestConstants.ECORE_URI2);
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

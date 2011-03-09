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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.common.util.CommonUtils;
import org.eclipse.emf.compare.mpatch.common.util.ExtensionManager;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;
import org.eclipse.emf.compare.mpatch.test.util.CommonTestOperations;
import org.eclipse.emf.compare.mpatch.test.util.CompareTestHelper;
import org.eclipse.emf.compare.mpatch.test.util.TestConstants;
import org.eclipse.emf.compare.mpatch.transform.util.ReverseTransformation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

/**
 * This test checks whether reversed MPatches are correct.
 * 
 * It first checks that an MPatch P equals reversed(reversed(P)).
 * 
 * Second, an MPatch P created from M1 (unchanged) and M2 (changed) is checked as follows: M1 equals reversed(P) applied
 * to M2.
 * 
 * We cannot test any model whose attributes are used as unique identifiers! This is why some test models are not used
 * here. The reason is that attribute changes will change the symbolic reference if that attribute is used in symbolic
 * references.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class ReverseTest {

	private final static String ID_BASED_SYMREFS = "ID-based";
	private final static String CONDITION_BASED_SYMREFS = "Condition-based";

	/**
	 * Reversed test with dependency cycle model.
	 */
	@Test
	public void testReverseDependencyCycle() {
		testReversed(TestConstants.DEP_CYCLE_URI1, TestConstants.DEP_CYCLE_URI2, ID_BASED_SYMREFS);
		testReversed(TestConstants.DEP_CYCLE_URI1, TestConstants.DEP_CYCLE_URI2, CONDITION_BASED_SYMREFS);
	}

	/**
	 * Reversed test with dependency model.
	 */
	@Test
	public void testReverseDifferencesDependency() {
		testReversed(TestConstants.DEPENDENCY_URI1, TestConstants.DEPENDENCY_URI2, ID_BASED_SYMREFS);
		testReversed(TestConstants.DEPENDENCY_URI1, TestConstants.DEPENDENCY_URI2, CONDITION_BASED_SYMREFS);
	}

	/**
	 * Reversed test with eachonce model.
	 */
	@Test
	public void testReverseDifferencesEachonce() {
		testReversed(TestConstants.EACHONCE_URI1, TestConstants.EACHONCE_URI2, ID_BASED_SYMREFS);
		testReversed(TestConstants.EACHONCE_URI1, TestConstants.EACHONCE_URI2, CONDITION_BASED_SYMREFS);
	}

	/**
	 * Reversed test with ecore model.
	 */
	@Test
	public void testReverseDifferencesEcore() {
		testReversed(TestConstants.ECORE_URI1, TestConstants.ECORE_URI2, ID_BASED_SYMREFS);
		testReversed(TestConstants.ECORE_URI1, TestConstants.ECORE_URI2, CONDITION_BASED_SYMREFS);
	}

	/**
	 * Reversed test with UML model.
	 */
	@Test
	public void testReverseDifferencesUML() {
		testReversed(TestConstants.UML_URI1, TestConstants.UML_URI2, ID_BASED_SYMREFS);
		// condition-based does not work here, unfortunately, because one of the changes (cardinality from 0 to 1)
		// resolves ambiguously in the reverted changes.
	}

	/**
	 * Reverse test with simple model.
	 */
	@Test
	public void testReverseSimple() {
		testReversed(TestConstants.SIMPLE_URI1, TestConstants.SIMPLE_URI2, ID_BASED_SYMREFS);
		testReversed(TestConstants.SIMPLE_URI1, TestConstants.SIMPLE_URI2, CONDITION_BASED_SYMREFS);
	}

	/**
	 * Reverse test with simple model.
	 */
	@Test
	public void testReverseLibrary() {
		testReversed(TestConstants.LIBRARY_URI1, TestConstants.LIBRARY_URI2, ID_BASED_SYMREFS);
		testReversed(TestConstants.LIBRARY_URI1, TestConstants.LIBRARY_URI2, CONDITION_BASED_SYMREFS);
	}

	private void testReversed(String unchangedUri, String changedUri, String symrefs) {
		final ISymbolicReferenceCreator symrefCreator = ExtensionManager.getAllSymbolicReferenceCreators().get(symrefs);
		assertNotNull("Please make sure this symref creator is installed correctly: " + symrefs, symrefCreator);

		// but we can test all model descriptors!
		for (IModelDescriptorCreator descriptorCreator : TestConstants.MODEL_DESCRIPTOR_CREATORS) {

			final String info = "symrefCreator: " + symrefCreator.getLabel() + ", descriptorCreator: "
					+ descriptorCreator.getLabel();

			// prepare models
			final MPatchModel mpatch = CompareTestHelper.getMPatchFromUris(changedUri, unchangedUri, symrefCreator,
					descriptorCreator);
			assertNotNull("Preceeding transformation failed! Make sure mpatch can be produced for: " + unchangedUri
					+ " and " + changedUri + " and " + info, mpatch);

			// now reverse it!
			reverseAndTest(mpatch, info + ", model: " + unchangedUri);

			ResourceSet resourceSet = new ResourceSetImpl(); // get new resource set to avoid conflicts
			final EObject rightModel = CompareTestHelper.loadModel(unchangedUri, resourceSet).get(0);
			final EObject leftModel = CompareTestHelper.loadModel(changedUri, resourceSet).get(0);

			// apply the differences and validate the result against the rightModel (!)
			CommonTestOperations.createAndApplyMPatch(mpatch, leftModel, rightModel, null, info + ", model: "
					+ unchangedUri);
		}
	}

	private void reverseAndTest(MPatchModel reversed, String info) {
		final MPatchModel original = EcoreUtil.copy(reversed);
		ReverseTransformation.reverse(reversed);
		final MPatchModel doubleReversed = EcoreUtil.copy(reversed);
		ReverseTransformation.reverse(doubleReversed);
		final ComparisonResourceSnapshot mpatchDiff = CommonUtils.createEmfdiff(original, doubleReversed);
		final Collection<DiffElement> differences = CommonUtils.analyzeDiff(mpatchDiff, CommonUtils.DIFF_ORDERINGS);
		assertTrue("Double reversed mpatch does not equal original mpatch (" + info + "): " + differences,
				differences.isEmpty());
	}

}

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

import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.util.TransformationLauncher;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;
import org.eclipse.emf.compare.mpatch.test.util.CommonTestOperations;
import org.eclipse.emf.compare.mpatch.test.util.CompareTestHelper;
import org.eclipse.emf.compare.mpatch.test.util.TestConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Test;


/**
 * Low-level tests for the transformation from emfdiffs to mpatches for several test models.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class Emfdiff2MPatchTest {

	@Test
	public void testTransformSimple() {
		/*
		 * Just one single difference - makes it easy to test ;-)
		 */
		testTransformation(TestConstants.SIMPLE_URI1, TestConstants.SIMPLE_URI2, 1);
	}

	@Test
	public void testTransformDependency() {
		/*
		 * 4 differences build a group for additions, 5 differences for removals.
		 */
		testTransformation(TestConstants.DEPENDENCY_URI1, TestConstants.DEPENDENCY_URI2, 9);
	}

	@Test
	public void testTransformEachonce() {
		/*
		 * FIXME: EMF Compare still doesn't recognize added (or removed?!) attributes! So I also added one attribute 'd'
		 * to RemoveAttribute.multiAttribute to compensate the missing change.
		 * 
		 * At least it's not my fault ;-) But unfortunately we cannot test attribute additions here!
		 */
		testTransformation(TestConstants.EACHONCE_URI1, TestConstants.EACHONCE_URI2, 14);
	}

	@Test
	public void testTransformConflict() {
		final List<EObject> inModels = CompareTestHelper.loadModel(TestConstants.CONFLICT_EMFDIFF_URI,
				new ResourceSetImpl());
		final ComparisonSnapshot emfdiff = (ComparisonSnapshot) inModels.get(0);
		try {
			// here it doesn't matter which symrefCreator and descriptorCreator we take!
			TransformationLauncher.transform(emfdiff, null,
					TestConstants.SYM_REF_CREATORS.iterator().next(), TestConstants.MODEL_DESCRIPTOR_CREATORS
							.iterator().next());

			/*
			 * The test should not reach this point!
			 */
			fail("Transformation expected to throw an exception!");
		} catch (final Exception e) {
			// successful test run
		}
	}

	private void testTransformation(String unchangedUri, String changedUri, int numberOfChanges) {
		final ComparisonSnapshot emfdiff = CompareTestHelper.getEmfdiffFromEmfCompare(changedUri, unchangedUri);
		for (ISymbolicReferenceCreator symrefCreator : TestConstants.SYM_REF_CREATORS) {
			for (IModelDescriptorCreator descriptorCreator : TestConstants.MODEL_DESCRIPTOR_CREATORS) {
				try {
					final MPatchModel mpatch = TransformationLauncher.transform(emfdiff, null, symrefCreator,
							descriptorCreator);

					CommonTestOperations.checkForDifferences(mpatch, numberOfChanges);
				} catch (final Exception e) {
					fail("Transformation unexpectedly threw an exception with symrefDescriptor: " + symrefCreator
							+ " and descriptorCreator: " + descriptorCreator + ": " + e.getMessage());
				}
			}
		}
	}
}

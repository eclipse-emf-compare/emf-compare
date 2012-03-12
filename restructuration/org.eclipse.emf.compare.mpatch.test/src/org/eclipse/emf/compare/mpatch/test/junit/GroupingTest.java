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

import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;
import org.eclipse.emf.compare.mpatch.test.util.CommonTestOperations;
import org.eclipse.emf.compare.mpatch.test.util.CompareTestHelper;
import org.eclipse.emf.compare.mpatch.test.util.TestConstants;
import org.junit.Test;


/**
 * Check the creation of groups for several test models.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 *
 */
public class GroupingTest {

	@Test
	public void testGroupsSimple() {
		/*
		 * just one change leads to one group
		 */
		testGroups(TestConstants.SIMPLE_URI1, TestConstants.SIMPLE_URI2, 1);
	}

	@Test
	public void testGroupsDependencies() {
		/*
		 * an added element with some changes and a removed element with some changes leads to two groups
		 */
		testGroups(TestConstants.DEPENDENCY_URI1, TestConstants.DEPENDENCY_URI2, 2);
	}

	@Test
	public void testGroupsEachonce() {
		/*
		 * Uhm... difficult to foresee. I think 9. Each type one except for the 'add reference changes' because they add
		 * a reference to a newly added element and thus are in their group; and the three update references, because
		 * they all operate on the same element.
		 */
		testGroups(TestConstants.EACHONCE_URI1, TestConstants.EACHONCE_URI2, 9);
	}
	
	private void testGroups(String unchangedUri, String changedUri, int expectedNumberOfGroups) {
		for (ISymbolicReferenceCreator symrefCreator : TestConstants.SYM_REF_CREATORS) {
			for (IModelDescriptorCreator descriptorCreator : TestConstants.MODEL_DESCRIPTOR_CREATORS) {
				final MPatchModel mpatch = CompareTestHelper.getMPatchFromUris(
						changedUri, unchangedUri, symrefCreator, descriptorCreator);
				assertNotNull("Preceeding transformation emfdiff2mpatch failed with symrefCreator: "
						+ symrefCreator.getLabel() + " and descriptorCreator: " + descriptorCreator.getLabel(), mpatch);

				CommonTestOperations.groupingAndCheckForGroups(mpatch, expectedNumberOfGroups);
			}
		}
	}
}

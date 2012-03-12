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
 * Low-level test for checking whether the symbolic references resolve correctly for different test models.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class ResolveTest {

	@Test
	public void testResolveSymbolicReferencesSimple() {
		testResolveSymbolicReferences(TestConstants.SIMPLE_URI1, TestConstants.SIMPLE_URI2);
	}

	@Test
	public void testResolveSymbolicReferencesDependency() {
		testResolveSymbolicReferences(TestConstants.DEPENDENCY_URI1, TestConstants.DEPENDENCY_URI2);
	}

	@Test
	public void testResolveSymbolicReferencesEachonce() {
		testResolveSymbolicReferences(TestConstants.EACHONCE_URI1, TestConstants.EACHONCE_URI2);
	}

	private void testResolveSymbolicReferences(String unchangedUri, String changedUri) {
		for (ISymbolicReferenceCreator symrefCreator : TestConstants.SYM_REF_CREATORS) {
			for (IModelDescriptorCreator descriptorCreator : TestConstants.MODEL_DESCRIPTOR_CREATORS) {
				CommonTestOperations.checkSymbolicReferenceResolution(unchangedUri, changedUri, symrefCreator,
						descriptorCreator);
			}
		}
	}
}

/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import org.eclipse.emf.compare.tests.merge.data.EcoreMergeInputData;

/**
 * A Simple test case applying and undoing a real history of an existing Ecore file.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class SimpleEcoreHistoryMerge extends MergeTestBase {

	EcoreMergeInputData input = new EcoreMergeInputData();

	// AB
	public void testAtoB() throws Exception {
		mergeAllDiffsLeftToRight(input.getA(), input.getB());
	}

	public void testBtoA() throws Exception {
		mergeAllDiffsRightToLeft(input.getA(), input.getB());
	}

	// BC
	public void testBtoC() throws Exception {
		mergeAllDiffsLeftToRight(input.getB(), input.getC());
	}

	public void testCtoB() throws Exception {
		mergeAllDiffsRightToLeft(input.getB(), input.getC());
	}

	// CD

	public void testCtoD() throws Exception {
		mergeAllDiffsLeftToRight(input.getC(), input.getD());
	}

	public void testDtoC() throws Exception {
		mergeAllDiffsRightToLeft(input.getC(), input.getD());
	}

	// DE

	public void testDtoE() throws Exception {
		mergeAllDiffsLeftToRight(input.getD(), input.getE());
	}

	public void testEtoD() throws Exception {
		mergeAllDiffsRightToLeft(input.getD(), input.getE());
	}

	// EF

	public void testEtoF() throws Exception {
		mergeAllDiffsLeftToRight(input.getE(), input.getF());
	}

	public void testFtoE() throws Exception {
		mergeAllDiffsRightToLeft(input.getE(), input.getF());
	}

	// FG

	public void testFtoG() throws Exception {
		mergeAllDiffsLeftToRight(input.getF(), input.getG());
	}

	public void testGtoF() throws Exception {
		mergeAllDiffsRightToLeft(input.getF(), input.getG());
	}

	// GH

	public void testGtoH() throws Exception {
		mergeAllDiffsLeftToRight(input.getG(), input.getH());
	}

	public void testHtoG() throws Exception {
		mergeAllDiffsRightToLeft(input.getG(), input.getH());
	}

	// AC
	public void testAtoC() throws Exception {
		mergeAllDiffsLeftToRight(input.getA(), input.getC());
	}

	public void testCtoA() throws Exception {
		mergeAllDiffsRightToLeft(input.getA(), input.getC());
	}

	// AD
	public void testAtoD() throws Exception {
		mergeAllDiffsLeftToRight(input.getA(), input.getD());
	}

	public void testDtoA() throws Exception {
		mergeAllDiffsRightToLeft(input.getA(), input.getD());
	}

}

/**
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.merge.complexunmatch;

import org.eclipse.emf.compare.tests.merge.MergeTestBase;
import org.eclipse.emf.compare.tests.merge.complexunmatch.data.MergeMMComplexUnmatchedInputData;

public class ComplexUnmatchedMergeUseCases extends MergeTestBase {
	MergeMMComplexUnmatchedInputData input = new MergeMMComplexUnmatchedInputData();

	public void testUseCase176() throws Exception {
		mergeAllDiffsLeftToRight(input.getUseCase176Base(), input.getUseCase176());
	}

	public void testReverseUseCase176() throws Exception {
		mergeAllDiffsRightToLeft(input.getUseCase176Base(), input.getUseCase176());
	}

	public void testUseCase177() throws Exception {
		mergeAllDiffsLeftToRight(input.getUseCase176Base(), input.getUseCase177());
	}

	public void testReverseUseCase177() throws Exception {
		mergeAllDiffsRightToLeft(input.getUseCase176Base(), input.getUseCase177());
	}

	public void testUseCase178() throws Exception {
		mergeAllDiffsLeftToRight(input.getUseCase178Base(), input.getUseCase178());
	}

	public void testReverseUseCase178() throws Exception {
		mergeAllDiffsRightToLeft(input.getUseCase178Base(), input.getUseCase178());
	}

	public void testUseCase179() throws Exception {
		mergeAllDiffsLeftToRight(input.getUseCase179Base(), input.getUseCase179());
	}

	public void testReverseUseCase179() throws Exception {
		mergeAllDiffsRightToLeft(input.getUseCase179Base(), input.getUseCase179());
	}

	public void testUseCase180() throws Exception {
		mergeAllDiffsLeftToRight(input.getUseCase180Base(), input.getUseCase180());
	}

	public void testReverseUseCase180() throws Exception {
		mergeAllDiffsRightToLeft(input.getUseCase180Base(), input.getUseCase180());
	}

	public void testUseCase181() throws Exception {
		mergeAllDiffsLeftToRight(input.getUseCase180Base(), input.getUseCase181());
	}

	public void testReverseUseCase181() throws Exception {
		mergeAllDiffsRightToLeft(input.getUseCase180Base(), input.getUseCase181());
	}

	public void testUseCase182() throws Exception {
		mergeAllDiffsLeftToRight(input.getUseCase180Base(), input.getUseCase182());
	}

	public void testReverseUseCase182() throws Exception {
		mergeAllDiffsRightToLeft(input.getUseCase180Base(), input.getUseCase182());
	}

	public void testUseCase183() throws Exception {
		mergeAllDiffsLeftToRight(input.getUseCase180Base(), input.getUseCase183());
	}

	public void testReverseUseCase183() throws Exception {
		mergeAllDiffsRightToLeft(input.getUseCase180Base(), input.getUseCase183());
	}

	public void testUseCase184() throws Exception {
		mergeAllDiffsLeftToRight(input.getUseCase180Base(), input.getUseCase184());
	}

	public void testReverseUseCase184() throws Exception {
		mergeAllDiffsRightToLeft(input.getUseCase180Base(), input.getUseCase184());
	}

	public void testUseCase185() throws Exception {
		mergeAllDiffsLeftToRight(input.getUseCase180Base(), input.getUseCase185());
	}

	public void testReverseUseCase185() throws Exception {
		mergeAllDiffsRightToLeft(input.getUseCase180Base(), input.getUseCase185());
	}
}

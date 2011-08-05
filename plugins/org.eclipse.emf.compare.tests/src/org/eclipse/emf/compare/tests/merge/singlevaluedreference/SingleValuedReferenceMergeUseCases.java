/**
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.merge.singlevaluedreference;

import org.eclipse.emf.compare.tests.merge.MergeTestBase;
import org.eclipse.emf.compare.tests.merge.singlevaluedreference.data.MergeMMSingleValuedReferenceInputData;

public class SingleValuedReferenceMergeUseCases extends MergeTestBase {
	MergeMMSingleValuedReferenceInputData input = new MergeMMSingleValuedReferenceInputData();

	public void testAddResolvedValue() throws Exception {
		mergeAllDiffsLeftToRight(input.getNoValue(), input.getValue());
	}

	public void testReverseAddResolvedValue() throws Exception {
		mergeAllDiffsRightToLeft(input.getNoValue(), input.getValue());
	}

	public void testRemoveResolvedValue() throws Exception {
		mergeAllDiffsLeftToRight(input.getValue(), input.getNoValue());
	}

	public void testReverseRemoveResolvedValue() throws Exception {
		mergeAllDiffsRightToLeft(input.getValue(), input.getNoValue());
	}

	public void testChangeResolvedValueToResolvedValue() throws Exception {
		mergeAllDiffsLeftToRight(input.getValue(), input.getChangedValue());
	}

	public void testReverseChangeResolvedValueToResolvedValue() throws Exception {
		mergeAllDiffsRightToLeft(input.getValue(), input.getChangedValue());
	}

	public void testChangeResolvedValueToProxy() throws Exception {
		mergeAllDiffsLeftToRight(input.getValue(), input.getChangedValueProxy());
	}

	public void testReverseChangeResolvedValueToProxy() throws Exception {
		mergeAllDiffsRightToLeft(input.getValue(), input.getChangedValueProxy());
	}

	public void testAddProxy() throws Exception {
		mergeAllDiffsLeftToRight(input.getNoValue(), input.getValueProxy());
	}

	public void testReverseAddProxy() throws Exception {
		mergeAllDiffsRightToLeft(input.getNoValue(), input.getValueProxy());
	}

	public void testRemoveProxy() throws Exception {
		mergeAllDiffsLeftToRight(input.getValueProxy(), input.getNoValue());
	}

	public void testReverseRemoveProxy() throws Exception {
		mergeAllDiffsRightToLeft(input.getValueProxy(), input.getNoValue());
	}

	public void testChangeProxyToResolvedValue() throws Exception {
		mergeAllDiffsLeftToRight(input.getValueProxy(), input.getChangedValue());
	}

	public void testReverseChangeProxyToResolvedValue() throws Exception {
		mergeAllDiffsRightToLeft(input.getValueProxy(), input.getChangedValue());
	}

	public void testChangeProxyToProxy() throws Exception {
		mergeAllDiffsLeftToRight(input.getValueProxy(), input.getChangedValueProxy());
	}

	public void testReverseChangeProxyToProxy() throws Exception {
		mergeAllDiffsRightToLeft(input.getValueProxy(), input.getChangedValueProxy());
	}
}

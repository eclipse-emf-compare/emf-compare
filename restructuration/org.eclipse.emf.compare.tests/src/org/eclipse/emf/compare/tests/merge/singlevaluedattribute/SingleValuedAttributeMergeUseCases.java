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
package org.eclipse.emf.compare.tests.merge.singlevaluedattribute;

import org.eclipse.emf.compare.tests.merge.MergeTestBase;
import org.eclipse.emf.compare.tests.merge.singlevaluedattribute.data.MergeMMSingleValuedAttributeInputData;

public class SingleValuedAttributeMergeUseCases extends MergeTestBase {
	MergeMMSingleValuedAttributeInputData input = new MergeMMSingleValuedAttributeInputData();

	public void testAddValue() throws Exception {
		mergeAllDiffsLeftToRight(input.getNoValue(), input.getValue());
	}

	public void testReverseAddValue() throws Exception {
		mergeAllDiffsRightToLeft(input.getNoValue(), input.getValue());
	}

	public void testRemoveValue() throws Exception {
		mergeAllDiffsLeftToRight(input.getValue(), input.getNoValue());
	}

	public void testReverseRemoveValue() throws Exception {
		mergeAllDiffsRightToLeft(input.getValue(), input.getNoValue());
	}

	public void testChangeValue() throws Exception {
		mergeAllDiffsLeftToRight(input.getValue(), input.getChangedValue());
	}

	public void testReverseChangeValue() throws Exception {
		mergeAllDiffsRightToLeft(input.getValue(), input.getChangedValue());
	}
}

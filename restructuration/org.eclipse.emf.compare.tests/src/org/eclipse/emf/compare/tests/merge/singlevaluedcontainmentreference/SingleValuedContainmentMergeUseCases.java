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
package org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference;

import org.eclipse.emf.compare.tests.merge.MergeTestBase;
import org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference.data.MergeMMSingleValuedContainmentReferenceInputData;

public class SingleValuedContainmentMergeUseCases extends MergeTestBase {
	MergeMMSingleValuedContainmentReferenceInputData input = new MergeMMSingleValuedContainmentReferenceInputData();

	public void testAddChild() throws Exception {
		mergeAllDiffsLeftToRight(input.getNoChild(), input.getChild());
	}

	public void testReverseAddChild() throws Exception {
		mergeAllDiffsRightToLeft(input.getNoChild(), input.getChild());
	}

	public void testRemoveChild() throws Exception {
		mergeAllDiffsLeftToRight(input.getChild(), input.getNoChild());
	}

	public void testReverseRemoveChild() throws Exception {
		mergeAllDiffsRightToLeft(input.getChild(), input.getNoChild());
	}

	public void testChangeChild() throws Exception {
		mergeAllDiffsLeftToRight(input.getChild(), input.getChangedChild());
	}

	public void testReverseChangeChild() throws Exception {
		mergeAllDiffsRightToLeft(input.getChild(), input.getChangedChild());
	}
}

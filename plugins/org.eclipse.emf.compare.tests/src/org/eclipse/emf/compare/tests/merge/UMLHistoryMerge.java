/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
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
 * A Simple test case applying and undoing a real history of an existing uml.Ecore file.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class UMLHistoryMerge extends MergeTestBase {

	EcoreMergeInputData input = new EcoreMergeInputData();

	public void test171to172() throws Exception {
		mergeAllDiffsLeftToRight(input.getUML171(), input.getUML172());
	}

	public void test172to171() throws Exception {
		mergeAllDiffsRightToLeft(input.getUML171(), input.getUML172());
	}

	public void test172to173() throws Exception {
		mergeAllDiffsLeftToRight(input.getUML172(), input.getUML173());
	}

	public void test173to172() throws Exception {
		mergeAllDiffsRightToLeft(input.getUML172(), input.getUML173());
	}

}

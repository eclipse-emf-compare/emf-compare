/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.unit.diff;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.tests.merge.data.EcoreMergeInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * A Simple test checking conflict detection (and more especialy false positive) using the following approach
 * : let's take two versions of models, copy the "changed model" as "right" model and then compare using three
 * way comparison. As it's a copy you should never ever ends up with a conflict.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class UMLHistoryDiff extends TestCase {

	EcoreMergeInputData input = new EcoreMergeInputData();

	public void testSameChangesOnLeftAndRightAreNotConflictsAB() throws Exception {
		doTest(input.getA(), input.getB());
	}

	public void testSameChangesOnLeftAndRightAreNotConflictsBC() throws Exception {
		doTest(input.getB(), input.getC());
	}

	public void testSameChangesOnLeftAndRightAreNotConflictsCD() throws Exception {
		doTest(input.getC(), input.getD());
	}

	public void testSameChangesOnLeftAndRightAreNotConflictsDE() throws Exception {
		doTest(input.getD(), input.getE());
	}

	public void testSameChangesOnLeftAndRightAreNotConflictsEF() throws Exception {
		doTest(input.getE(), input.getF());
	}

	public void testSameChangesOnLeftAndRightAreNotConflictsFG() throws Exception {
		doTest(input.getF(), input.getG());
	}

	public void testSameChangesOnLeftAndRightAreNotConflictsGH() throws Exception {
		doTest(input.getG(), input.getH());
	}

	public void testSameChangesOnLeftAndRightAreNotConflictsUML171_UML172() throws Exception {
		doTest(input.getUML171(), input.getUML172());
	}

	public void testSameChangesOnLeftAndRightAreNotConflictsUML172_UML173() throws Exception {
		doTest(input.getUML172(), input.getUML173());
	}

	public void testSameChangesOnLeftAndRightAreNotConflictsUML172_B() throws Exception {
		doTest(input.getUML172(), input.getB());
	}

	protected void doTest(EObject ancestor, EObject left) throws IOException, InterruptedException {
		EObject right = EcoreUtil.copy(left);

		MatchModel match = MatchService.doMatch(left, right, ancestor, Collections.EMPTY_MAP);
		DiffModel diff = DiffService.doDiff(match, true);

		List<DiffElement> deltas = diff.getDifferences();
		for (DiffElement delta : deltas) {
			assertFalse(
					"There should be no conflict as left and right did the same change :" + delta.toString(),
					delta.isConflicting());
		}
	}

}

/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Tanja Mayerhofer - initial API and implementation
 *******************************************************************************/

package org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;

public class ConflictsGroupWithRefinedDiffTestScenario {

	private static final CompareFactory FACTORY = CompareFactory.eINSTANCE;

	final Diff diff1;

	final Diff diff1a;

	final Diff diff1b;

	final Diff diff2;

	final Diff diff2a;

	final Diff diff2b;

	final Comparison comparison;

	public ConflictsGroupWithRefinedDiffTestScenario() {
		// Create diffs left side
		diff1 = FACTORY.createDiff();
		diff1a = FACTORY.createDiff();
		diff1b = FACTORY.createDiff();
		diff1.getRefinedBy().add(diff1a);
		diff1.getRefinedBy().add(diff1b);

		diff1.setSource(DifferenceSource.LEFT);
		diff1a.setSource(DifferenceSource.LEFT);
		diff1b.setSource(DifferenceSource.LEFT);

		// Create diffs right side
		diff2 = FACTORY.createDiff();
		diff2a = FACTORY.createDiff();
		diff2b = FACTORY.createDiff();
		diff2.getRefinedBy().add(diff2a);
		diff2.getRefinedBy().add(diff2b);

		diff2.setSource(DifferenceSource.RIGHT);
		diff2a.setSource(DifferenceSource.RIGHT);
		diff2b.setSource(DifferenceSource.RIGHT);

		// Create comparison
		comparison = FACTORY.createComparison();
		comparison.setThreeWay(true);
		final Match match1 = FACTORY.createMatch();
		match1.getDifferences().add(diff1);
		match1.getDifferences().add(diff1a);
		match1.getDifferences().add(diff1b);
		match1.getDifferences().add(diff2);
		match1.getDifferences().add(diff2a);
		match1.getDifferences().add(diff2b);
		comparison.getMatches().add(match1);
	}

	Conflict addConflict(Diff conflictingDiff1, Diff conflictingDiff2, ConflictKind kind) {
		final Conflict conflict = FACTORY.createConflict();
		conflict.getDifferences().add(conflictingDiff1);
		conflict.getDifferences().add(conflictingDiff2);
		conflict.setKind(kind);
		comparison.getConflicts().add(conflict);
		return conflict;
	}
}

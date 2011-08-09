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
package org.eclipse.emf.compare.tests.merge.multiplecontainmentreference;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.MatchOptions;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;

public class MultipleContainmentMergeUseCasesScoped extends MultipleContainmentMergeUseCases {
	private String initialLeftValue;

	private String initialRightValue;

	@Override
	protected void doPerformTest(boolean isLeftToRight) throws Exception {
		initialLeftValue = ModelUtils.serialize(leftModel);
		initialRightValue = ModelUtils.serialize(rightModel);

		super.doPerformTest(isLeftToRight);
	}

	@Override
	protected List<DiffElement> detectDifferences(EObject left, EObject right) throws InterruptedException {
		Map<String, Object> options = new HashMap<String, Object>(1);
		options.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER, new NoLeafScopeProvider());

		MatchModel match = MatchService.doMatch(left, right, options);
		DiffModel diff = DiffService.doDiff(match);

		EList<DiffElement> differences = diff.getDifferences();
		return differences;
	}

	@Override
	protected void assertDiffCount(List<DiffElement> differences) {
		assertTrue(differences.isEmpty());
	}

	@Override
	protected void assertResult(boolean isLeftToRight, EObject testLeftModel, EObject testRightModel)
			throws IOException {
		// We expect that there will be no difference at all, thus no change merged
		final String expected;
		final String actual;

		if (isLeftToRight) {
			expected = initialLeftValue;
			actual = ModelUtils.serialize(testLeftModel);
		} else {
			expected = initialRightValue;
			actual = ModelUtils.serialize(testRightModel);
		}

		assertEquals(expected, actual);
	}
}

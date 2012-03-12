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

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class ComplexUnmatchedMergeUseCases3WayLocalChangesNoResource extends ComplexUnmatchedMergeUseCases {
	Resource leftRes;

	Resource rightRes;

	Resource ancestorRes;

	@Override
	protected List<DiffElement> detectDifferences(EObject left, EObject right) throws InterruptedException {
		leftRes = left.eResource();
		rightRes = right.eResource();
		EcoreUtil.resolveAll(left);
		EcoreUtil.resolveAll(right);
		left.eResource().getContents().clear();
		right.eResource().getContents().clear();

		Map<String, Object> options = Collections.emptyMap();

		// We need here to detect and merge "local" changes. We'll use the "right" as ancestor
		EObject ancestor = EcoreUtil.copy(right);

		MatchModel match = MatchService.doMatch(left, right, ancestor, options);
		DiffModel diff = DiffService.doDiff(match, true);

		EList<DiffElement> differences = diff.getDifferences();
		return differences;
	}

	@Override
	protected void assertResult(boolean isLeftToRight, EObject testLeftModel, EObject testRightModel)
			throws IOException {
		leftRes.getContents().add(testLeftModel);
		rightRes.getContents().add(testRightModel);
		super.assertResult(isLeftToRight, testLeftModel, testRightModel);
	}
}

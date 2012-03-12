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

public class SimpleEcoreHistoryMergeWithResource extends SimpleEcoreHistoryMerge {

	@Override
	protected List<DiffElement> detectDifferences(EObject left, EObject right) throws InterruptedException {
		/*
		 * we'd like to test the case where we have an eResource, so let's make sure it's the case !
		 */
		assertNotNull(left.eResource());
		assertNotNull(right.eResource());
		Map<String, Object> options = Collections.emptyMap();

		MatchModel match = MatchService.doResourceMatch(left.eResource(), right.eResource(), options);
		DiffModel diff = DiffService.doDiff(match);

		EList<DiffElement> differences = diff.getDifferences();
		return differences;
	}
}

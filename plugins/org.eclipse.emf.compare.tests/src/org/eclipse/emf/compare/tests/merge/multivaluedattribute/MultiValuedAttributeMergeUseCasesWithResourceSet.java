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
package org.eclipse.emf.compare.tests.merge.multivaluedattribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class MultiValuedAttributeMergeUseCasesWithResourceSet extends MultiValuedAttributeMergeUseCases {
	@Override
	protected List<DiffElement> detectDifferences(EObject left, EObject right) throws InterruptedException {
		/*
		 * we'd like to test the case where we have an eResource, so let's make sure it's the case !
		 */
		assertNotNull(left.eResource());
		assertNotNull(right.eResource());

		/*
		 * putting everything in a resourceSet
		 */

		ResourceSet leftResourceSet = new ResourceSetImpl();
		leftResourceSet.getResources().add(left.eResource());
		ResourceSet rightResourceSet = new ResourceSetImpl();
		rightResourceSet.getResources().add(right.eResource());

		Map<String, Object> options = Collections.emptyMap();

		MatchResourceSet match = MatchService.doResourceSetMatch(leftResourceSet, rightResourceSet, options);
		DiffResourceSet diff = DiffService.doDiff(match);

		List<DiffElement> differences = new ArrayList<DiffElement>();
		for (DiffModel dModel : diff.getDiffModels()) {
			differences.addAll(dModel.getDifferences());
		}
		return differences;
	}
}

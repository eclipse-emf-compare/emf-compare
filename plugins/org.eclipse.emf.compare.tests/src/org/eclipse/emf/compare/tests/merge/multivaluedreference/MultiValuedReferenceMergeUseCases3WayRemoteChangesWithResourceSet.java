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
package org.eclipse.emf.compare.tests.merge.multivaluedreference;

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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class MultiValuedReferenceMergeUseCases3WayRemoteChangesWithResourceSet extends MultiValuedReferenceMergeUseCases {
	@SuppressWarnings("nls")
	@Override
	protected List<DiffElement> detectDifferences(EObject left, EObject right) throws InterruptedException {
		assertNotNull(left.eResource());
		assertNotNull(right.eResource());

		ResourceSet leftResourceSet = new ResourceSetImpl();
		leftResourceSet.getResources().add(left.eResource());
		ResourceSet rightResourceSet = new ResourceSetImpl();
		rightResourceSet.getResources().add(right.eResource());

		Map<String, Object> options = Collections.emptyMap();

		// We need here to detect and merge "remote" changes. We'll use the "left" as ancestor
		Resource leftRes = left.eResource();
		Resource ancestorRes = null;
		if (leftRes != null) {
			try {
				ancestorRes = leftRes.getClass().newInstance();
				ancestorRes.setURI(leftRes.getURI());
			} catch (InstantiationException e) {
				fail("Couldn't copy '" + leftRes.getURI() + "' resource");
			} catch (IllegalAccessException e) {
				fail("Couldn't copy '" + leftRes.getURI() + "' resource");
			}
		}
		EObject ancestor = EcoreUtil.copy(left);
		if (ancestorRes != null) {
			ancestorRes.getContents().add(ancestor);
		}
		ResourceSet ancestorResourceSet = new ResourceSetImpl();
		ancestorResourceSet.getResources().add(ancestorRes);

		MatchResourceSet match = MatchService.doResourceSetMatch(leftResourceSet, rightResourceSet,
				ancestorResourceSet, options);
		DiffResourceSet diff = DiffService.doDiff(match, true);

		List<DiffElement> differences = new ArrayList<DiffElement>();
		for (DiffModel dModel : diff.getDiffModels()) {
			differences.addAll(dModel.getDifferences());
		}
		return differences;
	}
}

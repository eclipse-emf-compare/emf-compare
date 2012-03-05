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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class MultiValuedAttributeMergeUseCases3WayLocalChangesWithResourceSet extends MultiValuedAttributeMergeUseCases {
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

		// We need here to detect and merge "local" changes. We'll use the "right" as ancestor
		Resource rightRes = right.eResource();
		Resource ancestorRes = null;
		if (rightRes != null) {
			try {
				ancestorRes = rightRes.getClass().newInstance();
				ancestorRes.setURI(rightRes.getURI());
			} catch (InstantiationException e) {
				fail("Couldn't copy '" + rightRes.getURI() + "' resource");
			} catch (IllegalAccessException e) {
				fail("Couldn't copy '" + rightRes.getURI() + "' resource");
			}
		}
		EObject ancestor = EcoreUtil.copy(right);
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

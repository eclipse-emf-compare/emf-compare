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

@SuppressWarnings("nls")
public class MultiValuedAttributeMergeUseCases3WayRemoteChanges extends MultiValuedAttributeMergeUseCases {
	@Override
	protected List<DiffElement> detectDifferences(EObject left, EObject right) throws InterruptedException {
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

		MatchModel match = MatchService.doMatch(left, right, ancestor, options);
		DiffModel diff = DiffService.doDiff(match, true);

		EList<DiffElement> differences = diff.getDifferences();
		return differences;
	}
}

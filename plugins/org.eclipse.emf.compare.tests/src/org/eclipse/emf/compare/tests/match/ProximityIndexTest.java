/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.match;

import static org.junit.Assert.assertNull;

import java.util.Iterator;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.match.eobject.EObjectIndex.Side;
import org.eclipse.emf.compare.match.eobject.ProximityEObjectMatcher.DistanceFunction;
import org.eclipse.emf.compare.match.eobject.ScopeQuery;
import org.eclipse.emf.compare.match.eobject.internal.ProximityIndex;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

/**
 * A few tests to validate the behavior of the ProximityIndex.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@SuppressWarnings("nls")
public class ProximityIndexTest {

	@Test
	public void neverMatchWhenDistanceIsMax() throws Exception {
		DistanceFunction notAChance = new DistanceFunction() {

			public double distance(Comparison inProgress, EObject a, EObject b) {
				return Double.MAX_VALUE;
			}

			public boolean areIdentic(Comparison inProgress, EObject a, EObject b) {
				return false;
			}
		};
		ScopeQuery alwaysIn = new ScopeQuery() {

			public boolean isInScope(EObject any) {
				return true;
			}
		};
		ProximityIndex index = new ProximityIndex(notAChance, alwaysIn);
		fillIndex(index, Side.LEFT, EcoreUtil.copy(EcorePackage.eINSTANCE));
		fillIndex(index, Side.RIGHT, EcoreUtil.copy(EcorePackage.eINSTANCE));

		Comparison comp = CompareFactory.eINSTANCE.createComparison();
		for (EObject leftElement : index.getValuesStillThere(Side.LEFT)) {
			assertNull("With a distance which always return Double.MAX_VALUE we should never find a closest.",
					index.findClosests(comp, leftElement, Side.LEFT));
		}

	}

	private void fillIndex(ProximityIndex index, Side side, EObject model) {
		Iterator<EObject> it = model.eAllContents();
		while (it.hasNext()) {
			EObject eObj = it.next();
			index.index(eObj, side);
		}
	}
}

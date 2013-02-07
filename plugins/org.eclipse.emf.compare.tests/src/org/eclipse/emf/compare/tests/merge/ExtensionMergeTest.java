/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import static com.google.common.base.Predicates.and;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;

import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.merge.AbstractMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.merge.data.IndividualDiffInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

@SuppressWarnings("nls")
public class ExtensionMergeTest {
	private IndividualDiffInputData input = new IndividualDiffInputData();

	@Test
	public void testInstantiationMerger() throws IOException {
		final Resource left = input.getAttributeMonoChangeLeft();
		final Resource right = input.getAttributeMonoChangeRight();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final String featureName = "singleValuedAttribute";
		final Diff diff = Iterators.find(differences.iterator(), and(fromSide(DifferenceSource.LEFT),
				changedAttribute("root.origin", featureName, "originValue", "leftValue")));

		IMerger merger = getMerger(IMerger.RegistryImpl.createStandaloneInstance(), diff,
				AbstractMerger.class);
		merger.copyLeftToRight(diff, null);

		final EObject originNode = getNodeNamed(right, "origin");
		assertNotNull(originNode);
		final EStructuralFeature feature = originNode.eClass().getEStructuralFeature(featureName);
		assertNotNull(feature);

		assertEquals("leftValue", originNode.eGet(feature));

		comparison = EMFCompare.builder().build().compare(scope);
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}

	public static IMerger getMerger(final IMerger.Registry registry, final Diff diff,
			final Class<? extends IMerger> expectedMerger) {
		IMerger merger = registry.getHighestRankingMerger(diff);
		assertNotNull("No merger has been found for the diff: " + diff, merger);
		assertTrue("The found merger is not the expexted Merger", expectedMerger.isInstance(merger));
		return merger;
	}

	private EObject getNodeNamed(Resource res, String name) {
		final Iterator<EObject> iterator = EcoreUtil.getAllProperContents(res, false);
		while (iterator.hasNext()) {
			final EObject next = iterator.next();
			final EStructuralFeature nameFeature = next.eClass().getEStructuralFeature("name");
			if (nameFeature != null && name.equals(next.eGet(nameFeature))) {
				return next;
			}
		}
		return null;
	}
}

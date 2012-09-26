/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.fragmentation;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.fragmentation.data.FragmentationInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

@SuppressWarnings("nls")
public class FragmentationTest {
	private final FragmentationInputData input = new FragmentationInputData();

	@Test
	public void testDeletedRootResourceSet() throws IOException {
		final Resource left = input.getDeletedRootLeft();
		final Resource origin = input.getDeletedRootOrigin();
		final Resource right = input.getDeletedRootRight();

		final ResourceSet leftSet = left.getResourceSet();
		final ResourceSet originSet = origin.getResourceSet();
		final ResourceSet rightSet = right.getResourceSet();

		assertNotNull(leftSet);
		assertNotNull(originSet);
		assertNotNull(rightSet);

		EcoreUtil.resolveAll(leftSet);
		EcoreUtil.resolveAll(originSet);
		EcoreUtil.resolveAll(rightSet);

		assertSame(Integer.valueOf(1), Integer.valueOf(leftSet.getResources().size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(originSet.getResources().size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(rightSet.getResources().size()));

		final IComparisonScope scope = EMFCompare.createDefaultScope(leftSet, rightSet, originSet);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(1), Integer.valueOf(differences.size()));

		final Diff diff = differences.get(0);
		assertTrue(diff instanceof ResourceAttachmentChange);
		assertEquals(diff.getMatch().getRight(), getNodeNamed(right, "deletedRoot"));
		assertEquals(diff.getMatch().getOrigin(), getNodeNamed(origin, "deletedRoot"));
		assertNull(diff.getMatch().getLeft());
		assertSame(diff.getSource(), DifferenceSource.LEFT);
		assertSame(diff.getKind(), DifferenceKind.DELETE);

		comparison = EMFCompare.newComparator(scope).compare();
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
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

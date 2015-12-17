/*******************************************************************************
 * Copyright (c) 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.conflict.data.bug484557;

import static org.eclipse.emf.compare.DifferenceKind.DELETE;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Iterables;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This class tests that conflicts between ResourceAttachmentChanges of type DELETE and other changes made to
 * the element that was a root of a resource are seen as conflicts when necessary. See bug 484557 for more
 * details.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class Bug484557ConflictTest {

	private Conflict484557InputData input = new Conflict484557InputData();

	@Test
	public void testAttributeConflict() throws IOException {
		final Resource ancestor = input.getAttributeAncestorResource();
		final Resource left = input.getAttributeLeftResource();
		final Resource right = input.getAttributeRightResource();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		assertEquals(2, differences.size());
		assertEquals(1, conflicts.size());
		assertTrue(conflicts.get(0).getDifferences().containsAll(differences));

		Iterable<ResourceAttachmentChange> raChanges = Iterables.filter(differences,
				ResourceAttachmentChange.class);
		assertEquals(1, Iterables.size(raChanges));
		assertEquals(DifferenceSource.LEFT, raChanges.iterator().next().getSource());

		Iterable<AttributeChange> attChanges = Iterables.filter(differences, AttributeChange.class);
		assertEquals(1, Iterables.size(attChanges));
		assertEquals(DifferenceSource.RIGHT, attChanges.iterator().next().getSource());
	}

	@Test
	public void testSingleReferenceConflict() throws IOException {
		final Resource ancestor = input.getSingleRefAncestorResource();
		final Resource left = input.getSingleRefLeftResource();
		final Resource right = input.getSingleRefRightResource();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		assertEquals(3, differences.size());
		assertEquals(1, conflicts.size());
		assertTrue(conflicts.get(0).getDifferences().containsAll(differences));

		Iterable<ResourceAttachmentChange> raChanges = Iterables.filter(differences,
				ResourceAttachmentChange.class);
		assertEquals(1, Iterables.size(raChanges));
		ResourceAttachmentChange rac = raChanges.iterator().next();
		assertEquals(LEFT, rac.getSource());
		assertEquals(DELETE, rac.getKind());

		Iterable<ReferenceChange> refChanges = Iterables.filter(differences, ReferenceChange.class);
		assertEquals(2, Iterables.size(refChanges));
		assertTrue(Iterables.any(refChanges, EMFComparePredicates.fromSide(LEFT)));
		assertTrue(Iterables.any(refChanges, EMFComparePredicates.fromSide(RIGHT)));
	}

	@Test
	public void testMultiReferenceConflict() throws IOException {
		final Resource ancestor = input.getMultiRefAncestorResource();
		final Resource left = input.getMultiRefLeftResource();
		final Resource right = input.getMultiRefRightResource();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		assertEquals(3, differences.size());
		assertEquals(1, conflicts.size());
		assertEquals(2, conflicts.get(0).getDifferences().size());

		Iterable<ResourceAttachmentChange> raChanges = Iterables.filter(differences,
				ResourceAttachmentChange.class);
		assertEquals(1, Iterables.size(raChanges));
		ResourceAttachmentChange rac = raChanges.iterator().next();
		assertEquals(LEFT, rac.getSource());
		assertEquals(DELETE, rac.getKind());

		Iterable<ReferenceChange> refChanges = Iterables.filter(differences, ReferenceChange.class);
		assertEquals(2, Iterables.size(refChanges));
		assertTrue(Iterables.any(refChanges, fromSide(LEFT)));
		assertTrue(Iterables.any(refChanges, fromSide(RIGHT)));

		// The left refChange should not be conflicting
		ReferenceChange leftRefChange = Iterables.filter(refChanges, EMFComparePredicates.fromSide(LEFT))
				.iterator().next();
		assertNull(leftRefChange.getConflict());

		// The right refChange should be conflicting with the ResourceAttachmentChange
		ReferenceChange rightRefChange = Iterables.filter(refChanges, EMFComparePredicates.fromSide(RIGHT))
				.iterator().next();
		assertNotNull(rightRefChange.getConflict());
	}

	@Test
	@Ignore("Need to resolve a NPE")
	public void testFeatureMapConflict() throws IOException {
		final Resource ancestor = input.getFeatureMapAncestorResource();
		final Resource left = input.getFeatureMapLeftResource();
		final Resource right = input.getFeatureMapRightResource();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		assertEquals(7, differences.size());
		assertEquals(1, conflicts.size());
		assertEquals(6, conflicts.get(0).getDifferences().size());
	}

}

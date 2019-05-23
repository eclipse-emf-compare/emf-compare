/*******************************************************************************
 * Copyright (c) 2019 Obeo.
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
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onEObject;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.referenceValueMatch;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.merge.data.IndividualDiffInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

/**
 * This class double-checks that there are no dangling references after a batch merge.
 * <p>
 * A particular case that has been isolated presenting such an occurrence is when the user changes the element
 * contained in a single-valued containment reference while this value is referenced through non-containment
 * features.
 * </p>
 * 
 * @author lgoubet
 */
@SuppressWarnings("nls")
public class DanglingReferenceAfterMergeTest {
	private IndividualDiffInputData input = new IndividualDiffInputData();

	private final BatchMerger batchMerger = new BatchMerger(IMerger.RegistryImpl.createStandaloneInstance());

	@Test
	public void testDanglingReferencePostMergeLtR() throws IOException {
		Resource ancestor = input.getDanglingPostMergeAncestor();
		Resource left = input.getDanglingPostMergeLeft();
		Resource right = input.getDanglingPostMergeRight();

		IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		Diff changedReference = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.RIGHT),
						changedReference("root.container.ClassA", "singleValuedReference",
								"root.referencedContainer.ClassB", "root.referencedContainer.ClassC")));
		Diff addedClassC = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.RIGHT), addedToSingleValuedReference("root.referencedContainer",
						"singleValueContainment", "root.referencedContainer.ClassC")));
		Diff removedClassB = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.RIGHT),
						removedFromSingleValuedReference("root.referencedContainer", "singleValueContainment",
								"root.referencedContainer.ClassB")));

		batchMerger.copyAllLeftToRight(Arrays.asList(addedClassC), new BasicMonitor());
		assertNoDangling(right);
		assertNotNull(getNodeNamed(right, "ClassB"));
		assertEquals(DifferenceState.DISCARDED, changedReference.getState());
		assertEquals(DifferenceState.DISCARDED, addedClassC.getState());
		assertEquals(DifferenceState.DISCARDED, removedClassB.getState());
	}

	@Test
	public void testDanglingReferencePostMergeRtL() throws IOException {
		Resource ancestor = input.getDanglingPostMergeAncestor();
		Resource left = input.getDanglingPostMergeLeft();
		Resource right = input.getDanglingPostMergeRight();

		IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		Diff changedReference = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.RIGHT),
						changedReference("root.container.ClassA", "singleValuedReference",
								"root.referencedContainer.ClassB", "root.referencedContainer.ClassC")));
		Diff addedClassC = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.RIGHT), addedToSingleValuedReference("root.referencedContainer",
						"singleValueContainment", "root.referencedContainer.ClassC")));
		Diff removedClassB = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.RIGHT),
						removedFromSingleValuedReference("root.referencedContainer", "singleValueContainment",
								"root.referencedContainer.ClassB")));

		batchMerger.copyAllRightToLeft(Arrays.asList(addedClassC), new BasicMonitor());
		assertNoDangling(left);
		assertNotNull(getNodeNamed(left, "ClassC"));
		assertEquals(DifferenceState.MERGED, changedReference.getState());
		assertEquals(DifferenceState.MERGED, addedClassC.getState());
		assertEquals(DifferenceState.MERGED, removedClassB.getState());
	}

	private void assertNoDangling(Resource res) {
		TreeIterator<EObject> iterator = res.getAllContents();
		while (iterator.hasNext()) {
			EObject next = iterator.next();
			for (EObject o : next.eCrossReferences()) {
				assertNotNull(o.eResource());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Predicate<? super Diff> addedToSingleValuedReference(String qualifiedName, String referenceName,
			String addedQualifiedName) {
		return and(ofKind(DifferenceKind.ADD), onEObject(qualifiedName),
				referenceValueMatch(referenceName, addedQualifiedName, false));
	}

	@SuppressWarnings("unchecked")
	private Predicate<? super Diff> removedFromSingleValuedReference(String qualifiedName,
			String referenceName, String addedQualifiedName) {
		return and(ofKind(DifferenceKind.DELETE), onEObject(qualifiedName),
				referenceValueMatch(referenceName, addedQualifiedName, false));
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

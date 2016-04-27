/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.req;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.getOnlyElement;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.movedInReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.req.data.ReqInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

@SuppressWarnings("nls")
public class ReqComputingTest {

	enum TestKind {
		ADD, DELETE;
	}

	private ReqInputData input = new ReqInputData();

	@Test
	public void testA1UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testA2UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB2(TestKind.DELETE, comparison);
	}

	@Test
	public void testA3UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB3(TestKind.DELETE, comparison);
	}

	@Test
	public void testA4UseCase() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB4(TestKind.DELETE, comparison);
	}

	@Test
	public void testA5UseCase() throws IOException {
		final Resource left = input.getA5Left();
		final Resource right = input.getA5Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB5(TestKind.DELETE, comparison);
	}

	@Test
	public void testA6UseCase() throws IOException {
		final Resource left = input.getA6Left();
		final Resource right = input.getA6Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB6(TestKind.DELETE, comparison);

	}

	@Test
	public void testA7UseCase() throws IOException {
		final Resource left = input.getA7Left();
		final Resource right = input.getA7Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB7(TestKind.ADD, comparison);
	}

	@Test
	public void testA8UseCase() throws IOException {
		final Resource left = input.getA8Left();
		final Resource right = input.getA8Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB8(TestKind.ADD, comparison);
	}

	@Test
	public void testA9UseCase() throws IOException {
		final Resource left = input.getA9Left();
		final Resource right = input.getA9Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB9(TestKind.ADD, comparison);
	}

	@Test
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA10Left();
		final Resource right = input.getA10Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB10(TestKind.ADD, comparison);
	}

	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA11Left();
		final Resource right = input.getA11Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB11(TestKind.ADD, comparison);
	}

	@Test
	public void testB1UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testB2UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB2(TestKind.ADD, comparison);
	}

	@Test
	public void testB3UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB3(TestKind.ADD, comparison);
	}

	@Test
	public void testB4UseCase() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB4(TestKind.ADD, comparison);
	}

	@Test
	public void testB5UseCase() throws IOException {
		final Resource left = input.getA5Left();
		final Resource right = input.getA5Right();

		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB5(TestKind.ADD, comparison);
	}

	@Test
	public void testB6UseCase() throws IOException {
		final Resource left = input.getA6Left();
		final Resource right = input.getA6Right();

		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB6(TestKind.ADD, comparison);
	}

	@Test
	public void testB7UseCase() throws IOException {
		final Resource left = input.getA7Left();
		final Resource right = input.getA7Right();

		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB7(TestKind.DELETE, comparison);
	}

	@Test
	public void testB8UseCase() throws IOException {
		final Resource left = input.getA8Left();
		final Resource right = input.getA8Right();

		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB8(TestKind.DELETE, comparison);
	}

	@Test
	public void testB9UseCase() throws IOException {
		final Resource left = input.getA9Left();
		final Resource right = input.getA9Right();

		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB9(TestKind.DELETE, comparison);
	}

	@Test
	public void testB10UseCase() throws IOException {
		final Resource left = input.getA10Left();
		final Resource right = input.getA10Right();

		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB10(TestKind.DELETE, comparison);
	}

	@Test
	public void testB11UseCase() throws IOException {
		final Resource left = input.getA11Left();
		final Resource right = input.getA11Right();

		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB11(TestKind.DELETE, comparison);
	}

	@Test
	public void testC1UseCase() throws IOException {
		final Resource left = input.getC1Left();
		final Resource right = input.getC1Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD1(TestKind.DELETE, comparison);
	}

	@Test
	public void testC2UseCase() throws IOException {
		final Resource left = input.getC2Left();
		final Resource right = input.getC2Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD2(TestKind.DELETE, comparison);
	}

	@Test
	public void testC3UseCase() throws IOException {
		final Resource left = input.getC3Left();
		final Resource right = input.getC3Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD3(TestKind.DELETE, comparison);
	}

	@Test
	public void testC4UseCase() throws IOException {
		final Resource left = input.getC4Left();
		final Resource right = input.getC4Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD4(TestKind.DELETE, comparison);
	}

	@Test
	public void testC5UseCase() throws IOException {
		final Resource left = input.getC5Left();
		final Resource right = input.getC5Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD5(TestKind.DELETE, comparison);
	}

	@Test
	public void testD1UseCase() throws IOException {
		final Resource left = input.getC1Left();
		final Resource right = input.getC1Right();

		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD1(TestKind.ADD, comparison);
	}

	@Test
	public void testD2UseCase() throws IOException {
		final Resource left = input.getC2Left();
		final Resource right = input.getC2Right();

		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD2(TestKind.ADD, comparison);
	}

	@Test
	public void testD3UseCase() throws IOException {
		final Resource left = input.getC3Left();
		final Resource right = input.getC3Right();

		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD3(TestKind.ADD, comparison);
	}

	@Test
	public void testD4UseCase() throws IOException {
		final Resource left = input.getC4Left();
		final Resource right = input.getC4Right();

		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD4(TestKind.ADD, comparison);
	}

	@Test
	public void testD5UseCase() throws IOException {
		final Resource left = input.getC5Left();
		final Resource right = input.getC5Right();

		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD5(TestKind.ADD, comparison);
	}

	@Test
	public void testE1UseCase1() throws IOException {
		final Resource left = input.getE1Left();
		final Resource right = input.getE1Right();
		final Resource ancestor = input.getE1Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testE2UseCase() throws IOException {
		final Resource left = input.getE2Left();
		final Resource right = input.getE2Right();
		final Resource ancestor = input.getE2Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB2(TestKind.DELETE, comparison);
	}

	@Test
	public void testE3UseCase() throws IOException {
		final Resource left = input.getE3Left();
		final Resource right = input.getE3Right();
		final Resource ancestor = input.getE3Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB3(TestKind.DELETE, comparison);
	}

	@Test
	public void testE4UseCase() throws IOException {
		final Resource left = input.getE4Left();
		final Resource right = input.getE4Right();
		final Resource ancestor = input.getE4Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB4(TestKind.DELETE, comparison);
	}

	@Test
	public void testE5UseCase() throws IOException {
		final Resource left = input.getE5Left();
		final Resource right = input.getE5Right();
		final Resource ancestor = input.getE5Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB5(TestKind.DELETE, comparison);
	}

	@Test
	public void testE6UseCase() throws IOException {
		final Resource left = input.getE6Left();
		final Resource right = input.getE6Right();
		final Resource ancestor = input.getE6Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB6(TestKind.DELETE, comparison);
	}

	@Test
	public void testE7UseCase() throws IOException {
		final Resource left = input.getE7Left();
		final Resource right = input.getE7Right();
		final Resource ancestor = input.getE7Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD1(TestKind.DELETE, comparison);
	}

	@Test
	public void testE8UseCase() throws IOException {
		final Resource left = input.getE8Left();
		final Resource right = input.getE8Right();
		final Resource ancestor = input.getE8Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD2(TestKind.DELETE, comparison);
	}

	@Test
	public void testE9UseCase() throws IOException {
		final Resource left = input.getE9Left();
		final Resource right = input.getE9Right();
		final Resource ancestor = input.getE9Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD3(TestKind.DELETE, comparison);
	}

	@Test
	public void testE10UseCase() throws IOException {
		final Resource left = input.getE10Left();
		final Resource right = input.getE10Right();
		final Resource ancestor = input.getE10Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD4(TestKind.DELETE, comparison);
	}

	@Test
	public void testE11UseCase() throws IOException {
		final Resource left = input.getE11Left();
		final Resource right = input.getE11Right();
		final Resource ancestor = input.getE11Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD5(TestKind.DELETE, comparison);
	}

	@Test
	public void testF1UseCase1() throws IOException {
		final Resource left = input.getF1Left();
		final Resource right = input.getF1Right();
		final Resource ancestor = input.getF1Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testF2UseCase() throws IOException {
		final Resource left = input.getF2Left();
		final Resource right = input.getF2Right();
		final Resource ancestor = input.getF2Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB2(TestKind.ADD, comparison);
	}

	@Test
	public void testF3UseCase() throws IOException {
		final Resource left = input.getF3Left();
		final Resource right = input.getF3Right();
		final Resource ancestor = input.getF3Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB3(TestKind.ADD, comparison);
	}

	@Test
	public void testF4UseCase() throws IOException {
		final Resource left = input.getF4Left();
		final Resource right = input.getF4Right();
		final Resource ancestor = input.getF4Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB4(TestKind.ADD, comparison);
	}

	@Test
	public void testF5UseCase() throws IOException {
		final Resource left = input.getF5Left();
		final Resource right = input.getF5Right();
		final Resource ancestor = input.getF5Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB5(TestKind.ADD, comparison);
	}

	@Test
	public void testF6UseCase() throws IOException {
		final Resource left = input.getF6Left();
		final Resource right = input.getF6Right();
		final Resource ancestor = input.getF6Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testAB6(TestKind.ADD, comparison);
	}

	@Test
	public void testF7UseCase() throws IOException {
		final Resource left = input.getF7Left();
		final Resource right = input.getF7Right();
		final Resource ancestor = input.getF7Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD1(TestKind.ADD, comparison);
	}

	@Test
	public void testF8UseCase() throws IOException {
		final Resource left = input.getF8Left();
		final Resource right = input.getF8Right();
		final Resource ancestor = input.getF8Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD2(TestKind.ADD, comparison);
	}

	@Test
	public void testF9UseCase() throws IOException {
		final Resource left = input.getF9Left();
		final Resource right = input.getF9Right();
		final Resource ancestor = input.getF9Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD3(TestKind.ADD, comparison);
	}

	@Test
	public void testF10UseCase() throws IOException {
		final Resource left = input.getF10Left();
		final Resource right = input.getF10Right();
		final Resource ancestor = input.getF10Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD4(TestKind.ADD, comparison);
	}

	@Test
	public void testF11UseCase() throws IOException {
		final Resource left = input.getF11Left();
		final Resource right = input.getF11Right();
		final Resource ancestor = input.getF11Ancestor();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		testCD5(TestKind.ADD, comparison);
	}

	@Test
	public void testG1UseCase() throws IOException {
		final Resource left = input.getG1Left();
		final Resource right = input.getG1Right();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		String root = "root";
		String node1 = root + ".node1";
		String node2 = node1 + ".node2";

		EList<Diff> differences = comparison.getDifferences();

		assertEquals(3, differences.size());
		Diff added1 = getOnlyElement(filter(differences, added(node1)), null);
		assertNotNull(added1);
		Diff added2 = getOnlyElement(filter(differences, added(node2)), null);
		assertNotNull(added2);

		ReferenceChange singleChange = null;
		for (ReferenceChange change : filter(differences, ReferenceChange.class)) {
			if ("singleValuedReference".equals(change.getReference().getName())) {
				singleChange = change;
				break;
			}
		}
		assertNotNull(singleChange);
		// Happy compiler
		assert singleChange != null;
		assertTrue(singleChange.getValue().eIsProxy());
		assertEquals(0, added1.getRequires().size());
		assertEquals(1, added2.getRequires().size());
		assertTrue(added2.getRequires().contains(added1));
		assertEquals(1, singleChange.getRequires().size());
		assertTrue(singleChange.getRequires().contains(added2));
	}

	@Test
	public void testH1UseCase() throws IOException {
		final Resource left = input.getH1Left();
		final Resource origin = input.getH1Ancestor();
		final Resource right = input.getH1Right();

		final ResourceSet leftSet = left.getResourceSet();
		final ResourceSet originSet = origin.getResourceSet();
		final ResourceSet rightSet = right.getResourceSet();

		assertNotNull(leftSet);
		assertNotNull(originSet);
		assertNotNull(rightSet);

		EcoreUtil.resolveAll(leftSet);
		EcoreUtil.resolveAll(originSet);
		EcoreUtil.resolveAll(rightSet);

		assertEquals(1, leftSet.getResources().size());
		assertEquals(2, originSet.getResources().size());
		assertEquals(2, rightSet.getResources().size());

		IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = EMFCompare.builder().build().compare(scope);
		testH(TestKind.DELETE, comparison);

		scope = new DefaultComparisonScope(rightSet, leftSet, originSet);
		comparison = EMFCompare.builder().build().compare(scope);
		testH(TestKind.DELETE, comparison);
	}

	@Test
	public void testH2UseCase() throws IOException {
		final Resource left = input.getH2Left();
		final Resource origin = input.getH2Ancestor();
		final Resource right = input.getH2Right();

		final ResourceSet leftSet = left.getResourceSet();
		final ResourceSet originSet = origin.getResourceSet();
		final ResourceSet rightSet = right.getResourceSet();

		assertNotNull(leftSet);
		assertNotNull(originSet);
		assertNotNull(rightSet);

		EcoreUtil.resolveAll(leftSet);
		EcoreUtil.resolveAll(originSet);
		EcoreUtil.resolveAll(rightSet);

		assertEquals(2, leftSet.getResources().size());
		assertEquals(1, originSet.getResources().size());
		assertEquals(1, rightSet.getResources().size());

		IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = EMFCompare.builder().build().compare(scope);
		testH(TestKind.ADD, comparison);

		scope = new DefaultComparisonScope(rightSet, leftSet, originSet);
		comparison = EMFCompare.builder().build().compare(scope);
		testH(TestKind.ADD, comparison);
	}

	private void testH(TestKind testKind, Comparison comparison) {

		EList<Diff> differences = comparison.getDifferences();
		Collection<Diff> racs = Collections2.filter(differences, Predicates
				.instanceOf(ResourceAttachmentChange.class));
		assertEquals(1, racs.size());
		Diff rac = racs.iterator().next();

		Predicate<? super Diff> deleteFragmentedDiffDescription = null;
		Predicate<? super Diff> deleteInnerNodeDiffDescription = null;

		if (testKind == TestKind.DELETE) {
			deleteFragmentedDiffDescription = removed("root.fragmented"); //$NON-NLS-1$			
			deleteInnerNodeDiffDescription = removed("root.fragmented.innerNode"); //$NON-NLS-1$
		} else {
			deleteFragmentedDiffDescription = added("root.fragmented"); //$NON-NLS-1$			
			deleteInnerNodeDiffDescription = added("root.fragmented.innerNode"); //$NON-NLS-1$
		}

		final Diff deleteFragmentedDiff = Iterators.find(differences.iterator(),
				deleteFragmentedDiffDescription);
		final Diff deleteInnerNodeDiff = Iterators.find(differences.iterator(),
				deleteInnerNodeDiffDescription);

		if (testKind == TestKind.DELETE) {
			assertEquals(1, rac.getRequiredBy().size());
			assertEquals(deleteFragmentedDiff, rac.getRequiredBy().get(0));
			assertEquals(0, rac.getRequires().size());

			assertEquals(1, deleteInnerNodeDiff.getRequiredBy().size());
			assertEquals(deleteFragmentedDiff, deleteInnerNodeDiff.getRequiredBy().get(0));
			assertEquals(0, deleteInnerNodeDiff.getRequires().size());
		} else {
			assertEquals(1, rac.getRequires().size());
			assertEquals(deleteFragmentedDiff, rac.getRequires().get(0));
			assertEquals(0, rac.getRequiredBy().size());

			assertEquals(1, deleteInnerNodeDiff.getRequires().size());
			assertEquals(deleteFragmentedDiff, deleteInnerNodeDiff.getRequires().get(0));
			assertEquals(0, deleteInnerNodeDiff.getRequiredBy().size());
		}
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 3 differences
		assertEquals(3, differences.size());

		Predicate<? super Diff> deleteSourceDiffDescription = null;
		Predicate<? super Diff> deleteDestinationDiffDescription = null;
		Predicate<? super Diff> changedSingleValuedRefDiffDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			deleteSourceDiffDescription = removed("Requirements.containerSource.source"); //$NON-NLS-1$

			deleteDestinationDiffDescription = removed("Requirements.containerDestination.destination"); //$NON-NLS-1$

			changedSingleValuedRefDiffDescription = changedReference("Requirements.containerSource.source", //$NON-NLS-1$
					"singleValuedReference", "Requirements.containerDestination.destination", null); //$NON-NLS-1$//$NON-NLS-2$
		} else {
			deleteSourceDiffDescription = added("Requirements.containerSource.source"); //$NON-NLS-1$

			deleteDestinationDiffDescription = added("Requirements.containerDestination.destination"); //$NON-NLS-1$

			changedSingleValuedRefDiffDescription = changedReference("Requirements.containerSource.source", //$NON-NLS-1$
					"singleValuedReference", null, "Requirements.containerDestination.destination"); //$NON-NLS-1$//$NON-NLS-2$
		}

		final Diff deleteSourceDiff = Iterators.find(differences.iterator(), deleteSourceDiffDescription);

		final Diff deleteDestinationDiff = Iterators.find(differences.iterator(),
				deleteDestinationDiffDescription);

		final Diff changedSingleValuedRefDiff = Iterators.find(differences.iterator(),
				changedSingleValuedRefDiffDescription);

		assertNotNull(deleteSourceDiff);
		assertNotNull(deleteDestinationDiff);
		assertNotNull(changedSingleValuedRefDiff);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(0, deleteSourceDiff.getRequires().size());

			assertEquals(0, deleteDestinationDiff.getRequires().size());

			assertEquals(2, changedSingleValuedRefDiff.getRequires().size());
			assertTrue(changedSingleValuedRefDiff.getRequires().contains(deleteDestinationDiff));
			assertTrue(changedSingleValuedRefDiff.getRequires().contains(deleteSourceDiff));
		} else {
			assertEquals(1, deleteSourceDiff.getRequires().size());

			assertEquals(1, deleteDestinationDiff.getRequires().size());

			assertEquals(0, changedSingleValuedRefDiff.getRequires().size());
		}
	}

	private void testAB2(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 3 differences
		assertEquals(3, differences.size());

		Predicate<? super Diff> deleteSourceDiffDescription = null;
		Predicate<? super Diff> deleteDestinationDiffDescription = null;
		Predicate<? super Diff> deleteMultiValuedRefDiffDescription = null;
		if (kind.equals(TestKind.DELETE)) {
			deleteSourceDiffDescription = removed("Requirements.containerSource.source"); //$NON-NLS-1$
			deleteDestinationDiffDescription = removed("Requirements.containerDestination.destination"); //$NON-NLS-1$
			deleteMultiValuedRefDiffDescription = removedFromReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.containerDestination.destination"); //$NON-NLS-1$//$NON-NLS-2$
		} else {
			deleteSourceDiffDescription = added("Requirements.containerSource.source"); //$NON-NLS-1$
			deleteDestinationDiffDescription = added("Requirements.containerDestination.destination"); //$NON-NLS-1$
			deleteMultiValuedRefDiffDescription = addedToReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.containerDestination.destination"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		final Diff deleteSourceDiff = Iterators.find(differences.iterator(), deleteSourceDiffDescription);

		final Diff deleteDestinationDiff = Iterators.find(differences.iterator(),
				deleteDestinationDiffDescription);

		final Diff deleteMultiValuedRefDiff = Iterators.find(differences.iterator(),
				deleteMultiValuedRefDiffDescription);

		assertNotNull(deleteSourceDiff);
		assertNotNull(deleteDestinationDiff);
		assertNotNull(deleteMultiValuedRefDiff);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(0, deleteSourceDiff.getRequires().size());

			assertEquals(0, deleteDestinationDiff.getRequires().size());

			assertEquals(2, deleteMultiValuedRefDiff.getRequires().size());
			assertTrue(deleteMultiValuedRefDiff.getRequires().contains(deleteDestinationDiff));
			assertTrue(deleteMultiValuedRefDiff.getRequires().contains(deleteSourceDiff));
		} else {
			assertEquals(1, deleteSourceDiff.getRequires().size());
			assertTrue(deleteSourceDiff.getRequires().contains(deleteMultiValuedRefDiff));

			assertEquals(1, deleteDestinationDiff.getRequires().size());
			assertTrue(deleteDestinationDiff.getRequires().contains(deleteMultiValuedRefDiff));

			assertEquals(0, deleteMultiValuedRefDiff.getRequires().size());
		}
	}

	private void testAB3(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertEquals(2, differences.size());

		Predicate<? super Diff> deleteDestinationDiffDescription = null;
		Predicate<? super Diff> changedSingleValuedRefDiffDescription = null;
		if (kind.equals(TestKind.DELETE)) {
			deleteDestinationDiffDescription = removed("Requirements.containerDestination.destination2"); //$NON-NLS-1$
			changedSingleValuedRefDiffDescription = changedReference("Requirements.containerSource.source", //$NON-NLS-1$
					"singleValuedReference", "Requirements.containerDestination.destination2", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.destination1"); //$NON-NLS-1$
		} else {
			deleteDestinationDiffDescription = added("Requirements.containerDestination.destination2"); //$NON-NLS-1$
			changedSingleValuedRefDiffDescription = changedReference("Requirements.containerSource.source", //$NON-NLS-1$
					"singleValuedReference", "Requirements.destination1", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.containerDestination.destination2"); //$NON-NLS-1$
		}

		final Diff deleteDestinationDiff = Iterators.find(differences.iterator(),
				deleteDestinationDiffDescription);

		final Diff changedSingleValuedRefDiff = Iterators.find(differences.iterator(),
				changedSingleValuedRefDiffDescription);

		assertNotNull(deleteDestinationDiff);
		assertNotNull(changedSingleValuedRefDiff);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(0, deleteDestinationDiff.getRequires().size());

			assertEquals(1, changedSingleValuedRefDiff.getRequires().size());
			assertTrue(changedSingleValuedRefDiff.getRequires().contains(deleteDestinationDiff));
		} else {
			assertEquals(1, deleteDestinationDiff.getRequires().size());
			assertTrue(deleteDestinationDiff.getRequires().contains(changedSingleValuedRefDiff));

			assertEquals(0, changedSingleValuedRefDiff.getRequires().size());
		}
	}

	private void testAB4(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 3 differences
		assertEquals(3, differences.size());

		Predicate<? super Diff> deleteDestinationDiffDescription = null;
		Predicate<? super Diff> deletedMultiValuedRefDiffDescription = null;
		Predicate<? super Diff> addedMultiValuedRefDiffDescription = null;
		if (kind.equals(TestKind.DELETE)) {
			deleteDestinationDiffDescription = removed("Requirements.containerDestination.destination2"); //$NON-NLS-1$
			deletedMultiValuedRefDiffDescription = removedFromReference(
					"Requirements.containerSource.source", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.containerDestination.destination2"); //$NON-NLS-1$
			addedMultiValuedRefDiffDescription = addedToReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination1"); //$NON-NLS-1$//$NON-NLS-2$
		} else {
			deleteDestinationDiffDescription = added("Requirements.containerDestination.destination2"); //$NON-NLS-1$
			deletedMultiValuedRefDiffDescription = addedToReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.containerDestination.destination2"); //$NON-NLS-1$ //$NON-NLS-2$
			addedMultiValuedRefDiffDescription = removedFromReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination1"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		final Diff deleteDestinationDiff = Iterators.find(differences.iterator(),
				deleteDestinationDiffDescription);

		final Diff deletedMultiValuedRefDiff = Iterators.find(differences.iterator(),
				deletedMultiValuedRefDiffDescription);

		final Diff addedMultiValuedRefDiff = Iterators.find(differences.iterator(),
				addedMultiValuedRefDiffDescription);

		assertNotNull(deleteDestinationDiff);
		assertNotNull(deletedMultiValuedRefDiff);
		assertNotNull(addedMultiValuedRefDiffDescription);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(0, deleteDestinationDiff.getRequires().size());

			assertEquals(1, deletedMultiValuedRefDiff.getRequires().size());

			assertEquals(0, addedMultiValuedRefDiff.getRequires().size());
			assertTrue(deletedMultiValuedRefDiff.getRequires().contains(deleteDestinationDiff));
		} else {
			assertEquals(1, deleteDestinationDiff.getRequires().size());
			assertTrue(deleteDestinationDiff.getRequires().contains(deletedMultiValuedRefDiff));

			assertEquals(0, deletedMultiValuedRefDiff.getRequires().size());

			assertEquals(0, addedMultiValuedRefDiff.getRequires().size());
		}

	}

	private void testAB5(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 8 differences
		assertEquals(8, differences.size());

		Predicate<? super Diff> addDest4Description = null;
		Predicate<? super Diff> delDest2Description = null;
		Predicate<? super Diff> delDest3Description = null;
		Predicate<? super Diff> addRefDest1Description = null;
		Predicate<? super Diff> addRefDest4Description = null;
		Predicate<? super Diff> delRefDest2Description = null;
		Predicate<? super Diff> delRefDest3Description = null;
		Predicate<? super Diff> delRefDest5Description = null;

		if (kind.equals(TestKind.DELETE)) {
			addDest4Description = added("Requirements.destination4"); //$NON-NLS-1$
			delDest2Description = removed("Requirements.containerDestination.destination2"); //$NON-NLS-1$
			delDest3Description = removed("Requirements.containerDestination.destination3"); //$NON-NLS-1$

			addRefDest1Description = addedToReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination1"); //$NON-NLS-1$ //$NON-NLS-2$
			addRefDest4Description = addedToReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination4"); //$NON-NLS-1$//$NON-NLS-2$
			delRefDest2Description = removedFromReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.containerDestination.destination2"); //$NON-NLS-1$ //$NON-NLS-2$
			delRefDest3Description = removedFromReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.containerDestination.destination3"); //$NON-NLS-1$ //$NON-NLS-2$
			delRefDest5Description = removedFromReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination5"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			addDest4Description = removed("Requirements.destination4"); //$NON-NLS-1$
			delDest2Description = added("Requirements.containerDestination.destination2"); //$NON-NLS-1$
			delDest3Description = added("Requirements.containerDestination.destination3"); //$NON-NLS-1$

			addRefDest1Description = removedFromReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination1"); //$NON-NLS-1$//$NON-NLS-2$
			addRefDest4Description = removedFromReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination4"); //$NON-NLS-1$//$NON-NLS-2$
			delRefDest2Description = addedToReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.containerDestination.destination2"); //$NON-NLS-1$//$NON-NLS-2$
			delRefDest3Description = addedToReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.containerDestination.destination3"); //$NON-NLS-1$ //$NON-NLS-2$
			delRefDest5Description = addedToReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination5"); //$NON-NLS-1$//$NON-NLS-2$
		}

		final Diff addDest4 = Iterators.find(differences.iterator(), addDest4Description);
		final Diff delDest2 = Iterators.find(differences.iterator(), delDest2Description);
		final Diff delDest3 = Iterators.find(differences.iterator(), delDest3Description);

		final Diff addRefDest1 = Iterators.find(differences.iterator(), addRefDest1Description);
		final Diff addRefDest4 = Iterators.find(differences.iterator(), addRefDest4Description);
		final Diff delRefDest2 = Iterators.find(differences.iterator(), delRefDest2Description);
		final Diff delRefDest3 = Iterators.find(differences.iterator(), delRefDest3Description);
		final Diff delRefDest5 = Iterators.find(differences.iterator(), delRefDest5Description);

		assertNotNull(addDest4);
		assertNotNull(delDest2);
		assertNotNull(delDest3);
		assertNotNull(addRefDest1);
		assertNotNull(addRefDest4);
		assertNotNull(delRefDest2);
		assertNotNull(delRefDest3);
		assertNotNull(delRefDest5);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(1, addDest4.getRequires().size());
			assertTrue(addDest4.getRequires().contains(addRefDest4));

			assertEquals(0, delDest2.getRequires().size());

			assertEquals(0, delDest3.getRequires().size());

			assertEquals(0, addRefDest1.getRequires().size());

			assertEquals(0, addRefDest4.getRequires().size());

			assertEquals(1, delRefDest2.getRequires().size());
			assertTrue(delRefDest2.getRequires().contains(delDest2));

			assertEquals(1, delRefDest3.getRequires().size());
			assertTrue(delRefDest3.getRequires().contains(delDest3));

			assertEquals(0, delRefDest5.getRequires().size());

		} else {
			assertEquals(0, addDest4.getRequires().size());

			assertEquals(1, delDest2.getRequires().size());
			assertTrue(delDest2.getRequires().contains(delRefDest2));

			assertEquals(1, delDest3.getRequires().size());
			assertTrue(delDest3.getRequires().contains(delRefDest3));

			assertEquals(0, addRefDest1.getRequires().size());

			assertEquals(1, addRefDest4.getRequires().size());
			assertTrue(addRefDest4.getRequires().contains(addDest4));

			assertEquals(0, delRefDest2.getRequires().size());

			assertEquals(0, delRefDest3.getRequires().size());

			assertEquals(0, delRefDest5.getRequires().size());
		}

	}

	private void testAB6(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 7 differences
		assertEquals(7, differences.size());

		Predicate<? super Diff> delDestDescription = null;
		Predicate<? super Diff> delContainerDescription = null;
		Predicate<? super Diff> delSubContainerDescription = null;
		Predicate<? super Diff> delSource1Description = null;
		Predicate<? super Diff> delSource2Description = null;
		Predicate<? super Diff> delRefSource1Description = null;
		Predicate<? super Diff> delRefSource2Description = null;

		if (kind.equals(TestKind.DELETE)) {
			delDestDescription = removed("Requirements.destination"); //$NON-NLS-1$
			delContainerDescription = removed("Requirements.container"); //$NON-NLS-1$
			delSubContainerDescription = removed("Requirements.container.subContainer"); //$NON-NLS-1$
			delSource1Description = removed("Requirements.container.subContainer.source1"); //$NON-NLS-1$
			delSource2Description = removed("Requirements.container.subContainer.source2"); //$NON-NLS-1$

			delRefSource1Description = changedReference("Requirements.container.subContainer.source1", //$NON-NLS-1$
					"singleValuedReference", "Requirements.destination", null); //$NON-NLS-1$//$NON-NLS-2$
			delRefSource2Description = removedFromReference("Requirements.container.subContainer.source2", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			delDestDescription = added("Requirements.destination"); //$NON-NLS-1$
			delContainerDescription = added("Requirements.container"); //$NON-NLS-1$
			delSubContainerDescription = added("Requirements.container.subContainer"); //$NON-NLS-1$
			delSource1Description = added("Requirements.container.subContainer.source1"); //$NON-NLS-1$
			delSource2Description = added("Requirements.container.subContainer.source2"); //$NON-NLS-1$

			delRefSource1Description = changedReference("Requirements.container.subContainer.source1", //$NON-NLS-1$
					"singleValuedReference", null, "Requirements.destination"); //$NON-NLS-1$ //$NON-NLS-2$
			delRefSource2Description = addedToReference("Requirements.container.subContainer.source2", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination"); //$NON-NLS-1$//$NON-NLS-2$
		}

		final Diff delDest = Iterators.find(differences.iterator(), delDestDescription);
		final Diff delContainer = Iterators.find(differences.iterator(), delContainerDescription);
		final Diff delSubContainer = Iterators.find(differences.iterator(), delSubContainerDescription);
		final Diff delSource1 = Iterators.find(differences.iterator(), delSource1Description);
		final Diff delSource2 = Iterators.find(differences.iterator(), delSource2Description);
		final Diff delRefSource1 = Iterators.find(differences.iterator(), delRefSource1Description);
		final Diff delRefSource2 = Iterators.find(differences.iterator(), delRefSource2Description);

		assertNotNull(delDest);
		assertNotNull(delContainer);
		assertNotNull(delSubContainer);
		assertNotNull(delSource1);
		assertNotNull(delSource2);
		assertNotNull(delRefSource1);
		assertNotNull(delRefSource2);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(0, delDest.getRequires().size());

			assertEquals(0, delContainer.getRequires().size());

			assertEquals(1, delSubContainer.getRequires().size());
			assertTrue(delSubContainer.getRequires().contains(delContainer));

			assertEquals(1, delSource1.getRequires().size());
			assertTrue(delSource1.getRequires().contains(delSubContainer));

			assertEquals(1, delSource2.getRequires().size());
			assertTrue(delSource2.getRequires().contains(delSubContainer));

			assertEquals(2, delRefSource1.getRequires().size());
			assertTrue(delRefSource1.getRequires().contains(delSource1));
			assertTrue(delRefSource2.getRequires().contains(delSource2));

			assertEquals(2, delRefSource2.getRequires().size());
			assertTrue(delRefSource1.getRequires().contains(delDest));
			assertTrue(delRefSource2.getRequires().contains(delDest));
		} else {
			assertEquals(2, delDest.getRequires().size());
			assertTrue(delDest.getRequires().contains(delRefSource1));
			assertTrue(delDest.getRequires().contains(delRefSource2));

			assertEquals(1, delContainer.getRequires().size());
			assertTrue(delContainer.getRequires().contains(delSubContainer));

			assertEquals(2, delSubContainer.getRequires().size());
			assertTrue(delSubContainer.getRequires().contains(delSource1));
			assertTrue(delSubContainer.getRequires().contains(delSource2));

			assertEquals(1, delSource1.getRequires().size());
			assertTrue(delSource1.getRequires().contains(delRefSource1));

			assertEquals(1, delSource2.getRequires().size());
			assertTrue(delSource2.getRequires().contains(delRefSource2));

			assertEquals(0, delRefSource1.getRequires().size());

			assertEquals(0, delRefSource2.getRequires().size());
		}

	}

	private void testAB7(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 4 differences
		assertEquals(4, differences.size());

		Predicate<? super Diff> addBDescription = null;
		Predicate<? super Diff> delCDescription = null;
		Predicate<? super Diff> addEDescription = null;
		Predicate<? super Diff> delFDescription = null;

		if (kind.equals(TestKind.ADD)) {
			addBDescription = added("Requirements.A.B"); //$NON-NLS-1$
			delCDescription = removed("Requirements.A.C"); //$NON-NLS-1$
			addEDescription = added("Requirements.D.E"); //$NON-NLS-1$
			delFDescription = removed("Requirements.D.F"); //$NON-NLS-1$
		} else {
			addBDescription = removed("Requirements.A.B"); //$NON-NLS-1$
			delCDescription = added("Requirements.A.C"); //$NON-NLS-1$
			addEDescription = removed("Requirements.D.E"); //$NON-NLS-1$
			delFDescription = added("Requirements.D.F"); //$NON-NLS-1$
		}

		final Diff addB = Iterators.find(differences.iterator(), addBDescription);
		final Diff delC = Iterators.find(differences.iterator(), delCDescription);
		final Diff addE = Iterators.find(differences.iterator(), addEDescription);
		final Diff delF = Iterators.find(differences.iterator(), delFDescription);

		assertNotNull(addB);
		assertNotNull(delC);
		assertNotNull(addE);
		assertNotNull(delF);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(1, addB.getRequires().size());
			assertTrue(addB.getRequires().contains(delC));

			assertEquals(0, delC.getRequires().size());

			assertEquals(0, addE.getRequires().size());

			assertEquals(0, delF.getRequires().size());
		} else {
			assertEquals(0, addB.getRequires().size());

			assertEquals(1, delC.getRequires().size());
			assertTrue(delC.getRequires().contains(addB));

			assertEquals(0, addE.getRequires().size());

			assertEquals(0, delF.getRequires().size());
		}

	}

	private void testAB8(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 7 differences
		assertEquals(7, differences.size());

		Predicate<? super Diff> addBDescription = null;
		Predicate<? super Diff> delCDescription = null;
		Predicate<? super Diff> changeRefBDescription = null;
		Predicate<? super Diff> addEDescription = null;
		Predicate<? super Diff> delFDescription = null;
		Predicate<? super Diff> addRefEDescription = null;
		Predicate<? super Diff> delRefFDescription = null;

		if (kind.equals(TestKind.ADD)) {
			addBDescription = added("Requirements.B"); //$NON-NLS-1$
			delCDescription = removed("Requirements.C"); //$NON-NLS-1$
			changeRefBDescription = changedReference(
					"Requirements.A", "singleValuedReference", "Requirements.C", "Requirements.B"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			addEDescription = added("Requirements.E"); //$NON-NLS-1$
			delFDescription = removed("Requirements.F"); //$NON-NLS-1$
			addRefEDescription = addedToReference("Requirements.D", "multiValuedReference", "Requirements.E"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ 
			delRefFDescription = removedFromReference(
					"Requirements.D", "multiValuedReference", "Requirements.F"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ 
		} else {
			addBDescription = removed("Requirements.B"); //$NON-NLS-1$
			delCDescription = added("Requirements.C"); //$NON-NLS-1$
			changeRefBDescription = changedReference(
					"Requirements.A", "singleValuedReference", "Requirements.B", "Requirements.C"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			addEDescription = removed("Requirements.E"); //$NON-NLS-1$
			delFDescription = added("Requirements.F"); //$NON-NLS-1$
			addRefEDescription = removedFromReference(
					"Requirements.D", "multiValuedReference", "Requirements.E"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ 
			delRefFDescription = addedToReference("Requirements.D", "multiValuedReference", "Requirements.F"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ 
		}

		final Diff addB = Iterators.find(differences.iterator(), addBDescription);
		final Diff delC = Iterators.find(differences.iterator(), delCDescription);
		final Diff changeRefB = Iterators.find(differences.iterator(), changeRefBDescription);
		final Diff addE = Iterators.find(differences.iterator(), addEDescription);
		final Diff delF = Iterators.find(differences.iterator(), delFDescription);
		final Diff addRefE = Iterators.find(differences.iterator(), addRefEDescription);
		final Diff delRefF = Iterators.find(differences.iterator(), delRefFDescription);

		assertNotNull(addB);
		assertNotNull(delC);
		assertNotNull(changeRefB);
		assertNotNull(addE);
		assertNotNull(delF);
		assertNotNull(addRefE);
		assertNotNull(delRefF);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(0, addB.getRequires().size());

			assertEquals(1, delC.getRequires().size());
			assertTrue(delC.getRequires().contains(changeRefB));

			assertEquals(1, changeRefB.getRequires().size());
			assertTrue(changeRefB.getRequires().contains(addB));

			assertEquals(0, addE.getRequires().size());

			assertEquals(1, delF.getRequires().size());
			assertTrue(delF.getRequires().contains(delRefF));

			assertEquals(1, addRefE.getRequires().size());
			assertTrue(addRefE.getRequires().contains(addE));

			assertEquals(0, delRefF.getRequires().size());
		} else {
			assertEquals(1, addB.getRequires().size());
			assertTrue(addB.getRequires().contains(changeRefB));

			assertEquals(0, delC.getRequires().size());

			assertEquals(1, changeRefB.getRequires().size());
			assertTrue(changeRefB.getRequires().contains(delC));

			assertEquals(1, addE.getRequires().size());
			assertTrue(addE.getRequires().contains(addRefE));

			assertEquals(0, delF.getRequires().size());

			assertEquals(0, addRefE.getRequires().size());

			assertEquals(1, delRefF.getRequires().size());
			assertTrue(delRefF.getRequires().contains(delF));
		}

	}

	private void testAB9(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertEquals(2, differences.size());

		Predicate<? super Diff> addBDescription = null;
		Predicate<? super Diff> addCDescription = null;

		if (kind.equals(TestKind.ADD)) {
			addBDescription = added("Requirements.A.B"); //$NON-NLS-1$
			addCDescription = added("Requirements.A.B.C"); //$NON-NLS-1$
		} else {
			addBDescription = removed("Requirements.A.B"); //$NON-NLS-1$
			addCDescription = removed("Requirements.A.B.C"); //$NON-NLS-1$
		}

		final Diff addB = Iterators.find(differences.iterator(), addBDescription);
		final Diff addC = Iterators.find(differences.iterator(), addCDescription);

		assertNotNull(addB);
		assertNotNull(addC);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(0, addB.getRequires().size());

			assertEquals(1, addC.getRequires().size());
			assertTrue(addC.getRequires().contains(addB));
		} else {
			assertEquals(1, addB.getRequires().size());
			assertTrue(addB.getRequires().contains(addC));

			assertEquals(0, addC.getRequires().size());
		}

	}

	private void testAB10(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 4 differences
		assertEquals(4, differences.size());

		Predicate<? super Diff> addADescription = null;
		Predicate<? super Diff> addCDescription = null;
		Predicate<? super Diff> changeRefBDescription = null;
		Predicate<? super Diff> addRefBDescription = null;

		if (kind.equals(TestKind.ADD)) {
			addADescription = added("Requirements.A"); //$NON-NLS-1$
			addCDescription = added("Requirements.C"); //$NON-NLS-1$
			changeRefBDescription = changedReference("Requirements.A", "singleValuedReference", null,
					"Requirements.B");
			addRefBDescription = addedToReference("Requirements.C", "multiValuedReference", "Requirements.B");
		} else {
			addADescription = removed("Requirements.A"); //$NON-NLS-1$
			addCDescription = removed("Requirements.C"); //$NON-NLS-1$
			changeRefBDescription = changedReference("Requirements.A", "singleValuedReference",
					"Requirements.B", null);
			addRefBDescription = removedFromReference("Requirements.C", "multiValuedReference",
					"Requirements.B");
		}

		final Diff addA = Iterators.find(differences.iterator(), addADescription);
		final Diff addC = Iterators.find(differences.iterator(), addCDescription);
		final Diff changeRefB = Iterators.find(differences.iterator(), changeRefBDescription);
		final Diff addRefB = Iterators.find(differences.iterator(), addRefBDescription);

		assertNotNull(addA);
		assertNotNull(addC);
		assertNotNull(changeRefB);
		assertNotNull(addRefB);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(0, addA.getRequires().size());

			assertEquals(0, addC.getRequires().size());

			assertEquals(1, changeRefB.getRequires().size());
			assertTrue(changeRefB.getRequires().contains(addA));

			assertEquals(1, addRefB.getRequires().size());
			assertTrue(addRefB.getRequires().contains(addC));
		} else {
			assertEquals(1, addA.getRequires().size());
			assertTrue(addA.getRequires().contains(changeRefB));

			assertEquals(1, addC.getRequires().size());
			assertTrue(addC.getRequires().contains(addRefB));

			assertEquals(0, changeRefB.getRequires().size());

			assertEquals(0, addRefB.getRequires().size());
		}

	}

	private void testAB11(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 4 differences
		assertEquals(4, differences.size());

		Predicate<? super Diff> delBDescription = null;
		Predicate<? super Diff> moveCDescription = null;
		Predicate<? super Diff> moveDDescription = null;
		Predicate<? super Diff> moveEDescription = null;

		if (kind.equals(TestKind.ADD)) {
			delBDescription = removed("Requirements.A.B"); //$NON-NLS-1$
			moveCDescription = movedInReference("Requirements.A", "containmentRef1", "Requirements.A.C"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			moveDDescription = movedInReference("Requirements.A", "containmentRef1", "Requirements.A.D"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			moveEDescription = movedInReference("Requirements.A.D", "containmentRef1", "Requirements.A.D.E"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} else {
			delBDescription = added("Requirements.A.B"); //$NON-NLS-1$
			moveCDescription = movedInReference("Requirements.A.B", "containmentRef1", "Requirements.A.B.C"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			moveDDescription = movedInReference("Requirements.A.E", "containmentRef1", "Requirements.A.E.D"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			moveEDescription = movedInReference("Requirements.A", "containmentRef1", "Requirements.A.E"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		final Diff delB = Iterators.find(differences.iterator(), delBDescription);
		final Diff moveC = Iterators.find(differences.iterator(), moveCDescription);
		final Diff moveD = Iterators.find(differences.iterator(), moveDDescription);
		final Diff moveE = Iterators.find(differences.iterator(), moveEDescription);

		assertNotNull(delB);
		assertNotNull(moveC);
		assertNotNull(moveD);
		assertNotNull(moveE);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(1, delB.getRequires().size());
			assertTrue(delB.getRequires().contains(moveC));

			assertEquals(0, moveC.getRequires().size());

			assertEquals(0, moveD.getRequires().size());

			assertEquals(1, moveE.getRequires().size());
			assertTrue(moveE.getRequires().contains(moveD));
		} else {
			assertEquals(0, delB.getRequires().size());

			assertEquals(1, moveC.getRequires().size());
			assertTrue(moveC.getRequires().contains(delB));

			assertEquals(1, moveD.getRequires().size());
			assertTrue(moveD.getRequires().contains(moveE));

			assertEquals(0, moveE.getRequires().size());
		}

	}

	private void testCD1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 5 differences
		assertEquals(5, differences.size());

		Predicate<? super Diff> deleteADiffDescription = null;
		Predicate<? super Diff> deleteBDiffDescription = null;
		Predicate<? super Diff> deleteCDiffDescription = null;
		Predicate<? super Diff> deleteRefBDiffDescription = null;
		Predicate<? super Diff> deleteRefCDiffDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			deleteADiffDescription = removed("Requirements.A"); //$NON-NLS-1$
			deleteBDiffDescription = removed("Requirements.B"); //$NON-NLS-1$
			deleteCDiffDescription = removed("Requirements.C"); //$NON-NLS-1$
			deleteRefBDiffDescription = removedFromReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.B"); //$NON-NLS-1$
			deleteRefCDiffDescription = removedFromReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		} else {
			deleteADiffDescription = added("Requirements.A"); //$NON-NLS-1$
			deleteBDiffDescription = added("Requirements.B"); //$NON-NLS-1$
			deleteCDiffDescription = added("Requirements.C"); //$NON-NLS-1$
			deleteRefBDiffDescription = addedToReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.B"); //$NON-NLS-1$
			deleteRefCDiffDescription = addedToReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		}

		final Diff deleteADiff = Iterators.find(differences.iterator(), deleteADiffDescription);
		final Diff deleteBDiff = Iterators.find(differences.iterator(), deleteBDiffDescription);
		final Diff deleteCDiff = Iterators.find(differences.iterator(), deleteCDiffDescription);
		final Diff deleteRefBDiff = Iterators.find(differences.iterator(), deleteRefBDiffDescription);
		final Diff deleteRefCDiff = Iterators.find(differences.iterator(), deleteRefCDiffDescription);

		assertNotNull(deleteADiff);
		assertNotNull(deleteBDiff);
		assertNotNull(deleteCDiff);
		assertNotNull(deleteRefBDiff);
		assertNotNull(deleteRefCDiff);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(0, deleteADiff.getRequires().size());

			assertEquals(0, deleteBDiff.getRequires().size());

			assertEquals(0, deleteCDiff.getRequires().size());

			assertEquals(2, deleteRefBDiff.getRequires().size());
			assertTrue(deleteRefBDiff.getRequires().contains(deleteBDiff));
			assertTrue(deleteRefBDiff.getRequires().contains(deleteADiff));

			assertEquals(2, deleteRefCDiff.getRequires().size());
			assertTrue(deleteRefCDiff.getRequires().contains(deleteCDiff));
			assertTrue(deleteRefCDiff.getRequires().contains(deleteADiff));
		} else {
			assertEquals(2, deleteADiff.getRequires().size());
			assertTrue(deleteADiff.getRequires().contains(deleteRefBDiff));
			assertTrue(deleteADiff.getRequires().contains(deleteRefCDiff));

			assertEquals(1, deleteBDiff.getRequires().size());
			assertTrue(deleteBDiff.getRequires().contains(deleteRefBDiff));

			assertEquals(1, deleteCDiff.getRequires().size());
			assertTrue(deleteCDiff.getRequires().contains(deleteRefCDiff));

			assertEquals(0, deleteRefBDiff.getRequires().size());

			assertEquals(0, deleteRefCDiff.getRequires().size());
		}

	}

	private void testCD2(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 5 differences
		assertEquals(4, differences.size());

		Predicate<? super Diff> deleteADiffDescription = null;
		Predicate<? super Diff> deleteCDiffDescription = null;
		Predicate<? super Diff> deleteRefBDiffDescription = null;
		Predicate<? super Diff> deleteRefCDiffDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			deleteADiffDescription = removed("Requirements.A"); //$NON-NLS-1$
			deleteCDiffDescription = removed("Requirements.C"); //$NON-NLS-1$
			deleteRefBDiffDescription = removedFromReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.B"); //$NON-NLS-1$
			deleteRefCDiffDescription = removedFromReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$//$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		} else {
			deleteADiffDescription = added("Requirements.A"); //$NON-NLS-1$
			deleteCDiffDescription = added("Requirements.C"); //$NON-NLS-1$
			deleteRefBDiffDescription = addedToReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$//$NON-NLS-2$
					"Requirements.B"); //$NON-NLS-1$
			deleteRefCDiffDescription = addedToReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		}

		final Diff deleteADiff = Iterators.find(differences.iterator(), deleteADiffDescription);
		final Diff deleteCDiff = Iterators.find(differences.iterator(), deleteCDiffDescription);
		final Diff deleteRefBDiff = Iterators.find(differences.iterator(), deleteRefBDiffDescription);
		final Diff deleteRefCDiff = Iterators.find(differences.iterator(), deleteRefCDiffDescription);

		assertNotNull(deleteADiff);
		assertNotNull(deleteCDiff);
		assertNotNull(deleteRefBDiff);
		assertNotNull(deleteRefCDiff);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(0, deleteADiff.getRequires().size());

			assertEquals(0, deleteCDiff.getRequires().size());

			assertEquals(1, deleteRefBDiff.getRequires().size());
			assertTrue(deleteRefBDiff.getRequires().contains(deleteADiff));

			assertEquals(2, deleteRefCDiff.getRequires().size());
			assertTrue(deleteRefCDiff.getRequires().contains(deleteCDiff));
			assertTrue(deleteRefCDiff.getRequires().contains(deleteADiff));
		} else {
			assertEquals(2, deleteADiff.getRequires().size());
			assertTrue(deleteADiff.getRequires().contains(deleteRefBDiff));
			assertTrue(deleteADiff.getRequires().contains(deleteRefCDiff));

			assertEquals(1, deleteCDiff.getRequires().size());
			assertTrue(deleteCDiff.getRequires().contains(deleteRefCDiff));

			assertEquals(0, deleteRefBDiff.getRequires().size());

			assertEquals(0, deleteRefCDiff.getRequires().size());
		}

	}

	private void testCD3(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 5 differences
		assertEquals(4, differences.size());

		Predicate<? super Diff> deleteBDiffDescription = null;
		Predicate<? super Diff> deleteCDiffDescription = null;
		Predicate<? super Diff> deleteRefBDiffDescription = null;
		Predicate<? super Diff> deleteRefCDiffDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			deleteBDiffDescription = removed("Requirements.B"); //$NON-NLS-1$
			deleteCDiffDescription = removed("Requirements.C"); //$NON-NLS-1$
			deleteRefBDiffDescription = removedFromReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.B"); //$NON-NLS-1$
			deleteRefCDiffDescription = removedFromReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		} else {
			deleteBDiffDescription = added("Requirements.B"); //$NON-NLS-1$
			deleteCDiffDescription = added("Requirements.C"); //$NON-NLS-1$
			deleteRefBDiffDescription = addedToReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.B"); //$NON-NLS-1$
			deleteRefCDiffDescription = addedToReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		}

		final Diff deleteBDiff = Iterators.find(differences.iterator(), deleteBDiffDescription);
		final Diff deleteCDiff = Iterators.find(differences.iterator(), deleteCDiffDescription);
		final Diff deleteRefBDiff = Iterators.find(differences.iterator(), deleteRefBDiffDescription);
		final Diff deleteRefCDiff = Iterators.find(differences.iterator(), deleteRefCDiffDescription);

		assertNotNull(deleteBDiff);
		assertNotNull(deleteCDiff);
		assertNotNull(deleteRefBDiff);
		assertNotNull(deleteRefCDiff);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(0, deleteBDiff.getRequires().size());

			assertEquals(0, deleteCDiff.getRequires().size());

			assertEquals(1, deleteRefBDiff.getRequires().size());
			assertTrue(deleteRefBDiff.getRequires().contains(deleteBDiff));

			assertEquals(1, deleteRefCDiff.getRequires().size());
			assertTrue(deleteRefCDiff.getRequires().contains(deleteCDiff));
		} else {
			assertEquals(1, deleteBDiff.getRequires().size());
			assertTrue(deleteBDiff.getRequires().contains(deleteRefBDiff));

			assertEquals(1, deleteCDiff.getRequires().size());
			assertTrue(deleteCDiff.getRequires().contains(deleteRefCDiff));

			assertEquals(0, deleteRefBDiff.getRequires().size());

			assertEquals(0, deleteRefCDiff.getRequires().size());
		}

	}

	private void testCD4(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 5 differences
		assertEquals(3, differences.size());

		Predicate<? super Diff> deleteCDiffDescription = null;
		Predicate<? super Diff> deleteRefBDiffDescription = null;
		Predicate<? super Diff> deleteRefCDiffDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			deleteCDiffDescription = removed("Requirements.C"); //$NON-NLS-1$
			deleteRefBDiffDescription = removedFromReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$//$NON-NLS-2$
					"Requirements.B"); //$NON-NLS-1$
			deleteRefCDiffDescription = removedFromReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		} else {
			deleteCDiffDescription = added("Requirements.C"); //$NON-NLS-1$
			deleteRefBDiffDescription = addedToReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$//$NON-NLS-2$
					"Requirements.B"); //$NON-NLS-1$
			deleteRefCDiffDescription = addedToReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$//$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		}

		final Diff deleteCDiff = Iterators.find(differences.iterator(), deleteCDiffDescription);
		final Diff deleteRefBDiff = Iterators.find(differences.iterator(), deleteRefBDiffDescription);
		final Diff deleteRefCDiff = Iterators.find(differences.iterator(), deleteRefCDiffDescription);

		assertNotNull(deleteCDiff);
		assertNotNull(deleteRefBDiff);
		assertNotNull(deleteRefCDiff);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(0, deleteCDiff.getRequires().size());

			assertEquals(0, deleteRefBDiff.getRequires().size());

			assertEquals(1, deleteRefCDiff.getRequires().size());
			assertTrue(deleteRefCDiff.getRequires().contains(deleteCDiff));
		} else {
			assertEquals(1, deleteCDiff.getRequires().size());
			assertTrue(deleteCDiff.getRequires().contains(deleteRefCDiff));

			assertEquals(0, deleteRefBDiff.getRequires().size());

			assertEquals(0, deleteRefCDiff.getRequires().size());
		}

	}

	private void testCD5(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertEquals(2, differences.size());

		Predicate<? super Diff> deleteCDiffDescription = null;
		Predicate<? super Diff> deleteRefCDiffDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			deleteCDiffDescription = removed("Requirements.C"); //$NON-NLS-1$
			deleteRefCDiffDescription = removedFromReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$//$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		} else {
			deleteCDiffDescription = added("Requirements.C"); //$NON-NLS-1$
			deleteRefCDiffDescription = addedToReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$//$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		}

		final Diff deleteCDiff = Iterators.find(differences.iterator(), deleteCDiffDescription);
		final Diff deleteRefCDiff = Iterators.find(differences.iterator(), deleteRefCDiffDescription);

		assertNotNull(deleteCDiff);
		assertNotNull(deleteRefCDiff);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(0, deleteCDiff.getRequires().size());

			assertEquals(1, deleteRefCDiff.getRequires().size());
			assertTrue(deleteRefCDiff.getRequires().contains(deleteCDiff));
		} else {
			assertEquals(1, deleteCDiff.getRequires().size());
			assertTrue(deleteCDiff.getRequires().contains(deleteRefCDiff));

			assertEquals(0, deleteRefCDiff.getRequires().size());
		}

	}
}

/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexandra Buzila - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.multiplicitychanges;

import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.framework.RuntimeTestRunner;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.Compare;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.ResolutionStrategies;
import org.eclipse.emf.compare.uml2.internal.MultiplicityElementChange;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.runner.RunWith;

@RunWith(RuntimeTestRunner.class)
@ResolutionStrategies(ResolutionStrategyID.PROJECT)
public class MultiplicityElementChangesTest {

	private static final Predicate<Diff> IS_MULTIPLICITY_CHANGE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return diff instanceof MultiplicityElementChange;
		}
	};

	private static final Predicate<Diff> IS_LOWER_VALUE_REFERENCE_CHANGE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			if (!(diff instanceof ReferenceChange)) {
				return false;
			}
			ReferenceChange refChange = (ReferenceChange)diff;
			return refChange.getReference() == UMLPackage.eINSTANCE.getMultiplicityElement_LowerValue();
		}
	};

	private static final Predicate<Diff> IS_LOWER_VALUE_ATTRIBUTE_CHANGE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			if (!(diff instanceof AttributeChange)) {
				return false;
			}
			EObject container = MatchUtil.getContainer(diff.getMatch().getComparison(), diff);
			if (container instanceof LiteralInteger || container instanceof LiteralUnlimitedNatural) {
				EStructuralFeature eContainingFeature = container.eContainingFeature();
				return eContainingFeature == UMLPackage.eINSTANCE.getMultiplicityElement_LowerValue();
			}
			return false;
		}
	};

	private static final Predicate<Diff> IS_UPPER_VALUE_REFERENCE_CHANGE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			if (!(diff instanceof ReferenceChange)) {
				return false;
			}
			ReferenceChange refChange = (ReferenceChange)diff;
			return refChange.getReference() == UMLPackage.eINSTANCE.getMultiplicityElement_UpperValue();
		}
	};

	private static final Predicate<Diff> IS_UPPER_VALUE_ATTRIBUTE_CHANGE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			if (!(diff instanceof AttributeChange)) {
				return false;
			}
			EObject container = MatchUtil.getContainer(diff.getMatch().getComparison(), diff);
			if (container instanceof LiteralInteger || container instanceof LiteralUnlimitedNatural) {
				EStructuralFeature eContainingFeature = container.eContainingFeature();
				return eContainingFeature == UMLPackage.eINSTANCE.getMultiplicityElement_UpperValue();
			}
			return false;
		}
	};

	private static final Predicate<Diff> IS_UPPER_VALUE_CHANGE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return IS_UPPER_VALUE_REFERENCE_CHANGE.apply(diff) || IS_UPPER_VALUE_ATTRIBUTE_CHANGE.apply(diff);
		}
	};

	private static final Predicate<Diff> IS_LOWER_VALUE_CHANGE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return IS_LOWER_VALUE_REFERENCE_CHANGE.apply(diff) || IS_LOWER_VALUE_ATTRIBUTE_CHANGE.apply(diff);
		}
	};

	/**
	 * <b>origin:</b> an activity with an input pin <br>
	 * <b>left:</b> multiplicity lower value is added to the input pin <br>
	 * <b>right:</b> multiplicity upper value is added to the input pin
	 */
	@Compare(left = "a1/left.uml", right = "a1/right.uml", ancestor = "a1/origin.uml")
	public void testNonConflictingAddition(Comparison comparison) throws IOException {
		EList<Diff> diffs = comparison.getDifferences();
		Iterable<Diff> leftChanges = filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(LEFT)));
		Iterable<Diff> rightChanges = filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(RIGHT)));

		assertEquals(1, size(leftChanges));
		assertEquals(1, size(rightChanges));

		MultiplicityElementChange leftChange = (MultiplicityElementChange)leftChanges.iterator().next();
		assertEquals(1, leftChange.getRefinedBy().size());
		assertTrue(leftChange.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.ADD, leftChange.getKind());

		MultiplicityElementChange rightChange = (MultiplicityElementChange)rightChanges.iterator().next();
		assertEquals(1, rightChange.getRefinedBy().size());
		assertTrue(rightChange.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.ADD, rightChange.getKind());

		assertEquals(0, comparison.getConflicts().size());
	}

	/**
	 * <b>origin:</b> an activity with an input pin <br>
	 * <b>left:</b> multiplicity lower value is added to the input pin <br>
	 * <b>right:</b> multiplicity lower and upper values are added to the input pin, but the lower value is
	 * the same value as in the left change
	 */
	@Compare(left = "a2/left.uml", right = "a2/right.uml", ancestor = "a2/origin.uml")
	public void testAdditionsWithPseudoconflict(Comparison comparison) throws IOException {
		EList<Diff> diffs = comparison.getDifferences();
		ArrayList<Diff> leftChanges = Lists
				.newArrayList(filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(LEFT))));
		ArrayList<Diff> rightChanges = Lists
				.newArrayList(filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(RIGHT))));

		assertEquals(1, size(leftChanges));
		assertEquals(2, size(rightChanges));

		MultiplicityElementChange leftChange = (MultiplicityElementChange)leftChanges.get(0);
		assertEquals(1, leftChange.getRefinedBy().size());
		assertTrue(leftChange.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.ADD, leftChange.getKind());

		MultiplicityElementChange rightChange1 = (MultiplicityElementChange)rightChanges.get(0);
		assertEquals(1, rightChange1.getRefinedBy().size());
		assertTrue(rightChange1.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.ADD, rightChange1.getKind());

		MultiplicityElementChange rightChange2 = (MultiplicityElementChange)rightChanges.get(1);
		assertEquals(1, rightChange2.getRefinedBy().size());
		assertTrue(rightChange2.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.ADD, rightChange2.getKind());

		assertEquals(1, comparison.getConflicts().size());
		Conflict conflict = comparison.getConflicts().get(0);
		assertEquals(ConflictKind.PSEUDO, conflict.getKind());
		assertTrue(conflict.getDifferences().contains(leftChange));

		if (isLowerValueChange(rightChange1)) {
			assertEquals(conflict, rightChange1.getConflict());
		} else {
			assertTrue(isLowerValueChange(rightChange2));
			assertEquals(conflict, rightChange2.getConflict());
		}
	}

	/**
	 * <b>origin:</b> an activity with an input pin <br>
	 * <b>left:</b> multiplicity lower value is added to the input pin <br>
	 * <b>right:</b> multiplicity lower and upper values are added to the input pin, but the lower value is
	 * different than the one in the left change
	 */
	@Compare(left = "a3/left.uml", right = "a3/right.uml", ancestor = "a3/origin.uml")
	public void testAdditionsWithConflict(Comparison comparison) throws IOException {
		EList<Diff> diffs = comparison.getDifferences();
		ArrayList<Diff> leftChanges = Lists
				.newArrayList(filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(LEFT))));
		ArrayList<Diff> rightChanges = Lists
				.newArrayList(filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(RIGHT))));

		assertEquals(1, size(leftChanges));
		assertEquals(2, size(rightChanges));

		MultiplicityElementChange leftChange = (MultiplicityElementChange)leftChanges.get(0);
		assertEquals(1, leftChange.getRefinedBy().size());
		assertTrue(leftChange.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.ADD, leftChange.getKind());

		MultiplicityElementChange rightChange1 = (MultiplicityElementChange)rightChanges.get(0);
		assertEquals(1, rightChange1.getRefinedBy().size());
		assertTrue(rightChange1.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.ADD, rightChange1.getKind());

		MultiplicityElementChange rightChange2 = (MultiplicityElementChange)rightChanges.get(1);
		assertEquals(1, rightChange2.getRefinedBy().size());
		assertTrue(rightChange2.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.ADD, rightChange2.getKind());

		assertEquals(1, comparison.getConflicts().size());
		Conflict conflict = comparison.getConflicts().get(0);
		assertEquals(ConflictKind.REAL, conflict.getKind());

		if (isLowerValueChange(rightChange1)) {
			assertEquals(conflict, rightChange1.getConflict());
		} else {
			assertTrue(isLowerValueChange(rightChange2));
			assertEquals(conflict, rightChange2.getConflict());
		}
	}

	/**
	 * <b>origin:</b> an activity with an input pin, with a lower and upper value <br>
	 * <b>left:</b> multiplicity lower value is changed to 1 <br>
	 * <b>right:</b> multiplicity lower value is changed to 2 <br>
	 * A real conflict should be produced.
	 */
	@Compare(left = "a4/left.uml", right = "a4/right.uml", ancestor = "a4/origin.uml")
	public void testChangesWithConflict(Comparison comparison) throws IOException {
		EList<Diff> diffs = comparison.getDifferences();
		Iterable<Diff> leftChanges = filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(LEFT)));
		Iterable<Diff> rightChanges = filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(RIGHT)));

		assertEquals(1, size(leftChanges));
		MultiplicityElementChange leftChange = (MultiplicityElementChange)leftChanges.iterator().next();
		assertEquals(1, leftChange.getRefinedBy().size());
		assertTrue(leftChange.getRefinedBy().get(0) instanceof AttributeChange);
		assertEquals(DifferenceKind.CHANGE, leftChange.getKind());

		assertEquals(1, size(rightChanges));
		MultiplicityElementChange rightChange = (MultiplicityElementChange)rightChanges.iterator().next();
		assertEquals(1, rightChange.getRefinedBy().size());
		assertTrue(rightChange.getRefinedBy().get(0) instanceof AttributeChange);
		assertEquals(DifferenceKind.CHANGE, rightChange.getKind());

		assertEquals(1, comparison.getConflicts().size());
		Conflict conflict = comparison.getConflicts().get(0);
		assertEquals(ConflictKind.REAL, conflict.getKind());
		assertEquals(conflict, rightChange.getConflict());
	}

	/**
	 * <b>origin:</b> an activity with an input pin, with a lower and upper value <br>
	 * <b>left:</b> multiplicity lower value is changed to 1 <br>
	 * <b>right:</b> multiplicity lower value is changed to 1 <br>
	 * A pseudo conflict should be produced.
	 */
	@Compare(left = "a5/left.uml", right = "a5/right.uml", ancestor = "a5/origin.uml")
	public void testChangesWithPseudoconflict(Comparison comparison) throws IOException {
		EList<Diff> diffs = comparison.getDifferences();
		Iterable<Diff> leftChanges = filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(LEFT)));
		Iterable<Diff> rightChanges = filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(RIGHT)));

		assertEquals(1, size(leftChanges));
		Diff leftChange = leftChanges.iterator().next();
		assertEquals(1, leftChange.getRefinedBy().size());
		assertTrue(leftChange.getRefinedBy().get(0) instanceof AttributeChange);
		assertEquals(DifferenceKind.CHANGE, leftChange.getKind());

		assertEquals(1, size(rightChanges));
		Diff rightChange = rightChanges.iterator().next();
		assertEquals(1, rightChange.getRefinedBy().size());
		assertTrue(rightChange.getRefinedBy().get(0) instanceof AttributeChange);
		assertEquals(DifferenceKind.CHANGE, rightChange.getKind());

		assertEquals(1, comparison.getConflicts().size());
		Conflict conflict = comparison.getConflicts().get(0);
		assertEquals(ConflictKind.PSEUDO, conflict.getKind());
		assertEquals(conflict, rightChange.getConflict());

	}

	/**
	 * <b>origin:</b> an activity with an input pin, with a lower and upper value <br>
	 * <b>left:</b> multiplicity lower value is changed to 1 <br>
	 * <b>right:</b> multiplicity lower and upper values are removed <br>
	 */
	@Compare(left = "a6/left.uml", right = "a6/right.uml", ancestor = "a6/origin.uml")
	public void testRemoveConflict(Comparison comparison) throws IOException {
		EList<Diff> diffs = comparison.getDifferences();
		ArrayList<Diff> leftChanges = Lists
				.newArrayList(filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(LEFT))));
		ArrayList<Diff> rightChanges = Lists
				.newArrayList(filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(RIGHT))));

		assertEquals(1, size(leftChanges));
		MultiplicityElementChange leftChange = (MultiplicityElementChange)leftChanges.iterator().next();
		assertEquals(1, leftChange.getRefinedBy().size());
		assertTrue(leftChange.getRefinedBy().get(0) instanceof AttributeChange);
		assertEquals(DifferenceKind.CHANGE, leftChange.getKind());

		assertEquals(2, size(rightChanges));

		MultiplicityElementChange rightChange1 = (MultiplicityElementChange)rightChanges.get(0);
		assertEquals(1, rightChange1.getRefinedBy().size());
		assertTrue(rightChange1.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.DELETE, rightChange1.getKind());

		MultiplicityElementChange rightChange2 = (MultiplicityElementChange)rightChanges.get(1);
		assertEquals(1, rightChange2.getRefinedBy().size());
		assertTrue(rightChange2.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.DELETE, rightChange2.getKind());

		assertEquals(1, comparison.getConflicts().size());
		Conflict conflict = comparison.getConflicts().get(0);
		assertEquals(ConflictKind.REAL, conflict.getKind());

		if (isLowerValueChange(rightChange1)) {
			assertTrue(isUpperValueChange(rightChange2));
			assertEquals(conflict, rightChange1.getConflict());
		} else {
			assertTrue(isUpperValueChange(rightChange1));
			assertTrue(isLowerValueChange(rightChange2));
			assertEquals(conflict, rightChange2.getConflict());
		}
	}

	/**
	 * <b>origin:</b> an activity with an input pin, with a lower and upper value <br>
	 * <b>left:</b> multiplicity upper value is removed <br>
	 * <b>right:</b> multiplicity lower value is changed to 1 <br>
	 */
	@Compare(left = "a7/left.uml", right = "a7/right.uml", ancestor = "a7/origin.uml")
	public void testRemoveNoConflict(Comparison comparison) throws IOException {
		EList<Diff> diffs = comparison.getDifferences();
		Iterable<Diff> leftChanges = filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(LEFT)));
		Iterable<Diff> rightChanges = filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(RIGHT)));

		assertEquals(1, size(leftChanges));
		MultiplicityElementChange leftChange = (MultiplicityElementChange)leftChanges.iterator().next();
		assertEquals(1, leftChange.getRefinedBy().size());
		assertTrue(leftChange.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.DELETE, leftChange.getKind());

		assertEquals(1, size(rightChanges));
		MultiplicityElementChange rightChange = (MultiplicityElementChange)rightChanges.iterator().next();
		assertEquals(1, rightChange.getRefinedBy().size());
		assertTrue(rightChange.getRefinedBy().get(0) instanceof AttributeChange);
		assertEquals(DifferenceKind.CHANGE, rightChange.getKind());

		assertEquals(0, comparison.getConflicts().size());

	}

	/**
	 * <b>origin:</b> an activity with an input pin, with a lower and upper value <br>
	 * <b>left:</b> multiplicity upper value is removed <br>
	 * <b>right:</b> multiplicity upper value is removed <br>
	 */
	@Compare(left = "a8/left.uml", right = "a8/right.uml", ancestor = "a8/origin.uml")
	public void testRemovePseudoConflict(Comparison comparison) throws IOException {
		EList<Diff> diffs = comparison.getDifferences();
		Iterable<Diff> leftChanges = filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(LEFT)));
		Iterable<Diff> rightChanges = filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(RIGHT)));

		assertEquals(1, size(leftChanges));
		assertEquals(1, size(rightChanges));

		MultiplicityElementChange leftChange = (MultiplicityElementChange)leftChanges.iterator().next();
		assertEquals(1, leftChange.getRefinedBy().size());
		assertTrue(leftChange.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.DELETE, leftChange.getKind());

		MultiplicityElementChange rightChange = (MultiplicityElementChange)rightChanges.iterator().next();
		assertEquals(1, rightChange.getRefinedBy().size());
		assertTrue(rightChange.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.DELETE, rightChange.getKind());

		assertEquals(1, comparison.getConflicts().size());
		Conflict conflict = comparison.getConflicts().get(0);
		assertEquals(ConflictKind.PSEUDO, conflict.getKind());

	}

	/**
	 * <b>origin:</b> an activity with an input pin, with an upper value <br>
	 * <b>left:</b> multiplicity upper value is removed <br>
	 * <b>right:</b> multiplicity lower value is added <br>
	 */
	@Compare(left = "a9/left.uml", right = "a9/right.uml", ancestor = "a9/origin.uml")
	public void testRemoveAndAddNoConflict(Comparison comparison) throws IOException {
		EList<Diff> diffs = comparison.getDifferences();
		Iterable<Diff> leftChanges = filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(LEFT)));
		Iterable<Diff> rightChanges = filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(RIGHT)));

		assertEquals(1, size(leftChanges));
		assertEquals(1, size(rightChanges));

		MultiplicityElementChange leftChange = (MultiplicityElementChange)leftChanges.iterator().next();
		assertEquals(1, leftChange.getRefinedBy().size());
		assertTrue(leftChange.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.DELETE, leftChange.getKind());

		MultiplicityElementChange rightChange = (MultiplicityElementChange)rightChanges.iterator().next();
		assertEquals(1, rightChange.getRefinedBy().size());
		assertTrue(rightChange.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.ADD, rightChange.getKind());

		assertEquals(0, comparison.getConflicts().size());
	}

	/**
	 * <b>origin:</b> an activity with an input pin, with an upper value <br>
	 * <b>left:</b> multiplicity upper (0) and lower (1) value are added<br>
	 * <b>right:</b> multiplicity upper (1) and lower (1) value are added<br>
	 * These changes should produce a real and a pseudo conflict for the same multiplicity change
	 */
	@Compare(left = "a10/left.uml", right = "a10/right.uml", ancestor = "a10/origin.uml")
	public void testRealAndPseudoConflict(Comparison comparison) throws IOException {
		EList<Diff> diffs = comparison.getDifferences();
		ArrayList<Diff> leftChanges = Lists
				.newArrayList(filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(LEFT))));
		ArrayList<Diff> rightChanges = Lists
				.newArrayList(filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(RIGHT))));

		assertEquals(2, size(leftChanges));
		assertEquals(2, size(rightChanges));

		MultiplicityElementChange leftChange1 = (MultiplicityElementChange)leftChanges.get(0);
		assertEquals(1, leftChange1.getRefinedBy().size());
		assertTrue(leftChange1.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.ADD, leftChange1.getKind());

		MultiplicityElementChange leftChange2 = (MultiplicityElementChange)leftChanges.get(1);
		assertEquals(1, leftChange2.getRefinedBy().size());
		assertTrue(leftChange2.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.ADD, leftChange2.getKind());

		MultiplicityElementChange rightChange1 = (MultiplicityElementChange)rightChanges.get(0);
		assertEquals(1, rightChange1.getRefinedBy().size());
		assertTrue(rightChange1.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.ADD, rightChange1.getKind());

		MultiplicityElementChange rightChange2 = (MultiplicityElementChange)rightChanges.get(1);
		assertEquals(1, rightChange2.getRefinedBy().size());
		assertTrue(rightChange2.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.ADD, rightChange2.getKind());

		assertEquals(2, comparison.getConflicts().size());
		Conflict conflict1 = comparison.getConflicts().get(0);
		Conflict conflict2 = comparison.getConflicts().get(1);

		if (conflict1.getKind() == ConflictKind.PSEUDO) {
			assertEquals(ConflictKind.REAL, conflict2.getKind());
		} else {
			assertEquals(ConflictKind.PSEUDO, conflict2.getKind());
		}

		if (isUpperValueChange(leftChange1)) {
			assertTrue(isLowerValueChange(leftChange2));
			assertNotNull(leftChange1.getConflict());
			assertEquals(ConflictKind.REAL, leftChange1.getConflict().getKind());
			assertNotNull(leftChange2.getConflict());
			assertEquals(ConflictKind.PSEUDO, leftChange2.getConflict().getKind());
		} else {
			assertTrue(isLowerValueChange(leftChange1));
			assertTrue(isUpperValueChange(leftChange2));
			assertNotNull(leftChange1.getConflict());
			assertEquals(ConflictKind.PSEUDO, leftChange1.getConflict().getKind());
			assertNotNull(leftChange2.getConflict());
			assertEquals(ConflictKind.REAL, leftChange2.getConflict().getKind());
		}

		if (isUpperValueChange(rightChange1)) {
			assertTrue(isLowerValueChange(rightChange2));
			assertNotNull(rightChange1.getConflict());
			assertEquals(ConflictKind.REAL, rightChange1.getConflict().getKind());
			assertNotNull(rightChange2.getConflict());
			assertEquals(ConflictKind.PSEUDO, rightChange2.getConflict().getKind());
		} else {
			assertTrue(isLowerValueChange(rightChange1));
			assertTrue(isUpperValueChange(rightChange2));
			assertNotNull(rightChange1.getConflict());
			assertEquals(ConflictKind.PSEUDO, rightChange1.getConflict().getKind());
			assertNotNull(rightChange2.getConflict());
			assertEquals(ConflictKind.REAL, rightChange2.getConflict().getKind());
		}

	}

	private boolean isLowerValueChange(MultiplicityElementChange multiplicityElementChange) {
		if (multiplicityElementChange.getRefinedBy().size() != 1) {
			return false;
		}
		return IS_LOWER_VALUE_CHANGE.apply(multiplicityElementChange.getRefinedBy().get(0));
	}

	private boolean isUpperValueChange(MultiplicityElementChange multiplicityElementChange) {
		if (multiplicityElementChange.getRefinedBy().size() != 1) {
			return false;
		}
		return IS_UPPER_VALUE_CHANGE.apply(multiplicityElementChange.getRefinedBy().get(0));
	}

	/**
	 * <b>origin:</b> an activity with an input pin, with a lower value <br>
	 * <b>left:</b> multiplicity lower value is deleted and upper value is added (0)<br>
	 * <b>right:</b> multiplicity upper value is added, with the same value as in the left change and the
	 * lower value is changed to a different value than the one in the left change<br>
	 * These changes should produce a real and a pseudo conflict for the same multiplicity change
	 */
	@Compare(left = "a11/left.uml", right = "a11/right.uml", ancestor = "a11/origin.uml")
	public void testRealAndPseudoConflictWithChangeAndDelete(Comparison comparison) throws IOException {
		EList<Diff> diffs = comparison.getDifferences();
		ArrayList<Diff> leftChanges = Lists
				.newArrayList(filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(LEFT))));
		ArrayList<Diff> rightChanges = Lists
				.newArrayList(filter(diffs, and(IS_MULTIPLICITY_CHANGE, fromSide(RIGHT))));

		assertEquals(2, size(leftChanges));
		assertEquals(2, size(rightChanges));

		MultiplicityElementChange leftAddChange;
		MultiplicityElementChange leftDeleteChange;

		if (leftChanges.get(0).getKind() == DifferenceKind.ADD) {
			leftAddChange = (MultiplicityElementChange)leftChanges.get(0);
			leftDeleteChange = (MultiplicityElementChange)leftChanges.get(1);
		} else {
			leftAddChange = (MultiplicityElementChange)leftChanges.get(1);
			leftDeleteChange = (MultiplicityElementChange)leftChanges.get(0);
		}

		assertEquals(1, leftAddChange.getRefinedBy().size());
		assertTrue(leftAddChange.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.ADD, leftAddChange.getKind());

		assertEquals(1, leftDeleteChange.getRefinedBy().size());
		assertTrue(leftDeleteChange.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.DELETE, leftDeleteChange.getKind());

		MultiplicityElementChange rightChange;
		MultiplicityElementChange rightAddChange;

		if (rightChanges.get(0).getKind() == DifferenceKind.ADD) {
			rightAddChange = (MultiplicityElementChange)rightChanges.get(0);
			rightChange = (MultiplicityElementChange)rightChanges.get(1);
		} else {
			rightAddChange = (MultiplicityElementChange)rightChanges.get(1);
			rightChange = (MultiplicityElementChange)rightChanges.get(0);
		}

		assertEquals(1, rightAddChange.getRefinedBy().size());
		assertTrue(rightAddChange.getRefinedBy().get(0) instanceof ReferenceChange);
		assertEquals(DifferenceKind.ADD, rightAddChange.getKind());

		assertEquals(1, rightChange.getRefinedBy().size());
		assertTrue(rightChange.getRefinedBy().get(0) instanceof AttributeChange);
		assertEquals(DifferenceKind.CHANGE, rightChange.getKind());

		assertEquals(2, comparison.getConflicts().size());
		Conflict conflict1 = comparison.getConflicts().get(0);
		Conflict conflict2 = comparison.getConflicts().get(1);

		if (conflict1.getKind() == ConflictKind.PSEUDO) {
			assertEquals(ConflictKind.REAL, conflict2.getKind());
		} else {
			assertEquals(ConflictKind.PSEUDO, conflict2.getKind());
		}
		assertEquals(ConflictKind.REAL, leftDeleteChange.getConflict().getKind());
		assertEquals(leftDeleteChange.getConflict(), rightChange.getConflict());
		assertEquals(ConflictKind.PSEUDO, leftAddChange.getConflict().getKind());
		assertEquals(leftAddChange.getConflict(), rightAddChange.getConflict());
	}

	// @Override
	// protected void registerPostProcessors(
	// org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor.Registry<String> postProcessorRegistry)
	// {
	// super.registerPostProcessors(postProcessorRegistry);
	// postProcessorRegistry.put(MultiplicityElementChangePostProcessor.class.getName(),
	// new TestPostProcessor.TestPostProcessorDescriptor(Pattern
	// .compile("http://www.eclipse.org/uml2/\\d\\.0\\.0/UML"), null, //$NON-NLS-1$
	// new MultiplicityElementChangePostProcessor(), 25));
	// }
}

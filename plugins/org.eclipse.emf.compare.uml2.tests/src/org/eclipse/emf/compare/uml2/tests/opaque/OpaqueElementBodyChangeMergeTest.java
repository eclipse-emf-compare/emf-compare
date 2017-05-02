/*******************************************************************************
 * Copyright (c) 2014, 2017 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *     Martin Fleck - bug 516060
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.opaque;

import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;

import java.io.IOException;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.uml2.internal.OpaqueElementBodyChange;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLTest;
import org.eclipse.emf.compare.uml2.tests.opaque.data.OpaqueInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests merging of the body attribute of {@link OpaqueBehavior opaque behaviors}, {@link OpaqueAction opaque
 * actions}, and {@link OpaqueExpression opaque expressions} containing multi-line contents also with respect
 * to their respective language attribute.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
@SuppressWarnings("nls")
public class OpaqueElementBodyChangeMergeTest extends AbstractUMLTest {

	private static final String NL = "\r\n";

	private static final String OPAQUE_ACTION1_ID = "_opaqueAction1";

	private static final String OPAQUE_BEHAVIOR1_ID = "_opaqueBehavior1";

	private static final String OPAQUE_EXPRESSION1_ID = "_opaqueExpression1";

	private static final String OCL = "OCL";

	private static final String JAVA = "JAVA";

	private static final String EXPECTED_MERGE = "This is a" + NL //
			+ "test with multi-line (changed)" + NL //
			+ "String attribute" + NL //
			+ "and concurrent changes (changed)" + NL //
			+ "of them.";

	private static final String EXPECTED_MERGE_JAVA = "This is a JAVA" + NL//
			+ "test with multi-line" + NL //
			+ "String attribute" + NL //
			+ "and concurrent changes (changed)" + NL //
			+ "of them.";

	private static final String EXPECTED_MERGE_OCL = "This is an OCL" + NL//
			+ "test with multi-line (changed)" + NL //
			+ "String attribute" + NL //
			+ "and concurrent changes (changed)" + NL //
			+ "of them.";

	private static final String EXPECTED_JAVA = "This is a JAVA" + NL//
			+ "test with multi-line" + NL //
			+ "String attribute" + NL //
			+ "and concurrent changes" + NL //
			+ "of them.";

	private static final String EXPECTED_OCL = "This is an OCL" + NL//
			+ "test with multi-line" + NL //
			+ "String attribute" + NL //
			+ "and concurrent changes" + NL //
			+ "of them.";

	private static final String EXPECTED_C = "This is a C" + NL//
			+ "test with multi-line" + NL //
			+ "String attribute" + NL //
			+ "and concurrent changes" + NL //
			+ "of them.";

	private static final String A2_ORIGIN = "This is a" + NL //
			+ "test with multi-line" + NL //
			+ "String attribute" + NL //
			+ "and concurrent changes" + NL //
			+ "of them.";

	private static final String A2_LEFT = "This is a" + NL //
			+ "test with multi-line (changed)" + NL //
			+ "String attribute" + NL //
			+ "and concurrent changes" + NL //
			+ "of them.";

	private static final String A2_RIGHT = "This is a" + NL //
			+ "(changed)test with multi-line" + NL //
			+ "String attribute" + NL //
			+ "and concurrent changes" + NL //
			+ "of them.";

	private static final Predicate<Diff> IS_OPAQUE_ELEMENT_CHANGE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return diff instanceof OpaqueElementBodyChange;
		}
	};

	private static final Predicate<Conflict> IS_REAL_CONFLICT = new Predicate<Conflict>() {
		public boolean apply(Conflict conflict) {
			return ConflictKind.REAL.equals(conflict.getKind());
		}
	};

	private static final Predicate<Conflict> CONCERNS_OPAQUE_ELEMENT_BODY_CHANGE = new Predicate<Conflict>() {
		public boolean apply(Conflict conflict) {
			return any(conflict.getDifferences(), IS_OPAQUE_ELEMENT_CHANGE);
		}
	};

	private OpaqueInputData input = new OpaqueInputData();

	@BeforeClass
	public static void setupClass() {
		fillRegistries();
	}

	@AfterClass
	public static void teardownClass() {
		resetRegistries();
	}

	@Test
	public void testA1UseCaseRtoL() throws IOException {
		Resource origin = input.getA1Origin();
		Resource left = input.getA1Left();
		Resource right = input.getA1Right();

		Comparison comparison = compare(left, right, origin);
		assertNoRealConflict(comparison);

		applyRightOpaqueElementBodyChangesToLeft(comparison);
		assertA1UseCaseMergeResult(left.getEObject(OPAQUE_ACTION1_ID));
	}

	@Test
	public void testA1UseCaseLtoR() throws IOException {
		Resource origin = input.getA1Origin();
		Resource left = input.getA1Left();
		Resource right = input.getA1Right();

		Comparison comparison = compare(left, right, origin);
		assertNoRealConflict(comparison);

		applyLeftOpaqueElementBodyChangesToRight(comparison);
		assertA1UseCaseMergeResult(right.getEObject(OPAQUE_ACTION1_ID));
	}

	private void assertA1UseCaseMergeResult(EObject eObject) {
		assertTrue(eObject instanceof OpaqueAction);

		OpaqueAction action = (OpaqueAction)eObject;
		assertEquals(1, action.getLanguages().size());
		assertEquals(1, action.getBodies().size());

		assertEquals(JAVA, action.getLanguages().get(0));
		assertEquals(EXPECTED_MERGE, action.getBodies().get(0));
	}

	@Test
	public void testA1UseCase_RevertChangeLeftTwoWay() throws IOException {
		Resource origin = input.getA1Origin();
		Resource left = input.getA1Left();

		OpaqueAction originAction = (OpaqueAction)origin.getEObject(OPAQUE_ACTION1_ID);
		String originBody = originAction.getBodies().get(0);

		Comparison comparison = compare(left, origin, null);
		revertLeftOpaqueElementBodyChanges(comparison);
		assertOneBodyWithContents(left, originBody);
	}

	@Test
	public void testA1UseCase_RevertChangeRightThreeWay() throws IOException {
		Resource origin = input.getA1Origin();
		Resource left = input.getA1Left();
		Resource right = input.getA1Right();

		OpaqueAction originAction = (OpaqueAction)origin.getEObject(OPAQUE_ACTION1_ID);
		String originBody = originAction.getBodies().get(0);

		Comparison comparison = compare(left, right, origin);
		revertRightOpaqueElementBodyChanges(comparison);
		assertOneBodyWithContents(right, originBody);
	}

	@Test
	public void testA1UseCase_ApplyLeftRevertRightChangeThreeWay() throws IOException {
		Resource origin = input.getA1Origin();
		Resource left = input.getA1Left();
		Resource right = input.getA1Right();

		OpaqueAction leftAction = (OpaqueAction)left.getEObject(OPAQUE_ACTION1_ID);
		String leftBody = leftAction.getBodies().get(0);

		Comparison comparison = compare(left, right, origin);
		applyLeftOpaqueElementBodyChangesToRight(comparison);
		revertRightOpaqueElementBodyChanges(comparison);
		assertOneBodyWithContents(right, leftBody);
	}

	@Test
	public void testA1UseCase_ApplyRightRevertLeftChangeThreeWay() throws IOException {
		Resource origin = input.getA1Origin();
		Resource left = input.getA1Left();
		Resource right = input.getA1Right();

		OpaqueAction rightAction = (OpaqueAction)right.getEObject(OPAQUE_ACTION1_ID);
		String rightBody = rightAction.getBodies().get(0);

		Comparison comparison = compare(left, right, origin);
		applyRightOpaqueElementBodyChangesToLeft(comparison);
		revertLeftOpaqueElementBodyChanges(comparison);
		assertOneBodyWithContents(left, rightBody);
	}

	@Test
	public void testA2UseCase() throws IOException {
		Resource origin = input.getA2Origin();
		Resource left = input.getA2Left();
		Resource right = input.getA2Right();

		Comparison comparison = compare(left, right, origin);
		assertOneRealConflictOnOpaqueElementBodyChange(comparison);
	}

	@Test
	public void testA2UseCaseLtoR() throws IOException {
		Resource origin = input.getA2Origin();
		Resource left = input.getA2Left();
		Resource right = input.getA2Right();

		Comparison comparison = compare(left, right, origin);
		assertOneRealConflictOnOpaqueElementBodyChange(comparison);
		assertA2UseCaseBody(origin.getEObject(OPAQUE_ACTION1_ID), A2_ORIGIN);
		assertA2UseCaseBody(left.getEObject(OPAQUE_ACTION1_ID), A2_LEFT);
		assertA2UseCaseBody(right.getEObject(OPAQUE_ACTION1_ID), A2_RIGHT);

		// real conflict, apply left side
		applyLeftOpaqueElementBodyChangesToRight(comparison);

		assertA2UseCaseBody(origin.getEObject(OPAQUE_ACTION1_ID), A2_ORIGIN);
		assertA2UseCaseBody(left.getEObject(OPAQUE_ACTION1_ID), A2_LEFT);
		assertA2UseCaseBody(right.getEObject(OPAQUE_ACTION1_ID), A2_LEFT);
	}

	@Test
	public void testA2UseCaseRtoL() throws IOException {
		Resource origin = input.getA2Origin();
		Resource left = input.getA2Left();
		Resource right = input.getA2Right();

		Comparison comparison = compare(left, right, origin);
		assertOneRealConflictOnOpaqueElementBodyChange(comparison);
		assertA2UseCaseBody(origin.getEObject(OPAQUE_ACTION1_ID), A2_ORIGIN);
		assertA2UseCaseBody(left.getEObject(OPAQUE_ACTION1_ID), A2_LEFT);
		assertA2UseCaseBody(right.getEObject(OPAQUE_ACTION1_ID), A2_RIGHT);

		// real conflict, apply right side
		applyRightOpaqueElementBodyChangesToLeft(comparison);

		assertA2UseCaseBody(origin.getEObject(OPAQUE_ACTION1_ID), A2_ORIGIN);
		assertA2UseCaseBody(left.getEObject(OPAQUE_ACTION1_ID), A2_RIGHT);
		assertA2UseCaseBody(right.getEObject(OPAQUE_ACTION1_ID), A2_RIGHT);
	}

	private void assertA2UseCaseBody(EObject eObject, String expectedBody) {
		assertTrue(eObject instanceof OpaqueAction);
		OpaqueAction opaqueAction = (OpaqueAction)eObject;
		assertEquals(1, opaqueAction.getBodies().size());
		assertEquals(1, opaqueAction.getLanguages().size());
		assertEquals(JAVA, opaqueAction.getLanguages().get(0));
		assertEquals(expectedBody, opaqueAction.getBodies().get(0));
	}

	@Test
	public void testA3UseCase() throws IOException {
		Resource origin = input.getA3Origin();
		Resource left = input.getA3Left();
		Resource right = input.getA3Right();

		Comparison comparison = compare(left, right, origin);
		assertOneRealConflictOnOpaqueElementBodyChange(comparison);
	}

	@Test
	public void testA4UseCaseLtoR() throws IOException {
		Resource origin = input.getA4Origin();
		Resource left = input.getA4Left();
		Resource right = input.getA4Right();

		Comparison comparison = compare(left, right, origin);
		assertNoRealConflict(comparison);

		applyLeftOpaqueElementBodyChangesToRight(comparison);
		assertA4UseCaseMergeResult(right.getEObject(OPAQUE_ACTION1_ID));
	}

	@Test
	public void testA4UseCaseRtoL() throws IOException {
		Resource origin = input.getA4Origin();
		Resource left = input.getA4Left();
		Resource right = input.getA4Right();

		Comparison comparison = compare(left, right, origin);
		assertNoRealConflict(comparison);

		applyRightOpaqueElementBodyChangesToLeft(comparison);
		assertA4UseCaseMergeResult(left.getEObject(OPAQUE_ACTION1_ID));
	}

	private void assertA4UseCaseMergeResult(EObject eObject) {
		assertTrue(eObject instanceof OpaqueAction);

		OpaqueAction action = (OpaqueAction)eObject;
		assertEquals(2, action.getLanguages().size());
		assertEquals(2, action.getBodies().size());

		assertEquals(JAVA, action.getLanguages().get(0));
		assertEquals(OCL, action.getLanguages().get(1));

		assertEquals(EXPECTED_MERGE_JAVA, action.getBodies().get(0));
		assertEquals(EXPECTED_OCL, action.getBodies().get(1));
	}

	@Test
	public void testA4UseCase_RevertAdditionLeftTwoWay() throws IOException {
		Resource origin = input.getA4Origin();
		Resource left = input.getA4Left();

		OpaqueAction originAction = (OpaqueAction)origin.getEObject(OPAQUE_ACTION1_ID);
		String originBody = originAction.getBodies().get(0);

		Comparison comparison = compare(left, origin, null);
		revertLeftOpaqueElementBodyChanges(comparison);
		assertOneBodyWithContents(left, originBody);
	}

	@Test
	public void testA4UseCase_RevertDeletionLeftTwoWay() throws IOException {
		Resource left = input.getA4Left();
		Resource origin = input.getA4Origin();

		// swapped left and origin to create deletion
		Comparison comparison = compare(origin, left, null);
		revertLeftOpaqueElementBodyChanges(comparison);

		EObject eObject = origin.getEObject(OPAQUE_ACTION1_ID);
		OpaqueAction action = (OpaqueAction)eObject;
		assertEquals(2, action.getLanguages().size());
		assertEquals(2, action.getBodies().size());
	}

	@Test
	public void testA5UseCaseLtoR() throws IOException {
		Resource origin = input.getA5Origin();
		Resource left = input.getA5Left();
		Resource right = input.getA5Right();

		Comparison comparison = compare(left, right, origin);
		assertNoRealConflict(comparison);

		applyLeftOpaqueElementBodyChangesToRight(comparison);
		assertA5UseCaseMergeResult(right.getEObject(OPAQUE_ACTION1_ID));
	}

	@Test
	public void testA5UseCaseRtoL() throws IOException {
		Resource origin = input.getA5Origin();
		Resource left = input.getA5Left();
		Resource right = input.getA5Right();

		Comparison comparison = compare(left, right, origin);
		assertNoRealConflict(comparison);

		applyRightOpaqueElementBodyChangesToLeft(comparison);
		assertA5UseCaseMergeResult(left.getEObject(OPAQUE_ACTION1_ID));
	}

	private void assertA5UseCaseMergeResult(EObject eObject) {
		assertTrue(eObject instanceof OpaqueAction);

		OpaqueAction action = (OpaqueAction)eObject;
		assertEquals(3, action.getLanguages().size());
		assertEquals(3, action.getBodies().size());

		assertTrue(action.getLanguages().contains("JAVA"));
		assertTrue(action.getLanguages().contains("OCL"));
		assertTrue(action.getLanguages().contains("C"));

		int indexJava = action.getLanguages().indexOf("JAVA");
		int indexOcl = action.getLanguages().indexOf("OCL");
		int indexC = action.getLanguages().indexOf("C");

		assertEquals(EXPECTED_JAVA, action.getBodies().get(indexJava));
		assertEquals(EXPECTED_OCL, action.getBodies().get(indexOcl));
		assertEquals(EXPECTED_C, action.getBodies().get(indexC));
	}

	@Test
	public void testA6UseCaseLtoR() throws IOException {
		Resource origin = input.getA6Origin();
		Resource left = input.getA6Left();
		Resource right = input.getA6Right();

		Comparison comparison = compare(left, right, origin);
		assertNoRealConflict(comparison);

		applyLeftOpaqueElementBodyChangesToRight(comparison);
		assertA6UseCaseMergeResult(right.getEObject(OPAQUE_ACTION1_ID));
	}

	@Test
	public void testA6UseCaseRtoL() throws IOException {
		Resource origin = input.getA6Origin();
		Resource left = input.getA6Left();
		Resource right = input.getA6Right();

		Comparison comparison = compare(left, right, origin);
		assertNoRealConflict(comparison);

		applyRightOpaqueElementBodyChangesToLeft(comparison);
		assertA6UseCaseMergeResult(left.getEObject(OPAQUE_ACTION1_ID));
	}

	private void assertA6UseCaseMergeResult(EObject eObject) {
		assertTrue(eObject instanceof OpaqueAction);

		OpaqueAction action = (OpaqueAction)eObject;
		assertEquals(2, action.getLanguages().size());
		assertEquals(2, action.getBodies().size());

		assertTrue(action.getLanguages().contains("JAVA"));
		assertTrue(action.getLanguages().contains("OCL"));

		int indexJava = action.getLanguages().indexOf("JAVA");
		int indexOcl = action.getLanguages().indexOf("OCL");

		assertEquals(EXPECTED_JAVA, action.getBodies().get(indexJava));
		assertEquals(EXPECTED_MERGE_OCL, action.getBodies().get(indexOcl));

		assertEquals(indexOcl, 0);
		assertEquals(indexJava, 1);
	}

	@Test
	public void testA6UseCase_RevertReorderRightTwoWay() throws IOException {
		Resource origin = input.getA6Origin();
		// we take right as left, because we want to test reverting the reordering
		Resource left = input.getA6Right();

		Comparison comparison = compare(left, origin, null);
		revertLeftOpaqueElementBodyChanges(comparison);

		OpaqueAction action = (OpaqueAction)left.getEObject(OPAQUE_ACTION1_ID);

		assertEquals(2, action.getLanguages().size());
		assertEquals(2, action.getBodies().size());

		int indexJava = action.getLanguages().indexOf("JAVA");
		int indexOcl = action.getLanguages().indexOf("OCL");

		// assert that the order of languages and bodies is reverted
		assertEquals(indexJava, 0);
		assertEquals(indexOcl, 1);
		assertEquals(EXPECTED_JAVA, action.getBodies().get(indexJava));
		assertEquals(EXPECTED_OCL, action.getBodies().get(indexOcl));
	}

	@Test
	public void testA7UseCase() throws IOException {
		Resource origin = input.getA7Origin();
		Resource left = input.getA7Left();
		Resource right = input.getA7Right();

		Comparison comparison = compare(left, right, origin);
		assertOneRealConflictOnOpaqueElementBodyChange(comparison);
	}

	@Test
	public void testA8UseCase() throws IOException {
		Resource origin = input.getA8Origin();
		Resource left = input.getA8Left();
		Resource right = input.getA8Right();

		Comparison comparison = compare(left, right, origin);
		assertOneRealConflictOnOpaqueElementBodyChange(comparison);
	}

	@Test
	public void testA9UseCaseLtoR() throws IOException {
		Resource origin = input.getA9Origin();
		Resource left = input.getA9Left();
		Resource right = input.getA9Right();

		Comparison comparison = compare(left, right, origin);
		assertNoRealConflict(comparison);

		applyLeftOpaqueElementBodyChangesToRight(comparison);

		assertA9UseCaseMergeResult(right);
	}

	@Test
	public void testA9UseCaseRtoL() throws IOException {
		Resource origin = input.getA9Origin();
		Resource left = input.getA9Left();
		Resource right = input.getA9Right();

		Comparison comparison = compare(left, right, origin);
		assertNoRealConflict(comparison);

		applyRightOpaqueElementBodyChangesToLeft(comparison);

		assertA9UseCaseMergeResult(left);
	}

	private void assertA9UseCaseMergeResult(Resource resource) {
		EObject eObject = resource.getEObject(OPAQUE_ACTION1_ID);
		assertTrue(eObject instanceof OpaqueAction);
		OpaqueAction opaqueAction = (OpaqueAction)eObject;
		assertEquals(0, opaqueAction.getBodies().size());
		assertEquals(0, opaqueAction.getLanguages().size());
	}

	@Test
	public void testA10UseCaseRtoL() throws IOException {
		Resource origin = input.getA10Origin();
		Resource left = input.getA10Left();
		Resource right = input.getA10Right();

		Comparison comparison = compare(left, right, origin);
		assertNoRealConflict(comparison);

		applyRightOpaqueElementBodyChangesToLeft(comparison);

		assertOneBodyWithContents(left, EXPECTED_MERGE_OCL);
	}

	@Test
	public void testA10UseCaseLtoR() throws IOException {
		Resource origin = input.getA10Origin();
		Resource left = input.getA10Left();
		Resource right = input.getA10Right();

		Comparison comparison = compare(left, right, origin);
		assertNoRealConflict(comparison);

		applyLeftOpaqueElementBodyChangesToRight(comparison);

		assertOneBodyWithContents(right, EXPECTED_MERGE_OCL);
	}

	@Test
	public void testA10UseCaseRtoL_RevertLeftDeletion() throws IOException {
		Resource origin = input.getA10Origin();
		Resource left = input.getA10Left();
		Resource right = input.getA10Right();

		Comparison comparison = compare(left, right, origin);
		assertNoRealConflict(comparison);

		revertLeftOpaqueElementBodyChanges(comparison);

		EObject leftEObject = left.getEObject(OPAQUE_ACTION1_ID);
		OpaqueAction leftOpaqueAction = (OpaqueAction)leftEObject;

		String leftLanguageAt0 = leftOpaqueAction.getLanguages().get(0);
		String leftBodyAt0 = leftOpaqueAction.getBodies().get(0);

		assertEquals("JAVA", leftLanguageAt0);
		assertEquals(EXPECTED_JAVA, leftBodyAt0);

		String leftLanguageAt1 = leftOpaqueAction.getLanguages().get(1);
		String leftBodyAt1 = leftOpaqueAction.getBodies().get(1);

		assertEquals("OCL", leftLanguageAt1);
		assertEquals(EXPECTED_OCL, leftBodyAt1);
	}

	// reverting addition and insertion index?

	@Test
	public void testA11UseCase() throws IOException {
		Resource origin = input.getA11Origin();
		Resource left = input.getA11Left();
		Resource right = input.getA11Right();

		Comparison comparison = compare(left, right, origin);
		assertOneRealConflictOnOpaqueElementBodyChange(comparison);
	}

	@Test
	public void testA12UseCase() throws IOException {
		Resource origin = input.getA12Origin();
		Resource left = input.getA12Left();
		Resource right = input.getA12Right();

		Comparison comparison = compare(left, right, origin);
		assertOneRealConflictOnOpaqueElementBodyChange(comparison);
	}

	@Test
	public void testA13UseCase() throws IOException {
		Resource origin = input.getA13Origin();
		Resource left = input.getA13Left();
		Resource right = input.getA13Right();

		Comparison comparison = compare(left, right, origin);
		assertNoRealConflict(comparison);

		applyLeftOpaqueElementBodyChangesToRight(comparison);

		EObject leftEObject = left.getEObject(OPAQUE_ACTION1_ID);
		OpaqueAction leftOpaqueAction = (OpaqueAction)leftEObject;
		String bodyLeft = leftOpaqueAction.getBodies().get(0);

		assertOneBodyWithContents(right, bodyLeft);
	}

	@Test
	public void testB1UseCaseRtoL() throws IOException {
		Resource origin = input.getB1Origin();
		Resource left = input.getB1Left();
		Resource right = input.getB1Right();

		Comparison comparison = compare(left, right, origin);
		assertNoRealConflict(comparison);

		applyRightOpaqueElementBodyChangesToLeft(comparison);
		assertB1UseCaseMergeResult(left.getEObject(OPAQUE_BEHAVIOR1_ID));
	}

	@Test
	public void testB1UseCaseLtoR() throws IOException {
		Resource origin = input.getB1Origin();
		Resource left = input.getB1Left();
		Resource right = input.getB1Right();

		Comparison comparison = compare(left, right, origin);
		assertNoRealConflict(comparison);

		applyLeftOpaqueElementBodyChangesToRight(comparison);
		assertB1UseCaseMergeResult(right.getEObject(OPAQUE_BEHAVIOR1_ID));
	}

	private void assertB1UseCaseMergeResult(EObject eObject) {
		assertTrue(eObject instanceof OpaqueBehavior);

		OpaqueBehavior action = (OpaqueBehavior)eObject;
		assertEquals(1, action.getLanguages().size());
		assertEquals(1, action.getBodies().size());

		assertEquals(JAVA, action.getLanguages().get(0));
		assertEquals(EXPECTED_MERGE, action.getBodies().get(0));
	}

	@Test
	public void testE1UseCaseRtoL() throws IOException {
		Resource origin = input.getE1Origin();
		Resource left = input.getE1Left();
		Resource right = input.getE1Right();

		Comparison comparison = compare(left, right, origin);
		assertNoRealConflict(comparison);

		applyRightOpaqueElementBodyChangesToLeft(comparison);
		assertE1UseCaseMergeResult(left.getEObject(OPAQUE_EXPRESSION1_ID));
	}

	@Test
	public void testE1UseCaseLtoR() throws IOException {
		Resource origin = input.getE1Origin();
		Resource left = input.getE1Left();
		Resource right = input.getE1Right();

		Comparison comparison = compare(left, right, origin);
		assertNoRealConflict(comparison);

		applyLeftOpaqueElementBodyChangesToRight(comparison);
		assertE1UseCaseMergeResult(right.getEObject(OPAQUE_EXPRESSION1_ID));
	}

	private void assertE1UseCaseMergeResult(EObject eObject) {
		assertTrue(eObject instanceof OpaqueExpression);

		OpaqueExpression action = (OpaqueExpression)eObject;
		assertEquals(1, action.getLanguages().size());
		assertEquals(1, action.getBodies().size());

		assertEquals(JAVA, action.getLanguages().get(0));
		assertEquals(EXPECTED_MERGE, action.getBodies().get(0));
	}

	private void assertOneBodyWithContents(Resource resource, String contents) {
		EObject eObject = resource.getEObject(OPAQUE_ACTION1_ID);
		assertTrue(eObject instanceof OpaqueAction);
		OpaqueAction opaqueAction = (OpaqueAction)eObject;
		assertEquals(1, opaqueAction.getBodies().size());
		assertEquals(1, opaqueAction.getLanguages().size());

		String body = opaqueAction.getBodies().get(0);
		assertEquals(contents, body);
	}

	private void applyRightOpaqueElementBodyChangesToLeft(Comparison comparison) {
		final EList<Diff> allDifferences = comparison.getDifferences();
		final Iterable<Diff> rightOpaqueElementBodyChanges = filter(allDifferences,
				and(IS_OPAQUE_ELEMENT_CHANGE, fromSide(RIGHT)));
		final IBatchMerger merger = new BatchMerger(getMergerRegistry());
		merger.copyAllRightToLeft(rightOpaqueElementBodyChanges, new BasicMonitor());
	}

	private void revertLeftOpaqueElementBodyChanges(Comparison comparison) {
		final EList<Diff> allDifferences = comparison.getDifferences();
		final Iterable<Diff> leftOpaqueElementBodyChanges = filter(allDifferences,
				and(IS_OPAQUE_ELEMENT_CHANGE, fromSide(LEFT)));
		final IBatchMerger merger = new BatchMerger(getMergerRegistry());
		merger.copyAllRightToLeft(leftOpaqueElementBodyChanges, new BasicMonitor());
	}

	private void revertRightOpaqueElementBodyChanges(Comparison comparison) {
		final EList<Diff> allDifferences = comparison.getDifferences();
		final Iterable<Diff> rightOpaqueElementBodyChanges = filter(allDifferences,
				and(IS_OPAQUE_ELEMENT_CHANGE, fromSide(RIGHT)));
		final IBatchMerger merger = new BatchMerger(getMergerRegistry());
		merger.copyAllLeftToRight(rightOpaqueElementBodyChanges, new BasicMonitor());
	}

	private void applyLeftOpaqueElementBodyChangesToRight(Comparison comparison) {
		final EList<Diff> allDifferences = comparison.getDifferences();
		final Iterable<Diff> leftOpaqueElementBodyChanges = filter(allDifferences,
				and(IS_OPAQUE_ELEMENT_CHANGE, fromSide(LEFT)));
		final IBatchMerger merger = new BatchMerger(getMergerRegistry());
		merger.copyAllLeftToRight(leftOpaqueElementBodyChanges, new BasicMonitor());
	}

	private void assertNoRealConflict(Comparison comparison) {
		assertEquals(0, size(filter(comparison.getConflicts(), IS_REAL_CONFLICT)));
		assertEquals(0, size(filter(comparison.getConflicts(), CONCERNS_OPAQUE_ELEMENT_BODY_CHANGE)));
	}

	private void assertOneRealConflictOnOpaqueElementBodyChange(Comparison comparison) {
		assertEquals(1, size(filter(comparison.getConflicts(), IS_REAL_CONFLICT)));
		assertEquals(1, size(filter(comparison.getConflicts(),
				and(IS_REAL_CONFLICT, CONCERNS_OPAQUE_ELEMENT_BODY_CHANGE))));
	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

}

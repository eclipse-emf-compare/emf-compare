/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.stereotypes;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onFeature;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.valueIs;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.valueNameMatches;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.uml2.internal.ProfileApplicationChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeAttributeChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeReferenceChange;
import org.eclipse.emf.compare.uml2.tests.AbstractStaticProfileTest;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.stereotypes.data.static_.StaticStereotypeInputData;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.Test;

@SuppressWarnings("nls")
public class StaticStereotypeTest extends AbstractStaticProfileTest {

	private StaticStereotypeInputData input = new StaticStereotypeInputData();

	@Test
	public void testB10UseCase() throws IOException {
		final Resource left = input.getB1Left();
		final Resource right = input.getB1Right();

		final Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testMergeRtLB10UseCase() throws IOException {
		final Resource left = input.getB1Left();
		final Resource right = input.getB1Left();

		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testMergeLtRB10UseCase() throws IOException {
		final Resource left = input.getB1Left();
		final Resource right = input.getB1Left();

		testMergeLeftToRight(left, right, null);
	}

	@Test
	public void testB11UseCase() throws IOException {
		final Resource left = input.getB1Left();
		final Resource right = input.getB1Right();

		final Comparison comparison = compare(right, left);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testMergeRtLB11UseCase() throws IOException {
		final Resource left = input.getB1Left();
		final Resource right = input.getB1Left();

		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testMergeLtRB11UseCase() throws IOException {
		final Resource left = input.getB1Left();
		final Resource right = input.getB1Left();

		testMergeLeftToRight(right, left, null);
	}

	@Test
	public void testB10UseCase3way() throws IOException {
		final Resource left = input.getB1Left();
		final Resource right = input.getB1Right();

		final Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testMergeRtLB10UseCase3way() throws IOException {
		final Resource left = input.getB1Left();
		final Resource right = input.getB1Left();

		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testMergeLtRB10UseCase3way() throws IOException {
		final Resource left = input.getB1Left();
		final Resource right = input.getB1Left();

		testMergeLeftToRight(left, right, right);
	}

	@Test
	public void testB11UseCase3way() throws IOException {
		final Resource left = input.getB1Left();
		final Resource right = input.getB1Right();

		final Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testMergeRtLB11UseCase3way() throws IOException {
		final Resource left = input.getB1Left();
		final Resource right = input.getB1Left();

		testMergeRightToLeft(left, right, left);
	}

	@Test
	public void testMergeLtRB11UseCase3way() throws IOException {
		final Resource left = input.getB1Left();
		final Resource right = input.getB1Left();

		testMergeLeftToRight(left, right, left);
	}

	@Test
	public void testB20UseCase() throws IOException {
		final Resource left = input.getB2Left();
		final Resource right = input.getB2Right();

		final Comparison comparison = compare(left, right);
		testAB2(TestKind.ADD, comparison);
	}

	@Test
	public void testMergeRtLB20UseCase() throws IOException {
		final Resource left = input.getB2Left();
		final Resource right = input.getB2Right();

		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testMergeLtRB20UseCase() throws IOException {
		final Resource left = input.getB2Left();
		final Resource right = input.getB2Right();

		testMergeLeftToRight(left, right, null);
	}

	@Test
	public void testB21UseCase() throws IOException {
		final Resource left = input.getB2Left();
		final Resource right = input.getB2Right();

		final Comparison comparison = compare(right, left);
		testAB2(TestKind.DELETE, comparison);
	}

	@Test
	public void testMergeRtLB21UseCase() throws IOException {
		final Resource left = input.getB2Left();
		final Resource right = input.getB2Right();

		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testMergeLtRB21UseCase() throws IOException {
		final Resource left = input.getB2Left();
		final Resource right = input.getB2Right();

		testMergeLeftToRight(right, left, null);
	}

	@Test
	public void testB20UseCase3way() throws IOException {
		final Resource left = input.getB2Left();
		final Resource right = input.getB2Right();

		final Comparison comparison = compare(left, right, right);
		testAB2(TestKind.ADD, comparison);
	}

	@Test
	public void testMergeRtLB20UseCase3way() throws IOException {
		final Resource left = input.getB2Left();
		final Resource right = input.getB2Right();

		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testMergeLtRB20UseCase3way() throws IOException {
		final Resource left = input.getB2Left();
		final Resource right = input.getB2Right();

		testMergeLeftToRight(left, right, right);
	}

	@Test
	public void testB21UseCase3way() throws IOException {
		final Resource left = input.getB2Left();
		final Resource right = input.getB2Right();

		final Comparison comparison = compare(left, right, left);
		testAB2(TestKind.DELETE, comparison);
	}

	@Test
	public void testMergeRtLB21UseCase3way() throws IOException {
		final Resource left = input.getB2Left();
		final Resource right = input.getB2Right();

		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testMergeLtRB21UseCase3way() throws IOException {
		final Resource left = input.getB2Left();
		final Resource right = input.getB2Right();

		testMergeLeftToRight(left, right, left);
	}

	@Test
	public void testB30UseCase() throws IOException {
		final Resource left = input.getB3Left();
		final Resource right = input.getB3Right();

		final Comparison comparison = compare(left, right);
		testAB3(TestKind.ADD, comparison);
	}

	@Test
	public void testMergeRtLB30UseCase() throws IOException {
		final Resource left = input.getB3Left();
		final Resource right = input.getB3Right();

		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testMergeLtRB30UseCase() throws IOException {
		final Resource left = input.getB3Left();
		final Resource right = input.getB3Right();

		testMergeLeftToRight(left, right, null);
	}

	@Test
	public void testB31UseCase() throws IOException {
		final Resource left = input.getB3Left();
		final Resource right = input.getB3Right();

		final Comparison comparison = compare(right, left);
		testAB3(TestKind.DELETE, comparison);
	}

	@Test
	public void testMergeRtLB31UseCase() throws IOException {
		final Resource left = input.getB3Left();
		final Resource right = input.getB3Right();

		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testMergeLtRB31UseCase() throws IOException {
		final Resource left = input.getB3Left();
		final Resource right = input.getB3Right();

		testMergeLeftToRight(right, left, null);
	}

	@Test
	public void testB30UseCase3way() throws IOException {
		final Resource left = input.getB3Left();
		final Resource right = input.getB3Right();

		final Comparison comparison = compare(left, right, right);
		testAB3(TestKind.ADD, comparison);
	}

	@Test
	public void testMergeRtLB30UseCase3way() throws IOException {
		final Resource left = input.getB3Left();
		final Resource right = input.getB3Right();

		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testMergeLtRB30UseCase3way() throws IOException {
		final Resource left = input.getB3Left();
		final Resource right = input.getB3Right();

		testMergeLeftToRight(left, right, right);
	}

	@Test
	public void testB31UseCase3way() throws IOException {
		final Resource left = input.getB3Left();
		final Resource right = input.getB3Right();

		final Comparison comparison = compare(left, right, left);
		testAB3(TestKind.DELETE, comparison);
	}

	@Test
	public void testMergeRtLB31UseCase3way() throws IOException {
		final Resource left = input.getB3Left();
		final Resource right = input.getB3Right();

		testMergeRightToLeft(left, right, left);
	}

	@Test
	public void testMergeLtRB31UseCase3way() throws IOException {
		final Resource left = input.getB3Left();
		final Resource right = input.getB3Right();

		testMergeLeftToRight(left, right, left);
	}

	@Test
	public void testB40UseCase() throws IOException {
		final Resource left = input.getB4Left();
		final Resource right = input.getB4Right();

		final Comparison comparison = compare(left, right);
		testAB4(TestKind.ADD, comparison);

		testMergeRightToLeft(left, right, null);
		testMergeLeftToRight(left, right, null);
	}

	@Test
	public void testB41UseCase() throws IOException {
		final Resource left = input.getB4Left();
		final Resource right = input.getB4Right();

		final Comparison comparison = compare(right, left);
		testAB4(TestKind.DELETE, comparison);

		testMergeRightToLeft(right, left, null);
		testMergeLeftToRight(right, left, null);
	}

	@Test
	public void testB40UseCase3way() throws IOException {
		final Resource left = input.getB4Left();
		final Resource right = input.getB4Right();

		final Comparison comparison = compare(left, right, right);
		testAB4(TestKind.ADD, comparison);

		testMergeRightToLeft(left, right, right);
		testMergeLeftToRight(left, right, right);
	}

	@Test
	public void testB41UseCase3way() throws IOException {
		final Resource left = input.getB4Left();
		final Resource right = input.getB4Right();

		final Comparison comparison = compare(left, right, left);
		testAB4(TestKind.DELETE, comparison);

		testMergeRightToLeft(left, right, left);
		testMergeLeftToRight(left, right, left);
	}

	@Test
	public void testB50UseCase() throws IOException {
		final Resource left = input.getB5Left();
		final Resource right = input.getB5Right();

		final Comparison comparison = compare(left, right);
		testAB5(TestKind.ADD, comparison);

		testMergeRightToLeft(left, right, null);
		testMergeLeftToRight(left, right, null);
	}

	@Test
	public void testB51UseCase() throws IOException {
		final Resource left = input.getB5Left();
		final Resource right = input.getB5Right();

		final Comparison comparison = compare(right, left);
		testAB5(TestKind.DELETE, comparison);

		testMergeRightToLeft(right, left, null);
		testMergeLeftToRight(right, left, null);
	}

	@Test
	public void testB50UseCase3way() throws IOException {
		final Resource left = input.getB5Left();
		final Resource right = input.getB5Right();

		final Comparison comparison = compare(left, right, right);
		testAB5(TestKind.ADD, comparison);

		testMergeRightToLeft(left, right, right);
		testMergeLeftToRight(left, right, right);
	}

	@Test
	public void testB51UseCase3way() throws IOException {
		final Resource left = input.getB5Left();
		final Resource right = input.getB5Right();

		final Comparison comparison = compare(left, right, left);
		testAB5(TestKind.DELETE, comparison);

		testMergeRightToLeft(left, right, left);
		testMergeLeftToRight(left, right, left);
	}

	@Test
	public void testB60UseCase() throws IOException {
		final Resource left = input.getB6Left();
		final Resource right = input.getB6Right();

		final Comparison comparison = compare(left, right);
		testAB6(TestKind.ADD, comparison);

		testMergeRightToLeft(left, right, null);
		testMergeLeftToRight(left, right, null);
	}

	@Test
	public void testB61UseCase() throws IOException {
		final Resource left = input.getB6Left();
		final Resource right = input.getB6Right();

		final Comparison comparison = compare(right, left);
		testAB6(TestKind.DELETE, comparison);

		testMergeRightToLeft(right, left, null);
		testMergeLeftToRight(right, left, null);
	}

	@Test
	public void testB60UseCase3way() throws IOException {
		final Resource left = input.getB6Left();
		final Resource right = input.getB6Right();

		final Comparison comparison = compare(left, right, right);
		testAB6(TestKind.ADD, comparison);

		testMergeRightToLeft(left, right, right);
		testMergeLeftToRight(left, right, right);
	}

	@Test
	public void testB61UseCase3way() throws IOException {
		final Resource left = input.getB6Left();
		final Resource right = input.getB6Right();

		final Comparison comparison = compare(left, right, left);
		testAB6(TestKind.DELETE, comparison);

		testMergeRightToLeft(left, right, left);
		testMergeLeftToRight(left, right, left);
	}

	@Test
	public void testB70UseCase() throws IOException {
		final Resource left = input.getB7Left();
		final Resource right = input.getB7Right();

		final Comparison comparison = compare(left, right);
		testAB7(TestKind.ADD, comparison);

		testMergeRightToLeft(left, right, null);
		testMergeLeftToRight(left, right, null);
	}

	@Test
	public void testB71UseCase() throws IOException {
		final Resource left = input.getB7Left();
		final Resource right = input.getB7Right();

		final Comparison comparison = compare(right, left);
		testAB7(TestKind.DELETE, comparison);

		testMergeRightToLeft(right, left, null);
		testMergeLeftToRight(right, left, null);
	}

	@Test
	public void testB70UseCase3way() throws IOException {
		final Resource left = input.getB7Left();
		final Resource right = input.getB7Right();

		final Comparison comparison = compare(left, right, right);
		testAB7(TestKind.ADD, comparison);

		testMergeRightToLeft(left, right, right);
		testMergeLeftToRight(left, right, right);
	}

	@Test
	public void testB71UseCase3way() throws IOException {
		final Resource left = input.getB7Left();
		final Resource right = input.getB7Right();

		final Comparison comparison = compare(left, right, left);
		testAB7(TestKind.DELETE, comparison);

		testMergeRightToLeft(left, right, left);
		testMergeLeftToRight(left, right, left);
	}

	@Test
	public void testB80UseCase() throws IOException {
		final Resource left = input.getB8Left();
		final Resource right = input.getB8Right();

		final Comparison comparison = compare(left, right);
		testAB8(TestKind.ADD, comparison);

		testMergeRightToLeft(left, right, null);
		testMergeLeftToRight(left, right, null);
	}

	@Test
	public void testB81UseCase() throws IOException {
		final Resource left = input.getB8Left();
		final Resource right = input.getB8Right();

		final Comparison comparison = compare(right, left);
		testAB8(TestKind.DELETE, comparison);

		testMergeRightToLeft(right, left, null);
		testMergeLeftToRight(right, left, null);
	}

	@Test
	public void testB80UseCase3way() throws IOException {
		final Resource left = input.getB8Left();
		final Resource right = input.getB8Right();

		final Comparison comparison = compare(left, right, right);
		testAB8(TestKind.ADD, comparison);

		testMergeRightToLeft(left, right, right);
		testMergeLeftToRight(left, right, right);
	}

	@Test
	public void testB81UseCase3way() throws IOException {
		final Resource left = input.getB8Left();
		final Resource right = input.getB8Right();

		final Comparison comparison = compare(left, right, left);
		testAB8(TestKind.DELETE, comparison);

		testMergeRightToLeft(left, right, left);
		testMergeLeftToRight(left, right, left);
	}

	@Test
	public void testB90UseCase() throws IOException {
		final Resource left = input.getB9Left();
		final Resource right = input.getB9Right();

		final Comparison comparison = compare(left, right);
		testAB9(TestKind.ADD, comparison);

		testMergeRightToLeft(left, right, null);
		testMergeLeftToRight(left, right, null);
	}

	@Test
	public void testB91UseCase() throws IOException {
		final Resource left = input.getB9Left();
		final Resource right = input.getB9Right();

		final Comparison comparison = compare(right, left);
		testAB9(TestKind.DELETE, comparison);

		testMergeRightToLeft(right, left, null);
		testMergeLeftToRight(right, left, null);
	}

	@Test
	public void testB90UseCase3way() throws IOException {
		final Resource left = input.getB9Left();
		final Resource right = input.getB9Right();

		final Comparison comparison = compare(left, right, right);
		testAB9(TestKind.ADD, comparison);

		testMergeRightToLeft(left, right, right);
		testMergeLeftToRight(left, right, right);
	}

	@Test
	public void testB91UseCase3way() throws IOException {
		final Resource left = input.getB9Left();
		final Resource right = input.getB9Right();

		final Comparison comparison = compare(left, right, left);
		testAB9(TestKind.DELETE, comparison);

		testMergeRightToLeft(left, right, left);
		testMergeLeftToRight(left, right, left);
	}

	@Test
	public void testB100UseCase() throws IOException {
		final Resource left = input.getB10Left();
		final Resource right = input.getB10Right();

		final Comparison comparison = compare(left, right);
		testAB10(TestKind.ADD, comparison);

		testMergeRightToLeft(left, right, null);
		testMergeLeftToRight(left, right, null);
	}

	@Test
	public void testB101UseCase() throws IOException {
		final Resource left = input.getB10Left();
		final Resource right = input.getB10Right();

		final Comparison comparison = compare(right, left);
		testAB10(TestKind.DELETE, comparison);

		testMergeRightToLeft(right, left, null);
		testMergeLeftToRight(right, left, null);
	}

	@Test
	public void testB100UseCase3way() throws IOException {
		final Resource left = input.getB10Left();
		final Resource right = input.getB10Right();

		final Comparison comparison = compare(left, right, right);
		testAB10(TestKind.ADD, comparison);

		testMergeRightToLeft(left, right, right);
		testMergeLeftToRight(left, right, right);
	}

	@Test
	public void testB101UseCase3way() throws IOException {
		final Resource left = input.getB10Left();
		final Resource right = input.getB10Right();

		final Comparison comparison = compare(left, right, left);
		testAB10(TestKind.DELETE, comparison);

		testMergeRightToLeft(left, right, left);
		testMergeLeftToRight(left, right, left);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 6 differences
		assertEquals(8, differences.size());

		Predicate<? super Diff> addProfileApplicationDescription = null;
		Predicate<? super Diff> addAppliedProfileInProfileApplicationDescription = null;
		Predicate<? super Diff> addUMLAnnotationDescription = null;
		Predicate<? super Diff> addReferencesInUMLAnnotationDescription = null;
		Predicate<? super Diff> addStereotypeApplicationDescription = null;
		Predicate<? super Diff> addRefBaseClassDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addProfileApplicationDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.DELETE),
					onRealFeature(UMLPackage.Literals.PACKAGE__PROFILE_APPLICATION));

			// addAppliedProfileInProfileApplicationDescription = and(instanceOf(ReferenceChange.class),
			// ofKind(DifferenceKind.CHANGE),
			// onRealFeature(UMLPackage.Literals.PROFILE_APPLICATION__APPLIED_PROFILE),
			// not(isChangeAdd()));

			addUMLAnnotationDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.DELETE),
					onRealFeature(EcorePackage.Literals.EMODEL_ELEMENT__EANNOTATIONS));

			addReferencesInUMLAnnotationDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.DELETE),
					onRealFeature(EcorePackage.Literals.EANNOTATION__REFERENCES));
		} else {
			addProfileApplicationDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.ADD),
					onRealFeature(UMLPackage.Literals.PACKAGE__PROFILE_APPLICATION));

			// addAppliedProfileInProfileApplicationDescription = and(instanceOf(ReferenceChange.class),
			// ofKind(DifferenceKind.CHANGE),
			// onRealFeature(UMLPackage.Literals.PROFILE_APPLICATION__APPLIED_PROFILE), isChangeAdd());

			addUMLAnnotationDescription = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.ADD),
					onRealFeature(EcorePackage.Literals.EMODEL_ELEMENT__EANNOTATIONS));

			addReferencesInUMLAnnotationDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.ADD), onRealFeature(EcorePackage.Literals.EANNOTATION__REFERENCES));
		}

		addAppliedProfileInProfileApplicationDescription = and(instanceOf(ReferenceChange.class),
				ofKind(DifferenceKind.CHANGE), onFeature("appliedProfile"));

		addStereotypeApplicationDescription = instanceOf(ResourceAttachmentChange.class);
		addRefBaseClassDescription = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.CHANGE),
				onFeature("base_Class"));

		final Diff addProfileApplication = Iterators.find(differences.iterator(),
				addProfileApplicationDescription);
		final Diff addAppliedProfileInProfileApplication = Iterators.find(differences.iterator(),
				addAppliedProfileInProfileApplicationDescription);
		final Diff addUMLAnnotation = Iterators.find(differences.iterator(), addUMLAnnotationDescription);
		final Diff addReferencesInUMLAnnotation = Iterators.find(differences.iterator(),
				addReferencesInUMLAnnotationDescription);
		final Diff addStereotypeApplication = Iterators.find(differences.iterator(),
				addStereotypeApplicationDescription);
		final Diff addRefBaseClass = Iterators.find(differences.iterator(), addRefBaseClassDescription);

		assertNotNull(addProfileApplication);
		assertNotNull(addAppliedProfileInProfileApplication);
		assertNotNull(addUMLAnnotation);
		assertNotNull(addReferencesInUMLAnnotation);
		assertNotNull(addStereotypeApplication);
		assertNotNull(addRefBaseClass);

		// CHECK EXTENSION
		assertEquals(1, count(differences, instanceOf(ProfileApplicationChange.class)));
		assertEquals(1, count(differences, instanceOf(StereotypeApplicationChange.class)));
		Diff addUMLProfileApplication = null;
		Diff addUMLStereotypeApplication = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLProfileApplication = Iterators.find(differences.iterator(), and(
					instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.ADD)));
			addUMLStereotypeApplication = Iterators.find(differences.iterator(), and(
					instanceOf(StereotypeApplicationChange.class), ofKind(DifferenceKind.ADD)));
		} else {
			addUMLProfileApplication = Iterators.find(differences.iterator(), and(
					instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.DELETE)));
			addUMLStereotypeApplication = Iterators.find(differences.iterator(), and(
					instanceOf(StereotypeApplicationChange.class), ofKind(DifferenceKind.DELETE)));
		}
		assertNotNull(addUMLProfileApplication);
		assertEquals(4, addUMLProfileApplication.getRefinedBy().size());
		assertTrue(addUMLProfileApplication.getRefinedBy().contains(addReferencesInUMLAnnotation));
		assertTrue(addUMLProfileApplication.getRefinedBy().contains(addAppliedProfileInProfileApplication));
		assertTrue(addUMLProfileApplication.getRefinedBy().contains(addProfileApplication));
		assertTrue(addUMLProfileApplication.getRefinedBy().contains(addUMLAnnotation));

		assertNotNull(addUMLStereotypeApplication);
		assertEquals(2, addUMLStereotypeApplication.getRefinedBy().size());
		assertTrue(addUMLStereotypeApplication.getRefinedBy().contains(addStereotypeApplication));
		assertTrue(addUMLStereotypeApplication.getRefinedBy().contains(addRefBaseClass));

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {

			assertEquals(0, addProfileApplication.getRequires().size());

			assertEquals(1, addUMLAnnotation.getRequires().size());
			assertTrue(addUMLAnnotation.getRequires().contains(addProfileApplication));

			assertEquals(1, addReferencesInUMLAnnotation.getRequires().size());
			assertTrue(addReferencesInUMLAnnotation.getRequires().contains(addUMLAnnotation));

			assertEquals(0, addUMLProfileApplication.getRequires().size());

			assertEquals(1, addUMLStereotypeApplication.getRequires().size());
			assertTrue(addUMLStereotypeApplication.getRequires().contains(addUMLProfileApplication));

		} else {
			assertEquals(2, addProfileApplication.getRequires().size());
			assertTrue(addProfileApplication.getRequires().contains(addAppliedProfileInProfileApplication));
			assertTrue(addProfileApplication.getRequires().contains(addUMLAnnotation));

			assertEquals(1, addUMLAnnotation.getRequires().size());
			assertTrue(addUMLAnnotation.getRequires().contains(addReferencesInUMLAnnotation));

			assertEquals(0, addReferencesInUMLAnnotation.getRequires().size());

			assertEquals(1, addUMLProfileApplication.getRequires().size());
			assertTrue(addUMLProfileApplication.getRequires().contains(addUMLStereotypeApplication));

			assertEquals(0, addUMLStereotypeApplication.getRequires().size());

		}

		// CHECK EQUIVALENCE
		assertEquals(0, comparison.getEquivalences().size());

		testIntersections(comparison);

	}

	private void testAB2(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertEquals(2, differences.size());

		Predicate<? super Diff> addAttributeDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addAttributeDescription = and(instanceOf(AttributeChange.class), ofKind(DifferenceKind.DELETE),
					onFeature("manyValuedAttribute"));
		} else {
			addAttributeDescription = and(instanceOf(AttributeChange.class), ofKind(DifferenceKind.ADD),
					onFeature("manyValuedAttribute"));
		}

		final Diff addAttribute = Iterators.find(differences.iterator(), addAttributeDescription);
		assertNotNull(addAttribute);

		testIntersections(comparison);

	}

	private void testAB3(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		assertEquals(10, differences.size());

		Predicate<? super Diff> addProfileApplicationDescription = null;
		Predicate<? super Diff> addAppliedProfileInProfileApplicationDescription = null;
		Predicate<? super Diff> addUMLAnnotationDescription = null;
		Predicate<? super Diff> addReferencesInUMLAnnotationDescription = null;
		Predicate<? super Diff> addStereotypeApplicationDescription = null;
		Predicate<? super Diff> addModelDescription = null;
		Predicate<? super Diff> addClassDescription = null;
		Predicate<? super Diff> addRefBaseClassDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addProfileApplicationDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.DELETE),
					onRealFeature(UMLPackage.Literals.PACKAGE__PROFILE_APPLICATION));

			// addAppliedProfileInProfileApplicationDescription = and(instanceOf(ReferenceChange.class),
			// ofKind(DifferenceKind.CHANGE),
			// onRealFeature(UMLPackage.Literals.PROFILE_APPLICATION__APPLIED_PROFILE),
			// not(isChangeAdd()));

			addUMLAnnotationDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.DELETE),
					onRealFeature(EcorePackage.Literals.EMODEL_ELEMENT__EANNOTATIONS));

			addReferencesInUMLAnnotationDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.DELETE),
					onRealFeature(EcorePackage.Literals.EANNOTATION__REFERENCES));
			addModelDescription = removed("model.MyNiceModel");
			addClassDescription = removed("model.MyNiceModel.Class1");

		} else {
			addProfileApplicationDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.ADD),
					onRealFeature(UMLPackage.Literals.PACKAGE__PROFILE_APPLICATION));

			// addAppliedProfileInProfileApplicationDescription = and(instanceOf(ReferenceChange.class),
			// ofKind(DifferenceKind.CHANGE),
			// onRealFeature(UMLPackage.Literals.PROFILE_APPLICATION__APPLIED_PROFILE), isChangeAdd());

			addUMLAnnotationDescription = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.ADD),
					onRealFeature(EcorePackage.Literals.EMODEL_ELEMENT__EANNOTATIONS));

			addReferencesInUMLAnnotationDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.ADD), onRealFeature(EcorePackage.Literals.EANNOTATION__REFERENCES));
			addModelDescription = added("model.MyNiceModel");
			addClassDescription = added("model.MyNiceModel.Class1");
		}

		addAppliedProfileInProfileApplicationDescription = and(instanceOf(ReferenceChange.class),
				ofKind(DifferenceKind.CHANGE), onFeature("appliedProfile"));

		addStereotypeApplicationDescription = instanceOf(ResourceAttachmentChange.class);
		addRefBaseClassDescription = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.CHANGE),
				onFeature("base_Class"));

		final Diff addProfileApplication = Iterators.find(differences.iterator(),
				addProfileApplicationDescription);
		final Diff addAppliedProfileInProfileApplication = Iterators.find(differences.iterator(),
				addAppliedProfileInProfileApplicationDescription);
		final Diff addUMLAnnotation = Iterators.find(differences.iterator(), addUMLAnnotationDescription);
		final Diff addReferencesInUMLAnnotation = Iterators.find(differences.iterator(),
				addReferencesInUMLAnnotationDescription);
		final Diff addStereotypeApplication = Iterators.find(differences.iterator(),
				addStereotypeApplicationDescription);
		final Diff addModel = Iterators.find(differences.iterator(), addModelDescription);
		final Diff addClass = Iterators.find(differences.iterator(), addClassDescription);
		final Diff addRefBaseClass = Iterators.find(differences.iterator(), addRefBaseClassDescription);

		assertNotNull(addProfileApplication);
		assertNotNull(addAppliedProfileInProfileApplication);
		assertNotNull(addUMLAnnotation);
		assertNotNull(addReferencesInUMLAnnotation);
		assertNotNull(addStereotypeApplication);
		assertNotNull(addModel);
		assertNotNull(addClass);
		assertNotNull(addRefBaseClass);

		// CHECK EXTENSION
		assertEquals(1, count(differences, instanceOf(ProfileApplicationChange.class)));
		assertEquals(1, count(differences, instanceOf(StereotypeApplicationChange.class)));
		Diff addUMLProfileApplication = null;
		Diff addUMLStereotypeApplication = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLProfileApplication = Iterators.find(differences.iterator(), and(
					instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.ADD)));
			addUMLStereotypeApplication = Iterators.find(differences.iterator(), and(
					instanceOf(StereotypeApplicationChange.class), ofKind(DifferenceKind.ADD)));
		} else {
			addUMLProfileApplication = Iterators.find(differences.iterator(), and(
					instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.DELETE)));
			addUMLStereotypeApplication = Iterators.find(differences.iterator(), and(
					instanceOf(StereotypeApplicationChange.class), ofKind(DifferenceKind.DELETE)));
		}
		assertNotNull(addUMLProfileApplication);
		assertEquals(4, addUMLProfileApplication.getRefinedBy().size());
		assertTrue(addUMLProfileApplication.getRefinedBy().contains(addReferencesInUMLAnnotation));
		assertTrue(addUMLProfileApplication.getRefinedBy().contains(addAppliedProfileInProfileApplication));
		assertTrue(addUMLProfileApplication.getRefinedBy().contains(addProfileApplication));
		assertTrue(addUMLProfileApplication.getRefinedBy().contains(addUMLAnnotation));

		assertNotNull(addUMLStereotypeApplication);
		assertEquals(2, addUMLStereotypeApplication.getRefinedBy().size());
		assertTrue(addUMLStereotypeApplication.getRefinedBy().contains(addStereotypeApplication));
		assertTrue(addUMLStereotypeApplication.getRefinedBy().contains(addRefBaseClass));

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {

			assertEquals(1, addProfileApplication.getRequires().size());
			assertTrue(addProfileApplication.getRequires().contains(addModel));

			assertEquals(1, addUMLAnnotation.getRequires().size());
			assertTrue(addUMLAnnotation.getRequires().contains(addProfileApplication));

			assertEquals(1, addReferencesInUMLAnnotation.getRequires().size());
			assertTrue(addReferencesInUMLAnnotation.getRequires().contains(addUMLAnnotation));

			assertEquals(1, addUMLProfileApplication.getRequires().size());

			assertEquals(2, addUMLStereotypeApplication.getRequires().size());
			assertTrue(addUMLStereotypeApplication.getRequires().contains(addUMLProfileApplication));

		} else {
			assertEquals(2, addProfileApplication.getRequires().size());
			assertTrue(addProfileApplication.getRequires().contains(addAppliedProfileInProfileApplication));
			assertTrue(addProfileApplication.getRequires().contains(addUMLAnnotation));

			assertEquals(1, addUMLAnnotation.getRequires().size());
			assertTrue(addUMLAnnotation.getRequires().contains(addReferencesInUMLAnnotation));

			assertEquals(0, addReferencesInUMLAnnotation.getRequires().size());

			assertEquals(1, addUMLProfileApplication.getRequires().size());
			assertTrue(addUMLProfileApplication.getRequires().contains(addUMLStereotypeApplication));

			assertEquals(0, addUMLStereotypeApplication.getRequires().size());

		}

		// CHECK EQUIVALENCE
		assertEquals(0, comparison.getEquivalences().size());

		testIntersections(comparison);
	}

	private void testAB4(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 4 differences
		assertEquals(4, differences.size());

		Predicate<? super Diff> addStereotypeApplicationDescription = null;
		Predicate<? super Diff> addClassDescription = null;

		Diff addUMLStereotypeApplication = null;
		if (kind.equals(TestKind.DELETE)) {
			addClassDescription = removed("model.Class0");
			addUMLStereotypeApplication = Iterators.find(differences.iterator(), and(
					instanceOf(StereotypeApplicationChange.class), ofKind(DifferenceKind.DELETE)));
		} else {
			addClassDescription = added("model.Class0");
			addUMLStereotypeApplication = Iterators.find(differences.iterator(), and(
					instanceOf(StereotypeApplicationChange.class), ofKind(DifferenceKind.ADD)));
		}
		addStereotypeApplicationDescription = instanceOf(ResourceAttachmentChange.class);

		final Diff addClass = Iterators.find(differences.iterator(), addClassDescription);
		final Diff addStereotypeApplication = Iterators.find(differences.iterator(),
				addStereotypeApplicationDescription);
		assertNotNull(addClass);
		assertNotNull(addStereotypeApplication);
		assertNotNull(addUMLStereotypeApplication);

		testIntersections(comparison);

	}

	private void testAB5(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 4 differences
		assertEquals(4, differences.size());

		Predicate<? super Diff> changeAttributeDescription1 = null;
		Predicate<? super Diff> changeAttributeDescription2 = null;

		changeAttributeDescription1 = and(instanceOf(AttributeChange.class), ofKind(DifferenceKind.CHANGE),
				onFeature("xmlName"), valueIs("clazz"));
		changeAttributeDescription2 = and(instanceOf(AttributeChange.class), ofKind(DifferenceKind.CHANGE),
				onFeature("className"), valueIs("theEClassName"));

		final Diff changeAttribute1 = Iterators.find(differences.iterator(), changeAttributeDescription1);
		final Diff changeAttribute2 = Iterators.find(differences.iterator(), changeAttributeDescription2);
		final UnmodifiableIterator<Diff> changeUMLStereotypeProperties = Iterators.filter(differences
				.iterator(), and(instanceOf(StereotypeAttributeChange.class), ofKind(DifferenceKind.CHANGE)));

		assertTrue(changeUMLStereotypeProperties.hasNext());
		final Diff changeUMLStereotypeProperty1 = changeUMLStereotypeProperties.next();
		assertTrue(changeUMLStereotypeProperties.hasNext());
		final Diff changeUMLStereotypeProperty2 = changeUMLStereotypeProperties.next();

		assertNotNull(changeAttribute1);
		assertNotNull(changeAttribute2);
		assertNotNull(changeUMLStereotypeProperty1);
		assertNotNull(changeUMLStereotypeProperty2);

		assertEquals(1, changeUMLStereotypeProperty1.getRefinedBy().size());
		assertTrue(changeUMLStereotypeProperty1.getRefinedBy().contains(changeAttribute1));
		assertEquals(1, changeUMLStereotypeProperty2.getRefinedBy().size());
		assertTrue(changeUMLStereotypeProperty2.getRefinedBy().contains(changeAttribute2));

		testIntersections(comparison);

	}

	private void testAB6(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertEquals(2, differences.size());

		Predicate<? super Diff> addStereotypeRefDescription = null;
		Predicate<? super Diff> changeUMLStereotypeDescription = null;

		if (kind.equals(TestKind.ADD)) {
			addStereotypeRefDescription = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.ADD),
					onFeature("manyValuedReference"));
		} else {
			addStereotypeRefDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.DELETE), onFeature("manyValuedReference"));
		}
		changeUMLStereotypeDescription = and(instanceOf(StereotypeReferenceChange.class),
				ofKind(DifferenceKind.CHANGE));

		final Diff addStereotypeRef = Iterators.find(differences.iterator(), addStereotypeRefDescription);
		final Diff changeUMLStereotype = Iterators.find(differences.iterator(),
				changeUMLStereotypeDescription);

		assertNotNull(addStereotypeRef);
		assertNotNull(changeUMLStereotype);

		assertEquals(1, changeUMLStereotype.getRefinedBy().size());
		assertTrue(changeUMLStereotype.getRefinedBy().contains(addStereotypeRef));

		testIntersections(comparison);
	}

	private void testAB7(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertEquals(2, differences.size());

		Predicate<? super Diff> moveStereotypeRefDescription = null;
		Predicate<? super Diff> changeUMLStereotypeDescription = null;

		moveStereotypeRefDescription = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.MOVE),
				onFeature("manyValuedReference"));
		changeUMLStereotypeDescription = and(instanceOf(StereotypeReferenceChange.class),
				ofKind(DifferenceKind.CHANGE));

		final Diff moveStereotypeRef = Iterators.find(differences.iterator(), moveStereotypeRefDescription);
		final Diff changeUMLStereotype = Iterators.find(differences.iterator(),
				changeUMLStereotypeDescription);

		assertNotNull(moveStereotypeRef);
		assertNotNull(changeUMLStereotype);

		assertEquals(1, changeUMLStereotype.getRefinedBy().size());
		assertTrue(changeUMLStereotype.getRefinedBy().contains(moveStereotypeRef));

		testIntersections(comparison);
	}

	private void testAB8(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertEquals(2, differences.size());

		Predicate<? super Diff> changeStereotypeRefDescription = null;
		Predicate<? super Diff> changeUMLStereotypeDescription = null;

		if (kind.equals(TestKind.ADD)) {
			changeStereotypeRefDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.CHANGE), onFeature("singleValuedReference"),
					valueNameMatches("class0"));
		} else {
			changeStereotypeRefDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.CHANGE), onFeature("singleValuedReference"),
					valueNameMatches("class1"));
		}
		changeUMLStereotypeDescription = and(instanceOf(StereotypeReferenceChange.class),
				ofKind(DifferenceKind.CHANGE));

		final Diff changeStereotypeRef = Iterators.find(differences.iterator(),
				changeStereotypeRefDescription);
		final Diff changeUMLStereotype = Iterators.find(differences.iterator(),
				changeUMLStereotypeDescription);

		assertNotNull(changeStereotypeRef);
		assertNotNull(changeUMLStereotype);

		assertEquals(1, changeUMLStereotype.getRefinedBy().size());
		assertTrue(changeUMLStereotype.getRefinedBy().contains(changeStereotypeRef));

		testIntersections(comparison);
	}

	private void testAB9(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertEquals(2, differences.size());

		Predicate<? super Diff> changeStereotypeRefDescription = null;
		Predicate<? super Diff> changeUMLStereotypeDescription = null;

		changeStereotypeRefDescription = and(instanceOf(ReferenceChange.class),
				ofKind(DifferenceKind.CHANGE), onFeature("singleValuedReference"), valueNameMatches("class0"));

		changeUMLStereotypeDescription = and(instanceOf(StereotypeReferenceChange.class),
				ofKind(DifferenceKind.CHANGE));

		final Diff changeStereotypeRef = Iterators.find(differences.iterator(),
				changeStereotypeRefDescription);
		final Diff changeUMLStereotype = Iterators.find(differences.iterator(),
				changeUMLStereotypeDescription);

		assertNotNull(changeStereotypeRef);
		assertNotNull(changeUMLStereotype);

		assertEquals(1, changeUMLStereotype.getRefinedBy().size());
		assertTrue(changeUMLStereotype.getRefinedBy().contains(changeStereotypeRef));

		testIntersections(comparison);
	}

	private void testAB10(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertEquals(2, differences.size());

		Predicate<? super Diff> changeStereotypeRefDescription = null;
		Predicate<? super Diff> changeUMLStereotypeDescription = null;

		if (kind.equals(TestKind.ADD)) {
			changeStereotypeRefDescription = and(instanceOf(AttributeChange.class),
					ofKind(DifferenceKind.CHANGE), onFeature("singleValuedAttribute"), valueIs("myValue"));
		} else {
			changeStereotypeRefDescription = and(instanceOf(AttributeChange.class),
					ofKind(DifferenceKind.CHANGE), onFeature("singleValuedAttribute"), valueIs("oldValue"));
		}

		changeUMLStereotypeDescription = and(instanceOf(StereotypeAttributeChange.class),
				ofKind(DifferenceKind.CHANGE));

		final Diff changeStereotypeRef = Iterators.find(differences.iterator(),
				changeStereotypeRefDescription);
		final Diff changeUMLStereotype = Iterators.find(differences.iterator(),
				changeUMLStereotypeDescription);

		assertNotNull(changeStereotypeRef);
		assertNotNull(changeUMLStereotype);

		assertEquals(1, changeUMLStereotype.getRefinedBy().size());
		assertTrue(changeUMLStereotype.getRefinedBy().contains(changeStereotypeRef));

		testIntersections(comparison);

	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

}

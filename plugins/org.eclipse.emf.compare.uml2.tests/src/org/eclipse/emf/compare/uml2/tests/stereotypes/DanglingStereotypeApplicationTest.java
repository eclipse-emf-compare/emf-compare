/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.stereotypes;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasState;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.uml2.internal.DanglingStereotypeApplication;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLProfileTest;
import org.eclipse.emf.compare.uml2.tests.stereotypes.data.dangling.DanglingStereotypeApplicationInputData;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.UMLPlugin;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DanglingStereotypeApplicationTest extends AbstractUMLProfileTest {

	private DanglingStereotypeApplicationInputData input;

	@BeforeClass
	public static void fillRegistriesForStatic() {
		addProfilePathmap();
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			EPackage.Registry.INSTANCE.put(UML2CompareTestProfilePackage.eNS_URI,
					UML2CompareTestProfilePackage.eINSTANCE); // registers
			// against
			// EPackage.Registry
			// It is required to link the EPackage to the UML package of the UML Profile
			Map<String, URI> ePackageNsURIToProfileLocationMap = UMLPlugin
					.getEPackageNsURIToProfileLocationMap();
			ePackageNsURIToProfileLocationMap
					.put(UML2CompareTestProfilePackage.eNS_URI,
							URI.createURI("pathmap://UML_COMPARE_TESTS_PROFILE/uml2.compare.testprofile.profile.uml#_hZFTgIwkEeC_FYHMbTTxXw")); //$NON-NLS-1$
		}
	}

	@AfterClass
	public static void resetRegistriesForStatic() {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			UMLPlugin.getEPackageNsURIToProfileLocationMap().remove(UML2CompareTestProfilePackage.eNS_URI);
			EPackage.Registry.INSTANCE.remove(UML2CompareTestProfilePackage.eNS_URI);
		}
		resetProfilePathmap();
	}

	@Before
	@Override
	public void before() {
		super.before();
		input = new DanglingStereotypeApplicationInputData();
	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

	/**
	 * Case 1: Dangling stereotype with nothing on the other side.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testDetectDanglingstereotypeApplication_Case1() throws IOException {
		Comparison comparison = compare(input.getCase1Left(), input.getCase1Right(), null);
		// 1 ResourceAttachmentChange refining 1 DanglingStereotypeApplication (which is also a
		// ResourceAttachmentChange)
		assertEquals(2, size(comparison.getDifferences()));
		Iterable<ResourceAttachmentChange> racs = filter(comparison.getDifferences(),
				ResourceAttachmentChange.class);
		assertEquals(2, size(racs));
		Iterable<DanglingStereotypeApplication> danglings = filter(comparison.getDifferences(),
				DanglingStereotypeApplication.class);
		assertEquals(1, size(danglings));
	}

	/**
	 * Case 2: Dangling stereotype with class (without stereotype) on the other side
	 */
	@Test
	public void testDetectDanglingstereotypeApplication_Case2() throws IOException {
		Comparison comparison = compare(input.getCase2Left(), input.getCase2Right(), null);
		// 1 ResourceAttachmentChange refining 1 DanglingStereotypeApplication (which is also a
		// ResourceAttachmentChange)
		// 1 ReferenceChange (addition of a Class)
		assertEquals(3, size(comparison.getDifferences()));
		Iterable<ResourceAttachmentChange> racs = filter(comparison.getDifferences(),
				ResourceAttachmentChange.class);
		assertEquals(2, size(racs));
		Iterable<DanglingStereotypeApplication> danglings = filter(comparison.getDifferences(),
				DanglingStereotypeApplication.class);
		assertEquals(1, size(danglings));
	}

	/**
	 * Case 3: Dangling stereotype with class (with stereotype) on the other side In this case, no
	 * DanglingStereotypeApplication should be detected.
	 */
	@Test
	public void testDetectDanglingstereotypeApplication_Case3() throws IOException {
		Comparison comparison = compare(input.getCase3Left(), input.getCase3Right(), null);
		Iterable<ResourceAttachmentChange> racs = filter(comparison.getDifferences(),
				ResourceAttachmentChange.class);
		assertEquals(0, size(racs));
		Iterable<DanglingStereotypeApplication> danglings = filter(comparison.getDifferences(),
				DanglingStereotypeApplication.class);
		assertEquals(0, size(danglings));
	}

	/**
	 * Case 4 : a real Resource Attachment Change (a resource has been controlled on one side), but not a
	 * dangling stereotype application because there are no profiles involved in the comparison. So, In this
	 * case, no DanglingStereotypeApplication should be detected.
	 */
	@Test
	public void testDetectDanglingStereotypeApplication_Case4() throws IOException {
		Comparison comparison = compare(input.getCase4Left(), input.getCase4Right(), null);
		Iterable<ResourceAttachmentChange> racs = filter(comparison.getDifferences(),
				ResourceAttachmentChange.class);
		assertEquals(1, size(racs));
		Iterable<DanglingStereotypeApplication> danglings = filter(comparison.getDifferences(),
				DanglingStereotypeApplication.class);
		assertEquals(0, size(danglings));
	}

	/**
	 * Case 1: Dangling stereotype with nothing on the other side. A merge from left to right of the
	 * DanglingStereotypeApplication will lead to delete the dangling stereotype on the right model.
	 */
	@Test
	public void testMergeDanglingstereotypeApplication_Case1_LtR() throws IOException {
		Resource left = input.getCase1Left();
		Resource right = input.getCase1Right();
		assertEquals(1, left.getContents().size());
		assertEquals(2, right.getContents().size());
		Comparison comparison = compare(left, right, null);
		Iterable<DanglingStereotypeApplication> danglings = filter(comparison.getDifferences(),
				DanglingStereotypeApplication.class);
		for (Diff diff : danglings) {
			getMergerRegistry().getHighestRankingMerger(diff).copyLeftToRight(diff, null);
		}
		assertEquals(1, left.getContents().size());
		assertEquals(1, right.getContents().size());
		Iterable<Diff> unresolved = filter(comparison.getDifferences(), hasState(DifferenceState.UNRESOLVED));
		assertEquals(0, size(unresolved));
	}

	/**
	 * Case 1: Dangling stereotype with nothing on the other side.A merge from right to left of the
	 * DanglingStereotypeApplication will lead to have the dangling stereotype also on the left model.
	 */
	@Test
	public void testMergeDanglingstereotypeApplication_Case1_RtL() throws IOException {
		Resource left = input.getCase1Left();
		Resource right = input.getCase1Right();
		assertEquals(1, left.getContents().size());
		assertEquals(2, right.getContents().size());
		Comparison comparison = compare(left, right, null);
		Iterable<DanglingStereotypeApplication> danglings = filter(comparison.getDifferences(),
				DanglingStereotypeApplication.class);
		for (Diff diff : danglings) {
			getMergerRegistry().getHighestRankingMerger(diff).copyRightToLeft(diff, null);
		}
		assertEquals(2, left.getContents().size());
		assertEquals(2, right.getContents().size());
		Iterable<Diff> unresolved = filter(comparison.getDifferences(), hasState(DifferenceState.UNRESOLVED));
		assertEquals(0, size(unresolved));
	}

	/**
	 * Case 2: Dangling stereotype with class (without stereotype) on the other side. A merge from left to
	 * right of the DanglingStereotypeApplication will lead to delete the dangling stereotype on the right
	 * model.
	 */
	@Test
	public void testMergeDanglingstereotypeApplication_Case2_LtR() throws IOException {
		Resource left = input.getCase2Left();
		Resource right = input.getCase2Right();
		assertEquals(1, left.getContents().size());
		assertEquals(2, right.getContents().size());
		Comparison comparison = compare(left, right, null);
		Iterable<DanglingStereotypeApplication> danglings = filter(comparison.getDifferences(),
				DanglingStereotypeApplication.class);
		for (Diff diff : danglings) {
			getMergerRegistry().getHighestRankingMerger(diff).copyLeftToRight(diff, null);
		}
		assertEquals(1, left.getContents().size());
		assertEquals(1, right.getContents().size());
		// The class is not merged.
		Iterable<Diff> unresolved = filter(comparison.getDifferences(), hasState(DifferenceState.UNRESOLVED));
		assertEquals(1, size(unresolved));
	}

	/**
	 * Case 2: Dangling stereotype with class (without stereotype) on the other side. A merge from right to
	 * left of the DanglingStereotypeApplication will lead to have the dangling stereotype also on the left
	 * model.
	 */
	@Test
	public void testMergeDanglingstereotypeApplication_Case2_RtL() throws IOException {
		Resource left = input.getCase2Left();
		Resource right = input.getCase2Right();
		assertEquals(1, left.getContents().size());
		assertEquals(2, right.getContents().size());
		Comparison comparison = compare(left, right, null);
		Iterable<DanglingStereotypeApplication> danglings = filter(comparison.getDifferences(),
				DanglingStereotypeApplication.class);
		for (Diff diff : danglings) {
			getMergerRegistry().getHighestRankingMerger(diff).copyRightToLeft(diff, null);
		}
		assertEquals(2, left.getContents().size());
		assertEquals(2, right.getContents().size());
		// The class is not merged.
		Iterable<Diff> unresolved = filter(comparison.getDifferences(), hasState(DifferenceState.UNRESOLVED));
		assertEquals(1, size(unresolved));
	}

}

/*******************************************************************************
 * Copyright (c) 2014, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Christian W. Damus - bug 522080
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.stereotypes;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.stereotypes.data.conflict.StereotypeApplicationConflictInputData;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.uml2.uml.UMLPlugin;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests add-add scenarios for stereotype applications (same stereotype applied to same element on both
 * sides).
 */
@SuppressWarnings("nls")
public class StereotypeApplicationConflictTests extends AbstractStereotypedElementChangeTests {

	private StereotypeApplicationConflictInputData input;

	/**
	 * Initializes me.
	 */
	public StereotypeApplicationConflictTests() {
		super();
	}

	/**
	 * Tests add-add conflict for a stereotype that defines no attributes.
	 */
	@Test
	public void testApplyApplyPseudoConflictStereotypeNoAttributes() throws IOException {
		final Comparison comparison = compare(input.getA1Left(), input.getA1Right(), input.getA1Base());
		EList<Diff> differences = comparison.getDifferences();

		assertThat(differences, hasItem(hasDirectOrIndirectConflict(ConflictKind.PSEUDO)));
		assertThat(differences, not(hasItem(hasDirectOrIndirectConflict(ConflictKind.REAL))));
	}

	/**
	 * Tests add-add conflict for a stereotype that defines attributes where all attributes on both sides are
	 * equal.
	 */
	@Test
	public void testApplyApplyPseudoConflictStereotypeWithAttributes() throws IOException {
		final Comparison comparison = compare(input.getA2Left(), input.getA2Right(), input.getA2Base());
		EList<Diff> differences = comparison.getDifferences();

		assertThat(differences, hasItem(hasDirectOrIndirectConflict(ConflictKind.PSEUDO)));
		assertThat(differences, not(hasItem(hasDirectOrIndirectConflict(ConflictKind.REAL))));
	}

	/**
	 * Tests add-add conflict for a stereotype that defines attributes where the two sides have different
	 * attribute values.
	 */
	@Test
	public void testApplyApplyConflictStereotypeWithAttributes() throws IOException {
		final Comparison comparison = compare(input.getA3Left(), input.getA3Right(), input.getA3Base());
		EList<Diff> differences = comparison.getDifferences();

		assertThat(differences, hasItem(hasDirectOrIndirectConflict(ConflictKind.REAL)));
	}

	/**
	 * Tests add-add conflict for a stereotype that defines attributes where all attributes on both sides are
	 * equal.
	 */
	@Test
	public void testApplyApplyPseudoConflictStereotypeWithReferences() throws IOException {
		final Comparison comparison = compare(input.getA4Left(), input.getA4Right(), input.getA4Base());
		EList<Diff> differences = comparison.getDifferences();

		assertThat(differences, hasItem(hasDirectOrIndirectConflict(ConflictKind.PSEUDO)));
		assertThat(differences, not(hasItem(hasDirectOrIndirectConflict(ConflictKind.REAL))));
	}

	/**
	 * Tests add-add conflict for a stereotype that defines attributes where the two sides have different
	 * attribute values.
	 */
	@Test
	public void testApplyApplyConflictStereotypeWithReferences() throws IOException {
		final Comparison comparison = compare(input.getA5Left(), input.getA5Right(), input.getA5Base());
		EList<Diff> differences = comparison.getDifferences();

		assertThat(differences, hasItem(hasDirectOrIndirectConflict(ConflictKind.REAL)));
	}

	/**
	 * Tests delete-delete pseudoconflict for a stereotype that is unapplied on both sides.
	 */
	@Test
	public void testUnpplyUnapplyPseudoconflictStereotypeWithAttributes() throws IOException {
		final Comparison comparison = compare(input.getA6Left(), input.getA6Right(), input.getA6Base());
		EList<Diff> differences = comparison.getDifferences();

		assertThat(differences, hasItem(hasDirectOrIndirectConflict(ConflictKind.PSEUDO)));
	}

	//
	// Test framework
	//

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

	@BeforeClass
	public static void fillRegistriesForStatic() {
		beforeClass();
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			EPackage.Registry.INSTANCE.put(UML2CompareTestProfilePackage.eNS_URI,
					UML2CompareTestProfilePackage.eINSTANCE);

			// It is required to link the EPackage to the UML package of the UML Profile
			Map<String, URI> ePackageNsURIToProfileLocationMap = UMLPlugin
					.getEPackageNsURIToProfileLocationMap();
			ePackageNsURIToProfileLocationMap.put(UML2CompareTestProfilePackage.eNS_URI, URI.createURI(
					"pathmap://UML_COMPARE_TESTS_PROFILE/uml2.compare.testprofile.profile.uml#_hZFTgIwkEeC_FYHMbTTxXw"));
		}
	}

	@AfterClass
	public static void resetRegistriesForStatic() {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			UMLPlugin.getEPackageNsURIToProfileLocationMap().remove(UML2CompareTestProfilePackage.eNS_URI);
			EPackage.Registry.INSTANCE.remove(UML2CompareTestProfilePackage.eNS_URI);
		}
		afterClass();
	}

	@Before
	@Override
	public void before() {
		super.before();

		input = new StereotypeApplicationConflictInputData();
	}

	static Matcher<Diff> hasDirectOrIndirectConflict(final ConflictKind... ofKind) {
		return new TypeSafeMatcher<Diff>() {
			public void describeTo(Description description) {
				description.appendText("has ") //
						.appendValueList("", " or ", "", ofKind) //
						.appendText(" conflict");
			}

			@Override
			protected boolean matchesSafely(Diff item) {
				return EMFComparePredicates.hasDirectOrIndirectConflict(ofKind).apply(item);
			}
		};
	}
}

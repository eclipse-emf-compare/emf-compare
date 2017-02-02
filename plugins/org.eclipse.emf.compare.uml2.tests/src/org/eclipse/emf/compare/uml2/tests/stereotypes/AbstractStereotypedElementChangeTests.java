/*******************************************************************************
 * Copyright (c) 2014, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 501864
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.stereotypes;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static org.eclipse.emf.compare.merge.AbstractMerger.isInTerminalState;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Iterables;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.tests.postprocess.data.TestPostProcessor;
import org.eclipse.emf.compare.uml2.internal.StereotypedElementChange;
import org.eclipse.emf.compare.uml2.internal.postprocessor.StereotypedElementChangePostProcessor;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLProfileTest;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Abstract class used to test the merge of {@link StereotypedElementChange}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public abstract class AbstractStereotypedElementChangeTests extends AbstractUMLProfileTest {

	/**
	 * Each sublass of AbstractUMLTest have to call this method in a @BeforeClass annotated method. This allow
	 * each test to customize its context.
	 */
	public static void beforeClass() {
		addProfilePathmap();
	}

	/**
	 * Each sublass of AbstractUMLTest have to call this method in a @BeforeClass annotated method. This allow
	 * each test to safely delete its context.
	 */
	public static void afterClass() {
		resetProfilePathmap();
	}

	@Override
	protected void registerPostProcessors(
			org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor.Registry<String> postProcessorRegistry) {
		super.registerPostProcessors(postProcessorRegistry);
		postProcessorRegistry.put(StereotypedElementChangePostProcessor.class.getName(),
				new TestPostProcessor.TestPostProcessorDescriptor(
						Pattern.compile("http://www.eclipse.org/uml2/\\d\\.0\\.0/UML"), null, //$NON-NLS-1$
						new StereotypedElementChangePostProcessor(), 25));
	}

	/**
	 * Tests that no {@link StereotypedElementChange} is created when applying a stereotype on an existing
	 * element.
	 * <p>
	 * <h3>Inputs</h4>
	 * </p>
	 * <h3>Left model</h3>
	 * 
	 * <pre>
	 * &lt;Model&gt; aModel
	 * 	`-- &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Test
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * <b>ACliche [base &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Test]</b>
	 * </pre>
	 * 
	 * <h3>Right model</h3>
	 * 
	 * <pre>
	 * &lt;Model&gt; aModel
	 * 	`-- &lt;Class&gt; Test
	 * </pre>
	 * 
	 * @throws IOException
	 */
	protected void testApplyStereotypeOnExistingElement(Resource left, Resource right) throws IOException {

		final Comparison comparison = compare(left, right);

		EList<Diff> differences = comparison.getDifferences();
		Iterable<StereotypedElementChange> stereotypesChanges = getStereotypedElementChanges(differences,
				DifferenceKind.ADD);
		assertEquals(0, Iterables.size(stereotypesChanges));

	}

	/**
	 * Tests that no {@link StereotypedElementChange} is created when unapplying a stereotype.
	 * <p>
	 * <h3>Inputs</h4>
	 * </p>
	 * <h3>Left model</h3>
	 * 
	 * <pre>
	 * &lt;Model&gt; aModel
	 * 	`-- &lt;Class&gt; Test
	 * </pre>
	 * 
	 * <h3>Right model</h3>
	 * 
	 * <pre>
	 * &lt;Model&gt; aModel
	 * 	`-- &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Test
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * <b>ACliche [base &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Test]</b>
	 * </pre>
	 * 
	 * @throws IOException
	 */
	protected void testRemoveStereotypeOnExistingElement(Resource left, Resource right) throws IOException {

		final Comparison comparison = compare(left, right);

		EList<Diff> differences = comparison.getDifferences();
		Iterable<StereotypedElementChange> stereotypesChanges = getStereotypedElementChanges(differences,
				DifferenceKind.DELETE);
		assertEquals(0, Iterables.size(stereotypesChanges));

	}

	/**
	 * Tests basic use case:
	 * <ol>
	 * <li>Creates a {@link StereotypedElementChange} of kind {@link DifferenceKind#ADD}</li>
	 * <li>Merges it from left to right</li>
	 * </ol>
	 * <p>
	 * <h3>Inputs</h3>
	 * <h4>Left model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- <b>&lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class0</b>
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * <b>ACliche [base &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class0]</b>
	 * </pre>
	 * 
	 * <h4>Right model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * </pre>
	 * 
	 * <h4>Expected right model after merging</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class0
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * ACliche [base &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class0]
	 * </pre>
	 * </p>
	 * 
	 * @throws IOException
	 */
	protected void testAddStereotypedElementMergeLToR(Resource left, Resource right) throws IOException {

		final Comparison comparison = compare(left, right);

		// Checks comparison model
		final EList<Diff> differences = comparison.getDifferences();
		final StereotypedElementChange stereotypedElementChange = getStereotypedElementChange(differences,
				DifferenceKind.ADD, 2);

		assertAddedBaseElementDiff(differences, "model.Class0", stereotypedElementChange); //$NON-NLS-1$

		// Merges
		mergeLeftToRight(stereotypedElementChange);

		// Checks comparison model state
		for (Diff diff : differences) {
			assertSame(DifferenceState.MERGED, diff.getState());
		}

		// Checks right model content after merging
		assertEqualsM1(right);

		// Checks left model content after merging
		assertEqualsM1(left);

	}

	/**
	 * Tests basic use case:
	 * <ol>
	 * <li>Creates a {@link StereotypedElementChange} of kind {@link DifferenceKind#DELETE}</li>
	 * <li>Merges it from left to right</li>
	 * </ol>
	 * <p>
	 * <h3>Inputs</h3>
	 * <h4>Left model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * </pre>
	 * 
	 * <h4>Right model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`--<b> &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class0</b>
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * <b>ACliche [base &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class0]</b>
	 * </pre>
	 * 
	 * <h4>Expected right model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * </pre>
	 * </p>
	 * 
	 * @throws IOException
	 */
	protected void testDelStereotypedElementMergeLToR(Resource left, Resource right) throws IOException {

		final Comparison comparison = compare(left, right);

		// Checks comparison model
		final EList<Diff> differences = comparison.getDifferences();
		final StereotypedElementChange stereotypedElementChange = getStereotypedElementChange(differences,
				DifferenceKind.DELETE, 2);

		assertDeletedBaseElementDiff(differences, "model.Class0", stereotypedElementChange); //$NON-NLS-1$

		// Merges
		mergeLeftToRight(stereotypedElementChange);

		// Checks comparison model
		for (Diff diff : differences) {
			assertSame(DifferenceState.MERGED, diff.getState());
		}

		// Checks right model content after merging
		assertEqualsM2(right);

		// Checks left model content after merging
		assertEqualsM2(left);
	}

	/**
	 * Tests basic use case:
	 * <ol>
	 * <li>Creates a {@link StereotypedElementChange} of kind {@link DifferenceKind#ADD}</li>
	 * <li>Merges it from right to left</li>
	 * </ol>
	 * <p>
	 * <h3>Inputs</h3>
	 * <h4>Left model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- <b>&lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class0</b>
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * <b>ACliche [base &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class0]</b>
	 * </pre>
	 * 
	 * <h4>Right model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * </pre>
	 * 
	 * <h4>Expected left model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * </pre>
	 * </p>
	 * 
	 * @throws IOException
	 */
	protected void testAddStereotypedElementMergeRToL(Resource left, Resource right) throws IOException {

		final Comparison comparison = compare(left, right);

		// Checks comparison model
		EList<Diff> differences = comparison.getDifferences();
		final StereotypedElementChange stereotypedElementChange = getStereotypedElementChange(differences,
				DifferenceKind.ADD, 2);
		assertAddedBaseElementDiff(differences, "model.Class0", stereotypedElementChange); //$NON-NLS-1$

		// Merges
		mergeRightToLeft(stereotypedElementChange);

		// Checks comparison model after merging
		for (Diff diff : differences) {
			assertTrue(isInTerminalState(diff));
		}

		// Checks right model content after merging
		assertEqualsM2(right);

		// Checks left model content after merging
		assertEqualsM2(left);
	}

	/**
	 * Tests basic use case:
	 * <ol>
	 * <li>Creates a {@link StereotypedElementChange} of kind {@link DifferenceKind#DELETE}</li>
	 * <li>Merges it from right to left</li>
	 * </ol>
	 * <p>
	 * <h3>Inputs</h3>
	 * <h4>Left model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * </pre>
	 * 
	 * <h4>Right model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- <b>&lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class0</b>
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * <b>ACliche [base &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class0]</b>
	 * </pre>
	 * 
	 * <h4>Expected left model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class0
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * ACliche [base &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class0]
	 * </pre>
	 * </p>
	 * 
	 * @throws IOException
	 */
	protected void testDelStereotypedElementMergeRToL(Resource left, Resource right) throws IOException {

		final Comparison comparison = compare(left, right);

		// Checks model structure
		final EList<Diff> differences = comparison.getDifferences();
		final StereotypedElementChange stereotypedElementChange = getStereotypedElementChange(differences,
				DifferenceKind.DELETE, 2);

		assertDeletedBaseElementDiff(differences, "model.Class0", stereotypedElementChange); //$NON-NLS-1$

		// Merges
		mergeRightToLeft(stereotypedElementChange);
		for (Diff diff : differences) {
			assertTrue(isInTerminalState(diff));
		}

		// Checks right model content after merging
		assertEqualsM1(right);

		// Checks left model content after merging
		assertEqualsM1(left);
	}

	/**
	 * Tests advanced use case:
	 * <ol>
	 * <li>Creates a {@link StereotypedElementChange} of kind {@link DifferenceKind#ADD} with dependencies
	 * (requires creation of parent + profile application)</li>
	 * <li>Merges it from left to right</li>
	 * </ol>
	 * <p>
	 * <h3>Inputs</h3>
	 * <h4>Left model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- <b>&lt;Model&gt; MyNiceModel
	 * 		`-- &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class1
	 * 		`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 			`-- UML
	 * ACliche [base &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class1]</b>
	 * </pre>
	 * 
	 * <h4>Right model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * </pre>
	 * 
	 * <h4>Expected right model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;Model&gt; MyNiceModel
	 * 		`-- &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class1
	 * 		`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 			`-- UML
	 * ACliche [base &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class1]
	 * </pre>
	 * </p>
	 * 
	 * @throws IOException
	 */
	protected void testAddStereotypedElementLToR2(Resource left, Resource right) throws IOException {

		final Comparison comparison = compare(left, right);

		// Checks model structure
		final EList<Diff> differences = comparison.getDifferences();
		final StereotypedElementChange stereotypedElementChange = getStereotypedElementChange(differences,
				DifferenceKind.ADD, 2);

		assertAddedBaseElementDiff(differences, "model.MyNiceModel.Class1", stereotypedElementChange); //$NON-NLS-1$

		// Merge
		mergeLeftToRight(stereotypedElementChange);

		// Checks comparison model
		for (Diff diff : differences) {
			assertSame(DifferenceState.MERGED, diff.getState());
		}

		// Checks right model content after merging
		assertEqualsM3(right);

		// Checks left model content after merging
		assertEqualsM3(left);

	}

	/**
	 * Tests advanced use case:
	 * <ol>
	 * <li>Creates a {@link StereotypedElementChange} of kind {@link DifferenceKind#DELETE} with dependencies
	 * (requires creation of parent + profile application)</li>
	 * <li>Merges it from left to right</li>
	 * </ol>
	 * <p>
	 * <h3>Inputs</h3>
	 * <h4>Left model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * </pre>
	 * 
	 * <h4>Right model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;Model&gt; MyNiceModel
	 * 		`-- &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class1
	 * 		`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 			`-- UML
	 * ACliche [base &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class1]
	 * </pre>
	 * 
	 * <h4>Expected right model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;Model&gt; MyNiceModel
	 * 		`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 			`-- UML
	 * </pre>
	 * </p>
	 * 
	 * @throws IOException
	 */
	protected void testDelStereotypedElementLToR2(Resource left, Resource right) throws IOException {

		final Comparison comparison = compare(left, right);

		// Checks differences
		final EList<Diff> differences = comparison.getDifferences();
		final StereotypedElementChange stereotypedElementChange = getStereotypedElementChange(differences,
				DifferenceKind.DELETE, 2);

		assertDeletedBaseElementDiff(differences, "model.MyNiceModel.Class1", stereotypedElementChange); //$NON-NLS-1$

		// Merges
		mergeLeftToRight(stereotypedElementChange);

		// Checks comparison model
		Set<Diff> expectedMergeDifferences = getRefinedByClosure(stereotypedElementChange);
		for (Diff diff : differences) {
			final DifferenceState expectedDiffState;
			if (expectedMergeDifferences.contains(diff) || stereotypedElementChange.equals(diff)) {
				expectedDiffState = DifferenceState.MERGED;
			} else {
				expectedDiffState = DifferenceState.UNRESOLVED;
			}
			assertSame(expectedDiffState, diff.getState());
		}

		// Checks the content of the right model after merging
		// @formatter:off
		EList<EObject> contents = right.getContents();
		assertEquals(1, contents.size());
		Model model = (Model)contents.get(0);
		assertEquals(1, model.getPackagedElements().size());
		Model subModel = (Model)model.getPackagedElements().get(0);
		assertEquals(0, subModel.getPackagedElements().size());
		assertEquals(1, subModel.getAppliedProfiles().size());
		// @formatter:on

		// Checks the content of the left model after merging
		assertEqualsM6(left);
	}

	/**
	 * Tests advanced use case:
	 * <ol>
	 * <li>Creates a {@link StereotypedElementChange} of kind {@link DifferenceKind#ADD} with dependencies
	 * (requires creation of parent + profile application)</li>
	 * <li>Merges it from right to left</li>
	 * </ol>
	 * <p>
	 * <p>
	 * <h3>Inputs</h3>
	 * <h4>Left model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`--<b> &lt;Model&gt; MyNiceModel
	 * 		`-- &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class1
	 * 		`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 			`-- UML
	 * ACliche [base &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class1]</b>
	 * </pre>
	 * 
	 * <h4>Right model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * </pre>
	 * 
	 * <h4>Expected left model</h4>
	 * 
	 * <pre>
	 *  &lt;Model&gt; model
	 * 	`--<b> &lt;Model&gt; MyNiceModel
	 * 		`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 			`-- UML
	 * </pre>
	 * </p>
	 * 
	 * @throws IOException
	 */
	protected void testAddStereotypedElementRToL2(Resource left, Resource right) throws IOException {

		final Comparison comparison = compare(left, right);

		// Checks differences
		final EList<Diff> differences = comparison.getDifferences();
		final StereotypedElementChange stereotypedElementChange = getStereotypedElementChange(differences,
				DifferenceKind.ADD, 2);

		assertAddedBaseElementDiff(differences, "model.MyNiceModel.Class1", stereotypedElementChange); //$NON-NLS-1$

		// Merges
		mergeRightToLeft(stereotypedElementChange);

		// Checks comparison model after merging
		Set<Diff> expectedMergeDifferences = getRefinedByClosure(stereotypedElementChange);
		for (Diff diff : differences) {
			final DifferenceState expectedDiffState;
			if (expectedMergeDifferences.contains(diff) || stereotypedElementChange.equals(diff)) {
				expectedDiffState = DifferenceState.DISCARDED;
			} else {
				expectedDiffState = DifferenceState.UNRESOLVED;
			}
			assertSame(expectedDiffState, diff.getState());
		}

		// Checks left model content after merging
		// @formatter:off
		EList<EObject> contents = left.getContents();
		assertEquals(1, contents.size());
		Model model = (Model)contents.get(0);
		assertEquals(1, model.getPackagedElements().size());
		Model subModel = (Model)model.getPackagedElements().get(0);
		assertEquals(0, subModel.getPackagedElements().size());
		assertEquals(1, subModel.getAppliedProfiles().size());
		// @formatter:on

		// Checks right model content after merging
		assertEqualsM6(right);
	}

	/**
	 * Tests advanced use case:
	 * <ol>
	 * <li>Creates a {@link StereotypedElementChange} of kind {@link DifferenceKind#DELL} with dependencies
	 * </li>
	 * <li>Merges it from right to left</li>
	 * </ol>
	 * <p>
	 * <h3>Inputs</h3>
	 * <h4>Left model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * </pre>
	 * 
	 * <h4>Right model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`--<b> &lt;Model&gt; MyNiceModel
	 * 		`-- &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class1
	 * 		`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 			`-- UML
	 * ACliche [base &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class1]</b>
	 * </pre>
	 * 
	 * <h4>Expected left model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;Model&gt; MyNiceModel
	 * 		`-- &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class1
	 * 		`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 			`-- UML
	 * ACliche [base &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class1]
	 * </pre>
	 * </p>
	 * 
	 * @throws IOException
	 */
	protected void testDelStereotypedElementRToL2(Resource left, Resource right) throws IOException {

		final Comparison comparison = compare(left, right);

		// Checks model structure
		final EList<Diff> differences = comparison.getDifferences();
		final StereotypedElementChange stereotypedElementChange = getStereotypedElementChange(differences,
				DifferenceKind.DELETE, 2);

		assertDeletedBaseElementDiff(differences, "model.MyNiceModel.Class1", stereotypedElementChange); //$NON-NLS-1$
		// Merges
		mergeRightToLeft(stereotypedElementChange);

		// Check comparison model
		// Everything should be merged
		for (Diff diff : differences) {
			assertTrue(isInTerminalState(diff));
		}

		// Checks left model content after merging
		assertEqualsM3(left);

		// Checks right model content after merging
		assertEqualsM3(right);

	}

	/**
	 * Tests to merge a {@link StereotypedElementChange} of kind ADD with 2 stereotypes being applied from
	 * left to right.
	 * <p>
	 * <h3>Inputs</h3>
	 * <h4>Left model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`--<b> &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0</b>
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * <b>ACliche [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0]</b>
	 * <b>ACliche3 [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0]</b>
	 * </pre>
	 * 
	 * <h4>Right model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * </pre>
	 * 
	 * <h4>Expected right model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * ACliche [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0]
	 * ACliche3 [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0]
	 * </pre>
	 * </p>
	 * 
	 * @param left
	 * @param right
	 */
	protected void testAddMultipleStereotypesLToR(Resource left, Resource right) {
		final Comparison comparison = compare(left, right);

		// Checks differences
		final EList<Diff> differences = comparison.getDifferences();
		final StereotypedElementChange stereotypedElementChange = getStereotypedElementChange(differences,
				DifferenceKind.ADD, 3);

		assertAddedBaseElementDiff(differences, "model.Class0", stereotypedElementChange); //$NON-NLS-1$

		// Merges
		mergeLeftToRight(stereotypedElementChange);

		// Checks comparison model after merging
		for (Diff diff : differences) {
			assertSame(DifferenceState.MERGED, diff.getState());
		}

		// Checks right model content after merging
		assertEqualsM7(right);

		// Checks left model content after merging
		assertEqualsM7(left);

	}

	/**
	 * Tests to merge of a {@link StereotypedElementChange} of kind ADD with 2 stereotypes being applied from
	 * right to left.
	 * <p>
	 * <h3>Inputs</h3>
	 * <h4>Left model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- <b>&lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0</b>
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * <b>ACliche [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0]</b>
	 * <b>ACliche3 [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0]</b>
	 * </pre>
	 * 
	 * <h4>Right model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * </pre>
	 * 
	 * <h4>Expected left model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * </pre>
	 * 
	 * @param left
	 * @param right
	 */
	protected void testAddMultipleStereotypesRToL(Resource left, Resource right) {
		final Comparison comparison = compare(left, right);

		// Checks differences
		final EList<Diff> differences = comparison.getDifferences();
		final StereotypedElementChange stereotypedElementChange = getStereotypedElementChange(differences,
				DifferenceKind.ADD, 3);

		assertAddedBaseElementDiff(differences, "model.Class0", stereotypedElementChange); //$NON-NLS-1$

		// Merges
		mergeRightToLeft(stereotypedElementChange);

		// Check comparison model
		for (Diff diff : differences) {
			assertTrue(isInTerminalState(diff));
		}

		// Checks left model content after merging
		assertEqualsM2(left);

		// Checks right model content after merging
		assertEqualsM2(right);

	}

	/**
	 * Tests to merge a {@link StereotypedElementChange} of kind DELETE in conflict with another diff from
	 * right to left.
	 * <p>
	 * <h3>Inputs</h3>
	 * <h4>Left model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- <b>&lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0_newName</b>
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * <b>ACliche [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0_newName]</b>
	 * <b>ACliche3 [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0_newName]</b>
	 * </pre>
	 * 
	 * <h4>Right model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * </pre>
	 * 
	 * <h4>Ancestor model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * ACliche [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0]
	 * ACliche3 [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0]
	 * </pre>
	 * 
	 * <h4>Expected left model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * </pre>
	 * </p>
	 * 
	 * @param left
	 * @param right
	 * @param origin
	 */
	protected void testDelConflictRToL(Resource left, Resource right, Resource origin) {
		final Comparison comparison = compare(left, right, origin);

		// Checks differences
		final EList<Diff> differences = comparison.getDifferences();
		final StereotypedElementChange stereotypedElementChange = getStereotypedElementChange(differences,
				DifferenceKind.DELETE, 3);

		final ReferenceChange baseDiff = assertDeletedBaseElementDiff(differences, "model.Class0", //$NON-NLS-1$
				stereotypedElementChange);

		// the stereotype change itself is not in a conflict
		assertNull(stereotypedElementChange.getConflict());

		// but one of its refining diffs is in exactly one conflict
		final Set<Conflict> conflicts = getConflictsOfRefiningDiffs(stereotypedElementChange);
		final Conflict conflict = Iterables.getOnlyElement(conflicts);

		assertNotNull(conflict);
		assertEquals(2, conflict.getDifferences().size());

		final EList<Diff> leftDifferences = conflict.getLeftDifferences();
		assertEquals(1, leftDifferences.size());

		final Diff leftDiff = leftDifferences.get(0);
		assertTrue(leftDiff instanceof AttributeChange);
		assertEquals(1, conflict.getRightDifferences().size());
		assertTrue(conflict.getRightDifferences().contains(baseDiff));

		// Merges
		mergeRightToLeft(stereotypedElementChange);

		for (Diff diff : differences) {
			assertTrue(isInTerminalState(diff));
		}
		// Checks left model content after merging
		assertEqualsM2(left);

		// Checks right model content after merging
		assertEqualsM2(right);

	}

	/**
	 * Tests to merge a {@link StereotypedElementChange} of kind DELETE in conflict with another diff from
	 * left to right.
	 * <p>
	 * <h3>Inputs</h3>
	 * <h4>Left model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- <b>&lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0_newName</b>
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * <b>ACliche [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0_newName]</b>
	 * <b>ACliche3 [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0_newName]</b>
	 * </pre>
	 * 
	 * <h4>Right model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * </pre>
	 * 
	 * <h4>Ancestor model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * ACliche [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0]
	 * ACliche3 [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0]
	 * </pre>
	 * 
	 * <h4>Expected right model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0_newName
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * ACliche [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0_newName]
	 * ACliche3 [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0_newName]
	 * </pre>
	 * </p>
	 * 
	 * @param left
	 * @param right
	 * @param origin
	 */
	protected void testAbstractDelConflictLToR(Resource left, Resource right, Resource origin) {
		final Comparison comparison = compare(left, right, origin);

		// Checks model structure
		final EList<Diff> differences = comparison.getDifferences();
		final StereotypedElementChange stereotypedElementChange = getStereotypedElementChange(differences,
				DifferenceKind.DELETE, 3);

		final ReferenceChange baseDiff = assertDeletedBaseElementDiff(differences, "model.Class0", //$NON-NLS-1$
				stereotypedElementChange);

		// the stereotype change itself is not in a conflict
		assertNull(stereotypedElementChange.getConflict());

		// but one of its refining diffs is in exactly one conflict
		final Set<Conflict> conflicts = getConflictsOfRefiningDiffs(stereotypedElementChange);
		final Conflict conflict = Iterables.getOnlyElement(conflicts);
		assertEquals(2, conflict.getDifferences().size());

		final EList<Diff> leftDifferences = conflict.getLeftDifferences();
		assertEquals(1, leftDifferences.size());

		final Diff leftConflictDiff = leftDifferences.get(0);

		assertTrue(leftConflictDiff instanceof AttributeChange);
		assertEquals(1, conflict.getRightDifferences().size());
		assertTrue(conflict.getRightDifferences().contains(baseDiff));
		// Merges
		mergeLeftToRight(stereotypedElementChange);

		for (Diff diff : differences) {
			if (leftConflictDiff.equals(diff)) {
				assertSame(DifferenceState.UNRESOLVED, diff.getState());
			} else {
				assertSame(DifferenceState.DISCARDED, diff.getState());
			}
		}

		// Checks right model content after merging
		assertEqualsM4(right);

		// Checks left model content after merging
		assertEqualsM5(left);

	}

	/**
	 * Returns the conflicts of the refining diffs of the given {@code refinedDiff}.
	 * 
	 * @param refinedDiff
	 *            The refined diff to get the conflicts from.
	 * @return the list of conflicts.
	 */
	private Set<Conflict> getConflictsOfRefiningDiffs(Diff refinedDiff) {
		Builder<Conflict> builder = ImmutableSet.builder();
		for (Diff refiningDiff : refinedDiff.getRefinedBy()) {
			if (refiningDiff.getConflict() != null) {
				builder.add(refiningDiff.getConflict());
			}
		}
		return builder.build();
	}

	/**
	 * Checks that the input has the same structure than described bellow:
	 * <p>
	 * <h4>Expected model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class0
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * ACliche [base &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class0]
	 * </pre>
	 * </p>
	 * 
	 * @param input
	 */
	private void assertEqualsM1(final Resource input) {
		// @formatter:off
		EList<EObject> contents = input.getContents();
		assertEquals(2, contents.size());
		Model model = (Model)contents.get(0);
		assertEquals(1, model.getPackagedElements().size());
		Class clazz = (Class)model.getPackagedElements().get(0);
		assertEquals(1, clazz.getAppliedStereotypes().size());
		assertEquals(1, model.getAppliedProfiles().size());
		EObject stereotypeApplication = contents.get(1);
		assertSame(clazz, UMLUtil.getBaseElement(stereotypeApplication));
		// @formatter:on
	}

	/**
	 * Checks that the input has the same structure than described bellow:
	 * <p>
	 * <h4>Expected model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * </pre>
	 * </p>
	 * 
	 * @param input
	 */
	private void assertEqualsM2(final Resource input) {
		// @formatter:off
		EList<EObject> contents = input.getContents();
		assertEquals(1, contents.size());
		Model model = (Model)contents.get(0);
		assertEquals(0, model.getPackagedElements().size());
		assertEquals(1, model.getAppliedProfiles().size());
		// @formatter:on
	}

	/**
	 * Checks that the input has the same structure than described bellow:
	 * <p>
	 * <h4>Expected model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;Model&gt; MyNiceModel
	 * 		`-- &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class1
	 * 		`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 			`-- UML
	 * ACliche [base &lt;&lt;ACliche&gt;&gt; &lt;Class&gt; Class1]
	 * </pre>
	 * </p>
	 * 
	 * @param input
	 */
	private void assertEqualsM3(Resource input) {
		// @formatter:off
		EList<EObject> contents = input.getContents();
		assertEquals(2, contents.size());
		Model model = (Model)contents.get(0);
		assertEquals(1, model.getPackagedElements().size());
		Model subModel = (Model)model.getPackagedElements().get(0);
		assertEquals(1, subModel.getPackagedElements().size());
		Class clazz = (Class)subModel.getPackagedElements().get(0);
		assertEquals(1, clazz.getAppliedStereotypes().size());
		assertEquals(1, subModel.getAppliedProfiles().size());
		EObject stereotypeApplication = contents.get(1);
		assertSame(clazz, UMLUtil.getBaseElement(stereotypeApplication));
		// @formatter:on
	}

	/**
	 * Checks that the input has the same structure than described bellow:
	 * <p>
	 * <h4>Expected model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0_newName
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * ACliche [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0_newName]
	 * ACliche3 [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0_newName]
	 * </pre>
	 * </p>
	 * 
	 * @param input
	 */
	private void assertEqualsM4(Resource right) {
		// @formatter:off
		EList<EObject> contents = right.getContents();
		assertEquals(3, contents.size());
		Model model = (Model)contents.get(0);
		assertEquals(1, model.getPackagedElements().size());
		Class clazz = (Class)model.getPackagedElements().get(0);
		assertEquals(2, clazz.getAppliedStereotypes().size());
		assertEquals("Class0", clazz.getName()); //$NON-NLS-1$
		assertEquals(1, model.getAppliedProfiles().size());
		EObject stereotypeApplication = contents.get(1);
		assertSame(clazz, UMLUtil.getBaseElement(stereotypeApplication));
		EObject stereotypeApplication2 = contents.get(2);
		assertSame(clazz, UMLUtil.getBaseElement(stereotypeApplication2));
		// @formatter:on
	}

	/**
	 * Checks that the input has the same structure than described bellow:
	 * <p>
	 * <h4>Expected model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0_newName
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * ACliche [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0_newName]
	 * ACliche3 [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0_newName]
	 * </pre>
	 * </p>
	 * 
	 * @param input
	 */
	private void assertEqualsM5(Resource right) {
		// @formatter:off
		EList<EObject> contents = right.getContents();
		assertEquals(3, contents.size());
		Model model = (Model)contents.get(0);
		assertEquals(1, model.getPackagedElements().size());
		Class clazz = (Class)model.getPackagedElements().get(0);
		assertEquals(2, clazz.getAppliedStereotypes().size());
		assertEquals("Class0_newName", clazz.getName()); //$NON-NLS-1$
		assertEquals(1, model.getAppliedProfiles().size());
		EObject stereotypeApplication = contents.get(1);
		assertSame(clazz, UMLUtil.getBaseElement(stereotypeApplication));
		EObject stereotypeApplication2 = contents.get(2);
		assertSame(clazz, UMLUtil.getBaseElement(stereotypeApplication2));
		// @formatter:on
	}

	/**
	 * Checks that the input has the same structure than described bellow:
	 * <p>
	 * <h4>Expected model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * </pre>
	 * </p>
	 * 
	 * @param input
	 */
	private void assertEqualsM6(Resource input) {
		// @formatter:off
		EList<EObject> leftContent = input.getContents();
		assertEquals(1, leftContent.size());
		Model leftModel = (Model)leftContent.get(0);
		assertEquals(0, leftModel.getPackagedElements().size());
		assertEquals(0, leftModel.getAppliedProfiles().size());
		// @formatter:on
	}

	/**
	 * Checks that the input has the same structure than described bellow:
	 * <p>
	 * <h4>Expected model</h4>
	 * 
	 * <pre>
	 * &lt;Model&gt; model
	 * 	`-- &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0
	 * 	`-- &lt;Profile Application&gt; UML2CompareTestProfile
	 * 		`-- UML
	 * ACliche [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0]
	 * ACliche3 [base &lt;&lt;ACliche, ACliche3&gt;&gt; &lt;Class&gt; Class0]
	 * </pre>
	 * </p>
	 * 
	 * @param input
	 */
	private void assertEqualsM7(Resource inputs) {
		// @formatter:off
		EList<EObject> contents = inputs.getContents();
		assertEquals(3, contents.size());
		Model model = (Model)contents.get(0);
		assertEquals(1, model.getPackagedElements().size());
		Class clazz = (Class)model.getPackagedElements().get(0);
		assertEquals(2, clazz.getAppliedStereotypes().size());
		assertEquals(1, model.getAppliedProfiles().size());
		EObject stereotypeApplication = contents.get(1);
		assertSame(clazz, UMLUtil.getBaseElement(stereotypeApplication));
		EObject stereotypeApplication2 = contents.get(2);
		assertSame(clazz, UMLUtil.getBaseElement(stereotypeApplication2));
		// @formatter:on
	}

	private StereotypedElementChange getStereotypedElementChange(final EList<Diff> differences,
			DifferenceKind diffKind, int expectedRefine) {
		Iterable<StereotypedElementChange> stereotypesChanges = getStereotypedElementChanges(differences,
				diffKind);
		assertEquals(1, Iterables.size(stereotypesChanges));
		final StereotypedElementChange stereotypedElementChange = stereotypesChanges.iterator().next();
		assertSame(diffKind, stereotypedElementChange.getKind());
		assertEquals(expectedRefine, stereotypedElementChange.getRefinedBy().size());
		return stereotypedElementChange;
	}

	/**
	 * Gets the closure of the refined by elements from a starting diff.
	 * 
	 * @param startingDiff
	 * @return
	 */
	private Set<Diff> getRefinedByClosure(Diff startingDiff) {
		Set<Diff> result = new HashSet<Diff>();
		for (Diff refinedByDiff : startingDiff.getRefinedBy()) {
			if (!result.contains(refinedByDiff)) {
				getRefinedByClosure(refinedByDiff, result);
			}
		}
		return result;
	}

	private void getRefinedByClosure(Diff d, Set<Diff> result) {
		result.add(d);
		for (Diff refinedByDiff : d.getRefinedBy()) {
			if (!result.contains(refinedByDiff)) {
				getRefinedByClosure(refinedByDiff, result);
			}
		}
	}

	protected void mergeLeftToRight(Diff difference) {
		BatchMerger merger = new BatchMerger(getMergerRegistry());
		merger.copyAllLeftToRight(Collections.singleton(difference), new BasicMonitor());
	}

	protected void mergeRightToLeft(Diff difference) {
		BatchMerger merger = new BatchMerger(getMergerRegistry());
		merger.copyAllRightToLeft(Collections.singleton(difference), new BasicMonitor());
	}

	private ReferenceChange assertAddedBaseElementDiff(Iterable<Diff> differences, String qualifiedName,
			StereotypedElementChange stereotypedElementChange) {
		ReferenceChange referenceChange = (ReferenceChange)Iterables.find(differences, added(qualifiedName));
		assertTrue(stereotypedElementChange.getRefinedBy().contains(referenceChange));
		assertSame(referenceChange.getValue(), stereotypedElementChange.getDiscriminant());
		return referenceChange;
	}

	private ReferenceChange assertDeletedBaseElementDiff(Iterable<Diff> differences, String qualifiedName,
			StereotypedElementChange stereotypedElementChange) {
		ReferenceChange referenceChange = (ReferenceChange)Iterables.find(differences,
				removed(qualifiedName));
		assertTrue(stereotypedElementChange.getRefinedBy().contains(referenceChange));
		assertSame(referenceChange.getValue(), stereotypedElementChange.getDiscriminant());
		return referenceChange;
	}

	/**
	 * Gets the {@link StereotypedElementChange}s contained in the input differences.
	 * 
	 * @param differences
	 *            Input differences.
	 * @param diffKind
	 *            Kind of difference you are looking for.
	 * @return
	 */
	private Iterable<StereotypedElementChange> getStereotypedElementChanges(Iterable<Diff> differences,
			DifferenceKind diffKind) {
		final Predicate<Diff> selectingPredicate = and(instanceOf(StereotypedElementChange.class),
				ofKind(diffKind));
		return Iterables.transform(Iterables.filter(differences, selectingPredicate),
				new Function<Diff, StereotypedElementChange>() {

					public StereotypedElementChange apply(Diff input) {
						return (StereotypedElementChange)input;
					}
				});
	}

}

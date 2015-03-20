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
package org.eclipse.emf.compare.diagram.papyrus.tests.merge;

import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Iterables.size;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;

import java.io.IOException;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diagram.papyrus.internal.PapyrusDiagramPostProcessor;
import org.eclipse.emf.compare.diagram.papyrus.tests.AbstractTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.compare.diagram.papyrus.tests.merge.data.AssocMergeInputData;
import org.eclipse.emf.compare.tests.postprocess.data.TestPostProcessor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.Test;

@SuppressWarnings("nls")
public class AssocMergeTest extends AbstractTest {
	private AssocMergeInputData input = new AssocMergeInputData();

	/**
	 * Tests that merging a diff of a Connector that represents an association
	 * and whose target changes also merges the "equivalent" change of the
	 * related property whose type has changed accordingly.
	 * 
	 * @throws IOException
	 */
	@Test
	public void test2WayA1LeftToRight() throws IOException {
		final Resource left = input.get2WayA1Left();
		final Resource right = input.get2WayA1Right();

		Comparison comparison = buildComparison(left, right);

		EList<Diff> diffs = comparison.getDifferences();
		assertEquals(1, size(filter(diffs, edgeTargetChangeTo("Class3"))));

		// ** MERGE **
		Diff node = find(diffs, edgeTargetChangeTo("Class3"));
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node,
				new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		assertTrue(comparison.getDifferences().isEmpty());
	}

	/**
	 * Tests that merging a diff of a Connector that represents an association
	 * and whose target changes also merges the "equivalent" change of the
	 * related property whose type has changed accordingly.
	 * 
	 * @throws IOException
	 */
	@Test
	public void test2WayA1RightToLeft() throws IOException {
		final Resource left = input.get2WayA1Left();
		final Resource right = input.get2WayA1Right();

		Comparison comparison = buildComparison(left, right);

		EList<Diff> diffs = comparison.getDifferences();
		assertEquals(25, diffs.size());
		assertEquals(1, size(filter(diffs, edgeTargetChangeTo("Class3"))));

		// ** MERGE **
		Diff node = find(diffs, edgeTargetChangeTo("Class3"));
		getMergerRegistry().getHighestRankingMerger(node).copyRightToLeft(node,
				new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		// 2 diffs must have been merged
		assertEquals(23, comparison.getDifferences().size());
	}

	/**
	 * Tests that merging a diff of a Connector that represents an association
	 * and whose target changes also merges the "equivalent" change of the
	 * related property whose type has changed accordingly. The association owns
	 * both ends (contrary to A1).
	 * 
	 * @throws IOException
	 */
	@Test
	public void test2WayA2LeftToRight() throws IOException {
		final Resource left = input.get2WayA2Left();
		final Resource right = input.get2WayA2Right();

		Comparison comparison = buildComparison(left, right);

		EList<Diff> diffs = comparison.getDifferences();
		assertEquals(1, size(filter(diffs, edgeTargetChangeTo("Class3"))));

		// ** MERGE **
		Diff node = find(diffs, edgeTargetChangeTo("Class3"));
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node,
				new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		assertTrue(comparison.getDifferences().isEmpty());
	}

	/**
	 * Tests that merging a diff of a Connector that represents an association
	 * and whose target changes also merges the "equivalent" change of the
	 * related property whose type has changed accordingly. The association owns
	 * both ends (contrary to A1).
	 * 
	 * @throws IOException
	 */
	@Test
	public void test2WayA2RightToLeft() throws IOException {
		final Resource left = input.get2WayA2Left();
		final Resource right = input.get2WayA2Right();

		Comparison comparison = buildComparison(left, right);

		EList<Diff> diffs = comparison.getDifferences();
		assertEquals(25, diffs.size());
		assertEquals(1, size(filter(diffs, edgeTargetChangeTo("Class3"))));

		// ** MERGE **
		Diff node = find(diffs, edgeTargetChangeTo("Class3"));
		getMergerRegistry().getHighestRankingMerger(node).copyRightToLeft(node,
				new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		// 2 diffs must have been merged
		assertEquals(23, comparison.getDifferences().size());
	}

	/**
	 * Tests that merging a diff of an edge that represents a transition and
	 * whose target changes also merges the "equivalent" change of the related
	 * transition whose type has changed accordingly.
	 * 
	 * @throws IOException
	 */
	@Test
	public void test2WayT1LeftToRight() throws IOException {
		final Resource left = input.get2WayT1Left();
		final Resource right = input.get2WayT1Right();

		Comparison comparison = buildComparison(left, right);

		EList<Diff> diffs = comparison.getDifferences();
		assertEquals(4, diffs.size());
		assertEquals(1, size(filter(diffs, edgeTargetChangeTo("State2"))));

		// ** MERGE **
		Diff node = find(diffs, edgeTargetChangeTo("State2"));
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node,
				new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		assertEquals(2, comparison.getDifferences().size());
	}

	/**
	 * Tests that merging a diff of an edge that represents a transition and
	 * whose target changes also merges the "equivalent" change of the related
	 * transition whose type has changed accordingly.
	 * 
	 * @throws IOException
	 */
	@Test
	public void test2WayT1RightToLeft() throws IOException {
		final Resource left = input.get2WayT1Left();
		final Resource right = input.get2WayT1Right();

		Comparison comparison = buildComparison(left, right);

		EList<Diff> diffs = comparison.getDifferences();
		assertEquals(4, diffs.size());
		assertEquals(1, size(filter(diffs, edgeTargetChangeTo("State2"))));

		// ** MERGE **
		Diff node = find(diffs, edgeTargetChangeTo("State2"));
		getMergerRegistry().getHighestRankingMerger(node).copyRightToLeft(node,
				new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		assertEquals(2, comparison.getDifferences().size());
	}

	/**
	 * Tests that merging a diff of an edge that represents a transition and
	 * whose target changes also merges the "equivalent" change of the related
	 * transition whose type has changed accordingly, and reject the 2
	 * conflicts.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testConflictC1LeftToRight() throws IOException {
		final Resource ancestor = input.getConflictC1Ancestor();
		final Resource left = input.getConflictC1Left();
		final Resource right = input.getConflictC1Right();

		Comparison comparison = buildComparison(left, right, ancestor);

		EList<Diff> diffs = comparison.getDifferences();
		assertEquals(1, size(filter(diffs, edgeTargetChangeTo("Class1", LEFT))));
		assertEquals(1,
				size(filter(diffs, edgeTargetChangeTo("Class2", RIGHT))));
		assertEquals(1, size(filter(diffs, edgeTargetChangeTo("Class3", LEFT))));
		assertEquals(1,
				size(filter(diffs, edgeTargetChangeTo("Class4", RIGHT))));

		// ** MERGE **
		Diff node = find(diffs, edgeTargetChangeTo("Class1", LEFT));
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node,
				new BasicMonitor());
		node = find(diffs, edgeTargetChangeTo("Class3", LEFT));
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node,
				new BasicMonitor());

		// ** MERGE CHECKING **
		// left and right must now be equal
		comparison = buildComparison(left, right);
		assertTrue(comparison.getDifferences().isEmpty());
	}

	/**
	 * Tests that merging a diff of an edge that represents a transition and
	 * whose target changes also merges the "equivalent" change of the related
	 * transition whose type has changed accordingly, and reject the 2
	 * conflicts.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testConflictC1RightToLeft() throws IOException {
		final Resource ancestor = input.getConflictC1Ancestor();
		final Resource left = input.getConflictC1Left();
		final Resource right = input.getConflictC1Right();

		Comparison comparison = buildComparison(left, right, ancestor);

		EList<Diff> diffs = comparison.getDifferences();
		assertEquals(1, size(filter(diffs, edgeTargetChangeTo("Class1", LEFT))));
		assertEquals(1,
				size(filter(diffs, edgeTargetChangeTo("Class2", RIGHT))));
		assertEquals(1, size(filter(diffs, edgeTargetChangeTo("Class3", LEFT))));
		assertEquals(1,
				size(filter(diffs, edgeTargetChangeTo("Class4", RIGHT))));

		// ** MERGE **
		Diff node = find(diffs, edgeTargetChangeTo("Class2", RIGHT));
		getMergerRegistry().getHighestRankingMerger(node).copyRightToLeft(node,
				new BasicMonitor());
		node = find(diffs, edgeTargetChangeTo("Class4", RIGHT));
		getMergerRegistry().getHighestRankingMerger(node).copyRightToLeft(node,
				new BasicMonitor());

		// ** MERGE CHECKING **
		// left and right must now be equal
		comparison = buildComparison(left, right);
		assertTrue(comparison.getDifferences().isEmpty());
	}

	/**
	 * Tests that merging a diff of an edge that represents a transition and
	 * whose target changes also merges the "equivalent" change of the related
	 * transition whose type has changed accordingly, and reject the 2
	 * conflicts.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testConflictT1LeftToRight() throws IOException {
		final Resource ancestor = input.getConflictT1Ancestor();
		final Resource left = input.getConflictT1Left();
		final Resource right = input.getConflictT1Right();

		Comparison comparison = buildComparison(left, right, ancestor);

		EList<Diff> diffs = comparison.getDifferences();
		assertEquals(1, size(filter(diffs, edgeTargetChangeTo("State3"))));
		assertEquals(1, size(filter(diffs, edgeTargetChangeTo("State4"))));
		assertEquals(1, size(filter(diffs, transitionTargetToState("State3"))));
		assertEquals(1, size(filter(diffs, transitionTargetToState("State4"))));

		// ** MERGE **
		Diff node = find(diffs, edgeTargetChangeTo("State3"));
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node,
				new BasicMonitor());

		// ** MERGE CHECKING **
		// left and right must now be equal
		comparison = buildComparison(left, right);
		assertTrue(comparison.getDifferences().isEmpty());
	}

	/**
	 * Tests that merging a diff of an edge that represents a transition and
	 * whose target changes also merges the "equivalent" change of the related
	 * transition whose type has changed accordingly, and reject the 2
	 * conflicts.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testConflictT1RightToLeft() throws IOException {
		final Resource ancestor = input.getConflictT1Ancestor();
		final Resource left = input.getConflictT1Left();
		final Resource right = input.getConflictT1Right();

		Comparison comparison = buildComparison(left, right, ancestor);

		EList<Diff> diffs = comparison.getDifferences();
		assertEquals(1, size(filter(diffs, edgeTargetChangeTo("State3"))));
		assertEquals(1, size(filter(diffs, edgeTargetChangeTo("State4"))));
		assertEquals(1, size(filter(diffs, transitionTargetToState("State3"))));
		assertEquals(1, size(filter(diffs, transitionTargetToState("State4"))));

		// ** MERGE **
		Diff node = find(diffs, edgeTargetChangeTo("State4"));
		getMergerRegistry().getHighestRankingMerger(node).copyRightToLeft(node,
				new BasicMonitor());

		// ** MERGE CHECKING **
		// left and right must now be equal
		comparison = buildComparison(left, right);
		assertTrue(comparison.getDifferences().isEmpty());
	}

	/**
	 * Tests that merging a diff of an edge that represents a transition and
	 * whose target changes also merges the "equivalent" change of the related
	 * transition whose type has changed accordingly.
	 * 
	 * @throws IOException
	 */
	@Test
	public void test3WayT1LeftToRight() throws IOException {
		final Resource ancestor = input.get3WayT1Ancestor();
		final Resource left = input.get3WayT1Left();
		final Resource right = input.get3WayT1Right();

		Comparison comparison = buildComparison(left, right, ancestor);

		EList<Diff> diffs = comparison.getDifferences();
		assertEquals(1, size(filter(diffs, edgeTargetChangeTo("State3", LEFT))));
		assertEquals(1,
				size(filter(diffs, edgeSourceChangeTo("State3", RIGHT))));

		// ** MERGE **
		Diff node = find(diffs, edgeTargetChangeTo("State3", LEFT));
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node,
				new BasicMonitor());
		node = find(diffs, edgeSourceChangeTo("State3", RIGHT));
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node,
				new BasicMonitor());

		// ** MERGE CHECKING **
		// left and right must now be equal
		comparison = buildComparison(left, right);
		assertTrue(comparison.getDifferences().isEmpty());
	}

	/**
	 * Tests that merging a diff of an edge that represents a transition and
	 * whose target changes also merges the "equivalent" change of the related
	 * transition whose type has changed accordingly.
	 * 
	 * @throws IOException
	 */
	@Test
	public void test3WayC1LeftToRight() throws IOException {
		final Resource ancestor = input.get3WayC1Ancestor();
		final Resource left = input.get3WayC1Left();
		final Resource right = input.get3WayC1Right();

		Comparison comparison = buildComparison(left, right, ancestor);

		EList<Diff> diffs = comparison.getDifferences();
		assertEquals(1, size(filter(diffs, edgeTargetChangeTo("Class3", LEFT))));
		assertEquals(1,
				size(filter(diffs, edgeTargetChangeTo("Class1", RIGHT))));

		// ** MERGE **
		Diff node = find(diffs, edgeTargetChangeTo("Class3", LEFT));
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node,
				new BasicMonitor());
		node = find(diffs, edgeTargetChangeTo("Class1", RIGHT));
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node,
				new BasicMonitor());

		// ** MERGE CHECKING **
		// left and right must now be equal
		comparison = buildComparison(left, right);
		assertTrue(comparison.getDifferences().isEmpty());
	}

	/**
	 * Tests that merging a diff of an edge that represents a transition and
	 * whose target changes also merges the "equivalent" change of the related
	 * transition whose type has changed accordingly.
	 * 
	 * @throws IOException
	 */
	@Test
	public void test3WayC1RightToLeft() throws IOException {
		final Resource ancestor = input.get3WayC1Ancestor();
		final Resource left = input.get3WayC1Left();
		final Resource right = input.get3WayC1Right();

		Comparison comparison = buildComparison(left, right, ancestor);

		EList<Diff> diffs = comparison.getDifferences();
		assertEquals(1, size(filter(diffs, edgeTargetChangeTo("Class3", LEFT))));
		assertEquals(1,
				size(filter(diffs, edgeTargetChangeTo("Class1", RIGHT))));

		// ** MERGE **
		Diff node = find(diffs, edgeTargetChangeTo("Class3", LEFT));
		getMergerRegistry().getHighestRankingMerger(node).copyRightToLeft(node,
				new BasicMonitor());
		node = find(diffs, edgeTargetChangeTo("Class1", RIGHT));
		getMergerRegistry().getHighestRankingMerger(node).copyRightToLeft(node,
				new BasicMonitor());

		// ** MERGE CHECKING **
		// left and right must now be equal
		comparison = buildComparison(left, right);
		assertTrue(comparison.getDifferences().isEmpty());
	}

	/**
	 * Tests that merging a diff of an edge that represents a transition and
	 * whose target changes also merges the "equivalent" change of the related
	 * transition whose type has changed accordingly.
	 * 
	 * @throws IOException
	 */
	@Test
	public void test3WayT1RightToLeft() throws IOException {
		final Resource ancestor = input.get3WayT1Ancestor();
		final Resource left = input.get3WayT1Left();
		final Resource right = input.get3WayT1Right();

		Comparison comparison = buildComparison(left, right, ancestor);

		EList<Diff> diffs = comparison.getDifferences();
		assertEquals(1, size(filter(diffs, edgeTargetChangeTo("State3", LEFT))));
		assertEquals(1,
				size(filter(diffs, edgeSourceChangeTo("State3", RIGHT))));

		// ** MERGE **
		Diff node = find(diffs, edgeTargetChangeTo("State3", LEFT));
		getMergerRegistry().getHighestRankingMerger(node).copyRightToLeft(node,
				new BasicMonitor());
		node = find(diffs, edgeSourceChangeTo("State3", RIGHT));
		getMergerRegistry().getHighestRankingMerger(node).copyRightToLeft(node,
				new BasicMonitor());

		// ** MERGE CHECKING **
		// left and right must now be equal
		comparison = buildComparison(left, right);
		assertTrue(comparison.getDifferences().isEmpty());
	}

	private Predicate<Diff> edgeTargetChangeTo(String name,
			final DifferenceSource source) {
		return and(edgeTargetChangeTo(name), fromSide(source));
	}

	private Predicate<Diff> edgeSourceChangeTo(String name,
			final DifferenceSource source) {
		return and(edgeSourceChangeTo(name), fromSide(source));
	}

	private Predicate<Diff> edgeSourceChangeTo(String name) {
		return and(
				isReferenceChange(DifferenceKind.CHANGE,
						NotationPackage.Literals.EDGE__SOURCE, Shape.class),
				valueIsViewNamed(name));
	}

	private Predicate<Diff> edgeTargetChangeTo(String name) {
		return and(
				isReferenceChange(DifferenceKind.CHANGE,
						NotationPackage.Literals.EDGE__TARGET, Shape.class),
				valueIsViewNamed(name));
	}

	private Predicate<Diff> transitionTargetToState(String name) {
		return and(
				isReferenceChange(DifferenceKind.CHANGE,
						UMLPackage.Literals.TRANSITION__TARGET, State.class),
				valueIsElement(UMLPackage.Literals.STATE, name));
	}

	private Predicate<Diff> valueIsElement(final EClass eClass,
			final String name) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				ReferenceChange refChange = (ReferenceChange) input;
				EObject value = refChange.getValue();
				return value instanceof NamedElement
						&& eClass.isInstance(value)
						&& name.equals(((NamedElement) value).getName());
			}
		};
	}

	private Predicate<Diff> isReferenceChange(final DifferenceKind kind,
			final EReference ref, final Class<?> clazz) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				if (input instanceof ReferenceChange && input.getKind() == kind) {
					ReferenceChange refChange = (ReferenceChange) input;
					return refChange.getReference() == ref
							&& refChange.getValue() != null
							&& clazz.isAssignableFrom(refChange.getValue()
									.getClass());
				}
				return false;
			}
		};
	}

	private Predicate<Diff> valueIsViewNamed(final String name) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				ReferenceChange refChange = (ReferenceChange) input;
				if (refChange.getValue() instanceof View) {
					View view = (View) refChange.getValue();
					return view.getElement() instanceof NamedElement
							&& name.equals(((NamedElement) view.getElement())
									.getName());
				}
				return false;
			}
		};
	}

	@Override
	protected DiagramInputData getInput() {
		return input;
	}

	@Override
	public void registerPostProcessors() {
		super.registerPostProcessors();
		getPostProcessorRegistry()
				.put(PapyrusDiagramPostProcessor.class.getName(),
						new TestPostProcessor.TestPostProcessorDescriptor(
								Pattern.compile("http://www.eclipse.org/gmf/runtime/\\d.\\d.\\d/notation"),
								null, new PapyrusDiagramPostProcessor(), 35));
	}

}

/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.structuremergeviewer.actions;

import static com.google.common.collect.Iterables.all;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Iterables.size;
import static org.eclipse.emf.compare.DifferenceState.MERGED;
import static org.eclipse.emf.compare.DifferenceState.UNRESOLVED;
import static org.eclipse.emf.compare.merge.AbstractMerger.SUB_DIFF_AWARE_OPTION;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.containsConflictOfTypes;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasState;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeNonConflictingRunnable;
import org.eclipse.emf.compare.ide.ui.tests.framework.RuntimeTestRunner;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.Compare;
import org.eclipse.emf.compare.ide.ui.tests.framework.internal.CompareTestSupport;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMergeOptionAware;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.junit.runner.RunWith;

/**
 * <p>
 * This class tests the expected behavior of accepting non-conflicting changes when the cascading differences
 * filter is enabled. The cascading differences filter handles the setting of sub-diff awareness in mergers.
 * </p>
 * <p>
 * In particular, we test that bug 487151 is fixed where conflicting diffs are merged when 'accepting
 * non-conflicting changes'. The source of this bug lies in the sub-diff awareness of mergers that merge
 * sub-diffs, their dependendies, their sub-diffs, etc. Therefore, this bug only occurs when sub-diff
 * awareness ('cascading differences filter' to the user) is enabled.
 * </p>
 * We use the following structure to test the correct behavior:
 * <ul>
 * <li>Origin: Package 'Package1' containing class 'Class1'</li>
 * <li>Left: Package 'Package1' (class 'Class1' removed)</li>
 * <li>Right: Package 'Package1' containing class 'Class1', and class 'Class2' subclassing class 'Class1'
 * (class 'Class2' and generalization added)
 * </ul>
 * For EMF Compare this means that we have the following relationships between the UML differences:
 * <ul>
 * <li>L1: Left DELETE Class 1 (conflicts with R4)</li>
 * <li>R1: Right ADD Class 2 (subDiffs because also related to Class 2: R2 and R3)</li>
 * <li>R2: Right ADD Generalization (refinedBy R3 and R4)</li>
 * <li>R3: Right ADD generalization to Class2.generalization</li>
 * <li>R4: Right CHANGE generalization.general to Class 1 (conflicts with L1)</li>
 * </ul>
 * <p>
 * In the comparison, we detect a conflict between adding the generalization, setting the 'general' of the
 * generalization and the deletion of class 'Class1'. When accepting all non-conflicting changes (left changes
 * accepted, right changes merged into left for non-conflicts), we expect the left model to add nothing when
 * sub-diffs are enabled and to add Class 2 (R1) when sub-diffs are not enabled. The generalization (R2-R4)
 * should not be added in any case since in UML we consider the addition of the generalization and the setting
 * of the 'general' as a whole (DirectedRelationshipChange) and the 'general' can not be set since 'Class2'
 * has been removed (conflict).
 * </p>
 * <p>
 * All non-conflicting diffs should be in state {@link DifferenceState#MERGED} and all conflicting diffs
 * should be in state {@link DifferenceState#UNRESOLVED}.
 * </p>
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=487151
 */
@SuppressWarnings({"restriction", "nls", "boxing" })
@RunWith(RuntimeTestRunner.class)
public class MergeNonConflictingCascadingFilterTest {
	/** Cached cascading options before the last time the filter was enabled or disabled. */
	private static final Map<IMergeOptionAware, Object> CACHED_OPTIONS = Maps.newHashMap();

	private static Registry MERGER_REGISTRY = EMFCompareRCPPlugin.getDefault().getMergerRegistry();

	/** Filter On setting **/
	private static boolean FILTER_ON = true;

	/** Filter Off setting **/
	private static boolean FILTER_OFF = false;

	/**
	 * <p>
	 * Tests that bug 487151 is fixed when the cascading differences filter is turned ON and graphical
	 * elements and their relationships are involved.
	 * </p>
	 */
	@Compare(left = "data/bug487151/classes/left/left.notation", right = "data/bug487151/classes/right/right.notation", ancestor = "data/bug487151/classes/origin/ancestor.notation")
	public void testBug487151_Papyrus_On(final Comparison comparison) {
		// lots of graphical diffs, details not important
		Integer nrDiffs = null;

		// 1 REAL structural and 1 REAL graphical
		int nrRealConflicts = 2;

		// graphical diffs are not conflicting
		boolean hasNonConflictingDiffs = true;

		// conflicting structural differences: LEFT delete Class1, RIGHT change Generalization.general, RIGHT
		// add DirectedRelationshipChange + graphical differences: ...
		boolean hasConflictingDiffs = true;

		assertUMLDiffsAndStructure(comparison, FILTER_ON, nrDiffs, nrRealConflicts, hasNonConflictingDiffs,
				hasConflictingDiffs);
	}

	/**
	 * <p>
	 * Tests that bug 487151 is fixed when the cascading differences filter is turned OFF and graphical
	 * elements and their relationships are involved.
	 * </p>
	 */
	@Compare(left = "data/bug487151/classes/left/left.notation", right = "data/bug487151/classes/right/right.notation", ancestor = "data/bug487151/classes/origin/ancestor.notation")
	public void testBug487151_Papyrus_Off(final Comparison comparison) {
		// lots of graphical diffs, details not important
		Integer nrDiffs = null;

		// 1 REAL structural and 1 REAL graphical
		int nrRealConflicts = 2;

		// graphical diffs are not conflicting
		boolean hasNonConflictingDiffs = true;

		// conflicting structural differences: LEFT delete Class1, RIGHT change Generalization.general, RIGHT
		// add DirectedRelationshipChange + graphical differences: ...
		boolean hasConflictingDiffs = true;

		assertUMLDiffsAndStructure(comparison, FILTER_OFF, nrDiffs, nrRealConflicts, hasNonConflictingDiffs,
				hasConflictingDiffs);
	}

	/**
	 * <p>
	 * Tests that bug 487151 is fixed when the cascading differences filter is turned ON and only UML elements
	 * and their relationships are involved.
	 * </p>
	 */
	@Compare(left = "data/bug487151/classes/left/left.uml", right = "data/bug487151/classes/right/right.uml", ancestor = "data/bug487151/classes/origin/ancestor.uml")
	public void testBug487151_UML_On(final Comparison comparison) {
		// 5 differences: LEFT delete Class1, RIGHT add Class2, RIGHT add generalization, RIGHT change
		// Generalization.general, RIGHT add DirectedRelationshipChange (refining add/change for
		// Generalization; from UML PostProcessor)
		int nrDiffs = 5;

		// 1 REAL structural conflict
		int nrRealConflicts = 1;

		// all diffs are in conflict through their relationships
		boolean hasNonConflictingDiffs = false;
		boolean hasConflictingDiffs = true;

		assertUMLDiffsAndStructure(comparison, FILTER_ON, nrDiffs, nrRealConflicts, hasNonConflictingDiffs,
				hasConflictingDiffs);
	}

	/**
	 * <p>
	 * Tests that bug 487151 is fixed when the cascading differences filter is turned OFF and only UML
	 * elements and their relationships are involved.
	 * </p>
	 */
	@Compare(left = "data/bug487151/classes/left/left.uml", right = "data/bug487151/classes/right/right.uml", ancestor = "data/bug487151/classes/origin/ancestor.uml")
	public void testBug487151_UML_Off(final Comparison comparison) {
		// 5 differences: LEFT delete Class1, RIGHT add Class2, RIGHT add generalization, RIGHT change
		// Generalization.general, RIGHT add DirectedRelationshipChange (refining add/change for
		// Generalization; from UML PostProcessor)
		int nrDiffs = 5;

		// 1 REAL structural conflict
		int nrRealConflicts = 1;

		// all diffs are in conflict through their relationships
		boolean hasNonConflictingDiffs = true;
		boolean hasConflictingDiffs = true;

		assertUMLDiffsAndStructure(comparison, FILTER_OFF, nrDiffs, nrRealConflicts, hasNonConflictingDiffs,
				hasConflictingDiffs);
	}

	/**
	 * <p>
	 * Tests that bug 487151 is fixed when the cascading differences filter is turned ON and and Ecore
	 * representation is used. Since we do not use refinement in Ecore and the Generalization is represented
	 * as a reference, Class 2 can be added independently of the filter setting.
	 * </p>
	 * <ul>
	 * <li>Origin: Package 'Package1' containing class 'Class1'</li>
	 * <li>Left: Package 'Package1' (class 'Class1' removed)</li>
	 * <li>Right: Package 'Package1' containing class 'Class1' and class 'Class2' with reference named
	 * 'Generalization' that has as eType 'Class1' (class 'Class2' and added generalization reference)
	 * </ul>
	 * <p>
	 * In the comparison, we detect a conflict between setting the eType of the 'Generalization' reference and
	 * the deletion of class 'Class1'. When accepting all non-conflicting changes (left changes accepted,
	 * right changes merged into left for non-conflicts), we expect the left model to have 'Class2' AND the
	 * 'Generalization' reference without an eType since 'Class1' has been deleted. All non-conflicting diffs
	 * should be in state {@link DifferenceState#MERGED} and all conflicting diffs should be in state
	 * {@link DifferenceState#UNRESOLVED}.
	 * </p>
	 */
	@Compare(left = "data/bug487151/ecore/left/left.ecore", right = "data/bug487151/ecore/right/right.ecore", ancestor = "data/bug487151/ecore/origin/ancestor.ecore")
	public void testBug487151_Ecore_On(final Comparison comparison, final CompareTestSupport support) {
		// 4 differences: LEFT delete Class1, RIGHT add Class2, RIGHT add generalization, RIGHT change
		// Generalization.general
		int nrDiffs = 4;

		// 1 conflict (REAL)
		int nrRealConflicts = 1;

		// non-conflicting difference: RIGHT add Class2
		boolean hasNonConflictingDiffs = true;

		// conflicting differences: LEFT delete Class1, RIGHT change Generalization.eType, RIGHT add
		// DirectedRelationshipChange
		boolean hasConflictingDiffs = true;

		assertEcoreDiffsAndStructure(comparison, support, FILTER_ON, nrDiffs, nrRealConflicts,
				hasNonConflictingDiffs, hasConflictingDiffs);
	}

	/**
	 * <p>
	 * Tests that bug 487151 is fixed when the cascading differences filter is turned ON and and Ecore
	 * representation is used. Since we do not use refinement in Ecore and the Generalization is represented
	 * as a reference, Class 2 can be added independently of the filter setting.
	 * </p>
	 * <ul>
	 * <li>Origin: Package 'Package1' containing class 'Class1'</li>
	 * <li>Left: Package 'Package1' (class 'Class1' removed)</li>
	 * <li>Right: Package 'Package1' containing class 'Class1' and class 'Class2' with reference named
	 * 'Generalization' that has as eType 'Class1' (class 'Class2' and added generalization reference)
	 * </ul>
	 * <p>
	 * In the comparison, we detect a conflict between setting the eType of the 'Generalization' reference and
	 * the deletion of class 'Class1'. When accepting all non-conflicting changes (left changes accepted,
	 * right changes merged into left for non-conflicts), we expect the left model to have 'Class2' AND the
	 * 'Generalization' reference without an eType since 'Class1' has been deleted. All non-conflicting diffs
	 * should be in state {@link DifferenceState#MERGED} and all conflicting diffs should be in state
	 * {@link DifferenceState#UNRESOLVED}.
	 * </p>
	 */
	@Compare(left = "data/bug487151/ecore/left/left.ecore", right = "data/bug487151/ecore/right/right.ecore", ancestor = "data/bug487151/ecore/origin/ancestor.ecore")
	public void testBug487151_Ecore_Off(final Comparison comparison, final CompareTestSupport support) {
		// 4 differences: LEFT delete Class1, RIGHT add Class2, RIGHT add generalization, RIGHT change
		// Generalization.general
		int nrDiffs = 4;

		// 1 conflict (REAL)
		int nrRealConflicts = 1;

		// non-conflicting difference: RIGHT add Class2
		boolean hasNonConflictingDiffs = true;

		// conflicting differences: LEFT delete Class1, RIGHT change Generalization.eType, RIGHT add
		// DirectedRelationshipChange
		boolean hasConflictingDiffs = true;

		assertEcoreDiffsAndStructure(comparison, support, FILTER_OFF, nrDiffs, nrRealConflicts,
				hasNonConflictingDiffs, hasConflictingDiffs);
	}

	/**
	 * Asserts the expected differences and structure for Ecore models. Specifically, that independently of
	 * the filter setting, the class 2 and its generalization is added, but the type of the generalization is
	 * not set.
	 * 
	 * @param comparison
	 *            comparison
	 * @param support
	 *            test support for the comparison
	 * @param filter
	 *            sub-diff filter setting
	 * @param nrDiffs
	 *            expected number of differences
	 * @param nrRealConflicts
	 *            expected number of real conflicts
	 * @param hasNonConflictingDiffs
	 *            whether non-conflicting diffs are expected
	 * @param hasConflictingDiffs
	 *            whether conflicting diffs are expected
	 */
	protected void assertEcoreDiffsAndStructure(Comparison comparison, CompareTestSupport support,
			boolean filter, int nrDiffs, int nrRealConflicts, boolean hasNonConflictingDiffs,
			boolean hasConflictingDiffs) {
		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		// 4 differences: LEFT delete Class1, RIGHT add Class2, RIGHT add generalization, RIGHT change
		// Generalization.general
		assertEquals(nrDiffs, differences.size());

		// real conflicts
		Iterable<Conflict> realConflicts = filter(conflicts, containsConflictOfTypes(ConflictKind.REAL));
		assertEquals(nrRealConflicts, size(realConflicts));

		// before accepting any changes, all differences are unresolved
		assertTrue(all(differences, hasState(UNRESOLVED)));

		// accept non-conflicting changes and collect differences
		Iterable<Diff> nonConflictingDifferences = acceptNonConflictingChanges(comparison, filter);
		assertEquals(hasNonConflictingDiffs, !isEmpty(nonConflictingDifferences));

		// conflicting differences: LEFT delete Class1, RIGHT change Generalization.eType, RIGHT add
		// DirectedRelationshipChange
		final List<Diff> conflictingDifferences = getConflictingDifferences(
				Lists.newArrayList(nonConflictingDifferences), conflicts);
		assertEquals(hasConflictingDiffs, !isEmpty(conflictingDifferences));

		// all accepted differences have been merged
		assertTrue(all(nonConflictingDifferences, hasState(MERGED)));

		// any differences part of real conflicts are still unresolved
		assertTrue(all(conflictingDifferences, hasState(UNRESOLVED)));

		// assert new structure (right changes merged into left for non-conflicts)
		EObject root = support.getLeftResource().getContents().get(0);
		assertNotNull(root);
		assertTrue(root instanceof EPackage);
		EPackage package1 = (EPackage)root;
		assertNotNull(package1);
		EClassifier class1 = package1.getEClassifier("Class1");
		assertNull(class1); // still null due to conflict
		EClassifier class2 = package1.getEClassifier("Class2");

		// regardless of filter, class 2 and generalization is added
		assertNotNull(class2); // newly added
		EClass class2class = (EClass)class2;
		EStructuralFeature class2generalization = class2class.getEStructuralFeature("Generalization");
		assertNotNull(class2generalization); // reference generalization has been added
		assertNull(class2generalization.getEType()); // no type set due to conflict
	}

	/**
	 * Asserts the expected differences and structure for UML models. Specifically, whether Class 2
	 * conflicting with sub-diff filter enabled is added.
	 * 
	 * @param comparison
	 *            comparison
	 * @param filter
	 *            sub-diff filter setting
	 * @param nrDiffs
	 *            expected number of differences
	 * @param nrRealConflicts
	 *            expected number of real conflicts
	 * @param hasNonConflictingDiffs
	 *            whether non-conflicting diffs are expected
	 * @param hasConflictingDiffs
	 *            whether conflicting diffs are expected
	 */
	protected void assertUMLDiffsAndStructure(Comparison comparison, boolean filter, Integer nrDiffs,
			int nrRealConflicts, boolean hasNonConflictingDiffs, boolean hasConflictingDiffs) {
		final List<Diff> differences = comparison.getDifferences();
		final List<Conflict> conflicts = comparison.getConflicts();

		if (nrDiffs != null) {
			assertEquals(nrDiffs.intValue(), differences.size());
		}

		Iterable<Conflict> realConflicts = filter(conflicts, containsConflictOfTypes(ConflictKind.REAL));
		assertEquals(nrRealConflicts, size(realConflicts));

		// before accepting any changes, all differences are unresolved
		assertTrue(all(differences, hasState(UNRESOLVED)));

		// accept non-conflicting changes and collect differences
		Iterable<Diff> nonConflictingDifferences = acceptNonConflictingChanges(comparison, filter);
		assertEquals(hasNonConflictingDiffs, !isEmpty(nonConflictingDifferences));

		// get conflicting diffs
		final List<Diff> conflictingDifferences = getConflictingDifferences(
				Lists.newArrayList(nonConflictingDifferences), Lists.newArrayList(realConflicts));
		assertEquals(hasConflictingDiffs, !conflictingDifferences.isEmpty());

		// all accepted differences have been merged
		assertTrue(all(nonConflictingDifferences, hasState(MERGED)));

		// any differences part of real conflicts are still unresolved
		assertTrue(all(conflictingDifferences, hasState(UNRESOLVED)));

		// assert new structure (right changes merged into left for non-conflicts)
		EObject root = getLeftUMLRoot(comparison);
		assertNotNull(root);
		assertTrue(root instanceof Model);
		Model model = (Model)root;
		Package package1 = (Package)model.getPackagedElement("Package1");
		assertNotNull(package1);
		PackageableElement class1 = package1.getPackagedElement("Class1");
		assertNull(class1); // still null due to conflict
		PackageableElement class2 = package1.getPackagedElement("Class2");
		if (filter == FILTER_OFF) {
			assertNotNull(class2);
			Class class2class = (Class)class2;
			assertTrue(class2class.getGeneralizations().isEmpty()); // no generalization added due to conflict
		} else {
			assertNull(class2); // class not added, because sub-diffs are in conflict
		}
	}

	/**
	 * Returns the root element of the left UML resource or null if no such element can be found.
	 * 
	 * @param comparison
	 *            comparison
	 * @return left root element
	 */
	protected static EObject getLeftUMLRoot(Comparison comparison) {
		for (MatchResource resource : comparison.getMatchedResources()) {
			if (resource.getLeft().getURI().lastSegment().endsWith("uml")) {
				return resource.getLeft().getContents().get(0);
			}
		}
		return null;
	}

	/**
	 * Accepts all non-conflicting changes. The left changes will be accepted and the right changes will be
	 * merged into the left-hand side.
	 * 
	 * @param comparison
	 *            comparison with differences
	 * @param leftToRight
	 *            direction of merge
	 * @return affected differences
	 */
	protected static Iterable<Diff> acceptNonConflictingChanges(Comparison comparison,
			boolean cascadingFilter) {
		boolean leftToRight = false;
		boolean isLeftEditable = true;
		boolean isRightEditable = false;
		setCascadingFilter(cascadingFilter);
		MergeNonConflictingRunnable mergeNonConflicting = new MergeNonConflictingRunnable(isLeftEditable,
				isRightEditable, MergeMode.ACCEPT, null);
		Iterable<Diff> mergedDiffs = mergeNonConflicting.merge(comparison, leftToRight, MERGER_REGISTRY);
		restoreCascadingFilter();
		return mergedDiffs;
	}

	/**
	 * Returns a list of all differences that part of a conflict and ensures that there is no overlap between
	 * conflicting and non-conflicting diffs.
	 * 
	 * @param realConflicts
	 *            conflicts
	 * @return conflicting differences
	 */
	protected static List<Diff> getConflictingDifferences(List<Diff> nonConflictingDiffs,
			List<Conflict> realConflicts) {
		ArrayList<Diff> conflictingDiffs = Lists.newArrayList();
		for (Conflict conflict : realConflicts) {
			EList<Diff> conflctDiffs = conflict.getDifferences();
			conflictingDiffs.addAll(conflctDiffs);
		}

		// assert no overlap between non-conflicting and conflicting
		assertFalse(nonConflictingDiffs.removeAll(conflictingDiffs));
		assertFalse(conflictingDiffs.removeAll(nonConflictingDiffs));

		return conflictingDiffs;
	}

	/**
	 * Sets the cascading filter option (subdiff-awareness) of all mergers to the given state. Any changes
	 * done by this method can be restored by calling {@link #restoreCascadingFilter()}.
	 * 
	 * @param enabled
	 *            filter state
	 */
	protected static void setCascadingFilter(boolean enabled) {
		for (IMergeOptionAware merger : Iterables.filter(MERGER_REGISTRY.getMergers(null),
				IMergeOptionAware.class)) {
			Map<Object, Object> mergeOptions = merger.getMergeOptions();
			Object previousValue = mergeOptions.get(SUB_DIFF_AWARE_OPTION);
			CACHED_OPTIONS.put(merger, previousValue);
			mergeOptions.put(SUB_DIFF_AWARE_OPTION, Boolean.valueOf(enabled));
		}
	}

	/**
	 * Restores the cascading filter options changed by the last call to {@link #enableCascadingFilter()},
	 * {@link #disableCascadingFilter()}, or {@link #setCascadingFilter(boolean)}.
	 */
	protected static void restoreCascadingFilter() {
		// restore previous values
		for (Entry<IMergeOptionAware, Object> entry : CACHED_OPTIONS.entrySet()) {
			IMergeOptionAware merger = entry.getKey();
			merger.getMergeOptions().put(SUB_DIFF_AWARE_OPTION, entry.getValue());
		}
	}
}

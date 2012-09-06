package org.eclipse.emf.compare.uml2.tests.generalizationSet;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.uml2.tests.UMLComparePredicates.added;
import static org.eclipse.emf.compare.uml2.tests.UMLComparePredicates.addedToReference;
import static org.eclipse.emf.compare.uml2.tests.UMLComparePredicates.ofKind;
import static org.eclipse.emf.compare.uml2.tests.UMLComparePredicates.removed;
import static org.eclipse.emf.compare.uml2.tests.UMLComparePredicates.removedFromReference;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.uml2.GeneralizationSetChange;
import org.eclipse.emf.compare.uml2.tests.AbstractTest;
import org.eclipse.emf.compare.uml2.tests.generalizationSet.data.GeneralizationSetInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings("nls")
public class AddGeneralizationSetTest extends AbstractTest {

	private GeneralizationSetInputData input = new GeneralizationSetInputData();

	@Test
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();
		testAB1(TestKind.DELETE, comparison);
	}

	private static void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 6 differences
		assertSame(Integer.valueOf(6), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addGeneralizationSetDescription = null;
		Predicate<? super Diff> addRefGeneralizationSetInClass2Description = null;
		Predicate<? super Diff> addRefGeneralizationSetInClass0Description = null;
		Predicate<? super Diff> addRefGeneralizationInGeneralizationSetDescription1 = null;
		Predicate<? super Diff> addRefGeneralizationInGeneralizationSetDescription2 = null;

		if (kind.equals(TestKind.DELETE)) {
			addGeneralizationSetDescription = removed("model.GeneralizationSet_Class2_Class0"); //$NON-NLS-1$
			addRefGeneralizationSetInClass2Description = removedFromReference("model.Class2.Class1",
					"generalizationSet", "model.GeneralizationSet_Class2_Class0");
			addRefGeneralizationSetInClass0Description = removedFromReference("model.Class0.Class1",
					"generalizationSet", "model.GeneralizationSet_Class2_Class0");
			addRefGeneralizationInGeneralizationSetDescription1 = removedFromReference(
					"model.GeneralizationSet_Class2_Class0", "generalization", "model.Class0.Class1");
			addRefGeneralizationInGeneralizationSetDescription2 = removedFromReference(
					"model.GeneralizationSet_Class2_Class0", "generalization", "model.Class2.Class1");
		} else {
			addGeneralizationSetDescription = added("model.GeneralizationSet_Class2_Class0"); //$NON-NLS-1$
			addRefGeneralizationSetInClass2Description = addedToReference("model.Class2.Class1",
					"generalizationSet", "model.GeneralizationSet_Class2_Class0");
			addRefGeneralizationSetInClass0Description = addedToReference("model.Class0.Class1",
					"generalizationSet", "model.GeneralizationSet_Class2_Class0");
			addRefGeneralizationInGeneralizationSetDescription1 = addedToReference(
					"model.GeneralizationSet_Class2_Class0", "generalization", "model.Class0.Class1");
			addRefGeneralizationInGeneralizationSetDescription2 = addedToReference(
					"model.GeneralizationSet_Class2_Class0", "generalization", "model.Class2.Class1");
		}

		final Diff addGeneralizationSet = Iterators.find(differences.iterator(),
				addGeneralizationSetDescription);
		final Diff addRefGeneralizationSetInClass0 = Iterators.find(differences.iterator(),
				addRefGeneralizationSetInClass0Description);
		final Diff addRefGeneralizationSetInClass2 = Iterators.find(differences.iterator(),
				addRefGeneralizationSetInClass2Description);
		final Diff addRefGeneralizationInGeneralizationSet1 = Iterators.find(differences.iterator(),
				addRefGeneralizationInGeneralizationSetDescription1);
		final Diff addRefGeneralizationInGeneralizationSet2 = Iterators.find(differences.iterator(),
				addRefGeneralizationInGeneralizationSetDescription2);

		assertNotNull(addGeneralizationSet);
		assertNotNull(addRefGeneralizationSetInClass0);
		assertNotNull(addRefGeneralizationSetInClass2);
		assertNotNull(addRefGeneralizationInGeneralizationSet1);
		assertNotNull(addRefGeneralizationInGeneralizationSet2);

		// CHECK EXTENSION
		assertSame(Integer.valueOf(1), count(differences, instanceOf(GeneralizationSetChange.class)));
		Diff addUMLGeneralizationSet = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLGeneralizationSet = Iterators.find(differences.iterator(), and(
					instanceOf(GeneralizationSetChange.class), ofKind(DifferenceKind.ADD)));
			assertNotNull(addUMLGeneralizationSet);
			assertSame(Integer.valueOf(2), Integer.valueOf(addUMLGeneralizationSet.getRefinedBy().size()));
			assertTrue(addUMLGeneralizationSet.getRefinedBy().contains(
					addRefGeneralizationInGeneralizationSet1));
			assertTrue(addUMLGeneralizationSet.getRefinedBy().contains(
					addRefGeneralizationInGeneralizationSet2));
		} else {
			addUMLGeneralizationSet = Iterators.find(differences.iterator(), and(
					instanceOf(GeneralizationSetChange.class), ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLGeneralizationSet);
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLGeneralizationSet.getRefinedBy().size()));
			assertTrue(addUMLGeneralizationSet.getRefinedBy().contains(addGeneralizationSet));
		}

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(1), Integer.valueOf(addRefGeneralizationInGeneralizationSet1
					.getRequires().size()));
			assertTrue(addRefGeneralizationInGeneralizationSet1.getRequires().contains(addGeneralizationSet));

			assertSame(Integer.valueOf(1), Integer.valueOf(addRefGeneralizationInGeneralizationSet2
					.getRequires().size()));
			assertTrue(addRefGeneralizationInGeneralizationSet2.getRequires().contains(addGeneralizationSet));

			assertSame(Integer.valueOf(1), Integer.valueOf(addRefGeneralizationSetInClass0.getRequires()
					.size()));
			assertTrue(addRefGeneralizationSetInClass0.getRequires().contains(addGeneralizationSet));

			assertSame(Integer.valueOf(1), Integer.valueOf(addRefGeneralizationSetInClass2.getRequires()
					.size()));
			assertTrue(addRefGeneralizationSetInClass2.getRequires().contains(addGeneralizationSet));

			assertSame(Integer.valueOf(0), Integer.valueOf(addGeneralizationSet.getRequires().size()));
			assertSame(Integer.valueOf(0), Integer.valueOf(addUMLGeneralizationSet.getRequires().size()));
		} else {
			assertSame(Integer.valueOf(0), Integer.valueOf(addRefGeneralizationInGeneralizationSet1
					.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addRefGeneralizationInGeneralizationSet2
					.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addRefGeneralizationSetInClass0.getRequires()
					.size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addRefGeneralizationSetInClass2.getRequires()
					.size()));

			assertSame(Integer.valueOf(4), Integer.valueOf(addGeneralizationSet.getRequires().size()));
			assertTrue(addGeneralizationSet.getRequires().contains(addRefGeneralizationInGeneralizationSet1));
			assertTrue(addGeneralizationSet.getRequires().contains(addRefGeneralizationInGeneralizationSet2));
			assertTrue(addGeneralizationSet.getRequires().contains(addRefGeneralizationSetInClass0));
			assertTrue(addGeneralizationSet.getRequires().contains(addRefGeneralizationSetInClass2));

			assertSame(Integer.valueOf(0), Integer.valueOf(addUMLGeneralizationSet.getRequires().size()));
		}

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getEquivalences().size()));

		assertNotNull(addRefGeneralizationInGeneralizationSet1.getEquivalence());
		assertSame(Integer.valueOf(2), Integer.valueOf(addRefGeneralizationInGeneralizationSet1
				.getEquivalence().getDifferences().size()));
		assertTrue(addRefGeneralizationInGeneralizationSet1.getEquivalence().getDifferences().contains(
				addRefGeneralizationSetInClass0));

		assertNotNull(addRefGeneralizationInGeneralizationSet2.getEquivalence());
		assertSame(Integer.valueOf(2), Integer.valueOf(addRefGeneralizationInGeneralizationSet2
				.getEquivalence().getDifferences().size()));
		assertTrue(addRefGeneralizationInGeneralizationSet2.getEquivalence().getDifferences().contains(
				addRefGeneralizationSetInClass2));

	}

}

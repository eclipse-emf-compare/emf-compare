package org.eclipse.emf.compare.tests.equi;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.tests.equi.data.EquiInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

public class EquiComputingTest {

	private EquiInputData input = new EquiInputData();

	@Test
	public void testA1UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = EMFCompare.compare(left, right);

		List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 6 differences
		assertSame(Integer.valueOf(6), Integer.valueOf(differences.size()));

		Predicate<? super Diff> changeRefA2BDiffDescription = changedReference("Requirements.A",
				"destination", null, "Requirements.B");
		Predicate<? super Diff> changeRefB2ADiffDescription = changedReference("Requirements.B", "source",
				null, "Requirements.A");
		Predicate<? super Diff> changeRefC2DDiffDescription = addedToReference("Requirements.C",
				"destination", "Requirements.D");
		Predicate<? super Diff> changeRefD2CDiffDescription = changedReference("Requirements.D", "source",
				null, "Requirements.C");
		Predicate<? super Diff> changeRefE2FDiffDescription = addedToReference("Requirements.E",
				"destination", "Requirements.F");
		Predicate<? super Diff> changeRefF2EDiffDescription = addedToReference("Requirements.F", "source",
				"Requirements.E");

		final Diff changeRefA2BDiff = Iterators.find(differences.iterator(), changeRefA2BDiffDescription);
		final Diff changeRefB2ADiff = Iterators.find(differences.iterator(), changeRefB2ADiffDescription);
		final Diff changeRefC2DDiff = Iterators.find(differences.iterator(), changeRefC2DDiffDescription);
		final Diff changeRefD2CDiff = Iterators.find(differences.iterator(), changeRefD2CDiffDescription);
		final Diff changeRefE2FDiff = Iterators.find(differences.iterator(), changeRefE2FDiffDescription);
		final Diff changeRefF2EDiff = Iterators.find(differences.iterator(), changeRefF2EDiffDescription);

		assertNotNull(changeRefA2BDiff);
		assertNotNull(changeRefB2ADiff);
		assertNotNull(changeRefC2DDiff);
		assertNotNull(changeRefD2CDiff);
		assertNotNull(changeRefE2FDiff);
		assertNotNull(changeRefF2EDiff);

		// CHECK EQUIVALENCE
		assertNotNull(changeRefA2BDiff.getEquivalence());
		assertSame(Integer.valueOf(2), changeRefA2BDiff.getEquivalence().getDifferences().size());
		assertTrue(changeRefA2BDiff.getEquivalence().getDifferences().contains(changeRefA2BDiff));
		assertTrue(changeRefA2BDiff.getEquivalence().getDifferences().contains(changeRefB2ADiff));

		assertNotNull(changeRefC2DDiff.getEquivalence());
		assertSame(Integer.valueOf(2), changeRefC2DDiff.getEquivalence().getDifferences().size());
		assertTrue(changeRefC2DDiff.getEquivalence().getDifferences().contains(changeRefC2DDiff));
		assertTrue(changeRefC2DDiff.getEquivalence().getDifferences().contains(changeRefD2CDiff));

		assertNotNull(changeRefE2FDiff.getEquivalence());
		assertSame(Integer.valueOf(2), changeRefE2FDiff.getEquivalence().getDifferences().size());
		assertTrue(changeRefE2FDiff.getEquivalence().getDifferences().contains(changeRefE2FDiff));
		assertTrue(changeRefE2FDiff.getEquivalence().getDifferences().contains(changeRefF2EDiff));

	}

}

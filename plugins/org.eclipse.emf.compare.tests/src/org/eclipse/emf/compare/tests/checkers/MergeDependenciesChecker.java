package org.eclipse.emf.compare.tests.checkers;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.Sets;

import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.merge.DiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IDiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IMerger;

public class MergeDependenciesChecker {

	private IMerger.Registry registry;

	private Diff diff;

	private int nbMerges = 0;

	private int nbRejections = 0;

	private boolean rightToLeft = true;

	private MergeDependenciesChecker() {
		// Do nothing
	}

	private MergeDependenciesChecker(IMerger.Registry mergerRegistry, Diff testedDiff) {
		this.registry = mergerRegistry;
		this.diff = testedDiff;
	}

	public static MergeDependenciesChecker getDependenciesChecker(IMerger.Registry mergerRegistry,
			Diff testedDiff) {
		return new MergeDependenciesChecker(mergerRegistry, testedDiff);
	}

	public MergeDependenciesChecker implies(int nbResultingMerges) {
		this.nbMerges = nbResultingMerges;
		return this;
	}

	public MergeDependenciesChecker rejects(int nbResultingRejections) {
		this.nbRejections = nbResultingRejections;
		return this;
	}

	public MergeDependenciesChecker rightToLeft() {
		this.rightToLeft = true;
		return this;
	}

	public MergeDependenciesChecker leftToRight() {
		this.rightToLeft = false;
		return this;
	}

	public void check() {
		IDiffRelationshipComputer computer = new DiffRelationshipComputer(registry);
		Set<Diff> allResultingMerges = computer.getAllResultingMerges(diff, this.rightToLeft);
		Set<Diff> allResultingRejections = computer.getAllResultingRejections(diff, this.rightToLeft);
		assertEquals(this.nbMerges, Sets.difference(allResultingMerges, allResultingRejections).size());
		assertEquals(this.nbRejections, allResultingRejections.size());
	}
}

package org.eclipse.emf.compare.tests.checkers;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.internal.merge.MergeDependenciesUtil;
import org.eclipse.emf.compare.merge.IMerger;

@SuppressWarnings("restriction")
public class MergeDependenciesChecker {

	private IMerger.Registry registry;

	private Diff diff;

	private int nbMerges = 0;

	private int nbDeletions = 0;

	private boolean isFromRight = true;

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
		this.nbDeletions = nbResultingRejections;
		return this;
	}

	public MergeDependenciesChecker rightToLeft() {
		this.isFromRight = true;
		return this;
	}

	public MergeDependenciesChecker leftToRight() {
		this.isFromRight = false;
		return this;
	}

	public void check() {
		Set<Diff> allResultingMerges = MergeDependenciesUtil.getAllResultingMerges(diff, registry,
				this.isFromRight);
		Set<Diff> allResultingRejections = MergeDependenciesUtil.getAllResultingRejections(diff, registry,
				this.isFromRight);
		assertEquals(this.nbMerges, allResultingMerges.size());
		assertEquals(this.nbDeletions, allResultingRejections.size());
	}
}

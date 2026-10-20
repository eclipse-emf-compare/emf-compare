/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.fullcomparison;

import static org.eclipse.emf.compare.tests.framework.ComparisonSignatures.differences;
import static org.eclipse.emf.compare.tests.framework.ComparisonSignatures.matches;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.DiffBuilder;
import org.eclipse.emf.compare.match.eobject.WeightProvider;
import org.eclipse.emf.compare.match.eobject.WeightProviderDescriptorRegistryImpl;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.MatchingAlgorithm;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

/**
 * Measures the cost of proximity and content-hash matching on the same fixtures, with identifier matching
 * disabled. This diagnostic class is run separately as a JUnit test and is deliberately outside
 * {@code AllTests}; elapsed time is not a pass/fail criterion for the regression suite.
 * <p>
 * The cases extend {@link MatcherComparisonDiagnostic} with generated three-way models. Each case loads a
 * scope once, warms both implementations, alternates their execution order, and reports the median of the
 * measured runs. The timed interval includes match-engine construction and matching. Fixture loading,
 * difference computation and result-signature comparison are outside that interval.
 * </p>
 * <p>
 * The output reports match and difference totals together with the number of signatures shared by both
 * results, so a timing ratio can be read alongside any correspondence differences. Generated models also
 * assert the hash matcher's known partners and edit counts before reporting. Proximity can choose different
 * partners in the generated three-way cases, so this class does not require universal result equality.
 * </p>
 * <p>
 * Correctness on the shared regression fixtures is asserted by {@link MatcherComparisonDiagnostic} and the
 * focused matcher tests. These measurements provide fixture-specific timing observations, not a guaranteed
 * speedup on other models or machines.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@SuppressWarnings("nls")
public class ContentMatcherPerformanceTest extends MatcherComparisonDiagnostic {

	/** Unmeasured iterations used to warm both matcher implementations. */
	private static final int WARMUPS = 3;

	/** Measured iterations per matcher. */
	private static final int MEASUREMENTS = 5;

	/** Largest generated model retained in the timing diagnostics. */
	private static final int LARGE_MODEL_SIZE = 2000;

	/** Conversion factor for reporting elapsed milliseconds. */
	private static final double NANOS_PER_MILLISECOND = 1e6;

	/**
	 * Selects a fixture for repeatable matching-time measurements.
	 *
	 * @param fixture
	 *            the fixture name
	 * @param ancestor
	 *            the ancestor fixture suffix, or an empty string
	 * @param alignURIs
	 *            whether to give corresponding resources equal URIs
	 */
	public ContentMatcherPerformanceTest(String fixture, String ancestor, boolean alignURIs) {
		super(fixture, ancestor, alignURIs);
	}

	/**
	 * Retains all distance cases and both two-way and three-way generated timing cases.
	 *
	 * @return the performance fixtures
	 */
	@Parameters(name = "{0} {1} aligned={2}")
	public static Collection<Object[]> fixtures() {
		List<Object[]> cases = new ArrayList<>(MatcherComparisonDiagnostic.fixtures());
		for (int size : new int[] {100, 1000, LARGE_MODEL_SIZE }) {
			cases.add(new Object[] {"synthetic-" + size, "Origin", false });
		}
		return cases;
	}

	/**
	 * Reports median matching times and result overlap for the selected fixture. Only generated hash
	 * correspondence has fixed expectations here; neither elapsed time nor proximity agreement is asserted.
	 *
	 * @throws Exception
	 *             if the fixture cannot be loaded or matching fails
	 */
	@Override
	@Test
	public void compareMatchers() throws Exception {
		IComparisonScope scope = createScope();
		WeightProvider.Descriptor.Registry weights = WeightProviderDescriptorRegistryImpl
				.createStandaloneInstance();
		MatchingAlgorithm[] matchers = {MatchingAlgorithm.PROXIMITY, MatchingAlgorithm.CONTENT_HASH };
		long[][] elapsedNanosByMatcher = new long[matchers.length][MEASUREMENTS];
		Comparison[] comparisons = new Comparison[2];

		// Warm both implementations and alternate order to reduce systematic timing bias.
		for (int iteration = -WARMUPS; iteration < MEASUREMENTS; iteration++) {
			for (int turn = 0; turn < 2; turn++) {
				int matcherIndex = (iteration + turn) & 1;
				long matchStartNanos = System.nanoTime();
				comparisons[matcherIndex] = match(scope, weights, matchers[matcherIndex]);
				if (iteration >= 0) {
					elapsedNanosByMatcher[matcherIndex][iteration] = System.nanoTime() - matchStartNanos;
				}
			}
		}

		Comparison proximityResult = comparisons[0];
		Comparison hashResult = comparisons[1];

		// Validate/report the final results outside the timed interval; earlier iterations only warm/measure.
		for (Comparison comparison : comparisons) {
			new DefaultDiffEngine(new DiffBuilder()).diff(comparison, new BasicMonitor());
		}
		if (fixture.startsWith("synthetic-")) {
			// The generated models define identity; proximity can choose other partners in three-way edits.
			assertGeneratedCorrespondence(hashResult, (Resource)scope.getLeft(),
					(Resource)scope.getRight(), (Resource)scope.getOrigin());
		}

		// Include result overlap so a faster run with different partners is visible in the report.
		Map<List<Object>, Integer> proximityMatches = matches(proximityResult);
		Map<List<Object>, Integer> hashMatches = matches(hashResult);
		Map<List<Object>, Integer> proximityDifferences = differences(proximityResult);
		Map<List<Object>, Integer> hashDifferences = differences(hashResult);
		Arrays.sort(elapsedNanosByMatcher[0]);
		Arrays.sort(elapsedNanosByMatcher[1]);
		int medianIndex = MEASUREMENTS / 2;
		long proximityMedianNanos = elapsedNanosByMatcher[0][medianIndex];
		long hashMedianNanos = elapsedNanosByMatcher[1][medianIndex];
		System.out.printf(Locale.ROOT,
				"MATCHER,%s,%s,proximity_ms=%.3f,hash_ms=%.3f,speedup=%.2f,"
						+ "matches=%d/%d,common_matches=%d,diffs=%d/%d,common_diffs=%d%n",
				fixture, comparisonLabel(), proximityMedianNanos / NANOS_PER_MILLISECOND,
				hashMedianNanos / NANOS_PER_MILLISECOND,
				(double)proximityMedianNanos / hashMedianNanos, countSignatures(proximityMatches),
				countSignatures(hashMatches), countCommonSignatures(proximityMatches, hashMatches),
				countSignatures(proximityDifferences), countSignatures(hashDifferences),
				countCommonSignatures(proximityDifferences, hashDifferences));
	}

	/**
	 * Builds the timing label from the number of sides and URI alignment.
	 *
	 * @return the diagnostic comparison label
	 */
	private String comparisonLabel() {
		String label = "two-way";
		if (!ancestor.isEmpty()) {
			label = "three-way";
		}
		if (alignURIs) {
			label += "-aligned-uri";
		}
		return label;
	}

	/**
	 * Counts signatures including their multiplicities.
	 *
	 * @param signatureCounts
	 *            the occurrence count for each signature
	 * @return the total signature count
	 */
	private static int countSignatures(Map<List<Object>, Integer> signatureCounts) {
		return signatureCounts.values().stream().mapToInt(Integer::intValue).sum();
	}

	/**
	 * Counts signatures shared by both results including their multiplicities.
	 *
	 * @param first
	 *            the signature counts from the first matcher
	 * @param second
	 *            the signature counts from the second matcher
	 * @return the shared signature count
	 */
	private static int countCommonSignatures(Map<List<Object>, Integer> first,
			Map<List<Object>, Integer> second) {
		return first.entrySet().stream()
				.mapToInt(entry -> Math.min(entry.getValue(), second.getOrDefault(entry.getKey(), 0))).sum();
	}

}

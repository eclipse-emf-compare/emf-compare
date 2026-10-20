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
package org.eclipse.emf.compare.tests.monitor;

import static java.util.Collections.emptyIterator;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ComparisonCanceledException;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.equi.DefaultEquiEngine;
import org.eclipse.emf.compare.equi.IEquiEngine;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.req.DefaultReqEngine;
import org.eclipse.emf.compare.req.IReqEngine;
import org.eclipse.emf.compare.scope.AbstractComparisonScope;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractMatcherTest;
import org.eclipse.emf.compare.tests.framework.MatchingAlgorithm;
import org.eclipse.emf.compare.tests.monitor.data.MonitorCancelInputData;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.Test;

/**
 * Checks cancellation reporting and comparison stages using each configured matcher.
 *
 */
public class MonitorCancelTest extends AbstractMatcherTest {

	/**
	 * Selects the matcher for this execution.
	 *
	 * @param matcher
	 *            the matcher under test
	 */
	public MonitorCancelTest(MatchingAlgorithm matcher) {
		super(matcher);
	}

	/**
	 * Checks that the sending of a ComparisonCanceledException by the match engine causes a cancelled
	 * comparison to be returned.
	 *
	 * @throws IOException
	 *             if the input fixture cannot be loaded
	 */
	@Test
	public void testCancelDuringMatch() throws IOException {
		EMFCompare compare = matcher.builder(UseIdentifiers.WHEN_AVAILABLE).build();
		Comparison comparison = compare.compare(getNonEmptyTestScope(), getCanceledMonitor());
		assertEquals(Diagnostic.CANCEL, comparison.getDiagnostic().getSeverity());
	}

	/**
	 * Checks that the sending of a ComparisonCanceledException by the diff engine causes a cancelled
	 * comparison to be returned.
	 */
	@Test
	public void testCancelDuringDiff() {
		EMFCompare compare = matcher.builder(UseIdentifiers.WHEN_AVAILABLE).setDiffEngine(new IDiffEngine() {
			public void diff(Comparison comparison, Monitor monitor) {
				throw new ComparisonCanceledException();
			}
		}).build();
		Comparison comparison = compare.compare(getEmptyScope(), new BasicMonitor());
		assertEquals(Diagnostic.CANCEL, comparison.getDiagnostic().getSeverity());
	}

	/**
	 * Checks that the sending of a ComparisonCanceledException by the req engine causes a cancelled
	 * comparison to be returned.
	 */
	@Test
	public void testCancelDuringReq() {
		EMFCompare compare = matcher.builder(UseIdentifiers.WHEN_AVAILABLE)
				.setRequirementEngine(new IReqEngine() {
			public void computeRequirements(Comparison comparison, Monitor monitor) {
				throw new ComparisonCanceledException();
			}
		}).build();
		Comparison comparison = compare.compare(getEmptyScope(), new BasicMonitor());
		assertEquals(Diagnostic.CANCEL, comparison.getDiagnostic().getSeverity());
	}

	/**
	 * Checks that the sending of a ComparisonCanceledException by the equivalence engine causes a cancelled
	 * comparison to be returned.
	 */
	@Test
	public void testCancelDuringEqui() {
		EMFCompare compare = matcher.builder(UseIdentifiers.WHEN_AVAILABLE)
				.setEquivalenceEngine(new IEquiEngine() {
			public void computeEquivalences(Comparison comparison, Monitor monitor) {
				throw new ComparisonCanceledException();
			}
		}).build();
		Comparison comparison = compare.compare(getEmptyScope(), new BasicMonitor());
		assertEquals(Diagnostic.CANCEL, comparison.getDiagnostic().getSeverity());
	}

	@Test(expected = ComparisonCanceledException.class)
	public void testMonitorCancelInMatch() throws IOException {
		IMatchEngine engine = matcher.createMatchEngine(UseIdentifiers.WHEN_AVAILABLE);
		engine.match(getNonEmptyTestScope(), getCanceledMonitor());
	}

	@Test(expected = ComparisonCanceledException.class)
	public void testMonitorCancelInDiff() throws IOException {
		IDiffEngine diffEngine = new DefaultDiffEngine();
		diffEngine.diff(createMatchedComparison(), getCanceledMonitor());
	}

	@Test(expected = ComparisonCanceledException.class)
	public void testMonitorCancelInReq() throws IOException {
		IReqEngine reqEngine = new DefaultReqEngine();
		reqEngine.computeRequirements(createComparisonWithDifferences(), getCanceledMonitor());
	}

	@Test(expected = ComparisonCanceledException.class)
	public void testMonitorCancelInEqui() throws IOException {
		IEquiEngine equiEngine = new DefaultEquiEngine();
		equiEngine.computeEquivalences(createComparisonWithDifferences(), getCanceledMonitor());
	}

	/**
	 * Creates the matched input needed by the diff cancellation test.
	 *
	 * @return the comparison containing matches
	 * @throws IOException
	 *             if the input fixture cannot be loaded
	 */
	private Comparison createMatchedComparison() throws IOException {
		return matcher.createMatchEngine(UseIdentifiers.WHEN_AVAILABLE)
				.match(getNonEmptyTestScope(), new BasicMonitor());
	}

	/**
	 * Creates the differences needed by requirement and equivalence cancellation tests.
	 *
	 * @return the comparison containing differences
	 * @throws IOException
	 *             if the input fixture cannot be loaded
	 */
	private Comparison createComparisonWithDifferences() throws IOException {
		Comparison comparison = createMatchedComparison();
		new DefaultDiffEngine().diff(comparison, new BasicMonitor());
		return comparison;
	}

	private BasicMonitor getCanceledMonitor() {
		BasicMonitor monitor = new BasicMonitor();
		monitor.setCanceled(true);
		return monitor;
	}

	private IComparisonScope getNonEmptyTestScope() throws IOException {
		final MonitorCancelInputData testData = new MonitorCancelInputData();
		return new DefaultComparisonScope(testData.getLeft(), testData.getRight(), testData.getOrigin());
	}

	private IComparisonScope getEmptyScope() {
		return new AbstractComparisonScope(null, null, null) {

			public Iterator<? extends Resource> getCoveredResources(ResourceSet resourceSet) {
				return emptyIterator();
			}

			public Iterator<? extends EObject> getCoveredEObjects(Resource resource) {
				return emptyIterator();
			}

			public Iterator<? extends EObject> getChildren(EObject eObject) {
				return emptyIterator();
			}
		};
	}

}

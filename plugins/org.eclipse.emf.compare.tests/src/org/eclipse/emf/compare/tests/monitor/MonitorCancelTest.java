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

import static org.junit.Assert.assertEquals;

import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine.Factory;
import org.eclipse.emf.compare.req.DefaultReqEngine;
import org.eclipse.emf.compare.req.IReqEngine;
import org.eclipse.emf.compare.scope.AbstractComparisonScope;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.monitor.data.MonitorCancelInputData;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.Before;
import org.junit.Test;

public class MonitorCancelTest {

	private Comparison fComparison;

	private Comparison fComparisonWithDiff;

	/**
	 * Checks that the sending of a ComparisonCanceledException by the match engine causes a cancelled
	 * comparison to be returned.
	 */
	@Test
	public void testCancelDuringMatch() {
		EMFCompare compare = EMFCompare.builder().setMatchEngineFactoryRegistry(TestRegistry.INSTANCE)
				.build();
		Comparison comparison = compare.compare(getEmptyScope(), new BasicMonitor());
		assertEquals(Diagnostic.CANCEL, comparison.getDiagnostic().getSeverity());
	}

	/**
	 * Checks that the sending of a ComparisonCanceledException by the diff engine causes a cancelled
	 * comparison to be returned.
	 */
	@Test
	public void testCancelDuringDiff() {
		EMFCompare compare = EMFCompare.builder().setDiffEngine(new IDiffEngine() {
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
		EMFCompare compare = EMFCompare.builder().setRequirementEngine(new IReqEngine() {
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
		EMFCompare compare = EMFCompare.builder().setEquivalenceEngine(new IEquiEngine() {
			public void computeEquivalences(Comparison comparison, Monitor monitor) {
				throw new ComparisonCanceledException();
			}
		}).build();
		Comparison comparison = compare.compare(getEmptyScope(), new BasicMonitor());
		assertEquals(Diagnostic.CANCEL, comparison.getDiagnostic().getSeverity());
	}

	@Test(expected = ComparisonCanceledException.class)
	public void testMonitorCancelInMatch() throws IOException {
		IMatchEngine engine = DefaultMatchEngine.create(UseIdentifiers.WHEN_AVAILABLE);
		engine.match(getNonEmptyTestScope(), getCanceledMonitor());
	}

	@Test(expected = ComparisonCanceledException.class)
	public void testMonitorCancelInDiff() throws IOException {
		IDiffEngine diffEngine = new DefaultDiffEngine();
		diffEngine.diff(fComparison, getCanceledMonitor());
	}

	@Test(expected = ComparisonCanceledException.class)
	public void testMonitorCancelInReq() throws IOException {
		IReqEngine reqEngine = new DefaultReqEngine();
		reqEngine.computeRequirements(fComparisonWithDiff, getCanceledMonitor());
	}

	@Test(expected = ComparisonCanceledException.class)
	public void testMonitorCancelInEqui() throws IOException {
		IEquiEngine equiEngine = new DefaultEquiEngine();
		equiEngine.computeEquivalences(fComparisonWithDiff, getCanceledMonitor());
	}

	@Before
	public void setUp() throws IOException {
		IMatchEngine matchEngine = DefaultMatchEngine.create(UseIdentifiers.WHEN_AVAILABLE);
		fComparison = matchEngine.match(getNonEmptyTestScope(), new BasicMonitor());
		fComparisonWithDiff = matchEngine.match(getNonEmptyTestScope(), new BasicMonitor());
		IDiffEngine diffEngine = new DefaultDiffEngine();
		diffEngine.diff(fComparisonWithDiff, new BasicMonitor());
	}

	private BasicMonitor getCanceledMonitor() {
		return new BasicMonitor() {
			@Override
			public boolean isCanceled() {
				return true;
			}
		};
	}

	private IComparisonScope getNonEmptyTestScope() throws IOException {
		final MonitorCancelInputData testData = new MonitorCancelInputData();
		return new DefaultComparisonScope(testData.getLeft(), testData.getRight(), testData.getOrigin());
	}

	private IComparisonScope getEmptyScope() {
		return new AbstractComparisonScope(null, null, null) {

			public Iterator<? extends Resource> getCoveredResources(ResourceSet resourceSet) {
				return Iterators.emptyIterator();
			}

			public Iterator<? extends EObject> getCoveredEObjects(Resource resource) {
				return Iterators.emptyIterator();
			}

			public Iterator<? extends EObject> getChildren(EObject eObject) {
				return Iterators.emptyIterator();
			}
		};
	}

	/**
	 * A match engine to use for tests, which systematically throws a ComparisonCanceledException.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static final class CanceledMatchEngine implements IMatchEngine {

		private static final CanceledMatchEngine INSTANCE = new CanceledMatchEngine();

		public Comparison match(IComparisonScope scope, Monitor monitor) {
			throw new ComparisonCanceledException();
		}

	}

	/**
	 * A factory for tests that provides a CanceledMatchEngine.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a
	 */
	private static final class TestFactory implements IMatchEngine.Factory {

		private static final TestFactory INSTANCE = new TestFactory();

		public IMatchEngine getMatchEngine() {
			return CanceledMatchEngine.INSTANCE;
		}

		public int getRanking() {
			return 0;
		}

		public void setRanking(int parseInt) {
			// Intentionally blank
		}

		public boolean isMatchEngineFactoryFor(IComparisonScope scope) {
			return true;
		}

	}

	/**
	 * A registry for tests that provides a TestFactory.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static final class TestRegistry implements IMatchEngine.Factory.Registry {

		private static final TestRegistry INSTANCE = new TestRegistry();

		public Factory getHighestRankingMatchEngineFactory(IComparisonScope scope) {
			return TestFactory.INSTANCE;
		}

		public List<Factory> getMatchEngineFactories(IComparisonScope scope) {
			return Arrays.<Factory> asList(TestFactory.INSTANCE);
		}

		public Factory add(Factory matchEngineFactory) {
			throw new RuntimeException();
		}

		public Factory remove(String className) {
			return null;
		}

		public void clear() {
			// Intentionally blank
		}

	}
}

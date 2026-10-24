/**
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.fragmentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.fragmentation.data.FragmentationInputData;
import org.eclipse.emf.compare.tests.framework.AbstractMatcherTest;
import org.eclipse.emf.compare.tests.framework.MatcherKind;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

/**
 * Tests resource attachment changes with both matchers.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class ResourceAttachmentChangeBug492261 extends AbstractMatcherTest {

	private final FragmentationInputData input = new FragmentationInputData();

	private final BatchMerger batchMerger = new BatchMerger(IMerger.RegistryImpl.createStandaloneInstance());

	/**
	 * Selects the matcher for this execution.
	 *
	 * @param matcher
	 *            the matcher under test
	 */
	public ResourceAttachmentChangeBug492261(MatcherKind matcher) {
		super(matcher);
	}

	@Test
	public void testUncontrolRootResourceImplication_1() throws IOException {
		final Resource left = input.getRootElementImplicationLeft();
		final Resource origin = input.getRootElementImplicationUncontrolOrigin();
		final Resource right = input.getRootElementImplicationRight();

		final ResourceSet leftSet = left.getResourceSet();
		final ResourceSet originSet = origin.getResourceSet();
		final ResourceSet rightSet = right.getResourceSet();

		assertNotNull(leftSet);
		assertNotNull(originSet);
		assertNotNull(rightSet);

		EcoreUtil.resolveAll(leftSet);
		EcoreUtil.resolveAll(originSet);
		EcoreUtil.resolveAll(rightSet);

		assertEquals(1, leftSet.getResources().size());
		assertEquals(2, originSet.getResources().size());
		assertEquals(2, rightSet.getResources().size());

		mergeUncontrolDiffsAndAssertResult(leftSet, originSet, rightSet, true);
	}

	@Test
	public void testUncontrolRootResourceImplication_2() throws IOException {
		final Resource left = input.getRootElementImplicationRight();
		final Resource origin = input.getRootElementImplicationUncontrolOrigin();
		final Resource right = input.getRootElementImplicationLeft();

		final ResourceSet leftSet = left.getResourceSet();
		final ResourceSet originSet = origin.getResourceSet();
		final ResourceSet rightSet = right.getResourceSet();

		assertNotNull(leftSet);
		assertNotNull(originSet);
		assertNotNull(rightSet);

		EcoreUtil.resolveAll(leftSet);
		EcoreUtil.resolveAll(originSet);
		EcoreUtil.resolveAll(rightSet);

		assertEquals(2, leftSet.getResources().size());
		assertEquals(2, originSet.getResources().size());
		assertEquals(1, rightSet.getResources().size());

		mergeUncontrolDiffsAndAssertResult(leftSet, originSet, rightSet, false);
	}

	@Test
	public void testControlRootResourceImplication_1() throws IOException {
		final Resource left = input.getRootElementImplicationLeft();
		final Resource origin = input.getRootElementImplicationControlOrigin();
		final Resource right = input.getRootElementImplicationRight();

		final ResourceSet leftSet = left.getResourceSet();
		final ResourceSet originSet = origin.getResourceSet();
		final ResourceSet rightSet = right.getResourceSet();

		assertNotNull(leftSet);
		assertNotNull(originSet);
		assertNotNull(rightSet);

		EcoreUtil.resolveAll(leftSet);
		EcoreUtil.resolveAll(originSet);
		EcoreUtil.resolveAll(rightSet);

		assertEquals(1, leftSet.getResources().size());
		assertEquals(1, originSet.getResources().size());
		assertEquals(2, rightSet.getResources().size());

		mergeControlDiffsAndAssertResult(leftSet, originSet, rightSet, false);
	}

	@Test
	public void testControlRootResourceImplication_2() throws IOException {
		final Resource left = input.getRootElementImplicationRight();
		final Resource origin = input.getRootElementImplicationControlOrigin();
		final Resource right = input.getRootElementImplicationLeft();

		final ResourceSet leftSet = left.getResourceSet();
		final ResourceSet originSet = origin.getResourceSet();
		final ResourceSet rightSet = right.getResourceSet();

		assertNotNull(leftSet);
		assertNotNull(originSet);
		assertNotNull(rightSet);

		EcoreUtil.resolveAll(leftSet);
		EcoreUtil.resolveAll(originSet);
		EcoreUtil.resolveAll(rightSet);

		assertEquals(2, leftSet.getResources().size());
		assertEquals(1, originSet.getResources().size());
		assertEquals(1, rightSet.getResources().size());

		mergeControlDiffsAndAssertResult(leftSet, originSet, rightSet, true);
	}

	private void mergeUncontrolDiffsAndAssertResult(final ResourceSet leftSet, final ResourceSet originSet,
			final ResourceSet rightSet, boolean fragmentedOnLeft) {
		final IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = matcher.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		Collection<Diff> racFilter = Collections2.filter(differences,
				Predicates.instanceOf(ResourceAttachmentChange.class));
		Collection<Diff> rcFilter = Collections2.filter(differences,
				Predicates.instanceOf(ReferenceChange.class));
		assertEquals(1, racFilter.size());
		assertEquals(2, rcFilter.size());
		Diff rac = racFilter.iterator().next();
		Diff container = null;
		Diff innerNode = null;
		for (Diff diff : rcFilter) {
			if (fragmentedOnLeft && diff.getMatch().getLeft() == null) {
				innerNode = diff;
			} else if (!fragmentedOnLeft && diff.getMatch().getRight() == null) {
				innerNode = diff;
			} else {
				container = diff;
			}
		}

		assertNotNull(container);
		assertNotNull(innerNode);

		if (fragmentedOnLeft) {
			batchMerger.copyAllRightToLeft(Arrays.asList(rac), new BasicMonitor());
		} else {
			batchMerger.copyAllLeftToRight(Arrays.asList(rac), new BasicMonitor());
		}
		assertEquals(DifferenceState.DISCARDED, rac.getState());
		assertEquals(DifferenceState.DISCARDED, container.getState());
		assertEquals(DifferenceState.UNRESOLVED, innerNode.getState());

		if (fragmentedOnLeft) {
			batchMerger.copyAllRightToLeft(Arrays.asList(innerNode), new BasicMonitor());
		} else {
			batchMerger.copyAllLeftToRight(Arrays.asList(innerNode), new BasicMonitor());
		}
		assertEquals(DifferenceState.DISCARDED, rac.getState());
		assertEquals(DifferenceState.DISCARDED, container.getState());
		assertEquals(DifferenceState.DISCARDED, innerNode.getState());

		comparison = matcher.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	private void mergeControlDiffsAndAssertResult(final ResourceSet leftSet, final ResourceSet originSet,
			final ResourceSet rightSet, boolean fragmentedOnLeft) {
		final IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = matcher.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		Collection<Diff> racFilter = Collections2.filter(differences,
				Predicates.instanceOf(ResourceAttachmentChange.class));
		Collection<Diff> rcFilter = Collections2.filter(differences,
				Predicates.instanceOf(ReferenceChange.class));
		assertEquals(1, racFilter.size());
		assertEquals(2, rcFilter.size());
		Diff rac = racFilter.iterator().next();
		Diff container = null;
		Diff innerNode = null;
		for (Diff diff : rcFilter) {
			if (fragmentedOnLeft && diff.getMatch().getRight() == null) {
				innerNode = diff;
			} else if (!fragmentedOnLeft && diff.getMatch().getLeft() == null) {
				innerNode = diff;
			} else {
				container = diff;
			}
		}

		assertNotNull(container);
		assertNotNull(innerNode);

		if (fragmentedOnLeft) {
			batchMerger.copyAllRightToLeft(Arrays.asList(rac), new BasicMonitor());
		} else {
			batchMerger.copyAllLeftToRight(Arrays.asList(rac), new BasicMonitor());
		}
		assertEquals(DifferenceState.DISCARDED, rac.getState());
		assertEquals(DifferenceState.UNRESOLVED, container.getState());
		assertEquals(DifferenceState.UNRESOLVED, innerNode.getState());

		if (fragmentedOnLeft) {
			batchMerger.copyAllRightToLeft(Arrays.asList(container), new BasicMonitor());
		} else {
			batchMerger.copyAllLeftToRight(Arrays.asList(container), new BasicMonitor());
		}
		assertEquals(DifferenceState.DISCARDED, rac.getState());
		assertEquals(DifferenceState.DISCARDED, container.getState());
		assertEquals(DifferenceState.DISCARDED, innerNode.getState());

		comparison = matcher.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

}

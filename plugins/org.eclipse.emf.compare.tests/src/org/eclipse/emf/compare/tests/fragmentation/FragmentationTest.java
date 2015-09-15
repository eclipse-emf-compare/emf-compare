/**
 * Copyright (c) 2012, 2014 Obeo.
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.fragmentation.data.FragmentationInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("nls")
public class FragmentationTest {
	private final FragmentationInputData input = new FragmentationInputData();

	private final IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	@Test
	public void testUncontroledObjectResourceSet() throws IOException {
		final Resource left = input.getUncontrolLeft();
		final Resource origin = input.getUncontrolOrigin();
		final Resource right = input.getUncontrolRight();

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

		final IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final Diff diff = differences.get(0);
		assertTrue(diff instanceof ResourceAttachmentChange);
		assertEquals(diff.getMatch().getLeft(), getNodeNamed(leftSet, "fragmented"));
		assertEquals(diff.getMatch().getRight(), getNodeNamed(rightSet, "fragmented"));
		assertEquals(diff.getMatch().getOrigin(), getNodeNamed(originSet, "fragmented"));
		assertSame(diff.getSource(), DifferenceSource.LEFT);
		assertSame(diff.getKind(), DifferenceKind.DELETE);
	}

	// This only tests the merge. Will fail if testUncontroledObjectResourceSet does.
	@Test
	public void testMergeUncontroledObjectResourceSetLtR() throws IOException {
		final Resource left = input.getUncontrolLeft();
		final Resource origin = input.getUncontrolOrigin();
		final Resource right = input.getUncontrolRight();

		final ResourceSet leftSet = left.getResourceSet();
		final ResourceSet originSet = origin.getResourceSet();
		final ResourceSet rightSet = right.getResourceSet();

		EcoreUtil.resolveAll(leftSet);
		EcoreUtil.resolveAll(originSet);
		EcoreUtil.resolveAll(rightSet);

		final IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final Diff diff = differences.get(0);

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		assertEquals(1, leftSet.getResources().size());
		assertEquals(2, originSet.getResources().size());
		// Still two resources, though one is empty
		assertEquals(2, rightSet.getResources().size());

		for (Resource resource : rightSet.getResources()) {
			if (resource != right) {
				assertTrue(resource.getContents().isEmpty());
			}
		}

		final EObject leftFragmentedNode = getNodeNamed(leftSet, "fragmented");
		final EObject originFragmentedNode = getNodeNamed(originSet, "fragmented");
		final EObject rightFragmentedNode = getNodeNamed(rightSet, "fragmented");

		assertTrue(leftFragmentedNode.eContainer().eResource() == leftFragmentedNode.eResource());
		assertTrue(originFragmentedNode.eContainer().eResource() != originFragmentedNode.eResource());
		assertTrue(rightFragmentedNode.eContainer().eResource() == rightFragmentedNode.eResource());

		// there should be no diff between left and right
		final Comparison lrCompare = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(leftSet, rightSet, null));
		assertEquals(0, lrCompare.getDifferences().size());

		// but there should be two diffs (a pseudo conflict resource change) when compared with origin
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
		assertEquals(1, comparison.getConflicts().size());
		assertSame(ConflictKind.PSEUDO, comparison.getConflicts().get(0).getKind());
	}

	// This only tests the merge. Will fail if testUncontroledObjectResourceSet does.
	// TODO will fail until bug 406101 is fixed
	@Ignore
	@Test
	public void testMergeUncontroledObjectResourceSetRtL() throws IOException {
		final Resource left = input.getUncontrolLeft();
		final Resource origin = input.getUncontrolOrigin();
		final Resource right = input.getUncontrolRight();

		final ResourceSet leftSet = left.getResourceSet();
		final ResourceSet originSet = origin.getResourceSet();
		final ResourceSet rightSet = right.getResourceSet();

		EcoreUtil.resolveAll(leftSet);
		EcoreUtil.resolveAll(originSet);
		EcoreUtil.resolveAll(rightSet);

		final IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final Diff diff = differences.get(0);

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		assertEquals(2, leftSet.getResources().size());
		assertEquals(2, originSet.getResources().size());
		assertEquals(2, rightSet.getResources().size());

		final EObject leftFragmentedNode = getNodeNamed(leftSet, "fragmented");
		final EObject originFragmentedNode = getNodeNamed(originSet, "fragmented");
		final EObject rightFragmentedNode = getNodeNamed(rightSet, "fragmented");

		assertTrue(leftFragmentedNode.eContainer().eResource() != leftFragmentedNode.eResource());
		assertTrue(originFragmentedNode.eContainer().eResource() != originFragmentedNode.eResource());
		assertTrue(rightFragmentedNode.eContainer().eResource() != rightFragmentedNode.eResource());

		// there should be no diff between left and right
		final Comparison lrCompare = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(leftSet, rightSet, null));
		assertTrue(lrCompare.getDifferences().isEmpty());

		// And since we've undone the resource change, there are no diffs with origin either
		comparison = EMFCompare.builder().build().compare(scope);
		assertTrue(comparison.getDifferences().isEmpty());
	}

	@Test
	public void testUncontroledObjectResource() throws IOException {
		final Resource left = input.getUncontrolLeft();
		final Resource origin = input.getUncontrolOrigin();
		final Resource right = input.getUncontrolRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// At the resource level, we cannot match an object with the 'proxy' root.
		final List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		// There's one addition, and one deletion (the proxy root)
		final Diff addition;
		final Diff deletion;
		if (differences.get(0).getKind() == DifferenceKind.ADD) {
			addition = differences.get(0);
			deletion = differences.get(1);
		} else {
			deletion = differences.get(0);
			addition = differences.get(1);
		}
		assertTrue(addition instanceof ReferenceChange);
		assertEquals(addition.getMatch().getLeft(), getNodeNamed(left, "root"));
		assertEquals(addition.getMatch().getRight(), getNodeNamed(right, "root"));
		assertEquals(addition.getMatch().getOrigin(), getNodeNamed(origin, "root"));
		assertEquals(((ReferenceChange)addition).getValue(), getNodeNamed(left, "fragmented"));
		assertSame(addition.getSource(), DifferenceSource.LEFT);
		assertSame(addition.getKind(), DifferenceKind.ADD);

		assertTrue(deletion instanceof ReferenceChange);
		assertEquals(deletion.getMatch().getLeft(), getNodeNamed(left, "root"));
		assertEquals(deletion.getMatch().getRight(), getNodeNamed(right, "root"));
		assertEquals(deletion.getMatch().getOrigin(), getNodeNamed(origin, "root"));
		assertTrue(((ReferenceChange)deletion).getValue().eIsProxy());
		assertSame(deletion.getSource(), DifferenceSource.LEFT);
		assertSame(deletion.getKind(), DifferenceKind.DELETE);
	}

	// This only tests the merge. Will fail if testUncontroledObjectResource does.
	@Test
	public void testMergeUncontroledObjectResourceLtR() throws IOException {
		final Resource left = input.getUncontrolLeft();
		final Resource origin = input.getUncontrolOrigin();
		final Resource right = input.getUncontrolRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllLeftToRight(differences, new BasicMonitor());

		final EObject leftFragmentedNode = getNodeNamed(left, "fragmented");
		final EObject originFragmentedNode = getNodeNamed(origin, "fragmented");
		final EObject rightFragmentedNode = getNodeNamed(right, "fragmented");

		assertTrue(leftFragmentedNode.eContainer().eResource() == leftFragmentedNode.eResource());
		assertNull(originFragmentedNode);
		assertTrue(rightFragmentedNode.eContainer().eResource() == rightFragmentedNode.eResource());

		final List<EObject> leftRootContent = ((InternalEList<EObject>)getNodeNamed(left, "root").eContents())
				.basicList();
		final List<EObject> originRootContent = ((InternalEList<EObject>)getNodeNamed(origin, "root")
				.eContents()).basicList();
		final List<EObject> rightRootContent = ((InternalEList<EObject>)getNodeNamed(right, "root")
				.eContents()).basicList();

		assertEquals(1, leftRootContent.size());
		assertEquals(1, originRootContent.size());
		assertEquals(1, rightRootContent.size());

		assertTrue(leftRootContent.get(0) == leftFragmentedNode);
		assertTrue(originRootContent.get(0).eIsProxy());
		assertTrue(rightRootContent.get(0) == rightFragmentedNode);

		// there should be no diff between left and right
		final Comparison lrCompare = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(left, right, null));
		assertEquals(0, lrCompare.getDifferences().size());

		// but there should be four diffs (conflicting 2-by-2) when compared with origin :
		// two deletions (the proxies) and two additions (the previously fragmented root)
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
		assertEquals(2, comparison.getConflicts().size());
		assertSame(ConflictKind.PSEUDO, comparison.getConflicts().get(0).getKind());
		assertSame(ConflictKind.PSEUDO, comparison.getConflicts().get(1).getKind());
	}

	// This only tests the merge. Will fail if testUncontroledObjectResource does.
	@Test
	public void testMergeUncontroledObjectResourceRtL() throws IOException {
		final Resource left = input.getUncontrolLeft();
		final Resource origin = input.getUncontrolOrigin();
		final Resource right = input.getUncontrolRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllRightToLeft(differences, new BasicMonitor());

		final EObject leftFragmentedNode = getNodeNamed(left, "fragmented");
		final EObject originFragmentedNode = getNodeNamed(origin, "fragmented");
		final EObject rightFragmentedNode = getNodeNamed(right, "fragmented");

		assertNull(leftFragmentedNode);
		assertNull(originFragmentedNode);
		assertNull(rightFragmentedNode);

		final List<EObject> leftRootContent = ((InternalEList<EObject>)getNodeNamed(left, "root").eContents())
				.basicList();
		final List<EObject> originRootContent = ((InternalEList<EObject>)getNodeNamed(origin, "root")
				.eContents()).basicList();
		final List<EObject> rightRootContent = ((InternalEList<EObject>)getNodeNamed(right, "root")
				.eContents()).basicList();

		assertEquals(1, leftRootContent.size());
		assertEquals(1, originRootContent.size());
		assertEquals(1, rightRootContent.size());

		assertTrue(leftRootContent.get(0).eIsProxy());
		assertTrue(originRootContent.get(0).eIsProxy());
		assertTrue(rightRootContent.get(0).eIsProxy());

		// there should be no diff between left and right
		final Comparison lrCompare = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(left, right, null));
		assertEquals(0, lrCompare.getDifferences().size());

		// And since we've undone the changes, there are no diffs with origin either
		comparison = EMFCompare.builder().build().compare(scope);
		assertTrue(comparison.getDifferences().isEmpty());
	}

	@Test
	public void testControledObjectResourceSet() throws IOException {
		final Resource left = input.getControlLeft();
		final Resource origin = input.getControlOrigin();
		final Resource right = input.getControlRight();

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

		final IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final Diff diff = differences.get(0);
		assertTrue(diff instanceof ResourceAttachmentChange);
		assertEquals(diff.getMatch().getLeft(), getNodeNamed(leftSet, "fragmented"));
		assertEquals(diff.getMatch().getRight(), getNodeNamed(rightSet, "fragmented"));
		assertEquals(diff.getMatch().getOrigin(), getNodeNamed(originSet, "fragmented"));
		assertSame(diff.getSource(), DifferenceSource.LEFT);
		assertSame(diff.getKind(), DifferenceKind.ADD);
	}

	// This only tests the merge. Will fail if testControledObjectResourceSet does.
	// TODO will fail until bug 406101 is fixed
	@Ignore
	@Test
	public void testMergeControledObjectResourceSetLtR() throws IOException {
		final Resource left = input.getControlLeft();
		final Resource origin = input.getControlOrigin();
		final Resource right = input.getControlRight();

		final ResourceSet leftSet = left.getResourceSet();
		final ResourceSet originSet = origin.getResourceSet();
		final ResourceSet rightSet = right.getResourceSet();

		EcoreUtil.resolveAll(leftSet);
		EcoreUtil.resolveAll(originSet);
		EcoreUtil.resolveAll(rightSet);

		final IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final Diff diff = differences.get(0);

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		assertEquals(2, leftSet.getResources().size());
		assertEquals(1, originSet.getResources().size());
		assertEquals(2, rightSet.getResources().size());

		final EObject leftFragmentedNode = getNodeNamed(leftSet, "fragmented");
		final EObject originFragmentedNode = getNodeNamed(originSet, "fragmented");
		final EObject rightFragmentedNode = getNodeNamed(rightSet, "fragmented");

		assertTrue(leftFragmentedNode.eContainer().eResource() != leftFragmentedNode.eResource());
		assertTrue(originFragmentedNode.eContainer().eResource() == originFragmentedNode.eResource());
		assertTrue(rightFragmentedNode.eContainer().eResource() != rightFragmentedNode.eResource());

		// there should be no diff between left and right
		final Comparison lrCompare = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(leftSet, rightSet, null));
		assertEquals(0, lrCompare.getDifferences().size());

		// but there should be two diffs (a pseudo conflict resource change) when compared with origin
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
		assertEquals(1, comparison.getConflicts().size());
		assertSame(ConflictKind.PSEUDO, comparison.getConflicts().get(0).getKind());
	}

	// This only tests the merge. Will fail if testControledObjectResourceSet does.
	@Test
	public void testMergeControledObjectResourceSetRtL() throws IOException {
		final Resource left = input.getControlLeft();
		final Resource origin = input.getControlOrigin();
		final Resource right = input.getControlRight();

		final ResourceSet leftSet = left.getResourceSet();
		final ResourceSet originSet = origin.getResourceSet();
		final ResourceSet rightSet = right.getResourceSet();

		EcoreUtil.resolveAll(leftSet);
		EcoreUtil.resolveAll(originSet);
		EcoreUtil.resolveAll(rightSet);

		final IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final Diff diff = differences.get(0);

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// Note : we still have 2 resources there, though one is empty
		assertEquals(2, leftSet.getResources().size());
		assertEquals(1, originSet.getResources().size());
		assertEquals(1, rightSet.getResources().size());

		for (Resource res : leftSet.getResources()) {
			if (res != left) {
				assertTrue(res.getContents().isEmpty());
			}
		}

		final EObject leftFragmentedNode = getNodeNamed(leftSet, "fragmented");
		final EObject originFragmentedNode = getNodeNamed(originSet, "fragmented");
		final EObject rightFragmentedNode = getNodeNamed(rightSet, "fragmented");

		assertTrue(leftFragmentedNode.eContainer().eResource() == leftFragmentedNode.eResource());
		assertTrue(originFragmentedNode.eContainer().eResource() == originFragmentedNode.eResource());
		assertTrue(rightFragmentedNode.eContainer().eResource() == rightFragmentedNode.eResource());

		// there should be no diff between left and right
		final Comparison lrCompare = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(leftSet, rightSet, null));
		assertTrue(lrCompare.getDifferences().isEmpty());

		// And since we've undone the resource change, there are no diffs with origin either
		comparison = EMFCompare.builder().build().compare(scope);
		assertTrue(comparison.getDifferences().isEmpty());
	}

	@Test
	public void testControledObjectResource() throws IOException {
		final Resource left = input.getControlLeft();
		final Resource origin = input.getControlOrigin();
		final Resource right = input.getControlRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// At the resource level, we cannot match an object with the 'proxy' root.
		final List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		// There's one deletion, and one addition (the proxy root)
		final Diff addition;
		final Diff deletion;
		if (differences.get(0).getKind() == DifferenceKind.ADD) {
			addition = differences.get(0);
			deletion = differences.get(1);
		} else {
			deletion = differences.get(0);
			addition = differences.get(1);
		}
		assertTrue(addition instanceof ReferenceChange);
		assertEquals(addition.getMatch().getLeft(), getNodeNamed(left, "root"));
		assertEquals(addition.getMatch().getRight(), getNodeNamed(right, "root"));
		assertEquals(addition.getMatch().getOrigin(), getNodeNamed(origin, "root"));
		assertTrue(((ReferenceChange)addition).getValue().eIsProxy());
		assertSame(addition.getSource(), DifferenceSource.LEFT);
		assertSame(addition.getKind(), DifferenceKind.ADD);

		assertTrue(deletion instanceof ReferenceChange);
		assertEquals(deletion.getMatch().getLeft(), getNodeNamed(left, "root"));
		assertEquals(deletion.getMatch().getRight(), getNodeNamed(right, "root"));
		assertEquals(deletion.getMatch().getOrigin(), getNodeNamed(origin, "root"));
		assertEquals(((ReferenceChange)deletion).getValue(), getNodeNamed(origin, "fragmented"));
		assertSame(deletion.getSource(), DifferenceSource.LEFT);
		assertSame(deletion.getKind(), DifferenceKind.DELETE);
	}

	// This only tests the merge. Will fail if testControledObjectResource does.
	@Test
	public void testMergeControledObjectResourceLtR() throws IOException {
		final Resource left = input.getControlLeft();
		final Resource origin = input.getControlOrigin();
		final Resource right = input.getControlRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllLeftToRight(differences, new BasicMonitor());

		final EObject leftFragmentedNode = getNodeNamed(left, "fragmented");
		final EObject originFragmentedNode = getNodeNamed(origin, "fragmented");
		final EObject rightFragmentedNode = getNodeNamed(right, "fragmented");

		assertNull(leftFragmentedNode);
		assertTrue(originFragmentedNode.eContainer().eResource() == originFragmentedNode.eResource());
		assertNull(rightFragmentedNode);

		final List<EObject> leftRootContent = ((InternalEList<EObject>)getNodeNamed(left, "root").eContents())
				.basicList();
		final List<EObject> originRootContent = ((InternalEList<EObject>)getNodeNamed(origin, "root")
				.eContents()).basicList();
		final List<EObject> rightRootContent = ((InternalEList<EObject>)getNodeNamed(right, "root")
				.eContents()).basicList();

		assertEquals(1, leftRootContent.size());
		assertEquals(1, originRootContent.size());
		assertEquals(1, rightRootContent.size());

		assertTrue(leftRootContent.get(0).eIsProxy());
		assertTrue(originRootContent.get(0) == originFragmentedNode);
		assertTrue(rightRootContent.get(0).eIsProxy());

		// there should be no diff between left and right
		final Comparison lrCompare = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(left, right, null));
		assertEquals(0, lrCompare.getDifferences().size());

		// but there should be four diffs (conflicting 2-by-2) when compared with origin :
		// two additions (the proxies) and two deletions (the old root)
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
		assertEquals(2, comparison.getConflicts().size());
		assertSame(ConflictKind.PSEUDO, comparison.getConflicts().get(0).getKind());
		assertSame(ConflictKind.PSEUDO, comparison.getConflicts().get(1).getKind());
	}

	// This only tests the merge. Will fail if testControledObjectResource does.
	@Test
	public void testMergeControledObjectResourceRtL() throws IOException {
		final Resource left = input.getControlLeft();
		final Resource origin = input.getControlOrigin();
		final Resource right = input.getControlRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllRightToLeft(differences, new BasicMonitor());

		final EObject leftFragmentedNode = getNodeNamed(left, "fragmented");
		final EObject originFragmentedNode = getNodeNamed(origin, "fragmented");
		final EObject rightFragmentedNode = getNodeNamed(right, "fragmented");

		assertTrue(leftFragmentedNode.eContainer().eResource() == leftFragmentedNode.eResource());
		assertTrue(originFragmentedNode.eContainer().eResource() == originFragmentedNode.eResource());
		assertTrue(rightFragmentedNode.eContainer().eResource() == rightFragmentedNode.eResource());

		final List<EObject> leftRootContent = ((InternalEList<EObject>)getNodeNamed(left, "root").eContents())
				.basicList();
		final List<EObject> originRootContent = ((InternalEList<EObject>)getNodeNamed(origin, "root")
				.eContents()).basicList();
		final List<EObject> rightRootContent = ((InternalEList<EObject>)getNodeNamed(right, "root")
				.eContents()).basicList();

		assertEquals(1, leftRootContent.size());
		assertEquals(1, originRootContent.size());
		assertEquals(1, rightRootContent.size());

		assertTrue(leftRootContent.get(0) == leftFragmentedNode);
		assertTrue(originRootContent.get(0) == originFragmentedNode);
		assertTrue(rightRootContent.get(0) == rightFragmentedNode);

		// there should be no diff between left and right
		final Comparison lrCompare = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(left, right, null));
		assertEquals(0, lrCompare.getDifferences().size());

		// And since we've undone the changes, there are no diffs with origin either
		comparison = EMFCompare.builder().build().compare(scope);
		assertTrue(comparison.getDifferences().isEmpty());
	}

	@Test
	public void testControledObjectFolderResourceSet() throws IOException {
		final Resource left = input.getControlLeftFolder();
		final Resource origin = input.getControlOriginFolder();
		final Resource right = input.getControlRightFolder();

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

		final IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final Diff diff = differences.get(0);
		assertTrue(diff instanceof ResourceAttachmentChange);
		assertEquals(diff.getMatch().getLeft(), getNodeNamed(leftSet, "fragmented"));
		assertEquals(diff.getMatch().getRight(), getNodeNamed(rightSet, "fragmented"));
		assertEquals(diff.getMatch().getOrigin(), getNodeNamed(originSet, "fragmented"));
		assertSame(diff.getSource(), DifferenceSource.LEFT);
		assertSame(diff.getKind(), DifferenceKind.ADD);
	}

	// This only tests the merge. Will fail if testControledObjectFolderResourceSet does.
	@Ignore("To ignore while an Egit merge strategy hasn't be provided.")
	@Test
	public void testMergeControledObjectFolderResourceSetLtR() throws IOException {
		final Resource left = input.getControlLeftFolder();
		final Resource origin = input.getControlOriginFolder();
		final Resource right = input.getControlRightFolder();

		final ResourceSet leftSet = left.getResourceSet();
		final ResourceSet originSet = origin.getResourceSet();
		final ResourceSet rightSet = right.getResourceSet();

		EcoreUtil.resolveAll(leftSet);
		EcoreUtil.resolveAll(originSet);
		EcoreUtil.resolveAll(rightSet);

		final IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final Diff diff = differences.get(0);

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		assertEquals(2, leftSet.getResources().size());
		assertEquals(1, originSet.getResources().size());
		assertEquals(2, rightSet.getResources().size());

		final EObject leftFragmentedNode = getNodeNamed(leftSet, "fragmented");
		final EObject originFragmentedNode = getNodeNamed(originSet, "fragmented");
		final EObject rightFragmentedNode = getNodeNamed(rightSet, "fragmented");

		assertTrue(leftFragmentedNode.eContainer().eResource() != leftFragmentedNode.eResource());
		assertTrue(originFragmentedNode.eContainer().eResource() == originFragmentedNode.eResource());
		assertTrue(rightFragmentedNode.eContainer().eResource() != rightFragmentedNode.eResource());

		// there should be no diff between left and right
		final Comparison lrCompare = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(leftSet, rightSet, null));
		assertEquals(0, lrCompare.getDifferences().size());

		// but there should be two diffs (a pseudo conflict resource change) when compared with origin
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
		assertEquals(1, comparison.getConflicts().size());
		assertSame(ConflictKind.PSEUDO, comparison.getConflicts().get(0).getKind());
	}

	// This only tests the merge. Will fail if testControledObjectFolderResourceSet does.
	@Test
	public void testMergeControledObjectFolderResourceSetRtL() throws IOException {
		final Resource left = input.getControlLeftFolder();
		final Resource origin = input.getControlOriginFolder();
		final Resource right = input.getControlRightFolder();

		final ResourceSet leftSet = left.getResourceSet();
		final ResourceSet originSet = origin.getResourceSet();
		final ResourceSet rightSet = right.getResourceSet();

		EcoreUtil.resolveAll(leftSet);
		EcoreUtil.resolveAll(originSet);
		EcoreUtil.resolveAll(rightSet);

		final IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final Diff diff = differences.get(0);

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		// Note : we still have 2 resources there, though one is empty
		assertEquals(2, leftSet.getResources().size());
		assertEquals(1, originSet.getResources().size());
		assertEquals(1, rightSet.getResources().size());

		for (Resource res : leftSet.getResources()) {
			if (res != left) {
				assertTrue(res.getContents().isEmpty());
			}
		}

		final EObject leftFragmentedNode = getNodeNamed(leftSet, "fragmented");
		final EObject originFragmentedNode = getNodeNamed(originSet, "fragmented");
		final EObject rightFragmentedNode = getNodeNamed(rightSet, "fragmented");

		assertTrue(leftFragmentedNode.eContainer().eResource() == leftFragmentedNode.eResource());
		assertTrue(originFragmentedNode.eContainer().eResource() == originFragmentedNode.eResource());
		assertTrue(rightFragmentedNode.eContainer().eResource() == rightFragmentedNode.eResource());

		// there should be no diff between left and right
		final Comparison lrCompare = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(leftSet, rightSet, null));
		assertTrue(lrCompare.getDifferences().isEmpty());

		// And since we've undone the resource change, there are no diffs with origin either
		comparison = EMFCompare.builder().build().compare(scope);
		assertTrue(comparison.getDifferences().isEmpty());
	}

	@Test
	public void testControledObjectFolderResource() throws IOException {
		final Resource left = input.getControlLeft();
		final Resource origin = input.getControlOriginFolder();
		final Resource right = input.getControlRightFolder();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// At the resource level, we cannot match an object with the 'proxy' root.
		final List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());

		// There's one deletion, and one addition (the proxy root)
		final Diff addition;
		final Diff deletion;
		if (differences.get(0).getKind() == DifferenceKind.ADD) {
			addition = differences.get(0);
			deletion = differences.get(1);
		} else {
			deletion = differences.get(0);
			addition = differences.get(1);
		}
		assertTrue(addition instanceof ReferenceChange);
		assertEquals(addition.getMatch().getLeft(), getNodeNamed(left, "root"));
		assertEquals(addition.getMatch().getRight(), getNodeNamed(right, "root"));
		assertEquals(addition.getMatch().getOrigin(), getNodeNamed(origin, "root"));
		assertTrue(((ReferenceChange)addition).getValue().eIsProxy());
		assertSame(addition.getSource(), DifferenceSource.LEFT);
		assertSame(addition.getKind(), DifferenceKind.ADD);

		assertTrue(deletion instanceof ReferenceChange);
		assertEquals(deletion.getMatch().getLeft(), getNodeNamed(left, "root"));
		assertEquals(deletion.getMatch().getRight(), getNodeNamed(right, "root"));
		assertEquals(deletion.getMatch().getOrigin(), getNodeNamed(origin, "root"));
		assertEquals(((ReferenceChange)deletion).getValue(), getNodeNamed(origin, "fragmented"));
		assertSame(deletion.getSource(), DifferenceSource.LEFT);
		assertSame(deletion.getKind(), DifferenceKind.DELETE);
	}

	// This only tests the merge. Will fail if testControledObjectFolderResource does.
	@Test
	public void testMergeControledObjectFolderResourceLtR() throws IOException {
		final Resource left = input.getControlLeftFolder();
		final Resource origin = input.getControlOriginFolder();
		final Resource right = input.getControlRightFolder();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllLeftToRight(differences, new BasicMonitor());

		final EObject leftFragmentedNode = getNodeNamed(left, "fragmented");
		final EObject originFragmentedNode = getNodeNamed(origin, "fragmented");
		final EObject rightFragmentedNode = getNodeNamed(right, "fragmented");

		assertNull(leftFragmentedNode);
		assertTrue(originFragmentedNode.eContainer().eResource() == originFragmentedNode.eResource());
		assertNull(rightFragmentedNode);

		final List<EObject> leftRootContent = ((InternalEList<EObject>)getNodeNamed(left, "root").eContents())
				.basicList();
		final List<EObject> originRootContent = ((InternalEList<EObject>)getNodeNamed(origin, "root")
				.eContents()).basicList();
		final List<EObject> rightRootContent = ((InternalEList<EObject>)getNodeNamed(right, "root")
				.eContents()).basicList();

		assertEquals(1, leftRootContent.size());
		assertEquals(1, originRootContent.size());
		assertEquals(1, rightRootContent.size());

		assertTrue(leftRootContent.get(0).eIsProxy());
		assertTrue(originRootContent.get(0) == originFragmentedNode);
		assertTrue(rightRootContent.get(0).eIsProxy());

		// there should be no diff between left and right
		final Comparison lrCompare = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(left, right, null));
		assertEquals(0, lrCompare.getDifferences().size());

		// but there should be four diffs (conflicting 2-by-2) when compared with origin :
		// two additions (the proxies) and two deletions (the old root)
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(4, comparison.getDifferences().size());
		assertEquals(2, comparison.getConflicts().size());
		assertSame(ConflictKind.PSEUDO, comparison.getConflicts().get(0).getKind());
		assertSame(ConflictKind.PSEUDO, comparison.getConflicts().get(1).getKind());
	}

	// This only tests the merge. Will fail if testControledObjectFolderResource does.
	@Test
	public void testMergeControledObjectFolderResourceRtL() throws IOException {
		final Resource left = input.getControlLeftFolder();
		final Resource origin = input.getControlOriginFolder();
		final Resource right = input.getControlRightFolder();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllRightToLeft(differences, new BasicMonitor());

		final EObject leftFragmentedNode = getNodeNamed(left, "fragmented");
		final EObject originFragmentedNode = getNodeNamed(origin, "fragmented");
		final EObject rightFragmentedNode = getNodeNamed(right, "fragmented");

		assertTrue(leftFragmentedNode.eContainer().eResource() == leftFragmentedNode.eResource());
		assertTrue(originFragmentedNode.eContainer().eResource() == originFragmentedNode.eResource());
		assertTrue(rightFragmentedNode.eContainer().eResource() == rightFragmentedNode.eResource());

		final List<EObject> leftRootContent = ((InternalEList<EObject>)getNodeNamed(left, "root").eContents())
				.basicList();
		final List<EObject> originRootContent = ((InternalEList<EObject>)getNodeNamed(origin, "root")
				.eContents()).basicList();
		final List<EObject> rightRootContent = ((InternalEList<EObject>)getNodeNamed(right, "root")
				.eContents()).basicList();

		assertEquals(1, leftRootContent.size());
		assertEquals(1, originRootContent.size());
		assertEquals(1, rightRootContent.size());

		assertTrue(leftRootContent.get(0) == leftFragmentedNode);
		assertTrue(originRootContent.get(0) == originFragmentedNode);
		assertTrue(rightRootContent.get(0) == rightFragmentedNode);

		// there should be no diff between left and right
		final Comparison lrCompare = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(left, right, null));
		assertEquals(0, lrCompare.getDifferences().size());

		// And since we've undone the changes, there are no diffs with origin either
		comparison = EMFCompare.builder().build().compare(scope);
		assertTrue(comparison.getDifferences().isEmpty());
	}

	@Test
	public void testDeletedRootResourceSet() throws IOException {
		final Resource left = input.getDeletedRootLeft();
		final Resource origin = input.getDeletedRootOrigin();
		final Resource right = input.getDeletedRootRight();

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
		assertEquals(1, rightSet.getResources().size());

		final IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final Diff diff = differences.get(0);
		assertTrue(diff instanceof ResourceAttachmentChange);
		assertEquals(diff.getMatch().getRight(), getNodeNamed(right, "deletedRoot"));
		assertEquals(diff.getMatch().getOrigin(), getNodeNamed(origin, "deletedRoot"));
		assertNull(diff.getMatch().getLeft());
		assertSame(diff.getSource(), DifferenceSource.LEFT);
		assertSame(diff.getKind(), DifferenceKind.DELETE);
	}

	// This only tests the merge. Will fail if testDeletedRootResourceSet does.
	@Test
	public void testMergeDeletedRootResourceSetLtR() throws IOException {
		final Resource left = input.getDeletedRootLeft();
		final Resource origin = input.getDeletedRootOrigin();
		final Resource right = input.getDeletedRootRight();

		final ResourceSet leftSet = left.getResourceSet();
		final ResourceSet originSet = origin.getResourceSet();
		final ResourceSet rightSet = right.getResourceSet();

		EcoreUtil.resolveAll(leftSet);
		EcoreUtil.resolveAll(originSet);
		EcoreUtil.resolveAll(rightSet);

		final IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final Diff diff = differences.get(0);

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		assertEquals(1, left.getContents().size());
		assertEquals(2, origin.getContents().size());
		assertEquals(1, right.getContents().size());

		// there should be no diff between left and right
		final Comparison lrCompare = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(leftSet, rightSet, null));
		assertEquals(0, lrCompare.getDifferences().size());

		// but there should be two diffs (a pseudo conflict deletion) when compared with origin
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
		assertEquals(1, comparison.getConflicts().size());
		assertSame(ConflictKind.PSEUDO, comparison.getConflicts().get(0).getKind());
	}

	// This only tests the merge. Will fail if testDeletedRootResourceSet does.
	@Ignore("To ignore while an Egit merge strategy hasn't be provided.")
	@Test
	public void testMergeDeletedRootResourceSetRtL() throws IOException {
		final Resource left = input.getDeletedRootLeft();
		final Resource origin = input.getDeletedRootOrigin();
		final Resource right = input.getDeletedRootRight();

		final ResourceSet leftSet = left.getResourceSet();
		final ResourceSet originSet = origin.getResourceSet();
		final ResourceSet rightSet = right.getResourceSet();

		EcoreUtil.resolveAll(leftSet);
		EcoreUtil.resolveAll(originSet);
		EcoreUtil.resolveAll(rightSet);

		final IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final Diff diff = differences.get(0);

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		assertEquals(2, left.getContents().size());
		assertEquals(2, origin.getContents().size());
		assertEquals(2, right.getContents().size());

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testDeletedRootResource() throws IOException {
		final Resource left = input.getDeletedRootLeft();
		final Resource origin = input.getDeletedRootOrigin();
		final Resource right = input.getDeletedRootRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final Diff diff = differences.get(0);
		assertTrue(diff instanceof ResourceAttachmentChange);
		assertEquals(diff.getMatch().getRight(), getNodeNamed(right, "deletedRoot"));
		assertEquals(diff.getMatch().getOrigin(), getNodeNamed(origin, "deletedRoot"));
		assertNull(diff.getMatch().getLeft());
		assertSame(diff.getSource(), DifferenceSource.LEFT);
		assertSame(diff.getKind(), DifferenceKind.DELETE);
	}

	// This only tests the merge. Will fail if testDeletedRootResource does.
	@Test
	public void testMergeDeletedRootResourceLtR() throws IOException {
		final Resource left = input.getDeletedRootLeft();
		final Resource origin = input.getDeletedRootOrigin();
		final Resource right = input.getDeletedRootRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final Diff diff = differences.get(0);

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		assertEquals(1, left.getContents().size());
		assertEquals(2, origin.getContents().size());
		assertEquals(1, right.getContents().size());

		// there should be no diff between left and right
		final Comparison lrCompare = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(left, right, null));
		assertEquals(0, lrCompare.getDifferences().size());

		// but there should be two diffs (a pseudo conflict deletion) when compared with origin
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
		assertEquals(1, comparison.getConflicts().size());
		assertSame(ConflictKind.PSEUDO, comparison.getConflicts().get(0).getKind());
	}

	// This only tests the merge. Will fail if testDeletedRootResource does.
	@Test
	public void testMergeDeletedRootResourceRtL() throws IOException {
		final Resource left = input.getDeletedRootLeft();
		final Resource origin = input.getDeletedRootOrigin();
		final Resource right = input.getDeletedRootRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final Diff diff = differences.get(0);

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		assertEquals(2, left.getContents().size());
		assertEquals(2, origin.getContents().size());
		assertEquals(2, right.getContents().size());

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testNewRootResourceSet() throws IOException {
		final Resource left = input.getNewRootLeft();
		final Resource origin = input.getNewRootOrigin();
		final Resource right = input.getNewRootRight();

		final ResourceSet leftSet = left.getResourceSet();
		final ResourceSet originSet = origin.getResourceSet();
		final ResourceSet rightSet = right.getResourceSet();

		assertNotNull(leftSet);
		assertNotNull(originSet);
		assertNotNull(rightSet);

		EcoreUtil.resolveAll(leftSet);
		EcoreUtil.resolveAll(originSet);
		EcoreUtil.resolveAll(rightSet);

		assertEquals(2, left.getContents().size());
		assertEquals(1, origin.getContents().size());
		assertEquals(1, right.getContents().size());

		assertEquals(1, leftSet.getResources().size());
		assertEquals(1, originSet.getResources().size());
		assertEquals(1, rightSet.getResources().size());

		final IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final Diff diff = differences.get(0);
		assertTrue(diff instanceof ResourceAttachmentChange);
		assertEquals(diff.getMatch().getLeft(), getNodeNamed(left, "newRoot"));
		assertNull(diff.getMatch().getOrigin());
		assertNull(diff.getMatch().getRight());
		assertSame(diff.getSource(), DifferenceSource.LEFT);
		assertSame(diff.getKind(), DifferenceKind.ADD);
	}

	// This only tests the merge. Will fail if testNewRootResourceSet does.
	@Ignore("To ignore while an Egit merge strategy hasn't be provided.")
	@Test
	public void testMergeNewRootResourceSetLtR() throws IOException {
		final Resource left = input.getNewRootLeft();
		final Resource origin = input.getNewRootOrigin();
		final Resource right = input.getNewRootRight();

		final ResourceSet leftSet = left.getResourceSet();
		final ResourceSet originSet = origin.getResourceSet();
		final ResourceSet rightSet = right.getResourceSet();

		EcoreUtil.resolveAll(leftSet);
		EcoreUtil.resolveAll(originSet);
		EcoreUtil.resolveAll(rightSet);

		final IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final Diff diff = differences.get(0);

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		assertEquals(2, left.getContents().size());
		assertEquals(1, origin.getContents().size());
		assertEquals(2, right.getContents().size());

		// there should be no diff between left and right
		final Comparison lrCompare = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(leftSet, rightSet, null));
		assertEquals(0, lrCompare.getDifferences().size());

		// but there should be two diffs (a pseudo conflict addition) when compared with origin
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
		assertEquals(1, comparison.getConflicts().size());
		assertSame(ConflictKind.PSEUDO, comparison.getConflicts().get(0).getKind());
	}

	// This only tests the merge. Will fail if testNewRootResourceSet does.
	@Test
	public void testMergeNewRootResourceSetRtL() throws IOException {
		final Resource left = input.getNewRootLeft();
		final Resource origin = input.getNewRootOrigin();
		final Resource right = input.getNewRootRight();

		final ResourceSet leftSet = left.getResourceSet();
		final ResourceSet originSet = origin.getResourceSet();
		final ResourceSet rightSet = right.getResourceSet();

		EcoreUtil.resolveAll(leftSet);
		EcoreUtil.resolveAll(originSet);
		EcoreUtil.resolveAll(rightSet);

		final IComparisonScope scope = new DefaultComparisonScope(leftSet, rightSet, originSet);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final Diff diff = differences.get(0);

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		assertEquals(1, left.getContents().size());
		assertEquals(1, origin.getContents().size());
		assertEquals(1, right.getContents().size());

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	@Test
	public void testNewRootResource() throws IOException {
		final Resource left = input.getNewRootLeft();
		final Resource origin = input.getNewRootOrigin();
		final Resource right = input.getNewRootRight();

		assertEquals(2, left.getContents().size());
		assertEquals(1, origin.getContents().size());
		assertEquals(1, right.getContents().size());

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		assertEquals(1, differences.size());

		final Diff diff = differences.get(0);
		assertTrue(diff instanceof ResourceAttachmentChange);
		assertEquals(diff.getMatch().getLeft(), getNodeNamed(left, "newRoot"));
		assertNull(diff.getMatch().getOrigin());
		assertNull(diff.getMatch().getRight());
		assertSame(diff.getSource(), DifferenceSource.LEFT);
		assertSame(diff.getKind(), DifferenceKind.ADD);
	}

	// This only tests the merge. Will fail if testNewRootResource does.
	@Test
	public void testMergeNewRootResourceLtR() throws IOException {
		final Resource left = input.getNewRootLeft();
		final Resource origin = input.getNewRootOrigin();
		final Resource right = input.getNewRootRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final Diff diff = differences.get(0);

		mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
		assertEquals(2, left.getContents().size());
		assertEquals(1, origin.getContents().size());
		assertEquals(2, right.getContents().size());

		// there should be no diff between left and right
		final Comparison lrCompare = EMFCompare.builder().build().compare(
				new DefaultComparisonScope(left, right, null));
		assertEquals(0, lrCompare.getDifferences().size());

		// but there should be two diffs (a pseudo conflict addition) when compared with origin
		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(2, comparison.getDifferences().size());
		assertEquals(1, comparison.getConflicts().size());
		assertSame(ConflictKind.PSEUDO, comparison.getConflicts().get(0).getKind());
	}

	// This only tests the merge. Will fail if testNewRootResource does.
	@Test
	public void testMergeNewRootResourceRtL() throws IOException {
		final Resource left = input.getNewRootLeft();
		final Resource origin = input.getNewRootOrigin();
		final Resource right = input.getNewRootRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();
		final Diff diff = differences.get(0);

		mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
		assertEquals(1, left.getContents().size());
		assertEquals(1, origin.getContents().size());
		assertEquals(1, right.getContents().size());

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

	private EObject getNodeNamed(Notifier notifier, String name) {
		final Iterator<EObject> iterator;
		if (notifier instanceof ResourceSet) {
			iterator = Iterators.filter(EcoreUtil.getAllProperContents((ResourceSet)notifier, false),
					EObject.class);
		} else if (notifier instanceof Resource) {
			iterator = EcoreUtil.getAllProperContents((Resource)notifier, false);
		} else {
			iterator = EcoreUtil.getAllProperContents((EObject)notifier, false);
		}
		while (iterator.hasNext()) {
			final EObject next = iterator.next();
			final EStructuralFeature nameFeature = next.eClass().getEStructuralFeature("name");
			if (nameFeature != null && name.equals(next.eGet(nameFeature))) {
				return next;
			}
		}
		return null;
	}
}

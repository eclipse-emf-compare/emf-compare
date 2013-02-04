package org.eclipse.emf.compare.tests.diff;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodesFactory;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * This will specifically make sure that merging two large lists never takes more than a set amount of time.
 * <p>
 * I have a list (S1) on the left side that contains 2000 elements. Its counterpart on the right side (S2) has
 * 200 elements, only 100 of which are not differences (i.e. : also present in S1). We'll make sure that
 * merging these two lists is fast enough (through test timeouts).
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("nls")
public class LCSPerformanceTest {
	private XMIResource left;

	private XMIResource right;

	@Before
	public void setup() {
		left = new XMIResourceImpl(URI.createURI("left.xmi"));
		right = new XMIResourceImpl(URI.createURI("right.xmi"));

		createNode(left, 2000, 0, 1);
		createNode(right, 200, 0, 1901);
	}

	@Test
	public void checkTestData() {
		assertTrue(left.getContents().size() == 1);
		final Node leftRoot = (Node)left.getContents().get(0);
		assertTrue(leftRoot.eContents().size() == 2000);

		assertTrue(right.getContents().size() == 1);
		final Node rightRoot = (Node)right.getContents().get(0);
		assertTrue(rightRoot.eContents().size() == 200);

		IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final List<Diff> additions = Lists.newArrayList();
		final List<Diff> deletions = Lists.newArrayList();

		for (Diff difference : comparison.getDifferences()) {
			if (difference.getKind() == DifferenceKind.ADD) {
				additions.add(difference);
			} else {
				deletions.add(difference);
			}
		}

		assertEquals(Integer.valueOf(1900), Integer.valueOf(additions.size()));
		assertEquals(Integer.valueOf(100), Integer.valueOf(deletions.size()));
	}

	/**
	 * Will fail if {@link #checkTestData()} does.
	 * <p>
	 * The real assertion here is that this should never take more than 40 seconds to execute. We have 1900
	 * additions to merge, accounting for as many LCS computations.
	 * </p>
	 * <p>
	 * Note that this test should run in less than 30 seconds... However that is not true on our build
	 * machine, so the timeout has been raised to 40s.
	 * </p>
	 */
	@Test(timeout = 60000)
	// FIXME: raise the limit to 60sec after changes in ComparisonSpec#getDifferences(EObject) (
	public void copyLeftToRight() {
		IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		for (Diff diff : comparison.getDifferences()) {
			diff.copyLeftToRight();
		}

		comparison = EMFCompare.builder().build().compare(scope);
		assertTrue(comparison.getDifferences().isEmpty());
	}

	/**
	 * Will fail if {@link #checkTestData()} does.
	 * <p>
	 * The real assertion here is that this should never take more than 3 seconds to execute : we're resetting
	 * all differences so there are only 100 "slow" ones : resetting deletions need the LCS computation.
	 * </p>
	 */
	@Test(timeout = 3000)
	public void copyRightToLeft() {
		IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		for (Diff diff : comparison.getDifferences()) {
			diff.copyRightToLeft();
		}

		comparison = EMFCompare.builder().build().compare(scope);
		assertTrue(comparison.getDifferences().isEmpty());
	}

	private void createNode(XMIResource resource, int childCount, int nodeId, int idGap) {
		resource.eSetDeliver(false);
		Node node = NodesFactory.eINSTANCE.createNode();
		node.setName("node" + nodeId);
		resource.getContents().add(node);
		resource.setID(node, Integer.toString(nodeId));
		int childId = nodeId + Math.max(1, idGap);
		for (int i = 0; i < childCount; i++) {
			Node child = NodesFactory.eINSTANCE.createNode();
			child.setName("node" + childId);
			node.getContainmentRef1().add(child);
			resource.setID(child, Integer.toString(childId));
			childId++;
		}
		resource.eSetDeliver(true);
	}
}

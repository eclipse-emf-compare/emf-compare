package org.eclipse.emf.compare.diagram.ide.tests.nodechanges;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onFeature;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedAttribute;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.diagram.GMFCompare;
import org.eclipse.emf.compare.diagram.LabelChange;
import org.eclipse.emf.compare.diagram.NodeChange;
import org.eclipse.emf.compare.diagram.ide.diff.DiagramDiffExtensionPostProcessor;
import org.eclipse.emf.compare.diagram.diff.util.DiagramCompareConstants;
import org.eclipse.emf.compare.diagram.ide.tests.nodechanges.data.NodeChangesInputData;
import org.eclipse.emf.compare.diagram.tests.AbstractTest;
import org.eclipse.emf.compare.extension.EMFCompareExtensionRegistry;
import org.eclipse.emf.compare.extension.PostProcessorDescriptor;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterators;

@SuppressWarnings("nls")
public class NodechangesTest extends AbstractTest {

	private NodeChangesInputData input = new NodeChangesInputData();

	@Override
	@Before
	public void before() {
		super.before();
		EMFCompareExtensionRegistry.addPostProcessor(new PostProcessorDescriptor(
				"http://www.eclipse.org/gmf/runtime/\\d.\\d.\\d/notation", null,
				"org.eclipse.emf.compare.diagram.ide.diff.DiagramDiffExtensionPostProcessor",
				new DiagramDiffExtensionPostProcessor()));
	}
	
	@Test
	public void testA10UseCase() throws IOException {
		
		GMFCompare.getDefault().getPreferenceStore().setValue(DiagramCompareConstants.PREFERENCES_KEY_MOVE_THRESHOLD, 0);
		
		testMove(true);
		
	}
	
	@Test
	public void testA11UseCase() throws IOException {
		
		GMFCompare.getDefault().getPreferenceStore().setValue(DiagramCompareConstants.PREFERENCES_KEY_MOVE_THRESHOLD, 200);
		
		testMove(false);
		
	}
	
	@Test
	public void testA20UseCase() throws IOException {
		
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left.getResourceSet(), right.getResourceSet());
		final Comparison comparison = EMFCompare.newComparator(scope).compare();
		
		final List<Diff> differences = comparison.getDifferences();
		
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		
		final Diff changeLabel = Iterators.find(differences.iterator(), and(instanceOf(LabelChange.class), ofKind(DifferenceKind.CHANGE)));
		assertNotNull(changeLabel);
		
		final Diff changeName = Iterators.find(differences.iterator(), changedAttribute("tc01.EClass1", "name", "TheClass", "EClass1"));
		assertNotNull(changeName);
		
	}
	
	private void testMove(boolean overDetectionThreshold) throws IOException {
		
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left.getResourceSet(), right.getResourceSet());
		final Comparison comparison = EMFCompare.newComparator(scope).compare();
		
		final List<Diff> differences = comparison.getDifferences();

		if (overDetectionThreshold) {
			// We should have no less and no more than 3 differences
			assertSame(Integer.valueOf(3), Integer.valueOf(differences.size()));
		} else {
			// We should have no less and no more than 2 differences
			assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		}
		
		final Diff changeX = Iterators.find(differences.iterator(), onFeature("x"));
		final Diff changeY = Iterators.find(differences.iterator(), onFeature("y"));
		
		if (overDetectionThreshold) {
			final Diff moveNode = Iterators.find(differences.iterator(), and(instanceOf(NodeChange.class), ofKind(DifferenceKind.MOVE)));
			assertSame(Integer.valueOf(2), moveNode.getRefinedBy().size());
			assertTrue(moveNode.getRefinedBy().contains(changeX));
			assertTrue(moveNode.getRefinedBy().contains(changeY));
		}
		
	}

	@Override
	protected AbstractInputData getInput() {
		return input;
	}
	
}

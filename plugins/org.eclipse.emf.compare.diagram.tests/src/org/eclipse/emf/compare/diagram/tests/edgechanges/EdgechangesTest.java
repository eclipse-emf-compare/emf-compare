package org.eclipse.emf.compare.diagram.tests.edgechanges;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diagram.EdgeChange;
import org.eclipse.emf.compare.diagram.tests.AbstractTest;
import org.eclipse.emf.compare.diagram.tests.DiagramInputData;
import org.eclipse.emf.compare.diagram.tests.edgechanges.data.EdgeChangesInputData;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;

@SuppressWarnings("nls")
public class EdgechangesTest extends AbstractTest {

	private EdgeChangesInputData input = new EdgeChangesInputData();

	@Test
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left.getResourceSet(), right.getResourceSet());
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build().compare(scope);
		
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		
		final Diff attributeChange = Iterators.find(differences.iterator(), and(instanceOf(AttributeChange.class), ofKind(DifferenceKind.CHANGE)));
		final Diff edgeChange = Iterators.find(differences.iterator(), and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.MOVE)));
		
		assertNotNull(attributeChange);
		assertNotNull(edgeChange);
		
		assertSame(Integer.valueOf(1), edgeChange.getRefinedBy().size());
		assertTrue(edgeChange.getRefinedBy().contains(attributeChange));
	}
	
	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right.getResourceSet(), left.getResourceSet());
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build().compare(scope);
		
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 60 differences
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
		
		final Diff attributeChange = Iterators.find(differences.iterator(), and(instanceOf(AttributeChange.class), ofKind(DifferenceKind.CHANGE)));
		final Diff edgeChange = Iterators.find(differences.iterator(), and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.MOVE)));
		
		assertNotNull(attributeChange);
		assertNotNull(edgeChange);
		
		assertSame(Integer.valueOf(1), edgeChange.getRefinedBy().size());
		assertTrue(edgeChange.getRefinedBy().contains(attributeChange));	
	}
	
	@Test
	public void testA20UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left.getResourceSet(), right.getResourceSet());
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build().compare(scope);
		
		final List<Diff> differences = comparison.getDifferences();
		
		// We should have no less and no more than 4 differences
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));
		
		Collection<Diff> diffs = Collections2.filter(differences, and(instanceOf(AttributeChange.class), ofKind(DifferenceKind.CHANGE)));
		
		final Diff edgeChange = Iterators.find(differences.iterator(), and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.MOVE)));
		
		assertTrue(diffs.size() == 3);
		assertNotNull(edgeChange);
		
		assertSame(Integer.valueOf(3), edgeChange.getRefinedBy().size());
		assertTrue(edgeChange.getRefinedBy().containsAll(diffs));
		
	}
	
	@Test
	public void testA21UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right.getResourceSet(), left.getResourceSet());
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build().compare(scope);
		
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 4 differences
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));
				
		Collection<Diff> diffs = Collections2.filter(differences, and(instanceOf(AttributeChange.class), ofKind(DifferenceKind.CHANGE)));
				
		final Diff edgeChange = Iterators.find(differences.iterator(), and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.MOVE)));
				
		assertTrue(diffs.size() == 3);
		assertNotNull(edgeChange);
				
		assertSame(Integer.valueOf(3), edgeChange.getRefinedBy().size());
		assertTrue(edgeChange.getRefinedBy().containsAll(diffs));
	}

	@Test
	public void testA30UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left.getResourceSet(), right.getResourceSet());
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build().compare(scope);
		
		final List<Diff> differences = comparison.getDifferences();
		
		// We should have no less and no more than 2 differences
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
				
		final Diff attributeChange = Iterators.find(differences.iterator(), and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.ADD)));
		final Diff edgeChange = Iterators.find(differences.iterator(), and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.MOVE)));
				
		assertNotNull(attributeChange);
		assertNotNull(edgeChange);
				
		assertSame(Integer.valueOf(1), edgeChange.getRefinedBy().size());
		assertTrue(edgeChange.getRefinedBy().contains(attributeChange));	
		
	}
	
	@Test
	public void testA31UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right.getResourceSet(), left.getResourceSet());
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build().compare(scope);
		
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));
						
		final Diff attributeChange = Iterators.find(differences.iterator(), and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.DELETE)));
		final Diff edgeChange = Iterators.find(differences.iterator(), and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.MOVE)));
						
		assertNotNull(attributeChange);
		assertNotNull(edgeChange);
						
		assertSame(Integer.valueOf(1), edgeChange.getRefinedBy().size());
		assertTrue(edgeChange.getRefinedBy().contains(attributeChange));	
	}
	
	@Test
	public void testA40UseCase() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left.getResourceSet(), right.getResourceSet());
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build().compare(scope);
		
		final List<Diff> differences = comparison.getDifferences();
		
		// We should have no less and no more than 13 differences
		assertSame(Integer.valueOf(13), Integer.valueOf(differences.size()));
				
		Collection<Diff> diffs = Collections2.filter(differences, instanceOf(ReferenceChange.class));
		Diff addEdge = Iterators.find(differences.iterator(), and(valueIsEdge, ofKind(DifferenceKind.ADD)));
		assertNotNull(addEdge);
		
		final Diff edgeChange = Iterators.find(differences.iterator(), and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD)));

		assertSame(Integer.valueOf(12), diffs.size());
		assertNotNull(edgeChange);
				
		assertSame(Integer.valueOf(11), edgeChange.getRefinedBy().size());
		assertFalse(edgeChange.getRefinedBy().contains(addEdge));	
		
	}
	
	@Test
	public void testA41UseCase() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right.getResourceSet(), left.getResourceSet());
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build().compare(scope);
		
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 13 differences
		assertSame(Integer.valueOf(13), Integer.valueOf(differences.size()));
						
		Collection<Diff> diffs = Collections2.filter(differences, instanceOf(ReferenceChange.class));
				
		final Diff edgeChange = Iterators.find(differences.iterator(), and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE)));
						
		assertSame(Integer.valueOf(12), diffs.size());
		assertNotNull(edgeChange);
						
		assertSame(Integer.valueOf(1), edgeChange.getRefinedBy().size());
		Diff deleteEdge = Iterators.find(differences.iterator(), and(valueIsEdge, ofKind(DifferenceKind.DELETE)));
		assertTrue(edgeChange.getRefinedBy().contains(deleteEdge));
	}
	
	@Override
	protected DiagramInputData getInput() {
		return input;
	}
	
	final Predicate<Diff> valueIsEdge = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input instanceof ReferenceChange && ((ReferenceChange)input).getValue() instanceof Edge && ((ReferenceChange)input).getReference().isContainment();
		}
	};
	
	private static Predicate<? super Diff> valueUnder(final Diff container) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {		
				if (input instanceof ReferenceChange) {
					final ReferenceChange diff = (ReferenceChange) input;
					if (diff.getReference().isContainment()) {
						return diff.getValue() == MatchUtil.getValue(container);
					} else {
						return MatchUtil.getContainer(diff.getMatch().getComparison(), diff) == MatchUtil.getValue(container);
					}
				}
				return false;
			}
		};
	}

	private static Predicate<? super Diff> refinedBy(final Diff refining) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				return input.getRefinedBy().contains(refining);
			}
		};
	}
	
}

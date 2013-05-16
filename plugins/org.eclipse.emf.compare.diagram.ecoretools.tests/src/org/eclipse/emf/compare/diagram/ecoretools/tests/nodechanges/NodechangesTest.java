package org.eclipse.emf.compare.diagram.ecoretools.tests.nodechanges;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onFeature;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.valueIs;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.valueNameMatches;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.diagram.ecoretools.tests.AbstractTest;
import org.eclipse.emf.compare.diagram.ecoretools.tests.DiagramInputData;
import org.eclipse.emf.compare.diagram.ecoretools.tests.nodechanges.data.NodeChangesInputData;
import org.eclipse.emf.compare.diagram.internal.extensions.NodeChange;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.DrawerStyle;
import org.eclipse.gmf.runtime.notation.FilteringStyle;
import org.eclipse.gmf.runtime.notation.MultiDiagramLinkStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.ShapeStyle;
import org.eclipse.gmf.runtime.notation.SortingStyle;
import org.eclipse.gmf.runtime.notation.View;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

@SuppressWarnings("nls")
public class NodechangesTest extends AbstractTest {

	private NodeChangesInputData input = new NodeChangesInputData();

	@Test
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left.getResourceSet(),
				right.getResourceSet());
		final Comparison comparison = EMFCompare.builder()
				.setPostProcessorRegistry(getPostProcessorRegistry()).build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 57 differences
		assertSame(Integer.valueOf(57), Integer.valueOf(differences.size()));

		final Diff addEPackage = Iterators.find(differences.iterator(), added("tc1.EPackage0"));

		final Diff addEPackageView = Iterators.find(
				differences.iterator(),
				and(valueIsView, valueNameMatches("1002", NotationPackage.Literals.VIEW__TYPE),
						elementIs(((ReferenceChange)addEPackage).getValue())));
		final Diff addNodeInEPackageView1 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("4006", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addNodeInEPackageView2 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("5003", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addDrawerStyleInNodeInEPackageView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEPackageView2), valueIsInstanceof(DrawerStyle.class)));
		final Diff addSortingStyleInNodeInEPackageView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEPackageView2), valueIsInstanceof(SortingStyle.class)));
		final Diff addFilteringStyleInNodeInEPackageView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEPackageView2), valueIsInstanceof(FilteringStyle.class)));
		final Diff addShapeStyleInEPackageView = Iterators.find(differences.iterator(),
				and(valueUnder(addEPackageView), valueIsInstanceof(ShapeStyle.class)));
		final Diff addMultiDiagramLinkStyleInEPackageView = Iterators.find(differences.iterator(),
				and(valueUnder(addEPackageView), valueIsInstanceof(MultiDiagramLinkStyle.class)));
		final Diff addBoundsInEPackageView = Iterators.find(differences.iterator(),
				and(valueUnder(addEPackageView), valueIsInstanceof(Bounds.class)));
		final Diff addRefElementInEPackageView = Iterators.find(differences.iterator(),
				and(valueIs(((ReferenceChange)addEPackage).getValue()), onFeature("element")));

		final Diff addEClass = Iterators.find(differences.iterator(), added("tc1.EClass0"));

		final Diff addEClassView = Iterators.find(
				differences.iterator(),
				and(valueIsView, valueNameMatches("1001", NotationPackage.Literals.VIEW__TYPE),
						elementIs(((ReferenceChange)addEClass).getValue())));
		final Diff addNodeInEClassView1 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("4001", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addNodeInEClassView2 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("5001", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addDrawerStyleInNodeInEClassView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEClassView2), valueIsInstanceof(DrawerStyle.class)));
		final Diff addSortingStyleInNodeInEClassView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEClassView2), valueIsInstanceof(SortingStyle.class)));
		final Diff addFilteringStyleInNodeInEClassView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEClassView2), valueIsInstanceof(FilteringStyle.class)));
		final Diff addNodeInEClassView3 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("5002", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addDrawerStyleInNodeInEClassView3 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEClassView3), valueIsInstanceof(DrawerStyle.class)));
		final Diff addSortingStyleInNodeInEClassView3 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEClassView3), valueIsInstanceof(SortingStyle.class)));
		final Diff addFilteringStyleInNodeInEClassView = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEClassView3), valueIsInstanceof(FilteringStyle.class)));
		final Diff addShapeStyleInEClassView = Iterators.find(differences.iterator(),
				valueUnder(addEClassView));
		final Diff addRefElementInEClassView = Iterators.find(differences.iterator(),
				and(valueIs(((ReferenceChange)addEClass).getValue()), onFeature("element")));
		final Diff addBoundsInEClassView = Iterators.find(differences.iterator(),
				and(valueUnder(addEClassView), valueIsInstanceof(Bounds.class)));

		final Diff addEDataType = Iterators.find(differences.iterator(), added("tc1.EDataType0"));

		final Diff addEDataTypeView = Iterators.find(
				differences.iterator(),
				and(valueIsView, valueNameMatches("1004", NotationPackage.Literals.VIEW__TYPE),
						elementIs(((ReferenceChange)addEDataType).getValue())));
		final Diff addNodeInEDataTypeView1 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("4008", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addNodeInEDataTypeView2 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("4009", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addShapeStyleInEDataTypeView = Iterators.find(differences.iterator(),
				and(valueUnder(addEDataTypeView), valueIsInstanceof(ShapeStyle.class)));
		final Diff addRefElementInEDataTypeView = Iterators.find(differences.iterator(),
				and(valueIs(((ReferenceChange)addEDataType).getValue()), onFeature("element")));
		final Diff addBoundsInEDataTypeView = Iterators.find(differences.iterator(),
				and(valueUnder(addEDataTypeView), valueIsInstanceof(Bounds.class)));

		final Diff addEEnum = Iterators.find(differences.iterator(), added("tc1.EEnum0"));

		final Diff addEEnumView = Iterators.find(
				differences.iterator(),
				and(valueIsView, valueNameMatches("1005", NotationPackage.Literals.VIEW__TYPE),
						elementIs(((ReferenceChange)addEEnum).getValue())));
		final Diff addNodeInEEnumView1 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("4010", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addNodeInEEnumView2 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("5008", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addDrawerStyleInNodeInEEnumView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEEnumView2), valueIsInstanceof(DrawerStyle.class)));
		final Diff addSortingStyleInNodeInEEnumView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEEnumView2), valueIsInstanceof(SortingStyle.class)));
		final Diff addFilteringStyleInNodeInEEnumView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEEnumView2), valueIsInstanceof(FilteringStyle.class)));
		final Diff addShapeStyleInEEnumView = Iterators.find(differences.iterator(),
				and(valueUnder(addEEnumView), valueIsInstanceof(ShapeStyle.class)));
		final Diff addRefElementInEEnumView = Iterators.find(differences.iterator(),
				and(valueIs(((ReferenceChange)addEEnum).getValue()), onFeature("element")));
		final Diff addBoundsInEEnumView = Iterators.find(differences.iterator(),
				and(valueUnder(addEEnumView), valueIsInstanceof(Bounds.class)));

		final Diff addEAnnotation = Iterators.find(
				differences.iterator(),
				addedToReference("tc1", "eAnnotations", "tc1.EAnnotation0",
						EcorePackage.Literals.EANNOTATION__SOURCE));

		final Diff addEAnnotationView = Iterators.find(
				differences.iterator(),
				and(valueIsView, valueNameMatches("1003", NotationPackage.Literals.VIEW__TYPE),
						elementIs(((ReferenceChange)addEAnnotation).getValue())));
		final Diff addNodeInEAnnotationView1 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("4007", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addNodeInEAnnotationView2 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("5007", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addDrawerStyleInNodeInEAnnotationView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEAnnotationView2), valueIsInstanceof(DrawerStyle.class)));
		final Diff addSortingStyleInNodeInEAnnotationView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEAnnotationView2), valueIsInstanceof(SortingStyle.class)));
		final Diff addFilteringStyleInNodeInEAnnotationView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEAnnotationView2), valueIsInstanceof(FilteringStyle.class)));
		final Diff addShapeStyleInEAnnotationView = Iterators.find(differences.iterator(),
				and(valueUnder(addEAnnotationView), valueIsInstanceof(ShapeStyle.class)));
		final Diff addRefElementInEAnnotationView = Iterators.find(differences.iterator(),
				and(valueIs(((ReferenceChange)addEAnnotation).getValue()), onFeature("element")));
		final Diff addBoundsInEAnnotationView = Iterators.find(differences.iterator(),
				and(valueUnder(addEAnnotationView), valueIsInstanceof(Bounds.class)));

		assertSame(Integer.valueOf(5),
				count(differences, and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD))));

		final Diff addNodeEPackageExtension = Iterators.find(
				differences.iterator(),
				and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD),
						refinedBy(addRefElementInEPackageView)));
		final Diff addNodeEClassExtension = Iterators.find(
				differences.iterator(),
				and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD),
						refinedBy(addRefElementInEClassView)));
		final Diff addNodeEDataTypeExtension = Iterators.find(
				differences.iterator(),
				and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD),
						refinedBy(addRefElementInEDataTypeView)));
		final Diff addNodeEENumExtension = Iterators.find(
				differences.iterator(),
				and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD),
						refinedBy(addRefElementInEEnumView)));
		final Diff addNodeEAnnotationExtension = Iterators.find(
				differences.iterator(),
				and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD),
						refinedBy(addRefElementInEAnnotationView)));

		assertSame(Integer.valueOf(10), addNodeEPackageExtension.getRefinedBy().size());
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addEPackageView));//for convenience...
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addNodeInEPackageView1));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addNodeInEPackageView2));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addDrawerStyleInNodeInEPackageView2));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addSortingStyleInNodeInEPackageView2));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addFilteringStyleInNodeInEPackageView2));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addShapeStyleInEPackageView));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addMultiDiagramLinkStyleInEPackageView));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addBoundsInEPackageView));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addRefElementInEPackageView));

		assertSame(Integer.valueOf(13), addNodeEClassExtension.getRefinedBy().size());
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addEClassView));//for convenience...
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addNodeInEClassView1));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addNodeInEClassView2));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addDrawerStyleInNodeInEClassView2));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addSortingStyleInNodeInEClassView2));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addFilteringStyleInNodeInEClassView2));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addNodeInEClassView3));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addDrawerStyleInNodeInEClassView3));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addSortingStyleInNodeInEClassView3));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addFilteringStyleInNodeInEClassView));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addShapeStyleInEClassView));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addRefElementInEClassView));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addBoundsInEClassView));

		assertSame(Integer.valueOf(6), addNodeEDataTypeExtension.getRefinedBy().size());
		assertTrue(addNodeEDataTypeExtension.getRefinedBy().contains(addEDataTypeView));// for convenience...
		assertTrue(addNodeEDataTypeExtension.getRefinedBy().contains(addNodeInEDataTypeView1));
		assertTrue(addNodeEDataTypeExtension.getRefinedBy().contains(addNodeInEDataTypeView2));
		assertTrue(addNodeEDataTypeExtension.getRefinedBy().contains(addShapeStyleInEDataTypeView));
		assertTrue(addNodeEDataTypeExtension.getRefinedBy().contains(addRefElementInEDataTypeView));
		assertTrue(addNodeEDataTypeExtension.getRefinedBy().contains(addBoundsInEDataTypeView));

		assertSame(Integer.valueOf(9), addNodeEENumExtension.getRefinedBy().size());
		assertTrue(addNodeEENumExtension.getRefinedBy().contains(addEEnumView));// for convenience...
		assertTrue(addNodeEENumExtension.getRefinedBy().contains(addNodeInEEnumView1));
		assertTrue(addNodeEENumExtension.getRefinedBy().contains(addNodeInEEnumView2));
		assertTrue(addNodeEENumExtension.getRefinedBy().contains(addDrawerStyleInNodeInEEnumView2));
		assertTrue(addNodeEENumExtension.getRefinedBy().contains(addSortingStyleInNodeInEEnumView2));
		assertTrue(addNodeEENumExtension.getRefinedBy().contains(addFilteringStyleInNodeInEEnumView2));
		assertTrue(addNodeEENumExtension.getRefinedBy().contains(addShapeStyleInEEnumView));
		assertTrue(addNodeEENumExtension.getRefinedBy().contains(addRefElementInEEnumView));
		assertTrue(addNodeEENumExtension.getRefinedBy().contains(addBoundsInEEnumView));

		assertSame(Integer.valueOf(9), addNodeEAnnotationExtension.getRefinedBy().size());
		assertTrue(addNodeEAnnotationExtension.getRefinedBy().contains(addEAnnotationView));// for convenience...
		assertTrue(addNodeEAnnotationExtension.getRefinedBy().contains(addNodeInEAnnotationView1));
		assertTrue(addNodeEAnnotationExtension.getRefinedBy().contains(addNodeInEAnnotationView2));
		assertTrue(addNodeEAnnotationExtension.getRefinedBy()
				.contains(addDrawerStyleInNodeInEAnnotationView2));
		assertTrue(addNodeEAnnotationExtension.getRefinedBy().contains(
				addSortingStyleInNodeInEAnnotationView2));
		assertTrue(addNodeEAnnotationExtension.getRefinedBy().contains(
				addFilteringStyleInNodeInEAnnotationView2));
		assertTrue(addNodeEAnnotationExtension.getRefinedBy().contains(addShapeStyleInEAnnotationView));
		assertTrue(addNodeEAnnotationExtension.getRefinedBy().contains(addRefElementInEAnnotationView));
		assertTrue(addNodeEAnnotationExtension.getRefinedBy().contains(addBoundsInEAnnotationView));

		// requires tests
		assertSame(Integer.valueOf(0), addEPackageView.getRequires().size());

		assertSame(Integer.valueOf(1), addNodeInEPackageView1.getRequires().size());
		assertTrue(addNodeInEPackageView1.getRequires().contains(addEPackageView));

		assertSame(Integer.valueOf(1), addNodeInEPackageView2.getRequires().size());
		assertTrue(addNodeInEPackageView2.getRequires().contains(addEPackageView));

		assertSame(Integer.valueOf(1), addDrawerStyleInNodeInEPackageView2.getRequires().size());
		assertTrue(addDrawerStyleInNodeInEPackageView2.getRequires().contains(addNodeInEPackageView2));

		assertSame(Integer.valueOf(1), addSortingStyleInNodeInEPackageView2.getRequires().size());
		assertTrue(addSortingStyleInNodeInEPackageView2.getRequires().contains(addNodeInEPackageView2));

		assertSame(Integer.valueOf(1), addFilteringStyleInNodeInEPackageView2.getRequires().size());
		assertTrue(addFilteringStyleInNodeInEPackageView2.getRequires().contains(addNodeInEPackageView2));

		assertSame(Integer.valueOf(1), addShapeStyleInEPackageView.getRequires().size());
		assertTrue(addShapeStyleInEPackageView.getRequires().contains(addEPackageView));

		assertSame(Integer.valueOf(1), addMultiDiagramLinkStyleInEPackageView.getRequires().size());
		assertTrue(addMultiDiagramLinkStyleInEPackageView.getRequires().contains(addEPackageView));

		assertSame(Integer.valueOf(1), addBoundsInEPackageView.getRequires().size());
		assertTrue(addBoundsInEPackageView.getRequires().contains(addEPackageView));

		assertSame(Integer.valueOf(2), addRefElementInEPackageView.getRequires().size());
		assertTrue(addRefElementInEPackageView.getRequires().contains(addEPackage));
		assertTrue(addRefElementInEPackageView.getRequires().contains(addEPackageView));

		assertFalse("No resource attachment changes expected",
				Iterators.filter(differences.iterator(), instanceOf(ResourceAttachmentChange.class))
						.hasNext());
		
		testIntersections(comparison);
	}

	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right.getResourceSet(),
				left.getResourceSet());
		final Comparison comparison = EMFCompare.builder()
				.setPostProcessorRegistry(getPostProcessorRegistry()).build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 57 differences
		assertSame(Integer.valueOf(57), Integer.valueOf(differences.size()));

		final Diff addEPackage = Iterators.find(differences.iterator(), removed("tc1.EPackage0"));

		final Diff addEPackageView = Iterators.find(
				differences.iterator(),
				and(valueIsView, valueNameMatches("1002", NotationPackage.Literals.VIEW__TYPE),
						elementIs(((ReferenceChange)addEPackage).getValue())));
		final Diff addNodeInEPackageView1 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("4006", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addNodeInEPackageView2 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("5003", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addDrawerStyleInNodeInEPackageView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEPackageView2), valueIsInstanceof(DrawerStyle.class)));
		final Diff addSortingStyleInNodeInEPackageView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEPackageView2), valueIsInstanceof(SortingStyle.class)));
		final Diff addFilteringStyleInNodeInEPackageView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEPackageView2), valueIsInstanceof(FilteringStyle.class)));
		final Diff addShapeStyleInEPackageView = Iterators.find(differences.iterator(),
				and(valueUnder(addEPackageView), valueIsInstanceof(ShapeStyle.class)));
		final Diff addMultiDiagramLinkStyleInEPackageView = Iterators.find(differences.iterator(),
				and(valueUnder(addEPackageView), valueIsInstanceof(MultiDiagramLinkStyle.class)));
		final Diff addBoundsInEPackageView = Iterators.find(differences.iterator(),
				and(valueUnder(addEPackageView), valueIsInstanceof(Bounds.class)));
		final Diff addRefElementInEPackageView = Iterators.find(differences.iterator(),
				and(valueIs(((ReferenceChange)addEPackage).getValue()), onFeature("element")));

		final Diff addEClass = Iterators.find(differences.iterator(), removed("tc1.EClass0"));

		final Diff addEClassView = Iterators.find(
				differences.iterator(),
				and(valueIsView, valueNameMatches("1001", NotationPackage.Literals.VIEW__TYPE),
						elementIs(((ReferenceChange)addEClass).getValue())));
		final Diff addNodeInEClassView1 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("4001", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addNodeInEClassView2 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("5001", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addDrawerStyleInNodeInEClassView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEClassView2), valueIsInstanceof(DrawerStyle.class)));
		final Diff addSortingStyleInNodeInEClassView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEClassView2), valueIsInstanceof(SortingStyle.class)));
		final Diff addFilteringStyleInNodeInEClassView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEClassView2), valueIsInstanceof(FilteringStyle.class)));
		final Diff addNodeInEClassView3 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("5002", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addDrawerStyleInNodeInEClassView3 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEClassView3), valueIsInstanceof(DrawerStyle.class)));
		final Diff addSortingStyleInNodeInEClassView3 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEClassView3), valueIsInstanceof(SortingStyle.class)));
		final Diff addFilteringStyleInNodeInEClassView = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEClassView3), valueIsInstanceof(FilteringStyle.class)));
		final Diff addShapeStyleInEClassView = Iterators.find(differences.iterator(),
				valueUnder(addEClassView));
		final Diff addRefElementInEClassView = Iterators.find(differences.iterator(),
				and(valueIs(((ReferenceChange)addEClass).getValue()), onFeature("element")));
		final Diff addBoundsInEClassView = Iterators.find(differences.iterator(),
				and(valueUnder(addEClassView), valueIsInstanceof(Bounds.class)));

		final Diff addEDataType = Iterators.find(differences.iterator(), removed("tc1.EDataType0"));

		final Diff addEDataTypeView = Iterators.find(
				differences.iterator(),
				and(valueIsView, valueNameMatches("1004", NotationPackage.Literals.VIEW__TYPE),
						elementIs(((ReferenceChange)addEDataType).getValue())));
		final Diff addNodeInEDataTypeView1 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("4008", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addNodeInEDataTypeView2 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("4009", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addShapeStyleInEDataTypeView = Iterators.find(differences.iterator(),
				and(valueUnder(addEDataTypeView), valueIsInstanceof(ShapeStyle.class)));
		final Diff addRefElementInEDataTypeView = Iterators.find(differences.iterator(),
				and(valueIs(((ReferenceChange)addEDataType).getValue()), onFeature("element")));
		final Diff addBoundsInEDataTypeView = Iterators.find(differences.iterator(),
				and(valueUnder(addEDataTypeView), valueIsInstanceof(Bounds.class)));

		final Diff addEEnum = Iterators.find(differences.iterator(), removed("tc1.EEnum0"));

		final Diff addEEnumView = Iterators.find(
				differences.iterator(),
				and(valueIsView, valueNameMatches("1005", NotationPackage.Literals.VIEW__TYPE),
						elementIs(((ReferenceChange)addEEnum).getValue())));
		final Diff addNodeInEEnumView1 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("4010", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addNodeInEEnumView2 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("5008", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addDrawerStyleInNodeInEEnumView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEEnumView2), valueIsInstanceof(DrawerStyle.class)));
		final Diff addSortingStyleInNodeInEEnumView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEEnumView2), valueIsInstanceof(SortingStyle.class)));
		final Diff addFilteringStyleInNodeInEEnumView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEEnumView2), valueIsInstanceof(FilteringStyle.class)));
		final Diff addShapeStyleInEEnumView = Iterators.find(differences.iterator(),
				and(valueUnder(addEEnumView), valueIsInstanceof(ShapeStyle.class)));
		final Diff addRefElementInEEnumView = Iterators.find(differences.iterator(),
				and(valueIs(((ReferenceChange)addEEnum).getValue()), onFeature("element")));
		final Diff addBoundsInEEnumView = Iterators.find(differences.iterator(),
				and(valueUnder(addEEnumView), valueIsInstanceof(Bounds.class)));

		final Diff addEAnnotation = Iterators.find(
				differences.iterator(),
				removedFromReference("tc1", "eAnnotations", "tc1.EAnnotation0",
						EcorePackage.Literals.EANNOTATION__SOURCE));

		final Diff addEAnnotationView = Iterators.find(
				differences.iterator(),
				and(valueIsView, valueNameMatches("1003", NotationPackage.Literals.VIEW__TYPE),
						elementIs(((ReferenceChange)addEAnnotation).getValue())));
		final Diff addNodeInEAnnotationView1 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("4007", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addNodeInEAnnotationView2 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("5007", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addDrawerStyleInNodeInEAnnotationView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEAnnotationView2), valueIsInstanceof(DrawerStyle.class)));
		final Diff addSortingStyleInNodeInEAnnotationView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEAnnotationView2), valueIsInstanceof(SortingStyle.class)));
		final Diff addFilteringStyleInNodeInEAnnotationView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEAnnotationView2), valueIsInstanceof(FilteringStyle.class)));
		final Diff addShapeStyleInEAnnotationView = Iterators.find(differences.iterator(),
				and(valueUnder(addEAnnotationView), valueIsInstanceof(ShapeStyle.class)));
		final Diff addRefElementInEAnnotationView = Iterators.find(differences.iterator(),
				and(valueIs(((ReferenceChange)addEAnnotation).getValue()), onFeature("element")));
		final Diff addBoundsInEAnnotationView = Iterators.find(differences.iterator(),
				and(valueUnder(addEAnnotationView), valueIsInstanceof(Bounds.class)));

		assertSame(Integer.valueOf(5),
				count(differences, and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE))));

		final Diff addNodeEPackageExtension = Iterators.find(differences.iterator(),
				and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE), refinedBy(addEPackageView)));
		final Diff addNodeEClassExtension = Iterators.find(differences.iterator(),
				and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE), refinedBy(addEClassView)));
		final Diff addNodeEDataTypeExtension = Iterators
				.find(differences.iterator(),
						and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE),
								refinedBy(addEDataTypeView)));
		final Diff addNodeEENumExtension = Iterators.find(differences.iterator(),
				and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE), refinedBy(addEEnumView)));
		final Diff addNodeEAnnotationExtension = Iterators.find(
				differences.iterator(),
				and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE),
						refinedBy(addEAnnotationView)));
		
		assertSame(Integer.valueOf(10), addNodeEPackageExtension.getRefinedBy().size());
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addEPackageView));
		//for convenience:
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addNodeInEPackageView1));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addNodeInEPackageView2));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addDrawerStyleInNodeInEPackageView2));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addSortingStyleInNodeInEPackageView2));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addFilteringStyleInNodeInEPackageView2));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addShapeStyleInEPackageView));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addMultiDiagramLinkStyleInEPackageView));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addBoundsInEPackageView));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addRefElementInEPackageView));

		assertSame(Integer.valueOf(13), addNodeEClassExtension.getRefinedBy().size());
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addEClassView));
		//for convenience:
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addNodeInEClassView1));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addNodeInEClassView2));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addDrawerStyleInNodeInEClassView2));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addSortingStyleInNodeInEClassView2));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addFilteringStyleInNodeInEClassView2));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addNodeInEClassView3));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addDrawerStyleInNodeInEClassView3));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addSortingStyleInNodeInEClassView3));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addFilteringStyleInNodeInEClassView));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addShapeStyleInEClassView));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addRefElementInEClassView));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addBoundsInEClassView));

		assertSame(Integer.valueOf(6), addNodeEDataTypeExtension.getRefinedBy().size());
		assertTrue(addNodeEDataTypeExtension.getRefinedBy().contains(addEDataTypeView));
		// for convenience:
		assertTrue(addNodeEDataTypeExtension.getRefinedBy().contains(addNodeInEDataTypeView1));
		assertTrue(addNodeEDataTypeExtension.getRefinedBy().contains(addNodeInEDataTypeView2));
		assertTrue(addNodeEDataTypeExtension.getRefinedBy().contains(addShapeStyleInEDataTypeView));
		assertTrue(addNodeEDataTypeExtension.getRefinedBy().contains(addRefElementInEDataTypeView));
		assertTrue(addNodeEDataTypeExtension.getRefinedBy().contains(addBoundsInEDataTypeView));

		assertSame(Integer.valueOf(9), addNodeEENumExtension.getRefinedBy().size());
		assertTrue(addNodeEENumExtension.getRefinedBy().contains(addEEnumView));
		// for convenience:
		assertTrue(addNodeEENumExtension.getRefinedBy().contains(addNodeInEEnumView1));
		assertTrue(addNodeEENumExtension.getRefinedBy().contains(addNodeInEEnumView2));
		assertTrue(addNodeEENumExtension.getRefinedBy().contains(addDrawerStyleInNodeInEEnumView2));
		assertTrue(addNodeEENumExtension.getRefinedBy().contains(addSortingStyleInNodeInEEnumView2));
		assertTrue(addNodeEENumExtension.getRefinedBy().contains(addFilteringStyleInNodeInEEnumView2));
		assertTrue(addNodeEENumExtension.getRefinedBy().contains(addShapeStyleInEEnumView));
		assertTrue(addNodeEENumExtension.getRefinedBy().contains(addRefElementInEEnumView));
		assertTrue(addNodeEENumExtension.getRefinedBy().contains(addBoundsInEEnumView));

		assertSame(Integer.valueOf(9), addNodeEAnnotationExtension.getRefinedBy().size());
		assertTrue(addNodeEAnnotationExtension.getRefinedBy().contains(addEAnnotationView));
		// for convenience:
		assertTrue(addNodeEAnnotationExtension.getRefinedBy().contains(addNodeInEAnnotationView1));
		assertTrue(addNodeEAnnotationExtension.getRefinedBy().contains(addNodeInEAnnotationView2));
		assertTrue(addNodeEAnnotationExtension.getRefinedBy()
				.contains(addDrawerStyleInNodeInEAnnotationView2));
		assertTrue(addNodeEAnnotationExtension.getRefinedBy().contains(
				addSortingStyleInNodeInEAnnotationView2));
		assertTrue(addNodeEAnnotationExtension.getRefinedBy().contains(
				addFilteringStyleInNodeInEAnnotationView2));
		assertTrue(addNodeEAnnotationExtension.getRefinedBy().contains(addShapeStyleInEAnnotationView));
		assertTrue(addNodeEAnnotationExtension.getRefinedBy().contains(addRefElementInEAnnotationView));
		assertTrue(addNodeEAnnotationExtension.getRefinedBy().contains(addBoundsInEAnnotationView));

		testIntersections(comparison);
	}

	@Test
	public void testA30UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left.getResourceSet(),
				right.getResourceSet());
		final Comparison comparison = EMFCompare.builder()
				.setPostProcessorRegistry(getPostProcessorRegistry()).build().compare(scope);

		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 27 differences
		assertSame(Integer.valueOf(27), Integer.valueOf(differences.size()));

		final Diff addEPackage = Iterators.find(differences.iterator(), added("tc1.EPackage0"));

		final Diff addEPackageView = Iterators.find(
				differences.iterator(),
				and(valueIsView, valueNameMatches("1002", NotationPackage.Literals.VIEW__TYPE),
						elementIs(((ReferenceChange)addEPackage).getValue())));
		final Diff addNodeInEPackageView1 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("4006", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addNodeInEPackageView2 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("5003", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addDrawerStyleInNodeInEPackageView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEPackageView2), valueIsInstanceof(DrawerStyle.class)));
		final Diff addSortingStyleInNodeInEPackageView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEPackageView2), valueIsInstanceof(SortingStyle.class)));
		final Diff addFilteringStyleInNodeInEPackageView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEPackageView2), valueIsInstanceof(FilteringStyle.class)));
		final Diff addShapeStyleInEPackageView = Iterators.find(differences.iterator(),
				and(valueUnder(addEPackageView), valueIsInstanceof(ShapeStyle.class)));
		final Diff addMultiDiagramLinkStyleInEPackageView = Iterators.find(differences.iterator(),
				and(valueUnder(addEPackageView), valueIsInstanceof(MultiDiagramLinkStyle.class)));
		final Diff addBoundsInEPackageView = Iterators.find(differences.iterator(),
				and(valueUnder(addEPackageView), valueIsInstanceof(Bounds.class)));
		final Diff addRefElementInEPackageView = Iterators.find(differences.iterator(),
				and(valueIs(((ReferenceChange)addEPackage).getValue()), onFeature("element")));

		final Diff addEClass = Iterators.find(differences.iterator(), added("tc1.EPackage0.EClass0"));

		final Diff addEClassView = Iterators.find(
				differences.iterator(),
				and(valueIsView, valueNameMatches("2003", NotationPackage.Literals.VIEW__TYPE),
						elementIs(((ReferenceChange)addEClass).getValue())));
		final Diff addNodeInEClassView1 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("4002", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addNodeInEClassView2 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("5004", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addDrawerStyleInNodeInEClassView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEClassView2), valueIsInstanceof(DrawerStyle.class)));
		final Diff addSortingStyleInNodeInEClassView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEClassView2), valueIsInstanceof(SortingStyle.class)));
		final Diff addFilteringStyleInNodeInEClassView2 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEClassView2), valueIsInstanceof(FilteringStyle.class)));
		final Diff addNodeInEClassView3 = Iterators.find(differences.iterator(),
				and(valueIsView, valueNameMatches("5005", NotationPackage.Literals.VIEW__TYPE)));
		final Diff addDrawerStyleInNodeInEClassView3 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEClassView3), valueIsInstanceof(DrawerStyle.class)));
		final Diff addSortingStyleInNodeInEClassView3 = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEClassView3), valueIsInstanceof(SortingStyle.class)));
		final Diff addFilteringStyleInNodeInEClassView = Iterators.find(differences.iterator(),
				and(valueUnder(addNodeInEClassView3), valueIsInstanceof(FilteringStyle.class)));
		final Diff addShapeStyleInEClassView = Iterators.find(differences.iterator(),
				valueUnder(addEClassView));
		final Diff addRefElementInEClassView = Iterators.find(differences.iterator(),
				and(valueIs(((ReferenceChange)addEClass).getValue()), onFeature("element")));
		final Diff addBoundsInEClassView = Iterators.find(differences.iterator(),
				and(valueUnder(addEClassView), valueIsInstanceof(Bounds.class)));

		assertSame(Integer.valueOf(2),
				count(differences, and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD))));

		final Diff addNodeEPackageExtension = Iterators.find(
				differences.iterator(),
				and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD),
						refinedBy(addRefElementInEPackageView)));
		final Diff addNodeEClassExtension = Iterators.find(
				differences.iterator(),
				and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD),
						refinedBy(addRefElementInEClassView)));

		assertSame(Integer.valueOf(10), addNodeEPackageExtension.getRefinedBy().size());
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addEPackageView));// for convenience...
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addNodeInEPackageView1));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addNodeInEPackageView2));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addDrawerStyleInNodeInEPackageView2));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addSortingStyleInNodeInEPackageView2));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addFilteringStyleInNodeInEPackageView2));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addShapeStyleInEPackageView));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addMultiDiagramLinkStyleInEPackageView));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addBoundsInEPackageView));
		assertTrue(addNodeEPackageExtension.getRefinedBy().contains(addRefElementInEPackageView));

		assertSame(Integer.valueOf(13), addNodeEClassExtension.getRefinedBy().size());
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addEClassView));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addNodeInEClassView1));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addNodeInEClassView2));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addDrawerStyleInNodeInEClassView2));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addSortingStyleInNodeInEClassView2));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addFilteringStyleInNodeInEClassView2));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addNodeInEClassView3));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addDrawerStyleInNodeInEClassView3));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addSortingStyleInNodeInEClassView3));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addFilteringStyleInNodeInEClassView));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addShapeStyleInEClassView));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addRefElementInEClassView));
		assertTrue(addNodeEClassExtension.getRefinedBy().contains(addBoundsInEClassView));

		assertFalse("No resource attachment changes expected",
				Iterators.filter(differences.iterator(), instanceOf(ResourceAttachmentChange.class))
						.hasNext());
		
		testIntersections(comparison);
	}

	@Override
	protected DiagramInputData getInput() {
		return input;
	}

	final Predicate<Diff> valueIsView = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input instanceof ReferenceChange && ((ReferenceChange)input).getValue() instanceof View
					&& ((ReferenceChange)input).getReference().isContainment();
		}
	};

	private static Predicate<? super Diff> valueUnder(final Diff container) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				if (input instanceof ReferenceChange) {
					final ReferenceChange diff = (ReferenceChange)input;
					return container instanceof ReferenceChange
							&& diff.getValue().eContainer() == ((ReferenceChange)container).getValue();
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

	private static Predicate<? super Diff> valueIsInstanceof(final Class expectedValue) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				final Object value;
				if (input instanceof ReferenceChange) {
					value = ((ReferenceChange)input).getValue();
				} else if (input instanceof AttributeChange) {
					value = ((AttributeChange)input).getValue();
				} else {
					return false;
				}

				return expectedValue.isInstance(value);
			}
		};
	}

	private static Predicate<? super Diff> elementIs(final EObject expectedValue) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				final Object value;
				if (input instanceof ReferenceChange) {
					value = ((ReferenceChange)input).getValue();
					return value instanceof View && ((View)value).getElement() == expectedValue;
				}
				return false;
			}
		};
	}

}

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
package org.eclipse.emf.compare.diagram.papyrus.tests.groups;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import java.io.IOException;
import java.util.Collection;
import java.util.regex.Pattern;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diagram.ide.ui.internal.structuremergeviewer.filters.GMFRefinedElementsFilter;
import org.eclipse.emf.compare.diagram.internal.extensions.NodeChange;
import org.eclipse.emf.compare.diagram.internal.extensions.provider.spec.ExtensionsItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.diagram.internal.matchs.provider.spec.DiagramCompareItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.diagram.papyrus.internal.PapyrusDiagramPostProcessor;
import org.eclipse.emf.compare.diagram.papyrus.tests.AbstractTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.compare.diagram.papyrus.tests.groups.data.conflictsgroup.ConflictsGroupInputData;
import org.eclipse.emf.compare.provider.spec.CompareItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.StructureMergeViewerFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.ThreeWayComparisonGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.GroupItemProviderAdapter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.tests.postprocess.data.TestPostProcessor;
import org.eclipse.emf.compare.uml2.internal.provider.custom.UMLCompareCustomItemProviderAdapterFactory;
import org.eclipse.emf.compare.uml2.internal.provider.decorator.UMLCompareItemProviderDecoratorAdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.tree.TreeFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.gmf.runtime.notation.provider.NotationItemProviderAdapterFactory;
import org.eclipse.gmf.runtime.notation.util.NotationAdapterFactory;
import org.eclipse.uml2.uml.edit.providers.UMLItemProviderAdapterFactory;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("restriction")
public class PapyrusConflictsGroupProviderTests extends AbstractTest {

	private ConflictsGroupInputData input = new ConflictsGroupInputData();

	private static AdapterFactoryItemDelegator itemDelegator;

	@BeforeClass
	public static void beforeClass() {
		final Collection<AdapterFactory> factories = Lists.newArrayList();
		factories.add(new CompareItemProviderAdapterFactorySpec());
		factories.add(new TreeItemProviderAdapterFactorySpec(new StructureMergeViewerFilter(new EventBus())));
		factories.add(new EcoreItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());
		factories.add(new UMLCompareCustomItemProviderAdapterFactory());
		factories.add(new UMLItemProviderAdapterFactory());
		factories.add(new UMLCompareItemProviderDecoratorAdapterFactory());
		factories.add(new NotationAdapterFactory());
		factories.add(new ExtensionsItemProviderAdapterFactorySpec());
		factories.add(new DiagramCompareItemProviderAdapterFactorySpec());
		factories.add(new NotationItemProviderAdapterFactory());
		final AdapterFactory composedAdapterFactory = new ComposedAdapterFactory(factories);
		itemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);
	}

	/**
	 * The comparison produces several conflicts. One of this conflict concerns the diagram. It involves 4
	 * differences: 2 ReferenceChanges and 2 NodeChanges. The NodeChanges are part of the conflict because
	 * they refine the ReferenceChanges. When the "Diagram refined elements " filer is enabled, the conflict
	 * group must show both NodeChanges. When the "Diagram refined elements " filer is disabled, the conflict
	 * group must show both ReferenceChanges.
	 * 
	 * @throws IOException
	 */
	@Test
	@Ignore("This should be tested manually during validation of releases. The set-up doesn't allow this test to behave like a real running eclipse with papyrus installed.")
	public void testBug478539() throws IOException {

		final Resource left = input.getBug478539Left();
		final Resource right = input.getBug478539Right();
		final Resource origin = input.getBug478539Origin();

		Comparison comparison = buildComparison(left, right, origin);

		EList<Conflict> conflicts = comparison.getConflicts();
		assertEquals(2, conflicts.size());

		Conflict conflict = conflicts.get(0);
		EList<Diff> conflictedDiffs = conflict.getDifferences();
		assertEquals(4, conflictedDiffs.size());
		assertEquals(2, size(filter(conflictedDiffs, NodeChange.class)));
		assertEquals(2, size(filter(conflictedDiffs, ReferenceChange.class)));

		TreeNode groupTreeNode = TreeFactory.eINSTANCE.createTreeNode();
		groupTreeNode.setData(comparison);
		groupTreeNode.eAdapters().add(new ThreeWayComparisonGroupProvider());

		Predicate<? super EObject> diagramFilterEnabled = new GMFRefinedElementsFilter()
				.getPredicateWhenSelected();
		Predicate<? super EObject> diagramFilterDisabled = new GMFRefinedElementsFilter()
				.getPredicateWhenUnselected();

		Collection<?> children = itemDelegator.getChildren(groupTreeNode);
		assertEquals(3, children.size());

		GroupItemProviderAdapter conflictGroup = (GroupItemProviderAdapter)children.iterator().next();
		Collection<?> conflictGroupChildren = itemDelegator.getChildren(conflictGroup);
		assertEquals(2, conflictGroupChildren.size());

		TreeNode firstConflict = (TreeNode)conflictGroupChildren.iterator().next();
		assertEquals("> Conflict [4 out of 4 differences unresolved]", itemDelegator.getText(firstConflict));
		EList<TreeNode> firstConflictChildren = firstConflict.getChildren();
		// the first conflict contains 2 children: a ReferenceChange and a
		// NodeChange.
		assertEquals(2, firstConflictChildren.size());

		// the NodeChange on Class1
		TreeNode shapeClass1 = firstConflictChildren.get(0);
		assertEquals("Shape <Class> Class1 [children delete]", itemDelegator.getText(shapeClass1));
		// visible when filter is enabled because this is a diagram diff
		assertFalse(diagramFilterEnabled.apply(shapeClass1));
		assertTrue(diagramFilterDisabled.apply(shapeClass1));
		EList<TreeNode> shapeClass1Children = shapeClass1.getChildren();
		assertEquals(3, shapeClass1Children.size());

		// the children of NodeChange on Class1
		// first child : the ReferenceChange on Class1
		TreeNode bcClass1 = shapeClass1Children.get(0);
		assertEquals("BasicCompartment <Class> Class1 [children delete]", itemDelegator.getText(bcClass1));
		// visible when filter is disabled because this is refined by a diagram
		// diff
		assertTrue(diagramFilterEnabled.apply(bcClass1));
		assertFalse(diagramFilterDisabled.apply(bcClass1));
		EList<TreeNode> bcClass1Children = bcClass1.getChildren();
		assertEquals(2, bcClass1Children.size());

		// second child : the ReferenceChange on Property att1
		TreeNode shapeAtt1 = shapeClass1Children.get(1);
		assertEquals("Shape <Property> att1 : Class2 [children add]", itemDelegator.getText(shapeAtt1));
		// visible when filter is disabled because this is refined by a diagram
		// diff
		assertTrue(diagramFilterEnabled.apply(shapeAtt1));
		assertFalse(diagramFilterDisabled.apply(shapeAtt1));
		EList<TreeNode> shapeAtt1Children = shapeAtt1.getChildren();
		assertEquals(0, shapeAtt1Children.size());

		// third child : the NodeChange on Property att1
		shapeAtt1 = shapeClass1Children.get(2);
		assertEquals("Shape <Property> att1 : Class2 [children add]", itemDelegator.getText(shapeAtt1));
		// visible when filter is enabled because this is a diagram diff
		assertFalse(diagramFilterEnabled.apply(shapeAtt1));
		assertTrue(diagramFilterDisabled.apply(shapeAtt1));
		shapeAtt1Children = shapeAtt1.getChildren();
		assertEquals(0, shapeAtt1Children.size());

		// the ReferenceChange on Class1
		TreeNode basicCompartmentClass1 = firstConflictChildren.get(1);
		assertEquals("BasicCompartment <Class> Class1 [children delete]",
				itemDelegator.getText(basicCompartmentClass1));
		// visible when filter is disabled because this is refined by a diagram
		// diff
		assertTrue(diagramFilterEnabled.apply(basicCompartmentClass1));
		assertFalse(diagramFilterDisabled.apply(basicCompartmentClass1));
		EList<TreeNode> basicCompartmentClass1Children = basicCompartmentClass1.getChildren();
		assertEquals(2, basicCompartmentClass1Children.size());

		// the children of ReferenceChange on Class1
		// first child : the ReferenceChange on Property att1
		shapeAtt1 = basicCompartmentClass1Children.get(0);
		assertEquals("Shape <Property> att1 : Class2 [children add]", itemDelegator.getText(shapeAtt1));
		// visible when filter is disabled because this is refined by a diagram
		// diff
		assertTrue(diagramFilterEnabled.apply(shapeAtt1));
		assertFalse(diagramFilterDisabled.apply(shapeAtt1));
		shapeAtt1Children = shapeAtt1.getChildren();
		assertEquals(0, shapeAtt1Children.size());

		// second child : the NodeChange on Property att1
		shapeAtt1 = basicCompartmentClass1Children.get(1);
		assertEquals("Shape <Property> att1 : Class2 [children add]", itemDelegator.getText(shapeAtt1));
		// visible when filter is enabled because this is a diagram diff
		assertFalse(diagramFilterEnabled.apply(shapeAtt1));
		assertTrue(diagramFilterDisabled.apply(shapeAtt1));
		shapeAtt1Children = shapeAtt1.getChildren();
		assertEquals(0, shapeAtt1Children.size());
	}

	@Override
	protected DiagramInputData getInput() {
		return input;
	}

	@Override
	public void registerPostProcessors() {
		super.registerPostProcessors();
		getPostProcessorRegistry().put(PapyrusDiagramPostProcessor.class.getName(),
				new TestPostProcessor.TestPostProcessorDescriptor(
						Pattern.compile("http://www.eclipse.org/gmf/runtime/\\d.\\d.\\d/notation"), null,
						new PapyrusDiagramPostProcessor(), 35));
	}
}

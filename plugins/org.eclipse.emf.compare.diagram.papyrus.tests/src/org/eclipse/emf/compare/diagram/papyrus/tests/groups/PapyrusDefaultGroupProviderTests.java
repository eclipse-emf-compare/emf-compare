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
import java.util.Iterator;
import java.util.regex.Pattern;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diagram.ide.ui.internal.structuremergeviewer.filters.GMFRefinedElementsFilter;
import org.eclipse.emf.compare.diagram.internal.extensions.EdgeChange;
import org.eclipse.emf.compare.diagram.internal.extensions.provider.spec.ExtensionsItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.diagram.internal.matchs.provider.spec.DiagramCompareItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.diagram.papyrus.internal.PapyrusDiagramPostProcessor;
import org.eclipse.emf.compare.diagram.papyrus.tests.AbstractTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.compare.diagram.papyrus.tests.groups.data.defaultgroup.DefaultGroupInputData;
import org.eclipse.emf.compare.provider.spec.CompareItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.StructureMergeViewerFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DefaultGroupProvider;
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
import org.junit.Test;

@SuppressWarnings("restriction")
public class PapyrusDefaultGroupProviderTests extends AbstractTest {

	private DefaultGroupInputData input = new DefaultGroupInputData();

	private static AdapterFactoryItemDelegator itemDelegator;

	@BeforeClass
	public static void beforeClass() {
		final Collection<AdapterFactory> factories = Lists.newArrayList();
		factories.add(new CompareItemProviderAdapterFactorySpec());
		factories.add(new TreeItemProviderAdapterFactorySpec(
				new StructureMergeViewerFilter(new EventBus())));
		factories.add(new EcoreItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());
		factories.add(new UMLCompareCustomItemProviderAdapterFactory());
		factories.add(new UMLItemProviderAdapterFactory());
		factories.add(new UMLCompareItemProviderDecoratorAdapterFactory());
		factories.add(new NotationAdapterFactory());
		factories.add(new ExtensionsItemProviderAdapterFactorySpec());
		factories.add(new DiagramCompareItemProviderAdapterFactorySpec());
		factories.add(new NotationItemProviderAdapterFactory());
		final AdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
				factories);
		itemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);
	}

	/**
	 * The comparison produces one conflict. This conflict concerns the diagram.
	 * It involves 4 differences: 1 ReferenceChange, 1 AttributeChange and 2
	 * EdgeChanges. The EdgeChanges are part of the conflict because they refine
	 * the ReferenceChanges. When the "Diagram refined elements " filer is
	 * enabled, the conflict group must show both EdgeChanges. When the
	 * "Diagram refined elements " filer is disabled, the conflict group must
	 * show 1 ReferenceChange & 1 AttributeChange.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testBug480437() throws IOException {

		final Resource left = input.getBug480437Left();
		final Resource right = input.getBug480437Right();
		final Resource origin = input.getBug480437Origin();

		Comparison comparison = buildComparison(left, right, origin);

		EList<Conflict> conflicts = comparison.getConflicts();
		assertEquals(1, conflicts.size());

		Conflict conflict = conflicts.get(0);
		EList<Diff> conflictedDiffs = conflict.getDifferences();
		assertEquals(4, conflictedDiffs.size());
		assertEquals(2, size(filter(conflictedDiffs, EdgeChange.class)));
		assertEquals(1, size(filter(conflictedDiffs, ReferenceChange.class)));
		assertEquals(1, size(filter(conflictedDiffs, AttributeChange.class)));

		TreeNode groupTreeNode = TreeFactory.eINSTANCE.createTreeNode();
		groupTreeNode.setData(comparison);
		groupTreeNode.eAdapters().add(new DefaultGroupProvider());

		Predicate<? super EObject> diagramFilterEnabled = new GMFRefinedElementsFilter()
				.getPredicateWhenSelected();
		Predicate<? super EObject> diagramFilterDisabled = new GMFRefinedElementsFilter()
				.getPredicateWhenUnselected();

		Collection<?> children = itemDelegator.getChildren(groupTreeNode);
		assertEquals(4, children.size());

		TreeNode diagramNode = (TreeNode) children.iterator().next();
		Collection<?> diagramNodeChildren = itemDelegator
				.getChildren(diagramNode);
		assertEquals(7, diagramNodeChildren.size());

		// The firsts children are match nodes (match nodes are always before
		// diff nodes)
		// Iterates until first diff node.
		Iterator<?> iterator = diagramNodeChildren.iterator();
		TreeNode edgeDeleteConnectorClass1_ReferenceChange;
		do {
			edgeDeleteConnectorClass1_ReferenceChange = (TreeNode) iterator
					.next();
		} while (edgeDeleteConnectorClass1_ReferenceChange.getData() instanceof Match);

		assertEquals("Connector <Abstraction> Class1 [edges delete]",
				itemDelegator
						.getText(edgeDeleteConnectorClass1_ReferenceChange));
		// visible when filter is disabled because this is refined by a diagram
		// diff
		assertTrue(diagramFilterEnabled
				.apply(edgeDeleteConnectorClass1_ReferenceChange));
		assertFalse(diagramFilterDisabled
				.apply(edgeDeleteConnectorClass1_ReferenceChange));
		EList<TreeNode> edgeDeleteReferenceChangeChildren = edgeDeleteConnectorClass1_ReferenceChange
				.getChildren();
		assertEquals(10, edgeDeleteReferenceChangeChildren.size());

		// Iterates until first conflicted diff node.
		Iterator<?> iterator2 = edgeDeleteReferenceChangeChildren.iterator();
		TreeNode identityAnchor;
		Diff diff;
		do {
			identityAnchor = (TreeNode) iterator2.next();
			diff = (Diff) identityAnchor.getData();
		} while (diff.getConflict() == null);

		// the ReferenceChange on IdentityAnchor
		assertEquals("Identity Anchor (0.0,0.38) [sourceAnchor delete]",
				itemDelegator.getText(identityAnchor));
		// visible when filter is disabled because this is refined by a diagram
		// diff
		assertTrue(diagramFilterEnabled.apply(identityAnchor));
		assertFalse(diagramFilterDisabled.apply(identityAnchor));
		EList<TreeNode> identityAnchorChildren = identityAnchor.getChildren();
		assertEquals(1, identityAnchorChildren.size());

		// the children of ReferenceChange on Identity Anchor
		// first child : the AttributeChange
		TreeNode idSet = identityAnchorChildren.get(0);
		assertEquals("(0.0,0.7) [id set]", itemDelegator.getText(idSet));
		// visible when filter is disabled because this is refined by a diagram
		// diff
		assertTrue(diagramFilterEnabled.apply(idSet));
		assertFalse(diagramFilterDisabled.apply(idSet));
		EList<TreeNode> idSetChildren = idSet.getChildren();
		assertEquals(0, idSetChildren.size());

		TreeNode edgeDeleteConnectorClass1_EdgeChange = (TreeNode) iterator
				.next();
		assertEquals("Connector <Abstraction> Class1 [edges delete]",
				itemDelegator.getText(edgeDeleteConnectorClass1_EdgeChange));
		// visible when filter is disabled because this a diagram diff
		assertFalse(diagramFilterEnabled
				.apply(edgeDeleteConnectorClass1_EdgeChange));
		assertTrue(diagramFilterDisabled
				.apply(edgeDeleteConnectorClass1_EdgeChange));
		EList<TreeNode> edgeDeleteEdgeChangeChildren = edgeDeleteConnectorClass1_EdgeChange
				.getChildren();
		assertEquals(10, edgeDeleteEdgeChangeChildren.size());

		TreeNode lookChangeEdgeChange = edgeDeleteEdgeChangeChildren.get(9);

		// the EdgeChange change on Connector Class1
		assertEquals("Connector <Abstraction> Class1 [look change]",
				itemDelegator.getText(lookChangeEdgeChange));
		// visible when filter is disabled because this a diagram diff
		assertFalse(diagramFilterEnabled.apply(lookChangeEdgeChange));
		assertTrue(diagramFilterDisabled.apply(lookChangeEdgeChange));
		EList<TreeNode> lookChangeEdgeChangeChildren = lookChangeEdgeChange
				.getChildren();
		assertEquals(0, lookChangeEdgeChangeChildren.size());
	}

	@Override
	protected DiagramInputData getInput() {
		return input;
	}

	@Override
	public void registerPostProcessors() {
		super.registerPostProcessors();
		getPostProcessorRegistry()
				.put(PapyrusDiagramPostProcessor.class.getName(),
						new TestPostProcessor.TestPostProcessorDescriptor(
								Pattern.compile("http://www.eclipse.org/gmf/runtime/\\d.\\d.\\d/notation"),
								null, new PapyrusDiagramPostProcessor(), 35));
	}
}

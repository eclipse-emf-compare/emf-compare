/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.rcp.ui.tests.groups;

import static com.google.common.base.Predicates.alwaysTrue;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.postprocessor.BasicPostProcessorDescriptorImpl;
import org.eclipse.emf.compare.postprocessor.PostProcessorDescriptorRegistryImpl;
import org.eclipse.emf.compare.provider.spec.CompareItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.StructureMergeViewerFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.CascadingDifferencesFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.EmptyMatchedResourcesFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.IdenticalElementsFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.PseudoConflictsFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.BasicDifferenceGroupImpl;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.AbstractTestTreeNodeItemProviderAdapter;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.edit.data.ResourceScopeProvider;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodeSingleValueAttribute;
import org.eclipse.emf.compare.tests.nodes.NodesFactory;
import org.eclipse.emf.compare.uml2.internal.postprocessor.UMLPostProcessor;
import org.eclipse.emf.compare.uml2.internal.provider.custom.UMLCompareCustomItemProviderAdapterFactory;
import org.eclipse.emf.compare.uml2.internal.provider.decorator.UMLCompareItemProviderDecoratorAdapterFactory;
import org.eclipse.emf.compare.uml2.rcp.ui.internal.structuremergeviewer.filters.UMLRefinedElementsFilter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.emf.edit.tree.TreePackage;
import org.eclipse.uml2.uml.edit.providers.UMLItemProviderAdapterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the order of the differences in the StructuredMergeView for UML differences.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
@SuppressWarnings("restriction")
public class UMLDifferencesOrderTest extends AbstractTestTreeNodeItemProviderAdapter {
	/**
	 * Holds the expected result.
	 */
	private static final List<Node> expectedRoot = new ArrayList<Node>(2);

	/**
	 * Predicate to compute the expected result when the {@link UMLRefinedElementsFilter} is activated.
	 */
	private static final Predicate<EObject> FILTER_ELEMENT_PREDICATE = new Predicate<EObject>() {

		public boolean apply(EObject input) {
			return !Boolean.valueOf(((NodeSingleValueAttribute)input).getSingleValuedAttribute())
					.booleanValue();
		}
	};

	@SuppressWarnings("nls")
	@BeforeClass
	public static void beforeClass() {
		// @formatter:off
		//Creates a structure holding the expected result
			Node rootDiff = newNode("<Model> model",null);
			expectedRoot.add(rootDiff);
				newNode("<Package> OldHoldingPackage",rootDiff);
				Node l1  = newNode("<Package> NewHoldingPackage",rootDiff);
					newNode("<Class> MovingNode [packagedElement move]",l1);
				l1 = newNode("<Class> AssociationSource",rootDiff,true);
					Node l2 = newNode("<Property> associationOldTarget : AssociationOl... [ownedAttribute delete]",l1,true);
						newNode("<Class> AssociationOldTarget [type unset]",l2,true);
						newNode("<Literal Integer> 1 [lowerValue delete]",l2,true);
						newNode("<Literal Unlimited Natural> 1 [upperValue delete]",l2,true);
						newNode("<Association> A_associationSource_associationOl... [association unset]",l2,true);
				newNode("<Class> AssociationOldTarget",rootDiff);
				newNode("<Class> AssociationNewTarget",rootDiff);
				l1 = newNode("<Class> ChangingNameNode_NewName",rootDiff);
					newNode("ChangingNameNode_NewName [name changed]",l1);
				l1 = newNode("<Class> PseudoConflictingNode_NewName",rootDiff);
					newNode("PseudoConflictingNode [name changed]",l1);
					newNode("PseudoConflictingNode [name changed]",l1);
				newNode("<Class> NewNode [packagedElement add]",rootDiff);
				l1 = newNode("<Association> A_associationSource_associationOl... [packagedElement delete]",rootDiff);
					newNode("<Property> associationSource : AssociationSource [memberEnd delete]",l1,true);
					newNode("<Property> associationOldTarget : AssociationOl... [memberEnd delete]",l1,true);
					l2 = newNode("<Property> associationSource : AssociationSource [ownedEnd delete]",l1,true);
						newNode("<Class> AssociationSource [type unset]",l2,true);
						newNode("<Literal Integer> 1 [lowerValue delete]",l2,true);
						newNode("<Literal Unlimited Natural> 1 [upperValue delete]",l2,true);
						newNode("<Association> A_associationSource_associationOl... [association unset]",l2,true);
					newNode("<Property> associationSource : AssociationSource [navigableOwnedEnd delete]",l1,true);
				newNode("<Class> NodeToDelete [packagedElement delete]",rootDiff);
				l1 = newNode("<Class> ConflictingNode [packagedElement delete]",rootDiff);
					newNode("ConflictingNode_ConflictingName [name set]",l1);
			expectedRoot.add(newNode( "left.uml <-> right.uml (origin.uml)",null));
		// @formatter:on

	}

	/**
	 * Resulting comparison.
	 */
	private Comparison comparison;

	private ECrossReferenceAdapter crossReferenceAdapter;

	private AdapterFactoryItemDelegator itemDelegator;

	private StructureMergeViewerFilter filter;

	@Override
	@Before
	public void before() throws IOException {
		super.before();

		final Collection<AdapterFactory> factories = Lists.newArrayList();
		factories.add(new CompareItemProviderAdapterFactorySpec());
		factories.add(new TreeItemProviderAdapterFactorySpec());
		factories.add(new UMLCompareCustomItemProviderAdapterFactory());
		factories.add(new UMLItemProviderAdapterFactory());
		factories.add(new UMLCompareItemProviderDecoratorAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());

		final AdapterFactory composedAdapterFactory = new ComposedAdapterFactory(factories);
		itemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);

		comparison = getComparison(new DataInput());
		crossReferenceAdapter = new ECrossReferenceAdapter() {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#isIncluded(org.eclipse.emf.ecore.EReference)
			 */
			@Override
			protected boolean isIncluded(EReference eReference) {
				return eReference == TreePackage.Literals.TREE_NODE__DATA;
			}
		};
		// Sets the basic setting for the filter
		filter = new StructureMergeViewerFilter(new EventBus());
		IdenticalElementsFilter identicalFilter = new IdenticalElementsFilter();
		EmptyMatchedResourcesFilter emptyResourceFilter = new EmptyMatchedResourcesFilter();
		PseudoConflictsFilter pseudoConflictFilter = new PseudoConflictsFilter();
		CascadingDifferencesFilter cascadingFilter = new CascadingDifferencesFilter();
		filter.removeFilter(cascadingFilter);
		filter.removeFilter(pseudoConflictFilter);
		filter.removeFilter(emptyResourceFilter);
		filter.removeFilter(identicalFilter);
	}

	/**
	 * Test the representation structured of UML differences without the UML refine filter activated.
	 */
	@Test
	public void testDiffOrder() {
		IDifferenceGroup group = new BasicDifferenceGroupImpl(comparison, alwaysTrue(), crossReferenceAdapter);
		List<? extends TreeNode> roots = group.getChildren();
		assertEquals(2, roots.size());

		UMLRefinedElementsFilter umlRefineFilter = new UMLRefinedElementsFilter();
		filter.removeFilter(umlRefineFilter);

		for (int i = 0; i < expectedRoot.size(); i++) {
			compareTree(expectedRoot.get(i), false, roots.get(i));
		}

	}

	/**
	 * Test the representation structured of UML differences with the UML refine filter activated.
	 */
	@Test
	public void testDiffOrderWithUMLRefineFilter() {
		IDifferenceGroup group = new BasicDifferenceGroupImpl(comparison, alwaysTrue(), crossReferenceAdapter);
		List<? extends TreeNode> roots = group.getChildren();
		assertEquals(2, roots.size());

		UMLRefinedElementsFilter umlRefineFilter = new UMLRefinedElementsFilter();
		filter.addFilter(umlRefineFilter);

		for (int i = 0; i < expectedRoot.size(); i++) {
			compareTree(expectedRoot.get(i), true, roots.get(i));
		}

	}

	/**
	 * Get the comparison using the {@link UMLPostProcessor}.
	 */
	protected static Comparison getComparison(ResourceScopeProvider scopeProvider) throws IOException {
		final IComparisonScope scope = new DefaultComparisonScope(scopeProvider.getLeft(), scopeProvider
				.getRight(), scopeProvider.getOrigin());
		Builder builder = EMFCompare.builder();
		PostProcessorDescriptorRegistryImpl<Object> postProcessorRegistry = new PostProcessorDescriptorRegistryImpl<Object>();
		BasicPostProcessorDescriptorImpl descriptor = new BasicPostProcessorDescriptorImpl(
				new UMLPostProcessor(), Pattern.compile("http://www.eclipse.org/uml2/\\d.0.0/UML"), null); //$NON-NLS-1$
		postProcessorRegistry.put(UMLPostProcessor.class.getName(), descriptor);
		builder.setPostProcessorRegistry(postProcessorRegistry);
		return builder.build().compare(scope);
	}

	/**
	 * Add a new node to a tree. This node is not expected to be filtered by UML Refine filter.
	 * 
	 * @param name
	 *            Name of the node.
	 * @param parent
	 *            Parent node (or null).
	 * @return The newly created node.
	 */
	private static Node newNode(String name, Node parent) {
		return newNode(name, parent, false);
	}

	/**
	 * Add a new node to a tree.
	 * 
	 * @param name
	 *            Name of the node.
	 * @param parent
	 *            Parent node (or null).
	 * @param isFiltered
	 *            set to <code>true</code> if this node is expected to be filtered by the UML Refine filter.
	 * @return The newly created node.
	 */
	private static Node newNode(String name, Node parent, boolean isFiltered) {
		NodeSingleValueAttribute result = NodesFactory.eINSTANCE.createNodeSingleValueAttribute();
		result.setSingleValuedAttribute(Boolean.toString(isFiltered));
		result.setName(name);
		if (parent != null) {
			parent.getContainmentRef1().add(result);
		}
		return result;
	}

	/**
	 * Compare the expected difference tree with the real difference tree.
	 * 
	 * @param expectedTree
	 *            Expected tree.
	 * @param activatedfilter
	 *            Set to true if the expected difference tree should emulated UML Refine filter.
	 * @param realTree
	 *            Real tree.
	 */
	private void compareTree(Node expectedTree, boolean activatedfilter, TreeNode realTree) {
		// Filters elements to mock the behavior of the viewer filter.
		final Iterator<EObject> realIterator = Iterators.filter(realTree.eAllContents(),
				new Predicate<EObject>() {
					public boolean apply(EObject input) {
						AdapterImpl adapter = new AdapterImpl();
						adapter.setTarget(input);
						return filter.select(null, null, adapter);
					}
				});
		// Computes the expected result
		final Iterator<EObject> expectedIterator;
		if (activatedfilter) {
			expectedIterator = Iterators.filter(expectedTree.eAllContents(), FILTER_ELEMENT_PREDICATE);
		} else {
			expectedIterator = expectedTree.eAllContents();
		}
		// Compares
		while (expectedIterator.hasNext()) {
			Node expectedElement = (Node)expectedIterator.next();
			Assert.assertTrue("No match for element " + expectedElement.getName(), realIterator.hasNext()); //$NON-NLS-1$
			EObject realElem = realIterator.next();
			// Checks same name.
			Assert.assertEquals(expectedElement.getName(), itemDelegator.getText(realElem));
			if (expectedElement.eContainer() != null) {
				final Collection<Node> expectedChildren;
				// Computes expected children
				if (activatedfilter) {
					expectedChildren = Collections2.filter(expectedElement.getContainmentRef1(),
							FILTER_ELEMENT_PREDICATE);
				} else {
					expectedChildren = expectedElement.getContainmentRef1();
				}
				// Checks same number of children.
				Assert.assertEquals("Incorrect children for " + itemDelegator.getText(realElem), //$NON-NLS-1$
						expectedChildren.size(), ((TreeNode)realElem).getChildren().size());
			}
		}
	}

	/**
	 * Input data for this test.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	public static class DataInput extends AbstractInputData implements ResourceScopeProvider {

		public Resource getLeft() throws IOException {
			return loadFromClassLoader("data/a1/left.uml");//$NON-NLS-1$
		}

		public Resource getOrigin() throws IOException {
			return loadFromClassLoader("data/a1/origin.uml");//$NON-NLS-1$
		}

		public Resource getRight() throws IOException {
			return loadFromClassLoader("data/a1/right.uml");//$NON-NLS-1$
		}
	}

}

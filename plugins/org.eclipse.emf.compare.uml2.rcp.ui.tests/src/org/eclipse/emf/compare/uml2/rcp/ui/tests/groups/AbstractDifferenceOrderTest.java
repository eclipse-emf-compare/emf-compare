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

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor.Registry;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.StructureMergeViewerFilter;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodesFactory;
import org.eclipse.emf.compare.tests.nodes.util.NodesResourceFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.emf.edit.tree.TreePackage;
import org.junit.Assert;
import org.junit.Before;

/**
 * Abstract class used to test differences order the way they would be displayed in the structure merge
 * viewer.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public abstract class AbstractDifferenceOrderTest {

	/**
	 * Resulting comparison.
	 */
	private Comparison comparison;

	private ECrossReferenceAdapter crossReferenceAdapter;

	private AdapterFactoryItemDelegator itemDelegator;

	/**
	 * Predicate mocking the {@link StructureMergeViewerFilter} behavior.
	 */
	private Predicate<EObject> viewerFilterPredicate;

	/**
	 * {@link StructureMergeViewerFilter} use to filters elements from a {@link TreeNode}.
	 */
	private StructureMergeViewerFilter filter;

	@Before
	public void before() throws IOException {

		final Collection<AdapterFactory> factories = Lists.newArrayList();
		/*
		 * Adds all AdapterFactories to display the tree the same way it's being displayed in the structure
		 * merge viewer.
		 */
		factories.addAll(getAdaptersFactory());

		final AdapterFactory composedAdapterFactory = new ComposedAdapterFactory(factories);
		itemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);

		comparison = getComparison(getInput());
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
		filter = new StructureMergeViewerFilter(new EventBus());
		viewerFilterPredicate = new Predicate<EObject>() {
			public boolean apply(EObject input) {
				AdapterImpl adapter = new AdapterImpl();
				adapter.setTarget(input);
				return filter.select(null, null, adapter);
			}
		};

	}

	/**
	 * Compares the expected result with the list of roots.
	 * 
	 * @param expectedResult
	 *            Resource holding a Nodes models. This "nodes" model represent the expected.
	 * @param actualTrees
	 *            Actual trees that the viewer would display (without any filter being applied).
	 */
	protected void compareTree(Resource expectedResult, List<? extends TreeNode> actualTrees) {
		List<? extends TreeNode> nonFilteredActualRoot = Lists.newArrayList(Collections2.filter(actualTrees,
				viewerFilterPredicate));
		EList<EObject> expectedContent = expectedResult.getContents();
		Assert.assertEquals(expectedContent.size(), nonFilteredActualRoot.size());

		for (int i = 0; i < expectedContent.size(); i++) {
			Node expectedRootNode = (Node)expectedContent.get(i);
			TreeNode actualRoot = nonFilteredActualRoot.get(i);
			TreeIterator<EObject> expectedIterator = expectedRootNode.eAllContents();
			// Filters elements the way the structure merge viewer would with this filter.
			final Iterator<EObject> realIterator = Iterators.filter(actualRoot.eAllContents(),
					viewerFilterPredicate);
			// Compares
			compareTree(expectedIterator, realIterator);
		}

	}

	private void compareTree(TreeIterator<EObject> expectedIterator, final Iterator<EObject> realIterator) {
		while (expectedIterator.hasNext()) {
			Node expectedElement = (Node)expectedIterator.next();
			Assert.assertTrue("No match for element " + expectedElement.getName(), realIterator.hasNext()); //$NON-NLS-1$
			EObject actualElem = realIterator.next();

			// Checks same name.
			Assert.assertEquals(getErrorMessage(actualElem), expectedElement.getName(), itemDelegator
					.getText(actualElem));
			if (expectedElement.eContainer() != null) {
				final Collection<Node> expectedChildren = expectedElement.getContainmentRef1();
				// Checks same number of children.
				Assert.assertEquals("Incorrect children for " + getFullPath(actualElem), //$NON-NLS-1$
						expectedChildren.size(), Collections2.filter(((TreeNode)actualElem).getChildren(),
								viewerFilterPredicate).size());
			}
		}
	}

	/**
	 * @return List of AdapterFactory used to fill the {@link AdapterFactoryItemDelegator}
	 */
	protected abstract List<AdapterFactory> getAdaptersFactory();

	/**
	 * @return Input data for the comparison.
	 */
	protected abstract NotifierScopeProvider getInput();

	protected Comparison getComparison() {
		return comparison;
	}

	/**
	 * Returns the resulting comparison.
	 * 
	 * @param scopeProvider
	 *            Input of the comparison.
	 * @return {@link Comparison}
	 * @throws IOException
	 */
	private Comparison getComparison(NotifierScopeProvider scopeProvider) throws IOException {
		final IComparisonScope scope = new DefaultComparisonScope(scopeProvider.getLeft(), scopeProvider
				.getRight(), scopeProvider.getOrigin());
		Builder builder = EMFCompare.builder();
		Registry<?> postProcessorRegistry = getPostProcessorRegistry();
		if (postProcessorRegistry != null) {
			builder.setPostProcessorRegistry(postProcessorRegistry);
		}
		return builder.build().compare(scope);
	}

	protected ECrossReferenceAdapter getCrossReferenceAdapter() {
		return crossReferenceAdapter;
	}

	/**
	 * Creates a understandable error message.
	 * 
	 * @param actual
	 * @return
	 */
	private String getErrorMessage(EObject actual) {
		StringBuilder message = new StringBuilder();
		message.append("Content error on \"").append(getFullPath(actual)).append("\""); //$NON-NLS-1$ //$NON-NLS-2$
		return message.toString();
	}

	protected StructureMergeViewerFilter getFilter() {
		return filter;
	}

	/**
	 * Creates a String representing the full path of the element.
	 * 
	 * @param eObject
	 * @return
	 */
	private String getFullPath(EObject eObject) {
		final String result;
		if (eObject != null) {
			List<String> ancestors = new ArrayList<String>();
			ancestors.add(itemDelegator.getText(eObject));
			EObject eContainer = eObject.eContainer();
			while (eContainer != null) {
				ancestors.add(itemDelegator.getText(eContainer));
				eContainer = eContainer.eContainer();
			}
			result = Joiner.on("::").join(Lists.reverse(ancestors)); //$NON-NLS-1$

		} else {
			result = null;
		}
		return result;
	}

	/**
	 * Gets the {@link IPostProcessor.Descriptor.Registry} to use for the comparison or null if the default
	 * registry has to be used.
	 * 
	 * @return
	 */
	protected IPostProcessor.Descriptor.Registry<?> getPostProcessorRegistry() {
		return null;
	}

	// Used with TestWriterHelper for writing the expected model into a file.
	protected AdapterFactoryItemDelegator getItemDelegator() {
		return itemDelegator;
	}

	/**
	 * Creates a {@link TestWriterHelper} for this test.
	 * 
	 * @return
	 */
	protected final TestWriterHelper createTestHelper() {
		return new TestWriterHelper();
	}

	/**
	 * This class aims to help the writer of the test.
	 * <p>
	 * Once the tree in the {@link StructureMergeViewerFilter} has been MANUALLY checked, this class helps to
	 * serialize it in a resource. This resources holds a "Nodes" model. It represents the tree as it is
	 * displayed in the structure merge viewer.
	 * </p>
	 * model
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	protected final class TestWriterHelper {

		private TestWriterHelper() {
		}

		/**
		 * Creates the expected model from a list of TreeNode.
		 * <p>
		 * This method translates a list of {@link TreeNode} into a Nodes model. This model represents the
		 * tree as it is display in the StructureMergeViewer.
		 * </p>
		 * 
		 * @param fileLocation
		 *            Location of newly created file will be serialized.
		 * @param roots
		 *            Lists of roots to serialize in the model.
		 */
		public void createExpectedModel(String fileLocation, List<? extends TreeNode> roots) {
			URI fileURI = URI.createFileURI(fileLocation);
			Resource.Factory resourceFactory = new NodesResourceFactoryImpl();
			// resourceFactory cannot be null
			Resource res = resourceFactory.createResource(fileURI);
			fillTree(res, roots);
			try {
				res.save(Collections.EMPTY_MAP);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		/**
		 * Translates each {@link TreeNode} into a {@link Node}.
		 * <p>
		 * The label of each {@link TreeNode} displayed in the viewer is set as label of the node
		 * </p>
		 * 
		 * @param n
		 * @return
		 */
		private Node createNode(TreeNode n) {
			Node newNode = null;
			if (viewerFilterPredicate.apply(n)) {
				newNode = NodesFactory.eINSTANCE.createNode();
				newNode.setName(itemDelegator.getText(n));
				for (TreeNode child : n.getChildren()) {
					Node createNode = createNode(child);
					if (createNode != null) {
						newNode.getContainmentRef1().add(createNode);
					}
				}
			}
			return newNode;
		}

		private void fillTree(Resource resource, List<? extends TreeNode> roots) {
			for (TreeNode n : roots) {
				Node createNode = createNode(n);
				if (createNode != null) {
					resource.getContents().add(createNode);
				}
			}
		}
	}
}

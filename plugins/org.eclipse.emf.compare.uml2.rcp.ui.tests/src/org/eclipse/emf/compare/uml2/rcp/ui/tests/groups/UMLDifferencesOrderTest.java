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

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.postprocessor.BasicPostProcessorDescriptorImpl;
import org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor.Registry;
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
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.compare.uml2.internal.postprocessor.UMLPostProcessor;
import org.eclipse.emf.compare.uml2.internal.provider.custom.UMLCompareCustomItemProviderAdapterFactory;
import org.eclipse.emf.compare.uml2.internal.provider.decorator.UMLCompareItemProviderDecoratorAdapterFactory;
import org.eclipse.emf.compare.uml2.rcp.ui.internal.structuremergeviewer.filters.UMLRefinedElementsFilter;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.uml2.uml.edit.providers.UMLItemProviderAdapterFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the order of the differences the way they would be displayed in the structure merge viewer for UML
 * differences.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class UMLDifferencesOrderTest extends AbstractDifferenceOrderTest {

	private ExpectedResultData expectedResult;

	@Before
	@Override
	public void before() throws IOException {
		super.before();
		getFilter().removeFilter(new CascadingDifferencesFilter());
		getFilter().removeFilter(new PseudoConflictsFilter());
		getFilter().removeFilter(new EmptyMatchedResourcesFilter());
		getFilter().removeFilter(new IdenticalElementsFilter());
		expectedResult = new ExpectedResultData();
	}

	@Override
	protected NotifierScopeProvider getInput() {
		return new DataInput();
	}

	@Override
	protected List<AdapterFactory> getAdaptersFactory() {
		eventBus = new EventBus();
		return Lists.<AdapterFactory> newArrayList(new CompareItemProviderAdapterFactorySpec(),
				new TreeItemProviderAdapterFactorySpec(new StructureMergeViewerFilter(eventBus)),
				new UMLCompareCustomItemProviderAdapterFactory(), new UMLItemProviderAdapterFactory(),
				new UMLCompareItemProviderDecoratorAdapterFactory(),
				new ReflectiveItemProviderAdapterFactory());
	}

	@Override
	protected Registry<?> getPostProcessorRegistry() {
		PostProcessorDescriptorRegistryImpl<Object> postProcessorRegistry = new PostProcessorDescriptorRegistryImpl<Object>();
		BasicPostProcessorDescriptorImpl descriptor = new BasicPostProcessorDescriptorImpl(
				new UMLPostProcessor(), Pattern.compile("http://www.eclipse.org/uml2/\\d.0.0/UML"), null); //$NON-NLS-1$
		postProcessorRegistry.put(UMLPostProcessor.class.getName(), descriptor);
		return postProcessorRegistry;
	}

	/**
	 * Tests the structured representation of UML differences without the UML refine filter activated.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testDiffOrder() throws IOException {
		UMLRefinedElementsFilter umlRefineFilter = new UMLRefinedElementsFilter();
		getFilter().removeFilter(umlRefineFilter);

		IDifferenceGroup group = new BasicDifferenceGroupImpl(getComparison(), alwaysTrue(),
				getCrossReferenceAdapter());
		List<? extends TreeNode> roots = group.getChildren();
		// Uncomment the following lines to reserialize the expected model
		// TestWriterHelper writerHelper = createTestHelper();
		// writerHelper.createExpectedModel("PATH_TO_MODEL_FILE/expectedResult.nodes", roots,false);

		compareTree(expectedResult.getExpectedResultWithoutFilter(), roots, false);

	}

	/**
	 * Tests the structured representation of UML differences with the UML refine filter activated.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testDiffOrderWithUMLRefineFilter() throws IOException {
		UMLRefinedElementsFilter umlRefineFilter = new UMLRefinedElementsFilter();
		getFilter().addFilter(umlRefineFilter);

		IDifferenceGroup group = new BasicDifferenceGroupImpl(getComparison(), alwaysTrue(),
				getCrossReferenceAdapter());
		List<? extends TreeNode> roots = group.getChildren();
		// Uncomment the following lines to reserialize the expected model
		// TestWriterHelper writerHelper = createTestHelper();
		// writerHelper.createExpectedModel("PATH_TO_MODEL_FILE/expectedResultWithUMLRefineElement.nodes",
		// roots);

		compareTree(expectedResult.getExpectedResultWithFilter(), roots, false);

	}

	/**
	 * Input data for this test.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static class DataInput extends AbstractInputData implements NotifierScopeProvider {

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

	/**
	 * Expected result data.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static class ExpectedResultData extends AbstractInputData {

		public Resource getExpectedResultWithoutFilter() throws IOException {
			return loadFromClassLoader("data/a1/expectedResultWithoutUMLRefineElementFilter.nodes"); //$NON-NLS-1$
		}

		public Resource getExpectedResultWithFilter() throws IOException {
			return loadFromClassLoader("data/a1/expectedResultWithUMLRefineElementFilter.nodes"); //$NON-NLS-1$
		}
	}

}

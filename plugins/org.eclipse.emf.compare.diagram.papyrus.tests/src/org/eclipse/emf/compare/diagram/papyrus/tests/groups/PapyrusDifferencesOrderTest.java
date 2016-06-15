/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.groups;

import static com.google.common.base.Predicates.alwaysTrue;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.diagram.ide.ui.internal.structuremergeviewer.filters.GMFRefinedElementsFilter;
import org.eclipse.emf.compare.diagram.internal.CompareDiagramPostProcessor;
import org.eclipse.emf.compare.diagram.internal.extensions.provider.spec.ExtensionsItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.diagram.internal.matchs.provider.spec.DiagramCompareItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
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
import org.eclipse.emf.compare.uml2.rcp.ui.tests.groups.AbstractDifferenceOrderTest;
import org.eclipse.emf.compare.uml2.rcp.ui.tests.groups.NotifierScopeProvider;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.gmf.runtime.notation.provider.NotationItemProviderAdapterFactory;
import org.eclipse.gmf.runtime.notation.util.NotationAdapterFactory;
import org.eclipse.uml2.uml.edit.providers.UMLItemProviderAdapterFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the order of the differences they would be displayed in the structure merge viewer for MDT Papyrus
 * model.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class PapyrusDifferencesOrderTest extends AbstractDifferenceOrderTest {

	/**
	 * Data holding expecting result.
	 */
	private ExpectedResultData expectedResultData;

	private EventBus eventBus;

	@Before
	@Override
	public void before() throws IOException {
		super.before();
		getFilter().removeFilter(new CascadingDifferencesFilter());
		getFilter().removeFilter(new PseudoConflictsFilter());
		getFilter().removeFilter(new EmptyMatchedResourcesFilter());
		// Adds this filter for clarity
		getFilter().addFilter(new IdenticalElementsFilter());
		getFilter().addFilter(new UMLRefinedElementsFilter());
		expectedResultData = new ExpectedResultData();
	}

	@Override
	protected Registry<?> getPostProcessorRegistry() {
		PostProcessorDescriptorRegistryImpl<Object> postProcessorRegistry = new PostProcessorDescriptorRegistryImpl<Object>();
		// Adds UML post processor
		BasicPostProcessorDescriptorImpl descriptor = new BasicPostProcessorDescriptorImpl(
				new UMLPostProcessor(), Pattern.compile("http://www.eclipse.org/uml2/\\d.0.0/UML"), null); //$NON-NLS-1$
		postProcessorRegistry.put(UMLPostProcessor.class.getName(), descriptor);
		// Adds Diagram post processor
		BasicPostProcessorDescriptorImpl descriptor2 = new BasicPostProcessorDescriptorImpl(
				new CompareDiagramPostProcessor(),
				Pattern.compile("http://www.eclipse.org/gmf/runtime/\\d.\\d.\\d/notation"), null); //$NON-NLS-1$
		postProcessorRegistry.put(CompareDiagramPostProcessor.class.getName(), descriptor2);
		return postProcessorRegistry;
	}

	@Override
	protected NotifierScopeProvider getInput() {
		return new InputData();
	}

	@Override
	protected List<AdapterFactory> getAdaptersFactory() {
		eventBus = new EventBus();
		return Lists.<AdapterFactory> newArrayList(new CompareItemProviderAdapterFactorySpec(),
				new TreeItemProviderAdapterFactorySpec(new StructureMergeViewerFilter(eventBus)),
				new UMLCompareCustomItemProviderAdapterFactory(), new UMLItemProviderAdapterFactory(),
				new UMLCompareItemProviderDecoratorAdapterFactory(),
				new ReflectiveItemProviderAdapterFactory(), new NotationAdapterFactory(),
				new ExtensionsItemProviderAdapterFactorySpec(),
				new DiagramCompareItemProviderAdapterFactorySpec(), new NotationItemProviderAdapterFactory());
	}

	@Test
	public void testPapyrusDiffWithoutDiagRefineFilter() throws IOException {
		GMFRefinedElementsFilter diagramFilter = new GMFRefinedElementsFilter();
		getFilter().removeFilter(diagramFilter);

		IDifferenceGroup group = new BasicDifferenceGroupImpl(getComparison(), alwaysTrue(),
				getCrossReferenceAdapter());
		List<? extends TreeNode> roots = group.getChildren();

		// Uncomment the following lines to reserialize the expected model
		// TestWriterHelper writerHelper = createTestHelper();
		// writerHelper.createExpectedModel(PATH_TO_MODEL_FILE+"/expectedResult_DiagRefineOff.nodes",
		// roots,false);

		compareTree(expectedResultData.getExpectedReseultWithFilterOff(), roots, false);
	}

	@Test
	public void testPapyrusDiffWithDiagRefineFilter() throws IOException {
		GMFRefinedElementsFilter diagramFilter = new GMFRefinedElementsFilter();
		getFilter().addFilter(diagramFilter);

		IDifferenceGroup group = new BasicDifferenceGroupImpl(getComparison(), alwaysTrue(),
				getCrossReferenceAdapter());
		List<? extends TreeNode> roots = group.getChildren();

		// Uncomment the following lines to reserialize the expected model
		// TestWriterHelper writerHelper = createTestHelper();
		// writerHelper.createExpectedModel(PATH_TO_MODEL_FILE+"/expectedResult_DiagRefineOn.nodes",
		// roots);

		compareTree(expectedResultData.getExpectedReseultWithFilterOn(), roots, false);
	}

	/**
	 * Expected result data.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static final class ExpectedResultData extends AbstractInputData {

		public Resource getExpectedReseultWithFilterOn() throws IOException {
			return loadFromClassLoader("data/a1/expectedResult_DiagRefineOn.nodes");
		}

		public Resource getExpectedReseultWithFilterOff() throws IOException {
			return loadFromClassLoader("data/a1/expectedResult_DiagRefineOff.nodes");
		}
	}

	/**
	 * Input data for this test.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static final class InputData extends DiagramInputData implements NotifierScopeProvider {

		public ResourceSet getLeft() throws IOException {
			return loadFromClassLoader("data/a1/left.notation").getResourceSet();
		}

		public ResourceSet getRight() throws IOException {
			return loadFromClassLoader("data/a1/right.notation").getResourceSet();
		}

		public ResourceSet getOrigin() throws IOException {
			return loadFromClassLoader("data/a1/origin.notation").getResourceSet();
		}
	}

}

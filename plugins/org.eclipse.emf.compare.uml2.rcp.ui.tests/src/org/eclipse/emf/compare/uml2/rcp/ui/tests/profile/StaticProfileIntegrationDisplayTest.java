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
package org.eclipse.emf.compare.uml2.rcp.ui.tests.profile;

import static com.google.common.base.Predicates.alwaysTrue;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.postprocessor.BasicPostProcessorDescriptorImpl;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.postprocessor.PostProcessorDescriptorRegistryImpl;
import org.eclipse.emf.compare.provider.spec.CompareItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.StructureMergeViewerFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.CascadingDifferencesFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.EmptyMatchedResourcesFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.IdenticalElementsFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.BasicDifferenceGroupImpl;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.compare.uml2.internal.postprocessor.StereotypedElementChangePostProcessor;
import org.eclipse.emf.compare.uml2.internal.postprocessor.UMLPostProcessor;
import org.eclipse.emf.compare.uml2.internal.provider.custom.UMLCompareCustomItemProviderAdapterFactory;
import org.eclipse.emf.compare.uml2.internal.provider.decorator.UMLCompareItemProviderDecoratorAdapterFactory;
import org.eclipse.emf.compare.uml2.internal.provider.decorator.UMLProfileItemProviderAdapterFactoryDecorator;
import org.eclipse.emf.compare.uml2.internal.provider.profile.ProfiledUMLCompareItemProviderAdapterFactory;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.provider.UML2CompareTestProfileItemProviderAdapterFactory;
import org.eclipse.emf.compare.uml2.rcp.ui.internal.structuremergeviewer.filters.UMLRefinedElementsFilter;
import org.eclipse.emf.compare.uml2.rcp.ui.tests.groups.AbstractDifferenceOrderTest;
import org.eclipse.emf.compare.uml2.rcp.ui.tests.groups.NotifierScopeProvider;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.uml2.uml.edit.providers.UMLItemProviderAdapterFactory;
import org.junit.Test;

/**
 * Tests the display (label and icon) of UML elements on a model with a static profile applied.
 * <p>
 * This test uses the full integration of profiles in EMF Compare. That is to say:
 * <ul>
 * <li>The integration of {@link ProfileIntegrationItemProviderAdapterFactory}</li>
 * <li>The integration of {@link StereotypedElementChangePostProcessor}</li>
 * </ul>
 * </p>
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class StaticProfileIntegrationDisplayTest extends AbstractDifferenceOrderTest {

	@Override
	protected IPostProcessor.Descriptor.Registry<?> getPostProcessorRegistry() {
		PostProcessorDescriptorRegistryImpl<Object> postProcessorRegistry = new PostProcessorDescriptorRegistryImpl<Object>();
		BasicPostProcessorDescriptorImpl umlPostProcessor = new BasicPostProcessorDescriptorImpl(
				new UMLPostProcessor(), Pattern.compile("http://www.eclipse.org/uml2/\\d.0.0/UML"), null); //$NON-NLS-1$
		postProcessorRegistry.put(UMLPostProcessor.class.getName(), umlPostProcessor);
		BasicPostProcessorDescriptorImpl stereotypeElementPostProcessor = new BasicPostProcessorDescriptorImpl(
				new StereotypedElementChangePostProcessor(),
				Pattern.compile("http://www.eclipse.org/uml2/\\d.0.0/UML"), null); //$NON-NLS-1$
		postProcessorRegistry.put(StereotypedElementChangePostProcessor.class.getName(),
				stereotypeElementPostProcessor);
		return postProcessorRegistry;
	}

	@Override
	protected List<AdapterFactory> getAdaptersFactory() {
		// Adds UML2CompareTestProfileItemProviderAdapterFactory item adapter factory to use the custom icon
		// defined in it.
		eventBus = new EventBus();
		return Lists.<AdapterFactory> newArrayList(new ProfiledUMLCompareItemProviderAdapterFactory(),
				new UMLProfileItemProviderAdapterFactoryDecorator(),
				new CompareItemProviderAdapterFactorySpec(),
				new TreeItemProviderAdapterFactorySpec(new StructureMergeViewerFilter(eventBus)),
				new UMLCompareCustomItemProviderAdapterFactory(),
				new UML2CompareTestProfileItemProviderAdapterFactory(), new UMLItemProviderAdapterFactory(),
				new UMLCompareItemProviderDecoratorAdapterFactory(),
				new ReflectiveItemProviderAdapterFactory());
	}

	@Override
	protected NotifierScopeProvider getInput() {
		return new DataInput();
	}

	@Test
	public void testStaticProfileDisplay() throws IOException {
		getFilter().addFilter(new UMLRefinedElementsFilter());
		getFilter().addFilter(new EmptyMatchedResourcesFilter());
		getFilter().addFilter(new IdenticalElementsFilter());
		getFilter().addFilter(new CascadingDifferencesFilter());

		IDifferenceGroup group = new BasicDifferenceGroupImpl(getComparison(), alwaysTrue(),
				getCrossReferenceAdapter());
		List<? extends TreeNode> roots = group.getChildren();
		// Uncomment the following lines to reserialize the expected model
		// TestWriterHelper writerHelper = createTestHelper();
		// writerHelper
		// .createExpectedModel(
		// "{$Git_REPO_PATH}/plugins/org.eclipse.emf.compare.uml2.rcp.ui.tests/src/org/eclipse/emf/compare/uml2/rcp/ui/tests/profile/data/static_/expectedResult.nodes",
		// roots, true);

		compareTree(new ExpectedResult().getExpectedResult(), roots, true);
	}

	/**
	 * Input data for this test.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static class DataInput extends AbstractUMLInputData implements NotifierScopeProvider {

		public Resource getLeft() throws IOException {
			return loadFromClassLoader("data/static_/left.uml");//$NON-NLS-1$
		}

		public Resource getRight() throws IOException {
			return loadFromClassLoader("data/static_/right.uml");//$NON-NLS-1$
		}

		public Notifier getOrigin() throws IOException {
			return null;
		}
	}

	/**
	 * Expected result data for this test.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static class ExpectedResult extends AbstractInputData {

		public Resource getExpectedResult() throws IOException {
			return loadFromClassLoader("data/static_/expectedResult.nodes");//$NON-NLS-1$
		}
	}

}

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
package org.eclipse.emf.compare.uml2.tests.edit.provider;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.tests.postprocess.data.TestPostProcessor;
import org.eclipse.emf.compare.uml2.internal.postprocessor.StereotypedElementChangePostProcessor;
import org.eclipse.emf.compare.uml2.internal.provider.decorator.UMLProfileItemProviderAdapterFactoryDecorator;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.provider.UML2CompareTestProfileItemProviderAdapterFactory;
import org.eclipse.emf.compare.uml2.tests.AbstractStaticProfileTest;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.edit.provider.data.ModelInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the {@link UMLProfileItemProviderAdapterFactoryDecorator} on a model with a static profile.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class StaticStereotypedElementItemProviderTest extends AbstractStaticProfileTest {

	private ModelInputData inputData = new ModelInputData();

	@BeforeClass
	public static void setupClass() {
		fillRegistriesForStatic();
	}

	@AfterClass
	public static void teardownClass() {
		resetRegistriesForStatic();
	}

	@Override
	protected AbstractUMLInputData getInput() {
		return inputData;
	}

	@Override
	protected void registerPostProcessors(
			org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor.Registry<String> postProcessorRegistry) {
		super.registerPostProcessors(postProcessorRegistry);
		postProcessorRegistry.put(StereotypedElementChangePostProcessor.class.getName(),
				new TestPostProcessor.TestPostProcessorDescriptor(
						Pattern.compile("http://www.eclipse.org/uml2/\\d\\.0\\.0/UML"), null, //$NON-NLS-1$
						new StereotypedElementChangePostProcessor(), 25));
	}

	@Before
	@Override
	public void before() {
		super.before();
	}

	@Test
	public void testIconsAndLabelsStaticProfile() throws IOException {
		AdapterFactory stereotypedElementItemProviderDecorator = new ComposedAdapterFactory(
				Lists.<AdapterFactory> newArrayList(new UMLProfileItemProviderAdapterFactoryDecorator(),
						new UML2CompareTestProfileItemProviderAdapterFactory()));

		// Map<Ordered list of applied stereotyped, Expected icon>
		HashMap<String, String> expectedStaticIcons = new HashMap<String, String>();
		expectedStaticIcons.put("ACliche", "eclipse_luna.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		expectedStaticIcons.put("ACliche,ACliche2", "eclipse_luna.gif"); //$NON-NLS-1$//$NON-NLS-2$
		expectedStaticIcons.put("ACliche2,ACliche", "eclipse_kepler.gif"); //$NON-NLS-1$//$NON-NLS-2$
		expectedStaticIcons.put("ACliche2", "eclipse_kepler.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		expectedStaticIcons.put("ACliche3", "ACliche3.gif"); //$NON-NLS-1$ //$NON-NLS-2$

		Resource resource = inputData.getStaticProfileModel();

		StereotypedElementItemProviderTestUtil.checkIconAndLabel(stereotypedElementItemProviderDecorator,
				expectedStaticIcons, resource);

	}

}

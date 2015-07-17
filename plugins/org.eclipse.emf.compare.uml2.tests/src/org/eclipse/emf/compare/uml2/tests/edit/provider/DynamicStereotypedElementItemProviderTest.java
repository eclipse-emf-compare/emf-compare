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

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.uml2.internal.provider.decorator.UMLProfileItemProviderAdapterFactoryDecorator;
import org.eclipse.emf.compare.uml2.tests.AbstractDynamicProfileTest;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.edit.provider.data.ModelInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the {@link UMLProfileItemProviderAdapterFactoryDecorator} on a model with a dynamic profile.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class DynamicStereotypedElementItemProviderTest extends AbstractDynamicProfileTest {

	private ModelInputData inputData = new ModelInputData();

	@BeforeClass
	public static void setupClass() {
		initEPackageNsURIToProfileLocationMap();
	}

	@AfterClass
	public static void teardownClass() {
		resetEPackageNsURIToProfileLocationMap();
	}

	@Override
	protected AbstractUMLInputData getInput() {
		return inputData;
	}

	@Test
	public void testIconsAndLabelsDynamicProfile() throws IOException {
		AdapterFactory stereotypedElementItemProviderDecorator = new UMLProfileItemProviderAdapterFactoryDecorator();

		// Map<Ordered list of applied stereotyped, Expected icon>
		HashMap<String, String> expectedStaticIcons = new HashMap<String, String>();
		expectedStaticIcons.put("ACliche", "eclipse_luna.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		expectedStaticIcons.put("ACliche,ACliche2", "eclipse_luna.gif"); //$NON-NLS-1$//$NON-NLS-2$
		expectedStaticIcons.put("ACliche2,ACliche", "eclipse_luna.gif"); //$NON-NLS-1$//$NON-NLS-2$
		// ACliche2 and ACliche3 do not have any icon defined in the profile so the base element icon should
		// be used instead
		expectedStaticIcons.put("ACliche2", "Class.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		expectedStaticIcons.put("ACliche3", "Class.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		Resource resource = inputData.getDynamicProfileModelmodel();

		StereotypedElementItemProviderTestUtil.checkIconAndLabel(stereotypedElementItemProviderDecorator,
				expectedStaticIcons, resource);

	}

}

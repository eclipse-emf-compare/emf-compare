/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.modelextension;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Iterables;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import org.eclipse.core.internal.registry.ExtensionRegistry;
import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.util.ModelExtensionUtil;
import org.eclipse.emf.compare.diagram.papyrus.tests.modelextension.data.customparametermodel.CustomParameterModel;
import org.eclipse.emf.compare.diagram.papyrus.tests.util.PapyrusSaveParameterUtil;
import org.eclipse.papyrus.infra.core.resource.AbstractBaseModel;
import org.junit.Test;
import org.osgi.framework.Bundle;

/**
 * Tests the {@link ModelExtensionUtil} class.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
@SuppressWarnings("restriction")
public class ModelExtensionUtilTest {

	/**
	 * Tests if the Papyrus Model Extension Point can be found in the registry and has some default elements
	 * registered.
	 */
	@Test
	public void testGetModelExtensionsPapyrus() {
		final IConfigurationElement[] elements = ModelExtensionUtil.getModelExtensions();
		assertNotNull(elements);

		// check if at least the number of default papyrus models are registered
		assertTrue(elements.length >= 3);
	}

	/**
	 * Checks for the default file extensions registered by Papyrus.
	 */
	@Test
	public void testGetRegisteredFileExtensionsPapyrus() {
		final Collection<String> registeredFileExtensions = ModelExtensionUtil.getRegisteredFileExtensions();
		assertNotNull(registeredFileExtensions);

		// check for null entries
		assertFalse(Iterables.contains(registeredFileExtensions, null));

		// check for default file extensions registered in Papyrus
		assertTrue(Iterables.contains(registeredFileExtensions, "di"));
		assertTrue(Iterables.contains(registeredFileExtensions, "notation"));
		assertTrue(Iterables.contains(registeredFileExtensions, "uml"));
	}

	/**
	 * Checks if a custom file extension is correctly handled by the {@link ModelExtensionUtil}.
	 * 
	 * @throws IOException
	 *             If an Exception occurs during reading the custom extension file.
	 */
	@Test
	public void testGetRegisteredFileExtensionsCustom() throws IOException {
		register("fileextension/fileextension.xml");

		final Collection<String> registeredFileExtensions = ModelExtensionUtil.getRegisteredFileExtensions();
		assertNotNull(registeredFileExtensions);

		// check if custom file extension was registered
		assertTrue(Iterables.contains(registeredFileExtensions, "fileextensiontest"));
	}

	/**
	 * Checks if the {@link ModelExtensionUtil} filters {@code null} file extension.
	 * 
	 * @throws IOException
	 *             If an exception occurs during reading the custom extension file.
	 */
	@Test
	public void testNullRegisteredFileExtension() throws IOException {
		final boolean registered = register("nulldata/nullextension.xml");
		assertTrue(registered);

		// check for null entries
		final Collection<String> registeredFileExtensions = ModelExtensionUtil.getRegisteredFileExtensions();
		assertFalse(Iterables.contains(registeredFileExtensions, null));
	}

	/**
	 * Checks if the retrieved save parameters of .di, .notation and .uml files correspond to the actual save
	 * parameters used by Papyrus.
	 */
	@Test
	public void testGetSaveParametersPapyrus() {
		final Map<?, ?> diSaveParameters = ModelExtensionUtil.getSaveParameters("di");
		final Map<?, ?> papyrusDiSaveParameters = PapyrusSaveParameterUtil.getDISaveParameter();
		assertTrue(PapyrusSaveParameterUtil.isEqual(diSaveParameters, papyrusDiSaveParameters));

		final Map<?, ?> umlSaveParameters = ModelExtensionUtil.getSaveParameters("uml");
		final Map<?, ?> papyrusUmlSaveParameters = PapyrusSaveParameterUtil.getUMLSaveParameter();
		assertTrue(PapyrusSaveParameterUtil.isEqual(umlSaveParameters, papyrusUmlSaveParameters));

		final Map<?, ?> notationSaveParameters = ModelExtensionUtil.getSaveParameters("notation");
		final Map<?, ?> papyrusNotationSaveParameters = PapyrusSaveParameterUtil.getNotationSaveParameter();
		assertTrue(PapyrusSaveParameterUtil.isEqual(notationSaveParameters, papyrusNotationSaveParameters));
	}

	/**
	 * Tests if the {@link ModelExtensionUtil} can handle a missing or wrong registered model.
	 * 
	 * @throws IOException
	 *             If an exception occurs during reading the custom extension file.
	 */
	@Test
	public void testGetSaveParametersNullModel() throws IOException {
		final boolean registered = register("nullmodel/nullmodel.xml");
		assertTrue(registered);

		final Map<?, ?> saveParameters = ModelExtensionUtil.getSaveParameters("nullmodeltest");
		assertTrue(saveParameters.isEmpty());
	}

	/**
	 * Tests if the {@link ModelExtensionUtil} can handle models which return {@code null} as save parameter.
	 * 
	 * @throws IOException
	 *             If an exception occurs during reading the custom extension file.
	 */
	@Test
	public void testGetSaveParametersNullParameterModel() throws IOException {
		final boolean registered = register("nullparametermodel/nullparametermodel.xml");
		assertTrue(registered);

		final Map<?, ?> saveParameters = ModelExtensionUtil.getSaveParameters("nullparametermodeltest");
		assertTrue(saveParameters.isEmpty());
	}

	/**
	 * Tests if the {@link ModelExtensionUtil} can handle models which throw a RuntimeException.
	 * 
	 * @throws IOException
	 *             If an exception occurs during reading the custom extension file.
	 */
	@Test
	public void testGetSaveParametersRuntimeExceptionModel() throws IOException {
		final boolean registered = register("runtimeexceptionmodel/runtimeexceptionmodel.xml");
		assertTrue(registered);

		final Map<?, ?> saveParameters = ModelExtensionUtil.getSaveParameters("runtimeexceptionmodeltest");
		final Map<?, ?> defaultPapyrusSaveParameters = PapyrusSaveParameterUtil.getDefaultSaveParameter();
		assertTrue(PapyrusSaveParameterUtil.isEqual(saveParameters, defaultPapyrusSaveParameters));
	}

	/**
	 * Tests if the {@link ModelExtensionUtil} can handle models which are not a subclass of
	 * {@link AbstractBaseModel} and therefore do not offer a method to retrieve save parameters.
	 * 
	 * @throws IOException
	 *             If an exception occurs during reading the custom extension file.
	 */
	@Test
	public void testGetSaveParametersNoParameterModel() throws IOException {
		final boolean registered = register("noparametermodel/noparametermodel.xml");
		assertTrue(registered);

		final Map<?, ?> saveParameters = ModelExtensionUtil.getSaveParameters("noparametermodeltest");
		assertTrue(saveParameters.isEmpty());
	}

	/**
	 * Tests if the {@link ModelExtensionUtil} correctly retrieves custom save parameters declared within a
	 * custom model.
	 * 
	 * @throws IOException
	 *             If an exception occurs during reading the custom extension file.
	 */
	@Test
	public void testGetSaveParametersCustomParameterModel() throws IOException {
		final boolean registered = register("customparametermodel/customparametermodel.xml");
		assertTrue(registered);

		final Map<?, ?> saveParameters = ModelExtensionUtil.getSaveParameters("customparametermodeltest");
		final CustomParameterModel customParameterModel = new CustomParameterModel();
		assertTrue(PapyrusSaveParameterUtil.isEqual(saveParameters,
				customParameterModel.getSaveParametersForTest()));
	}

	/**
	 * Helper method to register an extension.
	 * 
	 * @param extensionPath
	 *            The path to the extension xml file relative to the data directory.
	 * @return {@code true} if the extension was successfully registered, {@code false} otherwise.
	 * @throws IOException
	 *             If an exception occurs during reading the custom extension file.
	 */
	private boolean register(final String extensionPath) throws IOException {
		final InputStream inputStream = getClass().getResource("data/" + extensionPath).openStream();
		final Bundle bundle = Platform.getBundle("org.eclipse.emf.compare.diagram.papyrus.tests");

		final ExtensionRegistry registry = (ExtensionRegistry)Platform.getExtensionRegistry();
		return registry.addContribution(inputStream, ContributorFactoryOSGi.createContributor(bundle), false,
				extensionPath, null, registry.getTemporaryUserToken());
	}

}

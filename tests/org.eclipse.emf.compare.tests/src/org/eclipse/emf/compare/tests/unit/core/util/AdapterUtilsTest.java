/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.unit.core.util;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.match.metamodel.MatchFactory;
import org.eclipse.emf.compare.tests.EMFCompareTestPlugin;
import org.eclipse.emf.compare.util.AdapterUtils;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * Tests {@link AdapterUtils} class to ensure it does allow retrieval of the factories needed.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings({"unchecked", "nls", })
public class AdapterUtilsTest extends TestCase {
	/** Full path to the model containing this test's input. */
	private static final String INPUT_MODEL_PATH = "/data/testInput.ecore";

	/** Expected factories for those tests. */
	private final String[] expectedFactories = {
			"org.eclipse.emf.compare.diff.provider.DiffItemProviderAdapterFactory",
			"org.eclipse.emf.compare.match.metamodel.provider.MatchItemProviderAdapterFactory",
			"org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory", };

	/**
	 * Tests {@link AdapterUtils#findAdapterFactory(EObject)} with all the contents of a loaded model. Since
	 * the model is an ecore, the returned factory is expected to be
	 * &quot;org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory&quot;.
	 * 
	 * @throws IOException
	 *             If the file does not exist.
	 */
	public void testFindAdapterFactoryFromFile() throws IOException {
		final File modelFile = new File(FileLocator.toFileURL(
				EMFCompareTestPlugin.getDefault().getBundle().getEntry(INPUT_MODEL_PATH)).getFile());
		final EObject model = ModelUtils.load(modelFile, new ResourceSetImpl());
		for (final TreeIterator contentIterator = model.eAllContents(); contentIterator.hasNext(); ) {
			final AdapterFactory factory = AdapterUtils.findAdapterFactory((EObject)contentIterator.next());
			assertNotNull("No adapter factory returned for valid EObject.", factory);
			assertEquals("Unexpected adapter factory returned for valid EObject.",
					"org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory", factory.getClass()
							.getName());
		}
	}

	/**
	 * Tests {@link AdapterUtils#findAdapterFactory(String)} with invalid arguments. Expects the returned
	 * factory to be <code>null</code>.
	 */
	public void testFindAdapterFactoryInvalidNsURI() {
		final String[] invalidURIs = {null, "", "-1",
				"org.eclipse.emf.compare.diff.provider.DiffItemProviderAdapterFactory", };
		for (int i = 0; i < invalidURIs.length; i++)
			assertNull("Unexpected adapter factory returned for invalid URI.", AdapterUtils
					.findAdapterFactory(invalidURIs[i]));
	}

	/**
	 * Tests {@link AdapterUtils#findAdapterFactory(EObject)} with <code>null</code> argument. Expects a
	 * NullPointerException to be thrown.
	 */
	public void testFindAdapterFactoryNullEObject() {
		try {
			AdapterUtils.findAdapterFactory((EObject)null);
			fail("findAdapterFactory(null) did not throw the expected NullPointerException.");
		} catch (NullPointerException e) {
			// This was expected
		}
	}

	/**
	 * Tests {@link AdapterUtils#findAdapterFactory(EObject)} with valid EObjects.
	 * <p>
	 * Expected results :<table>
	 * <tr>
	 * <td>URI</td>
	 * <td>factory class name</td>
	 * </tr>
	 * <tr>
	 * <td>{@link DiffModel}</td>
	 * <td>&quot;org.eclipse.emf.compare.diff.provider.DiffItemProviderAdapterFactory&quot;</td>
	 * </tr>
	 * <tr>
	 * <td>{@link MatchModel}</td>
	 * <td>&quot;org.eclipse.emf.compare.match.metamodel.provider.MatchItemProviderAdapterFactory&quot;</td>
	 * </tr>
	 * <tr>
	 * <td>{@link EObject}</td>
	 * <td>&quot;org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory&quot;</td>
	 * </tr>
	 * </table>
	 * </p>
	 */
	public void testFindAdapterFactoryValidEObject() {
		final EObject[] validObjects = {DiffFactory.eINSTANCE.createDiffModel(),
				MatchFactory.eINSTANCE.createMatchModel(), EcoreFactory.eINSTANCE.createEObject(), };
		for (int i = 0; i < validObjects.length; i++) {
			final AdapterFactory factory = AdapterUtils.findAdapterFactory(validObjects[i]);
			assertNotNull("No adapter factory returned for valid EObject.", factory);
			assertEquals("Unexpected adapter factory returned for valid EObject.", expectedFactories[i],
					factory.getClass().getName());
		}
	}

	/**
	 * Tests {@link AdapterUtils#findAdapterFactory(String)}.
	 * <p>
	 * Expected results :<table>
	 * <tr>
	 * <td>URI</td>
	 * <td>factory class name</td>
	 * </tr>
	 * <tr>
	 * <td>&quot;http://www.eclipse.org/emf/compare/diff/1.1&quot;</td>
	 * <td>&quot;org.eclipse.emf.compare.diff.provider.DiffItemProviderAdapterFactory&quot;</td>
	 * </tr>
	 * <tr>
	 * <td>&quot;http://www.eclipse.org/emf/compare/match/1.1&quot;</td>
	 * <td>&quot;org.eclipse.emf.compare.match.metamodel.provider.MatchItemProviderAdapterFactory&quot;</td>
	 * </tr>
	 * <tr>
	 * <td>&quot;http://www.eclipse.org/emf/2002/Ecore&quot;</td>
	 * <td>&quot;org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory&quot;</td>
	 * </tr>
	 * </table>
	 * </p>
	 */
	public void testFindAdapterFactoryValidNsURI() {
		final String[] nsURIs = {"http://www.eclipse.org/emf/compare/diff/1.1",
				"http://www.eclipse.org/emf/compare/match/1.1", "http://www.eclipse.org/emf/2002/Ecore", };
		for (int i = 0; i < nsURIs.length; i++) {
			final AdapterFactory factory = AdapterUtils.findAdapterFactory(nsURIs[i]);
			assertNotNull("No adapter factory returned for valid URI.", factory);
			assertEquals("Unexpected adapter factory returned for valid URI.", expectedFactories[i], factory
					.getClass().getName());
		}
	}
}

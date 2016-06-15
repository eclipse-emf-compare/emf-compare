/*******************************************************************************
 * Copyright (C) 2015 EclipseSource Munich Gmbh and Others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.egit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;

/**
 * Tests the extraction of a UML Package into a new sub-model with parallel changes to extracted UML Package.
 * This scenario contains two diagrams, one for the root of the model and another for the extracted package.
 * <dl>
 * <dt>Origin:</dt>
 * <dd>Given is a UML Class Diagram with two classes in the root model, as well as a package with two further
 * classes. We have two diagrams, one showing the root model, the other the contained package.</dd>
 * <dt>Left:</dt>
 * <dd>The contained package is extracted into a new resource named <em>SomePackage</em> (di, uml, and
 * notation). Note that also the diagram showing the extracted package is moved into the resource
 * <em>SomePackage.notation</em>.</dd>
 * <dt>Right:</dt>
 * <dd>The root model undergoes a series of changes, such as a new class <em>Ax</em> is added to the root
 * model, the type of property <em>RootElement/B/a</em> is changed to the new class <em>Ax</em>.</dd>
 * </dl>
 *
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class ResourceAttachmentChangeAdd1GitMergeTest extends AbstractGitMergeTestCase {

	private static final String TEST_SCENARIO_PATH = "testmodels/resourceattachmentchange/add1/";

	private static final String SOME_PACKAGE_UML = "SomePackage.uml";

	private static final String MODEL_UML = "model.uml";

	@Override
	protected String getTestScenarioPath() {
		return TEST_SCENARIO_PATH;
	}

	@Override
	protected boolean shouldValidate(File file) {
		return file.getName().equals(MODEL_UML) || file.getName().endsWith(SOME_PACKAGE_UML);
	}

	@Override
	protected void validateResult() throws Exception {
		assertTrue(noConflict());
		assertTrue(fileExists("model.di"));
		assertTrue(fileExists("model.notation"));
		assertTrue(fileExists(MODEL_UML));
		assertTrue(fileExists("SomePackage.di"));
		assertTrue(fileExists("SomePackage.notation"));
		assertTrue(fileExists(SOME_PACKAGE_UML));
	}

	@Override
	protected void validateResult(Resource resource) throws Exception {
		final String lastSegment = resource.getURI().lastSegment();
		if (MODEL_UML.equals(lastSegment)) {
			validateModelResource(resource);
		} else if (SOME_PACKAGE_UML.equals(lastSegment)) {
			validateSomePackageResource(resource);
		}
	}

	private void validateModelResource(Resource resource) {
		// assert changes of the right-hand side:
		// Type of RootElement/B/a is Ax,
		// which in turn has been added
		final Model rootElement = (Model)resource.getContents().get(0);
		final Property a = (Property)rootElement.getOwnedMember("B").getOwnedElements().get(0);
		assertNotNull(rootElement.getOwnedMember("Ax"));
		assertEquals(rootElement.getOwnedMember("Ax"), a.getType());
	}

	private void validateSomePackageResource(Resource resource) {
		// assert changes of the left: package was extracted to SomePackage.uml
		assertTrue(resource.getContents().size() == 1);
		assertTrue(resource.getContents().get(0) instanceof org.eclipse.uml2.uml.Package);
		assertEquals("SomePackage", ((org.eclipse.uml2.uml.Package)resource.getContents().get(0)).getName());
	}
}

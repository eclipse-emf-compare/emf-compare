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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;

/**
 * Tests the re-integration of a UML Package from a sub-model with parallel addition of an element to
 * re-integrated UML Package. This scenario contains two diagrams, one for the root of the model and another
 * for the extracted package.
 * <dl>
 * <dt>Origin:</dt>
 * <dd>Given is a UML Class Diagram that refers to an extracted UML Package contained in the resource
 * <em>Package1.uml</em>, which in turn contains one class. We have two diagrams, one is contained in
 * <em>model.notation</em> and shows the root model, the other is contained in <em>Package1.notation</em> and
 * shows the extracted package.</dd>
 * <dt>Left:</dt>
 * <dd>A second class is added to the extracted package in <em>Package1.uml</em> . Also a shape is added to
 * the diagram in <em>Package1.notation</em>.</dd>
 * <dt>Right:</dt>
 * <dd>The extracted package <em>Package1</em> (di, notation, and uml) is re-integrated into <em>model</em>
 * (di, notation, and uml).</dd>
 * </dl>
 *
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class ResourceAttachmentChangeDelete1GitMergeTest extends AbstractGitMergeTestCase {

	private static final String TEST_SCENARIO_PATH = "testmodels/resourceattachmentchange/delete1/";

	private static final String MODEL_UML = "model.uml";

	private static final String MODEL_NOTATION = "model.notation";

	@Override
	protected String getTestScenarioPath() {
		return TEST_SCENARIO_PATH;
	}

	@Override
	protected boolean shouldValidate(File file) {
		return file.getName().equals(MODEL_UML) || file.getName().equals(MODEL_NOTATION);
	}

	@Override
	protected void validateResult() throws Exception {
		assertTrue(noConflict());
		assertTrue(fileExists("model.di"));
		assertTrue(fileExists(MODEL_NOTATION));
		assertTrue(fileExists(MODEL_UML));
		assertFalse(fileExists("Package1.di"));
		assertFalse(fileExists("Package1.notation"));
		assertFalse(fileExists("Package1.uml"));
	}

	@Override
	protected void validateResult(Resource resource) throws Exception {
		final String lastSegment = resource.getURI().lastSegment();
		if (MODEL_UML.equals(lastSegment)) {
			validateModelResource(resource);
		} else if (MODEL_NOTATION.equals(lastSegment)) {
			validateModelNotation(resource);
		}
	}

	private void validateModelResource(Resource resource) {
		// assert changes of the left-hand side: addition of Class2
		final Model rootElement = (Model)resource.getContents().get(0);
		assertEquals("RootElement", rootElement.getName());
		final Package package1 = (Package)rootElement.getOwnedElements().get(0);
		assertTrue(package1.getOwnedElements().size() == 2);
		assertTrue(package1.getOwnedElements().get(0) instanceof Class);
		assertTrue(package1.getOwnedElements().get(1) instanceof Class);
		final Class class1 = (Class)package1.getOwnedElements().get(0);
		final Class class2 = (Class)package1.getOwnedElements().get(1);
		assertEquals("Class1", class1.getName());
		assertEquals("Class2", class2.getName());
	}

	private void validateModelNotation(Resource resource) {
		// assert changes of the right-hand side: integration of second diagram
		assertEquals(2, resource.getContents().size());
		assertEquals("Class Diagram", ((Diagram)resource.getContents().get(0)).getName());
		assertEquals("ClassDiagram2", ((Diagram)resource.getContents().get(1)).getName());
	}
}

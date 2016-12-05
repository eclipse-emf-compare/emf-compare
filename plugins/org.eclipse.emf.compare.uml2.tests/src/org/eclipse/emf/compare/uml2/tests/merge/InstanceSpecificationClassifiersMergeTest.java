/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.merge;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLTest;
import org.eclipse.emf.compare.uml2.tests.merge.data.InstanceSpecificationClassifiersMergeInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests merging changes of the feature {@link InstanceSpecification#getClassifiers()}.
 * <p>
 * Merging {@link InstanceSpecification#getClassifiers()} requires special handling, if the instance
 * specification is an {@link EnumerationLiteral}, because {@link EnumerationLiteral} overwrites
 * {@link InstanceSpecification#getClassifiers()}, so that it returns an unmodifiable UnionEObjectEList, which
 * always contains the Enumeration as item. Thus, we have to handle changes of
 * {@link EnumerationLiteral#getClassifiers()} as if this feature would be derived.
 * </p>
 * <p>
 * See also <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=508665">bug 508665</a>.
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class InstanceSpecificationClassifiersMergeTest extends AbstractUMLTest {

	private InstanceSpecificationClassifiersMergeInputData input = new InstanceSpecificationClassifiersMergeInputData();

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

	@BeforeClass
	public static void setupClass() {
		fillRegistries();
	}

	@AfterClass
	public static void teardownClass() {
		resetRegistries();
	}

	/**
	 * Tests merging a move an enumeration literal from one enumeration to another.
	 * <p>
	 * The model contains two enumerations, <i>Enumeration1</i> and <i>Enumeration2</i>. On the left-hand
	 * side, <i>Enumeration1</i> contains a literal. On the right-hand side, this literal is contained by
	 * <i>Enumeration2</i> instead. This test merges in both directions.
	 * </p>
	 * 
	 * @throws IOException
	 *             Thrown if example model could not be read.
	 */
	@Test
	public void testMergeOfMovedEnumerationLiteral() throws IOException {
		final Resource left = input.getInstanceSpec1Left();
		final Resource right = input.getInstanceSpec1Right();
		testMergeBothDirections(left, right);
	}

	/**
	 * Tests merging a the addition / deletion of an enumeration literal.
	 * <p>
	 * The model contains two enumerations, <i>Enumeration1</i> and <i>Enumeration2</i>. On the left-hand
	 * side, <i>Enumeration1</i> contains an enumeration literal. On the right-hand side, it doesn't. This
	 * test merges in both directions to test the addition and the deletion in the merger.
	 * </p>
	 * 
	 * @throws IOException
	 *             Thrown if example model could not be read.
	 */
	@Test
	public void testMergeOfAddedOrDeletedEnumerationLiteral() throws IOException {
		final Resource left = input.getInstanceSpec2Left();
		final Resource right = input.getInstanceSpec2Right();
		testMergeBothDirections(left, right);
	}

	/**
	 * Tests merging a the addition / deletion of an instance specification's classifier.
	 * <p>
	 * The intention of this test is to make sure that we don't break the correct merging of normal instance
	 * specification classifiers, while still support correctly merging
	 * {@link EnumerationLiteral#getClassifier()}.
	 * </p>
	 * <p>
	 * The model contains two enumerations, <i>Enumeration1</i> and <i>Enumeration2</i>, and an instance
	 * specification. On the left-hand side, <i>Enumeration1</i> is a classifier of the instance
	 * specification. On the right-hand side, it doesn't. This test merges in both directions to test the
	 * addition and the deletion in the merger.
	 * </p>
	 * 
	 * @throws IOException
	 *             Thrown if example model could not be read.
	 */
	@Test
	public void testMergeOfAddedOrDeletedInstanceSpecificationClassifier() throws IOException {
		final Resource left = input.getInstanceSpec3Left();
		final Resource right = input.getInstanceSpec3Right();
		testMergeBothDirections(left, right);
	}

	/**
	 * Tests merging a the change of an instance specification's classifier.
	 * <p>
	 * The intention of this test is to make sure that we don't break the correct merging of normal instance
	 * specification classifiers, while still support correctly merging
	 * {@link EnumerationLiteral#getClassifier()}.
	 * </p>
	 * <p>
	 * The model contains two enumerations, <i>Enumeration1</i> and <i>Enumeration2</i>, and an instance
	 * specification. On the left-hand side, <i>Enumeration1</i> is a classifier of the instance
	 * specification. On the right-hand side, the instance specification's classifier is <i>Enumeration2</i>.
	 * This test merges in both directions to test the addition and the deletion in the merger.
	 * </p>
	 * 
	 * @throws IOException
	 *             Thrown if example model could not be read.
	 */
	@Test
	public void testMergeOfChangedInstanceSpecificationClassifier() throws IOException {
		final Resource left = input.getInstanceSpec4Left();
		final Resource right = input.getInstanceSpec4Right();
		testMergeBothDirections(left, right);
	}

	private void testMergeBothDirections(Resource left, Resource right) throws IOException {
		testMergeLeftToRight(left, right, null);
		reload(left, right);
		testMergeRightToLeft(left, right, null);
		reload(left, right);
		testMergeLeftToRight(right, left, null);
		reload(left, right);
		testMergeRightToLeft(right, left, null);
	}

	private void reload(Resource... resources) throws IOException {
		for (Resource resource : resources) {
			resource.unload();
			resource.load(Collections.EMPTY_MAP);
		}
	}
}

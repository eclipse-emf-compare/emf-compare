/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.postprocess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.postprocessor.PostProcessorDescriptorRegistryImpl;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;
import org.eclipse.emf.compare.tests.postprocess.data.PostProcessInputData;
import org.eclipse.emf.compare.tests.postprocess.data.TestPostProcessor;
import org.eclipse.emf.compare.tests.postprocess.data.TestPostProcessor.TestPostProcessorDescriptor;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("nls")
public class PostProcessorTest {

	private PostProcessInputData input = new PostProcessInputData();

	@BeforeClass
	public static void beforeClass() {
		EPackage.Registry.INSTANCE.put(NodesPackage.eNS_URI, NodesPackage.eINSTANCE);
	}

	@Test
	public void testNominalUseCase() throws IOException {
		// No post processes.
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		assertEquals(1, comparison.getMatches().size());
	}

	@Test
	public void testA1UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// Post processing (add a match element) if EMF Compare scans a model coming from the namespace URI
		// "http://www.eclipse.org/emf/compare/tests/nodes" at least.
		final Comparison comparison = compareWithPostProcessing(left, right, Pattern
				.compile("http://www.eclipse.org/emf/compare/tests/nodes"), null);

		assertEquals(2, comparison.getMatches().size());

	}

	@Test
	public void testA2UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// Post processing (add a match element) if EMF Compare scans a model from the names
		// matches the regex ".*/nodes" at least.
		final Comparison comparison = compareWithPostProcessing(left, right, Pattern.compile(".*/nodes"),
				null);

		assertEquals(2, comparison.getMatches().size());

	}

	@Test
	public void testA3UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// No post processes if the regex matches no scanned namespace URIs.
		final Comparison comparison = compareWithPostProcessing(left, right, Pattern.compile(".*/nides"),
				null);

		assertEquals(1, comparison.getMatches().size());

	}

	@Test
	public void testA4UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// No post processes if the regex matches no scanned namespace URIs (null value case)
		final Comparison comparison = compareWithPostProcessing(left, right, null, null);

		assertEquals(1, comparison.getMatches().size());

	}

	@Test
	public void testA5UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// No post processes if the regex matches no scanned namespace URIs (empty value cas)
		final Comparison comparison = compareWithPostProcessing(left, right, Pattern.compile(""), null);

		assertEquals(1, comparison.getMatches().size());

	}

	@Test
	public void testA6UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// No post processes if the regex matches no scanned namespace URIs (blank value cas)
		final Comparison comparison = compareWithPostProcessing(left, right, Pattern.compile(" "), null);

		assertEquals(1, comparison.getMatches().size());

	}

	@Test
	public void testB1UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// Post processing (add a match element) if EMF Compare scans a resource where its U
		// the specified one at least.
		final Comparison comparison = compareWithPostProcessing(left, right, null, Pattern.compile(left
				.getURI().toString()));

		assertEquals(2, comparison.getMatches().size());

	}

	@Test
	public void testB2UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// Post processing (add a match element) if EMF Compare scans a resource where its U
		// specified regex at least.
		final Comparison comparison = compareWithPostProcessing(left, right, null, Pattern
				.compile(".*.nodes"));

		assertEquals(2, comparison.getMatches().size());

	}

	@Test
	public void testB3UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// No post processes if the regex matches no scanned resource URIs
		final Comparison comparison = compareWithPostProcessing(left, right, null, Pattern
				.compile(".*.nides"));

		assertEquals(1, comparison.getMatches().size());

	}

	@Test
	public void testB4UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// No post processes if the regex matches no scanned resource URIs (empty value)
		final Comparison comparison = compareWithPostProcessing(left, right, null, Pattern.compile(""));

		assertEquals(1, comparison.getMatches().size());

	}

	@Test
	public void testB5UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// No post processes if the regex matches no scanned resource URIs (blank value)
		final Comparison comparison = compareWithPostProcessing(left, right, null, Pattern.compile(" "));

		assertEquals(1, comparison.getMatches().size());

	}

	@Test
	public void testOrderingPostProcessors() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();
		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);

		PostProcessorDescriptorRegistryImpl<String> registry = new PostProcessorDescriptorRegistryImpl<String>();
		registry.put(TestPostProcessor1.class.getName(), new TestPostProcessorDescriptor(Pattern.compile(""),
				null, new TestPostProcessor1(), 1));
		registry.put(TestPostProcessor2.class.getName(), new TestPostProcessorDescriptor(Pattern
				.compile("http://www.eclipse.org/emf/compare/tests/nodes"), null, new TestPostProcessor2(),
				10));
		registry.put(TestPostProcessor3.class.getName(),
				new TestPostProcessorDescriptor(Pattern
						.compile("http://www.eclipse.org/emf/compare/tests/nodes"), null,
						new TestPostProcessor3(), 9));
		registry.put(TestPostProcessor4.class.getName(), new TestPostProcessorDescriptor(Pattern
				.compile("http://www.eclipse.org/emf/compare/tests/nodes"), null, new TestPostProcessor4(),
				11));

		// Populate nsURIs of the scope.
		EMFCompare.builder().setPostProcessorRegistry(registry).build().compare(scope);

		List<IPostProcessor> postProcessors = registry.getPostProcessors(scope);

		assertEquals(3, postProcessors.size());
		assertTrue(postProcessors.get(0) instanceof TestPostProcessor3);
		assertTrue(postProcessors.get(1) instanceof TestPostProcessor2);
		assertTrue(postProcessors.get(2) instanceof TestPostProcessor4);
	}

	private Comparison compareWithPostProcessing(Resource left, Resource right, Pattern nsURI,
			Pattern resourceURI) {
		PostProcessorDescriptorRegistryImpl<String> registry = new PostProcessorDescriptorRegistryImpl<String>();
		registry.put(TestPostProcessor.class.getName(), new TestPostProcessorDescriptor(nsURI, resourceURI,
				new TestPostProcessor(), 10));

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		return EMFCompare.builder().setPostProcessorRegistry(registry).build().compare(scope);
	}

	/**
	 * Empty Implementation, used to test ordering
	 */
	private class TestPostProcessor1 extends TestPostProcessor {

	}

	/**
	 * Empty Implementation, used to test ordering
	 */
	private class TestPostProcessor2 extends TestPostProcessor {

	}

	/**
	 * Empty Implementation, used to test ordering
	 */
	private class TestPostProcessor3 extends TestPostProcessor {

	}

	/**
	 * Empty Implementation, used to test ordering
	 */
	private class TestPostProcessor4 extends TestPostProcessor {

	}
}

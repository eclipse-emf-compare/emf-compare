/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.postprocess;

import static junit.framework.Assert.assertSame;

import java.io.IOException;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.extension.PostProcessorDescriptor;
import org.eclipse.emf.compare.extension.PostProcessorRegistry;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;
import org.eclipse.emf.compare.tests.postprocess.data.PostProcessInputData;
import org.eclipse.emf.compare.tests.postprocess.data.TestPostProcessor;
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

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getMatches().size()));
	}

	@Test
	public void testA1UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		PostProcessorRegistry registry = new PostProcessorRegistry();
		// Post processing (add a match element) if EMF Compare scans a model coming from the namespace URI
		// "http://www.eclipse.org/emf/compare/tests/nodes" at least.
		registry.addPostProcessor(new PostProcessorDescriptor(
				"http://www.eclipse.org/emf/compare/tests/nodes", null,
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(registry).build()
				.compare(scope);

		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testA2UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		PostProcessorRegistry registry = new PostProcessorRegistry();
		// Post processing (add a match element) if EMF Compare scans a model from the namespace URI which
		// matches the regex ".*/nodes" at least.
		registry.addPostProcessor(new PostProcessorDescriptor(".*/nodes", null,
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(registry).build()
				.compare(scope);

		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testA3UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		PostProcessorRegistry registry = new PostProcessorRegistry();
		// No post processes if the regex matches no scanned namespace URIs.
		registry.addPostProcessor(new PostProcessorDescriptor(".*/nides", null,
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(registry).build()
				.compare(scope);

		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testA4UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		PostProcessorRegistry registry = new PostProcessorRegistry();
		// No post processes if the regex matches no scanned namespace URIs (null value case)
		registry.addPostProcessor(new PostProcessorDescriptor(null, null,
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(registry).build()
				.compare(scope);

		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testA5UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		PostProcessorRegistry registry = new PostProcessorRegistry();
		// No post processes if the regex matches no scanned namespace URIs (empty value case)
		registry.addPostProcessor(new PostProcessorDescriptor("", null,
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(registry).build()
				.compare(scope);

		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testA6UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		PostProcessorRegistry registry = new PostProcessorRegistry();
		// No post processes if the regex matches no scanned namespace URIs (blank value case)
		registry.addPostProcessor(new PostProcessorDescriptor(" ", null,
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(registry).build()
				.compare(scope);

		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testB1UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		PostProcessorRegistry registry = new PostProcessorRegistry();
		// Post processing (add a match element) if EMF Compare scans a resource where its URI is the same as
		// the specified one at least.
		registry.addPostProcessor(new PostProcessorDescriptor(null, left.getURI().toString(),
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(registry).build()
				.compare(scope);

		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testB2UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		PostProcessorRegistry registry = new PostProcessorRegistry();
		// Post processing (add a match element) if EMF Compare scans a resource where its URI matches the
		// specified regex at least.
		registry.addPostProcessor(new PostProcessorDescriptor(null, ".*.nodes",
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(registry).build()
				.compare(scope);

		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testB3UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		PostProcessorRegistry registry = new PostProcessorRegistry();
		// No post processes if the regex matches no scanned resource URIs
		registry.addPostProcessor(new PostProcessorDescriptor(null, ".*.nides",
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(registry).build()
				.compare(scope);

		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testB4UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		PostProcessorRegistry registry = new PostProcessorRegistry();
		// No post processes if the regex matches no scanned resource URIs (empty value)
		registry.addPostProcessor(new PostProcessorDescriptor(null, "",
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(registry).build()
				.compare(scope);

		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testB5UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		PostProcessorRegistry registry = new PostProcessorRegistry();
		// No post processes if the regex matches no scanned resource URIs (blank value)
		registry.addPostProcessor(new PostProcessorDescriptor(null, " ",
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.builder().setPostProcessorRegistry(registry).build()
				.compare(scope);

		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getMatches().size()));

	}

}
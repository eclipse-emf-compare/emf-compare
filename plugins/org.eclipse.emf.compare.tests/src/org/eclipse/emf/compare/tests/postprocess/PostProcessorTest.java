package org.eclipse.emf.compare.tests.postprocess;

import static junit.framework.Assert.assertSame;

import java.io.IOException;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.logical.extension.EMFCompareExtensionRegistry;
import org.eclipse.emf.compare.logical.extension.PostProcessorDescriptor;
import org.eclipse.emf.compare.tests.postprocess.data.PostProcessInputData;
import org.eclipse.emf.compare.tests.postprocess.data.TestPostProcessor;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.After;
import org.junit.Test;

public class PostProcessorTest {

	private PostProcessInputData input = new PostProcessInputData();

	@After
	public void after() {
		EMFCompareExtensionRegistry.clearRegistry();
	}

	@Test
	public void testNominalUseCase() throws IOException {
		// No post processes.
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		final Comparison comparison = EMFCompare.compare(left, right);

		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getMatches().size()));
	}

	@Test
	public void testA1UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// Post processing (add a match element) if EMF Compare scans a model coming from the namespace URI
		// "http://www.eclipse.org/emf/compare/tests/nodes" at least.
		EMFCompareExtensionRegistry.addPostProcessor(new PostProcessorDescriptor(
				"http://www.eclipse.org/emf/compare/tests/nodes", null,
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final Comparison comparison = EMFCompare.compare(left, right);

		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testA2UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// Post processing (add a match element) if EMF Compare scans a model from the namespace URI which
		// matches the regex ".*/nodes" at least.
		EMFCompareExtensionRegistry.addPostProcessor(new PostProcessorDescriptor(".*/nodes", null,
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final Comparison comparison = EMFCompare.compare(left, right);

		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testA3UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// No post processes if the regex matches no scanned namespace URIs.
		EMFCompareExtensionRegistry.addPostProcessor(new PostProcessorDescriptor(".*/nides", null,
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final Comparison comparison = EMFCompare.compare(left, right);

		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testA4UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// No post processes if the regex matches no scanned namespace URIs (null value case)
		EMFCompareExtensionRegistry.addPostProcessor(new PostProcessorDescriptor(null, null,
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final Comparison comparison = EMFCompare.compare(left, right);

		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testA5UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// No post processes if the regex matches no scanned namespace URIs (empty value case)
		EMFCompareExtensionRegistry.addPostProcessor(new PostProcessorDescriptor("", null,
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final Comparison comparison = EMFCompare.compare(left, right);

		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testA6UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// No post processes if the regex matches no scanned namespace URIs (blank value case)
		EMFCompareExtensionRegistry.addPostProcessor(new PostProcessorDescriptor(" ", null,
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final Comparison comparison = EMFCompare.compare(left, right);

		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testB1UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// Post processing (add a match element) if EMF Compare scans a resource where its URI is the same as
		// the specified one at least.
		EMFCompareExtensionRegistry.addPostProcessor(new PostProcessorDescriptor(null, left.getURI()
				.toString(), "org.eclipse.emf.compare.logical.extension.TestPostProcess",
				new TestPostProcessor()));

		final Comparison comparison = EMFCompare.compare(left, right);

		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testB2UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// Post processing (add a match element) if EMF Compare scans a resource where its URI matches the
		// specified regex at least.
		EMFCompareExtensionRegistry.addPostProcessor(new PostProcessorDescriptor(null, ".*.nodes",
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final Comparison comparison = EMFCompare.compare(left, right);

		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testB3UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// No post processes if the regex matches no scanned resource URIs
		EMFCompareExtensionRegistry.addPostProcessor(new PostProcessorDescriptor(null, ".*.nides",
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final Comparison comparison = EMFCompare.compare(left, right);

		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testB4UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// No post processes if the regex matches no scanned resource URIs (empty value)
		EMFCompareExtensionRegistry.addPostProcessor(new PostProcessorDescriptor(null, "",
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final Comparison comparison = EMFCompare.compare(left, right);

		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getMatches().size()));

	}

	@Test
	public void testB5UseCase() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		// No post processes if the regex matches no scanned resource URIs (blank value)
		EMFCompareExtensionRegistry.addPostProcessor(new PostProcessorDescriptor(null, " ",
				"org.eclipse.emf.compare.logical.extension.TestPostProcess", new TestPostProcessor()));

		final Comparison comparison = EMFCompare.compare(left, right);

		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getMatches().size()));

	}

}

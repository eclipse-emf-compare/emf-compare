package org.eclipse.emf.compare.diagram.ui.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diagram.ui.service.GMFCompareService;
import org.eclipse.emf.compare.diagram.ui.service.GMFCompareService.ComparedResourceSets;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class TestAPI {

	static final String DIAGRAM_KIND_PATH = "/diagrams/decorators"; //$NON-NLS-1$

	static final String MODIFIED_MODEL_UML = "/modified/TC.ecorediag"; //$NON-NLS-1$

	static final String ORIGINAL_MODEL_UML = "/original/TC.ecorediag"; //$NON-NLS-1$

	static final String EXPECTED_MODIFIED_MODEL_UML = "/expected" + MODIFIED_MODEL_UML; //$NON-NLS-1$

	static final String EXPECTED_ORIGINAL_MODEL_UML = "/expected" + ORIGINAL_MODEL_UML; //$NON-NLS-1$

	@Test
	public void testLayerDecorators1() throws IOException, InterruptedException {

		// Get the input diagram models to compare.

		final ResourceSet originalResourceSet = new ResourceSetImpl();
		final Resource original = originalResourceSet.createResource(URI.createURI(DIAGRAM_KIND_PATH
				+ ORIGINAL_MODEL_UML));
		original.load(getClass().getResourceAsStream(DIAGRAM_KIND_PATH + ORIGINAL_MODEL_UML),
				Collections.emptyMap());

		final ResourceSet modifiedResourceSet = new ResourceSetImpl();
		final Resource modified = modifiedResourceSet.createResource(URI.createURI(DIAGRAM_KIND_PATH
				+ MODIFIED_MODEL_UML));
		modified.load(getClass().getResourceAsStream(DIAGRAM_KIND_PATH + MODIFIED_MODEL_UML),
				Collections.emptyMap());

		// Launch match processing.

		final MatchResourceSet matchModel = MatchService.doResourceSetMatch(modifiedResourceSet,
				originalResourceSet, Collections.<String, Object> emptyMap());

		// Launch diff processing.

		final DiffResourceSet diffModel = DiffService.doDiff(matchModel, false);

		// Launch api to test
		ComparedResourceSets result = GMFCompareService.layerDifferences(diffModel);

		// Load expected models

		final ResourceSet expectedOriginalResourceSet = new ResourceSetImpl();
		final Resource expectedOriginal = expectedOriginalResourceSet.createResource(URI
				.createURI(DIAGRAM_KIND_PATH + EXPECTED_ORIGINAL_MODEL_UML));
		expectedOriginal.load(
				getClass().getResourceAsStream(DIAGRAM_KIND_PATH + EXPECTED_ORIGINAL_MODEL_UML),
				Collections.emptyMap());

		final ResourceSet expectedModifiedResourceSet = new ResourceSetImpl();
		final Resource expectedModified = expectedModifiedResourceSet.createResource(URI
				.createURI(DIAGRAM_KIND_PATH + EXPECTED_MODIFIED_MODEL_UML));
		expectedModified.load(
				getClass().getResourceAsStream(DIAGRAM_KIND_PATH + EXPECTED_MODIFIED_MODEL_UML),
				Collections.emptyMap());

		String sResultOriginal = ModelUtils.serialize(result.getRight().get(0).getContents().get(0));
		String sResultModified = ModelUtils.serialize(result.getLeft().get(0).getContents().get(0));

		String sExpectedOriginal = ModelUtils.serialize(expectedOriginal.getContents().get(0));
		String sExpectedModified = ModelUtils.serialize(expectedModified.getContents().get(0));

		// test the generated diagrams to check they own decorators.

		assertEquals(sExpectedOriginal, sResultOriginal);
		assertEquals(sExpectedModified, sResultModified);

	}

	@Test
	public void testLayerDecorators2() throws IOException, InterruptedException {

		// Get the input diagram models to compare.

		final ResourceSet originalResourceSet = new ResourceSetImpl();
		final Resource original = originalResourceSet.createResource(URI.createURI(DIAGRAM_KIND_PATH
				+ ORIGINAL_MODEL_UML));
		original.load(getClass().getResourceAsStream(DIAGRAM_KIND_PATH + ORIGINAL_MODEL_UML),
				Collections.emptyMap());

		final ResourceSet modifiedResourceSet = new ResourceSetImpl();
		final Resource modified = modifiedResourceSet.createResource(URI.createURI(DIAGRAM_KIND_PATH
				+ MODIFIED_MODEL_UML));
		modified.load(getClass().getResourceAsStream(DIAGRAM_KIND_PATH + MODIFIED_MODEL_UML),
				Collections.emptyMap());

		ComparedResourceSets input = new ComparedResourceSets();
		input.setLeft(modifiedResourceSet.getResources());
		input.setRight(originalResourceSet.getResources());

		// Launch api to test
		ComparedResourceSets result = GMFCompareService.layerDifferences(input, Collections.EMPTY_MAP);

		// Load expected models

		final ResourceSet expectedOriginalResourceSet = new ResourceSetImpl();
		final Resource expectedOriginal = expectedOriginalResourceSet.createResource(URI
				.createURI(DIAGRAM_KIND_PATH + EXPECTED_ORIGINAL_MODEL_UML));
		expectedOriginal.load(
				getClass().getResourceAsStream(DIAGRAM_KIND_PATH + EXPECTED_ORIGINAL_MODEL_UML),
				Collections.emptyMap());

		final ResourceSet expectedModifiedResourceSet = new ResourceSetImpl();
		final Resource expectedModified = expectedModifiedResourceSet.createResource(URI
				.createURI(DIAGRAM_KIND_PATH + EXPECTED_MODIFIED_MODEL_UML));
		expectedModified.load(
				getClass().getResourceAsStream(DIAGRAM_KIND_PATH + EXPECTED_MODIFIED_MODEL_UML),
				Collections.emptyMap());

		String sResultOriginal = ModelUtils.serialize(result.getRight().get(0).getContents().get(0));
		String sResultModified = ModelUtils.serialize(result.getLeft().get(0).getContents().get(0));

		String sExpectedOriginal = ModelUtils.serialize(expectedOriginal.getContents().get(0));
		String sExpectedModified = ModelUtils.serialize(expectedModified.getContents().get(0));

		// test the generated diagrams to check they own decorators.

		assertEquals(sExpectedOriginal, sResultOriginal);
		assertEquals(sExpectedModified, sResultModified);

	}

}

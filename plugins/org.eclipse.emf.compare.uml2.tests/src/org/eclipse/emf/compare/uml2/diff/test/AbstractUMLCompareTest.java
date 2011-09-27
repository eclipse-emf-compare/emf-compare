/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.diff.test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.merge.service.MergeService;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.service.DiffEngineRegistry;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
import org.eclipse.emf.compare.match.service.MatchEngineRegistry;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.match.UML2MatchEngine;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Assert;
import org.junit.Before;

public abstract class AbstractUMLCompareTest {

	static final String MODIFIED_MODEL_UML = "/modified/model.uml";

	static final String ORIGINAL_MODEL_UML = "/original/model.uml";

	static final String EXPECTED_EMFDIFF = "result.emfdiff";

	static final String EXPECTED_SUFFIX = "/expected/result.emfdiff";

	@Before
	public void before() {
		DiffEngineRegistry.INSTANCE.putValue("uml", new UML2DiffEngine());
		MatchEngineRegistry.INSTANCE.putValue("uml", new UML2MatchEngine());
	}

	abstract String getDiagramKindPath();

	protected final void testCompare(String testFolderPath) throws IOException, InterruptedException {
		DiffModel expected_diff = firstNonEmptyDiffModel(getExpectedDiff(testFolderPath));
		DiffModel computed_diff = firstNonEmptyDiffModel(getComputedDiff(testFolderPath));

		Assert.assertEquals(ModelUtils.serialize(expected_diff), ModelUtils.serialize(computed_diff));
	}

	protected final void testCompare3Way(String testFolderPath) throws IOException, InterruptedException {
		DiffModel expected_diff = firstNonEmptyDiffModel(getComputed3WayDiff(testFolderPath));
		DiffModel computed_diff = firstNonEmptyDiffModel(getComputed3WayDiff(testFolderPath));

		Assert.assertEquals(ModelUtils.serialize(expected_diff), ModelUtils.serialize(computed_diff));
	}

	private DiffModel firstNonEmptyDiffModel(DiffResourceSet diffResourceSet) {
		for (DiffModel diffModel : diffResourceSet.getDiffModels()) {
			if (diffModel.getOwnedElements().size() > 1
					|| (diffModel.getOwnedElements().get(0) instanceof DiffGroup && !diffModel
							.getOwnedElements().get(0).getSubDiffElements().isEmpty())) {
				return diffModel;
			}
		}
		return null;
	}

	protected final void testMerge(String testFolderPath) throws IOException, InterruptedException {
		testMerge(testFolderPath, true);
		testMerge(testFolderPath, false);
	}

	protected final void testMerge(String testFolderPath, Class<? extends DiffElement> diffKind)
			throws IOException, InterruptedException {
		testMerge(testFolderPath, true, diffKind);
		testMerge(testFolderPath, false, diffKind);
	}

	private void testMerge(String testFolderPath, boolean leftToRight) throws IOException,
			InterruptedException {
		DiffResourceSet computed_diff = getComputedDiff(testFolderPath);
		merge(leftToRight, computed_diff);
		testMerge(testFolderPath, leftToRight, computed_diff);
	}

	private void testMerge(String testFolderPath, boolean leftToRight, Class<? extends DiffElement> diffKind)
			throws IOException, InterruptedException {
		DiffResourceSet computed_diff = getComputedDiff(testFolderPath);
		Iterator<EObject> diffs = computed_diff.eAllContents();
		while (diffs.hasNext()) {
			EObject next = diffs.next();
			if (diffKind.isAssignableFrom(next.getClass())) {
				merge(leftToRight, (DiffElement)next);
				break;
			}
		}
		testMerge(testFolderPath, leftToRight, computed_diff);
	}

	private void testMerge(String testFolderPath, boolean leftToRight, DiffResourceSet computed_diff)
			throws IOException {
		Resource referenceResource;
		Resource mergedResource;
		if (leftToRight) {
			ResourceSet referenceResourceSet = getModelResourceSet(testFolderPath, MODIFIED_MODEL_UML);
			referenceResource = referenceResourceSet.getResource(URI.createURI(".." + MODIFIED_MODEL_UML),
					true);

			ResourceSet mergedResourceSet = getRightResourceSet(computed_diff);
			mergedResource = mergedResourceSet.getResource(URI.createURI(".." + ORIGINAL_MODEL_UML), true);
		} else {
			ResourceSet referenceResourceSet = getModelResourceSet(testFolderPath, ORIGINAL_MODEL_UML);
			referenceResource = referenceResourceSet.getResource(URI.createURI(".." + ORIGINAL_MODEL_UML),
					true);

			ResourceSet mergedResourceSet = getLeftResourceSet(computed_diff);
			mergedResource = mergedResourceSet.getResource(URI.createURI(".." + MODIFIED_MODEL_UML), true);
		}

		Assert.assertEquals(referenceResource.getContents().size(), mergedResource.getContents().size());

		Assert.assertEquals(ModelUtils.serialize(referenceResource.getContents().get(0)),
				ModelUtils.serialize(mergedResource.getContents().get(0)));
	}

	private void merge(boolean leftToRight, DiffResourceSet computed_diff) {
		for (DiffModel diffModel : computed_diff.getDiffModels()) {
			MergeService.merge(diffModel.getOwnedElements(), leftToRight);
		}
	}

	private void merge(boolean leftToRight, DiffElement computed_diff) {
		MergeService.merge(computed_diff, leftToRight);
	}

	private ResourceSet getLeftResourceSet(DiffResourceSet diffResourceSet) {
		return getResourceSet(diffResourceSet, DiffPackage.Literals.DIFF_MODEL__LEFT_ROOTS);
	}

	private ResourceSet getRightResourceSet(DiffResourceSet diffResourceSet) {
		return getResourceSet(diffResourceSet, DiffPackage.Literals.DIFF_MODEL__RIGHT_ROOTS);
	}

	@SuppressWarnings("unchecked")
	private ResourceSet getResourceSet(DiffResourceSet diffResourceSet, EReference sideRoots) {
		for (DiffModel diffModel : diffResourceSet.getDiffModels()) {
			for (EObject element : (List<EObject>)diffModel.eGet(sideRoots)) {
				if (element.eResource() != null && element.eResource().getResourceSet() != null) {
					return element.eResource().getResourceSet();
				}
			}
		}
		return null;
	}

	private DiffResourceSet getComputedDiff(String testFolderPath) throws IOException, InterruptedException {
		ResourceSet originalResourceSet = getModelResourceSet(testFolderPath, ORIGINAL_MODEL_UML);
		ResourceSet modifiedResourceSet = getModelResourceSet(testFolderPath, MODIFIED_MODEL_UML);

		Map<String, Object> matchOptions = new HashMap<String, Object>();

		MatchResourceSet computed_match = MatchService.doResourceSetMatch(modifiedResourceSet,
				originalResourceSet, matchOptions);
		DiffResourceSet computed_diff = DiffService.doDiff(computed_match);

		ResourceSet computedResourceSet = new ResourceSetImpl();
		Resource computedResource = computedResourceSet.createResource(URI.createURI(EXPECTED_EMFDIFF));
		computedResource.getContents().add(computed_diff);
		return computed_diff;
	}

	private DiffResourceSet getComputed3WayDiff(String testFolderPath) throws IOException,
			InterruptedException {
		ResourceSet originalResourceSet = getModelResourceSet(testFolderPath, "/origin/model.uml");
		ResourceSet localResourceSet = getModelResourceSet(testFolderPath, "/local/model.uml");
		ResourceSet remoteResourceSet = getModelResourceSet(testFolderPath, "/remote/model.uml");

		Map<String, Object> matchOptions = new HashMap<String, Object>();

		MatchResourceSet computed_match = MatchService.doResourceSetMatch(remoteResourceSet,
				localResourceSet, originalResourceSet, matchOptions);
		DiffResourceSet computed_diff = DiffService.doDiff(computed_match);

		ResourceSet computedResourceSet = new ResourceSetImpl();
		Resource computedResource = computedResourceSet.createResource(URI.createURI(EXPECTED_EMFDIFF));
		computedResource.getContents().add(computed_diff);
		return computed_diff;
	}

	private ResourceSet getModelResourceSet(String testFolderPath, String model) throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		final Resource original = resourceSet.createResource(URI.createURI(".." + model));
		original.load(
				AbstractUMLCompareTest.class.getResourceAsStream(getDiagramKindPath() + testFolderPath
						+ model), Collections.emptyMap());
		return resourceSet;
	}

	private DiffResourceSet getExpectedDiff(String testFolderPath) throws IOException {
		ResourceSet expectedResourceSet = new ResourceSetImpl();
		final ComparisonResourceSetSnapshot expected_diff_snapshot = (ComparisonResourceSetSnapshot)ModelUtils
				.load(AbstractUMLCompareTest.class.getResourceAsStream(getDiagramKindPath() + testFolderPath
						+ EXPECTED_SUFFIX), EXPECTED_EMFDIFF, expectedResourceSet);
		return expected_diff_snapshot.getDiffResourceSet();
	}

}

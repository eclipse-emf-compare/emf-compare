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

	protected final ResourceSet initResourceSet(ResourceSet rs) {
		// rs.getPackageRegistry().put(EcorePackage.eNS_URI,
		// EcorePackage.eINSTANCE);
		// rs.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		// rs.getPackageRegistry().put(DiffPackage.eNS_URI, DiffPackage.eINSTANCE);
		// rs.getPackageRegistry().put(MatchPackage.eNS_URI,
		// MatchPackage.eINSTANCE);
		// rs.getPackageRegistry().put(UML2DiffPackage.eNS_URI,
		// UML2DiffPackage.eINSTANCE);
		//
		// rs.getResourceFactoryRegistry().getExtensionToFactoryMap()
		// .put("uml", UMLResource.Factory.INSTANCE);
		// rs.getResourceFactoryRegistry().getExtensionToFactoryMap()
		// .put("emfdiff", new XMIResourceFactoryImpl());
		// rs.getResourceFactoryRegistry().getExtensionToFactoryMap()
		// .put("ecore", new EcoreResourceFactoryImpl());
		// rs.getResourceFactoryRegistry()
		// .getExtensionToFactoryMap()
		// .put(Resource.Factory.Registry.DEFAULT_EXTENSION,
		// new XMIResourceFactoryImpl());
		//
		// Map<URI, URI> uriMap = rs.getURIConverter().getURIMap();
		//
		// URI UML2Jar =
		// URI.createURI("jar:file:/home/mbarbero/Obeo/Projects/Ericsson/emfcompare-0/dev/target-platform/3.7RC1/plugins/org.eclipse.uml2.uml.resources_3.1.100.v201008191510.jar!/");
		// URI sysMLJar =
		// URI.createURI("jar:file:/home/mbarbero/Obeo/Projects/Ericsson/emfcompare-0/dev/target-platform/3.7RC1/plugins/org.eclipse.papyrus.sysml_0.8.0.v201105181418.jar!/");
		//
		// uriMap.put(
		// URI.createURI(UMLResource.LIBRARIES_PATHMAP),
		// UML2Jar.appendSegment("libraries").appendSegment(""));
		// uriMap.put(
		// URI.createURI(UMLResource.METAMODELS_PATHMAP),
		// UML2Jar.appendSegment("metamodels").appendSegment(""));
		// uriMap.put(
		// URI.createURI(UMLResource.PROFILES_PATHMAP),
		// UML2Jar.appendSegment("profiles").appendSegment(""));
		//
		// uriMap.put(
		// URI.createURI(SysmlResource.PROFILES_PATHMAP),
		// sysMLJar.appendSegment("model").appendSegment(""));
		// uriMap.put(
		// URI.createURI(SysmlResource.LIBRARIES_PATHMAP),
		// sysMLJar.appendSegment("libraries").appendSegment(""));
		// uriMap.put(URI.createURI(SysmlPackage.eNS_URI),
		// sysMLJar.appendSegment("model").appendSegment("sysml.ecore"));

		return rs;
	}

	abstract String getDiagramKindPath();

	protected final void testCompare(String testFolderPath) throws IOException, InterruptedException {
		DiffModel expected_diff = firstNonEmptyDiffModel(getExpectedDiff(testFolderPath));
		DiffModel computed_diff = firstNonEmptyDiffModel(getComputedDiff(testFolderPath));

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
	
	protected final void testMerge(String testFolderPath, Class<? extends DiffElement> diffKind) throws IOException, InterruptedException {
		testMerge(testFolderPath, true, diffKind);
		testMerge(testFolderPath, false, diffKind);
	}

	private void testMerge(String testFolderPath, boolean leftToRight) throws IOException,
			InterruptedException {
		DiffResourceSet computed_diff = getComputedDiff(testFolderPath);
		merge(leftToRight, computed_diff);
		testMerge(testFolderPath, leftToRight, computed_diff);
	}
	
	private void testMerge(String testFolderPath, boolean leftToRight, Class<? extends DiffElement> diffKind) throws IOException,
			InterruptedException {
		DiffResourceSet computed_diff = getComputedDiff(testFolderPath);
		Iterator<EObject> diffs = computed_diff.eAllContents();
		while (diffs.hasNext()) {
			EObject next = diffs.next();
			if (diffKind.isAssignableFrom(next.getClass())) {
				merge(leftToRight, (DiffElement) next);
			}
		}
		testMerge(testFolderPath, leftToRight, computed_diff);
	}

	private void testMerge(String testFolderPath, boolean leftToRight, DiffResourceSet computed_diff)
			throws IOException {
		Resource originalResource;
		Resource mergedResource;
		if (leftToRight) {
			ResourceSet originalResourceSet = getModelResourceSet(testFolderPath, MODIFIED_MODEL_UML);
			ResourceSet mergeResourceSet = getLeftResourceSet(computed_diff);

			originalResource = originalResourceSet
					.getResource(URI.createURI(".." + MODIFIED_MODEL_UML), true);
			mergedResource = mergeResourceSet.getResource(URI.createURI(".." + MODIFIED_MODEL_UML), true);
		} else {
			ResourceSet originalResourceSet = getModelResourceSet(testFolderPath, ORIGINAL_MODEL_UML);
			ResourceSet mergeResourceSet = getRightResourceSet(computed_diff);

			originalResource = originalResourceSet
					.getResource(URI.createURI(".." + ORIGINAL_MODEL_UML), true);
			mergedResource = mergeResourceSet.getResource(URI.createURI(".." + ORIGINAL_MODEL_UML), true);
		}

		Assert.assertEquals(ModelUtils.serialize(originalResource.getContents().get(0)),
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
		// matchOptions.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER,
		// (Object) new GenericMatchScopeProvider(modifiedResourceSet,
		// originalResourceSet));

		MatchResourceSet computed_match = MatchService.doResourceSetMatch(modifiedResourceSet,
				originalResourceSet, matchOptions);
		DiffResourceSet computed_diff = DiffService.doDiff(computed_match);

		ResourceSet computedResourceSet = initResourceSet(new ResourceSetImpl());
		Resource computedResource = computedResourceSet.createResource(URI.createURI(EXPECTED_EMFDIFF));
		computedResource.getContents().add(computed_diff);
		return computed_diff;
	}

	private ResourceSet getModelResourceSet(String testFolderPath, String model) throws IOException {
		ResourceSet resourceSet = initResourceSet(new ResourceSetImpl());
		final Resource original = resourceSet.createResource(URI.createURI(".." + model));
		original.load(
				AbstractUMLCompareTest.class.getResourceAsStream(getDiagramKindPath() + testFolderPath
						+ model), Collections.emptyMap());
		return resourceSet;
	}

	private DiffResourceSet getExpectedDiff(String testFolderPath) throws IOException {
		ResourceSet expectedResourceSet = initResourceSet(new ResourceSetImpl());
		final ComparisonResourceSetSnapshot expected_diff_snapshot = (ComparisonResourceSetSnapshot)ModelUtils
				.load(AbstractUMLCompareTest.class.getResourceAsStream(getDiagramKindPath() + testFolderPath
						+ EXPECTED_SUFFIX), EXPECTED_EMFDIFF, expectedResourceSet);
		return expected_diff_snapshot.getDiffResourceSet();
	}

}

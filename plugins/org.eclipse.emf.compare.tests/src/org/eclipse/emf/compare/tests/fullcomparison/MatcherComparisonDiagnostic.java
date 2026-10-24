/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.fullcomparison;

import static org.eclipse.emf.compare.tests.framework.ComparisonSignatures.differences;
import static org.eclipse.emf.compare.tests.framework.ComparisonSignatures.matches;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.DiffBuilder;
import org.eclipse.emf.compare.match.eobject.WeightProvider;
import org.eclipse.emf.compare.match.eobject.WeightProviderDescriptorRegistryImpl;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.MatcherKind;
import org.eclipse.emf.compare.tests.fullcomparison.data.distance.DistanceMatchInputData;
import org.eclipse.emf.compare.tests.suite.AllTests;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Checks whether content-hash and proximity matching produce the same object correspondence and differences
 * on selected existing Ecore and UML fixtures, with identifier matching disabled for both implementations.
 * This provides regression coverage beyond the small examples used by the focused matcher tests.
 * <p>
 * The parameter list includes the distance fixtures with original and aligned resource URIs, the available
 * UML and bug 390666 ancestor models, and generated two-way class models. Each matcher receives independently
 * loaded or generated inputs. Partner and difference signatures are compared as counted collections, so
 * equal totals cannot hide different partners, match parents or changed values.
 * </p>
 * <p>
 * For existing fixtures, the expected result is agreement with proximity on those cases. Generated models
 * additionally have known partners by construction: copied classes retain their corresponding positions,
 * and the explicit edit schedule determines the expected difference count. Those assertions check the hash
 * result independently of proximity's choices.
 * </p>
 * <p>
 * This class runs in {@code AllTests}. Timing measurements and generated three-way diagnostics are provided
 * by {@link ContentMatcherPerformanceTest}; focused three-way identity checks run in
 * {@code ContentHashEObjectMatcherTest}.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@RunWith(Parameterized.class)
@SuppressWarnings("nls")
public class MatcherComparisonDiagnostic {

	/** Largest generated model size used by the regression fixtures. */
	private static final int LARGE_MODEL_SIZE = 2000;

	/** UML fixture with a two-way and a three-way variant. */
	private static final String UML_FIXTURE = "NominalUML";

	/** Ecore regression fixture whose third side is named Ancestor. */
	private static final String BUG_390666 = "390666";

	/** Suffix used for the UML and generated three-way inputs. */
	private static final String ORIGIN_SUFFIX = "Origin";

	/** Prefix used to select generated class models. */
	private static final String SYNTHETIC_PREFIX = "synthetic-";

	/** Fixture selected for this parameterized test. */
	protected final String fixture;

	/** Ancestor-side fixture suffix. */
	protected final String ancestor;

	/** Whether corresponding resources receive the same URI. */
	protected final boolean alignURIs;

	/**
	 * Selects a fixture and optional ancestor for matcher comparison.
	 *
	 * @param fixture
	 *            the fixture name
	 * @param ancestor
	 *            the ancestor fixture suffix, or an empty string
	 * @param alignURIs
	 *            whether to give corresponding resources equal URIs
	 */
	public MatcherComparisonDiagnostic(String fixture, String ancestor, boolean alignURIs) {
		this.fixture = fixture;
		this.ancestor = ancestor;
		this.alignURIs = alignURIs;
	}

	/**
	 * Lists the distance fixtures explicitly, including their two available three-way cases.
	 *
	 * @return the fixture, ancestor suffix and URI-alignment parameters
	 */
	@Parameters(name = "{0} {1} aligned={2}")
	public static Collection<Object[]> fixtures() {
		List<Object[]> result = new ArrayList<>();
		for (String fixture : Arrays.asList("AbstractAndSuper", "Compare", "FeatureUpdateDelete", UML_FIXTURE,
				"PackageAddDelete", "PackageAddRemoveNoRename", "VerySmall", BUG_390666, "391657",
				"391798")) {
			result.add(new Object[] {fixture, "", false });
			result.add(new Object[] {fixture, "", true });
		}
		result.add(new Object[] {UML_FIXTURE, ORIGIN_SUFFIX, false });
		result.add(new Object[] {UML_FIXTURE, ORIGIN_SUFFIX, true });
		result.add(new Object[] {BUG_390666, "Ancestor", false });
		result.add(new Object[] {BUG_390666, "Ancestor", true });
		for (int size : new int[] {100, 1000, LARGE_MODEL_SIZE }) {
			result.add(new Object[] {SYNTHETIC_PREFIX + size, "", false });
		}
		return result;
	}

	/** Registers the test metamodels, including the UML fixture namespace alias. */
	@SuppressWarnings("restriction")
	@Before
	public void registerModels() {
		AllTests.fillEMFRegistries();
		EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		// This fixture uses the legacy namespace; standalone JUnit has no Eclipse alias registration.
		EPackage.Registry.INSTANCE.put("http://www.eclipse.org/uml2/3.0.0/UML", UMLPackage.eINSTANCE);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("uml",
				new org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl());
	}

	/**
	 * Checks partner and difference signatures on independent copies of the same fixture, without IDs.
	 * Existing fixtures use proximity as the reference result; generated cases additionally assert partners
	 * by their construction positions and counts from the edit schedule. Agreement alone cannot detect a
	 * mistake shared by both matchers. Conflict detection and merging are outside this test.
	 *
	 * @throws Exception
	 *             if the fixture cannot be loaded or the comparison fails
	 */
	@Test
	public void compareMatchers() throws Exception {
		IComparisonScope scope = createScope();
		WeightProvider.Descriptor.Registry weights = WeightProviderDescriptorRegistryImpl
				.createStandaloneInstance();
		Comparison proximity = match(scope, weights, MatcherKind.PROXIMITY);

		// Reload rather than sharing objects or resolution state between the implementations.
		IComparisonScope hashScope = createScope();
		Comparison hash = match(hashScope, weights, MatcherKind.CONTENT_HASH);
		assertSameResults(hashScope, proximity, hash);
	}

	/**
	 * Loads the fixture or creates its generated models, optionally aligning resource URIs.
	 *
	 * @return the fixture comparison scope
	 * @throws Exception
	 *             if the fixture cannot be loaded or the comparison fails
	 */
	protected IComparisonScope createScope() throws Exception {
		Resource left;
		Resource right;
		Resource origin = null;
		if (fixture.startsWith(SYNTHETIC_PREFIX)) {
			EPackage model = EcoreFactory.eINSTANCE.createEPackage();
			model.setName("model");
			int size = Integer.parseInt(fixture.substring(SYNTHETIC_PREFIX.length()));
			for (int i = 0; i < size; i++) {
				EClass type = EcoreFactory.eINSTANCE.createEClass();
				type.setName("Type" + i);
				model.getEClassifiers().add(type);
			}
			EPackage changed = EcoreUtil.copy(model);
			// Change one class per ten on the right; copies retain their original order and names.
			for (int i = 0; i < size; i += 10) {
				((EClass)changed.getEClassifiers().get(i)).setAbstract(true);
			}
			if (!ancestor.isEmpty()) {
				origin = new ResourceImpl(URI.createURI("ancestor.ecore"));
				origin.getContents().add(EcoreUtil.copy(model));
				// The left edits affect different classes, leaving the ancestor untouched.
				for (int i = 5; i < size; i += 10) {
					((EClass)model.getEClassifiers().get(i)).setInterface(true);
				}
			}
			left = new ResourceImpl(URI.createURI("model.ecore"));
			right = new ResourceImpl(URI.createURI("model.ecore"));
			left.getContents().add(model);
			right.getContents().add(changed);
		} else {
			DistanceMatchInputData data = new DistanceMatchInputData();
			left = load(data, "Left");
			right = load(data, "Right");
			if (!ancestor.isEmpty()) {
				origin = load(data, ancestor);
			}
			if (UML_FIXTURE.equals(fixture)) {
				for (Resource resource : Arrays.asList(left, right, origin)) {
					if (resource != null) {
						assertTrue("The UML fixture must load as a UML Model",
								resource.getContents().get(0) instanceof org.eclipse.uml2.uml.Model);
					}
				}
			}
		}
		if (alignURIs) {
			right.setURI(left.getURI());
		}
		return new DefaultComparisonScope(left, right, origin);
	}

	/**
	 * Runs one named content matcher with identifier matching disabled.
	 *
	 * @param scope
	 *            the fixture scope
	 * @param weights
	 *            the feature weights
	 * @param matcher
	 *            the matcher being measured or compared
	 * @return the object correspondence
	 */
	protected Comparison match(IComparisonScope scope, WeightProvider.Descriptor.Registry weights,
			MatcherKind matcher) {
		return matcher.factory(UseIdentifiers.NEVER, weights).getMatchEngine().match(scope,
				new BasicMonitor());
	}

	/**
	 * Compares complete signatures so equal counts cannot hide different partners or difference values.
	 *
	 * @param scope
	 *            the hash comparison's fixture objects
	 * @param proximity
	 *            the proximity result
	 * @param hash
	 *            the hash result
	 */
	protected void assertSameResults(IComparisonScope scope, Comparison proximity, Comparison hash) {
		assertEquals("Object pairings: " + fixture, matches(proximity), matches(hash));
		new DefaultDiffEngine(new DiffBuilder()).diff(proximity, new BasicMonitor());
		new DefaultDiffEngine(new DiffBuilder()).diff(hash, new BasicMonitor());
		assertEquals("Differences: " + fixture, differences(proximity), differences(hash));
		if (fixture.startsWith(SYNTHETIC_PREFIX)) {
			assertGeneratedCorrespondence(hash, (Resource)scope.getLeft(), (Resource)scope.getRight(),
					(Resource)scope.getOrigin());
		}
	}

	/**
	 * Checks generated object identities, owners and expected edit counts.
	 *
	 * @param comparison
	 *            the comparison in progress
	 * @param left
	 *            the left-side input
	 * @param right
	 *            the right-side input
	 * @param origin
	 *            the ancestor-side input
	 */
	protected static void assertGeneratedCorrespondence(Comparison comparison, Resource left, Resource right,
			Resource origin) {
		EPackage leftModel = (EPackage)left.getContents().get(0);
		EPackage rightModel = (EPackage)right.getContents().get(0);
		EPackage originModel = null;
		int changedSides = 1;
		if (origin != null) {
			originModel = (EPackage)origin.getContents().get(0);
			changedSides = 2;
		}
		Match parent = comparison.getMatch(leftModel);
		assertSame(rightModel, parent.getRight());
		assertSame(originModel, parent.getOrigin());
		for (int i = 0; i < leftModel.getEClassifiers().size(); i++) {
			Match match = comparison.getMatch(leftModel.getEClassifiers().get(i));
			assertSame(rightModel.getEClassifiers().get(i), match.getRight());
			Object ancestor = null;
			if (originModel != null) {
				ancestor = originModel.getEClassifiers().get(i);
			}
			assertSame(ancestor, match.getOrigin());
			assertSame(parent, match.eContainer());
		}
		// One boolean edit per ten classes on each changed side; matching adds no extra differences.
		assertEquals(leftModel.getEClassifiers().size() / 10 * changedSides,
				comparison.getDifferences().size());
	}

	/**
	 * Loads one side of an existing distance fixture.
	 *
	 * @param data
	 *            the existing fixture loader
	 * @param side
	 *            the requested comparison side
	 * @return the loaded resource
	 * @throws Exception
	 *             if the fixture cannot be loaded or the comparison fails
	 */
	private Resource load(DistanceMatchInputData data, String side) throws Exception {
		return (Resource)DistanceMatchInputData.class.getMethod("get" + fixture + side).invoke(data);
	}

}

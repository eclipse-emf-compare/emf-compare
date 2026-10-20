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
package org.eclipse.emf.compare.tests.match;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.tests.edit.data.ecore.a1.EcoreA1InputData;
import org.eclipse.emf.compare.tests.equi.data.EquiInputData;
import org.eclipse.emf.compare.tests.framework.AbstractMatcherTest;
import org.eclipse.emf.compare.tests.framework.MatchingAlgorithm;
import org.eclipse.emf.compare.tests.fullcomparison.data.identifier.IdentifierMatchInputData;
import org.eclipse.emf.compare.tests.merge.data.IndividualDiffInputData;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.junit.Test;

/**
 * Verifies the differences and merge behavior produced when existing identifier-based fixtures are compared
 * with identifier matching disabled. Each scenario runs independently with proximity and content-hash
 * matching on freshly loaded models.
 * <p>
 * In the selected feature-map and reference fixtures, identifier-first matching treats differently
 * identified objects as replacements. Content-only matching instead pairs those objects as renames. These
 * tests assert the expected partner and name change, then merge the change and check that the target object,
 * its XMI ID and the relevant incoming or opposite references are preserved. A three-way addition case checks
 * the expected counterpart, absent ancestor and pseudo-conflict grouping.
 * </p>
 * <p>
 * Library cases check renamed and moved attributes, using named fixture objects as the expected partners.
 * The three-way tests explicitly record a difference between the algorithms: hash selects the ancestor's
 * firstName field while proximity selects lastName, with corresponding conflict counts. The item-provider
 * copy of the library fixture is exercised as well. These expectations document each matcher's result on
 * those fixtures rather than assuming both implementations always agree.
 * </p>
 * <p>
 * This coverage complements the historical merge and presentation tests that explicitly retain identifier
 * priority. Difference counts, conflict kinds and partner identities are fixed expectations of these
 * scenarios; the other matcher's output is not used to compute them.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@SuppressWarnings("nls")
public class ContentMatchingScenariosTest extends AbstractMatcherTest {
	/** Name of the source feature-map child. */
	private static final String FIRST_NODE = "node1";

	/** Name of the corresponding target feature-map child. */
	private static final String SECOND_NODE = "node2";

	/** Library class containing renamed name fields. */
	private static final String PERSON = "Person";

	/** Renamed name field in the left library fixture. */
	private static final String FULL_NAME = "fullName";

	/** Known original name field in the library fixture. */
	private static final String FIRST_NAME = "firstName";

	/** Title field preserved across library hierarchy changes. */
	private static final String TITLE = "title";

	/** Differences expected in the two-way library fixture. */
	private static final int TWO_WAY_LIBRARY_DIFFERENCES = 30;

	/** Differences expected in the three-way library fixture. */
	private static final int THREE_WAY_LIBRARY_DIFFERENCES = 41;

	/** Conflicts with the original ancestor chosen by content hash. */
	private static final int HASH_LIBRARY_CONFLICTS = 7;

	/** Conflicts with proximity choosing the other name field as ancestor. */
	private static final int PROXIMITY_LIBRARY_CONFLICTS = 8;

	/** Fresh loader: no model instances are shared between matcher executions. */
	private final IndividualDiffInputData input = new IndividualDiffInputData();

	/**
	 * Selects the matcher whose content-only result is asserted.
	 *
	 * @param matcher
	 *            the selected matcher
	 */
	public ContentMatchingScenariosTest(MatchingAlgorithm matcher) {
		super(matcher);
	}

	/**
	 * Without the IDs, the two similarly shaped feature-map children correspond as a rename. The shared
	 * assertion merges left to right and checks that the target object and its original XMI ID survive.
	 *
	 * @throws IOException
	 *             if a fixture cannot be loaded
	 */
	@Test
	public void featureMapAdditionBecomesARenameWithoutIdentifiers() throws IOException {
		mergeRenameAndAssertTargetPreserved(input.getFeatureMapContainmentLeftAddScope(),
				input.getFeatureMapContainmentRightAddScope(), null, FIRST_NODE, SECOND_NODE);
	}

	/**
	 * The delete fixture has the same two-way content correspondence despite different XMI IDs. Merging the
	 * resulting name change must update the existing target rather than delete and recreate it.
	 *
	 * @throws IOException
	 *             if a fixture cannot be loaded
	 */
	@Test
	public void featureMapDeletionBecomesARenameWithoutIdentifiers() throws IOException {
		mergeRenameAndAssertTargetPreserved(input.getFeatureMapContainmentLeftDeleteScope(),
				input.getFeatureMapContainmentRightDeleteScope(), null, FIRST_NODE, SECOND_NODE);
	}

	/**
	 * Similar additions are matched across branches when identifiers are explicitly disabled. The paired
	 * addition has no ancestor, and all four differences belong to the single pseudo-conflict. This checks
	 * grouping and counts, not the individual kind/value of every difference or a merge result.
	 *
	 * @throws IOException
	 *             if a fixture cannot be loaded
	 */
	@Test
	public void similarFeatureMapAdditionsFormAPseudoConflict() throws IOException {
		Resource left = input.getFeatureMapContainmentLeftAddScope();
		Resource right = input.getFeatureMapContainmentRightAddScope();
		Resource origin = input.getFeatureMapContainmentOriginAddScope();
		Comparison result = compareContent(left, right, origin);
		Match addition = result.getMatch(findNode(left, FIRST_NODE));
		// Pair the independently added children; neither is present in the ancestor fixture.
		assertSame(findNode(right, SECOND_NODE), addition.getRight());
		assertNull(addition.getOrigin());
		assertEquals(4, result.getDifferences().size());
		assertEquals(1, result.getConflicts().size());
		assertEquals(ConflictKind.PSEUDO, result.getConflicts().get(0).getKind());
		assertEquals(4, result.getConflicts().get(0).getDifferences().size());
	}

	/**
	 * A content rename retains the existing referenced object instead of replacing it.
	 *
	 * @throws IOException
	 *             if a fixture cannot be loaded
	 */
	@Test
	public void renamedReferencedChildKeepsItsIncomingReference() throws IOException {
		Resource left = input.getDanglingPostMergeLeft();
		Resource right = input.getDanglingPostMergeRight();
		Resource origin = input.getDanglingPostMergeAncestor();
		mergeRenameAndAssertTargetPreserved(left, right, origin, "ClassB", "ClassC");
		EObject owner = findNode(right, "ClassA");
		assertSame(findNode(right, "ClassB"),
				owner.eGet(owner.eClass().getEStructuralFeature("singleValuedReference")));
	}

	/**
	 * Matching opposite-reference targets by content reduces the C5 replacement to a rename.
	 *
	 * @throws IOException
	 *             if a fixture cannot be loaded
	 */
	@Test
	public void renamedOppositeReferenceTargetKeepsItsReferences() throws IOException {
		EquiInputData data = new EquiInputData();
		Resource left = data.getC5Left();
		Resource right = data.getC5Right();
		mergeRenameAndAssertTargetPreserved(left, right, null, "Node3", "Node4");
		EObject source = findNode(right, "Node1");
		EObject target = findNode(right, "Node3");
		assertSame(target, source.eGet(source.eClass().getEStructuralFeature("source")));
		assertSame(source, target.eGet(target.eClass().getEStructuralFeature("destination")));
	}

	/**
	 * Both content matchers recognize renamed attributes and moved attribute content in two-way data.
	 *
	 * @throws IOException
	 *             if a fixture cannot be loaded
	 */
	@Test
	public void libraryAttributeRenamesAndMovesRetainTheirPartners() throws IOException {
		IdentifierMatchInputData data = new IdentifierMatchInputData();
		Resource left = data.getExtlibraryLeft();
		Resource right = data.getExtlibraryRight();
		Comparison result = compareContent(left, right, null);
		EPackage leftModel = (EPackage)left.getContents().get(0);
		EPackage rightModel = (EPackage)right.getContents().get(0);
		assertSame(findFeature(rightModel, PERSON, FIRST_NAME),
				result.getMatch(findFeature(leftModel, PERSON, FULL_NAME)).getRight());
		assertSame(findFeature(rightModel, "Book", "subtitle"),
				result.getMatch(findFeature(leftModel, "Book", TITLE)).getRight());
		assertSame(findFeature(rightModel, "TitledItem", TITLE),
				result.getMatch(findFeature(leftModel, "AudioVisualItem", TITLE)).getRight());
		assertEquals(TWO_WAY_LIBRARY_DIFFERENCES, result.getDifferences().size());
	}

	/**
	 * Hash follows firstName in the ancestor; proximity follows lastName in this fixture. These are explicit
	 * regression expectations for each algorithm, not a claim that both ancestor choices are semantically
	 * equivalent. The helper also checks the resulting difference and conflict totals.
	 *
	 * @throws IOException
	 *             if a fixture cannot be loaded
	 */
	@Test
	public void libraryRenameKeepsTheExpectedAncestorForEachMatcher() throws IOException {
		IdentifierMatchInputData data = new IdentifierMatchInputData();
		assertLibraryAncestors(data.getExtlibraryLeft(), data.getExtlibraryRight(),
				data.getExtlibraryOrigin());
	}

	/**
	 * The different ancestor choices prove which fallback ran, not just that some fallback ran.
	 *
	 * @throws IOException
	 *             if a fixture cannot be loaded
	 */
	@Test
	public void identifierPolicyDelegatesToTheSelectedAlgorithmWithoutIds() throws IOException {
		IdentifierMatchInputData data = new IdentifierMatchInputData();
		Resource left = data.getExtlibraryLeft();
		Resource right = data.getExtlibraryRight();
		Resource origin = data.getExtlibraryOrigin();
		for (Resource resource : new Resource[] {left, right, origin }) {
			TreeIterator<EObject> contents = resource.getAllContents();
			while (contents.hasNext()) {
				((XMLResource)resource).setID(contents.next(), null);
			}
		}
		Comparison result = matcher.builder(UseIdentifiers.WHEN_AVAILABLE).build()
				.compare(new DefaultComparisonScope(left, right, origin));
		String expectedAncestorName = FIRST_NAME;
		if (matcher == MatchingAlgorithm.PROXIMITY) {
			expectedAncestorName = "lastName";
		}
		assertSame(findFeature((EPackage)origin.getContents().get(0), PERSON, expectedAncestorName),
				result.getMatch(findFeature((EPackage)left.getContents().get(0), PERSON, FULL_NAME))
						.getOrigin());
	}

	/**
	 * The item-provider copies of the same library fixtures also exercise content-only matching.
	 *
	 * @throws IOException
	 *             if a fixture cannot be loaded
	 */
	@Test
	public void itemProviderLibraryCopyKeepsTheSameAncestorCorrespondence() throws IOException {
		EcoreA1InputData data = new EcoreA1InputData();
		assertLibraryAncestors(data.getLeft(), data.getRight(), data.getOrigin());
	}

	/**
	 * Checks the specified rename, merges it and retains both the object identity and its XMI ID.
	 *
	 * @param left
	 *            the left resource
	 * @param right
	 *            the right resource
	 * @param origin
	 *            the optional ancestor
	 * @param leftName
	 *            the source object's name
	 * @param rightName
	 *            the target object's name
	 */
	private void mergeRenameAndAssertTargetPreserved(Resource left, Resource right, Resource origin,
			String leftName, String rightName) {
		EObject source = findNode(left, leftName);
		EObject target = findNode(right, rightName);
		String originalTargetId = ((XMLResource)right).getID(target);

		// Establish that content matching produces exactly one name change on the known target.
		Comparison result = compareContent(left, right, origin);
		assertSame(target, result.getMatch(source).getRight());
		assertEquals(1, result.getDifferences().size());
		Diff rename = result.getDifferences().get(0);
		assertTrue(rename instanceof AttributeChange);
		assertEquals("name", ((AttributeChange)rename).getAttribute().getName());
		assertSame(result.getMatch(source), rename.getMatch());

		// Retain the pre-merge target reference: re-finding only by name could hide object replacement.
		new BatchMerger(IMerger.RegistryImpl.createStandaloneInstance())
				.copyAllLeftToRight(Collections.singletonList(rename), new BasicMonitor());
		assertSame(target, findNode(right, leftName));
		assertEquals(originalTargetId, ((XMLResource)right).getID(target));
		// A fresh two-way comparison verifies convergence after the left-to-right merge.
		assertEquals(0, compareContent(left, right, null).getDifferences().size());
	}

	/**
	 * Keeps the known ancestor choice visible for both matcher implementations.
	 *
	 * @param left
	 *            the left fixture
	 * @param right
	 *            the right fixture
	 * @param origin
	 *            the ancestor fixture
	 */
	private void assertLibraryAncestors(Resource left, Resource right, Resource origin) {
		Comparison result = compareContent(left, right, origin);
		EPackage leftModel = (EPackage)left.getContents().get(0);
		EPackage rightModel = (EPackage)right.getContents().get(0);
		EPackage originModel = (EPackage)origin.getContents().get(0);
		Match personName = result.getMatch(findFeature(leftModel, PERSON, FULL_NAME));
		assertSame(findFeature(rightModel, PERSON, FIRST_NAME), personName.getRight());
		if (matcher == MatchingAlgorithm.CONTENT_HASH) {
			assertSame(findFeature(originModel, PERSON, FIRST_NAME), personName.getOrigin());
			assertEquals(HASH_LIBRARY_CONFLICTS, result.getConflicts().size());
		} else {
			// Proximity associates the renamed field with lastName in this fixture.
			assertSame(findFeature(originModel, PERSON, "lastName"), personName.getOrigin());
			assertEquals(PROXIMITY_LIBRARY_CONFLICTS, result.getConflicts().size());
		}
		assertEquals(THREE_WAY_LIBRARY_DIFFERENCES, result.getDifferences().size());
	}

	/**
	 * Runs precisely one selected content matcher on these freshly loaded objects.
	 *
	 * @param left
	 *            the left fixture
	 * @param right
	 *            the right fixture
	 * @param origin
	 *            the optional ancestor fixture
	 * @return the fully processed comparison
	 */
	private Comparison compareContent(Resource left, Resource right, Resource origin) {
		return matcher.builder(UseIdentifiers.NEVER)
				.build().compare(new DefaultComparisonScope(left, right, origin));
	}

	/**
	 * Finds a named node in these small, explicitly named fixtures.
	 *
	 * @param resource
	 *            the fixture resource
	 * @param name
	 *            the expected node name
	 * @return the fixture node
	 */
	private static EObject findNode(Resource resource, String name) {
		for (TreeIterator<EObject> contents = resource.getAllContents(); contents.hasNext();) {
			EObject object = contents.next();
			if (object instanceof Node && name.equals(((Node)object).getName())) {
				return object;
			}
		}
		throw new AssertionError("Missing fixture node: " + name);
	}

	/**
	 * Selects a named Ecore feature without depending on its serialized identifier.
	 *
	 * @param model
	 *            the fixture package
	 * @param owner
	 *            the class name
	 * @param name
	 *            the feature name
	 * @return the named structural feature
	 */
	private static EObject findFeature(EPackage model, String owner, String name) {
		EClass type = (EClass)model.getEClassifier(owner);
		assertNotNull(type);
		EObject feature = type.getEStructuralFeature(name);
		assertNotNull(feature);
		return feature;
	}
}

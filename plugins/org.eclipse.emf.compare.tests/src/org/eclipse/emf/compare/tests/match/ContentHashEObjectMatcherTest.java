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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ComparisonCanceledException;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.DiffBuilder;
import org.eclipse.emf.compare.match.DefaultComparisonFactory;
import org.eclipse.emf.compare.match.DefaultEqualityHelperFactory;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.eobject.EditionDistance;
import org.eclipse.emf.compare.match.eobject.IdentifierEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.WeightProvider;
import org.eclipse.emf.compare.match.eobject.WeightProviderDescriptorRegistryImpl;
import org.eclipse.emf.compare.match.hash.CandidateMatcher;
import org.eclipse.emf.compare.match.hash.CandidateMatcher.CandidateGroupKey;
import org.eclipse.emf.compare.match.hash.CandidateMatcher.MatchingMode;
import org.eclipse.emf.compare.match.hash.ContentHashEObjectMatcher;
import org.eclipse.emf.compare.match.hash.ContentHasher;
import org.eclipse.emf.compare.match.hash.FeatureValue;
import org.eclipse.emf.compare.match.hash.Fingerprint;
import org.eclipse.emf.compare.match.hash.IContentHasher;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.tests.conflict.data.ConflictInputData;
import org.eclipse.emf.compare.tests.merge.data.IndividualDiffInputData;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodesFactory;
import org.eclipse.emf.compare.tests.req.data.ReqInputData;
import org.eclipse.emf.compare.tests.suite.AllTests;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.junit.Test;

/**
 * Verifies that {@link ContentHashEObjectMatcher} selects the expected object partners and preserves
 * existing correspondence across edits, moves and three-way comparisons. Most cases construct the hash
 * matcher directly or through a match engine without an identifier-matching wrapper.
 * <p>
 * Small generated Ecore and node models isolate partial-match combination, missing parents and ancestors,
 * independent additions, numbered-name renames, resource correspondence and reuse after model edits. The
 * identifier-matched-owner case deliberately adds an identifier wrapper to check that anonymous children
 * stay under their already matched parents. Custom-hasher and cancellation cases exercise the matcher API
 * directly so the surrounding comparison engine cannot supply the behavior being asserted.
 * </p>
 * <p>
 * Selected add/delete, feature-map move, containment-feature move and complex three-way fixtures have
 * declared XMI identities. Those identities supply expected partners for assertions only; the matcher must
 * recover the correspondence from content. Generated sibling models instead define partners by their known
 * positions on each side, and their edit schedule supplies the expected difference count. A deep containment
 * chain exercises snapshot computation and matching without relying on recursive fixture construction.
 * </p>
 * <p>
 * These are explicit fixture expectations, rather than expectations copied from another matcher's output.
 * They complement the existing parameterized corpus and the proximity comparisons in
 * {@code MatcherComparisonDiagnostic}.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@SuppressWarnings("nls")
public class ContentHashEObjectMatcherTest {
	/** Depth used to expose recursive traversal or match nesting. */
	private static final int CONTAINMENT_DEPTH = 2000;

	/** Largest generated sibling set used to check three-way correspondence. */
	private static final int LARGE_MODEL_SIZE = 2000;

	/** Common package name for the generated fixtures. */
	private static final String MODEL = "model";

	/** First owner name in the generated fixtures. */
	private static final String FIRST = "first";

	/** Second owner name in the generated fixtures. */
	private static final String SECOND = "second";

	/** Name shared by corresponding classes. */
	private static final String TYPE = "type";

	/** Name of the three-way fixture's original class. */
	private static final String EXAMPLE = "Example";

	/** Feature weights used by the matcher. */
	private final WeightProvider.Descriptor.Registry weights = WeightProviderDescriptorRegistryImpl
			.createStandaloneInstance();

	/** A client can replace candidate matching and its distance policy without sharing state between runs. */
	@Test
	public void candidateFactorySupportsClientSubclassAndCustomDistance() {
		EClass left = namedClass("original");
		EClass right = namedClass("unrelated");
		assertNull(match(left, right, null).getMatch(left).getRight());

		PermissiveContentMatcher matcher = new PermissiveContentMatcher(weights);
		DefaultMatchEngine engine = new DefaultMatchEngine(matcher,
				new DefaultComparisonFactory(new DefaultEqualityHelperFactory()));
		for (int attempt = 0; attempt < 2; attempt++) {
			Comparison comparison = engine.match(new DefaultComparisonScope(left, right, null),
					new BasicMonitor());
			assertSame(right, comparison.getMatch(left).getRight());
		}
		assertEquals(2, matcher.createdMatchers.size());
		assertNotSame(matcher.createdMatchers.get(0), matcher.createdMatchers.get(1));
		assertTrue(matcher.candidatePasses > 0);
		assertTrue(matcher.distanceChecks > 0);
	}

	/** Cancellation during capture must stop before the next object, leaving the comparison incomplete. */
	@Test(expected = ComparisonCanceledException.class)
	public void cancellationDuringCaptureStopsBeforeTheNextObject() {
		Resource left = new ResourceImpl();
		left.getContents().addAll(Arrays.asList(namedClass(FIRST), namedClass(TYPE), namedClass(SECOND)));
		Resource right = new ResourceImpl();
		right.getContents().addAll(EcoreUtil.copyAll(left.getContents()));
		BasicMonitor monitor = new BasicMonitor();
		ContentHasher hasher = new CancelAfterCaptureHasher(weights, monitor, left.getContents().get(1),
				left.getContents().get(2));
		Comparison comparison = CompareFactory.eINSTANCE.createComparison();
		try {
			new ContentHashEObjectMatcher(weights, hasher).createMatches(comparison,
					left.getContents().iterator(), right.getContents().iterator(), Collections.emptyIterator(),
					monitor);
		} finally {
			assertTrue(monitor.isCanceled());
			assertNull(comparison.getMatch(left.getContents().get(2)));
		}
	}

	/** Identical unnamed children must stay paired under their already matched owners after a reorder. */
	@Test
	public void identicalChildrenStayWithTheirIdentifierMatchedOwners() {
		for (boolean threeWay : new boolean[] {false, true }) {
			Node left = identifiedOwners(false);
			Node right = identifiedOwners(true);
			Node origin = null;
			if (threeWay) {
				origin = identifiedOwners(false);
			}
			IdentifierEObjectMatcher matcher = new IdentifierEObjectMatcher(
					new ContentHashEObjectMatcher(weights, new ContentHasher(weights)),
					object -> ((Node)object).getName());
			// Named owners are paired first; only their unnamed children reach the content matcher.
			Comparison comparison = new DefaultMatchEngine(matcher,
					new DefaultComparisonFactory(new DefaultEqualityHelperFactory()))
					.match(new DefaultComparisonScope(left, right, origin), new BasicMonitor());
			for (Node owner : left.getContainmentRef1()) {
				Node counterpart = (Node)comparison.getMatch(owner).getRight();
				Match child = comparison.getMatch(owner.getContainmentRef1().get(0));
				assertSame(counterpart.getContainmentRef1().get(0), child.getRight());
				if (threeWay) {
					Node ancestor = (Node)comparison.getMatch(owner).getOrigin();
					assertSame(ancestor.getContainmentRef1().get(0), child.getOrigin());
				}
			}
			new DefaultDiffEngine(new DiffBuilder()).diff(comparison, new BasicMonitor());
			// The parent reorder is the only actual change.
			assertEquals(1, comparison.getDifferences().size());
		}
	}

	/**
	 * A chain of {@link #CONTAINMENT_DEPTH} packages must support deferred similarity and correctly nested
	 * matches without overflowing the test JVM's stack. This checks that depth, not an arbitrary depth bound.
	 */
	@Test
	public void deepContainmentCanBeFingerprintedAndMatchedWithoutStackOverflow() {
		List<EObject> left = nestedPackages();
		List<EObject> right = nestedPackages();
		ContentHasher hasher = new ContentHasher(weights);
		// Force the deferred computation as well as capture on the full containment chain.
		Fingerprint snapshot = hasher.fingerprint(left.get(0));
		snapshot.similarity();

		// Call the matcher directly to exclude the comparison engine's own recursive scope checks.
		Comparison comparison = new DefaultComparisonFactory(new DefaultEqualityHelperFactory())
				.createComparison();
		new ContentHashEObjectMatcher(weights, hasher).createMatches(comparison, left.iterator(),
				right.iterator(), Collections.emptyIterator(), new BasicMonitor());
		for (int i = 0; i < left.size(); i++) {
			assertSame(right.get(i), comparison.getMatch(left.get(i)).getRight());
			if (i == 0) {
				assertSame(comparison, comparison.getMatch(left.get(i)).eContainer());
			} else {
				assertSame(comparison.getMatch(left.get(i - 1)),
						comparison.getMatch(left.get(i)).eContainer());
			}
		}
	}

	/** Direct content matching can join compatible seeds when no identifier policy separates them. */
	@Test
	public void joinsCompatibleExistingPartialMatchesWithoutDuplicatingObjects() {
		EClass left = namedClass(TYPE);
		EClass right = namedClass(TYPE);
		EClass origin = namedClass(TYPE);
		Comparison comparison = new DefaultComparisonFactory(new DefaultEqualityHelperFactory())
				.createComparison();
		comparison.setThreeWay(true);
		Match leftMatch = CompareFactory.eINSTANCE.createMatch();
		leftMatch.setLeft(left);
		Match ancestorMatch = CompareFactory.eINSTANCE.createMatch();
		ancestorMatch.setRight(right);
		ancestorMatch.setOrigin(origin);
		comparison.getMatches().addAll(Arrays.asList(leftMatch, ancestorMatch));

		// This direct invocation supplies both seeds as content candidates, without an identifier matcher.
		new ContentHashEObjectMatcher(weights, new ContentHasher(weights)).createMatches(comparison,
				Collections.singletonList(left).iterator(), Collections.singletonList(right).iterator(),
				Collections.singletonList(origin).iterator(), new BasicMonitor());
		assertEquals(1, comparison.getMatches().size());
		assertSame(comparison.getMatch(left), comparison.getMatch(right));
		assertSame(comparison.getMatch(left), comparison.getMatch(origin));
	}

	/** An ID change stays a replacement even when fallback matching runs on the anonymous children. */
	@Test
	public void threeWayHashFallbackPreservesDistinctIdentifierMatches() {
		Resource left = identifiedModel("left.nodes", "left-id");
		Resource right = identifiedModel("right.nodes", "shared-id");
		Resource origin = identifiedModel("origin.nodes", "shared-id");
		List<EObject> capturedObjects = new ArrayList<>();
		ContentHasher hasher = new ContentHasher(weights) {
			@Override
			public Fingerprint fingerprint(EObject object) {
				capturedObjects.add(object);
				return super.fingerprint(object);
			}
		};
		IdentifierEObjectMatcher matcher = new IdentifierEObjectMatcher(
				new ContentHashEObjectMatcher(weights, hasher));
		Comparison comparison = new DefaultMatchEngine(matcher,
				new DefaultComparisonFactory(new DefaultEqualityHelperFactory()))
				.match(new DefaultComparisonScope(left, right, origin), new BasicMonitor());

		Match leftOnly = comparison.getMatch(left.getContents().get(0));
		Match rightAndOrigin = comparison.getMatch(right.getContents().get(0));
		assertNotSame(leftOnly, rightAndOrigin);
		assertNull(leftOnly.getRight());
		assertNull(leftOnly.getOrigin());
		assertNull(rightAndOrigin.getLeft());
		assertSame(origin.getContents().get(0), rightAndOrigin.getOrigin());
		for (Resource resource : Arrays.asList(left, right, origin)) {
			Node root = (Node)resource.getContents().get(0);
			assertTrue(capturedObjects.contains(root.getContainmentRef1().get(0)));
		}
	}

	/** Two lone classes can correspond despite a changed numeric suffix. */
	@Test
	public void aNumberedNameCanChangeWhenThereAreNoCompetingLabels() {
		EClass left = namedClass("Type1");
		EClass right = namedClass("Type2");
		assertSame(right, match(left, right, null).getMatch(left).getRight());
	}

	/** An abstract flag identifies the renamed Type1 among competing numbered class names. */
	@Test
	public void contentCanDisambiguateARenameBetweenCompetingNumberedNames() {
		EPackage left = EcoreFactory.eINSTANCE.createEPackage();
		left.setName(MODEL);
		EClass changed = namedClass("Type1");
		changed.setAbstract(true);
		left.getEClassifiers().add(changed);
		left.getEClassifiers().add(namedClass("Type2"));
		EPackage right = EcoreUtil.copy(left);
		right.getEClassifiers().get(0).setName("Type3");
		assertSame(right.getEClassifiers().get(0), match(left, right, null).getMatch(changed).getRight());
	}

	/**
	 * The A5 add/delete fixture must match by content to the partners declared by its XMI IDs, in both
	 * directions.
	 *
	 * @throws IOException
	 *             if the fixture cannot be loaded or the comparison fails
	 */
	@Test
	public void addDeleteFixturePreservesDeclaredIdentities() throws IOException {
		AllTests.fillEMFRegistries();
		ReqInputData data = new ReqInputData();
		assertMatchesFixtureIdentities(data.getA5Left(), data.getA5Right(), null);
		assertMatchesFixtureIdentities(data.getA5Right(), data.getA5Left(), null);
	}

	/**
	 * The feature-map move fixture must match by content to the partners declared by its XMI IDs.
	 *
	 * @throws IOException
	 *             if the fixture cannot be loaded or the comparison fails
	 */
	@Test
	public void featureMapMoveFixturePreservesDeclaredIdentities() throws IOException {
		AllTests.fillEMFRegistries();
		IndividualDiffInputData data = new IndividualDiffInputData();
		assertMatchesFixtureIdentities(data.getFeatureMapContainmentLeftMoveScope(),
				data.getFeatureMapContainmentRightMoveScope(), null);
	}

	/**
	 * The containment-feature move fixture must match by content to the partners declared by its XMI IDs.
	 *
	 * @throws IOException
	 *             if the fixture cannot be loaded or the comparison fails
	 */
	@Test
	public void containmentFeatureMoveFixturePreservesDeclaredIdentities() throws IOException {
		AllTests.fillEMFRegistries();
		IndividualDiffInputData data = new IndividualDiffInputData();
		assertMatchesFixtureIdentities(data.getReferenceDifferentContainmentMoveLeft(),
				data.getReferenceDifferentContainmentMoveRight(),
				data.getReferenceDifferentContainmentMoveOrigin());
	}

	/**
	 * The complex three-way fixture must match by content to the partners declared by its XMI IDs.
	 *
	 * @throws IOException
	 *             if the fixture cannot be loaded or the comparison fails
	 */
	@Test
	public void complexThreeWayFixturePreservesDeclaredIdentities() throws IOException {
		AllTests.fillEMFRegistries();
		ConflictInputData data = new ConflictInputData();
		assertMatchesFixtureIdentities(data.getComplexLeft(), data.getComplexRight(),
				data.getComplexOrigin());
	}

	/**
	 * The supplied hasher receives every object in side/iterator order, and case-normalized names pair the
	 * reordered inputs correctly. Recorded calls prove use of the extension; these partners alone would not
	 * prove that the default hasher could not find the same correspondence.
	 */
	@Test
	public void customHasherNormalizesNamesOnAllThreeSides() {
		EClass leftClass = namedClass("CLASS");
		EClass leftOther = namedClass("OTHER");
		EClass rightClass = namedClass("class");
		EClass rightOther = namedClass("other");
		EClass originalClass = namedClass("Class");
		EClass originalOther = namedClass("Other");
		Comparison comparison = new DefaultComparisonFactory(new DefaultEqualityHelperFactory())
				.createComparison();
		comparison.setThreeWay(true);
		CaseInsensitiveNameHasher hasher = new CaseInsensitiveNameHasher();
		// Reversing the right input prevents a positional zip from satisfying the partner assertions.
		new ContentHashEObjectMatcher(weights, hasher).createMatches(comparison,
				Arrays.<EObject> asList(leftClass, leftOther).iterator(),
				Arrays.<EObject> asList(rightOther, rightClass).iterator(),
				Arrays.<EObject> asList(originalClass, originalOther).iterator(), new BasicMonitor());
		assertEquals(
				Arrays.asList(leftClass, leftOther, rightOther, rightClass, originalClass, originalOther),
				hasher.fingerprintedObjects);
		assertSame(rightClass, comparison.getMatch(leftClass).getRight());
		assertSame(originalClass, comparison.getMatch(leftClass).getOrigin());
		assertSame(rightOther, comparison.getMatch(leftOther).getRight());
		assertSame(originalOther, comparison.getMatch(leftOther).getOrigin());
	}

	/** Independent abstract/interface edits must retain a single left/right/ancestor match. */
	@Test
	public void threeWayRetainsIndependentEditsAgainstTheSameAncestor() {
		EClass origin = namedClass(EXAMPLE);
		EClass left = EcoreUtil.copy(origin);
		EClass right = EcoreUtil.copy(origin);
		left.setAbstract(true);
		right.setInterface(true);
		Comparison comparison = match(left, right, origin);
		assertSame(left, comparison.getMatch(origin).getLeft());
		assertSame(right, comparison.getMatch(origin).getRight());
		assertSame(origin, comparison.getMatch(left).getOrigin());
	}

	/** Deleting a parent must leave ancestor/right matches for both that parent and its child. */
	@Test
	public void deletedParentsAndChildrenRemainMatchedOnTheSurvivingBranch() {
		EPackage origin = EcoreFactory.eINSTANCE.createEPackage();
		origin.setName(MODEL);
		EPackage nested = EcoreFactory.eINSTANCE.createEPackage();
		nested.setName("nested");
		EClass child = namedClass("child");
		nested.getEClassifiers().add(child);
		origin.getESubpackages().add(nested);
		EPackage left = EcoreUtil.copy(origin);
		EPackage right = EcoreUtil.copy(origin);
		left.getESubpackages().clear();

		Comparison comparison = match(left, right, origin);
		Match parentMatch = comparison.getMatch(nested);
		Match childMatch = comparison.getMatch(child);
		assertNull(parentMatch.getLeft());
		assertSame(right.getESubpackages().get(0), parentMatch.getRight());
		assertNull(childMatch.getLeft());
		assertSame(right.getESubpackages().get(0).getEClassifiers().get(0), childMatch.getRight());
		assertSame(parentMatch, childMatch.eContainer());
	}

	/** Equivalent independently added elements must form one left/right match with no ancestor. */
	@Test
	public void equalAdditionsOnBothBranchesMatchWithoutAnAncestor() {
		EPackage origin = EcoreFactory.eINSTANCE.createEPackage();
		origin.setName(MODEL);
		EPackage left = EcoreUtil.copy(origin);
		EPackage right = EcoreUtil.copy(origin);
		EEnum added = EcoreFactory.eINSTANCE.createEEnum();
		added.setName("shared");
		EEnum counterpart = EcoreUtil.copy(added);
		left.getEClassifiers().add(added);
		right.getEClassifiers().add(counterpart);

		Comparison comparison = match(left, right, origin);
		assertSame(right, comparison.getMatch(left).getRight());
		assertSame(origin, comparison.getMatch(left).getOrigin());
		assertSame(counterpart, comparison.getMatch(added).getRight());
		assertNull(comparison.getMatch(added).getOrigin());
	}

	/** Unrelated additions of different metaclasses must remain separate from each other and the root. */
	@Test
	public void unrelatedAdditionsHaveNoCounterpartOrAncestor() {
		EPackage origin = EcoreFactory.eINSTANCE.createEPackage();
		origin.setName(MODEL);
		EPackage left = EcoreUtil.copy(origin);
		EPackage right = EcoreUtil.copy(origin);
		EClass leftOnly = namedClass("leftOnly");
		EDataType rightOnly = EcoreFactory.eINSTANCE.createEDataType();
		rightOnly.setName("rightOnly");
		left.getEClassifiers().add(leftOnly);
		right.getEClassifiers().add(rightOnly);

		Comparison comparison = match(left, right, origin);
		assertNull(comparison.getMatch(leftOnly).getRight());
		assertNull(comparison.getMatch(leftOnly).getOrigin());
		assertNull(comparison.getMatch(rightOnly).getLeft());
		assertNull(comparison.getMatch(rightOnly).getOrigin());
	}

	/** Renaming an element on one branch must retain its unchanged counterpart and ancestor. */
	@Test
	public void renamedBranchStillMatchesItsUnchangedAncestor() {
		EClass origin = namedClass("OriginalName");
		EClass left = EcoreUtil.copy(origin);
		EClass right = EcoreUtil.copy(origin);
		left.setName("OriginalNames");
		Comparison comparison = match(left, right, origin);
		assertSame(left, comparison.getMatch(origin).getLeft());
		assertSame(right, comparison.getMatch(origin).getRight());
	}

	/**
	 * Moving an attribute must retain its correspondence even when the other branch deletes its new owner.
	 */
	@Test
	public void movedAttributesKeepTheirIdentityWhenTheOtherBranchDeletesTheDestination() {
		EPackage origin = EcoreFactory.eINSTANCE.createEPackage();
		origin.setName(MODEL);
		EClass first = namedClass(FIRST);
		EClass second = namedClass(SECOND);
		origin.getEClassifiers().addAll(Arrays.asList(first, second));
		EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();
		attribute.setName("label");
		attribute.setEType(EcorePackage.Literals.ESTRING);
		first.getEStructuralFeatures().add(attribute);
		EPackage deletionBranch = EcoreUtil.copy(origin);
		EPackage moveBranch = EcoreUtil.copy(origin);
		deletionBranch.getEClassifiers().remove(1);
		EStructuralFeature survivingFeature = ((EClass)deletionBranch.getEClassifiers().get(0))
				.getEStructuralFeatures().get(0);
		EStructuralFeature movedFeature = ((EClass)moveBranch.getEClassifiers().get(0)).getEStructuralFeatures()
				.get(0);
		((EClass)moveBranch.getEClassifiers().get(1)).getEStructuralFeatures().add(movedFeature);
		Comparison forward = match(deletionBranch, moveBranch, origin);
		assertSame(survivingFeature, forward.getMatch(attribute).getLeft());
		assertSame(movedFeature, forward.getMatch(attribute).getRight());

		// Reversing sides checks that the surviving destination is not assumed to be on the right.
		Comparison reversed = match(moveBranch, deletionBranch, origin);
		assertSame(movedFeature, reversed.getMatch(attribute).getLeft());
		assertSame(survivingFeature, reversed.getMatch(attribute).getRight());
	}

	/** Reusing an engine after exchanging names must select the new partners. */
	@Test
	public void reusedMatcherTakesFreshSnapshotsAfterModelChanges() {
		EPackage left = EcoreFactory.eINSTANCE.createEPackage();
		left.setName(MODEL);
		EClass first = EcoreFactory.eINSTANCE.createEClass();
		first.setName(FIRST);
		EClass second = EcoreFactory.eINSTANCE.createEClass();
		second.setName(SECOND);
		left.getEClassifiers().add(first);
		left.getEClassifiers().add(second);
		EPackage right = EcoreUtil.copy(left);
		DefaultComparisonScope scope = new DefaultComparisonScope(left, right, null);
		DefaultMatchEngine engine = new DefaultMatchEngine(
				new ContentHashEObjectMatcher(weights, new ContentHasher(weights)),
				new DefaultComparisonFactory(new DefaultEqualityHelperFactory()));
		Comparison original = engine.match(scope, new BasicMonitor());
		assertSame(right.getEClassifiers().get(0), original.getMatch(first).getRight());

		// Keep the same objects and engine: stale cached snapshots would retain the old pairings.
		right.getEClassifiers().get(0).setName(SECOND);
		right.getEClassifiers().get(1).setName(FIRST);
		Comparison changed = engine.match(scope, new BasicMonitor());
		assertSame(right.getEClassifiers().get(1), changed.getMatch(first).getRight());
		assertSame(right.getEClassifiers().get(0), changed.getMatch(second).getRight());
	}

	/** Explicit resource pairs must determine partners when content is identical and URIs conflict. */
	@Test
	public void resourceCorrespondenceTakesPriorityOverEqualURIs() {
		EClass leftFirst = namedClass(TYPE);
		EClass leftSecond = namedClass(TYPE);
		EClass rightFirst = namedClass(TYPE);
		EClass rightSecond = namedClass(TYPE);
		Comparison comparison = CompareFactory.eINSTANCE.createComparison();
		recordResourceMatch(comparison, resource("first.ecore", leftFirst),
				resource("second.ecore", rightSecond));
		recordResourceMatch(comparison, resource("second.ecore", leftSecond),
				resource("first.ecore", rightFirst));
		// All classes have identical content; only the explicit resource pairs distinguish them.
		new ContentHashEObjectMatcher(weights, new ContentHasher(weights)).createMatches(comparison,
				Arrays.asList(leftFirst, leftSecond).iterator(),
				Arrays.asList(rightFirst, rightSecond).iterator(),
				Collections.<EObject> emptyList().iterator(), new BasicMonitor());
		assertSame(rightSecond, comparison.getMatch(leftFirst).getRight());
		assertSame(rightFirst, comparison.getMatch(leftSecond).getRight());
	}

	/**
	 * Creates reordered, identified owners with indistinguishable children without IDs.
	 *
	 * @param reverse
	 *            whether to reverse the owners
	 * @return the fixture root
	 */
	private static Node identifiedOwners(boolean reverse) {
		Node root = NodesFactory.eINSTANCE.createNode();
		root.setName(MODEL);
		for (String name : Arrays.asList(FIRST, SECOND)) {
			Node owner = NodesFactory.eINSTANCE.createNode();
			owner.setName(name);
			owner.getContainmentRef1().add(NodesFactory.eINSTANCE.createNode());
			root.getContainmentRef1().add(owner);
		}
		if (reverse) {
			root.getContainmentRef1().move(0, 1);
		}
		return root;
	}

	/**
	 * Creates a chain without relying on recursive copying or traversal in the fixture.
	 *
	 * @return the packages in parent-first order
	 */
	private static List<EObject> nestedPackages() {
		List<EObject> result = new ArrayList<>();
		EPackage parent = null;
		for (int i = 0; i < CONTAINMENT_DEPTH; i++) {
			EPackage child = EcoreFactory.eINSTANCE.createEPackage();
			child.setName(MODEL + i);
			if (parent != null) {
				parent.getESubpackages().add(child);
			}
			result.add(child);
			parent = child;
		}
		return result;
	}

	/**
	 * Checks the fixture's XMI identities directly; the matcher itself receives no identifier strategy. These
	 * selected regression fixtures supplement the parameterized corpus tests with assertions of known object
	 * partners.
	 *
	 * @param left
	 *            the left fixture
	 * @param right
	 *            the right fixture
	 * @param origin
	 *            the optional ancestor fixture
	 */
	private void assertMatchesFixtureIdentities(Resource left, Resource right, Resource origin) {
		DefaultMatchEngine engine = new DefaultMatchEngine(
				new ContentHashEObjectMatcher(weights, new ContentHasher(weights)),
				new DefaultComparisonFactory(new DefaultEqualityHelperFactory()));
		Comparison comparison = engine.match(new DefaultComparisonScope(left, right, origin),
				new BasicMonitor());
		for (Resource side : Arrays.asList(left, right, origin)) {
			if (side == null) {
				continue;
			}
			for (TreeIterator<EObject> objects = side.getAllContents(); objects.hasNext();) {
				EObject object = objects.next();
				String id = ((XMLResource)side).getID(object);
				assertNotNull("The fixture must declare an XMI identity for each object", id);
				Match match = comparison.getMatch(object);
				assertNotNull("Missing match for fixture identity " + id, match);
				assertSame(id, left.getEObject(id), match.getLeft());
				assertSame(id, right.getEObject(id), match.getRight());
				EObject expectedOrigin = null;
				if (origin != null) {
					expectedOrigin = origin.getEObject(id);
				}
				assertSame(id, expectedOrigin, match.getOrigin());
			}
		}
	}

	/** Each generated class must retain its known partners after independent edits on both branches. */
	@Test
	public void independentSiblingEditsRetainEveryGeneratedThreeWayPartner() {
		for (int size : new int[] {100, 1000, LARGE_MODEL_SIZE }) {
			EPackage origin = EcoreFactory.eINSTANCE.createEPackage();
			origin.setName(MODEL);
			for (int i = 0; i < size; i++) {
				origin.getEClassifiers().add(namedClass("Type" + i));
			}
			EPackage left = EcoreUtil.copy(origin);
			EPackage right = EcoreUtil.copy(origin);
			// Two independent edits per ten classes, with no reordering: indices remain known identities.
			for (int i = 0; i < size; i += 10) {
				((EClass)right.getEClassifiers().get(i)).setAbstract(true);
				((EClass)left.getEClassifiers().get(i + 5)).setInterface(true);
			}
			Comparison comparison = match(left, right, origin);
			assertSame(origin, comparison.getMatch(left).getOrigin());
			assertSame(right, comparison.getMatch(left).getRight());
			for (int i = 0; i < size; i++) {
				Match matched = comparison.getMatch(origin.getEClassifiers().get(i));
				assertSame(left.getEClassifiers().get(i), matched.getLeft());
				assertSame(right.getEClassifiers().get(i), matched.getRight());
				assertSame(comparison.getMatch(origin), matched.eContainer());
			}
			new DefaultDiffEngine(new DiffBuilder()).diff(comparison, new BasicMonitor());
			// One abstract and one interface change per block of ten gives size / 5 differences.
			assertEquals(size / 5, comparison.getDifferences().size());
		}
	}

	/**
	 * Runs the content hash matcher directly, without an identifier-matching wrapper.
	 *
	 * @param left
	 *            the left-side input
	 * @param right
	 *            the right-side input
	 * @param origin
	 *            the ancestor-side input
	 * @return the resulting comparison
	 */
	private Comparison match(EObject left, EObject right, EObject origin) {
		DefaultMatchEngine engine = new DefaultMatchEngine(
				new ContentHashEObjectMatcher(weights, new ContentHasher(weights)),
				new DefaultComparisonFactory(new DefaultEqualityHelperFactory()));
		return engine.match(new DefaultComparisonScope(left, right, origin), new BasicMonitor());
	}

	/**
	 * Creates a class with the supplied semantic name.
	 *
	 * @param name
	 *            the name
	 * @return the named class
	 */
	private static EClass namedClass(String name) {
		EClass type = EcoreFactory.eINSTANCE.createEClass();
		type.setName(name);
		return type;
	}

	/**
	 * Creates a resource containing the supplied root.
	 *
	 * @param uri
	 *            the resource URI
	 * @param root
	 *            the resource root
	 * @return the populated resource
	 */
	private static Resource resource(String uri, EObject root) {
		Resource resource = new ResourceImpl(URI.createURI(uri));
		resource.getContents().add(root);
		return resource;
	}

	/**
	 * Declares an explicit resource correspondence for the matcher.
	 *
	 * @param comparison
	 *            the comparison in progress
	 * @param left
	 *            the left-side input
	 * @param right
	 *            the right-side input
	 */
	private static void recordResourceMatch(Comparison comparison, Resource left, Resource right) {
		MatchResource match = CompareFactory.eINSTANCE.createMatchResource();
		match.setLeft(left);
		match.setRight(right);
		comparison.getMatchedResources().add(match);
	}

	/**
	 * Creates an identified root with an anonymous child so identifier-first comparisons invoke fallback.
	 *
	 * @param uri
	 *            resource URI
	 * @param rootId
	 *            XMI ID overriding the root's common name
	 * @return the resource with its root and child
	 */
	private static Resource identifiedModel(String uri, String rootId) {
		XMIResourceImpl resource = new XMIResourceImpl(URI.createURI(uri));
		Node root = NodesFactory.eINSTANCE.createNode();
		root.setName(TYPE);
		root.getContainmentRef1().add(NodesFactory.eINSTANCE.createNode());
		resource.getContents().add(root);
		resource.setID(root, rootId);
		return resource;
	}

	/** Client specialization accepting similarity pairs that the default distance rejects. */
	private static final class PermissiveContentMatcher extends ContentHashEObjectMatcher {
		/** Feature weights used to construct the custom distance. */
		private final WeightProvider.Descriptor.Registry weights;

		/** Comparison-local matchers created through the protected factory. */
		private final List<CandidateMatcher> createdMatchers = new ArrayList<>();

		/** Matching passes dispatched to the client candidate matcher. */
		private int candidatePasses;

		/** Similarity pairs evaluated by the custom distance. */
		private int distanceChecks;

		/**
		 * Uses ordinary content snapshots with a custom candidate-distance policy.
		 *
		 * @param weights
		 *            the feature weights
		 */
		private PermissiveContentMatcher(WeightProvider.Descriptor.Registry weights) {
			super(weights, new ContentHasher(weights));
			this.weights = weights;
		}

		@Override
		protected CandidateMatcher createCandidateMatcher(Comparison comparison,
				Map<EObject, Fingerprint> fingerprintsByObject, EditionDistance editionDistance,
				Set<Match> parentsToProcess, Monitor monitor) {
			EditionDistance permissiveDistance = new EditionDistance(weights) {
				@Override
				public double distance(Comparison inProgress, EObject first, EObject second) {
					distanceChecks++;
					return 0;
				}
			};
			CandidateMatcher matcher = new CandidateMatcher(comparison, fingerprintsByObject,
					permissiveDistance, parentsToProcess, monitor) {
				@Override
				public void matchWithinGroups(List<EObject> left, List<EObject> right,
						List<EObject> origin, Function<EObject, CandidateGroupKey> groupingFunction,
						MatchingMode mode) {
					candidatePasses++;
					super.matchWithinGroups(left, right, origin, groupingFunction, mode);
				}
			};
			createdMatchers.add(matcher);
			return matcher;
		}
	}

	/** Cancels midway through capture and fails if the matcher requests the next object. */
	private static final class CancelAfterCaptureHasher extends ContentHasher {
		/** Monitor cancelled at the designated capture boundary. */
		private final BasicMonitor monitor;

		/** Last object allowed before cancellation. */
		private final EObject lastObject;

		/** First object that must not be requested after cancellation. */
		private final EObject firstObjectAfterCancellation;

		/**
		 * Creates a hasher that cancels at a boundary and rejects the following object.
		 *
		 * @param weights
		 *            feature weights used for capture
		 * @param monitor
		 *            monitor to cancel
		 * @param lastObject
		 *            object whose capture triggers cancellation
		 * @param firstObjectAfterCancellation
		 *            next object whose capture would prove cancellation was ignored
		 */
		private CancelAfterCaptureHasher(WeightProvider.Descriptor.Registry weights, BasicMonitor monitor,
				EObject lastObject, EObject firstObjectAfterCancellation) {
			super(weights);
			this.monitor = monitor;
			this.lastObject = lastObject;
			this.firstObjectAfterCancellation = firstObjectAfterCancellation;
		}

		@Override
		public Fingerprint fingerprint(EObject object) {
			if (object == firstObjectAfterCancellation) {
				fail("Capture continued after cancellation");
			}
			Fingerprint captured = super.fingerprint(object);
			if (object == lastObject) {
				monitor.setCanceled(true);
			}
			return captured;
		}
	}

	/** Implements case-insensitive class identity and records the objects passed by the matcher. */
	private static final class CaseInsensitiveNameHasher implements IContentHasher {
		/** Objects actually fingerprinted in this comparison. */
		private final List<EObject> fingerprintedObjects = new ArrayList<>();

		@Override
		public Fingerprint fingerprint(EObject object) {
			fingerprintedObjects.add(object);
			String name = ((EClass)object).getName().toLowerCase(Locale.ROOT);
			FeatureValue nameSnapshot = FeatureValue.of(EcorePackage.Literals.ENAMED_ELEMENT__NAME,
					Collections.singletonList(name));
			return new Fingerprint(name, Collections.singletonList(nameSnapshot), 0L);
		}
	}
}

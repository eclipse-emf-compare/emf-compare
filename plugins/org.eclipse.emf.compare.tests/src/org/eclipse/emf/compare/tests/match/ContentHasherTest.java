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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.match.eobject.WeightProvider;
import org.eclipse.emf.compare.match.eobject.WeightProviderDescriptorRegistryImpl;
import org.eclipse.emf.compare.match.hash.ContentHasher;
import org.eclipse.emf.compare.match.hash.FeatureValue;
import org.eclipse.emf.compare.match.hash.Fingerprint;
import org.eclipse.emf.compare.tests.fragmentation.data.FragmentationInputData;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodeMultiValueReference;
import org.eclipse.emf.compare.tests.nodes.NodeMultiValuedNonUniqueAttribute;
import org.eclipse.emf.compare.tests.nodes.NodeSingleValueAttribute;
import org.eclipse.emf.compare.tests.nodes.NodesFactory;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;
import org.eclipse.emf.compare.tests.req.data.ReqInputData;
import org.eclipse.emf.compare.tests.suite.AllTests;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.VisibilityKind;
import org.junit.Test;

/**
 * Verifies the matching evidence produced by {@link ContentHasher} before any object partners are selected.
 * The tests exercise the hasher directly on small Ecore, node and UML models, supplemented by reference and
 * controlled-resource fixtures where resource boundaries matter.
 * <p>
 * Snapshot cases check that captured content and deferred similarity survive model edits and cache resets.
 * They cover list mutation, mutable dates, binary values, sharing equal content and distinguishing colliding
 * hashes. A guarded model object rejects reads after capture, making a deferred live-model read fail at its
 * source. Scalar and optional-value cases check that stored changes affect the next snapshot while defaults
 * retain their intended meaning.
 * </p>
 * <p>
 * Reference cases distinguish exact target identity from similarity after a move, honor existing resource
 * matches, preserve proxy fragments and handle ambiguous URIs. A resource set that rejects resolution checks
 * single-valued, many-valued and feature-map references and containments; a controlled-resource fixture checks
 * that fingerprinting does not load a referenced fragment. Named subclasses exercise feature, anchor and
 * reference-segment customization.
 * </p>
 * <p>
 * Similarity expectations are relational: a small text edit must remain closer than unrelated text, child
 * similarity must contribute to its parent, and repeated values must not overwhelm another feature's weight.
 * These assertions check the selected examples without prescribing arbitrary 64-bit signatures or claiming
 * that similarity alone determines the correct object correspondence.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@SuppressWarnings("nls")
public class ContentHasherTest {

	/** Name of the class holding the tested reference. */
	private static final String SOURCE = "source";

	/** Name shared by reference targets in the move and qualification fixtures. */
	private static final String TARGET = "target";

	/** First distinct name or stored value in paired fixtures. */
	private static final String FIRST = "first";

	/** Second distinct name or stored value in paired fixtures. */
	private static final String SECOND = "second";

	/** URI of the left resource in correspondence fixtures. */
	private static final String LEFT_URI = "left.ecore";

	/** URI of the right resource in correspondence fixtures. */
	private static final String RIGHT_URI = "right.ecore";

	/** Class name used in copy and rename tests. */
	private static final String EXAMPLE = "Example";

	/** Common name of reference-owning classes. */
	private static final String ENTITY = "entity";

	/** Common name of superclasses in resource-key fixtures. */
	private static final String BASE = "base";

	/** Feature-weight providers for the current matcher. */
	private final WeightProvider.Descriptor.Registry weights = WeightProviderDescriptorRegistryImpl
			.createStandaloneInstance();

	/** An uncomputed signature must retain the old model values after an edit and cache reset. */
	@Test
	public void deferredSimilarityKeepsTheSnapshotAfterEditsAndReinitialization() {
		EPackage model = EcoreFactory.eINSTANCE.createEPackage();
		model.setName("original");
		EClass source = namedClass(SOURCE);
		EClass target = namedClass(TARGET);
		source.getESuperTypes().add(target);
		model.getEClassifiers().addAll(Arrays.asList(source, target));
		ContentHasher hasher = new ContentHasher(weights);
		long expected = new ContentHasher(weights).fingerprint(model).similarity();
		Fingerprint captured = hasher.fingerprint(model);

		// Leave captured.similarity() unevaluated until both the model and the hasher cache have changed.
		model.setName("changed");
		source.getESuperTypes().clear();
		target.setName("otherTarget");
		model.getEClassifiers().clear();
		hasher.initialize(CompareFactory.eINSTANCE.createComparison());
		assertEquals(expected, captured.similarity());
		assertNotEquals(expected, hasher.fingerprint(model).similarity());
	}

	/** Computing a deferred signature must use captured values only. */
	@Test
	public void deferredSimilarityDoesNotReadTheModelAfterCapture() {
		SnapshotGuardObject object = new SnapshotGuardObject();
		object.eSet(NodesPackage.Literals.NODE__NAME, "snapshot");
		Fingerprint snapshot = new ContentHasher(weights).fingerprint(object);
		object.snapshotTaken = true;

		// SnapshotGuardObject supplies the assertion: any later eGet fails during this computation.
		snapshot.similarity();
	}

	/** Equivalent objects share one immutable content snapshot to avoid duplicate allocations. */
	@Test
	public void equalObjectsShareTheirImmutableContent() {
		EClass first = namedClass("shared");
		EClass copy = EcoreUtil.copy(first);
		ContentHasher hasher = new ContentHasher(weights);
		assertSame(hasher.fingerprint(first).content(), hasher.fingerprint(copy).content());
	}

	/** Aa and BB collide as String hashes but must retain their distinct exact content. */
	@Test
	public void contentHashCollisionsDoNotShareDifferentSnapshots() {
		ContentHasher hasher = new ContentHasher(weights);
		Fingerprint first = hasher.fingerprint(namedClass("Aa"));
		Fingerprint collision = hasher.fingerprint(namedClass("BB"));
		assertEquals(first.content().hashCode(), collision.content().hashCode());
		assertNotEquals(first.content(), collision.content());
	}

	/** A minor edit must retain more similarity than an unrelated description. */
	@Test
	public void smallTextEditsKeepMoreSimilarityThanUnrelatedText() {
		assertSmallTextEditIsCloser(new TextOnlyHasher(weights));
	}

	/** A shared name must not drown the similarity contribution of the text attribute. */
	@Test
	public void nameFeatureDoesNotHideTextSimilarity() {
		assertSmallTextEditIsCloser(new ContentHasher(weights));
	}

	/**
	 * Compares one small text edit with an unrelated replacement using the supplied feature selection.
	 *
	 * @param hasher
	 *            the text-only or default hasher
	 */
	private void assertSmallTextEditIsCloser(ContentHasher hasher) {
		Fingerprint original = hasher
				.fingerprint(textNode("The model contains packages, classes, attributes and references."));
		Fingerprint edited = hasher
				.fingerprint(textNode("The model contains packages, classes, operations and references."));
		Fingerprint unrelated = hasher.fingerprint(
				textNode("A completely different description about weather forecasts and rainfall."));
		assertNotEquals(original.content(), edited.content());
		assertTrue("The small edit must keep more common signature bits than the unrelated replacement",
				similarityDistance(original, edited) < similarityDistance(original, unrelated));
	}

	/** A small child edit must leave its parent closer than replacing the child with unrelated content. */
	@Test
	public void parentSimilarityPreservesTheSimilarityOfItsChildren() {
		Node original = parentOf(
				textNode("The model contains packages, classes, attributes and references."));
		Node edited = parentOf(textNode("The model contains packages, classes, operations and references."));
		Node unrelated = parentOf(
				textNode("A completely different description about weather forecasts and rainfall."));
		ContentHasher hasher = new ContainmentOnlyHasher(weights);
		// The parent has no selected scalar features: only child evidence can distinguish these cases.
		Fingerprint first = hasher.fingerprint(original);
		Fingerprint changed = hasher.fingerprint(edited);
		Fingerprint other = hasher.fingerprint(unrelated);
		assertNotEquals(first.content(), changed.content());
		assertTrue(similarityDistance(first, changed) < similarityDistance(first, other));
	}

	/**
	 * Gives one child a parent whose content depends on that child.
	 *
	 * @param child
	 *            the contained node
	 * @return the new parent
	 */
	private static Node parentOf(Node child) {
		Node parent = NodesFactory.eINSTANCE.createNode();
		parent.getContainmentRef1().add(child);
		return parent;
	}

	/**
	 * Changing a reference between two named fixture objects must change its exact content.
	 *
	 * @throws IOException
	 *             if the fixture cannot be loaded or the comparison fails
	 */
	@Test
	public void referenceKeysDistinguishTheNamedTargetsInTheRequirementsFixture() throws IOException {
		AllTests.fillEMFRegistries();
		Resource right = new ReqInputData().getE5Right();
		Node root = (Node)right.getContents().get(0);
		NodeMultiValueReference source = (NodeMultiValueReference)root.getContainmentRef1().get(1).eContents()
				.get(0);
		Node destination = root.getContainmentRef1().get(2);
		source.getMultiValuedReference().clear();
		source.getMultiValuedReference().add(destination.getContainmentRef1().get(0));
		ContentHasher hasher = new ContentHasher(weights);
		Fingerprint first = hasher.fingerprint(source);
		Node otherTarget = destination.getContainmentRef1().get(1);
		assertNotEquals(source.getMultiValuedReference().get(0).getName(), otherTarget.getName());
		source.getMultiValuedReference().set(0, otherTarget);
		hasher.initialize(CompareFactory.eINSTANCE.createComparison());
		assertNotEquals("Different semantic reference targets must remain distinct", first.content(),
				hasher.fingerprint(source).content());
	}

	/** Moving the source preserves exact content; moving its target preserves local similarity. */
	@Test
	public void movingSourceKeepsExactContentAndMovingTargetKeepsSimilarity() {
		EPackage firstParent = EcoreFactory.eINSTANCE.createEPackage();
		firstParent.setName("firstParent");
		EPackage secondParent = EcoreFactory.eINSTANCE.createEPackage();
		secondParent.setName("secondParent");
		EClass source = namedClass(SOURCE);
		EClass target = namedClass(TARGET);
		source.getESuperTypes().add(target);
		firstParent.getEClassifiers().addAll(Arrays.asList(source, target));
		Resource resource = resource("source.ecore", firstParent);
		resource.getContents().add(secondParent);
		ContentHasher hasher = new ContentHasher(weights);
		Fingerprint original = hasher.fingerprint(source);

		// Moving the source leaves its referenced target unchanged, so exact content stays equal.
		resource("destination.ecore", source);
		hasher.initialize(CompareFactory.eINSTANCE.createComparison());
		assertEquals(original, hasher.fingerprint(source));

		// Moving the target changes its qualified path, but its local name remains the same.
		secondParent.getEClassifiers().add(target);
		hasher.initialize(CompareFactory.eINSTANCE.createComparison());
		assertEquals("Moving the target must preserve local similarity", original.similarity(),
				hasher.fingerprint(source).similarity());
	}

	/** Container names must distinguish otherwise identically named reference targets. */
	@Test
	public void exactReferenceKeysDistinguishSameNamedTargetsInDifferentContainers() {
		EPackage root = EcoreFactory.eINSTANCE.createEPackage();
		for (String name : new String[] {FIRST, SECOND }) {
			EPackage nested = EcoreFactory.eINSTANCE.createEPackage();
			nested.setName(name);
			nested.getEClassifiers().add(namedClass(TARGET));
			root.getESubpackages().add(nested);
		}
		EClass source = namedClass(SOURCE);
		root.getEClassifiers().add(source);
		source.getESuperTypes().add((EClass)root.getESubpackages().get(0).getEClassifiers().get(0));
		ContentHasher hasher = new ContentHasher(weights);
		Fingerprint first = hasher.fingerprint(source);
		source.getESuperTypes().set(0, (EClass)root.getESubpackages().get(1).getEClassifiers().get(0));
		hasher.initialize(CompareFactory.eINSTANCE.createComparison());
		assertNotEquals(first.content(), hasher.fingerprint(source).content());
	}

	/** Repeating one value changes exact content but leaves this fixture's similarity signature unchanged. */
	@Test
	public void repeatedValuesChangeExactContentButKeepTheSameSimilarity() {
		NodeMultiValuedNonUniqueAttribute small = NodesFactory.eINSTANCE
				.createNodeMultiValuedNonUniqueAttribute();
		small.setName("sameName");
		small.getMultiValuedAttribute().add("repeated value");
		NodeMultiValuedNonUniqueAttribute large = NodesFactory.eINSTANCE
				.createNodeMultiValuedNonUniqueAttribute();
		large.setName("sameName");
		large.getMultiValuedAttribute().addAll(Collections.nCopies(100, "repeated value"));
		ContentHasher hasher = new ContentHasher(weights);
		assertNotEquals(hasher.fingerprint(small).content(), hasher.fingerprint(large).content());
		// This checks duplicate-value normalization, not the weighting of arbitrary distinct values.
		assertEquals(hasher.fingerprint(small).similarity(), hasher.fingerprint(large).similarity());
	}

	/**
	 * Creates a text-bearing node with the same name in each similarity example.
	 *
	 * @param text
	 *            the captured text
	 * @return the text node
	 */
	private static NodeSingleValueAttribute textNode(String text) {
		NodeSingleValueAttribute node = NodesFactory.eINSTANCE.createNodeSingleValueAttribute();
		node.setName("Entry");
		node.setSingleValuedAttribute(text);
		return node;
	}

	/**
	 * Computes Hamming distance between two similarity signatures.
	 *
	 * @param first
	 *            the first fingerprint being compared
	 * @param second
	 *            the second fingerprint being compared
	 * @return the differing bit count
	 */
	private static int similarityDistance(Fingerprint first, Fingerprint second) {
		return Long.bitCount(first.similarity() ^ second.similarity());
	}

	/** A copy of a model object must have the same fingerprint as the original. */
	@Test
	public void equalModelsHaveEqualFingerprints() {
		EClass original = namedClass(EXAMPLE);
		ContentHasher hasher = new ContentHasher(weights);
		assertEquals(hasher.fingerprint(original), hasher.fingerprint(EcoreUtil.copy(original)));
	}

	/** Changing a stored name must change the next comparison snapshot. */
	@Test
	public void renamingChangesTheFingerprint() {
		EClass object = namedClass(EXAMPLE);
		ContentHasher hasher = new ContentHasher(weights);
		Fingerprint original = hasher.fingerprint(object);
		object.setName("Renamed");
		hasher.initialize(CompareFactory.eINSTANCE.createComparison());
		assertNotEquals(original, hasher.fingerprint(object));
	}

	/** Missing and empty names must not be used as semantic anchors. */
	@Test
	public void emptyOrMissingNamesHaveNoSemanticAnchor() {
		ContentHasher hasher = new ContentHasher(weights);
		assertNull(hasher.fingerprint(EcoreFactory.eINSTANCE.createEClass()).anchor());
		assertNull(hasher.fingerprint(namedClass("")).anchor());
	}

	/**
	 * The anchor preserves accents and supplementary characters; a copied object has the same fingerprint.
	 * Equality alone does not establish how Unicode text is encoded into similarity bits.
	 */
	@Test
	public void unicodeNamesArePreservedInAnchorsAndCopiedFingerprints() {
		String name = "Módèle_😀";
		EClass object = namedClass(name);
		ContentHasher hasher = new ContentHasher(weights);
		Fingerprint snapshot = hasher.fingerprint(object);
		assertEquals(name, snapshot.anchor());
		assertEquals(snapshot, hasher.fingerprint(EcoreUtil.copy(object)));
	}

	/** A feature-selection override must affect the captured content. */
	@Test
	public void customFeatureSelectionCanIgnoreNames() {
		EClass first = namedClass("FirstName");
		EClass second = namedClass("SecondName");
		ContentHasher hasher = new NameIgnoringHasher(weights);
		assertEquals(hasher.fingerprint(first).content(), hasher.fingerprint(second).content());
		ContentHasher defaults = new ContentHasher(weights);
		assertNotEquals(defaults.fingerprint(first).content(), defaults.fingerprint(second).content());
	}

	/** An anchor override must receive the object and determine its semantic anchor. */
	@Test
	public void customAnchorCanNormalizeTheObjectName() {
		ContentHasher hasher = new LowercaseAnchorHasher(weights);
		assertEquals("example", hasher.fingerprint(namedClass("EXAMPLE")).anchor());
	}

	/** A reference-key override can treat differently cased target names as the same semantic name. */
	@Test
	public void customReferenceKeysCanIgnoreNameCase() {
		EClass left = EcoreFactory.eINSTANCE.createEClass();
		EClass right = EcoreFactory.eINSTANCE.createEClass();
		EClass leftSuper = EcoreFactory.eINSTANCE.createEClass();
		EClass rightSuper = EcoreFactory.eINSTANCE.createEClass();
		leftSuper.setName("Base");
		rightSuper.setName("BASE");
		left.getESuperTypes().add(leftSuper);
		right.getESuperTypes().add(rightSuper);
		ContentHasher hasher = new LowercaseReferenceKeyHasher(weights);
		assertEquals(hasher.fingerprint(left).content(), hasher.fingerprint(right).content());
		ContentHasher defaults = new ContentHasher(weights);
		assertNotEquals(defaults.fingerprint(left).content(), defaults.fingerprint(right).content());
	}

	/** Matched resources identify equivalent reference targets even when their URIs differ. */
	@Test
	public void referencesUseMatchedResourcesDespiteDifferentURIs() {
		EClass left = namedClass(ENTITY);
		EClass right = namedClass(ENTITY);
		EClass leftSuper = namedClass(BASE);
		EClass rightSuper = namedClass(BASE);
		left.getESuperTypes().add(leftSuper);
		right.getESuperTypes().add(rightSuper);
		Comparison comparison = CompareFactory.eINSTANCE.createComparison();
		matchResources(comparison, resource(LEFT_URI, left), resource(RIGHT_URI, right));
		matchResources(comparison, resource("base-left.ecore", leftSuper),
				resource("base-right.ecore", rightSuper));
		ContentHasher hasher = new ContentHasher(weights);
		hasher.initialize(comparison);
		assertEquals(hasher.fingerprint(left), hasher.fingerprint(right));
	}

	/** An equal URI must not merge reference targets in resources declared unmatched. */
	@Test
	public void referencesToUnmatchedResourcesRemainDistinctWithEqualURIs() {
		EClass left = namedClass(ENTITY);
		EClass right = namedClass(ENTITY);
		EClass leftSuper = namedClass(BASE);
		EClass rightSuper = namedClass(BASE);
		left.getESuperTypes().add(leftSuper);
		right.getESuperTypes().add(rightSuper);
		Comparison comparison = CompareFactory.eINSTANCE.createComparison();
		matchResources(comparison, resource(LEFT_URI, left), resource(RIGHT_URI, right));
		matchResources(comparison, resource("base.ecore", leftSuper), null);
		// Separate one-sided mappings mean unmatched resources, despite their equal URI strings.
		matchResources(comparison, null, resource("base.ecore", rightSuper));
		ContentHasher hasher = new ContentHasher(weights);
		hasher.initialize(comparison);
		assertNotEquals(hasher.fingerprint(left).content(), hasher.fingerprint(right).content());
	}

	/** Resource matching normalizes a proxy's resource URI while preserving its target fragment. */
	@Test
	public void proxyReferencesUseMatchedResourceURIsAndKeepTheirFragments() {
		EClass left = namedClass(ENTITY);
		EClass right = namedClass(ENTITY);
		left.getESuperTypes().add(proxy("base-left.ecore#//base"));
		right.getESuperTypes().add(proxy("base-right.ecore#//base"));
		Comparison comparison = CompareFactory.eINSTANCE.createComparison();
		matchResources(comparison, resource(LEFT_URI, left), resource(RIGHT_URI, right));
		matchResources(comparison, resource("base-left.ecore", namedClass(BASE)),
				resource("base-right.ecore", namedClass(BASE)));
		ContentHasher hasher = new ContentHasher(weights);
		hasher.initialize(comparison);
		assertEquals(hasher.fingerprint(left), hasher.fingerprint(right));
		right.getESuperTypes().clear();
		right.getESuperTypes().add(proxy("base-right.ecore#//other"));
		hasher.initialize(comparison);
		assertNotEquals(hasher.fingerprint(left).content(), hasher.fingerprint(right).content());
	}

	/** Reading a single-valued reference must preserve its unresolved proxy. */
	@Test
	public void singleValuedReferencesDoNotResolveProxies() {
		assertProxyRemainsUnresolved(
				NodesPackage.Literals.NODE_SINGLE_VALUE_REFERENCE__SINGLE_VALUED_REFERENCE);
	}

	/** Reading a single-valued containment must preserve its unresolved proxy. */
	@Test
	public void singleValuedContainmentsDoNotResolveProxies() {
		assertProxyRemainsUnresolved(
				NodesPackage.Literals.NODE_SINGLE_VALUE_CONTAINMENT__SINGLE_VALUE_CONTAINMENT);
	}

	/** Reading a reference list must preserve its unresolved proxy. */
	@Test
	public void manyValuedReferencesDoNotResolveProxies() {
		assertProxyRemainsUnresolved(
				NodesPackage.Literals.NODE_MULTI_VALUE_REFERENCE__MULTI_VALUED_REFERENCE);
	}

	/** Reading a containment list must preserve its unresolved proxy. */
	@Test
	public void manyValuedContainmentsDoNotResolveProxies() {
		assertProxyRemainsUnresolved(NodesPackage.Literals.NODE__CONTAINMENT_REF1);
	}

	/** Reading a reference stored in a feature map must preserve its unresolved proxy. */
	@Test
	public void featureMapReferencesDoNotResolveProxies() {
		assertProxyRemainsUnresolved(NodesPackage.Literals.NODE_FEATURE_MAP_NON_CONTAINMENT__FIRST_KEY_NC);
	}

	/** Reading a containment stored in a feature map must preserve its unresolved proxy. */
	@Test
	public void featureMapContainmentsDoNotResolveProxies() {
		assertProxyRemainsUnresolved(NodesPackage.Literals.NODE_FEATURE_MAP_CONTAINMENT__FIRST_KEY);
	}

	/**
	 * Captures exact and deferred content while a resource set rejects proxy resolution.
	 *
	 * @param feature
	 *            the structural feature
	 */
	@SuppressWarnings("unchecked")
	private void assertProxyRemainsUnresolved(EStructuralFeature feature) {
		EObject owner = EcoreUtil.create(feature.getEContainingClass());
		InternalEObject proxy = (InternalEObject)NodesFactory.eINSTANCE.createNode();
		proxy.eSetProxyURI(URI.createURI("target.nodes#first"));
		if (feature.isMany()) {
			((List<EObject>)owner.eGet(feature, false)).add(proxy);
		} else {
			owner.eSet(feature, proxy);
		}
		Resource source = resource("source.nodes", owner);
		new NonResolvingResourceSet().getResources().add(source);
		InternalEObject container = proxy.eInternalContainer();
		ContentHasher hasher = new ContentHasher(weights);
		Fingerprint original = hasher.fingerprint(owner);
		original.similarity();
		assertSame(container, proxy.eInternalContainer());
		// Read without resolution here too, so the assertion itself cannot change the fixture.
		Object stored = owner.eGet(feature, false);
		Object target = stored;
		if (feature.isMany()) {
			target = ((InternalEList<?>)stored).basicGet(0);
		}
		assertSame(proxy, target);
		assertEquals(URI.createURI("target.nodes#first"), proxy.eProxyURI());
		assertEquals(1, source.getResourceSet().getResources().size());

		// Non-resolution must not be achieved by ignoring the reference altogether.
		proxy.eSetProxyURI(URI.createURI("target.nodes#second"));
		hasher.initialize(CompareFactory.eINSTANCE.createComparison());
		assertNotEquals("Proxy fragments must remain significant", original.content(),
				hasher.fingerprint(owner).content());
	}

	/** Changing a target's proxy-owner URI must not change its local similarity or resolve the owner. */
	@Test
	public void referenceSimilarityIgnoresContainerProxyLocationsWithoutResolvingThem() {
		EClass owner = namedClass("owner");
		EClass referenced = namedClass("referenced");
		EPackage parent = EcoreFactory.eINSTANCE.createEPackage();
		((InternalEObject)parent).eSetProxyURI(URI.createURI("target.ecore#first"));
		parent.getEClassifiers().add(referenced);
		owner.getESuperTypes().add(referenced);
		Resource source = resource("source.ecore", owner);
		source.getContents().add(parent);
		new NonResolvingResourceSet().getResources().add(source);
		ContentHasher hasher = new ContentHasher(weights);
		Fingerprint original = hasher.fingerprint(owner);
		assertSame(parent, ((InternalEObject)referenced).eInternalContainer());
		((InternalEObject)parent).eSetProxyURI(URI.createURI("target.ecore#second"));
		hasher.initialize(CompareFactory.eINSTANCE.createComparison());
		assertEquals(original.similarity(), hasher.fingerprint(owner).similarity());
	}

	/**
	 * Capturing exact content and computing similarity must not load the controlled child resource.
	 *
	 * @throws IOException
	 *             if the fixture cannot be loaded or the comparison fails
	 */
	@Test
	public void fingerprintingDoesNotLoadControlledFragments() throws IOException {
		AllTests.fillEMFRegistries();
		Resource source = new FragmentationInputData().getControlLeft();
		EObject root = source.getContents().get(0);
		InternalEList<?> children = (InternalEList<?>)root.eGet(NodesPackage.Literals.NODE__CONTAINMENT_REF1,
				false);
		EObject proxy = (EObject)children.basicGet(0);
		URI uri = ((InternalEObject)proxy).eProxyURI();
		// Establish that loading the fixture has not already resolved the controlled child.
		assertTrue(proxy.eIsProxy());
		assertEquals(1, source.getResourceSet().getResources().size());

		new ContentHasher(weights).fingerprint(root).similarity();
		assertEquals(1, source.getResourceSet().getResources().size());
		assertSame(proxy, children.basicGet(0));
		assertEquals(uri, ((InternalEObject)proxy).eProxyURI());
		assertSame(root, ((InternalEObject)proxy).eInternalContainer());
	}

	/** An ambiguous resource URI must not assign a proxy to an arbitrary matched resource pair. */
	@Test
	public void ambiguousProxyResourceURIsAreNotNormalizedToAnArbitraryPair() {
		EClass left = namedClass(ENTITY);
		EClass right = namedClass(ENTITY);
		left.getESuperTypes().add(proxy("shared.ecore#//base"));
		Comparison comparison = CompareFactory.eINSTANCE.createComparison();
		matchResources(comparison, resource("shared.ecore", namedClass(BASE)),
				resource("one.ecore", namedClass(BASE)));
		matchResources(comparison, resource("other.ecore", namedClass(BASE)),
				resource("shared.ecore", namedClass(BASE)));
		ContentHasher hasher = new ContentHasher(weights);

		// Reject both possible groups: checking only one would allow a first-wins or last-wins bug.
		for (String unambiguousURI : Arrays.asList("one.ecore", "other.ecore")) {
			right.getESuperTypes().clear();
			right.getESuperTypes().add(proxy(unambiguousURI + "#//base"));
			hasher.initialize(comparison);
			assertNotEquals(hasher.fingerprint(left).content(), hasher.fingerprint(right).content());
		}
	}

	/** Without resource correspondence, equal resource URIs identify equivalent reference targets. */
	@Test
	public void referenceKeysUseURIsWhenResourcesHaveNoMatches() {
		EClass left = namedClass(ENTITY);
		EClass right = namedClass(ENTITY);
		EClass leftSuper = namedClass(BASE);
		EClass rightSuper = namedClass(BASE);
		left.getESuperTypes().add(leftSuper);
		right.getESuperTypes().add(rightSuper);
		resource("same.ecore", leftSuper);
		Resource rightResource = resource("same.ecore", rightSuper);
		ContentHasher hasher = new ContentHasher(weights);
		hasher.initialize(CompareFactory.eINSTANCE.createComparison());
		assertEquals(hasher.fingerprint(left), hasher.fingerprint(right));
		rightResource.setURI(URI.createURI("different.ecore"));
		hasher.initialize(CompareFactory.eINSTANCE.createComparison());
		assertNotEquals(hasher.fingerprint(left).content(), hasher.fingerprint(right).content());
	}

	/**
	 * Creates an unresolved class reference with the given URI.
	 *
	 * @param uri
	 *            the resource URI
	 * @return the proxy
	 */
	private static EClass proxy(String uri) {
		EClass proxy = EcoreFactory.eINSTANCE.createEClass();
		((InternalEObject)proxy).eSetProxyURI(URI.createURI(uri));
		return proxy;
	}

	/** A stored boolean change must contribute to the UML fingerprint. */
	@Test
	public void changingAUMLBooleanChangesTheFingerprint() {
		org.eclipse.uml2.uml.Class type = UMLFactory.eINSTANCE.createClass();
		ContentHasher hasher = new ContentHasher(weights);
		Fingerprint original = hasher.fingerprint(type);
		type.setIsAbstract(true);
		hasher.initialize(CompareFactory.eINSTANCE.createComparison());
		assertNotEquals(original, hasher.fingerprint(type));
	}

	/** A stored enumeration change must contribute to the UML fingerprint. */
	@Test
	public void changingAUMLEnumChangesTheFingerprint() {
		org.eclipse.uml2.uml.Class type = UMLFactory.eINSTANCE.createClass();
		ContentHasher hasher = new ContentHasher(weights);
		Fingerprint original = hasher.fingerprint(type);
		type.setVisibility(VisibilityKind.PRIVATE_LITERAL);
		hasher.initialize(CompareFactory.eINSTANCE.createComparison());
		assertNotEquals(original, hasher.fingerprint(type));
	}

	/** Adding then removing an optional contained value must restore the original UML content. */
	@Test
	public void removingAUMLOptionalValueRestoresTheOriginalFingerprint() {
		org.eclipse.uml2.uml.Class type = UMLFactory.eINSTANCE.createClass();
		ContentHasher hasher = new ContentHasher(weights);
		Fingerprint original = hasher.fingerprint(type);
		type.setNameExpression(UMLFactory.eINSTANCE.createStringExpression());
		hasher.initialize(CompareFactory.eINSTANCE.createComparison());
		assertNotEquals(original, hasher.fingerprint(type));
		type.setNameExpression(null);
		hasher.initialize(CompareFactory.eINSTANCE.createComparison());
		assertEquals(original, hasher.fingerprint(type));
	}

	/** An in-place date edit must not mutate or corrupt an already interned exact snapshot. */
	@Test
	public void mutableDateEditsLeaveCapturedContentAndHashesUnchanged() {
		Date date = new Date(1000);
		EObject object = scalarObject(EcorePackage.Literals.EDATE, date);
		EStructuralFeature feature = object.eClass().getEStructuralFeature("payload");
		EObject unchanged = new DynamicEObjectImpl(object.eClass());
		unchanged.eSet(feature, new Date(1000));
		ContentHasher hasher = new ContentHasher(weights);
		Fingerprint captured = hasher.fingerprint(object);
		Fingerprint shared = hasher.fingerprint(unchanged);
		Fingerprint expected = new ContentHasher(weights).fingerprint(unchanged);
		int hashBeforeEdit = captured.hashCode();
		assertSame(captured.content(), shared.content());

		date.setTime(2000);

		assertEquals(expected, captured);
		assertEquals(expected, shared);
		assertEquals(hashBeforeEdit, captured.hashCode());
		// A fresh capture must still notice the edit; an always-constant snapshot would be incorrect.
		hasher.initialize(CompareFactory.eINSTANCE.createComparison());
		assertNotEquals(captured.content(), hasher.fingerprint(object).content());
	}

	/** Binary content is compared by value, and an in-place edit cannot alter the old snapshot. */
	@Test
	public void equalByteArraysHaveEqualStableFingerprints() {
		byte[] bytes = new byte[] {1, 2, 3 };
		EObject object = scalarObject(EcorePackage.Literals.EBYTE_ARRAY, bytes);
		EObject copy = new DynamicEObjectImpl(object.eClass());
		copy.eSet(object.eClass().getEStructuralFeature("payload"), bytes.clone());
		Fingerprint expected = new ContentHasher(weights).fingerprint(copy);
		ContentHasher hasher = new ContentHasher(weights);
		Fingerprint captured = hasher.fingerprint(object);
		assertEquals(expected, captured);

		bytes[0] = 4;

		assertEquals(expected, captured);
		hasher.initialize(CompareFactory.eINSTANCE.createComparison());
		assertNotEquals(captured.content(), hasher.fingerprint(object).content());
	}

	/** Scalar normalization must not turn a default value into evidence for a numbered rename. */
	@Test
	public void capturedScalarDefaultsAreNotDiscriminating() {
		EObject object = scalarObject(EcorePackage.Literals.EBOOLEAN, false);
		Fingerprint captured = new ContentHasher(weights).fingerprint(object);
		// False is the EBoolean default: it is captured, but cannot disambiguate otherwise similar objects.
		assertEquals(1, captured.content().size());
		assertFalse(captured.content().get(0).isDiscriminating());
	}

	/**
	 * Creates one dynamic object with a single scalar attribute, without unrelated matching setup.
	 *
	 * @param datatype
	 *            the payload datatype
	 * @param value
	 *            the initial scalar value
	 * @return the object whose payload can be edited directly
	 */
	private static EObject scalarObject(EDataType datatype, Object value) {
		EPackage schema = EcoreFactory.eINSTANCE.createEPackage();
		schema.setNsURI("test:scalar-snapshot");
		EClass type = namedClass("holder");
		schema.getEClassifiers().add(type);
		EAttribute payload = EcoreFactory.eINSTANCE.createEAttribute();
		payload.setName("payload");
		payload.setEType(datatype);
		type.getEStructuralFeatures().add(payload);
		EObject object = new DynamicEObjectImpl(type);
		object.eSet(payload, value);
		return object;
	}

	/** Changing a live model list must not change the exact values captured before that edit. */
	@Test
	public void capturedContentDoesNotChangeWhenTheSourceListChanges() {
		EPackage schema = EcoreFactory.eINSTANCE.createEPackage();
		schema.setNsURI("test:snapshot");
		EClass type = namedClass("holder");
		schema.getEClassifiers().add(type);
		EAttribute payload = EcoreFactory.eINSTANCE.createEAttribute();
		payload.setName("payload");
		payload.setEType(EcorePackage.Literals.EJAVA_OBJECT);
		// A single-valued Object attribute deliberately holds a mutable Java list, not an EMF many-list.
		type.getEStructuralFeatures().add(payload);
		EObject object = new DynamicEObjectImpl(type);
		List<String> values = new ArrayList<>(Arrays.asList(FIRST, SECOND));
		object.eSet(payload, values);
		ContentHasher hasher = new ContentHasher(weights);
		Fingerprint original = hasher.fingerprint(object);
		List<FeatureValue> expected = Collections
				.singletonList(FeatureValue.of(payload, Arrays.asList(FIRST, SECOND)));
		values.add("third");
		assertEquals(expected, original.content());
		hasher.initialize(CompareFactory.eINSTANCE.createComparison());
		assertNotEquals(expected, hasher.fingerprint(object).content());
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
	 * Declares which two resources represent the same resource across comparison sides.
	 *
	 * @param comparison
	 *            the comparison in progress
	 * @param left
	 *            the left-side input
	 * @param right
	 *            the right-side input
	 */
	private static void matchResources(Comparison comparison, Resource left, Resource right) {
		MatchResource match = CompareFactory.eINSTANCE.createMatchResource();
		match.setLeft(left);
		match.setRight(right);
		comparison.getMatchedResources().add(match);
	}

	/** Fails if a deferred signature reads a model object after capture. */
	private static final class SnapshotGuardObject extends DynamicEObjectImpl {
		/** Whether fingerprint capture is complete. */
		private boolean snapshotTaken;

		/** Creates a dynamic Node whose reads can be rejected after fingerprint capture. */
		private SnapshotGuardObject() {
			super(NodesPackage.Literals.NODE);
		}

		@Override
		public Object eGet(EStructuralFeature feature, boolean resolve) {
			assertTrue("Deferred similarity read the live model", !snapshotTaken);
			return super.eGet(feature, resolve);
		}
	}

	/** Selects the text attribute alone to check its contribution without a name feature. */
	private static final class TextOnlyHasher extends ContentHasher {
		/**
		 * Selects text-only feature weights.
		 *
		 * @param weights
		 *            the feature weights
		 */
		private TextOnlyHasher(WeightProvider.Descriptor.Registry weights) {
			super(weights);
		}

		@Override
		protected List<EStructuralFeature> featuresOf(EClass type) {
			return Collections.singletonList(
					NodesPackage.Literals.NODE_SINGLE_VALUE_ATTRIBUTE__SINGLE_VALUED_ATTRIBUTE);
		}
	}

	/** Isolates the propagation of child signatures to their owner. */
	private static final class ContainmentOnlyHasher extends ContentHasher {
		/**
		 * Creates the hasher with the supplied feature weights.
		 *
		 * @param weights
		 *            the feature weights
		 */
		private ContainmentOnlyHasher(WeightProvider.Descriptor.Registry weights) {
			super(weights);
		}

		@Override
		protected List<EStructuralFeature> featuresOf(EClass type) {
			if (type == NodesPackage.Literals.NODE) {
				return Collections.singletonList(NodesPackage.Literals.NODE__CONTAINMENT_REF1);
			}
			return super.featuresOf(type);
		}
	}

	/** Keeps normal stored features except the class name. */
	private static final class NameIgnoringHasher extends ContentHasher {
		/**
		 * Creates the custom feature selector.
		 *
		 * @param weights
		 *            the feature weights
		 */
		private NameIgnoringHasher(WeightProvider.Descriptor.Registry weights) {
			super(weights);
		}

		@Override
		protected List<EStructuralFeature> featuresOf(EClass type) {
			List<EStructuralFeature> features = new ArrayList<>(super.featuresOf(type));
			features.remove(EcorePackage.Literals.ENAMED_ELEMENT__NAME);
			return features;
		}
	}

	/** Normalizes semantic anchors without changing the stored exact content. */
	private static final class LowercaseAnchorHasher extends ContentHasher {
		/**
		 * Creates the custom anchor policy.
		 *
		 * @param weights
		 *            the feature weights
		 */
		private LowercaseAnchorHasher(WeightProvider.Descriptor.Registry weights) {
			super(weights);
		}

		@Override
		protected String anchor(EObject object) {
			String name = super.anchor(object);
			if (name == null) {
				return null;
			}
			return name.toLowerCase(Locale.ROOT);
		}
	}

	/** Normalizes reference names for the selected Ecore objects. */
	private static final class LowercaseReferenceKeyHasher extends ContentHasher {
		/**
		 * Creates the hasher with the supplied feature weights.
		 *
		 * @param weights
		 *            the feature weights
		 */
		private LowercaseReferenceKeyHasher(WeightProvider.Descriptor.Registry weights) {
			super(weights);
		}

		@Override
		protected boolean contributesCustomReferenceSegment(EObject object) {
			return object instanceof EClass;
		}

		@Override
		protected String getCustomKeyForElement(EObject object) {
			return ((EClass)object).getName().toLowerCase(Locale.ROOT);
		}
	}

	/** Rejects proxy resolution in fingerprinting regressions. */
	private static final class NonResolvingResourceSet extends ResourceSetImpl {
		@Override
		public EObject getEObject(URI uri, boolean loadOnDemand) {
			throw new AssertionError("Fingerprinting attempted to resolve " + uri);
		}
	}

}

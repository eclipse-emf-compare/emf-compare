/**
 * Copyright (c) 2009, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *     Stephen McCants - Initial API and implementation
 */
package org.eclipse.emf.compare.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.merge.service.MergeService;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.tests.external.ExternalFactory;
import org.eclipse.emf.compare.tests.external.Holder;
import org.eclipse.emf.compare.tests.external.NoncontainmentHolder;
import org.eclipse.emf.compare.tests.external.StringHolder;
import org.eclipse.emf.compare.tests.nodes.Group;
import org.eclipse.emf.compare.tests.nodes.Leaf;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodesFactory;
import org.eclipse.emf.compare.tests.nonemf.NonEMFStringHolder;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * This is a set of generic tests to test EMFCompare and make sure it is working okay. The reason I'm using
 * generic tests is so I can share them with the EMFCompare authors if needed.
 * 
 * @author smccants
 */
public class MergeTests extends TestCase {
	/**
	 * Can we find a changed reference correctly? This is the reference changing, not just the contents of the
	 * reference.
	 * 
	 * @throws InterruptedException
	 */
	public void testNonContainmentReferenceChange() throws InterruptedException {
		// Build the left holder
		ResourceSet holderResourceSet = new ResourceSetImpl();

		// Makes the URI Similarity not cause problems
		Resource holder1Resource = holderResourceSet.createResource(URI
				.createFileURI("/tmp/holder1asdfasdfasdfasdfasdfasdfadsfasdfasdfasdfasdfasdfasdfasdf"));
		StringHolder holder1 = ExternalFactory.eINSTANCE.createStringHolder();
		holder1.setName("holder1");
		holder1Resource.getContents().add(holder1);

		// Build the right holder
		Resource holder2Resource = holderResourceSet.createResource(URI.createFileURI("/tmp/holder2"));
		StringHolder holder2 = ExternalFactory.eINSTANCE.createStringHolder();
		holder2.setName("holder2");
		holder2Resource.getContents().add(holder2);

		// Build the left and right models
		UUID uuid = UUID.randomUUID();
		ResourceSet nodeResourceSet1 = new ResourceSetImpl();
		Resource nodeResource1 = nodeResourceSet1.createResource(URI
				.createFileURI("/tmp/referenceChange1adsf.nodes"));
		ResourceSet nodeResourceSet2 = new ResourceSetImpl();
		Resource nodeResource2 = nodeResourceSet2.createResource(URI
				.createFileURI("/tmp/referenceChange1.nodes"));
		Leaf leaf1 = NodesFactory.eINSTANCE.createLeaf();
		leaf1.setName("leaf");
		leaf1.setUuid(uuid);
		leaf1.setNoncontainmentHolder(holder1);
		nodeResource1.getContents().add(leaf1);

		Leaf leaf2 = NodesFactory.eINSTANCE.createLeaf();
		leaf2.setName("leaf");
		leaf2.setUuid(uuid);
		leaf2.setNoncontainmentHolder(holder2);
		nodeResource2.getContents().add(leaf2);

		// Do the merge test
		MatchResourceSet match = MatchService.doResourceSetMatch(leaf1.eResource().getResourceSet(), leaf2
				.eResource().getResourceSet(), Collections.<String, Object> emptyMap());
		assertTrue(match.getUnmatchedModels().isEmpty());
		DiffResourceSet diff = DiffService.doDiff(match, false);

		for (DiffModel resourceDiff : diff.getDiffModels()) {
			List<DiffElement> differences = new ArrayList<DiffElement>(resourceDiff.getOwnedElements());
			UpdateReference referenceUpdate = (UpdateReference)differences.get(0).getSubDiffElements().get(0)
					.getSubDiffElements().get(0);
			assertEquals(holder2, referenceUpdate.getLeftTarget());
			assertEquals(holder1, referenceUpdate.getRightTarget());
			MergeService.merge(differences, false);
		}

		// Check results
		assertEquals(leaf1.getNoncontainmentHolder(), holder2);
	}

	/**
	 * Can we find a changed reference correctly? Here the contents of the reference change, however the
	 * reference doesn't stay in it's original resource.
	 * 
	 * @throws InterruptedException
	 */
	public void testContainmentReferenceChange() throws InterruptedException {
		// Build the left holder
		ResourceSet holderResourceSet = new ResourceSetImpl();
		Resource holder1Resource = holderResourceSet.createResource(URI.createFileURI("/tmp/holder1"));
		StringHolder holder1 = ExternalFactory.eINSTANCE.createStringHolder();
		holder1.setName("holder1");
		holder1Resource.getContents().add(holder1);

		// Build the right holder
		Resource holder2Resource = holderResourceSet.createResource(URI.createFileURI("/tmp/holder2"));
		StringHolder holder2 = ExternalFactory.eINSTANCE.createStringHolder();
		holder2.setName("holder2");
		holder2Resource.getContents().add(holder2);

		// Build the left and right models
		UUID uuid = UUID.randomUUID();
		ResourceSet nodeResourceSet1 = new ResourceSetImpl();
		Resource nodeResource1 = nodeResourceSet1.createResource(URI
				.createFileURI("/tmp/referenceChange1adsf.nodes"));
		ResourceSet nodeResourceSet2 = new ResourceSetImpl();
		Resource nodeResource2 = nodeResourceSet2.createResource(URI
				.createFileURI("/tmp/referenceChange1.nodes"));
		Leaf leaf1 = NodesFactory.eINSTANCE.createLeaf();
		leaf1.setName("leaf");
		leaf1.setUuid(uuid);
		leaf1.setContainmentHolder(holder1);
		nodeResource1.getContents().add(leaf1);

		Leaf leaf2 = NodesFactory.eINSTANCE.createLeaf();
		leaf2.setName("leaf");
		leaf2.setUuid(uuid);
		leaf2.setContainmentHolder(holder2);
		nodeResource2.getContents().add(leaf2);

		// Do the merge test
		// merge(leaf1, leaf2);
		MatchResourceSet match = MatchService.doResourceSetMatch(leaf1.eResource().getResourceSet(), leaf2
				.eResource().getResourceSet(), Collections.<String, Object> emptyMap());
		assertTrue(match.getUnmatchedModels().isEmpty());
		DiffResourceSet diff = DiffService.doDiff(match, false);

		for (DiffModel resourceDiff : diff.getDiffModels()) {
			List<DiffElement> differences = new ArrayList<DiffElement>(resourceDiff.getOwnedElements());
			MergeService.merge(differences, false);
		}

		// Check results
		assertEquals(holder2.getName(), leaf1.getContainmentHolder().getName());
	}

	/**
	 * Make sure we can add a new leaf correctly.
	 * 
	 * @throws InterruptedException
	 */
	public void testAddNewLeaf() throws InterruptedException {
		// Build the non-containment holders
		ResourceSet holderResourceSet = new ResourceSetImpl();
		// Makes the URI Similarity not cause problems
		Resource holder1Resource = holderResourceSet.createResource(URI
				.createFileURI("/tmp/holder1asdfasdfasdfasdfasdfasdfadsfasdfasdfasdfasdfasdfasdfasdf"));
		StringHolder noncontainmentHolder1 = ExternalFactory.eINSTANCE.createStringHolder();
		noncontainmentHolder1.setName("non-containment holder1111111111111111111"); // Extra ones to make sure
																					// name comparison fails
		holder1Resource.getContents().add(noncontainmentHolder1);

		Resource holder2Resource = holderResourceSet.createResource(URI.createFileURI("/tmp/holder2"));
		StringHolder noncontainmentHolder2 = ExternalFactory.eINSTANCE.createStringHolder();
		noncontainmentHolder2.setName("non-containment holder2222222222222222222");
		holder2Resource.getContents().add(noncontainmentHolder2);

		// Build containment holders
		StringHolder containmentHolder1 = ExternalFactory.eINSTANCE.createStringHolder();
		containmentHolder1.setName("containment holder 1111111111111111111");
		StringHolder containmentHolder1v2 = ExternalFactory.eINSTANCE.createStringHolder();
		containmentHolder1v2.setName("containment holder 1111111111111111111");
		StringHolder containmentHolder2 = ExternalFactory.eINSTANCE.createStringHolder();
		containmentHolder2.setName("containment holder 2222222222222222222");

		UUID leaf1UUID = UUID.randomUUID();
		UUID groupUUID = UUID.randomUUID();
		// Build the left model
		ResourceSet nodeResourceSet1 = new ResourceSetImpl();
		Resource nodeResource1 = nodeResourceSet1.createResource(URI
				.createFileURI("/tmp/referenceChange2.nodes"));
		// Build leaf 1
		Leaf leaf1v1 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v1.setName("leaf 1");
		leaf1v1.setUuid(leaf1UUID);
		leaf1v1.setContainmentHolder(containmentHolder1);
		leaf1v1.setNoncontainmentHolder(noncontainmentHolder1);
		leaf1v1.setNumber(100);
		// Build group
		Group groupv1 = NodesFactory.eINSTANCE.createGroup();
		groupv1.setName("group");
		groupv1.setUuid(groupUUID);
		groupv1.getChildren().add(leaf1v1);
		// Put it in the resource
		nodeResource1.getContents().add(groupv1);

		// Build the right model
		ResourceSet nodeResourceSet2 = new ResourceSetImpl();
		Resource nodeResource2 = nodeResourceSet2.createResource(URI
				.createFileURI("/tmp/referenceChange1.nodes"));
		// Build the second version of leaf1
		Leaf leaf1v2 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v2.setName("leaf 1");
		leaf1v2.setUuid(leaf1UUID);
		leaf1v2.setContainmentHolder(containmentHolder1v2);
		leaf1v2.setNoncontainmentHolder(noncontainmentHolder1);
		leaf1v2.setNumber(100);
		// We will add leaf 2
		Leaf leaf2v1 = NodesFactory.eINSTANCE.createLeaf();
		leaf2v1.setName("leaf 2");
		leaf2v1.setUuid(UUID.randomUUID());
		leaf2v1.setContainmentHolder(containmentHolder2);
		leaf2v1.setNoncontainmentHolder(noncontainmentHolder2);
		leaf2v1.setNumber(200);
		// Build the group (v2)
		Group groupv2 = NodesFactory.eINSTANCE.createGroup();
		groupv2.setName("group");
		groupv2.setUuid(groupUUID);
		groupv2.getChildren().add(leaf1v2);
		groupv2.getChildren().add(leaf2v1);
		nodeResource2.getContents().add(groupv2);

		// Do the merge test
		MatchResourceSet match = MatchService.doResourceSetMatch(groupv1.eResource().getResourceSet(),
				groupv2.eResource().getResourceSet(), Collections.<String, Object> emptyMap());
		assertTrue(match.getUnmatchedModels().isEmpty());
		DiffResourceSet diff = DiffService.doDiff(match, false);

		for (DiffModel resourceDiff : diff.getDiffModels()) {
			List<DiffElement> differences = new ArrayList<DiffElement>(resourceDiff.getOwnedElements());
			MergeService.merge(differences, false);
		}

		// Check results
		assertEquals(groupv1.getChildren().size(), 2);
		for (Node node : groupv1.getChildren()) {
			if (node == leaf1v1) {
				assertEquals(containmentHolder1.getName(), leaf1v1.getContainmentHolder().getName());
				assertEquals(noncontainmentHolder1.getName(), leaf1v1.getNoncontainmentHolder().getName());
				assertEquals(100, leaf1v1.getNumber());
			} else if (node instanceof Leaf) {
				Leaf newLeaf = (Leaf)node;
				assertNotNull(newLeaf.getContainmentHolder());
				assertEquals(containmentHolder2.getName(), newLeaf.getContainmentHolder().getName());
				assertNotNull(newLeaf.getNoncontainmentHolder());
				assertEquals(noncontainmentHolder2.getName(), newLeaf.getNoncontainmentHolder().getName());
				assertEquals(200, newLeaf.getNumber());
			} else
				fail("Found something we didn't expect:\n" + node);
		}
	}

	/**
	 * Can we find a changed attribute correctly?
	 * 
	 * @throws InterruptedException
	 */
	public void testAttributeChange() throws InterruptedException {
		// Build the containment holder
		StringHolder holder1Left = ExternalFactory.eINSTANCE.createStringHolder();
		holder1Left.setName("holder1");
		StringHolder holder1Right = ExternalFactory.eINSTANCE.createStringHolder();
		holder1Right.setName("holder1");

		// Build the non-containment holder
		ResourceSet holderResourceSet = new ResourceSetImpl();
		Resource holder2Resource = holderResourceSet.createResource(URI.createFileURI("/tmp/holder2"));
		StringHolder holder2 = ExternalFactory.eINSTANCE.createStringHolder();
		holder2.setName("holder2");
		holder2Resource.getContents().add(holder2);

		// Build the left and right models
		UUID uuid = UUID.randomUUID();
		ResourceSet nodeResourceSet1 = new ResourceSetImpl();
		Resource nodeResource1 = nodeResourceSet1.createResource(URI
				.createFileURI("/tmp/referenceChange1.nodes"));
		ResourceSet nodeResourceSet2 = new ResourceSetImpl();
		Resource nodeResource2 = nodeResourceSet2.createResource(URI
				.createFileURI("/tmp/referenceChange2.nodes"));
		Leaf leaf1 = NodesFactory.eINSTANCE.createLeaf();
		leaf1.setName("leaf");
		leaf1.setUuid(uuid);
		leaf1.setContainmentHolder(holder1Left);
		leaf1.setNoncontainmentHolder(holder2);
		leaf1.setNumber(100);
		nodeResource1.getContents().add(leaf1);

		Leaf leaf2 = NodesFactory.eINSTANCE.createLeaf();
		leaf2.setName("leaf");
		leaf2.setUuid(uuid);
		leaf2.setContainmentHolder(holder1Right);
		leaf2.setNoncontainmentHolder(holder2);
		leaf2.setNumber(200);
		nodeResource2.getContents().add(leaf2);

		// Do the merge test
		MatchResourceSet match = MatchService.doResourceSetMatch(leaf1.eResource().getResourceSet(), leaf2
				.eResource().getResourceSet(), Collections.<String, Object> emptyMap());
		assertTrue(match.getUnmatchedModels().isEmpty());
		DiffResourceSet diff = DiffService.doDiff(match, false);

		for (DiffModel resourceDiff : diff.getDiffModels()) {
			List<DiffElement> differences = new ArrayList<DiffElement>(resourceDiff.getOwnedElements());
			MergeService.merge(differences, false);
		}

		// Check results
		assertEquals(holder1Right.getName(), leaf1.getContainmentHolder().getName());
		assertEquals(holder2.getName(), leaf1.getNoncontainmentHolder().getName());
		assertEquals(200, leaf1.getNumber());
	}

	/**
	 * Adds a group with two leafs under it to an existing group. This is a three way matching test.
	 * 
	 * @throws InterruptedException
	 */
	public void testAddPopulatedGroup() throws InterruptedException {
		// Resources and Sets
		UUID uuid = UUID.randomUUID();
		ResourceSet nodeResourceSetOrig = new ResourceSetImpl();
		Resource nodeResourceOrig = nodeResourceSetOrig.createResource(URI
				.createFileURI("/tmp/referenceChange0.nodes"));
		ResourceSet nodeResourceSet1 = new ResourceSetImpl();
		Resource nodeResource1 = nodeResourceSet1.createResource(URI
				.createFileURI("/tmp/referenceChange1.nodes"));
		ResourceSet nodeResourceSet2 = new ResourceSetImpl();
		Resource nodeResource2 = nodeResourceSet2.createResource(URI
				.createFileURI("/tmp/referenceChange2.nodes"));

		// Build original model
		Group group1v0 = NodesFactory.eINSTANCE.createGroup();
		group1v0.setName("group 1");
		group1v0.setUuid(uuid);
		nodeResourceOrig.getContents().add(group1v0);

		// Build the left model
		Group group1v1 = NodesFactory.eINSTANCE.createGroup();
		group1v1.setName("group 1");
		group1v1.setUuid(uuid);
		nodeResource1.getContents().add(group1v1);

		// Build the right model
		Group group1v2 = NodesFactory.eINSTANCE.createGroup();
		group1v2.setName("group 1");
		group1v2.setUuid(group1v1.getUuid());
		nodeResource2.getContents().add(group1v2);
		Group group2v2 = NodesFactory.eINSTANCE.createGroup();
		group2v2.setName("group 2");
		group2v2.setUuid(UUID.randomUUID());
		group1v2.getChildren().add(group2v2);
		Leaf leaf1v2 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v2.setName("leaf 1");
		leaf1v2.setUuid(UUID.randomUUID());
		group2v2.getChildren().add(leaf1v2);
		Leaf leaf2v2 = NodesFactory.eINSTANCE.createLeaf();
		leaf2v2.setName("leaf 2");
		leaf2v2.setUuid(UUID.randomUUID());
		group2v2.getChildren().add(leaf2v2);

		// Do the merge test
		MatchResourceSet match = MatchService.doResourceSetMatch(group1v1.eResource().getResourceSet(),
				group1v2.eResource().getResourceSet(), group1v0.eResource().getResourceSet(),
				Collections.<String, Object> emptyMap());
		DiffResourceSet diff = DiffService.doDiff(match, true);

		for (DiffModel resourceDiff : diff.getDiffModels()) {
			List<DiffElement> differences = new ArrayList<DiffElement>(resourceDiff.getOwnedElements());
			MergeService.merge(differences, false);
		}

		// Check results
		assertEquals(1, group1v1.getChildren().size());
		assertTrue(group1v1.getChildren().get(0) instanceof Group);
		assertEquals(2, ((Group)group1v1.getChildren().get(0)).getChildren().size());
		for (Node node : ((Group)group1v1.getChildren().get(0)).getChildren()) {
			assertTrue(node instanceof Leaf);
		}
	}

	/**
	 * Two version with a simple parent each have one additional leaf. After the merge, the merged model
	 * should have both leaves.
	 * 
	 * @throws InterruptedException
	 */
	public void testNonConflictingAdds() throws InterruptedException {
		// Resources and Sets
		UUID uuid = UUID.randomUUID();
		ResourceSet nodeResourceSetOrig = new ResourceSetImpl();
		Resource nodeResourceOrig = nodeResourceSetOrig.createResource(URI
				.createFileURI("/tmp/referenceChange0.nodes"));
		ResourceSet nodeResourceSet1 = new ResourceSetImpl();
		Resource nodeResource1 = nodeResourceSet1.createResource(URI
				.createFileURI("/tmp/referenceChange1.nodes"));
		ResourceSet nodeResourceSet2 = new ResourceSetImpl();
		Resource nodeResource2 = nodeResourceSet2.createResource(URI
				.createFileURI("/tmp/referenceChange2.nodes"));

		// Build original model
		Group group1v0 = NodesFactory.eINSTANCE.createGroup();
		group1v0.setName("group 1");
		group1v0.setUuid(uuid);
		nodeResourceOrig.getContents().add(group1v0);

		// Build the left model
		Group group1v1 = NodesFactory.eINSTANCE.createGroup();
		group1v1.setName("group 1");
		group1v1.setUuid(uuid);
		nodeResource1.getContents().add(group1v1);
		Leaf leaf1v1 = NodesFactory.eINSTANCE.createLeaf();
		String leaf1Name = "leaf 1";
		leaf1v1.setName(leaf1Name);
		leaf1v1.setUuid(UUID.randomUUID());
		group1v1.getChildren().add(leaf1v1);

		// Build the right model
		Group group1v2 = NodesFactory.eINSTANCE.createGroup();
		group1v2.setName("group 1");
		group1v2.setUuid(uuid);
		nodeResource2.getContents().add(group1v2);
		Leaf leaf2v2 = NodesFactory.eINSTANCE.createLeaf();
		String leaf2Name = "leaf 2";
		leaf2v2.setName(leaf2Name);
		leaf2v2.setUuid(UUID.randomUUID());
		group1v2.getChildren().add(leaf2v2);

		// Do the merge test
		MatchResourceSet match = MatchService.doResourceSetMatch(group1v1.eResource().getResourceSet(),
				group1v2.eResource().getResourceSet(), group1v0.eResource().getResourceSet(),
				Collections.<String, Object> emptyMap());
		DiffResourceSet diff = DiffService.doDiff(match, true);

		for (DiffModel resourceDiff : diff.getDiffModels()) {
			List<DiffElement> differences = new ArrayList<DiffElement>(resourceDiff.getDifferences());
			List<DiffElement> remoteDifferences = new ArrayList<DiffElement>(differences.size());
			// Merging right to left, so hide the local changes from the merge service or the merge service
			// will delete them!
			for (DiffElement difference : differences) {
				if (difference.isRemote())
					remoteDifferences.add(difference);
			}
			MergeService.merge(remoteDifferences, false);
		}

		// Check results
		assertEquals(2, group1v1.getChildren().size());
		boolean foundLeaf1 = false;
		boolean foundLeaf2 = false;
		for (Node node : group1v1.getChildren()) {
			assertTrue(node instanceof Leaf);
			if (leaf1Name.equals(node.getName())) {
				if (foundLeaf1)
					fail("Found leaf 1 twice!");
				foundLeaf1 = true;
			} else if (leaf2Name.equals(node.getName())) {
				if (foundLeaf2)
					fail("Found leaf 2 twice!");
				foundLeaf2 = true;
			} else
				fail("Found an unexpected leaf node named '" + node.getName() + "'.");
		}
		assertTrue(foundLeaf1);
		assertTrue(foundLeaf2);
	}

	/**
	 * We have a original model with a leaf node. The two derivative models have non-conflicting changes to
	 * the leaf node. It should merge correctly without conflict. Problem this test completely misses the
	 * change to the non-containment reference. EMF Compare Defect #295616
	 * (https://bugs.eclipse.org/bugs/show_bug.cgi?id=295616) HDWB Task #5058
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void testNonConflictingAlterations() throws InterruptedException, IOException {
		final String tmpFolder = getTemporaryDataAbsolutePath();

		// Build the non-containment holders
		ResourceSet holderResourceSet = new ResourceSetImpl();
		Resource holder1Resource = holderResourceSet.createResource(URI.createFileURI(tmpFolder
				+ "/holder1asdfasdfasdfasdfasdfasdfasfdasdfasdfasfdasdfasdfasdfasdfasdf")); // Makes the URI
																							// Similarity not
																							// cause problems
		StringHolder nonContainmentHolder1 = ExternalFactory.eINSTANCE.createStringHolder();
		nonContainmentHolder1.setName("non holder1");
		holder1Resource.getContents().add(nonContainmentHolder1);
		holder1Resource.save(null);
		Resource holder2Resource = holderResourceSet
				.createResource(URI.createFileURI(tmpFolder + "/holder2")); // Makes the URI Similarity not
																			// cause problems
		StringHolder nonContainmentHolder2 = ExternalFactory.eINSTANCE.createStringHolder();
		nonContainmentHolder2.setName("non holder2");
		holder2Resource.getContents().add(nonContainmentHolder2);
		holder2Resource.save(null);
		// Build a containment holder
		StringHolder containmentHolder1v0 = ExternalFactory.eINSTANCE.createStringHolder();
		containmentHolder1v0.setName("holder1");
		StringHolder containmentHolder1v2 = ExternalFactory.eINSTANCE.createStringHolder();
		containmentHolder1v2.setName("holder1");
		StringHolder containmentHolder2 = ExternalFactory.eINSTANCE.createStringHolder();
		containmentHolder2.setName("holder2");

		// Resources and Sets
		UUID uuid = UUID.randomUUID();
		ResourceSet nodeResourceSetOrig = new ResourceSetImpl();
		Resource nodeResourceOrig = nodeResourceSetOrig.createResource(URI.createFileURI(tmpFolder
				+ "/referenceChange0.nodes"));
		ResourceSet nodeResourceSet1 = new ResourceSetImpl();
		Resource nodeResource1 = nodeResourceSet1.createResource(URI.createFileURI(tmpFolder
				+ "/referenceChange1.nodes"));
		ResourceSet nodeResourceSet2 = new ResourceSetImpl();
		Resource nodeResource2 = nodeResourceSet2.createResource(URI.createFileURI(tmpFolder
				+ "/referenceChange2.nodes"));

		// Build original model
		Group group1v0 = NodesFactory.eINSTANCE.createGroup();
		group1v0.setName("group 1");
		group1v0.setUuid(uuid);
		nodeResourceOrig.getContents().add(group1v0);
		Leaf leaf1v0 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v0.setName("leaf 1");
		leaf1v0.setNumber(10);
		leaf1v0.setContainmentHolder(containmentHolder1v0);
		StringHolder nonContainmentHolder1v0 = (StringHolder)nodeResourceSetOrig
				.getResource(nonContainmentHolder1.eResource().getURI(), true).getContents().get(0);
		leaf1v0.setNoncontainmentHolder(nonContainmentHolder1v0);
		leaf1v0.setUuid(UUID.randomUUID());
		group1v0.getChildren().add(leaf1v0);

		// Build the left model
		Group group1v1 = NodesFactory.eINSTANCE.createGroup();
		group1v1.setName("group 1");
		group1v1.setUuid(uuid);
		nodeResource1.getContents().add(group1v1);
		Leaf leaf1v1 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v1.setName("leaf 1");
		leaf1v1.setNumber(20);
		leaf1v1.setContainmentHolder(containmentHolder2);
		StringHolder nonContainmentHolder1v1 = (StringHolder)nodeResourceSet1
				.getResource(nonContainmentHolder1.eResource().getURI(), true).getContents().get(0);
		leaf1v1.setNoncontainmentHolder(nonContainmentHolder1v1);
		leaf1v1.setUuid(leaf1v0.getUuid());
		group1v1.getChildren().add(leaf1v1);

		// Build the right model
		Group group1v2 = NodesFactory.eINSTANCE.createGroup();
		group1v2.setName("group 1");
		group1v2.setUuid(uuid);
		nodeResource2.getContents().add(group1v2);
		Leaf leaf1v2 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v2.setName("leaf 2");
		leaf1v2.setNumber(10);
		leaf1v2.setContainmentHolder(containmentHolder1v2);
		StringHolder nonContainmentHolder2v2 = (StringHolder)nodeResourceSet2
				.getResource(nonContainmentHolder2.eResource().getURI(), true).getContents().get(0);
		leaf1v2.setNoncontainmentHolder(nonContainmentHolder2v2);
		leaf1v2.setUuid(leaf1v0.getUuid());
		group1v2.getChildren().add(leaf1v2);

		// Do the merge test
		MatchResourceSet match = MatchService.doResourceSetMatch(group1v1.eResource().getResourceSet(),
				group1v2.eResource().getResourceSet(), group1v0.eResource().getResourceSet(),
				Collections.<String, Object> emptyMap());
		DiffResourceSet diff = DiffService.doDiff(match, true);

		for (DiffModel resourceDiff : diff.getDiffModels()) {
			List<DiffElement> differences = new ArrayList<DiffElement>(resourceDiff.getDifferences());
			List<DiffElement> remoteDifferences = new ArrayList<DiffElement>(differences.size());
			// Merging right to left, so hide the left changes from the merge service or the merge service
			// will undo them!
			for (DiffElement difference : differences) {
				if (difference.isRemote())
					remoteDifferences.add(difference);
			}
			MergeService.merge(remoteDifferences, false);
		}

		// Check results
		assertEquals(1, group1v1.getChildren().size());
		assertTrue(group1v1.getChildren().get(0) instanceof Leaf);
		Leaf leaf = (Leaf)group1v1.getChildren().get(0);
		assertTrue(leaf.getName().equals("leaf 2"));
		assertTrue(leaf.getContainmentHolder().getName().equals("holder2"));
		assertTrue(leaf.getNoncontainmentHolder().getName().equals("non holder2"));
		assertTrue(leaf.getNumber() == 20);
	}

	/**
	 * We start out with two children under the main node and then delete one node in the left model and one
	 * node in the right model. The merged model should have both deletes.
	 * 
	 * @throws InterruptedException
	 */
	public void testNonConflictingDeletes() throws InterruptedException {
		// Resources and Sets
		UUID uuid = UUID.randomUUID();
		ResourceSet nodeResourceSetOrig = new ResourceSetImpl();
		Resource nodeResourceOrig = nodeResourceSetOrig.createResource(URI
				.createFileURI("/tmp/referenceChange0.nodes"));
		ResourceSet nodeResourceSet1 = new ResourceSetImpl();
		Resource nodeResource1 = nodeResourceSet1.createResource(URI
				.createFileURI("/tmp/referenceChange1.nodes"));
		ResourceSet nodeResourceSet2 = new ResourceSetImpl();
		Resource nodeResource2 = nodeResourceSet2.createResource(URI
				.createFileURI("/tmp/referenceChange2.nodes"));

		// Build original model
		Group group1v0 = NodesFactory.eINSTANCE.createGroup();
		group1v0.setName("group 1");
		group1v0.setUuid(uuid);
		nodeResourceOrig.getContents().add(group1v0);
		Leaf leaf1v0 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v0.setName("leaf 1");
		leaf1v0.setUuid(UUID.randomUUID());
		group1v0.getChildren().add(leaf1v0);
		Leaf leaf2v0 = NodesFactory.eINSTANCE.createLeaf();
		leaf2v0.setName("leaf 2");
		leaf2v0.setUuid(UUID.randomUUID());
		group1v0.getChildren().add(leaf2v0);

		// Build the left model
		Group group1v1 = NodesFactory.eINSTANCE.createGroup();
		group1v1.setName("group 1");
		group1v1.setUuid(uuid);
		nodeResource1.getContents().add(group1v1);
		Leaf leaf1v1 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v1.setName("leaf 1");
		leaf1v1.setUuid(leaf1v0.getUuid());
		group1v1.getChildren().add(leaf1v1);

		// Build the right model
		Group group1v2 = NodesFactory.eINSTANCE.createGroup();
		group1v2.setName("group 1");
		group1v2.setUuid(uuid);
		nodeResource2.getContents().add(group1v2);
		Leaf leaf2v2 = NodesFactory.eINSTANCE.createLeaf();
		leaf2v2.setName("leaf 2");
		leaf2v2.setUuid(leaf2v0.getUuid());
		group1v2.getChildren().add(leaf2v2);

		// Do the merge test
		MatchResourceSet match = MatchService.doResourceSetMatch(group1v1.eResource().getResourceSet(),
				group1v2.eResource().getResourceSet(), group1v0.eResource().getResourceSet(),
				Collections.<String, Object> emptyMap());
		DiffResourceSet diff = DiffService.doDiff(match, true);

		for (DiffModel resourceDiff : diff.getDiffModels()) {
			List<DiffElement> differences = new ArrayList<DiffElement>(resourceDiff.getDifferences());
			List<DiffElement> remoteDifferences = new ArrayList<DiffElement>(differences.size());
			// Merging right to left, so hide the local changes from the merge service or the merge service
			// will undo them!
			for (DiffElement difference : differences) {
				if (difference.isRemote())
					remoteDifferences.add(difference);
			}
			MergeService.merge(remoteDifferences, false);
		}

		// Check results
		assertEquals(0, group1v1.getChildren().size());
	}

	/**
	 * This test results in things being removed that shouldn't be.
	 * 
	 * @throws InterruptedException
	 */
	public void testRemoteAddBeforeRemoteRemove() throws InterruptedException {
		// Build a regular holder
		Holder holder1v0 = ExternalFactory.eINSTANCE.createHolder();
		StringHolder holderHolder1v0 = ExternalFactory.eINSTANCE.createStringHolder();
		holderHolder1v0.setName("holderHolder1");
		holder1v0.setStringHolder(holderHolder1v0);
		Holder holder1v1 = ExternalFactory.eINSTANCE.createHolder();
		StringHolder holderHolder1v1 = ExternalFactory.eINSTANCE.createStringHolder();
		holderHolder1v1.setName("holderHolder1");
		holder1v1.setStringHolder(holderHolder1v1);
		Holder holder1v2 = ExternalFactory.eINSTANCE.createHolder();
		StringHolder holderHolder1v2 = ExternalFactory.eINSTANCE.createStringHolder();
		holderHolder1v2.setName("holderHolder1adsfasdfasdfasdf");
		holder1v2.setStringHolder(holderHolder1v2);

		// Resources and Sets
		UUID uuid = UUID.randomUUID();
		ResourceSet nodeResourceSetOrig = new ResourceSetImpl();
		Resource nodeResourceOrig = nodeResourceSetOrig.createResource(URI
				.createFileURI("/tmp/referenceChange0.nodes"));
		ResourceSet nodeResourceSet1 = new ResourceSetImpl();
		Resource nodeResource1 = nodeResourceSet1.createResource(URI
				.createFileURI("/tmp/referenceChange1.nodes"));
		ResourceSet nodeResourceSet2 = new ResourceSetImpl();
		Resource nodeResource2 = nodeResourceSet2.createResource(URI
				.createFileURI("/tmp/referenceChange2.nodes"));

		// Build original model
		Group group1v0 = NodesFactory.eINSTANCE.createGroup();
		group1v0.setName("group 1");
		group1v0.setUuid(uuid);
		nodeResourceOrig.getContents().add(group1v0);
		Leaf leaf1v0 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v0.setName("leaf 1");
		leaf1v0.setNumber(10);
		leaf1v0.setHolder(holder1v0);
		leaf1v0.setUuid(UUID.randomUUID());
		group1v0.getChildren().add(leaf1v0);

		// Build the left model
		Group group1v1 = NodesFactory.eINSTANCE.createGroup();
		group1v1.setName("group 1");
		group1v1.setUuid(uuid);
		nodeResource1.getContents().add(group1v1);
		Leaf leaf1v1 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v1.setName("leaf 1");
		leaf1v1.setNumber(10);
		leaf1v1.setHolder(holder1v1);
		leaf1v1.setUuid(leaf1v0.getUuid());
		group1v1.getChildren().add(leaf1v1);

		// Build the right model
		Group group1v2 = NodesFactory.eINSTANCE.createGroup();
		group1v2.setName("group 1");
		group1v2.setUuid(uuid);
		nodeResource2.getContents().add(group1v2);
		Leaf leaf1v2 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v2.setName("leaf 1");
		leaf1v2.setNumber(10);
		leaf1v2.setHolder(holder1v2);
		leaf1v2.setUuid(leaf1v0.getUuid());
		group1v2.getChildren().add(leaf1v2);

		// Do the merge test
		MatchResourceSet match = MatchService.doResourceSetMatch(group1v1.eResource().getResourceSet(),
				group1v2.eResource().getResourceSet(), group1v0.eResource().getResourceSet(),
				Collections.<String, Object> emptyMap());
		DiffResourceSet diff = DiffService.doDiff(match, true);

		for (DiffModel resourceDiff : diff.getDiffModels()) {
			List<DiffElement> differences = new ArrayList<DiffElement>(resourceDiff.getDifferences());
			List<DiffElement> remoteDifferences = new ArrayList<DiffElement>(differences.size());
			// Merging right to left, so hide the local changes from the merge service or the merge service
			// will undo them!
			for (DiffElement difference : differences) {
				if (difference.isRemote())
					remoteDifferences.add(difference);
			}
			// Reverse the order
			// DiffElement temp = remoteDifferences.get(0);
			// remoteDifferences.set(0, remoteDifferences.get(1));
			// remoteDifferences.set(1, temp);
			// Merge
			MergeService.merge(remoteDifferences, false);
		}

		assertEquals("holderHolder1adsfasdfasdfasdf", leaf1v1.getHolder().getStringHolder().getName());
	}

	/**
	 * Here we have a containment non-EMF attribute set to null on "remote" copy. Eclipse Bug #300004
	 * 
	 * @throws InterruptedException
	 */
	public void testNullDifference() throws InterruptedException {
		// Build non-EMF String Holders
		NonEMFStringHolder holder1v0 = new NonEMFStringHolder();
		holder1v0.setString("non-EMF Holder 1");
		NonEMFStringHolder holder1v1 = new NonEMFStringHolder();
		holder1v1.setString("non-EMF Holder 1");

		// Resources and Sets
		UUID uuid = UUID.randomUUID();
		ResourceSet nodeResourceSetOrig = new ResourceSetImpl();
		Resource nodeResourceOrig = nodeResourceSetOrig.createResource(URI
				.createFileURI("/tmp/referenceChange0.nodes"));
		ResourceSet nodeResourceSet1 = new ResourceSetImpl();
		Resource nodeResource1 = nodeResourceSet1.createResource(URI
				.createFileURI("/tmp/referenceChange1.nodes"));
		ResourceSet nodeResourceSet2 = new ResourceSetImpl();
		Resource nodeResource2 = nodeResourceSet2.createResource(URI
				.createFileURI("/tmp/referenceChange2.nodes"));

		// Build original model
		Group group1v0 = NodesFactory.eINSTANCE.createGroup();
		group1v0.setName("group 1");
		group1v0.setUuid(uuid);
		nodeResourceOrig.getContents().add(group1v0);
		Leaf leaf1v0 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v0.setName("leaf 1");
		leaf1v0.setNumber(10);
		leaf1v0.setNonEMF(holder1v0);
		leaf1v0.setUuid(UUID.randomUUID());
		group1v0.getChildren().add(leaf1v0);

		// Build the left model
		Group group1v1 = NodesFactory.eINSTANCE.createGroup();
		group1v1.setName("group 1");
		group1v1.setUuid(uuid);
		nodeResource1.getContents().add(group1v1);
		Leaf leaf1v1 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v1.setName("leaf 1");
		leaf1v1.setNumber(10);
		leaf1v1.setNonEMF(holder1v1);
		leaf1v1.setUuid(leaf1v0.getUuid());
		group1v1.getChildren().add(leaf1v1);

		// Build the right model
		Group group1v2 = NodesFactory.eINSTANCE.createGroup();
		group1v2.setName("group 1");
		group1v2.setUuid(uuid);
		nodeResource2.getContents().add(group1v2);
		Leaf leaf1v2 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v2.setName("leaf 1");
		leaf1v2.setNumber(10);
		leaf1v2.setUuid(leaf1v0.getUuid());
		group1v2.getChildren().add(leaf1v2);

		// Do the merge test
		MatchResourceSet match = MatchService.doResourceSetMatch(group1v1.eResource().getResourceSet(),
				group1v2.eResource().getResourceSet(), group1v0.eResource().getResourceSet(),
				Collections.<String, Object> emptyMap());
		DiffResourceSet diff = DiffService.doDiff(match, true);
		assertTrue(diff.getDiffModels().size() > 0);

		for (DiffModel resourceDiff : diff.getDiffModels()) {
			List<DiffElement> differences = new ArrayList<DiffElement>(resourceDiff.getDifferences());
			List<DiffElement> remoteDifferences = new ArrayList<DiffElement>(differences.size());
			// Merging right to left, so hide the local changes from the merge service or the merge service
			// will undo them!
			for (DiffElement difference : differences) {
				if (difference.isRemote()) // Comment out this line and the test passes
					remoteDifferences.add(difference);
			}
			// Merge
			MergeService.merge(remoteDifferences, false);
		}

		assertNull(leaf1v1.getNonEMF());
	}

	/**
	 * Here we have a containment non-EMF attribute set to null on "remote" copy.
	 * 
	 * @throws InterruptedException
	 */
	public void testNullDifference2() throws InterruptedException {
		// Build non-EMF String Holders
		NonEMFStringHolder holder1v0 = new NonEMFStringHolder();
		holder1v0.setString("non-EMF Holder 1");
		NonEMFStringHolder holder2v0 = new NonEMFStringHolder();
		holder2v0.setString("non-EMF Holder 2.22222222");
		NonEMFStringHolder holder1v1 = new NonEMFStringHolder();
		holder1v1.setString("non-EMF Holder 1");
		NonEMFStringHolder holder2v1 = new NonEMFStringHolder();
		holder2v1.setString("non-EMF Holder 2.22222222");
		NonEMFStringHolder holder1v2 = new NonEMFStringHolder();
		NonEMFStringHolder holder2v2 = new NonEMFStringHolder();

		// Resources and Sets
		UUID uuid = UUID.randomUUID();
		ResourceSet nodeResourceSetOrig = new ResourceSetImpl();
		Resource nodeResourceOrig = nodeResourceSetOrig.createResource(URI
				.createFileURI("/tmp/referenceChange0.nodes"));
		ResourceSet nodeResourceSet1 = new ResourceSetImpl();
		Resource nodeResource1 = nodeResourceSet1.createResource(URI
				.createFileURI("/tmp/referenceChange1.nodes"));
		ResourceSet nodeResourceSet2 = new ResourceSetImpl();
		Resource nodeResource2 = nodeResourceSet2.createResource(URI
				.createFileURI("/tmp/referenceChange2.nodes"));

		// Build original model
		Group group1v0 = NodesFactory.eINSTANCE.createGroup();
		group1v0.setName("group 1");
		group1v0.setUuid(uuid);
		nodeResourceOrig.getContents().add(group1v0);
		Leaf leaf1v0 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v0.setName("leaf 1");
		leaf1v0.setNumber(10);
		leaf1v0.setNonEMF(holder1v0);
		leaf1v0.setUuid(UUID.randomUUID());
		group1v0.getChildren().add(leaf1v0);
		Leaf leaf2v0 = NodesFactory.eINSTANCE.createLeaf();
		leaf2v0.setName("leaf 2");
		leaf2v0.setNumber(20);
		leaf2v0.setNonEMF(holder2v0);
		leaf2v0.setUuid(UUID.randomUUID());
		group1v0.getChildren().add(leaf2v0);

		// Build the left model
		Group group1v1 = NodesFactory.eINSTANCE.createGroup();
		group1v1.setName("group 1");
		group1v1.setUuid(uuid);
		nodeResource1.getContents().add(group1v1);
		Leaf leaf1v1 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v1.setName("leaf 1");
		leaf1v1.setNumber(10);
		leaf1v1.setNonEMF(holder1v1);
		leaf1v1.setUuid(UUID.fromString(leaf1v0.getUuid().toString()));
		group1v1.getChildren().add(leaf1v1);
		Leaf leaf2v1 = NodesFactory.eINSTANCE.createLeaf();
		leaf2v1.setName("leaf 2");
		leaf2v1.setNumber(20);
		leaf2v1.setNonEMF(holder2v1);
		leaf2v1.setUuid(UUID.fromString(leaf2v0.getUuid().toString()));
		group1v1.getChildren().add(leaf2v1);

		// Build the right model
		Group group1v2 = NodesFactory.eINSTANCE.createGroup();
		group1v2.setName("group 1");
		group1v2.setUuid(uuid);
		nodeResource2.getContents().add(group1v2);
		Leaf leaf1v2 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v2.setName("leaf 1");
		leaf1v2.setNumber(10);
		leaf1v2.setNonEMF(holder1v2);
		leaf1v2.setUuid(UUID.fromString(leaf1v0.getUuid().toString()));
		group1v2.getChildren().add(leaf1v2);
		Leaf leaf2v2 = NodesFactory.eINSTANCE.createLeaf();
		leaf2v2.setName("leaf 2");
		leaf2v2.setNumber(20);
		leaf2v2.setNonEMF(holder2v2);
		leaf2v2.setUuid(UUID.fromString(leaf2v0.getUuid().toString()));
		group1v2.getChildren().add(leaf2v2);

		// Do the merge test
		MatchResourceSet match = MatchService.doResourceSetMatch(group1v1.eResource().getResourceSet(),
				group1v2.eResource().getResourceSet(), group1v0.eResource().getResourceSet(),
				Collections.<String, Object> emptyMap());
		DiffResourceSet diff = DiffService.doDiff(match, true);
		assertTrue(diff.getDiffModels().size() > 0);

		for (DiffModel resourceDiff : diff.getDiffModels()) {
			List<DiffElement> differences = new ArrayList<DiffElement>(resourceDiff.getDifferences());
			List<DiffElement> remoteDifferences = new ArrayList<DiffElement>(differences.size());
			// Merging right to left, so hide the local changes from the merge service or the merge service
			// will undo them!
			for (DiffElement difference : differences) {
				if (difference.isRemote()) // Comment out this line and the test passes
					remoteDifferences.add(difference);
			}
			// Merge
			MergeService.merge(remoteDifferences, false);
		}
		// Check
		assertNull(leaf2v1.getNonEMF().getString());
		assertNull(leaf1v1.getNonEMF().getString());
	}

	/**
	 * This test tries to reproduce a problem where a nest change to containment references causes a
	 * non-containment reference to disappear. This is a two way comparison, not a three way.
	 * 
	 * @throws InterruptedException
	 */
	public void testDisappearingNoncontainmentReference() throws InterruptedException {
		// Resources and Sets
		ResourceSet nodeResourceSet1 = new ResourceSetImpl();
		Resource nodeResource1 = nodeResourceSet1.createResource(URI
				.createFileURI("/tmp/referenceChange1.nodes"));
		ResourceSet nodeResourceSet2 = new ResourceSetImpl();
		Resource nodeResource2 = nodeResourceSet2.createResource(URI
				.createFileURI("/tmp/referenceChange2.nodes"));

		// Build the left holder
		ResourceSet nonHolderResourceSet = new ResourceSetImpl();
		// Makes the URI Similarity not cause problems
		Resource nonHolder1Resource = nonHolderResourceSet.createResource(URI
				.createFileURI("/tmp/holder1asdfasdfasdfasdfasdfasdfadsfasdfasdfasdfasdfasdfasdfasdf"));
		StringHolder nonHolder1 = ExternalFactory.eINSTANCE.createStringHolder();
		String nonHolderName = "non-holder1";
		nonHolder1.setName(nonHolderName);
		nonHolder1Resource.getContents().add(nonHolder1);

		// Build contained holders
		StringHolder holder1v0 = ExternalFactory.eINSTANCE.createStringHolder();
		holder1v0.setName("holder 1");
		StringHolder holder1v1 = ExternalFactory.eINSTANCE.createStringHolder();
		String name2 = "something completely different";
		holder1v1.setName(name2);
		// Build the noncontainment holder
		NoncontainmentHolder noncontainmentHolder1v0 = ExternalFactory.eINSTANCE.createNoncontainmentHolder();
		NoncontainmentHolder noncontainmentHolder1v1 = ExternalFactory.eINSTANCE.createNoncontainmentHolder();
		noncontainmentHolder1v0.setStringHolder(nonHolder1);
		noncontainmentHolder1v1.setStringHolder(nonHolder1);
		// The code commented out below makes this unit test pass
		// Resource nonHolder2Resource =
		// nonHolderResourceSet.createResource(URI.createFileURI("/tmp/holder1asdfasdfasdfasdfasdfasdfa")); //
		// Makes the URI Similarity not cause problems
		// Resource nonHolder3Resource =
		// nonHolderResourceSet.createResource(URI.createFileURI("/tmp/holder1asdfasdfasdfkjljlkjlkjlkjlkjsdfasdfasdfasdf"));
		// // Makes the URI Similarity not cause problems
		// HolderHolder holderHolder0 = ExternalFactory.eINSTANCE.createHolderHolder();
		// HolderHolder holderHolder1 = ExternalFactory.eINSTANCE.createHolderHolder();
		// holderHolder0.setHolder(noncontainmentHolder1v0);
		// holderHolder1.setHolder(noncontainmentHolder1v1);
		// nonHolder2Resource.getContents().add(holderHolder0);
		// nonHolder3Resource.getContents().add(holderHolder1);

		// Build the initial (left) model.
		Leaf leaf1v0 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v0.setName("leaf 1");
		leaf1v0.setNoncontainmentNoncontainmentHolder(noncontainmentHolder1v0);
		leaf1v0.setContainmentHolder(holder1v0);
		leaf1v0.setUuid(UUID.randomUUID());
		nodeResource1.getContents().add(leaf1v0);

		// Build the new (right) model.
		Leaf leaf1v1 = NodesFactory.eINSTANCE.createLeaf();
		leaf1v1.setName("leaf 1");
		leaf1v1.setNoncontainmentNoncontainmentHolder(noncontainmentHolder1v1);
		leaf1v1.setContainmentHolder(holder1v1);
		leaf1v1.setUuid(leaf1v0.getUuid());
		nodeResource2.getContents().add(leaf1v1);

		// Merge and cry havoc!
		MatchResourceSet match = MatchService.doResourceSetMatch(leaf1v0.eResource().getResourceSet(),
				leaf1v1.eResource().getResourceSet(), Collections.<String, Object> emptyMap());
		DiffResourceSet diff = DiffService.doDiff(match, false);
		assertTrue(diff.getDiffModels().size() > 0);

		for (DiffModel resourceDiff : diff.getDiffModels()) {
			List<DiffElement> differences = new ArrayList<DiffElement>(resourceDiff.getDifferences());
			MergeService.merge(differences, false);
		}
		// Check
		assertTrue(name2.equals(leaf1v0.getContainmentHolder().getName()));
		assertNotNull(leaf1v0.getNoncontainmentNoncontainmentHolder());
		assertNotNull(leaf1v0.getNoncontainmentNoncontainmentHolder().getStringHolder());
		assertTrue(nonHolderName.equals(leaf1v0.getNoncontainmentNoncontainmentHolder().getStringHolder()
				.getName()));
	}

	@Override
	protected void tearDown() throws Exception {
		try {
			final File temporaryFolder = new File(getTemporaryDataAbsolutePath());
			if (temporaryFolder.exists() && temporaryFolder.isDirectory()) {
				deleteAllChildren(temporaryFolder);
			}
		} catch (IOException e) {
			// swallow
		}
		super.tearDown();
	}

	private void deleteAllChildren(File folder) {
		for (File child : folder.listFiles()) {
			if (child.isDirectory())
				deleteAllChildren(child);
			child.delete();
		}
	}

	private String getTemporaryDataAbsolutePath() throws IOException {
		return System.getProperty("java.io.tmpdir");

	}
}

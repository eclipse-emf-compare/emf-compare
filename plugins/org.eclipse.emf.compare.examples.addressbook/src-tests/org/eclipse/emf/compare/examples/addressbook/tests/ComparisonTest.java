/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.examples.addressbook.tests;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.merge.service.MergeService;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressBook;
import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookFactory;
import org.eclipse.emf.compare.examples.addressbook.addressbook.Electronic;
import org.eclipse.emf.compare.examples.addressbook.addressbook.Office;
import org.eclipse.emf.compare.examples.addressbook.addressbook.People;
import org.eclipse.emf.compare.examples.addressbook.addressbook.Repository;
import org.eclipse.emf.compare.examples.addressbook.service.DiffHelper;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * @author Cedric Brun <cedric.brun@obeo.fr>
 */
public class ComparisonTest extends TestCase {
	/**
	 * Used to keep track of the versions between synchronizations.
	 */
	Repository repository;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		loadRepository();
	}

	private void loadRepository() throws Exception {
		repository = (Repository)ModelUtils.load(URI.createPlatformPluginURI(
				"/org.eclipse.emf.compare.examples.addressbook/data/repository.addressbook", true),
				new ResourceSetImpl());
	}

	/**
	 * Test case in which an "agent" checkout the repository, do some changes, and then get the differences.
	 * 
	 * @throws Exception
	 */
	public void testAgentCheckoutChangeThenDiff() throws Exception {
		final AddressBook agentBook = repository.checkout(-1);

		final Resource agentResource = repository.eResource().getResourceSet().createResource(
				URI.createURI("http://agent.addressbook", true));
		agentResource.getContents().add(agentBook);

		final People alice = createAlice();
		final People bob = createBob();
		agentBook.getPeoples().add(alice);
		agentBook.getPeoples().add(bob);
		final DiffModel changes = getDiff(repository.getHead(), agentBook);

		assertTrue("Alice has not been detected as added !", DiffHelper.isAdded(alice, changes) != null);
		assertTrue("Bob has not been detected as added !", DiffHelper.isAdded(bob, changes) != null);
		assertEquals("We don't have the right number of differences", 2, ((DiffGroup)changes
				.getOwnedElements().get(0)).getSubchanges());
	}

	/**
	 * The agent checkout, add a few people, merge everythin in the repository.
	 * 
	 * @throws Exception
	 */
	public void testAgentCheckoutChangeThenMerge() throws Exception {
		final AddressBook agentBook = repository.checkout(-1);

		final Resource agentResource = repository.eResource().getResourceSet().createResource(
				URI.createURI("http://agent.addressbook", true));
		agentResource.getContents().add(agentBook);

		final People alice = createAlice();
		final People bob = createBob();
		agentBook.getPeoples().add(alice);
		agentBook.getPeoples().add(bob);
		final DiffModel changes = getDiff(repository.getHead(), agentBook);

		/*
		 * let's merge the changes
		 */
		ModelElementChangeLeftTarget add = DiffHelper.isAdded(alice, changes);
		MergeService.merge(add, false);
		add = DiffHelper.isAdded(bob, changes);
		MergeService.merge(add, false);
		repository.checkin();

		final DiffModel newChanges = getDiff(repository.getHead(), agentBook);
		assertEquals("As we merged everything we should not have any change !", 0, ((DiffGroup)newChanges
				.getOwnedElements().get(0)).getSubchanges());
	}

	/**
	 * Two agents are checking out, and adding "almost" the same element.
	 * 
	 * @throws Exception
	 */
	public void testAgent2AgentsConflicts() throws Exception {
		final AddressBook agent1Book = repository.checkout(-1);
		final Resource agent1Resource = repository.eResource().getResourceSet().createResource(
				URI.createURI("http://agent1.addressbook", true));
		agent1Resource.getContents().add(agent1Book);
		/*
		 * Agent1 changes
		 */
		final People alice = createAlice();
		final People bob = createBob();
		agent1Book.getPeoples().add(alice);
		agent1Book.getPeoples().add(bob);

		final AddressBook agent2Book = repository.checkout(-1);
		final Resource agent2Resource = repository.eResource().getResourceSet().createResource(
				URI.createURI("http://agent2.addressbook", true));
		agent2Resource.getContents().add(agent2Book);
		/*
		 * Agent2 changes
		 */
		final People almostSameAlice = createAlice();
		final Electronic alicemail = (Electronic)almostSameAlice.getContacts().get(0);
		alicemail.setEmail("alice@world.fr");
		agent2Book.getPeoples().add(almostSameAlice);

		final DiffModel changes = getDiff(agent2Book, agent1Book, repository.getHead());

		/*
		 * let's merge the changes
		 */
		final ModelElementChangeLeftTarget addbob = DiffHelper.isAdded(bob, changes);
		final ModelElementChangeLeftTarget addalice = DiffHelper.isAdded(alice, changes);
		final ModelElementChangeLeftTarget addAlmostAlice = DiffHelper.isAdded(almostSameAlice, changes);
		assertNotNull("the Bob change from agent 1 has not been detected", addbob);
		assertNotNull("the Alice change from agent 1 has not been detected", addalice);
		assertNotNull("the almost alice change from agent 2 has not been detected", addAlmostAlice);

		assertTrue("add alice change should be conflicting", addalice.isConflicting());
		assertTrue("add alice change should be conflicting", addAlmostAlice.isConflicting());

	}

	private DiffModel getDiff(AddressBook checkout, AddressBook agentBook) throws Exception {
		final Map options = new HashMap();
		final MatchModel match = MatchService.doContentMatch(checkout, agentBook, options);
		return DiffService.doDiff(match);
	}

	private DiffModel getDiff(AddressBook left, AddressBook right, AddressBook ancestor) throws Exception {
		final Map options = new HashMap();
		final MatchModel match = MatchService.doContentMatch(left, right, ancestor, options);
		return DiffService.doDiff(match);
	}

	private People createAlice() {
		final People alice = AddressbookFactory.eINSTANCE.createPeople();
		alice.setName("Alice");
		final Electronic alicemail = AddressbookFactory.eINSTANCE.createElectronic();
		alicemail.setEmail("alice@world.com");
		alice.getContacts().add(alicemail);
		return alice;
	}

	private People createBob() {
		final People bob = AddressbookFactory.eINSTANCE.createPeople();
		bob.setName("Bob");
		final Office boboffice = AddressbookFactory.eINSTANCE.createOffice();
		bob.getContacts().add(boboffice);
		return bob;
	}

}

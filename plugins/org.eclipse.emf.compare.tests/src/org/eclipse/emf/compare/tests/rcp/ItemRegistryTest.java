/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.rcp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine.Factory;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryImpl;
import org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor;
import org.eclipse.emf.compare.rcp.internal.extension.IItemRegistry;
import org.eclipse.emf.compare.rcp.internal.extension.impl.ItemRegistry;
import org.eclipse.emf.compare.rcp.internal.extension.impl.WrapperItemDescriptor;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link ItemRegistry}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
@SuppressWarnings({"restriction", "nls" })
public class ItemRegistryTest {

	/** Registry to test. */
	private IItemRegistry<IMatchEngine.Factory> registry;

	@Before
	public void setUp() {
		registry = new ItemRegistry<IMatchEngine.Factory>();
	}

	/**
	 * Create a {@link IMatchEngine.Factory} descriptor.
	 * 
	 * @param id
	 *            Id of the descriptor.
	 * @param rank
	 *            Rank of the descriptor.
	 * @param instance
	 *            Instance that the descriptor describes.
	 * @return {@link IItemDescriptor} of {@link IMatchEngine.Factory}.
	 */
	private IItemDescriptor<IMatchEngine.Factory> createDescriptor(String id, int rank,
			IMatchEngine.Factory instance) {
		return new WrapperItemDescriptor<IMatchEngine.Factory>("Fake label " + id, "Fake description " + id,
				rank, id, instance);
	}

	/**
	 * Nominal use case: Add an element in the registry.
	 */
	@Test
	public void testAdd() {
		String id = "fakeID";
		IItemDescriptor<Factory> instance = createDescriptor(id, 1, new MatchEngineFactoryImpl());
		IItemDescriptor<Factory> oldValue = registry.add(instance);
		assertEquals(registry.getItemDescriptors().size(), 1);
		assertSame(registry.getItemDescriptor(id), instance);
		assertSame(registry.getHighestRankingDescriptor(), instance);
		assertNull(oldValue);
	}

	/**
	 * Adds a null to the registry.
	 */
	@Test(expected = NullPointerException.class)
	public void testAddNull() {
		registry.add(null);
	}

	/**
	 * Adds two elements with the same id in the registry.
	 */
	@Test
	public void testSameID() {
		String id = "fakeID";
		IItemDescriptor<Factory> instance1 = createDescriptor(id, 1, new MatchEngineFactoryImpl());
		registry.add(instance1);
		IItemDescriptor<Factory> instance2 = createDescriptor(id, 2, new MatchEngineFactoryImpl());
		IItemDescriptor<Factory> oldValue = registry.add(instance2);
		assertTrue(oldValue == instance1);
		assertTrue(registry.getItemDescriptor(id) == instance2);
		assertTrue(registry.getItemDescriptors().size() == 1);
	}

	/**
	 * Nominal use case: Checks that the registry returns the item with the highest rank.
	 */
	@Test
	public void testGetHighestRankingItem() {
		registry.add(createDescriptor("Id-10", -10, new MatchEngineFactoryImpl()));
		registry.add(createDescriptor("Id0", 0, new MatchEngineFactoryImpl()));
		registry.add(createDescriptor("Id10", 10, new MatchEngineFactoryImpl()));
		IItemDescriptor<Factory> higherRankedInstance = createDescriptor("Id100", 100,
				new MatchEngineFactoryImpl());
		registry.add(higherRankedInstance);
		assertTrue(registry.getHighestRankingDescriptor() == higherRankedInstance);
	}

	/**
	 * Gets the highest ranking item from an empty registry.
	 */
	@Test
	public void testGetHighestRankingItemEmptyRegistry() {
		assertTrue(registry.getHighestRankingDescriptor() == null);
	}

	/**
	 * Checks that the registry handles correctly items with equal rank.
	 */
	@Test
	public void testHighestRankingItemEqualRank() {
		IItemDescriptor<Factory> instance1 = createDescriptor("Id1", 10, new MatchEngineFactoryImpl());
		registry.add(instance1);
		IItemDescriptor<Factory> instance2 = createDescriptor("Id2", 10, new MatchEngineFactoryImpl());
		registry.add(instance2);
		IItemDescriptor<Factory> highestRankedElement = registry.getHighestRankingDescriptor();
		assertTrue(highestRankedElement == instance2 || highestRankedElement == instance1);
	}

	/**
	 * Nominal use case: Remove an item from the registry.
	 */
	@Test
	public void testRemove() {
		String id = "fakeID";
		IItemDescriptor<Factory> instance = createDescriptor(id, 1, new MatchEngineFactoryImpl());
		registry.add(instance);
		IItemDescriptor<Factory> oldValue = registry.remove(id);

		assertTrue(oldValue == instance);
		assertTrue(registry.getItemDescriptors().isEmpty());
	}

	/**
	 * Tries to remove null from the registry.
	 */
	@Test(expected = NullPointerException.class)
	public void testRemoveNull() {
		registry.remove(null);
	}

	/**
	 * Tries to remove an element with an id that is not in the registry.
	 */
	@Test
	public void testRemoveWrongElementId() {
		String id = "fakeID";
		IItemDescriptor<Factory> oldValue = registry.remove(id);
		assertTrue(oldValue == null);
	}

	/**
	 * Nominal use case: Clear the registry.
	 */
	@Test
	public void testClearRegistry() {
		registry.add(createDescriptor("Id-10", -10, new MatchEngineFactoryImpl()));
		registry.add(createDescriptor("Id0", 0, new MatchEngineFactoryImpl()));
		registry.add(createDescriptor("Id10", 10, new MatchEngineFactoryImpl()));
		registry.clear();
		assertTrue(registry.getItemDescriptors().isEmpty());
	}
}

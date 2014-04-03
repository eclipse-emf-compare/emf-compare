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
package org.eclipse.emf.compare.ide.ui.tests.logical;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.compare.ide.ui.internal.logical.ModelResolverDescriptor;
import org.eclipse.emf.compare.ide.ui.internal.logical.ModelResolverManager;
import org.eclipse.emf.compare.ide.ui.logical.AbstractModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.junit.Before;
import org.junit.Test;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Test class for {@link ModelResolverManagerTest}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
@SuppressWarnings({"nls" })
public class ModelResolverManagerTest {

	/**
	 * Copied from {@link ModelResolverManager}.
	 * 
	 * @see ModelResolverManager#ENABLE_RESOLVING_PREF_KEY
	 */
	private static final String ENABLE_RESOLVING_PREF_KEY = "org.eclipse.emf.compare.ide.ui.enable.resolving";

	private IEclipsePreferences preferences;

	private ModelResolverManager manager;

	// Suppress deprecation for Helios compatibility
	@SuppressWarnings({"deprecation" })
	@Before
	public void setUp() throws BackingStoreException {
		// Mock preference node.
		preferences = new InstanceScope()
				.getNode("org.eclipse.emf.compare.ide.ui.tests.logical.ModelResolverManagerTest");
		preferences.clear();
		manager = new ModelResolverManager(preferences);
	}

	/**
	 * Adds a resolver to the manager.
	 */
	@Test
	public void testAdd() {
		MockEnabledModelResolver resolver = new MockEnabledModelResolver();
		ModelResolverDescriptor oldValue = manager.add(resolver, resolver.getClass().getName(), "", "");
		assertSame(manager.getAllResolver().iterator().next().getModelResolver(), resolver);
		assertNull(oldValue);
	}

	/**
	 * Adds two resolvers with identical id.
	 */
	@Test
	public void testAddSameId() {
		MockEnabledModelResolver resolver = new MockEnabledModelResolver();
		String id = resolver.getClass().getName();
		manager.add(resolver, id, "", "");

		MockEnabledModelResolver resolver2 = new MockEnabledModelResolver();
		ModelResolverDescriptor oldValue = manager.add(resolver2, id, "", "");

		assertSame(oldValue.getModelResolver(), resolver);
	}

	/**
	 * Adds null to the manager.
	 */
	@Test(expected = NullPointerException.class)
	public void testAddResolverNull() {
		manager.add(null, "id", "", "");
	}

	/**
	 * Adds a null id to the manager.
	 */
	@Test(expected = NullPointerException.class)
	public void testAddIDNull() {
		manager.add(new MockEnabledModelResolver(), null, "", "");
	}

	/**
	 * Adds a null label and a null description to the manager.
	 */
	@Test
	public void testAddLabelDescriptionNull() {
		manager.add(new MockEnabledModelResolver(), "id", null, null);
		assertEquals(manager.getAllResolver().size(), 1);
	}

	/**
	 * Removes an element from the manager.
	 */
	@Test
	public void testRemove() {
		MockEnabledModelResolver resolver = new MockEnabledModelResolver();
		String id = resolver.getClass().getName();
		manager.add(resolver, id, "", "");

		ModelResolverDescriptor oldValue = manager.remove(id);
		assertTrue(manager.getAllResolver().isEmpty());
		assertSame(oldValue.getModelResolver(), resolver);
	}

	/**
	 * Removes null from the manager.
	 */
	@Test
	public void testRemoveNull() {
		ModelResolverDescriptor oldValue = manager.remove(null);
		assertNull(oldValue);
	}

	/**
	 * Remove a resolver from the manager with a wrong id.
	 */
	@Test
	public void testRemoveWrongId() {
		MockEnabledModelResolver resolver = new MockEnabledModelResolver();
		manager.add(resolver, resolver.getClass().getName(), "", "");

		ModelResolverDescriptor oldValue = manager.remove("fakeID");
		assertNull(oldValue);
		assertEquals(manager.getAllResolver().size(), 1);
	}

	/**
	 * Gets all resolvers contributed to the manager.
	 */
	@Test
	public void testGetAllResolver() {
		MockEnabledModelResolver resolver1 = new MockEnabledModelResolver();
		manager.add(resolver1, resolver1.getClass().getName() + "1", "", "");

		MockEnabledModelResolver resolver2 = new MockEnabledModelResolver();
		manager.add(resolver2, resolver2.getClass().getName() + "2", "", "");

		MockEnabledModelResolver resolver3 = new MockEnabledModelResolver();
		manager.add(resolver3, resolver3.getClass().getName() + "3", "", "");

		Collection<ModelResolverDescriptor> resolverDescriptor = manager.getAllResolver();
		Collection<IModelResolver> resolvers = Collections2.transform(resolverDescriptor,
				new Function<ModelResolverDescriptor, IModelResolver>() {

					public IModelResolver apply(ModelResolverDescriptor arg0) {
						return arg0.getModelResolver();
					}

				});
		assertEquals(Sets.newHashSet(resolvers), Sets.newHashSet(resolver1, resolver2, resolver3));
	}

	/**
	 * Gets all resolvers from an empty manager.
	 */
	@Test
	public void testGetAllResolverEmpty() {
		assertTrue(manager.getAllResolver().isEmpty());
	}

	/**
	 * Tests if the resolution is enabled.
	 */
	@Test
	public void testIsResolutionEnabled() {
		// Default value is true
		assertTrue(manager.isResolutionEnabled());
		// Set the value to false
		preferences.put(ENABLE_RESOLVING_PREF_KEY, Boolean.FALSE.toString());
		assertFalse(manager.isResolutionEnabled());
	}

	/**
	 * Tests if the resolution is enabled with a corrupted preference value.
	 */
	@Test
	public void testIsResolutionEnabledCorruptPreferences() {
		preferences.put(ENABLE_RESOLVING_PREF_KEY, "FakeValue");
		assertFalse(manager.isResolutionEnabled());
	}

	/**
	 * Gets a resolver from its id.
	 */
	@Test
	public void testGetResolverFromId() {
		MockEnabledModelResolver resolver1 = new MockEnabledModelResolver();
		String resolver1Id = resolver1.getClass().getName() + "1";
		manager.add(resolver1, resolver1Id, "", "");

		MockEnabledModelResolver resolver2 = new MockEnabledModelResolver();
		String resolver2Id = resolver2.getClass().getName() + "2";
		manager.add(resolver2, resolver2Id, "", "");

		assertSame(manager.getDescriptor(resolver1Id).getModelResolver(), resolver1);
		assertSame(manager.getDescriptor(resolver2Id).getModelResolver(), resolver2);
	}

	/**
	 * Sets and Gets the user selected resolver.
	 */
	@Test
	public void testUserSelectedResolver() {
		MockEnabledModelResolver resolver1 = new MockEnabledModelResolver();
		manager.add(resolver1, resolver1.getClass().getName() + "1", "", "");

		MockEnabledModelResolver resolver2 = new MockEnabledModelResolver();
		manager.add(resolver2, resolver2.getClass().getName() + "2", "", "");

		MockEnabledModelResolver resolver3 = new MockEnabledModelResolver();
		String resolver3Id = resolver3.getClass().getName() + "3";
		manager.add(resolver3, resolver3Id, "", "");

		manager.setUserSelectedResolver(manager.getDescriptor(resolver3Id));
		assertEquals(manager.getUserSelectedResolver().getModelResolver(), resolver3);
	}

	/**
	 * Gets the user selected resolver. The user resolver has not been set on this manager.
	 */
	@Test
	public void testGetUserSelectResolverNotSet() {
		MockEnabledModelResolver resolver1 = new MockEnabledModelResolver();
		manager.add(resolver1, resolver1.getClass().getName() + "1", "", "");

		MockEnabledModelResolver resolver2 = new MockEnabledModelResolver();
		manager.add(resolver2, resolver2.getClass().getName() + "2", "", "");

		MockEnabledModelResolver resolver3 = new MockEnabledModelResolver();
		String resolver3Id = resolver3.getClass().getName() + "3";
		manager.add(resolver3, resolver3Id, "", "");

		assertNull(manager.getUserSelectedResolver());

	}

	/**
	 * Unsets the user resolver from the manager.
	 */
	@Test
	public void testUnsetUserResolver() {
		MockEnabledModelResolver resolver1 = new MockEnabledModelResolver();
		manager.add(resolver1, resolver1.getClass().getName() + "1", "", "");

		MockEnabledModelResolver resolver2 = new MockEnabledModelResolver();
		manager.add(resolver2, resolver2.getClass().getName() + "2", "", "");

		MockEnabledModelResolver resolver3 = new MockEnabledModelResolver();
		String resolver3Id = resolver3.getClass().getName() + "3";
		manager.add(resolver3, resolver3Id, "", "");

		manager.setUserSelectedResolver(manager.getDescriptor(resolver3Id));

		manager.setUserSelectedResolver(null);
		assertNull(manager.getUserSelectedResolver());
	}

	/**
	 * Set the resolution of the manager.
	 */
	@Test
	public void testSetResolution() {
		manager.setResolution(false);
		assertFalse(manager.isResolutionEnabled());
		manager.setResolution(true);
		assertTrue(manager.isResolutionEnabled());
	}

	/**
	 * Clears the manager.
	 */
	@Test
	public void testClear() {
		MockEnabledModelResolver resolver1 = new MockEnabledModelResolver();
		manager.add(resolver1, resolver1.getClass().getName() + "1", "", "");

		MockEnabledModelResolver resolver2 = new MockEnabledModelResolver();
		manager.add(resolver2, resolver2.getClass().getName() + "2", "", "");

		manager.clear();
		assertTrue(manager.getAllResolver().isEmpty());
	}

	/**
	 * Mock model resolver.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static class MockEnabledModelResolver extends AbstractModelResolver {

		public SynchronizationModel resolveLocalModels(IResource left, IResource right, IResource origin,
				IProgressMonitor monitor) {
			return null;
		}

		public SynchronizationModel resolveModels(IStorageProviderAccessor storageAccessor, IStorage left,
				IStorage right, IStorage origin, IProgressMonitor monitor) {
			return null;
		}

		public StorageTraversal resolveLocalModel(IResource resource, IProgressMonitor monitor) {
			return null;
		}

		public boolean canResolve(IStorage sourceStorage) {
			return true;
		}

	}

}

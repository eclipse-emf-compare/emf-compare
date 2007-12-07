/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.unit.core.util.emfcomparemap;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.emf.compare.util.EMFCompareMap;

/**
 * Tests the behavior of the {@link EMFCompareMap}'s entrySet and its iterator so that they behave the same
 * as their {@link HashMap} counterparts.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings({"unchecked", "nls", })
public class TestEntrySet extends TestCase {
	/** Input keys for a set map. */
	private static final Object[] KEY_SET = {null, "", "Alize", "Bise", "Boree", "Chinook", "Eurus",
			"Hurricane", "Noroit", "Rafale", "Sirocco", "Tourbillon", "Typhon", new Integer(0), new Long(10),
			new Float(20), new Double(30), new Boolean(false), new HashSet(), 5, 15L, 25f, 35d, '\u00ab',
			true, };

	/** String displayed when an expected {@link ConcurrentModificationException} isn't thrown. */
	private static final String MESSAGE_CONCURRENT = "ConcurrentModificationException hasn't been thrown by the iterator after changing map via";

	/** String displayed when an unexpected {@link UnsupportedOperationException} is thrown. */
	private static final String MESSAGE_UNSUPPORTED = "threw an unexpected UnsupportedOperationException.";

	/** Input values for a set map. */
	private static final Object[] VALUE_SET = {null, "", "Aquilon", "Blizzard", "Brise", "Cyclone", "Foehn",
			"Mistral", "Notus", "Simoon", "Suroit", "Tramontane", "Zephyr", new Integer(0), new Long(10),
			new Float(20), new Double(30), new Boolean(true), new HashSet(), 5, 15L, 25f, 35d, '\u00aa',
			false, };

	/** Map that will be used for all these tests. */
	private final EMFCompareMap testedMap = new EMFCompareMap();

	/** Name of the currently tested method for the error messages. */
	private String testedMethod;

	/**
	 * Tests the {@link EMFCompareMap}'s entry set. The set is expected to support the clear() operation and
	 * it effectively empties the map.
	 */
	public void testClear() {
		testedMethod = "Clear()";
		final Set<Map.Entry> entrySet = testedMap.entrySet();
		try {
			entrySet.clear();
		} catch (UnsupportedOperationException e) {
			fail(testedMethod + ' ' + MESSAGE_UNSUPPORTED);
		}
		assertEquals("Clear() operation didn't effectively empty the map.", 0, testedMap.size());
	}

	/**
	 * Tests the {@link EMFCompareMap}'s entry set. The set's iterator is expected to throw a
	 * {@link ConcurrentModificationException} if its original map is structurally modified via the clear()
	 * method.
	 */
	public void testConcurrentClear() {
		testedMethod = "Clear()";
		final Iterator<Map.Entry> entryIterator = testedMap.entrySet().iterator();
		testedMap.clear();

		try {
			entryIterator.next();
			fail(MESSAGE_CONCURRENT + ' ' + testedMethod + '.');
		} catch (ConcurrentModificationException e) {
			// We expected this
		} catch (NoSuchElementException e) {
			fail(MESSAGE_CONCURRENT + ' ' + testedMethod + '.');
		}
	}

	/**
	 * Tests the {@link EMFCompareMap}'s entry set. The set's iterator is expected to throw a
	 * {@link ConcurrentModificationException} if its original map is structurally modified via the
	 * put(Object) method.
	 */
	public void testConcurrentPut() {
		testedMethod = "Put(Object)";
		final Iterator<Map.Entry> entryIterator = testedMap.entrySet().iterator();
		// This entry cannot exist (random value as a key, random key as a value)
		testedMap.put(VALUE_SET[5], KEY_SET[10]);

		try {
			entryIterator.next();
			fail(MESSAGE_CONCURRENT + ' ' + testedMethod + '.');
		} catch (ConcurrentModificationException e) {
			// We expected this
		} catch (NoSuchElementException e) {
			fail(MESSAGE_CONCURRENT + ' ' + testedMethod + '.');
		}
	}

	/**
	 * Tests the {@link EMFCompareMap}'s entry set. The set's iterator is expected to throw a
	 * {@link ConcurrentModificationException} if its original map is structurally modified via the
	 * putAll(Collection) method.
	 */
	public void testConcurrentPutAll() {
		testedMethod = "PutAll(Collection)";
		final EMFCompareMap anotherMap = new EMFCompareMap();
		final Iterator<Map.Entry> entryIterator = anotherMap.entrySet().iterator();
		anotherMap.putAll(testedMap);

		try {
			entryIterator.next();
			fail(MESSAGE_CONCURRENT + ' ' + testedMethod + '.');
		} catch (ConcurrentModificationException e) {
			// We expected this
		} catch (NoSuchElementException e) {
			fail(MESSAGE_CONCURRENT + ' ' + testedMethod + '.');
		}
	}

	/**
	 * Tests the {@link EMFCompareMap}'s entry set. The set's iterator is expected to throw a
	 * {@link ConcurrentModificationException} if its original map is structurally modified via the
	 * remove(Object) method.
	 */
	public void testConcurrentRemove() {
		testedMethod = "Remove(Object)";
		final Iterator<Map.Entry> entryIterator = testedMap.entrySet().iterator();
		// Removes a random entry from the set
		testedMap.remove(KEY_SET[10]);

		try {
			entryIterator.next();
			fail(MESSAGE_CONCURRENT + ' ' + testedMethod + '.');
		} catch (ConcurrentModificationException e) {
			// We expected this
		} catch (NoSuchElementException e) {
			fail(MESSAGE_CONCURRENT + ' ' + testedMethod + '.');
		}
	}

	/**
	 * Tests the {@link EMFCompareMap}'s entry set. The set is expected not to support the add() operation.
	 */
	public void testNoAddThroughSet() {
		final Set<Map.Entry> entrySet = testedMap.entrySet();
		try {
			entrySet.add((Map.Entry)testedMap.entrySet().iterator().next());
			fail("UnsupportedOperationException should have been thrown.");
		} catch (UnsupportedOperationException e) {
			// We expected this
		}
	}

	/**
	 * Tests the {@link EMFCompareMap}'s entry set. The set is expected to support the removeAll(Collection)
	 * operation and it effectively removes all the map's element contained within the given collection.
	 */
	public void testRemoveAll() {
		testedMethod = "RemoveAll(Collection)";
		Set<Map.Entry> entrySet = testedMap.entrySet();
		Set<Map.Entry> removedElements = new HashSet(entrySet);
		try {
			entrySet.removeAll(removedElements);
		} catch (UnsupportedOperationException e) {
			fail(testedMethod + ' ' + MESSAGE_UNSUPPORTED);
		}
		assertEquals("Map should have been emptied by its entrySet removeAll().", 0, testedMap.size());

		for (int i = 0; i < KEY_SET.length; i++) {
			// Adds more and more values to the map for each loop
			for (int j = 0; j < i; j++) {
				testedMap.put(KEY_SET[j], VALUE_SET[j]);
			}

			entrySet = testedMap.entrySet();
			// Create an array of the entries to avoid concurrent modification exception
			final Object[] array = entrySet.toArray();
			int currentMapSize = testedMap.size();
			removedElements = new HashSet();
			for (int j = 0; j < array.length; j++) {
				removedElements.add((Map.Entry)array[j]);
				try {
					entrySet.removeAll(removedElements);
				} catch (UnsupportedOperationException e) {
					fail(testedMethod + ' ' + MESSAGE_UNSUPPORTED);
				}
				assertFalse(
						"Operation removeAll(Collection) of the entry set did not remove necessary elements from the map.",
						testedMap.containsKey(((Map.Entry)array[j]).getKey()));
				assertEquals("Unexpected size of the map after removeAll() execution.", testedMap.size(),
						--currentMapSize);
			}
		}
	}

	/**
	 * Tests the {@link EMFCompareMap}'s entry set's iterator. The iterator is expected to support the
	 * remove(Object) operation and this method is expected to effectively remove the given object from the
	 * map. Size of the map afterward should have decreased by 1.
	 */
	public void testRemoveViaIterator() {
		testedMethod = "Iterator's remove(Object)";

		int currentMapSize = testedMap.size();
		for (final Iterator entryIterator = testedMap.entrySet().iterator(); entryIterator.hasNext(); ) {
			final Map.Entry currentEntry = (Map.Entry)(entryIterator.next());
			try {
				entryIterator.remove();
			} catch (UnsupportedOperationException e) {
				fail(testedMethod + ' ' + MESSAGE_UNSUPPORTED);
			}
			assertFalse(
					"Operation remove() of the entry set's iterator did not remove the element from the map.",
					testedMap.containsKey(currentEntry.getKey()));
			assertEquals("Map size didn't decrease by one after removal of one element.", --currentMapSize,
					testedMap.size());
		}
	}

	/**
	 * Tests the {@link EMFCompareMap}'s entry set. The set is expected to support the remove(Object)
	 * operation and this method is expected to effectively remove the given object from the map. Size of the
	 * map afterward should have decreased by 1.
	 */
	public void testRemoveViaSet() {
		testedMethod = "Set's remove(Object)";
		final Set<Map.Entry> entrySet = testedMap.entrySet();
		// Create an array of the entries to avoid concurrent modification exception
		final Object[] array = entrySet.toArray();
		int currentMapSize = testedMap.size();
		for (Object entry : array) {
			try {
				entrySet.remove(entry);
			} catch (UnsupportedOperationException e) {
				fail(testedMethod + ' ' + MESSAGE_UNSUPPORTED);
			}
			assertFalse("Operation remove() of the entry set did not remove the element from the map.",
					testedMap.containsKey(((Map.Entry)entry).getKey()));
			assertEquals("Map size didn't decrease by one after removal of one element.", testedMap.size(),
					--currentMapSize);
		}
	}

	/**
	 * Tests the {@link EMFCompareMap}'s entry set. The set is expected to support the retainAll(Collection)
	 * operation and it effectively removes all the map's element not contained within the given collection.
	 */
	public void testRetainAll() {
		testedMethod = "RetainAll(Collection)";
		Set<Map.Entry> entrySet = testedMap.entrySet();
		final Set<Map.Entry> retainedElements = new HashSet();
		try {
			entrySet.retainAll(retainedElements);
		} catch (UnsupportedOperationException e) {
			fail(testedMethod + ' ' + MESSAGE_UNSUPPORTED);
		}
		assertEquals("Map should have been emptied by its entrySet retainAll().", 0, testedMap.size());

		for (int i = 0; i < KEY_SET.length; i++) {
			// Adds more and more values to the map for each loop
			for (int j = 0; j < i; j++) {
				testedMap.put(KEY_SET[j], VALUE_SET[j]);
			}

			entrySet = testedMap.entrySet();
			// Create an array of the entries to avoid concurrent modification exception
			final Object[] array = entrySet.toArray();
			int currentMapSize = testedMap.size();
			for (int j = 0; j < array.length; j++) {
				for (int k = 0; k < array.length - j; k++) {
					retainedElements.add((Map.Entry)array[k]);
				}
				try {
					entrySet.retainAll(retainedElements);
				} catch (UnsupportedOperationException e) {
					fail(testedMethod + ' ' + MESSAGE_UNSUPPORTED);
				}

				for (int k = 0; k < array.length - j; k++) {
					assertTrue(
							"Operation retainAll(Collection) did not retain necessary elements within the map.",
							testedMap.containsKey(((Map.Entry)array[k]).getKey()));
				}

				assertEquals("Unexpected size of the map after retainAll() execution.", currentMapSize--,
						array.length - j);
			}
		}
	}

	/**
	 * Tests the creation of the {@link EMFCompareMap}'s entry set.
	 * <p>
	 * <ul>
	 * Assertions :
	 * <li>Created set isn't <code>null</code></li>
	 * <li>Created set's size is the same as its original map's.</li>
	 * <li>All the objects contained by the set are instances of {@link Map.Entry}.</li>
	 * <li>The set's entries correspond to the original map's mappings.</li>
	 * </ul>
	 * </p>
	 */
	public void testSetCreation() {
		final Set entrySet = testedMap.entrySet();

		assertNotNull("Entry set hasn't been created.", entrySet);
		assertEquals("Created entry set hasn't got the same size as its original map.", testedMap.size(),
				entrySet.size());

		for (final Iterator entryIterator = entrySet.iterator(); entryIterator.hasNext(); ) {
			final Object currentEntry = entryIterator.next();

			assertTrue("Iterator contains objects that aren't instances of Map.Entry.",
					currentEntry instanceof Map.Entry);
			assertEquals("The entry set's iterator contains mappings different than its original map's.",
					((Map.Entry)currentEntry).getValue(), testedMap.get(((Map.Entry)currentEntry).getKey()));
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() {
		for (int i = 0; i < KEY_SET.length; i++) {
			testedMap.put(KEY_SET[i], VALUE_SET[i]);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() {
		testedMap.clear();
	}
}

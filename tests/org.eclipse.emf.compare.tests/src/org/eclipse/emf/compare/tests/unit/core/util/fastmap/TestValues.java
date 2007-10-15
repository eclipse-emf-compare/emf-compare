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
package org.eclipse.emf.compare.tests.unit.core.util.fastmap;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.emf.compare.util.FastMap;

/**
 * Tests the behavior of the {@link FastMap}'s values collection and its iterator so that they behave the
 * same as their {@link HashMap} counterparts.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings({"unchecked", "nls", })
public class TestValues extends TestCase {
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
	private final FastMap testedMap = new FastMap();

	/** Name of the currently tested method for the error messages. */
	private String testedMethod;

	/**
	 * Tests the {@link FastMap}'s values collection. The collection is expected to support the clear()
	 * operation and it effectively empties the map.
	 */
	public void testClear() {
		testedMethod = "Clear()";
		final Collection values = testedMap.values();
		try {
			values.clear();
		} catch (UnsupportedOperationException e) {
			fail(testedMethod + ' ' + MESSAGE_UNSUPPORTED);
		}
		assertEquals("Clear() operation didn't effectively empty the map.", 0, testedMap.size());
	}

	/**
	 * Tests the creation of the {@link FastMap}'s values collection.
	 * <p>
	 * <ul>
	 * Assertions :
	 * <li>Created collection isn't <code>null</code></li>
	 * <li>Created collection's size is the same as its original map's.</li>
	 * <li>The collection's entries correspond to the original map's values.</li>
	 * </ul>
	 * </p>
	 */
	public void testCollectionCreation() {
		final Collection values = testedMap.values();

		assertNotNull("Values collection hasn't been created.", values);
		assertEquals("Created collection hasn't got the same size as its original map.", testedMap.size(),
				values.size());

		for (final Iterator valueIterator = values.iterator(); valueIterator.hasNext(); ) {
			final Object currentValue = valueIterator.next();
			assertTrue(
					"The collection's iterator contains mappings different than its original map's values.",
					testedMap.containsValue(currentValue));
		}
	}

	/**
	 * Tests the {@link FastMap}'s values collection. The collection's iterator is expected to throw a
	 * {@link ConcurrentModificationException} if its original map is structurally modified via the clear()
	 * method.
	 */
	public void testConcurrentClear() {
		testedMethod = "Clear()";
		final Iterator valueIterator = testedMap.values().iterator();
		testedMap.clear();

		try {
			valueIterator.next();
			fail(MESSAGE_CONCURRENT + ' ' + testedMethod + '.');
		} catch (ConcurrentModificationException e) {
			// We expected this
		} catch (NoSuchElementException e) {
			fail(MESSAGE_CONCURRENT + ' ' + testedMethod + '.');
		}
	}

	/**
	 * Tests the {@link FastMap}'s values collection. The collection's iterator is expected to throw a
	 * {@link ConcurrentModificationException} if its original map is structurally modified via the
	 * put(Object) method.
	 */
	public void testConcurrentPut() {
		testedMethod = "Put(Object)";
		final Iterator valueIterator = testedMap.values().iterator();
		// This entry cannot exist (random value as a key, random key as a value)
		testedMap.put(VALUE_SET[5], KEY_SET[10]);

		try {
			valueIterator.next();
			fail(MESSAGE_CONCURRENT + ' ' + testedMethod + '.');
		} catch (ConcurrentModificationException e) {
			// We expected this
		} catch (NoSuchElementException e) {
			fail(MESSAGE_CONCURRENT + ' ' + testedMethod + '.');
		}
	}

	/**
	 * Tests the {@link FastMap}'s values collection. The collection's iterator is expected to throw a
	 * {@link ConcurrentModificationException} if its original map is structurally modified via the
	 * putAll(Collection) method.
	 */
	public void testConcurrentPutAll() {
		testedMethod = "PutAll(Collection)";
		final FastMap anotherMap = new FastMap();
		final Iterator valueIterator = anotherMap.values().iterator();
		anotherMap.putAll(testedMap);

		try {
			valueIterator.next();
			fail(MESSAGE_CONCURRENT + ' ' + testedMethod + '.');
		} catch (ConcurrentModificationException e) {
			// We expected this
		} catch (NoSuchElementException e) {
			fail(MESSAGE_CONCURRENT + ' ' + testedMethod + '.');
		}
	}

	/**
	 * Tests the {@link FastMap}'s values collection. The collection's iterator is expected to throw a
	 * {@link ConcurrentModificationException} if its original map is structurally modified via the
	 * remove(Object) method.
	 */
	public void testConcurrentRemove() {
		testedMethod = "Remove(Object)";
		final Iterator valueIterator = testedMap.values().iterator();
		// Removes a random entry from the set
		testedMap.remove(KEY_SET[10]);

		try {
			valueIterator.next();
			fail(MESSAGE_CONCURRENT + ' ' + testedMethod + '.');
		} catch (ConcurrentModificationException e) {
			// We expected this
		} catch (NoSuchElementException e) {
			fail(MESSAGE_CONCURRENT + ' ' + testedMethod + '.');
		}
	}

	/**
	 * Tests the {@link FastMap}'s values collection. The collection is expected not to support the add()
	 * operation.
	 */
	public void testNoAddThroughCollection() {
		final FastMap map = new FastMap();
		final Collection col = map.values();
		try {
			col.add(KEY_SET[5]);
			fail("UnsupportedOperationException should have been thrown.");
		} catch (UnsupportedOperationException e) {
			// We expected this
		}
	}

	/**
	 * Tests the {@link FastMap}'s values collection. The collection is expected to support the
	 * removeAll(Collection) operation and it effectively removes all the map's element contained within the
	 * given collection.
	 */
	public void testRemoveAll() {
		testedMethod = "RemoveAll(Collection)";
		Collection values = testedMap.values();
		Set removedElements = new HashSet(values);
		try {
			values.removeAll(removedElements);
		} catch (UnsupportedOperationException e) {
			fail(testedMethod + ' ' + MESSAGE_UNSUPPORTED);
		}
		assertEquals("Map should have been emptied by its values set removeAll().", 0, testedMap.size());

		for (int i = 0; i < KEY_SET.length; i++) {
			// Adds more and more values to the map for each loop
			for (int j = 0; j < i; j++) {
				testedMap.put(KEY_SET[j], VALUE_SET[j]);
			}

			values = testedMap.values();
			// Create an array of the entries to avoid concurrent modification exception
			final Object[] array = values.toArray();
			int currentMapSize = testedMap.size();
			removedElements = new HashSet();
			for (int j = 0; j < array.length; j++) {
				removedElements.add(array[j]);
				try {
					values.removeAll(removedElements);
				} catch (UnsupportedOperationException e) {
					fail(testedMethod + ' ' + MESSAGE_UNSUPPORTED);
				}
				assertFalse(
						"Operation removeAll(Collection) of the values set did not remove necessary elements from the map.",
						testedMap.containsValue(array[j]));
				assertEquals("Unexpected size of the map after removeAll() execution.", --currentMapSize,
						testedMap.size());
			}
		}
	}

	/**
	 * Tests the {@link FastMap}'s values collection. The collection is expected to support the
	 * remove(Object) operation and this method is expected to effectively remove the given object from the
	 * map. Size of the map afterward should have decreased by 1.
	 */
	public void testRemoveViaCollection() {
		testedMethod = "Collection's remove(Object)";

		final Collection values = testedMap.values();
		// Create an array of the entries to avoid concurrent modification exception
		final Object[] array = values.toArray();
		int currentMapSize = testedMap.size();
		for (Object value : array) {
			try {
				values.remove(value);
			} catch (UnsupportedOperationException e) {
				fail(testedMethod + ' ' + MESSAGE_UNSUPPORTED);
			}
			assertFalse(
					"Operation remove() of the values collection did not remove the element from the map.",
					testedMap.containsValue(value));
			assertEquals("Map size didn't decrease by one after removal of one element.", --currentMapSize,
					testedMap.size());
		}
	}

	/**
	 * Tests the {@link FastMap}'s values collection's iterator. The iterator is expected to support the
	 * remove(Object) operation and this method is expected to effectively remove the given object from the
	 * map. Size of the map afterward should have decreased by 1.
	 */
	public void testRemoveViaIterator() {
		testedMethod = "Iterator's remove(Object)";

		int currentMapSize = testedMap.size();
		for (final Iterator valueIterator = testedMap.values().iterator(); valueIterator.hasNext(); ) {
			final Object currentValue = valueIterator.next();
			try {
				valueIterator.remove();
			} catch (UnsupportedOperationException e) {
				fail(testedMethod + ' ' + MESSAGE_UNSUPPORTED);
			}
			assertFalse(
					"Operation remove() of the values collection's iterator did not remove the element from the map.",
					testedMap.containsValue(currentValue));
			assertEquals("Unexpected size of the map after removal of an element.", --currentMapSize,
					testedMap.size());
		}
	}

	/**
	 * Tests the {@link FastMap}'s values collection. The collection is expected to support the
	 * retainAll(Collection) operation and it effectively removes all the map's element not contained within
	 * the given collection.
	 */
	public void testRetainAll() {
		testedMethod = "RetainAll(Collection)";
		Collection values = testedMap.values();
		final Set retainedElements = new HashSet();
		try {
			values.retainAll(retainedElements);
		} catch (UnsupportedOperationException e) {
			fail(testedMethod + ' ' + MESSAGE_UNSUPPORTED);
		}
		assertEquals("Map should have been emptied by its values collection retainAll().", 0, testedMap
				.size());

		for (int i = 0; i < KEY_SET.length; i++) {
			// Adds more and more values to the map for each loop
			for (int j = 0; j < i; j++) {
				testedMap.put(KEY_SET[j], VALUE_SET[j]);
			}

			values = testedMap.values();
			// Create an array of the entries to avoid concurrent modification exception
			final Object[] array = values.toArray();
			int currentMapSize = testedMap.size();
			for (int j = 0; j < array.length; j++) {
				for (int k = 0; k < array.length - j; k++) {
					retainedElements.add(array[k]);
				}
				try {
					values.retainAll(retainedElements);
				} catch (UnsupportedOperationException e) {
					fail(testedMethod + ' ' + MESSAGE_UNSUPPORTED);
				}

				for (int k = 0; k < array.length - j; k++) {
					assertTrue(
							"Operation retainAll(Collection) did not retain necessary elements within the map.",
							testedMap.containsValue(array[k]));
				}

				assertEquals("Unexpected size of the map after retainAll() execution.", currentMapSize--,
						array.length - j);
			}
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

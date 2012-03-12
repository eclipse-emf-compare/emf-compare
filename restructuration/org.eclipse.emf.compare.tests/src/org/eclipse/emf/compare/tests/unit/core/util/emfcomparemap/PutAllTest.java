/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.unit.core.util.emfcomparemap;

import java.util.HashSet;

import junit.framework.TestCase;

import org.eclipse.emf.compare.util.EMFCompareMap;

/**
 * Tests the behavior of {@link EMFCompareMap#putAll(Collection)} so that it behaves the same as its
 * {@link HashMap} counterpart.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings({"unchecked", "nls", })
public class PutAllTest extends TestCase {
	/** Input keys for a set map. */
	private static final Object[] KEY_SET = {null, "", "Alize", "Bise", "Boree", "Chinook", "Eurus",
			"Hurricane", "Noroit", "Rafale", "Sirocco", "Tourbillon", "Typhon", new Integer(0), new Long(10),
			new Float(20), new Double(30), new Boolean(false), new HashSet(), 5, 15L, 25f, 35d, '\u00ab',
			true, };

	/** String displayed when an unexpected exception is thrown. */
	private static final String MESSAGE_UNEXPECTED = "threw an unexpected";

	/** Name of the tested method. Will be used to display junit error messages. */
	private static final String METHOD_NAME = "putAll(Collection)";

	/** Input values for a set map. */
	private static final Object[] VALUE_SET = {null, "", "Aquilon", "Blizzard", "Brise", "Cyclone", "Foehn",
			"Mistral", "Notus", "Simoon", "Suroit", "Tramontane", "Zephyr", new Integer(0), new Long(10),
			new Float(20), new Double(30), new Boolean(true), new HashSet(), 5, 15L, 25f, 35d, '\u00aa',
			false, };

	/**
	 * Tests {@link EMFCompareMap#putAll(Collection)} on a non-empty map with a non-empty collection with some
	 * keys intersecting. Expects that all mappings from the source will be inserted in the target, replacing
	 * the existing mapping if any.
	 */
	public void testPutAllIntersects() {
		final EMFCompareMap map1 = new EMFCompareMap();
		final EMFCompareMap map2 = new EMFCompareMap();

		for (int i = 0; i < (KEY_SET.length >> 1) + 3; i++) {
			map1.put(KEY_SET[i], VALUE_SET[i]);
		}
		// inverts values for the second map so that we can test the intersection replacement
		for (int i = (KEY_SET.length >> 1) - 3; i < KEY_SET.length; i++) {
			map2.put(KEY_SET[i], VALUE_SET[VALUE_SET.length - 1 - i]);
		}

		try {
			map1.putAll(map2);
		} catch (UnsupportedOperationException e) {
			fail(METHOD_NAME + ' ' + MESSAGE_UNEXPECTED + e.getClass().getName() + '.');
		} catch (ClassCastException e) {
			fail(METHOD_NAME + ' ' + MESSAGE_UNEXPECTED + e.getClass().getName() + '.');
		}

		assertEquals("Unexpected size of the map after putAll() execution.", KEY_SET.length, map1.size());
		assertEquals("PutAll() altered its source map's size.", (KEY_SET.length >> 1) + 4, map2.size());
		// These are keys from map1 that did not intersect with map2's mappings
		for (int i = 0; i < (KEY_SET.length >> 1) - 3; i++) {
			assertTrue("PutAll() did not insert all of the necessary mappings in the map.", map1
					.containsKey(KEY_SET[i]));
			assertEquals("PutAll() altered non-intersecting mappings.", VALUE_SET[i], map1.get(KEY_SET[i]));
		}
		// These are keys from map2 that did intersect with map1's mappings
		for (int i = (KEY_SET.length >> 1) - 3; i < (KEY_SET.length >> 1) + 3; i++) {
			assertTrue("PutAll() did not insert all necessary mappings in the map.", map1
					.containsKey(KEY_SET[i]));
			assertEquals("PutAll() did not replace intersecting mappings.", VALUE_SET[VALUE_SET.length - 1
					- i], map1.get(KEY_SET[i]));
		}
		// These are keys from map2 that did not intersect with map1's mappings
		for (int i = (KEY_SET.length >> 1) + 3; i < KEY_SET.length; i++) {
			assertTrue("PutAll() did not insert all necessary mappings in the map.", map1
					.containsKey(KEY_SET[i]));
			assertEquals("PutAll() did not insert non-intersecting mappings.", VALUE_SET[VALUE_SET.length - 1
					- i], map1.get(KEY_SET[i]));
		}
	}

	/**
	 * Tests {@link EMFCompareMap#putAll(Collection)} on a non-empty map with a non-empty collection without
	 * any key intersection. Expects that all mappings from the source will be inserted in the target.
	 */
	public void testPutAllNoIntersects() {
		final EMFCompareMap map1 = new EMFCompareMap();
		final EMFCompareMap map2 = new EMFCompareMap();

		for (int i = 0; i < KEY_SET.length >> 1; i++) {
			map1.put(KEY_SET[i], VALUE_SET[i]);
		}
		for (int i = KEY_SET.length >> 1; i < KEY_SET.length; i++) {
			map2.put(KEY_SET[i], VALUE_SET[i]);
		}

		try {
			map1.putAll(map2);
		} catch (UnsupportedOperationException e) {
			fail(METHOD_NAME + ' ' + MESSAGE_UNEXPECTED + e.getClass().getName() + '.');
		} catch (ClassCastException e) {
			fail(METHOD_NAME + ' ' + MESSAGE_UNEXPECTED + e.getClass().getName() + '.');
		}

		assertEquals("Unexpected size of the map after putAll() execution.", KEY_SET.length, map1.size());
		assertEquals("PutAll() altered its source map's size.", (KEY_SET.length >> 1) + 1, map2.size());
		for (int i = 0; i < KEY_SET.length; i++) {
			assertTrue("PutAll() did not insert all of the necessary mappings in the map.", map1
					.containsKey(KEY_SET[i]));
		}
	}

	/**
	 * Tests {@link EMFCompareMap#putAll(Collection)} on an empty map with an empty collection. Expects the
	 * size to stay <code>0</code>.
	 */
	public void testPutEmptyInEmpty() {
		final EMFCompareMap target = new EMFCompareMap();
		final EMFCompareMap source = new EMFCompareMap();

		try {
			target.putAll(source);
			assertEquals("Operation putAll() with an empty map inserted mappings in its target.", 0, target
					.size());
		} catch (UnsupportedOperationException e) {
			fail(METHOD_NAME + ' ' + MESSAGE_UNEXPECTED + e.getClass().getName() + '.');
		} catch (ClassCastException e) {
			fail(METHOD_NAME + ' ' + MESSAGE_UNEXPECTED + e.getClass().getName() + '.');
		}
	}

	/**
	 * Tests {@link EMFCompareMap#putAll(Collection)} on a non-empty map with an empty collection. Expects the
	 * size and the mappings not to be altered.
	 */
	public void testPutEmptyInNonEmpty() {
		final EMFCompareMap target = new EMFCompareMap();
		final EMFCompareMap source = new EMFCompareMap();

		for (int i = 0; i < KEY_SET.length; i++) {
			target.put(KEY_SET[i], VALUE_SET[i]);
		}
		try {
			target.putAll(source);
			assertEquals("Operation putAll() with an empty map altered its target's size.", KEY_SET.length,
					target.size());
			for (int i = 0; i < KEY_SET.length; i++) {
				assertTrue("Operation putAll() with deleted keys from its target.", target
						.containsKey(KEY_SET[i]));
				assertEquals("Operation putAll() with an empty map altered its target's mappings.",
						VALUE_SET[i], target.get(KEY_SET[i]));
			}
		} catch (UnsupportedOperationException e) {
			fail(METHOD_NAME + ' ' + MESSAGE_UNEXPECTED + e.getClass().getName() + '.');
		} catch (ClassCastException e) {
			fail(METHOD_NAME + ' ' + MESSAGE_UNEXPECTED + e.getClass().getName() + '.');
		}
	}

	/**
	 * Tests {@link EMFCompareMap#putAll(Collection)} on an empty map with a non-empty collection. Expects the
	 * size and the mappings of the target to be equal to the source's.
	 */
	public void testPutNonEmptyInEmpty() {
		final EMFCompareMap target = new EMFCompareMap();
		final EMFCompareMap source = new EMFCompareMap();

		for (int i = 0; i < KEY_SET.length; i++) {
			source.put(KEY_SET[i], VALUE_SET[i]);
		}
		try {
			target.putAll(source);
			assertEquals(
					"Operation putAll() with non-empty map did not insert all the collection's mappings.",
					KEY_SET.length, target.size());
			for (int i = 0; i < KEY_SET.length; i++) {
				assertEquals(
						"Operation putAll() with non-empty map did not insert all mappings in its target.",
						VALUE_SET[i], target.get(KEY_SET[i]));
			}
		} catch (UnsupportedOperationException e) {
			fail(METHOD_NAME + ' ' + MESSAGE_UNEXPECTED + e.getClass().getName() + '.');
		} catch (ClassCastException e) {
			fail(METHOD_NAME + ' ' + MESSAGE_UNEXPECTED + e.getClass().getName() + '.');
		}
	}
}

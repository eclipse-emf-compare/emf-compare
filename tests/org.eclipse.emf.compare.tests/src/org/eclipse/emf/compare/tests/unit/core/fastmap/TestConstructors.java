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
package org.eclipse.emf.compare.tests.unit.core.fastmap;

import java.util.HashSet;

import junit.framework.TestCase;

import org.eclipse.emf.compare.util.FastMap;

/**
 * Tests the behavior of the map's constructors.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings({"unchecked", "nls", })
public class TestConstructors extends TestCase {
	/** These represents invalid initial capacities for the map. */
	private static final int[] INVALID_CAPACITY = {Integer.MIN_VALUE, -10, -2, -1, };

	/** Invalid load factors for the map. */
	private static final float[] INVALID_LOAD = {Float.NEGATIVE_INFINITY, -10f, -5f, -2f, -1f, -0.5f, -0.1f,
			-Float.MIN_VALUE, 0, +0.0f, Float.NaN, };

	/** Input keys for a set map. */
	private static final Object[] KEY_SET = {null, "", "Alize", "Bise", "Boree", "Chinook", "Eurus",
			"Hurricane", "Noroit", "Rafale", "Sirocco", "Tourbillon", "Typhon", new Integer(0), new Long(10),
			new Float(20), new Double(30), new Boolean(false), new HashSet(), 5, 15L, 25f, 35d, '\u00ab',
			true, };

	/** Name displayed before the constructor method names. */
	private static final String MESSAGE_CONSTRUCTOR_NAME = "Constructor Fastmap";

	/** Error message displayed when an expected {@link IllegalArgumentException} isn't thrown. */
	private static final String MESSAGE_ILLEGAL_ARGUMENT = "did not throw IllegalArgumentException exception.";

	/** Error message for a non-empty map creation. */
	private static final String MESSAGE_NON_EMPTY_MAP = "created a non empty map.";

	/** These are valid initial capacities for the map. */
	private static final int[] VALID_CAPACITY = {0, 1, 2, 5, 10, 100, };

	/** These are valid load factors for the map. */
	private static final float[] VALID_LOAD = {Float.MIN_VALUE, 0.1f, 0.5f, 0.75f, 1f, 100f, Float.MAX_VALUE, };

	/** Input values for a set map. */
	private static final Object[] VALUE_SET = {null, "", "Aquilon", "Blizzard", "Brise", "Cyclone", "Foehn",
			"Mistral", "Notus", "Simoon", "Suroit", "Tramontane", "Zephyr", new Integer(0), new Long(10),
			new Float(20), new Double(30), new Boolean(true), new HashSet(), 5, 15L, 25f, 35d, '\u00aa',
			false, };

	/**
	 * Checks that the default constructor creates an empty map.
	 */
	public void testDefaultConstructor() {
		final FastMap map = new FastMap();
		assertTrue(MESSAGE_CONSTRUCTOR_NAME + "()" + ' ' + MESSAGE_NON_EMPTY_MAP, map.isEmpty());
	}

	/**
	 * Tests the constructor {@link FastMap#FastMap(int, float)} with illegal initial capacities and valid
	 * load factors. Expects an {@link IllegalArgumentException} to be thrown.
	 */
	public void testIllegalCapacityValidLoadFactorConstructor() {
		for (int invalidCapacity : INVALID_CAPACITY) {
			for (float validLoadFactor : VALID_LOAD) {
				try {
					new FastMap(invalidCapacity, validLoadFactor);
					fail(MESSAGE_CONSTRUCTOR_NAME + '(' + invalidCapacity + ',' + ' ' + validLoadFactor + ')'
							+ ' ' + MESSAGE_ILLEGAL_ARGUMENT);
				} catch (IllegalArgumentException e) {
					// We expected this
				}
			}
		}
	}

	/**
	 * Tests the constructor {@link FastMap#FastMap(int)} with invalid initial capacities. Expects an
	 * {@link IllegalArgumentException} to be thrown.
	 */
	public void testIllegalInitialCapacityConstructor() {
		for (int invalidCapacity : INVALID_CAPACITY) {
			try {
				new FastMap(invalidCapacity);
				fail(MESSAGE_CONSTRUCTOR_NAME + '(' + invalidCapacity + ')' + ' ' + MESSAGE_ILLEGAL_ARGUMENT);
			} catch (IllegalArgumentException e) {
				// We expected this
			}
		}
	}

	/**
	 * Tests the constructor {@link FastMap#FastMap(Map)} with a non-<code>null</code> map. Expects the
	 * creation of a new map containing all the mappings from the given map.
	 */
	public void testMapConstructor() {
		final FastMap map = new FastMap();

		FastMap testMap = new FastMap(map);
		assertTrue(MESSAGE_CONSTRUCTOR_NAME + "(new FastMap())" + ' ' + MESSAGE_NON_EMPTY_MAP, testMap
				.isEmpty());

		for (int i = 0; i < KEY_SET.length; i++) {
			map.put(KEY_SET[i], VALUE_SET[i]);

			testMap = new FastMap(map);
			assertEquals(MESSAGE_CONSTRUCTOR_NAME + "(map)" + ' ' + "created map with wrong size.",
					i + 1, testMap.size());

			for (int j = 0; j < i; j++) {
				assertEquals("FastMap(Map)" + ' ' + "creates map with wrong mappings.", VALUE_SET[j],
						testMap.get(KEY_SET[j]));
			}
		}
	}

	/**
	 * Tests the constructor {@link FastMap#FastMap(Map)} with a <code>null</code> map. Expects a
	 * {@link NullPointerException} to be thrown.
	 */
	public void testNullMapConstructor() {
		try {
			new FastMap(null);
			fail(MESSAGE_CONSTRUCTOR_NAME + "(null)" + ' ' + "did not throw NullPointerException exception.");
		} catch (NullPointerException e) {
			// We expected this
		}
	}

	/**
	 * Tests the constructor {@link FastMap#FastMap(int, float)} with valid initial capacities and invalid
	 * load factors. Expects an {@link IllegalArgumentException} to be thrown.
	 */
	public void testValidCapacityInvalidLoadFactorConstructor() {
		for (int validCapacity : VALID_CAPACITY) {
			for (float invalidLoadFactor : INVALID_LOAD) {
				try {
					new FastMap(validCapacity, invalidLoadFactor);
					fail(MESSAGE_CONSTRUCTOR_NAME + '(' + validCapacity + ',' + ' ' + invalidLoadFactor + ')'
							+ ' ' + MESSAGE_ILLEGAL_ARGUMENT);
				} catch (IllegalArgumentException e) {
					// We expected this
				}
			}
		}
	}

	/**
	 * Tests the constructor {@link FastMap#FastMap(int, float)} with valid initial capacities and load
	 * factors. Expects an empty map to be created.
	 */
	public void testValidCapacityValidLoadFactorConstructor() {
		for (int validCapacity : VALID_CAPACITY) {
			for (float validLoadFactor : VALID_LOAD) {
				final FastMap map = new FastMap(validCapacity, validLoadFactor);
				assertTrue(MESSAGE_CONSTRUCTOR_NAME + '(' + validCapacity + ',' + ' ' + validLoadFactor + ')'
						+ ' ' + MESSAGE_NON_EMPTY_MAP, map.isEmpty());
			}
		}
	}

	/**
	 * Tests the constructor {@link FastMap#FastMap(int)} with valid initial capacities. Expects an empty map
	 * to be created.
	 */
	public void testValidInitialCapacityConstructor() {
		for (int validCapacity : VALID_CAPACITY) {
			final FastMap map = new FastMap(validCapacity);
			assertTrue(MESSAGE_CONSTRUCTOR_NAME + '(' + validCapacity + ')' + ' ' + MESSAGE_NON_EMPTY_MAP,
					map.isEmpty());
		}
	}
}

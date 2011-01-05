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
package org.eclipse.emf.compare.tests.unit.core.util.emfcomparemap;

import java.util.HashSet;

import junit.framework.TestCase;

import org.eclipse.emf.compare.util.EMFCompareMap;

/**
 * Tests the behavior of {@link EMFCompareMap#get(Object)} and {@link EMFCompareMap#put(Object, Object)} so
 * that they behave the same as their {@link HashMap} counterparts.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings({"unchecked", "nls", })
public class GetPutTest extends TestCase {
	/** Input keys for a set map. */
	private static final Object[] KEY_SET = {null, "", "Alize", "Bise", "Boree", "Chinook", "Eurus",
			"Hurricane", "Noroit", "Rafale", "Sirocco", "Tourbillon", "Typhon", new Integer(0), new Long(10),
			new Float(20), new Double(30), new Boolean(false), new HashSet(), 5, 15L, 25f, 35d, '\u00ab',
			true, };

	/** Input values for a set map. */
	private static final Object[] VALUE_SET = {null, "", "Aquilon", "Blizzard", "Brise", "Cyclone", "Foehn",
			"Mistral", "Notus", "Simoon", "Suroit", "Tramontane", "Zephyr", new Integer(0), new Long(10),
			new Float(20), new Double(30), new Boolean(true), new HashSet(), 5, 15L, 25f, 35d, '\u00aa',
			false, };

	/**
	 * Tests {@link EMFCompareMap#put(Object, Object)} on a non-empty map. Expects
	 * {@link EMFCompareMap#get(Object)} to reflect the accurate mapping added and
	 * {@link EMFCompareMap#put(Object, Object)} to return the previous value mapped to the given key or
	 * <code>null</code> if none.
	 */
	public void testAlreadyStoredPut() {
		EMFCompareMap map = null;
		Object expected = null;

		for (int i = 0; i < KEY_SET.length; i++) {
			map = new EMFCompareMap();
			for (int j = 0; j <= i; j++) {
				map.put(KEY_SET[j], VALUE_SET[j]);
			}

			for (int j = 0; j < VALUE_SET.length; j++) {
				if (j > i) {
					expected = null;
				} else {
					expected = VALUE_SET[j];
				}

				assertEquals("Method put(Object,Object) returns an unexpected value.", expected, map.put(
						KEY_SET[j], VALUE_SET[j]));
				assertEquals(
						"Method put(Object,Object) did not associate the specified value with the specified key.",
						VALUE_SET[j], map.get(KEY_SET[j]));
			}
		}
	}

	/**
	 * Tests {@link EMFCompareMap#get(Object)} on an empty map. Expects <code>null</code> to be returned.
	 */
	public void testEmptyMapGet() {
		final EMFCompareMap map = new EMFCompareMap();
		for (int i = 0; i < KEY_SET.length; i++) {
			assertNull("Method get(Object) returns a value for an empty map.", map.get(KEY_SET[i]));
		}
	}

	/**
	 * Tests {@link EMFCompareMap#put(Object, Object)} on an empty map. Expects
	 * {@link EMFCompareMap#get(Object)} to reflect the accurate mapping added and
	 * {@link EMFCompareMap#put(Object, Object)} to return <code>null</code>.
	 */
	public void testEmptyMapPut() {
		final EMFCompareMap map = new EMFCompareMap();
		for (int i = 0; i < KEY_SET.length; i++) {
			assertNull("Put(Object,Object) returns with non null value when no previous mapping existed.",
					map.put(KEY_SET[i], VALUE_SET[i]));
			assertEquals(
					"Method put(Object,Object) did not associate the specified value with the specified key.",
					VALUE_SET[i], map.get(KEY_SET[i]));
		}
	}

	/**
	 * Tests {@link EMFCompareMap#get(Object)} on a non-empty map with not contained keys. Expects
	 * <code>null</code> to be returned.
	 */
	public void testNotFoundGet() {
		final EMFCompareMap map = new EMFCompareMap();
		for (int i = 0; i < KEY_SET.length; i++) {
			map.put(KEY_SET[i], VALUE_SET[i]);
			for (int j = i + 1; j < KEY_SET.length; j++) {
				assertNull("Method get(Object) returns a value for an uncontained key.", map.get(KEY_SET[j]));
			}
		}
	}

	/**
	 * Tests {@link EMFCompareMap#get(Object)} on a non-empty map with valid keys. Expects the accurate value
	 * to be returned.
	 */
	public void testValidGet() {
		final EMFCompareMap map = new EMFCompareMap();
		for (int i = 0; i < KEY_SET.length; i++) {
			map.put(KEY_SET[i], VALUE_SET[i]);
			for (int j = 0; j <= i; j++) {
				assertEquals("Method get(Object) returns inaccurate value for key " + KEY_SET[j],
						VALUE_SET[j], map.get(KEY_SET[j]));
			}
		}
	}
}

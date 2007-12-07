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

import java.util.HashSet;

import junit.framework.TestCase;

import org.eclipse.emf.compare.util.EMFCompareMap;

/**
 * Tests the behavior of {@link EMFCompareMap#containsKey(Object)} and
 * {@link EMFCompareMap#containsValue(Object)} so that they behave the same as their {@link HashMap}
 * counterparts.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings({"unchecked", "nls", })
public class TestContainsKeyValue extends TestCase {
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
	 * Tests {@link EMFCompareMap#containsKey(Object)} on an empty map. Expects <code>False</code> to be
	 * returned.
	 */
	public void testContainsKeyEmptyMap() {
		final EMFCompareMap map = new EMFCompareMap();
		for (int i = 0; i < KEY_SET.length; i++) {
			assertFalse("Empty maps shouldn't contain mappings.", map.containsKey(KEY_SET[i]));
		}
	}

	/**
	 * Tests {@link EMFCompareMap#containsValue(Object)} on an empty map. Expects <code>False</code> to be
	 * returned.
	 */
	public void testContainsValueEmptyMap() {
		final EMFCompareMap map = new EMFCompareMap();
		for (int i = 0; i < VALUE_SET.length; i++) {
			assertFalse("Empty maps shouldn't contain mappings." + i, map.containsValue(VALUE_SET[i]));
		}
	}

	/**
	 * Tests {@link EMFCompareMap#containsKey(Object)} on a non-empty map with not contained keys. Expects
	 * <code>False</code> to be returned.
	 */
	public void testNotFoundKey() {
		final EMFCompareMap map = new EMFCompareMap();
		for (int i = 0; i < KEY_SET.length; i++) {
			map.put(KEY_SET[i], VALUE_SET[i]);
			for (int j = i + 1; j < KEY_SET.length; j++) {
				assertFalse("ContainsKey returns true for uncontained keys.", map.containsKey(KEY_SET[j]));
			}
		}
	}

	/**
	 * Tests {@link EMFCompareMap#containsValue(Object)} on a non-empty map with not contained values. Expects
	 * <code>False</code> to be returned.
	 */
	public void testNotFoundValue() {
		final EMFCompareMap map = new EMFCompareMap();
		for (int i = 0; i < KEY_SET.length; i++) {
			map.put(KEY_SET[i], VALUE_SET[i]);
			for (int j = i + 1; j < KEY_SET.length; j++) {
				assertFalse("ContainsValue returns true for uncontained values.", map
						.containsValue(VALUE_SET[j]));
			}
		}
	}

	/**
	 * Tests {@link EMFCompareMap#containsKey(Object)} on a non-empty map with contained keys. Expects
	 * <code>True</code> to be returned.
	 */
	public void testValidKey() {
		final EMFCompareMap map = new EMFCompareMap();
		for (int i = 0; i < KEY_SET.length; i++) {
			map.put(KEY_SET[i], VALUE_SET[i]);
		}
		for (int i = 0; i < KEY_SET.length; i++) {
			assertTrue("ContainsKey returns false for contained keys.", map.containsKey(KEY_SET[i]));
		}
	}

	/**
	 * Tests {@link EMFCompareMap#containsValue(Object)} on a non-empty map with contained values. Expects
	 * <code>True</code> to be returned.
	 */
	public void testValidValue() {
		final EMFCompareMap map = new EMFCompareMap();
		for (int i = 0; i < VALUE_SET.length; i++) {
			map.put(KEY_SET[i], VALUE_SET[i]);
		}
		for (int i = 0; i < VALUE_SET.length; i++) {
			assertTrue("ContainsValue returns false for contained values.", map.containsValue(VALUE_SET[i]));
		}
	}
}

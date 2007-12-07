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
 * Tests the behavior of {@link EMFCompareMap#equals(Object)}. Two maps are equal if and only if they have
 * the same size and contain the same mappings. A map cannot be considered equal to anything that isn't an
 * instance of Map itself.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings( {"unchecked", "nls",})
public class TestEquals extends TestCase {
	/** Input keys for a set map. */
	private static final Object[] KEY_SET = {null, "", "Alize", "Bise", "Boree", "Chinook", "Eurus",
			"Hurricane", "Noroit", "Rafale", "Sirocco", "Tourbillon", "Typhon", new Integer(0), new Long(10),
			new Float(20), new Double(30), new Boolean(false), new HashSet(), 5, 15L, 25f, 35d, '\u00ab',
			true,};

	/** Input values for a set map. */
	private static final Object[] VALUE_SET = {null, "", "Aquilon", "Blizzard", "Brise", "Cyclone", "Foehn",
			"Mistral", "Notus", "Simoon", "Suroit", "Tramontane", "Zephyr", new Integer(0), new Long(10),
			new Float(20), new Double(30), new Boolean(true), new HashSet(), 5, 15L, 25f, 35d, '\u00aa',
			false,};

	/**
	 * Tests {@link EMFCompareMap#equals(Object)}.
	 * <p>
	 * Expected results : <table>
	 * <tr>
	 * <td>map</td>
	 * <td>object to be compared with</td>
	 * <td>result</td>
	 * </tr>
	 * <tr>
	 * <td>valid empty map</td>
	 * <td><code>null</code></td>
	 * <td><code>False</code></td>
	 * </tr>
	 * <tr>
	 * <td>valid empty map</td>
	 * <td>any object that isn't instance of Map</td>
	 * <td><code>False</code></td>
	 * </tr>
	 * <tr>
	 * <td>valid empty map</td>
	 * <td>valid non-empty map</td>
	 * <td><code>False</code></td>
	 * </tr>
	 * <tr>
	 * <td>valid empty map</td>
	 * <td>valid empty map</td>
	 * <td><code>True</code></td>
	 * </tr>
	 * <tr>
	 * <td>valid empty map</td>
	 * <td>same instance of map</td>
	 * <td><code>True</code></td>
	 * </tr>
	 * </table>
	 * </p>
	 */
	public void testEqualsEmptyMap() {
		final EMFCompareMap map = new EMFCompareMap();
		final EMFCompareMap testMap = new EMFCompareMap();
		for (int i = 0; i < KEY_SET.length; i++)
			testMap.put(KEY_SET[i], VALUE_SET[i]);

		final Object[] invalidTestObjects = {null, "", "Suroit", new Long(10), new HashSet(), 25f, '\u00aa',
				testMap,};
		final Object[] validTestObjects = {new EMFCompareMap(), map,};

		for (int i = 0; i < invalidTestObjects.length; i++)
			assertFalse("Unexpected result of equals() with non-equal objects.", map
					.equals(invalidTestObjects[i]));
		for (int i = 0; i < validTestObjects.length; i++)
			assertTrue("Unexpected result of equals() with equal objects.", map.equals(validTestObjects[i]));
	}

	/**
	 * Tests {@link EMFCompareMap#equals(Object)}.
	 * <p>
	 * Expected results : <table>
	 * <tr>
	 * <td>map</td>
	 * <td>object to be compared with</td>
	 * <td>result</td>
	 * </tr>
	 * <tr>
	 * <td>valid non-empty map</td>
	 * <td><code>null</code></td>
	 * <td><code>False</code></td>
	 * </tr>
	 * <tr>
	 * <td>valid non-empty map</td>
	 * <td>any object that isn't instance of Map</td>
	 * <td><code>False</code></td>
	 * </tr>
	 * <tr>
	 * <td>valid non-empty map</td>
	 * <td>valid empty map</td>
	 * <td><code>False</code></td>
	 * </tr>
	 * <tr>
	 * <td>valid non-empty map</td>
	 * <td>valid non-empty map with different size</td>
	 * <td><code>False</code></td>
	 * </tr>
	 * <tr>
	 * <td>valid non-empty map</td>
	 * <td>valid non-empty map with same size and different mappings</td>
	 * <td><code>False</code></td>
	 * </tr>
	 * <tr>
	 * <td>valid non-empty map</td>
	 * <td>valid non-empty map with same mappings</td>
	 * <td><code>True</code></td>
	 * </tr>
	 * <tr>
	 * <td>valid non-empty map</td>
	 * <td>same instance of map</td>
	 * <td><code>True</code></td>
	 * </tr>
	 * </table>
	 * </p>
	 * <p>
	 * Also ensures that equal maps have same hashCode.
	 * </p>
	 */
	public void testEqualsNonEmptyMap() {
		final EMFCompareMap map = new EMFCompareMap();
		final EMFCompareMap differentSize = new EMFCompareMap();
		final EMFCompareMap sameSizeDifferentMappings = new EMFCompareMap();
		final EMFCompareMap sameMappings = new EMFCompareMap();
		for (int i = 0; i < KEY_SET.length; i++)
			map.put(KEY_SET[i], VALUE_SET[i]);
		for (int i = 0; i < KEY_SET.length >> 1; i++)
			differentSize.put(KEY_SET[i], VALUE_SET[i]);
		for (int i = 0; i < KEY_SET.length; i++)
			sameSizeDifferentMappings.put(KEY_SET[i], VALUE_SET[VALUE_SET.length - 1 - i]);
		for (int i = 0; i < KEY_SET.length; i++)
			sameMappings.put(KEY_SET[i], VALUE_SET[i]);

		final Object[] invalidTestObjects = {null, "", "Foehn", new Long(10), new HashSet(), 25f, '\u00aa',
				differentSize, sameSizeDifferentMappings,};
		final Object[] validTestObjects = {sameMappings, map,};

		for (int i = 0; i < invalidTestObjects.length; i++)
			assertFalse("Unexpected result of equals() with non-equal objects.", map
					.equals(invalidTestObjects[i]));
		for (int i = 0; i < validTestObjects.length; i++) {
			assertTrue("Unexpected result of equals() with equal objects.", map.equals(validTestObjects[i]));
			assertEquals("Equal maps should return the same hashCode.", map.hashCode(), validTestObjects[i]
					.hashCode());
		}
	}
}

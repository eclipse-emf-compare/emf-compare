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
 * Tests the behavior of {@link EMFCompareMap#remove(Object)} so that it behaves the same as its
 * {@link HashMap} counterpart.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings({"unchecked", "nls", })
public class TestRemove extends TestCase {
	/** Input keys for a set map. */
	private static final Object[] KEY_SET = {null, "", "Alize", "Bise", "Boree", "Chinook", "Eurus",
			"Hurricane", "Noroit", "Rafale", "Sirocco", "Tourbillon", "Typhon", new Integer(0), new Long(10),
			new Float(20), new Double(30), new Boolean(false), new HashSet(), 5, 15L, 25f, 35d, '\u00ab',
			true, };

	/** String displayed when an unexpected {@link UnsupportedOperationException} is thrown. */
	private static final String MESSAGE_UNSUPPORTED = "threw an unexpected UnsupportedOperationException.";

	/** Input values for a set map. */
	private static final Object[] VALUE_SET = {null, "", "Aquilon", "Blizzard", "Brise", "Cyclone", "Foehn",
			"Mistral", "Notus", "Simoon", "Suroit", "Tramontane", "Zephyr", new Integer(0), new Long(10),
			new Float(20), new Double(30), new Boolean(true), new HashSet(), 5, 15L, 25f, 35d, '\u00aa',
			false, };

	/**
	 * Tests {@link EMFCompareMap#remove(Object)} on an empty map. Expects <code>null</code> to be returned.
	 */
	public void testRemoveFromEmptyMap() {
		final EMFCompareMap map = new EMFCompareMap();

		for (int i = 0; i < KEY_SET.length; i++) {
			try {
				assertNull("Unexpected result of remove() operation on an empty map.", map.remove(KEY_SET[i]));
			} catch (UnsupportedOperationException e) {
				fail("Remove()" + ' ' + MESSAGE_UNSUPPORTED);
			}
			assertEquals("Unexpected size of the map after a remove() operation.", 0, map.size());
		}
	}

	/**
	 * Tests {@link EMFCompareMap#remove(Object)} on a non-empty map. Expects it to return the old value if
	 * present.
	 */
	public void testRemoveIfPresent() {
		for (int i = 0; i < KEY_SET.length; i++) {
			for (int j = 0; j < KEY_SET.length; j++) {
				final EMFCompareMap map = new EMFCompareMap();

				// We'll progressively add more and more mappings to the tested map
				for (int k = 0; k <= i; k++) {
					map.put(KEY_SET[k], VALUE_SET[k]);
				}

				Object result = null;
				try {
					result = map.remove(KEY_SET[j]);
				} catch (UnsupportedOperationException e) {
					fail("Remove()" + ' ' + MESSAGE_UNSUPPORTED);
				}

				if (j <= i) {
					assertFalse("Remove() didn't deleted necessary mapping from the map.", map
							.containsKey(KEY_SET[j]));
					assertEquals("Remove() didn't return accurate value.", VALUE_SET[j], result);
				} else {
					assertNull(result);
				}

				for (int k = 0; k < i; k++) {
					if (k != j) {
						assertTrue("Remove() deleted innacurate mapping from the map.", map
								.containsKey(KEY_SET[k]));
						assertEquals("Unexpected result of the remove() operation.", VALUE_SET[k], map
								.get(KEY_SET[k]));
					}
				}
			}
		}
	}
}

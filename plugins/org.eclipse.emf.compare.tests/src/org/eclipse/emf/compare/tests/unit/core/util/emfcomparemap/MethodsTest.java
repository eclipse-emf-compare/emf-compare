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
 * Tests the behavior of {@link EMFCompareMap#clear()}, {@link EMFCompareMap#clone()},
 * {@link EMFCompareMap#size()} and {@link EMFCompareMap#isEmpty()} so that they behave the same as their
 * {@link HashMap} counterparts.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings({"unchecked", "nls", })
public class MethodsTest extends TestCase {
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

	/** Map that will be used for all these tests. */
	private final EMFCompareMap testedMap = new EMFCompareMap();

	/**
	 * Tests {@link EMFCompareMap#clear()} with sizes growing from 0 to 12 elements. Expects the size to be
	 * reverted back to 0 after each execution of clear().
	 */
	public void testClear() {
		try {
			testedMap.clear();
		} catch (UnsupportedOperationException e) {
			fail("Clear()" + ' ' + MESSAGE_UNSUPPORTED);
		}

		assertEquals("Map hasn't been emptied by clear() operation or size() returns inaccurate result.", 0,
				testedMap.size());
		for (int j = 0; j < KEY_SET.length; j++) {
			assertFalse("Values contained by the map weren't nulled out by clear().", testedMap
					.containsKey(KEY_SET[j]));
			assertNull("Values contained by the map weren't nulled out by clear().", testedMap
					.get(KEY_SET[j]));
		}
	}

	/**
	 * Tests {@link EMFCompareMap#clone()} and {@link EMFCompareMap#size()}.
	 * <p>
	 * <ul>
	 * Assertions :
	 * <li>Clone() returns non-null map.</li>
	 * <li>Clone() returns an instance of {@link EMFCompareMap}.</li>
	 * <li>Original and cloned map both have the same size.</li>
	 * <li>Original and cloned map contain the same mappings.</li>
	 * </ul>
	 * </p>
	 */
	public void testCloneSize() {
		EMFCompareMap clonedMap = null;
		try {
			clonedMap = (EMFCompareMap)(testedMap.clone());
		} catch (ClassCastException e) {
			fail("Result of clone() was not an instance of EMFCompareMap.");
		}

		assertNotNull("Result of clone() was null.", clonedMap);
		// Keeps compiler happy
		assert clonedMap != null;
		assertEquals("Result of clone() hasn't the same size as its original.", testedMap.size(), clonedMap
				.size());

		for (int i = 0; i < KEY_SET.length; i++) {
			assertTrue("Cloned map doesn't contain all the keys of its original.", clonedMap
					.containsKey(KEY_SET[i]));
			assertEquals("Result of clone() hasn't the same mappings as its original.", VALUE_SET[i],
					clonedMap.get(KEY_SET[i]));
		}
	}

	/**
	 * Tests {@link EMFCompareMap#hashCode()} to ensure it always produces the same hash code for two maps
	 * that have the same size and contain the same mappings.
	 */
	public void testHashCode() {
		final EMFCompareMap map1 = new EMFCompareMap();
		final EMFCompareMap map2 = new EMFCompareMap();
		assertEquals("Two empty maps should have the same hash code.", map1.hashCode(), map2.hashCode());

		// progressively add mappings to both maps
		for (int i = 0; i < KEY_SET.length; i++) {
			map1.put(KEY_SET[i], VALUE_SET[i]);
			// hashCode() doesn't necessarily produce distinct result for non-equal maps,
			// yet we try to reduce the probability of this happening.
			assertNotSame("Two distinct maps' hashCode() shouldn't produce the same result.",
					map1.hashCode(), map2.hashCode());

			map2.put(KEY_SET[i], VALUE_SET[i]);
			assertTrue("Unexpected behavior of put().", map1.equals(map2));
			assertEquals("Two identical maps' hashCode() didn't produce the same result.", map1.hashCode(),
					map2.hashCode());
		}

		// progressively removes mappings from both maps
		for (int i = 0; i < KEY_SET.length; i++) {
			map1.put(KEY_SET[i], VALUE_SET[i]);
			map2.put(KEY_SET[i], VALUE_SET[i]);
			assertEquals("Two identical maps' hashCode() didn't produce the same result.", map1.hashCode(),
					map2.hashCode());
		}

		// progressively populate two maps with same keys, different values
		for (int i = 0; i < KEY_SET.length; i++) {
			map1.put(KEY_SET[i], VALUE_SET[i]);
			map2.put(KEY_SET[i], VALUE_SET[VALUE_SET.length - 1 - i]);
			// hashCode() doesn't necessarily produce distinct result for non-equal maps,
			// yet we try to reduce the probability of this happening.
			assertNotSame("Two different maps' hashCode() produced the same result.", map1.hashCode(), map2
					.hashCode());
		}
	}

	/**
	 * Tests {@link EMFCompareMap#isEmpty()}.
	 * <p>
	 * <ul>
	 * Assertions :
	 * <li>Empty map =&gt; isEmpty() returns <code>True</code>.</li>
	 * <li>After put() operation =&gt; isEmpty() returns <code>False</code>.</li>
	 * <li>Cleared map =&gt; isEmpty() returns <code>True</code>.</li>
	 * </ul>
	 * </p>
	 */
	public void testIsEmpty() {
		assertTrue("Method isEmpty() returns false on new map.", new EMFCompareMap().isEmpty());
		assertFalse("Method isEmpty() returns true on non-empty map.", testedMap.isEmpty());
		testedMap.clear();
		assertTrue("Unexpected result of isEmpty() on cleared map.", testedMap.isEmpty());
	}

	/**
	 * We cannot really test the output of the toString() method, the main purpose of this test is to ensure
	 * it throws no exception. Result of toString() is expected to be neither <code>null</code> nor empty.
	 */
	public void testToString() {
		for (int i = 0; i < KEY_SET.length; i++) {
			final EMFCompareMap map = new EMFCompareMap();
			for (int j = 0; j < i; j++) {
				map.put(KEY_SET[j], VALUE_SET[j]);
			}
			final String toString = map.toString();
			assertNotNull("Operation toString() returned null result.", toString);
			assertNotSame("Operation toString() on the map returned empty String.", toString, "");
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

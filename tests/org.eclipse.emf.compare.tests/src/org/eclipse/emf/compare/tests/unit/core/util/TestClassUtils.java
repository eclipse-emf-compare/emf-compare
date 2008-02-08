/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.unit.core.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import junit.framework.TestCase;

import org.eclipse.emf.compare.util.ClassUtils;

/**
 * Tests for the utility methods offered by {@link ClassUtils}.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings("nls")
public class TestClassUtils extends TestCase {
	/** These are all the primitive classes. */
	private static final Class<?>[] PRIMITIVES = {short.class, int.class, long.class, byte.class, char.class,
			boolean.class, float.class, double.class, void.class, };

	/** These are all the wrapper classes for primitive types. */
	private static final Class<?>[] WRAPPERS = {Short.class, Integer.class, Long.class, Byte.class,
			Character.class, Boolean.class, Float.class, Double.class, Void.class, };

	/**
	 * Tests
	 * {@link ClassUtils#classEquals(Class, Class) with classes assumed non-equal. Expects <code>False</code> to be returned. 
	 */
	public void testClassEqualsDistinctClasses() {
		for (int i = 0; i < PRIMITIVES.length; i++) {
			// Special case when we are right at the middle of the input
			if ((PRIMITIVES.length & 1) == 1 && i == (PRIMITIVES.length >> 1))
				assertFalse("Distinct classes shouldn't be considered equal.", ClassUtils.classEquals(
						PRIMITIVES[i], Object.class));
			else
				assertFalse("Distinct classes shouldn't be considered equal." + i, ClassUtils.classEquals(
						PRIMITIVES[i], WRAPPERS[WRAPPERS.length - i - 1]));
		}
	}

	/**
	 * Tests
	 * {@link ClassUtils#classEquals(Class, Class) with classes assumed equal. Expects <code>True</code> to be returned. 
	 */
	public void testClassEqualsEqualClasses() {
		for (Class<?> clazz : WRAPPERS) {
			assertTrue("Class considered different than itself.", ClassUtils.classEquals(clazz, clazz));
		}
		for (Class<?> clazz : PRIMITIVES) {
			assertTrue("Class considered different than itself.", ClassUtils.classEquals(clazz, clazz));
		}
		for (int i = 0; i < WRAPPERS.length; i++) {
			assertTrue("A primitive should be considered equal to its wrapper.", ClassUtils.classEquals(
					WRAPPERS[i], PRIMITIVES[i]));
		}
	}

	/**
	 * Tests
	 * {@link ClassUtils#classEquals(Class, Class) with <code>null</code> as one of the tested classes. Expects a {@link NullPointerException}
	 * to be thrown.
	 */
	public void testClassEqualsNullClass() {
		try {
			ClassUtils.classEquals(null, String.class);
			fail("Expected IllegalArgumentException hasn't been thrown by classEquals.");
		} catch (IllegalArgumentException e) {
			// We were expectin this
		}
		try {
			ClassUtils.classEquals(String.class, null);
			fail("Expected IllegalArgumentException hasn't been thrown by classEquals.");
		} catch (IllegalArgumentException e) {
			// We were expectin this
		}
	}

	/**
	 * Tests {@link ClassUtils#hasMethod(Class, String, Class...)} with a method name and parameters known to
	 * be invalid for the receiver. Expects <code>False</code> to be returned.
	 */
	public void testHasMethodInvalidMethod() {
		for (Method method : Object.class.getDeclaredMethods()) {
			if (Modifier.isPublic(method.getModifiers())) {
				assertFalse("Object declared an unknown method for hasMethod.", ClassUtils.hasMethod(
						Object.class, new StringBuilder(method.getName()).reverse().toString(), method
								.getParameterTypes()));
			} else {
				assertFalse("Method hasMethod should return false when seeking a non-public method.",
						ClassUtils.hasMethod(Object.class, method.getName(), method.getParameterTypes()));
			}
		}
	}

	/**
	 * Tests {@link ClassUtils#hasMethod(Class, String, Class...)} with <code>null</code> as the receiver
	 * class. Expects a {@link NullPointerException} to be thrown.
	 */
	public void testHasMethodNullClass() {
		try {
			ClassUtils.hasMethod(null, "");
			fail("Expected NullPointerException hasn't been thrown by hasMethod.");
		} catch (NullPointerException e) {
			// This was expected behavior
		}
	}

	/**
	 * Tests {@link ClassUtils#hasMethod(Class, String, Class...)} with a method name and parameters known to
	 * exist in the receiver. Expects <code>True</code> to be returned.
	 */
	public void testHasMethodValidMethod() {
		for (Method method : Object.class.getMethods()) {
			assertTrue("A String did not present a method declared on Object for hasMethod.", ClassUtils
					.hasMethod(String.class, method.getName(), method.getParameterTypes()));
		}
	}

	/**
	 * Tests {@link ClassUtils#invokeMethod(Object, String, Object...)} with a method name and parameters
	 * known to be invalid for the receiver. Expects <code>null</code> to be returned.
	 */
	public void testInvokeMethodInvalidMethod() {
		final String testObject = "This shall be the tested String";
		assertNull("Invocation of an inexistant method returned non-null value.", ClassUtils.invokeMethod(
				testObject, "doesNotExistOnString"));
		assertNull("Invocation of an inexistant method returned non-null value.", ClassUtils.invokeMethod(
				testObject, "gnirtsbus", 5, new Integer(10)));
		assertNull("Invocation of an inexistant method returned successfully.", ClassUtils.invokeMethod(
				testObject, "", new Object()));
	}

	/**
	 * Tests {@link ClassUtils#invokeMethod(Object, String, Object...)} with <code>null</code> as the
	 * receiver Object. Expects a {@link NullPointerException} to be thrown.
	 */
	public void testInvokeMethodNullObject() {
		try {
			ClassUtils.invokeMethod(null, "");
			fail("Expected NullPointerException hasn't been thrown by invokeMethod.");
		} catch (NullPointerException e) {
			// We were expectin this
		}
		try {
			ClassUtils.invokeMethod(null, "", new String(), new Object(), new Integer(5));
			fail("Expected NullPointerException hasn't been thrown by invokeMethod.");
		} catch (NullPointerException e) {
			// We were expectin this
		}
	}

	/**
	 * Tests {@link ClassUtils#invokeMethod(Object, String, Object...)} on a String with existing methods to
	 * ensure that the results are the same as the results of direct invocation.
	 */
	public void testInvokeMethodValidMethod() {
		final String testObject = "This shall be the tested String";
		try {
			final int startIndex = ((Integer)ClassUtils.invokeMethod(testObject, "indexOf", "tested"))
					.intValue();
			assertSame(
					"Invocation of String#indexOf(String) via invokeMethod did not return expected result.",
					testObject.indexOf("tested"), startIndex);
			final int length = ((Integer)ClassUtils.invokeMethod(testObject, "length")).intValue();
			assertSame("Invocation of String#Length() via invokeMethod did not return expected result.",
					testObject.length(), length);
			final String result = (String)ClassUtils
					.invokeMethod(testObject, "substring", startIndex, length);
			assertEquals(
					"Invocation of String#subString(int, int) via invokeMethod did not return expected result.",
					testObject.substring(startIndex, length), result);
		} catch (ClassCastException e) {
			e.printStackTrace();
			fail("Invocation via invokeMethod returned a result of an unexpected type.");
		}
	}
}

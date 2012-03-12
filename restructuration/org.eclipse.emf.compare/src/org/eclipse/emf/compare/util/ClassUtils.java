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
package org.eclipse.emf.compare.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.emf.compare.EMFCompareMessages;

/**
 * Provides utility methods to get information on {@link Class} objects.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class ClassUtils {
	/** These are all the primitive classes. */
	private static final Class<?>[] PRIMITIVES = {short.class, int.class, long.class, byte.class, char.class,
			boolean.class, float.class, double.class, void.class, };

	/** These are all the wrapper classes for primitive types. */
	private static final Class<?>[] WRAPPERS = {Short.class, Integer.class, Long.class, Byte.class,
			Character.class, Boolean.class, Float.class, Double.class, Void.class, };

	/**
	 * Utility classes don't need to (and shouldn't) be instantiated.
	 */
	private ClassUtils() {
		// prevents instantiation
	}

	/**
	 * This will test equality between two classes.
	 * <p>
	 * We'll assume that primitives types are equal to their wrappers so that
	 * <code>parameterEquals(java.lang.Integer, int)</code> returns <code>True</code>.
	 * </p>
	 * 
	 * @param class1
	 *            First of the two classes to test for equality.
	 * @param class2
	 *            Second of the two classes to test for equality.
	 * @return <code>True</code> if the two classes are considered equal, <code>False</code> otherwise.
	 */
	public static boolean classEquals(Class<?> class1, Class<?> class2) {
		if (class1 == null || class2 == null)
			throw new IllegalArgumentException(EMFCompareMessages.getString("ClassUtils.NullArguments")); //$NON-NLS-1$

		boolean result = false;
		if (class1 != class2 && !class1.equals(class2)) {
			if (class1.isPrimitive()) {
				for (int i = 0; i < PRIMITIVES.length; i++) {
					if (class1.equals(PRIMITIVES[i])) {
						result = class2.equals(WRAPPERS[i]);
						break;
					}
				}
			} else if (class2.isPrimitive()) {
				for (int i = 0; i < PRIMITIVES.length; i++) {
					if (class2.equals(PRIMITIVES[i])) {
						result = class1.equals(WRAPPERS[i]);
						break;
					}
				}
			}
		} else {
			result = true;
		}
		return result;
	}

	/**
	 * Iterates through a given {@link Class} public {@link Method}s to determine if it declares a method of
	 * the given name and parameters.
	 * 
	 * @param clazz
	 *            {@link Class} to visit.
	 * @param methodName
	 *            Name of the method we search for.
	 * @param parameters
	 *            Types of the researched method parameters.
	 * @return <code>True</code> if the given {@link Class} object declares a {@link Method} called
	 *         <code>methodName</code>.
	 */
	public static boolean hasMethod(Class<?> clazz, String methodName, Class<?>... parameters) {
		return getMethod(clazz, methodName, parameters) != null;
	}

	/**
	 * Convenience method to reflectively invoke a given method on the given object with the given parameters.
	 * This implementation takes care of checking if the method exists beforehand and will return
	 * <code>null</code> if it does not.
	 * 
	 * @param object
	 *            Invocation target.
	 * @param methodName
	 *            Name of the method to invoke.
	 * @param parameters
	 *            Parameters with which to invoke the method.
	 * @return Result of the invocation or <code>null</code> if the method does not exist according to
	 *         {@link #hasMethod(Class, String, Class...)}.
	 */
	public static Object invokeMethod(Object object, String methodName, Object... parameters) {
		Object result = null;
		final Class<?>[] parameterTypes = new Class<?>[parameters.length];
		for (int i = 0; i < parameters.length; i++)
			parameterTypes[i] = parameters[i].getClass();

		try {
			if (hasMethod(object.getClass(), methodName, parameterTypes)) {
				final Method method = getMethod(object.getClass(), methodName, parameterTypes);
				result = method.invoke(object, parameters);
			}
		} catch (InvocationTargetException e) {
			// Should never be here, check ClassUtils#hasMethod(Class, String)
			assert false;
		} catch (IllegalAccessException e) {
			// Should never be here, check ClassUtils#hasMethod(Class, String)
			assert false;
		}
		return result;
	}

	/**
	 * Iterates through a given {@link Class} public {@link Method}s to determine if it declares a method of
	 * the given name and parameters and returns it if it does.
	 * 
	 * @param clazz
	 *            {@link Class} to visit.
	 * @param methodName
	 *            Name of the method we search for.
	 * @param parameters
	 *            Types of the researched method parameters.
	 * @return The method we were searching for if existing, <code>null</code> otherwise.
	 */
	private static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameters) {
		Method soughtMethod = null;
		/*
		 * We use Class#getMethods() instead of Class#getDeclaredMethods() to avoid protected, private and
		 * default (package) methods.
		 */
		final Method[] methods = clazz.getMethods();
		for (final Method method : methods) {
			if (method.getName().equals(methodName)) {
				final Class<?>[] params = method.getParameterTypes();
				if (parameters.length == params.length) {
					soughtMethod = method;
					for (int i = 0; i < params.length; i++) {
						if (!classEquals(parameters[i], params[i])) {
							soughtMethod = null;
							break;
						}
					}
					if (soughtMethod != null)
						break;
				}
			}
		}
		return soughtMethod;
	}
}

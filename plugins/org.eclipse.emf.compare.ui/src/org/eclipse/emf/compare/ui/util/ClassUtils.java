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
package org.eclipse.emf.compare.ui.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Provides utility methods to get information on {@link Class} objects.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public final class ClassUtils {
	/**
	 * Utility classes don't need to be instantiated.
	 */
	private ClassUtils() {
		// prevents instantiation
	}
	
	/**
	 * Iterates through a given {@link Class} public {@link Method}s to determine
	 * if it declares a method of the given name.
	 * 
	 * @param clazz
	 * 			{@link Class} to visit.
	 * @param methodName
	 * 			Name of the method we serach for.
	 * @return
	 * 			<code>True</code> if the given {@link Class} object declares a
	 * 			{@link Method} called <code>methodName</code>.
	 */
	public static boolean hasMethod(Class clazz, String methodName) {
		boolean hasMethod = false;
		/* We use Class#getMethods() instead of Class#getDeclaredMethods()
		 * to avoid protected, private and default (package) methods.
		 */
		final Method[] methods = clazz.getMethods();
		for (final Method method : methods) {
			if (method.getName().equals(methodName)) {
				hasMethod = true;
			}
		}
		return hasMethod;
	}
	
	/**
	 * Convenience method to reflectively fetch the value of an attribute of a 
	 * given {@link Object} given its getter name.
	 * 
	 * @param object
	 * 			Invocation target. 
	 * @param methodName
	 * 			Name of the method to invoke.
	 * @return
	 * 			Result of the invocation.
	 */
	public static Object invokeMethod(Object object, String methodName) {
		Object result = null;
		try {
			if (hasMethod(object.getClass(), methodName)) {
				final Method method = object.getClass().getMethod(methodName, new Class[]{});
				result = method.invoke(object, new Object[]{});
			}
		} catch (NoSuchMethodException e) {
			// Should never be here, check ClassUtils#hasMethod(Class, String)
			assert false;
		} catch (InvocationTargetException e) {
			// Should never be here, check ClassUtils#hasMethod(Class, String)
			assert false;
		} catch (IllegalAccessException e) {
			// Should never be here, check ClassUtils#hasMethod(Class, String)
			assert false;
		}
		return result;
	}
}

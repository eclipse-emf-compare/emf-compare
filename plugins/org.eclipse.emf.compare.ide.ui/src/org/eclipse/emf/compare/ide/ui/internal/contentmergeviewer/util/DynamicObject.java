/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util;

import static com.google.common.base.Throwables.propagate;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Utility class to access (R/W) field in super class hierarchy.
 * <p>
 * It has decent performance as it LRU-caches reflective call.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class DynamicObject {

	private final Cache<String, Field> fFieldCache;

	private final Cache<MethodInfo, Method> fMethodCache;

	private final Object fTarget;

	private final Class<?> fTargetClass;

	/**
	 * 
	 */
	public DynamicObject(Object target) {
		fTarget = target;
		fTargetClass = target.getClass();
		fFieldCache = CacheBuilder.newBuilder().maximumSize(16).build(new CacheLoader<String, Field>() {
			@Override
			public Field load(String key) throws Exception {
				return getField(fTargetClass, key);
			}
		});
		fMethodCache = CacheBuilder.newBuilder().maximumSize(16).build(new CacheLoader<MethodInfo, Method>() {
			@Override
			public Method load(MethodInfo key) throws Exception {
				return getMethod(fTargetClass, key);
			}

		});
	}

	public Object get(String fieldName) {
		Object ret = null;
		try {
			Field field = fFieldCache.get(fieldName);
			ret = field.get(fTarget);
		} catch (Exception e) {
			handleException(e);
		}
		return ret;
	}

	public void set(String fieldName, Object value) {
		try {
			Field field = fFieldCache.get(fieldName);
			field.set(fTarget, value);
		} catch (Exception e) {
			handleException(e);
		}
	}

	public double getDouble(String fieldName) {
		double ret = Double.NaN;
		try {
			Field field = fFieldCache.get(fieldName);
			ret = field.getDouble(fTarget);
		} catch (Exception e) {
			handleException(e);
		}
		return ret;
	}

	public void setDouble(String fieldName, double value) {
		try {
			Field field = fFieldCache.get(fieldName);
			field.setDouble(fTarget, value);
		} catch (Exception e) {
			handleException(e);
		}
	}

	public int getInt(String fieldName) {
		int ret = 0;
		try {
			Field field = fFieldCache.get(fieldName);
			ret = field.getInt(fTarget);
		} catch (Exception e) {
			handleException(e);
		}
		return ret;
	}

	public void setInt(String fieldName, int value) {
		try {
			Field field = fFieldCache.get(fieldName);
			field.setInt(fTarget, value);
		} catch (Exception e) {
			handleException(e);
		}
	}

	public Optional<?> invokeMethod(String methodName, Class<?>[] types, Object... args) {
		MethodInfo info = new MethodInfo(methodName, types);
		Optional<?> value = Optional.absent();
		try {
			Method method = fMethodCache.get(info);
			value = Optional.fromNullable(method.invoke(fTarget, args));
		} catch (ExecutionException e) {
			handleException(e);
		} catch (IllegalArgumentException e) {
			handleException(e);
		} catch (IllegalAccessException e) {
			handleException(e);
		} catch (InvocationTargetException e) {
			handleException(e);
		}
		return value;
	}

	/**
	 * @param cause
	 */
	protected void handleException(Throwable cause) {
		propagate(cause);
	}

	private Method getMethod(Class<?> clazz, MethodInfo info) {
		try {
			Method declaredMethod = clazz.getDeclaredMethod(info.methodName, info.parameterTypes);
			makeAccessible(declaredMethod);
			return declaredMethod;
		} catch (SecurityException e) {
			propagate(e);
		} catch (NoSuchMethodException e) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass == null) {
				Method method = getMethodFromInterfaces(clazz, info);
				if (method == null) {
					propagate(e);
				} else {
					return method;
				}
			} else {
				return getMethod(superClass, info);
			}
		}
		return null;
	}

	private Method getMethodFromInterfaces(Class<?> clazz, MethodInfo info) {
		Method method = null;
		Class<?>[] interfaces = clazz.getInterfaces();
		for (int i = 0; i < interfaces.length && method == null; i++) {
			Class<?> iface = interfaces[i];
			method = getMethod(iface, info);
		}
		return method;
	}

	private static Field getField(Class<?> clazz, String fieldName) {
		try {
			Field declaredField = clazz.getDeclaredField(fieldName);
			makeAccessible(declaredField);
			return declaredField;
		} catch (NoSuchFieldException e) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass == null) {
				propagate(e);
			} else {
				return getField(superClass, fieldName);
			}
		}
		return null;
	}

	private static void makeAccessible(Member member) {
		if (!Modifier.isPublic(member.getModifiers())
				|| !Modifier.isPublic(member.getDeclaringClass().getModifiers())) {
			if (member instanceof AccessibleObject) {
				((AccessibleObject)member).setAccessible(true);
			}
		}
	}

	private static class MethodInfo {
		private final String methodName;

		private final Class<?>[] parameterTypes;

		/**
		 * 
		 */
		public MethodInfo(String methodName, Class<?>[] parameterTypes) {
			this.methodName = methodName;
			this.parameterTypes = parameterTypes;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return 31 * Arrays.hashCode(parameterTypes) + Objects.hashCode(methodName);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof MethodInfo) {
				MethodInfo that = (MethodInfo)obj;
				return Objects.equal(methodName, that.methodName)
						&& Arrays.equals(parameterTypes, that.parameterTypes);
			}
			return super.equals(obj);
		}
	}
}

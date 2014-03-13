/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Utility class to access (R/W) field in super class hierarchy.
 * <p>
 * It has decent performance as it LRU-caches reflective call.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class DynamicObject {

	private final LoadingCache<String, Field> fFieldCache;

	private final LoadingCache<MethodKey, Method> fMethodCache;

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
		fMethodCache = CacheBuilder.newBuilder().maximumSize(16).build(new CacheLoader<MethodKey, Method>() {
			@Override
			public Method load(MethodKey key) throws Exception {
				return getMethod(fTargetClass, key.methodName, key.parameterTypes);
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

	public Object invoke(String methodName, Class<?>[] parameterTypes, Object... args) {
		Object ret = null;
		try {
			Method method = fMethodCache.get(MethodKey.create(methodName, parameterTypes));
			ret = method.invoke(fTarget, args);
		} catch (Exception e) {
			handleException(e);
		}
		return ret;
	}

	/**
	 * @param cause
	 */
	protected void handleException(Throwable cause) {
		propagate(cause);
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

	private static Method getMethod(Class<?> clazz, String name, Class<?>[] parameterTypes) {
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(name, parameterTypes);
		} catch (NoSuchMethodException nsme) {
			if (!clazz.isInterface()) {
				Class<?> superClass = clazz.getSuperclass();
				if (superClass != null) {
					method = getMethod(superClass, name, parameterTypes);
				}
			}

			if (method == null) {
				for (Class<?> superInterface : clazz.getInterfaces()) {
					method = getMethod(superInterface, name, parameterTypes);
					if (method != null) {
						break;
					}
				}
			}
		} catch (Exception e) {
			propagate(e);
		}

		if (method != null && !method.isAccessible()) {
			makeAccessible(method);
		}

		return method;
	}

	private static void makeAccessible(Member member) {
		final boolean instanceOfAccesibleAndNotAccessible = member instanceof AccessibleObject
				&& !((AccessibleObject)member).isAccessible();
		if (instanceOfAccesibleAndNotAccessible || !Modifier.isPublic(member.getModifiers())
				|| !Modifier.isPublic(member.getDeclaringClass().getModifiers())) {
			if (member instanceof AccessibleObject) {
				((AccessibleObject)member).setAccessible(true);
			} else {
				throw new RuntimeException("Can not set accessible " + member); //$NON-NLS-1$
			}
		}
	}

	private static class MethodKey {
		final String methodName;

		final Class<?>[] parameterTypes;

		private MethodKey(String methodName, Class<?>[] parameterTypes) {
			this.methodName = methodName;
			this.parameterTypes = parameterTypes;
		}

		static MethodKey create(String methodName, Class<?>[] parameterTypes) {
			return new MethodKey(methodName, parameterTypes);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
			result = prime * result + Arrays.hashCode(parameterTypes);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			MethodKey other = (MethodKey)obj;
			if (methodName == null) {
				if (other.methodName != null) {
					return false;
				}
			} else if (!methodName.equals(other.methodName)) {
				return false;
			}
			if (!Arrays.equals(parameterTypes, other.parameterTypes)) {
				return false;
			}
			return true;
		}

	}
}

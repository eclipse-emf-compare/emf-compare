/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.utils;

import com.google.common.base.Joiner;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility method useable on any {@link Object}.
 * <p>
 * Mostly answers the same interfaces as Guava's "Objects", but re-implemented here to avoid deprecation
 * issues.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 3.5
 */
public final class Objects {
	/** hides default constructor. */
	private Objects() {
		// prevents instantiation
	}

	/**
	 * Creates a builder for the {@code toString()} of the given object.
	 * 
	 * @param object
	 *            The object we'll need a string representation of.
	 * @return A builder for the {@code toString()} of the given object.
	 */
	public static ToStringHelper toStringHelper(Object object) {
		return new ToStringHelper(object);
	}

	/**
	 * Fluent interface to build a String representation of an object following the same format as guava's
	 * Objects.toStringHelper().
	 * <p>
	 * Resulting toString() will look like &lt;className>{attribute=value, attribute1=value1} according to the
	 * number of attributes that have been {@link #add(String, String) added}.
	 * </p>
	 */
	public static class ToStringHelper {
		/** Target of this toString builder. */
		private Object target;

		/** attribute=>value mappings that we'll add to the string representation. */
		private Map<String, String> values;

		/**
		 * Builds a toStringHelper for the given target object.
		 * 
		 * @param target
		 *            Object for which we'll need a string representation.
		 */
		public ToStringHelper(Object target) {
			this.target = target;
			this.values = new LinkedHashMap<>();
		}

		/**
		 * Adds the given key/value pair that needs to appear in the final string representation.
		 * <p>
		 * Key/value pairs will appear in the final representation in the same order they have been added
		 * through here.
		 * </p>
		 * 
		 * @param name
		 *            Name of the pair.
		 * @param value
		 *            Value of the pair.
		 * @return {@code this} instance.
		 */
		public ToStringHelper add(String name, Object value) {
			if (value == null) {
				values.put(name, "null");
			} else {
				values.put(name, value.toString());
			}
			return this;
		}

		@Override
		public String toString() {
			StringBuilder result = new StringBuilder();
			result.append(target.getClass().getName());
			result.append('{');
			Joiner.on(',').withKeyValueSeparator("=").appendTo(result, values);
			result.append('}');
			return result.toString();
		}
	}
}

package org.eclipse.emf.compare.adapterfactory.context;

import java.util.Map;

import org.eclipse.emf.compare.Comparison;

/**
 * An abstract implementation of {@link IContextTester} that provides convenience methods for retrieving
 * values from the context map.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 * @since 4.3
 */
public abstract class AbstractContextTester implements IContextTester {

	/**
	 * Returns the comparison stored under the {@link IContextTester#CTX_COMPARISON} key.
	 * 
	 * @param context
	 *            context map
	 * @return comparison or null, if no comparison is stored in the context
	 */
	protected Comparison getComparison(Map<Object, Object> context) {
		return getValue(context, CTX_COMPARISON, Comparison.class);
	}

	/**
	 * Returns the value of the context map stored under the given key, if it is an instance of the given
	 * class.
	 * 
	 * @param context
	 *            context map
	 * @param key
	 *            the key for the context map
	 * @param expectedClass
	 *            expected class for the value stored under the key
	 * @param <T>
	 *            expected type of the value stored under the key
	 * @return stored value or null, if no matching value is stored under the key
	 */
	protected static <T> T getValue(Map<Object, Object> context, Object key, Class<T> expectedClass) {
		if (context == null || key == null || expectedClass == null) {
			return null;
		}
		Object value = context.get(key);
		if (value != null && expectedClass.isInstance(value)) {
			return expectedClass.cast(value);
		}
		return null;
	}
}

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
package org.eclipse.emf.compare.tests.unit.core;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Locale;

import junit.framework.TestCase;

import org.eclipse.emf.compare.EMFCompareMessages;

/**
 * Tests Messages class. These tests successful completion heavily depends on org.eclipse.emf.compare
 * "messages.properties" file contents.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings({"unchecked", "nls", })
public class TestMessages extends TestCase {
	/** Expected result of the parameterisable keys (only used if locale is en). */
	private final String[] expectedForParameterisable = {"The feature {0} does not exist in {1}.",
			"Illegal load factor for map: {0}.", };

	/** Contains the expected results for the valid keys (only used if locale is en). */
	private final String[] expectedForValidKeys = {"Required element not found.",
			"A java exception has been thrown.", "Unknown EMF Compare problem.", };

	/** These will be used when testing message retrieval with invalid keys. */
	private final String[] invalidKeys = {"invalidKey", "EMFComparePlugin.ElementNotFound1", "", "\u00ec", };

	/** Contains possible parameters for the messages. */
	private final Object[] messageParameters = {null, "", "Foehn", -1, new Long(10), '\u0043', new HashSet(), };

	/** These two are valid, parameterisable keys. See org.eclipse.emf.compare "messages.properties". */
	private final String[] parameterisableKeys = {"EFactory.FeatureNotFound",
			"EMFCompareMap.IllegalLoadFactor", };

	/** These are valid, un-parameterisable keys. See org.eclipse.emf.compare "messages.properties". */
	private final String[] validKeys = {"EMFComparePlugin.ElementNotFound", "EMFComparePlugin.JavaException",
			"EMFComparePlugin.UnexpectedException", };

	/**
	 * Tests {@link EMFCompareMessages#getString(String, Object...)} with an invalid key. Expects the String
	 * 
	 * <pre>
	 * &quot;!&quot; + key + &quot;!&quot;
	 * </pre>
	 * 
	 * to be returned. Parameters won't affect result here.
	 */
	public void testFormattedGetStringInvalidKey() {
		for (int i = 0; i < messageParameters.length; i++) {
			for (int j = i; j < messageParameters.length; j++) {
				final Object[] parameters = partialArrayCopy(messageParameters, i, j);
				for (String invalidKey : invalidKeys)
					assertEquals("Unexpected result of getString() with an invalid key.",
							'!' + invalidKey + '!', EMFCompareMessages.getString(invalidKey, parameters));
			}
		}
	}

	/**
	 * Tests {@link EMFCompareMessages#getString(String, Object...)} with <code>null</code> key. Expects a
	 * NullPointerException to be thrown. Parameters won't affect result here.
	 */
	public void testFormattedGetStringNullKey() {
		for (int i = 0; i < messageParameters.length; i++) {
			for (int j = i; j < messageParameters.length; j++) {
				final Object[] parameters = partialArrayCopy(messageParameters, i, j);
				try {
					EMFCompareMessages.getString(null, parameters);
					fail("Calling getString() with null key did not throw NullPointerException.");
				} catch (NullPointerException e) {
					// This was expected
				}
			}
		}
	}

	/**
	 * Tests {@link EMFCompareMessages#getString(String, Object...)} with valid keys.<p>If the System locale is configured for english language, expects the String associated to
	 * the key in the properties file to be returned with all occurences of <code>&quot;{[0-9]*}&quot;</code>
	 * replaced by the correct parameter if any. Otherwise, expects the key to have been found, and the parameters to be correctly substituted.</p>
	 */
	public void testFormattedGetStringValidKey() {
		for (int i = 0; i < messageParameters.length; i++) {
			for (int j = i; j < messageParameters.length; j++) {
				final Object[] parameters = partialArrayCopy(messageParameters, i, j);
				for (int k = 0; k < parameterisableKeys.length; k++) {
					String expectedResult = expectedForParameterisable[k];
					int parameterCount = 0;
					while (expectedResult.matches(".*\\{[0-9]+\\}.*") && parameterCount < parameters.length) {
						if (parameters[parameterCount] == null)
							expectedResult = expectedResult.replaceFirst("\\{[0-9]+\\}", "null");
						else
							expectedResult = expectedResult.replaceFirst("\\{[0-9]+\\}",
									parameters[parameterCount].toString());
						parameterCount++;
					}
					if (Locale.getDefault().getLanguage().equalsIgnoreCase("en"))
						assertEquals("Unexpected formatted String returned by getString(String, Object...).",
								expectedResult, EMFCompareMessages.getString(parameterisableKeys[k], parameters));
					else {
						final String result = EMFCompareMessages.getString(parameterisableKeys[k], parameters);
						assertFalse("Message class did not find an existing parameterisable key.", result.equals('!' + "parameterisableKeys[k]" + '!'));
						for (int l = 0; l < parameterCount; l++) {
							if (parameters[l] != null)
								assertTrue("Message class did not substitute the parameter in the result.", result.contains(parameters[l].toString()));
							else
								assertTrue("Message class did not substitute the parameter in the result.", result.contains("null"));
						}
					}
				}
			}
		}
	}

	/**
	 * Tests {@link EMFCompareMessages#getString(String, Object...)} with valid keys and <code>null</code> as
	 * formatting parameter. Expects the result to be the same as the {@link EMFCompareMessages#getString(String)}.
	 */
	public void testFormattedGetStringValidKeyNullParameter() {
		for (int i = 0; i < parameterisableKeys.length; i++) {
			assertEquals("Unexpected formatted String returned by getString(String, Object...).", EMFCompareMessages
					.getString(parameterisableKeys[i]), EMFCompareMessages.getString(parameterisableKeys[i],
					(Object[])null));
		}
	}

	/**
	 * Tests {@link EMFCompareMessages#getString(String)} with an invalid key. Expects the String
	 * 
	 * <pre>
	 * &quot;!&quot; + key + &quot;!&quot;
	 * </pre>
	 * 
	 * to be returned.
	 */
	public void testUnFormattedGetStringInvalidKey() {
		for (String invalidKey : invalidKeys)
			assertEquals("Unexpected result of getString() with an invalid key.", '!' + invalidKey + '!',
					EMFCompareMessages.getString(invalidKey));
	}

	/**
	 * Tests {@link EMFCompareMessages#getString(String)} with <code>null</code> argument. Expects a
	 * NullPointerException to be thrown.
	 */
	public void testUnFormattedGetStringNullKey() {
		try {
			EMFCompareMessages.getString(null);
			fail("Calling getString() with null argument did not throw NullPointerException.");
		} catch (NullPointerException e) {
			// This was expected
		}
	}

	/**
	 * Tests {@link EMFCompareMessages#getString(String)} with valid keys. Expects the String associated to the key in
	 * the properties file to be returned.
	 */
	public void testUnFormattedGetStringValidKey() {
		for (int i = 0; i < validKeys.length; i++) {
			if (Locale.getDefault().getLanguage().equalsIgnoreCase("en"))
				assertEquals("Unexpected String returned by getString(String).", expectedForValidKeys[i],
						EMFCompareMessages.getString(validKeys[i]));
			else {
				final String result = EMFCompareMessages.getString(validKeys[i]);
				assertFalse("Message class did not find an existing parameterisable key.", result.equals('!' + "parameterisableKeys[k]" + '!'));
			}
		}
	}
	/**
	 * This will return a partial copy of an array.
	 * 
	 * @param <T>
	 *            Type of the copied array.
	 * @param original
	 *            Array to be copied.
	 * @param start
	 *            starting index of the copy.
	 * @param end
	 *            end index of the copy.
	 * @return Array containing a copy of the given range from <code>original</code>.
	 */
	private <T> T[] partialArrayCopy(T[] original, int start, int end) {
		final int range = end - start;
		if (range < 0)
			throw new IllegalArgumentException("Illegal copy range.");
		final T[] copy = (T[])Array.newInstance(original.getClass().getComponentType(), range);
		System.arraycopy(original, start, copy, 0, Math.min(original.length - start, range));
		return copy;
	}
}

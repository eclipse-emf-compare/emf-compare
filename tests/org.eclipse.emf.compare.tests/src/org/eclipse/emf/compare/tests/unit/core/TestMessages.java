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

import java.util.Arrays;
import java.util.HashSet;

import junit.framework.TestCase;

import org.eclipse.emf.compare.Messages;

/**
 * Tests Messages class. These tests successful completion heavily depends on org.eclipse.emf.compare
 * "messages.properties" file contents.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings({"unchecked", "nls", })
public class TestMessages extends TestCase {
	/** Expected result of the parameterisable keys. */
	private final String[] expectedForParameterisable = {"The feature {0} does not exist in {1}.",
			"Illegal load factor for map: {0}.", };

	/** Contains the expected results for the valid keys. */
	private final String[] expectedForValidKeys = {"Required element not found.",
			"A java exception has been thrown.", "Unknown EMF Compare problem.", };

	/** These will be used when testing message retrieval with invalid keys. */
	private final String[] invalidKeys = {"invalidKey", "EMFComparePlugin.ElementNotFound1", "", "\u00ec", };

	/** Contains possible parameters for the messages. */
	private final Object[] messageParameters = {null, "", "Foehn", -1, new Long(10), '\u0043', new HashSet(), };

	/** These two are valid, parameterisable keys. See org.eclipse.emf.compare "messages.properties". */
	private final String[] parameterisableKeys = {"EFactory.FeatureNotFound", "EMFCompareMap.IllegalLoadFactor", };

	/** These are valid, un-parameterisable keys. See org.eclipse.emf.compare "messages.properties". */
	private final String[] validKeys = {"EMFComparePlugin.ElementNotFound", "EMFComparePlugin.JavaException",
			"EMFComparePlugin.UnexpectedException", };

	/**
	 * Tests {@link Messages#getString(String, Object...)} with an invalid key. Expects the String
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
				final Object[] parameters = Arrays.copyOfRange(messageParameters, i, j);
				for (String invalidKey : invalidKeys)
					assertEquals("Unexpected result of getString() with an invalid key.",
							'!' + invalidKey + '!', Messages.getString(invalidKey, parameters));
			}
		}
	}

	/**
	 * Tests {@link Messages#getString(String, Object...)} with <code>null</code> key. Expects a
	 * NullPointerException to be thrown. Parameters won't affect result here.
	 */
	public void testFormattedGetStringNullKey() {
		for (int i = 0; i < messageParameters.length; i++) {
			for (int j = i; j < messageParameters.length; j++) {
				final Object[] parameters = Arrays.copyOfRange(messageParameters, i, j);
				try {
					Messages.getString(null, parameters);
					fail("Calling getString() with null key did not throw NullPointerException.");
				} catch (NullPointerException e) {
					// This was expected
				}
			}
		}
	}

	/**
	 * Tests {@link Messages#getString(String, Object...)} with valid keys. Expects the String associated to
	 * the key in the properties file to be returned with all occurences of <code>&quot;{[0-9]*}&quot;</code>
	 * replaced by the correct parameter if any.
	 */
	public void testFormattedGetStringValidKey() {
		for (int i = 0; i < messageParameters.length; i++) {
			for (int j = i; j < messageParameters.length; j++) {
				final Object[] parameters = Arrays.copyOfRange(messageParameters, i, j);
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
					assertEquals("Unexpected formatted String returned by getString(String, Object...).",
							expectedResult, Messages.getString(parameterisableKeys[k], parameters));
				}
			}
		}
	}

	/**
	 * Tests {@link Messages#getString(String, Object...)} with valid keys and <code>null</code> as
	 * formatting parameter. Expects the result to be the same as the {@link Messages#getString(String)}.
	 */
	public void testFormattedGetStringValidKeyNullParameter() {
		for (int i = 0; i < parameterisableKeys.length; i++) {
			assertEquals("Unexpected formatted String returned by getString(String, Object...).", Messages
					.getString(parameterisableKeys[i]), Messages.getString(parameterisableKeys[i],
					(Object[])null));
		}
	}

	/**
	 * Tests {@link Messages#getString(String)} with an invalid key. Expects the String
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
					Messages.getString(invalidKey));
	}

	/**
	 * Tests {@link Messages#getString(String)} with <code>null</code> argument. Expects a
	 * NullPointerException to be thrown.
	 */
	public void testUnFormattedGetStringNullKey() {
		try {
			Messages.getString(null);
			fail("Calling getString() with null argument did not throw NullPointerException.");
		} catch (NullPointerException e) {
			// This was expected
		}
	}

	/**
	 * Tests {@link Messages#getString(String)} with valid keys. Expects the String associated to the key in
	 * the properties file to be returned.
	 */
	public void testUnFormattedGetStringValidKey() {
		for (int i = 0; i < validKeys.length; i++) {
			assertEquals("Unexpected String returned by getString(String).", expectedForValidKeys[i],
					Messages.getString(validKeys[i]));
		}
	}
}

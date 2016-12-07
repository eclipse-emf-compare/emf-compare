/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.internal.preferences;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.runtime.Platform;

/**
 * A tester for values of properties.
 * <p>
 * This tester supports one property, named &quot;preferenceValueEquals&quot; (see
 * {@link #PREFERENCE_VALUE_EQUALS}), which takes two arguments:
 * </p>
 * <ol>
 * <li>The qualifier of the preference</li>
 * <li>The preference key of the preference</li>
 * </ol>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class PreferencesPropertyTester extends PropertyTester {

	/** Property for testing whether a preference value equals the specified value. */
	private static final String PREFERENCE_VALUE_EQUALS = "preferenceValueEquals"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 */
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (PREFERENCE_VALUE_EQUALS.equals(property) && args.length == 2 && expectedValue != null) {
			final String qualifier = args[0].toString();
			final String preferenceKey = args[1].toString();
			final String preferenceValue = Platform.getPreferencesService().getString(qualifier,
					preferenceKey, null, null);
			return expectedValue.toString().equals(preferenceValue);
		}
		return false;
	}

}

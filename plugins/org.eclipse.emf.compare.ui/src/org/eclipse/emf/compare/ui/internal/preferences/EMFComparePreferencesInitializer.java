/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.internal.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.emf.compare.ui.EMFCompareUIPlugin;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.compare.util.EMFComparePreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;

/**
 * This will feed default value to the preferences page.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFComparePreferencesInitializer extends AbstractPreferenceInitializer {
	/**
	 * {@inheritDoc}
	 * 
	 * @see AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		final IPreferenceStore store = EMFCompareUIPlugin.getDefault().getPreferenceStore();
		/*
		 * "ignore ID", "ignore XMI ID", "engine selection" and "distinct metamodel" booleans don't need a
		 * default : initialized to false
		 */
		// Default search window
		store.setDefault(EMFComparePreferenceConstants.PREFERENCES_KEY_SEARCH_WINDOW,
				EMFComparePreferenceConstants.PREFERENCES_DEFAULT_SEARCH_WINDOW);
		// Default value for the "draw differences" boolean
		store.setDefault(EMFCompareConstants.PREFERENCES_KEY_DRAW_DIFFERENCES, true);

		// Default colors
		PreferenceConverter.setDefault(store, EMFCompareConstants.PREFERENCES_KEY_HIGHLIGHT_COLOR,
				EMFCompareConstants.PREFERENCES_DEFAULT_HIGHLIGHT_COLOR);
		PreferenceConverter.setDefault(store, EMFCompareConstants.PREFERENCES_KEY_CHANGED_COLOR,
				EMFCompareConstants.PREFERENCES_DEFAULT_CHANGED_COLOR);
		PreferenceConverter.setDefault(store, EMFCompareConstants.PREFERENCES_KEY_CONFLICTING_COLOR,
				EMFCompareConstants.PREFERENCES_DEFAULT_CONFLICTING_COLOR);
		PreferenceConverter.setDefault(store, EMFCompareConstants.PREFERENCES_KEY_ADDED_COLOR,
				EMFCompareConstants.PREFERENCES_DEFAULT_ADDED_COLOR);
		PreferenceConverter.setDefault(store, EMFCompareConstants.PREFERENCES_KEY_REMOVED_COLOR,
				EMFCompareConstants.PREFERENCES_DEFAULT_REMOVED_COLOR);
	}
}

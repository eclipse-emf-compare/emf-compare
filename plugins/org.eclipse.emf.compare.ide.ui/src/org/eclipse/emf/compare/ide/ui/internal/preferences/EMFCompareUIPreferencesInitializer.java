/*******************************************************************************
 * Copyright (c) 2014, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 508855, 522372
 *     Martin Fleck - bug 514767
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * This will be used to initialize all of our UI preferences to their default.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFCompareUIPreferencesInitializer extends AbstractPreferenceInitializer {
	/** {@inheritDoc} */
	@Override
	public void initializeDefaultPreferences() {
		final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault().getPreferenceStore();
		store.setDefault(EMFCompareUIPreferences.DISABLE_RESOLVERS_PREFERENCE, false);
		store.setDefault(EMFCompareUIPreferences.DISABLE_THREADING_PREFERENCE, false);
		store.setDefault(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE,
				CrossReferenceResolutionScope.CONTAINER.name());
		store.setDefault(EMFCompareUIPreferences.PRE_MERGE_MODELS_WHEN_CONFLICT, false);
		store.setDefault(EMFCompareUIPreferences.ENABLE_MODEL_RESOLUTION_FROM_CONTAINERS, false);
		store.setDefault(EMFCompareUIPreferences.EDITOR_TREE_AUTO_EXPAND_LEVEL, 1);
		store.setDefault(EMFCompareUIPreferences.EDITOR_TREE_EXPAND_TIMEOUT, 5);
		store.setDefault(EMFCompareUIPreferences.EDITOR_TREE_AUTO_SELECT_FIRST_CHANGE, true);
		store.setDefault(EMFCompareUIPreferences.EDITOR_TREE_HIGHLIGHT_RELATED_CHANGES, true);
		store.setDefault(EMFCompareUIPreferences.SELECT_NEXT_UNRESOLVED_DIFF, true);
		store.setDefault(EMFCompareUIPreferences.MODEL_PROVIDER_CACHE_TIMEOUT, 120L);
	}
}

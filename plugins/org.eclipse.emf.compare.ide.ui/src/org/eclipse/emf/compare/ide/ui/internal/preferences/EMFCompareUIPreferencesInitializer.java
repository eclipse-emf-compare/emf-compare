/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
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
	}
}

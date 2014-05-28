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

import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;

/**
 * Set of preferences affecting the behavior of the EMF Compare user interface.
 * <p>
 * Default values for each of these can be checked from {@link EMFCompareUIPreferencesInitializer}.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public interface EMFCompareUIPreferences {
	/**
	 * Preference telling us whether the model resolvers are enabled (value <code>false</code>) or disabled
	 * (value <code>true</code>).
	 */
	String DISABLE_RESOLVERS_PREFERENCE = EMFCompareIDEUIPlugin.PLUGIN_ID + ".preference.disableResolving"; //$NON-NLS-1$

	/**
	 * Preference telling us whether the model resolver can use threading (value <code>false</code>) or if it
	 * should remain single-threaded (value <code>true</code>).
	 */
	String DISABLE_THREADING_PREFERENCE = EMFCompareIDEUIPlugin.PLUGIN_ID + ".preference.disableThreading"; //$NON-NLS-1$

	/**
	 * Preference telling EMF Compare which scope to crawl for cross-referenced resources. Should be one of
	 * the literals from
	 * {@link org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope}.
	 */
	String RESOLUTION_SCOPE_PREFERENCE = EMFCompareIDEUIPlugin.PLUGIN_ID + ".preference.resolutionScope"; //$NON-NLS-1$
}

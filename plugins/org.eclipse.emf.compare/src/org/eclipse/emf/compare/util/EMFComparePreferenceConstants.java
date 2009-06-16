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
package org.eclipse.emf.compare.util;

/**
 * Defines constants used for EMF Compare preferences. These are used by the UI as well as the match engine.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 1.0
 */
public interface EMFComparePreferenceConstants {
	/**
	 * Preferences key for the boolean indicating that the comparison should assume the compared models have
	 * distinct metamodels.
	 */
	String PREFERENCES_KEY_DISTINCT_METAMODEL = "emfcompare.distinct.metamodel"; //$NON-NLS-1$

	/**
	 * Preferences key for the boolean indicating that the comparison should ignore IDs.
	 */
	String PREFERENCES_KEY_IGNORE_ID = "emfcompare.ignore.ID"; //$NON-NLS-1$

	/**
	 * Preferences key for the boolean indicating that the comparison should ignore XMI IDs.
	 */
	String PREFERENCES_KEY_IGNORE_XMIID = "emfcompare.ignore.XMI.ID"; //$NON-NLS-1$

	/** Preferences key for the siblings search window. */
	String PREFERENCES_KEY_SEARCH_WINDOW = "emfcompare.search.window"; //$NON-NLS-1$

	/** Preferences key for the engine selection. */
	String PREFERENCES_KEY_ENGINE_SELECTION = "emfcompare.engine.selection"; //$NON-NLS-1$

	/** Default value for the siblings search window. */
	int PREFERENCES_DEFAULT_SEARCH_WINDOW = 100;
}

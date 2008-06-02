/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.util;

import org.eclipse.emf.compare.ui.EMFCompareUIMessages;
import org.eclipse.swt.graphics.RGB;

/**
 * Defines constants used throughout EMF Compare's UI.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public interface EMFCompareConstants {
	/** Three-way change constant (value 3) indicating the ancestor. */
	int ANCESTOR = 3;

	/** Constant (value 12) allowing enablement of the "show ancestor" action. */
	int ENABLE_ANCESTOR = 12;

	/** Full path of the preferences help icon. */
	String ICONS_PREFERENCES_HELP = "icons/full/prefshelp.gif"; //$NON-NLS-1$

	/** Three-way change constant (value 1) indicating the left side. */
	int LEFT = 1;

	/** Difference constant (value 0) indicating no difference. */
	int NO_CHANGE = 0;

	/** Default color (green) for an added element circling. */
	RGB PREFERENCES_DEFAULT_ADDED_COLOR = new RGB(138, 226, 52);

	/** Default color (blue) for a modified element circling. */
	RGB PREFERENCES_DEFAULT_CHANGED_COLOR = new RGB(114, 159, 207);

	/** Default color (orange) for a conflicting element circling. */
	RGB PREFERENCES_DEFAULT_CONFLICTING_COLOR = new RGB(255, 165, 0);

	/** Default color (light yellow) for changes background. */
	RGB PREFERENCES_DEFAULT_HIGHLIGHT_COLOR = new RGB(255, 255, 224);

	/** Default color (red) for a removed element circling. */
	RGB PREFERENCES_DEFAULT_REMOVED_COLOR = new RGB(239, 41, 41);

	/** Default value for the siblings search window. */
	int PREFERENCES_DEFAULT_SEARCH_WINDOW = 100;

	/** Preferences description for the added element color. */
	String PREFERENCES_DESCRIPTION_ADDED_COLOR = EMFCompareUIMessages
			.getString("EMFCompareConstants.preferences.addedColorLabel") + ':'; //$NON-NLS-1$

	/** Preferences description for the changed element color. */
	String PREFERENCES_DESCRIPTION_CHANGED_COLOR = EMFCompareUIMessages
			.getString("EMFCompareConstants.preferences.changedColorLabel") + ':'; //$NON-NLS-1$

	/** Preferences description for the conflicting element color. */
	String PREFERENCES_DESCRIPTION_CONFLICTING_COLOR = EMFCompareUIMessages
			.getString("EMFCompareConstants.preferences.conflictingColorLabel") + ':'; //$NON-NLS-1$
	
	/**
	 * Preferences description for the boolean indicating that the comparison
	 * should assume the compared models have distinct metamodels.
	 */
	String PREFERENCES_DESCRIPTION_DISTINCT_METAMODEL = EMFCompareUIMessages
			.getString("EMFCompareConstants.preferences.distinctMMLabel"); //$NON-NLS-1$

	/**
	 * Preferences description for the boolean indicating that the differences
	 * markers have to be drawn.
	 */
	String PREFERENCES_DESCRIPTION_DRAW_DIFFERENCES = EMFCompareUIMessages
			.getString("EMFCompareConstants.preferences.drawDifferencesLabel"); //$NON-NLS-1$

	/** Preferences description for the highlight color. */
	String PREFERENCES_DESCRIPTION_HIGHLIGHT_COLOR = EMFCompareUIMessages
			.getString("EMFCompareConstants.preferences.highlightColorLabel") + ':'; //$NON-NLS-1$

	/**
	 * Preferences description for the boolean indicating that the comparison
	 * should ignore IDs.
	 */
	String PREFERENCES_DESCRIPTION_IGNORE_ID = EMFCompareUIMessages
			.getString("EMFCompareConstants.preferences.ignoreIDLabel"); //$NON-NLS-1$

	/**
	 * Preferences description for the boolean indicating that the comparison
	 * should ignore XMI IDs.
	 */
	String PREFERENCES_DESCRIPTION_IGNORE_XMIID = EMFCompareUIMessages
			.getString("EMFCompareConstants.preferences.ignoreXMIIDLabel"); //$NON-NLS-1$

	/** Preferences description for the removed element color. */
	String PREFERENCES_DESCRIPTION_REMOVED_COLOR = EMFCompareUIMessages
			.getString("EMFCompareConstants.preferences.removedColorLabel") + ':'; //$NON-NLS-1$

	/** Preferences description for the siblings search window. */
	String PREFERENCES_DESCRIPTION_SEARCH_WINDOW = EMFCompareUIMessages
			.getString("EMFCompareConstants.preferences.searchWindowLabel") + ':'; //$NON-NLS-1$

	/** Preferences key for the circling color of an added element. */
	String PREFERENCES_KEY_ADDED_COLOR = "added.color"; //$NON-NLS-1$

	/** Preferences key for the circling color of a changed element. */
	String PREFERENCES_KEY_CHANGED_COLOR = "changed.color"; //$NON-NLS-1$

	/** Preferences key for the circling color of a changed element. */
	String PREFERENCES_KEY_CONFLICTING_COLOR = "conflicting.color"; //$NON-NLS-1$

	/**
	 * Preferences key for the boolean indicating that the differences markers
	 * have to be drawn.
	 */
	String PREFERENCES_KEY_DRAW_DIFFERENCES = "viewer.draw.differences"; //$NON-NLS-1$

	/** Preferences key for the highlight color. */
	String PREFERENCES_KEY_HIGHLIGHT_COLOR = "highlight.color"; //$NON-NLS-1$

	/** Preferences key for the circling color of a removed element. */
	String PREFERENCES_KEY_REMOVED_COLOR = "removed.color"; //$NON-NLS-1$

	/** Key for the property associated with the comparison time. */
	String PROPERTY_COMPARISON_TIME = "comparison.time"; //$NON-NLS-1$

	/**
	 * Key of the property associated with an input change in the content
	 * viewer.
	 */
	String PROPERTY_CONTENT_INPUT_CHANGED = "content.input.changed"; //$NON-NLS-1$

	/**
	 * Key of the property associated with a change in the content merge viewer
	 * trees' selection.
	 */
	String PROPERTY_CONTENT_SELECTION = "content.selection.changed"; //$NON-NLS-1$

	/**
	 * Key of the property associated with a selection in the structure merge
	 * viewer.
	 */
	String PROPERTY_STRUCTURE_SELECTION = "structure.selection.changed"; //$NON-NLS-1$

	/** Three-way change constant (value 2) indicating right side. */
	int RIGHT = 2;
}

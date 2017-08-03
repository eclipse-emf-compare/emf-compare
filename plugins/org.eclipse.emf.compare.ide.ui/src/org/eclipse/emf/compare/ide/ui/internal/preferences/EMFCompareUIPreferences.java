/*******************************************************************************
 * Copyright (c) 2014, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 508855
 *     Martin Fleck - bug 514767
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

	/**
	 * Preference indicating whether a pre-merge must be performed when a REAL conflict is detected. The
	 * expected values are <code>true</code> or <code>false</code>.
	 */
	String PRE_MERGE_MODELS_WHEN_CONFLICT = EMFCompareIDEUIPlugin.PLUGIN_ID
			+ ".preference.preMergeOnConflict"; //$NON-NLS-1$

	/**
	 * Preference indicating whether a the EMFModelProvider should be active also for comparisons of
	 * containers. The expected values are <code>true</code> or <code>false</code>.
	 */
	String ENABLE_MODEL_RESOLUTION_FROM_CONTAINERS = EMFCompareIDEUIPlugin.PLUGIN_ID
			+ ".preference.enableModelResolutionFromContainers"; //$NON-NLS-1$

	/**
	 * Preference key holding the value for the comparison editor to automatically expand the tree viewer of
	 * the top panel to a specific level.
	 */
	String EDITOR_TREE_AUTO_EXPAND_LEVEL = EMFCompareIDEUIPlugin.PLUGIN_ID
			+ ".preference.editor.tree.expand.level"; //$NON-NLS-1$

	/**
	 * Preference key holding the value for the comparison editor to automatically select the first change in
	 * the tree viewer of the top panel.
	 */
	String EDITOR_TREE_AUTO_SELECT_FIRST_CHANGE = EMFCompareIDEUIPlugin.PLUGIN_ID
			+ ".preference.editor.tree.selection.firstChange"; //$NON-NLS-1$

	/**
	 * Preference key holding the value for the comparison editor to automatically highlight changes related
	 * to the current selection in the tree viewer of the top panel.
	 */
	String EDITOR_TREE_HIGHLIGHT_RELATED_CHANGES = EMFCompareIDEUIPlugin.PLUGIN_ID
			+ ".preference.editor.tree.highlight"; //$NON-NLS-1$

	/**
	 * Preference indicating whether the next unresolved diff should be selected after a merge action. The
	 * expected values are <code>true</code> or <code>false</code>.
	 */
	String SELECT_NEXT_UNRESOLVED_DIFF = EMFCompareIDEUIPlugin.PLUGIN_ID
			+ ".preference.selectNextUnresolvedDiff"; //$NON-NLS-1$
}

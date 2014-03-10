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
package org.eclipse.emf.compare.rcp.internal.preferences;

/**
 * All preferences constant for EMF Compare.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public final class EMFComparePreferences {

	/** Differences Engine preference. (Key to use in the registry to retrieve the engine descriptor). */
	public static final String DIFF_ENGINES = "org.eclipse.emf.compare.preference.diff.engine"; //$NON-NLS-1$

	/** Equivalences Engine preference. (Key to use in the registry to retrieve the engine descriptor). */
	public static final String EQUI_ENGINES = "org.eclipse.emf.compare.preference.equi.engine"; //$NON-NLS-1$

	/** Requirements Engine preference. (Key to use in the registry to retrieve the engine descriptor). */
	public static final String REQ_ENGINES = "org.eclipse.emf.compare.preference.req.engine"; //$NON-NLS-1$

	/** Conflicts detector preference. (Key to use in the registry to retrieve the engine descriptor). */
	public static final String CONFLICTS_DETECTOR = "org.eclipse.emf.compare.preference.conflict.detector"; //$NON-NLS-1$

	/** Disabled match engine preference. (Key to use in the registry to retrieve the engine descriptor). */
	public static final String MATCH_ENGINE_DISABLE_ENGINES = "org.eclipse.emf.compare.preference.match.engine"; //$NON-NLS-1$

	/** Disabled post processors preference. */
	public static final String DISABLED_POST_PROCESSOR = "org.eclipse.emf.compare.preference.postprocessor.disabled"; //$NON-NLS-1$

	/** Ordered list of groups for two way comparison. */
	public static final String TWO_WAY_GROUP_RANKING = "org.eclipse.emf.compare.preference.groups.2way"; //$NON-NLS-1$

	/** Ordered list of groups for three way comparison. */
	public static final String THREE_WAY_GROUP_RANKING = "org.eclipse.emf.compare.preference.groups.3way"; //$NON-NLS-1$

	/**
	 * Private constructor. Not to be called.
	 */
	private EMFComparePreferences() {
		// Hide default constructor.
	}

	/**
	 * Value of {@link EMFComparePreferences#THREE_WAY_COMPARISON_AUTOMATIC_BEHAVIOR} and
	 * {@link EMFComparePreferences#TWO_WAY_COMPARISON_AUTOMATIC_BEHAVIOR}.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	public static enum GroupAutomaticBehavior {
		/** Never synchronize current selected group with preferences. */
		NEVER,
		/** Always synchronize current selected group with preferences. */
		ALWAYS,
		/** Ask each time the user if he want to synchronize. */
		ASK_EACH_TIME
	}

}

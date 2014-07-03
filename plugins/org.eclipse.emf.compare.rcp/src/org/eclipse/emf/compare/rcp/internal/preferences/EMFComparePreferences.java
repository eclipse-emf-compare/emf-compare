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

	/** Disabled adapter factories preference.*/
	public static final String DISABLED_ADAPTER_FACTORY = "org.eclipse.emf.compare.preference.disabled.adapter.factories"; //$NON-NLS-1$
	/**
	 * Private constructor. Not to be called.
	 */
	private EMFComparePreferences() {
		// Hide default constructor.
	}

}

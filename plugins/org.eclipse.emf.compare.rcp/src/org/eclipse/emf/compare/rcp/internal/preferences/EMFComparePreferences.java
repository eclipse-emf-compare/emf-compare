/*******************************************************************************
 * Copyright (c) 2014, 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.internal.preferences;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;

/***
 * Preferences constants and utilities for the EMF Compare RCP plug-in.
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

	/** Disabled adapter factories preference. */
	public static final String DISABLED_ADAPTER_FACTORY = "org.eclipse.emf.compare.preference.disabled.adapter.factories"; //$NON-NLS-1$

	/** Preference key for log4j file name. */
	public static final String LOG_FILENAME_KEY = "org.eclipse.emf.compare.log.file.name"; //$NON-NLS-1$

	/** Preference key for log4j log level. */
	public static final String LOG_LEVEL_KEY = "org.eclipse.emf.compare.log.level"; //$NON-NLS-1$

	/** Preference key for log4j backup file max number. */
	public static final String LOG_BACKUP_COUNT_KEY = "org.eclipse.emf.compare.log.backup.count"; //$NON-NLS-1$

	/** Preference key for log4j maximum file size. */
	public static final String LOG_FILE_MAX_SIZE_KEY = "org.eclipse.emf.compare.log.file.max.size"; //$NON-NLS-1$

	/** Log4j appender used by EMFCompare. */
	public static final String EMFC_APPENDER_NAME = "EMFCFile"; //$NON-NLS-1$

	/** Default log4j file name. */
	public static final String LOG_FILE_DEFAULT = ""; //$NON-NLS-1$

	/** Default log4j level. */
	public static final String LOG_LEVEL_DEFAULT = "OFF"; //$NON-NLS-1$

	/** Default log4j backup file max number. */
	public static final int LOG_BACKUP_DEFAULT = 10;

	/** Default log4j maximum file size. */
	public static final int LOG_FILE_SIZE_DEFAULT = 100;

	/**
	 * Pattern used for log4j logging.
	 * 
	 * <pre>
	 * Date [Thread name] LEVEL 3.last.segments.of.logger.name <NDC Tag> - message\n
	 * </pre>
	 */
	public static final String LOG_PATTERN = "%d{ISO8601} [%t] %-5p %c{3} %x - %m%n"; //$NON-NLS-1$

	/**
	 * Private constructor. Not to be called.
	 */
	private EMFComparePreferences() {
		// Hide default constructor.
	}

	/**
	 * Gets the ids of all disabled EMF Compare adapter factory descriptors.
	 * 
	 * @return List of ids;
	 */
	public static List<String> getDisabledAdapterFactoryDescriptorIds() {
		String disabledAdapterFactoriesString = Platform.getPreferencesService()
				.getString(EMFCompareRCPPlugin.PLUGIN_ID, DISABLED_ADAPTER_FACTORY, "", null); //$NON-NLS-1$

		final List<String> disabledAdapterFactories = Lists.newArrayList(
				Splitter.on(';').omitEmptyStrings().trimResults().split(disabledAdapterFactoriesString));
		return disabledAdapterFactories;
	}

}

/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.engine;

import java.util.Map;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.match.MatchOptions;
import org.eclipse.emf.compare.util.EMFCompareMap;
import org.eclipse.emf.compare.util.EMFComparePreferenceConstants;

/**
 * Class wrapping the settings one can specify for the Match engine of EMF Compare.
 * 
 * @since 1.1
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class MatchSettings {
	/**
	 * The match search window.
	 */
	private int searchWindow = MatchOptions.DEFAULT_SEARCH_WINDOW;

	/**
	 * true if we should ignore XMI IDs.
	 */
	private boolean ignoresXMIID = MatchOptions.DEFAULT_IGNORE_XMI_ID;

	/**
	 * true if we should ignore business ID's.
	 */
	private boolean ignoreID = MatchOptions.DEFAULT_IGNORE_ID;

	/**
	 * true if we should consider disting metamodels.
	 */
	private boolean distingMetamodels = MatchOptions.DEFAULT_DISTINCT_METAMODEL;

	/**
	 * progress monitor to use during the match process.
	 */
	private Object progressMonitor;

	/**
	 * Create a new Match Setting.
	 */
	public MatchSettings() {
		update(loadPreferenceOptionMap());
	}

	/**
	 * Update the setting object using a standard options map as defined in the API. Non specified options
	 * will keep the default values as defined in {@link MatchOptions}.
	 * 
	 * @param options
	 *            the option map.
	 */
	public void update(Map<String, Object> options) {
		if (isSet(options, MatchOptions.OPTION_DISTINCT_METAMODELS))
			this.distingMetamodels = this.<Boolean> getOption(options,
					MatchOptions.OPTION_DISTINCT_METAMODELS);
		if (isSet(options, MatchOptions.OPTION_IGNORE_ID))
			this.ignoreID = this.<Boolean> getOption(options, MatchOptions.OPTION_IGNORE_ID);
		if (isSet(options, MatchOptions.OPTION_IGNORE_XMI_ID))
			this.ignoresXMIID = this.<Boolean> getOption(options, MatchOptions.OPTION_IGNORE_XMI_ID);
		if (isSet(options, MatchOptions.OPTION_PROGRESS_MONITOR))
			this.progressMonitor = this.<Object> getOption(options, MatchOptions.OPTION_PROGRESS_MONITOR);
		if (isSet(options, MatchOptions.OPTION_SEARCH_WINDOW))
			this.searchWindow = this.<Integer> getOption(options, MatchOptions.OPTION_SEARCH_WINDOW);
	}

	/**
	 * Set the search window size.
	 * 
	 * @param windowSize
	 *            set the search window size
	 */
	public void setSearchWindow(int windowSize) {
		this.searchWindow = windowSize;
	}

	/**
	 * Activate or deactivate the fact that the match engine should ignore XMI ID's.
	 * 
	 * @param ignore
	 *            true to ignore XMIId, false to use them for matching.
	 */
	public void ignoreXMIID(boolean ignore) {
		this.ignoresXMIID = ignore;
	}

	/**
	 * Activate or deactivate the fact that the match engine should ignore ID's attributes defined in the
	 * Ecore model.
	 * 
	 * @param ignore
	 *            true to ignore ID's attribute, false to use them for matching.
	 */
	public void ignoreEcoreID(boolean ignore) {
		this.ignoreID = ignore;
	}

	/**
	 * Activate or deactivate the fact that the match engine will try to match element even if they come from
	 * a different Ecore model.
	 * 
	 * @param distinctMetamodels
	 *            true if we
	 */
	public void matchDistinctMetamodels(boolean distinctMetamodels) {
		this.distingMetamodels = distinctMetamodels;
	}

	/**
	 * Set a progress monitor used during the match.
	 * 
	 * @param monitor
	 *            the progressmonitor to use.
	 */
	public void setProgressMonitor(Object monitor) {
		this.progressMonitor = monitor;
	}

	/**
	 * return the matching search window.
	 * 
	 * @return the matching search window.
	 */
	public final int getSearchWindow() {
		return this.searchWindow;
	}

	/**
	 * return true if the match should ignore XMI Id's.
	 * 
	 * @return true if the match should ignore XMI Id's
	 */
	public final boolean isIgnoringXMIID() {
		return this.ignoresXMIID;
	}

	/**
	 * return true if the match should ignore business ID's.
	 * 
	 * @return true if the match should ignore business ID's.
	 */
	public final boolean isIgnoringID() {
		return this.ignoreID;
	}

	/**
	 * return true if we should allow to match distinct metamodels.
	 * 
	 * @return true if we should allow to match distinct metamodels.
	 */
	public final boolean shouldMatchDistinctMetamodels() {
		return this.distingMetamodels;
	}

	/**
	 * return the progress monitor to use during the match process.
	 * 
	 * @return the progress monitor to use during the match process.
	 */
	public final Object getProgressMonitor() {
		return this.progressMonitor;
	}

	/**
	 * This will return the value associated to the given key in the options map.
	 * <p>
	 * NOTE : Misuses of this method will easily throw {@link ClassCastException}s.
	 * </p>
	 * 
	 * @param options
	 *            the map containing the options.
	 * @param <T>
	 *            Expected type of the value associated to <code>key</code>.
	 * @param key
	 *            Key of the value to retrieve.
	 * @return Value associated to the given key in the options map.
	 * @throws ClassCastException
	 *             If the value isn't assignment compatible with the expected type.
	 */
	@SuppressWarnings("unchecked")
	private <T> T getOption(Map<String, Object> options, String key) throws ClassCastException {
		return (T)options.get(key);
	}

	/**
	 * return true if the option is set in the given map.
	 * 
	 * @param options
	 *            the options map.
	 * @param optionID
	 *            the option key.
	 * @return true if the option is set in the given map
	 */
	private boolean isSet(Map<String, Object> options, String optionID) {
		return options.containsKey(optionID);
	}

	/**
	 * This will load all the needed options with their default values.
	 * 
	 * @return Map containing all the needed options with their default values.
	 */
	private Map<String, Object> loadPreferenceOptionMap() {
		final Map<String, Object> optionMap = new EMFCompareMap<String, Object>(17);
		optionMap.put(MatchOptions.OPTION_SEARCH_WINDOW, getPreferenceSearchWindow());
		optionMap.put(MatchOptions.OPTION_IGNORE_ID, getPreferenceIgnoreID());
		optionMap.put(MatchOptions.OPTION_IGNORE_XMI_ID, getPreferenceIgnoreXMIID());
		optionMap.put(MatchOptions.OPTION_DISTINCT_METAMODELS, getPreferenceDistinctMetaModel());
		optionMap.put(MatchOptions.OPTION_PROGRESS_MONITOR, null);
		return optionMap;
	}

	/**
	 * Returns whether we should assume the metamodels of the compared models are distinct.
	 * 
	 * @return <code>true</code> if the metamodels are to be assumed distinct, <code>false</code> otherwise.
	 */
	private boolean getPreferenceDistinctMetaModel() {
		if (EMFPlugin.IS_ECLIPSE_RUNNING && EMFComparePlugin.getDefault() != null)
			return EMFComparePlugin.getDefault().getBoolean(
					EMFComparePreferenceConstants.PREFERENCES_KEY_DISTINCT_METAMODEL);
		return MatchOptions.DEFAULT_DISTINCT_METAMODEL;
	}

	/**
	 * Returns whether we should ignore the IDs or compare using them.
	 * 
	 * @return <code>True</code> if we should ignore ID, <code>False</code> otherwise.
	 */
	private boolean getPreferenceIgnoreID() {
		if (EMFPlugin.IS_ECLIPSE_RUNNING && EMFComparePlugin.getDefault() != null)
			return EMFComparePlugin.getDefault().getBoolean(
					EMFComparePreferenceConstants.PREFERENCES_KEY_IGNORE_ID);
		return MatchOptions.DEFAULT_IGNORE_ID;
	}

	/**
	 * Returns whether we should ignore the XMI IDs or compare with them.
	 * 
	 * @return <code>True</code> if we should ignore XMI ID, <code>False</code> otherwise.
	 */
	private boolean getPreferenceIgnoreXMIID() {
		if (EMFPlugin.IS_ECLIPSE_RUNNING && EMFComparePlugin.getDefault() != null)
			return EMFComparePlugin.getDefault().getBoolean(
					EMFComparePreferenceConstants.PREFERENCES_KEY_IGNORE_XMIID);
		return MatchOptions.DEFAULT_IGNORE_XMI_ID;
	}

	/**
	 * Returns the search window corresponding to the number of siblings to consider while matching. Reducing
	 * this number (on the preferences page) considerably improve performances while reducing precision.
	 * 
	 * @return An <code>int</code> representing the number of siblings to consider for matching.
	 */
	private int getPreferenceSearchWindow() {
		int searchWindowPrefereence = MatchOptions.DEFAULT_SEARCH_WINDOW;
		if (EMFPlugin.IS_ECLIPSE_RUNNING
				&& EMFComparePlugin.getDefault() != null
				&& EMFComparePlugin.getDefault().getInt(
						EMFComparePreferenceConstants.PREFERENCES_KEY_SEARCH_WINDOW) > 0) {
			searchWindowPrefereence = EMFComparePlugin.getDefault().getInt(
					EMFComparePreferenceConstants.PREFERENCES_KEY_SEARCH_WINDOW);
		}
		if (searchWindowPrefereence < 0) {
			searchWindowPrefereence = 0;
		}
		return searchWindowPrefereence;
	}

}

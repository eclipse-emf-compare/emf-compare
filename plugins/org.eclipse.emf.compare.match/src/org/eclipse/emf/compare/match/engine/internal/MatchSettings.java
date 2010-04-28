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
package org.eclipse.emf.compare.match.engine.internal;

import java.util.Map;

import org.eclipse.emf.compare.match.MatchOptions;

/**
 * Class wrapping the settings one can specify for the MAtch engine of EMF Compare.
 * 
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
	 * Fill the setting object using a standard options map as defined in the API.
	 * 
	 * @param options
	 *            the option map.
	 */
	public void init(Map<String, Object> options) {
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
	 * return the matching search window.
	 * 
	 * @return the matching search window.
	 */
	public int getSearchWindow() {
		return this.searchWindow;
	}

	/**
	 * return true if the match should ignore XMI Id's.
	 * 
	 * @return true if the match should ignore XMI Id's
	 */
	public boolean ignoreXMIID() {
		return this.ignoresXMIID;
	}

	/**
	 * return true if the match should ignore business ID's.
	 * 
	 * @return true if the match should ignore business ID's.
	 */
	public boolean ignoreID() {
		return this.ignoreID;
	}

	/**
	 * return true if we should allow to match disting metamodels.
	 * 
	 * @return true if we should allow to match disting metamodels.
	 */
	public boolean distinctMetamodels() {
		return this.distingMetamodels;
	}

	/**
	 * return the progress monitor to use during the match process.
	 * 
	 * @return the progress monitor to use during the match process.
	 */
	public Object getProgressMonitor() {
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

}

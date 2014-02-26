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
package org.eclipse.emf.compare.rcp.ui.internal.configuration.ui;

import org.eclipse.swt.widgets.Composite;
import org.osgi.service.prefs.Preferences;

/**
 * Abstract composite that is used to configure an item. The configuration shall be stored in the
 * {@link Preferences} passed in parameter.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public abstract class AbstractConfigurationUI extends Composite {

	/** {@link Preferences} holding the configuration. */
	private final Preferences pref;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            Parent {@link Composite}.
	 * @param style
	 *            Style of this {@link Composite}
	 * @param pref
	 *            {@link Preferences} holding the configuration reprenseted by this composite.
	 */
	public AbstractConfigurationUI(Composite parent, int style, Preferences pref) {
		super(parent, style);
		this.pref = pref;

	}

	/**
	 * {@link AbstractConfigurationUI#pref}
	 * 
	 * @return
	 */
	protected Preferences getPreference() {
		return pref;
	}

	/**
	 * Content of this composite. This shall called by client.
	 */
	public abstract void createContent();

	/**
	 * Used to store the configuration. Implementation shall store all the configuration in the
	 * {@link Preferences}.
	 */
	public abstract void storeConfiguration();

	/**
	 * Called to restore default preferences. This should be used to reset the configuration in the store.
	 */
	public abstract void resetDefault();

}

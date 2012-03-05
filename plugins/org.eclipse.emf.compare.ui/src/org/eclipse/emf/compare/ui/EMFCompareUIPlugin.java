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
package org.eclipse.emf.compare.ui;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.ui.viewer.filter.DifferenceFilterExtensionRegistryListener;
import org.eclipse.emf.compare.ui.viewer.filter.DifferenceFilterRegistry;
import org.eclipse.emf.compare.ui.viewer.group.DifferenceGroupExtensionRegistryListener;
import org.eclipse.emf.compare.ui.viewer.group.DifferenceGroupingFacilityRegistry;
import org.eclipse.emf.compare.ui.viewer.menus.ContextualMenuRegistry;
import org.eclipse.emf.compare.ui.viewer.menus.ContextualMenuRegistryListener;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Activator for the EMF Compare's UI plugin.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFCompareUIPlugin extends AbstractUIPlugin {
	/** This plugin's ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.ui"; //$NON-NLS-1$

	/** This plugin's shared instance. */
	private static EMFCompareUIPlugin plugin;

	/** The listener for the filters extension management. */
	private static DifferenceFilterExtensionRegistryListener filterExtensionRegistryListener = new DifferenceFilterExtensionRegistryListener();

	/** The listener for the groups extension management. */
	private static DifferenceGroupExtensionRegistryListener groupExtensionRegistryListener = new DifferenceGroupExtensionRegistryListener();

	/** The listener for the contextual menus management. */
	private static ContextualMenuRegistryListener contextualMenuRegistryListener = new ContextualMenuRegistryListener();

	/** Default Constructor. */
	public EMFCompareUIPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the plugin's shared instance.
	 * 
	 * @return The plugin's shared instance.
	 */
	public static EMFCompareUIPlugin getDefault() {
		return plugin;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		final IExtensionRegistry registry = Platform.getExtensionRegistry();

		registry.addListener(filterExtensionRegistryListener,
				DifferenceFilterRegistry.DIFF_FILTER_EXTENSION_POINT);
		DifferenceFilterRegistry.INSTANCE.parseInitialContributions();

		registry.addListener(groupExtensionRegistryListener,
				DifferenceGroupingFacilityRegistry.DIFF_GROUPING_EXTENSION_POINT);
		DifferenceGroupingFacilityRegistry.INSTANCE.parseInitialContributions();

		registry.addListener(contextualMenuRegistryListener,
				ContextualMenuRegistry.CONTEXTUAL_MENU_EXTENSION_POINT);
		ContextualMenuRegistry.INSTANCE.parseInitialContributions();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see AbstractUIPlugin#stop(BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		final IExtensionRegistry registry = Platform.getExtensionRegistry();

		registry.removeListener(filterExtensionRegistryListener);
		DifferenceFilterRegistry.INSTANCE.clearRegistry();

		registry.removeListener(groupExtensionRegistryListener);
		DifferenceGroupingFacilityRegistry.INSTANCE.clearRegistry();

		registry.removeListener(contextualMenuRegistryListener);
		ContextualMenuRegistry.INSTANCE.clearRegistry();

		super.stop(context);
	}

	/**
	 * Returns the dialog settings section corresponding to the given <em>name</em>, creating it if needed.
	 * 
	 * @param name
	 *            Name of the dialog settings section that is to be retrieved.
	 * @return The dialog settings section corresponding to the given <em>name</em>, creating it if needed.
	 * @since 1.2
	 */
	public IDialogSettings getDialogSettingsSection(String name) {
		final IDialogSettings dialogSettings = getDialogSettings();
		IDialogSettings section = dialogSettings.getSection(name);
		if (section == null) {
			section = dialogSettings.addNewSection(name);
		}
		return section;
	}
}

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
package org.eclipse.emf.compare.match;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.match.filter.IResourceFilter;
import org.eclipse.emf.compare.match.internal.filter.ResourceFilterRegistryEclipseUtil;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class MatchPlugin extends Plugin {
	/** The plugin ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.match"; //$NON-NLS-1$

	/** This plug-in's shared instance. */
	private static MatchPlugin plugin;

	/** Name of the "class" attribute of the resource filters extension point's filter tag. */
	private static final String RESOURCE_FILTERS_CLASS_ATTRIBUTE = "class"; //$NON-NLS-1$

	/** ID of the resource filters extension point. */
	private static final String RESOURCE_FILTERS_EXTENSION_POINT = "org.eclipse.emf.compare.match.resourcefilters"; //$NON-NLS-1$

	/** Name of the "filter" tag of the resource filters extension point. */
	private static final String RESOURCE_FILTERS_FILTER_TAG = "filter"; //$NON-NLS-1$

	/**
	 * Instance of the listener that will be registered against the plugin registry to listen to changes
	 * concerning the resource filters extension point.
	 */
	private final IRegistryChangeListener resourceFiltersListener = new ResourceFiltersRegistryListener();

	/**
	 * Default constructor.
	 */
	public MatchPlugin() {
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static MatchPlugin getDefault() {
		return plugin;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.addRegistryChangeListener(resourceFiltersListener, PLUGIN_ID);
		parseInitialContributions();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.removeRegistryChangeListener(resourceFiltersListener);
		ResourceFilterRegistryEclipseUtil.clearRegistry();
		super.stop(context);
	}

	/**
	 * Though we have listeners on the provided extension points, there could have been contributions before
	 * this plugin got started. This will parse them.
	 */
	private void parseInitialContributions() {
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		for (final IExtension extension : registry.getExtensionPoint(RESOURCE_FILTERS_EXTENSION_POINT)
				.getExtensions()) {
			for (final IConfigurationElement configurationElement : extension.getConfigurationElements()) {
				if (RESOURCE_FILTERS_FILTER_TAG.equals(configurationElement.getName())) {
					try {
						ResourceFilterRegistryEclipseUtil.addFilter((IResourceFilter)configurationElement
								.createExecutableExtension(RESOURCE_FILTERS_CLASS_ATTRIBUTE));
					} catch (final CoreException e) {
						EMFComparePlugin.log(e, false);
					}
				}
			}
		}
	}

	/**
	 * This registry listener will allow us to be aware of changes regarding the resource filters extension
	 * point.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	final class ResourceFiltersRegistryListener implements IRegistryChangeListener {
		/** Short name of the resource filters extension point. */
		private static final String RESOURCE_FILTERS_EP_SHORT = "resourcefilters"; //$NON-NLS-1$
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.core.runtime.IRegistryChangeListener#registryChanged(org.eclipse.core.runtime.IRegistryChangeEvent)
		 */
		public void registryChanged(IRegistryChangeEvent event) {
			for (IExtensionDelta delta : event
					.getExtensionDeltas(PLUGIN_ID, RESOURCE_FILTERS_EP_SHORT)) {
				final IExtension extension = delta.getExtension();
				if (delta.getKind() == IExtensionDelta.ADDED) {
					for (final IConfigurationElement configurationElement : extension
							.getConfigurationElements()) {
						if (RESOURCE_FILTERS_FILTER_TAG.equals(configurationElement.getName())) {
							try {
								ResourceFilterRegistryEclipseUtil
										.addFilter((IResourceFilter)configurationElement
												.createExecutableExtension(RESOURCE_FILTERS_CLASS_ATTRIBUTE));
							} catch (final CoreException e) {
								EMFComparePlugin.log(e, false);
							}
						}
					}
				} else if (delta.getKind() == IExtensionDelta.REMOVED) {
					for (final IConfigurationElement configurationElement : extension
							.getConfigurationElements()) {
						if (RESOURCE_FILTERS_FILTER_TAG.equals(configurationElement.getName())) {
							ResourceFilterRegistryEclipseUtil.removeFilter(configurationElement
									.getAttribute(RESOURCE_FILTERS_CLASS_ATTRIBUTE));
						}
					}
				}
			}
		}
	}
}

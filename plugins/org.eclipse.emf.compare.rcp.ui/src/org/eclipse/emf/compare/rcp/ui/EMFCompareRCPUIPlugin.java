/** ****************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.rcp.ui.internal.util.AbstractRegistryEventListener;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @since 3.0
 */
public class EMFCompareRCPUIPlugin extends AbstractUIPlugin {

	/**
	 * The plug-in ID.
	 */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.rcp.ui"; //$NON-NLS-1$

	public static final String GROUP_PROVIDER_PPID = "groups"; //$NON-NLS-1$

	public static final String FILTER_PROVIDER_PPID = "filters"; //$NON-NLS-1$

	/**
	 * the shared instance.
	 */
	private static EMFCompareRCPUIPlugin plugin;

	/** keep track of resources that should be freed when exiting. */
	private static Map<String, Image> resourcesMapper = new HashMap<String, Image>();

	private AbstractRegistryEventListener groupProviderRegistryListener;

	private IDifferenceGroupProvider.Registry groupProviderRegistry;

	private AbstractRegistryEventListener filterRegistryListener;

	private IDifferenceFilter.Registry filterRegistry;

	/**
	 * The constructor.
	 */
	public EMFCompareRCPUIPlugin() {

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();

		groupProviderRegistry = new IDifferenceGroupProvider.RegistryImpl();

		groupProviderRegistryListener = new DifferenceGroupProviderExtensionRegistryListener(PLUGIN_ID,
				GROUP_PROVIDER_PPID);
		extensionRegistry.addListener(groupProviderRegistryListener, PLUGIN_ID + "." + GROUP_PROVIDER_PPID); //$NON-NLS-1$
		groupProviderRegistryListener.readRegistry(extensionRegistry);

		filterRegistry = new IDifferenceFilter.RegistryImpl();

		filterRegistryListener = new FilterExtensionRegistryListener(PLUGIN_ID, FILTER_PROVIDER_PPID);
		extensionRegistry.addListener(filterRegistryListener, PLUGIN_ID + "." + FILTER_PROVIDER_PPID); //$NON-NLS-1$
		filterRegistryListener.readRegistry(extensionRegistry);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		Platform.getExtensionRegistry().removeListener(filterRegistryListener);
		filterRegistry = null;
		Platform.getExtensionRegistry().removeListener(groupProviderRegistryListener);
		groupProviderRegistry = null;

		plugin = null;
		disposeCachedImages();
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static EMFCompareRCPUIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Log an {@link Exception} in the {@link #getLog() current logger}.
	 * 
	 * @param e
	 *            the exception to be logged.
	 */
	public void log(Throwable e) {
		getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, e.getMessage(), e));
	}

	/**
	 * Log the given message with the give severity level. Severity is one of {@link IStatus#INFO},
	 * {@link IStatus#WARNING} and {@link IStatus#ERROR}.
	 * 
	 * @param severity
	 *            the severity of the message
	 * @param message
	 *            the message
	 */
	public void log(int severity, String message) {
		getLog().log(new Status(severity, PLUGIN_ID, message));
	}

	/**
	 * @return the groupProviderRegistry
	 */
	public IDifferenceGroupProvider.Registry getDifferenceGroupProviderRegistry() {
		return groupProviderRegistry;
	}

	public IDifferenceFilter.Registry getFilterActionRegistry() {
		return filterRegistry;
	}

	/**
	 * <p>
	 * returns a plugin image. The returned image does not need to be explicitly disposed.
	 * </p>
	 * 
	 * @param imagePath
	 *            : plugin relative path to the image
	 * @return Image : plugin hosted image
	 */
	public static Image getImage(String imagePath) {
		Image image = resourcesMapper.get(imagePath);
		if (image == null) {
			ImageDescriptor imageDescriptor = imageDescriptorFromPlugin(PLUGIN_ID, imagePath);
			image = imageDescriptor.createImage();
			resourcesMapper.put(imagePath, image);
		}
		return image;
	}

	/**
	 * <p>
	 * returns a plugin image descriptor.
	 * </p>
	 * 
	 * @param imagePath
	 *            : plugin relative path to the image
	 * @return ImageDescriptor : image descriptor.
	 */
	public static ImageDescriptor getImageDescriptor(String imagePath) {
		return imageDescriptorFromPlugin(PLUGIN_ID, imagePath);
	}

	/**
	 * Dispose image with the given id.
	 * 
	 * @param id
	 *            : dispose system resources associated with the image with the given id.
	 */
	public static void disposeImage(String id) {
		Image image = resourcesMapper.remove(id);
		if (image != null) {
			image.dispose();
		}
	}

	/**
	 * dispose system resources associated with cached images.
	 */
	public static void disposeCachedImages() {
		Iterator<Image> iterator = resourcesMapper.values().iterator();
		while (iterator.hasNext()) {
			iterator.next().dispose();
		}
		resourcesMapper.clear();
	}

	private class DifferenceGroupProviderExtensionRegistryListener extends AbstractRegistryEventListener {

		static final String TAG_GROUP_PROVIDER = "group"; //$NON-NLS-1$

		static final String ATT_CLASS = "class"; //$NON-NLS-1$

		static final String ATT_LABEL = "label"; //$NON-NLS-1$

		static final String ATT_ACTIVE = "activeByDefault"; //$NON-NLS-1$

		/**
		 * @param pluginID
		 * @param extensionPointID
		 * @param registry
		 */
		public DifferenceGroupProviderExtensionRegistryListener(String pluginID, String extensionPointID) {
			super(pluginID, extensionPointID);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.util.AbstractRegistryEventListener#readElement(org.eclipse.core.runtime.IConfigurationElement,
		 *      org.eclipse.emf.compare.rcp.ui.internal.util.AbstractRegistryEventListener.Action)
		 */
		@SuppressWarnings("boxing")
		@Override
		protected boolean readElement(IConfigurationElement element, Action b) {
			if (element.getName().equals(TAG_GROUP_PROVIDER)) {
				if (element.getAttribute(ATT_CLASS) == null) {
					logMissingAttribute(element, ATT_CLASS);
				} else if (element.getAttribute(ATT_LABEL) == null) {
					logMissingAttribute(element, ATT_LABEL);
				} else if (element.getAttribute(ATT_ACTIVE) == null) {
					logMissingAttribute(element, ATT_ACTIVE);
				} else {
					switch (b) {
						case ADD:
							try {
								IDifferenceGroupProvider provider = (IDifferenceGroupProvider)element
										.createExecutableExtension(ATT_CLASS);
								provider.setLabel(element.getAttribute(ATT_LABEL));
								if (Boolean.valueOf(element.getAttribute(ATT_ACTIVE))) {
									provider.setDefaultSelected(true);
								} else {
									provider.setDefaultSelected(false);
								}
								IDifferenceGroupProvider previous = groupProviderRegistry.add(provider);
								if (previous != null) {
									log(IStatus.WARNING, "The provider '" + provider.getClass().getName() //$NON-NLS-1$
											+ "' is registered twice."); //$NON-NLS-1$
								}
							} catch (CoreException e) {
								logError(element, e.getMessage());
							}
							break;
						case REMOVE:
							groupProviderRegistry.remove(element.getAttribute(ATT_CLASS));
							break;
					}
					return true;
				}
			}
			return false;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.util.AbstractRegistryEventListener#logError(org.eclipse.core.runtime.IConfigurationElement,
		 *      java.lang.String)
		 */
		@Override
		protected void logError(IConfigurationElement element, String string) {
			log(IStatus.ERROR, string);
		}
	}

	private class FilterExtensionRegistryListener extends AbstractRegistryEventListener {

		static final String TAG_FILTER_ACTION = "filter"; //$NON-NLS-1$

		static final String ATT_CLASS = "class"; //$NON-NLS-1$

		static final String ATT_LABEL = "label"; //$NON-NLS-1$

		static final String ATT_ACTIVE = "activeByDefault"; //$NON-NLS-1$

		/**
		 * @param pluginID
		 * @param extensionPointID
		 * @param registry
		 */
		public FilterExtensionRegistryListener(String pluginID, String extensionPointID) {
			super(pluginID, extensionPointID);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.util.AbstractRegistryEventListener#readElement(org.eclipse.core.runtime.IConfigurationElement,
		 *      org.eclipse.emf.compare.rcp.ui.internal.util.AbstractRegistryEventListener.Action)
		 */
		@SuppressWarnings("boxing")
		@Override
		protected boolean readElement(IConfigurationElement element, Action b) {
			if (element.getName().equals(TAG_FILTER_ACTION)) {
				if (element.getAttribute(ATT_CLASS) == null) {
					logMissingAttribute(element, ATT_CLASS);
				} else if (element.getAttribute(ATT_LABEL) == null) {
					logMissingAttribute(element, ATT_LABEL);
				} else if (element.getAttribute(ATT_ACTIVE) == null) {
					logMissingAttribute(element, ATT_ACTIVE);
				} else {
					switch (b) {
						case ADD:
							try {
								IDifferenceFilter filter = (IDifferenceFilter)element
										.createExecutableExtension(ATT_CLASS);
								filter.setLabel(element.getAttribute(ATT_LABEL));
								if (Boolean.valueOf(element.getAttribute(ATT_ACTIVE))) {
									filter.setDefaultSelected(true);
								} else {
									filter.setDefaultSelected(false);
								}
								IDifferenceFilter previous = filterRegistry.add(filter);
								if (previous != null) {
									log(IStatus.WARNING, "The filter '" + filter.getClass().getName() //$NON-NLS-1$
											+ "' is registered twice."); //$NON-NLS-1$
								}
							} catch (CoreException e) {
								logError(element, e.getMessage());
							}
							break;
						case REMOVE:
							filterRegistry.remove(element.getAttribute(ATT_CLASS));
							break;
					}
					return true;
				}
			}
			return false;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.util.AbstractRegistryEventListener#logError(org.eclipse.core.runtime.IConfigurationElement,
		 *      java.lang.String)
		 */
		@Override
		protected void logError(IConfigurationElement element, String string) {
			log(IStatus.ERROR, string);
		}
	}
}

/** ****************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
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

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory.IAccessorFactory;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory.impl.AccessorFactoryExtensionRegistryListener;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory.impl.AccessorFactoryRegistryImpl;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.DifferenceFilterExtensionRegistryListener;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.DifferenceFilterRegistryImpl;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.extender.DifferenceGroupExtenderRegistryImpl;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.extender.DifferenceGroupExtenderRegistryListener;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.extender.IDifferenceGroupExtender;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DifferenceGroupProviderExtensionRegistryListener;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DifferenceGroupRegistryImpl;
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

	public static final String ACCESSOR_FACTORY_PPID = "accessorFactory"; //$NON-NLS-1$

	/**
	 * @since 4.0
	 */
	public static final String DIFFERENCE_GROUP_EXTENDER_PPID = "differenceGroupExtender"; //$NON-NLS-1$

	/**
	 * the shared instance.
	 */
	private static EMFCompareRCPUIPlugin plugin;

	/** keep track of resources that should be freed when exiting. */
	private static Map<String, Image> resourcesMapper = new HashMap<String, Image>();

	private AbstractRegistryEventListener groupProviderRegistryListener;

	private IDifferenceGroupProvider.Descriptor.Registry groupProviderRegistry;

	private AbstractRegistryEventListener filterRegistryListener;

	private IDifferenceFilter.Registry filterRegistry;

	private AbstractRegistryEventListener accessorFactoryRegistryListener;

	private IAccessorFactory.Registry accessorFactoryRegistry;

	private AbstractRegistryEventListener differenceGroupExtenderRegistryListener;

	private IDifferenceGroupExtender.Registry differenceGroupExtenderRegistry;

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

		groupProviderRegistry = new DifferenceGroupRegistryImpl();
		groupProviderRegistryListener = new DifferenceGroupProviderExtensionRegistryListener(PLUGIN_ID,
				GROUP_PROVIDER_PPID, getLog(), groupProviderRegistry);
		extensionRegistry.addListener(groupProviderRegistryListener, PLUGIN_ID + "." + GROUP_PROVIDER_PPID); //$NON-NLS-1$
		groupProviderRegistryListener.readRegistry(extensionRegistry);

		filterRegistry = new DifferenceFilterRegistryImpl();
		filterRegistryListener = new DifferenceFilterExtensionRegistryListener(PLUGIN_ID,
				FILTER_PROVIDER_PPID, getLog(), filterRegistry);
		extensionRegistry.addListener(filterRegistryListener, PLUGIN_ID + "." + FILTER_PROVIDER_PPID); //$NON-NLS-1$
		filterRegistryListener.readRegistry(extensionRegistry);

		accessorFactoryRegistry = new AccessorFactoryRegistryImpl();
		accessorFactoryRegistryListener = new AccessorFactoryExtensionRegistryListener(PLUGIN_ID,
				ACCESSOR_FACTORY_PPID, getLog(), accessorFactoryRegistry);
		extensionRegistry.addListener(accessorFactoryRegistryListener, PLUGIN_ID
				+ "." + ACCESSOR_FACTORY_PPID); //$NON-NLS-1$
		accessorFactoryRegistryListener.readRegistry(extensionRegistry);

		differenceGroupExtenderRegistry = new DifferenceGroupExtenderRegistryImpl();
		differenceGroupExtenderRegistryListener = new DifferenceGroupExtenderRegistryListener(PLUGIN_ID,
				DIFFERENCE_GROUP_EXTENDER_PPID, getLog(), differenceGroupExtenderRegistry);
		extensionRegistry.addListener(differenceGroupExtenderRegistryListener, PLUGIN_ID
				+ "." + DIFFERENCE_GROUP_EXTENDER_PPID); //$NON-NLS-1$
		differenceGroupExtenderRegistryListener.readRegistry(extensionRegistry);

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();

		extensionRegistry.removeListener(differenceGroupExtenderRegistryListener);
		differenceGroupExtenderRegistryListener = null;
		differenceGroupExtenderRegistry = null;

		extensionRegistry.removeListener(accessorFactoryRegistryListener);
		accessorFactoryRegistryListener = null;
		accessorFactoryRegistry = null;

		extensionRegistry.removeListener(filterRegistryListener);
		filterRegistryListener = null;
		filterRegistry = null;

		extensionRegistry.removeListener(groupProviderRegistryListener);
		groupProviderRegistryListener = null;
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
	 * @since 4.0
	 */
	public IDifferenceGroupProvider.Descriptor.Registry getDifferenceGroupProviderRegistry() {
		return groupProviderRegistry;
	}

	/**
	 * @since 4.0
	 */
	public IDifferenceFilter.Registry getDifferenceFilterRegistry() {
		return filterRegistry;
	}

	/**
	 * @return the registry
	 */
	public IAccessorFactory.Registry getAccessorFactoryRegistry() {
		return accessorFactoryRegistry;
	}

	/**
	 * @return the sub tree registry
	 * @since 4.0
	 */
	public IDifferenceGroupExtender.Registry getDifferenceGroupExtenderRegistry() {
		return differenceGroupExtenderRegistry;
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
}

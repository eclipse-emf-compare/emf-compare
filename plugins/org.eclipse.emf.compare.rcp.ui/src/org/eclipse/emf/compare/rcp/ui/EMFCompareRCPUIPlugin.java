/******************************************************************************
 * Copyright (c) 2012, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;
import org.eclipse.emf.compare.rcp.internal.extension.IItemRegistry;
import org.eclipse.emf.compare.rcp.internal.extension.impl.ItemRegistry;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.factory.IAccessorFactory;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.ui.ConfigurationUIRegistryEventListener;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.ui.IConfigurationUIFactory;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory.impl.AccessorFactoryExtensionRegistryListener;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory.impl.AccessorFactoryRegistryImpl;
import org.eclipse.emf.compare.rcp.ui.internal.preferences.LoggingPreferencePage;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.DifferenceFilterExtensionRegistryListener;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.DifferenceFilterManager;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.DifferenceFilterRegistryImpl;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.extender.DifferenceGroupExtenderRegistryImpl;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.extender.DifferenceGroupExtenderRegistryListener;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DifferenceGroupManager;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DifferenceGroupProviderExtensionRegistryListener;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DifferenceGroupRegistryImpl;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.extender.IDifferenceGroupExtender;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.Preferences;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @since 3.0
 */
public class EMFCompareRCPUIPlugin extends AbstractUIPlugin {

	/**
	 * Log4j logger to use throughout EMFCompare for logging purposes.
	 * 
	 * @since 4.1
	 */
	public static final Logger LOGGER = Logger.getLogger("org.eclipse.emf.compare"); //$NON-NLS-1$

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
	 * @since 4.0
	 */
	private static final String MATCH_ENGINE_FACTORY_CONFIGURATION_UI_PPID = "matchEngineFactoryConfigurationUI";//$NON-NLS-1$

	/**
	 * Pattern used for log4j logging:
	 * 
	 * <pre>
	 * Date [Thread name] LEVEL 3.last.segments.of.logger.name <NDC Tag> - message\n
	 * </pre>
	 */
	private static final String LOG_PATTERN = "%d{ISO8601} [%t] %-5p %c{3} %x - %m%n"; //$NON-NLS-1$

	/**
	 * the shared instance.
	 */
	private static EMFCompareRCPUIPlugin plugin;

	/** keep track of resources that should be freed when exiting. */
	private static Map<String, Image> resourcesMapper = new HashMap<String, Image>();

	private AbstractRegistryEventListener groupProviderRegistryListener;

	private IItemRegistry<IDifferenceGroupProvider.Descriptor> groupItemRegistry;

	private IDifferenceGroupProvider.Descriptor.Registry groupProviderRegistry;

	private AbstractRegistryEventListener filterRegistryListener;

	private IDifferenceFilter.Registry filterRegistry;

	private DifferenceFilterManager filterManager;

	private AbstractRegistryEventListener accessorFactoryRegistryListener;

	private IAccessorFactory.Registry accessorFactoryRegistry;

	private AbstractRegistryEventListener differenceGroupExtenderRegistryListener;

	private IDifferenceGroupExtender.Registry differenceGroupExtenderRegistry;

	private Map<String, IConfigurationUIFactory> matchEngineConfiguratorRegistry;

	private ConfigurationUIRegistryEventListener matchEngineConfiguratorRegistryListener;

	private IEMFCompareConfiguration compareConfiguration;

	private IPropertyChangeListener propertyChangeListener;

	/**
	 * Instance scope for preferences.
	 * <p>
	 * Do not use singleton to respect Helios compatibility
	 * </p>
	 * 
	 * @see org.eclipse.core.runtime.preferences.InstanceScope#INSTANCE
	 */
	@SuppressWarnings("deprecation")
	private InstanceScope instanceScope = new InstanceScope();

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

		groupItemRegistry = new ItemRegistry<IDifferenceGroupProvider.Descriptor>();
		DifferenceGroupManager groupManager = new DifferenceGroupManager(getEMFCompareUIPreferences(),
				groupItemRegistry);
		groupProviderRegistry = new DifferenceGroupRegistryImpl(groupManager, groupItemRegistry);
		groupProviderRegistryListener = new DifferenceGroupProviderExtensionRegistryListener(PLUGIN_ID,
				GROUP_PROVIDER_PPID, getLog(), groupItemRegistry);

		extensionRegistry.addListener(groupProviderRegistryListener, PLUGIN_ID + "." + GROUP_PROVIDER_PPID); //$NON-NLS-1$
		groupProviderRegistryListener.readRegistry(extensionRegistry);

		filterManager = new DifferenceFilterManager(getEMFCompareUIPreferences());
		filterRegistry = new DifferenceFilterRegistryImpl(filterManager);
		filterRegistryListener = new DifferenceFilterExtensionRegistryListener(PLUGIN_ID,
				FILTER_PROVIDER_PPID, getLog(), filterManager);
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

		matchEngineConfiguratorRegistry = new ConcurrentHashMap<String, IConfigurationUIFactory>();
		matchEngineConfiguratorRegistryListener = new ConfigurationUIRegistryEventListener(PLUGIN_ID,
				MATCH_ENGINE_FACTORY_CONFIGURATION_UI_PPID, getLog(), matchEngineConfiguratorRegistry,
				EMFCompareRCPPlugin.getDefault().getMatchEngineFactoryDescriptorRegistry());
		extensionRegistry.addListener(matchEngineConfiguratorRegistryListener, PLUGIN_ID + '.'
				+ MATCH_ENGINE_FACTORY_CONFIGURATION_UI_PPID);
		matchEngineConfiguratorRegistryListener.readRegistry(extensionRegistry);

		initLogging();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		getPreferenceStore().removePropertyChangeListener(propertyChangeListener);
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();

		extensionRegistry.removeListener(matchEngineConfiguratorRegistryListener);
		matchEngineConfiguratorRegistry = null;
		matchEngineConfiguratorRegistryListener = null;

		extensionRegistry.removeListener(differenceGroupExtenderRegistryListener);
		differenceGroupExtenderRegistryListener = null;
		differenceGroupExtenderRegistry = null;

		extensionRegistry.removeListener(accessorFactoryRegistryListener);
		accessorFactoryRegistryListener = null;
		accessorFactoryRegistry = null;

		extensionRegistry.removeListener(filterRegistryListener);
		filterRegistryListener = null;
		filterRegistry = null;
		filterManager = null;

		extensionRegistry.removeListener(groupProviderRegistryListener);
		groupProviderRegistryListener = null;
		groupItemRegistry = null;
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
	 * Initializes log4j by reading the preferences.
	 */
	private void initLogging() {
		resetDefaultLoggingPreferences();
		LOGGER.setLevel(Level.toLevel(getPreferenceStore().getString(LoggingPreferencePage.LOG_LEVEL_KEY)));
		RollingFileAppender appender = (RollingFileAppender)LOGGER
				.getAppender(LoggingPreferencePage.EMFC_APPENDER_NAME);
		String logFileName = getPreferenceStore().getString(LoggingPreferencePage.LOG_FILENAME_KEY);
		if (logFileName.length() > 0) {
			if (appender == null) {
				try {
					createLogAppender(logFileName);
				} catch (IOException e) {
					// Invalidate file name
					getPreferenceStore().setToDefault(LoggingPreferencePage.LOG_FILENAME_KEY);
				}
			} else {
				appender.setMaxBackupIndex(getPreferenceStore().getInt(
						LoggingPreferencePage.LOG_BACKUP_COUNT_KEY));
				appender.setMaximumFileSize((getPreferenceStore()
						.getLong(LoggingPreferencePage.LOG_FILE_MAX_SIZE_KEY)) * 1024 * 1024);
			}
		}
		propertyChangeListener = new LoggingPropertyChangeListener();
		getPreferenceStore().addPropertyChangeListener(propertyChangeListener);
	}

	private void resetDefaultLoggingPreferences() {
		getPreferenceStore().setDefault(LoggingPreferencePage.LOG_FILENAME_KEY, ""); //$NON-NLS-1$
		getPreferenceStore().setDefault(LoggingPreferencePage.LOG_LEVEL_KEY, "OFF"); //$NON-NLS-1$
		getPreferenceStore().setValue(LoggingPreferencePage.LOG_BACKUP_COUNT_KEY, 20);
		getPreferenceStore().setValue(LoggingPreferencePage.LOG_FILE_MAX_SIZE_KEY, 10);
	}

	/**
	 * Creates the RollingFileAppender used to log with log4j.
	 * 
	 * @param newFileName
	 *            Path opf the log file
	 * @throws IOException
	 *             If an IO problem occurs, like the given path does not represent a file, or it cannot be
	 *             written;
	 */
	private void createLogAppender(String newFileName) throws IOException {
		RollingFileAppender appender;
		appender = new RollingFileAppender(new PatternLayout(LOG_PATTERN), newFileName, true);
		LOGGER.removeAllAppenders(); // We don't want to log elsewhere
		LOGGER.addAppender(appender);
		appender.setMaxBackupIndex(getPreferenceStore().getInt(LoggingPreferencePage.LOG_BACKUP_COUNT_KEY));
		appender.setMaximumFileSize((EMFCompareRCPUIPlugin.getDefault().getPreferenceStore()
				.getLong(LoggingPreferencePage.LOG_FILE_MAX_SIZE_KEY)) * 1024 * 1024);
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
	 * @return the item registry for group providers.
	 * @since 4.0
	 */
	public IItemRegistry<IDifferenceGroupProvider.Descriptor> getItemDifferenceGroupProviderRegistry() {
		return groupItemRegistry;
	}

	/**
	 * @since 4.0
	 */
	public IDifferenceFilter.Registry getDifferenceFilterRegistry() {
		return filterRegistry;
	}

	/**
	 * @return The Difference Filter manager.
	 * @since 4.0
	 */
	public DifferenceFilterManager getDifferenceFilterManager() {
		return filterManager;
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

	/**
	 * Get the Match Engine Configurator Registry
	 * 
	 * @return Map<String, IConfigurationUIFactory>
	 * @since 4.0
	 */
	public Map<String, IConfigurationUIFactory> getMatchEngineConfiguratorRegistry() {
		return matchEngineConfiguratorRegistry;
	}

	/**
	 * @return the preferences related to EMF Compare RCP UI plugin.
	 */
	public Preferences getEMFCompareUIPreferences() {
		return instanceScope.getNode(PLUGIN_ID);
	}

	/**
	 * @return the compare configuration object.
	 * @since 4.1
	 */
	public IEMFCompareConfiguration getEMFCompareConfiguration() {
		return compareConfiguration;
	}

	/**
	 * Set the compare configuration object.
	 * 
	 * @param compareConfiguration
	 *            the compare configuration object
	 * @since 4.1
	 */
	public void setEMFCompareConfiguration(IEMFCompareConfiguration compareConfiguration) {
		this.compareConfiguration = compareConfiguration;
	}

	private static class LoggingPropertyChangeListener implements IPropertyChangeListener {

		public void propertyChange(PropertyChangeEvent event) {
			if (LoggingPreferencePage.LOG_FILENAME_KEY.equals(event.getProperty())) {
				String newFileName = (String)event.getNewValue();
				RollingFileAppender appender = (RollingFileAppender)LOGGER
						.getAppender(LoggingPreferencePage.EMFC_APPENDER_NAME);
				if (newFileName != null && newFileName.length() > 0) {
					if (appender == null) {
						try {
							EMFCompareRCPUIPlugin.getDefault().createLogAppender(newFileName);
						} catch (IOException e) {
							EMFCompareRCPUIPlugin.getDefault().getPreferenceStore().setToDefault(
									LoggingPreferencePage.LOG_FILENAME_KEY);
							MessageDialog.openError(Display.getCurrent().getActiveShell(),
									EMFCompareRCPUIMessages
											.getString("LoggingPreferencePage.appender.error.title"), //$NON-NLS-1$
									EMFCompareRCPUIMessages.getString(
											"LoggingPreferencePage.appender.error.msg", //$NON-NLS-1$
											newFileName, e.getMessage()));
						}
					} else {
						appender.setFile(newFileName);
					}
				} else {
					// No file name, remove appender
					LOGGER.removeAllAppenders();
				}
			} else if (LoggingPreferencePage.LOG_LEVEL_KEY.equals(event.getProperty())) {
				LOGGER.setLevel(Level.toLevel((String)event.getNewValue()));
			} else if (LoggingPreferencePage.LOG_BACKUP_COUNT_KEY.equals(event.getProperty())) {
				RollingFileAppender appender = (RollingFileAppender)LOGGER
						.getAppender(LoggingPreferencePage.EMFC_APPENDER_NAME);
				if (appender != null) {
					appender.setMaxBackupIndex(Integer.parseInt(((String)event.getNewValue())));
				}
			} else if (LoggingPreferencePage.LOG_FILE_MAX_SIZE_KEY.equals(event.getProperty())) {
				RollingFileAppender appender = (RollingFileAppender)LOGGER
						.getAppender(LoggingPreferencePage.EMFC_APPENDER_NAME);
				if (appender != null) {
					appender.setMaximumFileSize(Long.parseLong((String)event.getNewValue()) * 1024 * 1024);
				}
			}
		}
	}
}

/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.postprocessor.PostProcessorDescriptorRegistryImpl;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;
import org.eclipse.emf.compare.rcp.internal.merger.MergerExtensionRegistryListener;
import org.eclipse.emf.compare.rcp.internal.policy.LoadOnDemandPolicyRegistryImpl;
import org.eclipse.emf.compare.rcp.internal.policy.LoadOnDemandPolicyRegistryListener;
import org.eclipse.emf.compare.rcp.internal.postprocessor.PostProcessorFactoryRegistryListener;
import org.eclipse.emf.compare.rcp.policy.ILoadOnDemandPolicy;
import org.osgi.framework.BundleContext;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareRCPPlugin extends Plugin {

	/** The plug-in ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.rcp"; //$NON-NLS-1$

	public static final String POST_PROCESSOR_PPID = "postProcessor"; //$NON-NLS-1$

	/** The id of the load on demand policy extension point. */
	public static final String LOAD_ON_DEMAND_POLICY_PPID = "loadOnDemandPolicy"; //$NON-NLS-1$

	/** The id of the merger extension point. */
	public static final String MERGER_PPID = "merger"; //$NON-NLS-1$

	// This plugin is a singleton, so it's quite ok to keep the plugin in a static field.
	private static EMFCompareRCPPlugin plugin;

	private IMerger.Registry mergerRegistry;

	private AbstractRegistryEventListener mergerRegistryListener;

	/** The registry that will hold references to all {@link ILoadOnDemandPolicy}. **/
	private ILoadOnDemandPolicy.Registry loadOnDemandRegistry;

	/** The registry listener that will be used to react to load on demand policy changes. */
	private AbstractRegistryEventListener loadOnDemandRegistryListener;

	/**
	 * The registry that will hold references to all post processors.
	 */
	private IPostProcessor.Descriptor.Registry<String> postProcessorDescriptorsRegistry;

	/** The registry listener that will be used to react to post processor changes. */
	private AbstractRegistryEventListener postProcessorFactoryRegistryListener;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		EMFCompareRCPPlugin.plugin = this;

		final IExtensionRegistry registry = Platform.getExtensionRegistry();

		mergerRegistry = new IMerger.RegistryImpl();
		mergerRegistryListener = new MergerExtensionRegistryListener(PLUGIN_ID, MERGER_PPID, getLog(),
				mergerRegistry);
		registry.addListener(mergerRegistryListener, PLUGIN_ID + '.' + MERGER_PPID);
		mergerRegistryListener.readRegistry(registry);

		postProcessorDescriptorsRegistry = new PostProcessorDescriptorRegistryImpl<String>();
		postProcessorFactoryRegistryListener = new PostProcessorFactoryRegistryListener(PLUGIN_ID,
				POST_PROCESSOR_PPID, getLog(), postProcessorDescriptorsRegistry);
		registry.addListener(postProcessorFactoryRegistryListener, PLUGIN_ID + '.' + POST_PROCESSOR_PPID);
		postProcessorFactoryRegistryListener.readRegistry(registry);

		loadOnDemandRegistry = new LoadOnDemandPolicyRegistryImpl();
		loadOnDemandRegistryListener = new LoadOnDemandPolicyRegistryListener(loadOnDemandRegistry,
				PLUGIN_ID, LOAD_ON_DEMAND_POLICY_PPID, getLog());
		registry.addListener(loadOnDemandRegistryListener, PLUGIN_ID + '.' + LOAD_ON_DEMAND_POLICY_PPID);
		loadOnDemandRegistryListener.readRegistry(registry);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		EMFCompareRCPPlugin.plugin = null;

		final IExtensionRegistry registry = Platform.getExtensionRegistry();

		registry.removeListener(loadOnDemandRegistryListener);
		loadOnDemandRegistryListener = null;
		loadOnDemandRegistry = null;

		registry.removeListener(postProcessorFactoryRegistryListener);
		postProcessorFactoryRegistryListener = null;
		postProcessorDescriptorsRegistry = null;

		registry.removeListener(mergerRegistryListener);
		mergerRegistryListener = null;
		mergerRegistry = null;
	}

	/**
	 * Returns the merger registry to which extension will be registered.
	 * 
	 * @return the merger registry to which extension will be registered
	 * @since 3.0
	 */
	public IMerger.Registry getMergerRegistry() {
		return mergerRegistry;
	}

	/**
	 * Returns the post processor registry to which extension will be registered.
	 * 
	 * @return the post processor registry to which extension will be registered
	 */
	public IPostProcessor.Descriptor.Registry<String> getPostProcessorRegistry() {
		return postProcessorDescriptorsRegistry;
	}

	/**
	 * Returns the registry of load on demand policies.
	 * 
	 * @return the registry of load on demand policies.
	 */
	public ILoadOnDemandPolicy.Registry getLoadOnDemandPolicyRegistry() {
		return loadOnDemandRegistry;
	}

	/**
	 * Log the given message with the given severity to the logger of this plugin.
	 * 
	 * @param severity
	 *            the severity of the message.
	 * @param message
	 *            the message to log.
	 */
	public void log(int severity, String message) {
		getLog().log(new Status(severity, PLUGIN_ID, message));
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static EMFCompareRCPPlugin getDefault() {
		return plugin;
	}

}

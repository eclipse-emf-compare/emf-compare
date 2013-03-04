/*******************************************************************************
 * Copyright (c) 2011, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.ide.internal.policy.LoadOnDemandPolicyRegistryImpl;
import org.eclipse.emf.compare.ide.internal.policy.LoadOnDemandPolicyRegistryListener;
import org.eclipse.emf.compare.ide.policy.ILoadOnDemandPolicy;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFCompareIDEPlugin extends Plugin {
	/** The plug-in ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.ide"; //$NON-NLS-1$

	/** The id of the load on demand policy extension point. */
	public static final String LOAD_ON_DEMAND_POLICY_PPID = "loadOnDemandPolicy"; //$NON-NLS-1$

	/** This plugin's shared instance. */
	private static EMFCompareIDEPlugin plugin;

	/** The registry that will hold references to all {@link ILoadOnDemandPolicy}. **/
	private ILoadOnDemandPolicy.Registry loadOnDemandRegistry;

	/** The registry listener that will be used to react to load on demand policy changes. */
	private LoadOnDemandPolicyRegistryListener loadOnDemandRegistryListener;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		plugin = this;
		super.start(context);

		final IExtensionRegistry registry = Platform.getExtensionRegistry();

		this.loadOnDemandRegistry = new LoadOnDemandPolicyRegistryImpl();
		this.loadOnDemandRegistryListener = new LoadOnDemandPolicyRegistryListener(loadOnDemandRegistry,
				PLUGIN_ID, LOAD_ON_DEMAND_POLICY_PPID, getLog());

		registry.addListener(loadOnDemandRegistryListener, PLUGIN_ID + '.' + LOAD_ON_DEMAND_POLICY_PPID);
		loadOnDemandRegistryListener.readRegistry(registry);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;

		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.removeListener(loadOnDemandRegistryListener);
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
	 * Returns the registry of load on demand policies.
	 * 
	 * @return the registry of load on demand policies.
	 */
	public ILoadOnDemandPolicy.Registry getLoadOnDemandPolicyRegistry() {
		return loadOnDemandRegistry;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static EMFCompareIDEPlugin getDefault() {
		return plugin;
	}
}

package org.eclipse.emf.compare;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class MatchPlugin extends Plugin {

	/**
	 * The plugin ID
	 */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.match"; //$NON-NLS-1$

	// The shared instance
	private static MatchPlugin plugin;

	/**
	 * The constructor
	 */
	public MatchPlugin() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static MatchPlugin getDefault() {
		return plugin;
	}
}

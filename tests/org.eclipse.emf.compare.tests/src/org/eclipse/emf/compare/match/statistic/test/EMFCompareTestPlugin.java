package org.eclipse.emf.compare.match.statistic.test;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class EMFCompareTestPlugin extends AbstractUIPlugin {

	/**
	 *  The plug-in ID
	 */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.tests"; //$NON-NLS-1$

	// The shared instance
	private static EMFCompareTestPlugin plugin;

	/**
	 * The constructor
	 */
	public EMFCompareTestPlugin() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static EMFCompareTestPlugin getDefault() {
		return plugin;
	}

}

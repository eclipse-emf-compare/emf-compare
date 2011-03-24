package org.eclipse.emf.compare.logical;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public static final String PLUGIN_ID = "org.eclipse.emf.compare.logical"; //$NON-NLS-1$

	private static BundleContext context;

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	static BundleContext getContext() {
		return context;
	}
}

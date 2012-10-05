package org.eclipse.emf.compare.diagram.ide;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.emf.compare.diagram.provider.ViewLabelProviderExtensionRegistry;
import org.eclipse.emf.compare.diagram.provider.internal.ViewLabelProviderRegistryListener;
import org.osgi.framework.BundleContext;

public class GMFCompareIDEPlugin extends Plugin {

	/** The plug-in ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.diagram.ide"; //$NON-NLS-1$
	
	/** This plugin's shared instance. */
	private static GMFCompareIDEPlugin plugin;
	
	/** The registry listener that will be used to listen to extension changes. */
	private ViewLabelProviderRegistryListener registryListener = new ViewLabelProviderRegistryListener();

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		plugin = this;
		super.start(context);
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.addListener(registryListener, ViewLabelProviderRegistryListener.VIEW_LABELPROVIDER_EXTENSION_POINT);
		registryListener.parseInitialContributions();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.removeListener(registryListener);
		ViewLabelProviderExtensionRegistry.clearRegistry();
		
		super.stop(context);
		plugin = null;
	}
	
	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static GMFCompareIDEPlugin getDefault() {
		return plugin;
	}

}

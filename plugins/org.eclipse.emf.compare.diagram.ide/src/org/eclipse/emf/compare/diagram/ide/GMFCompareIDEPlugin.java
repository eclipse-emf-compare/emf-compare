package org.eclipse.emf.compare.diagram.ide;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.diagram.diff.DiagramComparisonConfiguration;
import org.eclipse.emf.compare.diagram.diff.DiagramDiffExtensionPostProcessor;
import org.eclipse.emf.compare.diagram.diff.util.DiagramCompareConstants;
import org.eclipse.emf.compare.diagram.provider.ViewLabelProviderExtensionRegistry;
import org.eclipse.emf.compare.diagram.provider.internal.ViewLabelProviderRegistryListener;
import org.eclipse.emf.compare.extension.PostProcessorDescriptor;
import org.eclipse.emf.compare.extension.PostProcessorRegistry;
import org.eclipse.emf.compare.ide.EMFCompareIDEPlugin;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class GMFCompareIDEPlugin extends AbstractUIPlugin {

	/** The plug-in ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.diagram.ide"; //$NON-NLS-1$
	
	/** This plugin's shared instance. */
	private static GMFCompareIDEPlugin plugin;
	
	/** The registry listener that will be used to listen to extension changes. */
	private ViewLabelProviderRegistryListener registryListener = new ViewLabelProviderRegistryListener();

	private DiagramComparisonConfiguration configuration;

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
		
		configuration = new DiagramComparisonConfiguration();
		getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				// XXX: does it work ?
				
				if (DiagramCompareConstants.PREFERENCES_KEY_MOVE_THRESHOLD.equals(event.getProperty())) {
					Object newValue = event.getNewValue();
					if (newValue != null) {
						configuration.setMoveThreshold((Integer)newValue);
					} else {
						configuration.setMoveThreshold(0);
					}
				}
			}
		});
		configuration.setMoveThreshold(getPreferenceStore().getInt(DiagramCompareConstants.PREFERENCES_KEY_MOVE_THRESHOLD));
		
		PostProcessorRegistry postProcessorRegistry = EMFCompareIDEPlugin.getDefault().getPostProcessorRegistry();
		for (PostProcessorDescriptor descriptor : postProcessorRegistry.getRegisteredPostProcessors()) {
			if (descriptor.getExtensionClassName().equals(DiagramDiffExtensionPostProcessor.class.getName())) {
				DiagramDiffExtensionPostProcessor postProcessor = (DiagramDiffExtensionPostProcessor)descriptor.getPostProcessor();
				postProcessor.setConfiguration(configuration);
			}
		}
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

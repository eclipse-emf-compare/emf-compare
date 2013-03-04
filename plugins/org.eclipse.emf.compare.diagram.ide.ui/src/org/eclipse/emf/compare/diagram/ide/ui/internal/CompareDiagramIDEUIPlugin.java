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
package org.eclipse.emf.compare.diagram.ide.ui.internal;

import org.eclipse.emf.compare.diagram.internal.CompareDiagramConfiguration;
import org.eclipse.emf.compare.diagram.internal.CompareDiagramConstants;
import org.eclipse.emf.compare.diagram.internal.CompareDiagramPostProcessor;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CompareDiagramIDEUIPlugin extends AbstractUIPlugin {

	/** The shared instance. */
	private static CompareDiagramIDEUIPlugin plugin;

	/** Specific configuration for diagram comparison. */
	private CompareDiagramConfiguration configuration;

	/**
	 * The constructor.
	 */
	public CompareDiagramIDEUIPlugin() {
		// Do nothing
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		configuration = new CompareDiagramConfiguration();
		getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				// XXX: does it work ?

				if (CompareDiagramConstants.PREFERENCES_KEY_MOVE_THRESHOLD.equals(event.getProperty())) {
					Object newValue = event.getNewValue();
					if (newValue != null) {
						configuration.setMoveThreshold((Integer)newValue);
					} else {
						configuration.setMoveThreshold(0);
					}
				}
			}
		});
		configuration.setMoveThreshold(getPreferenceStore().getInt(
				CompareDiagramConstants.PREFERENCES_KEY_MOVE_THRESHOLD));

		IPostProcessor.Registry postProcessorRegistry = EMFCompareRCPPlugin.getDefault()
				.getPostProcessorRegistry();
		for (IPostProcessor postprocessor : postProcessorRegistry.getPostProcessors()) {
			if (postprocessor instanceof CompareDiagramPostProcessor) {
				((CompareDiagramPostProcessor)postprocessor).setConfiguration(configuration);
			} else if (postprocessor instanceof IPostProcessor.Descriptor
					&& ((IPostProcessor.Descriptor)postprocessor).getPostProcessor() instanceof CompareDiagramPostProcessor) {
				((CompareDiagramPostProcessor)((IPostProcessor.Descriptor)postprocessor).getPostProcessor())
						.setConfiguration(configuration);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static CompareDiagramIDEUIPlugin getDefault() {
		return plugin;
	}

}

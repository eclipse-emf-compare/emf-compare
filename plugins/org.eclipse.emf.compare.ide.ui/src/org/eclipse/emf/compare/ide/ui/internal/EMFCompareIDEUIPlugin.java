/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - Bug 456699
 *     Michael Borkowski - Bug 462863
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.compare.internal.CompareEditor;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.ide.ui.dependency.ModelDependencyProviderRegistry;
import org.eclipse.emf.compare.ide.ui.dependency.ModelDependencyProviderRegistryListener;
import org.eclipse.emf.compare.ide.ui.internal.editor.PropertySheetAdapterFactory;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.registry.ModelResolverRegistry;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.registry.ModelResolverRegistryListener;
import org.eclipse.emf.compare.ide.ui.internal.logical.view.registry.LogicalModelViewHandlerRegistry;
import org.eclipse.emf.compare.ide.ui.internal.logical.view.registry.LogicalModelViewHandlerRegistryListener;
import org.eclipse.emf.compare.ide.ui.internal.mergeresolution.MergeResolutionListenerRegistry;
import org.eclipse.emf.compare.ide.ui.internal.mergeresolution.MergeResolutionListenerRegistryListener;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class, controls the plug-in life cycle.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFCompareIDEUIPlugin extends AbstractUIPlugin {
	/** The plugin ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.ide.ui"; //$NON-NLS-1$

	/** Plug-in's shared instance. */
	private static EMFCompareIDEUIPlugin plugin;

	/** Model resolvers extension point. */
	private static final String MODEL_RESOLVER_PPID = "modelResolvers"; //$NON-NLS-1$

	/** Logical Model Editors Handlers extension point. */
	private static final String LOGICAL_MODEL_VIEW_HANDLERS_PPID = "logicalModelViewHandlers"; //$NON-NLS-1$

	/** Model dependency providers extension point. */
	private static final String MODEL_DEPENDENCY_PROVIDER_PPID = "modelDependencyProvider"; //$NON-NLS-1$

	/** Merge resolution listener extension point. */
	private static final String MERGE_RESOLUTION_PPID = "mergeResolutionListener"; //$NON-NLS-1$

	/** keep track of resources that should be freed when exiting. */
	private static Map<String, Image> resourcesMapper = new HashMap<String, Image>();

	/** Listener for the model resolver extension point. */
	private AbstractRegistryEventListener modelResolverRegistryListener;

	/** Listener for the merge resolution listener extension point. */
	private MergeResolutionListenerRegistryListener mergeResolutionListenerRegistryListener;

	/** Registry of model resolvers. */
	private ModelResolverRegistry modelResolverRegistry;

	/** Listener for the Logical Model View Handlers extension point. */
	private AbstractRegistryEventListener logicalModelViewHandlerRegistryListener;

	/** Registry of Logical Model View Handlers. */
	private LogicalModelViewHandlerRegistry logicalModelViewHandlerRegistry;

	/** Listener for the model dependency provider extension point. */
	private AbstractRegistryEventListener modelDependencyProviderRegistryListener;

	/** Registry of model dependency providers. */
	private ModelDependencyProviderRegistry modelDependencyProviderRegistry;

	/** Registry for the merge resolution listener extension point. */
	private MergeResolutionListenerRegistry mergeResolutionListenerRegistry;

	/** Default constructor. */
	public EMFCompareIDEUIPlugin() {
		// Empty constructor
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

		modelDependencyProviderRegistry = new ModelDependencyProviderRegistry();
		modelResolverRegistry = new ModelResolverRegistry();
		logicalModelViewHandlerRegistry = new LogicalModelViewHandlerRegistry();
		mergeResolutionListenerRegistry = new MergeResolutionListenerRegistry();

		final IExtensionRegistry globalRegistry = Platform.getExtensionRegistry();

		modelDependencyProviderRegistryListener = new ModelDependencyProviderRegistryListener(PLUGIN_ID,
				MODEL_DEPENDENCY_PROVIDER_PPID, getLog(), modelDependencyProviderRegistry);
		globalRegistry.addListener(modelDependencyProviderRegistryListener);
		modelDependencyProviderRegistryListener.readRegistry(globalRegistry);

		modelResolverRegistryListener = new ModelResolverRegistryListener(PLUGIN_ID, MODEL_RESOLVER_PPID,
				getLog(), modelResolverRegistry);
		globalRegistry.addListener(modelResolverRegistryListener);
		modelResolverRegistryListener.readRegistry(globalRegistry);

		mergeResolutionListenerRegistryListener = new MergeResolutionListenerRegistryListener(PLUGIN_ID,
				MERGE_RESOLUTION_PPID, getLog(), mergeResolutionListenerRegistry);
		globalRegistry.addListener(mergeResolutionListenerRegistryListener);
		mergeResolutionListenerRegistryListener.readRegistry(globalRegistry);

		logicalModelViewHandlerRegistryListener = new LogicalModelViewHandlerRegistryListener(PLUGIN_ID,
				LOGICAL_MODEL_VIEW_HANDLERS_PPID, getLog(), logicalModelViewHandlerRegistry);
		globalRegistry.addListener(logicalModelViewHandlerRegistryListener);
		logicalModelViewHandlerRegistryListener.readRegistry(globalRegistry);

		Platform.getAdapterManager().registerAdapters(new PropertySheetAdapterFactory(), CompareEditor.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		final IExtensionRegistry globalRegistry = Platform.getExtensionRegistry();
		globalRegistry.removeListener(logicalModelViewHandlerRegistryListener);
		logicalModelViewHandlerRegistry.clear();
		globalRegistry.removeListener(modelResolverRegistryListener);
		modelResolverRegistry.clear();
		globalRegistry.removeListener(modelDependencyProviderRegistryListener);
		modelDependencyProviderRegistry.clear();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance.
	 */
	public static EMFCompareIDEUIPlugin getDefault() {
		return plugin;
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
	 * Returns the registry containing all known model resolvers.
	 * 
	 * @return The registry containing all known model resolvers.
	 */
	public ModelResolverRegistry getModelResolverRegistry() {
		return modelResolverRegistry;
	}

	/**
	 * Returns the registry containing all known Logical Model View handlers.
	 * 
	 * @return The registry containing all known Logical Model View handlers.
	 */
	public LogicalModelViewHandlerRegistry getLogicalModelViewHandlerRegistry() {
		return logicalModelViewHandlerRegistry;
	}

	/**
	 * Returns the registry containing all known dependency providers.
	 * 
	 * @return The registry containing all known dependency providers.
	 */
	public ModelDependencyProviderRegistry getModelDependencyProviderRegistry() {
		return modelDependencyProviderRegistry;
	}

	/**
	 * Returns the registry containing all known merge resolution listeners.
	 * 
	 * @return the registry containing all known merge resolution listeners.
	 */
	public MergeResolutionListenerRegistry getMergeResolutionListenerRegistry() {
		return mergeResolutionListenerRegistry;
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
}

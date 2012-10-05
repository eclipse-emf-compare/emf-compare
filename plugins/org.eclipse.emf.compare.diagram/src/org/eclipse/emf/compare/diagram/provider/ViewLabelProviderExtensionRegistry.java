/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.provider;

import java.util.ArrayList;
import java.util.List;

/**
 * This will contain all extensions that have been parsed from the extension point.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ViewLabelProviderExtensionRegistry {

	/** List of extensions created from the extension point contributions. */
	private static final List<ViewLabelProviderExtensionDescriptor> VIEW_LABEL_PROVIDERS = new ArrayList<ViewLabelProviderExtensionDescriptor>();

	/**
	 * Constructor.
	 */
	private ViewLabelProviderExtensionRegistry() {
	}

	/**
	 * Adds an extension to the registry.
	 * 
	 * @param extension
	 *            The extension that is to be added to the registry.
	 */
	public static void addViewLabelProvider(ViewLabelProviderExtensionDescriptor extension) {
		VIEW_LABEL_PROVIDERS.add(extension);
	}

	/**
	 * Removes all extensions from the registry. This will be called at plugin stopping.
	 */
	public static void clearRegistry() {
		VIEW_LABEL_PROVIDERS.clear();
	}

	/**
	 * Returns a copy of the registered extensions list.
	 * 
	 * @return A copy of the registered extensions list.
	 */
	public static List<ViewLabelProviderExtensionDescriptor> getRegisteredViewLabelProviders() {
		return new ArrayList<ViewLabelProviderExtensionDescriptor>(VIEW_LABEL_PROVIDERS);
	}

	/**
	 * Removes a phantom from the registry.
	 * 
	 * @param extensionClassName
	 *            Qualified class name of the sync element which corresponding phantom is to be removed from
	 *            the registry.
	 */
	public static void removeExtension(String extensionClassName) {
		for (ViewLabelProviderExtensionDescriptor extension : getRegisteredViewLabelProviders()) {
			if (extension.getExtensionClassName().equals(extensionClassName)) {
				VIEW_LABEL_PROVIDERS.remove(extension);
			}
		}
	}

	/**
	 * Return the {@link IViewLabelProvider} for the given diagram type.
	 * 
	 * @param diagramType
	 *            The diagram type.
	 * @return {@link IViewLabelProvider}
	 */
	public static IViewLabelProvider getExtensionForType(String diagramType) {
		if (diagramType == null || "".equals(diagramType.trim())) { //$NON-NLS-1$
			throw new IllegalArgumentException("nsURI"); //$NON-NLS-1$
		}

		final List<ViewLabelProviderExtensionDescriptor> registeredExtensions = getRegisteredViewLabelProviders();
		for (ViewLabelProviderExtensionDescriptor viewLabelTypeProviderExtensionDescriptor : registeredExtensions) {
			if (diagramType.equals(viewLabelTypeProviderExtensionDescriptor.getDiagramType())) {
				return viewLabelTypeProviderExtensionDescriptor.getExtension();
			}
		}
		return null;
	}
}

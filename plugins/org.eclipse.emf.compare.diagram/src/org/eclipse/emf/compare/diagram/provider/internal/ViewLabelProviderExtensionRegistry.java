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
package org.eclipse.emf.compare.diagram.provider.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.diagram.provider.IViewLabelProvider;

/**
 * This will contain all extensions that have been parsed from the extension point.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public final class ViewLabelProviderExtensionRegistry {

	/** The instance of the registry. */
	public static final ViewLabelProviderExtensionRegistry INSTANCE = new ViewLabelProviderExtensionRegistry();

	/** List of extensions created from the extension point contributions. */
	private final List<ViewLabelProviderExtensionDescriptor> extensionsDescriptors;

	/**
	 * Constructor.
	 */
	private ViewLabelProviderExtensionRegistry() {
		extensionsDescriptors = new ArrayList<ViewLabelProviderExtensionDescriptor>();
	}

	/**
	 * Adds an extension to the registry.
	 * 
	 * @param extension
	 *            The extension that is to be added to the registry.
	 */
	public synchronized void addExtension(ViewLabelProviderExtensionDescriptor extension) {
		extensionsDescriptors.add(extension);
	}

	/**
	 * Removes all extensions from the registry. This will be called at plugin stopping.
	 */
	public synchronized void clearRegistry() {
		extensionsDescriptors.clear();
	}

	/**
	 * Returns a copy of the registered extensions list.
	 * 
	 * @return A copy of the registered extensions list.
	 */
	public synchronized List<ViewLabelProviderExtensionDescriptor> getRegisteredExtensionsDescriptors() {
		return new ArrayList<ViewLabelProviderExtensionDescriptor>(extensionsDescriptors);
	}

	/**
	 * Removes a phantom from the registry.
	 * 
	 * @param extensionClassName
	 *            Qualified class name of the sync element which corresponding phantom is to be removed from
	 *            the registry.
	 */
	public synchronized void removeExtension(String extensionClassName) {
		for (ViewLabelProviderExtensionDescriptor extension : getRegisteredExtensionsDescriptors()) {
			if (extension.getExtensionClassName().equals(extensionClassName)) {
				extensionsDescriptors.remove(extension);
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
	public IViewLabelProvider getExtensionForType(String diagramType) {
		if (diagramType == null || "".equals(diagramType.trim())) { //$NON-NLS-1$
			throw new IllegalArgumentException("nsURI"); //$NON-NLS-1$
		}

		final List<ViewLabelProviderExtensionDescriptor> registeredExtensions = getRegisteredExtensionsDescriptors();
		for (ViewLabelProviderExtensionDescriptor viewLabelTypeProviderExtensionDescriptor : registeredExtensions) {
			if (diagramType.equals(viewLabelTypeProviderExtensionDescriptor.getDiagramType())) {
				return viewLabelTypeProviderExtensionDescriptor.getViewLabelTypeProviderExtension();
			}
		}
		return null;
	}
}

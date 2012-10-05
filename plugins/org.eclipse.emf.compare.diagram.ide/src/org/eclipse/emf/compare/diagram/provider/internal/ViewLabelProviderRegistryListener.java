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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.diagram.provider.ViewLabelProviderExtensionRegistry;

/**
 * This listener will allow us to be aware of contribution changes against the extension point.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ViewLabelProviderRegistryListener implements IRegistryEventListener {

	/** Name of the extension point's tag. */
	public static final String VIEW_LABELPROVIDER_EXTENSION_POINT = "org.eclipse.emf.compare.diagram.ide.viewLabelProvider"; //$NON-NLS-1$

	/** Name of the extension tag point's tag. */
	private static final String VIEW_LABELPROVIDER_TAG = "viewLabelProvider"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IRegistryEventListener#added(org.eclipse.core.runtime.IExtension[])
	 */
	public void added(IExtension[] extensions) {
		for (IExtension extension : extensions) {
			parseExtension(extension);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IRegistryEventListener#added(org.eclipse.core.runtime.IExtensionPoint[])
	 */
	public void added(IExtensionPoint[] extensionPoints) {
		// no need to listen to this event
	}

	/**
	 * Though this listener reacts to the extension point changes, there could have been contributions before
	 * it's been registered. This will parse these initial contributions.
	 */
	public void parseInitialContributions() {
		final IExtensionRegistry registry = Platform.getExtensionRegistry();

		final IExtensionPoint extensionPoint = registry.getExtensionPoint(VIEW_LABELPROVIDER_EXTENSION_POINT);	
		if (extensionPoint != null) {
			for (IExtension extension :extensionPoint.getExtensions()) {
				parseExtension(extension);
			}
		}
		
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IRegistryEventListener#removed(org.eclipse.core.runtime.IExtension[])
	 */
	public void removed(IExtension[] extensions) {
		for (IExtension extension : extensions) {
			final IConfigurationElement[] configElements = extension.getConfigurationElements();
			for (IConfigurationElement elem : configElements) {
				if (VIEW_LABELPROVIDER_TAG.equals(elem.getName())) {
					final String extensionClassName = elem
							.getAttribute(ViewLabelProviderIDEExtensionDescriptor.ATTRIBUTE_CLASS);
					ViewLabelProviderExtensionRegistry.removeExtension(extensionClassName);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IRegistryEventListener#removed(org.eclipse.core.runtime.IExtensionPoint[])
	 */
	public void removed(IExtensionPoint[] extensionPoints) {
		// no need to listen to this event
	}

	/**
	 * Parses a single extension contribution.
	 * 
	 * @param extension
	 *            Parses the given extension and adds its contribution to the registry.
	 */
	private static void parseExtension(IExtension extension) {
		final IConfigurationElement[] configElements = extension.getConfigurationElements();
		for (IConfigurationElement elem : configElements) {
			if (VIEW_LABELPROVIDER_TAG.equals(elem.getName())) {
				ViewLabelProviderExtensionRegistry.addViewLabelProvider(new ViewLabelProviderIDEExtensionDescriptor(elem));
			}
		}
	}
}

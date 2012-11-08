/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.utils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.emf.common.util.WrappedException;

public abstract class AbstractRegistryEventListener implements IRegistryEventListener {

	protected static enum Action {
		ADD, REMOVE
	}

	private final String pluginID;

	private final String extensionPointID;

	private final IExtensionRegistry extensionRegistry;

	/**
	 * @param registry
	 */
	public AbstractRegistryEventListener(IExtensionRegistry extensionRegistry, String pluginID,
			String extensionPointID) {
		this.extensionRegistry = extensionRegistry;
		this.pluginID = pluginID;
		this.extensionPointID = extensionPointID;
	}

	/**
	 * 
	 */
	public void readRegistry() {
		IExtensionPoint point = extensionRegistry.getExtensionPoint(pluginID, extensionPointID);
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			for (int i = 0; i < elements.length; i++) {
				internalReadElement(elements[i], Action.ADD);
			}
		}
	}

	/**
	 * @param element
	 * @param b
	 * @return
	 */
	protected abstract boolean readElement(IConfigurationElement element,
			AbstractRegistryEventListener.Action b);

	/**
	 * @param iConfigurationElement
	 * @param b
	 */
	private void internalReadElement(IConfigurationElement element, AbstractRegistryEventListener.Action b) {
		boolean recognized = readElement(element, b);
		if (recognized) {
			IConfigurationElement[] children = element.getChildren();
			for (int i = 0; i < children.length; ++i) {
				internalReadElement(children[i], b);
			}
		} else {
			logError(element, "Error processing extension: " + element); //$NON-NLS-1$
		}
	}

	protected void logMissingAttribute(IConfigurationElement element, String attributeName) {
		logError(element, "The required attribute '" + attributeName + "' not defined"); //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * @param element
	 * @param string
	 */
	protected abstract void logError(IConfigurationElement element, String string);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IRegistryEventListener#added(org.eclipse.core.runtime.IExtension[])
	 */
	public void added(IExtension[] extensions) {
		for (IExtension extension : extensions) {
			if (extension.getExtensionPointUniqueIdentifier().equals(extensionPointID)) {
				IConfigurationElement[] configurationElement = extension.getConfigurationElements();
				for (int j = 0; j < configurationElement.length; ++j) {
					internalReadElement(configurationElement[j], Action.ADD);
				}
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
			if (extension.getExtensionPointUniqueIdentifier().equals(extensionPointID)) {
				IConfigurationElement[] configurationElement = extension.getConfigurationElements();
				for (int j = 0; j < configurationElement.length; ++j) {
					internalReadElement(configurationElement[j], Action.REMOVE);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IRegistryEventListener#added(org.eclipse.core.runtime.IExtensionPoint[])
	 */
	public void added(IExtensionPoint[] extensionPoints) {
		// no need to listen to this.
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IRegistryEventListener#removed(org.eclipse.core.runtime.IExtensionPoint[])
	 */
	public void removed(IExtensionPoint[] extensionPoints) {
		// no need to listen to this.
	}

	public static class PluginClassDescriptor {
		protected IConfigurationElement element;

		protected String attributeName;

		public PluginClassDescriptor(IConfigurationElement element, String attributeName) {
			this.element = element;
			this.attributeName = attributeName;
		}

		public Object createInstance() {
			try {
				return element.createExecutableExtension(attributeName);
			} catch (CoreException e) {
				throw new WrappedException(e);
			}
		}
	}
}

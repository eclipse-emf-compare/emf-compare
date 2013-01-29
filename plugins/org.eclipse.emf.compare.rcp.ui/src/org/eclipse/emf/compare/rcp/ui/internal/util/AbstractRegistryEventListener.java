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
package org.eclipse.emf.compare.rcp.ui.internal.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryEventListener;

/**
 * Abstract utility class to listen to the {@link IExtensionRegistry}. It provides base implementation to
 * {@link #added(IExtension[])} and {@link #removed(IExtension[])}. Users must implement
 * {@link #readElement(IConfigurationElement, Action)} according to their extension schema.
 * <p>
 * The helper method {@link #readRegistry()} is browsing already registered extension to the extension
 * registry making it easy to read the registry after having added the listener to it.
 * <p>
 * {@link #readRegistry()} is delegating to {@link #readElement(IConfigurationElement, Action)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractRegistryEventListener implements IRegistryEventListener {

	/**
	 * Enumeration that says if the {@link IConfigurationElement} is to be added or removed from the
	 * {@link IExtensionRegistry}.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	protected static enum Action {
		/**
		 * The {@link IConfigurationElement} is to be added.
		 */
		ADD,
		/**
		 * The {@link IConfigurationElement} is to be removed.
		 */
		REMOVE
	}

	/**
	 * The plugin ID which declare the extension point to be monitored.
	 */
	private final String pluginID;

	/**
	 * The extension point ID to be monitored.
	 */
	private final String extensionPointID;

	/**
	 * Creates a new registry event listener.
	 * 
	 * @param pluginID
	 *            The plugin ID which declare the extension point to be monitored.
	 * @param extensionPointID
	 *            The extension point ID to be monitored
	 */
	public AbstractRegistryEventListener(String pluginID, String extensionPointID) {
		this.pluginID = pluginID;
		this.extensionPointID = extensionPointID;
	}

	/**
	 * Reads the given registry and call {@link #readElement(IConfigurationElement, Action)} as the read
	 * {@link IConfigurationElement} were just added to it.
	 * 
	 * @param extensionRegistry
	 *            the registry to read.
	 */
	public void readRegistry(IExtensionRegistry extensionRegistry) {
		IExtensionPoint point = extensionRegistry.getExtensionPoint(pluginID, extensionPointID);
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			for (int i = 0; i < elements.length; i++) {
				internalReadElement(elements[i], Action.ADD);
			}
		}
	}

	/**
	 * Reads the given {@code element}. This method can be call when the element is added or remove. The
	 * {@code action} reflect it. It returns true if the element is recognized as valid regarding the
	 * monitored extension point. It will be call on sub elements recursively for all recognized ones.
	 * 
	 * @param element
	 *            the element to be read.
	 * @param action
	 *            is the element added or removed.
	 * @return true if the element is recognized as valid regarding the monitored extension point.
	 */
	protected abstract boolean readElement(IConfigurationElement element, Action action);

	/**
	 * Reads the given element and, if recognized, browse recursively the children and try to read it.
	 * 
	 * @param element
	 *            the element to be read.
	 * @param action
	 *            is the element added or removed.
	 */
	private void internalReadElement(IConfigurationElement element, Action action) {
		boolean recognized = readElement(element, action);
		if (recognized) {
			IConfigurationElement[] children = element.getChildren();
			for (int i = 0; i < children.length; ++i) {
				internalReadElement(children[i], action);
			}
		} else {
			logError(element, "Error processing extension: " + element); //$NON-NLS-1$
		}
	}

	/**
	 * Delegates the logging of a missing attribute to {@link #logError(IConfigurationElement, String)} with a
	 * proper message.
	 * 
	 * @param element
	 *            the element to which an attribute is missing.
	 * @param attributeName
	 *            the name of the missing attribute.
	 */
	protected void logMissingAttribute(IConfigurationElement element, String attributeName) {
		logError(element, "The required attribute '" + attributeName + "' not defined"); //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * Log the error to any mean (often the current plugin logger).
	 * 
	 * @param element
	 *            the element from which comes to the error.
	 * @param string
	 *            the message to be logged.
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

	/**
	 * Simple utility class to create proxy of extension that are
	 * {@link IConfigurationElement#createExecutableExtension(String) instantiable}.
	 * <p>
	 * No test of the {@link IConfigurationElement#isValid() validity} of the wrapped
	 * {@link IConfigurationElement} is performed. As such you should always extend this class while listening
	 * to the {@link IExtensionRegistry} and react properly the removal of the wrapped
	 * {@link IConfigurationElement}.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	public static class PluginClassDescriptor {
		/**
		 * The element from which create an instance.
		 */
		protected IConfigurationElement element;

		/**
		 * The name of the attribute that holds the class full name to be instantiated.
		 */
		protected String attributeName;

		/**
		 * Creates a new descriptor for given element keeping the class name to be instantiated in an
		 * attribute named {@code attributeName}.
		 * 
		 * @param element
		 *            The element from which create an instance.
		 * @param attributeName
		 *            The name of the attribute that holds the class full name to be instantiated.
		 */
		public PluginClassDescriptor(IConfigurationElement element, String attributeName) {
			this.element = element;
			this.attributeName = attributeName;
		}

		/**
		 * Creates a new instance.
		 * 
		 * @return the new instance.
		 * @throws RuntimeException
		 *             wraps a CoreException if an instance of the executable extension could not be created
		 *             for any reason.
		 */
		public Object createInstance() {
			try {
				return element.createExecutableExtension(attributeName);
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
		}
	}
}

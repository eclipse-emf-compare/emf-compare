/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.extension;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

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
	 * The namespace of the extension point to be monitored.
	 */
	private final String namespace;

	/**
	 * The extension point ID to be monitored.
	 */
	private final String extensionPointID;

	/** The log in which to report failures from this listener. */
	private final ILog log;

	/**
	 * Creates a new registry event listener.
	 * 
	 * @param namespace
	 *            The namespace of the extension point to be monitored.
	 * @param extensionPointID
	 *            The extension point ID to be monitored
	 * @param log
	 *            The log object to be used to log error and/or warning.
	 */
	public AbstractRegistryEventListener(String namespace, String extensionPointID, ILog log) {
		this.namespace = namespace;
		this.extensionPointID = extensionPointID;
		this.log = log;
	}

	/**
	 * Reads the given registry and call {@link #readElement(IConfigurationElement, Action)} as the read
	 * {@link IConfigurationElement} were just added to it.
	 * 
	 * @param extensionRegistry
	 *            the registry to read.
	 */
	public void readRegistry(IExtensionRegistry extensionRegistry) {
		IExtensionPoint point = extensionRegistry.getExtensionPoint(namespace, extensionPointID);
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
	protected boolean readElement(IConfigurationElement element, Action action) {
		final boolean ret;
		if (validateExtensionElement(element)) {
			switch (action) {
				case ADD:
					ret = addedValid(element);
					break;
				case REMOVE:
					ret = removedValid(element);
					break;
				default:
					ret = false;
					break;
			}
		} else {
			ret = false;
		}
		return ret;
	}

	/**
	 * Validates if the given element is an element for the given extension and is well constructed. Returns
	 * true if the element should be further parsed for addition or removal.
	 * 
	 * @param element
	 *            the element to validate.
	 * @return true if the element should be further parsed for addition or removal, else otherwise.
	 */
	protected abstract boolean validateExtensionElement(IConfigurationElement element);

	/**
	 * Process the given element as the addition of a valid element extension.
	 * 
	 * @param element
	 *            the element to be added.
	 * @return true if the given element has been added and if its children should be processed, false
	 *         otherwise.
	 */
	protected abstract boolean addedValid(IConfigurationElement element);

	/**
	 * Process the given element as the removal of a valid element extension.
	 * 
	 * @param element
	 *            the element to be removed.
	 * @return true if the given element has been removed and if its children should be processed, false
	 *         otherwise.
	 */
	protected abstract boolean removedValid(IConfigurationElement element);

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
		}
	}

	/**
	 * Delegates the logging of a missing attribute to {@link #log(IConfigurationElement, String)} with a
	 * proper message.
	 * 
	 * @param element
	 *            the element to which an attribute is missing.
	 * @param attributeName
	 *            the name of the missing attribute.
	 */
	protected void logMissingAttribute(IConfigurationElement element, String attributeName) {
		log(IStatus.ERROR, element, "The required attribute '" + attributeName + "' not defined"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Log the error to the current plugin logger.
	 * 
	 * @param severity
	 *            Severity of this message. One of <code>IStatus.OK</code>, <code>IStatus.ERROR</code>,
	 *            <code>IStatus.INFO</code>, <code>IStatus.WARNING</code>, or <code>IStatus.CANCEL</code>
	 * @param element
	 *            the element from which comes to the error.
	 * @param message
	 *            the message to be logged.
	 */
	protected void log(int severity, IConfigurationElement element, String message) {
		log.log(new Status(severity, element.getDeclaringExtension().getContributor().getName(), message));
	}

	/**
	 * Log the error to the current plugin logger.
	 * 
	 * @param element
	 *            the element from which comes to the error.
	 * @param t
	 *            the exception to be logged.
	 */
	protected void log(IConfigurationElement element, Throwable t) {
		log.log(new Status(IStatus.ERROR, element.getDeclaringExtension().getContributor().getName(), t
				.getMessage(), t));
	}

	/**
	 * Logs the given error with a human-readable error message.
	 * 
	 * @param element
	 *            The element from which originates the error.
	 * @param errorMessage
	 *            Human-readable cause of this exception.
	 * @param cause
	 *            Actual exception that is to be logged.
	 * @since 2.2
	 */
	protected void log(IConfigurationElement element, String errorMessage, Throwable cause) {
		log.log(new Status(IStatus.ERROR, element.getDeclaringExtension().getContributor().getName(),
				errorMessage, cause));
	}

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
}

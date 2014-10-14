/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.view.registry;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;

/**
 * This listener will react to changes against our logical model editor handler extension point, allowing us
 * to be in sync with plugin activation and deactivation.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class LogicalModelViewHandlerRegistryListener extends AbstractRegistryEventListener {
	/** The "handler" tag of our extension point. */
	private static final String TAG_HANDLER = "handler"; //$NON-NLS-1$

	/** The "class" attribute of our handler tag. */
	private static final String ATTRIBUTE_CLASS = "class"; //$NON-NLS-1$

	/** The "ranking" attribute of our handler tag. */
	private static final String ATTRIBUTE_RANKING = "ranking"; //$NON-NLS-1$

	/** The actual registry this listener will alter. */
	private final LogicalModelViewHandlerRegistry registry;

	/**
	 * Initialize a registry event listener for our handlers.
	 * 
	 * @param pluginID
	 *            ID of the plugin contributing the extension point to monitor.
	 * @param extensionPointID
	 *            Actual id of the extension point to monitor.
	 * @param log
	 *            Log in which errors/warning should be logged.
	 * @param registry
	 *            The actual store of handlers this registry will alter.
	 */
	public LogicalModelViewHandlerRegistryListener(String pluginID, String extensionPointID, ILog log,
			LogicalModelViewHandlerRegistry registry) {
		super(pluginID, extensionPointID, log);
		this.registry = registry;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener#addedValid(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean addedValid(IConfigurationElement element) {
		if (element.getName().equals(TAG_HANDLER)) {
			final String className = element.getAttribute(ATTRIBUTE_CLASS);

			final String rankingStr = element.getAttribute(ATTRIBUTE_RANKING);
			int ranking = -1;
			try {
				ranking = Integer.parseInt(rankingStr);
			} catch (NumberFormatException e) {
				log(IStatus.ERROR, element, EMFCompareIDEUIMessages.getString(
						"ModelhandlerRegistry.invalidRanking", className, rankingStr)); //$NON-NLS-1$
			}

			final LogicalModelViewHandlerDescriptor descriptor = new LogicalModelViewHandlerDescriptor(
					element, ATTRIBUTE_CLASS, ranking);
			registry.addHandler(className, descriptor);
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener#removedValid(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean removedValid(IConfigurationElement element) {
		final String className = element.getAttribute(ATTRIBUTE_CLASS);
		registry.removeHandler(className);
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener#validateExtensionElement(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean validateExtensionElement(IConfigurationElement element) {
		// Don't work twice as much, validate as we add.
		// Removing cannot fail.
		return true;
	}
}

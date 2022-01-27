/*******************************************************************************
 * Copyright (c) 2022 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.subscriber;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.subscriber.SubscriberProviderRegistry;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;

/**
 * This listener will react to changes against the subscriber provider extension point, allowing us to be in
 * sync with plugin activation and deactivation.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
public class SubscriberProviderRegistryListener extends AbstractRegistryEventListener {

	/**
	 * The name of the provider element.
	 */
	private static final String PROVIDER_ELEMENT_NAME = "provider"; //$NON-NLS-1$

	/**
	 * The name of the class attribute of the provider element.
	 */
	private static final String ATTRIBUTE_CLASS = "class"; //$NON-NLS-1$

	/**
	 * The name of the class attribute of the provider element.
	 */
	private static final String ATTRIBUTE_RANKING = "ranking"; //$NON-NLS-1$

	/**
	 * The registry which will actually hold all information.
	 */
	private final SubscriberProviderRegistry registry;

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
	public SubscriberProviderRegistryListener(String pluginID, String extensionPointID, ILog log,
			SubscriberProviderRegistry registry) {
		super(pluginID, extensionPointID, log);
		this.registry = registry;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean validateExtensionElement(IConfigurationElement element) {
		if (PROVIDER_ELEMENT_NAME.equals(element.getName())) {
			final String className = element.getAttribute(ATTRIBUTE_CLASS);
			return className != null && className.trim().length() > 0;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean addedValid(IConfigurationElement element) {
		final String className = element.getAttribute(ATTRIBUTE_CLASS);
		final String rankingStr = element.getAttribute(ATTRIBUTE_RANKING);
		int ranking = -1;
		try {
			ranking = Integer.parseInt(rankingStr);
		} catch (NumberFormatException e) {
			log(IStatus.ERROR, element, EMFCompareIDEUIMessages
					.getString("ModelResolverRegistry.invalidRanking", className, rankingStr)); //$NON-NLS-1$
		}

		final SubscriberProviderDescriptor descriptor = new SubscriberProviderDescriptor(ATTRIBUTE_CLASS,
				element, ranking);
		registry.addProvider(className, descriptor);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean removedValid(IConfigurationElement element) {
		final String className = element.getAttribute(ATTRIBUTE_CLASS);
		registry.removeProvider(className);
		return true;
	}

}

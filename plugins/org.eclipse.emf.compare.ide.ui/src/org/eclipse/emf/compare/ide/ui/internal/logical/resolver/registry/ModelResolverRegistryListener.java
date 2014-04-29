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
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver.registry;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;

/**
 * This listener will react to changes against our model resolver extension point, allowing us to be in sync
 * with plugin activation and deactivation.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ModelResolverRegistryListener extends AbstractRegistryEventListener {
	/** The "resolver" tag of our extension point. */
	private static final String TAG_RESOLVER = "resolver"; //$NON-NLS-1$

	/** The "class" attribute of our resolver tag. */
	private static final String ATTRIBUTE_CLASS = "class"; //$NON-NLS-1$

	/** Optional label attribute of our resolver tag. */
	private static final String ATTRIBUTE_LABEL = "label"; //$NON-NLS-1$

	/** Optional label attribute of our resolver tag. */
	private static final String ATTRIBUTE_DESCRIPTION = "description"; //$NON-NLS-1$

	/** The "ranking" attribute of our resolver tag. */
	private static final String ATTRIBUTE_RANKING = "ranking"; //$NON-NLS-1$

	/** The actual registry this listener will alter. */
	private final ModelResolverRegistry registry;

	/**
	 * Initialize a registry event listener for our model resolvers.
	 * 
	 * @param pluginID
	 *            ID of the plugin contributing the extension point to monitor.
	 * @param extensionPointID
	 *            Actual id of the extension point to monitor.
	 * @param log
	 *            Log in which errors/warning should be logged.
	 * @param registry
	 *            The actual store of model resolvers this registry will alter.
	 */
	public ModelResolverRegistryListener(String pluginID, String extensionPointID, ILog log,
			ModelResolverRegistry registry) {
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
		if (element.getName().equals(TAG_RESOLVER)) {
			final String className = element.getAttribute(ATTRIBUTE_CLASS);

			final String rankingStr = element.getAttribute(ATTRIBUTE_RANKING);
			int ranking = -1;
			try {
				ranking = Integer.parseInt(rankingStr);
			} catch (NumberFormatException e) {
				log(IStatus.ERROR, element, EMFCompareIDEUIMessages.getString(
						"ModelResolverRegistry.invalidRanking", className, rankingStr)); //$NON-NLS-1$
			}

			final String label = element.getAttribute(ATTRIBUTE_LABEL);
			final String description = element.getAttribute(ATTRIBUTE_DESCRIPTION);

			final ModelResolverDescriptor descriptor = new ModelResolverDescriptor(element, ATTRIBUTE_CLASS,
					ranking, label, description);
			registry.addResolver(className, descriptor);
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
		registry.removeResolver(className);
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

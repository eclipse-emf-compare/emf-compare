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
package org.eclipse.emf.compare.ide.ui.internal.logical;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
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

	/** The "ranking" attribute of our resolver tag. */
	private static final String ATTRIBUTE_RANKING = "ranking"; //$NON-NLS-1$

	/** The actual registry this listener will alter. */
	private final IModelResolverRegistry registry;

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
			IModelResolverRegistry registry) {
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
			IModelResolver resolver;
			try {
				resolver = (IModelResolver)element.createExecutableExtension(ATTRIBUTE_CLASS);
			} catch (CoreException e) {
				final String message = EMFCompareIDEUIMessages.getString(
						"ModelResolverRegistry.invalidResolver", className); //$NON-NLS-1$
				log(element, message, e);
				return false;
			}

			assert resolver != null;
			final String rankingStr = element.getAttribute(ATTRIBUTE_RANKING);
			int ranking = -1;
			try {
				ranking = Integer.parseInt(rankingStr);
			} catch (NumberFormatException e) {
				log(IStatus.ERROR, element, EMFCompareIDEUIMessages.getString(
						"ModelResolverRegistry.invalidRanking", className, rankingStr)); //$NON-NLS-1$
			}
			resolver.setRanking(ranking);

			resolver.initialize();

			registry.addResolver(className, resolver);
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
		IModelResolver resolver = registry.removeResolver(className);
		if (resolver != null) {
			resolver.dispose();
		}
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

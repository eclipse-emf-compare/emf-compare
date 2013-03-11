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
package org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory.impl;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory.IAccessorFactory;

public class AccessorFactoryExtensionRegistryListener extends AbstractRegistryEventListener {

	static final String TAG_FACTORY = "factory"; //$NON-NLS-1$

	static final String ATT_CLASS = "class"; //$NON-NLS-1$

	static final String ATT_RANKING = "ranking"; //$NON-NLS-1$

	private final IAccessorFactory.Registry registry;

	/**
	 * @param pluginID
	 * @param extensionPointID
	 * @param registry
	 */
	public AccessorFactoryExtensionRegistryListener(String pluginID, String extensionPointID, ILog log,
			IAccessorFactory.Registry registry) {
		super(pluginID, extensionPointID, log);
		this.registry = registry;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener#validateExtensionElement(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean validateExtensionElement(IConfigurationElement element) {
		final boolean valid;
		if (element.getName().equals(TAG_FACTORY)) {
			if (element.getAttribute(ATT_CLASS) == null) {
				logMissingAttribute(element, ATT_CLASS);
				valid = false;
			} else if (element.getAttribute(ATT_RANKING) == null) {
				String rankingStr = element.getAttribute(ATT_RANKING);
				try {
					Integer.parseInt(rankingStr);
				} catch (NumberFormatException nfe) {
					log(IStatus.ERROR, element, "Attribute '" + ATT_RANKING
							+ "' is malformed, should be an integer.");
				}
				logMissingAttribute(element, ATT_RANKING);
				valid = false;
			} else {
				valid = true;
			}
		} else {
			valid = false;
		}
		return valid;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener#addedValid(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean addedValid(IConfigurationElement element) {
		try {
			IAccessorFactory factory = (IAccessorFactory)element.createExecutableExtension(ATT_CLASS);
			factory.setRanking(Integer.parseInt(element.getAttribute(ATT_RANKING)));
			IAccessorFactory previous = registry.add(factory);
			if (previous != null) {
				log(IStatus.WARNING, element, "The accessor factory '" + factory.getClass().getName()
						+ "' is registered twice.");
			}
		} catch (CoreException e) {
			log(element, e);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener#removedValid(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean removedValid(IConfigurationElement element) {
		registry.remove(element.getAttribute(ATT_CLASS));
		return true;
	}
}

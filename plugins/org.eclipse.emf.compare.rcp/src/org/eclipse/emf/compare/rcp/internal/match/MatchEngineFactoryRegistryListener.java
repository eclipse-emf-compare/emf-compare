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
package org.eclipse.emf.compare.rcp.internal.match;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;
import org.eclipse.emf.compare.rcp.internal.EMFCompareRCPMessages;

/**
 * Listener for contributions to the match engine extension.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class MatchEngineFactoryRegistryListener extends AbstractRegistryEventListener {

	/** TAG_ENGINE. */
	private static final String TAG_ENGINE_FACTORY = "engineFactory"; //$NON-NLS-1$

	/** ATT_CLASS. */
	private static final String ATT_CLASS = "class"; //$NON-NLS-1$

	/** ATT_RANKING. */
	private static final String ATT_RANKING = "ranking"; //$NON-NLS-1$

	/** The match engine factory registry to which extension will be registered. */
	private final IMatchEngine.Factory.Registry matchEngineFactoryRegistry;

	/**
	 * Creates a new registry listener with the given match engine factory registry to which extension will be
	 * registered.
	 * 
	 * @param registry
	 *            the match engine factory registry to which extension will be registered.
	 * @param pluginID
	 *            The pluginID of the extension point to be monitored.
	 * @param extensionPointID
	 *            The extension point ID to be monitored.
	 * @param log
	 *            The log object to be used to log error and/or warning.
	 */
	public MatchEngineFactoryRegistryListener(String pluginID, String extensionPointID, ILog log,
			IMatchEngine.Factory.Registry registry) {
		super(pluginID, extensionPointID, log);
		this.matchEngineFactoryRegistry = registry;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener#validateExtensionElement(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean validateExtensionElement(IConfigurationElement element) {
		final boolean ret;
		if (TAG_ENGINE_FACTORY.equals(element.getName())) {
			if (element.getAttribute(ATT_CLASS) == null) {
				logMissingAttribute(element, ATT_CLASS);
				ret = false;
			} else if (element.getAttribute(ATT_RANKING) == null) {
				logMissingAttribute(element, ATT_RANKING);
				ret = false;
			} else if (element.getAttribute(ATT_RANKING) != null) {
				String rankingStr = element.getAttribute(ATT_RANKING);
				try {
					Integer.parseInt(rankingStr);
				} catch (NumberFormatException nfe) {
					log(IStatus.ERROR, element, EMFCompareRCPMessages.getString(
							"malformed.extension.attribute", //$NON-NLS-1$
							ATT_RANKING));
					return false;
				}
				ret = true;
			} else {
				ret = true;
			}
		} else {
			ret = false;
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener#addedValid(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean addedValid(IConfigurationElement element) {
		try {
			IMatchEngine.Factory matchEngineFactory = (IMatchEngine.Factory)element
					.createExecutableExtension(ATT_CLASS);
			matchEngineFactory.setRanking(Integer.parseInt(element.getAttribute(ATT_RANKING)));
			IMatchEngine.Factory previous = matchEngineFactoryRegistry.add(matchEngineFactory);
			if (previous != null) {
				log(IStatus.WARNING, element, EMFCompareRCPMessages.getString(
						"duplicate.extension", matchEngineFactoryRegistry.getClass().getName())); //$NON-NLS-1$
			}
		} catch (CoreException e) {
			log(IStatus.ERROR, element, e.getMessage());
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
		matchEngineFactoryRegistry.remove(element.getAttribute(ATT_CLASS));
		return true;
	}
}

/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.internal.match;

import java.util.regex.Pattern;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.compare.match.eobject.EqualityHelperExtensionProvider;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;
import org.eclipse.emf.compare.rcp.internal.EMFCompareRCPMessages;

/**
 * Listener for contributions to the match engine extension.
 * 
 * @author <a href="mailto:stephane.thibaudeau@obeo.fr">Stephane Thibaudeau</a>
 */
public class EqualityHelperExtensionProviderDescriptorRegistryListener extends AbstractRegistryEventListener {

	/** Attribute name for the equality helper extension provider implementation. */
	public static final String ATT_CLASS = "class"; //$NON-NLS-1$

	/** TAG_EQUALITY_HELPER_EXTENSION_PROVIDER. */
	private static final String TAG_EQUALITY_HELPER_EXTENSION_PROVIDER = "provider"; //$NON-NLS-1$

	/** ATT_RANKING. */
	private static final String ATT_RANKING = "ranking"; //$NON-NLS-1$

	/** ATT_NS_URI. */
	private static final String ATT_NS_URI = "nsURI"; //$NON-NLS-1$

	/** The equality helper extension provider registry to which extension will be registered. */
	private final EqualityHelperExtensionProvider.Descriptor.Registry equalityHelperExtensionProviderRegistry;

	/**
	 * Creates a new registry listener with the given equality helper extension provider registry to which
	 * extension will be registered.
	 * 
	 * @param pluginID
	 *            The pluginID of the extension point to be monitored.
	 * @param extensionPointID
	 *            The extension point ID to be monitored.
	 * @param log
	 *            The log object to be used to log error and/or warning.
	 * @param registry
	 *            the equality helper extension provider registry to which extension will be registered.
	 */
	public EqualityHelperExtensionProviderDescriptorRegistryListener(String pluginID, String extensionPointID,
			ILog log, EqualityHelperExtensionProvider.Descriptor.Registry registry) {
		super(pluginID, extensionPointID, log);
		this.equalityHelperExtensionProviderRegistry = registry;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean validateExtensionElement(IConfigurationElement element) {
		final boolean ret;
		if (TAG_EQUALITY_HELPER_EXTENSION_PROVIDER.equals(element.getName())) {
			if (element.getAttribute(ATT_CLASS) == null) {
				logMissingAttribute(element, ATT_CLASS);
				ret = false;
			} else if (element.getAttribute(ATT_RANKING) == null) {
				logMissingAttribute(element, ATT_RANKING);
				ret = false;
			} else if (element.getAttribute(ATT_NS_URI) == null) {
				logMissingAttribute(element, ATT_NS_URI);
				ret = false;
			} else if (element.getAttribute(ATT_RANKING) != null) {
				String rankingStr = element.getAttribute(ATT_RANKING);
				try {
					Integer.parseInt(rankingStr);
				} catch (NumberFormatException nfe) {
					log(IStatus.ERROR, element,
							EMFCompareRCPMessages.getString("malformed.extension.attribute", //$NON-NLS-1$
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
	 */
	@Override
	protected boolean addedValid(IConfigurationElement element) {
		String className = element.getAttribute(ATT_CLASS);
		String nsURI = element.getAttribute(ATT_NS_URI);
		int rank = Integer.parseInt(element.getAttribute(ATT_RANKING));
		EqualityHelperExtensionProvider.Descriptor descriptor = new EqualityHelperExtensionProviderDescriptorRCPImpl(
				element, rank, Pattern.compile(nsURI));
		EqualityHelperExtensionProvider.Descriptor previous = equalityHelperExtensionProviderRegistry
				.put(className, descriptor);
		if (previous != null) {
			log(IStatus.WARNING, element, EMFCompareRCPMessages.getString("duplicate.extension", //$NON-NLS-1$
					equalityHelperExtensionProviderRegistry.getClass().getName()));
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean removedValid(IConfigurationElement element) {
		equalityHelperExtensionProviderRegistry.remove(element.getAttribute(ATT_CLASS));
		return true;
	}
}

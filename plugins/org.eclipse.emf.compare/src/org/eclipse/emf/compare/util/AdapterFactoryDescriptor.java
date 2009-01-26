/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.EMFComparePlugin;

/**
 * Describes an {@link AdapterFactory}, keeping track of its <code>nsURI</code>, <code>className</code> and
 * the {@link IConfigurationElement} it's been created from.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
/* package */class AdapterFactoryDescriptor {
	/** Keeps a reference to the configuration element that describes the {@link AdapterFactory factory}. */
	private final IConfigurationElement element;

	/** This descriptor's wrapped factory. */
	private AdapterFactory factory;

	/** Name space URI of the wrapped {@link AdapterFactory}. */
	private final String nsURI;

	/**
	 * Constructs a new descriptor from an IConfigurationElement.
	 * 
	 * @param configElements
	 *            Configuration of the {@link AdapterFactory}.
	 */
	public AdapterFactoryDescriptor(IConfigurationElement configElements) {
		element = configElements;
		this.nsURI = element.getAttribute("uri"); //$NON-NLS-1$
	}

	/**
	 * Returns the enclosed {@link AdapterFactory}'s instance.
	 * 
	 * @return The corresponding {@link AdapterFactory} instance.
	 */
	public AdapterFactory getAdapterInstance() {
		if (factory == null) {
			try {
				factory = (AdapterFactory)element.createExecutableExtension("class"); //$NON-NLS-1$
			} catch (final CoreException e) {
				EMFComparePlugin.log(new Status(IStatus.ERROR, EMFComparePlugin.PLUGIN_ID, e.getMessage()));
			}
		}
		return factory;
	}

	/**
	 * Returns the {@link AdapterFactory} <code>nsURI</code>.
	 * 
	 * @return The {@link AdapterFactory} <code>nsURI</code>.
	 */
	public String getNsURI() {
		return nsURI;
	}
}

/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.internal.adapterfactory;

import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactoryDescriptor;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;

/**
 * This listener will allow us to be aware of contribution changes against the emf compare adapter factory
 * extension point.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class AdapterFactoryDescriptorRegistryListener extends AbstractRegistryEventListener {

	/** TAG_FACTORY. */
	static final String TAG_FACTORY = "factory"; //$NON-NLS-1$

	/** ATT_CLASS. */
	static final String ATT_CLASS = "class"; //$NON-NLS-1$

	/** ATT_URI. */
	static final String ATT_URI = "uri"; //$NON-NLS-1$

	/** ATT_SUPPORTED_TYPES. */
	static final String ATT_SUPPORTED_TYPES = "supportedTypes"; //$NON-NLS-1$

	/** ATT_RANKING. */
	static final String ATT_RANKING = "ranking"; //$NON-NLS-1$

	/** The adapter factory descriptor registry against which extension will be registered. */
	private final Multimap<Collection<?>, RankedAdapterFactoryDescriptor> adapterFactoryRegistry;

	/**
	 * Creates a new registry event listener.
	 * 
	 * @param namespace
	 *            The namespace of the extension point to be monitored.
	 * @param extensionPointID
	 *            The extension point ID to be monitored
	 * @param log
	 *            The log object to be used to log error and/or warning.
	 * @param adapterFactoryRegistryBackingMultimap
	 *            Multimap holding the registered extensions.
	 */
	public AdapterFactoryDescriptorRegistryListener(String namespace, String extensionPointID, ILog log,
			Multimap<Collection<?>, RankedAdapterFactoryDescriptor> adapterFactoryRegistryBackingMultimap) {
		super(namespace, extensionPointID, log);
		this.adapterFactoryRegistry = adapterFactoryRegistryBackingMultimap;
	}

	/**
	 * Validates if the given element is an element for the given extension and is well constructed. Returns
	 * true if the element should be further parsed for addition or removal.
	 * 
	 * @param element
	 *            the element to validate.
	 * @return true if the element should be further parsed for addition or removal, else otherwise.
	 */
	@Override
	protected boolean validateExtensionElement(IConfigurationElement element) {
		final boolean valid;
		if (element.getName().equals(TAG_FACTORY)) {
			if (element.getAttribute(ATT_CLASS) == null) {
				logMissingAttribute(element, ATT_CLASS);
				valid = false;
			} else if (element.getAttribute(ATT_RANKING) == null) {
				logMissingAttribute(element, ATT_RANKING);
				valid = false;
			} else if (element.getAttribute(ATT_RANKING) != null) {
				String ordinalStr = element.getAttribute(ATT_RANKING);
				try {
					Integer.parseInt(ordinalStr);
				} catch (NumberFormatException nfe) {
					log(IStatus.ERROR, element, "Attribute ranking is malformed, should be an integer."); //$NON-NLS-1$
					return false;
				}
				valid = true;
			} else if (element.getAttribute(ATT_URI) == null) {
				logMissingAttribute(element, ATT_URI);
				valid = false;
			} else if (element.getAttribute(ATT_SUPPORTED_TYPES) == null) {
				logMissingAttribute(element, ATT_SUPPORTED_TYPES);
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
	 * Process the given element as the addition of a valid element extension.
	 * 
	 * @param element
	 *            the element to be added.
	 * @return true if the given element has been added and if its children should be processed, false
	 *         otherwise.
	 */
	@Override
	protected boolean addedValid(IConfigurationElement element) {
		int ranking = Integer.parseInt(element.getAttribute(ATT_RANKING));
		RankedAdapterFactoryDescriptor descriptor = new RankedAdapterFactoryDescriptorImpl(element, ranking);
		String supportedTypes = element.getAttribute(ATT_SUPPORTED_TYPES);

		for (StringTokenizer stringTokenizer = new StringTokenizer(supportedTypes); stringTokenizer
				.hasMoreTokens();) {
			String supportedType = stringTokenizer.nextToken();
			List<Object> key = new ArrayList<Object>();
			key.add(element.getAttribute(ATT_URI));
			key.add(supportedType);
			adapterFactoryRegistry.put(key, descriptor);
		}

		return true;
	}

	/**
	 * Process the given element as the removal of a valid element extension.
	 * 
	 * @param element
	 *            the element to be removed.
	 * @return true if the given element has been removed and if its children should be processed, false
	 *         otherwise.
	 */
	@Override
	protected boolean removedValid(IConfigurationElement element) {
		String supportedTypes = element.getAttribute(ATT_SUPPORTED_TYPES);

		for (StringTokenizer stringTokenizer = new StringTokenizer(supportedTypes); stringTokenizer
				.hasMoreTokens();) {
			String supportedType = stringTokenizer.nextToken();
			List<Object> key = new ArrayList<Object>();
			key.add(element.getAttribute(ATT_URI));
			key.add(supportedType);
			Iterator<RankedAdapterFactoryDescriptor> composedAdapterFactoryIterator = adapterFactoryRegistry
					.get(key).iterator();
			while (composedAdapterFactoryIterator.hasNext()) {
				RankedAdapterFactoryDescriptor next = composedAdapterFactoryIterator.next();
				if (next.getId() != null && next.getId().equals(element.getAttribute(ATT_CLASS))) {
					composedAdapterFactoryIterator.remove();
				}
			}
		}

		return true;
	}
}

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
package org.eclipse.emf.compare.rcp.internal.extension.impl;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;
import org.eclipse.emf.compare.rcp.internal.EMFCompareRCPMessages;
import org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor;
import org.eclipse.emf.compare.rcp.internal.extension.IItemRegistry;

/**
 * Base class for Descriptor Registry.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 * @param <T>
 *            one descriptor type
 */
public class DescriptorRegistryEventListener<T> extends AbstractRegistryEventListener {

	/** Label attribute of extension point. */
	public static final String LABEL_DESCRIPTOR_ATTR = "label"; //$NON-NLS-1$

	/** Description attribute of extension point. */
	public static final String DESCRITPION_DESCRIPTOR_ATTR = "description"; //$NON-NLS-1$

	/** Engine implementation class attribute of extension point. */
	public static final String IMPL_CLASS_DESCRIPTPOR_ATTR = "impl"; //$NON-NLS-1$

	/** Rank attribute of extension point. */
	public static final String RANK_DESCRIPTOR_ATTR = "ranking"; //$NON-NLS-1$

	/** Id attribute of the extension point. */
	public static final String ID_DESCRIPTOR_ATTR = "id"; //$NON-NLS-1$

	/** Descriptor tag of extension point. */
	public static final String TAG_DESCRIPTOR = "descriptor"; //$NON-NLS-1$

	/** EMPTY_STRING. */
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/** Registry of items. */
	private final IItemRegistry<T> registry;

	/**
	 * Constructor.
	 * 
	 * @param namespace
	 *            namespace of the extension point
	 * @param extensionPointID
	 *            id of the extension point
	 * @param log
	 *            Logger
	 * @param registry
	 *            {@link IItemRegistry} where
	 *            {@link org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor} will be stored
	 */
	public DescriptorRegistryEventListener(String namespace, String extensionPointID, ILog log,
			IItemRegistry<T> registry) {
		super(namespace, extensionPointID, log);
		this.registry = registry;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener#validateExtensionElement(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean validateExtensionElement(IConfigurationElement element) {
		final boolean ret;
		if (TAG_DESCRIPTOR.equals(element.getName())) {
			if (element.getAttribute(IMPL_CLASS_DESCRIPTPOR_ATTR) == null) {
				logMissingAttribute(element, IMPL_CLASS_DESCRIPTPOR_ATTR);
				ret = false;
			} else if (element.getAttribute(DescriptorRegistryEventListener.LABEL_DESCRIPTOR_ATTR) == null) {
				logMissingAttribute(element, DescriptorRegistryEventListener.LABEL_DESCRIPTOR_ATTR);
				ret = false;
			} else if (element.getAttribute(DESCRITPION_DESCRIPTOR_ATTR) == null) {
				logMissingAttribute(element, DESCRITPION_DESCRIPTOR_ATTR);
				ret = false;
			} else if (element.getAttribute(RANK_DESCRIPTOR_ATTR) == null) {
				logMissingAttribute(element, RANK_DESCRIPTOR_ATTR);
				ret = false;
			} else if (element.getAttribute(ID_DESCRIPTOR_ATTR) == null) {
				logMissingAttribute(element, ID_DESCRIPTOR_ATTR);
				ret = false;
			} else if (EMPTY_STRING.equals(element.getAttribute(ID_DESCRIPTOR_ATTR))) {
				logMissingAttribute(element, ID_DESCRIPTOR_ATTR);
				ret = false;
			} else if (element.getAttribute(RANK_DESCRIPTOR_ATTR) != null) {
				String rankingStr = element.getAttribute(RANK_DESCRIPTOR_ATTR);
				try {
					Integer.parseInt(rankingStr);
				} catch (NumberFormatException nfe) {
					log(IStatus.ERROR, element, EMFCompareRCPMessages.getString(
							"malformed.extension.attribute", //$NON-NLS-1$
							RANK_DESCRIPTOR_ATTR));
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
		int rank = Integer.parseInt(element.getAttribute(RANK_DESCRIPTOR_ATTR));
		String label = element.getAttribute(DescriptorRegistryEventListener.LABEL_DESCRIPTOR_ATTR);
		String description = element.getAttribute(DESCRITPION_DESCRIPTOR_ATTR);
		String id = element.getAttribute(ID_DESCRIPTOR_ATTR);
		ItemDescriptor<T> descriptor = new ItemDescriptor<T>(label, description, rank, element, id);
		IItemDescriptor<T> previous = registry.add(descriptor);
		if (previous != null) {
			log(IStatus.WARNING, element, EMFCompareRCPMessages.getString(
					"duplicate.extension", registry.getClass().getName())); //$NON-NLS-1$
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
		return registry.remove(element.getAttribute(ID_DESCRIPTOR_ATTR)) != null;
	}

}

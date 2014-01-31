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
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.compare.rcp.extension.PluginClassDescriptor;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;

/**
 * Default implementation of {@link IDifferenceGroupProvider.Descriptor}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 4.0
 */
public class DifferenceGroupProviderDescriptorImpl extends PluginClassDescriptor<IDifferenceGroupProvider> implements IDifferenceGroupProvider.Descriptor {

	/** The label of the group provider. */
	private String label;

	/** The state of the default selection of the group provider. */
	private boolean defaultSelected;

	/**
	 * Creates a descriptor corresponding to the information of the given <em>element</em>.
	 * 
	 * @param element
	 *            Configuration element from which to create this descriptor.
	 * @param label
	 *            The label of the group provider.
	 * @param defaultSelected
	 *            The state of the default selection of the group provider.
	 */
	public DifferenceGroupProviderDescriptorImpl(IConfigurationElement element, String label,
			boolean defaultSelected) {
		super(element, DifferenceGroupProviderExtensionRegistryListener.ATT_CLASS);
		this.label = label;
		this.defaultSelected = defaultSelected;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IDifferenceGroupProvider.Descriptor#createGroupProvider()
	 */
	public IDifferenceGroupProvider createGroupProvider() {
		IDifferenceGroupProvider differenceGroupProvider = this.createInstance();
		differenceGroupProvider.setLabel(label);
		differenceGroupProvider.setDefaultSelected(defaultSelected);
		return differenceGroupProvider;
	}

}

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
package org.eclipse.emf.compare.rcp.internal.adapterfactory;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactoryDescriptor;
import org.eclipse.emf.compare.rcp.extension.PluginClassDescriptor;

/**
 * Default implementation of {@link RankedAdapterFactoryDescriptor}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class RankedAdapterFactoryDescriptorImpl extends PluginClassDescriptor<AdapterFactory> implements RankedAdapterFactoryDescriptor {

	/** The wrapped adapter factory. */
	private AdapterFactory factory;

	/** Ranking of this adapter factory. */
	private final int ranking;

	/**
	 * Creates a descriptor corresponding to the information of the given <em>element</em>.
	 * 
	 * @param element
	 *            Configuration element from which to create this descriptor.
	 * @param ranking
	 *            The ranking of the adapter factory.
	 */
	public RankedAdapterFactoryDescriptorImpl(IConfigurationElement element, int ranking) {
		super(element, AdapterFactoryDescriptorRegistryListener.ATT_CLASS);
		this.ranking = ranking;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactoryDescriptor#createAdapterFactory()
	 */
	public AdapterFactory createAdapterFactory() {
		if (factory == null) {
			factory = this.createInstance();
		}
		return factory;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactoryDescriptor#getRanking()
	 */
	public int getRanking() {
		return ranking;
	}

}

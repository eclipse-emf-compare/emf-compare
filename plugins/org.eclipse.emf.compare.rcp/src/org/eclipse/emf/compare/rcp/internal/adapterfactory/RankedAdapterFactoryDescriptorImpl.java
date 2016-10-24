/*******************************************************************************
 * Copyright (c) 2013, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Martin Fleck - bug 483798
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.internal.adapterfactory;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.adapterfactory.context.IContextTester;
import org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactoryDescriptor;
import org.eclipse.emf.compare.rcp.extension.PluginClassDescriptor;

/**
 * Default implementation of {@link RankedAdapterFactoryDescriptor}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class RankedAdapterFactoryDescriptorImpl extends PluginClassDescriptor<AdapterFactory> implements RankedAdapterFactoryDescriptor {

	/** Optional attribute in extension point. */
	private static final String ATT_OPTIONAL = "optional"; //$NON-NLS-1$

	/** Label attribute in extension point. */
	private static final String ATT_LABEL = "label"; //$NON-NLS-1$

	/** Description attribute in extension point. */
	private static final String ATT_DESCRIPTION = "description"; //$NON-NLS-1$

	/** Ranking of this adapter factory. */
	private final int ranking;

	/** ContextTester of this adapter factory. */
	private final IContextTester contextTester;

	/** Holds the id of this descriptor. */
	private final String id;

	/**
	 * Creates a descriptor corresponding to the information of the given <em>element</em>.
	 * 
	 * @param element
	 *            Configuration element from which to create this descriptor.
	 * @param ranking
	 *            The ranking of the adapter factory.
	 */
	public RankedAdapterFactoryDescriptorImpl(IConfigurationElement element, int ranking) {
		this(element, ranking, null);
	}

	/**
	 * Creates a descriptor corresponding to the information of the given <em>element</em>.
	 * 
	 * @param element
	 *            Configuration element from which to create this descriptor.
	 * @param ranking
	 *            The ranking of the adapter factory.
	 * @param contextTester
	 *            The context tester of the adapter factory.
	 */
	public RankedAdapterFactoryDescriptorImpl(IConfigurationElement element, int ranking,
			IContextTester contextTester) {
		super(element, AdapterFactoryDescriptorRegistryListener.ATT_CLASS);
		this.ranking = ranking;
		this.contextTester = contextTester;
		this.id = element.getAttribute(AdapterFactoryDescriptorRegistryListener.ATT_CLASS);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactoryDescriptor#createAdapterFactory()
	 */
	public AdapterFactory createAdapterFactory() {
		return createInstance();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactoryDescriptor#getRanking()
	 */
	public int getRanking() {
		return ranking;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactoryDescriptor#getContextTester()
	 */
	public IContextTester getContextTester() {
		return contextTester;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see RankedAdapterFactoryDescriptor#getId()
	 */
	public String getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see {org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactoryDescriptor#getLabel()}
	 */
	public String getLabel() {
		String label = element.getAttribute(ATT_LABEL);
		if (label == null) {
			label = element.getAttribute(AdapterFactoryDescriptorRegistryListener.ATT_CLASS);
		}
		return label;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see {@link org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactoryDescriptor#getDescription()}
	 */
	public String getDescription() {
		return element.getAttribute(ATT_DESCRIPTION);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see {@link org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactoryDescriptor#isOptional()}
	 */
	public boolean isOptional() {
		return Boolean.parseBoolean(element.getAttribute(ATT_OPTIONAL));
	}

}

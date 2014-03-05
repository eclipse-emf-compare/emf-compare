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
package org.eclipse.emf.compare.rcp.internal.match;

import java.util.regex.Pattern;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.compare.match.eobject.WeightProvider;
import org.eclipse.emf.compare.rcp.extension.PluginClassDescriptor;

/**
 * Describes an extension as contributed to the "org.eclipse.emf.compare.rcp.weightProvider" extension point.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class WeightProviderDescriptorRCPImpl extends PluginClassDescriptor<WeightProvider> implements WeightProvider.Descriptor {

	/** The wrapped weight provider. */
	private WeightProvider instance;

	/** The ranking of the weight provider. */
	private int ranking;

	/** The pattern of namespace URI on which this weight provider can be applied. */
	private Pattern nsURI;

	/**
	 * Creates a descriptor corresponding to the information of the given <em>element</em>.
	 * 
	 * @param element
	 *            Configuration element from which to create this descriptor.
	 * @param ranking
	 *            the ranking of the weight provider.
	 * @param nsURI
	 *            The pattern of namespace URI on which this post processor can be applied.
	 */
	public WeightProviderDescriptorRCPImpl(IConfigurationElement element, int ranking, Pattern nsURI) {
		super(element, WeightProviderDescriptorRegistryListener.ATT_CLASS);
		this.ranking = ranking;
		this.nsURI = nsURI;
	}

	/**
	 * {@inheritDoc}
	 */
	public Pattern getNsURI() {
		return nsURI;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRanking() {
		return ranking;
	}

	/**
	 * {@inheritDoc}
	 */
	public WeightProvider getWeightProvider() {
		if (instance == null) {
			instance = createInstance();
		}
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getInstanceClassName() {
		return element.getAttribute(attributeName);
	}

}

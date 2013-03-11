/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.internal.postprocessor;

import java.util.regex.Pattern;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.rcp.extension.PluginClassDescriptor;

/**
 * Describes an extension as contributed to the "org.eclipse.emf.compare.rcp.postProcessor" extension point.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class PostProcessorDescriptor extends PluginClassDescriptor<IPostProcessor> implements IPostProcessor.Descriptor {

	private final Pattern nsURI;

	private final Pattern resourceURI;

	private IPostProcessor instance;

	/**
	 * Creates a descriptor corresponding to the information of the given <em>element</em>.
	 * 
	 * @param element
	 *            Configuration element from which to create this descriptor.
	 */
	public PostProcessorDescriptor(IConfigurationElement element, Pattern nsURI, Pattern resourceURI) {
		super(element, PostProcessorFactoryRegistryListener.ATT_CLASS);
		this.nsURI = nsURI;
		this.resourceURI = resourceURI;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#getNsURI()
	 */
	public Pattern getNsURI() {
		return nsURI;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#getResourceURI()
	 */
	public Pattern getResourceURI() {
		return resourceURI;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor#getPostProcessor()
	 */
	public IPostProcessor getPostProcessor() {
		if (instance == null) {
			instance = createInstance();
		}
		return instance;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor#getInstanceClassName()
	 */
	public String getInstanceClassName() {
		return element.getAttribute(attributeName);
	}

}

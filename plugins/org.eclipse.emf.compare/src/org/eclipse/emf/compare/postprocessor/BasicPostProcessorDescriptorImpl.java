/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.postprocessor;

import java.util.regex.Pattern;

import org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor;

/**
 * A simple implementation of {@link Descriptor} that will delegate its method implementation to values given
 * to its constructor.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class BasicPostProcessorDescriptorImpl implements Descriptor {

	/** The nsURI pattern on which the described post processor applied to. */
	private final Pattern nsURI;

	/** The resourceURI pattern on which the described post processor applied to. */
	private final Pattern resourceURI;

	/** The described post processor. */
	private final IPostProcessor postProcessor;

	/**
	 * Constructs a new descriptor with the given value.
	 * 
	 * @param postProcessor
	 *            the described post processor
	 * @param nsURI
	 *            The nsURI pattern on which the described post processor applied to. May be <code>null</code>
	 *            .
	 * @param resourceURI
	 *            The resourceURI pattern on which the described post processor applied to. May be
	 *            <code>null</code>
	 */
	public BasicPostProcessorDescriptorImpl(IPostProcessor postProcessor, Pattern nsURI, Pattern resourceURI) {
		this.postProcessor = postProcessor;
		this.nsURI = nsURI;
		this.resourceURI = resourceURI;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor#getNsURI()
	 */
	public Pattern getNsURI() {
		return nsURI;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor#getResourceURI()
	 */
	public Pattern getResourceURI() {
		return resourceURI;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor#getInstanceClassName()
	 */
	public String getInstanceClassName() {
		return getPostProcessor().getClass().getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor#getPostProcessor()
	 */
	public IPostProcessor getPostProcessor() {
		return postProcessor;
	}
}

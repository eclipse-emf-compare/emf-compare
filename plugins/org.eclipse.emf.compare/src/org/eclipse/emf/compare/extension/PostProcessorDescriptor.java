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
package org.eclipse.emf.compare.extension;

/**
 * Describes an extension to make post handling at each step of the comparison. This descriptor has to be
 * added in the EMFCompareExtensionRegistry in order that the contribution be effective.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class PostProcessorDescriptor {

	/** Qualified class name of the post processor. This will be used as an id to remove contributions. */
	protected String extensionClassName;

	/** Namespace URIs (regex) this processor applies to. */
	protected String nsUri;

	/** Resource URIs (regex) this processor applies to. */
	protected String resourceUri;

	/** We only need to create the instance once, this will keep reference to it. */
	protected IPostProcessor postProcessor;

	/**
	 * Constructor.
	 */
	public PostProcessorDescriptor() {
	}

	/**
	 * Constructor used in standalone applications. It enables to manually initialize the descriptor.
	 * 
	 * @param nsUri
	 *            The namespace URI the post processor applies to.
	 * @param resourceUri
	 *            The resource URI the post processor applies to.
	 * @param extensionClassName
	 *            The qualified name of the post processor.
	 * @param postProcessor
	 *            The post processor itself.
	 */
	public PostProcessorDescriptor(String nsUri, String resourceUri, String extensionClassName,
			IPostProcessor postProcessor) {
		this.nsUri = nsUri;
		this.resourceUri = resourceUri;
		this.extensionClassName = extensionClassName;
		this.postProcessor = postProcessor;
	}

	/**
	 * Returns this descriptor's post processor qualified name.
	 * 
	 * @return This descriptor's post processor qualified name.
	 */
	public String getExtensionClassName() {
		return extensionClassName;
	}

	/**
	 * Returns the post processor related to this descriptor.
	 * 
	 * @return The post processor.
	 */
	public IPostProcessor getPostProcessor() {
		return postProcessor;
	}

	/**
	 * Returns the namespace URI.
	 * 
	 * @return The namespace URI.
	 */
	public String getNsURI() {
		return nsUri;
	}

	/**
	 * Returns the resource URI.
	 * 
	 * @return The resource URI.
	 */
	public String getResourceURI() {
		return resourceUri;
	}

}

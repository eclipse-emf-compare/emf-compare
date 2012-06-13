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
package org.eclipse.emf.compare.ide.internal.extension;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.compare.logical.extension.IPostProcessor;
import org.eclipse.emf.compare.logical.extension.PostProcessorDescriptor;

/**
 * Describes an extension as contributed to the "org.eclipse.emf.compare.postProcessor" extension point.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class PostProcessorIDEDescriptor extends PostProcessorDescriptor {

	/** Name of the attribute holding the {@link IPostProcessor} qualified names. */
	public static final String POST_PROCESSOR_CLASS_ATTRIBUTE = "class"; //$NON-NLS-1$

	/** Name of the attribute holding the namespace URI(s) this processor applies to. */
	public static final String POST_PROCESSOR_NS_URI = "nsURI"; //$NON-NLS-1$

	/** Name of the attribute holding the resource URI(s) this processor applies to. */
	public static final String POST_PROCESSOR_RESOURCE_URI = "resourceURI"; //$NON-NLS-1$

	/** This attribute is common to the "enablement" tags : nsURI, resourceURI. */
	public static final String ENABLEMENT_TAG_VALUE = "value"; //$NON-NLS-1$

	/** All of our "enablement" tags accept comma-separated values. */
	public static final String VALUE_SEPARATOR = ","; //$NON-NLS-1$

	/** Configuration element of this descriptor. */
	private final IConfigurationElement element;

	/**
	 * Creates a descriptor corresponding to the information of the given <em>element</em>.
	 * 
	 * @param element
	 *            Configuration element from which to create this descriptor.
	 */
	public PostProcessorIDEDescriptor(IConfigurationElement element) {
		super();
		this.element = element;

		extensionClassName = element.getAttribute(POST_PROCESSOR_CLASS_ATTRIBUTE);

		IConfigurationElement[] nsURIConfig = element.getChildren(POST_PROCESSOR_NS_URI);
		if (nsURIConfig.length > 0) {
			nsUri = nsURIConfig[0].getAttribute(ENABLEMENT_TAG_VALUE);
		} else {
			nsUri = null;
		}

		IConfigurationElement[] resourceUriConfig = element.getChildren(POST_PROCESSOR_RESOURCE_URI);
		if (resourceUriConfig.length > 0) {
			resourceUri = resourceUriConfig[0].getAttribute(ENABLEMENT_TAG_VALUE);
		} else {
			resourceUri = null;
		}
	}

	/**
	 * Creates an instance of this descriptor's post processor if needed, then return it.
	 * 
	 * @return An instance of this descriptor's post processor.
	 */
	@Override
	public IPostProcessor getPostProcessor() {
		if (postProcessor == null) {
			try {
				postProcessor = (IPostProcessor)element
						.createExecutableExtension(POST_PROCESSOR_CLASS_ATTRIBUTE);
			} catch (CoreException e) {
				// FIXME log this!
			}
		}
		return postProcessor;
	}

}

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
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;

/**
 * This listener will allow us to be aware of contribution changes against the model resolver extension point.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class PostProcessorRegistryListener extends AbstractRegistryEventListener {

	static final String TAG_PROCESSOR = "processor"; //$NON-NLS-1$

	static final String TAG_NS_URI = "nsURI"; //$NON-NLS-1$

	static final String TAG_RESOURCE_URI = "resourceURI"; //$NON-NLS-1$

	static final String ATT_VALUE = "value"; //$NON-NLS-1$

	static final String ATT_CLASS = "class"; //$NON-NLS-1$

	/** The post processor registry to which extension will be registered. */
	private final IPostProcessor.Registry registry;

	/**
	 * Creates a new registry listener with the given post processor registry to which extension will be
	 * registered.
	 * 
	 * @param registry
	 *            the post processor registry to which extension will be registered.
	 * @param pluginID
	 * @param extensionPointID
	 * @param log
	 */
	public PostProcessorRegistryListener(String pluginID, String extensionPointID, ILog log,
			IPostProcessor.Registry registry) {
		super(pluginID, extensionPointID, log);
		this.registry = registry;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener#validateExtensionElement(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean validateExtensionElement(IConfigurationElement element) {
		final boolean valid;
		if (TAG_PROCESSOR.equals(element.getName())) {
			IConfigurationElement[] nsURIChildren = element.getChildren(TAG_NS_URI);
			IConfigurationElement[] resourceURIChildren = element.getChildren(TAG_RESOURCE_URI);
			if (element.getAttribute(ATT_CLASS) == null) {
				logMissingAttribute(element, ATT_CLASS);
				valid = false;
			} else if (nsURIChildren.length > 0) {
				if (nsURIChildren[0].getAttribute(ATT_VALUE) == null) {
					logMissingAttribute(nsURIChildren[0], ATT_VALUE);
					valid = false;
				} else {
					valid = true;
				}
			} else if (resourceURIChildren.length > 0) {
				if (resourceURIChildren[0].getAttribute(ATT_VALUE) == null) {
					logMissingAttribute(resourceURIChildren[0], ATT_VALUE);
					valid = false;
				} else {
					valid = true;
				}
			} else {
				log(IStatus.ERROR, element, "Post processor must have an nsURI or a resource URI");
				valid = false;
			}
		} else {
			valid = false;
		}

		return valid;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener#addedValid(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean addedValid(IConfigurationElement element) {
		IConfigurationElement[] nsURIChildren = element.getChildren(TAG_NS_URI);
		IConfigurationElement[] resourceURIChildren = element.getChildren(TAG_RESOURCE_URI);
		Pattern nsURI = null;
		if (nsURIChildren.length > 0) {
			nsURI = Pattern.compile(nsURIChildren[0].getAttribute(ATT_VALUE));
		}
		Pattern resourceURI = null;
		if (resourceURIChildren.length > 0) {
			resourceURI = Pattern.compile(resourceURIChildren[0].getAttribute(ATT_VALUE));
		}

		IPostProcessor processor = new PostProcessorDescriptor(element, nsURI, resourceURI);
		IPostProcessor previous = registry.addPostProcessor(processor);
		if (previous != null) {
			EMFCompareRCPPlugin.getDefault().log(IStatus.WARNING,
					"The post processor '" + element.getAttribute(ATT_CLASS) + "' is registered twice.");
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
		registry.removePostProcessor(element.getAttribute(ATT_CLASS));
		return true;
	}
}

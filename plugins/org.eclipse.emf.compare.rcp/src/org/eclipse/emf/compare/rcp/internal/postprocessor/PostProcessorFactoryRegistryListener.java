/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.internal.postprocessor;

import com.google.common.base.Preconditions;

import java.util.regex.Pattern;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;
import org.eclipse.emf.compare.rcp.internal.EMFCompareRCPMessages;
import org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor;
import org.eclipse.emf.compare.rcp.internal.extension.IItemRegistry;
import org.eclipse.emf.compare.rcp.internal.extension.impl.WrapperItemDescriptor;

/**
 * This listener will allow us to be aware of contribution changes against the model resolver extension point.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class PostProcessorFactoryRegistryListener extends AbstractRegistryEventListener {

	/** TAG_PROCESSOR. */
	static final String TAG_PROCESSOR = "processor"; //$NON-NLS-1$

	/** TAG_NS_URI. */
	static final String TAG_NS_URI = "nsURI"; //$NON-NLS-1$

	/** TAG_RESOURCE_URI. */
	static final String TAG_RESOURCE_URI = "resourceURI"; //$NON-NLS-1$

	/** ATT_VALUE. */
	static final String ATT_VALUE = "value"; //$NON-NLS-1$

	/** ATT_CLASS. */
	static final String ATT_CLASS = "class"; //$NON-NLS-1$

	/** ATT_ORDINAL. */
	static final String ATT_ORDINAL = "ordinal"; //$NON-NLS-1$

	/** ATT_LABEL. */
	static final String ATT_LABEL = "label"; //$NON-NLS-1$

	/** ATT_DESCRIPTION. */
	static final String ATT_DESCRIPTION = "description"; //$NON-NLS-1$

	/** The post processor registry to which extension will be registered. */
	private final IItemRegistry<IPostProcessor.Descriptor> registry;

	/**
	 * Creates a new registry listener with the given post processor registry to which extension will be
	 * registered.
	 * 
	 * @param pluginID
	 *            The pluginID of the extension point to be monitored.
	 * @param extensionPointID
	 *            The extension point ID to be monitored
	 * @param log
	 *            The log object to be used to log error and/or warning.
	 * @param registry
	 *            the post processor registry to which extension will be registered.
	 */
	public PostProcessorFactoryRegistryListener(String pluginID, String extensionPointID, ILog log,
			IItemRegistry<IPostProcessor.Descriptor> registry) {
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
			} else if (element.getAttribute(ATT_ORDINAL) == null) {
				logMissingAttribute(element, ATT_ORDINAL);
				valid = false;
			} else if (element.getAttribute(ATT_ORDINAL) != null) {
				String ordinalStr = element.getAttribute(ATT_ORDINAL);
				try {
					Integer.parseInt(ordinalStr);
				} catch (NumberFormatException nfe) {
					log(IStatus.ERROR, element,
							EMFCompareRCPMessages.getString("malformed.extension.attribute", //$NON-NLS-1$
									ATT_ORDINAL));
					return false;
				}
				valid = true;
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
				log(IStatus.ERROR, element, "Post processor must have an nsURI or a resource URI"); //$NON-NLS-1$
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
		String className = element.getAttribute(ATT_CLASS);

		IPostProcessor.Descriptor postProcessorDescriptor = new PostProcessorDescriptor(element, nsURI,
				resourceURI);
		postProcessorDescriptor.setOrdinal(Integer.parseInt(element.getAttribute(ATT_ORDINAL)));

		WrapperItemDescriptor<IPostProcessor.Descriptor> postProcessorItemDescriptor = new WrapperItemDescriptor<IPostProcessor.Descriptor>(
				element.getAttribute(ATT_LABEL), element.getAttribute(ATT_DESCRIPTION),
				postProcessorDescriptor.getOrdinal(), className, postProcessorDescriptor) {
			@Override
			public int compareTo(IItemDescriptor<Descriptor> o) {
				/*
				 * Inverse natural order for post processor since they are registered with ordinal attribute
				 * instead of rank.
				 */
				Preconditions.checkNotNull(o);
				int comp = getRank() - o.getRank();
				if (comp == 0) {
					comp = getID().compareTo(o.getID());
				}
				return comp;
			}
		};
		IItemDescriptor<Descriptor> previous = registry.add(postProcessorItemDescriptor);
		if (previous != null) {
			EMFCompareRCPPlugin.getDefault().log(IStatus.WARNING,
					"The post processor factory '" + className + "' is registered twice."); //$NON-NLS-1$//$NON-NLS-2$
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
		String className = element.getAttribute(ATT_CLASS);
		registry.remove(className);
		return true;
	}
}

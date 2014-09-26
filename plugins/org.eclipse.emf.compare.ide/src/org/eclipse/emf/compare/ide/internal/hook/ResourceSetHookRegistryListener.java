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
package org.eclipse.emf.compare.ide.internal.hook;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.emf.compare.ide.EMFCompareIDEPlugin;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;

/**
 * Listener that fill a {@link ResourceSetHookRegistry}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class ResourceSetHookRegistryListener extends AbstractRegistryEventListener {

	/**
	 * Extension point id.
	 */
	public static final String EXT_ID = "resourceSetHook"; //$NON-NLS-1$

	/**
	 * Attribute referencing the implementation class of the
	 * {@link org.eclipse.emf.compare.ide.hook.IResourceSetHook}.
	 */
	static final String CLASS_ATTR = "class"; //$NON-NLS-1$

	/**
	 * {@link ResourceSetHookRegistry}.
	 */
	private final ResourceSetHookRegistry registry;

	/**
	 * Constructor.
	 * 
	 * @param log
	 *            {@link ILog}.
	 * @param registry
	 *            Registry to fill.
	 */
	public ResourceSetHookRegistryListener(ILog log, ResourceSetHookRegistry registry) {
		super(EMFCompareIDEPlugin.PLUGIN_ID, ResourceSetHookRegistryListener.EXT_ID, log);
		this.registry = registry;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener#validateExtensionElement(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean validateExtensionElement(IConfigurationElement element) {
		if (element.getAttribute(CLASS_ATTR) == null) {
			logMissingAttribute(element, CLASS_ATTR);
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener#addedValid(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean addedValid(IConfigurationElement element) {
		registry.add(element.getAttribute(CLASS_ATTR), new ResourceSetHookDescriptor(element));
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener#removedValid(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean removedValid(IConfigurationElement element) {
		String key = element.getAttribute(CLASS_ATTR);
		if (key != null) {
			return registry.remove(key);
		}
		return false;
	}

}

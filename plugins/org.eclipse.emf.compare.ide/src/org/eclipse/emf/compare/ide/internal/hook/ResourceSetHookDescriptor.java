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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.ide.hook.IResourceSetHook;
import org.eclipse.emf.compare.ide.internal.EMFCompareIDEMessages;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;

/**
 * Descriptor of {@link IResourceSetHook}.
 * <p>
 * It prevents greedy instantiation of {@link IResourceSetHook} if not needed
 * </p>
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
class ResourceSetHookDescriptor {

	/**
	 * {@link IConfigurationElement} used to instantiate the hook.
	 */
	private final IConfigurationElement element;

	/**
	 * Prevents logging several time the same error.
	 */
	private boolean error;

	/**
	 * Constructor.
	 * 
	 * @param element
	 *            {@link #element}
	 */
	public ResourceSetHookDescriptor(IConfigurationElement element) {
		super();
		this.element = element;
	}

	/**
	 * Lazy instantiation of the hook.
	 * 
	 * @return a {@link IResourceSetHook} instance or <code>null</code> is something went wrong during the
	 *         instantiation.
	 */
	public IResourceSetHook getHook() {
		// Prevents creating a new object if we know that it can not be created.
		if (!error) {
			try {
				return (IResourceSetHook)element
						.createExecutableExtension(ResourceSetHookRegistryListener.CLASS_ATTR);
			} catch (CoreException e) {
				error = true;
				String contributorName = element.getDeclaringExtension().getContributor().getName();
				String message = EMFCompareIDEMessages.getString(
						"ResourceSetHookRegistry.hookInstanceError", contributorName); //$NON-NLS-1$
				final IStatus status = new Status(IStatus.ERROR, contributorName, message
						+ element.getDeclaringExtension().getContributor().getName(), e);
				EMFCompareRCPPlugin.getDefault().getLog().log(status);
			}
		}
		return null;
	}

}

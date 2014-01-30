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
package org.eclipse.emf.compare.rcp.internal.policy;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;
import org.eclipse.emf.compare.rcp.policy.ILoadOnDemandPolicy;
import org.eclipse.emf.compare.rcp.policy.ILoadOnDemandPolicy.Registry;

/**
 * A listener for load on demand policy extension point.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class LoadOnDemandPolicyRegistryListener extends AbstractRegistryEventListener {

	/** The name of the policy attribute. */
	static final String TAG_POLICY = "policy"; //$NON-NLS-1$

	/** The name of the class attribute. */
	static final String ATT_CLASS = "class"; //$NON-NLS-1$

	/** The registry to which policies will be added and removed. */
	private final Registry registry;

	/**
	 * Creates a new listener for the given {@code pluginID} and {@code extensionPointID}.
	 * 
	 * @param registry
	 *            the registry to which contributed policies will be added and removed.
	 * @param pluginID
	 *            the plugin id of the registering extension point.
	 * @param extensionPointID
	 *            the id of the extension point to listen to.
	 * @param log
	 *            the log in which to report errors.
	 */
	public LoadOnDemandPolicyRegistryListener(ILoadOnDemandPolicy.Registry registry, String pluginID,
			String extensionPointID, ILog log) {
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
		if (TAG_POLICY.equals(element.getName())) {
			if (element.getAttribute(ATT_CLASS) == null) {
				logMissingAttribute(element, ATT_CLASS);
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener#addedValid(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean addedValid(IConfigurationElement element) {
		try {
			ILoadOnDemandPolicy policy = (ILoadOnDemandPolicy)element.createExecutableExtension(ATT_CLASS);
			ILoadOnDemandPolicy previous = registry.addPolicy(policy);
			if (previous != null) {
				log(IStatus.WARNING, element, "The factory '" + policy.getClass().getName() //$NON-NLS-1$
						+ "' is registered twice."); //$NON-NLS-1$
			}
		} catch (CoreException e) {
			log(IStatus.ERROR, element, e.getMessage());
			return false;
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
		registry.removePolicy(element.getAttribute(ATT_CLASS));
		return true;
	}

}

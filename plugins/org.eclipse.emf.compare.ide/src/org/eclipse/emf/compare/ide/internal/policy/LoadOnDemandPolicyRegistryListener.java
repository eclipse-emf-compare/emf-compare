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
package org.eclipse.emf.compare.ide.internal.policy;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.compare.ide.EMFCompareIDEPlugin;
import org.eclipse.emf.compare.ide.policy.ILoadOnDemandPolicy;
import org.eclipse.emf.compare.ide.policy.ILoadOnDemandPolicy.Registry;
import org.eclipse.emf.compare.ide.utils.AbstractRegistryEventListener;

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
	 */
	public LoadOnDemandPolicyRegistryListener(ILoadOnDemandPolicy.Registry registry, String pluginID,
			String extensionPointID) {
		super(pluginID, extensionPointID);
		this.registry = registry;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.utils.AbstractRegistryEventListener#readElement(org.eclipse.core.runtime.IConfigurationElement,
	 *      org.eclipse.emf.compare.ide.utils.AbstractRegistryEventListener.Action)
	 */
	@Override
	protected boolean readElement(IConfigurationElement element, Action action) {
		if (element.getName().equals(TAG_POLICY)) {
			if (element.getAttribute(ATT_CLASS) == null) {
				logMissingAttribute(element, ATT_CLASS);
			} else {
				switch (action) {
					case ADD:
						try {
							ILoadOnDemandPolicy policy = (ILoadOnDemandPolicy)element
									.createExecutableExtension(ATT_CLASS);
							ILoadOnDemandPolicy previous = registry.addPolicy(policy);
							if (previous != null) {
								EMFCompareIDEPlugin.getDefault().log(
										IStatus.WARNING,
										"The factory '" + policy.getClass().getName()
												+ "' is registered twice.");
							}
						} catch (CoreException e) {
							logError(element, e.getMessage());
						}
						break;
					case REMOVE:
						registry.removePolicy(element.getAttribute(ATT_CLASS));
						break;
					default:
						throw new IllegalStateException("Unsupported case " + action);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.utils.AbstractRegistryEventListener#logError(org.eclipse.core.runtime.IConfigurationElement,
	 *      java.lang.String)
	 */
	@Override
	protected void logError(IConfigurationElement element, String string) {
		EMFCompareIDEPlugin.getDefault().log(IStatus.ERROR, string);
	}

}

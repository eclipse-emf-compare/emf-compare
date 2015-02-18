/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.dependency;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;

/**
 * This listener will react to changes against the model dependency extension point, allowing us to be in sync
 * with plugin activation and deactivation.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public class ModelDependencyProviderRegistryListener extends AbstractRegistryEventListener {

	/**
	 * The name of the dependency element.
	 */
	private static final String DEPENDENCY_ELEMENT_NAME = "dependency"; //$NON-NLS-1$

	/**
	 * The name of the class attribute of the dependency element.
	 */
	private static final String ATTRIBUTE_CLASS = "class"; //$NON-NLS-1$

	/**
	 * The registry which will actually hold all information.
	 */
	private final ModelDependencyProviderRegistry registry;

	/**
	 * Initialize a registry event listener for our handlers.
	 * 
	 * @param pluginID
	 *            ID of the plugin contributing the extension point to monitor.
	 * @param extensionPointID
	 *            Actual id of the extension point to monitor.
	 * @param log
	 *            Log in which errors/warning should be logged.
	 * @param registry
	 *            The actual store of handlers this registry will alter.
	 */
	public ModelDependencyProviderRegistryListener(String pluginID, String extensionPointID, ILog log,
			ModelDependencyProviderRegistry registry) {
		super(pluginID, extensionPointID, log);
		this.registry = registry;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean validateExtensionElement(IConfigurationElement element) {
		if (DEPENDENCY_ELEMENT_NAME.equals(element.getName())) {
			final String className = element.getAttribute(ATTRIBUTE_CLASS);
			return className != null && className.trim().length() > 0;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean addedValid(IConfigurationElement element) {
		final String className = element.getAttribute(ATTRIBUTE_CLASS);
		final DependencyProviderDescriptor descriptor = new DependencyProviderDescriptor(ATTRIBUTE_CLASS,
				element);
		registry.addProvider(className, descriptor);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean removedValid(IConfigurationElement element) {
		final String className = element.getAttribute(ATTRIBUTE_CLASS);
		registry.removeProvider(className);
		return true;
	}

}

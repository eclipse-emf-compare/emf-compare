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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;

/**
 * This class is used for information flow between {@link ModelDependencyProviderRegistryListener} and
 * {@link ModelDependencyProviderRegistry} and managing the creation of {@link IDependencyProvider} instances.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public class DependencyProviderDescriptor {

	/** Underlying {@link IConfigurationElement} describing this dependency provider. */
	private final IConfigurationElement configurationElement;

	/**
	 * Name of the configuration property that can be used to retrieve the qualified class name of this
	 * dependency provider.
	 */
	private final String attributeClassName;

	/** Don't log the same error multiple times. */
	private boolean logOnce;

	/**
	 * Default constructor.
	 * 
	 * @param attributeName
	 *            The name of the configuration attribute responsible for the registered
	 *            {@link IDependencyProvider}.
	 * @param configurationElement
	 *            The {@link IConfigurationElement} containing all relevant extension information.
	 */
	public DependencyProviderDescriptor(String attributeName, IConfigurationElement configurationElement) {
		this.attributeClassName = attributeName;
		this.configurationElement = configurationElement;
	}

	/**
	 * Returns the {@link IDependencyProvider}.
	 * 
	 * @return The newly created {@link IDependencyProvider}.
	 */
	public IDependencyProvider getDependencyProvider() {
		try {
			final IDependencyProvider provider = (IDependencyProvider)configurationElement
					.createExecutableExtension(attributeClassName);
			return provider;
		} catch (CoreException e) {
			if (!logOnce) {
				logOnce = true;
				final String className = configurationElement.getAttribute(attributeClassName);
				final String message = EMFCompareIDEUIMessages.getString(
						"ModelDependencyProviderRegistry.invalidModelDependency", className); //$NON-NLS-1$
				final IStatus status = new Status(IStatus.ERROR, configurationElement.getDeclaringExtension()
						.getContributor().getName(), message, e);
				EMFCompareIDEUIPlugin.getDefault().getLog().log(status);
			}
		}
		return null;
	}
}

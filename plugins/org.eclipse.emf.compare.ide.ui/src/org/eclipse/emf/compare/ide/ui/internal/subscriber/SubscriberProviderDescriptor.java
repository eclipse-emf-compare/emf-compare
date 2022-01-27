/*******************************************************************************
 * Copyright (c) 2022 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.subscriber;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.ide.ui.dependency.IDependencyProvider;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.subscriber.ISubscriberProvider;
import org.eclipse.emf.compare.ide.ui.subscriber.SubscriberProviderRegistry;

/**
 * This class is used for information flow between {@link SubscriberProviderRegistryListener} and
 * {@link SubscriberProviderRegistry} and managing the creation of {@link ISubscriberProvider} instances.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
public class SubscriberProviderDescriptor {

	/** Underlying {@link IConfigurationElement} describing this subscriber provider. */
	private final IConfigurationElement configurationElement;

	/**
	 * Name of the configuration property that can be used to retrieve the qualified class name of this
	 * dependency provider.
	 */
	private final String attributeClassName;

	/** Don't log the same error multiple times. */
	private boolean logOnce;

	/**
	 * The cached provider demand created the first time {@link #getSubscriberProvider()} is called.
	 */
	private ISubscriberProvider provider;

	private int ranking;

	/**
	 * Default constructor.
	 * 
	 * @param attributeName
	 *            The name of the configuration attribute responsible for the registered
	 *            {@link IDependencyProvider}.
	 * @param configurationElement
	 *            The {@link IConfigurationElement} containing all relevant extension information.
	 */
	public SubscriberProviderDescriptor(String attributeName, IConfigurationElement configurationElement,
			int ranking) {
		this.attributeClassName = attributeName;
		this.configurationElement = configurationElement;
		this.ranking = ranking;
	}

	public int getRanking() {
		return ranking;
	}

	/**
	 * Returns the {@link IDependencyProvider}.
	 * 
	 * @return The newly created {@link IDependencyProvider}.
	 */
	public ISubscriberProvider getSubscriberProvider() {
		if (provider == null && !logOnce) {
			try {
				provider = (ISubscriberProvider)configurationElement
						.createExecutableExtension(attributeClassName);
			} catch (CoreException e) {
				if (!logOnce) {
					logOnce = true;
					final String className = configurationElement.getAttribute(attributeClassName);
					final String message = EMFCompareIDEUIMessages
							.getString("SubscriberProviderRegistry.invalidSubscriber", className); //$NON-NLS-1$
					final IStatus status = new Status(IStatus.ERROR,
							configurationElement.getDeclaringExtension().getContributor().getName(), message,
							e);
					EMFCompareIDEUIPlugin.getDefault().getLog().log(status);
				}
			}
		}
		return provider;
	}
}

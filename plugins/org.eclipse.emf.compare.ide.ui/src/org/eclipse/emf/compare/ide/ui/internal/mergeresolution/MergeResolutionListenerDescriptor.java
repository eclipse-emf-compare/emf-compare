/*******************************************************************************
 * Copyright (c) 2015 EclipseSource GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Borkowski - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.mergeresolution;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.mergeresolution.IMergeResolutionListener;

/**
 * Describes meta-data about a listener to merge resolution events. See {@link IMergeResolutionListener}.
 * 
 * @author Michael Borkowski <mborkowski@eclipsesource.com>
 */
public class MergeResolutionListenerDescriptor {

	/** Underlying {@link IConfigurationElement} describing this listener. */
	private final IConfigurationElement configurationElement;

	/**
	 * Name of the configuration property that can be used to retrieve the qualified class name of this
	 * listener.
	 */
	private final String attributeClassName;

	/** Don't log the same error multiple times. */
	private boolean logOnce;

	/**
	 * Default constructor.
	 * 
	 * @param attributeName
	 *            The name of the configuration attribute responsible for the registered
	 *            {@link IMergeResolutionListener}.
	 * @param configurationElement
	 *            The {@link IConfigurationElement} containing all relevant extension information.
	 */
	public MergeResolutionListenerDescriptor(String attributeName,
			IConfigurationElement configurationElement) {
		this.attributeClassName = attributeName;
		this.configurationElement = configurationElement;
	}

	/**
	 * Returns the {@link IMergeResolutionListener}.
	 * 
	 * @return The newly created {@link IMergeResolutionListener}.
	 */
	public IMergeResolutionListener getMergeResolutionListener() {
		try {
			final IMergeResolutionListener provider = (IMergeResolutionListener)configurationElement
					.createExecutableExtension(attributeClassName);
			return provider;
		} catch (CoreException e) {
			if (!logOnce) {
				logOnce = true;
				final String className = configurationElement.getAttribute(attributeClassName);
				final String message = EMFCompareIDEUIMessages
						.getString("ModelDependencyProviderRegistry.invalidModelDependency", className); //$NON-NLS-1$
				final IStatus status = new Status(IStatus.ERROR,
						configurationElement.getDeclaringExtension().getContributor().getName(), message, e);
				EMFCompareIDEUIPlugin.getDefault().getLog().log(status);
			}
		}
		return null;
	}

}

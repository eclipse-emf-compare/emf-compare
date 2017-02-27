/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.logical.IModelMinimizer;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;

/**
 * A registry event listener that propagates model minimizers changes from the extension registry to a given
 * model minimizer registry.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
public class ModelMinimizerRegistryListener extends AbstractRegistryEventListener {
	/** The "modelMinimizer" tag of our extension point. */
	private static final String TAG_MODEL_MINIMIZER = "modelMinimizer"; //$NON-NLS-1$

	/** The "class" attribute of our modelMinimizer tag. */
	private static final String ATTRIBUTE_CLASS = "class"; //$NON-NLS-1$

	/** The actual registry this listener will alter. */
	private IModelMinimizer.Registry registry;

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
	 *            The actual registry of model minimizers this listener will alter.
	 */
	public ModelMinimizerRegistryListener(String pluginID, String extensionPointID, ILog log,
			IModelMinimizer.Registry registry) {
		super(pluginID, extensionPointID, log);
		this.registry = registry;
	}

	@Override
	protected boolean validateExtensionElement(IConfigurationElement element) {
		// Don't work twice as much, validate as we add.
		// Removing cannot fail.
		return true;
	}

	@Override
	protected boolean addedValid(IConfigurationElement element) {
		if (element.getName().equals(TAG_MODEL_MINIMIZER)) {
			try {
				IModelMinimizer minimizer = (IModelMinimizer)element
						.createExecutableExtension(ATTRIBUTE_CLASS);
				registry.addMinimizer(minimizer);
			} catch (CoreException e) {
				// Shouldn't happen since the registry listener should have checked that.
				// log anyway.
				final String message = EMFCompareIDEUIMessages.getString(
						"ModelMinimizerRegistry.invalidClass", element.getAttribute(ATTRIBUTE_CLASS)); //$NON-NLS-1$
				final IStatus status = new Status(IStatus.ERROR,
						element.getDeclaringExtension().getContributor().getName(), message, e);
				EMFCompareIDEUIPlugin.getDefault().getLog().log(status);
			}
			return true;
		}
		return false;
	}

	@Override
	protected boolean removedValid(IConfigurationElement element) {
		final String className = element.getAttribute(ATTRIBUTE_CLASS);
		registry.removeMinimizer(className);
		return true;
	}

}

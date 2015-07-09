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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.emf.compare.ide.ui.mergeresolution.IMergeResolutionListener;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;

/**
 * This class is used for information flow between {@link MergeResolutionListenerRegistryListener} and
 * {@link MergeResolutionListenerRegistry} and managing the creation of {@link IMergeResolutionListener}
 * instances.
 * 
 * @author Michael Borkowski <mborkowski@eclipsesource.com>
 */
public class MergeResolutionListenerRegistryListener extends AbstractRegistryEventListener {

	/**
	 * The name of the listener element.
	 */
	private static final String LISTENER_ELEMENT_NAME = "listener"; //$NON-NLS-1$

	/**
	 * The name of the class attribute of the dependency element.
	 */
	private static final String ATTRIBUTE_CLASS = "class"; //$NON-NLS-1$

	/**
	 * The registry which will actually hold all information.
	 */
	private final MergeResolutionListenerRegistry registry;

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
	public MergeResolutionListenerRegistryListener(String pluginID, String extensionPointID, ILog log,
			MergeResolutionListenerRegistry registry) {
		super(pluginID, extensionPointID, log);
		this.registry = registry;
	}

	@Override
	protected boolean validateExtensionElement(IConfigurationElement element) {
		if (LISTENER_ELEMENT_NAME.equals(element.getName())) {
			final String className = element.getAttribute(ATTRIBUTE_CLASS);
			return className != null && className.trim().length() > 0;
		}
		return false;
	}

	@Override
	protected boolean addedValid(IConfigurationElement element) {
		final String className = element.getAttribute(ATTRIBUTE_CLASS);
		final MergeResolutionListenerDescriptor descriptor = new MergeResolutionListenerDescriptor(
				ATTRIBUTE_CLASS, element);
		registry.addProvider(className, descriptor);
		return true;
	}

	@Override
	protected boolean removedValid(IConfigurationElement element) {
		final String className = element.getAttribute(ATTRIBUTE_CLASS);
		registry.removeProvider(className);
		return true;
	}
}

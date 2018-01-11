/*******************************************************************************
 * Copyright (c) 2018 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.internal.logical;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.emf.compare.ide.EMFCompareIDEPlugin;
import org.eclipse.emf.compare.ide.internal.hook.ResourceSetHookRegistry;
import org.eclipse.emf.compare.ide.logical.IModelInclusionTester;
import org.eclipse.emf.compare.ide.logical.ModelInclusionTesterRegistry;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;

/**
 * Listener keeping the {@link ModelInclusionTesterRegistry} in sync with the extension point.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class ModelInclusionTesterRegistryListener extends AbstractRegistryEventListener {

	/**
	 * Extension point id.
	 */
	public static final String EXT_ID = "modelInclusionTester"; //$NON-NLS-1$

	/**
	 * {@link ResourceSetHookRegistry}.
	 */
	private final ModelInclusionTesterRegistry registry;

	/**
	 * The factory for creating {@link IModelInclusionTester model inclusion testers}.
	 */
	private final ModelInclusionTesterFactory factory;

	/**
	 * Constructor.
	 * 
	 * @param log
	 *            {@link ILog}.
	 * @param registry
	 *            Registry to fill.
	 */
	public ModelInclusionTesterRegistryListener(ILog log, ModelInclusionTesterRegistry registry) {
		super(EMFCompareIDEPlugin.PLUGIN_ID, ModelInclusionTesterRegistryListener.EXT_ID, log);
		this.registry = registry;
		this.factory = new ModelInclusionTesterFactory();
	}

	@Override
	protected boolean validateExtensionElement(IConfigurationElement element) {
		return factory.canCreateModelInclusionTester(element);
	}

	@Override
	protected boolean addedValid(IConfigurationElement element) {
		final AbstractModelInclusionTester tester = factory.createModelInclusionTester(element);
		registry.add(tester.getKey(), tester);
		return true;
	}

	@Override
	protected boolean removedValid(IConfigurationElement element) {
		String key = factory.getKey(element);
		if (key != null) {
			return registry.remove(key);
		}
		return false;
	}

}

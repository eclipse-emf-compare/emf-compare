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

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.ide.ui.logical.CompoundModelMinimizer;
import org.eclipse.emf.compare.ide.ui.logical.IModelMinimizer;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;

/**
 * A basic implementation of a registry for model minimizers that stores the model minimizers in a map with
 * their respective class names.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
public class ModelMinimizerRegistry implements IModelMinimizer.Registry {

	/** The store of registered policy. */
	private final Map<String, IModelMinimizer> modelMinimizers;

	public ModelMinimizerRegistry() {
		modelMinimizers = new ConcurrentHashMap<String, IModelMinimizer>();
	}

	public List<IModelMinimizer> getModelMinimizers() {
		return Lists.newArrayList(modelMinimizers.values());
	}

	public IModelMinimizer addMinimizer(IModelMinimizer minimizer) {
		return modelMinimizers.put(minimizer.getClass().getName(), minimizer);
	}

	public IModelMinimizer removeMinimizer(String className) {
		return modelMinimizers.remove(className);
	}

	public void minimize(SynchronizationModel syncModel, IProgressMonitor monitor) {
		for (IModelMinimizer modelMinimizer : modelMinimizers.values()) {
			modelMinimizer.minimize(syncModel, monitor);
		}
	}

	public CompoundModelMinimizer getCompoundMinimizer() {
		return new CompoundModelMinimizer(getModelMinimizers());
	}

}

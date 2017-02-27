/*******************************************************************************
 * Copyright (c) 2013, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - javadoc fixes
 *     Martin Fleck - bug 512562
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.logical;

import com.google.common.annotations.Beta;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * This can be used in order to tell EMF Compare how to minimize the logical model to a reduced set of
 * resources. For example, this can be used to remove all binary identical resources from the comparison
 * scope, since we know there can be no differences on such resources.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 4.0
 */
@Beta
public interface IModelMinimizer {
	/**
	 * This will be called to reduce the number of resources in this model's traversals.
	 * 
	 * @param syncModel
	 *            The synchronization model to be minimized.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 */
	void minimize(SynchronizationModel syncModel, IProgressMonitor monitor);

	/**
	 * This will be called to reduce the number of resources in this model's traversals based on the given
	 * starting point of the left logical model.
	 * 
	 * @param file
	 *            The file that has been used as the starting point to resolve the left logical model.
	 * @param syncModel
	 *            The synchronization model to be minimized.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 * @since 4.4
	 */
	void minimize(IFile file, SynchronizationModel syncModel, IProgressMonitor monitor);

	/**
	 * A registry for model minimizers.
	 * 
	 * @since 4.4
	 */
	interface Registry {
		/**
		 * Returns the list of registered model minimizers.
		 * 
		 * @return the list of registered model minimizers.
		 */
		List<IModelMinimizer> getModelMinimizers();

		/**
		 * Add the given {@code minimizer} to this registry.
		 * 
		 * @param minimizer
		 *            the minimizer to be added.
		 * @return the previous value associated with the class name of the given {@code minimizer}.
		 */
		IModelMinimizer addMinimizer(IModelMinimizer minimizer);

		/**
		 * Removes the {@code minimizer} registered within this registry with the given class name.
		 * 
		 * @param className
		 *            the class name of a previously registered {@code minimizer}.
		 * @return the previously registered {@code minimizer} or null if none was registered.
		 */
		IModelMinimizer removeMinimizer(String className);

		/**
		 * Returns a minimizer containing all registered minimizers.
		 * 
		 * @return a new compound minimizer with all registered minimizers.
		 */
		CompoundModelMinimizer getCompoundMinimizer();
	}
}

/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.logical;

import com.google.common.annotations.Beta;

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
	 * @param leftTraversal
	 *            The traversal corresponding to the left side.
	 * @param rightTraversal
	 *            The traversal corresponding to the right side.
	 * @param originTraversal
	 *            The traversal, if any, corresponding to the common ancestor of left and right sides.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 */
	void minimize(SynchronizationModel syncModel, IProgressMonitor monitor);
}

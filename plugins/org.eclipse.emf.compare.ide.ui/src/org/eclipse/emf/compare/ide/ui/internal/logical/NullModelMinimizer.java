/*******************************************************************************
 * Copyright (c) 2015, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Martin Fleck - bug 512562
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.ide.ui.logical.IModelMinimizer;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;

/**
 * An empty implementation of an {@link IModelMinimizer}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class NullModelMinimizer implements IModelMinimizer {
	/** {@inheritDoc} */
	public void minimize(SynchronizationModel syncModel, IProgressMonitor monitor) {
		// Do nothing
	}

	/** {@inheritDoc} */
	public void minimize(IFile file, SynchronizationModel syncModel, IProgressMonitor monitor) {
		// Do nothing
	}
}

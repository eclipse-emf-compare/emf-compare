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
package org.eclipse.emf.compare.ide.ui.logical;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * This class encapsulates a list of model minimizers and propagates any calls to each element in the list.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 * @since 4.4
 */
public class CompoundModelMinimizer implements IModelMinimizer {

	/** The list of model minimizers. */
	protected List<IModelMinimizer> minimizers;

	/**
	 * Creates a new compound model minimizer with the given minimizers.
	 * 
	 * @param minimizers
	 *            model minimizers
	 */
	public CompoundModelMinimizer(IModelMinimizer... minimizers) {
		this(Arrays.asList(minimizers));
	}

	/**
	 * Creates a new compound model minimizer with the given minimizers.
	 * 
	 * @param minimizers
	 *            model minimizers
	 */
	public CompoundModelMinimizer(List<IModelMinimizer> minimizers) {
		if (minimizers == null) {
			this.minimizers = Lists.newArrayList();
		} else {
			this.minimizers = minimizers;
		}
	}

	/**
	 * {@inheritDoc} Specifically, this minimizers propagates the call to all encapsulated minimizers.
	 */
	public void minimize(SynchronizationModel syncModel, IProgressMonitor monitor) {
		for (IModelMinimizer minimizer : minimizers) {
			minimizer.minimize(syncModel, monitor);
		}
	}

	/**
	 * {@inheritDoc} Specifically, this minimizers propagates the call to all encapsulated minimizers.
	 */
	public void minimize(IFile file, SynchronizationModel syncModel, IProgressMonitor monitor) {
		for (IModelMinimizer minimizer : minimizers) {
			minimizer.minimize(file, syncModel, monitor);
		}
	}
}

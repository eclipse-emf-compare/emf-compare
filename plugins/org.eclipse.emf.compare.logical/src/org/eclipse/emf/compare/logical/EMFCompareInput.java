/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.logical;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.logical.synchronization.EMFCompareAdapter;
import org.eclipse.emf.compare.logical.synchronization.EMFDelta;
import org.eclipse.emf.compare.logical.synchronization.EMFSaveableBuffer;
import org.eclipse.team.ui.mapping.ISynchronizationCompareInput;
import org.eclipse.team.ui.mapping.SaveableComparison;

/**
 * This will be used as an input for the compare editor when called from our model provider (
 * {@link EMFCompareAdapter#asCompareInput(org.eclipse.team.core.mapping.ISynchronizationContext, Object)}).
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class EMFCompareInput extends DiffNode implements ISynchronizationCompareInput {
	/** The actual result of the comparison. */
	private final EMFDelta delta;

	/** Model buffer for this editor. */
	private final EMFSaveableBuffer buffer;

	/**
	 * Instantiates this input.
	 * 
	 * @param buffer
	 *            The buffer for this comparison editor.
	 * @param delta
	 *            The actual comparison result.
	 * @param ancestor
	 *            Common ancestor of the two compared objects of this comparison.
	 * @param local
	 *            The local variant of the compared object.
	 * @param remote
	 *            The remote variant of the compared object.
	 */
	public EMFCompareInput(EMFSaveableBuffer buffer, EMFDelta delta, ITypedElement ancestor,
			ITypedElement local, ITypedElement remote) {
		super(delta.getKind(), ancestor, local, remote);
		this.buffer = buffer;
		this.delta = delta;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.ISynchronizationCompareInput#getSaveable()
	 */
	public SaveableComparison getSaveable() {
		return buffer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.ISynchronizationCompareInput#getFullPath()
	 */
	public String getFullPath() {
		// FIXME should we return the local resource's path?
		return getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.ISynchronizationCompareInput#prepareInput(org.eclipse.compare.CompareConfiguration,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void prepareInput(CompareConfiguration configuration, IProgressMonitor monitor)
			throws CoreException {
		// Input has already been prepared
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.ISynchronizationCompareInput#isCompareInputFor(java.lang.Object)
	 */
	public boolean isCompareInputFor(Object object) {
		// Not used
		return false;
	}
}

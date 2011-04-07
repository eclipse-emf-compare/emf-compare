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
package org.eclipse.emf.compare.logical.synchronization;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.logical.model.EMFModelProvider;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.team.ui.mapping.SaveableComparison;

/**
 * Instances of this class will be used to buffer the changes made to the EMF logical models during a
 * comparison and determine when a save is needed.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class EMFSaveableBuffer extends SaveableComparison {
	/** Cache key for the logical model buffer within the synchronization cache. */
	public static final String SYNCHRONIZATION_CACHE_KEY = EMFModelProvider.PROVIDER_ID + ".sync.cache"; //$NON-NLS-1$

	/** Human-readable name of this saveable. */
	private final String name;

	/** Resource set which contains the actual models. */
	private final ResourceSet resourceSet;

	/**
	 * Creates our buffer given its human-readable name and its underlying resource set.
	 * 
	 * @param name
	 *            Human-readable name of this saveable.
	 * @param resourceSet
	 *            Resource set which contains the actual models.
	 */
	public EMFSaveableBuffer(String name, ResourceSet resourceSet) {
		this.name = name;
		this.resourceSet = resourceSet;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.SaveableComparison#performSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void performSave(IProgressMonitor monitor) throws CoreException {
		for (Resource resource : resourceSet.getResources()) {
			try {
				resource.save(Collections.emptyMap());
			} catch (IOException e) {
				// FIXME provide feedback
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.SaveableComparison#performRevert(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void performRevert(IProgressMonitor monitor) {
		// FIXME how can we revert changes made to this logical model?
		// for (Resource resource : resourceSet.getResources()) {
		// resource.unload();
		// }
		// EcoreUtil.resolveAll(resourceSet);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.Saveable#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.Saveable#getToolTipText()
	 */
	@Override
	public String getToolTipText() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.Saveable#getImageDescriptor()
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		// FIXME where is this displayed when non-null?
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.Saveable#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		return object instanceof EMFSaveableBuffer && name.equals(((EMFSaveableBuffer)object).name)
				&& resourceSet.equals(((EMFSaveableBuffer)object).resourceSet);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.Saveable#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int hashCode = prime * name.hashCode();
		hashCode += prime * resourceSet.hashCode();
		return hashCode;
	}
}

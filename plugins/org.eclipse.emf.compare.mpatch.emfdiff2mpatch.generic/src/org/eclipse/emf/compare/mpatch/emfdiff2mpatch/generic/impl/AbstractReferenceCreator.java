/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.emfdiff2mpatch.generic.impl;

import java.util.Collection;

import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.generic.Activator;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.generic.util.QvtlibHelper;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;
import org.eclipse.emf.compare.mpatch.symrefs.ExternalElementReference;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This symbolic reference creator filters external references, that is, references to other resources. In such cases,
 * {@link ExternalElementReference}s are created by {@link ExternalElementReferenceCreator}. In all other cases,
 * subclasses must create the symbolic reference.
 * 
 * Non-external references must be set via {@link AbstractReferenceCreator#setNonExternalResources(Collection)}.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
abstract class AbstractReferenceCreator implements ISymbolicReferenceCreator {

	/**
	 * Non-external resources are used to identify for which model elements an {@link ExternalElementReference} has to
	 * be created.
	 */
	private Collection<Resource> nonExternalResources;

	/**
	 * In case <code>self</code> is contained in {@link AbstractReferenceCreator#nonExternalResources},
	 * {@link AbstractReferenceCreator#createSymbolicReference(EObject)} is called. Otherwise,
	 * {@link ExternalElementReferenceCreator} is used to create the symbolic reference.
	 */
	@Override
	public final IElementReference toSymbolicReference(EObject self) {

		if (isExternalElement(self)) {
			// for external references we need a special reference
			return ExternalElementReferenceCreator.toSymbolicReference(self);
		} else {
			// create symbolic reference object for any object
			return createSymbolicReference(self);
		}
	}

	/**
	 * Subclasses must create and return their specific symbolic references for all non-external model elements.
	 * 
	 * @param self
	 *            The model element for which a symbolic reference should be created.
	 * @return A symbolic reference which represents the given model element.
	 * @see ISymbolicReferenceCreator#toSymbolicReference(EObject)
	 */
	protected abstract IElementReference createSymbolicReference(EObject self);

	/**
	 * @return <code>true</code> if <code>self</code> is contained in one of the non-external resources;
	 *         <code>false</code> otherwise.
	 */
	protected boolean isExternalElement(EObject self) {
		// do not allow proxies here!
		if (self.eIsProxy()) {
			Activator.getDefault().logError(
					"Cannot create a " + MPatchConstants.SYMBOLIC_REFERENCE_NAME + " for a proxy! "
							+ "Please check whether the referenced models exist and all "
							+ MPatchConstants.SYMBOLIC_REFERENCES_NAME + " are correct! \nProxy: " + self);
			throw new IllegalArgumentException("Cannot create a " + MPatchConstants.SYMBOLIC_REFERENCE_NAME
					+ " for a proxy! " + "Please check whether the referenced models exist and all "
					+ MPatchConstants.SYMBOLIC_REFERENCES_NAME + " are correct! \nProxy: " + self);
		}

		// if the element doesn't have a resource, it is not an external element
		if (self.eResource() == null)
			return false;

		// if no non-external resources are defined, treat every element as non-external!
		if (nonExternalResources == null)
			return false;

		// check if the element's resource is contained in non-external resources
		if (nonExternalResources.contains(self.eResource()))
			return false; // fast way (may not always work)
		for (Resource resource : nonExternalResources) {
			if (resource.getURI().equals(self.eResource().getURI())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNonExternalResources(Collection<Resource> resources) {
		nonExternalResources = resources;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUriString(EObject self) {
		return QvtlibHelper.getUriString(self);
	}

}
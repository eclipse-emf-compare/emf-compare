/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.internal.merge.impl;

import org.eclipse.emf.compare.diff.merge.DefaultMerger;
import org.eclipse.emf.compare.diff.merge.service.MergeService;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * Merger for an {@link UpdateUniqueReferenceValue} operation.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class UpdateReferenceMerger extends DefaultMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#applyInOrigin()
	 */
	@Override
	public void applyInOrigin() {
		final UpdateReference theDiff = (UpdateReference)this.diff;
		final EReference reference = theDiff.getReference();
		final EObject element = theDiff.getLeftElement();
		final EObject leftTarget = (EObject)theDiff.getRightElement().eGet(reference);
		final EObject matchedLeftTarget = theDiff.getLeftTarget();

		if (leftTarget == null) {
			// We're unsetting the value, no need to copy
			element.eUnset(reference);
		} else {
			MergeService.getCopier(diff).copyReferenceValue(reference, element, leftTarget,
					matchedLeftTarget, -1);
		}

		super.applyInOrigin();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#undoInTarget()
	 */
	@Override
	public void undoInTarget() {
		final UpdateReference theDiff = (UpdateReference)this.diff;
		final EReference reference = theDiff.getReference();
		final EObject element = theDiff.getRightElement();
		final EObject rightTarget = (EObject)theDiff.getLeftElement().eGet(reference);
		final EObject matchedRightTarget = theDiff.getRightTarget();

		if (rightTarget == null) {
			// We're unsetting the value, no need to copy
			element.eUnset(reference);
		} else {
			MergeService.getCopier(diff).copyReferenceValue(reference, element, rightTarget,
					matchedRightTarget, -1);
		}

		super.undoInTarget();
	}
}

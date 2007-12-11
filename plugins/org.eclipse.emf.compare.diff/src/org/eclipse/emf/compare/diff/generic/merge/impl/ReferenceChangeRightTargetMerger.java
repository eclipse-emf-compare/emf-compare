/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.generic.merge.impl;

import java.util.Iterator;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.merge.api.DefaultMerger;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EObject;

/**
 * Merger for an {@link ReferenceChangeRightTarget} operation.<br/>
 * <p>
 * Are considered for this merger :
 * <ul>
 * <li>{@link AddReferenceValue}</li>
 * <li>{@link RemoteRemoveReferenceValue}</li>
 * </ul>
 * </p>
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class ReferenceChangeRightTargetMerger extends DefaultMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#applyInOrigin()
	 */
	@Override
	public void applyInOrigin() {
		/*
		 * FIXME [bug #209521] if we're merging a reference pointing to an UnmatchedElement, we should merge
		 * its corresponding AddModelElement (or RemoteDeleteModelElement) beforehand. In the current state,
		 * we're doing a hard-link between the two models.
		 */
		final ReferenceChangeRightTarget theDiff = (ReferenceChangeRightTarget)this.diff;
		final EObject element = theDiff.getLeftElement();
		final EObject leftTarget = theDiff.getLeftAddedTarget();
		try {
			EFactory.eAdd(element, theDiff.getReference().getName(), leftTarget);
		} catch (FactoryException e) {
			EMFComparePlugin.log(e, true);
		}
		// We'll now look through this reference's eOpposite as they are already taken care of
		final Iterator<EObject> siblings = getDiffModel().eAllContents();
		while (siblings.hasNext()) {
			final DiffElement op = (DiffElement)siblings.next();
			if (op instanceof ReferenceChangeRightTarget) {
				final ReferenceChangeRightTarget link = (ReferenceChangeRightTarget)op;
				// If this is my eOpposite, delete it from the DiffModel (merged along with this one)
				if (link.getReference().equals(theDiff.getReference().getEOpposite())
						&& link.getLeftAddedTarget().equals(element)) {
					removeFromContainer(link);
				}
			}
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
		final ReferenceChangeRightTarget theDiff = (ReferenceChangeRightTarget)this.diff;
		final EObject element = theDiff.getRightElement();
		final EObject rightTarget = theDiff.getRightAddedTarget();
		try {
			EFactory.eRemove(element, theDiff.getReference().getName(), rightTarget);
		} catch (FactoryException e) {
			EMFComparePlugin.log(e, true);
		}
		// we should now have a look for AddReferencesLinks needing this object
		final Iterator<EObject> siblings = getDiffModel().eAllContents();
		while (siblings.hasNext()) {
			final DiffElement op = (DiffElement)siblings.next();
			if (op instanceof ReferenceChangeRightTarget) {
				final ReferenceChangeRightTarget link = (ReferenceChangeRightTarget)op;
				// now if I'm in the target References I should put my copy in the origin
				if (link.getReference().equals(theDiff.getReference().getEOpposite())
						&& link.getRightAddedTarget().equals(element)) {
					removeFromContainer(link);
				}
			}
		}
		super.undoInTarget();
	}
}

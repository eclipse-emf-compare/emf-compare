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
package org.eclipse.emf.compare.diff.merge.internal.impl;

import java.util.Iterator;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.merge.api.DefaultMerger;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EObject;

/**
 * Merger for an {@link ReferenceChangeLeftTarget} operation.<br/>
 * <p>
 * Are considered for this merger :
 * <ul>
 * <li>{@link RemoveReferenceValue}</li>
 * <li>{@link RemoteAddReferenceValue}</li>
 * </ul>
 * </p>
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class ReferenceChangeLeftTargetMerger extends DefaultMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#applyInOrigin()
	 */
	@Override
	public void applyInOrigin() {
		final ReferenceChangeLeftTarget theDiff = (ReferenceChangeLeftTarget)this.diff;
		final EObject element = theDiff.getLeftElement();
		final EObject leftTarget = theDiff.getLeftRemovedTarget();
		try {
			EFactory.eRemove(element, theDiff.getReference().getName(), leftTarget);
		} catch (FactoryException e) {
			EMFComparePlugin.log(e, true);
		}
		// we should now have a look for AddReferencesLinks needing this object
		final Iterator<EObject> siblings = getDiffModel().eAllContents();
		while (siblings.hasNext()) {
			final DiffElement op = (DiffElement)siblings.next();
			if (op instanceof ReferenceChangeLeftTarget) {
				final ReferenceChangeLeftTarget link = (ReferenceChangeLeftTarget)op;
				// now if I'm in the target References I should put my copy in the origin
				if (link.getReference().equals(theDiff.getReference().getEOpposite())
						&& link.getLeftRemovedTarget().equals(element)) {
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
		/*
		 * FIXME [bug #209521] if we're merging a reference pointing to an UnmatchedElement, we should merge
		 * its corresponding DeleteModelElement (or RemoteAddModelElement) beforehand. In the current state,
		 * we're doing a hard-link between the two models.
		 */
		final ReferenceChangeLeftTarget theDiff = (ReferenceChangeLeftTarget)this.diff;
		final EObject element = theDiff.getRightElement();
		final EObject rightTarget = theDiff.getRightRemovedTarget();
		try {
			EFactory.eAdd(element, theDiff.getReference().getName(), rightTarget);
		} catch (FactoryException e) {
			EMFComparePlugin.log(e, true);
		}
		// we should now have a look for AddReferencesLinks needing this object
		final Iterator<EObject> siblings = getDiffModel().eAllContents();
		while (siblings.hasNext()) {
			final DiffElement op = (DiffElement)siblings.next();
			if (op instanceof ReferenceChangeLeftTarget) {
				final ReferenceChangeLeftTarget link = (ReferenceChangeLeftTarget)op;
				// now if I'm in the target References I should put my copy in the origin
				if (link.getReference().equals(theDiff.getReference().getEOpposite())
						&& link.getRightRemovedTarget().equals(element)) {
					removeFromContainer(link);
				}
			}
		}
		super.undoInTarget();
	}
}

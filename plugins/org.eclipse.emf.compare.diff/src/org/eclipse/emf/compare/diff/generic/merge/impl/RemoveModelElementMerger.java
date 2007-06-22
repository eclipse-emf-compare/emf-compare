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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.RemoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Merger for a {@link RemoveModelElement}.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class RemoveModelElementMerger extends DefaultMerger {
	/**
	 * Constructs a merger for an {@link RemoveModelElement} operation.
	 * 
	 * @param element
	 *            The element for which we create the merger.
	 */
	public RemoveModelElementMerger(DiffElement element) {
		super(element);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.merge.api.AbstractMerger#applyInOrigin()
	 */
	@Override
	public void applyInOrigin() {
		final RemoveModelElement diff = (RemoveModelElement)this.diff;
		final EObject element = diff.getLeftElement();
		final EObject parent = element.eContainer();
		EcoreUtil.remove(element);

		// now removes all the dangling references
		removeFromContainer(element);

		try {
			EcoreUtil.getRootContainer(parent).eResource().save(new HashMap());
		} catch (IOException e) {
			EMFComparePlugin.getDefault().log(e, true);
		}
		super.applyInOrigin();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.merge.api.AbstractMerger#undoInTarget()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void undoInTarget() {
		final RemoveModelElement diff = (RemoveModelElement)this.diff;
		// we should copy the element to the Origin one.
		final EObject origin = diff.getRightParent();
		final EObject element = diff.getLeftElement();
		final EObject newOne = EcoreUtil.copy(element);
		final EReference ref = element.eContainmentFeature();
		try {
			EFactory.eAdd(origin, ref.getName(), newOne);
		} catch (FactoryException e) {
			EMFComparePlugin.getDefault().log(e, true);
		}

		try {
			EcoreUtil.getRootContainer(origin).eResource().save(new HashMap());
		} catch (IOException e) {
			EMFComparePlugin.getDefault().log(e, true);
		}
		// we should now have a look for RemovedReferencesLinks needing elements
		// to apply
		final DiffModel log = (DiffModel)diff.eContainer();
		final Iterator siblings = log.eAllContents();
		while (siblings.hasNext()) {
			final DiffElement op = (DiffElement)siblings.next();
			if (op instanceof RemoveReferenceValue) {
				final RemoveReferenceValue link = (RemoveReferenceValue)op;
				// now if I'm in the target References I should put my copy in
				// the origin
				if (link.getLeftRemovedTarget().contains(element)) {
					link.getLeftRemovedTarget().add(newOne);
					try {
						EcoreUtil.getRootContainer(link).eResource().save(new HashMap());
					} catch (IOException e) {
						EMFComparePlugin.getDefault().log(e, true);
					}

				}

			}
		}
		super.undoInTarget();
	}
}

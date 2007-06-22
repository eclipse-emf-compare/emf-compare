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
import org.eclipse.emf.compare.diff.metamodel.AddModelElement;
import org.eclipse.emf.compare.diff.metamodel.AddReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Merger for an {@link AddModelElement}.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class AddModelElementMerger extends DefaultMerger {
	/**
	 * Constructs a merger for an {@link AddModelElement} operation.
	 * 
	 * @param element
	 *            The element for which we create the merger.
	 */
	public AddModelElementMerger(DiffElement element) {
		super(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.api.AbstractMerger#applyInOrigin()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void applyInOrigin() {
		final AddModelElement diff = (AddModelElement)this.diff;
		final EObject origin = diff.getLeftParent();
		final EObject element = diff.getRightElement();
		final EObject newOne = EcoreUtil.copy(element);
		final EReference ref = element.eContainmentFeature();
		try {
			EFactory.eAdd(origin, ref.getName(), newOne);
			EcoreUtil.getRootContainer(origin).eResource().save(new HashMap());
		} catch (FactoryException e) {
			EMFComparePlugin.getDefault().log(e, true);
		} catch (IOException e) {
			EMFComparePlugin.getDefault().log(e, true);
		}
		// we should now have a look for AddReferencesLinks needed this object
		final DiffModel log = (DiffModel)diff.eContainer();
		final Iterator siblings = log.eAllContents();
		while (siblings.hasNext()) {
			final DiffElement op = (DiffElement)siblings.next();
			if (op instanceof AddReferenceValue) {
				final AddReferenceValue link = (AddReferenceValue)op;
				// now if I'm in the target References I should put my copy in
				// the origin
				if (link.getRightAddedTarget().contains(element)) {
					link.getRightAddedTarget().add(newOne);
					try {
						EcoreUtil.getRootContainer(link).eResource().save(new HashMap());
					} catch (IOException e) {
						EMFComparePlugin.getDefault().log(e, true);
					}
				}
			}
		}
		super.applyInOrigin();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.merge.api.AbstractMerger#undoInTarget()
	 */
	@Override
	public void undoInTarget() {
		// getElement is the target element
		final AddModelElement diff = (AddModelElement)this.diff;
		final EObject element = diff.getRightElement();
		final EObject parent = diff.getRightElement().eContainer();
		EcoreUtil.remove(element);
		// now removes all the dangling references
		removeDanglingReferences(parent);
		super.undoInTarget();
	}
}

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

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.merge.DefaultMerger;
import org.eclipse.emf.compare.diff.merge.service.MergeService;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange;
import org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChange;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Merger for an {@link ReferenceChangeLeftTarget} operation.<br/>
 * <p>
 * Are considered for this merger :
 * <ul>
 * <li>AddReferenceValue</li>
 * <li>RemoteRemoveReferenceValue</li>
 * </ul>
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ReferenceChangeLeftTargetMerger extends DefaultMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.DefaultMerger#applyInOrigin()
	 */
	@Override
	public void applyInOrigin() {
		final ReferenceChangeLeftTarget theDiff = (ReferenceChangeLeftTarget)this.diff;
		final EObject element = theDiff.getLeftElement();
		final EObject leftTarget = theDiff.getLeftTarget();
		try {
			EFactory.eRemove(element, theDiff.getReference().getName(), leftTarget);
		} catch (final FactoryException e) {
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
						&& link.getLeftTarget().equals(element)) {
					removeFromContainer(link);
				}
			} else if (op instanceof ResourceDependencyChange) {
				final ResourceDependencyChange link = (ResourceDependencyChange)op;
				final Resource res = link.getRoots().get(0).eResource();
				if (res == leftTarget.eResource()) {
					EcoreUtil.remove(link);
					res.unload();
				}
			}
		}
		super.applyInOrigin();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.DefaultMerger#undoInTarget()
	 */
	@Override
	public void undoInTarget() {
		final ReferenceChangeLeftTarget theDiff = (ReferenceChangeLeftTarget)this.diff;

		final EReference reference = theDiff.getReference();

		final EObject rightElement = theDiff.getRightElement();
		final EObject leftElement = theDiff.getLeftElement();

		final EObject leftTarget = theDiff.getLeftTarget();
		final EObject rightTarget = theDiff.getRightTarget();

		int index = -1;
		if (reference.isMany()) {
			final Object leftRefValue = leftElement.eGet(reference);
			if (leftRefValue instanceof List) {
				final List refLeftValueList = (List)leftRefValue;
				index = refLeftValueList.indexOf(leftTarget);
			}
		}
		final EObject copiedValue = MergeService.getCopier(diff).copyReferenceValue(reference, rightElement,
				leftTarget, rightTarget, index);

		// we should now have a look for AddReferencesLinks needing this object
		final Iterator<EObject> siblings = getDiffModel().eAllContents();
		while (siblings.hasNext()) {
			final DiffElement op = (DiffElement)siblings.next();
			if (op instanceof ReferenceChangeLeftTarget) {
				final ReferenceChangeLeftTarget link = (ReferenceChangeLeftTarget)op;
				// now if I'm in the target References I should put my copy in the origin
				if (link.getReference().equals(reference.getEOpposite())
						&& link.getLeftTarget().equals(rightElement)) {
					removeFromContainer(link);
				}
			} else if (op instanceof ReferenceOrderChange) {
				final ReferenceOrderChange link = (ReferenceOrderChange)op;
				if (link.getReference().equals(reference)) {
					// FIXME respect ordering!
					link.getRightTarget().add(copiedValue);
				}
			}
		}
		super.undoInTarget();
	}
}

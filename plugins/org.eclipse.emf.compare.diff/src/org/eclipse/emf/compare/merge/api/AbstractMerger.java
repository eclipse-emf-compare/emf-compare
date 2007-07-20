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
package org.eclipse.emf.compare.merge.api;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Abstract class for every merger implementation.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public abstract class AbstractMerger {
	/** {@link DiffElement} to be merged by this merger. */
	protected DiffElement diff;

	/**
	 * Sets the {@link DiffElement} to be merged.
	 * 
	 * @param element
	 *            The {@link DiffElement} to be merged.
	 */
	public void setElement(DiffElement element) {
		diff = element;
	}

	/**
	 * Cancels the modification in the target (right) model.
	 */
	public abstract void undoInTarget();

	/**
	 * Applies the modification in the original (left) model.
	 */
	public abstract void applyInOrigin();

	/**
	 * Returns <code>True</code> if the merger is allowed to apply changes in the origin (left) model.
	 * 
	 * @return <code>True</code> if the merger is allowed to apply changes in the origin (left) model, <code>False</code> otherwise.
	 */
	public abstract boolean canApplyInOrigin();

	/**
	 * Returns <code>True</code> if the merger is allowed to undo changes in the target (right) model.
	 * 
	 * @return <code>True</code> if the merger is allowed to undo changes in the target (right) model, <code>False</code> otherwise.
	 */
	public abstract boolean canUndoInTarget();

	/**
	 * Removes a {@link DiffElement} from its {@link DiffGroup}.
	 * 
	 * @param diffElement
	 *            {@link DiffElement} to remove from its container.
	 */
	protected void removeFromContainer(DiffElement diffElement) {
		final EObject parent = diffElement.eContainer();
		EcoreUtil.remove(diffElement);

		// now removes all the dangling references
		removeDanglingReferences(parent);

		// If diff was contained by a ConflictingDiffElement, we call back this on it
		if (parent instanceof ConflictingDiffElement)
			removeFromContainer((DiffElement)parent);
		
		// if diff was in a diffGroup and it was the last one, we also remove the diffgroup
		if (parent instanceof DiffGroup)
			cleanDiffGroup((DiffGroup)parent);
	}

	/**
	 * Removes all references to the given {@link EObject} from the {@link DiffModel}.
	 * 
	 * @param deletedObject
	 *            Object to remove all references to.
	 */
	protected void removeDanglingReferences(EObject deletedObject) {
		EObject root = EcoreUtil.getRootContainer(deletedObject);
		if (root instanceof ModelInputSnapshot)
			root = ((ModelInputSnapshot)root).getDiff();
		if (root != null) {
			EcoreUtil.CrossReferencer referencer = new EcoreUtil.CrossReferencer(root.eResource()) {
				private static final long serialVersionUID = 616050158241084372L;

				{
					crossReference();
				}

				@Override
				protected boolean crossReference(EObject eObject, EReference eReference, EObject crossReferencedEObject) {
					return crossReferencedEObject.eResource() == null;
				}
			};
			for (final Iterator i = referencer.entrySet().iterator(); i.hasNext(); ) {
				final Map.Entry entry = (Map.Entry)i.next();
				for (final Iterator j = ((List)entry.getValue()).iterator(); j.hasNext(); ) {
					EcoreUtil.remove((EStructuralFeature.Setting)j.next(), entry.getKey());
				}
			}
		}
	}

	/**
	 * Removes the given {@link DiffGroup} from its container if it was its last child, also calls for the same cleanup operation on its hierarchy.
	 * 
	 * @param diffGroup
	 *            {@link DiffGroup} we want to cleanup.
	 */
	protected void cleanDiffGroup(DiffGroup diffGroup) {
		if (diffGroup != null && diffGroup.getSubchanges() == 0) {
			final EObject parent = diffGroup.eContainer();
			if (parent != null && parent instanceof DiffGroup) {
				EcoreUtil.remove(diffGroup);
				cleanDiffGroup((DiffGroup)parent);
			}
		}
	}

	/**
	 * Returns the {@link DiffModel} containing the {@link DiffElement} this merger is intended to merge.
	 * 
	 * @return The {@link DiffModel} containing the {@link DiffElement} this merger is intended to merge.
	 */
	protected DiffModel getDiffModel() {
		return ((ModelInputSnapshot)EcoreUtil.getRootContainer(diff)).getDiff();
	}

	/**
	 * Returns the left resource.
	 * 
	 * @return The left resource.
	 */
	protected Resource findLeftResource() {
		Resource leftResource = null;
		final MatchModel match = ((ModelInputSnapshot)EcoreUtil.getRootContainer(diff)).getMatch();
		for (final Iterator matchIterator = match.getMatchedElements().iterator(); matchIterator.hasNext(); ) {
			final Match2Elements element = (Match2Elements)matchIterator.next();
			if (element.getLeftElement() != null) {
				leftResource = element.getLeftElement().eResource();
			}
		}
		return leftResource;
	}

	/**
	 * Returns the right resource.
	 * 
	 * @return The right resource.
	 */
	protected Resource findRightResource() {
		Resource rightResource = null;
		final MatchModel match = ((ModelInputSnapshot)EcoreUtil.getRootContainer(diff)).getMatch();
		for (final Iterator matchIterator = match.getMatchedElements().iterator(); matchIterator.hasNext(); ) {
			final Match2Elements element = (Match2Elements)matchIterator.next();
			if (element.getRightElement() != null) {
				rightResource = element.getRightElement().eResource();
			}
		}
		return rightResource;
	}
}

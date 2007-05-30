/*  
 * Copyright (c) 2006, Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.merge.api;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Abstract class for every merger
 * 
 * @author Cedric Brun  <a href="mailto:cedric.brun@obeo.fr ">cedric.brun@obeo.fr</a> 
 * 
 */
public class AbstractMerger {
	protected DiffElement diff = null;

	/**
	 * Create a merger from a delta
	 * 
	 * @param element
	 */
	public AbstractMerger(DiffElement element) {
		diff = element;
	}

	/**
	 * Undo the difference in the target (right) model
	 * 
	 */
	public void undoInTarget() {
		removeFromContainer(diff);

	}

	/**
	 * Apply the difference in the original (left) model
	 * 
	 */
	public void applyInOrigin() {
		removeFromContainer(diff);
	}

	/**
	 * Return true if one can apply the difference in the original model
	 * 
	 * @return true if the merger is able to apply in origin
	 */
	public boolean canApplyInOrigin() {
		return true;
	}

	/**
	 * Return true if one can undo the difference in the target model
	 * 
	 * @return true if the merger is able to undo in target
	 */
	public boolean canUndoInTarget() {
		return true;
	}

	/**
	 * Set the element to merge
	 */
	public void setDiffElement(DiffElement elem) {
		diff = elem;
	}

	protected void removeFromContainer(EObject obj) {

		EObject parent = obj.eContainer();
		EcoreUtil.remove(obj);

		// now removes all the dangling references
		for (Iterator i = new EcoreUtil.CrossReferencer(EcoreUtil
				.getRootContainer(parent).eResource()) {
			private static final long serialVersionUID = -6324609614260707598L;

			{
				crossReference();
			}

			protected boolean crossReference(EObject eObject,
					EReference eReference, EObject crossReferencedEObject) {
				return crossReferencedEObject.eResource() == null;
			}
		}.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			for (Iterator j = ((List) entry.getValue()).iterator(); j.hasNext();) {
				EcoreUtil.remove((EStructuralFeature.Setting) j.next(), entry
						.getKey());
			}
		}
	}

	protected void removeDanglingReferences(EObject deletedObject) {
		for (Iterator i = new EcoreUtil.CrossReferencer(EcoreUtil
				.getRootContainer(deletedObject).eResource()) {
			private static final long serialVersionUID = 769922667795187353L;

			{
				crossReference();
			}

			protected boolean crossReference(EObject eObject,
					EReference eReference, EObject crossReferencedEObject) {
				return crossReferencedEObject.eResource() == null;
			}
		}.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			for (Iterator j = ((List) entry.getValue()).iterator(); j.hasNext();) {
				EcoreUtil.remove((EStructuralFeature.Setting) j.next(), entry
						.getKey());
			}
		}
	}
}

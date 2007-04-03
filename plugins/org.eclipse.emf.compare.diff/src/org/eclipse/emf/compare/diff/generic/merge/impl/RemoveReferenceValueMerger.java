package org.eclipse.emf.compare.diff.generic.merge.impl;

import java.util.Iterator;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.diff.RemoveReferenceValue;
import org.eclipse.emf.compare.merge.api.AbstractMerger;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * Merger for a diff element
 * 
 * @author Cédric Brun <cedric.brun@obeo.fr>
 * 
 */
public class RemoveReferenceValueMerger extends AbstractMerger {

	public RemoveReferenceValueMerger(DiffElement element) {
		super(element);
	}

	public void applyInOrigin() {
		RemoveReferenceValue diff = (RemoveReferenceValue) this.diff;
		EObject element = diff.getLeftElement();
		// Iterator oldTarget = getReferencesOrigins().iterator();
		Iterator oldTarget = diff.getLeftRemovedTarget().iterator();
		while (oldTarget.hasNext()) {
			try {
				EFactory.eRemove(element, ((EReference) diff.getReference())
						.getName(), oldTarget.next());
			} catch (FactoryException e) {
				EMFComparePlugin.getDefault().log(e, true);
			}
		}

		super.applyInOrigin();

	}

	public boolean canApplyInOrigin() {
		return true;
	}

	public boolean canUndoInTarget() {
		return true;
	}

	public void undoInTarget() {
		RemoveReferenceValue diff = (RemoveReferenceValue) this.diff;
		EObject element = diff.getRightElement();
		if (canUndoInTarget()) {
			{
				Iterator newTarget = diff.getRightRemovedTarget().iterator();
				while (newTarget.hasNext()) {
					try {
						EFactory.eAdd(element, ((EReference) diff.getReference())
								.getName(), newTarget.next());
					} catch (FactoryException e) {
						EMFComparePlugin.getDefault().log(e, true);
					}
				}
			}
			super.undoInTarget();
		}
	}

}

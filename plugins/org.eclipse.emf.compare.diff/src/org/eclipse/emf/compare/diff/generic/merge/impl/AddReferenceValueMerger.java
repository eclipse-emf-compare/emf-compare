package org.eclipse.emf.compare.diff.generic.merge.impl;

import java.util.Iterator;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.AddReferenceValue;
import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.merge.api.AbstractMerger;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EObject;

/**
 * Merger for a diff element
 * 
 * @author Cedric Brun <cedric.brun@obeo.fr>
 * 
 */
public class AddReferenceValueMerger extends AbstractMerger {
	/**
	 * Constructs a merger
	 * 
	 * @param element :
	 *            the corresponding delta
	 */
	public AddReferenceValueMerger(DiffElement element) {
		super(element);
	}

	public void applyInOrigin() {
		AddReferenceValue diff = (AddReferenceValue) this.diff;
		EObject element = diff.getLeftElement();
		if (canApplyInOrigin()) {
			Iterator oldTarget = diff.getLeftAddedTarget().iterator();
			while (oldTarget.hasNext()) {
				try {
					EFactory.eAdd(element, (diff.getReference())
							.getName(), oldTarget.next());
				} catch (FactoryException e) {
					EMFComparePlugin.getDefault().log(e, true);
				}
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
		AddReferenceValue diff = (AddReferenceValue) this.diff;
		EObject element = diff.getRightElement();
		// Iterator newTarget = getReferencesTargets().iterator();
		Iterator newTarget = diff.getRightAddedTarget().iterator();
		while (newTarget.hasNext()) {
			try {
				EFactory.eRemove(element, ( diff.getReference())
						.getName(), newTarget.next());
			} catch (FactoryException e) {
				EMFComparePlugin.getDefault().log(e, true);
			}
		}
		super.undoInTarget();
	}

}

package org.eclipse.emf.compare.diff.generic.merge.impl;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.diff.UpdateAttribute;
import org.eclipse.emf.compare.merge.api.AbstractMerger;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * Merger for a diff element
 * 
 * @author Cedric Brun <cedric.brun@obeo.fr>
 * 
 */
public class UpdateAttributeMerger extends AbstractMerger {
	/**
	 * Constructs a merger
	 * 
	 * @param element :
	 *            the corresponding delta
	 */
	public UpdateAttributeMerger(DiffElement element) {
		super(element);
	}

	public void applyInOrigin() {
		UpdateAttribute diff = (UpdateAttribute) this.diff;
		EObject element = diff.getRightElement();
		EObject origin = diff.getLeftElement();
		EAttribute attr =  diff.getAttribute();
		try {
			EFactory.eSet(origin, attr.getName(), EFactory.eGet(element, attr
					.getName()));
		} catch (FactoryException e) {
			EMFComparePlugin.getDefault().log(e, true);
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
		UpdateAttribute diff = (UpdateAttribute) this.diff;
		EObject element = diff.getRightElement();
		EObject origin = diff.getLeftElement();
		EAttribute attr = diff.getAttribute();
		try {
			EFactory.eSet(element, attr.getName(), EFactory.eGet(origin, attr
					.getName()));
		} catch (FactoryException e) {
			EMFComparePlugin.getDefault().log(e, true);
		}
		super.undoInTarget();
	}

}

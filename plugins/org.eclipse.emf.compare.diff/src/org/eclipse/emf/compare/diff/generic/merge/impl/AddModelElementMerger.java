package org.eclipse.emf.compare.diff.generic.merge.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.AddModelElement;
import org.eclipse.emf.compare.diff.AddReferenceValue;
import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.diff.DiffModel;
import org.eclipse.emf.compare.merge.api.AbstractMerger;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Merger for a diff element
 * 
 * @author Cédric Brun <cedric.brun@obeo.fr>
 * 
 */
public class AddModelElementMerger extends AbstractMerger {

	public AddModelElementMerger(DiffElement element) {
		super(element);
	}

	public void applyInOrigin() {
		AddModelElement diff = (AddModelElement) this.diff;
		EObject origin = diff.getLeftParent();
		EObject element = diff.getRightElement();
		EObject newOne = EcoreUtil.copy(element);
		EReference ref = element.eContainmentFeature();
		try {
			EFactory.eAdd(origin, ref.getName(), newOne);
			EcoreUtil.getRootContainer(origin).eResource().save(new HashMap());
		} catch (FactoryException e) {
			EMFComparePlugin.getDefault().log(e, true);
		} catch (IOException e) {
			EMFComparePlugin.getDefault().log(e, true);
		}
		// we should now have a look for AddReferencesLinks needed this object
		DiffModel log = (DiffModel) diff.eContainer();
		Iterator siblings = log.eAllContents();
		while (siblings.hasNext()) {
			DiffElement op = (DiffElement) siblings.next();
			if (op instanceof AddReferenceValue) {
				AddReferenceValue link = (AddReferenceValue) op;
				// now if I'm in the target References I should put my copy in
				// the origin
				if (link.getRightAddedTarget().contains(element)) {

					link.getRightAddedTarget().add(newOne);
					try {
						EcoreUtil.getRootContainer(link).eResource().save(
								new HashMap());
					} catch (IOException e) {
						EMFComparePlugin.getDefault().log(e, true);
					}

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
		System.out.println("AddModelElementMerger.undoInTarget()");
		// getElement is the target element
		AddModelElement diff = (AddModelElement) this.diff;
		EObject element = diff.getRightElement();
		EObject parent = diff.getRightElement().eContainer();
		EcoreUtil.remove(element);
		// now removes all the dangling references
		removeDanglingReferences(parent);
		super.undoInTarget();

	}

}

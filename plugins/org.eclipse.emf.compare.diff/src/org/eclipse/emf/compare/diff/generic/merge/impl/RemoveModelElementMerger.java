package org.eclipse.emf.compare.diff.generic.merge.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.diff.DiffModel;
import org.eclipse.emf.compare.diff.RemoveModelElement;
import org.eclipse.emf.compare.diff.RemoveReferenceValue;
import org.eclipse.emf.compare.merge.api.AbstractMerger;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Merger for a diff element
 * 
 * @author Cedric Brun  cedric.brun@obeo.fr 
 * 
 */
public class RemoveModelElementMerger extends AbstractMerger {
	/**
	 * Constructs a merger
	 * 
	 * @param element :
	 *            the corresponding delta
	 */
	public RemoveModelElementMerger(DiffElement element) {
		super(element);
	}

	public void applyInOrigin() {
		RemoveModelElement diff = (RemoveModelElement) this.diff;
		EObject element = diff.getLeftElement();
		EObject parent = element.eContainer();
		EcoreUtil.remove(element);

		// now removes all the dangling references
		for (Iterator i = new EcoreUtil.CrossReferencer(EcoreUtil
				.getRootContainer(parent).eResource()) {
			private static final long serialVersionUID = 616050158241084372L;

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

		try {
			EcoreUtil.getRootContainer(parent).eResource().save(new HashMap());
		} catch (IOException e) {
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
		RemoveModelElement diff = (RemoveModelElement) this.diff;
		// we should copy the element to the Origin one.
		EObject origin = diff.getRightParent();
		EObject element = diff.getLeftElement();
		EObject newOne = EcoreUtil.copy(element);
		EReference ref = element.eContainmentFeature();
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
		DiffModel log = (DiffModel) diff.eContainer();
		Iterator siblings = log.eAllContents();
		while (siblings.hasNext()) {
			DiffElement op = (DiffElement) siblings.next();
			if (op instanceof RemoveReferenceValue) {
				RemoveReferenceValue link = (RemoveReferenceValue) op;
				// now if I'm in the target References I should put my copy in
				// the origin
				if (link.getLeftRemovedTarget().contains(element)) {
					link.getLeftRemovedTarget().add(newOne);
					try {
						EcoreUtil.getRootContainer(link).eResource().save(
								new HashMap());
					} catch (IOException e) {
						EMFComparePlugin.getDefault().log(e, true);
					}

				}

			}
		}
		super.undoInTarget();
	}

}

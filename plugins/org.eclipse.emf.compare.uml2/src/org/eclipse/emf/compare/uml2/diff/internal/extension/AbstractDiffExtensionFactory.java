/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.diff.internal.extension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.util.UMLUtil;

public abstract class AbstractDiffExtensionFactory implements IDiffExtensionFactory {

	private UML2DiffEngine fEngine;

	private EcoreUtil.CrossReferencer fCrossReferencer;

	protected AbstractDiffExtensionFactory(UML2DiffEngine engine, EcoreUtil.CrossReferencer crossReferencer) {
		fEngine = engine;
		fCrossReferencer = crossReferencer;
	}

	protected final UML2DiffEngine getEngine() {
		return fEngine;
	}

	protected final EcoreUtil.CrossReferencer getCrossReferencer() {
		return fCrossReferencer;
	}

	public DiffElement getParentDiff(DiffElement input) {
		return (DiffElement)input.eContainer();
	}

	protected final DiffGroup findOrCreateDiffGroup(DiffModel diffModel, EObject right) {
		DiffGroup referencingDiffGroup = firstReferencingDiffGroup(getCrossReferencer().get(right));
		if (referencingDiffGroup == null) {
			List<EObject> ancestors = ancestors(right);
			for (EObject ancestor : ancestors) {
				referencingDiffGroup = firstReferencingDiffGroup(getCrossReferencer().get(ancestor));
				if (referencingDiffGroup != null) {
					List<EObject> ancestorsAndSelf = new ArrayList<EObject>(ancestors);
					ancestorsAndSelf.add(0, right);
					referencingDiffGroup = createSubTreeOfDiffGroup(referencingDiffGroup, ancestorsAndSelf);
					break;
				}
			}

			if (referencingDiffGroup == null) {
				// check if it is in the roots
				if (diffModel.getRightRoots().contains(ancestors.get(ancestors.size() - 1))
						&& !diffModel.getOwnedElements().isEmpty()) {
					referencingDiffGroup = (DiffGroup)diffModel.getOwnedElements().get(0);
				} else {
					// their is no DiffGroup in the DiffModel that can contains sub diff group tree of the
					// given
					// right element. We add a diff group, directly to diffmodel
					referencingDiffGroup = DiffFactory.eINSTANCE.createDiffGroup();
					diffModel.getOwnedElements().add(referencingDiffGroup);
				}
			}
		}

		return referencingDiffGroup;
	}

	/**
	 * Creates sub tree of diffgroup; and return the deepest one
	 * 
	 * @param referencingDiffGroup
	 * @param ancestors
	 * @return
	 */
	private DiffGroup createSubTreeOfDiffGroup(DiffGroup referencingDiffGroup, List<EObject> ancestors) {
		// one diff group per ancestor with no referencing diff group
		List<EObject> subList = ancestors
				.subList(0, ancestors.indexOf(referencingDiffGroup.getRightParent()));

		// iterating on reverse order to create the deepest one (index = 0) as the last one.
		for (ListIterator<EObject> it = subList.listIterator(subList.size()); it.hasPrevious();) {
			EObject previous = it.previous();
			DiffGroup newGroup = DiffFactory.eINSTANCE.createDiffGroup();
			referencingDiffGroup.getSubDiffElements().add(newGroup);
			newGroup.setRightParent(previous);
			newGroup.setRemote(referencingDiffGroup.isRemote());
			referencingDiffGroup = newGroup;
		}

		return referencingDiffGroup;
	}

	private List<EObject> ancestors(EObject eObject) {
		List<EObject> ret = new ArrayList<EObject>();
		EObject eContainer = eObject.eContainer();
		while (eContainer != null) {
			ret.add(eContainer);
			eContainer = eContainer.eContainer();
		}

		return ret;
	}

	private final DiffGroup firstReferencingDiffGroup(Collection<Setting> settings) {
		if (settings != null) {
			for (Setting setting : settings) {
				EObject eObject = setting.getEObject();
				if (setting.getEStructuralFeature() == DiffPackage.Literals.DIFF_GROUP__RIGHT_PARENT
						&& eObject instanceof DiffGroup) {
					return (DiffGroup)eObject;
				}
			}
		}
		return null;

	}

	protected static final List<EObject> getInverseReferences(EObject lookup, EStructuralFeature inFeature) {
		return getInverseReferences(lookup, inFeature, alwaysTrue());
	}

	protected static final List<EObject> getInverseReferences(EObject lookup, EStructuralFeature inFeature,
			UMLPredicate<Setting> alwaysTrue) {
		List<EObject> ret = new ArrayList<EObject>();
		for (Setting setting : UMLUtil.getInverseReferences(lookup)) {
			if (setting.getEStructuralFeature() == inFeature) {
				ret.add(setting.getEObject());
			}
		}
		return ret;
	}

	protected static final List<EObject> getNonNavigableInverseReferences(EObject lookup,
			EStructuralFeature inFeature) {
		return getNonNavigableInverseReferences(lookup, inFeature, alwaysTrue());
	}

	protected static final List<EObject> getNonNavigableInverseReferences(EObject lookup,
			EStructuralFeature inFeature, UMLPredicate<Setting> alwaysTrue) {
		List<EObject> ret = new ArrayList<EObject>();
		for (Setting setting : UMLUtil.getNonNavigableInverseReferences(lookup)) {
			if (setting.getEStructuralFeature() == inFeature) {
				ret.add(setting.getEObject());
			}
		}
		return ret;
	}

	protected final List<DiffElement> findCrossReferences(EObject lookup, EStructuralFeature inFeature) {
		return findCrossReferences(lookup, inFeature, alwaysTrue());
	}

	protected final List<DiffElement> findCrossReferences(EObject lookup, EStructuralFeature inFeature,
			UMLPredicate<Setting> p) {
		List<DiffElement> ret = new ArrayList<DiffElement>();
		Collection<Setting> settings = getCrossReferencer().get(lookup);
		if (settings != null) {
			for (Setting setting : settings) {
				EStructuralFeature eStructuralFeature = setting.getEStructuralFeature();
				if (eStructuralFeature == inFeature && p.apply(setting)) {
					ret.add((DiffElement)setting.getEObject());
				}
			}
		}
		return ret;
	}

	protected final void hideCrossReferences(EObject lookup, EStructuralFeature inFeature,
			AbstractDiffExtension hiddingExtension, UMLPredicate<Setting> p) {
		for (DiffElement diffElement : findCrossReferences(lookup, inFeature, p)) {
			hiddingExtension.getHideElements().add(diffElement);
		}
	}

	protected final void hideCrossReferences(EObject lookup, EStructuralFeature inFeature,
			AbstractDiffExtension hiddingExtension) {
		hideCrossReferences(lookup, inFeature, hiddingExtension, alwaysTrue());
	}

	protected static interface UMLPredicate<T> {
		boolean apply(T input);
	}

	@SuppressWarnings("unchecked")
	private static final UMLPredicate<Setting> alwaysTrue() {
		return (UMLPredicate<Setting>)ALWAYS_TRUE; // predicate works for all T
	}

	private static final UMLPredicate<?> ALWAYS_TRUE = new UMLPredicate<Object>() {
		public boolean apply(Object input) {
			return true;
		}
	};
}

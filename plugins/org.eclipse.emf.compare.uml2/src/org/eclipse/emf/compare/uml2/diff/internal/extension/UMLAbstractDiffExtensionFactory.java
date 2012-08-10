/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.diff.internal.extension;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.UMLComparePackage;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
import org.eclipse.uml2.common.util.UML2Util;

/**
 * Factory for the difference extensions.
 */
public abstract class UMLAbstractDiffExtensionFactory extends AbstractDiffExtensionFactory {

	private final UMLPredicate<Setting> REFINING_PREDICATE = new UMLPredicate<EStructuralFeature.Setting>() {
		public boolean apply(Setting input) {
			final Diff diff = (Diff)input.getEObject();
			return isPartOfRefiningDifference(diff);
		}
	};

	private final UMLPredicate<Setting> REQUIRES_ADD_DISCRIMINANT_PREDICATE = new UMLPredicate<EStructuralFeature.Setting>() {
		public boolean apply(Setting input) {
			return getExtensionKind().isInstance(input.getEObject())
					&& ((UMLDiff)input.getEObject()).getKind().equals(DifferenceKind.ADD);
		}
	};

	private final UMLPredicate<Setting> REQUIRES_CHANGE_DISCRIMINANT_PREDICATE = new UMLPredicate<EStructuralFeature.Setting>() {
		public boolean apply(Setting input) {
			return getExtensionKind().isInstance(input.getEObject())
					&& ((UMLDiff)input.getEObject()).getKind().equals(DifferenceKind.CHANGE);
		}
	};

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean handles(Diff input) {
		return (getRelatedExtensionKind(input) != null) && !isExtensionAlreadyExist(input);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#create(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	public Diff create(Diff input, EcoreUtil.CrossReferencer crossReferencer) {

		final UMLDiff ret = createExtension();

		final EObject discriminant = getDiscriminantFromDiff(input);

		final DifferenceKind extensionKind = getRelatedExtensionKind(input);

		if (discriminant != null) {
			if (extensionKind == DifferenceKind.DELETE) {
				ret.getRefinedBy().add(input);
			} else {
				fillRefiningDifferences(crossReferencer, ret, discriminant);
			}
		}

		ret.setDiscriminant(discriminant);
		ret.setKind(extensionKind);
		if (extensionKind == DifferenceKind.ADD || extensionKind == DifferenceKind.DELETE) {
			ret.setEReference(((ReferenceChange)input).getReference());
		}

		registerUMLDiff(crossReferencer, ret, UMLComparePackage.Literals.UML_DIFF__DISCRIMINANT, discriminant);

		return ret;
	}

	protected void fillRefiningDifferences(EcoreUtil.CrossReferencer crossReferencer,
			final Diff diffExtension, final EObject discriminant) {
		for (EObject elt : getPotentialChangedValuesFromDiscriminant(discriminant)) {
			beRefinedByCrossReferences(elt, ComparePackage.Literals.REFERENCE_CHANGE__VALUE, diffExtension,
					REFINING_PREDICATE, crossReferencer);
		}
	}

	@Override
	public void fillRequiredDifferences(UMLDiff extension, CrossReferencer crossReferencer) {
		if (getExtensionKind().isInstance(extension)) {
			if (extension.getKind().equals(DifferenceKind.CHANGE)) {
				extension.getRequires().addAll(
						findCrossReferences(extension.getDiscriminant(),
								UMLComparePackage.Literals.UML_DIFF__DISCRIMINANT,
								REQUIRES_ADD_DISCRIMINANT_PREDICATE, crossReferencer));
			} else if (extension.getKind().equals(DifferenceKind.DELETE)) {
				extension.getRequires().addAll(
						findCrossReferences(extension.getDiscriminant(),
								UMLComparePackage.Literals.UML_DIFF__DISCRIMINANT,
								REQUIRES_CHANGE_DISCRIMINANT_PREDICATE, crossReferencer));
			}
		}
	}

	@Override
	public Match getParentMatch(Diff input, CrossReferencer crossReferencer) {
		if (getRelatedExtensionKind(input) == DifferenceKind.CHANGE && getDiscriminantFromDiff(input) != null) {
			return (Match)input.getMatch().getComparison().getMatch(getDiscriminantFromDiff(input))
					.eContainer();
		}
		return super.getParentMatch(input, crossReferencer);
	}

	protected abstract UMLDiff createExtension();

	protected boolean isPartOfRefiningDifference(Diff diff) {
		return diff instanceof ReferenceChange && getRelatedExtensionKind(diff) == DifferenceKind.CHANGE;
	}

	private boolean isExtensionAlreadyExist(Diff input) {
		for (Diff diff : input.getRefines()) {
			if (diff instanceof UMLDiff
					&& ((UMLDiff)diff).getDiscriminant().equals(getDiscriminantFromDiff(input))) {
				return true;
			}

		}
		return false;
	}

	protected abstract EObject getDiscriminantFromDiff(Diff input);

	protected abstract List<EObject> getPotentialChangedValuesFromDiscriminant(EObject discriminant);

	protected abstract DifferenceKind getRelatedExtensionKind(Diff input);

	protected Setting getInverseReferences(EObject object, Predicate<EStructuralFeature.Setting> predicate) {
		final Iterator<EStructuralFeature.Setting> crossReferences = UML2Util.getInverseReferences(object)
				.iterator();
		try {
			return Iterators.find(crossReferences, predicate);
		} catch (Exception e) {
			return null;
		}
	}

}

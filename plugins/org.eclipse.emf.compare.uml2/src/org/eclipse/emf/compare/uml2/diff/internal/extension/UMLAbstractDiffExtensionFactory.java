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

import java.util.List;

import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2diff.UMLExtension;
import org.eclipse.emf.compare.uml2diff.Uml2diffPackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;

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
					&& ((UMLExtension)input.getEObject()).getKind().equals(DifferenceKind.ADD);
		}
	};

	private final UMLPredicate<Setting> REQUIRES_CHANGE_DISCRIMINANT_PREDICATE = new UMLPredicate<EStructuralFeature.Setting>() {
		public boolean apply(Setting input) {
			return getExtensionKind().isInstance(input.getEObject())
					&& ((UMLExtension)input.getEObject()).getKind().equals(DifferenceKind.CHANGE);
		}
	};

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean handles(Diff input) {
		return (isRelatedToAnExtensionChange(input) || isRelatedToAnExtensionAdd(input) || isRelatedToAnExtensionDelete(input))
				&& !isExtensionAlreadyExist(input);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#create(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	public Diff create(Diff input, EcoreUtil.CrossReferencer crossReferencer) {

		final UMLExtension ret = createExtension();

		final EObject discriminant = getDiscriminantFromDiff(input);

		if (discriminant != null) {
			if (isRelatedToAnExtensionDelete(input)) {
				ret.getRefinedBy().add(input);
			} else {
				fillRefiningDifferences(crossReferencer, ret, discriminant);
			}
		}

		ret.setDiscriminant(discriminant);
		if (isRelatedToAnExtensionAdd(input)) {
			ret.setKind(DifferenceKind.ADD);
		} else if (isRelatedToAnExtensionDelete(input)) {
			ret.setKind(DifferenceKind.DELETE);
		} else if (isRelatedToAnExtensionChange(input)) {
			ret.setKind(DifferenceKind.CHANGE);
		}

		registerUMLExtension(crossReferencer, ret, Uml2diffPackage.Literals.UML_EXTENSION__DISCRIMINANT,
				discriminant);

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
	public void fillRequiredDifferences(UMLExtension extension, CrossReferencer crossReferencer) {
		if (getExtensionKind().isInstance(extension)) {
			if (extension.getKind().equals(DifferenceKind.CHANGE)) {
				extension.getRequires().addAll(
						findCrossReferences(extension.getDiscriminant(),
								Uml2diffPackage.Literals.UML_EXTENSION__DISCRIMINANT,
								REQUIRES_ADD_DISCRIMINANT_PREDICATE, crossReferencer));
			} else if (extension.getKind().equals(DifferenceKind.DELETE)) {
				extension.getRequires().addAll(
						findCrossReferences(extension.getDiscriminant(),
								Uml2diffPackage.Literals.UML_EXTENSION__DISCRIMINANT,
								REQUIRES_CHANGE_DISCRIMINANT_PREDICATE, crossReferencer));
			}
		}
	}

	protected abstract UMLExtension createExtension();

	protected boolean isPartOfRefiningDifference(Diff diff) {
		return diff instanceof ReferenceChange && isRelatedToAnExtensionChange(diff);
	}

	private boolean isExtensionAlreadyExist(Diff input) {
		for (Diff diff : input.getRefines()) {
			if (diff instanceof UMLExtension
					&& ((UMLExtension)diff).getDiscriminant().equals(getDiscriminantFromDiff(input))) {
				return true;
			}

		}
		return false;
	}

	protected abstract EObject getDiscriminantFromDiff(Diff input);

	protected abstract List<EObject> getPotentialChangedValuesFromDiscriminant(EObject discriminant);

	protected abstract boolean isRelatedToAnExtensionChange(Diff input);

	protected abstract boolean isRelatedToAnExtensionAdd(Diff input);

	protected abstract boolean isRelatedToAnExtensionDelete(Diff input);

}

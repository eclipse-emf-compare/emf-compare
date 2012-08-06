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
package org.eclipse.emf.compare.uml2.diff.internal.extension.usecase;

import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UMLExtendChange;
import org.eclipse.emf.compare.uml2diff.UMLExtension;
import org.eclipse.emf.compare.uml2diff.Uml2diffFactory;
import org.eclipse.emf.compare.uml2diff.Uml2diffPackage;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.ExtensionPoint;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Factory for UMLExtendChangeLeftTarget.
 */
public class UMLExtendChangeFactory extends AbstractDiffExtensionFactory {

	/**
	 * The predicate to hide difference elements.
	 */
	private static final UMLPredicate<Setting> REFINING_PREDICATE = new UMLPredicate<EStructuralFeature.Setting>() {
		public boolean apply(Setting input) {
			final ReferenceChange referenceChange = (ReferenceChange)input.getEObject();
			return isChangeExtend(referenceChange);
		}
	};

	private static final UMLPredicate<Setting> REQUIRES_ADD_EXTEND_PREDICATE = new UMLPredicate<EStructuralFeature.Setting>() {
		public boolean apply(Setting input) {
			return input.getEObject() instanceof UMLExtendChange
					&& ((UMLExtendChange)input.getEObject()).getKind().equals(DifferenceKind.ADD);
		}
	};

	private static final UMLPredicate<Setting> REQUIRES_CHANGE_EXTEND_PREDICATE = new UMLPredicate<EStructuralFeature.Setting>() {
		public boolean apply(Setting input) {
			return input.getEObject() instanceof UMLExtendChange
					&& ((UMLExtendChange)input.getEObject()).getKind().equals(DifferenceKind.CHANGE);
		}
	};

	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            The UML2 difference engine.
	 */
	public UMLExtendChangeFactory() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean handles(Diff input) {
		return (isAddExtend(input) || isDeleteExtend(input) || isChangeExtend(input))
				&& !isAlreadyExist(input);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#create(org.eclipse.emf.compare.diff.metamodel.DiffElement,
	 *      org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer)
	 */
	public Diff create(Diff input, EcoreUtil.CrossReferencer crossReferencer) {
		final ReferenceChange referenceChange = (ReferenceChange)input;

		final UMLExtendChange ret = Uml2diffFactory.eINSTANCE.createUMLExtendChange();

		final Extend extend = getExtend(referenceChange);

		if (extend != null) {
			if (isDeleteExtend(input)) {
				ret.getRefinedBy().add(input);
			} else {
				fillRefiningDifferences(crossReferencer, ret, extend);
			}
		}

		ret.setExtend(extend);
		if (isAddExtend(input)) {
			ret.setKind(DifferenceKind.ADD);
		} else if (isDeleteExtend(input)) {
			ret.setKind(DifferenceKind.DELETE);
		} else if (isChangeExtend(input)) {
			ret.setKind(DifferenceKind.CHANGE);
		}

		registerUMLExtension(crossReferencer, ret, Uml2diffPackage.Literals.UML_EXTEND_CHANGE__EXTEND, extend);

		return ret;
	}

	private void fillRefiningDifferences(EcoreUtil.CrossReferencer crossReferencer,
			final UMLExtendChange ret, final Extend extend) {

		beRefinedByCrossReferences(extend.getExtendedCase(), ComparePackage.Literals.REFERENCE_CHANGE__VALUE,
				ret, REFINING_PREDICATE, crossReferencer);

		for (ExtensionPoint extension : extend.getExtensionLocations()) {
			beRefinedByCrossReferences(extension, ComparePackage.Literals.REFERENCE_CHANGE__VALUE, ret,
					REFINING_PREDICATE, crossReferencer);
		}
	}

	@Override
	public void fillRequiredDifferences(UMLExtension diff, CrossReferencer crossReferencer) {
		if (diff instanceof UMLExtendChange && diff.getKind().equals(DifferenceKind.CHANGE)) {
			diff.getRequires().addAll(
					findCrossReferences(((UMLExtendChange)diff).getExtend(),
							Uml2diffPackage.Literals.UML_EXTEND_CHANGE__EXTEND,
							REQUIRES_ADD_EXTEND_PREDICATE, crossReferencer));
		} else if (diff instanceof UMLExtendChange && diff.getKind().equals(DifferenceKind.DELETE)) {
			diff.getRequires().addAll(
					findCrossReferences(((UMLExtendChange)diff).getExtend(),
							Uml2diffPackage.Literals.UML_EXTEND_CHANGE__EXTEND,
							REQUIRES_CHANGE_EXTEND_PREDICATE, crossReferencer));
		}
	}

	private Extend getExtend(final ReferenceChange referenceChange) {
		Extend extend = null;
		if (isAddExtend(referenceChange) || isDeleteExtend(referenceChange)) {
			extend = (Extend)referenceChange.getValue();
		} else if (isChangeExtend(referenceChange)) {
			final EObject container = MatchUtil.getContainer(MatchUtil.getComparison(referenceChange),
					referenceChange);
			if (container instanceof Extend) {
				extend = (Extend)container;
			}
		}
		return extend;
	}

	private static boolean isAddExtend(Diff input) {
		return input instanceof ReferenceChange && ((ReferenceChange)input).getReference().isContainment()
				&& input.getKind().equals(DifferenceKind.ADD)
				&& ((ReferenceChange)input).getValue() instanceof Extend
				&& ((Extend)((ReferenceChange)input).getValue()).getExtendedCase() != null
				&& ((Extend)((ReferenceChange)input).getValue()).getExtensionLocations() != null
				&& !((Extend)((ReferenceChange)input).getValue()).getExtensionLocations().isEmpty();
	}

	private static boolean isDeleteExtend(Diff input) {
		return input instanceof ReferenceChange && ((ReferenceChange)input).getReference().isContainment()
				&& input.getKind().equals(DifferenceKind.DELETE)
				&& ((ReferenceChange)input).getValue() instanceof Extend;
	}

	private static boolean isChangeExtend(Diff input) {
		return (input instanceof ReferenceChange && (((ReferenceChange)input).getReference().equals(
				UMLPackage.Literals.EXTEND__EXTENDED_CASE) || ((ReferenceChange)input).getReference().equals(
				UMLPackage.Literals.EXTEND__EXTENSION_LOCATION)));
	}

	private boolean isAlreadyExist(Diff input) {
		if (input instanceof ReferenceChange) {
			for (Diff diff : input.getRefines()) {
				if (diff instanceof UMLExtendChange
						&& ((UMLExtendChange)diff).getExtend().equals(getExtend((ReferenceChange)input))) {
					return true;
				}
			}
		}
		return false;
	}

	public Class<? extends UMLExtension> getExtensionKind() {
		return UMLExtendChange.class;
	}
}

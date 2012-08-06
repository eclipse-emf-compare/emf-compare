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
package org.eclipse.emf.compare.uml2.diff.internal.extension.clazz;

import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UMLAssociationChange;
import org.eclipse.emf.compare.uml2diff.UMLExtension;
import org.eclipse.emf.compare.uml2diff.Uml2diffFactory;
import org.eclipse.emf.compare.uml2diff.Uml2diffPackage;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Factory for UMLAssociationChangeLeftTarget.
 */
public class UMLAssociationChangeFactory extends AbstractDiffExtensionFactory {

	/**
	 * The predicate to hide difference elements.
	 */
	private static final UMLPredicate<Setting> REFINING_PREDICATE = new UMLPredicate<EStructuralFeature.Setting>() {
		public boolean apply(Setting input) {
			final ReferenceChange referenceChange = (ReferenceChange)input.getEObject();
			return isChangeAssociation(referenceChange);
		}
	};

	private static final UMLPredicate<Setting> REQUIRES_ADD_ASSOCIATION_PREDICATE = new UMLPredicate<EStructuralFeature.Setting>() {
		public boolean apply(Setting input) {
			return input.getEObject() instanceof UMLAssociationChange
					&& ((UMLAssociationChange)input.getEObject()).getKind().equals(DifferenceKind.ADD);
		}
	};

	private static final UMLPredicate<Setting> REQUIRES_CHANGE_ASSOCIATION_PREDICATE = new UMLPredicate<EStructuralFeature.Setting>() {
		public boolean apply(Setting input) {
			return input.getEObject() instanceof UMLAssociationChange
					&& ((UMLAssociationChange)input.getEObject()).getKind().equals(DifferenceKind.CHANGE);
		}
	};

	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            The UML2 difference engine.
	 */
	public UMLAssociationChangeFactory() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean handles(Diff input) {
		return (isAddAssociation(input) || isDeleteAssociation(input) || isChangeAssociation(input))
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

		final UMLAssociationChange ret = Uml2diffFactory.eINSTANCE.createUMLAssociationChange();

		final Association association = getAssociation(referenceChange);

		if (association != null) {
			if (isDeleteAssociation(input)) {
				ret.getRefinedBy().add(input);
			} else {
				fillRefiningDifferences(crossReferencer, ret, association);
			}
		}

		ret.setAssociation(association);
		if (isAddAssociation(input)) {
			ret.setKind(DifferenceKind.ADD);
		} else if (isDeleteAssociation(input)) {
			ret.setKind(DifferenceKind.DELETE);
		} else if (isChangeAssociation(input)) {
			ret.setKind(DifferenceKind.CHANGE);
		}

		registerUMLExtension(crossReferencer, ret,
				Uml2diffPackage.Literals.UML_ASSOCIATION_CHANGE__ASSOCIATION, association);

		return ret;
	}

	private void fillRefiningDifferences(EcoreUtil.CrossReferencer crossReferencer,
			final UMLAssociationChange ret, final Association association) {
		beRefinedByCrossReferences(association, ComparePackage.Literals.REFERENCE_CHANGE__VALUE, ret,
				REFINING_PREDICATE, crossReferencer);
		for (Type type : association.getEndTypes()) {
			beRefinedByCrossReferences(type, ComparePackage.Literals.REFERENCE_CHANGE__VALUE, ret,
					REFINING_PREDICATE, crossReferencer);
		}
		for (Property property : association.getMemberEnds()) {
			if (property.getLowerValue() != null) {
				beRefinedByCrossReferences(property.getLowerValue(),
						ComparePackage.Literals.REFERENCE_CHANGE__VALUE, ret, REFINING_PREDICATE,
						crossReferencer);
			}
			if (property.getUpperValue() != null) {
				beRefinedByCrossReferences(property.getUpperValue(),
						ComparePackage.Literals.REFERENCE_CHANGE__VALUE, ret, REFINING_PREDICATE,
						crossReferencer);
			}
		}
	}

	private Association getAssociation(final ReferenceChange referenceChange) {
		Association association = null;
		if (isAddAssociation(referenceChange) || isDeleteAssociation(referenceChange)) {
			association = (Association)referenceChange.getValue();
		} else if (isChangeAssociation(referenceChange)) {
			final EObject container = MatchUtil.getContainer(MatchUtil.getComparison(referenceChange),
					referenceChange);
			if (container instanceof Property) {
				association = ((Property)container).getAssociation();
			}
		}
		return association;
	}

	private static boolean isAddAssociation(Diff input) {
		return input instanceof ReferenceChange && ((ReferenceChange)input).getReference().isContainment()
				&& input.getKind().equals(DifferenceKind.ADD)
				&& ((ReferenceChange)input).getValue() instanceof Association
				&& ((Association)((ReferenceChange)input).getValue()).getEndTypes() != null
				&& ((Association)((ReferenceChange)input).getValue()).getEndTypes().size() > 1;
	}

	private static boolean isDeleteAssociation(Diff input) {
		return input instanceof ReferenceChange && ((ReferenceChange)input).getReference().isContainment()
				&& input.getKind().equals(DifferenceKind.DELETE)
				&& ((ReferenceChange)input).getValue() instanceof Association;
	}

	private static boolean isChangeAssociation(Diff input) {
		final EObject diffContainer = MatchUtil.getContainer(MatchUtil.getComparison(input),
				(ReferenceChange)input);
		return input instanceof ReferenceChange
				&& (isAssociationPropertyChange(input, diffContainer) || isAssociationPropertyCardinalityChange(
						input, diffContainer));
	}

	private static boolean isAssociationPropertyChange(Diff input, EObject diffContainer) {
		return ((ReferenceChange)input).getReference().equals(UMLPackage.Literals.TYPED_ELEMENT__TYPE)
				&& diffContainer instanceof Property && ((Property)diffContainer).getAssociation() != null
				|| ((ReferenceChange)input).getReference().equals(UMLPackage.Literals.PROPERTY__ASSOCIATION);
	}

	private static boolean isAssociationPropertyCardinalityChange(Diff input, EObject diffContainer) {
		return (((ReferenceChange)input).getReference().equals(
				UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE) || ((ReferenceChange)input)
				.getReference().equals(UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE))
				&& diffContainer instanceof Property && ((Property)diffContainer).getAssociation() != null;
	}

	private boolean isAlreadyExist(Diff input) {
		if (input instanceof ReferenceChange) {
			for (Diff diff : input.getRefines()) {
				if (diff instanceof UMLAssociationChange
						&& ((UMLAssociationChange)diff).getAssociation().equals(
								getAssociation((ReferenceChange)input))) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void fillRequiredDifferences(UMLExtension diff, CrossReferencer crossReferencer) {
		if (diff instanceof UMLAssociationChange && diff.getKind().equals(DifferenceKind.CHANGE)) {
			diff.getRequires().addAll(
					findCrossReferences(((UMLAssociationChange)diff).getAssociation(),
							Uml2diffPackage.Literals.UML_ASSOCIATION_CHANGE__ASSOCIATION,
							REQUIRES_ADD_ASSOCIATION_PREDICATE, crossReferencer));
		} else if (diff instanceof UMLAssociationChange && diff.getKind().equals(DifferenceKind.DELETE)) {
			diff.getRequires().addAll(
					findCrossReferences(((UMLAssociationChange)diff).getAssociation(),
							Uml2diffPackage.Literals.UML_ASSOCIATION_CHANGE__ASSOCIATION,
							REQUIRES_CHANGE_ASSOCIATION_PREDICATE, crossReferencer));
		}
	}

	public Class<? extends UMLExtension> getExtensionKind() {
		return UMLAssociationChange.class;
	}
}

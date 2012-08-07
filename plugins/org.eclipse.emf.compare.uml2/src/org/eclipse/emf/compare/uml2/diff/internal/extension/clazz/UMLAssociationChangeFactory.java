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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.diff.internal.extension.UMLAbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UMLAssociationChange;
import org.eclipse.emf.compare.uml2diff.UMLExtension;
import org.eclipse.emf.compare.uml2diff.Uml2diffFactory;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Factory for UMLAssociationChangeLeftTarget.
 */
public class UMLAssociationChangeFactory extends UMLAbstractDiffExtensionFactory {

	public Class<? extends UMLExtension> getExtensionKind() {
		return UMLAssociationChange.class;
	}

	@Override
	protected UMLExtension createExtension() {
		return Uml2diffFactory.eINSTANCE.createUMLAssociationChange();
	}

	@Override
	protected EObject getDiscriminantFromDiff(Diff input) {
		EObject result = null;
		final DifferenceKind kind = getRelatedExtensionKind(input);
		if (kind == DifferenceKind.ADD || kind == DifferenceKind.DELETE) {
			result = ((ReferenceChange)input).getValue();
		} else if (kind == DifferenceKind.CHANGE) {
			final EObject container = MatchUtil.getContainer(input.getMatch().getComparison(), input);
			if (container instanceof Property) {
				result = ((Property)container).getAssociation();
			}
		}
		return result;
	}

	@Override
	protected List<EObject> getPotentialChangedValuesFromDiscriminant(EObject discriminant) {
		List<EObject> result = new ArrayList<EObject>();
		if (discriminant instanceof Association) {
			result.add(discriminant);
			result.addAll(((Association)discriminant).getEndTypes());
			for (Property property : ((Association)discriminant).getMemberEnds()) {
				if (property.getLowerValue() != null) {
					result.add(property.getLowerValue());
				}
				if (property.getUpperValue() != null) {
					result.add(property.getUpperValue());
				}
			}
		}
		return result;
	}

	@Override
	protected DifferenceKind getRelatedExtensionKind(Diff input) {
		if (input instanceof ReferenceChange) {
			if (isRelatedToAnExtensionChange((ReferenceChange)input)) {
				return DifferenceKind.CHANGE;
			} else if (isRelatedToAnExtensionAdd((ReferenceChange)input)) {
				return DifferenceKind.ADD;
			} else if (isRelatedToAnExtensionDelete((ReferenceChange)input)) {
				return DifferenceKind.DELETE;
			}
		}
		return null;
	}

	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		return input.getReference().isContainment() && input.getKind().equals(DifferenceKind.ADD)
				&& input.getValue() instanceof Association
				&& ((Association)input.getValue()).getEndTypes() != null
				&& ((Association)input.getValue()).getEndTypes().size() > 1;
	}

	protected static boolean isRelatedToAnExtensionDelete(ReferenceChange input) {
		return input.getReference().isContainment() && input.getKind().equals(DifferenceKind.DELETE)
				&& input.getValue() instanceof Association;
	}

	private static boolean isRelatedToAnExtensionChange(ReferenceChange input) {
		final EObject diffContainer = MatchUtil.getContainer(input.getMatch().getComparison(), input);
		return isAssociationPropertyChange(input, diffContainer)
				|| isAssociationPropertyCardinalityChange(input, diffContainer);
	}

	private static boolean isAssociationPropertyChange(ReferenceChange input, EObject diffContainer) {
		return input.getReference().equals(UMLPackage.Literals.TYPED_ELEMENT__TYPE)
				&& diffContainer instanceof Property && ((Property)diffContainer).getAssociation() != null
				|| input.getReference().equals(UMLPackage.Literals.PROPERTY__ASSOCIATION);
	}

	private static boolean isAssociationPropertyCardinalityChange(ReferenceChange input, EObject diffContainer) {
		return (input.getReference().equals(UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE) || input
				.getReference().equals(UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE))
				&& diffContainer instanceof Property && ((Property)diffContainer).getAssociation() != null;
	}

}

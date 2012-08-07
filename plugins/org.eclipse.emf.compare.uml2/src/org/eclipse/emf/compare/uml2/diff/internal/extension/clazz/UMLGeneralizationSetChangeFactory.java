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
import org.eclipse.emf.compare.uml2.GeneralizationSetChange;
import org.eclipse.emf.compare.uml2.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.compare.uml2.diff.internal.extension.UMLAbstractDiffExtensionFactory;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.GeneralizationSet;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Factory for UMLGeneralizationSetChangeLeftTarget.
 */
public class UMLGeneralizationSetChangeFactory extends UMLAbstractDiffExtensionFactory {

	public Class<? extends UMLDiff> getExtensionKind() {
		return GeneralizationSetChange.class;
	}

	@Override
	protected UMLDiff createExtension() {
		return UMLCompareFactory.eINSTANCE.createGeneralizationSetChange();
	}

	@Override
	protected EObject getDiscriminantFromDiff(Diff input) {
		EObject result = null;
		final DifferenceKind kind = getRelatedExtensionKind(input);
		if (kind == DifferenceKind.ADD || kind == DifferenceKind.DELETE) {
			result = ((ReferenceChange)input).getValue();
		} else if (kind == DifferenceKind.CHANGE) {
			final EObject container = MatchUtil.getContainer(input.getMatch().getComparison(), input);
			if (container instanceof GeneralizationSet) {
				result = container;
			}
		}
		return result;
	}

	@Override
	protected List<EObject> getPotentialChangedValuesFromDiscriminant(EObject discriminant) {
		List<EObject> result = new ArrayList<EObject>();
		if (discriminant instanceof GeneralizationSet) {
			result.addAll(((GeneralizationSet)discriminant).getGeneralizations());
		}
		return result;
	}

	@Override
	protected DifferenceKind getRelatedExtensionKind(Diff input) {
		if (input instanceof ReferenceChange) {
			if (isChangeGeneralizationSet((ReferenceChange)input)) {
				return DifferenceKind.CHANGE;
			} else if (isAddGeneralizationSet((ReferenceChange)input)) {
				return DifferenceKind.ADD;
			} else if (isDeleteGeneralizationSet((ReferenceChange)input)) {
				return DifferenceKind.DELETE;
			}
		}
		return null;
	}

	protected boolean isAddGeneralizationSet(ReferenceChange input) {
		return input.getReference().isContainment() && input.getKind().equals(DifferenceKind.ADD)
				&& input.getValue() instanceof GeneralizationSet
				&& ((GeneralizationSet)input.getValue()).getGeneralizations() != null
				&& !((GeneralizationSet)input.getValue()).getGeneralizations().isEmpty();
	}

	protected boolean isDeleteGeneralizationSet(ReferenceChange input) {
		return input.getReference().isContainment() && input.getKind().equals(DifferenceKind.DELETE)
				&& input.getValue() instanceof GeneralizationSet;
	}

	protected boolean isChangeGeneralizationSet(ReferenceChange input) {
		return input.getReference().equals(UMLPackage.Literals.GENERALIZATION_SET__GENERALIZATION);
	}

}

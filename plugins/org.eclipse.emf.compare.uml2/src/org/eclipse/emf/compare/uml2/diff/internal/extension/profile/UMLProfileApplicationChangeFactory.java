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
package org.eclipse.emf.compare.uml2.diff.internal.extension.profile;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.ProfileApplicationChange;
import org.eclipse.emf.compare.uml2.UMLCompareFactory;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.compare.uml2.diff.internal.extension.UMLAbstractDiffExtensionFactory;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.impl.ProfileApplicationImpl;

/**
 * Factory for UMLProfileApplicationAddition.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class UMLProfileApplicationChangeFactory extends UMLAbstractDiffExtensionFactory {

	public Class<? extends UMLDiff> getExtensionKind() {
		return ProfileApplicationChange.class;
	}

	@Override
	protected UMLDiff createExtension() {
		return UMLCompareFactory.eINSTANCE.createProfileApplicationChange();
	}

	@Override
	protected EObject getDiscriminantFromDiff(Diff input) {
		EObject result = null;
		final DifferenceKind kind = getRelatedExtensionKind(input);
		if (kind == DifferenceKind.ADD || kind == DifferenceKind.DELETE) {
			return ((ReferenceChange)input).getValue();
		} else if (kind == DifferenceKind.CHANGE) {
			final EObject container = MatchUtil.getContainer(input.getMatch().getComparison(), input);
			return getDiscriminantForChanges(container);
		}
		return result;
	}

	private ProfileApplication getDiscriminantForChanges(EObject container) {
		// FIXME Doit gérer une liste de ProfileApplications et choisir le bon.
		if (container instanceof ProfileApplication) {
			return (ProfileApplication)container;
		} else if (container instanceof EAnnotation && container.eContainer() instanceof ProfileApplication) {
			return (ProfileApplication)container.eContainer();
		}
		// } else if (container instanceof Profile) {
		// final Setting setting = getInverseReferences(container,
		// new Predicate<EStructuralFeature.Setting>() {
		// public boolean apply(EStructuralFeature.Setting currentSetting) {
		// return currentSetting.getEStructuralFeature() ==
		// UMLPackage.Literals.PROFILE_APPLICATION__APPLIED_PROFILE;
		// }
		// });
		// return (ProfileApplication)setting.getEObject();
		// } else if (container instanceof EPackage) {
		// final Setting setting = getInverseReferences(container,
		// new Predicate<EStructuralFeature.Setting>() {
		// public boolean apply(EStructuralFeature.Setting currentSetting) {
		// return currentSetting.getEStructuralFeature() == EcorePackage.Literals.EANNOTATION__REFERENCES
		// && currentSetting.getEObject().eContainer() instanceof ProfileApplication;
		// }
		// });
		// return (ProfileApplication)((EAnnotation)setting.getEObject()).eContainer();
		// }
		return null;
	}

	@Override
	protected List<EObject> getPotentialChangedValuesFromDiscriminant(EObject discriminant) {
		List<EObject> result = new ArrayList<EObject>();
		if (discriminant instanceof ProfileApplicationImpl) {
			result.add(((ProfileApplicationImpl)discriminant).basicGetAppliedProfile());
			for (EAnnotation annotation : (List<? extends EAnnotation>)ReferenceUtil.getAsList(discriminant,
					EcorePackage.Literals.EMODEL_ELEMENT__EANNOTATIONS)) {
				result.addAll((List<? extends EObject>)ReferenceUtil.getAsList(annotation,
						EcorePackage.Literals.EANNOTATION__REFERENCES));
			}
		}
		return result;
	}

	@Override
	protected DifferenceKind getRelatedExtensionKind(Diff input) {
		if (input instanceof ReferenceChange) {
			if (isRelatedToAnExtensionAdd((ReferenceChange)input)) {
				return DifferenceKind.ADD;
			} else if (isRelatedToAnExtensionDelete((ReferenceChange)input)) {
				return DifferenceKind.DELETE;
			} else if (isRelatedToAnExtensionChange((ReferenceChange)input)) {
				return DifferenceKind.CHANGE;
			}
		}
		return null;
	}

	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		return input.getReference().isContainment() && input.getKind().equals(DifferenceKind.ADD)
				&& input.getValue() instanceof ProfileApplication;
	}

	protected boolean isRelatedToAnExtensionDelete(ReferenceChange input) {
		return input.getReference().isContainment() && input.getKind().equals(DifferenceKind.DELETE)
				&& input.getValue() instanceof ProfileApplication;
	}

	protected boolean isRelatedToAnExtensionChange(ReferenceChange input) {
		return (input.getReference().equals(UMLPackage.Literals.PROFILE_APPLICATION__APPLIED_PROFILE)
				|| input.getReference().equals(EcorePackage.Literals.EANNOTATION__REFERENCES) || input
				.getReference().equals(EcorePackage.Literals.EMODEL_ELEMENT__EANNOTATIONS));
	}

}

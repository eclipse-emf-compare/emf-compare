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
import org.eclipse.emf.compare.uml2diff.UMLExtension;
import org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChange;
import org.eclipse.emf.compare.uml2diff.Uml2diffFactory;
import org.eclipse.emf.compare.uml2diff.Uml2diffPackage;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.GeneralizationSet;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Factory for UMLGeneralizationSetChangeLeftTarget.
 */
public class UMLGeneralizationSetChangeFactory extends AbstractDiffExtensionFactory {

	private static final UMLPredicate<Setting> REFINING_PREDICATE = new UMLPredicate<EStructuralFeature.Setting>() {
		public boolean apply(Setting input) {
			final ReferenceChange referenceChange = (ReferenceChange)input.getEObject();
			return isChangeGeneralizationSet(referenceChange);
		}
	};

	private static final UMLPredicate<Setting> REQUIRES_ADD_GENERALIZATIONSET_PREDICATE = new UMLPredicate<EStructuralFeature.Setting>() {
		public boolean apply(Setting input) {
			return input.getEObject() instanceof UMLGeneralizationSetChange
					&& ((Diff)input.getEObject()).getKind().equals(DifferenceKind.ADD);
		}
	};

	private static final UMLPredicate<Setting> REQUIRES_CHANGE_GENERALIZATIONSET_PREDICATE = new UMLPredicate<EStructuralFeature.Setting>() {
		public boolean apply(Setting input) {
			return input.getEObject() instanceof UMLGeneralizationSetChange
					&& ((Diff)input.getEObject()).getKind().equals(DifferenceKind.CHANGE);
		}
	};

	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            The UML2 difference engine.
	 */
	public UMLGeneralizationSetChangeFactory() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean handles(Diff input) {
		return (isAddGeneralizationSet(input) || isDeleteGeneralizationSet(input) || isChangeGeneralizationSet(input))
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

		final UMLGeneralizationSetChange ret = Uml2diffFactory.eINSTANCE.createUMLGeneralizationSetChange();

		final GeneralizationSet generalizationSet = getGeneralizationSet(referenceChange);

		if (generalizationSet != null) {
			if (isDeleteGeneralizationSet(input)) {
				ret.getRefinedBy().add(input);
			} else {
				fillRefiningDifferences(crossReferencer, ret, generalizationSet);
			}
		}

		ret.setGeneralizationSet(generalizationSet);
		if (isAddGeneralizationSet(input)) {
			ret.setKind(DifferenceKind.ADD);
		} else if (isDeleteGeneralizationSet(input)) {
			ret.setKind(DifferenceKind.DELETE);
		} else if (isChangeGeneralizationSet(input)) {
			ret.setKind(DifferenceKind.CHANGE);
		}

		registerUMLExtension(crossReferencer, ret,
				Uml2diffPackage.Literals.UML_GENERALIZATION_SET_CHANGE__GENERALIZATION_SET, generalizationSet);

		return ret;
	}

	private void fillRefiningDifferences(EcoreUtil.CrossReferencer crossReferencer,
			final UMLGeneralizationSetChange ret, final GeneralizationSet generalizationSet) {
		for (Generalization namedElement : generalizationSet.getGeneralizations()) {
			beRefinedByCrossReferences(namedElement, ComparePackage.Literals.REFERENCE_CHANGE__VALUE, ret,
					REFINING_PREDICATE, crossReferencer);
		}
	}

	@Override
	public void fillRequiredDifferences(UMLExtension diff, CrossReferencer crossReferencer) {
		if (diff instanceof UMLGeneralizationSetChange && diff.getKind().equals(DifferenceKind.CHANGE)) {
			diff.getRequires().addAll(
					findCrossReferences(((UMLGeneralizationSetChange)diff).getGeneralizationSet(),
							Uml2diffPackage.Literals.UML_GENERALIZATION_SET_CHANGE__GENERALIZATION_SET,
							REQUIRES_ADD_GENERALIZATIONSET_PREDICATE, crossReferencer));
		} else if (diff instanceof UMLGeneralizationSetChange && diff.getKind().equals(DifferenceKind.DELETE)) {
			diff.getRequires().addAll(
					findCrossReferences(((UMLGeneralizationSetChange)diff).getGeneralizationSet(),
							Uml2diffPackage.Literals.UML_GENERALIZATION_SET_CHANGE__GENERALIZATION_SET,
							REQUIRES_CHANGE_GENERALIZATIONSET_PREDICATE, crossReferencer));
		}
	}

	private GeneralizationSet getGeneralizationSet(final ReferenceChange referenceChange) {
		GeneralizationSet generalizationSet = null;
		if (isAddGeneralizationSet(referenceChange) || isDeleteGeneralizationSet(referenceChange)) {
			generalizationSet = (GeneralizationSet)referenceChange.getValue();
		} else if (isChangeGeneralizationSet(referenceChange)) {
			final EObject container = MatchUtil.getContainer(MatchUtil.getComparison(referenceChange),
					referenceChange);
			if (container instanceof GeneralizationSet) {
				generalizationSet = (GeneralizationSet)container;
			}
		}
		return generalizationSet;
	}

	private static boolean isAddGeneralizationSet(Diff input) {
		return input instanceof ReferenceChange && ((ReferenceChange)input).getReference().isContainment()
				&& input.getKind().equals(DifferenceKind.ADD)
				&& ((ReferenceChange)input).getValue() instanceof GeneralizationSet
				&& ((GeneralizationSet)((ReferenceChange)input).getValue()).getGeneralizations() != null
				&& !((GeneralizationSet)((ReferenceChange)input).getValue()).getGeneralizations().isEmpty();
	}

	private static boolean isDeleteGeneralizationSet(Diff input) {
		return input instanceof ReferenceChange && ((ReferenceChange)input).getReference().isContainment()
				&& input.getKind().equals(DifferenceKind.DELETE)
				&& ((ReferenceChange)input).getValue() instanceof GeneralizationSet;
	}

	private static boolean isChangeGeneralizationSet(Diff input) {
		return input instanceof ReferenceChange
				&& ((ReferenceChange)input).getReference().equals(
						UMLPackage.Literals.GENERALIZATION_SET__GENERALIZATION);
	}

	private boolean isAlreadyExist(Diff input) {
		if (input instanceof ReferenceChange) {
			for (Diff diff : input.getRefines()) {
				if (diff instanceof UMLGeneralizationSetChange
						&& ((UMLGeneralizationSetChange)diff).getGeneralizationSet().equals(
								getGeneralizationSet((ReferenceChange)input))) {
					return true;
				}
			}
		}
		return false;
	}

	public Class<? extends UMLExtension> getExtensionKind() {
		return UMLGeneralizationSetChange.class;
	}
}

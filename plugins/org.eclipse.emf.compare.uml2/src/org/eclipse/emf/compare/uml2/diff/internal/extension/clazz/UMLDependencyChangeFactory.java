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
import org.eclipse.emf.compare.uml2diff.UMLDependencyChange;
import org.eclipse.emf.compare.uml2diff.UMLExtension;
import org.eclipse.emf.compare.uml2diff.Uml2diffFactory;
import org.eclipse.emf.compare.uml2diff.Uml2diffPackage;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Factory for UMLDependencyChangeLeftTargetFactory.
 */
public class UMLDependencyChangeFactory extends AbstractDiffExtensionFactory {

	/**
	 * The predicate to hide difference elements.
	 */
	private static final UMLPredicate<Setting> REFINING_PREDICATE = new UMLPredicate<EStructuralFeature.Setting>() {
		public boolean apply(Setting input) {
			final ReferenceChange referenceChange = (ReferenceChange)input.getEObject();
			return isChangeDependency(referenceChange);
		}
	};

	private static final UMLPredicate<Setting> REQUIRES_ADD_DEPENDENCY_PREDICATE = new UMLPredicate<EStructuralFeature.Setting>() {
		public boolean apply(Setting input) {
			return input.getEObject() instanceof UMLDependencyChange
					&& ((UMLDependencyChange)input.getEObject()).getKind().equals(DifferenceKind.ADD);
		}
	};

	private static final UMLPredicate<Setting> REQUIRES_CHANGE_DEPENDENCY_PREDICATE = new UMLPredicate<EStructuralFeature.Setting>() {
		public boolean apply(Setting input) {
			return input.getEObject() instanceof UMLDependencyChange
					&& ((UMLDependencyChange)input.getEObject()).getKind().equals(DifferenceKind.CHANGE);
		}
	};

	/**
	 * Constructor.
	 * 
	 * @param engine
	 *            The UML2 difference engine.
	 */
	public UMLDependencyChangeFactory() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean handles(Diff input) {
		return (isAddDependency(input) || isDeleteDependency(input) || isChangeDependency(input))
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

		final UMLDependencyChange ret = Uml2diffFactory.eINSTANCE.createUMLDependencyChange();

		final Dependency dependency = getDependency(referenceChange);

		if (dependency != null) {
			if (isDeleteDependency(input)) {
				ret.getRefinedBy().add(input);
			} else {
				fillRefiningDifferences(crossReferencer, ret, dependency);
			}
		}

		ret.setDependency(dependency);
		if (isAddDependency(input)) {
			ret.setKind(DifferenceKind.ADD);
		} else if (isDeleteDependency(input)) {
			ret.setKind(DifferenceKind.DELETE);
		} else if (isChangeDependency(input)) {
			ret.setKind(DifferenceKind.CHANGE);
		}

		registerUMLExtension(crossReferencer, ret,
				Uml2diffPackage.Literals.UML_DEPENDENCY_CHANGE__DEPENDENCY, dependency);

		return ret;
	}

	private void fillRefiningDifferences(EcoreUtil.CrossReferencer crossReferencer,
			final UMLDependencyChange ret, final Dependency dependency) {
		for (NamedElement namedElement : dependency.getClients()) {
			beRefinedByCrossReferences(namedElement, ComparePackage.Literals.REFERENCE_CHANGE__VALUE, ret,
					REFINING_PREDICATE, crossReferencer);
		}
		for (NamedElement namedElement : dependency.getSuppliers()) {
			beRefinedByCrossReferences(namedElement, ComparePackage.Literals.REFERENCE_CHANGE__VALUE, ret,
					REFINING_PREDICATE, crossReferencer);
		}
	}

	@Override
	public void fillRequiredDifferences(UMLExtension diff, CrossReferencer crossReferencer) {
		if (diff instanceof UMLDependencyChange && diff.getKind().equals(DifferenceKind.CHANGE)) {
			diff.getRequires().addAll(
					findCrossReferences(((UMLDependencyChange)diff).getDependency(),
							Uml2diffPackage.Literals.UML_DEPENDENCY_CHANGE__DEPENDENCY,
							REQUIRES_ADD_DEPENDENCY_PREDICATE, crossReferencer));
		} else if (diff instanceof UMLDependencyChange && diff.getKind().equals(DifferenceKind.DELETE)) {
			diff.getRequires().addAll(
					findCrossReferences(((UMLDependencyChange)diff).getDependency(),
							Uml2diffPackage.Literals.UML_DEPENDENCY_CHANGE__DEPENDENCY,
							REQUIRES_CHANGE_DEPENDENCY_PREDICATE, crossReferencer));
		}
	}

	private Dependency getDependency(final ReferenceChange referenceChange) {
		Dependency dependency = null;
		if (isAddDependency(referenceChange) || isDeleteDependency(referenceChange)) {
			dependency = (Dependency)referenceChange.getValue();
		} else if (isChangeDependency(referenceChange)) {
			final EObject container = MatchUtil.getContainer(MatchUtil.getComparison(referenceChange),
					referenceChange);
			if (container instanceof Dependency) {
				dependency = (Dependency)container;
			}
		}
		return dependency;
	}

	private static boolean isAddDependency(Diff input) {
		return input instanceof ReferenceChange && ((ReferenceChange)input).getReference().isContainment()
				&& input.getKind().equals(DifferenceKind.ADD)
				&& ((ReferenceChange)input).getValue() instanceof Dependency
				&& ((Dependency)((ReferenceChange)input).getValue()).getClients() != null
				&& !((Dependency)((ReferenceChange)input).getValue()).getClients().isEmpty();
	}

	private static boolean isDeleteDependency(Diff input) {
		return input instanceof ReferenceChange && ((ReferenceChange)input).getReference().isContainment()
				&& input.getKind().equals(DifferenceKind.DELETE)
				&& ((ReferenceChange)input).getValue() instanceof Dependency;
	}

	private static boolean isChangeDependency(Diff input) {
		return input instanceof ReferenceChange
				&& (((ReferenceChange)input).getReference().equals(UMLPackage.Literals.DEPENDENCY__CLIENT) || ((ReferenceChange)input)
						.getReference().equals(UMLPackage.Literals.DEPENDENCY__SUPPLIER));
	}

	private boolean isAlreadyExist(Diff input) {
		if (input instanceof ReferenceChange) {
			for (Diff diff : input.getRefines()) {
				if (diff instanceof UMLDependencyChange
						&& ((UMLDependencyChange)diff).getDependency().equals(
								getDependency((ReferenceChange)input))) {
					return true;
				}
			}
		}
		return false;
	}

	public Class<? extends UMLExtension> getExtensionKind() {
		return UMLDependencyChange.class;
	}

}

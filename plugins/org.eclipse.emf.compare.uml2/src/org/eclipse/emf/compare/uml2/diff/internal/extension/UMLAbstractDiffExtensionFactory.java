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

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.uml2.common.util.UML2Util;

/**
 * Factory for the difference extensions.
 */
public abstract class UMLAbstractDiffExtensionFactory extends AbstractDiffExtensionFactory {

	private final Predicate<Diff> REFINING_PREDICATE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return isPartOfRefiningDifference(diff);
		}
	};

	private final Predicate<Diff> REQUIRES_ADD_DISCRIMINANT_PREDICATE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return getExtensionKind().isInstance(diff)
					&& ((UMLDiff)diff).getKind().equals(DifferenceKind.ADD);
		}
	};

	private final Predicate<Diff> REQUIRES_CHANGE_DISCRIMINANT_PREDICATE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return getExtensionKind().isInstance(diff)
					&& ((UMLDiff)diff).getKind().equals(DifferenceKind.CHANGE);
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
	public Diff create(Diff input) {

		final UMLDiff ret = createExtension();

		final EObject discriminant = getDiscriminantFromDiff(input);

		final DifferenceKind extensionKind = getRelatedExtensionKind(input);

		if (discriminant != null) {
			if (extensionKind == DifferenceKind.DELETE) {
				ret.getRefinedBy().add(input);
			} else {
				fillRefiningDifferences(input.getMatch().getComparison(), ret, discriminant);
			}
		}

		ret.setDiscriminant(discriminant);
		ret.setKind(extensionKind);
		if (extensionKind == DifferenceKind.ADD || extensionKind == DifferenceKind.DELETE) {
			ret.setEReference(((ReferenceChange)input).getReference());
		}

		return ret;
	}

	protected void fillRefiningDifferences(final Comparison comparison, final UMLDiff diffExtension,
			final EObject discriminant) {
		// Find Diffs through ComparePackage.Literals.REFERENCE_CHANGE__VALUE
		for (EObject elt : getPotentialChangedValuesFromDiscriminant(discriminant)) {
			beRefinedByCrossReferences(comparison, elt, diffExtension, REFINING_PREDICATE);
		}
	}

	@Override
	public void fillRequiredDifferences(Comparison comparison, UMLDiff extension) {
		if (getExtensionKind().isInstance(extension)) {
			// Find UMLDiffs through UMLComparePackage.Literals.UML_DIFF__DISCRIMINANT
			final EObject discriminant = extension.getDiscriminant();
			if (discriminant != null) {
				if (extension.getKind().equals(DifferenceKind.CHANGE)) {
					extension.getRequires()
							.addAll(findCrossReferences(comparison, discriminant,
									REQUIRES_ADD_DISCRIMINANT_PREDICATE));
				} else if (extension.getKind().equals(DifferenceKind.DELETE)) {
					extension.getRequires().addAll(
							findCrossReferences(comparison, discriminant,
									REQUIRES_CHANGE_DISCRIMINANT_PREDICATE));
				}
			}
		}
	}

	@Override
	public Match getParentMatch(Diff input) {
		if (getRelatedExtensionKind(input) == DifferenceKind.CHANGE && getDiscriminantFromDiff(input) != null) {
			return (Match)input.getMatch().getComparison().getMatch(getDiscriminantFromDiff(input))
					.eContainer();
		}
		return super.getParentMatch(input);
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

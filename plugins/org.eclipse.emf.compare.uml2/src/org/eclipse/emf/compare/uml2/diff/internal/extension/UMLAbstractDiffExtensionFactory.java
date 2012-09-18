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

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.compare.util.CompareSwitch;
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

	private CompareSwitch<DifferenceKind> differenceKindCompareSwitch = new CompareSwitch<DifferenceKind>() {

		@Override
		public DifferenceKind caseAttributeChange(AttributeChange object) {
			if (isRelatedToAnExtensionAdd(object)) {
				return DifferenceKind.ADD;
			} else if (isRelatedToAnExtensionDelete(object)) {
				return DifferenceKind.DELETE;
			} else if (isRelatedToAnExtensionChange(object)) {
				return DifferenceKind.CHANGE;
			} else if (isRelatedToAnExtensionMove(object)) {
				return DifferenceKind.MOVE;
			} else {
				return super.caseAttributeChange(object);
			}
		}

		@Override
		public DifferenceKind caseReferenceChange(ReferenceChange object) {
			if (isRelatedToAnExtensionAdd(object)) {
				return DifferenceKind.ADD;
			} else if (isRelatedToAnExtensionDelete(object)) {
				return DifferenceKind.DELETE;
			} else if (isRelatedToAnExtensionChange(object)) {
				return DifferenceKind.CHANGE;
			} else if (isRelatedToAnExtensionMove(object)) {
				return DifferenceKind.MOVE;
			} else {
				return super.caseReferenceChange(object);
			}
		}

		@Override
		public DifferenceKind caseResourceAttachmentChange(ResourceAttachmentChange object) {
			if (isRelatedToAnExtensionAdd(object)) {
				return DifferenceKind.ADD;
			} else if (isRelatedToAnExtensionDelete(object)) {
				return DifferenceKind.DELETE;
			} else if (isRelatedToAnExtensionChange(object)) {
				return DifferenceKind.CHANGE;
			} else if (isRelatedToAnExtensionMove(object)) {
				return DifferenceKind.MOVE;
			} else {
				return super.caseResourceAttachmentChange(object);
			}
		}
	};

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#handles(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean handles(Diff input) {
		return getRelatedExtensionKind(input) != null && !isExtensionAlreadyExist(input)
				&& !isChangeOnAddOrDelete(input);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory#create(org.eclipse.emf.
	 *      compare.diff.metamodel.DiffElement)
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
			if (input instanceof ReferenceChange) {
				ret.setEReference(((ReferenceChange)input).getReference());
			}
		} /*
		 * else if (discriminant != null && discriminant.eContainingFeature() instanceof EReference) {
		 * ret.setReference((EReference)discriminant.eContainingFeature()); } //FIXME: replace discriminant
		 * and eReference by value and reference. ret.setValue(ret.getDiscriminant());
		 * ret.setReference(ret.getEReference());
		 */

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
		if (getRelatedExtensionKind(input) == DifferenceKind.CHANGE) {
			final EObject discriminant = getDiscriminantFromDiff(input);
			if (discriminant != null) {
				final Match match = input.getMatch().getComparison().getMatch(discriminant);
				if (match.eContainer() instanceof Match) {
					return (Match)match.eContainer();
				} else {
					return match;
				}
			} else {
				return input.getMatch();
			}
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

	private boolean isChangeOnAddOrDelete(Diff input) {
		if (getRelatedExtensionKind(input) == DifferenceKind.CHANGE) {
			final List<Diff> differences = input.getMatch().getComparison().getDifferences();
			differences.remove(input);
			for (Diff diff : differences) {
				if ((getRelatedExtensionKind(diff) == DifferenceKind.ADD || getRelatedExtensionKind(diff) == DifferenceKind.DELETE)
						&& getDiscriminantFromDiff(diff) == getDiscriminantFromDiff(input)) {
					return true;
				}
			}
		}
		return false;
	}

	protected abstract EObject getDiscriminantFromDiff(Diff input);

	protected abstract List<EObject> getPotentialChangedValuesFromDiscriminant(EObject discriminant);

	protected DifferenceKind getRelatedExtensionKind(Diff input) {
		return differenceKindCompareSwitch.doSwitch(input);
	}

	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionDelete(ReferenceChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionChange(ReferenceChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionMove(ReferenceChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionAdd(AttributeChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionDelete(AttributeChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionChange(AttributeChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionMove(AttributeChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionAdd(ResourceAttachmentChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionDelete(ResourceAttachmentChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionChange(ResourceAttachmentChange input) {
		return false;
	}

	protected boolean isRelatedToAnExtensionMove(ResourceAttachmentChange input) {
		return false;
	}

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

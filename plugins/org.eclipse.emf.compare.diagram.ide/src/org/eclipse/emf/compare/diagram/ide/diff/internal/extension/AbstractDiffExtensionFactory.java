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
package org.eclipse.emf.compare.diagram.ide.diff.internal.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.util.CompareSwitch;
import org.eclipse.emf.ecore.EObject;

import com.google.common.base.Predicate;

/**
 * Factory for the difference extensions.
 */
public abstract class AbstractDiffExtensionFactory implements IDiffExtensionFactory {

	private final Predicate<Diff> REFINING_PREDICATE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return isPartOfRefiningDifference(diff);
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

		final Diff ret = createExtension();

		final EObject discriminant = getDiscriminantFromDiff(input);

		final DifferenceKind extensionKind = getRelatedExtensionKind(input);

		if (discriminant != null) {
			if (extensionKind == DifferenceKind.DELETE) {
				ret.getRefinedBy().add(input);
			} else {
				fillRefiningDifferences(input.getMatch().getComparison(), ret, discriminant);
			}
		}

		// ret.setDiscriminant(discriminant);
		ret.setKind(extensionKind);
		if (extensionKind == DifferenceKind.ADD || extensionKind == DifferenceKind.DELETE) {
			if (input instanceof ReferenceChange) {
				// ret.setEReference(((ReferenceChange)input).getReference());
			}
		}
		
		ret.setSource(input.getSource());

		return ret;
	}

	protected void fillRefiningDifferences(final Comparison comparison, final Diff diffExtension,
			final EObject discriminant) {
		// Find Diffs through ComparePackage.Literals.REFERENCE_CHANGE__VALUE
		for (EObject elt : getPotentialChangedValuesFromDiscriminant(discriminant)) {
			beRefinedByCrossReferences(comparison, elt, diffExtension, REFINING_PREDICATE);
		}
	}

	public void fillRequiredDifferences(Comparison comparison, Diff extension) {
		if (getExtensionKind().isInstance(extension)) {
			// final EObject discriminant = extension.getDiscriminant();
			// if (discriminant != null) {
			// if (extension.getKind().equals(DifferenceKind.CHANGE)) {
			// extension.getRequires()
			// .addAll(findCrossReferences(comparison, discriminant,
			// REQUIRES_ADD_DISCRIMINANT_PREDICATE));
			// } else if (extension.getKind().equals(DifferenceKind.DELETE)) {
			// extension.getRequires().addAll(
			// findCrossReferences(comparison, discriminant,
			// REQUIRES_CHANGE_DISCRIMINANT_PREDICATE));
			// }
			// }
		}
	}

	public Match getParentMatch(Diff input) {
		return input.getMatch();
	}

	protected Diff createExtension() {
		return CompareFactory.eINSTANCE.createDiff();
	}

	protected boolean isPartOfRefiningDifference(Diff diff) {
		return diff instanceof ReferenceChange && getRelatedExtensionKind(diff) == DifferenceKind.CHANGE;
	}

	private boolean isExtensionAlreadyExist(Diff input) {
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

	protected EObject getDiscriminantFromDiff(Diff input) {
		return null;
	}

	protected List<EObject> getPotentialChangedValuesFromDiscriminant(EObject discriminant) {
		return Collections.EMPTY_LIST;
	}

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

	/**
	 * Find the cross references of the given model object, through the specified feature, with a cross
	 * referencer and a predicate.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @param lookup
	 *            The model object.
	 * @param inFeature
	 *            The feature.
	 * @param p
	 *            The predicate.
	 * @param crossReferencer
	 *            The cross referencer.
	 * @return The cross references.
	 */
	protected final List<Diff> findCrossReferences(Comparison comparison, EObject lookup, Predicate<Diff> p) {
		final List<Diff> result = new ArrayList<Diff>();
		for (Diff diff : comparison.getDifferences(lookup)) {
			if (p.apply(diff)) {
				result.add(diff);
			}
		}
		return result;
	}

	/**
	 * Hide the difference elements from the given extension, from the specified model object, the feature and
	 * cross referencer, with a predicate.
	 * 
	 * @param lookup
	 *            The model object.
	 * @param inFeature
	 *            The feature.
	 * @param hiddingExtension
	 *            The extension
	 * @param p
	 *            The predicate
	 * @param crossReferencer
	 *            The cross referencer.
	 */
	protected final void beRefinedByCrossReferences(Comparison comparison, EObject lookup,
			Diff refinedExtension, Predicate<Diff> p) {
		for (Diff diffElement : findCrossReferences(comparison, lookup, p)) {
			refinedExtension.getRefinedBy().add(diffElement);
		}
	}

	public Class<? extends Diff> getExtensionKind() {
		return Diff.class;
	}

}

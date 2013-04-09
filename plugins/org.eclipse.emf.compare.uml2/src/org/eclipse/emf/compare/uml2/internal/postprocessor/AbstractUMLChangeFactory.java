/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor;

import static com.google.common.base.Predicates.instanceOf;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory;
import org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.uml2.common.util.UML2Util;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Factory of diagram difference extensions.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public abstract class AbstractUMLChangeFactory extends AbstractChangeFactory {

	@Override
	public boolean handles(Diff input) {
		return super.handles(input) && !isChangeOnAddOrDelete(input) && input.getRefines().isEmpty();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.IChangeFactory#create(org.eclipse.emf.compare.Diff)
	 */
	@Override
	public Diff create(Diff input) {
		Diff ret = super.create(input);
		if (ret instanceof UMLDiff) {
			setDiscriminant((UMLDiff)ret, input);

			if (getRelatedExtensionKind(input) == DifferenceKind.ADD
					|| getRelatedExtensionKind(input) == DifferenceKind.DELETE) {
				if (input instanceof ReferenceChange) {
					((UMLDiff)ret).setEReference(((ReferenceChange)input).getReference());
				} else if (input instanceof ResourceAttachmentChange
						&& ret instanceof StereotypeApplicationChange) {
					// the resource attachment concerns the stereotype application itself.
					// The reference is located "below" that.
					final List<Diff> candidates = input.getMatch().getDifferences();
					// Little chance that there is more is that the input ... and what we seek.
					for (Diff candidate : candidates) {
						if (candidate instanceof ReferenceChange) {
							((UMLDiff)ret).setEReference(((ReferenceChange)candidate).getReference());
						}
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Get the view and set the given extension with it, from the given refining one.
	 * 
	 * @param extension
	 *            The extension to set.
	 * @param refiningDiff
	 *            The difference refining the given extension.
	 * @return The view.
	 */
	public EObject setDiscriminant(UMLDiff extension, Diff refiningDiff) {
		EObject discriminant = getDiscriminantFromDiff(refiningDiff);
		extension.setDiscriminant(discriminant);
		return discriminant;
	}

	@Override
	public void setRefiningChanges(Diff extension, DifferenceKind extensionKind, Diff refiningDiff) {
		EObject discriminant = getDiscriminantFromDiff(refiningDiff);
		// Find Diffs through ComparePackage.Literals.REFERENCE_CHANGE__VALUE in the UML domain.
		for (EObject elt : getPotentialChangedValuesFromDiscriminant(discriminant)) {
			beRefinedByCrossReferences(refiningDiff.getMatch().getComparison(), elt, (UMLDiff)extension,
					keepOnlyDifferences());
		}
	}

	protected abstract EObject getDiscriminantFromDiff(Diff input);

	protected abstract List<EObject> getPotentialChangedValuesFromDiscriminant(EObject discriminant);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.IChangeFactory#getParentMatch(org.eclipse.emf.compare.Diff)
	 */
	@Override
	public Match getParentMatch(Diff input) {
		return getParentMatch(input.getMatch().getComparison(), input);
	}

	/**
	 * Get the match in which the given difference should be added.
	 * 
	 * @param comparison
	 *            The current comparison.
	 * @param input
	 *            The difference to locate.
	 * @return The containing match.
	 */
	public Match getParentMatch(Comparison comparison, Diff input) {
		if (input.getKind() == DifferenceKind.CHANGE) {
			return comparison.getMatch(getDiscriminantFromDiff(input));
		} else {
			return input.getMatch();
		}
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
			UMLDiff refinedExtension, Predicate<Diff> p) {
		List<Diff> crossReferences = findCrossReferences(comparison, lookup, p);
		refinedExtension.getRefinedBy().addAll(crossReferences);
	}

	protected Predicate<Diff> keepOnlyDifferences() {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				return !isNotOnUmlDomain(input);
			}
		};
	}

	private boolean isNotOnUmlDomain(Diff input) {
		return input instanceof ReferenceChange
				&& ((ReferenceChange)input).getReference().getEContainingClass().getEPackage() != UMLPackage.eINSTANCE;
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

	private boolean isChangeOnAddOrDelete(Diff input) {
		if (getRelatedExtensionKind(input) == DifferenceKind.CHANGE) {
			final Comparison comparison = input.getMatch().getComparison();
			final EObject discriminant = getDiscriminantFromDiff(input);

			if (!Collections2.filter(comparison.getMatch(discriminant).getDifferences(),
					instanceOf(ResourceAttachmentChange.class)).isEmpty()) {
				return true;
			}

			final List<Diff> candidates = comparison.getDifferences(discriminant);

			for (Diff diff : candidates) {
				if (diff == input) {
					// ignore this one
				} else {
					DifferenceKind relatedExtensionKind = getRelatedExtensionKind(diff);
					if ((relatedExtensionKind == DifferenceKind.ADD || relatedExtensionKind == DifferenceKind.DELETE)
							&& getDiscriminantFromDiff(diff) == discriminant) {
						return true;
					}
				}
			}
		}
		return false;
	}

}

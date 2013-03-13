/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor;

import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.ManyStructuralFeatureAccessorImpl;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UMLManyStructuralFeatureAccessor extends ManyStructuralFeatureAccessorImpl {

	/**
	 * @param diff
	 * @param side
	 */
	public UMLManyStructuralFeatureAccessor(Diff diff, MergeViewerSide side) {
		super(diff, side);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.AbstractStructuralFeatureAccessor#getAffectedFeature(org.eclipse.emf.compare.Diff)
	 */
	@Override
	protected EStructuralFeature getAffectedFeature(Diff diff) {
		if (diff instanceof UMLDiff) {
			return ((UMLDiff)diff).getEReference();
		}
		return super.getAffectedFeature(diff);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.ManyStructuralFeatureAccessorImpl#getDiffValue(org.eclipse.emf.compare.Diff)
	 */
	@Override
	protected Object getDiffValue(Diff diff) {
		if (diff instanceof UMLDiff) {
			return ((UMLDiff)diff).getDiscriminant();
		}
		return super.getDiffValue(diff);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.AbstractStructuralFeatureAccessor#computeDifferences()
	 */
	@Override
	protected ImmutableList<Diff> computeDifferences() {
		List<Diff> siblingDifferences = getInitialDiff().getMatch().getDifferences();
		// We'll display all diffs of the same type, excluding the pseudo conflicts.
		Predicate<? super Diff> diffFilter = not(hasConflict(ConflictKind.PSEUDO));

		return ImmutableList.copyOf(filter(filter(siblingDifferences, diffFilter), getInitialDiff()
				.getClass()));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.ManyStructuralFeatureAccessorImpl#findInsertionIndex(org.eclipse.emf.compare.Diff,
	 *      boolean)
	 */
	@Override
	protected int findInsertionIndex(Diff diff, boolean rightToLeft) {
		if (diff instanceof UMLDiff) {
			return super.findInsertionIndex(getDiffFromUMLDiff((UMLDiff)diff), rightToLeft);
		}
		return super.findInsertionIndex(diff, rightToLeft);
	}

	private static Diff getDiffFromUMLDiff(UMLDiff diff) {
		EObject discriminant = diff.getDiscriminant();
		EList<Diff> differences = diff.getMatch().getComparison().getDifferences(discriminant);
		for (ReferenceChange referenceChange : filter(differences, ReferenceChange.class)) {
			if (referenceChange.getKind() == diff.getKind()) {
				EReference reference = referenceChange.getReference();
				if (reference == diff.getEReference() && referenceChange.getValue() == discriminant) {
					return referenceChange;
				}
			}
		}
		return null;
	}
}

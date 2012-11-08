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
package org.eclipse.emf.compare.uml2.ide.ui.internal.accessor;

import static com.google.common.collect.Iterables.filter;

import com.google.common.collect.ImmutableList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IDEManyStructuralFeatureAccessorImpl;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.MergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UMLIDEManyStructuralFeatureAccessor extends IDEManyStructuralFeatureAccessorImpl {

	/**
	 * @param diff
	 * @param side
	 */
	public UMLIDEManyStructuralFeatureAccessor(Diff diff, MergeViewerSide side) {
		super(diff, side);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor.BasicStructuralFeatureAccessorImpl#getAffectedFeature(org.eclipse.emf.compare.Diff)
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
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor.ManyStructuralFeatureAccessorImpl#getDiffValue(org.eclipse.emf.compare.Diff)
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
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor.BasicStructuralFeatureAccessorImpl#computeDifferences()
	 */
	@Override
	protected ImmutableList<Diff> computeDifferences() {
		return ImmutableList.of(getInitialDiff());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor.ManyStructuralFeatureAccessorImpl#findInsertionIndex(org.eclipse.emf.compare.Diff,
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
		Diff ret = null;
		EObject discriminant = diff.getDiscriminant();
		EList<Diff> differences = diff.getMatch().getComparison().getDifferences(discriminant);
		for (ReferenceChange referenceChange : filter(differences, ReferenceChange.class)) {
			if (referenceChange.getKind() == diff.getKind()) {
				EReference reference = referenceChange.getReference();
				if (reference == diff.getEReference() && referenceChange.getValue() == discriminant) {
					ret = referenceChange;
				}
			}
		}
		return ret;
	}
}

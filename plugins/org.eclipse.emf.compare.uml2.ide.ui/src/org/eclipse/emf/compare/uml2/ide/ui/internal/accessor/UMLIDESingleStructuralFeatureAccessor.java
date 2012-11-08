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

import com.google.common.collect.ImmutableList;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IDESingleStructuralFeatureAccessorImpl;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.MergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UMLIDESingleStructuralFeatureAccessor extends IDESingleStructuralFeatureAccessorImpl {

	/**
	 * @param diff
	 * @param side
	 */
	public UMLIDESingleStructuralFeatureAccessor(Diff diff, MergeViewerSide side) {
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
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor.BasicStructuralFeatureAccessorImpl#computeDifferences()
	 */
	@Override
	protected ImmutableList<Diff> computeDifferences() {
		return ImmutableList.of(getInitialDiff());
	}
}

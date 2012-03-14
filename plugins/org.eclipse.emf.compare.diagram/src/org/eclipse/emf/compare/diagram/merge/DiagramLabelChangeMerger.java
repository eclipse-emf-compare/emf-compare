/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.merge;

import org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramLabelChange;
import org.eclipse.emf.compare.diagram.diff.util.DiffUtil;
import org.eclipse.emf.compare.diagram.provider.IViewLabelProvider;
import org.eclipse.emf.compare.diff.merge.DefaultMerger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Merger for DiagramLabelChange differences.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public class DiagramLabelChangeMerger extends DefaultMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#applyInOrigin()
	 */
	@Override
	public void doApplyInOrigin() {
		final BusinessDiagramLabelChange theDiff = (BusinessDiagramLabelChange)this.diff;
		doMerge(theDiff, true);
	}

	/**
	 * Make the merge of the specified difference according to the direction (applyInOrigin or undoInTarget).
	 * If the difference is an extension, only hidden differences will be merged. Otherwise, it is the
	 * difference itself which is merged.
	 * 
	 * @param pDiff
	 *            The difference.
	 * @param inOrigin
	 *            The direction of the merge.
	 */
	private void doMerge(BusinessDiagramLabelChange pDiff, boolean inOrigin) {
		final EObject element = pDiff.getRightElement();
		final EObject origin = pDiff.getLeftElement();
		if (element instanceof View && origin instanceof View) {
			final View vElement = (View)element;
			final View vOrigin = (View)origin;
			if (inOrigin)
				// DiffUtil.setLabel(vOrigin, pDiff.getRightLabel());
				setLabel(vOrigin, pDiff.getRightLabel());
			else
				// DiffUtil.setLabel(vElement, pDiff.getLeftLabel());
				setLabel(vElement, pDiff.getLeftLabel());
			DiffUtil.clearLabelExtensions();
		}
	}

	/**
	 * Set the label of the view.
	 * 
	 * @param view
	 *            the view.
	 * @param label
	 *            the label.
	 */
	private void setLabel(View view, String label) {
		final IViewLabelProvider extensionForType = DiffUtil.getExtension(view);
		if (DiffUtil.isLabelAvailable(extensionForType, view)) {
			extensionForType.setLabel(view, label);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#undoInTarget()
	 */
	@Override
	public void doUndoInTarget() {
		final BusinessDiagramLabelChange theDiff = (BusinessDiagramLabelChange)this.diff;
		doMerge(theDiff, false);
	}

}

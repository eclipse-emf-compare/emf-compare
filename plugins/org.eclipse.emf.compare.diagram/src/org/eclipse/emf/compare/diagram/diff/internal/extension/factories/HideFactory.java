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
package org.eclipse.emf.compare.diagram.diff.internal.extension.factories;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.diagram.DiagramCompareFactory;
import org.eclipse.emf.compare.diagram.Hide;
import org.eclipse.emf.compare.diagram.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Factory for UMLAssociationChangeLeftTarget.
 */
public class HideFactory extends AbstractDiffExtensionFactory {

	@Override
	public Class<? extends Diff> getExtensionKind() {
		return Hide.class;
	}

	public Diff create(Diff input) {
		final Hide ret = DiagramCompareFactory.eINSTANCE.createHide();

		final DifferenceKind extensionKind = getRelatedExtensionKind(input);

		ret.setKind(extensionKind);

		ret.getRefinedBy().add(input);

		return ret;
	}

	@Override
	protected boolean isRelatedToAnExtensionChange(AttributeChange input) {
		if (input.getAttribute().equals(NotationPackage.eINSTANCE.getView_Visible())) {
			final EObject left = input.getMatch().getLeft();
			if (left instanceof View) {
				final View leftView = (View)left;
				final boolean cond1 = !leftView.isVisible() && input.getSource() != DifferenceSource.RIGHT;
				final boolean cond2 = leftView.isVisible() && input.getSource() == DifferenceSource.RIGHT;
				return cond1 || cond2;
			}
		}
		return false;
	}

}

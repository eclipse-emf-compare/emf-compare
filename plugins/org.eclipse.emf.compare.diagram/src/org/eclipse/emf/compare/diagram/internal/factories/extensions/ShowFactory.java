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
package org.eclipse.emf.compare.diagram.internal.factories.extensions;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.diagram.internal.extensions.ExtensionsFactory;
import org.eclipse.emf.compare.diagram.internal.extensions.Show;
import org.eclipse.emf.compare.diagram.internal.factories.AbstractDiagramChangeFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Factory of show changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class ShowFactory extends AbstractDiagramChangeFactory {

	@Override
	public Class<? extends Diff> getExtensionKind() {
		return Show.class;
	}

	@Override
	public Show createExtension() {
		return ExtensionsFactory.eINSTANCE.createShow();
	}

	@Override
	public void setRefiningChanges(Diff extension, DifferenceKind extensionKind, Diff refiningDiff) {
		extension.getRefinedBy().add(refiningDiff);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#isRelatedToAnExtensionChange(org.eclipse.emf.compare.AttributeChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionChange(AttributeChange input) {
		if (input.getAttribute().equals(NotationPackage.eINSTANCE.getView_Visible())) {
			final EObject right = input.getMatch().getRight();
			if (right instanceof View) {
				final View rightView = (View)right;
				final boolean cond1 = !rightView.isVisible() && input.getSource() != DifferenceSource.RIGHT;
				final boolean cond2 = rightView.isVisible() && input.getSource() == DifferenceSource.RIGHT;
				return cond1 || cond2;
			}
		}
		return false;
	}

}

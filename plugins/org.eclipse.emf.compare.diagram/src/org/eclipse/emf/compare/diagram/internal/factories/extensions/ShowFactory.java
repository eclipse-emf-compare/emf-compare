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
import org.eclipse.emf.compare.diagram.internal.factories.AbstractDiagramExtensionFactory;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Factory of show changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class ShowFactory extends AbstractDiagramExtensionFactory {

	@Override
	public Class<? extends Diff> getExtensionKind() {
		return Show.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.internal.factories.IDiagramExtensionFactory#create(org.eclipse.emf.compare.Diff)
	 */
	public Diff create(Diff input) {
		final Show ret = ExtensionsFactory.eINSTANCE.createShow();

		final DifferenceKind extensionKind = getRelatedExtensionKind(input);

		ret.setKind(extensionKind);

		ret.getRefinedBy().add(input);

		if (input instanceof AttributeChange) {
			ret.setView(MatchUtil.getContainer(input.getMatch().getComparison(), input));
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.internal.factories.AbstractDiagramExtensionFactory#isRelatedToAnExtensionChange(org.eclipse.emf.compare.AttributeChange)
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

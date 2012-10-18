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
package org.eclipse.emf.compare.diagram.ide.ecoretools;

import org.eclipse.emf.compare.diagram.provider.AbstractLabelProvider;
import org.eclipse.emf.ecoretools.diagram.edit.parts.EcoreEditPartFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The provider for EcoreTools diagram kind.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public class EcoreToolsViewLabelProvider extends AbstractLabelProvider {

	/**
	 * EcoreEditPartFactory.
	 */
	private static final EcoreEditPartFactory EDIT_PART_FACTORY = new EcoreEditPartFactory();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.provider.IViewLabelProvider#isManaged(org.eclipse.gmf.runtime.notation.View)
	 */
	public boolean isManaged(View view) {
		return EDIT_PART_FACTORY.createEditPart(null, view) instanceof ITextAwareEditPart;
	}
}

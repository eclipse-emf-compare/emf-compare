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
package org.eclipse.emf.compare.diagram.papyrus;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.diagram.deployment.edit.parts.UMLEditPartFactory;

/**
 * View label provider for Deployment.
 * @author Mickael Barbero <a href="mailto:mickael.barbero@obeo.fr">mickael.barbero@obeo.fr</a>
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DeploymentViewLabelProvider extends AbstractUMLViewLabelProvider {

	/**
	 * The edit part factory.
	 */
	private static final UMLEditPartFactory EDIT_PART_FACTORY = new UMLEditPartFactory();
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.diagram.provider.AbstractLabelProvider#createEditPart(org.eclipse.gmf.runtime.notation.View)
	 */
	@Override
	protected EditPart createEditPart(View view) {
		return EDIT_PART_FACTORY.createEditPart(null, view);
	}

}

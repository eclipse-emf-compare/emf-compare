/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.diagramdiff;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Common interface for extensions of differences.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public interface BusinessDiffExtension extends DiagramDiffExtension {

	/**
	 * Initialize the extension.
	 * 
	 * @param origin
	 *            The difference from which this extension is created.
	 * @param crossReferencer
	 *            A cross referencer on the DiffModel. It can be null.
	 * @param match
	 *            The match model. It can be null.
	 */
	void init(DiffElement origin, EcoreUtil.CrossReferencer crossReferencer, MatchModel match);

	/**
	 * Get one of the objects taken into account by this difference.
	 * 
	 * @return The element.
	 */
	EObject getElement();

	/**
	 * Get the right resource.
	 * 
	 * @return The right resource.
	 */
	Resource getRightResource();

	/**
	 * Get the left resource.
	 * 
	 * @return The left resource.
	 */
	Resource getLeftResource();

}

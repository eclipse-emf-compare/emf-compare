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

import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Specific and common interface for {@link DiagramHideElement} and {@link DiagramShowElement}.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public interface BusinessDiagramShowHideElement extends BusinessDiffExtension {

	/**
	 * Get the difference hidden by this extension.
	 * 
	 * @return The hidden difference.
	 */
	UpdateAttribute getUpdateAttributeVisible();

	/**
	 * Get the left view.
	 * 
	 * @return The left view.
	 */
	View getLeftView();

	/**
	 * Get the right view.
	 * 
	 * @return The right view.
	 */
	View getRightView();

}

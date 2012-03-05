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
package org.eclipse.emf.compare.diagram.diagramdiff;

/**
 * Overriding of {@link DiagramLabelChange} for specific interface.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public interface BusinessDiagramLabelChange extends DiagramLabelChange, BusinessDiffExtension {

	/**
	 * Set the left label.
	 * 
	 * @param label
	 *            The label.
	 */
	void setLeftLabel(String label);

	/**
	 * Set the right label.
	 * 
	 * @param label
	 *            The label.
	 */
	void setRightLabel(String label);

	/**
	 * Get the left label.
	 * 
	 * @return The label.
	 */
	String getLeftLabel();

	/**
	 * Get the right label.
	 * 
	 * @return The label.
	 */
	String getRightLabel();

}

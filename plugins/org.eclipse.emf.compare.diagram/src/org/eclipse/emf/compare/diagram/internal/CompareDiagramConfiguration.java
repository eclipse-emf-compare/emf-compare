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
package org.eclipse.emf.compare.diagram.internal;

/**
 * Configuration of the diagram comparison.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CompareDiagramConfiguration {

	/** Detection threshold value for the move of a graphical object. */
	private int moveThreshold;

	/**
	 * Get the threshold value for the move change.
	 * 
	 * @return The threshold value.
	 */
	public int getMoveThreshold() {
		return moveThreshold;
	}

	/**
	 * Set the threshold value for the move change.
	 * 
	 * @param moveThreshold
	 *            The threshold value.
	 */
	public void setMoveThreshold(int moveThreshold) {
		this.moveThreshold = moveThreshold;
	}

}

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
package org.eclipse.emf.compare.diagram.ide.ui.decoration;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;

/**
 * Figure to represent a phantom of a graphical object.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DeleteGhostImageFigure extends RectangleFigure {

	/**
	 * Constructor.
	 */
	public DeleteGhostImageFigure() {
		super();
	}

	// public DeleteGhostImageFigure(IFigure figure, int alpha, RGB transparency) {
	// super(figure, alpha, transparency);
	// }

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.draw2d.Shape#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	@Override
	public void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
	}

}

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
package org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures;

import java.util.Map;

import org.eclipse.draw2d.AbstractPointListShape;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.ICompareColor;

/**
 * Class which allows to manage the look of node lists decorators.<br>
 * Phantoms are simple horizontal segments and markers are simple rectangles with a colored transparent
 * background.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class NodeListFigure extends NodeFigure {

	/** Parameter to specify the Y coordinate of the phantom to draw. */
	public static final String PARAM_Y_POS = "yPos"; //$NON-NLS-1$

	/**
	 * Constructor.
	 * 
	 * @param difference
	 *            The difference related to this decorator.
	 * @param isThreeWay
	 *            The kind of comparison.
	 * @param compareColor
	 *            The compare color used to color this decorator.
	 * @param reference
	 *            The figure used as reference to draw this decorator.
	 * @param bounds
	 *            The bounds of this decorator.
	 * @param isPhantom
	 *            True if it is the phantom version which has to be drawn, False for the marker version.
	 * @param parameters
	 *            Additional parameters accepted by this decorator (for e.g.
	 *            {@link NodeListFigure#PARAM_Y_POS}).
	 */
	public NodeListFigure(Diff difference, boolean isThreeWay, ICompareColor compareColor, IFigure reference,
			Rectangle bounds, boolean isPhantom, Map<String, Object> parameters) {
		super(difference, isThreeWay, compareColor, reference, bounds, isPhantom, parameters);
	}

	/**
	 * Constructor.
	 * 
	 * @param difference
	 *            The difference related to this decorator.
	 * @param isThreeWay
	 *            The kind of comparison.
	 * @param compareColor
	 *            The compare color used to color this decorator.
	 * @param reference
	 *            The figure used as reference to draw this decorator.
	 * @param bounds
	 *            The bounds of this decorator.
	 * @param isPhantom
	 *            True if it is the phantom version which has to be drawn, False for the marker version.
	 */
	public NodeListFigure(Diff difference, boolean isThreeWay, ICompareColor compareColor, IFigure reference,
			Rectangle bounds, boolean isPhantom) {
		super(difference, isThreeWay, compareColor, reference, bounds, isPhantom);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.DecoratorFigure#createFigureForPhantom()
	 */
	@Override
	protected AbstractPointListShape createFigureForPhantom() {
		return new Polyline();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.NodeFigure#buildFigureForPhantom()
	 */
	@Override
	protected void buildFigureForPhantom() {
		if (getParameters() != null) {
			int yPos = (Integer)getParameters().get(PARAM_Y_POS);
			((AbstractPointListShape)getMainFigure()).setEndpoints(new Point(getBounds().x, yPos), new Point(
					getBounds().x + getBounds().width, yPos));
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.NodeFigure#highlightForPhantom(org.eclipse.draw2d.IFigure)
	 */
	@Override
	protected void highlightForPhantom(IFigure figure) {
		defaultHighlightForPhantom(figure);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.NodeFigure#unhighlightForPhantom(org.eclipse.draw2d.IFigure)
	 */
	@Override
	protected void unhighlightForPhantom(IFigure figure) {
		defaultUnhighlightForPhantom(figure);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.DecoratorFigure#hasToEnlarge()
	 */
	@Override
	protected boolean hasToEnlarge() {
		return false;
	}

}

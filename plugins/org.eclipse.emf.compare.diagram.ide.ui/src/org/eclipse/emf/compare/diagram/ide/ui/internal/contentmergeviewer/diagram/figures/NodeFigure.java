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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.ICompareColor;
import org.eclipse.swt.graphics.Color;

/**
 * Class which allows to manage the look of nodes decorators.<br>
 * Highlighted phantoms and markers are simple rectangles with a colored transparent background.<br>
 * Unhilighted phantoms do not own background.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class NodeFigure extends DecoratorFigure {

	/** A child figure to fill background of the rectangle with a transparent color. */
	private RectangleFigure fChildDecoratorFigure;

	/**
	 * Constructor.
	 * 
	 * @param difference
	 *            The difference related to this node decorator.
	 * @param isThreeWay
	 *            The kind of comparison.
	 * @param compareColor
	 *            The compare color used to color this decorator.
	 * @param reference
	 *            The figure used as reference to draw this decorator.
	 * @param bounds
	 *            The bounds of this node decorator.
	 * @param isPhantom
	 *            True if it is the phantom version which has to be drawn, False for the marker version.
	 */
	public NodeFigure(Diff difference, boolean isThreeWay, ICompareColor compareColor, IFigure reference,
			Rectangle bounds, boolean isPhantom) {
		super(difference, isThreeWay, compareColor, reference, bounds, isPhantom);
	}

	/**
	 * Constructor.
	 * 
	 * @param difference
	 *            The difference related to this node decorator.
	 * @param isThreeWay
	 *            The kind of comparison.
	 * @param compareColor
	 *            The compare color used to color this decorator.
	 * @param reference
	 *            The figure used as reference to draw this decorator.
	 * @param bounds
	 *            The bounds of this node decorator.
	 * @param isPhantom
	 *            True if it is the phantom version which has to be drawn, False for the marker version.
	 * @param parameters
	 *            Additional parameters (potentially used by decorators inheriting from this one).
	 */
	public NodeFigure(Diff difference, boolean isThreeWay, ICompareColor compareColor, IFigure reference,
			Rectangle bounds, boolean isPhantom, Map<String, Object> parameters) {
		super(difference, isThreeWay, compareColor, reference, bounds, isPhantom, parameters);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.DecoratorFigure#buildFigureForPhantom()
	 */
	@Override
	protected void buildFigureForPhantom() {
		doBuildFigure();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.DecoratorFigure#buildFigureForMarker()
	 */
	@Override
	protected void buildFigureForMarker() {
		doBuildFigure();
	}

	/** It builds the decorator. */
	private void doBuildFigure() {
		getMainFigure().setBounds(getBounds());
		((Shape)getMainFigure()).setOpaque(false);
		((Shape)getMainFigure()).setFill(false);
		fChildDecoratorFigure = createDefaultFigure();
		fChildDecoratorFigure.setBounds(getBounds());
		getMainFigure().add(fChildDecoratorFigure);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.DecoratorFigure#highlightForMarker(org.eclipse.draw2d.IFigure)
	 */
	@Override
	protected void highlightForMarker(IFigure figure) {
		doHighlight(figure);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.DecoratorFigure#highlightForPhantom(org.eclipse.draw2d.IFigure)
	 */
	@Override
	protected void highlightForPhantom(IFigure figure) {
		doHighlight(figure);
	}

	/**
	 * It highlights the main figure of this node decorator.
	 * 
	 * @param figure
	 *            The main figure.
	 */
	private void doHighlight(IFigure figure) {
		Color strokeColor = getStrokeColor(true);
		((Shape)figure).setForegroundColor(strokeColor);
		((Shape)figure).setLineWidth(((Shape)figure).getLineWidth() + 1);

		fChildDecoratorFigure.setForegroundColor(strokeColor);
		fChildDecoratorFigure.setBackgroundColor(strokeColor);

		fChildDecoratorFigure.setAlpha(getAlpha());
		fChildDecoratorFigure.setFill(true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.DecoratorFigure#unhighlightForMarker(org.eclipse.draw2d.IFigure)
	 */
	@Override
	protected void unhighlightForMarker(IFigure figure) {
		doUnhighlight(figure);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.DecoratorFigure#unhighlightForPhantom(org.eclipse.draw2d.IFigure)
	 */
	@Override
	protected void unhighlightForPhantom(IFigure figure) {
		doUnhighlight(figure);
	}

	/**
	 * It unhighlights the main figure of this node decorator.
	 * 
	 * @param figure
	 *            The main figure.
	 */
	private void doUnhighlight(IFigure figure) {
		Color strokeColor = getStrokeColor(false);
		((Shape)figure).setForegroundColor(strokeColor);
		((Shape)figure).setLineWidth(((Shape)figure).getLineWidth() - 1);

		fChildDecoratorFigure.setForegroundColor(strokeColor);
		fChildDecoratorFigure.setBackgroundColor(strokeColor);

		fChildDecoratorFigure.setFill(false);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.DecoratorFigure#postFigureCreation()
	 */
	@Override
	protected void postFigureCreation() {
		if (hasToEnlarge()) {
			getBounds().x -= getDecoratorThickness();
			getBounds().y -= getDecoratorThickness();
			getBounds().width += getDecoratorThickness() * 2;
			getBounds().height += getDecoratorThickness() * 2;
		}
		super.postFigureCreation();
	}

}

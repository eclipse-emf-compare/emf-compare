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
 * Class which allows to manage the look of different figures related to decorators.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DecoratorFigure {

	/** Thickness of the decorator if used. */
	private static final int DECORATOR_THICKNESS = 6;

	/** Thickness of the decorator if used. */
	private static final int ALPHA = 30;

	/** The main figure representing this decorator. */
	private IFigure fMainDecoratorFigure;

	/** The compare color used to color this decorator. */
	private ICompareColor fCompareColor;

	/** The difference related to this decorator. */
	private Diff fDifference;

	/** The kind of comparison. */
	private boolean fIsThreeWay;

	/** State of this decorator in relation to the highlighting. */
	private boolean fIsHighlighted;

	/** It defines if this decorator represents a phantom or a marker. */
	private boolean fIsPhantom;

	/** The bounds of this decorator. */
	private Rectangle fBounds;

	/** The figure used as reference to draw this decorator. */
	private IFigure fReference;

	/** Additional parameters (potentially used by decorators inheriting from this one). */
	private Map<String, Object> fParameters;

	/**
	 * Constructor.
	 * 
	 * @param difference
	 *            The difference related to this decorator.
	 * @param isThreeWay
	 *            The kind of comparison.
	 * @param compareColor
	 *            The compare color used to color this decorator.
	 * @param referenceFigure
	 *            The figure used as reference to draw this decorator.
	 * @param decoratorBounds
	 *            The bounds of this decorator.
	 * @param isPhantom
	 *            True if it is the phantom version which has to be drawn, False for the marker version.
	 */
	public DecoratorFigure(Diff difference, boolean isThreeWay, ICompareColor compareColor,
			IFigure referenceFigure, Rectangle decoratorBounds, boolean isPhantom) {
		preFigureCreation(difference, isThreeWay, compareColor, isPhantom, decoratorBounds, referenceFigure,
				null);
		fMainDecoratorFigure = createFigure();
		postFigureCreation();
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
	 * @param referenceFigure
	 *            The figure used as reference to draw this decorator.
	 * @param decoratorBounds
	 *            The bounds of this decorator.
	 * @param isPhantom
	 *            True if it is the phantom version which has to be drawn, False for the marker version.
	 * @param parameters
	 *            Additional parameters (potentially used by decorators inheriting from this one).
	 */
	public DecoratorFigure(Diff difference, boolean isThreeWay, ICompareColor compareColor,
			IFigure referenceFigure, Rectangle decoratorBounds, boolean isPhantom,
			Map<String, Object> parameters) {
		preFigureCreation(difference, isThreeWay, compareColor, isPhantom, decoratorBounds, referenceFigure,
				parameters);
		fMainDecoratorFigure = createFigure();
		postFigureCreation();
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
	 * @param referenceFigure
	 *            The figure used as reference to draw this decorator.
	 * @param decoratorFigure
	 *            The figure to use as decorator.
	 * @param isPhantom
	 *            True if it is the phantom version which has to be drawn, False for the marker version.
	 */
	public DecoratorFigure(Diff difference, boolean isThreeWay, ICompareColor compareColor,
			IFigure referenceFigure, IFigure decoratorFigure, boolean isPhantom) {
		preFigureCreation(difference, isThreeWay, compareColor, isPhantom, decoratorFigure.getBounds()
				.getCopy(), referenceFigure, null);
		fMainDecoratorFigure = decoratorFigure;
		postFigureCreation();
	}

	/**
	 * It highlights the decorator figure if it is not already highlighted.
	 */
	public void highlight() {
		if (!fIsHighlighted && fMainDecoratorFigure != null) {
			highlight(fMainDecoratorFigure);
			fIsHighlighted = true;
		}
	}

	/**
	 * It unhilights the decorator figure if it is highlighted.
	 */
	public void unhighlight() {
		if (fIsHighlighted && fMainDecoratorFigure != null) {
			unhighlight(fMainDecoratorFigure);
			fIsHighlighted = false;
		}
	}

	/**
	 * Get the main figure representing this decorator.
	 * 
	 * @return The figure.
	 */
	public IFigure getMainFigure() {
		return fMainDecoratorFigure;
	}

	/**
	 * It builds the decorator figure.
	 */
	private void buildFigure() {
		if (isPhantom()) {
			buildFigureForPhantom();
		} else {
			buildFigureForMarker();
		}
	}

	/**
	 * It builds the figure to display a phantom. It is intended to be extended.
	 */
	protected void buildFigureForPhantom() {
	}

	/**
	 * It builds the figure to display a marker. It is intended to be extended.
	 */
	protected void buildFigureForMarker() {
	}

	/**
	 * It checks that the decorator figure has to be enlarged. By default, only markers have to be enlarged.
	 * 
	 * @return True if it has to be enlarged.
	 */
	protected boolean hasToEnlarge() {
		return !isPhantom();
	}

	/**
	 * It creates the figure related to the decorator to display.
	 * 
	 * @return The decorator figure.
	 */
	private IFigure createFigure() {
		if (isPhantom()) {
			return createFigureForPhantom();
		} else {
			return createFigureForMarker();
		}
	}

	/**
	 * It creates the figure corresponding to a phantom.
	 * 
	 * @return The phantom figure.
	 */
	protected IFigure createFigureForPhantom() {
		return createDefaultFigure();
	}

	/**
	 * It creates the figure corresponding to a marker.
	 * 
	 * @return The marker figure.
	 */
	protected IFigure createFigureForMarker() {
		return createDefaultFigure();
	}

/**
	 * It creates a {@link RectangleFigure) for phantoms or markers by default (if DecoratorFigure#createFigureForPhantom() or DecoratorFigure#createFigureForMarker() are not respectively overridden).
	 * @return a {@link RectangleFigure).
	 */
	protected RectangleFigure createDefaultFigure() {
		return new RectangleFigure();
	}

	/**
	 * It highlights the given figure.
	 * 
	 * @param figure
	 *            The figure to highlight.
	 */
	private void highlight(IFigure figure) {
		if (isPhantom()) {
			highlightForPhantom(figure);
		} else {
			highlightForMarker(figure);
		}
	}

	/**
	 * It unhighlights the given figure.
	 * 
	 * @param figure
	 *            The figure to unhilight.
	 */
	private void unhighlight(IFigure figure) {
		if (isPhantom()) {
			unhighlightForPhantom(figure);
		} else {
			unhighlightForMarker(figure);
		}
	}

	/**
	 * It highlights the given figure corresponding to a phantom.
	 * 
	 * @param figure
	 *            The phantom.
	 */
	protected void highlightForPhantom(IFigure figure) {
		defaultHighlightForPhantom(figure);
	}

	/**
	 * It highlights, by default, the given figure corresponding to a phantom.
	 * 
	 * @param figure
	 *            The phantom.
	 */
	protected void defaultHighlightForPhantom(IFigure figure) {
		if (figure instanceof Shape) {
			((Shape)figure).setLineWidth(((Shape)figure).getLineWidth() + 1);
		}
		Color strokeColor = getStrokeColor(true);
		figure.setForegroundColor(strokeColor);
		figure.setBackgroundColor(strokeColor);
	}

	/**
	 * It highlights the given figure corresponding to a marker.
	 * 
	 * @param figure
	 *            The marker.
	 */
	protected void highlightForMarker(IFigure figure) {
		defaultHighlightForMarker(figure);
	}

	/**
	 * It highlights, by default, the given figure corresponding to a marker.
	 * 
	 * @param figure
	 *            The marker.
	 */
	protected void defaultHighlightForMarker(IFigure figure) {
		defaultHighlightForPhantom(figure);
		((Shape)figure).setAlpha(getAlpha());
	}

	/**
	 * It unhighlights the given figure corresponding to a phantom.
	 * 
	 * @param figure
	 *            The phantom.
	 */
	protected void unhighlightForPhantom(IFigure figure) {
		defaultUnhighlightForPhantom(figure);
	}

	/**
	 * It unhighlights, by default, the given figure corresponding to a phantom.
	 * 
	 * @param figure
	 *            The phantom.
	 */
	protected void defaultUnhighlightForPhantom(IFigure figure) {
		if (figure instanceof Shape) {
			((Shape)figure).setLineWidth(((Shape)figure).getLineWidth() - 1);
		}
		Color strokeColor = getStrokeColor(false);
		figure.setForegroundColor(strokeColor);
		figure.setBackgroundColor(strokeColor);
	}

	/**
	 * It unhighlights the given figure corresponding to a marker.
	 * 
	 * @param figure
	 *            The marker.
	 */
	protected void unhighlightForMarker(IFigure figure) {
		defaultUnhighlightForMarker(figure);
	}

	/**
	 * It unhighlights, by default, the given figure corresponding to a marker.
	 * 
	 * @param figure
	 *            The marker.
	 */
	protected void defaultUnhighlightForMarker(IFigure figure) {
		defaultUnhighlightForPhantom(figure);
	}

	protected Map<String, Object> getParameters() {
		return fParameters;
	}

	protected int getAlpha() {
		return ALPHA;
	}

	protected int getDecoratorThickness() {
		return DECORATOR_THICKNESS;
	}

	protected Rectangle getBounds() {
		return fBounds;
	}

	protected IFigure getReference() {
		return fReference;
	}

	protected boolean isPhantom() {
		return fIsPhantom;
	}

	/**
	 * Get the color according to the given flag.
	 * 
	 * @param isHighlight
	 *            True to get the color for a shape highlighted, false otherwise.
	 * @return The color.
	 */
	protected Color getStrokeColor(boolean isHighlight) {
		if (isHighlight) {
			return fCompareColor.getStrokeColor(fDifference, fIsThreeWay, false, true);
		} else {
			return fCompareColor.getStrokeColor(fDifference, fIsThreeWay, false, false);
		}
	}

	/**
	 * Initialize the decorator according to the given parameters, before the creation of the figure.
	 * 
	 * @param difference
	 *            The difference related to this decorator.
	 * @param isThreeWay
	 *            The kind of comparison.
	 * @param compareColor
	 *            The compare color used to color this decorator.
	 * @param isPhantom
	 *            True if it is the phantom version which has to be drawn, False for the marker version.
	 * @param bounds
	 *            The bounds of this decorator.
	 * @param reference
	 *            The figure used as reference to draw this decorator.
	 * @param parameters
	 *            Additional parameters (potentially used by decorators inheriting from this one).
	 */
	private void preFigureCreation(Diff difference, boolean isThreeWay, ICompareColor compareColor,
			boolean isPhantom, Rectangle bounds, IFigure reference, Map<String, Object> parameters) {
		fDifference = difference;
		fIsThreeWay = isThreeWay;
		fCompareColor = compareColor;
		fIsPhantom = isPhantom;
		fBounds = bounds;
		fReference = reference;
		fParameters = parameters;
	}

	/**
	 * Initialize the decorator, after the creation of the figure.
	 */
	protected void postFigureCreation() {
		buildFigure();
		highlight();
	}

}

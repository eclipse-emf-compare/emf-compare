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

import org.eclipse.draw2d.AbstractPointListShape;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polygon;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.ICompareColor;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg.Sign;
import org.eclipse.swt.graphics.Color;

/**
 * Class which allows to manage the look of edges decorators.<br>
 * Phantoms are simple lines with bendpoints. Markers are polygons around the focused edge.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class EdgeFigure extends DecoratorFigure {

	/** A child figure to fill background of the polygon with a transparent color (for marker). */
	private AbstractPointListShape fChildMarkerFigure;

	/**
	 * Constructor.
	 * 
	 * @param difference
	 *            The difference related to this edge decorator.
	 * @param isThreeWay
	 *            The kind of comparison.
	 * @param compareColor
	 *            The compare color used to color this decorator.
	 * @param reference
	 *            The figure used as reference to draw this decorator.
	 * @param bounds
	 *            The bounds of this edge decorator.
	 * @param isPhantom
	 *            True if it is the phantom version which has to be drawn, False for the marker version.
	 */
	public EdgeFigure(Diff difference, boolean isThreeWay, ICompareColor compareColor, IFigure reference,
			Rectangle bounds, boolean isPhantom) {
		super(difference, isThreeWay, compareColor, reference, bounds, isPhantom);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.DecoratorFigure#createFigureForPhantom()
	 *      It draws a simple line (with bend points) for the phantom.
	 */
	@Override
	protected AbstractPointListShape createFigureForPhantom() {
		return new PolylineConnection();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.DecoratorFigure#createFigureForMarker()
	 *      It creates a polygon shape around the object of interest (an edge for i.e.).
	 */
	@Override
	protected AbstractPointListShape createFigureForMarker() {
		return new Polygon();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.DecoratorFigure#buildFigureForPhantom()
	 *      It retrieves the bend points from the reference figure to draw the phantom.
	 */
	@Override
	protected void buildFigureForPhantom() {
		if (getReference() instanceof PolylineConnection) {
			((PolylineConnection)getMainFigure()).setPoints(((PolylineConnection)getReference()).getPoints()
					.getCopy());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.DecoratorFigure#buildFigureForMarker()
	 *      It buils the marker thanks to 2 figures: the main one to define contours and a child one to fill
	 *      the shape with a transparent color.
	 */
	@Override
	protected void buildFigureForMarker() {
		((Shape)getMainFigure()).setOpaque(false);
		((Shape)getMainFigure()).setFill(false);
		buildPolygon((AbstractPointListShape)getMainFigure());
		fChildMarkerFigure = createFigureForMarker();
		buildPolygon(fChildMarkerFigure);
		getMainFigure().add(fChildMarkerFigure);
	}

	/**
	 * It builds the polygon figure (for the marker).
	 * 
	 * @param polygon
	 *            the polygon figure.
	 */
	private void buildPolygon(AbstractPointListShape polygon) {
		PointList refPoints = ((PolylineConnection)getReference()).getPoints();
		int nbPoints = refPoints.size();
		PointList targetPoints = new PointList();

		LineSeg previousTargetSeg = null;
		for (int i = 0; i < nbPoints; i++) {
			Point start = refPoints.getPoint(i);
			if (i + 1 < nbPoints) {
				Point end = refPoints.getPoint(i + 1);
				LineSeg segRef = new LineSeg(start, end);
				previousTargetSeg = computePoints(targetPoints, previousTargetSeg, segRef);
			} else if (previousTargetSeg != null) {
				targetPoints.addPoint(previousTargetSeg.getTerminus());
			}
		}

		previousTargetSeg = null;
		for (int i = nbPoints - 1; i >= 0; i--) {
			Point start = refPoints.getPoint(i);
			if (i - 1 >= 0) {
				Point end = refPoints.getPoint(i - 1);
				LineSeg segRef = new LineSeg(start, end);
				previousTargetSeg = computePoints(targetPoints, previousTargetSeg, segRef);
			} else if (previousTargetSeg != null) {
				targetPoints.addPoint(previousTargetSeg.getTerminus());
			}
		}

		polygon.setPoints(targetPoints);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.DecoratorFigure#highlightForMarker(org.eclipse.draw2d.IFigure)
	 */
	@Override
	protected void highlightForMarker(IFigure figure) {
		Color strokeColor = getStrokeColor(true);
		((AbstractPointListShape)figure).setForegroundColor(strokeColor);
		((AbstractPointListShape)figure).setLineWidth(((Shape)figure).getLineWidth() + 1);

		fChildMarkerFigure.setForegroundColor(strokeColor);
		fChildMarkerFigure.setBackgroundColor(strokeColor);

		fChildMarkerFigure.setAlpha(getAlpha());
		fChildMarkerFigure.setFill(true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.DecoratorFigure#unhighlightForMarker(org.eclipse.draw2d.IFigure)
	 */
	@Override
	protected void unhighlightForMarker(IFigure figure) {
		Color strokeColor = getStrokeColor(false);
		((AbstractPointListShape)figure).setForegroundColor(strokeColor);
		((AbstractPointListShape)figure).setLineWidth(((Shape)figure).getLineWidth() - 1);

		fChildMarkerFigure.setForegroundColor(strokeColor);
		fChildMarkerFigure.setBackgroundColor(strokeColor);

		fChildMarkerFigure.setFill(false);
	}

	/**
	 * Compute the points to add to the polygon being drawn, from the given line segment (<code>lineSeg</code>
	 * ) of the focused edge. The points are added in the given point list.
	 * 
	 * @param targetPoints
	 *            The list of polygon points updated by the call of this method.
	 * @param previousTargetSeg
	 *            The previous computed polygon segment.
	 * @param segRef
	 *            The current line segment of the focused edge.
	 * @return A new computed segment for the polygon.
	 */
	private LineSeg computePoints(PointList targetPoints, LineSeg previousTargetSeg, LineSeg segRef) {
		// Compute a parallel segment to the given one (segRef)
		Point target = segRef.locatePoint(0.0, getDecoratorThickness(), Sign.POSITIVE);
		LineSeg targetSeg = segRef.getParallelLineSegThroughPoint(target);

		if (previousTargetSeg != null) {
			Point instersection = targetSeg.intersect(previousTargetSeg, 1);
			if (instersection != null) {
				// This segment intersects with the previous one
				targetPoints.addPoint(instersection);
			} else {
				// This segment does not intersect with the previous one
				targetPoints.addPoint(previousTargetSeg.getTerminus());
				targetPoints.addPoint(targetSeg.getOrigin());
			}
		} else {
			// It is the fist point of the focused edge.
			targetPoints.addPoint(targetSeg.getOrigin());
		}
		return targetSeg;
	}

}

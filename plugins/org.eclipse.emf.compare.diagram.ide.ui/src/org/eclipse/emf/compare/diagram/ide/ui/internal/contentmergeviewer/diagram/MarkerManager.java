/*******************************************************************************
 * Copyright (c) 2020 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram;

import static com.google.common.base.Predicates.instanceOf;

import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.DecoratorFigure;
import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.EdgeFigure;
import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.NodeFigure;
import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.NodeListFigure;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.ICompareColor;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Marker manager to create, hide or reveal marker figures related to deleted or added graphical objects.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class MarkerManager extends AbstractDecoratorManager {

	/**
	 * Marker represented by a <code>figure</code> on a <code>layer</code>, from the given <code>side</code>
	 * of the merge viewer. An edit part may be linked to the <code>figure</code> in some cases.<br>
	 * The marker is related to a <code>difference</code> and it is binded with the reference view and figure.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	private class Marker extends AbstractDecorator {

		/**
		 * Constructor.
		 * 
		 * @param layer
		 *            {@link Marker#fLayer}.
		 * @param side
		 *            {@link Marker#fSide}.
		 * @param originView
		 *            {@link Marker#fOriginView}.
		 * @param originFigure
		 *            {@link Marker#fOriginFigure}.
		 * @param diff
		 *            {@link Marker#fDifference}.
		 */
		Marker(IFigure layer, MergeViewerSide side, View originView, IFigure originFigure, Diff diff) {
			setLayer(layer);
			setSide(side);
			setOriginView(originView);
			setOriginFigure(originFigure);
			setDifference(diff);
		}
	}

	/** Registry of created markers, indexed by difference. */
	private Multimap<Diff, Marker> fMarkerRegistry = HashMultimap.create();

	/**
	 * Constructor.
	 * 
	 * @param compareConfiguration
	 *            The compare configuration of the viewer.
	 * @param left
	 *            The left area of the viewer.
	 * @param right
	 *            The right area of the viewer.
	 * @param ancestor
	 *            The ancestor area of the viewer.
	 * @param color
	 *            The color of the difference.
	 */
	public MarkerManager(EMFCompareConfiguration compareConfiguration, DiagramMergeViewer left,
			DiagramMergeViewer right, DiagramMergeViewer ancestor, ICompareColor color) {
		super(compareConfiguration, left, right, ancestor, color);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager#getReferenceViews(
	 *      org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff)
	 */
	@Override
	protected List<View> getReferenceViews(DiagramDiff difference) {
		List<View> result = new ArrayList<View>();
		Match matchValue = getCompareConfiguration().getComparison().getMatch(difference.getView());
		if (matchValue != null) {
			if (matchValue.getLeft() != null) {
				result.add((View)matchValue.getLeft());
			}
			if (matchValue.getRight() != null) {
				result.add((View)matchValue.getRight());
			}
			if (getCompareConfiguration().getComparison().isThreeWay()) {
				switch (difference.getKind()) {
					case DELETE:
					case CHANGE:
					case MOVE:
						result.add((View)matchValue.getOrigin());
						break;
					default:
						break;
				}
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager#getTargetSide(org.eclipse.emf.compare.Match,
	 *      org.eclipse.gmf.runtime.notation.View)
	 */
	@Override
	public MergeViewerSide getTargetSide(Match match, View referenceView) {
		return getSide(referenceView);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager#createAndRegisterDecorator(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.gmf.runtime.notation.View, org.eclipse.draw2d.IFigure,
	 *      org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide)
	 */
	@Override
	protected Marker createAndRegisterDecorator(Diff diff, View referenceView, IFigure referenceFigure,
			MergeViewerSide targetSide) {
		Marker marker = createMarker(diff, referenceView, referenceFigure, targetSide);
		if (marker != null) {
			fMarkerRegistry.put(diff, marker);
		}
		return marker;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager#removeDecorators(org.eclipse.emf.compare.Diff)
	 */
	@Override
	public void removeDecorators(Diff difference) {
		fMarkerRegistry.removeAll(difference);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager#removeAll()
	 */
	@Override
	public void removeAll() {
		fMarkerRegistry.clear();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager#getDecorators(org.eclipse.emf.compare.Diff)
	 */
	@Override
	protected Collection<Marker> getDecorators(Diff difference) {
		return fMarkerRegistry.get(difference);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager#handleAddDecorator(org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager.AbstractDecorator,
	 *      org.eclipse.draw2d.IFigure, org.eclipse.draw2d.IFigure, boolean)
	 */
	@Override
	protected void handleAddDecorator(AbstractDecorator decorator, IFigure parent, IFigure toAdd,
			boolean isMain) {
		super.handleAddDecorator(decorator, parent, toAdd, isMain);
		DiagramMergeViewer viewer = getViewer(decorator.getSide());
		EditPart editPart = viewer.getEditPart(decorator.getOriginView());
		if (editPart != null) {
			viewer.getGraphicalViewer().reveal(editPart);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager#goodCandidate()
	 *      <br>
	 *      All graphical differences are concerned.
	 */
	@Override
	protected Predicate<Diff> goodCandidate() {
		return new Predicate<Diff>() {
			public boolean apply(Diff difference) {
				return instanceOf(DiagramDiff.class).apply(difference);
			}
		};
	}

	/**
	 * It creates a new marker from the given difference, view and figure.
	 * 
	 * @param diff
	 *            The related difference used as index for the main marker.
	 * @param referenceView
	 *            The reference view as base for creation of the marker.
	 * @param referenceFigure
	 *            The reference figure as base for creation of the marker.
	 * @param side
	 *            The side where the marker has to be created.
	 * @return The phantom or null if the target layer is not found.
	 */
	private Marker createMarker(Diff diff, View referenceView, IFigure referenceFigure,
			MergeViewerSide side) {

		IFigure referenceLayer = getLayer(referenceView, side);
		if (referenceLayer != null) {
			Rectangle referenceBounds = referenceFigure.getBounds().getCopy();
			translateCoordinates(referenceFigure, referenceLayer, referenceBounds);

			DecoratorFigure markerFigure = null;

			Marker marker = new Marker(referenceLayer, side, referenceView, referenceFigure, diff);

			if (isNodeList(referenceView)) {
				markerFigure = new NodeListFigure(diff, isThreeWay(), getCompareColor(), referenceFigure,
						referenceBounds, false);
			} else if (referenceView instanceof Edge && referenceFigure instanceof PolylineConnection) {
				markerFigure = new EdgeFigure(diff, isThreeWay(), getCompareColor(), referenceFigure,
						referenceBounds, false);
			}

			// Default case: Nodes
			if (markerFigure == null) {
				markerFigure = new NodeFigure(diff, isThreeWay(), getCompareColor(), referenceFigure,
						referenceBounds, false);
			}

			marker.setDecoratorFigure(markerFigure);
			return marker;
		}

		return null;

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.IDecoratorManager#hideAll()
	 */
	public void hideAll() {
		for (Marker marker : fMarkerRegistry.values()) {
			handleDeleteDecorator(marker, marker.getLayer(), marker.getFigure());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.IDecoratorManager#getAllDecorators()
	 */
	public Collection<AbstractDecorator> getAllDecorators() {
		Collection<AbstractDecorator> markers = new ArrayList<AbstractDecorator>();
		for (AbstractDecorator marker : fMarkerRegistry.values()) {
			markers.add(marker);
		}
		return markers;
	}

}

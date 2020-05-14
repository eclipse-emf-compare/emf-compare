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
package org.eclipse.emf.compare.diagram.ide.ui.sirius.internal.tools.image;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager.AbstractDecorator;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.sirius.diagram.ui.tools.internal.render.SiriusDiagramImageGenerator;
import org.eclipse.swt.graphics.Rectangle;

/**
 * This class is used to draw the {@link DiagramEditPart} in an image. Any existing Phantoms and Markers
 * decorators in the ContentMergeViewer are added to the generated images.
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
@SuppressWarnings("restriction")
public class EMFCompareDiagramImageGenerator extends SiriusDiagramImageGenerator {

	/**
	 * The {@link AbstractDecorator}s to draw.
	 */
	private List<AbstractDecorator> decorators;

	/**
	 * The layer on which the {@link AbstractDecorator}s are drawn.
	 */
	private IFigure layer;

	/**
	 * Constructor.
	 * 
	 * @param dgrmEP
	 *            the {@link DiagramEditPart} to draw.
	 * @param abstractDecorators
	 *            the list of all {@link AbstractDecorator}s
	 * @param side
	 *            the list of all decorators on each side of the ContentMergeViewer.
	 */
	public EMFCompareDiagramImageGenerator(DiagramEditPart dgrmEP,
			Collection<AbstractDecorator> abstractDecorators, MergeViewerSide side) {
		super(dgrmEP);
		decorators = new ArrayList<>();
		for (AbstractDecorator decorator : abstractDecorators) {
			if (decorator.getSide() == side) {
				decorators.add(decorator);
			}
		}
		layer = getLayer();
	}

	/**
	 * Get the layer on which the {@link DiagramEditPart} is represented on the ContentMergeViewer.
	 * 
	 * @return the returns the layer corresponding to the {@link DiagramEditPart}.
	 */
	protected IFigure getLayer() {
		return LayerManager.Helper.find(getDiagramEditPart()).getLayer(LayerConstants.PRINTABLE_LAYERS);
	}

	/**
	 * {@inheritDoc} Overridden to draw the {@link AbstractDecorator}s.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected void renderToGraphics(Graphics graphics, Point translateOffset, List editparts) {
		super.renderToGraphics(graphics, translateOffset, editparts);
		for (AbstractDecorator decorator : decorators) {
			paintFigure(graphics, decorator.getFigure());
		}
	}

	/**
	 * {@inheritDoc} Overridden to get the layer size.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Rectangle calculateImageRectangle(List editparts) {
		return new Rectangle(layer.getBounds().x, layer.getBounds().y, layer.getBounds().width,
				layer.getBounds().height);
	}
}

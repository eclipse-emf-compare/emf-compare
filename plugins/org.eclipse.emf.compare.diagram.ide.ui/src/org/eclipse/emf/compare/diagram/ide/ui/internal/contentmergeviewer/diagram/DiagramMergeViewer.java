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
package org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram;

import java.util.Collection;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.compare.diagram.ide.ui.AbstractGraphicalMergeViewer;
import org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramNodeAccessor;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.EditPartService;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * The graphical viewer on each side of the compare viewer.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
class DiagramMergeViewer extends AbstractGraphicalMergeViewer {

	/** the zoom factor of displayed diagrams. */
	private static final double ZOOM_FACTOR = 1;

	/** The input of the viewer. */
	private IDiagramNodeAccessor fInput;

	/** The graphical viewer. */
	private DiagramGraphicalViewerForCompare fGraphicalViewer;

	/** the current diagram used. */
	private Diagram currentDiag;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            The parent composite.
	 * @param side
	 *            The side having to be managed by this viewer.
	 */
	public DiagramMergeViewer(Composite parent, MergeViewerSide side) {
		super(parent, side);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.MergeViewer#createControl(org.eclipse.swt.widgets.Composite)
	 */
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createControl(Composite parent) {
		createDiagramGraphicalViewer(parent);
		return fGraphicalViewer.getControl();
	}

	/**
	 * It creates and initialize the graphical viewer.
	 * 
	 * @param composite
	 *            The composite.
	 */
	private void createDiagramGraphicalViewer(Composite composite) {
		fGraphicalViewer = new DiagramGraphicalViewerForCompare();
		fGraphicalViewer.createControl(composite);
		fGraphicalViewer.setEditDomain(editDomain);
		fGraphicalViewer.setEditPartFactory(EditPartService.getInstance());
		fGraphicalViewer.getControl().setBackground(ColorConstants.listBackground);
		fGraphicalViewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.CTRL),
				MouseWheelZoomHandler.SINGLETON);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.AbstractGraphicalMergeViewer#getGraphicalViewer()
	 */
	@Override
	public DiagramGraphicalViewerForCompare getGraphicalViewer() {
		return fGraphicalViewer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.AbstractEditPartMergeViewer#setInput(java.lang.Object)
	 */
	@Override
	public void setInput(final Object input) {
		if (input instanceof IDiagramNodeAccessor) {
			fInput = (IDiagramNodeAccessor)input;
			Diagram diagram = ((IDiagramNodeAccessor)input).getOwnedDiagram();
			View view = ((IDiagramNodeAccessor)input).getOwnedView();

			initEditingDomain(diagram);

			// Selection
			fGraphicalViewer.deselectAll();
			if (view != null) {

				EditPart viewPart = getEditPart(view);

				if (viewPart != null) {

					while (!viewPart.isSelectable()) {
						viewPart = viewPart.getParent();
					}

					setSelection(new StructuredSelection(viewPart));
					getGraphicalViewer().reveal(viewPart);

				}

			}
		}
	}

	/**
	 * It creates an editing domain for this diagram.
	 * 
	 * @param diagram
	 *            The diagram.
	 */
	private void initEditingDomain(Diagram diagram) {
		ResourceSet resourceSet = null;
		if (diagram != null) {
			resourceSet = diagram.eResource().getResourceSet();
		}
		if (resourceSet != null
				&& TransactionalEditingDomain.Factory.INSTANCE.getEditingDomain(resourceSet) == null) {
			TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain(resourceSet);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gef.EditPartViewer#findObjectAtExcluding(org.eclipse.draw2d.geometry.Point,
	 *      java.util.Collection, org.eclipse.gef.EditPartViewer.Conditional)
	 */
	public EditPart findObjectAtExcluding(Point location, Collection exclusionSet, Conditional conditional) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Get the editpart from the given view.
	 * 
	 * @param view
	 *            The view.
	 * @return The editpart.
	 */
	public EditPart getEditPart(final EObject view) {
		final EditPart editPart = (EditPart)fGraphicalViewer.getEditPartRegistry().get(view);
		if (editPart == null) {
			Diagram diagram = null;
			if (view instanceof Diagram) {
				diagram = (Diagram)view;
			} else if (view instanceof View) {
				diagram = ((View)view).getDiagram();
			}
			if (diagram != null && !diagram.equals(currentDiag)) {
				currentDiag = diagram;
				fGraphicalViewer.getEditPartRegistry().clear();
				final DiagramRootEditPart rootEditPart = new DiagramRootEditPart(diagram.getMeasurementUnit());
				fGraphicalViewer.setRootEditPart(rootEditPart);
				fGraphicalViewer.setContents(diagram);
				disableEditMode((DiagramEditPart)fGraphicalViewer.getContents());
				rootEditPart.getZoomManager().setZoomAnimationStyle(ZoomManager.ANIMATE_NEVER);
				rootEditPart.getZoomManager().setZoom(ZOOM_FACTOR);
			}
		}
		return (EditPart)fGraphicalViewer.getEditPartRegistry().get(view);
	}

	/**
	 * It disables editing actions from the given editpart.
	 * 
	 * @param diagEditPart
	 *            The editpart.
	 */
	private void disableEditMode(DiagramEditPart diagEditPart) {
		diagEditPart.disableEditMode();
		for (Object obj : diagEditPart.getPrimaryEditParts()) {
			if (obj instanceof IGraphicalEditPart) {
				disableEditMode((IGraphicalEditPart)obj);
			}
		}
	}

	/**
	 * It disables editing actions from the given editpart.
	 * 
	 * @param obj
	 *            The editpart.
	 */
	private void disableEditMode(IGraphicalEditPart obj) {
		obj.disableEditMode();
		obj.removeEditPolicy(EditPolicyRoles.OPEN_ROLE);
		for (Object child : obj.getChildren()) {
			if (child instanceof IGraphicalEditPart) {
				disableEditMode((IGraphicalEditPart)child);
			}
		}
	}

	/**
	 * {@link DiagramGraphicalViewer} which enables to make a reveal on a given figure, without editpart.
	 * FIXME: Find a better solution to make the reveal.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	public class DiagramGraphicalViewerForCompare extends DiagramGraphicalViewer {

		/**
		 * It reveals the given figure. It is a partial copy of
		 * {@link DiagramGraphicalViewer#reveal(EditPart)}.
		 * 
		 * @param figure
		 *            The figure.
		 */
		public void reveal(IFigure figure) {
			IFigure target = figure;
			Viewport port = getFigureCanvas().getViewport();
			Rectangle exposeRegion = target.getBounds().getCopy();
			target = target.getParent();
			while (target != null && target != port) {
				target.translateToParent(exposeRegion);
				target = target.getParent();
			}
			exposeRegion.expand(5, 5);

			Dimension viewportSize = port.getClientArea().getSize();

			Point topLeft = exposeRegion.getTopLeft();
			Point bottomRight = exposeRegion.getBottomRight().translate(viewportSize.getNegated());
			Point finalLocation = new Point();
			if (viewportSize.width < exposeRegion.width) {
				finalLocation.x = Math.min(bottomRight.x, Math.max(topLeft.x, port.getViewLocation().x));
			} else {
				finalLocation.x = Math.min(topLeft.x, Math.max(bottomRight.x, port.getViewLocation().x));
			}

			if (viewportSize.height < exposeRegion.height) {
				finalLocation.y = Math.min(bottomRight.y, Math.max(topLeft.y, port.getViewLocation().y));
			} else {
				finalLocation.y = Math.min(topLeft.y, Math.max(bottomRight.y, port.getViewLocation().y));
			}

			getFigureCanvas().scrollSmoothTo(finalLocation.x, finalLocation.y);

		}

	}

}

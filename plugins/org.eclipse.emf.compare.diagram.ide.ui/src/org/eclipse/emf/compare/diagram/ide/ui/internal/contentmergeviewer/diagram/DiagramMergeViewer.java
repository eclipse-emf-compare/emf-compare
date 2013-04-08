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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.compare.diagram.ide.ui.internal.AbstractGraphicalMergeViewer;
import org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramDiffAccessor;
import org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramNodeAccessor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.SelectionManager;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.parts.DomainEventDispatcher;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.EditPartService;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * The graphical viewer on each side of the compare viewer.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
class DiagramMergeViewer extends AbstractGraphicalMergeViewer {

	/**
	 * Selection manager to forbid manual selections on graphical objects.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	protected class NonEditingManager extends SelectionManager {

		@Override
		public void appendSelection(EditPart editpart) {
			// needed to disable manual selection on every objects.
		}
	}

	/**
	 * Event dispatcher to forbid external mouse actions on diagrams.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	protected class NonEditingEventDispatcher extends DomainEventDispatcher {

		/**
		 * Constructor.
		 * 
		 * @param d
		 *            The edit domain.
		 * @param v
		 *            The edit part viewer.
		 */
		public NonEditingEventDispatcher(EditDomain d, EditPartViewer v) {
			super(d, v);
		}

		@Override
		public void dispatchMousePressed(MouseEvent me) {
			// needed not to get a change of the cursor releasing from a connector.
		}

		@Override
		public void dispatchMouseReleased(MouseEvent me) {
			// needed not to get a change of the cursor releasing from a connector.
		}

		@Override
		public void dispatchMouseDoubleClicked(MouseEvent me) {
			// needed not to get a change of the cursor double-clicking on a connector.
		}

		@Override
		public void dispatchMouseHover(MouseEvent me) {
			// needed not to get a change of the cursor hovering a connector.
		}

		@Override
		public void dispatchMouseMoved(MouseEvent me) {
			// needed not to get a change of the cursor hovering a connector.
		}

	}

	/** the zoom factor of displayed diagrams. */
	private static final double ZOOM_FACTOR = 1;

	/** The graphical viewer. */
	private DiagramGraphicalViewerForCompare fGraphicalViewer;

	/** the current diagram used. */
	private Diagram currentDiag;

	/** The diagram edit domain. */
	private DiagramEditDomain editDomain;

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
		((FigureCanvas)fGraphicalViewer.getControl()).getLightweightSystem().setEventDispatcher(
				new NonEditingEventDispatcher(editDomain, fGraphicalViewer));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.AbstractGraphicalMergeViewer#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createControl(Composite parent) {
		editDomain = new DiagramEditDomain(null);
		editDomain.setCommandStack(new DiagramCommandStack(editDomain));
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

		fGraphicalViewer.setSelectionManager(new NonEditingManager());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.AbstractGraphicalMergeViewer#getGraphicalViewer()
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
			Diagram diagram = ((IDiagramNodeAccessor)input).getOwnedDiagram();
			if (diagram != null) {
				initEditingDomain(diagram);
				EditPart editPart = null;
				View view = ((IDiagramNodeAccessor)input).getOwnedView();
				if (view != null) {
					editPart = getEditPart(view);
					if (editPart == null) {
						editPart = initializeEditParts(view);
					}
				}
				fGraphicalViewer.deselectAll();
				// Selection only on matches.
				if (!(input instanceof IDiagramDiffAccessor) && editPart != null) {
					setSelection(editPart);
				}
			} else {
				fGraphicalViewer.setContents((EditPart)null);
			}
			currentDiag = diagram;
		}
	}

	/**
	 * Set the selection of the first selectable object from the given edit part.
	 * 
	 * @param editPart
	 *            The edit part.
	 */
	private void setSelection(EditPart editPart) {
		EditPart ep = editPart;
		while (ep != null && !ep.isSelectable()) {
			ep = ep.getParent();
		}

		if (ep != null) {
			setSelection(new StructuredSelection(ep));
			fGraphicalViewer.reveal(ep);
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
	 * Return the editpart from the given view.
	 * 
	 * @param view
	 *            The view.
	 * @return The editpart.
	 */
	public EditPart getEditPart(final EObject view) {
		return (EditPart)fGraphicalViewer.getEditPartRegistry().get(view);
	}

	private EditPart initializeEditParts(final EObject view) {
		Diagram diagram = null;
		if (view instanceof Diagram) {
			diagram = (Diagram)view;
		} else if (view instanceof View) {
			diagram = ((View)view).getDiagram();
		}

		if (diagram != null) {
			// fGraphicalViewer.getEditPartRegistry().clear();
			final DiagramRootEditPart rootEditPart = new DiagramRootEditPart(diagram.getMeasurementUnit());
			rootEditPart.getZoomManager().setZoomAnimationStyle(ZoomManager.ANIMATE_NEVER);
			rootEditPart.getZoomManager().setZoom(ZOOM_FACTOR);
			fGraphicalViewer.setRootEditPart(rootEditPart);
			fGraphicalViewer.setContents(diagram);
		}
		return (EditPart)fGraphicalViewer.getEditPartRegistry().get(view);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#refresh()
	 */
	@Override
	public void refresh() {

	}

	/**
	 * {@link DiagramGraphicalViewer} which enables to make a reveal on a given figure, without editpart.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	public class DiagramGraphicalViewerForCompare extends DiagramGraphicalViewer {

		/**
		 * It reveals the given figure. It is a partial copy of
		 * {@link DiagramGraphicalViewer#reveal(EditPart)}.<br>
		 * FIXME: Find a better solution to make the reveal.
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

		@Override
		protected DomainEventDispatcher getEventDispatcher() {
			return new NonEditingEventDispatcher(editDomain, this);
		}

	}

}

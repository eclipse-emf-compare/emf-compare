/*******************************************************************************
 * Copyright (c) 2012 Obeo.
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
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.compare.diagram.ide.ui.GraphicalMergeViewer;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor.IEObjectAccessor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain.Factory;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.rulers.RulerComposite;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.EditPartService;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

class DiagramMergeViewer extends GraphicalMergeViewer {

	private IEObjectAccessor fInput;

	private DiagramGraphicalViewer fGraphicalViewer;

	/** the zoom factor of displayed diagrams. */
	private static final double ZOOM_FACTOR = 0.7;

	/** the current diagram used. */
	private Diagram currentDiag;

	/**
	 * @param parent
	 */
	public DiagramMergeViewer(Composite parent, MergeViewerSide side) {
		super(parent, side);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.MergeViewer#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createControl(Composite parent) {
		// fGraphicalViewer = new DiagramGraphicalViewer();
		// fGraphicalViewer.createControl(parent);
		// fGraphicalViewer.setEditDomain(editDomain);
		// fGraphicalViewer.setEditPartFactory(EditPartService.getInstance());
		// fGraphicalViewer.getControl().setBackground(ColorConstants.listBackground);
		// fGraphicalViewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.CTRL),
		// MouseWheelZoomHandler.SINGLETON);
		// return fGraphicalViewer.getControl();

		// return createGraphicalViewer(parent);
		createDiagramGraphicalViewer(parent);
		return fGraphicalViewer.getControl();
	}

	protected Control createGraphicalViewer(Composite parent) {
		RulerComposite rulerComposite = new RulerComposite(parent, SWT.NONE);

		ScrollingGraphicalViewer sGViewer = new DiagramGraphicalViewer();
		sGViewer.createControl(rulerComposite);

		editDomain.addViewer(sGViewer);
		fGraphicalViewer = (DiagramGraphicalViewer)sGViewer;

		rulerComposite.setGraphicalViewer((ScrollingGraphicalViewer)getGraphicalViewer());

		return rulerComposite;
	}

	private void createDiagramGraphicalViewer(Composite composite) {
		fGraphicalViewer = new DiagramGraphicalViewer();
		fGraphicalViewer.createControl(composite);
		fGraphicalViewer.setEditDomain(editDomain);
		fGraphicalViewer.setEditPartFactory(EditPartService.getInstance());
		fGraphicalViewer.getControl().setBackground(ColorConstants.listBackground);
		fGraphicalViewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.CTRL),
				MouseWheelZoomHandler.SINGLETON);
	}

	protected void initializeGraphicalViewerContents() {
		getGraphicalViewer().setContents(getDiagram());
		initializeContents(getGraphicalViewer().getContents());
	}

	private void initializeContents(EditPart editpart) {
		((DiagramRootEditPart)getGraphicalViewer().getRootEditPart()).getZoomManager().setZoom(ZOOM_FACTOR);

		// ((DiagramEditPart)editpart).refreshPageBreaks();
		//
		// // Update the range model of the viewport
		// ((DiagramEditPart)editpart).getViewport().validate();
		//
		// // Get the Guide Style
		// GuideStyle guideStyle =
		// (GuideStyle)getDiagram().getStyle(NotationPackage.eINSTANCE.getGuideStyle());
		//
		// if (guideStyle != null) {
		//
		// RootEditPart rep = getGraphicalViewer().getRootEditPart();
		// DiagramRootEditPart root = (DiagramRootEditPart)rep;
		//
		// // Set the Vertical Ruler properties
		// DiagramRuler verticalRuler = ((DiagramRootEditPart)getRootEditPart()).getVerticalRuler();
		// verticalRuler.setGuideStyle(guideStyle);
		// if (getDiagram() != null) {
		// DiagramRulerProvider vertProvider = new DiagramRulerProvider(TransactionUtil
		// .getEditingDomain(getDiagram()), verticalRuler, root.getMapMode());
		// vertProvider.init();
		// getGraphicalViewer().setProperty(RulerProvider.PROPERTY_VERTICAL_RULER, vertProvider);
		// }
		//
		// // Set the Horizontal Ruler properties
		// DiagramRuler horizontalRuler = ((DiagramRootEditPart)getRootEditPart()).getHorizontalRuler();
		// horizontalRuler.setGuideStyle(guideStyle);
		// if (getDiagram() != null) {
		// DiagramRulerProvider horzProvider = new DiagramRulerProvider(TransactionUtil
		// .getEditingDomain(getDiagram()), horizontalRuler, root.getMapMode());
		// horzProvider.init();
		// getGraphicalViewer().setProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER, horzProvider);
		// }
		//
		// }
		//
		// // Grid Origin (always 0, 0)
		// Point origin = new Point();
		// getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ORIGIN, origin);
		//
		// // Grid Spacing
		// double dSpacing = ((DiagramRootEditPart)getGraphicalViewer().getContents().getRoot())
		// .getGridSpacing();
		// ((DiagramRootEditPart)getGraphicalViewer().getContents().getRoot()).setGridSpacing(dSpacing);

		// Scroll-wheel Zoom
		getGraphicalViewer().setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.CTRL),
				MouseWheelZoomHandler.SINGLETON);
	}

	protected void configureGraphicalViewer() {
		getGraphicalViewer().getControl().setBackground(ColorConstants.listBackground);

		IDiagramGraphicalViewer viewer = getGraphicalViewer();

		RootEditPart rootEP = EditPartService.getInstance().createRootEditPart(getDiagram());

		viewer.setRootEditPart(rootEP);

		viewer.setEditPartFactory(EditPartService.getInstance());

		// KeyHandler viewerKeyHandler = new
		// DiagramGraphicalViewerKeyHandler(viewer).setParent(getKeyHandler());
		// viewer.setKeyHandler(new DirectEditKeyHandler(viewer).setParent(viewerKeyHandler));
		// if (viewer.getControl() instanceof FigureCanvas) {
		// ((FigureCanvas)viewer.getControl()).setScrollBarVisibility(FigureCanvas.ALWAYS);
		// }
	}

	public Diagram getDiagram() {
		if (fInput != null) {
			EObject eObject = fInput.getEObject();
			if (eObject instanceof Diagram) {
				return (Diagram)eObject;
			} else if (eObject instanceof View) {
				return ((View)eObject).getDiagram();
			}
		}
		return null;
	}

	@Override
	protected DiagramGraphicalViewer getGraphicalViewer() {
		return fGraphicalViewer;
	}

	@Override
	public void setInput(Object input) {
		if (input instanceof IEObjectAccessor) {
			fInput = ((IEObjectAccessor)input);
			EObject eObject = ((IEObjectAccessor)input).getEObject();

			// FIXME
			ResourceSet resourceSet = null;
			if (eObject != null) {
				resourceSet = eObject.eResource().getResourceSet();
			}
			if (resourceSet != null && Factory.INSTANCE.getEditingDomain(resourceSet) == null) {
				Factory.INSTANCE.createEditingDomain(resourceSet);
			}

			// SOL 2
			// fGraphicalViewer.deselectAll();
			// EditPart part = findEditPart(eObject);
			// if (part != null) {
			// while (!part.isSelectable()) {
			// part = part.getParent();
			// }
			// fGraphicalViewer.setSelection(new StructuredSelection(part));
			// fGraphicalViewer.reveal(part);
			// }

			// SOL 1
			configureGraphicalViewer();
			initializeGraphicalViewerContents();

			final Object viewerInput = doGetInput(eObject);
			getGraphicalViewer().setContents(viewerInput);
			// getGraphicalViewer().setSelection(new StructuredSelection(eObject));
		} else {
			getGraphicalViewer().setContents(null);
		}
	}

	public Object getInput() {
		return fInput;
	}

	/**
	 * Returns either the {@link EObject#eContainer() container} of the given <code>eObject</code> if it is
	 * not null or its {@link EObject#eResource() containing resource} if it is not null.
	 * 
	 * @param eObject
	 *            the object to get the input from.
	 * @return either the {@link EObject#eContainer()} of the given <code>eObject</code> if it is not null or
	 *         its {@link EObject#eResource() containing resource} if it is not null.
	 */
	private static Object doGetInput(EObject eObject) {
		Object input = null;
		if (eObject instanceof Diagram) {
			input = eObject;
		} else if (eObject instanceof View) {
			input = ((View)eObject).getDiagram();
		}
		return input;
	}

	public EditPart findObjectAtExcluding(Point location, Collection exclusionSet, Conditional conditional) {
		// TODO Auto-generated method stub
		return null;
	}

	// public EditPart findEditPart(final EObject eobj) {
	// // check viewer
	// if (eobj instanceof View) {
	// final Diagram d = ((View)eobj).getDiagram();
	// checkAndDisplayDiagram(d);
	// }
	// return (EditPart)viewer.getEditPartRegistry().get(eobj);
	// }

}

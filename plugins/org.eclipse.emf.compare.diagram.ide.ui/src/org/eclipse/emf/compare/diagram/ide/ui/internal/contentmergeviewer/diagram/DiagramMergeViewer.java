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
import org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramNodeAccessor;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.MergeViewer.MergeViewerSide;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain.Factory;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

class DiagramMergeViewer extends GraphicalMergeViewer {

	private IDiagramNodeAccessor fInput;

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
		createDiagramGraphicalViewer(parent);
		return fGraphicalViewer.getControl();
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

	@Override
	protected DiagramGraphicalViewer getGraphicalViewer() {
		return fGraphicalViewer;
	}

	@Override
	public void setInput(Object input) {
		if (input instanceof IDiagramNodeAccessor) {
			fInput = ((IDiagramNodeAccessor)input);
			View eObject = ((IDiagramNodeAccessor)input).getOwnedView();

			// FIXME
			ResourceSet resourceSet = null;
			if (eObject != null) {
				resourceSet = eObject.eResource().getResourceSet();
			}
			if (resourceSet != null && Factory.INSTANCE.getEditingDomain(resourceSet) == null) {
				Factory.INSTANCE.createEditingDomain(resourceSet);
			}

			// SOL 2
			if (eObject != null) {
				fGraphicalViewer.deselectAll();
				EditPart part = findEditPart(eObject);
				if (part != null) {
					while (!part.isSelectable()) {
						part = part.getParent();
					}
					select(part);
				}
			}
		}
	}

	public Object getInput() {
		return fInput;
	}

	public EditPart findObjectAtExcluding(Point location, Collection exclusionSet, Conditional conditional) {
		// TODO Auto-generated method stub
		return null;
	}

	public EditPart findEditPart(final EObject eobj) {
		// check viewer
		if (eobj instanceof View) {
			final Diagram d = ((View)eobj).getDiagram();
			checkAndDisplayDiagram(d);
		}
		return (EditPart)fGraphicalViewer.getEditPartRegistry().get(eobj);
	}

	private void checkAndDisplayDiagram(final Diagram d) {
		if (d != null && !d.equals(currentDiag)) {
			currentDiag = d;
			displayDiagram(d);
		}
	}

	protected final void displayDiagram(final Diagram diag) {
		if (diag == null) {
			return;
		}
		currentDiag = diag;
		// be sure the viewer will be correctly refreshed ( connections )
		fGraphicalViewer.getEditPartRegistry().clear();
		final DiagramRootEditPart rootEditPart = new DiagramRootEditPart(diag.getMeasurementUnit());
		fGraphicalViewer.setRootEditPart(rootEditPart);
		fGraphicalViewer.setContents(diag);
		disableEditMode((DiagramEditPart)fGraphicalViewer.getContents());
		rootEditPart.getZoomManager().setZoomAnimationStyle(ZoomManager.ANIMATE_NEVER);
		rootEditPart.getZoomManager().setZoom(ZOOM_FACTOR);
	}

	private void disableEditMode(DiagramEditPart diagEditPart) {
		diagEditPart.disableEditMode();
		for (Object obj : diagEditPart.getPrimaryEditParts()) {
			if (obj instanceof IGraphicalEditPart) {
				disableEditMode((IGraphicalEditPart)obj);
			}
		}
	}

	private void disableEditMode(IGraphicalEditPart obj) {
		obj.disableEditMode();
		obj.removeEditPolicy(EditPolicyRoles.OPEN_ROLE);
		for (Object child : obj.getChildren()) {
			if (child instanceof IGraphicalEditPart) {
				disableEditMode((IGraphicalEditPart)child);
			}
		}
	}

}

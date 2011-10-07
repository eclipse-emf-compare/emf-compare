/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Obeo - initial API and implementation
 *******************************************************************************/

package org.eclipse.emf.compare.diagram.ui.mergeviewer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.ResourceNode;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramShowHideElement;
import org.eclipse.emf.compare.diagram.ui.GMFComparePlugin;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.compare.ui.util.EMFCompareEObjectUtils;
import org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab;
import org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabItem;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.EditPartService;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;

/**
 * The tab that contains the GMF viewer.
 * 
 * @author <a href="mailto:stephane.bouchet@obeo.fr">Stephane Bouchet</a>
 */
public class GMFContentMergeViewerTab implements IModelContentMergeViewerTab {

	/** the zoom factor of displayed diagrams. */
	private static final double ZOOM_FACTOR = 0.7;

	/** the parent tab folder. */
	protected GMFContentMergeTabFolder contentMergeTabFolderParent;

	/** the GMF diagramGraphicalViewer to use to display the current diagram. */
	private DiagramGraphicalViewer viewer;

	/** the compare viewer side. */
	private int partSide;

	/** the current diagram used. */
	private Diagram currentDiag;

	/** the diagram edit domain. */
	private DiagramEditDomain editDomain;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            the parent control.
	 * @param side
	 *            Side of this viewer part.
	 * @param parentFolder
	 *            Parent folder of this tab.
	 */
	public GMFContentMergeViewerTab(Composite parent, int side, GMFContentMergeTabFolder parentFolder) {
		contentMergeTabFolderParent = parentFolder;
		partSide = side;

		editDomain = new DiagramEditDomain(null);
		editDomain.setCommandStack(new DiagramCommandStack(editDomain));

		createDiagramGraphicalViewer(parent);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				// retrieve the selected diff in structureViewer.
//				System.out.println("selectionChanged()");
//				redraw();
			}
		});

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		viewer.addSelectionChangedListener(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab#dispose()
	 */
	public void dispose() {
		// unload resources form editing domain
		currentDiag = null;
		editDomain = null;
		viewer = null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab#getControl()
	 */
	public Control getControl() {
		return viewer.getControl();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab#getSelectedElements()
	 */
	public List<? extends Item> getSelectedElements() {
		return contentMergeTabFolderParent.getTreePart().getSelectedElements();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab#getUIItem(org.eclipse.emf.ecore.EObject)
	 */
	public ModelContentMergeTabItem getUIItem(EObject data) {
		return contentMergeTabFolderParent.getTreePart().getUIItem(data);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab#getVisibleElements()
	 */
	public List<ModelContentMergeTabItem> getVisibleElements() {
		return contentMergeTabFolderParent.getTreePart().getVisibleElements();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab#redraw()
	 */
	public void redraw() {
		viewer.getControl().redraw();
		if (viewer.getContents() != null)
			viewer.getContents().refresh();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab#setReflectiveInput(java.lang.Object)
	 */
	public void setReflectiveInput(Object input) {
		// resolve the name of local files
		// this is needed to resolve and load the semantic model elements
		final String inputName = getName(input);

		// load the models
		final Resource resource = loadResource(input, inputName);
		if (resource == null)
			return;

		// retrieve the diagram from the resource and display it
		displayDiagram(getDiagramFromResource(resource));

		// maintain synchronization with tree
		contentMergeTabFolderParent.getTreePart().setReflectiveInput(input);
		contentMergeTabFolderParent.getPropertyPart().setReflectiveInput(input);
		redraw();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab#showItems(java.util.List)
	 */
	public void showItems(List<DiffElement> items) {
		viewer.deselectAll();
		final List<EditPart> datas = new ArrayList<EditPart>();
		for (DiffElement diffElement : items) {
			EObject eobj = null;
			// filter diffgroups because they are buggy. see
			// org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabFolder.navigateToDiff(List<DiffElement>)
			if (!(diffElement instanceof DiffGroup)) {
				if (partSide == EMFCompareConstants.ANCESTOR) {
					eobj = getAncestorElement(diffElement);
				} else if (partSide == EMFCompareConstants.LEFT) {
					eobj = getLeftElement(diffElement);
				} else if (partSide == EMFCompareConstants.RIGHT) {
					eobj = getRightElement(diffElement);
				}
			}
			EditPart part = findEditPart(eobj);
			if (part != null) {
				while (!part.isSelectable()) {
					part = part.getParent();
				}
				datas.add(part);
			}
		}
		if (!datas.isEmpty()) {
			viewer.setSelection(new StructuredSelection(datas));
			viewer.reveal(datas.get(0));
		}
		redraw();
	}

	/**
	 * Finds the corresponding editpart from the current viewer. If viewer is not displaying the correct
	 * diagram, viewer will change its contents to display the good one.
	 * 
	 * @param eobj
	 *            the View Element to display
	 * @return the editPart found, if any.
	 */
	public EditPart findEditPart(final EObject eobj) {
		// check viewer
		if (eobj instanceof View) {
			final Diagram d = ((View)eobj).getDiagram();
			checkAndDisplayDiagram(d);
		}
		return (EditPart)viewer.getEditPartRegistry().get(eobj);
	}

	/**
	 * Display diagram if parameter is not the actual displayed diagram.
	 * 
	 * @param d
	 *            the diagram to test
	 */
	private void checkAndDisplayDiagram(final Diagram d) {
		if (d != null && !d.equals(currentDiag)) {
			currentDiag = d;
			displayDiagram(d);
		}
	}

	/**
	 * utility method to return the selected gmfView from the given diff.
	 * 
	 * @param diffElement
	 *            the DiffElement containing the necessary info.
	 * @return the gmfView to select
	 */
	private EObject getRightElement(DiffElement diffElement) {
		// be sure returning a view element, not a semantic element
		EObject eobj = EMFCompareEObjectUtils.getRightElement(diffElement);
		if (!GMFComparePlugin.isValid(eobj)) {
			if (diffElement instanceof BusinessDiagramShowHideElement) {
				eobj = ((BusinessDiagramShowHideElement)diffElement).getRightView();
			} else if (eobj instanceof Diagram) {
				checkAndDisplayDiagram((Diagram)eobj);
			} else {
				return null;
			}
		}
		return eobj;
	}

	/**
	 * utility method to return the selected gmfView from the given diff.
	 * 
	 * @param diffElement
	 *            the DiffElement containing the necessary info.
	 * @return the gmfView to select
	 */
	private EObject getLeftElement(DiffElement diffElement) {
		// be sure returning a view element, not a semantic element
		EObject eobj = EMFCompareEObjectUtils.getLeftElement(diffElement);
		if (!GMFComparePlugin.isValid(eobj)) {
			if (diffElement instanceof BusinessDiagramShowHideElement) {
				eobj = ((BusinessDiagramShowHideElement)diffElement).getLeftView();
			} else if (eobj instanceof Diagram) {
				checkAndDisplayDiagram((Diagram)eobj);
			} else {
				return null;
			}

		}
		return eobj;
	}

	/**
	 * utility method to return the selected gmfView from the given diff.
	 * 
	 * @param diffElement
	 *            the DiffElement containing the necessary info.
	 * @return the gmfView to select
	 */
	private EObject getAncestorElement(DiffElement diffElement) {
		// be sure returning a view element, not a semantic element
		final EObject eobj = EMFCompareEObjectUtils.getAncestorElement(contentMergeTabFolderParent
				.findMatchFromElement(EMFCompareEObjectUtils.getLeftElement(diffElement)));
		if (!GMFComparePlugin.isValid(eobj)) {
			if (eobj instanceof Diagram) {
				checkAndDisplayDiagram((Diagram)eobj);
			} else
				return null;
		}
		return eobj;
	}

	/**
	 * Creates a new {@link DiagramGraphicalViewer}.
	 * 
	 * @param composite
	 *            the parent composite.
	 */
	private void createDiagramGraphicalViewer(Composite composite) {
		viewer = new DiagramGraphicalViewer();
		viewer.createControl(composite);
		viewer.setEditDomain(editDomain);
		viewer.setEditPartFactory(EditPartService.getInstance());
		viewer.getControl().setBackground(ColorConstants.listBackground);
		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.CTRL), MouseWheelZoomHandler.SINGLETON);
	}

	/**
	 * Retrieve the first diagram from the resource.
	 * 
	 * @param resource
	 *            the GMF Resource
	 * @return the first diagram found in the resource
	 */
	public Diagram getDiagramFromResource(final Resource resource) {
		for (Object rootElement : resource.getContents())
			if (rootElement instanceof Diagram) {
				return (Diagram)rootElement;
			}
		return null;
	}

	/**
	 * Utility to load a resource and add it in the resourceSet..
	 * 
	 * @param object
	 *            the object to load ( must be different kind )
	 * @param objectName
	 *            the name of the resource.
	 * @return the loaded resource.
	 */
	private Resource loadResource(Object object, String objectName) {
		Resource res = null;
		if (object instanceof List<?> && !((List<?>)object).isEmpty())
			res = loadResource(((List<?>)object).get(0), objectName);
		else if (object instanceof Resource) {
			res = (Resource)object;
		} else {
			System.out.println("unsupported unput type");
		}
		return res;
	}

	/**
	 * utility to get the name of the resource.
	 * 
	 * @param object
	 *            the input
	 * @return the name of the resource
	 */
	private String getName(Object object) {
		String res = "";
		if (object instanceof ResourceNode)
			res = URI.createPlatformResourceURI(
					((ResourceNode)object).getResource().getFullPath().toString(), true).toString();
		else if (object instanceof Resource)
			res = ((Resource)object).getURI().lastSegment();
		else if (object instanceof ITypedElement)
			res = ((ITypedElement)object).getName();
		else if (object instanceof List<?> && !((List<?>)object).isEmpty())
			res = getName(((List<?>)object).get(0));
		return res;
	}

	/**
	 * Create the viewer, by setting it a content.
	 * 
	 * @param diag
	 *            the diagram to display in the viewer
	 */
	protected final void displayDiagram(final Diagram diag) {
		if (diag == null)
			return;
		currentDiag = diag;
		// be sure the viewer will be correctly refreshed ( connections )
		viewer.getEditPartRegistry().clear();
		final DiagramRootEditPart rootEditPart = new DiagramRootEditPart(diag.getMeasurementUnit());
		viewer.setRootEditPart(rootEditPart);
		viewer.setContents(diag);
		disableEditMode((DiagramEditPart)viewer.getContents());
		rootEditPart.getZoomManager().setZoomAnimationStyle(ZoomManager.ANIMATE_NEVER);
		rootEditPart.getZoomManager().setZoom(ZOOM_FACTOR);
	}

	/**
	 * make the diagram read only.
	 * 
	 * @param diagEditPart
	 *            the top level editpart
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
	 * set the edit part read only.
	 * 
	 * @param obj
	 *            the edit part
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

}

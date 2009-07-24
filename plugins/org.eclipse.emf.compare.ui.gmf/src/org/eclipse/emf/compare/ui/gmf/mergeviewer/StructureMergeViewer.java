/*******************************************************************************
 * Copyright (c) 2009 Tobias Jaehnel.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Tobias Jaehnel - Bug#241385
 *******************************************************************************/

package org.eclipse.emf.compare.ui.gmf.mergeviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.ResourceNode;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ui.gmf.viewmodel.ViewModelCreator;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gmf.ecore.edit.parts.EClassAttributesEditPart;
import org.eclipse.gmf.runtime.diagram.core.DiagramEditingDomainFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.EditPartService;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Tobias Jaehnel <tjaehnel@gmail.com>
 */
public class StructureMergeViewer extends StructuredViewer {
	Composite parent;
	CompareConfiguration compareConfiguration;
	Diagram diag;

	DiagramGraphicalViewer viewer;

	public StructureMergeViewer(Composite parent, CompareConfiguration config) {
		super();
		compareConfiguration = config;
		this.parent = parent;
		viewer = new DiagramGraphicalViewer();
		viewer.createControl(parent);
		viewer.getControl().setBackground(ColorConstants.listBackground);
		setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object inputElement) {
				return null;
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
		});
	}

	@Override
	protected void inputChanged(Object input, Object oldInput) {
		super.inputChanged(input, oldInput);

		if (!(input instanceof Diagram) && input != oldInput) {
			ICompareInput inp = (ICompareInput)input;
			Object left = inp.getLeft();
			Object right = inp.getRight();
			Object anc = inp.getAncestor();

			// setInput(ModelComparat.getComparator(configuration).getComparisonResult());
			ResourceSetImpl resourceset = new ResourceSetImpl();

			Diagram diag_left = null;
			Diagram diag_right = null;
			Resource left_resource = null;
			Resource right_resource = null;
			
			String right_name;
			String left_name;
			// resolve the absolute url if the left or right file is local
			// this is needed to resolve and load the semantic model elements
			if(left instanceof ResourceNode)
				left_name =
					URI.createPlatformResourceURI(((ResourceNode)left).getResource().getFullPath().toString(),true).toString();
			else
				left_name = ((ITypedElement)left).getName();
			if(right instanceof ResourceNode)
				right_name =
					URI.createPlatformResourceURI(((ResourceNode)right).getResource().getFullPath().toString(),true).toString();
			else
				right_name = ((ITypedElement)right).getName();
			
			try {
				left_resource = ModelUtils.load(((IStreamContentAccessor)left).getContents(),
						left_name,resourceset).eResource();
				right_resource = ModelUtils.load(((IStreamContentAccessor)right).getContents(),
						right_name,resourceset).eResource();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (Object rootElement : left_resource.getContents())
				if (rootElement instanceof Diagram)
					diag_left = (Diagram)rootElement;
			for (Object rootElement : right_resource.getContents())
				if (rootElement instanceof Diagram)
					diag_right = (Diagram)rootElement;

			ViewModelCreator modelCreator =
				ViewModelCreator.getCreator(compareConfiguration, diag_left, diag_right);
			// TODO: Have to create the EditingDomain NOT before comparing the objects,
			// otherwise an exception will be thrown when trying to edit the left model.
			// Have to write some lines about why this happens (I don't know yet why)
			DiagramEditingDomainFactory.getInstance().createEditingDomain(resourceset);
			setInput(modelCreator.getMerged());
		} else {
			Diagram mergeModel = (Diagram)input;

			viewer.setEditDomain(new DefaultEditDomain(null));
			viewer.setEditPartFactory(EditPartService.getInstance());
			viewer.setRootEditPart(new DiagramRootEditPart(mergeModel.getMeasurementUnit()));
			viewer.setContents(mergeModel);
		}
	}

	@Override
	protected Widget doFindInputItem(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Widget doFindItem(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doUpdateItem(Widget item, Object element, boolean fullMap) {
		// TODO Auto-generated method stub

	}

	@Override
	protected List getSelectionFromWidget() {
		// TODO Auto-generated method stub
		return new ArrayList();
	}

	@Override
	protected void internalRefresh(Object element) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reveal(Object element) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setSelectionToWidget(List l, boolean reveal) {
		// TODO Auto-generated method stub

	}

	@Override
	public Control getControl() {
		return viewer.getControl();
		// return null;
	}

}

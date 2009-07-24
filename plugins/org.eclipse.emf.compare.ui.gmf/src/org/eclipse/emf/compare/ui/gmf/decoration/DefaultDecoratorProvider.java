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

package org.eclipse.emf.compare.ui.gmf.decoration;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.Shape;
import org.eclipse.emf.compare.ui.gmf.Activator;
import org.eclipse.emf.compare.ui.gmf.Constants;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProviderChangeListener;
import org.eclipse.gmf.runtime.diagram.ui.editparts.CompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.AbstractDecorator;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.PropertiesSetStyle;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * This DecoratorProvider is responsible for visualizing the 
 * changes. It makes changes to the shapes according to the 
 * annotations an the notation model nodes and edges
 * 
 * @author Tobias Jaehnel <tjaehnel@gmail.com>
 */
public class DefaultDecoratorProvider implements IDecoratorProvider {

	public void createDecorators(IDecoratorTarget decoratorTarget) {
		decoratorTarget.installDecorator(Constants.DECORATOR_ID, new DefaultDecorator(decoratorTarget));
	}

	public void addProviderChangeListener(IProviderChangeListener listener) {
		// TODO Auto-generated method stub

	}

	public boolean provides(IOperation operation) {
		// TODO Auto-generated method stub
		return true;
	}

	public void removeProviderChangeListener(IProviderChangeListener listener) {
		// TODO Auto-generated method stub

	}

	static class DefaultDecorator extends AbstractDecorator {
		public static final Image ICON_ADD;

		public static final Image ICON_DELETE;

		static {
			IPath path = new Path("$nl$").append("icons//add.gif");
			URL url = FileLocator.find(Activator.getDefault().getBundle(), path, null);
			ImageDescriptor imgDesc = ImageDescriptor.createFromURL(url);
			ICON_ADD = imgDesc.createImage();
			path = new Path("$nl$").append("icons//delete.gif");
			url = FileLocator.find(Activator.getDefault().getBundle(), path, null);
			imgDesc = ImageDescriptor.createFromURL(url);
			ICON_DELETE = imgDesc.createImage();
		}

		public DefaultDecorator(IDecoratorTarget target) {
			super(target);
		}

		public void activate() {
			refresh();
		}

		public void refresh() {
			removeDecoration();

			GraphicalEditPart editPart = (GraphicalEditPart)getDecoratorTarget().getAdapter(EditPart.class);
			IFigure hostFigure = editPart.getFigure();
			View notationElement = (View)editPart.getModel();
			String state = getAnnotation(notationElement);
			
			// no annotation
			if(state == null)
				return;
			// workaround to hide annotation from edges, which are in a container
			// which is already annotated
			if (notationElement instanceof Edge) {
				Edge notationEdge = (Edge)notationElement;
				if(notationEdge.getSource().eContainer() == notationEdge.getTarget().eContainer()
						&& state.equals(getParentAnnotation((notationEdge.getSource()),true))
						&& state.equals(getParentAnnotation(notationEdge.getTarget(),true)))
					return; 
			}
			// parent already annotated
			if(state.equals(getParentAnnotation(notationElement, true)))
				return;
			
			boolean deleted;
			Image overlayIcon;
			if (Constants.STYLE_STATE_VALUE_ADDED.equals(state)) {
				deleted = false;
				overlayIcon = ICON_ADD;
				if (hostFigure instanceof PolylineConnection) {
					hostFigure.setForegroundColor(new Color(null, 160, 255, 160));
					((PolylineConnection)hostFigure).setLineStyle(Graphics.LINE_DASH);
				} else if (hostFigure instanceof WrappingLabel) {
					((WrappingLabel)hostFigure).setForegroundColor(new Color(null, 160, 255, 160));
				} else {
					hostFigure.setBackgroundColor(new Color(null, 160, 255, 160));
				}
			} else if(Constants.STYLE_STATE_VALUE_DELETED.equals(state)){
				deleted = true;
				overlayIcon = ICON_DELETE;
				if (hostFigure instanceof PolylineConnection) {
					hostFigure.setForegroundColor(new Color(null, 255, 160, 160));
					((PolylineConnection)hostFigure).setLineStyle(Graphics.LINE_DASH);
				} else if (hostFigure instanceof WrappingLabel) {
					((WrappingLabel)hostFigure).setForegroundColor(new Color(null, 255, 160, 160));
				} else {
					hostFigure.setBackgroundColor(new Color(null, 255, 160, 160));
				}
			} else { // changed
				deleted = false;
				overlayIcon = null;
				if (hostFigure instanceof PolylineConnection) {
					hostFigure.setForegroundColor(new Color(null, 160, 160, 160));
				} else if (hostFigure instanceof WrappingLabel) {
					((WrappingLabel)hostFigure).setForegroundColor(new Color(null, 160, 160, 160));
				} else {
					hostFigure.setBackgroundColor(new Color(null, 160, 160, 160));
				}
			}
			if (editPart instanceof ShapeEditPart) {
				setDecoration(getDecoratorTarget().addShapeDecoration(overlayIcon,
						IDecoratorTarget.Direction.NORTH_EAST, -3, false));
			} else if (editPart instanceof ConnectionEditPart) {
				setDecoration(getDecoratorTarget().addConnectionDecoration(overlayIcon, 50, false));
				if (deleted)
					((Shape)editPart.getFigure()).setLineStyle(Graphics.LINE_DASH);
			}
			// Decoration deco = (Decoration)getDecoratorTarget().addShapeDecoration(image,
			// IDecoratorTarget.Direction.SOUTH_WEST, 0, false);
			// editPart.getFigure().setBackgroundColor(new Color(null,255,0,0));
			// editPart.getFigure().add(el);
			// deco.add(el);
			// el.setBounds(new Rectangle(20,20,20,40));
			// Rectangle bounds = editPart.getFigure().getBounds();
		}
		
		/**
		 * Get the annotated state of the given notation element
		 * @param notationElement
		 * @return
		 */
		protected String getAnnotation(View notationElement) {
			PropertiesSetStyle propset = (PropertiesSetStyle)notationElement.getNamedStyle(
					NotationPackage.Literals.PROPERTIES_SET_STYLE, Constants.STYLE_ID);
			if(propset != null) {
				Object annotation =  propset.getProperty(Constants.STYLE_PROPERTY_STATE_ID);
				if (annotation instanceof String) {
					return (String)annotation;
				}
			}
			return null;
		}
		
		/**
		 * Get the annotated state of the parent of the given notation element.
		 * If the recursive flag is set, move up the tree until an element
		 * has an annotation set and return this one
		 * 
		 * @param notationElement
		 * @param recursive recurse until a annotation is found
		 * @return
		 */
		protected String getParentAnnotation(View notationElement, boolean recursive) {
			View parentNotation = (View)notationElement.eContainer();
			if(parentNotation == null)
				return null;

			// workaround - some attributes in diagram often change due to different diagrams
			if(parentNotation instanceof Diagram)
				return null;
			
			String annotation = getAnnotation(parentNotation); 
			if(recursive && annotation == null)
				annotation = getParentAnnotation(parentNotation, true); 

			return annotation;
			
		}		
	}

}

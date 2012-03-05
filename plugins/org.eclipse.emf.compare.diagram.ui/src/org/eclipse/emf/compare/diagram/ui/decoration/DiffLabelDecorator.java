/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Obeo - initial API and implementation
 *******************************************************************************/

package org.eclipse.emf.compare.diagram.ui.decoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.emf.compare.diagram.ui.GMFComparePlugin;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoration;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget.Direction;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.swt.graphics.Image;

/**
 * Decorator for labels.
 * 
 * @author <a href="mailto:stephane.bouchet@obeo.fr">Stephane Bouchet</a>
 */
public class DiffLabelDecorator extends AbstractDifferenceDecorator {

	/**
	 * Constructor.
	 * 
	 * @param target
	 *            the label to decorate.
	 */
	public DiffLabelDecorator(IDecoratorTarget target) {
		super(target);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecorator#activate()
	 */
	public void activate() {
		final IGraphicalEditPart gep = getTargetEditPart();
		DiagramEventBroker.getInstance(gep.getEditingDomain()).addNotificationListener(gep.getNotationView(),
				NotationPackage.eINSTANCE.getNode_LayoutConstraint(), notificationListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecorator#deactivate()
	 */
	public void deactivate() {
		removeDecorations();
		final IGraphicalEditPart gep = getTargetEditPart();
		DiagramEventBroker.getInstance(gep.getEditingDomain()).removeNotificationListener(
				gep.getNotationView(), NotationPackage.eINSTANCE.getNode_LayoutConstraint(),
				notificationListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ui.decoration.AbstractDifferenceDecorator#getModifiedDecorations()
	 */
	@Override
	protected List<IDecoration> getModifiedDecorations() {
		getTargetEditPart().getContentPane().setBorder(new LineBorder(ColorConstants.blue, 2));
		return Collections.emptyList();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ui.decoration.AbstractDifferenceDecorator#getAddedDecorations()
	 */
	@Override
	protected List<IDecoration> getAddedDecorations() {
		getTargetEditPart().getContentPane().setBorder(new LineBorder(ColorConstants.green, 2));
		return Collections.emptyList();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ui.decoration.AbstractDifferenceDecorator#getRemovedDecorations()
	 */
	@Override
	protected List<IDecoration> getRemovedDecorations() {
		getTargetEditPart().getContentPane().setBorder(new LineBorder(ColorConstants.red, 2));
		return Collections.emptyList();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ui.decoration.AbstractDifferenceDecorator#getMovedDecorations()
	 */
	@Override
	protected List<IDecoration> getMovedDecorations() {
		final ArrayList<IDecoration> decorations = new ArrayList<IDecoration>(1);
		final Image moved = GMFComparePlugin.getDefault().getImage(GMFComparePlugin.ICON_MOVED);
		final IDecoration decoration = getTarget().addShapeDecoration(moved, Direction.NORTH_EAST, 1, false);
		decorations.add(decoration);
		return decorations;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ui.decoration.AbstractDifferenceDecorator#getHidedDecorations()
	 */
	@Override
	protected List<IDecoration> getHidedDecorations() {
		getTargetEditPart().getContentPane().setBorder(new LineBorder(ColorConstants.orange, 2));
		return Collections.emptyList();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ui.decoration.AbstractDifferenceDecorator#getShowedDecorations()
	 */
	@Override
	protected List<IDecoration> getShowedDecorations() {
		getTargetEditPart().getContentPane().setBorder(new LineBorder(ColorConstants.orange, 2));
		return Collections.emptyList();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ui.decoration.AbstractDifferenceDecorator#getLabelModifiedDecorations()
	 */
	@Override
	protected List<IDecoration> getLabelModifiedDecorations() {
		getTargetEditPart().getContentPane().setBorder(new LineBorder(ColorConstants.blue, 2));
		return Collections.emptyList();
	}
}

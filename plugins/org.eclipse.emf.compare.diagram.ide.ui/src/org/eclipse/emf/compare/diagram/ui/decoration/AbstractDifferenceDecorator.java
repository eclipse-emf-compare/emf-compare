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
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.diagram.DiagramDiff;
import org.eclipse.emf.compare.diagram.Hide;
import org.eclipse.emf.compare.diagram.LabelChange;
import org.eclipse.emf.compare.diagram.Show;
import org.eclipse.emf.compare.diagram.ui.decoration.provider.DiffDecoratorProvider;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoration;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecorator;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;

/**
 * The diff decorator superclass.
 * 
 * @author <a href="mailto:stephane.bouchet@obeo.fr">Stephane Bouchet</a>
 */
public abstract class AbstractDifferenceDecorator implements IDecorator {

	/** the notificationListener. */
	protected NotificationListener notificationListener = new NotificationListener() {
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener#notifyChanged(org.eclipse.emf.common.notify.Notification)
		 */
		public void notifyChanged(Notification notification) {
			// on each notification, just refresh the decorator.
			refresh();
		}
	};

	/** the view target. */
	private IDecoratorTarget decoratorTarget;

	/** the decoration list to place on the target. */
	private List<IDecoration> decorationsTarget;

	/**
	 * Constructor.
	 * 
	 * @param target
	 *            the target to decorate.
	 */
	protected AbstractDifferenceDecorator(IDecoratorTarget target) {
		this.decoratorTarget = target;
	}

	/**
	 * Add a list of decoration to the list.
	 * 
	 * @param decorations
	 *            the decorations to add
	 * @return true if add has been correctly done.
	 */
	private boolean addDecorations(List<IDecoration> decorations) {
		if (decorationsTarget == null) {
			decorationsTarget = new ArrayList<IDecoration>(decorations.size());
		}
		return decorationsTarget.addAll(decorations);
	}

	/**
	 * Returns the decoratorTarget.
	 * 
	 * @return The decoratorTarget.
	 */
	public IDecoratorTarget getTarget() {
		return decoratorTarget;
	}

	/**
	 * Removes all the decorations if they exists.
	 */
	protected void removeDecorations() {
		if (decorationsTarget != null) {
			for (IDecoration decoration : decorationsTarget) {
				decoratorTarget.removeDecoration(decoration);
			}
			decorationsTarget.clear();
		}
	}

	protected IGraphicalEditPart getTargetEditPart() {
		return (IGraphicalEditPart)decoratorTarget.getAdapter(IGraphicalEditPart.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecorator#refresh()
	 */
	public final void refresh() {
		removeDecorations();
		if (DiffDecoratorProvider.shouldDecorate(getTarget())) {
			final IGraphicalEditPart gep = getTargetEditPart();
			final View view = gep.getNotationView();
			final DiagramDiff diff = DiffDecoratorProvider.getRelatedSelectedDifference(view);
			if (diff instanceof Hide) {
				addDecorations(getHidedDecorations());
			} else if (diff instanceof Show) {
				addDecorations(getShowedDecorations());
			} else if (diff instanceof LabelChange) {
				addDecorations(getLabelModifiedDecorations());
			} else if (diff.getKind() == DifferenceKind.MOVE) {
				addDecorations(getMovedDecorations());
			} else if (diff.getKind() == DifferenceKind.CHANGE) {
				addDecorations(getModifiedDecorations());
			} else if (diff.getKind() == DifferenceKind.ADD) {
				addDecorations(getAddedDecorations());
			} else if (diff.getKind() == DifferenceKind.DELETE) {
				addDecorations(getRemovedDecorations());
			}
		}
	}

	/**
	 * Utility method to highlight an edge with specified color and line width.
	 * 
	 * @param figure
	 *            the connection figure to highlight
	 * @param color
	 *            the highlight color to use
	 * @param size
	 *            the line width
	 */
	protected void highlightEdge(IFigure figure, Color color, int size) {
		if (figure instanceof PolylineConnection) {
			figure.setBackgroundColor(color);
			figure.setForegroundColor(color);
			((PolylineConnection)figure).setLineWidth(size);
		}
	}

	/**
	 * Utility method to highlight a node.
	 * 
	 * @param figure
	 *            the node to highlight
	 * @param color
	 *            the highlight color to use
	 * @param size
	 *            the line border width
	 */
	protected void highlightNode(IFigure figure, Color color, int size) {
		figure.setBorder(new LineBorder(color, size));
		figure.setOpaque(false);
	}

	/**
	 * Method used to create a specific set of decorations for the {@link DiffDecoratorProvider#DIFF_MODIFIED}
	 * decoration.
	 * 
	 * @return a list of {@link IDecoration}
	 */
	protected abstract List<IDecoration> getModifiedDecorations();

	/**
	 * Method used to create a specific set of decorations for the {@link DiffDecoratorProvider#DIFF_ADDED}
	 * decoration.
	 * 
	 * @return a list of {@link IDecoration}
	 */
	protected abstract List<IDecoration> getAddedDecorations();

	/**
	 * Method used to create a specific set of decorations for the {@link DiffDecoratorProvider#DIFF_REMOVED}
	 * decoration.
	 * 
	 * @return a list of {@link IDecoration}
	 */
	protected abstract List<IDecoration> getRemovedDecorations();

	/**
	 * Method used to create a specific set of decorations for the {@link DiffDecoratorProvider#DIFF_MOVED}
	 * decoration.
	 * 
	 * @return a list of {@link IDecoration}
	 */
	protected abstract List<IDecoration> getMovedDecorations();

	/**
	 * Method used to create a specific set of decorations for the {@link DiffDecoratorProvider#DIFF_HIDED}
	 * decoration.
	 * 
	 * @return a list of {@link IDecoration}
	 */
	protected abstract List<IDecoration> getHidedDecorations();

	/**
	 * Method used to create a specific set of decorations for the {@link DiffDecoratorProvider#DIFF_SHOWED}
	 * decoration.
	 * 
	 * @return a list of {@link IDecoration}
	 */
	protected abstract List<IDecoration> getShowedDecorations();

	/**
	 * Method used to create a specific set of decorations for the
	 * {@link DiffDecoratorProvider#DIFF_LABEL_MODIFIED} decoration.
	 * 
	 * @return a list of {@link IDecoration}
	 */
	protected abstract List<IDecoration> getLabelModifiedDecorations();
}

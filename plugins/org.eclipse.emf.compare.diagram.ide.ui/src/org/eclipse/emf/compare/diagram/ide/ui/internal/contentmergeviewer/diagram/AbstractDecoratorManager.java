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
package org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram;

import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.DecoratorFigure;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.ICompareColor;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListItemEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Decorator manager to create, hide or reveal decorator figures related to deleted or added graphical
 * objects.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public abstract class AbstractDecoratorManager implements IDecoratorManager {

	/**
	 * Decorator represented by a <code>figure</code> on a <code>layer</code>, from the given
	 * <code>side</code> of the merge viewer. An edit part may be linked to the <code>figure</code> in some
	 * cases.<br>
	 * The decorator is related to a <code>difference</code> and it is binded with the reference view and
	 * figure.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	protected abstract class AbstractDecorator {

		/** The reference <code>View</code> for this decorator. */
		protected View fOriginView;

		/** The reference <code>IFigure</code> for this decorator. */
		protected IFigure fOriginFigure;

		/** The <code>IFigure</code> representing this decorator. */
		protected IFigure fFigure;

		/**
		 * The <code>DecoratorFigure</code> representing this decorator. It references
		 * {@link AbstractDecorator#fFigure} through the accessor {@link DecoratorFigure#getMainFigure()}.
		 */
		protected DecoratorFigure fDecoratorFigure;

		/** The layer on which the <code>figure</code> has to be drawn. */
		protected IFigure fLayer;

		/** The side of the merge viewer on which the <code>figure</code> has to be drawn. */
		protected MergeViewerSide fSide;

		/** The difference related to this phantom. */
		protected Diff fDifference;

		/** The edit part of the figure representing this phantom. May be null. */
		protected EditPart fEditPart;

		/**
		 * Getter.
		 * 
		 * @return the originView {@link AbstractDecorator#fOriginView}.
		 */
		public View getOriginView() {
			return fOriginView;
		}

		/**
		 * Setter.
		 * 
		 * @param originView
		 *            {@link AbstractDecorator#fOriginView}.
		 */
		public void setOriginView(View originView) {
			this.fOriginView = originView;
		}

		/**
		 * Getter.
		 * 
		 * @return the originFigure {@link AbstractDecorator#fOriginFigure}.
		 */
		public IFigure getOriginFigure() {
			return fOriginFigure;
		}

		/**
		 * Setter.
		 * 
		 * @param originFigure
		 *            {@link AbstractDecorator#fOriginFigure}.
		 */
		public void setOriginFigure(IFigure originFigure) {
			this.fOriginFigure = originFigure;
		}

		/**
		 * Getter.
		 * 
		 * @return the figure {@link AbstractDecorator#fFigure}.
		 */
		public IFigure getFigure() {
			return fFigure;
		}

		/**
		 * Getter.
		 * 
		 * @return the decorator figure {@link AbstractDecorator#fDecoratorFigure}.
		 */
		public DecoratorFigure getDecoratorFigure() {
			return fDecoratorFigure;
		}

		/**
		 * Setter.
		 * 
		 * @param figure
		 *            {@link AbstractDecorator#fFigure}.
		 */
		public void setFigure(IFigure figure) {
			this.fFigure = figure;
		}

		/**
		 * Setter.
		 * 
		 * @param figure
		 *            {@link AbstractDecorator#fFigure}.
		 */
		public void setDecoratorFigure(DecoratorFigure figure) {
			this.fDecoratorFigure = figure;
			setFigure(figure.getMainFigure());
		}

		/**
		 * Getter.
		 * 
		 * @return the layer {@link AbstractDecorator#fLayer}.
		 */
		public IFigure getLayer() {
			return fLayer;
		}

		/**
		 * Setter.
		 * 
		 * @param layer
		 *            {@link AbstractDecorator#fLayer}.
		 */
		public void setLayer(IFigure layer) {
			this.fLayer = layer;
		}

		/**
		 * Getter.
		 * 
		 * @return the side {@link AbstractDecorator#fSide}.
		 */
		public MergeViewerSide getSide() {
			return fSide;
		}

		/**
		 * Setter.
		 * 
		 * @param side
		 *            {@link AbstractDecorator#fSide}.
		 */
		public void setSide(MergeViewerSide side) {
			this.fSide = side;
		}

		/**
		 * Getter.
		 * 
		 * @return the difference {@link AbstractDecorator#fDifference}.
		 */
		public Diff getDifference() {
			return fDifference;
		}

		/**
		 * Setter.
		 * 
		 * @param difference
		 *            {@link AbstractDecorator#fDifference}.
		 */
		public void setDifference(Diff difference) {
			this.fDifference = difference;
		}

		/**
		 * Getter.
		 * 
		 * @return the editPart {@link AbstractDecorator#fEditPart}.
		 */
		public EditPart getEditPart() {
			return fEditPart;
		}

		/**
		 * Setter.
		 * 
		 * @param editPart
		 *            {@link AbstractDecorator#fEditPart}.
		 */
		public void setEditPart(EditPart editPart) {
			this.fEditPart = editPart;
		}

	}

	/**
	 * The compare configuration of the viewer.
	 */
	private EMFCompareConfiguration fCompareConfiguration;

	/**
	 * The left area of the viewer.
	 */
	private DiagramMergeViewer fLeft;

	/**
	 * The right area of the viewer.
	 */
	private DiagramMergeViewer fRight;

	/**
	 * The ancestor area of the viewer.
	 */
	private DiagramMergeViewer fAncestor;

	/**
	 * The color of the difference.
	 */
	private ICompareColor fColor;

	/**
	 * Constructor.
	 * 
	 * @param compareConfiguration
	 *            The compare configuration of the viewer.
	 * @param left
	 *            The left area of the viewer.
	 * @param right
	 *            The right area of the viewer.
	 * @param ancestor
	 *            The ancestor area of the viewer.
	 * @param color
	 *            The color of the difference.
	 */
	protected AbstractDecoratorManager(EMFCompareConfiguration compareConfiguration, DiagramMergeViewer left,
			DiagramMergeViewer right, DiagramMergeViewer ancestor, ICompareColor color) {
		fCompareConfiguration = compareConfiguration;
		fLeft = left;
		fRight = right;
		fAncestor = ancestor;
		fColor = color;
	}

	/**
	 * Returns the compare configuration of this viewer.
	 *
	 * @return the compare configuration, never <code>null</code>
	 */
	protected EMFCompareConfiguration getCompareConfiguration() {
		return fCompareConfiguration;
	}

	/**
	 * Returns the left area of the viewer.
	 * 
	 * @return the fLeft
	 */
	protected DiagramMergeViewer getLeftMergeViewer() {
		return fLeft;
	}

	/**
	 * Returns the right area of the viewer.
	 * 
	 * @return the fRight
	 */
	protected DiagramMergeViewer getRightMergeViewer() {
		return fRight;
	}

	/**
	 * Returns the ancestor area of the viewer.
	 * 
	 * @return the fAncestor
	 */
	protected DiagramMergeViewer getAncestorMergeViewer() {
		return fAncestor;
	}

	/**
	 * Return whether the input is a three-way comparison.
	 * 
	 * @return whether the input is a three-way comparison
	 */
	protected boolean isThreeWay() {
		return getCompareConfiguration().getComparison().isThreeWay();
	}

	/**
	 * Returns the ICompareColor.
	 * 
	 * @return the ICompareColor.
	 */
	public ICompareColor getCompareColor() {
		return fColor;
	}

	/**
	 * From a given difference, it hides the related decorators.
	 * 
	 * @param difference
	 *            The difference.
	 */
	public void hideDecorators(Diff difference) {
		Collection<? extends AbstractDecorator> oldDecorators = getDecorators(difference);
		if (oldDecorators != null && !oldDecorators.isEmpty()
				&& getCompareConfiguration().getComparison() != null) {
			handleDecorators(oldDecorators, false, true);
		}
	}

	/**
	 * From a given difference, it reveals the related decorators.
	 * 
	 * @param difference
	 *            The difference.
	 */
	public void revealDecorators(Diff difference) {

		Collection<? super AbstractDecorator> decorators = (Collection<? super AbstractDecorator>)getDecorators(
				difference);

		// Create decorators only if they do not already exist and if the selected difference is a good
		// candidate for that.
		if ((decorators == null || decorators.isEmpty()) && isGoodCandidate(difference)) {

			DiagramDiff diagramDiff = (DiagramDiff)difference;

			List<View> referenveViews = getReferenceViews(diagramDiff);

			for (View referenceView : referenveViews) {
				IFigure referenceFigure = getFigure(referenceView);

				if (referenceFigure != null) {
					MergeViewerSide targetSide = getTargetSide(
							getCompareConfiguration().getComparison().getMatch(referenceView), referenceView);

					if (decorators == null) {
						decorators = new ArrayList<AbstractDecorator>();
					}

					AbstractDecorator decorator = createAndRegisterDecorator(difference, referenceView,
							referenceFigure, targetSide);
					if (decorator != null) {
						decorators.add(decorator);
					}
				}

			}

		}

		// The selected difference is a good candidate and decorators exist for it
		if (decorators != null && !decorators.isEmpty()) {
			revealDecorators((Collection<? extends AbstractDecorator>)decorators);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.IDecoratorManager#removeDecorators(org.eclipse.emf.compare.Diff)
	 */
	public abstract void removeDecorators(Diff difference);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.IDecoratorManager#removeAll()
	 */
	public abstract void removeAll();

	/**
	 * It reveals the given decorators.
	 * 
	 * @param decorators
	 *            The main decorators.
	 */
	protected void revealDecorators(Collection<? extends AbstractDecorator> decorators) {
		handleDecorators(decorators, true, true);
	}

	/**
	 * Get the figure related to the given view.
	 * 
	 * @param view
	 *            The view.
	 * @return the figure.
	 */
	protected IFigure getFigure(View view) {
		MergeViewerSide side = getSide(view);
		GraphicalEditPart originEditPart = (GraphicalEditPart)getViewer(side).getEditPart(view);
		if (originEditPart != null) {
			return originEditPart.getFigure();
		}
		return null;
	}

	/**
	 * It manages the display of the given decorators.<br>
	 * 
	 * @param decorators
	 *            The decorators to handle.
	 * @param isAdd
	 *            True if it has to be revealed, False otherwise.
	 * @param areMain
	 *            It indicates if the given decorators to handle are considered as the main ones (the ones
	 *            directly linked to the selected difference).
	 */
	protected void handleDecorators(Collection<? extends AbstractDecorator> decorators, boolean isAdd,
			boolean areMain) {
		for (AbstractDecorator decorator : decorators) {
			handleDecorator(decorator, isAdd, areMain);
		}
	}

	/**
	 * It manages the display of the given decorator.
	 * 
	 * @param decorator
	 *            The decorator to handle.
	 * @param isAdd
	 *            True if it has to be revealed, False otherwise.
	 * @param isMain
	 *            It indicates if the given decorator to handle is considered as the main one (the one
	 *            directly linked to the selected difference).
	 */
	protected void handleDecorator(AbstractDecorator decorator, boolean isAdd, boolean isMain) {
		IFigure layer = decorator.getLayer();
		IFigure figure = decorator.getFigure();
		if (isAdd) {
			handleAddDecorator(decorator, layer, figure, isMain);
		} else if (layer.getChildren().contains(figure)) {
			handleDeleteDecorator(decorator, layer, figure);
		}
	}

	/**
	 * It manages the reveal of the given decorator.
	 * 
	 * @param decorator
	 *            The decorator.
	 * @param parent
	 *            The parent figure which has to get the figure to reveal (<code>toAdd</code>)
	 * @param toAdd
	 *            The figure to reveal.
	 * @param isMain
	 *            It indicates if the given decorator to reveal is considered as the main one (the one
	 *            directly linked to the selected difference).
	 */
	protected void handleAddDecorator(AbstractDecorator decorator, IFigure parent, IFigure toAdd,
			boolean isMain) {
		if (decorator.getEditPart() != null) {
			decorator.getEditPart().activate();
		}
		if (!parent.getChildren().contains(toAdd)) {
			parent.add(toAdd);
		}
	}

	/**
	 * It manages the hiding of the given decorator.
	 * 
	 * @param decorator
	 *            The decorator.
	 * @param parent
	 *            The parent figure which has to get the figure to hide (<code>toDelete</code>)
	 * @param toDelete
	 *            The figure to hide.
	 */
	protected void handleDeleteDecorator(AbstractDecorator decorator, IFigure parent, IFigure toDelete) {
		if (decorator.getEditPart() != null) {
			decorator.getEditPart().deactivate();
		}
		if (parent.getChildren().contains(toDelete)) {
			parent.remove(toDelete);
		}
	}

	/**
	 * It checks if the given difference is a good candidate to manage decorators.<br>
	 * 
	 * @see {@link PhantomManager#goodCandidate()}.
	 * @param difference
	 *            The difference.
	 * @return True if it is a good candidate, False otherwise.
	 */
	private boolean isGoodCandidate(Diff difference) {
		return goodCandidate().apply(difference);
	}

	/**
	 * Get the layer on the given side, from the reference view.<br>
	 * 
	 * @see @ link {@link PhantomManager#getIDLayer(View)}.
	 * @param referenceView
	 *            The reference view.
	 * @param side
	 *            The side where the layer has to be found.
	 * @return The layer figure or null if the edit part of the diagram is not found.
	 */
	protected IFigure getLayer(View referenceView, MergeViewerSide side) {
		Diagram referenceDiagram = referenceView.getDiagram();
		Diagram targetDiagram = (Diagram)getMatchView(referenceDiagram, getEffectiveSide(side));
		DiagramMergeViewer targetViewer = getViewer(side);
		EditPart editPart = targetViewer.getEditPart(targetDiagram);
		if (editPart != null) {
			return LayerManager.Helper.find(editPart).getLayer(getIDLayer(referenceView));
		}
		return null;
	}

	/**
	 * Get the layer ID to use from the reference view.<br>
	 * If the reference view is an edge, it is the {@link LayerConstants.CONNECTION_LAYER} which is used,
	 * {@link LayerConstants.SCALABLE_LAYERS} otherwise.
	 * 
	 * @param referenceView
	 *            The reference view.
	 * @return The ID of the layer.
	 */
	protected Object getIDLayer(View referenceView) {
		if (referenceView instanceof Edge) {
			return LayerConstants.CONNECTION_LAYER;
		} else {
			return LayerConstants.SCALABLE_LAYERS;
		}
	}

	/**
	 * It translates the coordinates of the given bounds, from the reference figure and the root of this one,
	 * to absolute coordinates.
	 * 
	 * @param referenceFigure
	 *            The reference figure.
	 * @param rootReferenceFigure
	 *            The root of the reference figure.
	 * @param boundsToTranslate
	 *            The bounds to translate.
	 */
	protected void translateCoordinates(IFigure referenceFigure, IFigure rootReferenceFigure,
			Rectangle boundsToTranslate) {
		IFigure referenceParentFigure = referenceFigure.getParent();
		if (referenceParentFigure != null && referenceFigure != rootReferenceFigure) {
			// rootReferenceFigure may be located to (-x,0)... We consider that the root reference is
			// always (0,0)
			if (referenceParentFigure.isCoordinateSystem() && referenceParentFigure != rootReferenceFigure) {
				boundsToTranslate.x += referenceParentFigure.getBounds().x;
				boundsToTranslate.y += referenceParentFigure.getBounds().y;
			}
			translateCoordinates(referenceParentFigure, rootReferenceFigure, boundsToTranslate);
		}
	}

	/**
	 * It checks that the given view represents an element of a list.
	 * 
	 * @param view
	 *            The view.
	 * @return True it it is an element of a list.
	 */
	protected boolean isNodeList(View view) {
		DiagramMergeViewer viewer = getViewer(getSide(view));
		EditPart part = viewer.getEditPart(view);
		return isNodeList(part);
	}

	/**
	 * It checks that the given part represents an element of a list.
	 * 
	 * @param part
	 *            The part.
	 * @return True it it represents an element of a list.
	 */
	private boolean isNodeList(EditPart part) {
		return part instanceof ListItemEditPart || isInListContainer(part);

	}

	/**
	 * It checks that the given part is in one representing a list container.
	 * 
	 * @param part
	 *            The part.
	 * @return True it it is in a part representing a list container.
	 */
	private boolean isInListContainer(EditPart part) {
		EditPart parent = part.getParent();
		while (parent != null) {
			if (parent instanceof ListCompartmentEditPart) {
				return true;
			}
			parent = parent.getParent();
		}
		return false;
	}

	/**
	 * Utility method to retrieve the {@link DiagramMergeViewer} from the given side.
	 * 
	 * @param side
	 *            The side to focus.
	 * @return The viewer.
	 */
	public DiagramMergeViewer getViewer(MergeViewerSide side) {
		DiagramMergeViewer result = null;
		switch (side) {
			case LEFT:
				result = getLeftMergeViewer();
				break;
			case RIGHT:
				result = getRightMergeViewer();
				break;
			case ANCESTOR:
				result = getAncestorMergeViewer();
				break;
			default:
		}
		return result;
	}

	/**
	 * Utility method to know the side where is located the given view.
	 * 
	 * @param view
	 *            The view.
	 * @return The side of the view.
	 */
	public MergeViewerSide getSide(View view) {
		MergeViewerSide result = null;
		Match match = getCompareConfiguration().getComparison().getMatch(view);
		if (match.getLeft() == view) {
			result = MergeViewerSide.LEFT;
		} else if (match.getRight() == view) {
			result = MergeViewerSide.RIGHT;
		} else if (match.getOrigin() == view) {
			result = MergeViewerSide.ANCESTOR;
		}
		return getEffectiveSide(result);
	}

	/**
	 * Returns the effective side taking into account {@link CompareConfiguration#isMirrored()} to switch left
	 * and right.
	 * 
	 * @param side
	 *            The side where the potential matching object has to be retrieved.
	 * @return the effective side with respect to mirroring.
	 */
	protected MergeViewerSide getEffectiveSide(MergeViewerSide side) {
		if (side != null && getCompareConfiguration().isMirrored()) {
			return side.opposite();
		}
		return side;
	}

	/**
	 * Utility method to get the object matching with the given one, to the given side.
	 * 
	 * @param object
	 *            The object as base of the lookup.
	 * @param side
	 *            The side where the potential matching object has to be retrieved.
	 * @return The matching object.
	 */
	public EObject getMatchView(EObject object, MergeViewerSide side) {
		Match match = getCompareConfiguration().getComparison().getMatch(object);
		return getMatchView(match, side);
	}

	/**
	 * Utility method to get the object in the given side from the given match.
	 * 
	 * @param match
	 *            The match.
	 * @param side
	 *            The side where the potential matching object has to be retrieved.
	 * @return The matching object.
	 */
	private EObject getMatchView(Match match, MergeViewerSide side) {
		EObject result = null;
		switch (side) {
			case LEFT:
				result = match.getLeft();
				break;
			case RIGHT:
				result = match.getRight();
				break;
			case ANCESTOR:
				result = match.getOrigin();
				break;
			default:
		}
		return result;
	}

	/**
	 * Get the predicate to know the differences concerned by the display of decorators.
	 * 
	 * @return The predicate.
	 */
	protected abstract Predicate<Diff> goodCandidate();

	/**
	 * Get the views which have to be used as reference to build the related decorators from the given
	 * difference of the value concerned by the difference.<br>
	 * 
	 * @param difference
	 *            The difference.
	 * @return The list of reference views.
	 */
	protected abstract List<View> getReferenceViews(DiagramDiff difference);

	/**
	 * Get the side where decorators have to be drawn, according to the given reference view and its
	 * match.<br>
	 * 
	 * @param match
	 *            The match of the reference view.
	 * @param referenceView
	 *            The reference view.
	 * @return The side for phantoms.
	 */
	protected abstract MergeViewerSide getTargetSide(Match match, View referenceView);

	/**
	 * It creates new decorators and registers them.
	 * 
	 * @param diff
	 *            The related difference used as index for the main decorator.
	 * @param referenceView
	 *            The reference view as base for creation of the decorator.
	 * @param referenceFigure
	 *            The reference figure as base for creation of the decorator.
	 * @param targetSide
	 *            The side where the decorator has to be created.
	 * @return The list of main decorators.
	 */
	protected abstract AbstractDecorator createAndRegisterDecorator(Diff diff, View referenceView,
			IFigure referenceFigure, MergeViewerSide targetSide);

	/**
	 * Get the main decorators related to the given difference.
	 * 
	 * @param difference
	 *            The difference.
	 * @return The list of main decorators.
	 */
	protected abstract Collection<? extends AbstractDecorator> getDecorators(Diff difference);

}

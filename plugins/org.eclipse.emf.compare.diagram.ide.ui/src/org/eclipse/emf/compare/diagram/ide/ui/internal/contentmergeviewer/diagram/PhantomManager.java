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

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.or;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onFeature;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.valueIs;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.DecoratorFigure;
import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.EdgeFigure;
import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.NodeFigure;
import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.figures.NodeListFigure;
import org.eclipse.emf.compare.diagram.internal.extensions.CoordinatesChange;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.diagram.internal.extensions.Hide;
import org.eclipse.emf.compare.diagram.internal.extensions.Show;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.ICompareColor;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Phantom manager to create, hide or reveal phantom figures related to deleted or added graphical objects.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class PhantomManager extends AbstractDecoratorManager {

	/**
	 * Phantom represented by a <code>figure</code> on a <code>layer</code>, from the given <code>side</code>
	 * of the merge viewer. An edit part may be linked to the <code>figure</code> in some cases.<br>
	 * The phantom is related to a <code>difference</code> and it is binded with the reference view and
	 * figure.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	private class Phantom extends AbstractDecorator {

		/**
		 * Constructor.
		 * 
		 * @param layer
		 *            {@link Phantom#fLayer}.
		 * @param side
		 *            {@link Phantom#fSide}.
		 * @param originView
		 *            {@link Phantom#fOriginView}.
		 * @param originFigure
		 *            {@link Phantom#fOriginFigure}.
		 * @param diff
		 *            {@link Phantom#fDifference}.
		 */
		Phantom(IFigure layer, MergeViewerSide side, View originView, IFigure originFigure, Diff diff) {
			setLayer(layer);
			setSide(side);
			setOriginView(originView);
			setOriginFigure(originFigure);
			setDifference(diff);
		}

		/**
		 * Get the decorator dependencies of this one. The dependencies are the decorator ancestors plus the
		 * extremities of an edge decorator.<br>
		 * DO NOT CALL in an iterate of {@link PhantomManager#fPhantomRegistry}
		 * 
		 * @return The list of found decorators.
		 */
		public List<? extends AbstractDecorator> getDependencies() {
			List<AbstractDecorator> result = new ArrayList<AbstractDecorator>();
			result.addAll(getAncestors());
			if (fOriginView instanceof Edge) {
				View source = ((Edge)fOriginView).getSource();
				View target = ((Edge)fOriginView).getTarget();
				result.addAll(getOrCreateRelatedPhantoms(source, fSide));
				result.addAll(getOrCreateRelatedPhantoms(target, fSide));
			}
			return result;
		}

		/**
		 * Get the ancestor decorators of this one.
		 * 
		 * @return The list of the ancestors.
		 */
		private List<? extends AbstractDecorator> getAncestors() {
			List<AbstractDecorator> result = new ArrayList<AbstractDecorator>();
			EObject parentOriginView = fOriginView.eContainer();
			while (parentOriginView != null) {
				result.addAll(getOrCreateRelatedPhantoms(parentOriginView, fSide));
				parentOriginView = parentOriginView.eContainer();
			}
			return result;
		}
	}

	/** Registry of created phantoms, indexed by difference. */
	private final Map<Diff, Phantom> fPhantomRegistry = new HashMap<Diff, Phantom>();

	/** Predicate witch checks that the given difference is an ADD or DELETE of a graphical object. */
	private Predicate<Diff> isAddOrDelete = and(instanceOf(DiagramDiff.class),
			or(ofKind(DifferenceKind.ADD), ofKind(DifferenceKind.DELETE)));

	/** Predicate witch checks that the given difference is a HIDE or REVEAL of a graphical object. */
	private Predicate<Diff> isHideOrReveal = or(instanceOf(Show.class), instanceOf(Hide.class));

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
	public PhantomManager(EMFCompareConfiguration compareConfiguration, DiagramMergeViewer left,
			DiagramMergeViewer right, DiagramMergeViewer ancestor, ICompareColor color) {
		super(compareConfiguration, left, right, ancestor, color);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager#goodCandidate()
	 *      <br>
	 *      Only the diagram differences ADD/REVEAL or DELETE/HIDE are concerned by this display.
	 */
	@Override
	protected Predicate<Diff> goodCandidate() {
		return new Predicate<Diff>() {
			public boolean apply(Diff difference) {
				return Predicates.or(isAddOrDelete, isHideOrReveal).apply(difference)
						&& difference.getState() == DifferenceState.UNRESOLVED;
			}
		};
	}

	/**
	 * From the given view, get or create the related phantoms in the given side.
	 * 
	 * @param referenceView
	 *            The given view.
	 * @param side
	 *            The given side.
	 * @return The list of phantoms.
	 */
	private List<Phantom> getOrCreateRelatedPhantoms(EObject referenceView, MergeViewerSide side) {
		List<Phantom> result = new ArrayList<Phantom>();
		Collection<Diff> changes = Collections2.filter(
				getCompareConfiguration().getComparison().getDifferences(referenceView), goodCandidate());
		for (Diff change : changes) {
			Phantom phantom = fPhantomRegistry.get(change);
			if (phantom == null) {
				IFigure referenceFigure = PhantomManager.this.getFigure((View)referenceView);
				if (referenceFigure != null) {
					phantom = createAndRegisterDecorator(change, (View)referenceView, referenceFigure, side);
				}
			}
			if (phantom != null) {
				result.add(phantom);
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager#getReferenceViews(
	 *      org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff)
	 */
	@Override
	protected List<View> getReferenceViews(DiagramDiff difference) {
		List<View> result = new ArrayList<View>();

		Match match = getCompareConfiguration().getComparison().getMatch(difference.getView());

		EObject originObj = match.getOrigin();
		EObject leftObj = match.getLeft();
		EObject rightObj = match.getRight();

		if (leftObj instanceof View || rightObj instanceof View) {
			View referenceView = getReferenceView((View)originObj, (View)leftObj, (View)rightObj);
			// It may be null if it is hidden
			if (referenceView != null) {
				result.add(referenceView);
			}
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager#getTargetSide(org.eclipse.emf.compare.
	 *      Match, org.eclipse.gmf.runtime.notation.View) <br>
	 *      If the left object is null, a phantom should be drawn instead. Else, it means that the right
	 *      object is null and a phantom should be displayed on the right side.
	 */
	@Override
	public MergeViewerSide getTargetSide(Match match, View referenceView) {
		MergeViewerSide targetSide = null;
		EObject leftMatch = match.getLeft();

		if (leftMatch == null || (leftMatch instanceof View && !isFigureExist((View)leftMatch))) {
			targetSide = MergeViewerSide.LEFT;
		} else {
			targetSide = MergeViewerSide.RIGHT;
		}
		return getEffectiveSide(targetSide);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager#createAndRegisterDecorator(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.gmf.runtime.notation.View, org.eclipse.draw2d.IFigure,
	 *      org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide)
	 */
	@Override
	protected Phantom createAndRegisterDecorator(Diff diff, View referenceView, IFigure referenceFigure,
			MergeViewerSide targetSide) {
		Phantom phantom = createPhantom(diff, referenceView, referenceFigure, targetSide);
		if (phantom != null) {
			fPhantomRegistry.put(diff, phantom);
		}
		return phantom;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager#removeDecorators(org.eclipse.emf.compare.Diff)
	 */
	@Override
	public void removeDecorators(Diff difference) {
		fPhantomRegistry.remove(difference);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager#removeAll()
	 */
	@Override
	public void removeAll() {
		fPhantomRegistry.clear();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager#getDecorators(org.eclipse.emf.compare.Diff)
	 */
	@Override
	protected List<Phantom> getDecorators(Diff difference) {
		List<Phantom> result = new ArrayList<PhantomManager.Phantom>();
		Phantom phantom = fPhantomRegistry.get(difference);
		if (phantom != null) {
			result.add(phantom);
		}
		return result;
	}

	/**
	 * {@inheritDoc}.<br>
	 * DO NOT CALL on a phantom within an iteration on the phantom registry.
	 * {@link PhantomManager#fPhantomRegistry}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager#handleDecorator(org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager.AbstractDecorator,
	 *      boolean, boolean)
	 */
	@Override
	protected void handleDecorator(AbstractDecorator decorator, boolean isAdd, boolean isMain) {
		super.handleDecorator(decorator, isAdd, isMain);
		// Display the dependencies (context) of this decorator
		for (AbstractDecorator ancestor : ((Phantom)decorator).getDependencies()) {
			super.handleDecorator(ancestor, isAdd, false);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager#handleAddDecorator(org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.AbstractDecoratorManager.AbstractDecorator,
	 *      org.eclipse.draw2d.IFigure, org.eclipse.draw2d.IFigure, boolean)
	 */
	@Override
	protected void handleAddDecorator(AbstractDecorator decorator, IFigure parent, IFigure toAdd,
			boolean isMain) {
		super.handleAddDecorator(decorator, parent, toAdd, isMain);
		// Set the highlight of the figure
		if (isMain) {
			decorator.getDecoratorFigure().highlight();
			getViewer(decorator.getSide()).getGraphicalViewer().reveal(toAdd);
		} else {
			decorator.getDecoratorFigure().unhighlight();
		}
	}

	/**
	 * It checks that the given view graphically exists.
	 * 
	 * @param view
	 *            The view.
	 * @return True if it exists.
	 */
	private boolean isFigureExist(View view) {
		return view != null && view.isVisible();
	}

	/**
	 * Get the view which has to be used as reference to build a phantom.<br>
	 * The reference is the non null object among the given objects. In case of delete object, in the context
	 * of three-way comparison, the reference will be the ancestor one (<code>originObj</code>).
	 * 
	 * @param originObj
	 *            The ancestor object.
	 * @param leftView
	 *            The left object.
	 * @param rightView
	 *            The right object.
	 * @return The reference object.
	 */
	private View getReferenceView(View originObj, View leftView, View rightView) {
		View referenceView;
		if (isFigureExist(originObj)) {
			referenceView = originObj;
		} else if (isFigureExist(leftView)) {
			referenceView = leftView;
		} else {
			referenceView = rightView;
		}
		return referenceView;
	}

	/**
	 * It creates a new phantom from the given difference, view and figure.
	 * 
	 * @param diff
	 *            The related difference used as index for the main phantom.
	 * @param referenceView
	 *            The reference view as base for creation of the phantom.
	 * @param referenceFigure
	 *            The reference figure as base for creation of the phantom.
	 * @param side
	 *            The side where the phantom has to be created.
	 * @return The phantom or null if the target layer is not found.
	 */
	private Phantom createPhantom(Diff diff, View referenceView, IFigure referenceFigure,
			MergeViewerSide side) {
		IFigure targetLayer = getLayer(referenceView, side);
		if (targetLayer != null) {
			MergeViewerSide referenceSide = getSide(referenceView);

			Rectangle rect = referenceFigure.getBounds().getCopy();

			IFigure referenceLayer = getLayer(referenceView, referenceSide);
			translateCoordinates(referenceFigure, referenceLayer, rect);

			DecoratorFigure ghost = null;

			Phantom phantom = new Phantom(targetLayer, side, referenceView, referenceFigure, diff);

			// Container "list" case
			if (isNodeList(referenceView)) {

				int index = getIndex(diff, referenceView, side);

				IFigure referenceParentFigure = referenceFigure.getParent();
				Rectangle referenceParentBounds = referenceParentFigure.getBounds().getCopy();
				translateCoordinates(referenceParentFigure, referenceLayer, referenceParentBounds);

				View parentView = (View)getMatchView(referenceView.eContainer(), side);
				if (parentView != null) {
					int nbElements = getVisibleViews(parentView).size();
					// CHECKSTYLE:OFF
					if (index > nbElements) {
						// CHECKSTYLE:ON
						index = nbElements;
					}
				}

				// FIXME: The add of decorators modifies the physical coordinates of elements
				// FIXME: Compute position from the y position of the first child + sum of height of the
				// children.
				int pos = rect.height * index + referenceParentBounds.y + 1;
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put(NodeListFigure.PARAM_Y_POS, Integer.valueOf(pos));

				ghost = new NodeListFigure(diff, isThreeWay(), getCompareColor(), referenceFigure, rect, true,
						parameters);

				// Edge case
			} else if (referenceView instanceof Edge) {
				// If the edge phantom ties shapes where their coordinates changed
				if (hasAnExtremityChange((Edge)referenceView, side)) {
					EditPart edgeEditPart = createEdgeEditPart((Edge)referenceView, referenceSide, side);
					// CHECKSTYLE:OFF
					if (edgeEditPart instanceof GraphicalEditPart) {
						// CHECKSTYLE:ON
						phantom.setEditPart(edgeEditPart);

						IFigure fig = ((GraphicalEditPart)edgeEditPart).getFigure();
						fig.getChildren().clear();
						ghost = new DecoratorFigure(diff, isThreeWay(), getCompareColor(), referenceFigure,
								fig, true);

					}
					// Else, it creates only a polyline connection figure with the same properties as the
					// reference
				} else {
					if (referenceFigure instanceof PolylineConnection) {
						ghost = new EdgeFigure(diff, isThreeWay(), getCompareColor(), referenceFigure, rect,
								true);
					}
				}
			}

			// Default case: Nodes
			if (ghost == null) {
				ghost = new NodeFigure(diff, isThreeWay(), getCompareColor(), referenceFigure, rect, true);
			}

			phantom.setDecoratorFigure(ghost);

			translateWhenInsideContainerChange(phantom);

			return phantom;
		}

		return null;

	}

	/**
	 * Get the index of the phantom to draw.
	 * 
	 * @param diff
	 *            The related difference used as index for the main phantom.
	 * @param referenceView
	 *            The reference view to compute the phantom.
	 * @param side
	 *            The side where the phantom has to be drawn.
	 * @return The index in the list where the phantom has to be drawn.
	 */
	private int getIndex(Diff diff, View referenceView, MergeViewerSide side) {
		// Case for invisible objects
		if (diff instanceof Hide || diff instanceof Show) {
			List<Object> source = null;
			List<Object> target = null;
			Object newElement = null;
			Match match = diff.getMatch();
			List<Object> leftList = ReferenceUtil.getAsList(match.getLeft().eContainer(),
					NotationPackage.Literals.VIEW__PERSISTED_CHILDREN);
			List<Object> rightList = ReferenceUtil.getAsList(match.getRight().eContainer(),
					NotationPackage.Literals.VIEW__PERSISTED_CHILDREN);
			if (diff instanceof Hide) {
				source = rightList;
				target = leftList;
				newElement = diff.getMatch().getRight();
			} else {
				source = leftList;
				target = rightList;
				newElement = diff.getMatch().getLeft();
			}
			Iterable<Object> ignoredElements = Iterables.filter(target, new Predicate() {
				public boolean apply(Object input) {
					return input instanceof View && !((View)input).isVisible();
				}
			});
			return DiffUtil.findInsertionIndex(getCompareConfiguration().getComparison(), ignoredElements,
					source, target, newElement);
		}
		// Case for deleted objects
		Diff refiningDiff = Iterators.find(diff.getRefinedBy().iterator(), and(valueIs(referenceView),
				onFeature(NotationPackage.Literals.VIEW__PERSISTED_CHILDREN.getName())));

		return DiffUtil.findInsertionIndex(getCompareConfiguration().getComparison(), refiningDiff,
				side == MergeViewerSide.LEFT);
	}

	/**
	 * Get the visible view under the given parent view.
	 * 
	 * @param parent
	 *            The parent view.
	 * @return The list of views.
	 */
	private List<View> getVisibleViews(View parent) {
		return (List<View>)Lists
				.newArrayList(Iterators.filter(parent.getChildren().iterator(), new Predicate<Object>() {
					public boolean apply(Object input) {
						return input instanceof View && ((View)input).isVisible();
					}
				}));
	}

	/**
	 * It translates and resizes the figure of the given phantom when this one is nested in a container which
	 * is subjected to a coordinates change.
	 * 
	 * @param phantom
	 *            The phantom.
	 */
	private void translateWhenInsideContainerChange(Phantom phantom) {
		boolean isCandidate = false;
		Diff diff = phantom.getDifference();
		if (diff instanceof DiagramDiff) {
			EObject parent = ((DiagramDiff)diff).getView().eContainer();
			while (parent instanceof View && !isCandidate) {
				isCandidate = Iterables.any(getCompareConfiguration().getComparison().getDifferences(parent),
						instanceOf(CoordinatesChange.class));
				parent = parent.eContainer();
			}
		}
		if (isCandidate) {
			View referenceView = phantom.getOriginView();
			View parentReferenceView = (View)referenceView.eContainer();
			if (parentReferenceView != null) {
				View parentView = (View)getMatchView(parentReferenceView, phantom.getSide());
				IFigure parentFigure = getFigure(parentView);
				if (parentFigure != null) {
					Rectangle parentRect = parentFigure.getBounds().getCopy();
					translateCoordinates(parentFigure, getLayer(parentReferenceView, getSide(parentView)),
							parentRect);

					IFigure parentReferenceFigure = getFigure(parentReferenceView);
					// CHECKSTYLE:OFF
					if (parentReferenceFigure != null) {
						Rectangle parentReferenceRect = parentReferenceFigure.getBounds().getCopy();
						translateCoordinates(parentReferenceFigure,
								getLayer(parentReferenceView, getSide(parentReferenceView)),
								parentReferenceRect);

						int deltaX = parentRect.x - parentReferenceRect.x;
						int deltaY = parentRect.y - parentReferenceRect.y;
						int deltaWidth = parentRect.width - parentReferenceRect.width;
						int deltaHeight = parentRect.height - parentReferenceRect.height;

						IFigure figure = phantom.getFigure();

						Rectangle rect = figure.getBounds().getCopy();
						rect.x += deltaX;
						rect.y += deltaY;
						rect.width += deltaWidth;
						if (!(figure instanceof Polyline)) {
							rect.height += deltaHeight;
						}
						figure.setBounds(rect);

						if (figure instanceof Polyline) {

							Point firstPoint = ((Polyline)figure).getPoints().getFirstPoint().getCopy();
							Point lastPoint = ((Polyline)figure).getPoints().getLastPoint().getCopy();

							firstPoint.x += deltaX;
							firstPoint.y += deltaY;

							lastPoint.x += deltaX + deltaWidth;
							lastPoint.y += deltaY;

							((Polyline)figure).setEndpoints(firstPoint, lastPoint);

						}
					}
					// CHECKSTYLE:ON
				}
			}
		}
	}

	/**
	 * It checks that the given edge is linked to graphical objects subjected to coordinate changes, on the
	 * given side.
	 * 
	 * @param edge
	 *            The edge to check.
	 * @param targetSide
	 *            The side to check extremities (side of the phantom).
	 * @return True if an extremity at least changed its location, False otherwise.
	 */
	private boolean hasAnExtremityChange(Edge edge, MergeViewerSide targetSide) {
		View referenceSource = edge.getSource();
		View referenceTarget = edge.getTarget();
		return hasChange(referenceSource, targetSide) || hasChange(referenceTarget, targetSide);
	}

	/**
	 * It checks that the coordinates of the given view changed between left and right, from the given side.
	 * 
	 * @param referenceView
	 *            The view to check.
	 * @param targetSide
	 *            The side to focus.
	 * @return True if the view changed its location, False otherwise.
	 */
	private boolean hasChange(View referenceView, MergeViewerSide targetSide) {
		View extremity = (View)getMatchView(referenceView, targetSide);
		// Look for a related change coordinates on the extremity of the edge reference.
		Collection<Diff> diffs = Collections2.filter(
				getCompareConfiguration().getComparison().getDifferences(referenceView),
				instanceOf(CoordinatesChange.class));
		if (diffs.isEmpty()) {
			// Look for a related change coordinates on the matching extremity (other side) of the edge
			// reference.
			diffs = Collections2.filter(getCompareConfiguration().getComparison().getDifferences(extremity),
					instanceOf(CoordinatesChange.class));
		}
		return !diffs.isEmpty();
	}

	/**
	 * It creates and returns a new edit part from the given edge. This edit part listens to the reference
	 * edge but is attached to the controllers of the target (phantom) side.
	 * 
	 * @param referenceEdge
	 *            The edge as base of the edit part.
	 * @param referenceSide
	 *            The side of this edge.
	 * @param targetSide
	 *            The side where the edit part has to be created to draw the related phantom.
	 * @return The new edit part.
	 */
	private EditPart createEdgeEditPart(Edge referenceEdge, MergeViewerSide referenceSide,
			MergeViewerSide targetSide) {
		EditPart edgeEditPartReference = getViewer(referenceSide).getEditPart(referenceEdge);
		EditPart edgeEditPart = null;
		if (edgeEditPartReference instanceof ConnectionEditPart) {

			edgeEditPart = getOrCreatePhantomEditPart(referenceEdge, referenceSide, targetSide);

			if (edgeEditPart instanceof ConnectionEditPart) {
				EditPart edgeSourceEp = getOrCreateExtremityPhantomEditPart(
						((ConnectionEditPart)edgeEditPartReference).getSource(), referenceSide, targetSide);

				((ConnectionEditPart)edgeEditPart).setSource(edgeSourceEp);

				EditPart edgeTargetEp = getOrCreateExtremityPhantomEditPart(
						((ConnectionEditPart)edgeEditPartReference).getTarget(), referenceSide, targetSide);

				((ConnectionEditPart)edgeEditPart).setTarget(edgeTargetEp);
			}
		}
		return edgeEditPart;
	}

	/**
	 * From the given edit part, it retrieves the matched one, from the given target side. If the retrieved
	 * edit part is not linked to a GMF object, in the target side, a phantom GEF edit part is returned which
	 * will locate a rectangle invisible figure in the same location as the related phantom.
	 * 
	 * @param referenceEdgeExtremityEp
	 *            The reference edit part for one of the extremities of an edge.
	 * @param referenceSide
	 *            The side of the reference.
	 * @param targetSide
	 *            The other side, where the phantom has to be drawn.
	 * @return The phantom edit part used to attach the extremity of an edge phantom.
	 */
	private EditPart getOrCreateExtremityPhantomEditPart(EditPart referenceEdgeExtremityEp,
			MergeViewerSide referenceSide, MergeViewerSide targetSide) {
		View referenceExtremityView = (View)referenceEdgeExtremityEp.getModel();

		EditPart edgeExtremityEp = getOrCreatePhantomEditPart(referenceExtremityView, referenceSide,
				targetSide);

		if (isPhantomEditPart((AbstractGraphicalEditPart)edgeExtremityEp)) {

			final AbstractGraphicalEditPart edgeExtremityEpParent = (AbstractGraphicalEditPart)edgeExtremityEp
					.getParent();

			List<Phantom> phantoms = getOrCreateRelatedPhantoms(referenceExtremityView, targetSide);
			if (!phantoms.isEmpty()) {
				Phantom phantomToTarget = phantoms.get(0);
				final IFigure figureToTarget = phantomToTarget.getFigure();

				edgeExtremityEp = new org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart(
						referenceExtremityView) {
					@Override
					protected void createDefaultEditPolicies() {
					}

					@Override
					protected IFigure createFigure() {
						RectangleFigure fig = new RectangleFigure();
						fig.setBounds(figureToTarget.getBounds());
						fig.setParent(edgeExtremityEpParent.getFigure());
						return fig;
					}
				};

				edgeExtremityEp.setParent(edgeExtremityEpParent);
			}

			((AbstractGraphicalEditPart)edgeExtremityEp).activate();
			((AbstractGraphicalEditPart)edgeExtremityEp).getFigure();
		}

		return edgeExtremityEp;
	}

	/**
	 * It checks if the given edit part is related to a phantom edit part (created for nothing, without link
	 * to a GMF object in the target side).
	 * 
	 * @param editPart
	 *            The edit part to check.
	 * @return True if it is a phantom edit part, false otherwise.
	 */
	private boolean isPhantomEditPart(AbstractGraphicalEditPart editPart) {
		Rectangle targetBounds = editPart.getFigure().getBounds();
		return targetBounds.x == 0 && targetBounds.y == 0 && targetBounds.width == 0
				&& targetBounds.height == 0;
	}

	/**
	 * It creates and returns a new edit part from the given view. This edit part listens the reference view
	 * but is attached to the controllers of the target (phantom) side.
	 * 
	 * @param referenceView
	 *            The view as base of the edit part.
	 * @param referenceSide
	 *            The side of this view.
	 * @param targetSide
	 *            The side where the edit part has to be created to draw the related phantom.
	 * @return The new edit part.
	 */
	private EditPart getOrCreatePhantomEditPart(EObject referenceView, MergeViewerSide referenceSide,
			MergeViewerSide targetSide) {
		EditPart editPartParent = null;
		EditPart editPart = null;
		EditPart editPartReference = getViewer(referenceSide).getEditPart(referenceView);
		EditPart editPartReferenceParent = editPartReference.getParent();
		Object referenceViewParent = editPartReferenceParent.getModel();
		if (!(referenceViewParent instanceof EObject)) {
			referenceViewParent = referenceView.eContainer();
		}
		View viewParent = (View)getMatchView((EObject)referenceViewParent, targetSide);
		if (viewParent != null) {
			editPartParent = getViewer(targetSide).getEditPart(viewParent);
		}
		if (editPartParent == null) {
			editPartParent = getOrCreatePhantomEditPart((EObject)referenceViewParent, referenceSide,
					targetSide);

		}
		if (editPartParent != null) {
			View view = (View)getMatchView(referenceView, targetSide);
			if (view != null) {
				editPart = getViewer(targetSide).getEditPart(view);
			}
			if (editPart == null) {

				editPart = getViewer(targetSide).getGraphicalViewer().getEditPartFactory()
						.createEditPart(editPartParent, referenceView);
				editPart.setParent(editPartParent);
				getViewer(targetSide).getGraphicalViewer().getEditPartRegistry().put(referenceView, editPart);

			}

		}
		return editPart;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.IDecoratorManager#hideAll()
	 */
	public void hideAll() {
		for (Phantom phantom : fPhantomRegistry.values()) {
			handleDeleteDecorator(phantom, phantom.getLayer(), phantom.getFigure());
		}
	}
}

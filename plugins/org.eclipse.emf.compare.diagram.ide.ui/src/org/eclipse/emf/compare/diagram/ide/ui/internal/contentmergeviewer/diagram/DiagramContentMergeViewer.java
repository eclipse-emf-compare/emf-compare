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

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.or;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onFeature;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.valueIs;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.DiagramDiff;
import org.eclipse.emf.compare.diagram.ide.ui.AbstractEditPartMergeViewer;
import org.eclipse.emf.compare.diagram.ide.ui.AbstractGraphicalMergeViewer;
import org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramDiffAccessor;
import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.DiagramCompareContentMergeViewer;
import org.eclipse.emf.compare.ide.EMFCompareIDEPlugin;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.DiffNode;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.utils.DiffUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.BasicCompartment;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;

/**
 * Specialized {@link org.eclipse.compare.contentmergeviewer.ContentMergeViewer} that uses
 * {@link org.eclipse.jface.viewers.TreeViewer} to display left, right and ancestor {@link EObject}.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramContentMergeViewer extends DiagramCompareContentMergeViewer {

	/**
	 * Phantom manager to create, hide or reveal phantom figures related to deleted or added graphical
	 * objects.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	private class PhantomManager {

		/**
		 * Phantom represented by a <code>figure</code> on a <code>layer</code>, from the given
		 * <code>side</code> of the merge viewer. An edit part may be linked to the <code>figure</code> in
		 * some cases.<br>
		 * The phantom is related to a <code>difference</code> and it is binded with the reference view and
		 * figure.
		 * 
		 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
		 */
		private class Phantom {
			/** The reference <code>View</code> for this phantom. */
			private View fOriginView;

			/** The reference <code>IFigure</code> for this phantom. */
			private IFigure fOriginFigure;

			/** The <code>IFigure</code> representing this phantom. */
			private IFigure fFigure;

			/** The layer on which the <code>figure</code> has to be drawn. */
			private IFigure fLayer;

			/** The side of the merge viewer on which the <code>figure</code> has to be drawn. */
			private MergeViewerSide fSide;

			/** The difference related to this phantom. */
			private Diff fDifference;

			/** The edit part of the figure representing this phantom. May be null. */
			private EditPart fEditPart;

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
			public Phantom(IFigure layer, MergeViewerSide side, View originView, IFigure originFigure,
					Diff diff) {
				setLayer(layer);
				setSide(side);
				setOriginView(originView);
				setOriginFigure(originFigure);
				setDifference(diff);
			}

			/**
			 * Constructor.
			 * 
			 * @param figure
			 *            {@link Phantom#fFigure}.
			 * @param editPart
			 *            {@link Phantom#fEditPart}.
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
			public Phantom(IFigure figure, EditPart editPart, IFigure layer, MergeViewerSide side,
					View originView, IFigure originFigure, Diff diff) {
				setFigure(figure);
				setLayer(layer);
				setSide(side);
				setOriginView(originView);
				setOriginFigure(originFigure);
				setDifference(diff);
				setEditPart(editPart);
			}

			/**
			 * Get the ancestor phantoms of this one.
			 * 
			 * @return The list of the ancestors.
			 */
			private List<Phantom> getAncestors() {
				List<Phantom> result = new ArrayList<Phantom>();
				EObject parentOriginView = fOriginView.eContainer();
				while (parentOriginView != null) {
					result.addAll(getOrCreatePhantoms(parentOriginView));
					parentOriginView = parentOriginView.eContainer();
				}
				return result;
			}

			/**
			 * From the given view, get or create the related phantoms.
			 * 
			 * @param referenceView
			 *            The given view.
			 * @return The list of phantoms.
			 */
			private List<Phantom> getOrCreatePhantoms(EObject referenceView) {
				List<Phantom> result = new ArrayList<Phantom>();
				Collection<Diff> diffs = Collections2.filter(fComparison.getDifferences(referenceView),
						goodCandidate());
				for (Diff change : diffs) {
					Phantom phantom = fPhantomRegistry.get(change);
					if (phantom == null) {
						phantom = createAndRegisterPhantom(change, (View)referenceView, PhantomManager.this
								.getFigure((View)referenceView), fSide);
					}
					result.add(phantom);
				}
				return result;
			}

			/**
			 * Get the phantom dependencies of this one. The dependencies are the phantom ancestors plus the
			 * extremities of an edge phantom.
			 * 
			 * @return The list of found phantoms.
			 */
			public List<Phantom> getDependencies() {
				List<Phantom> result = new ArrayList<Phantom>();
				result.addAll(getAncestors());
				if (fOriginView instanceof Edge) {
					View source = ((Edge)fOriginView).getSource();
					View target = ((Edge)fOriginView).getTarget();
					result.addAll(getOrCreatePhantoms(source));
					result.addAll(getOrCreatePhantoms(target));
				}
				return result;
			}

			/**
			 * Getter.
			 * 
			 * @return the originView {@link Phantom#fOriginView}.
			 */
			public View getOriginView() {
				return fOriginView;
			}

			/**
			 * Setter.
			 * 
			 * @param originView
			 *            {@link Phantom#fOriginView}.
			 */
			public void setOriginView(View originView) {
				this.fOriginView = originView;
			}

			/**
			 * Getter.
			 * 
			 * @return the originFigure {@link Phantom#fOriginFigure}.
			 */
			public IFigure getOriginFigure() {
				return fOriginFigure;
			}

			/**
			 * Setter.
			 * 
			 * @param originFigure
			 *            {@link Phantom#fOriginFigure}.
			 */
			public void setOriginFigure(IFigure originFigure) {
				this.fOriginFigure = originFigure;
			}

			/**
			 * Getter.
			 * 
			 * @return the figure {@link Phantom#fFigure}.
			 */
			public IFigure getFigure() {
				return fFigure;
			}

			/**
			 * Setter.
			 * 
			 * @param figure
			 *            {@link Phantom#fFigure}.
			 */
			public void setFigure(IFigure figure) {
				this.fFigure = figure;
			}

			/**
			 * Getter.
			 * 
			 * @return the layer {@link Phantom#fLayer}.
			 */
			public IFigure getLayer() {
				return fLayer;
			}

			/**
			 * Setter.
			 * 
			 * @param layer
			 *            {@link Phantom#fLayer}.
			 */
			public void setLayer(IFigure layer) {
				this.fLayer = layer;
			}

			/**
			 * Getter.
			 * 
			 * @return the side {@link Phantom#fSide}.
			 */
			public MergeViewerSide getSide() {
				return fSide;
			}

			/**
			 * Setter.
			 * 
			 * @param side
			 *            {@link Phantom#fSide}.
			 */
			public void setSide(MergeViewerSide side) {
				this.fSide = side;
			}

			/**
			 * Getter.
			 * 
			 * @return the difference {@link Phantom#fDifference}.
			 */
			public Diff getDifference() {
				return fDifference;
			}

			/**
			 * Setter.
			 * 
			 * @param difference
			 *            {@link Phantom#fDifference}.
			 */
			public void setDifference(Diff difference) {
				this.fDifference = difference;
			}

			/**
			 * Getter.
			 * 
			 * @return the editPart {@link Phantom#fEditPart}.
			 */
			public EditPart getEditPart() {
				return fEditPart;
			}

			/**
			 * Setter.
			 * 
			 * @param editPart
			 *            {@link Phantom#fEditPart}.
			 */
			public void setEditPart(EditPart editPart) {
				this.fEditPart = editPart;
			}
		}

		/** Registry of created phantoms, indexed by difference. */
		private final Map<Diff, Phantom> fPhantomRegistry = new HashMap<Diff, Phantom>();

		/**
		 * Constructor.
		 */
		public PhantomManager() {

		}

		/**
		 * From a given difference, it hides the related phantoms.
		 * 
		 * @param difference
		 *            The difference.
		 */
		public void hidePhantoms(Diff difference) {
			Phantom old = getPhantom(difference);
			if (old != null && fComparison != null) {
				handlePhantom(old, false);
			}
		}

		/**
		 * It reveals the given phantom and its dependencies.
		 * 
		 * @param phantom
		 *            The phantom.
		 */
		public void revealPhantoms(Phantom phantom) {
			handlePhantom(phantom, true);
		}

		/**
		 * From a given difference, it reveals the related phantoms.
		 * 
		 * @param difference
		 *            The difference.
		 */
		public void revealPhantoms(Diff difference) {

			Phantom phantom = getPhantom(difference);

			// Create phantoms only if they do not already exist and if the related difference is an ADD or
			// DELETE
			if (phantom == null && isGoodCandidate(difference)) {

				DiagramDiff diagramDiff = (DiagramDiff)difference;

				Match match = fComparison.getMatch(diagramDiff.getView());

				EObject originObj = match.getOrigin();
				EObject leftObj = match.getLeft();
				EObject rightObj = match.getRight();

				if (leftObj instanceof View || rightObj instanceof View) {

					View leftView = (View)leftObj;
					View rightView = (View)rightObj;

					View referenceView = getReferenceView((View)originObj, leftView, rightView);
					IFigure referenceFigure = getFigure(referenceView);

					MergeViewerSide targetSide = getTargetSide(leftView);

					phantom = createAndRegisterPhantom(difference, referenceView, referenceFigure, targetSide);

				}

			}

			// The selected difference is an ADD or DELETE and a phantom exists for it
			if (phantom != null) {
				revealPhantoms(phantom);
			}
		}

		/**
		 * Get the phantom related to the given difference.
		 * 
		 * @param difference
		 *            The difference.
		 * @return The main phantom.
		 */
		public Phantom getPhantom(Diff difference) {
			return fPhantomRegistry.get(difference);
		}

		/**
		 * Get the predicate to know the differences concerned by the display of phantoms.<br>
		 * Only the diagram differences ADD or DELETE are concerned by this display.
		 * 
		 * @return The predicate.
		 */
		private Predicate<Diff> goodCandidate() {
			return new Predicate<Diff>() {
				public boolean apply(Diff difference) {
					return and(instanceOf(DiagramDiff.class),
							or(ofKind(DifferenceKind.ADD), ofKind(DifferenceKind.DELETE))).apply(difference);
				}
			};
		}

		/**
		 * It manages the display of the given phantom and its dependencies.
		 * 
		 * @param phantom
		 *            The main phantom to handle.
		 * @param isAdd
		 *            True if the phantoms have to be revealed, False otherwise.
		 */
		private void handlePhantom(Phantom phantom, boolean isAdd) {
			handlePhantomFigure(phantom, isAdd);
			IFigure figure = phantom.getFigure();
			if (figure instanceof Shape) {
				if (isAdd) {
					((Shape)figure).setLineWidth(((Shape)figure).getLineWidth() + 1);
					Color strokeColor = getCompareColor().getStrokeColor(phantom.getDifference(),
							isThreeWay(), false, true);
					figure.setForegroundColor(strokeColor);
					// FIXME: Find a way to set the focus on this figure.
					getViewer(phantom.getSide()).getGraphicalViewer().reveal(figure);
				} else {
					((Shape)figure).setLineWidth(((Shape)figure).getLineWidth() - 1);
				}
			}
			for (Phantom ancestor : phantom.getDependencies()) {
				handlePhantomFigure(ancestor, isAdd);
				if (isAdd) {
					Color strokeColor = getCompareColor().getStrokeColor(ancestor.getDifference(),
							isThreeWay(), false, false);
					ancestor.getFigure().setForegroundColor(strokeColor);
				}
			}
		}

		/**
		 * It manages the display of the given phantom.
		 * 
		 * @param phantom
		 *            The phantom to handle.
		 * @param isAdd
		 *            True if it has to be revealed, False otherwise.
		 */
		private void handlePhantomFigure(Phantom phantom, boolean isAdd) {
			if (phantom.getEditPart() == null) {
				if (isAdd) {
					phantom.getLayer().add(phantom.getFigure());
				} else {
					phantom.getLayer().remove(phantom.getFigure());
				}
			} else {
				if (isAdd) {
					phantom.getEditPart().activate();
					phantom.getLayer().add(phantom.getFigure());
				} else {
					phantom.getEditPart().deactivate();
					phantom.getLayer().remove(phantom.getFigure());
				}
			}
		}

		/**
		 * It checks if the given difference is a good candidate to manage phantoms.<br>
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
		 * Get the view which has to be used as reference to build a phantom.<br>
		 * The reference is the non null object among the given objects. In case of delete object, in the
		 * context of three-way comparison, the reference will be the ancestor one (<code>originObj</code>).
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
			if (originObj != null) {
				referenceView = originObj;
			} else if (leftView != null) {
				referenceView = leftView;
			} else {
				referenceView = rightView;
			}
			return referenceView;
		}

		/**
		 * Get the figure related to the given view.
		 * 
		 * @param view
		 *            The view.
		 * @return the figure.
		 */
		private IFigure getFigure(View view) {
			MergeViewerSide side = getSide(view);
			GraphicalEditPart originEditPart = (GraphicalEditPart)getViewer(side).getEditPart(view);
			return originEditPart.getFigure();
		}

		/**
		 * Get the side where phantoms have to be drawn, according to the given left view.<br>
		 * If the left object is null, a phantom should be drawn instead. Else, it means that the right object
		 * is null and a phantom should be displayed on the right side.
		 * 
		 * @param leftView
		 *            The left view. It may be null.
		 * @return The side for phantoms.
		 */
		private MergeViewerSide getTargetSide(View leftView) {
			MergeViewerSide targetSide = null;
			if (leftView == null) {
				targetSide = MergeViewerSide.LEFT;
			} else {
				targetSide = MergeViewerSide.RIGHT;
			}
			return targetSide;
		}

		/**
		 * It creates a new phantom and registers it in the {@link PhantomManager#fPhantomRegistry}.
		 * 
		 * @param diff
		 *            The related difference used as index for the main phantom.
		 * @param referenceView
		 *            The reference view as base for creation of the phantom.
		 * @param referenceFigure
		 *            The reference figure as base for creation of the phantom.
		 * @param targetSide
		 *            The side where the phantom has to be created.
		 * @return The phantom.
		 */
		private Phantom createAndRegisterPhantom(Diff diff, View referenceView, IFigure referenceFigure,
				MergeViewerSide targetSide) {
			Phantom phantom = createPhantom(diff, referenceView, referenceFigure, targetSide);
			fPhantomRegistry.put(diff, phantom);
			return phantom;
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
		 * @return The phantom.
		 */
		private Phantom createPhantom(Diff diff, View referenceView, IFigure referenceFigure,
				MergeViewerSide side) {

			MergeViewerSide referenceSide = getSide(referenceView);

			Rectangle rect = referenceFigure.getBounds().getCopy();
			rect.performScale(((DiagramRootEditPart)getViewer(side).getGraphicalViewer().getRootEditPart())
					.getZoomManager().getZoom());

			IFigure referenceLayer = getLayer(referenceView, referenceSide);
			translateCoordinates(referenceFigure, referenceLayer, rect);

			// Default case: Nodes
			IFigure ghost = new RectangleFigure();
			ghost.setBounds(rect);
			// FIXME: To manage case inside a container where its coordinates changed.

			IFigure targetLayer = getLayer(referenceView, side);
			Phantom phantom = new Phantom(targetLayer, side, referenceView, referenceFigure, diff);

			// Container "list" case
			if (referenceView.eContainer() instanceof BasicCompartment) {
				ghost = new Polyline();

				Diff refiningDiff = Iterators.find(diff.getRefinedBy().iterator(), and(
						valueIs(referenceView), onFeature(NotationPackage.Literals.VIEW__PERSISTED_CHILDREN
								.getName())));

				// FIXME:
				// - It has to manage visible views.
				// - About transient children ?
				int index = DiffUtil.findInsertionIndex(fComparison, refiningDiff,
						side == MergeViewerSide.LEFT);

				IFigure referenceParentFigure = referenceFigure.getParent();
				Rectangle referenceParentBounds = referenceParentFigure.getBounds().getCopy();
				translateCoordinates(referenceParentFigure, referenceLayer, referenceParentBounds);
				// FIXME: To manage case inside a container where its coordinates changed.

				View parentView = (View)getMatchView(referenceView.eContainer(), side);
				if (parentView != null) {
					int nbElements = getVisibleViews(parentView).size();
					if (index > nbElements) {
						index = nbElements;
					}
				}

				// FIXME: The add of decorators modifies the physical coordinates of elements
				int pos = rect.height * index + referenceParentBounds.y + 1;

				((Polyline)ghost).addPoint(new Point(rect.x, pos));
				((Polyline)ghost).addPoint(new Point(rect.x + rect.width, pos));

				// Edge case
			} else if (referenceView instanceof Edge) {
				// If the edge phantom ties shapes where their coordinates changed
				if (hasAnExtremityChange((Edge)referenceView, side)) {
					EditPart edgeEditPart = createEdgeEditPart((Edge)referenceView, referenceSide, side);
					if (edgeEditPart instanceof GraphicalEditPart) {
						phantom.setEditPart(edgeEditPart);
						edgeEditPart.activate();
						ghost = ((GraphicalEditPart)edgeEditPart).getFigure();
						ghost.getChildren().clear();
					}
					// Else, it creates only a polyline connection figure with the same properties as the
					// reference
				} else {
					EditPart edgeEditPartReference = getViewer(referenceSide).getEditPart(referenceView);
					if (edgeEditPartReference instanceof ConnectionEditPart) {
						IFigure ref = ((ConnectionEditPart)edgeEditPartReference).getFigure();
						// CHECKSTYLE:OFF
						if (ref instanceof PolylineConnection) {
							// CHECKSTYLE:ON
							ghost = new PolylineConnection();
							ghost.setBounds(ref.getBounds().getCopy());
							((PolylineConnection)ghost).setPoints(((PolylineConnection)ref).getPoints()
									.getCopy());
						}
					}
				}
			}

			if (ghost instanceof Shape) {
				((Shape)ghost).setFill(false);
			}

			phantom.setFigure(ghost);

			return phantom;
		}

		/**
		 * Get the visible view under the given parent view.
		 * 
		 * @param parent
		 *            The parent view.
		 * @return The list of views.
		 */
		private List<View> getVisibleViews(View parent) {
			return (List<View>)Lists.newArrayList(Iterators.filter(parent.getChildren().iterator(),
					new Predicate<Object>() {
						public boolean apply(Object input) {
							return input instanceof View && ((View)input).isVisible();
						}
					}));
		}

		/**
		 * It translates the coordinates of the given bounds, from the reference figure and the root of this
		 * one, to absolute coordinates.
		 * 
		 * @param referenceFigure
		 *            The reference figure.
		 * @param rootReferenceFigure
		 *            The root of the reference figure.
		 * @param boundsToTranslate
		 *            The bounds to translate.
		 */
		private void translateCoordinates(IFigure referenceFigure, IFigure rootReferenceFigure,
				Rectangle boundsToTranslate) {
			IFigure referenceParentFigure = referenceFigure.getParent();
			if (referenceParentFigure != null && referenceFigure != rootReferenceFigure) {
				if (referenceParentFigure.isCoordinateSystem()) {
					boundsToTranslate.x += referenceParentFigure.getBounds().x;
					boundsToTranslate.y += referenceParentFigure.getBounds().y;
				}
				translateCoordinates(referenceParentFigure, rootReferenceFigure, boundsToTranslate);
			}
		}

		/**
		 * It checks that the given edge is linked to graphical objects subjected to coordinate changes, on
		 * the given side.
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
		 * It checks that the coordinates of the given view changed between left and right, from the given
		 * side.
		 * 
		 * @param referenceExtremity
		 *            The view to check.
		 * @param targetSide
		 *            The side to focus.
		 * @return True if the view changed its location, False otherwise.
		 */
		private boolean hasChange(View referenceExtremity, MergeViewerSide targetSide) {
			DifferenceKind lookup = DifferenceKind.MOVE; // FIXME: it will be change to CHANGE (change
															// coordinates)
			View extremity = (View)getMatchView(referenceExtremity, targetSide);
			// Look for a related change coordinates on the extremity of the edge reference.
			Collection<Diff> diffs = Collections2.filter(fComparison.getDifferences(referenceExtremity), and(
					instanceOf(DiagramDiff.class), ofKind(lookup)));
			if (diffs.isEmpty()) {
				// Look for a related change coordinates on the matching extremity (other side) of the edge
				// reference.
				diffs = Collections2.filter(fComparison.getDifferences(extremity), and(
						instanceOf(DiagramDiff.class), ofKind(lookup)));
			}
			return !diffs.isEmpty();
		}

		/**
		 * Get the layer on the given side, from the reference view.<br>
		 * 
		 * @see @ link PhantomManager#getIDLayer(View)} .
		 * @param referenceView
		 *            The reference view.
		 * @param side
		 *            The side where the layer has to be found.
		 * @return The layer figure.
		 */
		private IFigure getLayer(View referenceView, MergeViewerSide side) {
			Diagram referenceDiagram = referenceView.getDiagram();
			Diagram targetDiagram = (Diagram)getMatchView(referenceDiagram, side);
			DiagramMergeViewer targetViewer = getViewer(side);
			IFigure targetLayer = LayerManager.Helper.find(targetViewer.getEditPart(targetDiagram)).getLayer(
					getIDLayer(referenceView));
			return targetLayer;
		}

		/**
		 * Get the layer ID to use from the reference view.<br>
		 * If the reference view is an edge, it is the {@link LayerConstants.CONNECTION_LAYER} which is used,
		 * {@link LayerConstants.SCALABLE_LAYERS} otherwise.
		 * 
		 * @param referenceView
		 *            The reference view.
		 * @return The ID of te layer.
		 */
		private Object getIDLayer(View referenceView) {
			if (referenceView instanceof Edge) {
				return LayerConstants.CONNECTION_LAYER;
			} else {
				return LayerConstants.SCALABLE_LAYERS;
			}
		}

		/**
		 * It creates and returns a new edit part from the given edge. This edit part listens the reference
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
				edgeEditPart = createEditPartForPhantoms(referenceEdge, referenceSide, targetSide);
				if (edgeEditPart instanceof ConnectionEditPart) {
					View source = (View)((ConnectionEditPart)edgeEditPartReference).getSource().getModel();
					if (source == null) {
						source = referenceEdge.getSource();
					}
					View target = (View)((ConnectionEditPart)edgeEditPartReference).getTarget().getModel();
					if (target == null) {
						target = referenceEdge.getTarget();
					}
					EditPart sourceEp = createEditPartForPhantoms(source, referenceSide, targetSide);
					((AbstractGraphicalEditPart)sourceEp).activate();
					((AbstractGraphicalEditPart)sourceEp).getFigure();
					((ConnectionEditPart)edgeEditPart).setSource(sourceEp);
					EditPart targetEp = createEditPartForPhantoms(target, referenceSide, targetSide);
					((AbstractGraphicalEditPart)targetEp).activate();
					((AbstractGraphicalEditPart)targetEp).getFigure();
					((ConnectionEditPart)edgeEditPart).setTarget(targetEp);
				}
			}
			return edgeEditPart;
		}

		/**
		 * It creates and returns a new edit part from the given view. This edit part listens the reference
		 * view but is attached to the controllers of the target (phantom) side.
		 * 
		 * @param referenceView
		 *            The view as base of the edit part.
		 * @param referenceSide
		 *            The side of this view.
		 * @param targetSide
		 *            The side where the edit part has to be created to draw the related phantom.
		 * @return The new edit part.
		 */
		private EditPart createEditPartForPhantoms(EObject referenceView, MergeViewerSide referenceSide,
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
				editPartParent = createEditPartForPhantoms((EObject)referenceViewParent, referenceSide,
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
					editPart.removeEditPolicy(EditPolicyRoles.DECORATION_ROLE);
					editPart.removeEditPolicy(EditPolicyRoles.OPEN_ROLE);
					if (editPart instanceof IGraphicalEditPart) {
						((IGraphicalEditPart)editPart).disableEditMode();
					}
					getViewer(targetSide).getGraphicalViewer().getEditPartRegistry().put(referenceView,
							editPart);
				}

			}
			return editPart;
		}
	}

	/**
	 * Bundle name of the property file containing all displayed strings.
	 */
	private static final String BUNDLE_NAME = DiagramContentMergeViewer.class.getName();

	/** The phantom manager to use in the context of this viewer. */
	private PhantomManager phantomManager = new PhantomManager();

	/** The current "opened" difference. */
	private Diff currentSelectedDiff;

	/**
	 * The adapter factory used to create the content and label provider for ancestor, left and right
	 * {@link DiagramMergeViewer}.
	 */
	private final ComposedAdapterFactory fAdapterFactory;

	/**
	 * Creates a new {@link DiagramContentMergeViewer} by calling the super constructor with the given
	 * parameters.
	 * <p>
	 * It calls {@link #buildControl(Composite)} as stated in its javadoc.
	 * <p>
	 * It sets a {@link GMFModelContentMergeContentProvider}
	 * {@link #setContentProvider(org.eclipse.jface.viewers.IContentProvider) content provider} to properly
	 * display ancestor, left and right parts.
	 * 
	 * @param parent
	 *            the parent composite to build the UI in
	 * @param config
	 *            the {@link CompareConfiguration}
	 */
	public DiagramContentMergeViewer(Composite parent, CompareConfiguration config) {
		super(parent, SWT.NONE, ResourceBundle.getBundle(BUNDLE_NAME), config);
		fAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		fAdapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		fAdapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());

		buildControl(parent);
		setContentProvider(new GMFModelContentMergeContentProvider(config, fComparison));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		fAdapterFactory.dispose();
		super.handleDispose(event);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getAncestorMergeViewer()
	 */
	@SuppressWarnings("unchecked")
	// see createMergeViewer() to see it is safe
	@Override
	public AbstractGraphicalMergeViewer getAncestorMergeViewer() {
		return (AbstractGraphicalMergeViewer)super.getAncestorMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getLeftMergeViewer()
	 */
	@SuppressWarnings("unchecked")
	// see createMergeViewer() to see it is safe
	@Override
	public AbstractGraphicalMergeViewer getLeftMergeViewer() {
		return (AbstractGraphicalMergeViewer)super.getLeftMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getRightMergeViewer()
	 */
	@SuppressWarnings("unchecked")
	// see createMergeViewer() to see it is safe
	@Override
	public AbstractGraphicalMergeViewer getRightMergeViewer() {
		return (AbstractGraphicalMergeViewer)super.getRightMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#copyDiff(boolean)
	 */
	@Override
	protected void copyDiff(boolean leftToRight) {
		/*
		 * FIXME change this! For the moment we always do a new setInput() on the content viewer whenever we
		 * select a Diagram Difference. This is meant to change so that we use selection synchronization
		 * instead. This code will break whenever we implement that change.
		 */
		if (getInput() instanceof DiffNode) {
			final Command command = getEditingDomain().createCopyCommand(((DiffNode)getInput()).getTarget(),
					leftToRight, EMFCompareIDEPlugin.getDefault().getMergerRegistry());
			getEditingDomain().getCommandStack().execute(command);

			if (leftToRight) {
				setRightDirty(true);
			} else {
				setLeftDirty(true);
			}
			refresh();
			return;
		}
		final IStructuredSelection selection;
		if (leftToRight) {
			selection = (IStructuredSelection)getLeftMergeViewer().getSelection();
		} else {
			selection = (IStructuredSelection)getRightMergeViewer().getSelection();
		}

		Object firstElement = selection.getFirstElement();

		if (firstElement instanceof GraphicalEditPart) {
			Object elt = ((GraphicalEditPart)firstElement).getModel();
			if (elt instanceof EObject) {
				List<Diff> differences = getComparison().getDifferences((EObject)elt);

				final Command command = getEditingDomain().createCopyAllNonConflictingCommand(differences,
						leftToRight, EMFCompareIDEPlugin.getDefault().getMergerRegistry());
				getEditingDomain().getCommandStack().execute(command);

				if (leftToRight) {
					setRightDirty(true);
				} else {
					setLeftDirty(true);
				}
				refresh();
			}
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#getContents(boolean)
	 */
	@Override
	protected byte[] getContents(boolean left) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#createMergeViewer(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected AbstractEditPartMergeViewer createMergeViewer(final Composite parent, MergeViewerSide side,
			DiagramCompareContentMergeViewer master) {
		final DiagramMergeViewer mergeTreeViewer = new DiagramMergeViewer(parent, side);
		return mergeTreeViewer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#paintCenter(org.eclipse.swt.widgets.Canvas,
	 *      org.eclipse.swt.graphics.GC)
	 */
	@Override
	protected void paintCenter(GC g) {

	}

	@Override
	protected void updateContent(Object ancestor, Object left, Object right) {
		super.updateContent(ancestor, left, right);

		fLeft.flush();
		((DiagramMergeViewer)fLeft).getGraphicalViewer().flush();
		fRight.flush();
		((DiagramMergeViewer)fRight).getGraphicalViewer().flush();
		fAncestor.flush();
		((DiagramMergeViewer)fAncestor).getGraphicalViewer().flush();

		if (left instanceof IDiagramDiffAccessor) {
			IDiagramDiffAccessor input = (IDiagramDiffAccessor)left;

			// initialization: reset the current difference selection hiding potential visible phantoms
			if (currentSelectedDiff != null) {
				phantomManager.hidePhantoms(currentSelectedDiff);
			}

			Diff diff = input.getDiff();
			currentSelectedDiff = diff;

			phantomManager.revealPhantoms(diff);

			// reveal object
			EObject view = ((DiagramDiff)diff).getView();
			DiagramMergeViewer targetViewer = getViewer(getSide((View)view));
			getViewer(getSide((View)view)).getGraphicalViewer().reveal(targetViewer.getEditPart(view));
		}

	}

	/**
	 * Utility method to retrieve the {@link DiagramMergeViewer} from the given side.
	 * 
	 * @param side
	 *            The side to focus.
	 * @return The viewer.
	 */
	private DiagramMergeViewer getViewer(MergeViewerSide side) {
		DiagramMergeViewer result = null;
		switch (side) {
			case LEFT:
				result = (DiagramMergeViewer)fLeft;
				break;
			case RIGHT:
				result = (DiagramMergeViewer)fRight;
				break;
			case ANCESTOR:
				result = (DiagramMergeViewer)fAncestor;
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
	private MergeViewerSide getSide(View view) {
		MergeViewerSide result = null;
		Match match = fComparison.getMatch(view);
		if (match.getLeft() == view) {
			result = MergeViewerSide.LEFT;
		} else if (match.getRight() == view) {
			result = MergeViewerSide.RIGHT;
		} else if (match.getOrigin() == view) {
			result = MergeViewerSide.ANCESTOR;
		}
		return result;
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
	private EObject getMatchView(EObject object, MergeViewerSide side) {
		Match match = fComparison.getMatch(object);
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

}

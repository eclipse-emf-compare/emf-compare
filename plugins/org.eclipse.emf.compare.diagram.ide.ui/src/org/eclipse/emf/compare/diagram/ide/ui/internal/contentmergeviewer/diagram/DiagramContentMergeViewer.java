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

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.DiagramDiff;
import org.eclipse.emf.compare.diagram.NodeChange;
import org.eclipse.emf.compare.diagram.ide.ui.DMergeViewer;
import org.eclipse.emf.compare.diagram.ide.ui.GraphicalMergeViewer;
import org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramDiffAccessor;
import org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramNodeAccessor;
import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.DiagramCompareContentMergeViewer;
import org.eclipse.emf.compare.diagram.ui.decoration.DeleteGhostImageFigure;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree.TreeContentMergeViewerContentProvider;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.notation.BasicCompartment;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Specialized {@link org.eclipse.compare.contentmergeviewer.ContentMergeViewer} that uses
 * {@link org.eclipse.jface.viewers.TreeViewer} to display left, right and ancestor {@link EObject}.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramContentMergeViewer extends DiagramCompareContentMergeViewer {

	private Map<IFigure, Phantom> phantomsMap = new HashMap<IFigure, Phantom>();

	private Map<IFigure, IFigure> containerMap = new HashMap<IFigure, IFigure>();

	private Map<IFigure, Integer> phantomSelection = new HashMap<IFigure, Integer>();

	/**
	 * Bundle name of the property file containing all displayed strings.
	 */
	private static final String BUNDLE_NAME = DiagramContentMergeViewer.class.getName();

	/**
	 * The {@link org.eclipse.emf.common.notify.AdapterFactory} used to create
	 * {@link AdapterFactoryContentProvider} and {@link AdapterFactoryLabelProvider} for ancestor, left and
	 * right {@link org.eclipse.jface.viewers.TreeViewer}.
	 */
	private final ComposedAdapterFactory fAdapterFactory;

	/**
	 * Creates a new {@link DiagramContentMergeViewer} by calling the super constructor with the given
	 * parameters.
	 * <p>
	 * It calls {@link #buildControl(Composite)} as stated in its javadoc.
	 * <p>
	 * It sets a {@link TreeContentMergeViewerContentProvider specific}
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
	public GraphicalMergeViewer getAncestorMergeViewer() {
		return (GraphicalMergeViewer)super.getAncestorMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getLeftMergeViewer()
	 */
	@SuppressWarnings("unchecked")
	// see createMergeViewer() to see it is safe
	@Override
	public GraphicalMergeViewer getLeftMergeViewer() {
		return (GraphicalMergeViewer)super.getLeftMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#getRightMergeViewer()
	 */
	@SuppressWarnings("unchecked")
	// see createMergeViewer() to see it is safe
	@Override
	public GraphicalMergeViewer getRightMergeViewer() {
		return (GraphicalMergeViewer)super.getRightMergeViewer();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.EMFCompareContentMergeViewer#copyDiff(boolean)
	 */
	@Override
	protected void copyDiff(boolean leftToRight) {
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
						leftToRight);
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
	protected DMergeViewer createMergeViewer(final Composite parent, MergeViewerSide side,
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

	private class Phantom {
		public IFigure figure;

		public IFigure layer;

		public Phantom(IFigure figure, IFigure layer) {
			this.figure = figure;
			this.layer = layer;
		}
	}

	private Predicate<Diff> diffsForPhantoms = and(ofKind(DifferenceKind.DELETE),
			instanceOf(NodeChange.class));

	@Override
	protected void updateContent(Object ancestor, Object left, Object right) {
		// TODO: To Fix the problem to locate ghost figures
		// TODO: To test multi-diagrams to clean phantoms
		// TODO: To manage the edge cases for phantoms (display the same figures (composing the edge) but in
		// red color)
		// TODO: To change decorators (to take off the icons (+ for adds) and use the same decoration as
		// changes for moves)
		super.updateContent(ancestor, left, right);

		// FIXME: Alternative solution (start of implementation) to correctly manage the position of ghost
		// figures.
		// if (left instanceof IDiagramNodeAccessor) {
		//
		// IDiagramNodeAccessor input = (IDiagramNodeAccessor)left;
		// Comparison comparison = input.getComparison();
		//
		// PhantomsManager manager = new PhantomsManager();
		// manager.initCacheFigures(comparison);
		// manager.initPhantoms(comparison, Collections2.filter(input.getAllDiffs(), diffsForPhantoms));
		// manager.attachPhantoms();
		//
		// }

		if (left instanceof IDiagramNodeAccessor) {
			IDiagramNodeAccessor input = (IDiagramNodeAccessor)left;

			Collection<Diff> deleteDiffs = Collections2.filter(input.getAllDiffs(), diffsForPhantoms);

			// *** BEGIN first solution ***
			// FIXME: In some cases (o.e.e.c.d.ide.ui.papyrus.tests/NodeChange/a9) the position of the ghost
			// is outside the visible containing figure.
			// You have to create the hierarchy of figures between the visible one and the new ghost figure.
			// For that, you have to map all the figures from every side (see alternative solution).
			if (phantomsMap.isEmpty()) {

				fLeft.flush();
				((DiagramMergeViewer)fLeft).getGraphicalViewer().flush();
				fRight.flush();
				((DiagramMergeViewer)fRight).getGraphicalViewer().flush();
				fAncestor.flush();
				((DiagramMergeViewer)fAncestor).getGraphicalViewer().flush();

				// Create phantoms -> cache them and their possible container
				Iterator<Diff> itDeleteDiffs = deleteDiffs.iterator();
				while (itDeleteDiffs.hasNext()) {

					DiagramDiff diff = (DiagramDiff)itDeleteDiffs.next();

					View viewRef = (View)diff.getView();
					IFigure ref = getReferenceFigureValue(input.getComparison(), diff);
					Diagram diagram = viewRef.getDiagram();

					MergeViewerSide targetSide = getTargetSide(input.getComparison(), viewRef);
					Diagram targetDiagram = (Diagram)getMatchView(input.getComparison(), diagram, targetSide);
					DiagramMergeViewer targetViewer = getViewer(targetSide);

					IFigure targetLayer = LayerManager.Helper.find(targetViewer.getEditPart(targetDiagram))
							.getLayer(LayerConstants.SCALABLE_LAYERS);

					IFigure ghost = createGhostFigure(ref, targetSide,
							viewRef.eContainer() instanceof BasicCompartment);
					phantomsMap.put(ref, new Phantom(ghost, targetLayer));

					View parentViewRef = (View)getMatchView(diff.getMatch(), getSide(input.getComparison(),
							viewRef));

					if (parentViewRef != null) {
						IFigure parentRefFigure = getFigure(input.getComparison(), parentViewRef);
						View parentTargetView = (View)getMatchView(diff.getMatch(), getSide(diff));
						if (parentTargetView != null) {
							IFigure parentTargetFigure = getFigure(input.getComparison(), parentTargetView);
							containerMap.put(parentRefFigure, parentTargetFigure);
						}
					}

				}

				// Iterate on cache to attach phantoms on the correct figures
				for (Entry<IFigure, Phantom> entry : phantomsMap.entrySet()) {
					IFigure ref = entry.getKey();

					Phantom target = entry.getValue();
					IFigure ghost = target.figure;
					IFigure targetLayer = target.layer;

					IFigure parentGhost = findMatchingParentGhost(ref);
					if (parentGhost != null) {
						addToParent(ref, ghost, parentGhost);
					} else {
						IFigure parent = findMatchingParent(ref);
						if (parent != null) {
							addToParent(ref, ghost, parent);
						} else {
							targetLayer.add(ghost);
						}
					}
				}
			}
			// *** END first solution ***

			// reset phantom selections
			resetPhantomSelections();

			// set phantom selections
			if (input instanceof IDiagramDiffAccessor) {
				IFigure figureRef = getReferenceFigure(input.getComparison(),
						(View)((IDiagramDiffAccessor)left).getDiff().getView());

				Phantom target = phantomsMap.get(figureRef);
				if (target != null) {

					int oldLineWidth = ((Shape)target.figure).getLineWidth();

					// save its line width
					phantomSelection.put(target.figure, oldLineWidth);

					// enhance its line width
					((Shape)target.figure).setLineWidth(oldLineWidth + 2);

				}
			}
		}

	}

	private void addToParent(IFigure ref, IFigure ghost, IFigure parent) {
		parent.add(ghost);
		Rectangle copyRef = ref.getBounds().getCopy();
		ref.getParent().translateToAbsolute(copyRef);
		ghost.setBounds(copyRef);
	}

	private void resetPhantomSelections() {
		for (Entry<IFigure, Integer> entry : phantomSelection.entrySet()) {
			IFigure fig = entry.getKey();
			int oldValue = entry.getValue();
			((Shape)fig).setLineWidth(oldValue);
		}
		phantomSelection.clear();
	}

	private IFigure getReferenceFigureValue(Comparison comparison, DiagramDiff diff) {
		View viewRef = (View)diff.getView();
		return getReferenceFigure(comparison, viewRef);
	}

	private MergeViewerSide getTargetSide(Comparison comparison, View viewRef) {
		Match matchViewRef = comparison.getMatch(viewRef);
		// looking for null side
		MergeViewerSide side = null;
		if (matchViewRef.getLeft() == null) {
			side = MergeViewerSide.LEFT;
		} else {
			side = MergeViewerSide.RIGHT;
		}

		return side;
	}

	private IFigure findMatchingParentGhost(IFigure figure) {
		IFigure parent = figure.getParent();
		if (parent != null) {
			Phantom parentTarget = phantomsMap.get(parent);
			if (parentTarget == null) {
				return findMatchingParentGhost(parent);
			} else {
				return parentTarget.figure;
			}
		}
		return parent;
	}

	private IFigure findMatchingParent(IFigure figure) {
		IFigure parent = figure.getParent();
		if (parent != null) {
			IFigure parentTarget = containerMap.get(parent);
			if (parentTarget == null) {
				return findMatchingParent(parent);
			} else {
				return parentTarget;
			}
		}
		return parent;
	}

	private DiagramMergeViewer getViewer(MergeViewerSide side) {
		switch (side) {
			case LEFT:
				return (DiagramMergeViewer)fLeft;
			case RIGHT:
				return (DiagramMergeViewer)fRight;
			case ANCESTOR:
				return (DiagramMergeViewer)fAncestor;
			default:
				return null;
		}
	}

	private EObject getMatchView(Comparison comparison, View view, MergeViewerSide side) {
		Match match = comparison.getMatch(view);
		return getMatchView(match, side);
	}

	private EObject getMatchView(Match match, MergeViewerSide side) {
		switch (side) {
			case LEFT:
				return match.getLeft();
			case RIGHT:
				return match.getRight();
			case ANCESTOR:
				return match.getOrigin();
			default:
				return null;
		}
	}

	private IFigure getReferenceFigure(Comparison comparison, View view) {
		MergeViewerSide originSide = null;
		if (getMatchView(comparison, view, MergeViewerSide.ANCESTOR) != null) {
			originSide = MergeViewerSide.ANCESTOR;
		} else {
			Match match = comparison.getMatch(view);
			if (match.getLeft() != null) {
				originSide = MergeViewerSide.LEFT;
			} else {
				originSide = MergeViewerSide.RIGHT;
			}
		}

		EObject origin = getMatchView(comparison, view, originSide);

		if (origin instanceof View) {

			return getFigure(comparison, (View)origin);
		}
		return null;
	}

	private IFigure getFigure(Comparison comparison, View view) {
		MergeViewerSide side = getSide(comparison, view);
		GraphicalEditPart originEditPart = (GraphicalEditPart)getViewer(side).getEditPart(view);
		return originEditPart.getFigure();
	}

	private IFigure createGhostFigure(IFigure ref, MergeViewerSide side, boolean isEltOfList) {

		Rectangle rect = ref.getBounds().getCopy();

		IFigure ghost = null;

		if (isEltOfList) {
			ghost = new Polyline();
			((Polyline)ghost).addPoint(new Point(rect.x, rect.y - 1));
			((Polyline)ghost).addPoint(new Point(rect.x + rect.width, rect.y - 1));
			ghost.setBackgroundColor(new Color(Display.getCurrent(), new RGB(255, 0, 0)));
			((Shape)ghost).setLineWidth(2);
		} else {
			ghost = new DeleteGhostImageFigure();
			ghost.setBounds(rect);
		}

		rect.performScale(((DiagramRootEditPart)getViewer(side).getGraphicalViewer().getRootEditPart())
				.getZoomManager().getZoom());

		ghost.setForegroundColor(new Color(Display.getCurrent(), new RGB(255, 0, 0)));
		((Shape)ghost).setAlpha(150);
		return ghost;
	}

	private MergeViewerSide getSide(Diff diff) {
		if (diff.getSource() == DifferenceSource.LEFT) {
			return MergeViewerSide.LEFT;
		} else {
			return MergeViewerSide.RIGHT;
		}

	}

	private MergeViewerSide getSide(Comparison comparison, View view) {
		Match match = comparison.getMatch(view);
		if (match.getLeft() == view) {
			return MergeViewerSide.LEFT;
		} else if (match.getRight() == view) {
			return MergeViewerSide.RIGHT;
		} else if (match.getOrigin() == view) {
			return MergeViewerSide.ANCESTOR;
		} else {
			return null;
		}
	}

	// *** BEGIN alternative developing solution ***
	final class PhantomsManager {

		/**  */
		private Map<IFigure, MatchFigure> relevantFigures = new HashMap<IFigure, MatchFigure>();

		private List<MatchFigure> matchedFigures = new ArrayList<MatchFigure>();

		private Map<IFigure, IFigure> phantoms = new HashMap<IFigure, IFigure>();

		public void initCacheFigures(Comparison comparison) {
			List<Match> matches = getAllMatches(comparison);
			for (Match match : matches) {
				createMatchFigure(match);
			}
			// Add glue to the figures
			for (MatchFigure matchFigure : matchedFigures) {
				createParentMatchFigures(matchFigure);
			}
		}

		public void initPhantoms(Comparison comparison, Collection<Diff> deleteDifferences) {
			Iterator<Diff> itDeleteDiffs = deleteDifferences.iterator();
			while (itDeleteDiffs.hasNext()) {

				DiagramDiff diff = (DiagramDiff)itDeleteDiffs.next();

				Map<MergeViewerSide, IFigure> figures = getFigures(comparison, diff);

				View refView = (View)diff.getView();
				Match match = comparison.getMatch(refView);

				MergeViewerSide refSide = getReferenceSide(match);
				IFigure refFigure = figures.get(refSide);

				MatchFigure matchFigure = new MatchFigure(figures.get(MergeViewerSide.ANCESTOR), figures
						.get(MergeViewerSide.LEFT), figures.get(MergeViewerSide.RIGHT));

				MergeViewerSide targetSide = getTargetSide(comparison, refView);

				IFigure ghost = createGhostFigure(refFigure, targetSide,
						refView.eContainer() instanceof BasicCompartment);
				matchFigure.setGhost(targetSide, ghost);

				relevantFigures.put(refFigure, matchFigure);
				relevantFigures.put(ghost, matchFigure);
				phantoms.put(refFigure, ghost);

			}
		}

		public void attachPhantoms() {
			for (Entry<IFigure, IFigure> entry : phantoms.entrySet()) {
				IFigure refFigure = entry.getKey();
				MatchFigure matchFigure = relevantFigures.get(refFigure);
				MatchFigure matchFigureParent = relevantFigures.get(refFigure.getParent());
				IFigure ghost = matchFigure.ghost;
				if (ghost != null) {
					IFigure targetParent = matchFigureParent.getFigure(matchFigure.getSide(ghost));

					// Retrieve the target layer. FIXME: retrieve the target diagram view to get its editpart.
					IFigure targetLayer = LayerManager.Helper.find(
							getViewer(matchFigure.getSide(ghost)).getEditPart(null)).getLayer(
							LayerConstants.SCALABLE_LAYERS);

					if (targetParent instanceof FreeformLayeredPane) {
						targetLayer.add(ghost);
					} else {
						targetParent.add(ghost);
					}

				}
			}
		}

		private MergeViewerSide getReferenceSide(Match match) {
			EObject origin = match.getOrigin();
			if (origin != null) {
				return MergeViewerSide.ANCESTOR;
			} else {
				EObject left = match.getLeft();
				EObject right = match.getRight();
				if (left != null) {
					return MergeViewerSide.LEFT;
				} else if (right != null) {
					return MergeViewerSide.RIGHT;
				}
			}
			return null;
		}

		private ViewSide getReferenceView(Match match) {
			switch (getReferenceSide(match)) {
				case ANCESTOR:
					return new ViewSide(MergeViewerSide.ANCESTOR, (View)match.getOrigin());
				case LEFT:
					return new ViewSide(MergeViewerSide.LEFT, (View)match.getLeft());
				case RIGHT:
					return new ViewSide(MergeViewerSide.RIGHT, (View)match.getRight());
			}
			return null;
		}

		private List<Match> getAllMatches(Comparison comparison) {
			List<Match> result = new ArrayList<Match>();
			for (Match match : comparison.getMatches()) {
				result.add(match);
				result.addAll(Lists.newArrayList(match.getAllSubmatches()));
			}
			return result;
		}

		private List<Match> getDeepestMatches(Comparison comparison) {
			List<Match> result = new ArrayList<Match>();
			for (Match match : comparison.getMatches()) {
				result.addAll(getDeepestMatches(match));
			}
			return result;
		}

		private List<Match> getDeepestMatches(Match match) {
			List<Match> result = new ArrayList<Match>();
			Iterator<Match> subMatches = match.getAllSubmatches().iterator();
			if (!subMatches.hasNext()) {
				result.add(match);
			} else {
				while (subMatches.hasNext()) {
					Match subMatch = subMatches.next();
					if (subMatch.getSubmatches().isEmpty()) {
						result.add(subMatch);
					}
				}
			}
			return result;
		}

		private Map<MergeViewerSide, IFigure> getFigures(Comparison comparison, DiagramDiff diff) {
			Map<MergeViewerSide, IFigure> result = new HashMap<MergeViewerSide, IFigure>();
			View view = (View)diff.getView();
			Match match = comparison.getMatch(view);
			View origin = (View)match.getOrigin();
			result.putAll(getFigure(comparison, origin));
			View left = (View)match.getLeft();
			result.putAll(getFigure(comparison, left));
			View right = (View)match.getRight();
			result.putAll(getFigure(comparison, right));
			return result;
		}

		private IFigure getFigure(ViewSide viewSide) {
			GraphicalEditPart originEditPart = (GraphicalEditPart)getViewer(viewSide.side).getEditPart(
					viewSide.view);
			return originEditPart.getFigure();
		}

		private Map<MergeViewerSide, IFigure> getFigure(Comparison comparison, View view) {
			Map<MergeViewerSide, IFigure> result = new HashMap<MergeViewerSide, IFigure>();
			if (view != null) {
				MergeViewerSide side = getSide(comparison, view);
				GraphicalEditPart editPart = (GraphicalEditPart)getViewer(side).getEditPart(view);
				result.put(side, editPart.getFigure());
			}
			return result;
		}

		private MatchFigure createMatchFigure(Match match) {

			IFigure originFigure = null;
			IFigure leftFigure = null;
			IFigure rightFigure = null;

			if (match.getOrigin() != null) {
				originFigure = getFigure(new ViewSide(MergeViewerSide.ANCESTOR, (View)match.getOrigin()));
			}
			if (match.getLeft() != null) {
				leftFigure = getFigure(new ViewSide(MergeViewerSide.LEFT, (View)match.getLeft()));

			}
			if (match.getRight() != null) {
				rightFigure = getFigure(new ViewSide(MergeViewerSide.RIGHT, (View)match.getRight()));
			}

			MatchFigure matchFigure = createMatchFigure(originFigure, leftFigure, rightFigure);
			matchedFigures.add(matchFigure);
			matchFigure.match = match;

			return matchFigure;
		}

		private MatchFigure createMatchFigure(IFigure origin, IFigure left, IFigure right) {
			MatchFigure matchFigure = new MatchFigure(origin, left, right);
			if (origin != null) {
				relevantFigures.put(origin, matchFigure);
			}
			if (left != null) {
				relevantFigures.put(left, matchFigure);
			}
			if (right != null) {
				relevantFigures.put(right, matchFigure);
			}
			return matchFigure;
		}

		private void createParentMatchFigures(MatchFigure matchFigure) {
			IFigure origin = matchFigure.origin;
			IFigure left = matchFigure.left;
			IFigure right = matchFigure.right;
			while (createParentMatchFigure(origin, left, right)) {
				if (origin != null) {
					origin = origin.getParent();
				}
				if (left != null) {
					left = left.getParent();
				}
				if (right != null) {
					right = right.getParent();
				}
			}
		}

		private boolean createParentMatchFigure(IFigure origin, IFigure left, IFigure right) {
			IFigure originParent = null;
			IFigure leftParent = null;
			IFigure rightParent = null;
			if (left != null) {
				leftParent = left.getParent();
				if (leftParent == null || relevantFigures.get(leftParent) != null) {
					return false;
				}
			}
			if (right != null) {
				rightParent = right.getParent();
				if (rightParent == null || relevantFigures.get(rightParent) != null) {
					return false;
				}
			}
			if (origin != null) {
				originParent = origin.getParent();
				if (originParent == null || relevantFigures.get(originParent) != null) {
					return false;
				}
			}
			createMatchFigure(originParent, leftParent, rightParent);
			return true;

		}

		private IFigure getFigure(MatchFigure matchFigure, MergeViewerSide side) {
			switch (side) {
				case LEFT:
					return matchFigure.left;
				case RIGHT:
					return matchFigure.right;
				case ANCESTOR:
					return matchFigure.origin;
				default:
					return null;
			}
		}

		final class ViewSide {
			public MergeViewerSide side;

			public View view;

			public ViewSide(MergeViewerSide side, View view) {
				this.side = side;
				this.view = view;
			}
		}

		final class MatchFigure {

			public IFigure origin;

			public IFigure left;

			public IFigure right;

			public Match match;

			public IFigure ghost;

			public MatchFigure(IFigure origin, IFigure left, IFigure right) {
				this.origin = origin;
				this.left = left;
				this.right = right;
			}

			public MatchFigure(IFigure origin, IFigure left, IFigure right, Match match) {
				this.origin = origin;
				this.left = left;
				this.right = right;
				this.match = match;
			}

			public void setGhost(MergeViewerSide side, IFigure ghost) {
				this.ghost = ghost;
				switch (side) {
					case ANCESTOR:
						origin = ghost;
						break;
					case LEFT:
						left = ghost;
						break;
					case RIGHT:
						right = ghost;
						break;
				}
			}

			public MergeViewerSide getSide(IFigure figure) {
				if (figure == origin) {
					return MergeViewerSide.ANCESTOR;
				} else if (figure == left) {
					return MergeViewerSide.LEFT;
				} else if (figure == right) {
					return MergeViewerSide.RIGHT;
				}
				return null;
			}

			public IFigure getFigure(MergeViewerSide side) {
				switch (side) {
					case ANCESTOR:
						return origin;
					case LEFT:
						return left;
					case RIGHT:
						return right;
				}
				return null;
			}

		}

	}

	// *** END alternative developing solution ***

}

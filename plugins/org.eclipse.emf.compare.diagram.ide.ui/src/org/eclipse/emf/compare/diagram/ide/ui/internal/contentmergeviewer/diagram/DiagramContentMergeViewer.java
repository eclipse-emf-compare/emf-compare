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

import com.google.common.collect.Collections2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.DiagramDiff;
import org.eclipse.emf.compare.diagram.ide.ui.DMergeViewer;
import org.eclipse.emf.compare.diagram.ide.ui.GraphicalMergeViewer;
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

	private Map<IFigure, Target> figures = new HashMap<IFigure, Target>();

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
		final DiagramMergeViewer mergeTreeViewer = new DiagramMergeViewer(parent, side,
				(DiagramContentMergeViewer)master);
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

	private class Target {
		public IFigure figure;

		public IFigure layer;

		public Target(IFigure figure, IFigure layer) {
			this.figure = figure;
			this.layer = layer;
		}
	}

	@Override
	protected void updateContent(Object ancestor, Object left, Object right) {
		// TODO Auto-generated method stub
		super.updateContent(ancestor, left, right);

		if (left instanceof IDiagramNodeAccessor) {
			IDiagramNodeAccessor input = (IDiagramNodeAccessor)left;
			Iterator<Diff> deleteDiffs = Collections2.filter(input.getAllDiffs(),
					and(ofKind(DifferenceKind.DELETE), instanceOf(DiagramDiff.class))).iterator();
			while (deleteDiffs.hasNext()) {
				DiagramDiff diff = (DiagramDiff)deleteDiffs.next();
				Match match = input.getComparison().getMatch(diff.getView());
				// looking for null side
				MergeViewerSide side = null;
				if (match.getLeft() == null) {
					side = MergeViewerSide.LEFT;
				} else {
					side = MergeViewerSide.RIGHT;
				}

				IFigure ref = getReference(((IDiagramNodeAccessor)left).getComparison(), (View)diff.getView());

				Diagram diagram = ((View)diff.getView()).getDiagram();
				Diagram targetDiagram = (Diagram)getMatchView(((IDiagramNodeAccessor)left).getComparison(),
						diagram, side);

				DiagramMergeViewer targetViewer = getViewer(side);

				targetViewer.getGraphicalViewer().flush();
				targetViewer.flush();

				IFigure targetLayer = LayerManager.Helper.find(targetViewer.getEditPart(targetDiagram))
						.getLayer(LayerConstants.FEEDBACK_LAYER);

				figures.put(ref, new Target(createGhost(ref), targetLayer));

			}

			for (Entry<IFigure, Target> entry : figures.entrySet()) {
				IFigure ref = entry.getKey();

				Target target = entry.getValue();
				IFigure ghost = target.figure;
				IFigure targetLayer = target.layer;

				IFigure parentGhost = findMatchingParent(ref);
				if (parentGhost != null) {
					parentGhost.add(ghost);
					// ghost.setParent(parentGhost);
				} else {
					targetLayer.add(ghost);
					// ghost.setParent(targetLayer);
				}

			}

			// getAncestorMergeViewer().flush();
			// getLeftMergeViewer().flush();
			// getRightMergeViewer().flush();
			// ((DiagramMergeViewer)getAncestorMergeViewer()).getGraphicalViewer().flush();
			// ((DiagramMergeViewer)getLeftMergeViewer()).getGraphicalViewer().flush();
			// ((DiagramMergeViewer)getRightMergeViewer()).getGraphicalViewer().flush();

			figures.clear();

		}

	}

	private IFigure findMatchingParent(IFigure figure) {
		IFigure parent = figure.getParent();
		if (parent != null) {
			Target parentTarget = figures.get(parent);
			if (parentTarget == null) {
				return findMatchingParent(parent);
			} else {
				return parentTarget.figure;
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

	private IFigure getReference(Comparison comparison, View view) {
		MergeViewerSide originSide = null;
		if (getMatchView(comparison, view, MergeViewerSide.ANCESTOR) != null) {
			originSide = MergeViewerSide.ANCESTOR;
		} else {
			originSide = MergeViewerSide.LEFT;
		}

		EObject origin = getMatchView(comparison, view, originSide);

		if (origin instanceof View) {

			GraphicalEditPart originEditPart = (GraphicalEditPart)getViewer(originSide).getEditPart(
					((View)origin));

			getViewer(originSide).flush();
			getViewer(originSide).getGraphicalViewer().flush();

			return originEditPart.getFigure();
		}
		return null;
	}

	private IFigure createGhost(IFigure ref) {
		IFigure ghost = new DeleteGhostImageFigure();

		Rectangle rect = ref.getBounds().getCopy();

		// rect.performScale(((DiagramRootEditPart)getViewer(side).getGraphicalViewer()
		// .getRootEditPart()).getZoomManager().getZoom());
		ghost.setBounds(rect);
		// ghost.setBackgroundColor(new Color(Display.getCurrent(), new RGB(255, 255, 255)));
		ghost.setForegroundColor(new Color(Display.getCurrent(), new RGB(255, 0, 0)));
		((DeleteGhostImageFigure)ghost).setAlpha(150);
		return ghost;
	}

}

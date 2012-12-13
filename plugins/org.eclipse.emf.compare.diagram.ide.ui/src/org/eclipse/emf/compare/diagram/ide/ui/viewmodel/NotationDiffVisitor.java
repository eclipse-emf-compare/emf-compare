/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Obeo - rework on generic gmf comparison
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.viewmodel;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.EdgeChange;
import org.eclipse.emf.compare.diagram.Hide;
import org.eclipse.emf.compare.diagram.LabelChange;
import org.eclipse.emf.compare.diagram.NodeChange;
import org.eclipse.emf.compare.diagram.Show;
import org.eclipse.emf.compare.diagram.ide.ui.GMFCompareUIPlugin;
import org.eclipse.emf.compare.diagram.ide.ui.decoration.provider.DiffDecoratorProvider;
import org.eclipse.emf.compare.diagram.util.DiagramCompareSwitch;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The Diff visitor for annotate the diagrams to be used in comparison viewer. The annotation added are then
 * used to decorates the elements.
 * 
 * @see DiffDecoratorProvider
 * @author <a href="mailto:stephane.bouchet@obeo.fr">Stephane Bouchet</a>
 */
public class NotationDiffVisitor {

	/**
	 * Inner class for visit dagramDiffs.
	 * 
	 * @author <a href="mailto:stephane.bouchet@obeo.fr">Stephane Bouchet</a>
	 */
	private class DiagramdiffSwitchVisitor extends DiagramCompareSwitch<IStatus> {

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.util.DiagramdiffSwitch#caseDiagramLabelChange(org.eclipse.emf.compare.diagram.diagramdiff.DiagramLabelChange)
		 */
		@Override
		public IStatus caseLabelChange(LabelChange diff) {
			boolean result = true;
			final Match match = diff.getMatch();
			final EObject oneSide = MatchUtil.getContainer(match.getComparison(), diff);
			final EObject otherSide = MatchUtil.getOriginContainer(match.getComparison(), diff);
			result &= annotateNotation((View)oneSide, DiffDecoratorProvider.DIFF_LABEL_MODIFIED);
			result &= annotateNotation((View)otherSide, DiffDecoratorProvider.DIFF_LABEL_MODIFIED);
			return checkResult(result);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.util.DiagramdiffSwitch#caseDiagramShowElement(org.eclipse.emf.compare.diagram.diagramdiff.DiagramShowElement)
		 */
		@Override
		public IStatus caseShow(Show diff) {
			boolean result = true;
			final Match match = diff.getMatch();
			final EObject oneSide = MatchUtil.getContainer(match.getComparison(), diff);
			final EObject otherSide = MatchUtil.getOriginContainer(match.getComparison(), diff);
			result &= annotateNotation((View)oneSide, DiffDecoratorProvider.DIFF_SHOWED);
			result &= annotateNotation((View)otherSide, DiffDecoratorProvider.DIFF_SHOWED);
			return checkResult(result);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.util.DiagramdiffSwitch#caseDiagramHideElement(org.eclipse.emf.compare.diagram.diagramdiff.DiagramHideElement)
		 */
		@Override
		public IStatus caseHide(Hide diff) {
			final Match match = diff.getMatch();
			final EObject oneSide = MatchUtil.getContainer(match.getComparison(), diff);
			boolean result = annotateNotation((View)oneSide, DiffDecoratorProvider.DIFF_HIDED);
			return checkResult(result);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.util.DiagramdiffSwitch#caseDiagramMoveNode(org.eclipse.emf.compare.diagram.diagramdiff.DiagramMoveNode)
		 */
		@Override
		public IStatus caseNodeChange(NodeChange diff) {
			boolean result = true;
			if (diff.getKind() == DifferenceKind.MOVE) {
				final Match match = diff.getMatch();
				final EObject oneSide = MatchUtil.getContainer(match.getComparison(), diff);
				final EObject otherSide = MatchUtil.getOriginContainer(match.getComparison(), diff);
				result &= annotateNotation((View)oneSide, DiffDecoratorProvider.DIFF_MOVED);
				result &= annotateNotation((View)otherSide, DiffDecoratorProvider.DIFF_MOVED);
			} else if (diff.getKind() == DifferenceKind.ADD) {
				final Match match = diff.getMatch();
				final EObject oneSide = MatchUtil.getContainer(match.getComparison(), diff);
				result &= annotateNotation((View)oneSide, DiffDecoratorProvider.DIFF_ADDED);
			} else if (diff.getKind() == DifferenceKind.DELETE) {
				final Match match = diff.getMatch();
				final EObject oneSide = MatchUtil.getContainer(match.getComparison(), diff);
				result &= annotateNotation((View)oneSide, DiffDecoratorProvider.DIFF_REMOVED);
			}
			return checkResult(result);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.util.DiagramdiffSwitch#caseDiagramEdgeChange(org.eclipse.emf.compare.diagram.diagramdiff.DiagramEdgeChange)
		 */
		@Override
		public IStatus caseEdgeChange(EdgeChange diff) {
			boolean result = true;
			if (diff.getKind() == DifferenceKind.MOVE) {
				final Match match = diff.getMatch();
				final EObject oneSide = MatchUtil.getContainer(match.getComparison(), diff);
				final EObject otherSide = MatchUtil.getOriginContainer(match.getComparison(), diff);
				result &= annotateNotation((View)oneSide, DiffDecoratorProvider.DIFF_MOVED);
				result &= annotateNotation((View)otherSide, DiffDecoratorProvider.DIFF_MOVED);
			}
			return checkResult(result);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.util.DiagramdiffSwitch#defaultCase(org.eclipse.emf.ecore.EObject)
		 */
		@Override
		public IStatus defaultCase(EObject diff) {
			// we don't care about generic diffs
			return Status.OK_STATUS;
		}
	}

	/** the diagram diff visitor. */
	public DiagramdiffSwitchVisitor diagramdiffSwitch = new DiagramdiffSwitchVisitor();

	/** used to add or remove eannotation diffs when visiting. */
	private boolean annote;

	/**
	 * The side on which to apply the annotation.
	 */
	private DifferenceSource matchSide;

	/**
	 * Returns the annote.
	 * 
	 * @return The annote.
	 */
	public boolean isAnnote() {
		return annote;
	}

	public DifferenceSource getMatchSide() {
		return matchSide;
	}

	/**
	 * This is the core of the GMF diff. Annotates the left and right diagram.
	 * 
	 * @param diffs
	 *            the list of Diff Elements
	 * @param side
	 *            the side to annotate.
	 */
	public void addEannotations(List<Diff> diffs, DifferenceSource side) {
		annote = true;
		matchSide = side;
		doVisit(diffs);
	}

	/**
	 * Used to remove diffs annotations.
	 * 
	 * @param diffs
	 *            the list of Diff Elements
	 * @param side
	 *            the side to annotate.
	 */
	public void removeEAnnotations(List<Diff> diffs, DifferenceSource side) {
		annote = false;
		matchSide = side;
		doVisit(diffs);
	}

	/**
	 * Main visitor logic.
	 * 
	 * @param diffs
	 *            the list of Diff Elements
	 */
	private void doVisit(List<Diff> diffs) {
		// visit diff model
		// FIXME externalize this
		final MultiStatus status = new MultiStatus(GMFCompareUIPlugin.PLUGIN_ID, IStatus.WARNING,
				"Diff visitor failed.", null); //$NON-NLS-1$
		for (Diff diffElement : diffs) {
			status.merge(diagramdiffSwitch.doSwitch(diffElement));
		}
		// if (!status.isOK()) {
		// GMFComparePlugin.log(status.getMessage(), false);
		// }
	}

	/**
	 * Set the given annotation to the given notation element.
	 * 
	 * @param element
	 *            the notation element to annotate
	 * @param annotation
	 *            the diff annotation
	 * @return true if annotation has been added to the view
	 */
	protected boolean annotateNotation(View element, String annotation) {
		boolean result = false;
		if (isAnnote()) {
			EAnnotation diffAnnotation = null;
			if (element.getEAnnotation(DiffDecoratorProvider.DIFF) == null) {
				diffAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
				diffAnnotation.setSource(DiffDecoratorProvider.DIFF);
				result = element.getEAnnotations().add(diffAnnotation);
			} else {
				diffAnnotation = element.getEAnnotation(DiffDecoratorProvider.DIFF);
			}
			// FIXME should this string be externalized?
			diffAnnotation.getDetails().put(annotation, "diffDetail"); //$NON-NLS-1$
			result = true;
		} else {
			if (element.getEAnnotation(DiffDecoratorProvider.DIFF) != null) {
				final EAnnotation diffAnnotation = element.getEAnnotation(DiffDecoratorProvider.DIFF);
				result = element.getEAnnotations().remove(diffAnnotation);
			}
		}
		return result;
	}

	// /**
	// * Utility method to check if the diff is hidden by another diff.
	// *
	// * @param diff
	// * the diff element to check
	// * @return true if the diff is hidden by another diff.
	// */
	// protected boolean isHiddenDiff(Diff diff) {
	// return !diff.getIsHiddenBy().isEmpty();
	// }

	/**
	 * Utility method to transform a boolean result into status.
	 * 
	 * @param ok
	 *            the boolean state
	 * @return a status corresponding to the state of the boolean
	 */
	protected IStatus checkResult(boolean ok) {
		if (ok) {
			return Status.OK_STATUS;
		}
		return Status.CANCEL_STATUS;
	}

}

/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Obeo - rework on generic gmf comparison
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ui.viewmodel;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diagram.diagramdiff.BusinessDiagramShowHideElement;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramEdgeChange;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramHideElement;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramLabelChange;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChangeRightTarget;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramMoveNode;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramShowElement;
import org.eclipse.emf.compare.diagram.diagramdiff.util.DiagramdiffSwitch;
import org.eclipse.emf.compare.diagram.ui.GMFComparePlugin;
import org.eclipse.emf.compare.diagram.ui.decoration.provider.DiffDecoratorProvider;
import org.eclipse.emf.compare.diff.engine.IMatchManager.MatchSide;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
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
	private class DiagramdiffSwitchVisitor extends DiagramdiffSwitch<IStatus> {

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.util.DiagramdiffSwitch#caseDiagramLabelChange(org.eclipse.emf.compare.diagram.diagramdiff.DiagramLabelChange)
		 */
		@Override
		public IStatus caseDiagramLabelChange(DiagramLabelChange diff) {
			boolean result = true;
			if (!isHiddenDiff(diff) && GMFComparePlugin.isValid(diff.getLeftElement())
					&& GMFComparePlugin.isValid(diff.getRightElement())) {
				if (getMatchSide() == MatchSide.LEFT) {
					result &= annotateNotation((View)diff.getLeftElement(),
							DiffDecoratorProvider.DIFF_LABEL_MODIFIED);
				} else {
					result &= annotateNotation((View)diff.getRightElement(),
							DiffDecoratorProvider.DIFF_LABEL_MODIFIED);
				}
			}
			return checkResult(result);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.util.DiagramdiffSwitch#caseDiagramShowElement(org.eclipse.emf.compare.diagram.diagramdiff.DiagramShowElement)
		 */
		@Override
		public IStatus caseDiagramShowElement(DiagramShowElement diff) {
			boolean result = true;
			if (!isHiddenDiff(diff)) {
				if (diff instanceof BusinessDiagramShowHideElement) {
					if (diff.isRemote()) {
						if (getMatchSide() == MatchSide.RIGHT) {
							result = annotateNotation(((BusinessDiagramShowHideElement)diff).getRightView(),
									DiffDecoratorProvider.DIFF_SHOWED);
						}
					} else {
						if (getMatchSide() == MatchSide.LEFT) {
							result = annotateNotation(((BusinessDiagramShowHideElement)diff).getLeftView(),
									DiffDecoratorProvider.DIFF_SHOWED);
						}
					}
				}
			}
			return checkResult(result);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.util.DiagramdiffSwitch#caseDiagramHideElement(org.eclipse.emf.compare.diagram.diagramdiff.DiagramHideElement)
		 */
		@Override
		public IStatus caseDiagramHideElement(DiagramHideElement diff) {
			boolean result = true;
			if (!isHiddenDiff(diff)) {
				if (diff instanceof BusinessDiagramShowHideElement) {
					if (diff.isRemote()) {
						if (getMatchSide() == MatchSide.LEFT) {
							result = annotateNotation(((BusinessDiagramShowHideElement)diff).getLeftView(),
									DiffDecoratorProvider.DIFF_HIDED);
						}
					} else {
						if (getMatchSide() == MatchSide.RIGHT) {
							result = annotateNotation(((BusinessDiagramShowHideElement)diff).getRightView(),
									DiffDecoratorProvider.DIFF_HIDED);
						}
					}
				}
			}
			return checkResult(result);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.util.DiagramdiffSwitch#caseDiagramMoveNode(org.eclipse.emf.compare.diagram.diagramdiff.DiagramMoveNode)
		 */
		@Override
		public IStatus caseDiagramMoveNode(DiagramMoveNode diff) {
			boolean result = true;
			if (!isHiddenDiff(diff) && GMFComparePlugin.isValid(diff.getLeftElement())
					&& GMFComparePlugin.isValid(diff.getRightElement())) {
				if (getMatchSide() == MatchSide.LEFT) {
					result &= annotateNotation((View)diff.getLeftElement(), DiffDecoratorProvider.DIFF_MOVED);
				} else {
					result &= annotateNotation((View)diff.getRightElement(), DiffDecoratorProvider.DIFF_MOVED);
				}
			}
			return checkResult(result);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.util.DiagramdiffSwitch#caseDiagramEdgeChange(org.eclipse.emf.compare.diagram.diagramdiff.DiagramEdgeChange)
		 */
		@Override
		public IStatus caseDiagramEdgeChange(DiagramEdgeChange diff) {
			boolean result = true;
			if (!isHiddenDiff(diff) && GMFComparePlugin.isValid(diff.getLeftElement())
					&& GMFComparePlugin.isValid(diff.getRightElement())) {
				if (getMatchSide() == MatchSide.LEFT) {
					result &= annotateNotation((View)diff.getLeftElement(), DiffDecoratorProvider.DIFF_MOVED);
				} else {
					result &= annotateNotation((View)diff.getRightElement(), DiffDecoratorProvider.DIFF_MOVED);
				}
			}
			return checkResult(result);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.util.DiagramdiffSwitch#caseDiagramModelElementChangeLeftTarget(org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChangeLeftTarget)
		 */
		@Override
		public IStatus caseDiagramModelElementChangeLeftTarget(DiagramModelElementChangeLeftTarget diff) {
			boolean result = true;
			if (!isHiddenDiff(diff) && GMFComparePlugin.isValid(diff.getLeftElement())) {
				if (getMatchSide() == MatchSide.LEFT) {
					result = annotateNotation((View)diff.getLeftElement(), DiffDecoratorProvider.DIFF_ADDED);
				}
			}
			return checkResult(result);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.diagram.diagramdiff.util.DiagramdiffSwitch#caseDiagramModelElementChangeRightTarget(org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChangeRightTarget)
		 */
		@Override
		public IStatus caseDiagramModelElementChangeRightTarget(DiagramModelElementChangeRightTarget diff) {
			boolean result = true;
			if (!isHiddenDiff(diff) && GMFComparePlugin.isValid(diff.getRightElement())) {
				if (getMatchSide() == MatchSide.RIGHT) {
					result = annotateNotation((View)diff.getRightElement(),
							DiffDecoratorProvider.DIFF_REMOVED);
				}
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
	private DiagramdiffSwitchVisitor diagramdiffSwitch = new DiagramdiffSwitchVisitor();

	/** used to add or remove eannotation diffs when visiting. */
	private boolean annote;

	/**
	 * The side on which to apply the annotation.
	 */
	private MatchSide matchSide;

	/**
	 * Returns the annote.
	 * 
	 * @return The annote.
	 */
	public boolean isAnnote() {
		return annote;
	}

	public MatchSide getMatchSide() {
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
	public void addEannotations(List<DiffElement> diffs, MatchSide side) {
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
	public void removeEAnnotations(List<DiffElement> diffs, MatchSide side) {
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
	private void doVisit(List<DiffElement> diffs) {
		// visit diff model
		// FIXME externalize this
		final MultiStatus status = new MultiStatus(GMFComparePlugin.PLUGIN_ID, IStatus.WARNING,
				"Diff visitor failed.", null); //$NON-NLS-1$
		for (DiffElement diffElement : diffs) {
			status.merge(diagramdiffSwitch.doSwitch(diffElement));
		}
		if (!status.isOK()) {
			EMFComparePlugin.log(status.getMessage(), false);
		}
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

	/**
	 * Utility method to check if the diff is hidden by another diff.
	 * 
	 * @param diff
	 *            the diff element to check
	 * @return true if the diff is hidden by another diff.
	 */
	protected boolean isHiddenDiff(DiffElement diff) {
		return !diff.getIsHiddenBy().isEmpty();
	}

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

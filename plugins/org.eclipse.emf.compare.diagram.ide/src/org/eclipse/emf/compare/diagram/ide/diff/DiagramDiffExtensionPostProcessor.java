package org.eclipse.emf.compare.diagram.ide.diff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.DiagramCompareFactory;
import org.eclipse.emf.compare.diagram.GMFCompare;
import org.eclipse.emf.compare.diagram.LabelChange;
import org.eclipse.emf.compare.diagram.diff.util.DiffUtil;
import org.eclipse.emf.compare.diagram.ide.diff.internal.extension.IDiffExtensionFactory;
import org.eclipse.emf.compare.diagram.provider.IViewLabelProvider;
import org.eclipse.emf.compare.diagram.provider.ViewLabelProviderExtensionRegistry;
import org.eclipse.emf.compare.extension.EMFCompareExtensionRegistry;
import org.eclipse.emf.compare.extension.IPostProcessor;
import org.eclipse.emf.compare.extension.PostProcessorDescriptor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

public class DiagramDiffExtensionPostProcessor implements IPostProcessor {

	private Set<IDiffExtensionFactory> diagramExtensionFactories;

	public DiagramDiffExtensionPostProcessor() {
		// TODO Auto-generated constructor stub
	}

	public void postMatch(Comparison comparison) {
		// TODO Auto-generated method stub

	}

	public void postDiff(Comparison comparison) {
		// TODO Auto-generated method stub

	}

	public void postRequirements(Comparison comparison) {
		// computeLabels
		computeLabels(comparison);
	}

	public void postEquivalences(Comparison comparison) {
		// TODO Auto-generated method stub

	}

	public void postConflicts(Comparison comparison) {
		// TODO Auto-generated method stub

	}

	private void computeLabels(Comparison comparison) {
		final Iterator<Match> matches = getAllMatches(comparison).iterator();
		while (matches.hasNext()) {
			final Match match = (Match)matches.next();

			LabelChange diff = null;
			final EObject leftElement = match.getLeft();
			final EObject rightElement = match.getRight();

			if (leftElement instanceof View) {
				final View view = (View)leftElement;
				final Diagram diagram = view.getDiagram();

				if (diagram != null) {
					final String diagramType = diagram.getType();

					IViewLabelProvider extensionForType = ViewLabelProviderExtensionRegistry.getExtensionForType(diagramType);

					if (extensionForType == null) { // no extension registered for handling label in this
													// diagram,
						// use the default one
						GMFCompare.getDefault().getLog().log(
								new Status(IStatus.INFO, GMFCompare.PLUGIN_ID,
										"No IViewLabelProvider registered for diagram " + diagramType)); //$NON-NLS-1$
						extensionForType = IViewLabelProvider.DEFAULT_INSTANCE;
					}
					if (extensionForType.isManaged(view) && DiffUtil.isVisible(view)
							&& DiffUtil.isVisible((View)rightElement)) {
						final String leftLabel = extensionForType.elementLabel(view);
						final String rightLabel = extensionForType.elementLabel((View)rightElement);
						if (!leftLabel.equals(rightLabel)) {
							diff = DiagramCompareFactory.eINSTANCE.createLabelChange();
							diff.setKind(DifferenceKind.CHANGE);
							diff.setLeft(leftLabel);
							diff.setRight(rightLabel);
							match.getDifferences().add(diff);
						}
					}
				}

				if (comparison.isThreeWay() && diff != null) {
					final EObject ancestor = match.getOrigin();
					if (ancestor instanceof View) {
						final ITextAwareEditPart ancestorEp = DiffUtil.getTextEditPart((View)ancestor);
						if (ancestorEp != null) {
							final String ancestorLabel = ancestorEp.getEditText();
							final String leftLabel = diff.getLeft();
							final String rightLabel = diff.getRight();
							if (ancestorLabel.equals(leftLabel)) {
								diff.setSource(DifferenceSource.RIGHT);
							} else if (!ancestorLabel.equals(rightLabel)) {
								final Conflict conflict = CompareFactory.eINSTANCE.createConflict();
								conflict.getDifferences().add(diff);
								comparison.getConflicts().add(conflict);
							}
						}

					}
				}

			}
		}
	}

	private List<Match> getAllMatches(Comparison comparison) {
		final List<Match> result = new ArrayList<Match>();
		final Iterator<Match> matches = comparison.getMatches().iterator();
		while (matches.hasNext()) {
			final Match match = (Match)matches.next();
			result.add(match);
			final Iterator<Match> subMatches = match.getAllSubmatches().iterator();
			while (subMatches.hasNext()) {
				Match match2 = (Match)subMatches.next();
				result.add(match2);
			}
		}
		return result;
	}

}

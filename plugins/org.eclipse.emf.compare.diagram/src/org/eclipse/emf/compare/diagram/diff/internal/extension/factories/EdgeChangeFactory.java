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
package org.eclipse.emf.compare.diagram.diff.internal.extension.factories;

import static com.google.common.collect.Iterables.filter;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diagram.DiagramCompareFactory;
import org.eclipse.emf.compare.diagram.DiagramDiff;
import org.eclipse.emf.compare.diagram.EdgeChange;
import org.eclipse.emf.compare.diagram.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.IdentityAnchor;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Factory of edge changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class EdgeChangeFactory extends AbstractDiffExtensionFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diff.internal.extension.AbstractDiffExtensionFactory#getExtensionKind()
	 */
	@Override
	public Class<? extends Diff> getExtensionKind() {
		return EdgeChange.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diff.internal.extension.IDiffExtensionFactory#create(org.eclipse.emf.compare.Diff)
	 */
	public Diff create(Diff input) {
		final EdgeChange ret = DiagramCompareFactory.eINSTANCE.createEdgeChange();

		final DifferenceKind extensionKind = getRelatedExtensionKind(input);

		ret.setKind(extensionKind);

		// refined by
		if (extensionKind == DifferenceKind.MOVE) {
			ret.getRefinedBy().addAll(getAllDifferencesForMove(input));
		} else if (extensionKind == DifferenceKind.ADD) {
			ret.getRefinedBy().add(input);
			ret.getRefinedBy().addAll(getAllContainedDifferences((ReferenceChange)input));
		} else if (extensionKind == DifferenceKind.DELETE) {
			ret.getRefinedBy().add(input);
		}

		EObject view = null;
		if (input instanceof ReferenceChange) {
			view = ((ReferenceChange)input).getValue();
		} else if (input instanceof AttributeChange) {
			view = MatchUtil.getContainer(input.getMatch().getComparison(), input);
		}
		while (view != null && !(view instanceof Edge)) {
			view = view.eContainer();
		}
		ret.setView(view);

		ret.setSource(input.getSource());
		ret.setSemanticDiff(getSemanticDiff(input));

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diff.internal.extension.AbstractDiffExtensionFactory#fillRequiredDifferences(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.compare.Diff)
	 */
	@Override
	public void fillRequiredDifferences(Comparison comparison, Diff extension) {
		final DiagramDiff diff = (DiagramDiff)extension;
		final Diff semanticDiff = diff.getSemanticDiff();

		final View view = (View)diff.getView();
		final Match viewMatch = comparison.getMatch(view);
		if (view instanceof Edge) {
			final List<Diff> sourceCandidates = comparison.getDifferences(((Edge)view).getSource());
			for (Diff sourceDiff : sourceCandidates) {
				if (sourceDiff instanceof ReferenceChange
						&& ((ReferenceChange)sourceDiff).getReference() == NotationPackage.Literals.EDGE__SOURCE
						&& ((ReferenceChange)sourceDiff).getMatch() == viewMatch) {
					// This is the diff for the source reference of the underlying edge.
					// Check its requires/requiredBy.
					for (Diff sourceRequired : sourceDiff.getRequires()) {
						final List<Diff> candidates = comparison.getDifferences(sourceRequired);
						for (DiagramDiff diagramDiff : filter(candidates, DiagramDiff.class)) {
							if (diff != diagramDiff) {
								diff.getRequires().add(diagramDiff);
							}
						}
					}
					for (Diff sourceRequiredBy : sourceDiff.getRequiredBy()) {
						final List<Diff> candidates = comparison.getDifferences(sourceRequiredBy);
						for (DiagramDiff diagramDiff : filter(candidates, DiagramDiff.class)) {
							if (diff != diagramDiff) {
								diff.getRequiredBy().add(diagramDiff);
							}
						}
					}
				}
			}
			final List<Diff> targetCandidates = comparison.getDifferences(((Edge)view).getTarget());
			for (Diff targetDiff : targetCandidates) {
				if (targetDiff instanceof ReferenceChange
						&& ((ReferenceChange)targetDiff).getReference() == NotationPackage.Literals.EDGE__TARGET
						&& ((ReferenceChange)targetDiff).getMatch() == viewMatch) {
					// This is the diff for the target reference of the underlying edge.
					// Check its requires/requiredBy.
					for (Diff targetRequired : targetDiff.getRequires()) {
						final List<Diff> candidates = comparison.getDifferences(targetRequired);
						for (DiagramDiff diagramDiff : filter(candidates, DiagramDiff.class)) {
							if (diff != diagramDiff) {
								diff.getRequires().add(diagramDiff);
							}
						}
					}
					for (Diff targetRequiredBy : targetDiff.getRequiredBy()) {
						final List<Diff> candidates = comparison.getDifferences(targetRequiredBy);
						for (DiagramDiff diagramDiff : filter(candidates, DiagramDiff.class)) {
							if (diff != diagramDiff) {
								diff.getRequiredBy().add(diagramDiff);
							}
						}
					}
				}
			}
		}

		if (semanticDiff == null) {
			// no requires here
			return;
		}

		for (Diff semanticRequired : semanticDiff.getRequires()) {
			final List<Diff> candidates = comparison.getDifferences(semanticRequired);
			for (DiagramDiff diagramDiff : filter(candidates, DiagramDiff.class)) {
				if (diagramDiff.getSemanticDiff() == semanticRequired) {
					diff.getRequires().add(diagramDiff);
				}
			}
		}
		for (Diff semanticRequiredBy : semanticDiff.getRequiredBy()) {
			final List<Diff> candidates = comparison.getDifferences(semanticRequiredBy);
			for (DiagramDiff diagramDiff : filter(candidates, DiagramDiff.class)) {
				if (diagramDiff.getSemanticDiff() == semanticRequiredBy) {
					diff.getRequiredBy().add(diagramDiff);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diff.internal.extension.AbstractDiffExtensionFactory#getParentMatch(org.eclipse.emf.compare.Diff)
	 */
	@Override
	public Match getParentMatch(Diff input) {
		return input.getMatch().getComparison().getMatch(getViewContainer(input));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diff.internal.extension.AbstractDiffExtensionFactory#isRelatedToAnExtensionAdd(org.eclipse.emf.compare.ReferenceChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		return input.getValue() instanceof Edge && input.getReference().isContainment()
				&& input.getKind() == DifferenceKind.ADD;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diff.internal.extension.AbstractDiffExtensionFactory#isRelatedToAnExtensionDelete(org.eclipse.emf.compare.ReferenceChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionDelete(ReferenceChange input) {
		return input.getValue() instanceof Edge && input.getReference().isContainment()
				&& input.getKind() == DifferenceKind.DELETE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diff.internal.extension.AbstractDiffExtensionFactory#isRelatedToAnExtensionMove(org.eclipse.emf.compare.AttributeChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionMove(AttributeChange input) {
		return input.getAttribute().eContainer().equals(NotationPackage.eINSTANCE.getRelativeBendpoints())
				|| input.getAttribute().equals(NotationPackage.eINSTANCE.getIdentityAnchor_Id())
				&& input.getRefines().isEmpty();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.diff.internal.extension.AbstractDiffExtensionFactory#isRelatedToAnExtensionMove(org.eclipse.emf.compare.ReferenceChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionMove(ReferenceChange input) {
		return input.getValue() instanceof IdentityAnchor && input.getReference().isContainment()
				&& input.getRefines().isEmpty();
	}

	/**
	 * Get all differences which are part of a move extension, from the given difference (being part of the
	 * result).
	 * 
	 * @param input
	 *            The given difference.
	 * @return The found differences.
	 */
	private Set<Diff> getAllDifferencesForMove(Diff input) {
		// FIXME we're not taking enough diffs into account here
		final Comparison comparison = input.getMatch().getComparison();
		final Match match = input.getMatch();
		final Set<Diff> result = getAllNonExtendedDifferences(comparison, match);

		return result;
	}

	/**
	 * Get all differences which are part of a move extension, from the given match.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @param match
	 *            The match.
	 * @return The found differences.
	 */
	private Set<Diff> getAllNonExtendedDifferences(Comparison comparison, Match match) {
		final Set<Diff> result = Sets.newLinkedHashSet();

		final Set<Match> prune = Sets.newLinkedHashSet();
		for (Diff candidate : match.getDifferences()) {
			if (getRelatedExtensionKind(candidate) != null) {
				// It exists an extension for this candidate
				result.add(candidate);
			} else if (candidate instanceof ReferenceChange
					&& ((ReferenceChange)candidate).getReference().isContainment()) {
				prune.add(comparison.getMatch(((ReferenceChange)candidate).getValue()));
			}
		}

		for (Match submatch : match.getSubmatches()) {
			if (!prune.contains(submatch)) {
				result.addAll(getAllNonExtendedDifferences(comparison, submatch));
			}
		}

		return result;
	}

	/**
	 * From a given diagram difference extension, get the related semantic difference.
	 * 
	 * @param input
	 *            The diagram difference extension.
	 * @return The semantic one.
	 */
	private Diff getSemanticDiff(Diff input) {
		if (input instanceof ReferenceChange && ((ReferenceChange)input).getValue() instanceof View) {
			final View view = (View)((ReferenceChange)input).getValue();
			final Object element = ReferenceUtil.safeEGet(view, NotationPackage.Literals.VIEW__ELEMENT);
			if (element instanceof EObject) {
				final List<Diff> diffs = findCrossReferences(input.getMatch().getComparison(),
						(EObject)element, new Predicate<Diff>() {
							public boolean apply(Diff diff) {
								return diff instanceof ReferenceChange
										&& ((ReferenceChange)diff).getReference().isContainment();
							}
						});
				if (diffs.size() > 0) {
					return diffs.get(0);
				}
			}
		}
		return null;
	}
}

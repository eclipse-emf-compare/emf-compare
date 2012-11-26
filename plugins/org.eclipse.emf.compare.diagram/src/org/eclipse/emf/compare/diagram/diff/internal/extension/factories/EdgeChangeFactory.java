/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
 * Factory for UMLAssociationChangeLeftTarget.
 */
public class EdgeChangeFactory extends AbstractDiffExtensionFactory {

	@Override
	public Class<? extends Diff> getExtensionKind() {
		return EdgeChange.class;
	}

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

	@Override
	public void fillRequiredDifferences(Comparison comparison, Diff extension) {
		final DiagramDiff diff = (DiagramDiff)extension;
		final Diff semanticDiff = diff.getSemanticDiff();

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

	@Override
	public Match getParentMatch(Diff input) {
		return input.getMatch().getComparison().getMatch(getViewContainer(input));
	}

	@Override
	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		return input.getValue() instanceof Edge && input.getReference().isContainment()
				&& input.getKind() == DifferenceKind.ADD;
	}

	@Override
	protected boolean isRelatedToAnExtensionDelete(ReferenceChange input) {
		return input.getValue() instanceof Edge && input.getReference().isContainment()
				&& input.getKind() == DifferenceKind.DELETE;
	}

	@Override
	protected boolean isRelatedToAnExtensionMove(AttributeChange input) {
		return (input.getAttribute().eContainer().equals(NotationPackage.eINSTANCE.getRelativeBendpoints()) || input
				.getAttribute().equals(NotationPackage.eINSTANCE.getIdentityAnchor_Id())
				&& input.getRefines().isEmpty());
	}

	@Override
	protected boolean isRelatedToAnExtensionMove(ReferenceChange input) {
		return input.getValue() instanceof IdentityAnchor && input.getReference().isContainment()
				&& input.getRefines().isEmpty();
	}

	private List<Diff> getAllDifferencesForMove(Diff input) {
		final List<Diff> result = new ArrayList<Diff>();
		final EObject container = input.getMatch().eContainer();
		if (container instanceof Match) {
			final Iterator<Diff> diffs = ((Match)container).getAllDifferences().iterator();
			while (diffs.hasNext()) {
				final Diff diff = (Diff)diffs.next();
				if (getRelatedExtensionKind(input) != null) {
					result.add(diff);
				}
			}
		}
		return result;
	}

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

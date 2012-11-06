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

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.List;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diagram.DiagramCompareFactory;
import org.eclipse.emf.compare.diagram.NodeChange;
import org.eclipse.emf.compare.diagram.diff.DiagramComparisonConfiguration;
import org.eclipse.emf.compare.diagram.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Factory for UMLAssociationChangeLeftTarget.
 */
public class NodeChangeFactory extends AbstractDiffExtensionFactory {

	private final DiagramComparisonConfiguration configuration;

	public NodeChangeFactory(DiagramComparisonConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Class<? extends Diff> getExtensionKind() {
		return NodeChange.class;
	}

	public Diff create(Diff input) {
		final NodeChange ret = DiagramCompareFactory.eINSTANCE.createNodeChange();

		final DifferenceKind extensionKind = getRelatedExtensionKind(input);

		ret.setKind(extensionKind);

		// refined by
		if (extensionKind == DifferenceKind.DELETE) {
			ret.getRefinedBy().add(input);
		} else if (extensionKind == DifferenceKind.ADD) {
			ret.getRefinedBy().addAll(getAllContainedDifferences((ReferenceChange)input));
		} else if (extensionKind == DifferenceKind.MOVE) {
			ret.getRefinedBy().addAll(input.getMatch().getDifferences());
		}

		if (input instanceof ReferenceChange) {
			ret.setView(((ReferenceChange)input).getValue());
		} else if (input instanceof AttributeChange) {
			Comparison comparison = input.getMatch().getComparison();
			EObject container = MatchUtil.getContainer(comparison, input);
			while (container != null && !(container instanceof View)) {
				container = container.eContainer();
			}
			ret.setView(container);
		}

		ret.setSource(input.getSource());
		ret.setSemanticDiff(getSemanticDiff(input));

		return ret;
	}

	@Override
	public Match getParentMatch(Diff input) {
		return input.getMatch().getComparison().getMatch(getViewContainer(input));
	}

	@Override
	protected boolean isRelatedToAnExtensionAdd(ReferenceChange input) {
		return input.getReference().isContainment() && input.getKind() == DifferenceKind.ADD
				&& input.getValue() instanceof Node
				&& ReferenceUtil.safeEGet(input.getValue(), NotationPackage.Literals.VIEW__ELEMENT) != null;
	}

	@Override
	protected boolean isRelatedToAnExtensionDelete(ReferenceChange input) {
		return input.getReference().isContainment() && input.getKind() == DifferenceKind.DELETE
				&& input.getValue() instanceof Node
				&& ReferenceUtil.safeEGet(input.getValue(), NotationPackage.Literals.VIEW__ELEMENT) != null;
	}

	@Override
	protected boolean isRelatedToAnExtensionMove(AttributeChange input) {
		return input.getAttribute().eContainer().equals(NotationPackage.eINSTANCE.getLocation())
				&& Collections2.filter(input.getRefines(), isMoveNodeExtension()).isEmpty()
				&& isOverThreshold(input);
	}

	/**
	 * Check if the moving of the node is over the threshold (in pixels) specified in the emf compare
	 * preference page.
	 * 
	 * @param diff
	 *            The difference.
	 * @return True if it is over the threshold.
	 */
	private boolean isOverThreshold(AttributeChange diff) {
		final Comparison comparison = diff.getMatch().getComparison();
		final EObject left = MatchUtil.getContainer(comparison, diff);
		final EObject right = MatchUtil.getOriginContainer(comparison, diff);
		if (left instanceof Bounds && right instanceof Bounds) {
			final int leftX = ((Bounds)left).getX();
			final int leftY = ((Bounds)left).getY();
			final int rightX = ((Bounds)right).getX();
			final int rightY = ((Bounds)right).getY();
			final int deltaX = Math.abs(leftX - rightX);
			final int deltaY = Math.abs(leftY - rightY);
			final int threshold = configuration.getMoveThreshold();
			return deltaX + deltaY > threshold;
		}
		return false;
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

	private static Predicate<? super Diff> isMoveNodeExtension() {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				if (input instanceof NodeChange && input.getKind() == DifferenceKind.MOVE) {
					return true;
				}
				return false;
			}
		};
	}

}

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
package org.eclipse.emf.compare.diagram.internal.factories.extensions;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diagram.internal.CompareDiagramConfiguration;
import org.eclipse.emf.compare.diagram.internal.extensions.CoordinatesChange;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.diagram.internal.extensions.ExtensionsFactory;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.NotationPackage;

/**
 * Factory of coordinates changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class CoordinatesChangeFactory extends NodeChangeFactory {

	/** Configuration of the diagram comparison. */
	private final CompareDiagramConfiguration configuration;

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The configuration of the diagram comparison.
	 */
	public CoordinatesChangeFactory(CompareDiagramConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#getExtensionKind()
	 */
	@Override
	public Class<? extends Diff> getExtensionKind() {
		return CoordinatesChange.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#createExtension()
	 */
	@Override
	public DiagramDiff createExtension() {
		return ExtensionsFactory.eINSTANCE.createCoordinatesChange();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.internal.factories.extensions.NodeChangeFactory#setRefiningChanges(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.emf.compare.DifferenceKind, org.eclipse.emf.compare.Diff)
	 */
	@Override
	public void setRefiningChanges(Diff extension, DifferenceKind extensionKind, Diff refiningDiff) {
		if (extensionKind == DifferenceKind.CHANGE) {
			extension.getRefinedBy().addAll(getAllDifferencesForChange(refiningDiff));
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.internal.factories.extensions.NodeChangeFactory#getAllDifferencesForChange(org.eclipse.emf.compare.Diff)
	 */
	@Override
	protected Collection<Diff> getAllDifferencesForChange(Diff input) {
		Collection<Diff> diffs = super.getAllDifferencesForChange(input);
		return Collections2.filter(diffs, new Predicate<Diff>() {
			public boolean apply(Diff diff) {
				return diff instanceof AttributeChange && isCoordinatesChange((AttributeChange)diff);
			}
		});
	}

	/**
	 * It returns the predicate to check that the given difference is a macroscopic coordinates change.
	 * 
	 * @return The predicate.
	 */
	public static Predicate<? super Diff> isCoordinatesChangeExtension() {
		return and(instanceOf(CoordinatesChange.class), ofKind(DifferenceKind.CHANGE));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#isRelatedToAnExtensionMove(org.eclipse.emf.compare.AttributeChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionChange(AttributeChange input) {
		return isCoordinatesChange(input)
				&& Collections2.filter(input.getRefines(), isCoordinatesChangeExtension()).isEmpty()
				&& isOverThreshold(input) && !isLeadedByMoveNode(input);
	}

	/**
	 * It returns the predicate to check that the given difference concerns a move of node.
	 * 
	 * @param scannedMatches
	 *            List of browsed matches when looking for the match ancestor which contains a matching move
	 *            change.
	 * @return The predicate.
	 */
	private static Predicate<? super Diff> isRelatedToMove(final List<Match> scannedMatches) {
		return new Predicate<Diff>() {
			public boolean apply(Diff difference) {
				if (!scannedMatches.isEmpty()) {
					return difference instanceof ReferenceChange
							&& isRelatedToAMoveNode((ReferenceChange)difference)
							&& scannedMatches.contains(scannedMatches.get(0).getComparison().getMatch(
									((ReferenceChange)difference).getValue()));
				} else {
					return false;
				}

			}
		};
	}

	/**
	 * It checks that the given attribute change concerns a change of coordinates.
	 * 
	 * @param input
	 *            The difference.
	 * @return True if it is a change of coordinates.
	 */
	private static boolean isCoordinatesChange(AttributeChange input) {
		return input.getAttribute() == NotationPackage.Literals.LOCATION__X
				|| input.getAttribute() == NotationPackage.Literals.LOCATION__Y;
	}

	/**
	 * It checks that the given attribute change is leaded by a move of the same object.
	 * 
	 * @param input
	 *            The attribute change.
	 * @return True if it is leaded by a move, False otherwise.
	 */
	private boolean isLeadedByMoveNode(AttributeChange input) {
		List<Match> scannedMatches = new ArrayList<Match>();
		boolean result = false;
		EObject match = input.getMatch();
		while (!result && match instanceof Match) {
			scannedMatches.add((Match)match);
			result = Iterators.any(((Match)match).getDifferences().iterator(),
					isRelatedToMove(scannedMatches));
			match = match.eContainer();
		}
		return result;
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
			int threshold = 0;
			if (configuration != null) {
				threshold = configuration.getMoveThreshold();
			}
			return deltaX + deltaY > threshold;
		}
		return false;
	}

}

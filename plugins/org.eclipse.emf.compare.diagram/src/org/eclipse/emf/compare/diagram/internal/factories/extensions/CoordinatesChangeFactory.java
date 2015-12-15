/*******************************************************************************
 * Copyright (c) 2013, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 482404
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.internal.factories.extensions;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
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
import org.eclipse.gmf.runtime.notation.Location;
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
			extension.getRefinedBy().addAll(
					Collections2.filter(getAllDifferencesForChange(refiningDiff), fromSide(extension
							.getSource())));
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.internal.factories.extensions.NodeChangeFactory#getAllDifferencesForChange(org.eclipse.emf.compare.Diff)
	 */
	@Override
	protected Collection<Diff> getAllDifferencesForChange(final Diff input) {
		final Collection<Diff> diffs = super.getAllDifferencesForChange(input);
		return Collections2.filter(diffs, new Predicate<Diff>() {
			public boolean apply(Diff diff) {
				return diff instanceof AttributeChange && isCoordinatesChange((AttributeChange)diff)
						&& fromSide(input.getSource()).apply(diff);
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
		final CoordinateProvider leftCoordinateProvider = new CoordinateProvider(left);
		final CoordinateProvider rightCoordinateProvider = new CoordinateProvider(right);
		if (leftCoordinateProvider.hasCoordinates() && rightCoordinateProvider.hasCoordinates()) {
			final int leftX = leftCoordinateProvider.getX();
			final int leftY = leftCoordinateProvider.getY();
			final int rightX = rightCoordinateProvider.getX();
			final int rightY = rightCoordinateProvider.getY();
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

	/**
	 * A provider for coordinates.
	 * <p>
	 * This provider can return the coordinates of either a shape with {@link Bounds} or a decoration node
	 * with a {@link Location}.
	 * </p>
	 * 
	 * @author Philip Langer <planger@eclipsesource.com>
	 */
	private static class CoordinateProvider {

		/** The element this provider should return the coordinates of. */
		private EObject element;

		/**
		 * Creates a provider for the given {@code element}.
		 * 
		 * @param element
		 *            The element this provider should return the coordinates of.
		 */
		public CoordinateProvider(EObject element) {
			this.element = element;
		}

		/**
		 * Specifies whether this provider can provide coordinates for its element.
		 * 
		 * @return <code>true</code> if it can provide coordinates, <code>false</code> otherwise.
		 */
		public boolean hasCoordinates() {
			return element instanceof Bounds || element instanceof Location;
		}

		/**
		 * Returns the X value.
		 * 
		 * @return The X value.
		 */
		public int getX() {
			final int x;
			if (element instanceof Bounds) {
				x = ((Bounds)element).getX();
			} else if (element instanceof Location) {
				x = ((Location)element).getX();
			} else {
				x = -1;
			}
			return x;
		}

		/**
		 * Returns the Y value.
		 * 
		 * @return The Y value.
		 */
		public int getY() {
			final int y;
			if (element instanceof Bounds) {
				y = ((Bounds)element).getY();
			} else if (element instanceof Location) {
				y = ((Location)element).getY();
			} else {
				y = -1;
			}
			return y;
		}
	}
}

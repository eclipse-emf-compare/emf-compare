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
package org.eclipse.emf.compare;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.conflict.DefaultConflictDetector;
import org.eclipse.emf.compare.conflict.IConflictDetector;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.equi.DefaultEquiEngine;
import org.eclipse.emf.compare.equi.IEquiEngine;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.req.DefaultReqEngine;
import org.eclipse.emf.compare.req.IReqEngine;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.util.EcoreUtil;

// FIXME progress monitor!
/**
 * This class provides various utility methods that can be used to call EMF Compare on various notifiers.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class EMFCompare {
	/**
	 * This class does not need to be instantiated.
	 */
	private EMFCompare() {
		// hides default constructor
	}

	/**
	 * Launch a two-way comparison on the two given {@link Notifier}s and their direct children according to
	 * the semantics of the {@link DefaultComparisonScope}. The result of this comparison will be returned in
	 * the form of a {@link Comparison} instance.
	 * 
	 * @param left
	 *            The notifier that is to be consider as the left side of this comparison.
	 * @param right
	 *            The notifier that is to be consider as the right side of this comparison.
	 * @return The result of the two-way comparison of these two notifiers.
	 */
	public static Comparison compare(Notifier left, Notifier right) {
		return compare(left, right, null);
	}

	/**
	 * According to the value of <code>origin</code>, this will launch either a two-way or a three-way
	 * comparison on the given {@link Notifier}s and their direct content (according to the semantics of the
	 * {@link DefaultComparisonScope}). The result of this comparison will be returned in the form of a
	 * {@link Comparison} instance.
	 * 
	 * @param left
	 *            The notifier that is to be consider as the left side of this comparison.
	 * @param right
	 *            The notifier that is to be consider as the right side of this comparison.
	 * @param origin
	 *            The notifier that should be considered as the common ancestor of <code>left</code> and
	 *            <code>right</code>. If <code>null</code>, a two-way comparison will be performed instead.
	 * @return The result of the three-way comparison of these notifiers if <code>origin</code> is not
	 *         <code>null</code>, the result of the two-way comparison of <code>left</code> and
	 *         <code>right</code> otherwise.
	 */
	public static Comparison compare(Notifier left, Notifier right, Notifier origin) {
		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);

		return compare(scope);
	}

	/**
	 * Launches the comparison for the given comparison scope.
	 * 
	 * @param scope
	 *            The scope on which a comparison is to be performed.
	 * @return The result of this comparison.
	 */
	public static Comparison compare(IComparisonScope scope) {
		// TODO allow extension of the default match engine
		final IMatchEngine matchEngine = new DefaultMatchEngine();
		Comparison comparison = matchEngine.match(scope);

		// TODO allow extension of the default diff engine
		final IDiffEngine diffEngine = new DefaultDiffEngine();
		diffEngine.diff(comparison);

		EcoreUtil.CrossReferencer crossReferencer = ReferenceUtil.initializeCrossReferencer(comparison);

		// TODO allow extension of the default requirements engine
		final IReqEngine reqEngine = new DefaultReqEngine(crossReferencer);
		reqEngine.computeRequirements(comparison);

		// TODO allow extension of the default equivalences engine
		final IEquiEngine equiEngine = new DefaultEquiEngine(crossReferencer);
		equiEngine.computeEquivalences(comparison);

		// TODO allow extension of the default conflict detector
		if (comparison.isThreeWay()) {
			final IConflictDetector conflictDetector = new DefaultConflictDetector();
			conflictDetector.detect(comparison);
		}

		return comparison;
	}
}

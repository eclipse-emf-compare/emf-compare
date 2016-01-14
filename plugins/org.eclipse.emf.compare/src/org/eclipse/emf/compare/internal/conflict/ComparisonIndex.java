/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.conflict;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Index of diffs in a comparison.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public final class ComparisonIndex {

	/**
	 * Index of reference changes by their target.
	 */
	private final Multimap<Object, ReferenceChange> refChangeIndex;

	/**
	 * The indexed comparison.
	 */
	private final Comparison comparison;

	/**
	 * Constructor.
	 * 
	 * @param comparison
	 *            The comparison
	 */
	private ComparisonIndex(Comparison comparison) {
		this.comparison = comparison;
		refChangeIndex = LinkedHashMultimap.create();
		index();
	}

	/**
	 * Utility method to index a comparison.
	 * 
	 * @param comparison
	 *            The comparison to index, O(nb. diff)
	 * @param monitor
	 *            the monitor
	 * @return The index of the given comparison.
	 */
	public static ComparisonIndex index(Comparison comparison, Monitor monitor) {
		monitor.subTask("Indexing differences..."); //$NON-NLS-1$
		ComparisonIndex index = new ComparisonIndex(comparison);
		monitor.subTask("Indexing differences... Done."); //$NON-NLS-1$
		return index;
	}

	/**
	 * Actually computes the index.
	 */
	private void index() {
		for (Diff diff : comparison.getDifferences()) {
			if (diff instanceof ReferenceChange) {
				final EObject value = ((ReferenceChange)diff).getValue();
				Match match = comparison.getMatch(value);
				if (match != null) {
					refChangeIndex.put(match, (ReferenceChange)diff);
				} else {
					refChangeIndex.put(EcoreUtil.getURI(value), (ReferenceChange)diff);
				}
			} else {
				continue;
			}
		}
	}

	/**
	 * The indexed {@link ReferenceChange}s whose value is in the given Match.
	 * 
	 * @param value
	 *            The target {@link EObject}
	 * @return A never null collection of {@link ReferenceChange}s whose value is in the same match as the
	 *         given EObject, or has the same URI.
	 */
	public Collection<ReferenceChange> getReferenceChangesByValue(EObject value) {
		Match match = comparison.getMatch(value);
		if (refChangeIndex.containsKey(match)) {
			return refChangeIndex.get(match);
		}
		return getReferenceChangesByValueURI(EcoreUtil.getURI(value));
	}

	/**
	 * The indexed {@link ReferenceChange}s whose value is in the given Match.
	 * 
	 * @param valueMatch
	 *            The target {@link Match}
	 * @return A never null collection of {@link ReferenceChange}s whose value in the given match.
	 */
	public Collection<ReferenceChange> getReferenceChangesByValueMatch(Match valueMatch) {
		if (refChangeIndex.containsKey(valueMatch)) {
			return refChangeIndex.get(valueMatch);
		}
		return Collections.emptyList();
	}

	/**
	 * The indexed {@link ReferenceChange}s whose value has the given URI (only unresolved proxies are indexed
	 * that way).
	 * 
	 * @param valueURI
	 *            The URI to look for
	 * @return A never null collection of {@link ReferenceChange}s whose value is unresolved and has the given
	 *         URI
	 */
	public Collection<ReferenceChange> getReferenceChangesByValueURI(URI valueURI) {
		if (refChangeIndex.containsKey(valueURI)) {
			return refChangeIndex.get(valueURI);
		}
		return Collections.emptyList();
	}
}

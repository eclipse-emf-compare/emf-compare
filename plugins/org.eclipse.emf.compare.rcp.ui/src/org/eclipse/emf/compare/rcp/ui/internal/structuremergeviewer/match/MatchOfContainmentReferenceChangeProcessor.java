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
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.match;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.match.MatchOfContainmentReferenceChangeAdapter;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.ecore.EObject;

/**
 * Process a comparison to detect {@link Match}es related to containment ReferenceChange. Add a
 * {@link MatchOfContainmentReferenceChangeAdapter} on such Matches.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class MatchOfContainmentReferenceChangeProcessor {

	/**
	 * Constructor.
	 */
	public MatchOfContainmentReferenceChangeProcessor() {
	}

	/**
	 * Check for the given {@link Comparison}, if {@link Match}es are related to a containment
	 * ReferenceChange. If it is add a {@link MatchOfContainmentReferenceChangeAdapter} to these
	 * {@link Match}es.
	 * 
	 * @param comp
	 *            The {@link Comparison} to check.
	 */
	public void execute(Comparison comp) {
		for (Match rootMatch : comp.getMatches()) {
			checkForMatchRelatedToContainmentReferenceChange(rootMatch);
		}
	}

	/**
	 * Check if the given {@link Match} is related to a containment ReferenceChange. If it is add a
	 * {@link MatchOfContainmentReferenceChangeAdapter} to this {@link Match}. Also check for all sub-matches
	 * of the given {@link Match}.
	 * 
	 * @param match
	 *            The {@link Match} to check
	 */
	protected void checkForMatchRelatedToContainmentReferenceChange(Match match) {
		EObject parentMatch = match.eContainer();
		if (parentMatch instanceof Match) {
			EList<Diff> differences = ((Match)parentMatch).getDifferences();
			for (Diff parentMatchDiff : differences) {
				if (EMFComparePredicates.CONTAINMENT_REFERENCE_CHANGE.apply(parentMatchDiff)) {
					EObject value = ((ReferenceChange)parentMatchDiff).getValue();
					if (value != null && (value.equals(match.getLeft()) || value.equals(match.getRight())
							|| value.equals(match.getOrigin()))) {
						match.eAdapters().add(new MatchOfContainmentReferenceChangeAdapter(
								(ReferenceChange)parentMatchDiff));
						break;
					}
				}
			}
		}
		for (Match subMatch : match.getSubmatches()) {
			checkForMatchRelatedToContainmentReferenceChange(subMatch);
		}
	}
}

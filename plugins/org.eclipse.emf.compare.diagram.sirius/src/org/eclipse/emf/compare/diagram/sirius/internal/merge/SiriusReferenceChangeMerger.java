/*******************************************************************************
 * Copyright (c) 2020 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.sirius.internal.merge;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.merge.ComputeDiffsToMerge;
import org.eclipse.emf.compare.merge.DiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IDiffRelationshipComputer;
import org.eclipse.emf.compare.merge.ReferenceChangeMerger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.viewpoint.DMappingBased;
import org.eclipse.sirius.viewpoint.description.RepresentationElementMapping;

/**
 * This specific implementation of {@link ReferenceChangeMerger} will be used to merge Sirius changes.
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
public class SiriusReferenceChangeMerger extends ReferenceChangeMerger {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#isMergerFor(org.eclipse.emf.compare.Diff)
	 */
	@Override
	public boolean isMergerFor(Diff target) {
		boolean result = false;
		if (target instanceof ReferenceChange) {
			EObject value = ((ReferenceChange)target).getValue();
			result = (value instanceof DMappingBased || value instanceof RepresentationElementMapping)
					&& (!target.getImpliedBy().isEmpty() || !target.getImplies().isEmpty());
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.AbstractMerger#copyDiff(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.emf.common.util.Monitor, boolean)
	 */
	@Override
	protected void copyDiff(Diff target, Monitor monitor, boolean rightToLeft) {
		super.copyDiff(target, monitor, rightToLeft);

		Set<Diff> impliedMerges = getImpliedMerges(target, rightToLeft);
		Set<Diff> impliedMappings = new HashSet<Diff>();
		while (!impliedMerges.isEmpty()) {
			Diff impliedMerge = impliedMerges.iterator().next();
			impliedMerges.addAll(getImpliedMerges(impliedMerge, rightToLeft));
			if (impliedMerge instanceof ReferenceChange) {
				EObject value = ((ReferenceChange)impliedMerge).getValue();
				if (value instanceof DMappingBased || value instanceof RepresentationElementMapping) {
					impliedMerge.setState(DifferenceState.UNRESOLVED);
					impliedMappings.add(impliedMerge);
				}
			}
			impliedMerges.remove(impliedMerge);
		}

		mergeImpliedMappings(impliedMappings, rightToLeft);
	}

	/**
	 * Used to merge implied differences which are related to a RepresentationElementMapping or DMappingBased,
	 * in the correct order, according to the required dependencies.
	 * 
	 * @param impliedMappings
	 *            The set of implied differences which have a mapping.
	 * @param rightToLeft
	 *            Merge direction.
	 */
	private void mergeImpliedMappings(Set<Diff> impliedMappings, boolean rightToLeft) {
		IDiffRelationshipComputer relationshipComputer = new DiffRelationshipComputer(getRegistry());
		ComputeDiffsToMerge computer = new ComputeDiffsToMerge(rightToLeft, relationshipComputer);
		for (Diff impliedMapping : impliedMappings) {
			for (Diff toMerge : computer.getAllDiffsToMerge(impliedMapping)) {
				if (!isInTerminalState(toMerge)) {
					mergeDiff(toMerge, rightToLeft, null);
				}
			}
		}
	}
}

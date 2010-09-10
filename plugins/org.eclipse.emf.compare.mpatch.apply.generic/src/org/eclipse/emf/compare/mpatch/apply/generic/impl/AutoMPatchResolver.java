/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.apply.generic.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.apply.util.MPatchResolver;
import org.eclipse.emf.compare.mpatch.apply.util.MPatchValidator;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences;
import org.eclipse.emf.ecore.EObject;

/**
 * Helper class to automatically resolve all conflicts. In the worst case, all changes are ignored.
 * 
 * The strategy first tries to remove all resolved elements with invalid states, then it tries to fix cardinality
 * problems, and finally it removed all changes that depend on invalid changes.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class AutoMPatchResolver {

	/**
	 * Resolve all conflicts in the given mapping. In the worst case, all changes are ignored.
	 * 
	 * The strategy first tries to remove all resolved elements with invalid states, then it tries to fix cardinality
	 * problems, and finally it removed all changes that depend on invalid changes.
	 * 
	 * @param mapping
	 *            The mapping.
	 */
	static void resolve(ResolvedSymbolicReferences mapping) {
		// throw new UnsupportedOperationException("not yet implemented!");

		// 1. remove all additions for those corresponding parents that have already such an element
		/*
		 * TODO: The reason we have that here is that instead of re-adding an element here, we want to re-use the
		 * existing one. But if we ignore the change for adding that element, then all depending changes will of course
		 * fail! So what we rather have to do here is:
		 * 
		 * 1. add a new ValidationResult status ALREADY_EXISTS or so...
		 * 
		 * 2. The reference resolution is fine as is
		 * 
		 * 3. When applying the change, it is not added but only the binding is added!!!
		 */

		// 2. remove all corresponding elements that produce invalid states
		removeInvalidStateResolutions(mapping);

		// 3. remove all dependants of invalid changes
		removeInvalidDependencies(mapping);

		// 4. final validation whether we succeeded
		validateBinding(mapping);
	}

	/**
	 * Remove all changes that depend on invalid changes.
	 * 
	 * @param mapping
	 *            The mapping.
	 */
	private static void removeInvalidDependencies(ResolvedSymbolicReferences mapping) {
		final Set<IndepChange> valid = mapping.getResolutionByChange().keySet();
		final Set<IndepChange> invalid = collectInvalidChanges(valid);
		removeInvalidDependants(valid, invalid);
	}

	/**
	 * Iterate over all <code>invalid</code> changes and remove all dependants of them from <code>valid</code>.
	 * 
	 * @param valid
	 *            The current set of valid changes (might be reduced in this call).
	 * @param invalid
	 *            A set of invalid changes.
	 */
	private static void removeInvalidDependants(Set<IndepChange> valid, Set<IndepChange> invalid) {
		final Set<IndepChange> processed = new HashSet<IndepChange>(valid.size());

		// remove all dependants of invalid changes
		final Queue<IndepChange> queue2 = new ArrayDeque<IndepChange>();
		queue2.addAll(invalid);
		processed.clear();
		while (!queue2.isEmpty()) {
			final IndepChange change = queue2.poll();
			if (!processed.contains(change)) {
				processed.add(change);
				valid.remove(change); // if it exists, it is removed. nothing happens otherwise.
				queue2.addAll(change.getDependants());
			}
		}
	}

	/**
	 * Collect all invalid changes in the dependency graph.
	 * 
	 * @param valid
	 *            A set of all valid changes in the dependency graph.
	 * @return A set of all invalid changes in the dependency graph.
	 */
	private static Set<IndepChange> collectInvalidChanges(Set<IndepChange> valid) {
		final Set<IndepChange> invalid = new HashSet<IndepChange>(valid.size());
		final Set<IndepChange> processed = new HashSet<IndepChange>(valid.size());

		// collect all invalid changes in the dependency graph
		final Queue<IndepChange> queue1 = new ArrayDeque<IndepChange>();
		queue1.addAll(valid);
		while (!queue1.isEmpty()) {
			final IndepChange change = queue1.poll();
			if (!processed.contains(change)) {
				processed.add(change);
				if (!valid.contains(change)) {
					invalid.add(change);
				}
				queue1.addAll(change.getDependsOn());
			}
		}
		return invalid;
	}

	/**
	 * Try to fix the resolved corresponding elements. In case it could not be fixed, the entire change is removed from
	 * the mapping.
	 * 
	 * @param mapping
	 *            The mapping.
	 */
	private static void removeInvalidStateResolutions(ResolvedSymbolicReferences mapping) {
		final boolean forward = mapping.getDirection() == ResolvedSymbolicReferences.RESOLVE_UNCHANGED;
		final List<IndepChange> changes = new ArrayList<IndepChange>(mapping.getResolutionByChange().keySet());
		for (IndepChange change : changes) {

			// get some helper variables and validate the change unstrictly
			final Map<IElementReference, List<EObject>> resolution = mapping.getResolutionByChange().get(change);
			if (!MPatchResolver.checkStateResolution(change, resolution, true, forward)) {
				mapping.getResolutionByChange().remove(change);
			}
		}
	}

	/**
	 * Perform a final validation whether our strategy succeeded. If not, disable all changes that are invalid! In the
	 * worst case, all of them are disabled...
	 * 
	 * @param mapping
	 *            The mapping that is validated.
	 */
	private static void validateBinding(ResolvedSymbolicReferences mapping) {
		List<IndepChange> violations = MPatchValidator.validateResolutions(mapping);

		// brute force: deactivate all invalid changes.
		// this will terminate because in the worst case all changes have been removed.
		while (!violations.isEmpty()) {
			for (IndepChange change : violations) {
				mapping.getResolutionByChange().remove(change);
			}
			violations = MPatchValidator.validateResolutions(mapping);
		}
	}

}

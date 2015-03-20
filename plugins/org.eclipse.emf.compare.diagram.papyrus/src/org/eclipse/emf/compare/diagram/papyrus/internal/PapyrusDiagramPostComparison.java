/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.internal;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;

/**
 * This class materializes the post-comparison treatment. This treatment manages relations between notation
 * changes and UML semantic changes when such changes should be linked. For instance, when the target of a
 * connector is changed in the representation, it is also changed in the UML model.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class PapyrusDiagramPostComparison implements Runnable {
	/** The comparison. */
	private final Comparison comparison;

	/** The indexer. */
	private final DiffIndexer indexer;

	/**
	 * Constructor.
	 * 
	 * @param comparison
	 *            The comparison
	 */
	public PapyrusDiagramPostComparison(Comparison comparison) {
		this.comparison = comparison;
		indexer = new DiffIndexer();
	}

	/**
	 * Executes the treatment. The algorithm takes 2 passes:
	 * <ol>
	 * <li>Index all {@link Diff}s that may be related with another {@link Diff}, using specific keys</li>
	 * <li>for each key in the index, create bi-directional implication links between the concerned diffs</li>
	 * </ol>
	 * This algorithm is linear, indexing in the first pass prevents fancy searches in the comparison model
	 * which may impede scalability.
	 */
	public void run() {
		IDiffHandler handler = getHandler();
		for (Diff diff : comparison.getDifferences()) {
			handler.handle(diff);
		}
		linkEquivalentDiffsWithRequiresRelations();
	}

	/**
	 * Once all related differences are indexed, walk through the index and create the implication links
	 * between all concerned {@link Diff}s. The indexing of {@link Diff}s must have been done before calling
	 * this method.
	 */
	private void linkEquivalentDiffsWithRequiresRelations() {
		for (Object key : indexer.getEquivalentDiffsKeySet()) {
			Set<Diff> diffs = indexer.getEquivalentDiffs(key);
			if (diffs.size() > 1) {
				for (Diff diff : diffs) {
					diff.getRequires().addAll(Sets.difference(diffs, Collections.singleton(diff)));
				}
			}
		}
	}

	/**
	 * Provide the {@link IDiffHandler} for the comparison used by this instance.
	 * 
	 * @return An indexer that manages 3-way comparisons if the comparison is 3-way, or an indexer that
	 *         manages 2-way comparisons otherwise.
	 */
	protected IDiffHandler getHandler() {
		if (comparison.isThreeWay()) {
			return new PapyrusDiagram3WayDiffHandler(indexer);
		} else {
			return new PapyrusDiagram2WayDiffHandler(indexer);
		}
	}
}

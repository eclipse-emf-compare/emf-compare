package org.eclipse.emf.compare.egit.internal.merge;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.StrategyResolve;
import org.eclipse.jgit.merge.ThreeWayMerger;

/**
 * A three-way merge strategy leaving the merging to the
 * {@link org.eclipse.core.resources.mapping.ModelProvider models} if applicable, and delegating to the
 * {@link org.eclipse.jgit.merge.StrategyRecursive} otherwise.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class StrategyAdditiveModel extends StrategyResolve {
	@Override
	public ThreeWayMerger newMerger(Repository db) {
		return new AdditiveModelMerger(db, false);
	}

	@Override
	public ThreeWayMerger newMerger(Repository db, boolean inCore) {
		return new AdditiveModelMerger(db, inCore);
	}

	@Override
	public String getName() {
		return "model additive"; //$NON-NLS-1$
	}
}

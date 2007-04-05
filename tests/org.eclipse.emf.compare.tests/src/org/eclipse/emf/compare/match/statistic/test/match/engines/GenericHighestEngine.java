package org.eclipse.emf.compare.match.statistic.test.match.engines;

import org.eclipse.emf.compare.match.MatchModel;
import org.eclipse.emf.compare.match.api.MatchEngine;
import org.eclipse.emf.ecore.EObject;

/**
 * a dummy match engine
 * 
 * @author Cedric Brun <cedric.brun@obeo.fr>
 * 
 */
public class GenericHighestEngine implements MatchEngine {

	public MatchModel modelMatch(final EObject leftRoot, final EObject rightRoot) {
		return null;
	}

}

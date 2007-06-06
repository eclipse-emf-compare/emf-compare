package org.eclipse.emf.compare.tests.match.engines;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.match.MatchModel;
import org.eclipse.emf.compare.match.api.MatchEngine;
import org.eclipse.emf.ecore.EObject;
/**
 * A dummy match engine.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 * 
 */
public class CHighEngine implements MatchEngine {
	/**
	 * Returns the {@link org.eclipse.emf.compare.match.MatchModel MatchModel} of
	 * the comparison of two models.
	 * 
	 * @param leftRoot
	 * 			Left model of the comparison.
	 * @param rightRoot
	 * 			Right model of the comparison.
	 * @param monitor
	 * 			Progress monitor for this comparison.
	 * @return
	 * 			The {@link org.eclipse.emf.compare.match.MatchModel MatchModel} for
	 * 			the comparison of those two models.
	 */
	public MatchModel modelMatch(final EObject leftRoot, final EObject rightRoot, IProgressMonitor monitor) {
		return null;
	}
}

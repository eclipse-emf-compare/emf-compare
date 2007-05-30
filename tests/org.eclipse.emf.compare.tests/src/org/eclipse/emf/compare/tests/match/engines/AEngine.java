package org.eclipse.emf.compare.tests.match.engines;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.match.MatchModel;
import org.eclipse.emf.compare.match.api.MatchEngine;
import org.eclipse.emf.ecore.EObject;

/**
 * a dummy match engine
 * 
 * @author Cedric Brun  <a href="mailto:cedric.brun@obeo.fr ">cedric.brun@obeo.fr</a> 
 * 
 */
public class AEngine implements MatchEngine {

	public MatchModel modelMatch(final EObject leftRoot, final EObject rightRoot,IProgressMonitor monitor) {
		return null;
	}

}

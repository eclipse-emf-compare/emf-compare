package org.eclipse.emf.compare.tests.unit;

import junit.framework.ComparisonFailure;

import org.eclipse.emf.compare.match.api.MatchEngine;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.tests.util.EMFCompareTestCase;

/**
 * Test the engine contribution priority handling.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 * 
 */
public class TestEnginesPriority extends EMFCompareTestCase {
	/**
	 * Test the engine choosing with file extension.
	 * 
	 * @throws ComparisonFailure
	 * 			Thrown when an assertEquals for String has failed.
	 */
	public void testEngineFileExtension() throws ComparisonFailure {
		MatchEngine engine = null;
		engine = MatchService.getInstance().getBestMatchEngine("a"); //$NON-NLS-1$
		assertEquals("AEngine", engine.getClass().getSimpleName()); //$NON-NLS-1$
		engine = MatchService.getInstance().getBestMatchEngine("b"); //$NON-NLS-1$
		assertEquals("BEngine", engine.getClass().getSimpleName()); //$NON-NLS-1$
	}

	/**
	 * Test the engine picking with priority.
	 * 
	 * @throws ComparisonFailure
	 * 			Thrown when an assertEquals for String has failed.
	 */
	public void testEnginePriority() throws ComparisonFailure {
		MatchEngine engine = null;
		engine = MatchService.getInstance().getBestMatchEngine("c"); //$NON-NLS-1$
		assertEquals("CHighEngine", engine.getClass().getSimpleName()); //$NON-NLS-1$
	}
}

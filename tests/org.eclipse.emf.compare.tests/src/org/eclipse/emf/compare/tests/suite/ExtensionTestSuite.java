package org.eclipse.emf.compare.tests.suite;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.emf.compare.tests.unit.TestEnginesPriority;

/**
 * Test suite for extensions contributions
 * 
 * @author Cedric Brun  <a href="mailto:cedric.brun@obeo.fr ">cedric.brun@obeo.fr</a> 
 * 
 */
public class ExtensionTestSuite {

	/**
	 * @return the test suite
	 */
	public static Test suite() {
		final TestSuite suite = new TestSuite(
				"Test for org.eclipse.emf.compare.match.statistic.test.suite"); //$NON-NLS-1$
		// $JUnit-BEGIN$
		suite.addTestSuite(TestEnginesPriority.class);
		// $JUnit-END$
		return suite;
	}

}

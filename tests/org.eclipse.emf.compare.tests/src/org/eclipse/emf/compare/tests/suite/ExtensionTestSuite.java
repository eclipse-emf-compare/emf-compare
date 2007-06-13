package org.eclipse.emf.compare.tests.suite;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.emf.compare.tests.unit.TestEnginesPriority;
import org.eclipse.emf.compare.tests.unit.TestFindAdapterFactory;

/**
 * Test suite for extensions contributions.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 * 
 */
public final class ExtensionTestSuite {
	private ExtensionTestSuite() {
		// prevents instantiation.
	}

	/**
	 * Creates and returns the test suite.
	 * 
	 * @return The test suite.
	 */
	public static Test suite() {
		final TestSuite suite = new TestSuite(
				"Test for org.eclipse.emf.compare.match.statistic.test.suite"); //$NON-NLS-1$
		// $JUnit-BEGIN$
		suite.addTestSuite(TestEnginesPriority.class);
		suite.addTestSuite(TestFindAdapterFactory.class);
		// not working if GMF or UML2 not in the current environnement,
		// I need to get feedback from releng master :)
		// suite.addTestSuite(TestNonRegressionModels.class);
		// $JUnit-END$
		return suite;
	}
}

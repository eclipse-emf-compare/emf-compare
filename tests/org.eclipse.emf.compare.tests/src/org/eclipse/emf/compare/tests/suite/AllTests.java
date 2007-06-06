package org.eclipse.emf.compare.tests.suite;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.IPlatformRunnable;

/**
 * Launches all the JUnit tests for EMF compare.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 * 
 */
public class AllTests extends TestCase implements IPlatformRunnable {
	/**
	 * Launches the test with the given arguments.
	 * 
	 * @param args
	 * 			Arguments of the testCase.
	 */
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	/**
	 * Creates the {@link junit.framework.TestSuite TestSuite} for all the test.
	 * 
	 * @return
	 * 			The testsuite containing all the tests
	 */
	public static Test suite() {
		final TestSuite suite = new TestSuite();
		suite.addTest(ExtensionTestSuite.suite());
		return suite;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object run(Object args) throws Exception {
		TestRunner.run(suite());
		return Arrays
				.asList(new String[] { "Please see raw test suite output for details." }); //$NON-NLS-1$
	}

}

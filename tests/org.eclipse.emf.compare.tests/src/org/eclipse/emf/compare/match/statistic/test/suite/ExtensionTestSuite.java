package org.eclipse.emf.compare.match.statistic.test.suite;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.emf.compare.match.statistic.test.unit.TestEnginesPriority;

public class ExtensionTestSuite {

	public static Test suite() {
		final TestSuite suite = new TestSuite(
				"Test for org.eclipse.emf.compare.match.statistic.test.suite");
		// $JUnit-BEGIN$
		suite.addTestSuite(TestEnginesPriority.class);
		// $JUnit-END$
		return suite;
	}

}

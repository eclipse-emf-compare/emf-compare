package org.eclipse.emf.compare.uml2.diff.test.suite;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.eclipse.emf.compare.uml2.diff.test.TestClassDiagram;
import org.eclipse.emf.compare.uml2.diff.test.TestCommunicationDiagram;
import org.eclipse.emf.compare.uml2.diff.test.TestProfile;
import org.eclipse.emf.compare.uml2.diff.test.TestSequenceDiagram;
import org.eclipse.emf.compare.uml2.diff.test.TestUseCaseDiagram;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestClassDiagram.class, TestCommunicationDiagram.class, TestProfile.class,
		TestSequenceDiagram.class, TestUseCaseDiagram.class,})
public class AllUML2Tests {

	/**
	 * Launches the test with the given arguments.
	 * 
	 * @param args
	 *            Arguments of the testCase.
	 */
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	/**
	 * Creates the {@link junit.framework.TestSuite TestSuite} for all the test.
	 * 
	 * @return The test suite containing all the tests
	 */
	public static Test suite() {
		return new JUnit4TestAdapter(AllUML2Tests.class);
	}

}

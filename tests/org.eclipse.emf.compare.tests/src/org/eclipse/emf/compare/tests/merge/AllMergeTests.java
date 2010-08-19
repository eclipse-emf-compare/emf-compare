package org.eclipse.emf.compare.tests.merge;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllMergeTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.emf.compare.tests.merge");
		// $JUnit-BEGIN$
		suite.addTestSuite(TestContainmentRemoveMany.class);
		suite.addTestSuite(TestContainmentOrderAddMany.class);
		suite.addTestSuite(NonUniqueAttributeOrderTest.class);
		suite.addTestSuite(NonContainmentOrderTest.class);
		suite.addTestSuite(ContainmentOrderTest.class);
		suite.addTestSuite(AttributeOrderTest.class);
		suite.addTestSuite(TestContainmentRemove.class);
		// $JUnit-END$
		return suite;
	}

}

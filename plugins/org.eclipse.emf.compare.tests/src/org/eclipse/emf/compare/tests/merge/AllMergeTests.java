/*******************************************************************************
 * Copyright (c) 2010, 2011 Gerhardt Informatics and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Csaba Koncz (Gerhardt Informatics) - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllMergeTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.emf.compare.tests.merge");
		// $JUnit-BEGIN$
		suite.addTestSuite(TestContainmentRemoveMany.class);
		suite.addTestSuite(TestContainmentOrderAddMany.class);
		suite.addTestSuite(DanglingReferenceOnTwoAdds.class);
		suite.addTestSuite(NonUniqueAttributeOrderTest.class);
		suite.addTestSuite(NonContainmentOrderTest.class);
		suite.addTestSuite(ContainmentOrderTest.class);
		suite.addTestSuite(AttributeOrderTest.class);
		suite.addTestSuite(TestContainmentRemove.class);
		suite.addTestSuite(SimpleEcoreHistoryMerge.class);
		suite.addTestSuite(UMLHistoryMerge.class);
		// $JUnit-END$
		return suite;
	}

}

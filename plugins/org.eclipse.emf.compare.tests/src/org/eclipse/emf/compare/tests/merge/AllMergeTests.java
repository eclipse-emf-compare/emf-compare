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

import org.eclipse.emf.compare.tests.merge.dependencies.DependenciesMergeTest;
import org.eclipse.emf.compare.tests.merge.multiplecontainmentreference.suite.MultipleContainmentMergeTestSuite;
import org.eclipse.emf.compare.tests.merge.multivaluedattribute.suite.MultiValuedAttributeMergeTestSuite;
import org.eclipse.emf.compare.tests.merge.multivaluedreference.suite.MultiValuedReferenceMergeTestSuite;
import org.eclipse.emf.compare.tests.merge.onemultivaluedcontainmentreference.suite.SimpleMergeTestSuite;
import org.eclipse.emf.compare.tests.merge.singlevaluedattribute.suite.SingleValuedAttributeMergeTestSuite;
import org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference.suite.SingleValuedContainmentMergeTestSuite;
import org.eclipse.emf.compare.tests.merge.singlevaluedreference.suite.SingleValuedReferenceMergeTestSuite;

@SuppressWarnings("nls")
public class AllMergeTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tests the merge use cases");
		// $JUnit-BEGIN$
		suite.addTestSuite(TestContainmentRemoveMany.class);
		suite.addTestSuite(TestContainmentOrderAddMany.class);
		suite.addTestSuite(DanglingReferenceOnTwoAdds.class);
		suite.addTestSuite(NonUniqueAttributeOrderTest.class);
		suite.addTestSuite(NonContainmentOrderTest.class);
		suite.addTestSuite(ContainmentOrderTest.class);
		suite.addTestSuite(AttributeOrderTest.class);
		suite.addTestSuite(TestContainmentRemove.class);

		/*
		 * Merge test metamodel use cases *
		 */
		suite.addTest(SimpleMergeTestSuite.suite());
		suite.addTest(SingleValuedContainmentMergeTestSuite.suite());
		suite.addTest(MultipleContainmentMergeTestSuite.suite());
		suite.addTest(SingleValuedAttributeMergeTestSuite.suite());
		suite.addTest(MultiValuedAttributeMergeTestSuite.suite());
		suite.addTest(SingleValuedReferenceMergeTestSuite.suite());
		suite.addTest(MultiValuedReferenceMergeTestSuite.suite());
		// fix bug 369507 before uncommenting this
		// suite.addTest(ComplexUnmatchedMergeTestSuite.suite());

		/*
		 * End of merge test metamodel use cases *
		 */

		suite.addTestSuite(SimpleEcoreHistoryMerge.class);
		suite.addTestSuite(SimpleEcoreHistoryMergeNoResource.class);
		suite.addTestSuite(SimpleEcoreHistoryMergeWithResource.class);
		suite.addTestSuite(SimpleEcoreHistoryMergeWithResourceSet.class);

		suite.addTestSuite(UMLHistoryMerge.class);
		suite.addTestSuite(UMLHistoryMergeNoResource.class);
		suite.addTestSuite(UMLHistoryMergeWithResource.class);
		suite.addTestSuite(UMLHistoryMergeWithResourceSet.class);

		// Dependencies tests
		suite.addTestSuite(DependenciesMergeTest.class);

		// $JUnit-END$
		return suite;
	}

}

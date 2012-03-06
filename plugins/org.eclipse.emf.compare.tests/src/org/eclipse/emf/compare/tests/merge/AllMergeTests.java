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

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.eclipse.emf.compare.tests.merge.complexunmatch.suite.ComplexUnmatchedMergeTestSuite;
import org.eclipse.emf.compare.tests.merge.dependencies.DependenciesMergeTest;
import org.eclipse.emf.compare.tests.merge.multiplecontainmentreference.suite.MultipleContainmentMergeTestSuite;
import org.eclipse.emf.compare.tests.merge.multivaluedattribute.suite.MultiValuedAttributeMergeTestSuite;
import org.eclipse.emf.compare.tests.merge.multivaluedreference.suite.MultiValuedReferenceMergeTestSuite;
import org.eclipse.emf.compare.tests.merge.onemultivaluedcontainmentreference.suite.SimpleMergeTestSuite;
import org.eclipse.emf.compare.tests.merge.singlevaluedattribute.suite.SingleValuedAttributeMergeTestSuite;
import org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference.suite.SingleValuedContainmentMergeTestSuite;
import org.eclipse.emf.compare.tests.merge.singlevaluedreference.suite.SingleValuedReferenceMergeTestSuite;
import org.eclipse.emf.compare.tests.nonregression.NonRegressionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestContainmentRemoveMany.class, TestContainmentOrderAddMany.class,
		DanglingReferenceOnTwoAdds.class, NonUniqueAttributeOrderTest.class, NonContainmentOrderTest.class,
		ContainmentOrderTest.class, AttributeOrderTest.class, TestContainmentRemove.class,
		SimpleMergeTestSuite.class, SingleValuedContainmentMergeTestSuite.class,
		MultipleContainmentMergeTestSuite.class, SingleValuedAttributeMergeTestSuite.class,
		MultiValuedAttributeMergeTestSuite.class, SingleValuedReferenceMergeTestSuite.class,
		MultiValuedReferenceMergeTestSuite.class, ComplexUnmatchedMergeTestSuite.class,
		SimpleEcoreHistoryMerge.class, SimpleEcoreHistoryMergeNoResource.class,
		SimpleEcoreHistoryMergeWithResource.class, SimpleEcoreHistoryMergeWithResourceSet.class,
		UMLHistoryMerge.class, UMLHistoryMergeNoResource.class, UMLHistoryMergeWithResource.class,
		UMLHistoryMergeWithResourceSet.class, DependenciesMergeTest.class, NonRegressionTest.class, })
public class AllMergeTests {
	/**
	 * Creates the {@link junit.framework.TestSuite TestSuite} for all the test.
	 * 
	 * @return The test suite containing all the tests
	 */
	public static Test suite() {
		return new JUnit4TestAdapter(AllMergeTests.class);
	}
}

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
import org.eclipse.emf.compare.tests.merge.multiplecontainmentreference.MultipleContainmentMergeUseCases;
import org.eclipse.emf.compare.tests.merge.multiplecontainmentreference.MultipleContainmentMergeUseCases3WayLocalChanges;
import org.eclipse.emf.compare.tests.merge.multiplecontainmentreference.MultipleContainmentMergeUseCases3WayLocalChangesNoResource;
import org.eclipse.emf.compare.tests.merge.multiplecontainmentreference.MultipleContainmentMergeUseCases3WayLocalChangesWithResource;
import org.eclipse.emf.compare.tests.merge.multiplecontainmentreference.MultipleContainmentMergeUseCases3WayLocalChangesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.multiplecontainmentreference.MultipleContainmentMergeUseCases3WayRemoteChanges;
import org.eclipse.emf.compare.tests.merge.multiplecontainmentreference.MultipleContainmentMergeUseCases3WayRemoteChangesNoResource;
import org.eclipse.emf.compare.tests.merge.multiplecontainmentreference.MultipleContainmentMergeUseCases3WayRemoteChangesWithResource;
import org.eclipse.emf.compare.tests.merge.multiplecontainmentreference.MultipleContainmentMergeUseCases3WayRemoteChangesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.multiplecontainmentreference.MultipleContainmentMergeUseCasesNoResource;
import org.eclipse.emf.compare.tests.merge.multiplecontainmentreference.MultipleContainmentMergeUseCasesScoped;
import org.eclipse.emf.compare.tests.merge.multiplecontainmentreference.MultipleContainmentMergeUseCasesWithResource;
import org.eclipse.emf.compare.tests.merge.multiplecontainmentreference.MultipleContainmentMergeUseCasesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.multivaluedattribute.MultiValuedAttributeMergeUseCases;
import org.eclipse.emf.compare.tests.merge.multivaluedattribute.MultiValuedAttributeMergeUseCases3WayLocalChanges;
import org.eclipse.emf.compare.tests.merge.multivaluedattribute.MultiValuedAttributeMergeUseCases3WayLocalChangesNoResource;
import org.eclipse.emf.compare.tests.merge.multivaluedattribute.MultiValuedAttributeMergeUseCases3WayLocalChangesWithResource;
import org.eclipse.emf.compare.tests.merge.multivaluedattribute.MultiValuedAttributeMergeUseCases3WayLocalChangesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.multivaluedattribute.MultiValuedAttributeMergeUseCases3WayRemoteChanges;
import org.eclipse.emf.compare.tests.merge.multivaluedattribute.MultiValuedAttributeMergeUseCases3WayRemoteChangesNoResource;
import org.eclipse.emf.compare.tests.merge.multivaluedattribute.MultiValuedAttributeMergeUseCases3WayRemoteChangesWithResource;
import org.eclipse.emf.compare.tests.merge.multivaluedattribute.MultiValuedAttributeMergeUseCases3WayRemoteChangesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.multivaluedattribute.MultiValuedAttributeMergeUseCasesNoResource;
import org.eclipse.emf.compare.tests.merge.multivaluedattribute.MultiValuedAttributeMergeUseCasesWithResource;
import org.eclipse.emf.compare.tests.merge.multivaluedattribute.MultiValuedAttributeMergeUseCasesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.multivaluedreference.MultiValuedReferenceMergeUseCases;
import org.eclipse.emf.compare.tests.merge.multivaluedreference.MultiValuedReferenceMergeUseCases3WayLocalChanges;
import org.eclipse.emf.compare.tests.merge.multivaluedreference.MultiValuedReferenceMergeUseCases3WayLocalChangesNoResource;
import org.eclipse.emf.compare.tests.merge.multivaluedreference.MultiValuedReferenceMergeUseCases3WayLocalChangesWithResource;
import org.eclipse.emf.compare.tests.merge.multivaluedreference.MultiValuedReferenceMergeUseCases3WayLocalChangesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.multivaluedreference.MultiValuedReferenceMergeUseCases3WayRemoteChanges;
import org.eclipse.emf.compare.tests.merge.multivaluedreference.MultiValuedReferenceMergeUseCases3WayRemoteChangesNoResource;
import org.eclipse.emf.compare.tests.merge.multivaluedreference.MultiValuedReferenceMergeUseCases3WayRemoteChangesWithResource;
import org.eclipse.emf.compare.tests.merge.multivaluedreference.MultiValuedReferenceMergeUseCases3WayRemoteChangesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.multivaluedreference.MultiValuedReferenceMergeUseCasesNoResource;
import org.eclipse.emf.compare.tests.merge.multivaluedreference.MultiValuedReferenceMergeUseCasesWithResource;
import org.eclipse.emf.compare.tests.merge.multivaluedreference.MultiValuedReferenceMergeUseCasesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.onemultivaluedcontainmentreference.SimpleMergeUseCases;
import org.eclipse.emf.compare.tests.merge.onemultivaluedcontainmentreference.SimpleMergeUseCases3WayLocalChanges;
import org.eclipse.emf.compare.tests.merge.onemultivaluedcontainmentreference.SimpleMergeUseCases3WayLocalChangesNoResource;
import org.eclipse.emf.compare.tests.merge.onemultivaluedcontainmentreference.SimpleMergeUseCases3WayLocalChangesWithResource;
import org.eclipse.emf.compare.tests.merge.onemultivaluedcontainmentreference.SimpleMergeUseCases3WayLocalChangesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.onemultivaluedcontainmentreference.SimpleMergeUseCases3WayRemoteChanges;
import org.eclipse.emf.compare.tests.merge.onemultivaluedcontainmentreference.SimpleMergeUseCases3WayRemoteChangesNoResource;
import org.eclipse.emf.compare.tests.merge.onemultivaluedcontainmentreference.SimpleMergeUseCases3WayRemoteChangesWithResource;
import org.eclipse.emf.compare.tests.merge.onemultivaluedcontainmentreference.SimpleMergeUseCases3WayRemoteChangesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.onemultivaluedcontainmentreference.SimpleMergeUseCasesNoResource;
import org.eclipse.emf.compare.tests.merge.onemultivaluedcontainmentreference.SimpleMergeUseCasesScoped;
import org.eclipse.emf.compare.tests.merge.onemultivaluedcontainmentreference.SimpleMergeUseCasesWithResource;
import org.eclipse.emf.compare.tests.merge.onemultivaluedcontainmentreference.SimpleMergeUseCasesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.singlevaluedattribute.SingleValuedAttributeMergeUseCases;
import org.eclipse.emf.compare.tests.merge.singlevaluedattribute.SingleValuedAttributeMergeUseCases3WayLocalChanges;
import org.eclipse.emf.compare.tests.merge.singlevaluedattribute.SingleValuedAttributeMergeUseCases3WayLocalChangesNoResource;
import org.eclipse.emf.compare.tests.merge.singlevaluedattribute.SingleValuedAttributeMergeUseCases3WayLocalChangesWithResource;
import org.eclipse.emf.compare.tests.merge.singlevaluedattribute.SingleValuedAttributeMergeUseCases3WayLocalChangesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.singlevaluedattribute.SingleValuedAttributeMergeUseCases3WayRemoteChanges;
import org.eclipse.emf.compare.tests.merge.singlevaluedattribute.SingleValuedAttributeMergeUseCases3WayRemoteChangesNoResource;
import org.eclipse.emf.compare.tests.merge.singlevaluedattribute.SingleValuedAttributeMergeUseCases3WayRemoteChangesWithResource;
import org.eclipse.emf.compare.tests.merge.singlevaluedattribute.SingleValuedAttributeMergeUseCases3WayRemoteChangesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.singlevaluedattribute.SingleValuedAttributeMergeUseCasesNoResource;
import org.eclipse.emf.compare.tests.merge.singlevaluedattribute.SingleValuedAttributeMergeUseCasesWithResource;
import org.eclipse.emf.compare.tests.merge.singlevaluedattribute.SingleValuedAttributeMergeUseCasesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference.SingleValuedContainmentMergeUseCases;
import org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference.SingleValuedContainmentMergeUseCases3WayLocalChanges;
import org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference.SingleValuedContainmentMergeUseCases3WayLocalChangesNoResource;
import org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference.SingleValuedContainmentMergeUseCases3WayLocalChangesWithResource;
import org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference.SingleValuedContainmentMergeUseCases3WayLocalChangesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference.SingleValuedContainmentMergeUseCases3WayRemoteChanges;
import org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference.SingleValuedContainmentMergeUseCases3WayRemoteChangesNoResource;
import org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference.SingleValuedContainmentMergeUseCases3WayRemoteChangesWithResource;
import org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference.SingleValuedContainmentMergeUseCases3WayRemoteChangesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference.SingleValuedContainmentMergeUseCasesNoResource;
import org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference.SingleValuedContainmentMergeUseCasesScoped;
import org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference.SingleValuedContainmentMergeUseCasesWithResource;
import org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference.SingleValuedContainmentMergeUseCasesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.singlevaluedreference.SingleValuedReferenceMergeUseCases;
import org.eclipse.emf.compare.tests.merge.singlevaluedreference.SingleValuedReferenceMergeUseCases3WayLocalChanges;
import org.eclipse.emf.compare.tests.merge.singlevaluedreference.SingleValuedReferenceMergeUseCases3WayLocalChangesNoResource;
import org.eclipse.emf.compare.tests.merge.singlevaluedreference.SingleValuedReferenceMergeUseCases3WayLocalChangesWithResource;
import org.eclipse.emf.compare.tests.merge.singlevaluedreference.SingleValuedReferenceMergeUseCases3WayLocalChangesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.singlevaluedreference.SingleValuedReferenceMergeUseCases3WayRemoteChanges;
import org.eclipse.emf.compare.tests.merge.singlevaluedreference.SingleValuedReferenceMergeUseCases3WayRemoteChangesNoResource;
import org.eclipse.emf.compare.tests.merge.singlevaluedreference.SingleValuedReferenceMergeUseCases3WayRemoteChangesWithResource;
import org.eclipse.emf.compare.tests.merge.singlevaluedreference.SingleValuedReferenceMergeUseCases3WayRemoteChangesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.singlevaluedreference.SingleValuedReferenceMergeUseCasesNoResource;
import org.eclipse.emf.compare.tests.merge.singlevaluedreference.SingleValuedReferenceMergeUseCasesWithResource;
import org.eclipse.emf.compare.tests.merge.singlevaluedreference.SingleValuedReferenceMergeUseCasesWithResourceSet;

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

		suite.addTestSuite(SimpleMergeUseCases.class);
		suite.addTestSuite(SimpleMergeUseCases3WayLocalChanges.class);
		suite.addTestSuite(SimpleMergeUseCases3WayRemoteChanges.class);
		suite.addTestSuite(SimpleMergeUseCasesNoResource.class);
		suite.addTestSuite(SimpleMergeUseCases3WayLocalChangesNoResource.class);
		suite.addTestSuite(SimpleMergeUseCases3WayRemoteChangesNoResource.class);
		suite.addTestSuite(SimpleMergeUseCasesWithResource.class);
		suite.addTestSuite(SimpleMergeUseCases3WayLocalChangesWithResource.class);
		suite.addTestSuite(SimpleMergeUseCases3WayRemoteChangesWithResource.class);
		suite.addTestSuite(SimpleMergeUseCasesWithResourceSet.class);
		suite.addTestSuite(SimpleMergeUseCases3WayLocalChangesWithResourceSet.class);
		suite.addTestSuite(SimpleMergeUseCases3WayRemoteChangesWithResourceSet.class);
		// Scope
		suite.addTestSuite(SimpleMergeUseCasesScoped.class);

		suite.addTestSuite(SingleValuedContainmentMergeUseCases.class);
		suite.addTestSuite(SingleValuedContainmentMergeUseCases3WayLocalChanges.class);
		suite.addTestSuite(SingleValuedContainmentMergeUseCases3WayRemoteChanges.class);
		suite.addTestSuite(SingleValuedContainmentMergeUseCasesNoResource.class);
		suite.addTestSuite(SingleValuedContainmentMergeUseCases3WayLocalChangesNoResource.class);
		suite.addTestSuite(SingleValuedContainmentMergeUseCases3WayRemoteChangesNoResource.class);
		suite.addTestSuite(SingleValuedContainmentMergeUseCasesWithResource.class);
		suite.addTestSuite(SingleValuedContainmentMergeUseCases3WayLocalChangesWithResource.class);
		suite.addTestSuite(SingleValuedContainmentMergeUseCases3WayRemoteChangesWithResource.class);
		suite.addTestSuite(SingleValuedContainmentMergeUseCasesWithResourceSet.class);
		suite.addTestSuite(SingleValuedContainmentMergeUseCases3WayLocalChangesWithResourceSet.class);
		suite.addTestSuite(SingleValuedContainmentMergeUseCases3WayRemoteChangesWithResourceSet.class);
		// Scope
		suite.addTestSuite(SingleValuedContainmentMergeUseCasesScoped.class);

		suite.addTestSuite(MultipleContainmentMergeUseCases.class);
		suite.addTestSuite(MultipleContainmentMergeUseCases3WayLocalChanges.class);
		suite.addTestSuite(MultipleContainmentMergeUseCases3WayRemoteChanges.class);
		suite.addTestSuite(MultipleContainmentMergeUseCasesNoResource.class);
		suite.addTestSuite(MultipleContainmentMergeUseCases3WayLocalChangesNoResource.class);
		suite.addTestSuite(MultipleContainmentMergeUseCases3WayRemoteChangesNoResource.class);
		suite.addTestSuite(MultipleContainmentMergeUseCasesWithResource.class);
		suite.addTestSuite(MultipleContainmentMergeUseCases3WayLocalChangesWithResource.class);
		suite.addTestSuite(MultipleContainmentMergeUseCases3WayRemoteChangesWithResource.class);
		suite.addTestSuite(MultipleContainmentMergeUseCasesWithResourceSet.class);
		suite.addTestSuite(MultipleContainmentMergeUseCases3WayLocalChangesWithResourceSet.class);
		suite.addTestSuite(MultipleContainmentMergeUseCases3WayRemoteChangesWithResourceSet.class);
		// Scope
		suite.addTestSuite(MultipleContainmentMergeUseCasesScoped.class);

		suite.addTestSuite(SingleValuedAttributeMergeUseCases.class);
		suite.addTestSuite(SingleValuedAttributeMergeUseCases3WayLocalChanges.class);
		suite.addTestSuite(SingleValuedAttributeMergeUseCases3WayRemoteChanges.class);
		suite.addTestSuite(SingleValuedAttributeMergeUseCasesNoResource.class);
		suite.addTestSuite(SingleValuedAttributeMergeUseCases3WayLocalChangesNoResource.class);
		suite.addTestSuite(SingleValuedAttributeMergeUseCases3WayRemoteChangesNoResource.class);
		suite.addTestSuite(SingleValuedAttributeMergeUseCasesWithResource.class);
		suite.addTestSuite(SingleValuedAttributeMergeUseCases3WayLocalChangesWithResource.class);
		suite.addTestSuite(SingleValuedAttributeMergeUseCases3WayRemoteChangesWithResource.class);
		suite.addTestSuite(SingleValuedAttributeMergeUseCasesWithResourceSet.class);
		suite.addTestSuite(SingleValuedAttributeMergeUseCases3WayLocalChangesWithResourceSet.class);
		suite.addTestSuite(SingleValuedAttributeMergeUseCases3WayRemoteChangesWithResourceSet.class);

		suite.addTestSuite(MultiValuedAttributeMergeUseCases.class);
		suite.addTestSuite(MultiValuedAttributeMergeUseCases3WayLocalChanges.class);
		suite.addTestSuite(MultiValuedAttributeMergeUseCases3WayRemoteChanges.class);
		suite.addTestSuite(MultiValuedAttributeMergeUseCasesNoResource.class);
		suite.addTestSuite(MultiValuedAttributeMergeUseCases3WayLocalChangesNoResource.class);
		suite.addTestSuite(MultiValuedAttributeMergeUseCases3WayRemoteChangesNoResource.class);
		suite.addTestSuite(MultiValuedAttributeMergeUseCasesWithResource.class);
		suite.addTestSuite(MultiValuedAttributeMergeUseCases3WayLocalChangesWithResource.class);
		suite.addTestSuite(MultiValuedAttributeMergeUseCases3WayRemoteChangesWithResource.class);
		suite.addTestSuite(MultiValuedAttributeMergeUseCasesWithResourceSet.class);
		suite.addTestSuite(MultiValuedAttributeMergeUseCases3WayLocalChangesWithResourceSet.class);
		suite.addTestSuite(MultiValuedAttributeMergeUseCases3WayRemoteChangesWithResourceSet.class);

		suite.addTestSuite(SingleValuedReferenceMergeUseCases.class);
		suite.addTestSuite(SingleValuedReferenceMergeUseCases3WayLocalChanges.class);
		suite.addTestSuite(SingleValuedReferenceMergeUseCases3WayRemoteChanges.class);
		suite.addTestSuite(SingleValuedReferenceMergeUseCasesNoResource.class);
		suite.addTestSuite(SingleValuedReferenceMergeUseCases3WayLocalChangesNoResource.class);
		suite.addTestSuite(SingleValuedReferenceMergeUseCases3WayRemoteChangesNoResource.class);
		suite.addTestSuite(SingleValuedReferenceMergeUseCasesWithResource.class);
		suite.addTestSuite(SingleValuedReferenceMergeUseCases3WayLocalChangesWithResource.class);
		suite.addTestSuite(SingleValuedReferenceMergeUseCases3WayRemoteChangesWithResource.class);
		suite.addTestSuite(SingleValuedReferenceMergeUseCasesWithResourceSet.class);
		suite.addTestSuite(SingleValuedReferenceMergeUseCases3WayLocalChangesWithResourceSet.class);
		suite.addTestSuite(SingleValuedReferenceMergeUseCases3WayRemoteChangesWithResourceSet.class);

		suite.addTestSuite(MultiValuedReferenceMergeUseCases.class);
		suite.addTestSuite(MultiValuedReferenceMergeUseCases3WayLocalChanges.class);
		suite.addTestSuite(MultiValuedReferenceMergeUseCases3WayRemoteChanges.class);
		suite.addTestSuite(MultiValuedReferenceMergeUseCasesNoResource.class);
		suite.addTestSuite(MultiValuedReferenceMergeUseCases3WayLocalChangesNoResource.class);
		suite.addTestSuite(MultiValuedReferenceMergeUseCases3WayRemoteChangesNoResource.class);
		suite.addTestSuite(MultiValuedReferenceMergeUseCasesWithResource.class);
		suite.addTestSuite(MultiValuedReferenceMergeUseCases3WayLocalChangesWithResource.class);
		suite.addTestSuite(MultiValuedReferenceMergeUseCases3WayRemoteChangesWithResource.class);
		suite.addTestSuite(MultiValuedReferenceMergeUseCasesWithResourceSet.class);
		suite.addTestSuite(MultiValuedReferenceMergeUseCases3WayLocalChangesWithResourceSet.class);
		suite.addTestSuite(MultiValuedReferenceMergeUseCases3WayRemoteChangesWithResourceSet.class);

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

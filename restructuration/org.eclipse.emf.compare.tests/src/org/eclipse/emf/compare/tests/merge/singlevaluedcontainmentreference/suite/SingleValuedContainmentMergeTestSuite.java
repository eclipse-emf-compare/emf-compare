/**
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.merge.singlevaluedcontainmentreference.suite;

import junit.framework.Test;
import junit.framework.TestSuite;

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

@SuppressWarnings("nls")
public class SingleValuedContainmentMergeTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tests the single valued containmnent merge use cases");
		// $JUnit-BEGIN$

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

		// $JUnit-END$
		return suite;
	}

}

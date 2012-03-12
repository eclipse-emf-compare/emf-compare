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
package org.eclipse.emf.compare.tests.merge.singlevaluedreference.suite;

import junit.framework.Test;
import junit.framework.TestSuite;

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
public class SingleValuedReferenceMergeTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tests the single valued reference merge use cases");
		// $JUnit-BEGIN$

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

		// $JUnit-END$
		return suite;
	}

}

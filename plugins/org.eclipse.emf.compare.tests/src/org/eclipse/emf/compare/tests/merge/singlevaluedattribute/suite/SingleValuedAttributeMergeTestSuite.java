/**
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.merge.singlevaluedattribute.suite;

import junit.framework.Test;
import junit.framework.TestSuite;

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

@SuppressWarnings("nls")
public class SingleValuedAttributeMergeTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tests the single valued attribute merge use cases");
		// $JUnit-BEGIN$

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

		// $JUnit-END$
		return suite;
	}

}

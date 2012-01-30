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
package org.eclipse.emf.compare.tests.merge.multivaluedreference.suite;

import junit.framework.Test;
import junit.framework.TestSuite;

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

@SuppressWarnings("nls")
public class MultiValuedReferenceMergeTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tests the multi-valued reference merge use cases");
		// $JUnit-BEGIN$

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

		// $JUnit-END$
		return suite;
	}

}

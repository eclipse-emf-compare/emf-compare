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
package org.eclipse.emf.compare.tests.merge.multivaluedattribute.suite;

import junit.framework.Test;
import junit.framework.TestSuite;

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

@SuppressWarnings("nls")
public class MultiValuedAttributeMergeTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tests the multi-valued attribute merge use cases");
		// $JUnit-BEGIN$

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

		// $JUnit-END$
		return suite;
	}

}

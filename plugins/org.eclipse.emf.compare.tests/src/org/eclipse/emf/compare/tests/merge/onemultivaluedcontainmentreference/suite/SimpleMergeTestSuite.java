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
package org.eclipse.emf.compare.tests.merge.onemultivaluedcontainmentreference.suite;

import junit.framework.Test;
import junit.framework.TestSuite;

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

@SuppressWarnings("nls")
public class SimpleMergeTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tests the simple merge use cases");
		// $JUnit-BEGIN$

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

		// $JUnit-END$
		return suite;
	}

}

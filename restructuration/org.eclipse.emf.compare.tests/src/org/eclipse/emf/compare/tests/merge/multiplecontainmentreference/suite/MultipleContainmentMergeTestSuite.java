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
package org.eclipse.emf.compare.tests.merge.multiplecontainmentreference.suite;

import junit.framework.Test;
import junit.framework.TestSuite;

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

@SuppressWarnings("nls")
public class MultipleContainmentMergeTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tests the multiple containment merge use cases");
		// $JUnit-BEGIN$

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

		// $JUnit-END$
		return suite;
	}

}

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
package org.eclipse.emf.compare.tests.merge.complexunmatch.suite;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.emf.compare.tests.merge.complexunmatch.ComplexUnmatchedMergeUseCases;
import org.eclipse.emf.compare.tests.merge.complexunmatch.ComplexUnmatchedMergeUseCases3WayLocalChanges;
import org.eclipse.emf.compare.tests.merge.complexunmatch.ComplexUnmatchedMergeUseCases3WayLocalChangesNoResource;
import org.eclipse.emf.compare.tests.merge.complexunmatch.ComplexUnmatchedMergeUseCases3WayLocalChangesWithResource;
import org.eclipse.emf.compare.tests.merge.complexunmatch.ComplexUnmatchedMergeUseCases3WayLocalChangesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.complexunmatch.ComplexUnmatchedMergeUseCases3WayRemoteChanges;
import org.eclipse.emf.compare.tests.merge.complexunmatch.ComplexUnmatchedMergeUseCases3WayRemoteChangesNoResource;
import org.eclipse.emf.compare.tests.merge.complexunmatch.ComplexUnmatchedMergeUseCases3WayRemoteChangesWithResource;
import org.eclipse.emf.compare.tests.merge.complexunmatch.ComplexUnmatchedMergeUseCases3WayRemoteChangesWithResourceSet;
import org.eclipse.emf.compare.tests.merge.complexunmatch.ComplexUnmatchedMergeUseCasesNoResource;
import org.eclipse.emf.compare.tests.merge.complexunmatch.ComplexUnmatchedMergeUseCasesWithResource;
import org.eclipse.emf.compare.tests.merge.complexunmatch.ComplexUnmatchedMergeUseCasesWithResourceSet;

@SuppressWarnings("nls")
public class ComplexUnmatchedMergeTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tests the complex unmatched merge use cases");
		// $JUnit-BEGIN$

		suite.addTestSuite(ComplexUnmatchedMergeUseCases.class);
		suite.addTestSuite(ComplexUnmatchedMergeUseCases3WayLocalChanges.class);
		suite.addTestSuite(ComplexUnmatchedMergeUseCases3WayRemoteChanges.class);
		suite.addTestSuite(ComplexUnmatchedMergeUseCasesNoResource.class);
		suite.addTestSuite(ComplexUnmatchedMergeUseCases3WayLocalChangesNoResource.class);
		suite.addTestSuite(ComplexUnmatchedMergeUseCases3WayRemoteChangesNoResource.class);
		suite.addTestSuite(ComplexUnmatchedMergeUseCasesWithResource.class);
		suite.addTestSuite(ComplexUnmatchedMergeUseCases3WayLocalChangesWithResource.class);
		suite.addTestSuite(ComplexUnmatchedMergeUseCases3WayRemoteChangesWithResource.class);
		suite.addTestSuite(ComplexUnmatchedMergeUseCasesWithResourceSet.class);
		suite.addTestSuite(ComplexUnmatchedMergeUseCases3WayLocalChangesWithResourceSet.class);
		suite.addTestSuite(ComplexUnmatchedMergeUseCases3WayRemoteChangesWithResourceSet.class);

		// $JUnit-END$
		return suite;
	}

}

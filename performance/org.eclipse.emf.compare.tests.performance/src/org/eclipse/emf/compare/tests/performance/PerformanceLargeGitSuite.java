/*******************************************************************************
 * Copyright (c) 2015 Obeo. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.performance;

import org.eclipse.emf.compare.tests.performance.git.large.TestLargeGitCompare;
import org.eclipse.emf.compare.tests.performance.git.large.TestLargeGitConflict;
import org.eclipse.emf.compare.tests.performance.git.large.TestLargeGitDiff;
import org.eclipse.emf.compare.tests.performance.git.large.TestLargeGitEqui;
import org.eclipse.emf.compare.tests.performance.git.large.TestLargeGitMatchId;
import org.eclipse.emf.compare.tests.performance.git.large.TestLargeGitPostComparisonUML;
import org.eclipse.emf.compare.tests.performance.git.large.TestLargeGitPostMatchUML;
import org.eclipse.emf.compare.tests.performance.git.large.TestLargeGitReq;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TestLargeGitMatchId.class,
//	TestLargeGitMatchContent.class,
	TestLargeGitDiff.class,
	TestLargeGitReq.class,
	TestLargeGitEqui.class,
	TestLargeGitConflict.class,
	TestLargeGitCompare.class,
	TestLargeGitPostMatchUML.class,
	TestLargeGitPostComparisonUML.class,
})
public class PerformanceLargeGitSuite {
   

}

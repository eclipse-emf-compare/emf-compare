/*
 * Copyright (c) 2015 Obeo. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.performance;

import org.eclipse.emf.compare.tests.performance.git.TestGitCompare;
import org.eclipse.emf.compare.tests.performance.git.TestGitConflict;
import org.eclipse.emf.compare.tests.performance.git.TestGitDiff;
import org.eclipse.emf.compare.tests.performance.git.TestGitEqui;
import org.eclipse.emf.compare.tests.performance.git.TestGitMatchContent;
import org.eclipse.emf.compare.tests.performance.git.TestGitMatchId;
import org.eclipse.emf.compare.tests.performance.git.TestGitPostComparisonUML;
import org.eclipse.emf.compare.tests.performance.git.TestGitPostMatchUML;
import org.eclipse.emf.compare.tests.performance.git.TestGitReq;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TestGitMatchId.class,
	TestGitMatchContent.class,
	TestGitDiff.class,
	TestGitReq.class,
	TestGitEqui.class,
	TestGitConflict.class,
	TestGitCompare.class,
	TestGitPostMatchUML.class,
	TestGitPostComparisonUML.class,
})
public class PerformanceGitSuite {
   

}

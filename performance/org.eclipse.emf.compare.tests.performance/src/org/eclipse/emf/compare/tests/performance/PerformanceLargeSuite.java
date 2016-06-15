/*******************************************************************************
 * Copyright (c) 2015 Obeo. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.performance;

import org.eclipse.emf.compare.tests.performance.large.TestLargeCompare;
import org.eclipse.emf.compare.tests.performance.large.TestLargeConflict;
import org.eclipse.emf.compare.tests.performance.large.TestLargeDiff;
import org.eclipse.emf.compare.tests.performance.large.TestLargeEqui;
import org.eclipse.emf.compare.tests.performance.large.TestLargeLogicalModel;
import org.eclipse.emf.compare.tests.performance.large.TestLargeMatchId;
import org.eclipse.emf.compare.tests.performance.large.TestLargePostComparisonUML;
import org.eclipse.emf.compare.tests.performance.large.TestLargePostMatchUML;
import org.eclipse.emf.compare.tests.performance.large.TestLargeReq;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestLargeMatchId.class,
		// TestLargeMatchContent.class,
		TestLargeDiff.class, TestLargeReq.class, TestLargeEqui.class, TestLargeConflict.class,
		TestLargeCompare.class, TestLargePostMatchUML.class, TestLargePostComparisonUML.class,
		TestLargeLogicalModel.class, })
public class PerformanceLargeSuite {

}

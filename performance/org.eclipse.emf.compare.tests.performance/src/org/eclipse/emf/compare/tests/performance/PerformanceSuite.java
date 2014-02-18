/*
 * Copyright (c) 2011, 2014 Obeo. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.performance;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TestMatchId.class,
	TestMatchContent.class,
	TestDiff.class,
	TestReq.class,
	TestEqui.class,
	TestConflict.class,
	TestCompare.class,
	TestPostMatchUML.class,
	TestPostComparisonUML.class
	//TestLogicalModel.class
})
public class PerformanceSuite {
   

}

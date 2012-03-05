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
package org.eclipse.emf.compare.tests.nonregression;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.EMFCompareTestRunner;
import org.eclipse.emf.compare.tests.framework.EObjectCouple;
import org.eclipse.emf.compare.tests.framework.MergeTestBase;
import org.eclipse.emf.compare.tests.framework.UseCase;
import org.eclipse.emf.compare.tests.nonregression.data.NonRegressionInputData;
import org.junit.runner.RunWith;

/**
 * Non-regression tests for EMF Compare.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@RunWith(EMFCompareTestRunner.class)
public class NonRegressionTest extends MergeTestBase {
	private NonRegressionInputData inputData = new NonRegressionInputData();

	@UseCase("Non-regression for bug 355897")
	public EObjectCouple bug355897() throws IOException {
		return new EObjectCouple(inputData.getBug355897Original(), inputData.getBug355897Modified());
	}
}

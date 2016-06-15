/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.performance.git;

import org.eclipse.emf.compare.tests.performance.AbstractEMFComparePerformanceTest;
import org.eclipse.emf.compare.tests.performance.TestPostMatchUML;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import data.models.DataGit;
import data.models.NominalGitInputData;
import data.models.NominalSplitGitInputData;
import data.models.SmallGitInputData;
import data.models.SmallSplitGitInputData;
import fr.obeo.performance.api.PerformanceMonitor;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGitPostMatchUML extends AbstractEMFComparePerformanceTest {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.tests.performance.AbstractEMFComparePerformanceTest#setSUTName()
	 */
	@Override
	protected void setSUTName() {
		getPerformance().getSystemUnderTest().setName(TestPostMatchUML.class.getSimpleName());
	}

	@Test
	public void a_pmUMLUMLSmall() {
		PerformanceMonitor monitor = getPerformance().createMonitor("pmUMLUMLSmall");

		final DataGit data = new SmallGitInputData();
		data.match();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.postMatchUML();
			}
		});
		data.dispose();
	}

	@Test
	public void b_pmUMLUMLNominal() {
		PerformanceMonitor monitor = getPerformance().createMonitor("pmUMLUMLNominal");

		final DataGit data = new NominalGitInputData();
		data.match();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.postMatchUML();
			}
		});
		data.dispose();
	}

	@Test
	public void c_pmUMLUMLSmallSplit() {
		PerformanceMonitor monitor = getPerformance().createMonitor("pmUMLUMLSmallSplit");

		final DataGit data = new SmallSplitGitInputData();
		data.match();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.postMatchUML();
			}
		});
		data.dispose();
	}

	@Test
	public void d_pmUMLUMLNominalSplit() {
		PerformanceMonitor monitor = getPerformance().createMonitor("pmUMLUMLNominalSplit");

		final DataGit data = new NominalSplitGitInputData();
		data.match();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.postMatchUML();
			}
		});
		data.dispose();
	}
}

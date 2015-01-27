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
import org.eclipse.emf.compare.tests.performance.TestCompare;
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
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGitCompare extends AbstractEMFComparePerformanceTest {

	/** 
	 * {@inheritDoc}
	 * @see org.eclipse.emf.compare.tests.performance.AbstractEMFComparePerformanceTest#setSUTName()
	 */
	@Override
	protected void setSUTName() {
		getPerformance().getSystemUnderTest().setName(TestCompare.class.getSimpleName());
	}

	@Test
	public void a_compareUMLSmall() {
		PerformanceMonitor monitor = getPerformance().createMonitor("compareUMLSmall");
		
		final DataGit data = new SmallGitInputData();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.compare();
			}
		});
		data.dispose();
	}
	
	@Test
	public void b_compareUMLNominal() {
		PerformanceMonitor monitor = getPerformance().createMonitor("compareUMLNominal");
		
		final DataGit data = new NominalGitInputData();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.compare();
			}
		});
		data.dispose();
	}
	
	@Test
	public void c_compareUMLSmallSplit() {
		PerformanceMonitor monitor = getPerformance().createMonitor("compareUMLSmallSplit");
		
		final DataGit data = new SmallSplitGitInputData();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.compare();
			}
		});
		data.dispose();
	}
	
	@Test
	public void d_compareUMLNominalSplit() {
		PerformanceMonitor monitor = getPerformance().createMonitor("compareUMLNominalSplit");
		
		final DataGit data = new NominalSplitGitInputData();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.compare();
			}
		});
		data.dispose();
	}
}

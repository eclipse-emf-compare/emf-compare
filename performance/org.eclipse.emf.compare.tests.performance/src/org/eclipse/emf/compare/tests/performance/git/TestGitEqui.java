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
import org.eclipse.emf.compare.tests.performance.TestEqui;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import data.models.DataGit;
import data.models.NominalGitInputData;
import data.models.SmallGitInputData;
import data.models.SmallSplitGitInputData;
import fr.obeo.performance.api.PerformanceMonitor;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGitEqui extends AbstractEMFComparePerformanceTest {

	/** 
	 * {@inheritDoc}
	 * @see org.eclipse.emf.compare.tests.performance.AbstractEMFComparePerformanceTest#setSUTName()
	 */
	@Override
	protected void setSUTName() {
		getPerformance().getSystemUnderTest().setName(TestEqui.class.getSimpleName());
	}

	@Test
	public void a_matchIdUMLSmall() {
		PerformanceMonitor monitor = getPerformance().createMonitor("equiUMLSmall");
		
		final DataGit data = new SmallGitInputData();
		data.match();
		data.diff();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.equi();
			}
		});
		data.dispose();
	}
	
	@Test
	public void b_matchIdUMLNominal() {
		PerformanceMonitor monitor = getPerformance().createMonitor("equiUMLNominal");
		
		final DataGit data = new NominalGitInputData();
		data.match();
		data.diff();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.equi();
			}
		});
		data.dispose();
	}
	
	@Test
	public void c_matchIdUMLSmallSplit() {
		PerformanceMonitor monitor = getPerformance().createMonitor("equiUMLSmallSplit");
		
		final DataGit data = new SmallSplitGitInputData();
		data.match();
		data.diff();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.equi();
			}
		});
		data.dispose();
	}
}

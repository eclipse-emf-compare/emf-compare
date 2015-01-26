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
import org.eclipse.emf.compare.tests.performance.TestMatchId;
import org.eclipse.emf.compare.utils.UseIdentifiers;
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
public class TestGitMatchId extends AbstractEMFComparePerformanceTest {

	/** 
	 * {@inheritDoc}
	 * @see org.eclipse.emf.compare.tests.performance.AbstractEMFComparePerformanceTest#setSUTName()
	 */
	@Override
	protected void setSUTName() {
		getPerformance().getSystemUnderTest().setName(TestMatchId.class.getSimpleName());
	}

	@Test
	public void a_matchIdUMLSmall() {
		PerformanceMonitor monitor = getPerformance().createMonitor("matchIdUMLSmall");
		
		final DataGit data = new SmallGitInputData();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.match(UseIdentifiers.ONLY);
			}
		});
		data.dispose();
	}
	
	@Test
	public void b_matchIdUMLNominal() {
		PerformanceMonitor monitor = getPerformance().createMonitor("matchIdUMLNominal");
		
		final DataGit data = new NominalGitInputData();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.match(UseIdentifiers.ONLY);
			}
		});
		data.dispose();
	}
	
	@Test
	public void c_matchIdUMLSmallSplit() {
		PerformanceMonitor monitor = getPerformance().createMonitor("matchIdUMLSmallSplit");
		
		final DataGit data = new SmallSplitGitInputData();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.match(UseIdentifiers.ONLY);
			}
		});
		data.dispose();
	}
}

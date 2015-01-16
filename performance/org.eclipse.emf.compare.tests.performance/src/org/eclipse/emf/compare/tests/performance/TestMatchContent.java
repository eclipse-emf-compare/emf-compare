/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.performance;

import java.io.IOException;

import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import data.models.Data;
import data.models.LargeInputData;
import data.models.NominalInputData;
import data.models.SmallInputData;
import data.models.SmallSplitInputData;
import fr.obeo.performance.api.PerformanceMonitor;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMatchContent extends AbstractEMFComparePerformanceTest {

	/** 
	 * {@inheritDoc}
	 * @see org.eclipse.emf.compare.tests.performance.AbstractEMFComparePerformanceTest#setSUTName()
	 */
	@Override
	protected void setSUTName() {
		getPerformance().getSystemUnderTest().setName(TestMatchContent.class.getSimpleName());
	}

	@Test
	public void a_matchContentUMLSmall() {
		PerformanceMonitor monitor = getPerformance().createMonitor("matchContentUMLSmall");
		final Data data = new SmallInputData();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.match(UseIdentifiers.NEVER);
			}
		});
		data.dispose();
	}
	
	@Test
	public void b_matchContentUMLNominal() throws IOException {
		PerformanceMonitor monitor = getPerformance().createMonitor("matchContentUMLNominal");
		final Data data = new NominalInputData();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.match(UseIdentifiers.NEVER);
			}
		});
		data.dispose();
	}
	
	@Test
	public void c_matchContentUMLLarge() throws IOException {
		PerformanceMonitor monitor = getPerformance().createMonitor("matchContentUMLLarge");
		final Data data = new LargeInputData();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				//data.match(UseIdentifiers.NEVER);
			}
		});
		data.dispose();
	}
	
	@Test
	public void d_matchContentUMLSmallSplit() {
		PerformanceMonitor monitor = getPerformance().createMonitor("matchContentUMLSmallSplit");
		final Data data = new SmallSplitInputData();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.match(UseIdentifiers.NEVER);
			}
		});
		data.dispose();
	}
}

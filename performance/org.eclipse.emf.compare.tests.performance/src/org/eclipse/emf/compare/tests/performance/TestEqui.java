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

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import data.models.Data;
import data.models.NominalInputData;
import data.models.NominalSplitInputData;
import data.models.SmallInputData;
import data.models.SmallSplitInputData;
import fr.obeo.performance.api.PerformanceMonitor;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestEqui extends AbstractEMFComparePerformanceTest {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.tests.performance.AbstractEMFComparePerformanceTest#setSUTName()
	 */
	@Override
	protected void setSUTName() {
		getPerformance().getSystemUnderTest().setName(TestEqui.class.getSimpleName());
	}

	@Test
	public void a_equiUMLSmall() {
		PerformanceMonitor monitor = getPerformance().createMonitor("equiUMLSmall");
		final Data data = new SmallInputData();
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
	public void b_equiUMLNominal() throws IOException {
		PerformanceMonitor monitor = getPerformance().createMonitor("equiUMLNominal");
		final Data data = new NominalInputData();
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
	public void c_equiUMLSmallSplit() {
		PerformanceMonitor monitor = getPerformance().createMonitor("equiUMLSmallSplit");
		final Data data = new SmallSplitInputData();
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
	public void d_equiUMLNominalSplit() {
		PerformanceMonitor monitor = getPerformance().createMonitor("equiUMLNominalSplit");
		final Data data = new NominalSplitInputData();
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

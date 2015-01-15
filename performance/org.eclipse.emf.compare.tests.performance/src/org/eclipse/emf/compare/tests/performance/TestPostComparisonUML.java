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
import data.models.LargeInputData;
import data.models.NominalInputData;
import data.models.SmallInputData;
import fr.obeo.performance.api.PerformanceMonitor;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPostComparisonUML extends AbstractEMFComparePerformanceTest {

	/** 
	 * {@inheritDoc}
	 * @see org.eclipse.emf.compare.tests.performance.AbstractEMFComparePerformanceTest#setSUTName()
	 */
	@Override
	protected void setSUTName() {
		getPerformance().getSystemUnderTest().setName(TestPostComparisonUML.class.getSimpleName());
	}

	@Test
	public void a_pcUMLUMLSmall() {
		PerformanceMonitor monitor = getPerformance().createMonitor("pcUMLUMLSmall");
		final Data data = new SmallInputData();
		data.match();
		data.postMatchUML();
		data.diff();
		data.req();
		data.equi();
		data.conflict();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.postComparisonGMF();
			}
		});
		data.dispose();
	}
	
	@Test
	public void b_pcUMLUMLNominal() throws IOException {
		PerformanceMonitor monitor = getPerformance().createMonitor("pcUMLUMLNominal");
		final Data data = new NominalInputData();
		data.match();
		data.postMatchUML();
		data.diff();
		data.req();
		data.equi();
		data.conflict();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.postComparisonGMF();
			}
		});
		data.dispose();
	}
	
	@Test
	public void c_pcUMLUMLLarge() throws IOException {
		PerformanceMonitor monitor = getPerformance().createMonitor("pcUMLUMLLarge");
		final Data data = new LargeInputData();
		data.match();
		data.postMatchUML();
		data.diff();
		data.req();
		data.equi();
		data.conflict();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.postComparisonGMF();
			}
		});
		data.dispose();
	}
}

/*******************************************************************************
 * Copyright (c) 2014 Obeo.
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
public class TestPostMatchUML extends AbstractEMFComparePerformanceTest {

	/** 
	 * {@inheritDoc}
	 * @see org.eclipse.emf.compare.tests.performance.AbstractEMFComparePerformanceTest#setSUTName()
	 */
	@Override
	protected void setSUTName() {
		getPerformance().getSystemUnderTest().setName(TestPostMatchUML.class.getSimpleName());
	}

	@Test
	public void a_pmUMLUMLSmall() {
		PerformanceMonitor monitor = getPerformance().createMonitor("pmUMLUMLSmall");
		final Data data = new SmallInputData();
		data.match();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.postMatchUML();
			}
		});
		data.dispose();
	}
	
	@Test
	public void b_pmUMLUMLNominal() throws IOException {
		PerformanceMonitor monitor = getPerformance().createMonitor("pmUMLUMLNominal");
		final Data data = new NominalInputData();
		data.match();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.postMatchUML();
			}
		});
		data.dispose();
	}
	
//	@Test
	public void c_pmUMLUMLLarge() throws IOException {
		PerformanceMonitor monitor = getPerformance().createMonitor("pmUMLUMLLarge");
		final Data data = new LargeInputData();
		data.match();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.postMatchUML();
			}
		});
		data.dispose();
	}
}

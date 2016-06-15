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
package org.eclipse.emf.compare.tests.performance.large;

import java.io.IOException;

import org.eclipse.emf.compare.tests.performance.AbstractEMFComparePerformanceTest;
import org.eclipse.emf.compare.tests.performance.TestDiff;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import data.models.Data;
import data.models.LargeInputData;
import data.models.LargeSplitInputData;
import fr.obeo.performance.api.PerformanceMonitor;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLargeDiff extends AbstractEMFComparePerformanceTest {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.tests.performance.AbstractEMFComparePerformanceTest#setSUTName()
	 */
	@Override
	protected void setSUTName() {
		getPerformance().getSystemUnderTest().setName(TestDiff.class.getSimpleName());
	}

	@Test
	public void a_diffUMLLarge() throws IOException {
		PerformanceMonitor monitor = getPerformance().createMonitor("diffUMLLarge");
		final Data data = new LargeInputData();
		data.match();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.diff();
			}
		});
		data.dispose();
	}

	@Test
	public void b_diffUMLLargeSplit() throws IOException {
		PerformanceMonitor monitor = getPerformance().createMonitor("diffUMLLargeSplit");
		final Data data = new LargeSplitInputData();
		data.match();
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.diff();
			}
		});
		data.dispose();
	}
}

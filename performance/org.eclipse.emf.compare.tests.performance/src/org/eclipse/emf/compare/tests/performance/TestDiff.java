/*******************************************************************************
 * Copyright (c) 2012 Obeo.
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

import org.eclipse.emf.compare.match.eobject.IdentifierEObjectMatcher;
import org.junit.Test;

import data.models.Data;
import data.models.LargeInputData;
import data.models.NominalInputData;
import data.models.SmallInputData;
import fr.obeo.performance.api.PerformanceMonitor;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 *
 */
public class TestDiff extends AbstractEMFComparePerformanceTest {

	
	/** 
	 * {@inheritDoc}
	 * @see org.eclipse.emf.compare.tests.performance.AbstractEMFComparePerformanceTest#setSUTName()
	 */
	@Override
	protected void setSUTName() {
		getPerformance().getSystemUnderTest().setName(TestDiff.class.getSimpleName());
	}

	@Test
	public void diffUMLSmall() {
		PerformanceMonitor monitor = getPerformance().createMonitor("matchUMLSmall");
		final Data data = new SmallInputData();
		data.match(new IdentifierEObjectMatcher());
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.diff();
			}
		});
		data.dispose();
	}
	
	@Test
	public void diffUMLNominal() throws IOException {
		PerformanceMonitor monitor = getPerformance().createMonitor("matchUMLNominal");
		final Data data = new NominalInputData();
		data.match(new IdentifierEObjectMatcher());
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.diff();
			}
		});
		data.dispose();
	}
	
	@Test
	public void diffUMLLarge() throws IOException {
		PerformanceMonitor monitor = getPerformance().createMonitor("matchUMLLarge");
		final Data data = new LargeInputData();
		data.match(new IdentifierEObjectMatcher());
		monitor.measure(warmup(), getStepsNumber(), new Runnable() {
			public void run() {
				data.diff();
			}
		});
		data.dispose();
	}
}

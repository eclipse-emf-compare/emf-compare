/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.performance;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.transform;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.GitCorePreferences;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.google.common.io.Closeables;

import fr.obeo.performance.DataPoint;
import fr.obeo.performance.Dimension;
import fr.obeo.performance.Measure;
import fr.obeo.performance.PerformancePackage;
import fr.obeo.performance.Scenario;
import fr.obeo.performance.TestResult;
import fr.obeo.performance.api.Performance;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractEMFComparePerformanceTest {

	private static String timestamp;

	private static Performance performance;

	private static final Function<Measure, Scenario> MEASURE__SCENARIO = new Function<Measure, Scenario>() {
		public Scenario apply(Measure measure) {
			return ((TestResult)((DataPoint)measure.eContainer()).eContainer()).getScenario();
		}
	};

	private static final Function<Measure, Double> MEASURE__VALUE = new Function<Measure, Double>() {
		public Double apply(Measure measure) {
			return measure.getValue();
		}
	};

	private static final Function<Measure, String> MEASURE__NAME = new Function<Measure, String>() {
		public String apply(Measure measure) {
			return measure.getName();
		}
	};

	private static final Function<DataPoint, List<Measure>> DATAPOINT__MEASURE = new Function<DataPoint, List<Measure>>() {
		public List<Measure> apply(DataPoint point) {
			return point.getMeasures();
		}
	};

	private static final Function<TestResult, List<DataPoint>> TEST_RESULT__DATA_POINTS = new Function<TestResult, List<DataPoint>>() {
		public List<DataPoint> apply(TestResult testResult) {
			return testResult.getDataPoints();
		}
	};

	private static final Function<Iterable<Double>, Double> AVERAGE = new Function<Iterable<Double>, Double>() {
		public Double apply(Iterable<Double> it) {
			Double sum = 0.0;
			if (!Iterables.isEmpty(it)) {
				for (Double d : it) {
					sum += d;
				}
				return sum.doubleValue() / Iterables.size(it);
			}
			return sum;
		}
	};

	private static final int DEFAULT_STEPS_NUMBER = 3;

	private static final boolean DEFAULT_WARMUP = true;

	@BeforeClass
	public static void setUp() throws Exception {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			EPackage perf = PerformancePackage.eINSTANCE;
			EPackage.Registry.INSTANCE.put(perf.getNsURI(), perf);
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("performance",
					new XMIResourceFactoryImpl());

		}

		IEclipsePreferences eGitPreferences = InstanceScope.INSTANCE.getNode(Activator.getPluginId());
		eGitPreferences.putBoolean(GitCorePreferences.core_autoShareProjects, false);

		// Deactivate auto-building
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		if (workspace != null) {
			IWorkspaceDescription desc = workspace.getDescription();
			boolean isAutoBuilding = desc.isAutoBuilding();
			if (isAutoBuilding == true) {
				desc.setAutoBuilding(false);
				workspace.setDescription(desc);
			}
		}

		timestamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		performance = new Performance("emf.compare.performance");
	}

	@Before
	public final void configureSUTName() {
		setSUTName();
	}

	protected static final Performance getPerformance() {
		return performance;
	}

	/**
	 * Should be static but want to force impl.
	 */
	protected abstract void setSUTName();

	protected int getStepsNumber() {
		return DEFAULT_STEPS_NUMBER;
	}

	protected boolean warmup() {
		return DEFAULT_WARMUP;
	}

	@AfterClass
	public static void tearDown() {
		final NumberFormat nf = NumberFormat.getInstance(Locale.US);
		nf.setMaximumFractionDigits(2);

		EList<TestResult> allResults = performance.getPerformanceTest().getResults();
		Iterable<DataPoint> allDataPoints = concat(transform(allResults, TEST_RESULT__DATA_POINTS));
		Iterable<Measure> allMeasures = concat(transform(allDataPoints, DATAPOINT__MEASURE));

		ImmutableListMultimap<String, Measure> measuresByName = Multimaps.index(allMeasures, MEASURE__NAME);
		for (Entry<String, Collection<Measure>> entry : measuresByName.asMap().entrySet()) {
			String measureName = entry.getKey();

			File output = new File(MessageFormat.format("{0}-{1}.csv",
					performance.getSystemUnderTest().getName().replaceAll(" ", "_"),
					measureName.replaceAll(" ", "_")));

			PrintWriter writer = null;
			try {
				writer = new PrintWriter(new BufferedWriter(new FileWriter(output, true), 16384));
				BufferedReader br = new BufferedReader(new FileReader(output));
				String readLine = br.readLine();
				final int columns;
				if (readLine == null) {
					br.close();
					writer.println("Date, Small UML, Nominal UML, Small Split UML, Nominal Split UML");
					columns = 4;
				} else {
					// Get number of columns that contains measures
					columns = readLine.split(",").length - 1;
				}
				writer.print(timestamp + ",");
				Collection<Measure> measures = entry.getValue();
				ImmutableListMultimap<Scenario, Measure> measuresByScenario = Multimaps.index(measures,
						MEASURE__SCENARIO);
				String joinedMeasure = Joiner.on(',').join(transform(measuresByScenario.asMap().entrySet(),
						new Function<Entry<Scenario, Collection<Measure>>, String>() {
							public String apply(Entry<Scenario, Collection<Measure>> entry) {
								final Dimension dimension = getFirst(entry.getValue(), null).getDimension();
								Iterable<Double> transform = transform(entry.getValue(), MEASURE__VALUE);

								List<Double> minAvMax = Lists.newArrayList();
								minAvMax.add(Ordering.natural().min(transform));
								minAvMax.add(AVERAGE.apply(transform));
								minAvMax.add(Ordering.natural().max(transform));

								Iterable<String> transform2 = transform(minAvMax,
										new Function<Double, String>() {
											public String apply(Double d) {
												switch (dimension) {
													case MEMORY:
														return nf.format(SizeUnit.MEBI.convert(d))
																.replaceAll(",", "");
													case TIME:
														return nf.format(d).replaceAll(",", "");
												}
												return "";
											}
										});

								String ret = Joiner.on(';').join(transform2);
								return ret;
							}
						}));
				joinedMeasure = fillEmptyColumns(joinedMeasure, columns);
				writer.println(joinedMeasure);

			} catch (IOException e) {
				Throwables.propagate(e);
			} finally {
				try {
					Closeables.close(writer, true);
				} catch (IOException e) {
					// already swallowed
				}
			}
		}
		performance = null;
	}

	private static String fillEmptyColumns(String joinedMeasure, int columns) {
		final int filled = joinedMeasure.split(",").length;
		for (int i = 0; i < columns - filled; i++) {
			joinedMeasure += ",";
		}
		return joinedMeasure;
	}

	@After
	public void after() {
		// try to minimize difference between runs
		System.gc();
	}

}

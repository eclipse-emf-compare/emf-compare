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
package org.eclipse.emf.compare.tests.performance;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

/**
 * Temporary file: reorganize csv files of performances tests.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * 
 */
public class FixCSV {

	@Test
	public void test() throws IOException {
		Collection<String> scenarios = new ArrayList<String>();
		scenarios.add("TestCompare");
		scenarios.add("TestConflict");
		scenarios.add("TestDiff");
		scenarios.add("TestEqui");
		scenarios.add("TestLogicalModel");
		scenarios.add("TestMatchContent");
		scenarios.add("TestMatchId");
		scenarios.add("TestPostComparisonGMF");
		scenarios.add("TestPostComparisonUML");
		scenarios.add("TestPostMatchUML");
		scenarios.add("TestReq");

		Collection<String> tests = new ArrayList<String>();
		tests.add("-system_time.csv");
		tests.add("-heap_delta.csv");
		tests.add("-heap_peek.csv");

		for (String scenario : scenarios) {
			for (String test : tests) {
				File output = new File(scenario + test);
				if (output.exists()) {					
					Path path = Paths.get(output.toURI());
					Charset charset = StandardCharsets.UTF_8;
					List<String> content = Files.readAllLines(path, charset);
					String newContent = content.get(0) + "\n";
					// Filled lines with extra ',' if needed
					// Exclude the first line (titles)
					for (int i = 1; i < content.size(); i++) {
						String string = content.get(i);
						if (string != null && !string.isEmpty()) {
							newContent += fillEmptyColumns(string, 5) + "\n";
						}
					}
					Files.write(path, newContent.getBytes(charset));
				}
			}
		}
	}
	
	private static String fillEmptyColumns(String joinedMeasure, int columns) {
		final int filled = joinedMeasure.split(",").length;
		for (int i = 0; i < columns - filled; i++) {
			joinedMeasure += ",";
		}
		return joinedMeasure;
	}
}

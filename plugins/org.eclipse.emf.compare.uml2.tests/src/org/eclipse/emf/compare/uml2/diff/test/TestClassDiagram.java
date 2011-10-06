/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.diff.test;

import java.io.IOException;

import org.junit.Test;
// CHECKSTYLE:OFF
public class TestClassDiagram extends AbstractUMLCompareTest {

	private static final String DIAGRAM_KIND_PATH = "/diagrams/clazz/";

	@Test
	public void abstraction_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("abstraction/changeLeftTarget");
	}

	@Test
	public void abstraction_changeRightTarget() throws IOException, InterruptedException {
		testCompare("abstraction/changeRightTarget");
	}

	@Test
	public void association_changeLeftTarget_00() throws IOException, InterruptedException {
		testCompare("association/changeLeftTarget/_00");
	}

	@Test
	public void association_changeRightTarget_00() throws IOException, InterruptedException {
		testCompare("association/changeRightTarget/_00");
	}

	@Test
	public void association_changeLeftTarget_01() throws IOException, InterruptedException {
		testCompare("association/changeLeftTarget/_01");
	}

	@Test
	public void association_changeRightTarget_01() throws IOException, InterruptedException {
		testCompare("association/changeRightTarget/_01");
	}

	@Test
	public void associationBranch_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("associationBranch/changeLeftTarget");
	}

	@Test
	public void associationBranch_changeRightTarget() throws IOException, InterruptedException {
		testCompare("associationBranch/changeRightTarget");
	}

	@Test
	public void associationClass_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("associationClass/changeLeftTarget");
	}

	@Test
	public void associationClass_changeRightTarget() throws IOException, InterruptedException {
		testCompare("associationClass/changeRightTarget");
	}

	@Test
	public void clazz() throws IOException, InterruptedException {
		testCompare("clazz");
	}

	@Test
	public void containmentLink() throws IOException, InterruptedException {
		testCompare("containmentLink");
	}

	@Test
	public void dependency_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("dependency/changeLeftTarget");
	}

	@Test
	public void dependency_changeRightTarget() throws IOException, InterruptedException {
		testCompare("dependency/changeRightTarget");
	}

	@Test
	public void dependencyBranch_changeLeftTarget_00() throws IOException, InterruptedException {
		testCompare("dependencyBranch/changeLeftTarget/_00");
	}

	@Test
	public void dependencyBranch_changeRightTarget_00() throws IOException, InterruptedException {
		testCompare("dependencyBranch/changeRightTarget/_00");
	}

	@Test
	public void generalizationSet_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("generalizationSet/changeLeftTarget");
	}

	@Test
	public void generalizationSet_changeRightTarget() throws IOException, InterruptedException {
		testCompare("generalizationSet/changeRightTarget");
	}

	@Test
	public void interfaceRealization_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("interfaceRealization/changeLeftTarget");
	}

	@Test
	public void interfaceRealization_changeRightTarget() throws IOException, InterruptedException {
		testCompare("interfaceRealization/changeRightTarget");
	}

	@Test
	public void interfaze() throws IOException, InterruptedException {
		testCompare("interfaze");
	}

	@Test
	public void realization_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("realization/changeLeftTarget");
	}

	@Test
	public void realization_changeRightTarget() throws IOException, InterruptedException {
		testCompare("realization/changeRightTarget");
	}

	@Test
	public void substitution_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("substitution/changeLeftTarget");
	}

	@Test
	public void substitution_changeRightTarget() throws IOException, InterruptedException {
		testCompare("substitution/changeRightTarget");
	}

	@Test
	public void usage_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("usage/changeLeftTarget");
	}

	@Test
	public void usage_changeRightTarget() throws IOException, InterruptedException {
		testCompare("usage/changeRightTarget");
	}

	@Test
	public void abstraction_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("abstraction/changeLeftTarget");
	}

	@Test
	public void abstraction_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("abstraction/changeRightTarget");
	}

	@Test
	public void association_changeLeftTarget_00_merge() throws IOException, InterruptedException {
		testMerge("association/changeLeftTarget/_00");
	}

	@Test
	public void association_changeRightTarget_00_merge() throws IOException, InterruptedException {
		testMerge("association/changeRightTarget/_00");
	}

	@Test
	public void association_changeLeftTarget_01_merge() throws IOException, InterruptedException {
		testMerge("association/changeLeftTarget/_01");
	}

	@Test
	public void association_changeRightTarget_01_merge() throws IOException, InterruptedException {
		testMerge("association/changeRightTarget/_01");
	}

	@Test
	public void associationBranch_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("associationBranch/changeLeftTarget");
	}

	@Test
	public void associationBranch_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("associationBranch/changeRightTarget");
	}

	@Test
	public void associationClass_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("associationClass/changeLeftTarget");
	}

	@Test
	public void associationClass_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("associationClass/changeRightTarget");
	}

	@Test
	public void clazz_merge() throws IOException, InterruptedException {
		testMerge("clazz");
	}

	@Test
	public void containmentLink_merge() throws IOException, InterruptedException {
		testMerge("containmentLink");
	}

	@Test
	public void dependency_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("dependency/changeLeftTarget");
	}

	@Test
	public void dependency_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("dependency/changeRightTarget");
	}

	@Test
	public void dependencyBranch_changeLeftTarget_00_merge() throws IOException, InterruptedException {
		testMerge("dependencyBranch/changeLeftTarget/_00");
	}

	@Test
	public void dependencyBranch_changeRightTarget_00_merge() throws IOException, InterruptedException {
		testMerge("dependencyBranch/changeRightTarget/_00");
	}

	@Test
	public void generalizationSet_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("generalizationSet/changeLeftTarget");
	}

	@Test
	public void generalizationSet_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("generalizationSet/changeRightTarget");
	}

	@Test
	public void interfaceRealization_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("interfaceRealization/changeLeftTarget");
	}

	@Test
	public void interfaceRealization_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("interfaceRealization/changeRightTarget");
	}

	@Test
	public void interfaze_merge() throws IOException, InterruptedException {
		testMerge("interfaze");
	}

	@Test
	public void realization_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("realization/changeLeftTarget");
	}

	@Test
	public void realization_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("realization/changeRightTarget");
	}

	@Test
	public void substitution_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("substitution/changeLeftTarget");
	}

	@Test
	public void substitution_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("substitution/changeRightTarget");
	}

	@Test
	public void usage_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("usage/changeLeftTarget");
	}

	@Test
	public void usage_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("usage/changeRightTarget");
	}

	@Override
	String getDiagramKindPath() {
		return DIAGRAM_KIND_PATH;
	}
}

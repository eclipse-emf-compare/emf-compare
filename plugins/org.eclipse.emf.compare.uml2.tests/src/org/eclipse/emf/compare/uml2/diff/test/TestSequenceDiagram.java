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

public class TestSequenceDiagram extends AbstractUMLCompareTest {

	private static final String DIAGRAM_KIND_PATH = "/diagrams/sequence/";

	@Test
	public void actionExecutionSpecification_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("actionExecutionSpecification/changeLeftTarget");
	}

	@Test
	public void actionExecutionSpecification_changeRightTarget() throws IOException, InterruptedException {
		testCompare("actionExecutionSpecification/changeRightTarget");
	}

	@Test
	public void actionExecutionSpecification_changeLeftTarget_merge() throws IOException,
			InterruptedException {
		testMerge("actionExecutionSpecification/changeLeftTarget");
	}

	@Test
	public void actionExecutionSpecification_changeRightTarget_merge() throws IOException,
			InterruptedException {
		testMerge("actionExecutionSpecification/changeRightTarget");
	}

	@Test
	public void behaviorExecutionSpecification_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("behaviorExecutionSpecification/changeLeftTarget");
	}

	@Test
	public void behaviorExecutionSpecification_changeRightTarget() throws IOException, InterruptedException {
		testCompare("behaviorExecutionSpecification/changeRightTarget");
	}

	@Test
	public void behaviorExecutionSpecification_changeLeftTarget_merge() throws IOException,
			InterruptedException {
		testMerge("behaviorExecutionSpecification/changeLeftTarget");
	}

	@Test
	public void behaviorExecutionSpecification_changeRightTarget_merge() throws IOException,
			InterruptedException {
		testMerge("behaviorExecutionSpecification/changeRightTarget");
	}

	@Test
	public void destructionEvent_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("destructionEvent/changeLeftTarget");
	}

	@Test
	public void destructionEvent_changeRightTarget() throws IOException, InterruptedException {
		testCompare("destructionEvent/changeRightTarget");
	}

	@Test
	public void destructionEvent_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("destructionEvent/changeLeftTarget");
	}

	@Test
	public void destructionEvent_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("destructionEvent/changeRightTarget");
	}

	@Test
	public void timeConstraint_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("timeConstraint/changeLeftTarget");
	}

	@Test
	public void timeConstraint_changeRightTarget() throws IOException, InterruptedException {
		testCompare("timeConstraint/changeRightTarget");
	}

	@Test
	public void timeConstraint_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("timeConstraint/changeLeftTarget");
	}

	@Test
	public void timeConstraint_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("timeConstraint/changeRightTarget");
	}

	@Test
	public void message_standard_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("messages/standard/changeLeftTarget");
	}

	@Test
	public void message_standard_changeRightTarget() throws IOException, InterruptedException {
		testCompare("messages/standard/changeRightTarget");
	}

	@Test
	public void message_standard_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("messages/standard/changeLeftTarget");
	}

	@Test
	public void message_standard_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("messages/standard/changeRightTarget");
	}

	@Test
	public void message_lost_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("messages/lost/changeLeftTarget");
	}

	@Test
	public void message_lost_changeRightTarget() throws IOException, InterruptedException {
		testCompare("messages/lost/changeRightTarget");
	}

	@Test
	public void message_lost_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("messages/lost/changeLeftTarget");
	}

	@Test
	public void message_lost_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("messages/lost/changeRightTarget");
	}

	@Test
	public void message_found_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("messages/found/changeLeftTarget");
	}

	@Test
	public void message_found_changeRightTarget() throws IOException, InterruptedException {
		testCompare("messages/found/changeRightTarget");
	}

	@Test
	public void message_found_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("messages/found/changeLeftTarget");
	}

	@Test
	public void message_found_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("messages/found/changeRightTarget");
	}

	@Override
	String getDiagramKindPath() {
		return DIAGRAM_KIND_PATH;
	}

}

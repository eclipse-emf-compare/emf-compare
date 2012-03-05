/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
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

	private static final String DIAGRAM_KIND_PATH = "/diagrams/sequence/"; //$NON-NLS-1$

	@Test
	public void actionExecutionSpecification_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("actionExecutionSpecification/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void actionExecutionSpecification_changeRightTarget() throws IOException, InterruptedException {
		testCompare("actionExecutionSpecification/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void actionExecutionSpecification_changeLeftTarget_merge() throws IOException,
			InterruptedException {
		testMerge("actionExecutionSpecification/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void actionExecutionSpecification_changeRightTarget_merge() throws IOException,
			InterruptedException {
		testMerge("actionExecutionSpecification/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void behaviorExecutionSpecification_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("behaviorExecutionSpecification/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void behaviorExecutionSpecification_changeRightTarget() throws IOException, InterruptedException {
		testCompare("behaviorExecutionSpecification/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void behaviorExecutionSpecification_changeLeftTarget_merge() throws IOException,
			InterruptedException {
		testMerge("behaviorExecutionSpecification/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void behaviorExecutionSpecification_changeRightTarget_merge() throws IOException,
			InterruptedException {
		testMerge("behaviorExecutionSpecification/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void destructionEvent_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("destructionEvent/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void destructionEvent_changeRightTarget() throws IOException, InterruptedException {
		testCompare("destructionEvent/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void destructionEvent_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("destructionEvent/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void destructionEvent_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("destructionEvent/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void timeConstraint_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("timeConstraint/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void timeConstraint_changeRightTarget() throws IOException, InterruptedException {
		testCompare("timeConstraint/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void timeConstraint_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("timeConstraint/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void timeConstraint_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("timeConstraint/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void message_standard_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("messages/standard/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void message_standard_changeRightTarget() throws IOException, InterruptedException {
		testCompare("messages/standard/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void message_standard_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("messages/standard/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void message_standard_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("messages/standard/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void message_lost_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("messages/lost/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void message_lost_changeRightTarget() throws IOException, InterruptedException {
		testCompare("messages/lost/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void message_lost_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("messages/lost/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void message_lost_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("messages/lost/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void message_found_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("messages/found/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void message_found_changeRightTarget() throws IOException, InterruptedException {
		testCompare("messages/found/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void message_found_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("messages/found/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void message_found_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("messages/found/changeRightTarget"); //$NON-NLS-1$
	}

	@Override
	protected String getDiagramKindPath() {
		return DIAGRAM_KIND_PATH;
	}

}

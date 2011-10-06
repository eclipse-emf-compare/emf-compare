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
//CHECKSTYLE:OFF
public class TestCommunicationDiagram extends AbstractUMLCompareTest {

	private static final String DIAGRAM_KIND_PATH = "/diagrams/communication/";

	@Override
	String getDiagramKindPath() {
		return DIAGRAM_KIND_PATH;
	}

	@Test
	public void actionExecutionSpecification_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("message/changeLeftTarget");
	}

	@Test
	public void actionExecutionSpecification_changeRightTarget() throws IOException, InterruptedException {
		testCompare("message/changeRightTarget");
	}

	@Test
	public void actionExecutionSpecification_changeLeftTarget_merge() throws IOException,
			InterruptedException {
		testMerge("message/changeLeftTarget");
	}

	@Test
	public void actionExecutionSpecification_changeRightTarget_merge() throws IOException,
			InterruptedException {
		testMerge("message/changeRightTarget");
	}
}

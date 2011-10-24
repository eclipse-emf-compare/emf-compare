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

public class TestUseCaseDiagram extends AbstractUMLCompareTest {

	private static final String DIAGRAM_KIND_PATH = "/diagrams/usecase/"; //$NON-NLS-1$

	@Test
	public void extend_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("extend/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void extend_changeRightTarget() throws IOException, InterruptedException {
		testCompare("extend/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void extend_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("extend/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void extend_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("extend/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void include_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("include/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void include_changeRightTarget() throws IOException, InterruptedException {
		testCompare("include/changeRightTarget"); //$NON-NLS-1$
	}

	@Test
	public void include_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("include/changeLeftTarget"); //$NON-NLS-1$
	}

	@Test
	public void include_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("include/changeRightTarget"); //$NON-NLS-1$
	}

	@Override
	protected String getDiagramKindPath() {
		return DIAGRAM_KIND_PATH;
	}

}

/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.context;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.context.PapyrusContextUtil;
import org.eclipse.emf.compare.diagram.papyrus.tests.AbstractTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

public class PapyrusContextUtilTest extends AbstractTest {

	private PapyrusContextUtilInputData input = new PapyrusContextUtilInputData();

	@Test
	public void testPapyrusContext() throws IOException {
		final Resource left = input.getPapyrusLeft();
		final Resource right = input.getPapyrusRight();

		Comparison comparison = buildComparison(left, right);
		assertTrue(PapyrusContextUtil.isPapyrusContext(comparison));
	}

	@Test
	public void testEcoreContext() throws IOException {
		final Resource left = input.getEcoreLeft();
		final Resource right = input.getEcoreRight();

		Comparison comparison = buildComparison(left, right);
		assertFalse(PapyrusContextUtil.isPapyrusContext(comparison));
	}

	@Override
	protected DiagramInputData getInput() {
		return input;
	}

}

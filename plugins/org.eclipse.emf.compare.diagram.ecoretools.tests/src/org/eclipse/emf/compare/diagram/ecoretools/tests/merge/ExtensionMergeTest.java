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
package org.eclipse.emf.compare.diagram.ecoretools.tests.merge;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.diagram.ecoretools.tests.AbstractTest;
import org.eclipse.emf.compare.diagram.ecoretools.tests.DiagramInputData;
import org.eclipse.emf.compare.diagram.ecoretools.tests.edgechanges.data.EdgeChangesInputData;
import org.eclipse.emf.compare.extension.merge.DefaultMerger;
import org.eclipse.emf.compare.extension.merge.IMerger;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings("nls")
public class ExtensionMergeTest extends AbstractTest {
	private EdgeChangesInputData input = new EdgeChangesInputData();

	@Test
	public void testInstantiationMerger() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		testMergeRightToLeft(left, right, null);
	}

	
	protected void testMergeRightToLeft(Notifier left, Notifier right, Notifier origin) {
		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, origin);
		final Comparison comparisonBefore = getCompare().compare(scope);
		EList<Diff> differences = comparisonBefore.getDifferences();
		for (Diff diff : differences) {
			IMerger merger = org.eclipse.emf.compare.tests.merge.ExtensionMergeTest.getMerger(diff,
					new ArrayList<IMerger>(), DefaultMerger.class);
			merger.copyRightToLeft(diff, null);
		}
		final Comparison comparisonAfter = getCompare().compare(scope);
		assertTrue("Comparison#getDifferences() must be empty after copyAllRightToLeft", comparisonAfter
				.getDifferences().isEmpty());
	}

	@Override
	protected DiagramInputData getInput() {
		return input;
	}

}

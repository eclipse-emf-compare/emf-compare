/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ecoretools.tests.merge;

import java.io.IOException;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diagram.ecoretools.tests.AbstractTest;
import org.eclipse.emf.compare.diagram.ecoretools.tests.DiagramInputData;
import org.eclipse.emf.compare.diagram.ecoretools.tests.edgechanges.data.EdgeChangesInputData;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.diagram.internal.merge.CompareDiagramMerger;
import org.eclipse.emf.compare.merge.AttributeChangeMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.ReferenceChangeMerger;
import org.eclipse.emf.compare.merge.ResourceAttachmentChangeMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

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
		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparisonBefore = getCompare().compare(scope);
		EList<Diff> differences = comparisonBefore.getDifferences();
		final IMerger.Registry registry = IMerger.RegistryImpl.createStandaloneInstance();
		final IMerger diagramMerger = new CompareDiagramMerger();
		diagramMerger.setRanking(11);
		registry.add(diagramMerger);
		for (Diff diff : differences) {
			final Class<? extends IMerger> expectedMerger;
			if (diff instanceof DiagramDiff) {
				expectedMerger = CompareDiagramMerger.class;
			} else if (diff instanceof AttributeChange) {
				expectedMerger = AttributeChangeMerger.class;
			} else if (diff instanceof ReferenceChange) {
				expectedMerger = ReferenceChangeMerger.class;
			} else {
				expectedMerger = ResourceAttachmentChangeMerger.class;
			}
			IMerger merger = org.eclipse.emf.compare.tests.merge.ExtensionMergeTest.getMerger(registry, diff,
					expectedMerger);
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

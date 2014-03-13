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
package org.eclipse.emf.compare.uml2.tests.merge;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.merge.AttributeChangeMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.ReferenceChangeMerger;
import org.eclipse.emf.compare.merge.ResourceAttachmentChangeMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.compare.uml2.internal.merge.UMLMerger;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLTest;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.association.data.AssociationInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings("nls")
public class ExtensionMergeTest extends AbstractUMLTest {
	private AssociationInputData input = new AssociationInputData();

	@Test
	public void testInstantiationMerger() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		testMergeRightToLeft(left, right, null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.tests.AbstractUMLTest#testMergeRightToLeft(org.eclipse.emf.common.notify.Notifier,
	 *      org.eclipse.emf.common.notify.Notifier, org.eclipse.emf.common.notify.Notifier)
	 */
	@Override
	protected void testMergeRightToLeft(Notifier left, Notifier right, Notifier origin) {
		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparisonBefore = getCompare().compare(scope);
		EList<Diff> differences = comparisonBefore.getDifferences();
		final IMerger.Registry registry = IMerger.RegistryImpl.createStandaloneInstance();
		final IMerger umlMerger = new UMLMerger();
		umlMerger.setRanking(11);
		registry.add(umlMerger);
		for (Diff diff : differences) {
			final Class<? extends IMerger> expectedMerger;
			if (diff instanceof UMLDiff) {
				expectedMerger = UMLMerger.class;
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
	protected AbstractUMLInputData getInput() {
		return input;
	}

}

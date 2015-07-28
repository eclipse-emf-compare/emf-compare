/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.uriattachment;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.diagram.papyrus.tests.AbstractTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.compare.diagram.papyrus.tests.uriattachment.data.URIAttachmentData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

public class URIAttachmentTest extends AbstractTest {

	URIAttachmentData input = new URIAttachmentData();

	@Override
	protected DiagramInputData getInput() {
		return input;
	}

	/**
	 * Checks whether the EqualityHelper correctly determines equality regarding URI attachments. Setup:
	 * <ul>
	 * <li>origin/attribute -> uri=href:attribute?description?</li>
	 * <li>left/attribute -> uri=href:attribute</li>
	 * <li>right/attribute removed</li>
	 * </ul>
	 * Since the only difference between origin/attribute and left/attribute is the URI type description,
	 * they should be considered equal. If this is not the case the comparison will result in conflicts.
	 */
	@Test
	public void testA1() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();
		final Resource origin = input.getA1Origin();

		Comparison comparison = buildComparison(left, right, origin);

		assertEquals(0, comparison.getConflicts().size());
	}

}

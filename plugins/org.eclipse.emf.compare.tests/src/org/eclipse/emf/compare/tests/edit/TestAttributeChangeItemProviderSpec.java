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
package org.eclipse.emf.compare.tests.edit;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterators.filter;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.tests.edit.data.ecore.a1.EcoreA1InputData;
import org.junit.Test;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TestAttributeChangeItemProviderSpec extends AbstractTestCompareItemProviderAdapter {

	@Test
	public void testGetChildren_EcoreA1() throws IOException {
		Comparison comparison = getComparison(new EcoreA1InputData());

		List<AttributeChange> eAllContent_AttributeChange = newArrayList(
				filter(comparison.eAllContents(), AttributeChange.class));
		List<AttributeChange> eAllChildren_AttributeChange = newArrayList(
				filter(eAllChildren(comparison), AttributeChange.class));

		assertEquals(eAllContent_AttributeChange.size(), eAllChildren_AttributeChange.size());
		assertTrue(eAllChildren_AttributeChange.containsAll(eAllContent_AttributeChange));
	}
}

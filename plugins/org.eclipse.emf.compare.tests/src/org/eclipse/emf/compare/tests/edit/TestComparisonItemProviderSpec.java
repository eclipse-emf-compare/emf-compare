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
import static com.google.common.collect.Iterables.size;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.provider.ComparisonItemProvider;
import org.eclipse.emf.compare.provider.spec.ComparisonItemProviderSpec;
import org.eclipse.emf.compare.tests.edit.data.ecore.a1.EcoreA1InputData;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TestComparisonItemProviderSpec extends AbstractTestCompareItemProviderAdapter {

	private ComparisonItemProvider itemProvider;

	@Override
	@Before
	public void before() throws IOException {
		super.before();
		itemProvider = (ComparisonItemProviderSpec)compareItemProviderAdapterFactory
				.createComparisonAdapter();
	}

	@Test
	public void testGetChildren_EcoreA1() throws IOException {
		Comparison comparison = getComparison(new EcoreA1InputData());

		Collection<?> children = itemProvider.getChildren(comparison);

		assertEquals(7, children.size());
		assertEquals(1, size(filter(children, Match.class)));
		assertEquals(1, size(filter(children, MatchResource.class)));
	}
}

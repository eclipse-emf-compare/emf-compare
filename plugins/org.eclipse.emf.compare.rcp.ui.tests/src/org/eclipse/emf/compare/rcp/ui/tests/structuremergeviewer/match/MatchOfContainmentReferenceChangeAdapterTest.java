/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.match;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.match.MatchOfContainmentReferenceChangeAdapter;
import org.eclipse.emf.compare.rcp.internal.extension.impl.EMFCompareBuilderConfigurator;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.match.MatchOfContainmentReferenceChangeProcessor;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.match.data.MatchOfContainmentReferenceChangeAdapterTestData;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

/**
 * Tests for {@link MatchOfContainmentReferenceChangeAdapter}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("restriction")
public class MatchOfContainmentReferenceChangeAdapterTest {

	private static MatchOfContainmentReferenceChangeAdapterTestData inputData = new MatchOfContainmentReferenceChangeAdapterTestData();

	@Test
	public void test2wayAddNewStringTypedEReference() throws IOException {
		final Resource leftResource = inputData.getLeft();
		final Resource rightResource = inputData.getRight();
		final IComparisonScope scope = new DefaultComparisonScope(leftResource, rightResource, null);
		final Builder comparisonBuilder = EMFCompare.builder();
		EMFCompareBuilderConfigurator.createDefault().configure(comparisonBuilder);
		final Comparison comparison = comparisonBuilder.build().compare(scope);

		new MatchOfContainmentReferenceChangeProcessor().execute(comparison);

		for (Match match : comparison.getMatches()) {
			Adapter adapter = EcoreUtil.getAdapter(match.eAdapters(),
					MatchOfContainmentReferenceChangeAdapter.class);
			assertNull(adapter);
			for (Match subMatch : match.getAllSubmatches()) {
				adapter = EcoreUtil.getAdapter(subMatch.eAdapters(),
						MatchOfContainmentReferenceChangeAdapter.class);
				// The only match with a MatchOfContainmentReferenceChangeAdapter is the match on title
				EObject left = subMatch.getLeft();
				if (left instanceof EStructuralFeature
						&& ((EStructuralFeature)left).getName().equals("title")) {
					assertNotNull(adapter);
				} else {
					assertNull(adapter);
				}
			}
		}
	}
}

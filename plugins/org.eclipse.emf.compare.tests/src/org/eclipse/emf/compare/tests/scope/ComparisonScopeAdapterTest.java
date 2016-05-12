/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.scope;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope2;
import org.eclipse.emf.compare.tests.fullcomparison.data.identifier.IdentifierMatchInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

/**
 * This tests whether we can obtain the {@link IComparisonScope2} from a {@link Comparison}.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class ComparisonScopeAdapterTest {

	private static final URI TEST_URI_1 = URI.createURI("test_uri_1"); //$NON-NLS-1$

	private static final URI TEST_URI_2 = URI.createURI("test_uri_2"); //$NON-NLS-1$

	@Test
	public void testAccessingScopeFromComparison() throws IOException {
		final IdentifierMatchInputData mockModel = new IdentifierMatchInputData();
		final Resource left = mockModel.getExtlibraryLeft();
		final Resource right = mockModel.getExtlibraryRight();
		final Resource origin = mockModel.getExtlibraryOrigin();

		final IComparisonScope2 scope = new DefaultComparisonScope(left, right, origin);
		scope.getAllInvolvedResourceURIs().add(TEST_URI_1);
		scope.getAllInvolvedResourceURIs().add(TEST_URI_2);

		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		final IComparisonScope2 comparisonScope = adaptToComparisonScope(comparison);
		assertNotNull(comparisonScope);
		assertTrue(comparisonScope.getAllInvolvedResourceURIs().contains(TEST_URI_1));
		assertTrue(comparisonScope.getAllInvolvedResourceURIs().contains(TEST_URI_2));
	}

	private IComparisonScope2 adaptToComparisonScope(final Comparison comparison) {
		return (IComparisonScope2)EcoreUtil.getAdapter(comparison.eAdapters(), IComparisonScope2.class);
	}

}

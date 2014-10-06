/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.ide.ui.tests.logical;

import static org.eclipse.emf.compare.uml2.ide.tests.util.ProfileTestUtil.BASE_URI;
import static org.eclipse.emf.compare.uml2.ide.tests.util.ProfileTestUtil.createStorageTraversal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.ide.ui.internal.logical.ComparisonScopeBuilder;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.Test;

/**
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class ComparisonScopeBuilderTest {

	/**
	 * Tests that the scope will not filter the profile resource if it has been referenced using pathmap or
	 * plugin:/platform/ uris.
	 */
	@Test
	public void testSynchronizationModelWithPathMap() {
		String umlModelPath = BASE_URI + "pathmap/model.uml"; //$NON-NLS-1$
		String profilePath = BASE_URI + "model.profile.uml"; //$NON-NLS-1$
		StorageTraversal leftTraversal = createStorageTraversal(umlModelPath, profilePath);
		StorageTraversal rightTraversal = createStorageTraversal(umlModelPath, profilePath);

		SynchronizationModel syncModel = new SynchronizationModel(leftTraversal, rightTraversal, null);
		IComparisonScope scope = ComparisonScopeBuilder.create(syncModel, new NullProgressMonitor());
		assertTrue(scope.getLeft() instanceof ResourceSet);
		Iterator<? extends Resource> coveredLeftResourcesIte = scope.getCoveredResources((ResourceSet)scope
				.getLeft());
		List<Resource> coveredLeftResources = Lists.newArrayList(coveredLeftResourcesIte);
		assertEquals(2, coveredLeftResources.size());
	}
}

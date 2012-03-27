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
package org.eclipse.emf.compare.tests.scope;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.model.mock.MockCompareModel;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

/**
 * This class will allow us to test the behavior of the
 * {@link DefaultComparisonScope}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class DefaultComparisonScopeTest {
	@Test
	public void testGetRoots() throws IOException {
		final IComparisonScope nullScope = createNullScope();
		assertNull(nullScope.getLeft());
		assertNull(nullScope.getRight());
		assertNull(nullScope.getOrigin());

		// These are only getters, they should return us the unchanged object
		final MockCompareModel mockModel = new MockCompareModel();
		final Resource leftResource = mockModel.getLeftModel();
		final Resource rightResource = mockModel.getRightModel();
		final Resource originResource = mockModel.getOriginModel();

		assertNotNull(leftResource);
		assertNotNull(rightResource);
		assertNotNull(originResource);

		final Iterator<EObject> leftContent = EcoreUtil.getAllProperContents(
				leftResource, false);
		final Iterator<EObject> rightContent = EcoreUtil.getAllProperContents(
				leftResource, false);
		final Iterator<EObject> originContent = EcoreUtil.getAllProperContents(
				leftResource, false);

		while (leftContent.hasNext() && rightContent.hasNext()
				&& originContent.hasNext()) {
			final EObject left = leftContent.next();
			final EObject right = rightContent.next();
			final EObject origin = originContent.next();
			final IComparisonScope eObjectScope = new DefaultComparisonScope(
					left, right, origin);

			assertSame(left, eObjectScope.getLeft());
			assertSame(right, eObjectScope.getRight());
			assertSame(origin, eObjectScope.getOrigin());
		}

		final IComparisonScope resourceScope = new DefaultComparisonScope(
				leftResource, rightResource, originResource);
		assertSame(leftResource, resourceScope.getLeft());
		assertSame(rightResource, resourceScope.getRight());
		assertSame(originResource, resourceScope.getOrigin());

		final ResourceSet leftRS = newResourceSet(leftResource);
		final ResourceSet rightRS = newResourceSet(rightResource);
		final ResourceSet originRS = newResourceSet(originResource);

		final IComparisonScope resourceSetScope = new DefaultComparisonScope(
				leftRS, rightRS, originRS);
		assertSame(leftRS, resourceSetScope.getLeft());
		assertSame(rightRS, resourceSetScope.getRight());
		assertSame(originRS, resourceSetScope.getOrigin());
	}

	@Test
	public void testGetChildrenForNull() {
		final IComparisonScope nullScope = createNullScope();

		assertFalse(nullScope.getChildren((EObject) null).hasNext());
		assertFalse(nullScope.getChildren((Resource) null).hasNext());
		assertFalse(nullScope.getChildren((ResourceSet) null).hasNext());
	}

	@Test
	public void testGetEObjectCHildren() throws IOException {
		final IComparisonScope resourceScope = createResourceScope();

		assertTrue(resourceScope.getLeft() instanceof Resource);
		assertTrue(resourceScope.getRight() instanceof Resource);
		assertTrue(resourceScope.getOrigin() instanceof Resource);

		final Resource leftResource = (Resource) resourceScope.getLeft();
		final Resource rightResource = (Resource) resourceScope.getRight();
		final Resource originResource = (Resource) resourceScope.getOrigin();

		final Iterator<EObject> leftContent = EcoreUtil.getAllProperContents(
				leftResource, false);
		final Iterator<EObject> rightContent = EcoreUtil.getAllProperContents(
				rightResource, false);
		final Iterator<EObject> originContent = EcoreUtil.getAllProperContents(
				originResource, false);
		final Iterator<EObject> allEObjects = Iterators.concat(leftContent,
				rightContent, originContent);

		boolean empty = true;
		while (allEObjects.hasNext()) {
			empty = false;
			final EObject root = allEObjects.next();

			final Iterator<? extends EObject> scopeChildren = resourceScope
					.getChildren(root);
			final List<EObject> children = Lists
					.newArrayList(Iterators.filter(
							EcoreUtil.getAllProperContents(root, false),
							EObject.class));

			while (scopeChildren.hasNext()) {
				assertTrue(children.remove(scopeChildren.next()));
			}
			// We want the default scope to avoid all EGenericTypes
			for (EObject outOfScope : children) {
				assertTrue(outOfScope instanceof EGenericType);
			}
		}
		assertFalse(empty);
	}

	@Test
	public void testGetResourceChildren() throws IOException {
		final IComparisonScope resourceScope = createResourceScope();

		assertTrue(resourceScope.getLeft() instanceof Resource);
		assertTrue(resourceScope.getRight() instanceof Resource);
		assertTrue(resourceScope.getOrigin() instanceof Resource);

		final Resource leftResource = (Resource) resourceScope.getLeft();
		final Resource rightResource = (Resource) resourceScope.getRight();
		final Resource originResource = (Resource) resourceScope.getOrigin();

		final Iterator<? extends EObject> scopeLeftChildren = resourceScope
				.getChildren(leftResource);
		final List<EObject> leftChildren = Lists.newArrayList(Iterators.filter(
				EcoreUtil.getAllProperContents(leftResource, false),
				EObject.class));
		while (scopeLeftChildren.hasNext()) {
			assertTrue(leftChildren.remove(scopeLeftChildren.next()));
		}
		// We want the default scope to avoid all EGenericTypes
		for (EObject outOfScope : leftChildren) {
			assertTrue(outOfScope instanceof EGenericType);
		}

		final Iterator<? extends EObject> scopeRightChildren = resourceScope
				.getChildren(rightResource);
		final List<EObject> rightChildren = Lists.newArrayList(Iterators
				.filter(EcoreUtil.getAllProperContents(rightResource, false),
						EObject.class));
		while (scopeRightChildren.hasNext()) {
			assertTrue(rightChildren.remove(scopeRightChildren.next()));
		}
		// We want the default scope to avoid all EGenericTypes
		for (EObject outOfScope : rightChildren) {
			assertTrue(outOfScope instanceof EGenericType);
		}

		final Iterator<? extends EObject> scopeOriginChildren = resourceScope
				.getChildren(originResource);
		final List<EObject> originChildren = Lists.newArrayList(Iterators
				.filter(EcoreUtil.getAllProperContents(originResource, false),
						EObject.class));
		while (scopeOriginChildren.hasNext()) {
			assertTrue(originChildren.remove(scopeOriginChildren.next()));
		}
		// We want the default scope to avoid all EGenericTypes
		for (EObject outOfScope : originChildren) {
			assertTrue(outOfScope instanceof EGenericType);
		}
	}

	@Test
	public void testGetResourceSetChildren() throws IOException {
		final IComparisonScope resourceScope = createResourceSetScope();

		assertTrue(resourceScope.getLeft() instanceof ResourceSet);
		assertTrue(resourceScope.getRight() instanceof ResourceSet);
		assertTrue(resourceScope.getOrigin() instanceof ResourceSet);

		final ResourceSet leftResourceSet = (ResourceSet) resourceScope
				.getLeft();
		final ResourceSet rightResourceSet = (ResourceSet) resourceScope
				.getRight();
		final ResourceSet originResourceSet = (ResourceSet) resourceScope
				.getOrigin();

		final Iterator<? extends Resource> scopeLeftChildren = resourceScope
				.getChildren(leftResourceSet);
		final List<Resource> leftChildren = Lists.newArrayList(leftResourceSet
				.getResources());
		while (scopeLeftChildren.hasNext()) {
			Resource child = scopeLeftChildren.next();

			assertTrue(leftChildren.remove(child));
		}
		assertTrue(leftChildren.isEmpty());

		final Iterator<? extends Resource> scopeRightChildren = resourceScope
				.getChildren(rightResourceSet);
		final List<Resource> rightChildren = Lists
				.newArrayList(rightResourceSet.getResources());
		while (scopeRightChildren.hasNext()) {
			Resource child = scopeRightChildren.next();

			assertTrue(rightChildren.remove(child));
		}
		assertTrue(rightChildren.isEmpty());

		final Iterator<? extends Resource> scopeOriginChildren = resourceScope
				.getChildren(originResourceSet);
		final List<Resource> originChildren = Lists
				.newArrayList(originResourceSet.getResources());
		while (scopeOriginChildren.hasNext()) {
			Resource child = scopeOriginChildren.next();

			assertTrue(originChildren.remove(child));
		}
		assertTrue(originChildren.isEmpty());
	}

	private IComparisonScope createNullScope() {
		return new DefaultComparisonScope(null, null, null);
	}

	private IComparisonScope createResourceScope() throws IOException {
		final MockCompareModel mockModel = new MockCompareModel();
		final Resource leftResource = mockModel.getLeftModel();
		final Resource rightResource = mockModel.getRightModel();
		final Resource originResource = mockModel.getOriginModel();

		assertNotNull(leftResource);
		assertNotNull(rightResource);
		assertNotNull(originResource);

		return new DefaultComparisonScope(leftResource, rightResource,
				originResource);
	}

	private IComparisonScope createResourceSetScope() throws IOException {
		final MockCompareModel mockModel = new MockCompareModel();
		final Resource leftResource = mockModel.getLeftModel();
		final Resource rightResource = mockModel.getRightModel();
		final Resource originResource = mockModel.getOriginModel();

		assertNotNull(leftResource);
		assertNotNull(rightResource);
		assertNotNull(originResource);

		final ResourceSet leftRS = newResourceSet(leftResource);
		final ResourceSet rightRS = newResourceSet(rightResource);
		final ResourceSet originRS = newResourceSet(originResource);

		return new DefaultComparisonScope(leftRS, rightRS, originRS);
	}

	private ResourceSet newResourceSet(Resource... resources) {
		final ResourceSet resourceSet = new ResourceSetImpl();
		for (int i = 0; i < resources.length; i++) {
			resourceSet.getResources().add(resources[i]);
		}
		return resourceSet;
	}
}

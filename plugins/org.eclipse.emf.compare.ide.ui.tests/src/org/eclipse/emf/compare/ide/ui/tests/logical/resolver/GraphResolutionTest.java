/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.logical.resolver;

import static org.junit.Assert.assertTrue;

import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.ComparisonScopeBuilder;
import org.eclipse.emf.compare.ide.ui.internal.logical.IdenticalResourceMinimizer;
import org.eclipse.emf.compare.ide.ui.internal.logical.StorageTypedElement;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ThreadedModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.ide.ui.tests.CompareTestCase;
import org.eclipse.emf.compare.internal.utils.ReadOnlyGraph;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.junit.Test;

public class GraphResolutionTest extends CompareTestCase {
	private IFile leftR1;

	private IFile leftR2;

	private IFile leftR3;

	private IFile leftR4;

	private IFile rightR1;

	private IFile rightR2;

	private IFile rightR3;

	private IFile rightR4;

	private IFile originR1;

	private IFile originR2;

	private IFile originR3;

	private IFile originR4;

	@Override
	public void setUp() throws Exception {
		super.setUp();

		leftR1 = project.createFile("left/R1.ecore", getClass().getResource("data/left/R1.ecore")
				.openStream());
		leftR2 = project.createFile("left/R2.ecore", getClass().getResource("data/left/R2.ecore")
				.openStream());
		leftR3 = project.createFile("left/R3.ecore", getClass().getResource("data/left/R3.ecore")
				.openStream());
		leftR4 = project.createFile("left/R4.ecore", getClass().getResource("data/left/R4.ecore")
				.openStream());

		rightR1 = project.createFile("right/R1.ecore", getClass().getResource("data/right/R1.ecore")
				.openStream());
		rightR2 = project.createFile("right/R2.ecore", getClass().getResource("data/right/R2.ecore")
				.openStream());
		rightR3 = project.createFile("right/R3.ecore", getClass().getResource("data/right/R3.ecore")
				.openStream());
		rightR4 = project.createFile("right/R4.ecore", getClass().getResource("data/right/R4.ecore")
				.openStream());

		originR1 = project.createFile("origin/R1.ecore", getClass().getResource("data/origin/R1.ecore")
				.openStream());
		originR2 = project.createFile("origin/R2.ecore", getClass().getResource("data/origin/R2.ecore")
				.openStream());
		originR3 = project.createFile("origin/R3.ecore", getClass().getResource("data/origin/R3.ecore")
				.openStream());
		originR4 = project.createFile("origin/R4.ecore", getClass().getResource("data/origin/R4.ecore")
				.openStream());
	}

	@SuppressWarnings("restriction")
	@Test
	public void testComparison() throws Exception {
		ITypedElement leftTE = new StorageTypedElement(leftR1, leftR1.getFullPath().toOSString());
		ITypedElement rightTE = new StorageTypedElement(rightR1, rightR1.getFullPath().toOSString());
		ITypedElement originTE = new StorageTypedElement(originR1, originR1.getFullPath().toOSString());

		final IModelResolver resolver = EMFCompareIDEUIPlugin.getDefault().getModelResolverRegistry()
				.getBestResolverFor(leftR1);
		assertTrue(resolver instanceof ThreadedModelResolver);

		final ComparisonScopeBuilder scopeBuilder = new ComparisonScopeBuilder(resolver,
				new IdenticalResourceMinimizer(), null);
		final IComparisonScope scope = scopeBuilder.build(leftTE, rightTE, originTE,
				new NullProgressMonitor());
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		ReadOnlyGraph<URI> graph = ((ThreadedModelResolver)resolver).getDependencyGraph();

		assertTrue(graph.getDirectParents(getURI(leftR4)).contains(getURI(leftR3)));
		assertTrue(graph.getDirectParents(getURI(leftR3)).contains(getURI(leftR2)));
		assertTrue(graph.getDirectParents(getURI(leftR2)).contains(getURI(leftR1)));
		assertTrue(graph.getDirectParents(getURI(leftR1)).isEmpty());

		assertTrue(graph.getDirectParents(getURI(rightR4)).contains(getURI(rightR3)));
		assertTrue(graph.getDirectParents(getURI(rightR3)).contains(getURI(rightR2)));
		assertTrue(graph.getDirectParents(getURI(rightR2)).contains(getURI(rightR1)));
		assertTrue(graph.getDirectParents(getURI(rightR1)).isEmpty());

		assertTrue(graph.getDirectParents(getURI(originR4)).contains(getURI(originR3)));
		assertTrue(graph.getDirectParents(getURI(originR3)).contains(getURI(originR2)));
		assertTrue(graph.getDirectParents(getURI(originR2)).contains(getURI(originR1)));
		assertTrue(graph.getDirectParents(getURI(originR1)).isEmpty());

		assertTrue(graph.hasChild(getURI(leftR1), getURI(leftR2)));
		assertTrue(graph.hasChild(getURI(leftR2), getURI(leftR3)));
		assertTrue(graph.hasChild(getURI(leftR3), getURI(leftR4)));

		assertTrue(graph.hasChild(getURI(rightR1), getURI(rightR2)));
		assertTrue(graph.hasChild(getURI(rightR2), getURI(rightR3)));
		assertTrue(graph.hasChild(getURI(rightR3), getURI(rightR4)));

		assertTrue(graph.hasChild(getURI(originR1), getURI(originR2)));
		assertTrue(graph.hasChild(getURI(originR2), getURI(originR3)));
		assertTrue(graph.hasChild(getURI(originR3), getURI(originR4)));
	}

	@SuppressWarnings("restriction")
	@Test
	public void testModelResolver() throws Exception {
		ThreadedModelResolver resolver = new ThreadedModelResolver();
		resolver.initialize();
		resolver.resolveLocalModels(leftR1, rightR1, originR1, new NullProgressMonitor());
		ReadOnlyGraph<URI> graph = resolver.getDependencyGraph();

		assertTrue(graph.getDirectParents(getURI(leftR4)).contains(getURI(leftR3)));
		assertTrue(graph.getDirectParents(getURI(leftR3)).contains(getURI(leftR2)));
		assertTrue(graph.getDirectParents(getURI(leftR2)).contains(getURI(leftR1)));
		assertTrue(graph.getDirectParents(getURI(leftR1)).isEmpty());

		assertTrue(graph.getDirectParents(getURI(rightR4)).contains(getURI(rightR3)));
		assertTrue(graph.getDirectParents(getURI(rightR3)).contains(getURI(rightR2)));
		assertTrue(graph.getDirectParents(getURI(rightR2)).contains(getURI(rightR1)));
		assertTrue(graph.getDirectParents(getURI(rightR1)).isEmpty());

		assertTrue(graph.getDirectParents(getURI(originR4)).contains(getURI(originR3)));
		assertTrue(graph.getDirectParents(getURI(originR3)).contains(getURI(originR2)));
		assertTrue(graph.getDirectParents(getURI(originR2)).contains(getURI(originR1)));
		assertTrue(graph.getDirectParents(getURI(originR1)).isEmpty());

		assertTrue(graph.hasChild(getURI(leftR1), getURI(leftR2)));
		assertTrue(graph.hasChild(getURI(leftR2), getURI(leftR3)));
		assertTrue(graph.hasChild(getURI(leftR3), getURI(leftR4)));

		assertTrue(graph.hasChild(getURI(rightR1), getURI(rightR2)));
		assertTrue(graph.hasChild(getURI(rightR2), getURI(rightR3)));
		assertTrue(graph.hasChild(getURI(rightR3), getURI(rightR4)));

		assertTrue(graph.hasChild(getURI(originR1), getURI(originR2)));
		assertTrue(graph.hasChild(getURI(originR2), getURI(originR3)));
		assertTrue(graph.hasChild(getURI(originR3), getURI(originR4)));
	}

	private URI getURI(IFile file) {
		// trim the leading '/'
		return URI.createPlatformResourceURI(file.getFullPath().toString().substring(1), true);
	}
}

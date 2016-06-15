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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.graph.IGraphView;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.ComparisonScopeBuilder;
import org.eclipse.emf.compare.ide.ui.internal.logical.IdenticalResourceMinimizer;
import org.eclipse.emf.compare.ide.ui.internal.logical.StorageTypedElement;
import org.eclipse.emf.compare.ide.ui.internal.logical.StreamAccessorStorage;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ThreadedModelResolver;
import org.eclipse.emf.compare.ide.ui.internal.util.PlatformElementUtil;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.junit.Test;
import org.osgi.framework.Bundle;

@SuppressWarnings({"restriction", "nls" })
public class ThreadedModelResolverGraphTest {

	private static String LEFT = "left";

	private static String RIGHT = "right";

	private static String SEP = File.separator;

	private static String URI_SEP = "/";

	private static String PLATFORM_RESOURCE = "platform:" + URI_SEP + "resource";

	private IGraphView<URI> initGraph(String projectName) throws IOException, CoreException {
		Bundle bundle = Platform.getBundle("org.eclipse.emf.compare.ide.ui.tests");
		URL entry = bundle.getEntry("src" + SEP + "org" + SEP + "eclipse" + SEP + "emf" + SEP + "compare"
				+ SEP + "ide" + SEP + "ui" + SEP + "tests" + SEP + "structuremergeviewer" + SEP
				+ "notloadedfragment" + SEP + "data" + SEP + projectName + SEP + ".project");
		URL fileURL = FileLocator.toFileURL(entry);
		IProjectDescription description = ResourcesPlugin.getWorkspace()
				.loadProjectDescription(new Path(fileURL.getPath()));
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
		if (!project.exists()) {
			project.create(description, new NullProgressMonitor());
			project.open(new NullProgressMonitor());
		}

		final IFile leftFile = project.getFile(new Path("left" + SEP + "R1.ecore"));
		final IFile rightFile = project.getFile(new Path("right" + SEP + "R1.ecore"));
		final ITypedElement left = new StorageTypedElement(leftFile, leftFile.getFullPath().toOSString());
		final ITypedElement right = new StorageTypedElement(rightFile, rightFile.getFullPath().toOSString());

		IStorage leftStorage = PlatformElementUtil.findFile(left);
		if (leftStorage == null) {
			leftStorage = StreamAccessorStorage.fromTypedElement(left);
		}
		final IModelResolver resolver = EMFCompareIDEUIPlugin.getDefault().getModelResolverRegistry()
				.getBestResolverFor(leftStorage);

		assertTrue(resolver instanceof ThreadedModelResolver);

		final ComparisonScopeBuilder scopeBuilder = new ComparisonScopeBuilder(resolver,
				new IdenticalResourceMinimizer(), null);
		scopeBuilder.build(left, right, null, new NullProgressMonitor());

		return ((ThreadedModelResolver)resolver).getGraphView();
	}

	@Test
	public void testCase0_GetParentData() throws IOException, CoreException {
		String projectName = "case0";
		final IGraphView<URI> graph = initGraph(projectName);
		// Left
		String leftPath = projectName + URI_SEP + LEFT + URI_SEP;
		URI parentData = graph
				.getParentData(URI.createPlatformResourceURI(leftPath + URI_SEP + "R1.ecore", false));
		assertNull(parentData);
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R2.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R1.ecore#_A", parentData.toString());
		// Right
		String rightPath = projectName + URI_SEP + RIGHT + URI_SEP;
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R1.ecore", false));
		assertNull(parentData);
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R2.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R1.ecore#_A", parentData.toString());
	}

	@Test
	public void testCase1_GetParentData() throws IOException, CoreException {
		String projectName = "case1";
		final IGraphView<URI> graph = initGraph(projectName);
		// Left
		String leftPath = projectName + URI_SEP + LEFT + URI_SEP;
		URI parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R1.ecore", false));
		assertNull(parentData);
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R2.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R3.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R2.ecore#_B", parentData.toString());
		// Right
		String rightPath = projectName + URI_SEP + RIGHT + URI_SEP;
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R1.ecore", false));
		assertNull(parentData);
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R2.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R3.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R2.ecore#_B", parentData.toString());
	}

	@Test
	public void testCase2_GetParentData() throws IOException, CoreException {
		String projectName = "case2";
		final IGraphView<URI> graph = initGraph(projectName);
		// Left
		String leftPath = projectName + URI_SEP + LEFT + URI_SEP;
		URI parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R1.ecore", false));
		assertNull(parentData);
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R2.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R3.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R2.ecore#_B", parentData.toString());
		// Right
		String rightPath = projectName + URI_SEP + RIGHT + URI_SEP;
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R1.ecore", false));
		assertNull(parentData);
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R2.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R3.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R2.ecore#_B", parentData.toString());
	}

	@Test
	public void testCase3_GetParentData() throws IOException, CoreException {
		String projectName = "case3";
		final IGraphView<URI> graph = initGraph(projectName);
		// Left
		String leftPath = projectName + URI_SEP + LEFT + URI_SEP;
		URI parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R1.ecore", false));
		assertNull(parentData);
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R2.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R3.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R2.ecore#_B", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R4.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R3.ecore#_D", parentData.toString());
		// Right
		String rightPath = projectName + URI_SEP + RIGHT + URI_SEP;
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R1.ecore", false));
		assertNull(parentData);
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R2.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R3.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R2.ecore#_B", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R4.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R3.ecore#_D", parentData.toString());
	}

	@Test
	public void testCase4_GetParentData() throws IOException, CoreException {
		String projectName = "case4";
		final IGraphView<URI> graph = initGraph(projectName);
		// Left
		String leftPath = projectName + URI_SEP + LEFT + URI_SEP;
		URI parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R1.ecore", false));
		assertNull(parentData);
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R2.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R3.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R1.ecore#_A", parentData.toString());
		// Right
		String rightPath = projectName + URI_SEP + RIGHT + URI_SEP;
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R1.ecore", false));
		assertNull(parentData);
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R2.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R3.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R1.ecore#_A", parentData.toString());
	}

	@Test
	public void testCase5_GetParentData() throws IOException, CoreException {
		String projectName = "case5";
		final IGraphView<URI> graph = initGraph(projectName);
		// Left
		String leftPath = projectName + URI_SEP + LEFT + URI_SEP;
		URI parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R1.ecore", false));
		assertNull(parentData);
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R2.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R3.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R4.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R2.ecore#_B", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R5.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R3.ecore#_C", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R6.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R4.ecore#_D", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R7.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R5.ecore#_E", parentData.toString());
		// Right
		String rightPath = projectName + URI_SEP + RIGHT + URI_SEP;
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R1.ecore", false));
		assertNull(parentData);
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R2.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R3.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R4.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R2.ecore#_B", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R5.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R3.ecore#_C", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R6.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R4.ecore#_D", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R7.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R5.ecore#_E", parentData.toString());
	}

	@Test
	public void testCase6_GetParentData() throws IOException, CoreException {
		String projectName = "case6";
		final IGraphView<URI> graph = initGraph(projectName);
		// Left
		String leftPath = projectName + URI_SEP + LEFT + URI_SEP;
		URI parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R1.ecore", false));
		assertNull(parentData);
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R2.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R3.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R4.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R2.ecore#_B", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R5.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R4.ecore#_D", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R6.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R4.ecore#_D", parentData.toString());
		// Right
		String rightPath = projectName + URI_SEP + RIGHT + URI_SEP;
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R1.ecore", false));
		assertNull(parentData);
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R2.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R3.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R4.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R2.ecore#_B", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R5.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R4.ecore#_D", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R6.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R4.ecore#_D", parentData.toString());
	}

	@Test
	public void testCase7_GetParentData() throws IOException, CoreException {
		String projectName = "case7";
		final IGraphView<URI> graph = initGraph(projectName);
		// Left
		String leftPath = projectName + URI_SEP + LEFT + URI_SEP;
		URI parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R1.ecore", false));
		assertNull(parentData);
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R2.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R3.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R4.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R2.ecore#_B", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R5.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R4.ecore#_D", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(leftPath + "R6.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + leftPath + "R4.ecore#_D", parentData.toString());
		// Right
		String rightPath = projectName + URI_SEP + RIGHT + URI_SEP;
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R1.ecore", false));
		assertNull(parentData);
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R2.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R3.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R1.ecore#_A", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R4.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R2.ecore#_B", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R5.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R4.ecore#_D", parentData.toString());
		parentData = graph.getParentData(URI.createPlatformResourceURI(rightPath + "R6.ecore", false));
		assertEquals(PLATFORM_RESOURCE + URI_SEP + rightPath + "R4.ecore#_D", parentData.toString());
	}
}

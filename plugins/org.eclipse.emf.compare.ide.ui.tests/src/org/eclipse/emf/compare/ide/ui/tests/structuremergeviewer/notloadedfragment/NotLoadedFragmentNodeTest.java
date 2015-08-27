/*******************************************************************************
 * Copyright (c) 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - bug 475401
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.notloadedfragment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.compare.CompareConfiguration;
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
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.logical.ComparisonScopeBuilder;
import org.eclipse.emf.compare.ide.ui.internal.logical.IdenticalResourceMinimizer;
import org.eclipse.emf.compare.ide.ui.internal.logical.StorageTypedElement;
import org.eclipse.emf.compare.ide.ui.internal.logical.StreamAccessorStorage;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ThreadedModelResolver;
import org.eclipse.emf.compare.ide.ui.internal.util.PlatformElementUtil;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.internal.utils.ReadOnlyGraph;
import org.eclipse.emf.compare.match.impl.NotLoadedFragmentMatch;
import org.eclipse.emf.compare.provider.spec.CompareItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DefaultGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.ThreeWayComparisonGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.GroupItemProviderAdapter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.tree.TreeFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;

@SuppressWarnings({"restriction", "nls" })
public class NotLoadedFragmentNodeTest {

	private static final String RIGHT_SIDE = "Right Side";

	private static final String LEFT_SIDE = "Left Side";

	private static final String CONFLICTS = "Conflicts";

	private static final String ELLIPSIS = "[ ... ]";

	private static String SEP = File.separator;

	private static AdapterFactoryItemDelegator itemDelegator;

	@BeforeClass
	public static void beforeClass() {
		final Collection<AdapterFactory> factories = Lists.newArrayList();
		factories.add(new CompareItemProviderAdapterFactorySpec());
		factories.add(new TreeItemProviderAdapterFactorySpec());
		factories.add(new EcoreItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());

		final AdapterFactory composedAdapterFactory = new ComposedAdapterFactory(factories);
		itemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);
	}

	/**
	 * Function that retrieve the data of the given TreeNode.
	 */
	private static final Function<Object, EObject> TREE_NODE_DATA = new Function<Object, EObject>() {
		public EObject apply(Object node) {
			if (node instanceof TreeNode) {
				return ((TreeNode)node).getData();
			} else if (node instanceof EObject) {
				return (EObject)node;
			}
			return null;
		}
	};

	/**
	 * Predicate that returns true if the input is a TreeNode that holds a NotLoadedFragmentMatch.
	 */
	private static final Predicate<Object> NOT_LOADED_FRAGMENT = new Predicate<Object>() {
		public boolean apply(Object input) {
			EObject eObject = TREE_NODE_DATA.apply(input);
			if (eObject instanceof NotLoadedFragmentMatch) {
				return true;
			}
			return false;
		}
	};

	private Comparison initComparison(String projectName) throws IOException, CoreException {
		Bundle bundle = Platform.getBundle("org.eclipse.emf.compare.ide.ui.tests");
		URL entry = bundle.getEntry("src" + SEP + "org" + SEP + "eclipse" + SEP + "emf" + SEP + "compare"
				+ SEP + "ide" + SEP + "ui" + SEP + "tests" + SEP + "structuremergeviewer" + SEP
				+ "notloadedfragment" + SEP + "data" + SEP + projectName + SEP + ".project");
		URL fileURL = FileLocator.toFileURL(entry);
		IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(
				new Path(fileURL.getPath()));
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
		if (!project.exists()) {
			project.create(description, new NullProgressMonitor());
			project.open(new NullProgressMonitor());
		}

		final IFile leftFile = project.getFile(new Path("left" + SEP + "R1.ecore"));
		final IFile rightFile = project.getFile(new Path("right" + SEP + "R1.ecore"));
		final IFile originFile = project.getFile(new Path("origin" + SEP + "R1.ecore"));
		final ITypedElement left = new StorageTypedElement(leftFile, leftFile.getFullPath().toOSString());
		final ITypedElement right = new StorageTypedElement(rightFile, rightFile.getFullPath().toOSString());
		final ITypedElement origin = new StorageTypedElement(originFile, originFile.getFullPath()
				.toOSString());

		IStorage leftStorage = PlatformElementUtil.findFile(left);
		if (leftStorage == null) {
			leftStorage = StreamAccessorStorage.fromTypedElement(left);
		}
		final IModelResolver resolver = EMFCompareIDEUIPlugin.getDefault().getModelResolverRegistry()
				.getBestResolverFor(leftStorage);

		assertTrue(resolver instanceof ThreadedModelResolver);

		final ComparisonScopeBuilder scopeBuilder = new ComparisonScopeBuilder(resolver,
				new IdenticalResourceMinimizer(), null);
		final IComparisonScope scope = scopeBuilder.build(left, right, origin, new NullProgressMonitor());
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		ReadOnlyGraph<URI> graph = ((ThreadedModelResolver)resolver).getDependencyGraph();
		EMFCompareConfiguration emfCC = new EMFCompareConfiguration(new CompareConfiguration());
		emfCC.setResourcesGraph(graph);
		EMFCompareRCPUIPlugin.getDefault().setEMFCompareConfiguration(emfCC);

		return comparison;
	}

	/*
	 * Default Group Provider Tests
	 */

	@Test
	public void testCase0_DefaultGroupProvider() throws IOException, CoreException {
		final Comparison comparison = initComparison("case0");
		TreeNode groupTreeNode = TreeFactory.eINSTANCE.createTreeNode();
		groupTreeNode.setData(comparison);
		groupTreeNode.eAdapters().add(new DefaultGroupProvider());
		Collection<?> children = itemDelegator.getChildren(groupTreeNode);
		assertEquals(2, children.size());
		Iterable<?> nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		// 1st level of the Tree
		TreeNode rootTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(rootTreeNode));
		// 2nd level
		children = rootTreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeB = (TreeNode)children.iterator().next();
		assertEquals("B", itemDelegator.getText(treeNodeB));
	}

	@Test
	public void testCase1_DefaultGroupProvider() throws IOException, CoreException {
		final Comparison comparison = initComparison("case1");
		TreeNode groupTreeNode = TreeFactory.eINSTANCE.createTreeNode();
		groupTreeNode.setData(comparison);
		groupTreeNode.eAdapters().add(new DefaultGroupProvider());
		Collection<?> children = itemDelegator.getChildren(groupTreeNode);
		assertEquals(3, children.size());
		// 1st level of the Tree
		TreeNode rootTreeNode = (TreeNode)children.iterator().next();
		assertEquals("A", itemDelegator.getText(rootTreeNode));
		// 2nd level
		children = rootTreeNode.getChildren();
		assertEquals(2, children.size());
		Iterable<?> nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		TreeNode nlfTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(nlfTreeNode));
		// 3rd level
		children = nlfTreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeD = (TreeNode)children.iterator().next();
		assertEquals("D", itemDelegator.getText(treeNodeD));
	}

	@Test
	public void testCase2_DefaultGroupProvider() throws IOException, CoreException {
		final Comparison comparison = initComparison("case2");
		TreeNode groupTreeNode = TreeFactory.eINSTANCE.createTreeNode();
		groupTreeNode.setData(comparison);
		groupTreeNode.eAdapters().add(new DefaultGroupProvider());
		Collection<?> children = itemDelegator.getChildren(groupTreeNode);
		assertEquals(3, children.size());
		Iterable<?> nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		// 1st level of the Tree
		TreeNode rootTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(rootTreeNode));
		// 2nd level
		children = rootTreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeB = (TreeNode)children.iterator().next();
		assertEquals("B", itemDelegator.getText(treeNodeB));
	}

	@Test
	public void testCase3_DefaultGroupProvider() throws IOException, CoreException {
		final Comparison comparison = initComparison("case3");
		TreeNode groupTreeNode = TreeFactory.eINSTANCE.createTreeNode();
		groupTreeNode.setData(comparison);
		groupTreeNode.eAdapters().add(new DefaultGroupProvider());
		Collection<?> children = itemDelegator.getChildren(groupTreeNode);
		assertEquals(3, children.size());
		Iterable<?> nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		// 1st level of the Tree
		TreeNode rootTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(rootTreeNode));
		// 2nd level
		children = rootTreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode nodeB = (TreeNode)children.iterator().next();
		assertEquals("B", itemDelegator.getText(nodeB));
		// 3rd level
		children = nodeB.getChildren();
		assertEquals(2, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		TreeNode nlfcTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(nlfcTreeNode));
		// 4th level
		children = nlfcTreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode nodeE = (TreeNode)children.iterator().next();
		assertEquals("E", itemDelegator.getText(nodeE));
	}

	@Test
	public void testCase4_DefaultGroupProvider() throws IOException, CoreException {
		final Comparison comparison = initComparison("case4");
		TreeNode groupTreeNode = TreeFactory.eINSTANCE.createTreeNode();
		groupTreeNode.setData(comparison);
		groupTreeNode.eAdapters().add(new DefaultGroupProvider());
		Collection<?> children = itemDelegator.getChildren(groupTreeNode);
		assertEquals(3, children.size());
		Iterable<?> nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		// 1st level of the Tree
		TreeNode rootTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(rootTreeNode));
		// 2nd level
		children = rootTreeNode.getChildren();
		assertEquals(2, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(2, Iterables.size(nlfc));
		Iterator<?> nlfcIt = nlfc.iterator();
		TreeNode r2TreeNode = (TreeNode)nlfcIt.next();
		assertEquals(ELLIPSIS + " (R2.ecore)", itemDelegator.getText(r2TreeNode));
		// 3rd level
		children = r2TreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeB = (TreeNode)children.iterator().next();
		assertEquals("B", itemDelegator.getText(treeNodeB));
		// 2nd level
		TreeNode r3TreeNode = (TreeNode)nlfcIt.next();
		assertEquals(ELLIPSIS + " (R3.ecore)", itemDelegator.getText(r3TreeNode));
		// 3rd level
		children = r3TreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeC = (TreeNode)children.iterator().next();
		assertEquals("C", itemDelegator.getText(treeNodeC));
	}

	@Test
	public void testCase5_DefaultGroupProvider() throws IOException, CoreException {
		final Comparison comparison = initComparison("case5");
		TreeNode groupTreeNode = TreeFactory.eINSTANCE.createTreeNode();
		groupTreeNode.setData(comparison);
		groupTreeNode.eAdapters().add(new DefaultGroupProvider());
		Collection<?> children = itemDelegator.getChildren(groupTreeNode);
		assertEquals(3, children.size());
		Iterable<?> nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		// 1st level of the Tree
		TreeNode rootTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(rootTreeNode));
		// 2nd level
		children = rootTreeNode.getChildren();
		assertEquals(2, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(2, Iterables.size(nlfc));
		Iterator<?> nlfcIt = nlfc.iterator();
		TreeNode r6TreeNode = (TreeNode)nlfcIt.next();
		assertEquals(ELLIPSIS + " (R6.ecore)", itemDelegator.getText(r6TreeNode));
		// 3rd level
		children = r6TreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeF = (TreeNode)children.iterator().next();
		assertEquals("F", itemDelegator.getText(treeNodeF));
		// 2nd level
		TreeNode r7TreeNode = (TreeNode)nlfcIt.next();
		assertEquals(ELLIPSIS + " (R7.ecore)", itemDelegator.getText(r7TreeNode));
		// 3rd level
		children = r7TreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeG = (TreeNode)children.iterator().next();
		assertEquals("G", itemDelegator.getText(treeNodeG));
	}

	@Test
	public void testCase6_DefaultGroupProvider() throws IOException, CoreException {
		final Comparison comparison = initComparison("case6");
		TreeNode groupTreeNode = TreeFactory.eINSTANCE.createTreeNode();
		groupTreeNode.setData(comparison);
		groupTreeNode.eAdapters().add(new DefaultGroupProvider());
		Collection<?> children = itemDelegator.getChildren(groupTreeNode);
		assertEquals(5, children.size());
		Iterable<?> nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		// 1st level of the Tree
		TreeNode rootTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(rootTreeNode));
		// 2nd level
		children = rootTreeNode.getChildren();
		assertEquals(2, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(2, Iterables.size(nlfc));
		Iterator<?> children2ndLevel = nlfc.iterator();
		TreeNode r2TreeNode = (TreeNode)children2ndLevel.next();
		assertEquals(ELLIPSIS + " (R2.ecore)", itemDelegator.getText(r2TreeNode));
		// 3rd level
		children = r2TreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeB = (TreeNode)children.iterator().next();
		assertEquals("B", itemDelegator.getText(treeNodeB));
		// 4th level
		children = treeNodeB.getChildren();
		assertEquals(3, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(2, Iterables.size(nlfc));
		Iterator<?> children4thLevel = children.iterator();
		TreeNode treeNodeE = (TreeNode)children4thLevel.next();
		assertEquals("E [eSubpackages add]", itemDelegator.getText(treeNodeE));
		TreeNode r5TreeNode = (TreeNode)children4thLevel.next();
		assertEquals(ELLIPSIS + " (R5.ecore)", itemDelegator.getText(r5TreeNode));
		// 5th level
		children = r5TreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeG = (TreeNode)children.iterator().next();
		assertEquals("G", itemDelegator.getText(treeNodeG));
		// 4th level
		TreeNode r6TreeNode = (TreeNode)children4thLevel.next();
		assertEquals(ELLIPSIS + " (R6.ecore)", itemDelegator.getText(r6TreeNode));
		// 5th level
		children = r6TreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeH = (TreeNode)children.iterator().next();
		assertEquals("H", itemDelegator.getText(treeNodeH));
		// 2nd level
		TreeNode r3TreeNode = (TreeNode)children2ndLevel.next();
		assertEquals(ELLIPSIS + " (R3.ecore)", itemDelegator.getText(r3TreeNode));
		// 3rd level
		children = r3TreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeC = (TreeNode)children.iterator().next();
		assertEquals("C", itemDelegator.getText(treeNodeC));
	}

	@Test
	public void testCase7_DefaultGroupProvider() throws IOException, CoreException {
		final Comparison comparison = initComparison("case7");
		TreeNode groupTreeNode = TreeFactory.eINSTANCE.createTreeNode();
		groupTreeNode.setData(comparison);
		groupTreeNode.eAdapters().add(new DefaultGroupProvider());
		Collection<?> children = itemDelegator.getChildren(groupTreeNode);
		assertEquals(4, children.size());
		Iterable<?> nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		// 1st level of the Tree
		TreeNode rootTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(rootTreeNode));
		// 2nd level
		children = rootTreeNode.getChildren();
		assertEquals(3, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(3, Iterables.size(nlfc));
		Iterator<?> children2ndLevel = nlfc.iterator();
		// 2nd level
		TreeNode r3TreeNode = (TreeNode)children2ndLevel.next();
		assertEquals(ELLIPSIS + " (R3.ecore)", itemDelegator.getText(r3TreeNode));
		// 3rd level
		children = r3TreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeC = (TreeNode)children.iterator().next();
		assertEquals("C", itemDelegator.getText(treeNodeC));
		TreeNode r5TreeNode = (TreeNode)children2ndLevel.next();
		assertEquals(ELLIPSIS + " (R5.ecore)", itemDelegator.getText(r5TreeNode));
		// 3rd level
		children = r5TreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeG = (TreeNode)children.iterator().next();
		assertEquals("G", itemDelegator.getText(treeNodeG));
		// 2nd level
		TreeNode r6TreeNode = (TreeNode)children2ndLevel.next();
		assertEquals(ELLIPSIS + " (R6.ecore)", itemDelegator.getText(r6TreeNode));
		// 3rd level
		children = r6TreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeH = (TreeNode)children.iterator().next();
		assertEquals("H", itemDelegator.getText(treeNodeH));
	}

	/*
	 * 3-way Group Provider Tests
	 */

	@Test
	public void testCase0_3wayGroupProvider() throws IOException, CoreException {
		final Comparison comparison = initComparison("case0");
		TreeNode groupTreeNode = TreeFactory.eINSTANCE.createTreeNode();
		groupTreeNode.setData(comparison);
		groupTreeNode.eAdapters().add(new ThreeWayComparisonGroupProvider());
		Collection<?> children = itemDelegator.getChildren(groupTreeNode);
		assertEquals(3, children.size());
		// 1st level of the Tree
		Iterator<?> children1stLevel = children.iterator();
		GroupItemProviderAdapter conflicts = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(CONFLICTS, itemDelegator.getText(conflicts));
		// 2nd level
		children = conflicts.getChildren(null);
		assertEquals(0, children.size());
		// 1st level of the Tree
		GroupItemProviderAdapter leftSide = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(LEFT_SIDE, itemDelegator.getText(leftSide));
		// 2nd level
		children = leftSide.getChildren(null);
		assertEquals(1, children.size());
		Iterable<?> nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(0, Iterables.size(nlfc));
		// 1st level of the Tree
		GroupItemProviderAdapter rightSide = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(RIGHT_SIDE, itemDelegator.getText(rightSide));
		// 2nd level
		children = rightSide.getChildren(null);
		assertEquals(2, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		TreeNode nlfTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(nlfTreeNode));
		// 3rd level
		children = nlfTreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeB = (TreeNode)children.iterator().next();
		assertEquals("B", itemDelegator.getText(treeNodeB));
	}

	@Test
	public void testCase1_3wayGroupProvider() throws IOException, CoreException {
		final Comparison comparison = initComparison("case1");
		TreeNode groupTreeNode = TreeFactory.eINSTANCE.createTreeNode();
		groupTreeNode.setData(comparison);
		groupTreeNode.eAdapters().add(new ThreeWayComparisonGroupProvider());
		Collection<?> children = itemDelegator.getChildren(groupTreeNode);
		assertEquals(3, children.size());
		// 1st level of the Tree
		Iterator<?> children1stLevel = children.iterator();
		GroupItemProviderAdapter conflicts = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(CONFLICTS, itemDelegator.getText(conflicts));
		// 2nd level
		children = conflicts.getChildren(null);
		assertEquals(0, children.size());
		// 1st level of the Tree
		GroupItemProviderAdapter leftSide = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(LEFT_SIDE, itemDelegator.getText(leftSide));
		// 2nd level
		children = leftSide.getChildren(null);
		assertEquals(2, children.size());
		Iterable<?> nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(0, Iterables.size(nlfc));
		// 1st level of the Tree
		GroupItemProviderAdapter rightSide = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(RIGHT_SIDE, itemDelegator.getText(rightSide));
		// 2nd level
		children = rightSide.getChildren(null);
		assertEquals(3, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(0, Iterables.size(nlfc));
		TreeNode treeNodeA = (TreeNode)children.iterator().next();
		assertEquals("A", itemDelegator.getText(treeNodeA));
		// 3rd level
		children = treeNodeA.getChildren();
		assertEquals(2, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		TreeNode nlfTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(nlfTreeNode));
		// 4th level
		children = nlfTreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeD = (TreeNode)children.iterator().next();
		assertEquals("D", itemDelegator.getText(treeNodeD));
	}

	@Test
	public void testCase2_3wayGroupProvider() throws IOException, CoreException {
		final Comparison comparison = initComparison("case2");
		TreeNode groupTreeNode = TreeFactory.eINSTANCE.createTreeNode();
		groupTreeNode.setData(comparison);
		groupTreeNode.eAdapters().add(new ThreeWayComparisonGroupProvider());
		Collection<?> children = itemDelegator.getChildren(groupTreeNode);
		assertEquals(3, children.size());
		// 1st level of the Tree
		Iterator<?> children1stLevel = children.iterator();
		GroupItemProviderAdapter conflicts = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(CONFLICTS, itemDelegator.getText(conflicts));
		// 2nd level
		children = conflicts.getChildren(null);
		assertEquals(0, children.size());
		// 1st level of the Tree
		GroupItemProviderAdapter leftSide = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(LEFT_SIDE, itemDelegator.getText(leftSide));
		// 2nd level
		children = leftSide.getChildren(null);
		assertEquals(2, children.size());
		Iterable<?> nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(0, Iterables.size(nlfc));
		// 1st level of the Tree
		GroupItemProviderAdapter rightSide = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(RIGHT_SIDE, itemDelegator.getText(rightSide));
		// 2nd level
		children = rightSide.getChildren(null);
		assertEquals(3, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		TreeNode rootTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(rootTreeNode));
		// 2nd level
		children = rootTreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeB = (TreeNode)children.iterator().next();
		assertEquals("B", itemDelegator.getText(treeNodeB));
	}

	@Test
	public void testCase3_3wayGroupProvider() throws IOException, CoreException {
		final Comparison comparison = initComparison("case3");
		TreeNode groupTreeNode = TreeFactory.eINSTANCE.createTreeNode();
		groupTreeNode.setData(comparison);
		groupTreeNode.eAdapters().add(new ThreeWayComparisonGroupProvider());
		Collection<?> children = itemDelegator.getChildren(groupTreeNode);
		assertEquals(3, children.size());
		// 1st level of the Tree
		Iterator<?> children1stLevel = children.iterator();
		GroupItemProviderAdapter conflicts = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(CONFLICTS, itemDelegator.getText(conflicts));
		// 2nd level
		children = conflicts.getChildren(null);
		assertEquals(0, children.size());
		// 1st level of the Tree
		GroupItemProviderAdapter leftSide = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(LEFT_SIDE, itemDelegator.getText(leftSide));
		// 2nd level
		children = leftSide.getChildren(null);
		assertEquals(2, children.size());
		Iterable<?> nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(0, Iterables.size(nlfc));
		// 1st level of the Tree
		GroupItemProviderAdapter rightSide = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(RIGHT_SIDE, itemDelegator.getText(rightSide));
		// 2nd level
		children = rightSide.getChildren(null);
		assertEquals(3, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		TreeNode rootTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(rootTreeNode));
		// 3rd level
		children = rootTreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode nodeB = (TreeNode)children.iterator().next();
		assertEquals("B", itemDelegator.getText(nodeB));
		// 4th level
		children = nodeB.getChildren();
		assertEquals(2, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		TreeNode nlfcTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(nlfcTreeNode));
		// 5th level
		children = nlfcTreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode nodeE = (TreeNode)children.iterator().next();
		assertEquals("E", itemDelegator.getText(nodeE));
	}

	@Test
	public void testCase4_3wayGroupProvider() throws IOException, CoreException {
		final Comparison comparison = initComparison("case4");
		TreeNode groupTreeNode = TreeFactory.eINSTANCE.createTreeNode();
		groupTreeNode.setData(comparison);
		groupTreeNode.eAdapters().add(new ThreeWayComparisonGroupProvider());
		Collection<?> children = itemDelegator.getChildren(groupTreeNode);
		assertEquals(3, children.size());
		// 1st level of the Tree
		Iterator<?> children1stLevel = children.iterator();
		GroupItemProviderAdapter conflicts = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(CONFLICTS, itemDelegator.getText(conflicts));
		// 2nd level
		children = conflicts.getChildren(null);
		assertEquals(0, children.size());
		// 1st level of the Tree
		GroupItemProviderAdapter leftSide = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(LEFT_SIDE, itemDelegator.getText(leftSide));
		// 2nd level
		children = leftSide.getChildren(null);
		assertEquals(3, children.size());
		Iterable<?> nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		TreeNode rootTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(rootTreeNode));
		// 3rd level
		children = rootTreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeC = (TreeNode)children.iterator().next();
		assertEquals("C", itemDelegator.getText(treeNodeC));
		// 1st level of the Tree
		GroupItemProviderAdapter rightSide = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(RIGHT_SIDE, itemDelegator.getText(rightSide));
		// 2nd level
		children = rightSide.getChildren(null);
		assertEquals(3, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		rootTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(rootTreeNode));
		// 3rd level
		children = rootTreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeB = (TreeNode)children.iterator().next();
		assertEquals("B", itemDelegator.getText(treeNodeB));
	}

	@Test
	public void testCase5_3wayGroupProvider() throws IOException, CoreException {
		final Comparison comparison = initComparison("case5");
		TreeNode groupTreeNode = TreeFactory.eINSTANCE.createTreeNode();
		groupTreeNode.setData(comparison);
		groupTreeNode.eAdapters().add(new ThreeWayComparisonGroupProvider());
		Collection<?> children = itemDelegator.getChildren(groupTreeNode);
		assertEquals(3, children.size());
		// 1st level of the Tree
		Iterator<?> children1stLevel = children.iterator();
		GroupItemProviderAdapter conflicts = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(CONFLICTS, itemDelegator.getText(conflicts));
		// 2nd level
		children = conflicts.getChildren(null);
		assertEquals(0, children.size());
		// 1st level of the Tree
		GroupItemProviderAdapter leftSide = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(LEFT_SIDE, itemDelegator.getText(leftSide));
		// 2nd level
		children = leftSide.getChildren(null);
		assertEquals(3, children.size());
		Iterable<?> nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		TreeNode rootTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(rootTreeNode));
		// 3rd level
		children = rootTreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeF = (TreeNode)children.iterator().next();
		assertEquals("F", itemDelegator.getText(treeNodeF));
		// 1st level of the Tree
		GroupItemProviderAdapter rightSide = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(RIGHT_SIDE, itemDelegator.getText(rightSide));
		// 2nd level
		children = rightSide.getChildren(null);
		assertEquals(3, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		rootTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(rootTreeNode));
		// 3rd level
		children = rootTreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeG = (TreeNode)children.iterator().next();
		assertEquals("G", itemDelegator.getText(treeNodeG));
	}

	@Test
	public void testCase6_3wayGroupProvider() throws IOException, CoreException {
		final Comparison comparison = initComparison("case6");
		TreeNode groupTreeNode = TreeFactory.eINSTANCE.createTreeNode();
		groupTreeNode.setData(comparison);
		groupTreeNode.eAdapters().add(new ThreeWayComparisonGroupProvider());
		Collection<?> children = itemDelegator.getChildren(groupTreeNode);
		assertEquals(3, children.size());
		// 1st level of the Tree
		Iterator<?> children1stLevel = children.iterator();
		GroupItemProviderAdapter conflicts = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(CONFLICTS, itemDelegator.getText(conflicts));
		// 2nd level
		children = conflicts.getChildren(null);
		assertEquals(0, children.size());
		// 1st level of the Tree
		GroupItemProviderAdapter leftSide = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(LEFT_SIDE, itemDelegator.getText(leftSide));
		// 2nd level
		children = leftSide.getChildren(null);
		assertEquals(5, children.size());
		Iterable<?> nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		TreeNode rootTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(rootTreeNode));
		// 3rd level
		children = rootTreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeB = (TreeNode)children.iterator().next();
		assertEquals("B", itemDelegator.getText(treeNodeB));
		// 4th level
		children = treeNodeB.getChildren();
		assertEquals(2, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		Iterator<?> children4thLevel = children.iterator();
		TreeNode treeNodeE = (TreeNode)children4thLevel.next();
		assertEquals("E [eSubpackages add]", itemDelegator.getText(treeNodeE));
		TreeNode r5TreeNode = (TreeNode)children4thLevel.next();
		assertEquals(ELLIPSIS, itemDelegator.getText(r5TreeNode));
		// 5th level
		children = r5TreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeG = (TreeNode)children.iterator().next();
		assertEquals("G", itemDelegator.getText(treeNodeG));
		// 1st level of the Tree
		GroupItemProviderAdapter rightSide = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(RIGHT_SIDE, itemDelegator.getText(rightSide));
		// 2nd level
		children = rightSide.getChildren(null);
		assertEquals(5, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		rootTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(rootTreeNode));
		// 3rd level
		children = rootTreeNode.getChildren();
		assertEquals(2, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(2, Iterables.size(nlfc));
		Iterator<?> children3rdLevel = nlfc.iterator();
		TreeNode r3TreeNode = (TreeNode)children3rdLevel.next();
		assertEquals(ELLIPSIS + " (R3.ecore)", itemDelegator.getText(r3TreeNode));
		// 4th level
		children = r3TreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeC = (TreeNode)children.iterator().next();
		assertEquals("C", itemDelegator.getText(treeNodeC));
		// 3rd level
		TreeNode r6TreeNode = (TreeNode)children3rdLevel.next();
		assertEquals(ELLIPSIS + " (R6.ecore)", itemDelegator.getText(r6TreeNode));
		// 4th level
		children = r6TreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeH = (TreeNode)children.iterator().next();
		assertEquals("H", itemDelegator.getText(treeNodeH));
	}

	@Test
	public void testCase7_3wayGroupProvider() throws IOException, CoreException {
		final Comparison comparison = initComparison("case7");
		TreeNode groupTreeNode = TreeFactory.eINSTANCE.createTreeNode();
		groupTreeNode.setData(comparison);
		groupTreeNode.eAdapters().add(new ThreeWayComparisonGroupProvider());
		Collection<?> children = itemDelegator.getChildren(groupTreeNode);
		assertEquals(3, children.size());
		// 1st level of the Tree
		Iterator<?> children1stLevel = children.iterator();
		GroupItemProviderAdapter conflicts = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(CONFLICTS, itemDelegator.getText(conflicts));
		// 2nd level
		children = conflicts.getChildren(null);
		assertEquals(0, children.size());
		// 1st level of the Tree
		GroupItemProviderAdapter leftSide = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(LEFT_SIDE, itemDelegator.getText(leftSide));
		// 2nd level
		children = leftSide.getChildren(null);
		assertEquals(4, children.size());
		Iterable<?> nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		TreeNode rootTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(rootTreeNode));
		// 3rd level
		children = rootTreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeG = (TreeNode)children.iterator().next();
		assertEquals("G", itemDelegator.getText(treeNodeG));
		// 1st level of the Tree
		GroupItemProviderAdapter rightSide = (GroupItemProviderAdapter)children1stLevel.next();
		assertEquals(RIGHT_SIDE, itemDelegator.getText(rightSide));
		// 2nd level
		children = rightSide.getChildren(null);
		assertEquals(4, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(1, Iterables.size(nlfc));
		rootTreeNode = (TreeNode)nlfc.iterator().next();
		assertEquals(ELLIPSIS, itemDelegator.getText(rootTreeNode));
		// 3rd level
		children = rootTreeNode.getChildren();
		assertEquals(2, children.size());
		nlfc = Iterables.filter(children, NOT_LOADED_FRAGMENT);
		assertEquals(2, Iterables.size(nlfc));
		Iterator<?> children3rdLevel = nlfc.iterator();
		TreeNode r3TreeNode = (TreeNode)children3rdLevel.next();
		assertEquals(ELLIPSIS + " (R3.ecore)", itemDelegator.getText(r3TreeNode));
		// 4th level
		children = r3TreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeC = (TreeNode)children.iterator().next();
		assertEquals("C", itemDelegator.getText(treeNodeC));
		// 3rd level
		TreeNode r6TreeNode = (TreeNode)children3rdLevel.next();
		assertEquals(ELLIPSIS + " (R6.ecore)", itemDelegator.getText(r6TreeNode));
		// 4th level
		children = r6TreeNode.getChildren();
		assertEquals(1, children.size());
		TreeNode treeNodeH = (TreeNode)children.iterator().next();
		assertEquals("H", itemDelegator.getText(treeNodeH));
	}
}

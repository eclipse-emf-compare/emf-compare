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
package org.eclipse.emf.compare.ide.ui.tests.contentmergeviewer.notloadedfragment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

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
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
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
import org.eclipse.emf.compare.provider.spec.CompareItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.MergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.rcp.ui.internal.util.MergeViewerUtil;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;

@SuppressWarnings({"restriction", "nls" })
public class NotLoadedFragmentItemTest {

	private static final String ELLIPSIS = "[ ... ]";

	private static AdapterFactoryItemDelegator itemDelegator;

	private static String SEP = File.separator;

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
		final IComparisonScope scope = scopeBuilder.build(left, right, null, new NullProgressMonitor());
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		ReadOnlyGraph<URI> graph = ((ThreadedModelResolver)resolver).getDependencyGraph();
		EMFCompareConfiguration emfCC = new EMFCompareConfiguration(new CompareConfiguration());
		emfCC.setResourcesGraph(graph);
		EMFCompareRCPUIPlugin.getDefault().setEMFCompareConfiguration(emfCC);

		return comparison;
	}

	@Test
	public void testCase0() throws IOException, CoreException {
		final Comparison comparison = initComparison("case0");
		final List<Diff> differences = comparison.getDifferences();
		final Predicate<? super Diff> predicate_E = EMFComparePredicates.addedToReference("B",
				"eSubpackages", "B.E");
		final Diff diff_E = Iterators.find(differences.iterator(), predicate_E);
		final EObject value_E = (EObject)MergeViewerUtil.getDiffValue(diff_E);
		final Match match_E = comparison.getMatch(value_E);

		// Test Left Side
		IMergeViewerItem.Container item_E = new MergeViewerItem.Container(comparison, diff_E, match_E,
				MergeViewerSide.LEFT, itemDelegator.getAdapterFactory());
		assertEquals("E", itemDelegator.getText(item_E.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_B = item_E.getParent();
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_Ellipsis = item_B.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		assertNull(item_Ellipsis.getParent());

		IMergeViewerItem[] item_Ellipsis_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_Ellipsis_Children.length);
		item_B = (IMergeViewerItem.Container)item_Ellipsis_Children[0];
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.LEFT)));

		// Test Right Side
		item_E = new MergeViewerItem.Container(comparison, diff_E, match_E, MergeViewerSide.RIGHT,
				itemDelegator.getAdapterFactory());
		assertEquals("", itemDelegator.getText(item_E.getSideValue(MergeViewerSide.RIGHT)));
		item_B = item_E.getParent();
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = item_B.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		assertNull(item_Ellipsis.getParent());

		item_Ellipsis_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_Ellipsis_Children.length);
		item_B = (IMergeViewerItem.Container)item_Ellipsis_Children[0];
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.RIGHT)));
	}

	@Test
	public void testCase1() throws IOException, CoreException {
		final Comparison comparison = initComparison("case1");
		final List<Diff> differences = comparison.getDifferences();
		final Predicate<? super Diff> predicate_F = EMFComparePredicates.addedToReference("D",
				"eSubpackages", "D.F");
		final Diff diff_F = Iterators.find(differences.iterator(), predicate_F);
		final EObject value_F = (EObject)MergeViewerUtil.getDiffValue(diff_F);
		final Match match_F = comparison.getMatch(value_F);

		// Test Left Side
		IMergeViewerItem.Container item_F = new MergeViewerItem.Container(comparison, diff_F, match_F,
				MergeViewerSide.LEFT, itemDelegator.getAdapterFactory());
		assertEquals("F", itemDelegator.getText(item_F.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_D = item_F.getParent();
		assertEquals("D", itemDelegator.getText(item_D.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_Ellipsis = item_D.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_A = item_Ellipsis.getParent();
		assertEquals("A", itemDelegator.getText(item_A.getSideValue(MergeViewerSide.LEFT)));
		assertNull(item_A.getParent());

		IMergeViewerItem[] item_A_Children = item_A.getChildren(null, null);
		assertEquals(2, item_A_Children.length);
		IMergeViewerItem.Container item_C = (IMergeViewerItem.Container)item_A_Children[0];
		assertEquals("", itemDelegator.getText(item_C.getSideValue(MergeViewerSide.LEFT)));
		item_Ellipsis = (IMergeViewerItem.Container)item_A_Children[1];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem[] item_Ellipsis_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_Ellipsis_Children.length);
		item_D = (IMergeViewerItem.Container)item_Ellipsis_Children[0];
		assertEquals("D", itemDelegator.getText(item_D.getSideValue(MergeViewerSide.LEFT)));

		// Test Right Side
		item_F = new MergeViewerItem.Container(comparison, diff_F, match_F, MergeViewerSide.RIGHT,
				itemDelegator.getAdapterFactory());
		assertEquals("", itemDelegator.getText(item_F.getSideValue(MergeViewerSide.RIGHT)));
		item_D = item_F.getParent();
		assertEquals("D", itemDelegator.getText(item_D.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = item_D.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		item_A = item_Ellipsis.getParent();
		assertEquals("A", itemDelegator.getText(item_A.getSideValue(MergeViewerSide.RIGHT)));
		assertNull(item_A.getParent());

		item_A_Children = item_A.getChildren(null, null);
		assertEquals(2, item_A_Children.length);
		item_C = (IMergeViewerItem.Container)item_A_Children[0];
		assertEquals("C", itemDelegator.getText(item_C.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = (IMergeViewerItem.Container)item_A_Children[1];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_Ellipsis_Children.length);
		item_D = (IMergeViewerItem.Container)item_Ellipsis_Children[0];
		assertEquals("D", itemDelegator.getText(item_D.getSideValue(MergeViewerSide.RIGHT)));
	}

	@Test
	public void testCase2() throws IOException, CoreException {
		final Comparison comparison = initComparison("case2");
		final List<Diff> differences = comparison.getDifferences();
		final Predicate<? super Diff> predicate_E = EMFComparePredicates.addedToReference("B.D",
				"eSubpackages", "B.D.E");
		final Diff diff_E = Iterators.find(differences.iterator(), predicate_E);
		final EObject value_E = (EObject)MergeViewerUtil.getDiffValue(diff_E);
		final Match match_E = comparison.getMatch(value_E);

		// Test Left Side
		IMergeViewerItem.Container item_E = new MergeViewerItem.Container(comparison, diff_E, match_E,
				MergeViewerSide.LEFT, itemDelegator.getAdapterFactory());
		assertEquals("E", itemDelegator.getText(item_E.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_D = item_E.getParent();
		assertEquals("D", itemDelegator.getText(item_D.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_B = item_D.getParent();
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_Ellipsis = item_B.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		assertNull(item_Ellipsis.getParent());

		IMergeViewerItem[] item_Ellipsis_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_Ellipsis_Children.length);
		item_B = (IMergeViewerItem.Container)item_Ellipsis_Children[0];
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.LEFT)));

		// Test Right Side
		item_E = new MergeViewerItem.Container(comparison, diff_E, match_E, MergeViewerSide.RIGHT,
				itemDelegator.getAdapterFactory());
		assertEquals("", itemDelegator.getText(item_E.getSideValue(MergeViewerSide.RIGHT)));
		item_D = item_E.getParent();
		assertEquals("D", itemDelegator.getText(item_D.getSideValue(MergeViewerSide.RIGHT)));
		item_B = item_D.getParent();
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = item_B.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		assertNull(item_Ellipsis.getParent());

		item_Ellipsis_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_Ellipsis_Children.length);
		item_B = (IMergeViewerItem.Container)item_Ellipsis_Children[0];
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.RIGHT)));
	}

	@Test
	public void testCase3() throws IOException, CoreException {
		final Comparison comparison = initComparison("case3");
		final List<Diff> differences = comparison.getDifferences();
		final Predicate<? super Diff> predicate_F = EMFComparePredicates.addedToReference("E",
				"eSubpackages", "E.F");
		final Diff diff_F = Iterators.find(differences.iterator(), predicate_F);
		final EObject value_F = (EObject)MergeViewerUtil.getDiffValue(diff_F);
		final Match match_F = comparison.getMatch(value_F);

		// Test Left Side
		IMergeViewerItem.Container item_F = new MergeViewerItem.Container(comparison, diff_F, match_F,
				MergeViewerSide.LEFT, itemDelegator.getAdapterFactory());
		assertEquals("F", itemDelegator.getText(item_F.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_E = item_F.getParent();
		assertEquals("E", itemDelegator.getText(item_E.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_Ellipsis = item_E.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_B = item_Ellipsis.getParent();
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.LEFT)));
		item_Ellipsis = item_B.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		assertNull(item_Ellipsis.getParent());

		IMergeViewerItem[] item_Ellipsis_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_Ellipsis_Children.length);
		item_B = (IMergeViewerItem.Container)item_Ellipsis_Children[0];
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem[] item_B_Children = item_B.getChildren(null, null);
		assertEquals(2, item_B_Children.length);
		IMergeViewerItem.Container item_C = (IMergeViewerItem.Container)item_B_Children[0];
		assertEquals("", itemDelegator.getText(item_C.getSideValue(MergeViewerSide.LEFT)));
		item_Ellipsis = (IMergeViewerItem.Container)item_B_Children[1];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		item_Ellipsis_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_Ellipsis_Children.length);
		item_E = (IMergeViewerItem.Container)item_Ellipsis_Children[0];
		assertEquals("E", itemDelegator.getText(item_E.getSideValue(MergeViewerSide.LEFT)));

		// Test Right Side
		item_F = new MergeViewerItem.Container(comparison, diff_F, match_F, MergeViewerSide.RIGHT,
				itemDelegator.getAdapterFactory());
		assertEquals("", itemDelegator.getText(item_F.getSideValue(MergeViewerSide.RIGHT)));
		item_E = item_F.getParent();
		assertEquals("E", itemDelegator.getText(item_E.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = item_E.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		item_B = item_Ellipsis.getParent();
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = item_B.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		assertNull(item_Ellipsis.getParent());

		item_Ellipsis_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_Ellipsis_Children.length);
		item_B = (IMergeViewerItem.Container)item_Ellipsis_Children[0];
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.RIGHT)));
		item_B_Children = item_B.getChildren(null, null);
		assertEquals(2, item_B_Children.length);
		item_C = (IMergeViewerItem.Container)item_B_Children[0];
		assertEquals("C", itemDelegator.getText(item_C.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = (IMergeViewerItem.Container)item_B_Children[1];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_Ellipsis_Children.length);
		item_E = (IMergeViewerItem.Container)item_Ellipsis_Children[0];
		assertEquals("E", itemDelegator.getText(item_E.getSideValue(MergeViewerSide.RIGHT)));
	}

	@Test
	public void testCase4() throws IOException, CoreException {
		final Comparison comparison = initComparison("case4");
		final List<Diff> differences = comparison.getDifferences();
		final Predicate<? super Diff> predicate_D = EMFComparePredicates.removedFromReference("B",
				"eSubpackages", "B.D");
		final Diff diff_D = Iterators.find(differences.iterator(), predicate_D);
		final EObject value_D = (EObject)MergeViewerUtil.getDiffValue(diff_D);
		final Match match_D = comparison.getMatch(value_D);

		// Test Left Side
		IMergeViewerItem.Container item_D = new MergeViewerItem.Container(comparison, diff_D, match_D,
				MergeViewerSide.LEFT, itemDelegator.getAdapterFactory());
		assertEquals("", itemDelegator.getText(item_D.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_B = item_D.getParent();
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_Ellipsis = item_B.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		item_Ellipsis = item_Ellipsis.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		assertNull(item_Ellipsis.getParent());

		IMergeViewerItem[] item_Ellipsis_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(2, item_Ellipsis_Children.length);
		item_Ellipsis = (IMergeViewerItem.Container)item_Ellipsis_Children[0];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem[] item_R2_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R2_Children.length);
		item_B = (IMergeViewerItem.Container)item_R2_Children[0];
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.LEFT)));
		item_Ellipsis = (IMergeViewerItem.Container)item_Ellipsis_Children[1];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem[] item_R3_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R3_Children.length);
		IMergeViewerItem.Container item_C = (IMergeViewerItem.Container)item_R3_Children[0];
		assertEquals("C", itemDelegator.getText(item_C.getSideValue(MergeViewerSide.LEFT)));

		// Test Right Side
		item_D = new MergeViewerItem.Container(comparison, diff_D, match_D, MergeViewerSide.RIGHT,
				itemDelegator.getAdapterFactory());
		assertEquals("D", itemDelegator.getText(item_D.getSideValue(MergeViewerSide.RIGHT)));
		item_B = item_D.getParent();
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = item_B.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = item_Ellipsis.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		assertNull(item_Ellipsis.getParent());

		item_Ellipsis_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(2, item_Ellipsis_Children.length);
		item_Ellipsis = (IMergeViewerItem.Container)item_Ellipsis_Children[0];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		item_R2_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R2_Children.length);
		item_B = (IMergeViewerItem.Container)item_R2_Children[0];
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = (IMergeViewerItem.Container)item_Ellipsis_Children[1];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		item_R3_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R3_Children.length);
		item_C = (IMergeViewerItem.Container)item_R3_Children[0];
		assertEquals("C", itemDelegator.getText(item_C.getSideValue(MergeViewerSide.RIGHT)));
	}

	@Test
	public void testCase5() throws IOException, CoreException {
		final Comparison comparison = initComparison("case5");
		final List<Diff> differences = comparison.getDifferences();
		final Predicate<? super Diff> predicate_H = EMFComparePredicates.removedFromReference("F",
				"eSubpackages", "F.H");
		final Diff diff_H = Iterators.find(differences.iterator(), predicate_H);
		final EObject value_H = (EObject)MergeViewerUtil.getDiffValue(diff_H);
		final Match match_H = comparison.getMatch(value_H);

		// Test Left Side
		IMergeViewerItem.Container item_H = new MergeViewerItem.Container(comparison, diff_H, match_H,
				MergeViewerSide.LEFT, itemDelegator.getAdapterFactory());
		assertEquals("", itemDelegator.getText(item_H.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_F = item_H.getParent();
		assertEquals("F", itemDelegator.getText(item_F.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_Ellipsis = item_F.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		item_Ellipsis = item_Ellipsis.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		assertNull(item_Ellipsis.getParent());

		IMergeViewerItem[] item_Ellipsis_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(2, item_Ellipsis_Children.length);
		item_Ellipsis = (IMergeViewerItem.Container)item_Ellipsis_Children[0];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem[] item_R6_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R6_Children.length);
		item_F = (IMergeViewerItem.Container)item_R6_Children[0];
		assertEquals("F", itemDelegator.getText(item_F.getSideValue(MergeViewerSide.LEFT)));
		item_Ellipsis = (IMergeViewerItem.Container)item_Ellipsis_Children[1];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem[] item_R7_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R7_Children.length);
		IMergeViewerItem.Container item_G = (IMergeViewerItem.Container)item_R7_Children[0];
		assertEquals("G", itemDelegator.getText(item_G.getSideValue(MergeViewerSide.LEFT)));

		// Test Right Side
		item_H = new MergeViewerItem.Container(comparison, diff_H, match_H, MergeViewerSide.RIGHT,
				itemDelegator.getAdapterFactory());
		assertEquals("H", itemDelegator.getText(item_H.getSideValue(MergeViewerSide.RIGHT)));
		item_F = item_H.getParent();
		assertEquals("F", itemDelegator.getText(item_F.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = item_F.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = item_Ellipsis.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		assertNull(item_Ellipsis.getParent());

		item_Ellipsis_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(2, item_Ellipsis_Children.length);
		item_Ellipsis = (IMergeViewerItem.Container)item_Ellipsis_Children[0];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		item_R6_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R6_Children.length);
		item_F = (IMergeViewerItem.Container)item_R6_Children[0];
		assertEquals("F", itemDelegator.getText(item_F.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = (IMergeViewerItem.Container)item_Ellipsis_Children[1];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		item_R7_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R7_Children.length);
		item_G = (IMergeViewerItem.Container)item_R7_Children[0];
		assertEquals("G", itemDelegator.getText(item_G.getSideValue(MergeViewerSide.RIGHT)));
	}

	@Test
	public void testCase6() throws IOException, CoreException {
		final Comparison comparison = initComparison("case6");
		final List<Diff> differences = comparison.getDifferences();
		final Predicate<? super Diff> predicate_I = EMFComparePredicates.removedFromReference("G",
				"eSubpackages", "G.I");
		final Diff diff_I = Iterators.find(differences.iterator(), predicate_I);
		final EObject value_I = (EObject)MergeViewerUtil.getDiffValue(diff_I);
		final Match match_I = comparison.getMatch(value_I);

		// Test Left Side
		IMergeViewerItem.Container item_I = new MergeViewerItem.Container(comparison, diff_I, match_I,
				MergeViewerSide.LEFT, itemDelegator.getAdapterFactory());
		assertEquals("", itemDelegator.getText(item_I.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_G = item_I.getParent();
		assertEquals("G", itemDelegator.getText(item_G.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_Ellipsis = item_G.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_B = item_Ellipsis.getParent();
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.LEFT)));
		item_Ellipsis = item_B.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		item_Ellipsis = item_Ellipsis.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		assertNull(item_Ellipsis.getParent());

		IMergeViewerItem[] item_Ellipsis_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(2, item_Ellipsis_Children.length);
		item_Ellipsis = (IMergeViewerItem.Container)item_Ellipsis_Children[0];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem[] item_R2_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R2_Children.length);
		item_B = (IMergeViewerItem.Container)item_R2_Children[0];
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem[] item_B_Children = item_B.getChildren(null, null);
		assertEquals(3, item_B_Children.length);
		IMergeViewerItem.Container item_E = (IMergeViewerItem.Container)item_B_Children[0];
		assertEquals("E", itemDelegator.getText(item_E.getSideValue(MergeViewerSide.LEFT)));
		item_Ellipsis = (IMergeViewerItem.Container)item_B_Children[1];
		assertEquals(ELLIPSIS + " (R5.ecore)", itemDelegator.getText(item_Ellipsis
				.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem[] item_R5_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R5_Children.length);
		item_G = (IMergeViewerItem.Container)item_R5_Children[0];
		assertEquals("G", itemDelegator.getText(item_G.getSideValue(MergeViewerSide.LEFT)));
		item_Ellipsis = (IMergeViewerItem.Container)item_B_Children[2];
		assertEquals(ELLIPSIS + " (R6.ecore)", itemDelegator.getText(item_Ellipsis
				.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem[] item_R6_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R6_Children.length);
		IMergeViewerItem.Container item_H = (IMergeViewerItem.Container)item_R6_Children[0];
		assertEquals("H", itemDelegator.getText(item_H.getSideValue(MergeViewerSide.LEFT)));
		item_Ellipsis = (IMergeViewerItem.Container)item_Ellipsis_Children[1];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem[] item_R3_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R3_Children.length);
		IMergeViewerItem.Container item_C = (IMergeViewerItem.Container)item_R3_Children[0];
		assertEquals("C", itemDelegator.getText(item_C.getSideValue(MergeViewerSide.LEFT)));

		// Test Right Side
		item_I = new MergeViewerItem.Container(comparison, diff_I, match_I, MergeViewerSide.RIGHT,
				itemDelegator.getAdapterFactory());
		assertEquals("I", itemDelegator.getText(item_I.getSideValue(MergeViewerSide.RIGHT)));
		item_G = item_I.getParent();
		assertEquals("G", itemDelegator.getText(item_G.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = item_G.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		item_B = item_Ellipsis.getParent();
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = item_B.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = item_Ellipsis.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		assertNull(item_Ellipsis.getParent());

		item_Ellipsis_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(2, item_Ellipsis_Children.length);
		item_Ellipsis = (IMergeViewerItem.Container)item_Ellipsis_Children[0];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		item_R2_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R2_Children.length);
		item_B = (IMergeViewerItem.Container)item_R2_Children[0];
		assertEquals("B", itemDelegator.getText(item_B.getSideValue(MergeViewerSide.RIGHT)));
		item_B_Children = item_B.getChildren(null, null);
		assertEquals(3, item_B_Children.length);
		item_E = (IMergeViewerItem.Container)item_B_Children[0];
		assertEquals("", itemDelegator.getText(item_E.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = (IMergeViewerItem.Container)item_B_Children[1];
		assertEquals(ELLIPSIS + " (R5.ecore)", itemDelegator.getText(item_Ellipsis
				.getSideValue(MergeViewerSide.RIGHT)));
		item_R5_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R5_Children.length);
		item_G = (IMergeViewerItem.Container)item_R5_Children[0];
		assertEquals("G", itemDelegator.getText(item_G.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = (IMergeViewerItem.Container)item_B_Children[2];
		assertEquals(ELLIPSIS + " (R6.ecore)", itemDelegator.getText(item_Ellipsis
				.getSideValue(MergeViewerSide.RIGHT)));
		item_R6_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R6_Children.length);
		item_H = (IMergeViewerItem.Container)item_R6_Children[0];
		assertEquals("H", itemDelegator.getText(item_H.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = (IMergeViewerItem.Container)item_Ellipsis_Children[1];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		item_R3_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R3_Children.length);
		item_C = (IMergeViewerItem.Container)item_R3_Children[0];
		assertEquals("C", itemDelegator.getText(item_C.getSideValue(MergeViewerSide.RIGHT)));
	}

	@Test
	public void testCase7() throws IOException, CoreException {
		final Comparison comparison = initComparison("case7");
		final List<Diff> differences = comparison.getDifferences();
		final Predicate<? super Diff> predicate_I = EMFComparePredicates.removedFromReference("G",
				"eSubpackages", "G.I");
		final Diff diff_I = Iterators.find(differences.iterator(), predicate_I);
		final EObject value_I = (EObject)MergeViewerUtil.getDiffValue(diff_I);
		final Match match_I = comparison.getMatch(value_I);

		// Test Left Side
		IMergeViewerItem.Container item_I = new MergeViewerItem.Container(comparison, diff_I, match_I,
				MergeViewerSide.LEFT, itemDelegator.getAdapterFactory());
		assertEquals("", itemDelegator.getText(item_I.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_G = item_I.getParent();
		assertEquals("G", itemDelegator.getText(item_G.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem.Container item_Ellipsis = item_G.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		item_Ellipsis = item_Ellipsis.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		assertNull(item_Ellipsis.getParent());

		IMergeViewerItem[] item_Ellipsis_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(3, item_Ellipsis_Children.length);
		item_Ellipsis = (IMergeViewerItem.Container)item_Ellipsis_Children[0];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem[] item_R3_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R3_Children.length);
		IMergeViewerItem.Container item_C = (IMergeViewerItem.Container)item_R3_Children[0];
		assertEquals("C", itemDelegator.getText(item_C.getSideValue(MergeViewerSide.LEFT)));
		item_Ellipsis = (IMergeViewerItem.Container)item_Ellipsis_Children[1];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem[] item_R5_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R5_Children.length);
		item_G = (IMergeViewerItem.Container)item_R5_Children[0];
		assertEquals("G", itemDelegator.getText(item_G.getSideValue(MergeViewerSide.LEFT)));
		item_Ellipsis = (IMergeViewerItem.Container)item_Ellipsis_Children[2];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.LEFT)));
		IMergeViewerItem[] item_R6_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R6_Children.length);
		IMergeViewerItem.Container item_H = (IMergeViewerItem.Container)item_R6_Children[0];
		assertEquals("H", itemDelegator.getText(item_H.getSideValue(MergeViewerSide.LEFT)));

		// Test Right Side
		item_I = new MergeViewerItem.Container(comparison, diff_I, match_I, MergeViewerSide.LEFT,
				itemDelegator.getAdapterFactory());
		assertEquals("I", itemDelegator.getText(item_I.getSideValue(MergeViewerSide.RIGHT)));
		item_G = item_I.getParent();
		assertEquals("G", itemDelegator.getText(item_G.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = item_G.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = item_Ellipsis.getParent();
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		assertNull(item_Ellipsis.getParent());

		item_Ellipsis_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(3, item_Ellipsis_Children.length);
		item_Ellipsis = (IMergeViewerItem.Container)item_Ellipsis_Children[0];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		item_R3_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R3_Children.length);
		item_C = (IMergeViewerItem.Container)item_R3_Children[0];
		assertEquals("C", itemDelegator.getText(item_C.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = (IMergeViewerItem.Container)item_Ellipsis_Children[1];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		item_R5_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R5_Children.length);
		item_G = (IMergeViewerItem.Container)item_R5_Children[0];
		assertEquals("G", itemDelegator.getText(item_G.getSideValue(MergeViewerSide.RIGHT)));
		item_Ellipsis = (IMergeViewerItem.Container)item_Ellipsis_Children[2];
		assertEquals(ELLIPSIS, itemDelegator.getText(item_Ellipsis.getSideValue(MergeViewerSide.RIGHT)));
		item_R6_Children = item_Ellipsis.getChildren(null, null);
		assertEquals(1, item_R6_Children.length);
		item_H = (IMergeViewerItem.Container)item_R6_Children[0];
		assertEquals("H", itemDelegator.getText(item_H.getSideValue(MergeViewerSide.RIGHT)));

	}
}

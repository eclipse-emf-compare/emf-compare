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
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.google.common.collect.Iterables;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.internal.merge.MergeOperation;
import org.eclipse.emf.compare.merge.AbstractMerger;
import org.eclipse.emf.compare.merge.IMergeOptionAware;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DefaultGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeNodeItemProviderSpec;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.tree.TreeFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.StructuredSelection;
import org.junit.Before;
import org.junit.Test;

/**
 * This test is related to the bug <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=470503">470503</a>
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings({"nls", "restriction" })
public class TestBug470503 extends AbstractTestUITreeNodeItemProviderAdapter {

	private static TreeNodeItemProviderSpec itemProvider;

	private IMerger.Registry mergerRegistry;

	private TreeNode titledItemESuperTypesDelete;

	private TreeNode titledItemEClassifiersDelete;

	private TreeNode titleESFDelete;

	@Override
	@Before
	public void before() throws IOException {
		super.before();
		itemProvider = (TreeNodeItemProviderSpec)treeItemProviderAdapterFactory.createTreeNodeAdapter();
		mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();

		Bug470503InputData inputData = new Bug470503InputData();
		final Resource left = inputData.getResource("left.ecore"); //$NON-NLS-1$
		final Resource right = inputData.getResource("right.ecore"); //$NON-NLS-1$
		final DefaultComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		editingDomain = EMFCompareEditingDomain.create(left, right, null);

		TreeNode ePackageMatch = getExtlibrary_EPackageMatch(comparison);
		Collection<?> ePackage_MatchChildren = ePackageMatch.getChildren();
		Iterable<EObject> ePackage_MatchChildrenData = transform(ePackage_MatchChildren, TREE_NODE_DATA);

		// Get children of Match Book
		Match book_Match = getMatchWithFeatureValue(ePackage_MatchChildrenData, "name", "Book");
		TreeNode book_Match_Node = getTreeNode(ePackageMatch, book_Match);
		Collection<?> book_MatchChildren = book_Match_Node.getChildren();
		Iterable<EObject> book_MatchChildrenData = transform(book_MatchChildren, TREE_NODE_DATA);

		// Get TitledItem [eSuperTypes delete] difference
		ReferenceChange titledItemESuperTypesDeleteChange = getReferenceChangeWithFeatureValue(
				book_MatchChildrenData, "name", "TitledItem");
		titledItemESuperTypesDelete = getTreeNode(book_Match_Node, titledItemESuperTypesDeleteChange);

		// Get TitledItem [eClassifiers delete] difference
		ReferenceChange titledItemEClassifiersDeleteChange = getReferenceChangeWithFeatureValue(
				ePackage_MatchChildrenData, "name", "TitledItem");
		titledItemEClassifiersDelete = getTreeNode(ePackageMatch, titledItemEClassifiersDeleteChange);

		// Get children of Match TitledItem
		TreeNode titledItem_Match_Node = getTreeNode(ePackageMatch, titledItemEClassifiersDeleteChange);
		Collection<?> titledItem_MatchChildren = titledItem_Match_Node.getChildren();
		Iterable<EObject> titledItem_MatchChildrenData = transform(titledItem_MatchChildren, TREE_NODE_DATA);

		// Get title [eStructuralFeatures delete] difference
		ReferenceChange titleESFDeleteChange = getReferenceChangeWithFeatureValue(
				titledItem_MatchChildrenData, "name", "title");
		titleESFDelete = getTreeNode(titledItem_Match_Node, titleESFDeleteChange);
	}

	@Test
	public void testMergeWithCascadingFilter() {
		final MergeMode rightToLeft = MergeMode.RIGHT_TO_LEFT;
		final boolean leftEditable = true;
		final boolean rightEditable = true;
		final boolean cascadingFilter = true;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, rightToLeft, null);

		// Merge from right to left TitledItem [eSuperTypes delete] difference difference
		Diff titledItemESuperTypesDeleteDiff = (Diff)titledItemESuperTypesDelete.getData();
		assertFalse(rightToLeft.isLeftToRight(titledItemESuperTypesDeleteDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE, rightToLeft.getMergeAction(titledItemESuperTypesDeleteDiff,
				leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(titledItemESuperTypesDelete));
		setCascadingDifferencesFilterEnabled(cascadingFilter);
		action.run();
		assertEquals(DifferenceState.MERGED, titledItemESuperTypesDeleteDiff.getState());

		Diff titledItemEClassifiersDeleteDiff = (Diff)titledItemEClassifiersDelete.getData();
		assertEquals(DifferenceState.MERGED, titledItemEClassifiersDeleteDiff.getState());

		Diff titleESFDeleteDiff = (Diff)titleESFDelete.getData();
		assertEquals(DifferenceState.MERGED, titleESFDeleteDiff.getState());
	}

	@Test
	public void testMergeWithoutCascadingFilter() {
		final MergeMode rightToLeft = MergeMode.RIGHT_TO_LEFT;
		final boolean leftEditable = true;
		final boolean rightEditable = true;
		final boolean cascadingFilter = false;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, rightToLeft, null);

		// Merge from right to left TitledItem [eSuperTypes delete] difference difference
		Diff titledItemESuperTypesDeleteDiff = (Diff)titledItemESuperTypesDelete.getData();
		assertFalse(rightToLeft.isLeftToRight(titledItemESuperTypesDeleteDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE, rightToLeft.getMergeAction(titledItemESuperTypesDeleteDiff,
				leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(titledItemESuperTypesDelete));
		setCascadingDifferencesFilterEnabled(cascadingFilter);
		action.run();
		assertEquals(DifferenceState.MERGED, titledItemESuperTypesDeleteDiff.getState());

		Diff titledItemEClassifiersDeleteDiff = (Diff)titledItemEClassifiersDelete.getData();
		assertEquals(DifferenceState.MERGED, titledItemEClassifiersDeleteDiff.getState());

		Diff titleESFDeleteDiff = (Diff)titleESFDelete.getData();
		assertEquals(DifferenceState.UNRESOLVED, titleESFDeleteDiff.getState());
	}

	private void setCascadingDifferencesFilterEnabled(boolean enabled) {
		for (IMergeOptionAware merger : Iterables.filter(this.mergerRegistry.getMergers(null),
				IMergeOptionAware.class)) {
			Map<Object, Object> mergeOptions = merger.getMergeOptions();
			mergeOptions.put(AbstractMerger.SUB_DIFF_AWARE_OPTION, Boolean.valueOf(enabled));
		}
	}

	private static TreeNode getExtlibrary_EPackageMatch(Comparison comparison) throws IOException {
		TreeNode treeNode = TreeFactory.eINSTANCE.createTreeNode();
		treeNode.setData(comparison);
		treeNode.eAdapters().add(new DefaultGroupProvider());

		Collection<?> children = itemProvider.getChildren(treeNode);

		Iterable<?> matches = filter(children, matchTreeNode);
		return (TreeNode)matches.iterator().next();
	}

	/**
	 * Input data for this bug.
	 * 
	 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
	 */
	public class Bug470503InputData extends AbstractInputData {

		private static final String PATH_PREFIX = "data/_470503/"; //$NON-NLS-1$

		public Resource getResource(String resourceName) throws IOException {
			StringBuilder resourceURL = new StringBuilder(PATH_PREFIX);
			resourceURL.append(resourceName);
			return loadFromClassLoader(resourceURL.toString());
		}
	}
}

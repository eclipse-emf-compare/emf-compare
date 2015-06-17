/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Michael Borkowski - extraction of nested classes, TreeItem creation 
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.EMFCompareStructureMergeViewerContentProvider;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.EMFCompareStructureMergeViewerContentProvider.CallbackType;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.Navigatable;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.WrappableTreeViewer;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Utils class used for creating the {@link Tree} and the {@link WrappableTreeViewer}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
final class TestContext {

	/**
	 * Number of elements in the testContext.
	 */
	private int counter = 0;

	private Tree swtTree;

	private Map<Integer, TreeItem> itemRetreiver;

	private WrappableTreeViewer viewer;

	private final Shell currentShell;

	private Object[] currentSelection;

	public TestContext(Shell shell) {
		super();
		currentShell = shell;
		this.itemRetreiver = new HashMap<Integer, TreeItem>();
	}

	private Integer increment() {
		return new Integer(counter++);
	}

	public Tree getTree() {
		return swtTree;
	}

	public TreeItem getItem(int id) {
		return itemRetreiver.get(Integer.valueOf(id));
	}

	public int getNumberOfNodes() {
		return counter - 1;
	}

	/**
	 * Builds a {@link Tree} (convenience method for calling {@link #buildTree(int, int, boolean)} with
	 * useString = <code>true</code>)
	 * 
	 * @param depth
	 *            of the testContext
	 * @param numberOfChildren
	 *            number of chidlren by item.
	 * @return {@link Navigatable}
	 */
	public TestNavigatable buildTree(int depth, int numberOfChildren) {
		return buildTree(depth, numberOfChildren, true);
	}

	/**
	 * Builds a {@link Tree}.
	 * 
	 * @param depth
	 *            of the testContext
	 * @param numberOfChildren
	 *            number of children by item.
	 * @param useStrings
	 *            whether to use {@link String} data instead of {@link Adapter} data elements.
	 * @return {@link Navigatable}
	 */
	public TestNavigatable buildTree(int depth, int numberOfChildren, boolean useStrings) {
		if (swtTree == null) {
			swtTree = new Tree(currentShell, SWT.NONE);
			swtTree.setData("root"); //$NON-NLS-1$
			createSubNodes(swtTree, numberOfChildren, depth, useStrings);
			viewer = spy(new WrappableTreeViewer(swtTree));
			doAnswer(selectionChanged()).when(viewer).setSelection(any(ISelection.class));
			AdapterFactory adapterFactory = mock(AdapterFactory.class);
			EMFCompareStructureMergeViewerContentProvider contentProvider = new EMFCompareStructureMergeViewerContentProvider(
					adapterFactory, viewer); // mock(EMFCompareStructureMergeViewerContentProvider.class);
			return new TestNavigatable(viewer, contentProvider);
		} else {
			throw new AssertionError("The tree can only be built once"); //$NON-NLS-1$
		}
	}

	private Answer<Void> selectionChanged() {
		return new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) throws Throwable {
				ISelection selection = (ISelection)invocation.getArguments()[0];
				if (selection instanceof StructuredSelection) {
					currentSelection = ((StructuredSelection)selection).toArray();
				}
				invocation.callRealMethod();
				return null;
			}
		};
	}

	private void createSubNodes(Object parent, int numberOfChild, int depth, boolean useStrings) {
		if (depth > 0) {
			for (int childIndex = 0; childIndex < numberOfChild; childIndex++) {
				final TreeItem item;
				if (parent instanceof Tree) {
					item = new TreeItem((Tree)parent, SWT.NONE);
				} else {
					item = new TreeItem((TreeItem)parent, SWT.NONE);
				}
				Integer increment = increment();
				String name = String.valueOf(increment);
				item.setText(name);
				if (useStrings) {
					item.setData(name);
				} else {
					Adapter adapter = mock(Adapter.class);
					TreeNode notifier = mock(TreeNode.class);
					Diff diff = mock(Diff.class);
					when(adapter.getTarget()).thenReturn(notifier);
					when(notifier.getData()).thenReturn(diff);
					item.setData(adapter);
				}
				itemRetreiver.put(increment, item);
				createSubNodes(item, numberOfChild, depth - 1, useStrings);
			}
		}
	}

	/**
	 * The purpose of this class is to make protected members of {@link Navigatable} visible to tests.
	 * 
	 * @author Michael Borkowski <mborkowski@eclipsesource.com>
	 */
	public class TestNavigatable extends Navigatable {

		public TestNavigatable(WrappableTreeViewer viewer,
				EMFCompareStructureMergeViewerContentProvider contentProvider) {
			super(viewer, contentProvider);
			uiSyncCallbackType = CallbackType.IN_CURRENT_THREAD;
		}

		@Override
		public TreeItem getNextItem(TreeItem previousItem) {
			return super.getNextItem(previousItem);
		}

		@Override
		public TreeItem getPreviousItem(TreeItem previousItem) {
			return super.getPreviousItem(previousItem);
		}

	}

	/**
	 * Returns the current tree selection
	 * 
	 * @return
	 */
	public Object[] getCurrentSelection() {
		return currentSelection;
	}

}

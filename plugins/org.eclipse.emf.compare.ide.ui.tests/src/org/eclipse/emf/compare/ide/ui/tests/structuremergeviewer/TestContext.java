/*******************************************************************************
 * Copyright (c) 2014, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Michael Borkowski - extraction of nested classes, TreeItem creation 
 *     Martin Fleck - bug 518572
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.EMFCompareStructureMergeViewerContentProvider;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.EMFCompareStructureMergeViewerContentProvider.CallbackType;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.Navigatable;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.WrappableTreeViewer;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

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

	/**
	 * Artificial root element created for the element content provider.
	 */
	private Object root;

	/**
	 * Convenience map to retrieve elements by their counter number.
	 */
	private Map<Integer, Object> elementRetriever;

	/**
	 * Map to back the element content provider: Element -> Children.
	 */
	private Map<Object, List<Object>> elementChildren;

	/**
	 * Map to back the element content provider: Element -> Parent.
	 */
	private Map<Object, Object> elementParent;

	private WrappableTreeViewer viewer;

	private final Shell currentShell;

	private Object[] currentSelection;

	public TestContext(Shell shell) {
		super();
		currentShell = shell;
		this.elementRetriever = new HashMap<Integer, Object>();
		this.elementChildren = new HashMap<Object, List<Object>>();
		this.elementParent = new HashMap<Object, Object>();
	}

	private Integer increment() {
		return new Integer(counter++);
	}

	public Tree getTree() {
		return swtTree;
	}

	public Object getRoot() {
		return root;
	}

	public Object getElement(int id) {
		return elementRetriever.get(Integer.valueOf(id));
	}

	public int getNumberOfNodes() {
		return counter - 1;
	}

	public void dispose() {
		if (swtTree != null) {
			swtTree.dispose();
			swtTree = null;
		}
		elementRetriever.clear();
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
			// create viewer and tree
			swtTree = new Tree(currentShell, SWT.NONE);
			viewer = new WrappableTreeViewer(swtTree);
			viewer.addOpenListener(new IOpenListener() {
				public void open(OpenEvent event) {
					if (event.getSelection() instanceof IStructuredSelection) {
						currentSelection = ((IStructuredSelection)event.getSelection()).toArray();
					}
				}
			});

			// create elements
			root = swtTree;
			elementChildren.put(root, createSubNodes(root, numberOfChildren, depth, useStrings));

			// create content providers
			AdapterFactory adapterFactory = mock(AdapterFactory.class);
			EMFCompareStructureMergeViewerContentProvider navigatableContentProvider = new EMFCompareStructureMergeViewerContentProvider(
					adapterFactory, viewer);

			// init viewer
			ITreeContentProvider viewerContentProvider = createElementContentProvider();
			viewer.setContentProvider(viewerContentProvider);
			viewer.setInput(root);
			List<Object> rootChildren = elementChildren.get(root);
			if (!rootChildren.isEmpty()) {
				viewer.setSelection(new StructuredSelection(rootChildren.get(0)));
			}

			return new TestNavigatable(viewer, navigatableContentProvider);
		} else {
			throw new AssertionError("The tree can only be built once"); //$NON-NLS-1$
		}
	}

	private ITreeContentProvider createElementContentProvider() {
		return new ITreeContentProvider() {

			public Object[] getElements(Object inputElement) {
				return elementChildren.get(inputElement).toArray();
			}

			public Object[] getChildren(Object parentElement) {
				return elementChildren.get(parentElement).toArray();
			}

			public Object getParent(Object element) {
				return elementParent.get(element);
			}

			public boolean hasChildren(Object element) {
				return !elementChildren.get(element).isEmpty();
			}

			public void dispose() {
				// do nothing
			}

			public void inputChanged(Viewer viewers, Object oldInput, Object newInput) {
				// do nothing
			}
		};
	}

	private List<Object> createSubNodes(Object parent, int numberOfChild, int depth, boolean useStrings) {
		List<Object> children = Lists.newArrayList();
		if (depth > 0) {
			for (int childIndex = 0; childIndex < numberOfChild; childIndex++) {
				Integer increment = increment();
				String name = String.valueOf(increment);
				Object data = null;
				if (useStrings) {
					data = name;
				} else {
					Adapter adapter = mock(Adapter.class);
					TreeNode notifier = mock(TreeNode.class);
					Diff diff = mock(Diff.class);
					when(adapter.getTarget()).thenReturn(notifier);
					when(notifier.getData()).thenReturn(diff);
					data = adapter;
				}
				elementParent.put(data, parent);
				children.add(data);
				elementRetriever.put(increment, data);
				elementChildren.put(data, createSubNodes(data, numberOfChild, depth - 1, useStrings));
			}
		}
		return children;
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
		public Object getNextItem(Object start) {
			return super.getNextItem(start);
		}

		@Override
		public Object getPreviousItem(Object start) {
			return super.getPreviousItem(start);
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

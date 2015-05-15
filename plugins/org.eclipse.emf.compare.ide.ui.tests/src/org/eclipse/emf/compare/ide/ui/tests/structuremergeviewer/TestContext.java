/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Michael Borkowski - extraction of unnecessarily nested classes 
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.Navigatable;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.WrappableTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

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
	 * Builds a {@link Tree}.
	 * 
	 * @param depth
	 *            of the testContext
	 * @param numberOfChildren
	 *            number of chidlren by item.
	 * @return {@link Navigatable}
	 */
	public TestNavigatable buildTree(int depth, int numberOfChildren) {
		if (swtTree == null) {
			swtTree = new Tree(currentShell, SWT.NONE);
			swtTree.setData("root"); //$NON-NLS-1$
			createSubNodes(swtTree, numberOfChildren, depth);
			viewer = new WrappableTreeViewer(swtTree);
			return new TestNavigatable(viewer);
		} else {
			throw new AssertionError("The tree can only be built once"); //$NON-NLS-1$
		}
	}

	private void createSubNodes(Object parent, int numberOfChild, int depth) {
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
				item.setData(name);
				itemRetreiver.put(increment, item);
				createSubNodes(item, numberOfChild, depth - 1);
			}
		}
	}

	/**
	 * The purpose of this class is to make protected members of {@link Navigatable} visible to tests.
	 * 
	 * @author Michael Borkowski <mborkowski@eclipsesource.com>
	 */
	public class TestNavigatable extends Navigatable {

		public TestNavigatable(WrappableTreeViewer viewer) {
			super(viewer, null);
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

}

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
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.Navigatable;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.WrappableTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the iteration on the {@link Navigatable} class.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class NavigatableTest {
	/**
	 * Utils class used for creating the {@link Tree} and the {@link WrappableTreeViewer}.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	protected static final class TestContext {

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

		public Item getItem(int id) {
			return itemRetreiver.get(new Integer(id));
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
		 * @return {@link MockNavigatable}
		 */
		public MockNavigatable buildTree(int depth, int numberOfChildren) {
			if (swtTree == null) {
				swtTree = new Tree(currentShell, SWT.NONE);
				swtTree.setData("root"); //$NON-NLS-1$
				createSubNodes(swtTree, numberOfChildren, depth);
				viewer = new WrappableTreeViewer(swtTree);
				MockNavigatable navigatable = new MockNavigatable(viewer);
				return navigatable;
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
					itemRetreiver.put(increment, item);
					createSubNodes(item, numberOfChild, depth - 1);
				}
			}
		}

	}

	private Shell shell;

	protected TestContext testContext;

	private Display display;

	@Before
	public void before() {
		display = Display.getCurrent() != null ? Display.getCurrent() : Display.getDefault();
		shell = new Shell(display);
		testContext = new TestContext(shell);
	}

	@After
	public void after() {
		Tree swtTree = testContext.getTree();
		if (swtTree != null) {
			swtTree.dispose();
		}
		shell.dispose();
		display.dispose();
	}

	/**
	 * Inheriting class of {@link Navigatable} used for testing purpose.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static class MockNavigatable extends Navigatable {

		public MockNavigatable(WrappableTreeViewer viewer) {
			super(viewer, null);
		}

		// Makes it visible
		@Override
		protected Item getNextItem(Item previousItem) {
			return super.getNextItem(previousItem);
		}

		// Makes it visible
		@Override
		protected Item getPreviousItem(Item previousItem) {
			return super.getPreviousItem(previousItem);
		}
	}

	/**
	 * Test the iteration on an empty tree.
	 */
	@Test
	public void emptyTree() {
		MockNavigatable navigatable = testContext.buildTree(0, 0);
		assertAllNextItems(navigatable, testContext);
		assertAllPreviousItems(navigatable, testContext);
	}

	/**
	 * <p>
	 * Tests all possible iterations in a basic tree.
	 * 
	 * <pre>
	 * 0
	 *  \__1
	 * 
	 *  \__2
	 * 3
	 *  \__4
	 * 
	 *  \__5
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @throws Exception
	 */
	@Test
	public void littleTree() throws Exception {
		MockNavigatable navigatable = testContext.buildTree(2, 2);
		assertAllNextItems(navigatable, testContext);
		assertAllPreviousItems(navigatable, testContext);
	}

	/**
	 * Tests all possible iterations in a deep tree.
	 * 
	 * <pre>
	 * 0
	 * 	\__1
	 * 		\__2
	 * 			\__3
	 * 				\__4
	 * 					\__5
	 * 						\__6
	 * 							\__7
	 * 								\__8
	 * 									\__9
	 * </pre>
	 * 
	 * @throws Exception
	 */
	@Test
	public void deepTree() throws Exception {
		MockNavigatable navigatable = testContext.buildTree(10, 1);
		assertAllNextItems(navigatable, testContext);
		assertAllPreviousItems(navigatable, testContext);
	}

	/**
	 * Tests all possible iterations in a flat tree.
	 * 
	 * <pre>
	 * 0
	 * 1
	 * 2
	 * 3
	 * 4
	 * 5
	 * 6
	 * 7
	 * 8
	 * 10
	 * </pre>
	 * 
	 * @throws Exception
	 */
	@Test
	public void wideTree() throws Exception {
		MockNavigatable navigatable = testContext.buildTree(1, 10);
		assertAllNextItems(navigatable, testContext);
		assertAllPreviousItems(navigatable, testContext);
	}

	/**
	 * Test on a big tree.
	 * 
	 * @throws Exception
	 */
	@Test
	public void bigTree() throws Exception {
		MockNavigatable navigatable = testContext.buildTree(5, 5);
		// 0 = Root
		// 3593 = Level 1
		// 3843 = Level 2
		// 3874 = Level 3
		// 3850 = Level 4
		// 3904 = Last leaf
		// Only tests some use cases to avoid taking to much resources.
		assertNextIterationStartingOn(navigatable, testContext, 0, 3593, 3843, 3874, 3850, 3904);
		assertPreviousIterationStartingOn(navigatable, testContext, 0, 3593, 3843, 3874, 3850, 3904);
	}

	private void assertAllNextItems(MockNavigatable navigatable, TestContext context) {
		for (int startingElement = 0; startingElement <= context.getNumberOfNodes(); startingElement++) {
			assertNextIterations(navigatable, context, startingElement);
		}
	}

	private void assertNextIterationStartingOn(MockNavigatable navigatable, TestContext context,
			int... startingElements) {
		for (int startingElement : startingElements) {
			assertNextIterations(navigatable, context, startingElement);
		}
	}

	private void assertNextIterations(MockNavigatable navigatable, TestContext context, int startingElement) {
		Item previousSelection = context.getItem(startingElement);
		for (int expectedElement = startingElement + 1; expectedElement <= context.getNumberOfNodes(); expectedElement++) {
			Item nextItem = navigatable.getNextItem(previousSelection);
			StringBuilder messageBuilder = new StringBuilder();
			messageBuilder.append("Error with configuration: Starting iteration point=").append( //$NON-NLS-1$
					startingElement).append(", previsous item=").append(previousSelection.getText()); //$NON-NLS-1$
			assertNotNull(messageBuilder.toString(), nextItem);
			assertEquals(messageBuilder.toString(), expectedElement, Integer.valueOf(nextItem.getText())
					.intValue());
			previousSelection = nextItem;
		}
		assertNull(navigatable.getNextItem(previousSelection));
	}

	private void assertAllPreviousItems(MockNavigatable navigatable, TestContext context) {
		for (int startingElement = 0; startingElement <= context.getNumberOfNodes(); startingElement++) {
			assertPreviousIterations(navigatable, context, startingElement);
		}
	}

	private void assertPreviousIterationStartingOn(MockNavigatable navigatable, TestContext context,
			int... startingElements) {
		for (int startingElement : startingElements) {
			assertPreviousIterations(navigatable, context, startingElement);
		}
	}

	private void assertPreviousIterations(MockNavigatable navigatable, TestContext context,
			int startingElement) {
		Item previousSelection = context.getItem(startingElement);
		for (int expectedElement = startingElement - 1; expectedElement >= 0; expectedElement--) {
			Item previousItem = navigatable.getPreviousItem(previousSelection);
			StringBuilder messageBuilder = new StringBuilder();
			messageBuilder.append("Error with configuration: Starting iteration point=").append( //$NON-NLS-1$
					startingElement).append(", previsous item=").append(previousSelection.getText()); //$NON-NLS-1$
			assertNotNull(messageBuilder.toString(), previousItem);
			assertEquals(messageBuilder.toString(), expectedElement, Integer.valueOf(previousItem.getText())
					.intValue());
			previousSelection = previousItem;
		}
		assertNull(navigatable.getPreviousItem(previousSelection));
	}

	/**
	 * Utils method used to print a testContext.
	 * 
	 * @param aTree
	 * @return
	 */
	protected static String getTreeString(Tree aTree) {
		TreeItem[] children = aTree.getItems();
		StringBuilder treeString = new StringBuilder();
		for (TreeItem item : children) {
			appendChildrenInARow(item, treeString, 1);
		}
		return treeString.toString();
	}

	@SuppressWarnings("nls")
	private static void appendChildrenInARow(TreeItem item, StringBuilder builder, int tab) {
		builder.append(item.getText());

		for (Item child : item.getItems()) {
			builder.append("\n").append(Strings.repeat("\t", tab)).append("\\__");
			appendChildrenInARow((TreeItem)child, builder, tab + 1);
			builder.append("\n");
		}

	}

}

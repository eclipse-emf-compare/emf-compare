/*******************************************************************************
 * Copyright (c) 2014, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Michael Borkowski - extraction of nested classes, additional tests
 *     Martin Fleck - bug 518572
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Strings;

import org.eclipse.compare.INavigatable;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.Navigatable;
import org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.TestContext.TestNavigatable;
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
		testContext.dispose();
		shell.dispose();
	}

	/**
	 * Test the iteration on an empty tree.
	 */
	@Test
	public void emptyTree() {
		TestNavigatable navigatable = testContext.buildTree(0, 0);
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
	 * </p>
	 * 
	 * @throws Exception
	 */
	@Test
	public void littleTree() throws Exception {
		TestNavigatable navigatable = testContext.buildTree(2, 2);
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
		TestNavigatable navigatable = testContext.buildTree(10, 1);
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
		TestNavigatable navigatable = testContext.buildTree(1, 10);
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
		TestNavigatable navigatable = testContext.buildTree(5, 5);
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

	@Test
	public void testSelectNextChange() {
		TestNavigatable navigatable = testContext.buildTree(1, 2, false);
		navigatable.selectChange(INavigatable.NEXT_CHANGE);
		assertSelectedItemIndex(1);
	}

	@Test
	public void testSelectPreviousChange() {
		TestNavigatable navigatable = testContext.buildTree(1, 3, false);
		navigatable.selectChange(INavigatable.NEXT_CHANGE);
		navigatable.selectChange(INavigatable.NEXT_CHANGE);
		navigatable.selectChange(INavigatable.PREVIOUS_CHANGE);
		assertSelectedItemIndex(1);
	}

	private void assertSelectedItemIndex(int index) {
		Object data = testContext.getElement(index);
		assertNotNull(data);
		Object[] currentSelection = testContext.getCurrentSelection();
		assertEquals(1, currentSelection.length);
		assertTrue(data == currentSelection[0]);
	}

	private void assertAllNextItems(TestNavigatable navigatable, TestContext context) {
		for (int startingElement = 0; startingElement <= context.getNumberOfNodes(); startingElement++) {
			assertNextIterations(navigatable, context, startingElement);
		}
	}

	private void assertNextIterationStartingOn(TestNavigatable navigatable, TestContext context,
			int... startingElements) {
		for (int startingElement : startingElements) {
			assertNextIterations(navigatable, context, startingElement);
		}
	}

	private void assertNextIterations(TestNavigatable navigatable, TestContext context, int startingElement) {
		Object previousSelection = context.getElement(startingElement);
		for (int expectedElement = startingElement + 1; expectedElement <= context
				.getNumberOfNodes(); expectedElement++) {
			Object nextItem = navigatable.getNextItem(previousSelection);
			StringBuilder messageBuilder = new StringBuilder();
			messageBuilder.append("Error with configuration: Starting iteration point=").append( //$NON-NLS-1$
					startingElement).append(", previous item=").append(previousSelection.toString()); //$NON-NLS-1$
			assertNotNull(messageBuilder.toString(), nextItem);
			assertEquals(messageBuilder.toString(), expectedElement,
					Integer.valueOf(nextItem.toString()).intValue());
			previousSelection = nextItem;
		}
		assertNull(navigatable.getNextItem(previousSelection));
	}

	private void assertAllPreviousItems(TestNavigatable navigatable, TestContext context) {
		for (int startingElement = 0; startingElement <= context.getNumberOfNodes(); startingElement++) {
			assertPreviousIterations(navigatable, context, startingElement);
		}
	}

	private void assertPreviousIterationStartingOn(TestNavigatable navigatable, TestContext context,
			int... startingElements) {
		for (int startingElement : startingElements) {
			assertPreviousIterations(navigatable, context, startingElement);
		}
	}

	private void assertPreviousIterations(TestNavigatable navigatable, TestContext context,
			int startingElement) {
		Object previousSelection = context.getElement(startingElement);
		for (int expectedElement = startingElement - 1; expectedElement >= 0; expectedElement--) {
			Object previousItem = navigatable.getPreviousItem(previousSelection);
			StringBuilder messageBuilder = new StringBuilder();
			messageBuilder.append("Error with configuration: Starting iteration point=").append( //$NON-NLS-1$
					startingElement).append(", previsous item=").append(previousSelection.toString()); //$NON-NLS-1$
			assertNotNull(messageBuilder.toString(), previousItem);
			assertEquals(messageBuilder.toString(), expectedElement,
					Integer.valueOf(previousItem.toString()).intValue());
			previousSelection = previousItem;
		}
		assertRoot(context, navigatable.getPreviousItem(previousSelection));
	}

	protected void assertRoot(TestContext context, Object object) {
		assertEquals(context.getRoot(), object);
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

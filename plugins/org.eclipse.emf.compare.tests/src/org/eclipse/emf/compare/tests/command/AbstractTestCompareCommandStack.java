/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.compare.command.ICompareCommandStack;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractTestCompareCommandStack {

	private ICompareCommandStack commandStack;

	private MockCompareCommand leftToRight1;

	private MockCompareCommand leftToRight2;

	private MockCompareCommand leftToRight3;

	private MockCompareCommand rightToLeft1;

	private MockCompareCommand rightToLeft2;

	private MockCompareCommand rightToLeft3;

	@Before
	public void before() {
		commandStack = createCommandStack();

		leftToRight1 = new MockCompareCommand(true);
		leftToRight2 = new MockCompareCommand(true);
		leftToRight3 = new MockCompareCommand(true);

		rightToLeft1 = new MockCompareCommand(false);
		rightToLeft2 = new MockCompareCommand(false);
		rightToLeft3 = new MockCompareCommand(false);
	}

	/**
	 * @return the commandStack
	 */
	protected abstract ICompareCommandStack createCommandStack();

	@Test
	public void testInitState() {
		assertEquals(null, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(null, commandStack.getUndoCommand());
		assertFalse(commandStack.canRedo());
		assertFalse(commandStack.canUndo());
		assertFalse(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteLTROnce() {
		commandStack.execute(leftToRight1);

		assertEquals(leftToRight1, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(leftToRight1, commandStack.getUndoCommand());
		assertFalse(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertFalse(commandStack.isLeftSaveNeeded());
		assertTrue(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteLTROnceUndo() {
		commandStack.execute(leftToRight1);
		commandStack.undo();

		assertEquals(leftToRight1, commandStack.getMostRecentCommand());
		assertEquals(leftToRight1, commandStack.getRedoCommand());
		assertEquals(null, commandStack.getUndoCommand());
		assertTrue(commandStack.canRedo());
		assertFalse(commandStack.canUndo());
		assertFalse(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteLTROnceUndoRedo() {
		commandStack.execute(leftToRight1);
		commandStack.undo();
		commandStack.redo();

		assertEquals(leftToRight1, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(leftToRight1, commandStack.getUndoCommand());
		assertFalse(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertFalse(commandStack.isLeftSaveNeeded());
		assertTrue(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteLTRTwice() {
		commandStack.execute(leftToRight2);
		commandStack.execute(leftToRight1);

		assertEquals(leftToRight1, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(leftToRight1, commandStack.getUndoCommand());
		assertFalse(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertFalse(commandStack.isLeftSaveNeeded());
		assertTrue(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteLTRTwiceUndo() {
		commandStack.execute(leftToRight1);
		commandStack.execute(leftToRight2);
		commandStack.undo();

		assertEquals(leftToRight2, commandStack.getMostRecentCommand());
		assertEquals(leftToRight2, commandStack.getRedoCommand());
		assertEquals(leftToRight1, commandStack.getUndoCommand());
		assertTrue(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertFalse(commandStack.isLeftSaveNeeded());
		assertTrue(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteLTRTwiceUndoTwice() {
		commandStack.execute(leftToRight1);
		commandStack.execute(leftToRight2);
		commandStack.undo();
		commandStack.undo();

		assertEquals(leftToRight1, commandStack.getMostRecentCommand());
		assertEquals(leftToRight1, commandStack.getRedoCommand());
		assertEquals(null, commandStack.getUndoCommand());
		assertTrue(commandStack.canRedo());
		assertFalse(commandStack.canUndo());
		assertFalse(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteLTRTwiceUndoTwiceRedo() {
		commandStack.execute(leftToRight1);
		commandStack.execute(leftToRight2);
		commandStack.undo();
		commandStack.undo();
		commandStack.redo();

		assertEquals(leftToRight1, commandStack.getMostRecentCommand());
		assertEquals(leftToRight2, commandStack.getRedoCommand());
		assertEquals(leftToRight1, commandStack.getUndoCommand());
		assertTrue(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertFalse(commandStack.isLeftSaveNeeded());
		assertTrue(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteLTRTwiceUndoTwiceRedoTwice() {
		commandStack.execute(leftToRight1);
		commandStack.execute(leftToRight2);
		commandStack.undo();
		commandStack.undo();
		commandStack.redo();
		commandStack.redo();

		assertEquals(leftToRight2, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(leftToRight2, commandStack.getUndoCommand());
		assertFalse(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertFalse(commandStack.isLeftSaveNeeded());
		assertTrue(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteLTRTwiceUndoExecuteLTR() {
		commandStack.execute(leftToRight1);
		commandStack.execute(leftToRight2);
		commandStack.undo();
		commandStack.execute(leftToRight3);

		assertEquals(leftToRight3, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(leftToRight3, commandStack.getUndoCommand());
		assertFalse(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertFalse(commandStack.isLeftSaveNeeded());
		assertTrue(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteLTRTwiceUndoExecuteLTRUndo() {
		commandStack.execute(leftToRight1);
		commandStack.execute(leftToRight2);
		commandStack.undo();
		commandStack.execute(leftToRight3);
		commandStack.undo();

		assertEquals(leftToRight3, commandStack.getMostRecentCommand());
		assertEquals(leftToRight3, commandStack.getRedoCommand());
		assertEquals(leftToRight1, commandStack.getUndoCommand());
		assertTrue(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertFalse(commandStack.isLeftSaveNeeded());
		assertTrue(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLOnce() {
		commandStack.execute(rightToLeft1);

		assertEquals(rightToLeft1, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(rightToLeft1, commandStack.getUndoCommand());
		assertFalse(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertTrue(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLOnceUndo() {
		commandStack.execute(rightToLeft1);
		commandStack.undo();

		assertEquals(rightToLeft1, commandStack.getMostRecentCommand());
		assertEquals(rightToLeft1, commandStack.getRedoCommand());
		assertEquals(null, commandStack.getUndoCommand());
		assertTrue(commandStack.canRedo());
		assertFalse(commandStack.canUndo());
		assertFalse(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLOnceUndoRedo() {
		commandStack.execute(rightToLeft1);
		commandStack.undo();
		commandStack.redo();

		assertEquals(rightToLeft1, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(rightToLeft1, commandStack.getUndoCommand());
		assertFalse(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertTrue(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLTwice() {
		commandStack.execute(rightToLeft2);
		commandStack.execute(rightToLeft1);

		assertEquals(rightToLeft1, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(rightToLeft1, commandStack.getUndoCommand());
		assertFalse(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertTrue(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLTwiceUndo() {
		commandStack.execute(rightToLeft1);
		commandStack.execute(rightToLeft2);
		commandStack.undo();

		assertEquals(rightToLeft2, commandStack.getMostRecentCommand());
		assertEquals(rightToLeft2, commandStack.getRedoCommand());
		assertEquals(rightToLeft1, commandStack.getUndoCommand());
		assertTrue(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertTrue(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLTwiceUndoTwice() {
		commandStack.execute(rightToLeft1);
		commandStack.execute(rightToLeft2);
		commandStack.undo();
		commandStack.undo();

		assertEquals(rightToLeft1, commandStack.getMostRecentCommand());
		assertEquals(rightToLeft1, commandStack.getRedoCommand());
		assertEquals(null, commandStack.getUndoCommand());
		assertTrue(commandStack.canRedo());
		assertFalse(commandStack.canUndo());
		assertFalse(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLTwiceUndoTwiceRedo() {
		commandStack.execute(rightToLeft1);
		commandStack.execute(rightToLeft2);
		commandStack.undo();
		commandStack.undo();
		commandStack.redo();

		assertEquals(rightToLeft1, commandStack.getMostRecentCommand());
		assertEquals(rightToLeft2, commandStack.getRedoCommand());
		assertEquals(rightToLeft1, commandStack.getUndoCommand());
		assertTrue(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertTrue(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLTwiceUndoTwiceRedoTwice() {
		commandStack.execute(rightToLeft1);
		commandStack.execute(rightToLeft2);
		commandStack.undo();
		commandStack.undo();
		commandStack.redo();
		commandStack.redo();

		assertEquals(rightToLeft2, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(rightToLeft2, commandStack.getUndoCommand());
		assertFalse(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertTrue(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLTwiceUndoExecuteRTL() {
		commandStack.execute(rightToLeft1);
		commandStack.execute(rightToLeft2);
		commandStack.undo();
		commandStack.execute(rightToLeft3);

		assertEquals(rightToLeft3, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(rightToLeft3, commandStack.getUndoCommand());
		assertFalse(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertTrue(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLTwiceUndoExecuteRTLUndo() {
		commandStack.execute(rightToLeft1);
		commandStack.execute(rightToLeft2);
		commandStack.undo();
		commandStack.execute(rightToLeft3);
		commandStack.undo();

		assertEquals(rightToLeft3, commandStack.getMostRecentCommand());
		assertEquals(rightToLeft3, commandStack.getRedoCommand());
		assertEquals(rightToLeft1, commandStack.getUndoCommand());
		assertTrue(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertTrue(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteLTRExecuteRTL() {
		commandStack.execute(leftToRight1);
		commandStack.execute(rightToLeft1);

		assertEquals(rightToLeft1, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(rightToLeft1, commandStack.getUndoCommand());
		assertFalse(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertTrue(commandStack.isLeftSaveNeeded());
		assertTrue(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteLTRExecuteRTLUndo() {
		commandStack.execute(leftToRight1);
		commandStack.execute(rightToLeft1);
		commandStack.undo();

		assertEquals(rightToLeft1, commandStack.getMostRecentCommand());
		assertEquals(rightToLeft1, commandStack.getRedoCommand());
		assertEquals(leftToRight1, commandStack.getUndoCommand());
		assertTrue(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertFalse(commandStack.isLeftSaveNeeded());
		assertTrue(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLExecuteLTRUndo() {
		commandStack.execute(rightToLeft1);
		commandStack.execute(leftToRight1);
		commandStack.undo();

		assertEquals(leftToRight1, commandStack.getMostRecentCommand());
		assertEquals(leftToRight1, commandStack.getRedoCommand());
		assertEquals(rightToLeft1, commandStack.getUndoCommand());
		assertTrue(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertTrue(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLExecuteLTRUndoUndo() {
		commandStack.execute(rightToLeft1);
		commandStack.execute(leftToRight1);
		commandStack.undo();
		commandStack.undo();

		assertEquals(rightToLeft1, commandStack.getMostRecentCommand());
		assertEquals(rightToLeft1, commandStack.getRedoCommand());
		assertEquals(null, commandStack.getUndoCommand());
		assertTrue(commandStack.canRedo());
		assertFalse(commandStack.canUndo());
		assertFalse(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLExecuteLTRUndoRedo() {
		commandStack.execute(rightToLeft1);
		commandStack.execute(leftToRight1);
		commandStack.undo();
		commandStack.redo();

		assertEquals(leftToRight1, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(leftToRight1, commandStack.getUndoCommand());
		assertFalse(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertTrue(commandStack.isLeftSaveNeeded());
		assertTrue(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLExecuteLTRUndoExecuteLTR() {
		commandStack.execute(rightToLeft1);
		commandStack.execute(leftToRight1);
		commandStack.undo();
		commandStack.execute(leftToRight2);

		assertEquals(leftToRight2, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(leftToRight2, commandStack.getUndoCommand());
		assertFalse(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertTrue(commandStack.isLeftSaveNeeded());
		assertTrue(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLExecuteLTRUndoExecuteRTL() {
		commandStack.execute(rightToLeft1);
		commandStack.execute(leftToRight1);
		commandStack.undo();
		commandStack.execute(rightToLeft2);

		assertEquals(rightToLeft2, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(rightToLeft2, commandStack.getUndoCommand());
		assertFalse(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertTrue(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLExecuteLTRUndoExecuteRTLUndo() {
		commandStack.execute(rightToLeft1);
		commandStack.execute(leftToRight1);
		commandStack.undo();
		commandStack.execute(rightToLeft2);
		commandStack.undo();

		assertEquals(rightToLeft2, commandStack.getMostRecentCommand());
		assertEquals(rightToLeft2, commandStack.getRedoCommand());
		assertEquals(rightToLeft1, commandStack.getUndoCommand());
		assertTrue(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertTrue(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLExecuteLTRUndoExecuteRTLUndoUndo() {
		commandStack.execute(rightToLeft1);
		commandStack.execute(leftToRight1);
		commandStack.undo();
		commandStack.execute(rightToLeft2);
		commandStack.undo();
		commandStack.undo();

		assertEquals(rightToLeft1, commandStack.getMostRecentCommand());
		assertEquals(rightToLeft1, commandStack.getRedoCommand());
		assertEquals(null, commandStack.getUndoCommand());
		assertTrue(commandStack.canRedo());
		assertFalse(commandStack.canUndo());
		assertFalse(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLExecuteLTRUndoExecuteLTRUndo() {
		commandStack.execute(rightToLeft1);
		commandStack.execute(leftToRight1);
		commandStack.undo();
		commandStack.execute(leftToRight2);
		commandStack.undo();

		assertEquals(leftToRight2, commandStack.getMostRecentCommand());
		assertEquals(leftToRight2, commandStack.getRedoCommand());
		assertEquals(rightToLeft1, commandStack.getUndoCommand());
		assertTrue(commandStack.canRedo());
		assertTrue(commandStack.canUndo());
		assertTrue(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}

	@Test
	public void testExecuteRTLExecuteLTRUndoExecuteLTRUndoUndo() {
		commandStack.execute(rightToLeft1);
		commandStack.execute(leftToRight1);
		commandStack.undo();
		commandStack.execute(leftToRight2);
		commandStack.undo();
		commandStack.undo();

		assertEquals(rightToLeft1, commandStack.getMostRecentCommand());
		assertEquals(rightToLeft1, commandStack.getRedoCommand());
		assertEquals(null, commandStack.getUndoCommand());
		assertTrue(commandStack.canRedo());
		assertFalse(commandStack.canUndo());
		assertFalse(commandStack.isLeftSaveNeeded());
		assertFalse(commandStack.isRightSaveNeeded());
	}
}

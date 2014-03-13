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

import org.eclipse.emf.compare.command.impl.CompareCommandStack.CompareSideCommandStack;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TestCompareSideCommandStack {

	private MockCompareCommand leftToRight1;

	private MockCompareCommand leftToRight2;

	private CompareSideCommandStack commandStack;

	private MockCompareCommand leftToRight3;

	@Before
	public void before() {
		commandStack = new CompareSideCommandStack();
		leftToRight1 = new MockCompareCommand(true);
		leftToRight2 = new MockCompareCommand(true);
		leftToRight3 = new MockCompareCommand(true);
	}

	@Test
	public void testInitState() {
		assertEquals(null, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(null, commandStack.getUndoCommand());
		assertFalse(commandStack.isSaveNeeded());
	}

	@Test
	public void testExecutedOnce() {
		commandStack.executed(leftToRight1);

		assertEquals(leftToRight1, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(leftToRight1, commandStack.getUndoCommand());
		assertTrue(commandStack.isSaveNeeded());
	}

	@Test
	public void testExecutedOnceUndo() {
		commandStack.executed(leftToRight1);
		commandStack.undone();

		assertEquals(leftToRight1, commandStack.getMostRecentCommand());
		assertEquals(leftToRight1, commandStack.getRedoCommand());
		assertEquals(null, commandStack.getUndoCommand());
		assertFalse(commandStack.isSaveNeeded());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testExecutedOnceUndoTwice() {
		commandStack.executed(leftToRight1);
		commandStack.undone();
		commandStack.undone();
	}

	@Test
	public void testExecutedOnceUndoRedo() {
		commandStack.executed(leftToRight1);
		commandStack.undone();
		commandStack.redone();

		assertEquals(leftToRight1, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(leftToRight1, commandStack.getUndoCommand());
		assertTrue(commandStack.isSaveNeeded());
	}

	@Test
	public void testExecutedTwice() {
		commandStack.executed(leftToRight2);
		commandStack.executed(leftToRight1);

		assertEquals(leftToRight1, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(leftToRight1, commandStack.getUndoCommand());
		assertTrue(commandStack.isSaveNeeded());
	}

	@Test
	public void testExecutedTwiceUndo() {
		commandStack.executed(leftToRight1);
		commandStack.executed(leftToRight2);
		commandStack.undone();

		assertEquals(leftToRight2, commandStack.getMostRecentCommand());
		assertEquals(leftToRight2, commandStack.getRedoCommand());
		assertEquals(leftToRight1, commandStack.getUndoCommand());
		assertTrue(commandStack.isSaveNeeded());
	}

	@Test
	public void testExecutedTwiceUndoTwice() {
		commandStack.executed(leftToRight1);
		commandStack.executed(leftToRight2);
		commandStack.undone();
		commandStack.undone();

		assertEquals(leftToRight1, commandStack.getMostRecentCommand());
		assertEquals(leftToRight1, commandStack.getRedoCommand());
		assertEquals(null, commandStack.getUndoCommand());
		assertFalse(commandStack.isSaveNeeded());
	}

	@Test
	public void testExecutedTwiceUndoTwiceRedo() {
		commandStack.executed(leftToRight1);
		commandStack.executed(leftToRight2);
		commandStack.undone();
		commandStack.undone();
		commandStack.redone();

		assertEquals(leftToRight1, commandStack.getMostRecentCommand());
		assertEquals(leftToRight2, commandStack.getRedoCommand());
		assertEquals(leftToRight1, commandStack.getUndoCommand());
		assertTrue(commandStack.isSaveNeeded());
	}

	@Test
	public void testExecutedTwiceUndoTwiceRedoTwice() {
		commandStack.executed(leftToRight1);
		commandStack.executed(leftToRight2);
		commandStack.undone();
		commandStack.undone();
		commandStack.redone();
		commandStack.redone();

		assertEquals(leftToRight2, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(leftToRight2, commandStack.getUndoCommand());
		assertTrue(commandStack.isSaveNeeded());
	}

	@Test
	public void testExecutedTwiceUndoExecuted() {
		commandStack.executed(leftToRight1);
		commandStack.executed(leftToRight2);
		commandStack.undone();
		commandStack.executed(leftToRight3);

		assertEquals(leftToRight3, commandStack.getMostRecentCommand());
		assertEquals(null, commandStack.getRedoCommand());
		assertEquals(leftToRight3, commandStack.getUndoCommand());
		assertTrue(commandStack.isSaveNeeded());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testExecutedTwiceUndoExecutedRedo() {
		commandStack.executed(leftToRight1);
		commandStack.executed(leftToRight2);
		commandStack.undone();
		commandStack.executed(leftToRight3);
		commandStack.redone();
	}

	@Test
	public void testExecutedTwiceUndoExecutedUndo() {
		commandStack.executed(leftToRight1);
		commandStack.executed(leftToRight2);
		commandStack.undone();
		commandStack.executed(leftToRight3);
		commandStack.undone();

		assertEquals(leftToRight3, commandStack.getMostRecentCommand());
		assertEquals(leftToRight3, commandStack.getRedoCommand());
		assertEquals(leftToRight1, commandStack.getUndoCommand());
		assertTrue(commandStack.isSaveNeeded());
	}
}

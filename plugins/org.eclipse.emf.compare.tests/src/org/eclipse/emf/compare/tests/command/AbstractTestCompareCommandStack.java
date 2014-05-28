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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.EventObject;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.command.ICompareCommandStack;
import org.eclipse.emf.compare.tests.nodes.NodeMultipleContainment;
import org.eclipse.emf.compare.tests.nodes.NodesFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
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

	private NodeMultipleContainment leftNode;

	private NodeMultipleContainment rightNode;

	@Before
	public void before() {
		ResourceSet leftResourceSet = new ResourceSetImpl();
		Resource leftResource = new ResourceImpl(URI.createURI("left.model")); //$NON-NLS-1$
		leftResourceSet.getResources().add(leftResource);
		leftNode = NodesFactory.eINSTANCE.createNodeMultipleContainment();
		leftResource.getContents().add(leftNode);

		ResourceSet rightResourceSet = new ResourceSetImpl();
		Resource rightResource = new ResourceImpl(URI.createURI("right.model")); //$NON-NLS-1$
		rightResourceSet.getResources().add(rightResource);
		rightNode = NodesFactory.eINSTANCE.createNodeMultipleContainment();
		rightResource.getContents().add(rightNode);

		commandStack = createCommandStack(leftResourceSet, rightResourceSet);

		leftToRight1 = createMockCommand(true);
		leftToRight2 = createMockCommand(true);
		leftToRight3 = createMockCommand(true);

		rightToLeft1 = createMockCommand(false);
		rightToLeft2 = createMockCommand(false);
		rightToLeft3 = createMockCommand(false);
	}

	protected MockCompareCommand createMockCommand(final boolean leftToRight) {
		return new MockCompareCommand(leftToRight) {
			@Override
			public void execute() {
				if (leftToRight) {
					rightNode.setName("rightNode " + System.nanoTime()); //$NON-NLS-1$
				} else {
					leftNode.setName("leftNode" + System.nanoTime()); //$NON-NLS-1$
				}
			}
		};
	}

	/**
	 * @param rightResourceSet
	 * @param leftResourceSet
	 * @return the commandStack
	 */
	protected abstract ICompareCommandStack createCommandStack(ResourceSet leftResourceSet,
			ResourceSet rightResourceSet);

	/**
	 * @return the commandStack
	 */
	protected ICompareCommandStack getCommandStack() {
		return commandStack;
	}

	/**
	 * @return the leftNode
	 */
	protected final NodeMultipleContainment getLeftNode() {
		return leftNode;
	}

	/**
	 * @return the rightNode
	 */
	protected final NodeMultipleContainment getRightNode() {
		return rightNode;
	}

	@Test
	public void testListener0() {
		final AtomicInteger changed = new AtomicInteger();
		commandStack.addCommandStackListener(new CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				changed.incrementAndGet();
			}
		});
		assertEquals(0, changed.get());
	}

	@Test
	public void testListener1() {
		final AtomicInteger changed = new AtomicInteger();
		commandStack.addCommandStackListener(new CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				assertTrue(commandStack.canUndo());
				assertFalse(commandStack.canRedo());
				changed.set(Integer.MAX_VALUE);
			}
		});
		commandStack.execute(leftToRight1);
		assertEquals(Integer.MAX_VALUE, changed.get());
	}

	@Test
	public void testListener2() {
		final AtomicInteger changed = new AtomicInteger();
		commandStack.execute(leftToRight1);

		commandStack.addCommandStackListener(new CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				assertFalse(commandStack.canUndo());
				assertTrue(commandStack.canRedo());
				changed.set(Integer.MAX_VALUE);
			}
		});
		commandStack.undo();
		assertEquals(Integer.MAX_VALUE, changed.get());
	}

	@Test
	public void testListener3() {
		final AtomicInteger changed = new AtomicInteger();
		commandStack.execute(leftToRight1);
		commandStack.undo();

		commandStack.addCommandStackListener(new CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				assertTrue(commandStack.canUndo());
				assertFalse(commandStack.canRedo());
				changed.set(Integer.MAX_VALUE);
			}
		});

		commandStack.redo();
		assertEquals(Integer.MAX_VALUE, changed.get());
	}

	@Test
	public void testListener4() {
		final AtomicInteger changed = new AtomicInteger();
		commandStack.addCommandStackListener(new CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				assertFalse(commandStack.canUndo());
				assertFalse(commandStack.canRedo());
				changed.set(Integer.MAX_VALUE);
			}
		});

		commandStack.flush();
		assertEquals(Integer.MAX_VALUE, changed.get());
	}

	@Test
	public void testListener5() {
		final AtomicInteger changed = new AtomicInteger();
		commandStack.execute(leftToRight1);
		commandStack.addCommandStackListener(new CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				assertFalse(commandStack.canUndo());
				assertFalse(commandStack.canRedo());
				changed.set(Integer.MAX_VALUE);
			}
		});

		commandStack.flush();
		assertEquals(Integer.MAX_VALUE, changed.get());
	}

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

		Command mostRecentCommand = commandStack.getMostRecentCommand();
		assertEquals(leftToRight2, mostRecentCommand);
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

	@Test
	public void testExecuteWithException() {
		Command command = new MockCompareCommand(true) {
			@Override
			public void execute() {
				throw new IllegalStateException();
			}
		};

		getCommandStack().execute(command);

		assertEquals(null, getCommandStack().getMostRecentCommand());
		assertEquals(null, getCommandStack().getRedoCommand());
		assertEquals(null, getCommandStack().getUndoCommand());
		assertFalse(getCommandStack().canRedo());
		assertFalse(getCommandStack().canUndo());
		assertFalse(getCommandStack().isLeftSaveNeeded());
		assertFalse(getCommandStack().isRightSaveNeeded());
	}

	@Test
	public void testExecuteWithException2() {
		Command command = new MockCompareCommand(true) {
			@Override
			public void execute() {
				getRightNode().setName("newValue"); //$NON-NLS-1$
			}
		};

		Command command2 = new MockCompareCommand(true) {
			@Override
			public void execute() {
				throw new IllegalStateException();
			}
		};

		getCommandStack().execute(command);
		assertEquals("newValue", getRightNode().getName()); //$NON-NLS-1$

		assertEquals(command, getCommandStack().getMostRecentCommand());

		getCommandStack().execute(command2);
		assertEquals("newValue", getRightNode().getName()); //$NON-NLS-1$

		assertEquals(null, getCommandStack().getMostRecentCommand());
		assertEquals(null, getCommandStack().getRedoCommand());
		assertEquals(command, getCommandStack().getUndoCommand());
		assertFalse(getCommandStack().canRedo());
		assertTrue(getCommandStack().canUndo());
		assertFalse(getCommandStack().isLeftSaveNeeded());
		assertTrue(getCommandStack().isRightSaveNeeded());
	}

	@Test
	public void testUndoWithException() {
		Command command = new MockCompareCommand(true) {
			@Override
			public void execute() {
				getRightNode().setName("newValue"); //$NON-NLS-1$
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.compare.tests.command.MockCompareCommand#undo()
			 */
			@Override
			public void undo() {
				throw new IllegalStateException();
			}
		};

		getCommandStack().execute(command);

		assertEquals("newValue", getRightNode().getName()); //$NON-NLS-1$
		assertEquals(command, getCommandStack().getMostRecentCommand());
		assertEquals(null, getCommandStack().getRedoCommand());
		assertEquals(command, getCommandStack().getUndoCommand());
		assertFalse(getCommandStack().canRedo());
		assertTrue(getCommandStack().canUndo());
		assertFalse(getCommandStack().isLeftSaveNeeded());
		assertTrue(getCommandStack().isRightSaveNeeded());

		getCommandStack().undo();

		assertEquals("newValue", getRightNode().getName()); //$NON-NLS-1$
		assertEquals(null, getCommandStack().getMostRecentCommand());
		assertEquals(null, getCommandStack().getRedoCommand());
		assertEquals(null, getCommandStack().getUndoCommand());
		assertFalse(getCommandStack().canRedo());
		assertFalse(getCommandStack().canUndo());
		assertFalse(getCommandStack().isLeftSaveNeeded());
		assertFalse(getCommandStack().isRightSaveNeeded());
	}

	@Test
	public void testRedoWithException() {
		Command command = new MockCompareCommand(true) {
			@Override
			public void execute() {
				getRightNode().setName("newValue"); //$NON-NLS-1$
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.compare.tests.command.MockCompareCommand#undo()
			 */
			@Override
			public void redo() {
				throw new IllegalStateException();
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.compare.tests.command.MockCompareCommand#undo()
			 */
			@Override
			public void undo() {
				getRightNode().setName("undo"); //$NON-NLS-1$
			}
		};

		getCommandStack().execute(command);

		assertEquals("newValue", getRightNode().getName()); //$NON-NLS-1$
		assertEquals(command, getCommandStack().getMostRecentCommand());
		assertEquals(null, getCommandStack().getRedoCommand());
		assertEquals(command, getCommandStack().getUndoCommand());
		assertFalse(getCommandStack().canRedo());
		assertTrue(getCommandStack().canUndo());
		assertFalse(getCommandStack().isLeftSaveNeeded());
		assertTrue(getCommandStack().isRightSaveNeeded());

		getCommandStack().undo();

		assertEquals("undo", getRightNode().getName()); //$NON-NLS-1$
		assertEquals(command, getCommandStack().getMostRecentCommand());
		assertEquals(command, getCommandStack().getRedoCommand());
		assertNull(getCommandStack().getUndoCommand());
		assertTrue(getCommandStack().canRedo());
		assertFalse(getCommandStack().canUndo());
		assertFalse(getCommandStack().isLeftSaveNeeded());
		assertFalse(getCommandStack().isRightSaveNeeded());

		getCommandStack().redo();

		assertEquals("undo", getRightNode().getName()); //$NON-NLS-1$
		assertEquals(null, getCommandStack().getMostRecentCommand());
		assertEquals(null, getCommandStack().getRedoCommand());
		assertEquals(null, getCommandStack().getUndoCommand());
		assertFalse(getCommandStack().canRedo());
		assertFalse(getCommandStack().canUndo());
		assertFalse(getCommandStack().isLeftSaveNeeded());
		assertFalse(getCommandStack().isRightSaveNeeded());
	}

	@Test
	public void testListenerWithException1() {
		final AtomicInteger changed = new AtomicInteger();
		getCommandStack().addCommandStackListener(new CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				assertFalse(getCommandStack().canUndo());
				assertFalse(getCommandStack().canRedo());
				assertNull(getCommandStack().getMostRecentCommand());
				changed.set(Integer.MAX_VALUE);
			}
		});
		getCommandStack().execute(new MockCompareCommand(true) {
			@Override
			public void execute() {
				throw new IllegalStateException();
			}
		});
		assertEquals(Integer.MAX_VALUE, changed.get());
	}

	@Test
	public void testListenerWithException2() {
		final AtomicInteger changed = new AtomicInteger();
		getCommandStack().execute(new MockCompareCommand(true) {
			@Override
			public void execute() {
				getRightNode().setName("it will crash at undo"); //$NON-NLS-1$
			}

			@Override
			public void undo() {
				throw new IllegalStateException();
			}
		});

		getCommandStack().addCommandStackListener(new CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				assertFalse(getCommandStack().canUndo());
				assertFalse(getCommandStack().canRedo());
				assertNull(getCommandStack().getMostRecentCommand());
				changed.set(Integer.MAX_VALUE);
			}
		});
		getCommandStack().undo();
		assertEquals(Integer.MAX_VALUE, changed.get());
	}

	@Test
	public void testListenerWithException3() {
		final AtomicInteger changed = new AtomicInteger();
		getCommandStack().execute(new MockCompareCommand(true) {
			@Override
			public void execute() {
				getRightNode().setName("it will crash at redo"); //$NON-NLS-1$
			}

			@Override
			public void redo() {
				throw new IllegalStateException();
			}
		});
		getCommandStack().undo();

		getCommandStack().addCommandStackListener(new CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				assertFalse(getCommandStack().canUndo());
				assertFalse(getCommandStack().canRedo());
				assertNull(getCommandStack().getMostRecentCommand());
				changed.set(Integer.MAX_VALUE);
			}
		});

		getCommandStack().redo();
		assertEquals(Integer.MAX_VALUE, changed.get());
	}
}

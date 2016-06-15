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

import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.compare.command.ICompareCommandStack;
import org.eclipse.emf.compare.command.impl.TransactionalDualCompareCommandStack;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalCommandStack;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.AbstractTransactionalCommandStack;
import org.eclipse.emf.transaction.impl.EMFCommandTransaction;
import org.eclipse.emf.transaction.impl.TransactionalCommandStackImpl;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.junit.Test;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TestTransactionalDualCompareCommandStack extends AbstractTestCompareCommandStack {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.tests.command.AbstractTestCompareCommandStack#createCommandStack()
	 */
	@Override
	protected ICompareCommandStack createCommandStack(ResourceSet leftResourceSet,
			ResourceSet rightResourceSet) {
		ComposedAdapterFactory leftAdapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		TransactionalCommandStack leftCommandStack = new TransactionalCommandStackNoValidation();
		TransactionalEditingDomain leftEditingDomain = new TransactionalEditingDomainImpl(leftAdapterFactory,
				leftCommandStack, leftResourceSet);
		leftResourceSet.eAdapters()
				.add(new AdapterFactoryEditingDomain.EditingDomainProvider(leftEditingDomain));

		ComposedAdapterFactory rightAdapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		TransactionalCommandStack rightCommandStack = new TransactionalCommandStackNoValidation();
		TransactionalEditingDomain rightEditingDomain = new TransactionalEditingDomainImpl(
				rightAdapterFactory, rightCommandStack, rightResourceSet);
		rightResourceSet.eAdapters()
				.add(new AdapterFactoryEditingDomain.EditingDomainProvider(rightEditingDomain));

		return new TransactionalDualCompareCommandStack(
				(AbstractTransactionalCommandStack)leftEditingDomain.getCommandStack(),
				(AbstractTransactionalCommandStack)rightEditingDomain.getCommandStack());
	}

	@Test
	public void testExecuteSetNameWithException() {
		Command command = new MockCompareCommand(true) {
			@Override
			public void execute() {
				getRightNode().setName("newValue"); //$NON-NLS-1$
				throw new IllegalStateException();
			}
		};

		getCommandStack().execute(command);

		assertNotEquals("newValue", getRightNode().getName()); //$NON-NLS-1$
		assertNull(getRightNode().getName());
		assertEquals(null, getCommandStack().getMostRecentCommand());
		assertEquals(null, getCommandStack().getRedoCommand());
		assertEquals(null, getCommandStack().getUndoCommand());
		assertFalse(getCommandStack().canRedo());
		assertFalse(getCommandStack().canUndo());
		assertFalse(getCommandStack().isLeftSaveNeeded());
		assertFalse(getCommandStack().isRightSaveNeeded());
	}

	@Test
	public void testExecuteSetNameWithException2() {
		Command command = new MockCompareCommand(true) {
			@Override
			public void execute() {
				getRightNode().setName("newValue"); //$NON-NLS-1$
			}
		};

		Command command2 = new MockCompareCommand(true) {
			@Override
			public void execute() {
				getRightNode().setName("newValue2"); //$NON-NLS-1$
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

	/**
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private final class TransactionalCommandStackNoValidation extends TransactionalCommandStackImpl {
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.transaction.impl.TransactionalCommandStackImpl#createTransaction(org.eclipse.emf.common.command.Command,
		 *      java.util.Map)
		 */
		@Override
		public EMFCommandTransaction createTransaction(Command command, Map<?, ?> options)
				throws InterruptedException {
			Map<Object, Object> newOptions;
			if (options == null) {
				newOptions = newHashMap();
			} else {
				newOptions = newHashMap(options);
			}
			newOptions.put(Transaction.OPTION_NO_VALIDATION, Boolean.TRUE);
			return super.createTransaction(command, newOptions);
		}
	}
}

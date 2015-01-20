/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.editor;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.command.ICompareCommandStack;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;

/**
 * Abstract subclass of {@link CompareEditorInput} to be use to open CompareEditor with results of EMF
 * Compare.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractEMFCompareEditorInput extends CompareEditorInput {

	private final ICompareEditingDomain editingDomain;

	private final AdapterFactory adapterFactory;

	/**
	 * Constructor that call super with an unmodified {@link CompareConfiguration}.
	 * 
	 * @param configuration
	 *            the compare configuration as required by
	 *            {@link CompareEditorInput#CompareEditorInput(CompareConfiguration)}.
	 * @param editingDomain
	 *            the editing domain required to execute merge command. It must not be null.
	 * @param adapterFactory
	 *            the adapter factory that will be used to adapt EObject to displayable elements. It must not
	 *            be null.
	 * @throws NullPointerException
	 *             if {@code editingDomain} or {@code adapterFactory} is null.
	 */
	public AbstractEMFCompareEditorInput(EMFCompareConfiguration configuration,
			ICompareEditingDomain editingDomain, AdapterFactory adapterFactory) {
		super(configuration);
		this.editingDomain = checkNotNull(editingDomain);
		this.adapterFactory = checkNotNull(adapterFactory);
	}

	/**
	 * Returns the adapter factory as given to the constructor.
	 * 
	 * @return the adapter factory, never null.
	 */
	protected AdapterFactory getAdapterFactory() {
		return adapterFactory;
	}

	/**
	 * Returns the editing domain as given to the constructor.
	 * 
	 * @return the editing domain, never null.
	 */
	protected ICompareEditingDomain getEditingDomain() {
		return editingDomain;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * It will undo all operations executed on the command stack until {@link ICompareCommandStack#canUndo()
	 * no more can be undone}.
	 * 
	 * @see org.eclipse.compare.CompareEditorInput#cancelPressed()
	 */
	@Override
	public void cancelPressed() {
		while (editingDomain.getCommandStack().canUndo()) {
			editingDomain.getCommandStack().undo();
		}
		super.cancelPressed();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.CompareEditorInput#prepareInput(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected final Object prepareInput(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		getCompareConfiguration().setEditingDomain(editingDomain);
		return doPrepareInput(monitor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.CompareEditorInput#getCompareConfiguration()
	 */
	@Override
	public EMFCompareConfiguration getCompareConfiguration() {
		return (EMFCompareConfiguration)super.getCompareConfiguration();
	}

	/**
	 * Runs the compare operation and returns the compare result. If <code>null</code> is returned no
	 * differences were found and no compare editor needs to be opened. Progress should be reported to the
	 * given progress monitor. A request to cancel the operation should be honored and acknowledged by
	 * throwing <code>InterruptedException</code>.
	 * <p>
	 * Note: this method is typically called in a modal context thread which doesn't have a Display assigned.
	 * Implementors of this method shouldn't therefore allocated any SWT resources in this method.
	 * 
	 * @param monitor
	 *            the progress monitor to use to display progress and receive requests for cancellation
	 * @return the result of the compare operation, or <code>null</code> if there are no differences
	 * @exception InvocationTargetException
	 *                if the <code>prepareInput</code> method must propagate a checked exception, it should
	 *                wrap it inside an <code>InvocationTargetException</code>; runtime exceptions are
	 *                automatically wrapped in an <code>InvocationTargetException</code> by the calling
	 *                context
	 * @exception InterruptedException
	 *                if the operation detects a request to cancel, using
	 *                <code>IProgressMonitor.isCanceled()</code>, it should exit by throwing
	 *                <code>InterruptedException</code>
	 */
	protected abstract Object doPrepareInput(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException;

}

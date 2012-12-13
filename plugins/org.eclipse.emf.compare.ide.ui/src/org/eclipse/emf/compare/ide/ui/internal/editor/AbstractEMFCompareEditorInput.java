/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.editor;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareConstants;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractEMFCompareEditorInput extends CompareEditorInput {

	protected final ICompareEditingDomain editingDomain;

	protected final AdapterFactory adapterFactory;

	/**
	 * @param configuration
	 */
	public AbstractEMFCompareEditorInput(CompareConfiguration configuration,
			ICompareEditingDomain editingDomain, AdapterFactory adapterFactory) {
		super(configuration);
		this.editingDomain = editingDomain;
		this.adapterFactory = adapterFactory;
	}

	/**
	 * @return the adapterFactory
	 */
	protected AdapterFactory getAdapterFactory() {
		return adapterFactory;
	}

	/**
	 * @return the editingDomain
	 */
	protected ICompareEditingDomain getEditingDomain() {
		return editingDomain;
	}

	/**
	 * {@inheritDoc}
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
	protected Object prepareInput(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		getCompareConfiguration().setProperty(EMFCompareConstants.EDITING_DOMAIN, editingDomain);
		return doPrepareInput(monitor);
	}

	/**
	 * @param monitor
	 * @return
	 */
	protected abstract Object doPrepareInput(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException;

}

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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareConstants;
import org.eclipse.emf.compare.scope.IComparisonScope;

/**
 * CompareEditorInput that will compute the result of the comparison of the given scope with the given
 * comparator.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ComparisonScopeEditorInput extends AbstractEMFCompareEditorInput {

	private final IComparisonScope scope;

	private final EMFCompare comparator;

	/**
	 * @param configuration
	 */
	public ComparisonScopeEditorInput(CompareConfiguration configuration,
			ICompareEditingDomain editingDomain, AdapterFactory adapterFactory, EMFCompare comparator,
			IComparisonScope scope) {
		super(configuration, editingDomain, adapterFactory);
		this.comparator = comparator;
		this.scope = scope;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.editor.AbstractEMFCompareEditorInput#doPrepareInput(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected Object doPrepareInput(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		// FIXME: should not we compute the Comparison here and return the adaptation of the result as an
		// ICompareInput
		getCompareConfiguration().setProperty(EMFCompareConstants.COMPARATOR, comparator);
		return new ComparisonScopeInput(scope, getAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.CompareEditorInput#cancelPressed()
	 */
	@Override
	public void cancelPressed() {
		while (getEditingDomain().getCommandStack().canUndo()) {
			getEditingDomain().getCommandStack().undo();
		}
		super.cancelPressed();
	}
}

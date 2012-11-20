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
package org.eclipse.emf.compare.ide.ui.internal.util;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareConstants;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.EMFCompareStructureMergeViewer;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

public class EMFCompareEditorInput extends CompareEditorInput {

	private final IComparisonScope scope;

	private final EMFCompare comparator;

	private AdapterFactory adapterFactory;

	private ICompareEditingDomain editingDomain;

	/**
	 * @param configuration
	 */
	public EMFCompareEditorInput(CompareConfiguration configuration, EMFCompare comparator,
			IComparisonScope scope, ICompareEditingDomain editingDomain, AdapterFactory adapterFactory) {
		super(configuration);
		this.comparator = comparator;
		this.scope = scope;
		this.editingDomain = editingDomain;
		this.adapterFactory = adapterFactory;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.CompareEditorInput#prepareInput(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected Object prepareInput(IProgressMonitor progressMonitor) throws InvocationTargetException,
			InterruptedException {

		// FIXME: should not compute Comparison here. Should prepare a ICompareInpupt
		// and pass it to EMFCompareStructureMergeViewer
		Monitor monitor = BasicMonitor.toMonitor(progressMonitor);
		final Comparison comparison = comparator.compare(scope, monitor);

		// getCompareConfiguration().setProperty(EMFCompareConstants.COMPARE_RESULT, comparison);
		getCompareConfiguration().setProperty(EMFCompareConstants.EDITING_DOMAIN, editingDomain);

		return adapterFactory.adapt(comparison, IDiffElement.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.CompareEditorInput#createDiffViewer(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Viewer createDiffViewer(Composite parent) {
		return new EMFCompareStructureMergeViewer(parent, getCompareConfiguration());
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
}

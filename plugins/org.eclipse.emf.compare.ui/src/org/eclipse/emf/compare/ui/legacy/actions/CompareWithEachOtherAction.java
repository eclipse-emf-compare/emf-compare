/*******************************************************************************
 * Copyright (c) 2006, Intalio Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Intalio Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.legacy.actions;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.compare.ui.legacy.editor.ModelCompareEditorInput;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class CompareWithEachOtherAction implements IObjectActionDelegate {

	private IStructuredSelection selection;

	public CompareWithEachOtherAction() {
	}

	/**
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 *      org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(final IAction action, final IWorkbenchPart targetPart) {
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(final IAction action) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			public void run() {
				final CompareConfiguration config = new CompareConfiguration();
				final ModelCompareEditorInput compareEditorInput = new ModelCompareEditorInput(
						config, ((IFile) CompareWithEachOtherAction.this.selection.getFirstElement()),
						((IFile) CompareWithEachOtherAction.this.selection.toList().get(1)), null, false);
				CompareUI.openCompareEditor(compareEditorInput);

			}
		});

	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(final IAction action, final ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			this.selection = (IStructuredSelection) selection;
		}

	}

}

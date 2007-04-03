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
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.ui.legacy.editor.ModelCompareDialog;
import org.eclipse.emf.compare.ui.legacy.editor.ModelCompareEditorHistoryInput;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * @author <a href="http://www.intalio.com">&copy; Intalio, Inc.</a>
 * 
 */
public class CompareWithHistoryAction implements IObjectActionDelegate {

	private IStructuredSelection selection;

	/**
	 * 
	 */
	public CompareWithHistoryAction() {
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

				final Shell parentShell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell();
				final String title = "Compare with local history";
				IFileState states[] = null;
				try {
					states = ((IFile) CompareWithHistoryAction.this.selection.getFirstElement())
							.getHistory(null);
				} catch (final CoreException ex) {
					MessageDialog
							.openError(parentShell, title, ex.getMessage());
					return;
				}
				if ((states == null) || (states.length <= 0)) {
					MessageDialog.openInformation(parentShell, title,
							"No local history available for selected source");
					return;
				}
				try {
					final ModelCompareEditorHistoryInput compareEditorInput = new ModelCompareEditorHistoryInput(
							config, states, ((IFile) CompareWithHistoryAction.this.selection
									.getFirstElement()));
					new ModelCompareDialog(parentShell, compareEditorInput)
							.open();

				} catch (final Exception e) {
					EMFComparePlugin.getDefault().log(e, false);
				}
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

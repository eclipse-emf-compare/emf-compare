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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.ui.legacy.editor.ModelCompareDialog;
import org.eclipse.emf.compare.ui.legacy.editor.ModelCompareEditorHistoryInput;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
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
public class ReplaceWithHistoryAction implements IObjectActionDelegate {

	private IStructuredSelection selection;

	/**
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 *      org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(final IAction action, final IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub

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
					states = ((IFile) ReplaceWithHistoryAction.this.selection.getFirstElement())
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
							config, states, ((IFile) ReplaceWithHistoryAction.this.selection
									.getFirstElement()));
					final ModelCompareDialog dialog = new ModelCompareDialog(
							parentShell, compareEditorInput,
							ModelCompareDialog.REPLACE_WITH);
					final int returncode = dialog.open();
					if (returncode == IDialogConstants.OK_ID) {
						final IFileState newState = (IFileState) dialog
								.getSelectedState();
						final IFile file = ((IFile) ReplaceWithHistoryAction.this.selection.getFirstElement());
						file.setContents(newState, IResource.KEEP_HISTORY,
								new NullProgressMonitor());
					}
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

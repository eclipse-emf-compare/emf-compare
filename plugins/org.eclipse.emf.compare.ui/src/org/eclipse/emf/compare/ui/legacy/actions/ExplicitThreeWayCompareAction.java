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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.compare.ui.legacy.dialog.FileSelectionDialog;
import org.eclipse.emf.compare.ui.legacy.editor.ModelCompareEditorInput;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
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
public class ExplicitThreeWayCompareAction implements IObjectActionDelegate {

	private IStructuredSelection selection;

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

			@SuppressWarnings("unchecked")
			public void run() {
				final Shell parentShell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell();
				final CompareConfiguration config = new CompareConfiguration();

				IFile[] arrayFiles = new IFile[ExplicitThreeWayCompareAction.this.selection.size()];
				int i = 0;
				for (final Object sel : ExplicitThreeWayCompareAction.this.selection.toList()) {
					arrayFiles[i] = (IFile) sel;
					i++;
				}
				final FileSelectionDialog dialog = new FileSelectionDialog(
						parentShell, arrayFiles, "Choose ancestor model");
				if (dialog.open() == IDialogConstants.CANCEL_ID) {
					return;
				}
				final IFile ancestorFile = dialog.getSelectedFile();
				final List<IFile> files = new ArrayList<IFile>();
				files.addAll(ExplicitThreeWayCompareAction.this.selection.toList());
				files.remove(ancestorFile);
				arrayFiles = new IFile[files.size()];
				i = 0;
				for (final Object sel : files) {
					arrayFiles[i] = (IFile) sel;
					i++;
				}
				final FileSelectionDialog dialog2 = new FileSelectionDialog(
						parentShell, arrayFiles, "Choose local model");
				if (dialog2.open() == IDialogConstants.CANCEL_ID) {
					return;
				}
				final IFile leftFile = dialog2.getSelectedFile();
				files.remove(leftFile);

				final ModelCompareEditorInput compareEditorInput = new ModelCompareEditorInput(
						config, leftFile, files.get(0), ancestorFile,
						false);
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

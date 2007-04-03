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

import org.eclipse.emf.compare.ui.legacy.wizard.NewMergedFileWizard;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class AutomaticMergeAction implements IObjectActionDelegate {

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
		final NewMergedFileWizard wizard = new NewMergedFileWizard();
		wizard.init(PlatformUI.getWorkbench(), this.selection);
		final WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), wizard);
		dialog.open();
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

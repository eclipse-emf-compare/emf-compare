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
/**
 * 
 */
package org.eclipse.emf.compare.ui.legacy.wizard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.DialogUtil;
import org.eclipse.ui.internal.wizards.newresource.ResourceMessages;
import org.eclipse.ui.wizards.newresource.BasicNewFileResourceWizard;

/**
 * @author Antoine Toulm�
 * 
 */
public class NewMergedFileWizard extends BasicNewFileResourceWizard {

	private IFile left;

	private IFile right;

	private IFile ancestor;

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public void addPages() {
		super.addPages();
		((WizardNewFileCreationPage) getPage("newFilePage1")).setFileName(this.left
				.getName().substring(
						0,
						this.left.getName().length()
								- ((this.left.getFileExtension().length()) + 1))
				+ "_" + this.right.getName());
	}

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public boolean performFinish() {
		final IFile file = ((WizardNewFileCreationPage) getPage("newFilePage1"))
				.createNewFile();
		if (file == null) {
			return false;
		}
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			// engine.getMergedModel().eResource().save(output,null);
			// TODOCBR auto merge
			file.setContents(new ByteArrayInputStream(output.toByteArray()),
					true, false, new NullProgressMonitor());
			// } catch (IOException e1) {
			// EMFComparePlugin.getDefault().log(e1,false);
			// return false;
		} catch (final CoreException e) {
			EMFComparePlugin.getDefault().log(e, false);
			return false;
		}

		selectAndReveal(file);

		// Open editor on new file.
		final IWorkbenchWindow dw = getWorkbench().getActiveWorkbenchWindow();
		try {
			if (dw != null) {
				final IWorkbenchPage page = dw.getActivePage();
				if (page != null) {
					IDE.openEditor(page, file, true);
				}
			}
		} catch (final PartInitException e) {
			DialogUtil.openError(dw.getShell(),
					ResourceMessages.FileResource_errorMessage, e.getMessage(),
					e);
		}

		return true;
	}

	/*
	 * (non-Javadoc) Method declared on IWorkbenchWizard.
	 */
	public void init(final IWorkbench workbench, final IStructuredSelection currentSelection) {
		this.selection = currentSelection;
		this.left = (IFile) currentSelection.getFirstElement();
		this.right = (IFile) currentSelection.toList().get(1);
		if (currentSelection.toList().size() > 2) {
			this.ancestor = (IFile) currentSelection.toList().get(2);
		}
		setWindowTitle("Merge two model files");
		setNeedsProgressMonitor(true);

		// try {
		// engine = new
		// UMLComparisonEngine(left.getContents(),right.getContents(),ancestor
		// == null ? null : ancestor.getContents(),true);
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(workbench
				.getActiveWorkbenchWindow().getShell());
		// dialog.run(true,true,null);

		super.init(workbench, currentSelection);
		// } catch (CoreException e) {
		// EMFComparePlugin.getDefault().log(e,false);
		// } catch (InvocationTargetException e) {
		// EMFComparePlugin.getDefault().log(e,false);
		// } catch (InterruptedException e) {
		// silently out
		// }

	}
}

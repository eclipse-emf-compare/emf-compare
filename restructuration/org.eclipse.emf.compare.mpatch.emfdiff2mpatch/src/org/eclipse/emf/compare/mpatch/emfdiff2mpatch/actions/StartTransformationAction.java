/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.emfdiff2mpatch.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.wizards.EmfdiffExportWizard;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;


/**
 * Individual action on emfdiff files for transforming them into {@link MPatchModel}s.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class StartTransformationAction implements IObjectActionDelegate {

	/** Store the selection. */
	private IFile file;

	/** The shell. */
	private Shell shell;

	/**
	 * Load emfdiff and call mpatch transformation wizard.
	 */
	public void run(IAction action) {
		if (file != null) {

			// get emfdiff snapshot from emfdiff file
			final Resource emfdiffResource = new ResourceSetImpl().getResource(URI.createFileURI(file.getFullPath()
					.toString()), true);
			if (emfdiffResource.getContents().size() > 0
					&& emfdiffResource.getContents().get(0) instanceof ComparisonSnapshot) {
				final ComparisonSnapshot snapshot = (ComparisonSnapshot) emfdiffResource.getContents().get(0);

				// initialize wizard
				final EmfdiffExportWizard wizard = new EmfdiffExportWizard();
				final IWorkbench workbench = PlatformUI.getWorkbench();

				// open wizard
				wizard.init(workbench, new StructuredSelection(snapshot));
				final WizardDialog dialog = new WizardDialog(shell, wizard);
				dialog.open();
			} else {
				MessageDialog
						.openWarning(shell, "Invalid emfdiff",
								"Cannot find a ComparisonSnapshot in the emfdiff file! Please make sure the file is not corrupt.");
			}
		}
	}

	/**
	 * Store the current selection.
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() == 1 && structuredSelection.getFirstElement() instanceof IFile) {
				file = (IFile) structuredSelection.getFirstElement();
				if (!MPatchConstants.FILE_EXTENSION_EMF_COMPARE.equals(file.getFileExtension())) {
					file = null;
				}
			} else {
				file = null;
			}
		} else {
			file = null;
		}
		action.setEnabled(file != null);
	}

	/** Get the Shell. */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}
}

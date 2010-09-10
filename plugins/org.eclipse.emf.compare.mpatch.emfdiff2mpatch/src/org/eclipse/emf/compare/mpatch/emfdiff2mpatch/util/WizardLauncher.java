/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.emfdiff2mpatch.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.wizards.EmfdiffExportWizard;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * Public API for starting the wizard for user-interactively selecting and transforming an emfdiff to a target EMF
 * model.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public final class WizardLauncher {

	/**
	 * Open the wizard to transform an emfdiff to an {@link MPatchModel}.
	 * 
	 * @param snapshot
	 *            The EMF Compare comparison snapshot containing one or many {@link DiffModel}s.
	 */
	public static void openWizard(ComparisonSnapshot snapshot) {
		openWizard(snapshot, null);
	}

	/**
	 * Open the wizard to transform an emfdiff to an {@link MPatchModel} and propose the path for the MPatch file.
	 * 
	 * @param snapshot
	 *            The EMF Compare comparison snapshot containing one or many {@link DiffModel}s.
	 * @param mPatchFile
	 *            The proposed path for the resulting MPatch file.
	 */
	public static void openWizard(ComparisonSnapshot snapshot, IFile mPatchFile) {
		if (snapshot == null)
			throw new IllegalArgumentException("Input (emfdiff comparisonsnapshot) must not be null!");
		
		final EmfdiffExportWizard wizard = new EmfdiffExportWizard();
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Object[] selection = mPatchFile == null ? new Object[] { snapshot }
				: new Object[] { snapshot, mPatchFile };

		wizard.init(workbench, new StructuredSelection(selection));
		final WizardDialog dialog = new WizardDialog(workbench.getActiveWorkbenchWindow().getShell(), wizard);
		dialog.open();
	}
}

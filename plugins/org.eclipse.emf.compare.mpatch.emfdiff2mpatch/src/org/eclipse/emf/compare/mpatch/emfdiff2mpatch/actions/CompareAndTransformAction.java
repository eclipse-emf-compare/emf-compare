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
package org.eclipse.emf.compare.mpatch.emfdiff2mpatch.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.common.util.CommonUtils;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.Emfdiff2mpatchActivator;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.util.WizardLauncher;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Action for automatically compare two models with EMF Compare, and open the wizard for the creation of an
 * {@link MPatchModel}.<br>
 * <br>
 * 
 * <b>Note: currently deactivated in plugin.xml because it is too intrusive.</b>
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class CompareAndTransformAction implements IObjectActionDelegate {

	/** Default file name for new {@link MPatchModel}. */
	private static final String DEFAULT_FILE_NAME = "result." + MPatchConstants.FILE_EXTENSION_MPATCH;

	/** Store the selection. */
	private IFile oldFile;

	/** The new file. */
	private IFile newFile;

	/** The shell. */
	private Shell shell;

	/**
	 * Call EMF Compare to compare the selected files and call Apply MPatch Wizard afterwards.
	 */
	@Override
	public void run(IAction action) {
		if (oldFile != null && newFile != null) {

			// get emfdiff
			final ComparisonSnapshot emfdiff = createComparisonSnapshot(oldFile, newFile, shell);
			if (emfdiff != null) {

				// propose mpatch path
				final IFile mPatchFile = CommonUtils.createNewIFile(newFile, DEFAULT_FILE_NAME);

				// launch wizard
				WizardLauncher.openWizard(emfdiff, mPatchFile);
			}
		}
	}

	/**
	 * Call EMF Compare to create an emfdiff for the given models.
	 * 
	 * @param oldFile
	 *            The unchanged version of a model.
	 * @param newFile
	 *            The changed version of the same model.
	 * @param shell
	 *            The shell for showing dialogs.
	 * @return The emfdiff in case of successful differencing or <code>null</code> otherwise.
	 */
	private static ComparisonSnapshot createComparisonSnapshot(final IFile oldFile, final IFile newFile, Shell shell) {
		ComparisonResourceSnapshot emfdiff = null;
		try {
			// get resources
			final ResourceSet resourceSet = new ResourceSetImpl();
			final Resource oldResource = resourceSet.getResource(URI.createFileURI(oldFile.getFullPath().toString()),
					true);
			final Resource newResource = resourceSet.getResource(URI.createFileURI(newFile.getFullPath().toString()),
					true);
			if (oldResource.getContents().size() != 1)
				throw new IllegalArgumentException(oldFile.getFullPath()
						+ " is expected to contain exactly one root model element, but "
						+ oldResource.getContents().size() + " elements found!");
			if (newResource.getContents().size() != 1)
				throw new IllegalArgumentException(newFile.getFullPath()
						+ " is expected to contain exactly one root model element, but "
						+ newResource.getContents().size() + " elements found!");

			// ask user which is the modified version of the model
			@SuppressWarnings("deprecation")
			final MessageDialog dialog = new MessageDialog(shell, "Please select the modified model", MessageDialog
					.getImage(Dialog.DLG_IMG_QUESTION), "Please select the file which contains the MODIFIED version:",
					MessageDialog.QUESTION, new String[] { newFile.getName(), oldFile.getName() }, 0);
			final int dialogResult = dialog.open();
			if (dialogResult == SWT.DEFAULT)
				return null;

			// use emf compare to compute differences
			final EObject oldModel = (dialogResult == 0 ? oldResource : newResource).getContents().get(0);
			final EObject newModel = (dialogResult == 0 ? newResource : oldResource).getContents().get(0);
			emfdiff = CommonUtils.createEmfdiff(newModel, oldModel);
			if (emfdiff != null)
				return emfdiff;
			throw new IllegalArgumentException("EMF Compare failed to compute differences.");

		} catch (Exception e) {
			final String message = "Could not create differences with EMF Compare.";
			Emfdiff2mpatchActivator.getDefault().logError(message, e);
			MessageDialog.openError(shell, "Could not create differences with EMF Compare!", message
					+ " See error log for details.\n" + "Error message: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Get the old and the new file from the current selection.
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		oldFile = null;
		newFile = null;
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() == 2) {
				final Object[] files = structuredSelection.toArray();
				if (files[0] instanceof IFile && files[1] instanceof IFile) {
					newFile = (IFile) files[0];
					oldFile = (IFile) files[1];
				}
			}
		}
		action.setEnabled(oldFile != null && newFile != null);
	}

	/** Get the Shell. */
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}
}

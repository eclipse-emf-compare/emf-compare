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
package org.eclipse.emf.compare.mpatch.common.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.statushandlers.StatusManager;

/**
 * Abstract action which contains the basic functionality for individual transformation on {@link MPatchModel}s. If asks
 * the user to store the changed input in another file or to overwrite the existing file; then it calls
 * {@link AbstractCompareAction#runAction(Resource, Resource)}.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public abstract class AbstractCompareAction implements IObjectActionDelegate {

	/** Store the selection. */
	private IFile file;

	/** Shell. */
	private Shell shell;

	/** The file extension this action is registered on. */
	private final String inputFileExtension;

	/** The extension of the resulting file of this action. */
	private final String outputFileExtension;

	/**
	 * The action is performed in a separate {@link Job}. Please provide a title for it.
	 */
	private final String jobTitle;

	/**
	 * Default constructor.
	 * 
	 * @param inputFileExtension
	 *            The file extension this action is registered on.
	 * @param outputFileExtension
	 *            The extension of the resulting file of this action.
	 * @param jobTitle
	 *            The action is performed in a separate {@link Job}. Please provide a title for it.
	 */
	public AbstractCompareAction(String inputFileExtension, String outputFileExtension, String jobTitle) {
		this.inputFileExtension = inputFileExtension;
		this.outputFileExtension = outputFileExtension;
		this.jobTitle = jobTitle;
	}

	/**
	 * Prompt user for a destination folder for the result of this action.
	 * 
	 * @param suggestedPath
	 *            The predefined file name.
	 * @return {@link IFile}
	 */
	protected IFile promptFileDestination(IPath suggestedPath) {
		// prompt user for destination
		final Shell shell = this.shell == null ? new Shell() : this.shell;
		final String message = "Please select a folder where to store the output file.";
		final IFile file = WorkspaceResourceDialog.openNewFile(shell, "Save output file...", message, suggestedPath,
				null);
		// IContainer[] containers = WorkspaceResourceDialog.openFolderSelection(shell, "Save output file...", message,
		// false, new IContainer[] {
		// file.getParent()
		// }, null);

		if (file != null) {
			// if (containers.length == 1) {
			// final IFile file = containers[0].getFile(new Path(fileName));

			// what if the file already exists?
			if (file.exists()) {
				final MessageDialog dialog = new MessageDialog(shell, "File already exists...", null, "The file '"
						+ file.getName() + "' already exists in the specified folder.", 0, new String[] { "Overwrite",
						"Prompt again", "Cancel", }, 0);
				switch (dialog.open()) {
				case 0:
					return file;
				case 1:
					return promptFileDestination(file.getFullPath());
				default:
					return null;
				}
			}
			return file;
		} else {
			// dialog was canceled
			return null;
		}
	}

	/**
	 * Wrap the actual action in a job.
	 * 
	 * @param action
	 *            The calling action.
	 */
	@Override
	public void run(IAction action) {
		if (file != null) {

			// load the selected file as a resource
			final Resource diffResource = new ResourceSetImpl().getResource(URI.createFileURI(file.getFullPath()
					.toString()), true);

			// specify filename and ask where to save the new file
			final IPath path = file.getFullPath().removeFileExtension().addFileExtension(outputFileExtension);
			final IFile newFile = promptFileDestination(path);
			if (newFile != null) {

				/*
				 * PK: Maybe those actions are more complex in the future.. For now we don't use a job to keep it
				 * simple.
				 */
				// run jet transformation in a job (separate thread)
				final Job job = new Job(jobTitle) {
					@Override
					protected IStatus run(IProgressMonitor monitor) {

						// save transformation into a file
						final Resource resource = new XMIResourceImpl(URI.createURI(newFile.getFullPath().toString()));
						final Status status = runAction(diffResource, resource, monitor);

						// handle status
						if (status.getSeverity() == IStatus.OK) {
							PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
								public void run() {
									MessageDialog.openInformation(shell, "Transformation successful", status
											.getMessage());
								}
							});
						} else {
							StatusManager.getManager().handle(status, StatusManager.SHOW);
						}
						return status;
					}
				};

				// enqueue the job
				job.setUser(true);
				job.schedule();
			}
		}
	}

	/**
	 * The actual action that is performed.
	 * 
	 * @param input
	 *            The selected file as a {@link Resource}.
	 * @param output
	 *            The user-selected output-file as a {@link Resource}. <b>Note that the subclass is responsible for
	 *            saving the resource, e.g. by calling <code>output.setContents</code> and <code>output.save</code>!</b>
	 * @param monitor
	 *            The progress monitor for the job.
	 * @return The status of the action.
	 */
	protected abstract Status runAction(Resource input, Resource output, IProgressMonitor monitor);

	/**
	 * Store the current selection.
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection)selection;
			if (structuredSelection.size() == 1 && structuredSelection.getFirstElement() instanceof IFile) {
				file = (IFile)structuredSelection.getFirstElement();
				if (!inputFileExtension.equals(file.getFileExtension())) {
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

	/**
	 * To get the shell.
	 */
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

}

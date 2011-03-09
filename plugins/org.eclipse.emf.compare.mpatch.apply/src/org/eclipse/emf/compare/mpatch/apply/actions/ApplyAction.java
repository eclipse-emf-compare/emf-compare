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
package org.eclipse.emf.compare.mpatch.apply.actions;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.apply.wizards.ApplyWizard;
import org.eclipse.emf.compare.mpatch.common.util.CommonUtils;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;

/**
 * An action which checks the currently selected element for an {@link MPatchModel} and launches the wizard for applying
 * it to another model.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class ApplyAction implements IObjectActionDelegate, IEditorActionDelegate {

	/** Store the selection. */
	private IStructuredSelection selection;

	/** The shell. */
	private Shell shell;

	/** Store it because we need it to launch the wizard. */
	private IWorkbench workbench;

	/**
	 * This action simply takes the current selection and opens the wizard {@link ApplyWizard} with it.
	 */
	public void run(IAction action) {
		// Create the wizard
		final ApplyWizard wizard = new ApplyWizard();
		wizard.init(workbench, selection);

		// Create the wizard dialog
		final WizardDialog dialog = new WizardDialog(shell, wizard);
		// Open the wizard dialog
		dialog.open();
	}

	/**
	 * Enable action depending on the selection.
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = null;
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() == 1 || structuredSelection.size() == 2) {
				for (Iterator<?> i = structuredSelection.iterator(); i.hasNext();) {
					final Object obj = (Object) i.next();
					if (obj instanceof IFile) {
						final IFile file = (IFile) obj;
						if (MPatchConstants.FILE_EXTENSION_MPATCH.equals(file.getFileExtension())) {
							this.selection = structuredSelection;
							return;
						}
					} else if (obj instanceof EObject) {
						final EObject root = EcoreUtil.getRootContainer((EObject) obj);
						if (root instanceof MPatchModel && root.eResource() != null) {
							final IFile file = getFileFromResource(root.eResource());
							if (file != null) {
								this.selection = new StructuredSelection(file);
							}
						}
					} else if (obj instanceof Resource) {
						final IFile file = getFileFromResource((Resource)obj);
						if (file != null) {
							this.selection = new StructuredSelection(file);
						}
					}
				}
			}
		}
		action.setEnabled(this.selection != null);
	}

	private static IFile getFileFromResource(Resource obj) {
		final URI uri = obj.getURI();
		final IWorkspaceRoot ws = ResourcesPlugin.getWorkspace().getRoot();
		final String[] segments = uri.segments();
		if (segments != null && segments.length > 1) {
			final String[] newSegments = new String[segments.length - 1];
			System.arraycopy(segments, 1, newSegments, 0, newSegments.length);
			final IPath path = new Path("/" + CommonUtils.join(newSegments, "/"));
			final IFile file = ws.getFile(path);
			if (file != null && file.exists()) {
				return file;
			}
		}
		return null;
	}

	/**
	 * Get Shell and workbench.
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
		workbench = targetPart.getSite().getWorkbenchWindow().getWorkbench();
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		if (targetEditor != null) {
			shell = targetEditor.getSite().getShell();
			workbench = targetEditor.getSite().getWorkbenchWindow().getWorkbench();
		}
	}
}

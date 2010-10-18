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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.common.util.CommonUtils;
import org.eclipse.emf.compare.mpatch.common.util.ExtensionManager;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.Emfdiff2mpatchActivator;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.util.TransformationLauncher;
import org.eclipse.emf.compare.mpatch.extension.IMPatchTransformation;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Action for folders: go through entire container structure (folder or project) and whenever there are two files
 * 'unchanged.*' and 'changed.*' exist, create an MPatch for them. The resulting file is simply called
 * <code>result.mpatch</code> (it will be overwritten, if it already exists).<br>
 * <br>
 * 
 * <b>Note: usually deactivated because it's just for convenience of testing.</b>
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class BatchCreateMPatchAction implements IObjectActionDelegate {

	/** Default file name for new {@link MPatchModel}. */
	private static final String DEFAULT_FILE_NAME = "result." + MPatchConstants.FILE_EXTENSION_MPATCH;

	/**
	 * Shell.
	 */
	private Shell shell;

	/** Get some statistics. */
	private int successes = 0;

	/** Get some statistics. */
	private int failures = 0;

	/**
	 * List of all currently selected containers.
	 */
	private final List<IContainer> containers = new ArrayList<IContainer>();

	/**
	 * Call EMF Compare to compare the selected files and call Apply MPatch Wizard afterwards.
	 */
	@Override
	public void run(IAction action) {
		failures = 0;
		successes = 0;
		for (IContainer container : containers) {
			try {
				container.refreshLocal(IContainer.DEPTH_INFINITE, null);
				processContainer(container);
			} catch (CoreException e) {
				Emfdiff2mpatchActivator.getDefault().logError("Error processing container " + container.getName(), e);
				failures++;
			}
		}
		String msg = successes + " files successfully created.";
		if (failures > 0)
			msg += "\n" + failures + " errors occured. Please check error log.";
		MessageDialog.openInformation(shell, MPatchConstants.MPATCH_SHORT_NAME + " Creation results", msg);
	}

	private void processContainer(IContainer container) throws CoreException {
		IResource unchanged = null;
		IResource changed = null;
		for (IResource child : container.members()) {
			if (child instanceof IContainer) {
				processContainer((IContainer) child);
			} else if (!child.getName().endsWith("_diagram") && !child.getName().endsWith(".umlclass")) {
				if (child.getName().startsWith("unchanged."))
					unchanged = child;
				else if (child.getName().startsWith("changed."))
					changed = child;
			}
		}
		if (changed != null && unchanged != null) {
			if (unchanged.getFileExtension().equals(changed.getFileExtension())) {
				createMPatch(container, unchanged, changed);
			}
		}
	}

	private void createMPatch(IContainer container, IResource unchanged, IResource changed) {
		try {
			final ResourceSet resourceSet = new ResourceSetImpl();
			final Resource oldResource = resourceSet.getResource(URI.createFileURI(unchanged.getFullPath().toString()),
					true);
			final Resource newResource = resourceSet.getResource(URI.createFileURI(changed.getFullPath().toString()),
					true);

			final EObject oldModel = oldResource.getContents().get(0);
			final EObject newModel = newResource.getContents().get(0);

			final ComparisonResourceSnapshot emfdiff = CommonUtils.createEmfdiff(newModel, oldModel, false);

			final ISymbolicReferenceCreator symrefCreator = ExtensionManager.getSelectedSymbolicReferenceCreator();
			final IModelDescriptorCreator descriptorCreator = ExtensionManager.getSelectedModelDescriptorCreator();

			final MPatchModel mpatch = TransformationLauncher
					.transform(emfdiff, null, symrefCreator, descriptorCreator);

			for (String label : ExtensionManager.getMandatoryTransformations()) {
				final IMPatchTransformation trans = ExtensionManager.getAllTransformations().get(label);
				trans.transform(mpatch);
			}
			for (String label : ExtensionManager.getSelectedOptionalTransformations()) {
				final IMPatchTransformation trans = ExtensionManager.getAllTransformations().get(label);
				trans.transform(mpatch);
			}

			final IPath mPatchPath = container.getFullPath().append(DEFAULT_FILE_NAME);

			final ResourceSet newResourceSet = new ResourceSetImpl();
			final Resource resource = newResourceSet.createResource(URI.createFileURI(mPatchPath.toString()));
			resource.getContents().add(mpatch);
			resource.save(null);

			successes++;
		} catch (Exception e) {
			Emfdiff2mpatchActivator.getDefault().logError(
					"Error creating MPatch for " + unchanged.getName() + " and " + changed.getName(), e);
			failures++;
		}
	}

	/**
	 * Get the old and the new file from the current selection.
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		containers.clear();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			for (Object obj : structuredSelection.toArray()) {
				if (obj instanceof IContainer) {
					containers.add((IContainer) obj);
				}
			}
		}
		action.setEnabled(!containers.isEmpty());
	}

	/** Get the Shell. */
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}
}

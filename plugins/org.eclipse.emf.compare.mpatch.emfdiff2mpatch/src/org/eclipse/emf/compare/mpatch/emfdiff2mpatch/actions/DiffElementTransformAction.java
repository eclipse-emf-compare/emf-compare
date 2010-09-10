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

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.common.util.CommonUtils;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.util.WizardLauncher;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchSite;


/**
 * This action is an extension to the EMF Compare GUI (compare editor) and allows to selectively transform
 * {@link DiffElement}s into an {@link MPatchModel}.<br><br>
 * 
 * If the entire emfdiff should be transformed, {@link EmfdiffExportAction} is more appropriate.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class DiffElementTransformAction implements IEditorActionDelegate, ISelectionListener {

	private Collection<DiffElement> selection = new HashSet<DiffElement>();

	private IWorkbenchSite site;

	private IAction action;

	private EList<EObject> rightRoots;

	private EList<EObject> leftRoots;

	private static final String DEFAULT_FILE_NAME = "result." + MPatchConstants.FILE_EXTENSION_MPATCH;

	/** Get selection. */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		setSelection(selection);
	}

	/**
	 * Get site and set current action.
	 */
	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		// lets check if we need to change the selection listener
		if (targetEditor != null && !targetEditor.getSite().equals(site)) {
			if (site != null) {
				site.getWorkbenchWindow().getSelectionService().removePostSelectionListener(this);
			}
			site = targetEditor.getSite();
			site.getWorkbenchWindow().getSelectionService().addPostSelectionListener(this);
		}
		this.action = action;
	}

	/**
	 * Get the selected DiffElements and call wizard.
	 */
	@Override
	public void run(IAction action) {
		if (selection.size() > 0) {
			// create a container for all diffelements
			final DiffGroup container = DiffFactory.eINSTANCE.createDiffGroup();
			container.getSubDiffElements().addAll(EcoreUtil.copyAll(selection));

			// call export wizard for mpatch creation
			callExportEmfdiffWizard(container);
		} else if (site != null) {
			MessageDialog.openInformation(site.getShell(), "No differences selected",
					"There are no differences selected. Nothing to do...");
		}
	}

	private void callExportEmfdiffWizard(DiffGroup container) {
		// create snapshot and diffmodel
		final ComparisonResourceSnapshot snapshot = DiffFactory.eINSTANCE.createComparisonResourceSnapshot();
		final DiffModel diffModel = DiffFactory.eINSTANCE.createDiffModel();
		diffModel.getLeftRoots().addAll(leftRoots);
		diffModel.getRightRoots().addAll(rightRoots);
		snapshot.setDiff(diffModel);
		diffModel.getOwnedElements().add(container);

		// start wizard
		if (!leftRoots.isEmpty() && leftRoots.get(0).eResource() != null) {
			final IFile mPatchFile = CommonUtils.createNewIFile(leftRoots.get(0).eResource().getURI(), DEFAULT_FILE_NAME);
			WizardLauncher.openWizard(snapshot, mPatchFile);
		} else {
			WizardLauncher.openWizard(snapshot);
		}
	}

	/** Set selection. */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		setSelection(selection);
	}

	private void setSelection(ISelection selection) {
		// lets filter the selection for something useful
		this.selection.clear();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (!structuredSelection.isEmpty()) {

				// get all diffelements
				for (Object obj : structuredSelection.toArray()) {
					if (obj instanceof DiffElement) {
						addDiffElementsToSelection((DiffElement) obj, this.selection);
					} else if (obj instanceof DiffModel) {
						addDiffElementsToSelection((DiffModel) obj, this.selection);
					}
				}

				// get leftRoots and rightRoots
				if (!this.selection.isEmpty()) {
					final DiffModel diffModel = getDiffModelContainer(this.selection.iterator().next());
					leftRoots = diffModel == null ? null : diffModel.getLeftRoots();
					rightRoots = diffModel == null ? null : diffModel.getRightRoots();
				}
			}
		}

		// enable the action only if something useful was selected
		if (action != null) {
			action.setEnabled(!this.selection.isEmpty());
		}
	}

	/** Get containing {@link DiffModel}. */
	private DiffModel getDiffModelContainer(EObject obj) {
		if (obj.eContainer() == null)
			return null;
		if (obj.eContainer() instanceof DiffModel)
			return (DiffModel) obj.eContainer();
		return getDiffModelContainer(obj.eContainer());
	}

	/** Collect all {@link DiffElement} that are not {@link DiffGroup}s. */
	private void addDiffElementsToSelection(DiffModel diffModel, Collection<DiffElement> selection) {
		// iterate over all owned elements and add them to the selection
		for (DiffElement diffElement : diffModel.getOwnedElements()) {
			addDiffElementsToSelection(diffElement, selection);
		}
	}

	/** Collect all {@link DiffElement} that are not {@link DiffGroup}s. */
	private void addDiffElementsToSelection(DiffElement diffElement, Collection<DiffElement> selection) {
		if (diffElement instanceof DiffGroup) {
			// recursive call on children of this group
			final DiffGroup group = (DiffGroup) diffElement;
			for (DiffElement diffElement2 : group.getSubDiffElements()) {
				addDiffElementsToSelection(diffElement2, selection);
			}
		} else {
			// add element to selection
			selection.add(diffElement);
		}
	}
}

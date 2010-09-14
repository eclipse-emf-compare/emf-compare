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
package org.eclipse.emf.compare.mpatch.apply.wizards;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.apply.util.MPatchResolver;
import org.eclipse.emf.compare.mpatch.apply.util.MPatchValidator;
import org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding;
import org.eclipse.emf.compare.mpatch.common.util.CommonUtils;
import org.eclipse.emf.compare.mpatch.common.util.ExtensionManager;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.extension.IMPatchResolution;
import org.eclipse.emf.compare.mpatch.extension.IMPatchResolutionHost;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences.ValidationResult;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;


/**
 * Wizard page for resolving the symbolic references of the selected {@link MPatchModel} to the target model.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class ApplyWizardResolvePage extends WizardPage implements IMPatchResolutionHost {

	/** Info text for showing to the user how the changes resolved. */
	final String INFO_TEXT = "Selected changes: %d, to be applied: %d, already applied: %d, not resolved: %d, invalid state: %d";

	/** The model to which the diff should be applied (from a previous page). */
	private Resource modelResource;

	/** the diff itself (from a previous page). */
	private MPatchModel mpatch;

	/** The result of the symbolic reference resolution. */
	private ResolvedSymbolicReferences resolvedElements;

	/** Info label. */
	private Label infoLabel;

	/** Store intermediate models or not. */
	private Button storeIntermediateModelsCheckbox;

	/** The checkbox whether the {@link MPatchModelBinding} should be stored afterwards. */
	private Button storeBindingButton;

	/** Adapter factory for emf model. */
	private final AdapterFactory adapterFactory;

	private final AdapterFactoryLabelProvider labelProvider;

	private IMPatchResolution iDiffResolution;

	/**
	 * Constructor for this wizard page.
	 * 
	 * @param pageName
	 *            The name and title of the page.
	 * @param diffApplier
	 *            Instance of {@link IDiffApplier} which will be used to resolve references.
	 */
	public ApplyWizardResolvePage(String pageName, AdapterFactory adapterFactory) {
		super(pageName);
		setTitle(pageName);
		setDescription("Resolve " + MPatchConstants.SYMBOLIC_REFERENCES_NAME + " for the selected model.");
		this.adapterFactory = adapterFactory;
		labelProvider = new AdapterFactoryLabelProvider(adapterFactory);

		iDiffResolution = ExtensionManager.getSelectedResolution();
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		final GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		container.setLayout(layout);

		// add container for external symbolic reference resolution
		Group resolutionContainer = new Group(container, SWT.SHADOW_OUT);
		resolutionContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		resolutionContainer.setLayout(new GridLayout(1, false));
		resolutionContainer.setText(iDiffResolution.getLabel());

		// add specific GUI from the extension
		iDiffResolution.buildResolutionGUI(resolutionContainer, adapterFactory);

		// add some info about the resolved elements
		infoLabel = new Label(container, SWT.NULL);
		infoLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		// button whether or not intermediate models should be stored
		storeIntermediateModelsCheckbox = new Button(container, SWT.CHECK);
		storeIntermediateModelsCheckbox.setText("Store intermediate differences and model");
		storeIntermediateModelsCheckbox.setSelection(false); // default is false
		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		storeIntermediateModelsCheckbox.setLayoutData(gd);

		storeBindingButton = new Button(container, SWT.CHECK);
		storeBindingButton.setText("Store binding between " + MPatchConstants.MPATCH_SHORT_NAME + " and model in a file");
		storeBindingButton.setSelection(false); // default is false
		gd = new GridData();
		gd.horizontalSpan = 3;
		storeBindingButton.setLayoutData(gd);

		setControl(container);
	}

	/**
	 * @return Whether the user selected to store intermediate models.
	 */
	boolean storeIntermediateModels() {
		// package visibility only such that the wizard has access to it.
		return storeIntermediateModelsCheckbox.getSelection();
	}

	/**
	 * @return Whether the user selected to store the binding.
	 */
	boolean storeBinding() {
		return storeBindingButton.getSelection();
	}

	/**
	 * Update input from previous pages and re-resolve symbolic references, if necessary. Then, update the viewer.
	 */
	@Override
	public boolean resolved(ResolvedSymbolicReferences mapping) {
		resolvedElements = mapping;
		dialogChanged();
		return true;
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			final Resource modelResource = ((ApplyWizard) getWizard()).getModelResource();
			final MPatchModel mpatch = ((ApplyWizard) getWizard()).getMPatch();
			EObject modelTarget = ((ApplyWizard) getWizard()).getModelTarget();
			if (modelResource != null && mpatch != null) {
				if (!modelResource.equals(this.modelResource) || !mpatch.equals(this.mpatch) || true) {
					this.modelResource = modelResource;
					this.mpatch = mpatch;
					if (modelTarget == null)
						modelTarget = modelResource.getContents().get(0);

					// do we already have a resolution for that model and that mpatch?
					// if not, create new resolution
					if (resolvedElements == null || !mpatch.equals(resolvedElements.getMPatchModel()) || !modelTarget.equals(resolvedElements.getModel())) {
						resolvedElements = MPatchResolver.resolveSymbolicReferences(mpatch, modelTarget,
								ResolvedSymbolicReferences.RESOLVE_UNCHANGED);
					}
					
					// update this page
					iDiffResolution.refineResolution(resolvedElements, this);
					dialogChanged();
				} // else nothing changed
			} else {
				updateStatus("Please select a valid " + MPatchConstants.MPATCH_SHORT_NAME + " and a valid target model before.");
			}
		}
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	/** Notify the user if something is not right. */
	private void dialogChanged() {
		final String infoText;
		final String status;
		if (resolvedElements != null) {
			if (resolvedElements.getResolutionByChange().size() > 0) {
				
				// get statistics
				final List<IndepChange> invalidResolutions = MPatchValidator.validateResolutions(resolvedElements);
				final int unresolved = invalidResolutions.size();
				final int total = resolvedElements.getResolutionByChange().keySet().size();
				final int before = CommonUtils.filterByValue(resolvedElements.getValidation(), ValidationResult.STATE_BEFORE).size();
				final int after = CommonUtils.filterByValue(resolvedElements.getValidation(), ValidationResult.STATE_AFTER).size();
				final int invalid = CommonUtils.filterByValue(resolvedElements.getValidation(), ValidationResult.STATE_INVALID).size()
						+ CommonUtils.filterByValue(resolvedElements.getValidation(), ValidationResult.REFERENCE).size();
				
				infoText = String.format(INFO_TEXT, total, before, after, unresolved, invalid);

				// evaluate statistics
				if (unresolved == 0 && invalid == 0) {
					((ApplyWizard) getWizard()).setResolvedElements(resolvedElements);
					status = null;
				} else if (unresolved > 0) {
					status  ="Not all " + MPatchConstants.SYMBOLIC_REFERENCES_NAME + " resolved sufficiantly: "
					+ labelProvider.getText(invalidResolutions.get(0));
				} else { // invalid > 0
					status = "Not all changes can be applied to the selected model elements!";
				}
			} else {
				infoText = "Please select some changes.";
				status = "No changes are selected!";
			}
		} else {
			infoText = "There is an error in the resolution of " + MPatchConstants.SYMBOLIC_REFERENCES_NAME + "!";
			status = "There is an error in the resolution of " + MPatchConstants.SYMBOLIC_REFERENCES_NAME + "!";
		}
		infoLabel.setText(infoText);
		updateStatus(status);
	}

}

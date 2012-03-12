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
package org.eclipse.emf.compare.mpatch.apply.wizards;

import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


/**
 * A wizard page for summarizing the information the wizard collected.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class ApplyWizardSummaryPage extends WizardPage {

	/** The text widget for presenting the diff uri. */
	private Text diffUriText;

	/** The text widget for presenting the model uri. */
	private Text modelUriText;

	/** The text widget for presenting the uri of the new model uri. */
	private Text newModelUriText;

	/** The text widget for presenting the emfdiff uri. */
	private Text emfdiffUriText;

	/** The text widget for presenting the binding uri. */
	private Text bindingUriText;

	/** The label widget for presenting the summary of resolved references. */
	private Label resolvedLabel;

	/** The checkbox whether the result should be shown with EMF Compare afterwards. */
	private Button reviewDiffApplicationButton;

	/** Default text for the resolvedLabel label. */
	private static final String RESOLVED_LABEL_TEXT = " changes are selected.";

	/**
	 * Constructor for this page.
	 * 
	 * @param pageName
	 *            The name and title of the page.
	 */
	public ApplyWizardSummaryPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		setDescription("Select a place where the information is stored.");
	}

	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		final GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;

		new Label(container, SWT.NULL).setText(MPatchConstants.MPATCH_LONG_NAME + ":");
		diffUriText = new Text(container, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		diffUriText.setLayoutData(gd);
		diffUriText.setEditable(false);

		new Label(container, SWT.NULL).setText("Apply to model:");
		modelUriText = new Text(container, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		modelUriText.setLayoutData(gd);
		modelUriText.setEditable(false);

		new Label(container, SWT.NULL).setText("Save new model:");
		newModelUriText = new Text(container, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		newModelUriText.setLayoutData(gd);
		newModelUriText.setEditable(false);

		new Label(container, SWT.NULL).setText("Save emfdiff:");
		emfdiffUriText = new Text(container, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		emfdiffUriText.setLayoutData(gd);
		emfdiffUriText.setEditable(false);

		new Label(container, SWT.NULL).setText("Save binding:");
		bindingUriText = new Text(container, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		bindingUriText.setLayoutData(gd);
		bindingUriText.setEditable(false);

		new Label(container, SWT.NULL).setText("Reference resolution:");
		resolvedLabel = new Label(container, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		resolvedLabel.setLayoutData(gd);

		reviewDiffApplicationButton = new Button(container, SWT.CHECK);
		reviewDiffApplicationButton.setText("Review result with EMF Compare");
		reviewDiffApplicationButton.setSelection(true); // default is true
		gd = new GridData();
		gd.horizontalSpan = 2;
		reviewDiffApplicationButton.setLayoutData(gd);

		initialize();
		setControl(container);
		
		// page must at least be visited once before it is set to complete
		setPageComplete(false);
	}

	/**
	 * Collect the information from all other pages and present it to the user. In the end, the dialog is checked using
	 * {@link ApplyWizardSummaryPage#dialogChanged()}.
	 */
	private void initialize() {
		final EObject diff = ((ApplyWizard) getWizard()).getMPatch();
		final Resource model = ((ApplyWizard) getWizard()).getModelResource();
		final IFile newModelFile = ((ApplyWizard) getWizard()).getNewModelFile();
		final IFile emfdiffFile = ((ApplyWizard) getWizard()).getEmfdiffFile();
		final IFile bindingFile = ((ApplyWizard) getWizard()).getBindingFile();
		final ResolvedSymbolicReferences resolved = ((ApplyWizard) getWizard()).getResolvedElements();
		newModelUriText.setEnabled(((ApplyWizard) getWizard()).saveIntermediateFiles);
		emfdiffUriText.setEnabled(((ApplyWizard) getWizard()).saveIntermediateFiles);
		bindingUriText.setEnabled(((ApplyWizard) getWizard()).saveBinding);
		diffUriText.setText(diff == null ? "" : diff.eResource().getURI().toString());
		modelUriText.setText(model == null ? "" : model.getURI().toString());
		if (((ApplyWizard) getWizard()).saveIntermediateFiles) {
			newModelUriText.setText(newModelFile == null ? "" : URI.createPlatformResourceURI(
					newModelFile.getFullPath().toString(), true).toString());
			emfdiffUriText.setText(emfdiffFile == null ? "" : URI.createPlatformResourceURI(
					emfdiffFile.getFullPath().toString(), true).toString());
		}
		if (((ApplyWizard) getWizard()).saveBinding) {
			bindingUriText.setText(bindingFile == null ? "" : URI.createPlatformResourceURI(
					bindingFile.getFullPath().toString(), true).toString());
		}
		resolvedLabel.setText(resolved == null ? "" : resolved.getResolutionByChange().keySet().size() + " / "
				+ countChanges(resolved.getMPatchModel()) + " " + RESOLVED_LABEL_TEXT);

		dialogChanged();
	}

	private int countChanges(MPatchModel mpatch) {
		int counter = 0;
		Queue<IndepChange> queue = new LinkedList<IndepChange>();
		queue.addAll(mpatch.getChanges());
		while (!queue.isEmpty()) {
			IndepChange change = queue.poll();
			if (change instanceof ChangeGroup) {
				queue.addAll(((ChangeGroup) change).getSubChanges());
			} else
				counter++;
		}
		return counter;
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			initialize();
		}
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	boolean reviewDiffApplication() {
		return reviewDiffApplicationButton.getSelection();
	}

	/**
	 * Set status and notify user if something is wrong.
	 */
	private void dialogChanged() {
		// just a last check if all fields are set
		if (diffUriText.getText().trim().length() == 0) {
			updateStatus(MPatchConstants.MPATCH_SHORT_NAME + " is not set!");
		} else if (modelUriText.getText().trim().length() == 0) {
			updateStatus("Model is not set!");
		} else if (((ApplyWizard) getWizard()).saveIntermediateFiles
				&& emfdiffUriText.getText().trim().length() == 0) {
			updateStatus("Emfdiff is not set!");
		} else if (((ApplyWizard) getWizard()).saveIntermediateFiles
				&& newModelUriText.getText().trim().length() == 0) {
			updateStatus("New model is not set!");
		} else if (((ApplyWizard) getWizard()).saveBinding && bindingUriText.getText().trim().length() == 0) {
			updateStatus("Binding file is not set!");
		} else {
			updateStatus(null);
		}
	}
}

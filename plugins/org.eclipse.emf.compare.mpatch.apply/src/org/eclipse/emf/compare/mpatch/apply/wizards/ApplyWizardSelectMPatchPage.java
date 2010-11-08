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

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


/**
 * Wizard page for selecting a {@link MPatchModel} from the workspace.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class ApplyWizardSelectMPatchPage extends WizardPage {

	/** The uri of the selected file. */
	private URI uri;

	/** The text element on the page to display the uri. */
	private Text uriText;

	/** A viewer for presenting the content of the input. */
	private TreeViewer viewer;

	/** Adapter factory for emf model. */
	private final AdapterFactory adapterFactory;
	
	/**
	 * Constructor for this wizard page.
	 * 
	 * @param pageName
	 *            The name and title of this particular page.
	 * @param file
	 *            The default filename which can be provided.
	 */
	public ApplyWizardSelectMPatchPage(String pageName, IFile file, AdapterFactory adapterFactory) {
		super(pageName);
		if (file != null) {
			uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
		}
		setTitle(pageName);
		setDescription("Select " + MPatchConstants.MPATCH_LONG_NAME);
		this.adapterFactory = adapterFactory;
	}

	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		final GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		final Label label = new Label(container, SWT.NULL);
		label.setText("&" + MPatchConstants.MPATCH_LONG_NAME + ":");

		uriText = new Text(container, SWT.BORDER | SWT.SINGLE);
		final GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		uriText.setLayoutData(gd);
		// uriText.setEditable(false);
		uriText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		final Button button = new Button(container, SWT.PUSH);
		button.setText("B&rowse...");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});

		final GridData gd1 = new GridData(GridData.FILL_HORIZONTAL);
		gd1.horizontalSpan = 3;
		final Label infoLabel = new Label(container, SWT.NULL);
		infoLabel.setLayoutData(gd1);
		infoLabel.setText("Contents of the selected resource:");
		final GridData gd2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
		gd2.horizontalSpan = 3;
		gd2.grabExcessHorizontalSpace = true;
		gd2.grabExcessVerticalSpace = true;

		viewer = new TreeViewer(container, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getTree().setLayoutData(gd2);
		viewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
		viewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));

		initialize();
		// dialogChanged(); // is already called because of notifiers!
		setControl(container);
	}

	/**
	 * Set the uri as the default input.
	 */
	private void initialize() {
		if (uri != null && uriText.getText().length() == 0) {
			uriText.setText(uri.toString());
		} else {
			uriText.setText("");
		}
	}

	/**
	 * Ask user to select an mpatch from the workspace.
	 */
	private void handleBrowse() {
		final ResourceDialog dialog = new ResourceDialog(getShell(), "Select " + MPatchConstants.MPATCH_LONG_NAME, SWT.OPEN
				| SWT.SINGLE);
		if (dialog.open() == ResourceDialog.OK) {
			if (dialog.getURIs().size() >= 1) {
				uri = dialog.getURIs().get(0);
				uriText.setText(uri.toString());
			} else {
				uri = null;
				uriText.setText("");
			}
		}
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	/**
	 * Get the selected file, check whether it is ok and present its content to the user via the tree viewer. Print an
	 * appropriate error message if the input is incorrect.
	 */
	private void dialogChanged() {
		final String fileUri = uriText.getText().trim();
		viewer.setInput(null);
		if (fileUri.length() > 0) {

			try {
				uri = URI.createURI(fileUri);
				final ResourceSet resourceSet = new ResourceSetImpl();
				final Resource diffResource = resourceSet.getResource(uri, true);
				if (diffResource.getContents().get(0) instanceof MPatchModel) {
					final MPatchModel mpatch = (MPatchModel) diffResource.getContents().get(0);
					((ApplyWizard) getWizard()).setMPatch(mpatch);
					viewer.setInput(diffResource);
					updateStatus(null);
				} else {
					updateStatus("The selected resource does not contain valid " + MPatchConstants.MPATCH_LONG_NAME + "!");
					uri = null;
				}
			} catch (final RuntimeException e) {
				updateStatus("Exception loading resource: " + e.getMessage() + " " + e.getStackTrace()[0] + "...");
			}
		} else {
			updateStatus("Please select an input file.");
		}
	}

}

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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * A wizard page for selecting an arbitrary emf model file to which the {@link MPatchModel} should be applied.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class ApplyWizardSelectModelPage extends WizardPage implements ISelectionChangedListener {

	/** The uri of the selected file. */
	private URI uri;

	/** The text widget which is used to present the file uri to the user. */
	private Text uriText;

	/** A viewer to present the content of the selected file to the user. */
	private TreeViewer viewer;

	/** Adapter factory for emf model. */
	private final AdapterFactory adapterFactory;

	/**
	 * The constructor of this page.
	 * 
	 * @param pageName
	 *            The name and title of this page.
	 */
	public ApplyWizardSelectModelPage(String pageName, URI modelURI, AdapterFactory adapterFactory) {
		super(pageName);
		uri = modelURI;
		setTitle(pageName);
		setDescription("Select a model to which the " + MPatchConstants.MPATCH_SHORT_NAME + " should be applied.");
		this.adapterFactory = adapterFactory;
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		final GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		final Label label = new Label(container, SWT.NULL);
		label.setText("&Target model:");

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
		viewer.addSelectionChangedListener(this);

		final GridData gd3 = new GridData(GridData.FILL_HORIZONTAL);
		gd3.horizontalSpan = 3;
		final Label hintLabel = new Label(container, SWT.WRAP);
		hintLabel.setLayoutData(gd3);
		hintLabel.setText("Hint: You may also select a sub-model that contains ALL RELEVANT ELEMENTS for "
				+ MPatchConstants.MPATCH_SHORT_NAME + " application (improves performance).");

		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Set the uri as the default input.
	 */
	private void initialize() {
		if (uri != null && uriText.getText().length() == 0) {
			uriText.setText(uri.toString());
		} else {
			final URI diffURI = ((ApplyWizard) getWizard()).getMPatch().eResource().getURI();
			uriText.setText(diffURI.toString().substring(0,
					diffURI.toString().length() - diffURI.lastSegment().length()));
		}
	}

	/**
	 * Ask user to select an emf model file from the workspace.
	 */
	private void handleBrowse() {
		final ResourceDialog dialog = new ApplyToResourceDialog(getShell(), "Select model", SWT.OPEN | SWT.SINGLE);
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
	 * appropriate error message if the input is not an emf model.
	 */
	private void dialogChanged() {
		final String fileUri = uriText.getText().trim();
		viewer.setInput(null);
		if (fileUri.length() > 0) {

			try {
				uri = URI.createURI(fileUri);
				final ResourceSet resourceSet = new ResourceSetImpl();
				final Resource modelResource = resourceSet.getResource(uri, true);
				if (modelResource.getContents().size() != 1) {
					updateStatus("At the moment, only one root object is allowed!");
					viewer.setInput(modelResource);
					uri = null;
					return;
				} else if (modelResource.getContents().get(0) instanceof EObject) {
					((ApplyWizard) getWizard()).setModelResource(modelResource);
					viewer.setInput(modelResource);
				} else {
					updateStatus("The selected resource does not contain a valid model!");
					uri = null;
					return;
				}
			} catch (final RuntimeException e) {
				updateStatus("Exception loading resource: " + e.getMessage() + " " + e.getStackTrace()[0] + "...");
				return;
			}

			updateStatus(null);
		} else {
			updateStatus("Please select an input file.");
		}
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		final ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() == 1) {
				final Object obj = structuredSelection.getFirstElement();
				if (obj instanceof EObject)
					((ApplyWizard) getWizard()).setModelTarget((EObject) obj);
			}
		}
	}

	/**
	 * Resource dialog with suggested path.
	 * 
	 * @author Patrick Koenemann (pk@imm.dtu.dk)
	 *
	 */
	private final class ApplyToResourceDialog extends ResourceDialog {
		private ApplyToResourceDialog(Shell parent, String title, int style) {
			super(parent, title, style);
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			final Composite composite = (Composite) super.createDialogArea(parent);
			uriField.setText(ApplyWizardSelectModelPage.this.uriText.getText());
			return composite;
		}

		@Override
		protected void prepareBrowseWorkspaceButton(Button browseWorkspaceButton) {
			browseWorkspaceButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					// FIXME: Unfortunately the dialog does not recognize our default selection :-(
					IFile[] iFiles = new IFile[0];
					try {
						iFiles = new IFile[] { ResourcesPlugin.getWorkspace().getRoot().getFile(
								new Path(uriField.getText())), };
					} catch (Exception e) { }
					final IFile[] files = WorkspaceResourceDialog.openFileSelection(getShell(),
							"Apply differences to...", null, false, iFiles, null);
					if (files.length != 0) {
						final IFile file = files[0];
						if (file != null) {
							uriField.setText(URI.createPlatformResourceURI(file.getFullPath().toString(), true)
									.toString());
						}
					}
				}
			});
		}
	}
}

/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.dsl.wizard;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

// some code is based on org.eclipse.team.internal.ui.wizards.ExportProjectSetLocationPage
// role-model: org.eclipse.team.internal.ccvs.ui.wizards.GenerateDiffFileWizard.LocationPage

/**
 * @author Moritz Eysholdt
 */
public class SaveFileWizardPage extends WizardPage {
	class LocationPageContentProvider extends BaseWorkbenchContentProvider {
		// Never show closed projects
		boolean showClosedProjects = false;

		@Override
		public Object[] getChildren(Object element) {
			if (element instanceof IWorkspace) {
				// check if closed projects should be shown
				IProject[] allProjects = ((IWorkspace)element).getRoot().getProjects();
				if (showClosedProjects)
					return allProjects;

				ArrayList<IProject> accessibleProjects = new ArrayList<IProject>();
				for (int i = 0; i < allProjects.length; i++) {
					if (allProjects[i].isOpen()) {
						accessibleProjects.add(allProjects[i]);
					}
				}
				return accessibleProjects.toArray();
			}

			return super.getChildren(element);
		}
	}

	protected enum SaveTarget {
		CLIPBOARD, FILE, WORKSPACE
	}

	class WorkspaceDialog extends TitleAreaDialog {

		private Button okButton;

		protected IContainer wsContainer;

		protected Text wsFilenameText;

		protected TreeViewer wsTreeViewer;

		public WorkspaceDialog(Shell shell) {
			super(shell);
		}

		@Override
		protected void cancelPressed() {
			// this.page.validatePage();
			getSelectedContainer();
			super.cancelPressed();
		}

		@Override
		protected void createButtonsForButtonBar(Composite parent) {
			super.createButtonsForButtonBar(parent);
			okButton = getButton(IDialogConstants.OK_ID);
		}

		@Override
		protected Control createContents(Composite parent) {
			Control control = super.createContents(parent);
			setTitle("Select File");
			setMessage("Select File");

			return control;
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Composite composite = (Composite)super.createDialogArea(parent);

			GridLayout layout = new GridLayout();
			layout.numColumns = 1;
			composite.setLayout(layout);
			final GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
			composite.setLayoutData(data);

			getShell().setText("Select File");

			wsTreeViewer = new TreeViewer(composite, SWT.BORDER);
			final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
			gd.widthHint = 550;
			gd.heightHint = 250;
			wsTreeViewer.getTree().setLayoutData(gd);

			wsTreeViewer.setContentProvider(new LocationPageContentProvider());
			wsTreeViewer.setLabelProvider(new WorkbenchLabelProvider());
			wsTreeViewer.setInput(ResourcesPlugin.getWorkspace());

			final Composite group = new Composite(composite, SWT.NONE);
			layout = new GridLayout(2, false);
			layout.marginWidth = 0;
			group.setLayout(layout);
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			final Label label = new Label(group, SWT.NONE);
			label.setLayoutData(new GridData());
			label.setText("Select File");

			wsFilenameText = new Text(group, SWT.BORDER);
			wsFilenameText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			wsFilenameText.setText("patch.epatch");
			setupListeners();

			return parent;
		}

		private void getSelectedContainer() {
			Object obj = ((IStructuredSelection)wsTreeViewer.getSelection()).getFirstElement();

			if (obj instanceof IContainer)
				wsContainer = (IContainer)obj;
			else if (obj instanceof IFile) {
				wsContainer = ((IFile)obj).getParent();
			}
		}

		@Override
		protected void okPressed() {
			// Make sure that a container has been selected
			if (wsContainer == null) {
				getSelectedContainer();
			}
			// Assert.isNotNull(wsContainer);

			workspaceFile = wsContainer.getFile(new Path(wsFilenameText.getText()));
			if (workspaceFile != null) {
				workspaceText.setText(workspaceFile.getFullPath().toString());
			}
			// this.page.validatePage();
			// workspaceText.setText(wsFilenameText.getText());
			super.okPressed();
		}

		void setupListeners() {
			wsTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					IStructuredSelection s = (IStructuredSelection)event.getSelection();
					Object obj = s.getFirstElement();
					if (obj != null) {

					}
					if (obj instanceof IContainer)
						wsContainer = (IContainer)obj;
					else if (obj instanceof IFile) {
						IFile tempFile = (IFile)obj;
						wsContainer = tempFile.getParent();
						wsFilenameText.setText(tempFile.getName());
					}
				}
			});

			wsTreeViewer.addDoubleClickListener(new IDoubleClickListener() {
				public void doubleClick(DoubleClickEvent event) {
					ISelection s = event.getSelection();
					if (s instanceof IStructuredSelection) {
						Object item = ((IStructuredSelection)s).getFirstElement();
						if (wsTreeViewer.getExpandedState(item))
							wsTreeViewer.collapseToLevel(item, 1);
						else
							wsTreeViewer.expandToLevel(item, 1);
					}
				}
			});

			wsFilenameText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					String patchName = wsFilenameText.getText();
					if (patchName.trim().equals("")) { //$NON-NLS-1$
						okButton.setEnabled(false);
						setErrorMessage("No file name.");
					} else if (!ResourcesPlugin.getWorkspace().validateName(patchName, IResource.FILE).isOK()) {
						// make sure that the filename does not contain more than one segment
						okButton.setEnabled(false);
						setErrorMessage("Invalid file name.");
					} else {
						okButton.setEnabled(true);
						setErrorMessage(null);
					}
				}
			});
		}
	}

	private Button clipboardRadio;

	private String file = ""; //$NON-NLS-1$

	private Combo fileCombo;

	private Button fileRadio;

	private SaveTarget target;

	private IFile workspaceFile;

	private Button workspaceRadio;

	private Text workspaceText;

	public SaveFileWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		setDescription("Export Epatch");
	}

	/**
	 * Creates composite control and sets the default layout data.
	 * 
	 * @param parent
	 *            the parent of the new composite
	 * @param numColumns
	 *            the number of columns for the new composite
	 * @return the newly-created coposite
	 */
	protected Composite createComposite(Composite parent, int numColumns) {
		Composite composite = new Composite(parent, SWT.NULL);

		// GridLayout
		GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		composite.setLayout(layout);

		// GridData
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);
		return composite;
	}

	public void createControl(Composite parent) {
		Composite composite = createComposite(parent, 1);
		initializeDialogUnits(composite);

		Group locationGroup = new Group(composite, SWT.None);
		GridLayout layout = new GridLayout();
		locationGroup.setLayout(layout);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		locationGroup.setLayoutData(data);
		locationGroup.setText("Select the export destination:");

		createExportToClipboard(locationGroup);

		createExportToFile(locationGroup);

		createExportToWorkspace(locationGroup);

		target = SaveTarget.WORKSPACE;

		setControl(composite);
		updateEnablement();
		Dialog.applyDialogFont(parent);
	}

	/**
	 * Create a drop-down combo box specific for this application
	 * 
	 * @param parent
	 *            the parent of the new combo box
	 * @return the new combo box
	 */
	protected Combo createDropDownCombo(Composite parent) {
		Combo combo = new Combo(parent, SWT.DROP_DOWN);
		GridData comboData = new GridData(GridData.FILL_HORIZONTAL);
		comboData.verticalAlignment = GridData.CENTER;
		comboData.grabExcessVerticalSpace = false;
		comboData.widthHint = IDialogConstants.ENTRY_FIELD_WIDTH;
		combo.setLayoutData(comboData);
		return combo;
	}

	private void createExportToClipboard(Composite composite) {
		clipboardRadio = new Button(composite, SWT.RADIO);
		clipboardRadio.setText("Clipboard");
		clipboardRadio.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				target = SaveTarget.CLIPBOARD;
				updateEnablement();
			}
		});
	}

	private void createExportToFile(Composite composite) {
		fileRadio = new Button(composite, SWT.RADIO);
		fileRadio.setText("File");
		fileRadio.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				target = SaveTarget.FILE;
				file = fileCombo.getText();
				updateEnablement();
			}
		});

		Composite inner = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		inner.setLayout(layout);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
		inner.setLayoutData(data);

		fileCombo = createDropDownCombo(inner);
		file = ""; // TODO: get default
		// /fileCombo.setItems(PsfFilenameStore.getHistory());
		// TODO: get History
		fileCombo.setText(file);
		fileCombo.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				file = fileCombo.getText();
				updateEnablement();
			}
		});

		Button browseButton = new Button(inner, SWT.PUSH);
		browseButton.setText("Browse...");
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		data.widthHint = Math.max(widthHint, browseButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		browseButton.setLayoutData(data);
		browseButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				// if (!isSaveToFileSystem())
				target = SaveTarget.FILE;

				FileDialog d = new FileDialog(getShell(), SWT.SAVE);
				d.setFilterExtensions(new String[] {"*.epatch"}); //$NON-NLS-1$
				d.setFilterNames(new String[] {"*.epatch"});
				d.setFileName("patch.epatch"); // TODO customize
				String fileName = file;
				if (fileName != null) {
					int separator = fileName.lastIndexOf(System.getProperty("file.separator").charAt(0)); //$NON-NLS-1$
					if (separator != -1) {
						fileName = fileName.substring(0, separator);
					}
				}
				d.setFilterPath(fileName);
				String f = d.open();
				if (f != null) {
					fileCombo.setText(f);
					file = f;
				}
			}
		});
	}

	public String getSuggestedPatchName() {
		String name = "";
		switch (target) {
			case CLIPBOARD:
				return "unknown";
			case FILE:
				int separator = file.lastIndexOf(System.getProperty("file.separator").charAt(0)); //$NON-NLS-1$
				name = file.substring(separator);
				break;
			case WORKSPACE:
				name = workspaceFile.getName();
				break;
		}
		int dot = name.indexOf('.');
		if (dot >= 0)
			name = name.substring(0, dot);
		name = name.replaceAll("[^a-zA-Z0-9_]", "");
		return name.length() == 0 ? "unknown" : name;
	}

	private void createExportToWorkspace(Composite composite) {
		workspaceRadio = new Button(composite, SWT.RADIO);
		workspaceRadio.setText("Workspace");
		workspaceRadio.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				target = SaveTarget.WORKSPACE;
				updateEnablement();
			}
		});

		final Composite nameGroup = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		nameGroup.setLayout(layout);
		final GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
		nameGroup.setLayoutData(data);

		workspaceText = createTextField(nameGroup);
		workspaceText.setEditable(false);
		workspaceText.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				file = workspaceFile.getLocation().toString();
				updateEnablement();
			}
		});
		Button wsBrowseButton = new Button(nameGroup, SWT.PUSH);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		gd.widthHint = Math.max(widthHint, wsBrowseButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		wsBrowseButton.setLayoutData(gd);
		wsBrowseButton.setText("Browse...");
		wsBrowseButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				// if (isSaveToFileSystem())
				target = SaveTarget.WORKSPACE;

				WorkspaceDialog d = new WorkspaceDialog(getShell());
				d.open();
			}
		});
	}

	/**
	 * Create a text field specific for this application
	 * 
	 * @param parent
	 *            the parent of the new text field
	 * @return the new text field
	 */
	protected Text createTextField(Composite parent) {
		Text text = new Text(parent, SWT.SINGLE | SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.verticalAlignment = GridData.CENTER;
		data.grabExcessVerticalSpace = false;
		data.widthHint = IDialogConstants.ENTRY_FIELD_WIDTH;
		text.setLayoutData(data);
		return text;
	}

	public void refreshWorkspaceFile(IProgressMonitor monitor) throws CoreException {
		if (workspaceFile != null)
			workspaceFile.refreshLocal(IResource.DEPTH_ONE, monitor);
	}

	public void save(Resource res) throws IOException {
		switch (target) {
			case CLIPBOARD:
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				res.save(baos, null);
				TextTransfer plainTextTransfer = TextTransfer.getInstance();
				Clipboard clipboard = new Clipboard(getShell().getDisplay());
				clipboard.setContents(new String[] {baos.toString()}, new Transfer[] {plainTextTransfer});
				clipboard.dispose();
				break;

			case FILE:
				res.setURI(URI.createFileURI(file));
				res.save(null);
				break;

			case WORKSPACE:
				validateEditWorkspaceFile(getShell());
				res.setURI(URI.createPlatformResourceURI(workspaceFile.getFullPath().toString(), true));
				System.out.println("saving to " + res.getURI());
				res.save(null);
				break;
		}
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			workspaceText.setFocus();
		}
	}

	private void updateEnablement() {
		boolean complete;
		// update radio buttons
		fileRadio.setSelection(target == SaveTarget.FILE);
		workspaceRadio.setSelection(target == SaveTarget.WORKSPACE);
		clipboardRadio.setSelection(target == SaveTarget.CLIPBOARD);

		if (target == SaveTarget.FILE || target == SaveTarget.WORKSPACE) {
			if (file.length() == 0 || target == SaveTarget.WORKSPACE && workspaceFile == null) {
				setErrorMessage("No file specified.");
				complete = false;
			} else {
				File f = new File(file);
				if (f.isDirectory()) {
					setErrorMessage("You have specified a folder.");
					complete = false;
				} else {
					complete = true;
				}
			}
		} else
			complete = true;

		if (complete) {
			setErrorMessage(null);
			setDescription("Complete");
		}
		setPageComplete(complete);
	}

	public void validateEditWorkspaceFile(Shell shell) {
		if (workspaceFile == null || !workspaceFile.exists() || !workspaceFile.isReadOnly())
			return;
		IStatus status = ResourcesPlugin.getWorkspace().validateEdit(new IFile[] {workspaceFile}, shell);
		if (!status.isOK()) {
			throw new RuntimeException("Can not save file " + workspaceFile + "(" + status + ")");
		}
	}

}

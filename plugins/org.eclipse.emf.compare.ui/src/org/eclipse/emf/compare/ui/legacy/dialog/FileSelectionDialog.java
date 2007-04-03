/*******************************************************************************
 * Copyright (c) 2006, Intalio Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Intalio Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.legacy.dialog;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author <a href="http://www.intalio.com">&copy; Intalio, Inc.</a>
 * 
 */
public class FileSelectionDialog extends IconAndMessageDialog {

	private static final String SHELL_TITLE = "Select a file";

	private class FileSelectionLabelProvider implements ILabelProvider {

		/**
		 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
		 */
		public Image getImage(final Object element) {
			// TODO Add image support
			return null;
		}

		/**
		 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 */
		public String getText(final Object element) {
			if (element instanceof IFile) {
				return ((IFile) element).getProjectRelativePath()
						.toPortableString();
			}
			return element.toString();
		}

		/**
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void addListener(final ILabelProviderListener listener) {
		}

		/**
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
		 */
		public void dispose() {
		}

		/**
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object,
		 *      java.lang.String)
		 */
		public boolean isLabelProperty(final Object element, final String property) {
			return false;
		}

		/**
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void removeListener(final ILabelProviderListener listener) {
		}

	}

	private class FileSelectionContentProvider implements
			IStructuredContentProvider {

		/**
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {
		}

		/**
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		}

		/**
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements(final Object inputElement) {
			if (inputElement instanceof IFile[]) {
				return ((IFile[]) inputElement);
			}
			return null;
		}

	}

	private IFile selectedFile;

	private IFile[] files;

	private Button okButton;

	private Button cancelButton;

	/**
	 * @param parentShell
	 */
	public FileSelectionDialog(final Shell parentShell, final IFile[] files, final String title) {
		super(parentShell);
		this.files = files;
		this.message = title;
	}

	protected Control createDialogArea(final Composite parent) {

		createMessageArea(parent);
		final Composite topLevel = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		topLevel.setLayout(layout);
		final GridData gd = new GridData(GridData.VERTICAL_ALIGN_END);
		gd.horizontalSpan = 2;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		topLevel.setLayoutData(gd);
		topLevel.setFont(parent.getFont());
		final TableViewer fileTable = new TableViewer(topLevel, SWT.SINGLE
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		fileTable.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		fileTable.setContentProvider(new FileSelectionContentProvider());
		fileTable.setLabelProvider(new FileSelectionLabelProvider());
		fileTable.setInput(this.files);
		fileTable.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(final SelectionChangedEvent event) {
				FileSelectionDialog.this.okButton.setEnabled(true);
				if (event.getSelection() instanceof IStructuredSelection) {
					FileSelectionDialog.this.selectedFile = (IFile) ((IStructuredSelection) event
							.getSelection()).getFirstElement();
				}

			}

		});
		fileTable.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(final DoubleClickEvent event) {
				buttonPressed(IDialogConstants.OK_ID);

			}
		});
		return topLevel;
	}

	/**
	 * Adds buttons to this dialog's button bar.
	 * <p>
	 * The <code>Dialog</code> implementation of this framework method adds
	 * standard ok and cancel buttons using the <code>createButton</code>
	 * framework method. These standard buttons will be accessible from
	 * <code>getCancelButton</code>, and <code>getOKButton</code>.
	 * Subclasses may override.
	 * </p>
	 * 
	 * @param parent
	 *            the button bar composite
	 */
	protected void createButtonsForButtonBar(final Composite parent) {
		// create OK and Cancel buttons by default
		this.okButton = createButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.OK_LABEL, false);
		this.okButton.setEnabled(false);
		this.okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				buttonPressed(IDialogConstants.OK_ID);
			}
		});
		this.cancelButton = createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, true);
		this.cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				buttonPressed(IDialogConstants.CANCEL_ID);
			}
		});
	}

	protected void buttonPressed(final int code) {
		setReturnCode(code);
		close();
	}

	public IFile getSelectedFile() {
		return this.selectedFile;
	}

	/**
	 * @see org.eclipse.jface.dialogs.IconAndMessageDialog#getImage()
	 */
	@Override
	protected Image getImage() {
		// TODO add image support, will need logo
		return getInfoImage();
	}

	/*
	 * (non-Javadoc) Method declared in Window.
	 */
	protected void configureShell(final Shell shell) {
		super.configureShell(shell);
		shell.setText(SHELL_TITLE);
	}
}

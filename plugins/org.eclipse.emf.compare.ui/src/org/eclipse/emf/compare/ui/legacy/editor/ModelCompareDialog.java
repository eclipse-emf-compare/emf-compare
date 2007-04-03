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
package org.eclipse.emf.compare.ui.legacy.editor;

import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;

import org.eclipse.compare.internal.ResizableDialog;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.ui.legacy.org.eclipse.compare.CompareEditorInput;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author <a href="http://www.intalio.com">&copy; Intalio, Inc.</a>
 * 
 */
public class ModelCompareDialog extends ResizableDialog {

	public static final int VIEW_ONLY = 0;

	public static final int REPLACE_WITH = 1;

	private CompareEditorInput fCompareEditorInput;

	private Button closeButton;

	private Button cancelButton;

	private Button okButton;

	private int mode;

	public ModelCompareDialog(final Shell shell, final CompareEditorInput input) {
		this(shell, input, VIEW_ONLY);
	}

	public ModelCompareDialog(final Shell shell, final CompareEditorInput input, final int option) {
		super(
				shell,
				ResourceBundle
						.getBundle("org.eclipse.emf.compare.ui.legacy.editor.UMLCompareDialogResources"));
		this.mode = option;
		Assert.isNotNull(input);
		assert (input instanceof ModelCompareEditorInput);
		this.fCompareEditorInput = input;
		try {
			input.run(new NullProgressMonitor());
		} catch (final InterruptedException e) {
			EMFComparePlugin.getDefault().log(e,false);
		} catch (final InvocationTargetException e) {
			EMFComparePlugin.getDefault().log(e,false);
		}
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	protected void createButtonsForButtonBar(final Composite parent) {
		if (this.mode == VIEW_ONLY) {
			this.closeButton = createButton(parent, IDialogConstants.OK_ID,
					IDialogConstants.CLOSE_LABEL, true); 
			this.closeButton.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(final SelectionEvent e) {

				}

				public void widgetSelected(final SelectionEvent e) {
					ModelCompareDialog.this.close();

				}
			});
		} else if (this.mode == REPLACE_WITH) {
			this.cancelButton = createButton(parent, IDialogConstants.CANCEL_ID,
					IDialogConstants.CANCEL_LABEL, true); 
			this.cancelButton.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(final SelectionEvent e) {

				}

				public void widgetSelected(final SelectionEvent e) {
					ModelCompareDialog.this.close();

				}
			});
			this.okButton = createButton(parent, IDialogConstants.OK_ID,
					IDialogConstants.OK_LABEL, true); 
			this.okButton.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(final SelectionEvent e) {
					setReturnCode(IDialogConstants.OK_ID);
					ModelCompareDialog.this.close();

				}
			});
		} else {
			// we shouldn't be there since all options have been considered
			assert (false);
		}
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	protected Control createDialogArea(final Composite parent2) {

		final Composite parent = (Composite) super.createDialogArea(parent2);

		final Control c = this.fCompareEditorInput.createContents(parent);
		c.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Shell shell = c.getShell();
		shell.setText(this.fCompareEditorInput.getTitle());
		shell.setImage(this.fCompareEditorInput.getTitleImage());
		applyDialogFont(parent);
		return parent;
	}

	public Object getSelectedState() {
		return ((ModelCompareEditorHistoryInput) this.fCompareEditorInput)
				.getCurrentState();
	}
}

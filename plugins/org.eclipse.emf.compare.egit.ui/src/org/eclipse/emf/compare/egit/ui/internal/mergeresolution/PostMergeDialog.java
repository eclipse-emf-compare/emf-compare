/*******************************************************************************
 * Copyright (c) 2015 EclipseSource GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Borkowski - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.egit.ui.internal.mergeresolution;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.compare.egit.ui.internal.EMFCompareEGitUIMessages;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * Dialog for displaying a dialog after resolving a merge conflict.
 * 
 * @author Michael Borkowski <mborkowski@eclipsesource.com>
 */
public class PostMergeDialog extends Dialog {
	/**
	 * UI margin for left and right of dialog.
	 */
	private static final int MARGIN_WIDTH = 15;

	/**
	 * UI margin for top for dialog.
	 */
	private static final int MARGIN_TOP = 15;

	/**
	 * UI vertical margin between label and list.
	 */
	private static final int VERTICAL_SPACING = 10;

	/**
	 * The resources to be staged.
	 */
	private final IResource[] resources;

	/**
	 * Creates a new {@link PostMergeDialog}.
	 * 
	 * @param parentShell
	 *            the parent shell
	 * @param resources
	 *            the resources to be staged
	 */
	public PostMergeDialog(Shell parentShell, IResource[] resources) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		setBlockOnOpen(true);
		this.resources = resources;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(EMFCompareEGitUIMessages.getString("post.merge.dialog.title")); //$NON-NLS-1$
	}

	@Override
	public Control createDialogArea(final Composite parent) {
		final Composite composite = (Composite)super.createDialogArea(parent);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.verticalSpacing = VERTICAL_SPACING;
		gridLayout.marginWidth = MARGIN_WIDTH;
		gridLayout.marginTop = MARGIN_TOP;
		composite.setLayout(gridLayout);
		// result
		Label resultLabel = new Label(composite, SWT.NONE);
		resultLabel.setText(EMFCompareEGitUIMessages.getString("post.merge.dialog.text")); //$NON-NLS-1$
		resultLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

		TableViewer viewer = new TableViewer(composite);

		viewer.setContentProvider(ArrayContentProvider.getInstance());

		final IStyledLabelProvider styleProvider = new ListStyleProvider();
		viewer.setLabelProvider(new DelegatingStyledCellLabelProvider(styleProvider));
		applyDialogFont(composite);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL)
				.applyTo(viewer.getControl());
		viewer.setInput(resources);

		return composite;
	}

	/**
	 * Helper class for styling items.
	 * 
	 * @author <mborkowski@eclipsesource.com>
	 */
	public class ListStyleProvider implements IStyledLabelProvider {
		/**
		 * The label provider.
		 */
		private final WorkbenchLabelProvider wrapped = new WorkbenchLabelProvider();

		/**
		 * {@inheritDoc}
		 */
		public void removeListener(ILabelProviderListener listener) {
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		public void dispose() {
			wrapped.dispose();
		}

		/**
		 * {@inheritDoc}
		 */
		public void addListener(ILabelProviderListener listener) {
		}

		/**
		 * {@inheritDoc}
		 */
		public StyledString getStyledText(Object element) {
			IResource resource = (IResource)element;
			return new StyledString(resource.getProjectRelativePath().toString());
		}

		/**
		 * {@inheritDoc}
		 */
		public Image getImage(Object element) {
			return wrapped.getImage(element);
		}
	}

}

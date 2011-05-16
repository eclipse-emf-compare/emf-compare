/* This file has been inspired from org.eclipse.jdt.internal.junit.ui.CompareResultDialog*/
/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.compare.IEncodedStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.contentmergeviewer.TextMergeViewer;
import org.eclipse.compare.internal.MergeViewerContentProvider;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.ui.viewer.structure.ModelStructureMergeViewer;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog to show textual differences between text fields.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.2
 */
public class CompareTextDialog extends TrayDialog {
	/** This will be used as the dialog title when errors occur. */
	protected static final String ERROR_DIALOG_TITLE = EMFCompareUIMessages
			.getString("CompareTextDialog_titleErrorMsg"); //$NON-NLS-1$

	/** Configuration key for the prefix (and suffix) property. */
	private static final String PREFIX_SUFFIX_PROPERTY = "org.eclipse.emf.compare.ui.CompareTextDialog.prefixSuffix"; //$NON-NLS-1$

	/** The current meta attribute (text field) to compare. */
	protected EAttribute attribute;

	/** The current comparison element. */
	protected UpdateAttribute changeCompare;

	/** The compare configuration of the parent viewer. */
	protected CompareConfiguration parentConfiguration;

	/** The input of the parent viewer. */
	protected Object parentInput;

	/** Typed element holding the ancestor of this comparison. */
	protected ITypedElement ancestor;

	/** Typed element holding the left compared element. */
	protected ITypedElement left;

	/** Typed element holding the right compared element. */
	protected ITypedElement right;

	/** The actual viewer on which the comparison will be displayed. */
	private TextMergeViewer fViewer;

	/** The current value (text) to compare (right side). */
	private String rightElement;

	/** The current value (text) to compare (left side). */
	private String leftElement;

	/** The current value (text) to compare (ancestor side). */
	private String ancestorElement;

	/** Flag to check if the current comparison is conflicting. */
	private boolean isConflicting;

	/**
	 * Lengths of common prefix and suffix. Note: this array is passed to the DamagerRepairer and the lengths
	 * are updated on content change.
	 */
	private final int[] fPrefixSuffix = new int[2];

	/** The Compare Pane that will hold the viewers for this comparison. */
	private CompareViewerPane fCompareViewerPane;

	/**
	 * This implementation of a typed element will be used as input for the text comparison dialog.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	private static class CompareElement implements ITypedElement, IEncodedStreamContentAccessor {
		/** String that is to be compared. */
		private String fContent;

		/**
		 * Instantiates an {@link ITypedElement} for the given String.
		 * 
		 * @param content
		 *            The String we are to wrap within an {@link ITypedElement}.
		 */
		public CompareElement(String content) {
			fContent = content;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.ITypedElement#getName()
		 */
		public String getName() {
			return "<no name>"; //$NON-NLS-1$
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.ITypedElement#getImage()
		 */
		public Image getImage() {
			return null;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.ITypedElement#getType()
		 */
		public String getType() {
			return "txt"; //$NON-NLS-1$
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.IStreamContentAccessor#getContents()
		 */
		public InputStream getContents() {
			try {
				return new ByteArrayInputStream(fContent.getBytes("UTF-8")); //$NON-NLS-1$
			} catch (UnsupportedEncodingException e) {
				return new ByteArrayInputStream(fContent.getBytes());
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.IEncodedStreamContentAccessor#getCharset()
		 */
		public String getCharset() {
			return "UTF-8"; //$NON-NLS-1$
		}

	}

	/**
	 * Constructor.
	 * 
	 * @param parentShell
	 *            The shell of the parent viewer.
	 * @param element
	 *            The current comparison element.
	 */
	public CompareTextDialog(Shell parentShell, UpdateAttribute element) {
		super(parentShell);
		setShellStyle(getShellStyle() & ~SWT.APPLICATION_MODAL | SWT.TOOL);
		setChangeCompare(element);
	}

	/**
	 * Constructor.
	 * 
	 * @param parentShell
	 *            The shell of the parent viewer.
	 * @param element
	 *            The current comparison element.
	 * @param parentViewer
	 *            The parent viewer.
	 * @param input
	 *            The input of the parent viewer.
	 */
	public CompareTextDialog(Shell parentShell, UpdateAttribute element,
			ModelStructureMergeViewer parentViewer, Object input) {
		this(parentShell, element);
		parentConfiguration = parentViewer.getCompareConfiguration();
		parentInput = input;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
	 */
	@Override
	protected boolean isResizable() {
		return true;
	}

	/**
	 * Define all the elements to compare.
	 * 
	 * @param updateAttribute
	 *            The attribute from which to retrieve values.
	 */
	private void setChangeCompare(UpdateAttribute updateAttribute) {
		changeCompare = updateAttribute;

		attribute = updateAttribute.getAttribute();
		isConflicting = updateAttribute.isConflicting();

		setStringValues(updateAttribute);

		computePrefixSuffix();
	}

	/**
	 * Define the string values to compare.
	 * 
	 * @param updateAttribute
	 *            The attribute from which to retrieve values.
	 */
	private void setStringValues(UpdateAttribute updateAttribute) {
		final EObject oRightElement = updateAttribute.getRightElement();
		final EObject oLeftElement = updateAttribute.getLeftElement();

		final EObject parent = updateAttribute.eContainer();

		try {

			if (parent instanceof ConflictingDiffElement) {
				final ConflictingDiffElement conflict = (ConflictingDiffElement)parent;
				final EObject oAncestorElement = conflict.getOriginElement();
				if (oAncestorElement != null)
					ancestorElement = EFactory.eGetAsString(oAncestorElement, attribute.getName());
			}

			if (oRightElement != null)
				rightElement = EFactory.eGetAsString(oRightElement, attribute.getName());
			if (oLeftElement != null)
				leftElement = EFactory.eGetAsString(oLeftElement, attribute.getName());

			if (rightElement == null)
				rightElement = ""; //$NON-NLS-1$
			if (leftElement == null)
				leftElement = ""; //$NON-NLS-1$
			if (ancestorElement == null)
				ancestorElement = ""; //$NON-NLS-1$

		} catch (FactoryException e) {
			MessageDialog.openError(getShell(), ERROR_DIALOG_TITLE, e.getMessage());
		}

		// TODO To get the IResource or IFile from oRightElement, oLeftElement and oAncestorElement to know if
		// it is editable or not.
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#getDialogBoundsSettings()
	 */
	@Override
	protected IDialogSettings getDialogBoundsSettings() {
		return EMFCompareUIPlugin.getDefault().getDialogSettingsSection(getClass().getName());
	}

	/**
	 * This will take care of computing the accurate prefix and suffix for this comparison.
	 */
	private void computePrefixSuffix() {
		final int end = Math.min(rightElement.length(), leftElement.length());
		int i = 0;
		while (i < end) {
			if (rightElement.charAt(i) != leftElement.charAt(i)) {
				break;
			}
			i++;
		}
		fPrefixSuffix[0] = i;

		int j = rightElement.length() - 1;
		int k = leftElement.length() - 1;
		int l = 0;
		while (k >= i && j >= i) {
			if (rightElement.charAt(j) != leftElement.charAt(k)) {
				break;
			}
			k--;
			j--;
			l++;
		}
		fPrefixSuffix[1] = l;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(EMFCompareUIMessages.getString(
				"CompareTextDialog_title", new Object[] {attribute.getName() })); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID,
				EMFCompareUIMessages.getString("CompareTextDialog_labelOK"), true); //$NON-NLS-1$
		createButton(parent, IDialogConstants.CANCEL_ID,
				EMFCompareUIMessages.getString("CompareTextDialog_labelCancel"), true); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		fViewer.flush(new NullProgressMonitor());
		super.okPressed();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite composite = (Composite)super.createDialogArea(parent);
		final GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);

		fCompareViewerPane = new CompareViewerPane(composite, SWT.BORDER | SWT.FLAT);
		final GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		final int widthHint = 120;
		final int heightHint = 13;
		data.widthHint = convertWidthInCharsToPixels(widthHint);
		data.heightHint = convertHeightInCharsToPixels(heightHint);
		fCompareViewerPane.setLayoutData(data);

		final Control previewer = createPreviewer(fCompareViewerPane);

		fCompareViewerPane.setContent(previewer);
		final GridData gd = new GridData(GridData.FILL_BOTH);
		previewer.setLayoutData(gd);
		applyDialogFont(parent);
		return composite;
	}

	/**
	 * Create the text merge viewer to compare text fields.
	 * 
	 * @param parent
	 *            Parent control of our previewer.
	 * @return The Control of the viewer.
	 */
	private Control createPreviewer(Composite parent) {

		final CompareConfiguration compareConfiguration = new CompareConfiguration();
		compareConfiguration.setLeftLabel(parentConfiguration.getLeftLabel(parentInput));
		compareConfiguration.setLeftEditable(true);
		compareConfiguration.setRightLabel(parentConfiguration.getRightLabel(parentInput));
		compareConfiguration.setRightEditable(true);
		compareConfiguration.setAncestorLabel(parentConfiguration.getAncestorLabel(parentInput));
		compareConfiguration.setProperty(CompareConfiguration.IGNORE_WHITESPACE, Boolean.FALSE);
		compareConfiguration.setProperty(PREFIX_SUFFIX_PROPERTY, fPrefixSuffix);

		fViewer = new TextMergeViewer(parent, SWT.NONE, compareConfiguration);
		fViewer.setContentProvider(new CustomMergeViewerContentProvider(compareConfiguration));
		setCompareViewerInput();

		final Control control = fViewer.getControl();
		control.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				compareConfiguration.dispose();
			}
		});
		return control;
	}

	/**
	 * Define input of the text merge viewer.
	 */
	private void setCompareViewerInput() {
		if (!fViewer.getControl().isDisposed()) {

			if (isConflicting) {
				fViewer.setInput(new DiffNode(Differencer.CONFLICTING, new CompareElement(ancestorElement),
						new CompareElement(leftElement), new CompareElement(rightElement)));
			} else {
				fViewer.setInput(new DiffNode(new CompareElement(leftElement), new CompareElement(
						rightElement)));
			}

			fCompareViewerPane.setText(EMFCompareUIMessages.getString("CompareTextDialog_labelTextField", //$NON-NLS-1$
					new Object[] {attribute.getName() }));
		}
	}

	/**
	 * Initialize the input of the text merge viewer.
	 * 
	 * @param updateAttribute
	 *            The {@link UpdateAttribute} that will serve as input of our viewer.
	 */
	public void setInput(UpdateAttribute updateAttribute) {
		setChangeCompare(updateAttribute);
		setCompareViewerInput();
	}

	/**
	 * This implementation of a Content Provider will be used for the textual comparison dialog's viewer.
	 * 
	 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
	 */
	// TODO LGO raise bug against platform/compare and document restriction
	@SuppressWarnings("restriction")
	public class CustomMergeViewerContentProvider extends MergeViewerContentProvider {
		/**
		 * Constructor.
		 * 
		 * @param cc
		 *            the compare configuration
		 */
		public CustomMergeViewerContentProvider(CompareConfiguration cc) {
			super(cc);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.internal.MergeViewerContentProvider#isLeftEditable(java.lang.Object)
		 */
		@Override
		public boolean isLeftEditable(Object element) {
			// TODO To call the algorithm to know if the resource containing the element is editable.
			return true;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.internal.MergeViewerContentProvider#isRightEditable(java.lang.Object)
		 */
		@Override
		public boolean isRightEditable(Object element) {
			// TODO To call the algorithm to know if the resource containing the element is editable.
			return true;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.internal.MergeViewerContentProvider#saveLeftContent(java.lang.Object,
		 *      byte[])
		 */
		@Override
		public void saveLeftContent(Object element, byte[] bytes) {
			final EObject obj = changeCompare.getLeftElement();
			try {
				EFactory.eSet(obj, attribute.getName(), new String(bytes));
				obj.eResource().save(Collections.EMPTY_MAP);
			} catch (FactoryException e) {
				MessageDialog.openError(getShell(), ERROR_DIALOG_TITLE, e.getMessage());
			} catch (IOException e) {
				MessageDialog.openError(getShell(), ERROR_DIALOG_TITLE, e.getMessage());
			}

		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.internal.MergeViewerContentProvider#saveRightContent(java.lang.Object,
		 *      byte[])
		 */
		@Override
		public void saveRightContent(Object element, byte[] bytes) {
			final EObject obj = changeCompare.getLeftElement();
			try {
				EFactory.eSet(obj, attribute.getName(), new String(bytes));
				obj.eResource().save(Collections.EMPTY_MAP);
			} catch (FactoryException e) {
				MessageDialog.openError(getShell(), ERROR_DIALOG_TITLE, e.getMessage());
			} catch (IOException e) {
				MessageDialog.openError(getShell(), ERROR_DIALOG_TITLE, e.getMessage());
			}
		}

	}

}

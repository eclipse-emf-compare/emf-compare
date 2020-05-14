/*******************************************************************************
 * Copyright (c) 2020 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.sirius.internal.tools.dialog;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.compare.diagram.ide.ui.sirius.CompareDiagramIDEUISiriusPlugin;
import org.eclipse.gmf.runtime.diagram.ui.image.ImageFileFormat;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.sirius.common.ui.tools.api.util.SWTUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog used to export a graphical comparison as image.
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
public class ExportComparisonDialog extends Dialog {

	/**
	 * The id for the persistent settings for this dialog.
	 */
	public static final String DIALOG_SETTINGS_ID = "ExportComparisonAsImagesDialog"; //$NON-NLS-1$

	/**
	 * Default extension image.
	 */
	protected static final ImageFileFormat DEFAULT_VALUE = ImageFileFormat.JPG;

	/**
	 * The list of values for this enumerated type.
	 */
	protected static final ImageFileFormat[] SAFE_VALUES = {DEFAULT_VALUE, ImageFileFormat.PNG,
			ImageFileFormat.BMP, ImageFileFormat.GIF, };

	/**
	 * The empty string.
	 */
	protected static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/**
	 * The image format label text.
	 */
	protected static final String IMAGE_FORMAT_LABEL = "&Image Format";

	/**
	 * The browse button text.
	 */
	protected static final String BROWSE_LABEL = "&Browse...";

	/**
	 * The id for the persistent image format setting for this dialog.
	 */
	protected static final String DIALOG_SETTINGS_IMAGE_FORMAT = "ExportComparisonAsImageDialog.imageFormat"; //$NON-NLS-1$

	/**
	 * An error access message.
	 */
	protected static final String ACCES_ERROR_MESSAGE = "Access to path is impossible";

	/**
	 * An error folder not exist message.
	 */
	protected static final String FOLDER_NOT_EXIST_MESSAGE = "Folder does not exist";

	/**
	 * The id for the persistent folder setting for this dialog.
	 */
	private static final String DIALOG_SETTINGS_FOLDER = "ExportComparisonAsImageDialog.folder"; //$NON-NLS-1$

	/**
	 * The directory dialog message.
	 */
	private static final String DIRECTORY_DIALOG_MESSAGE = "Select the destination folder";

	/**
	 * The directory dialog text.
	 */
	private static final String DIRECTORY_DIALOG_TEXT = DIRECTORY_DIALOG_MESSAGE;

	/**
	 * Popup file field label.
	 */
	private static final String TO_DIRECTORY = "To directory";

	/**
	 * The dialog window title.
	 */
	private static final String DIALOG_TITLE = "Export comparison as image file";

	/**
	 * Combo length history path.
	 */
	private static final int COMBO_HISTORY_LENGTH = 5;

	/**
	 * Height of the browse button.
	 */
	private static final int HEIGHT_BROWSE_BUTTON = 14;

	/**
	 * Width of the folder combo field.
	 */
	private static final int WIDTH_FOLDER_TEXT = 350;

	/**
	 * Width of the message field.
	 */
	private static final int WIDTH_MESSAGE = 300;

	/**
	 * The folder combo field.
	 */
	protected Combo folderText;

	/**
	 * The extension image combo field.
	 */
	protected Combo imageFormatCombo;

	/**
	 * The image format selected in the image format pulldown field.
	 */
	protected ImageFileFormat imageFormat;

	/**
	 * The text entered into the folder text field.
	 */
	protected String folder;

	/**
	 * The message image field, displays the error (X) icon when the file name or folder is invalid.
	 */
	private Label messageImageLabel;

	/**
	 * The message field, displays an error message when the file name or folder is invalid.
	 */
	private Label messageLabel;

	/**
	 * Creates an instance of the copy to image dialog.
	 * 
	 * @param parentShell
	 *            the parent shell
	 */
	public ExportComparisonDialog(Shell parentShell) {
		super(parentShell);
		initDialogSettings();
	}

	/**
	 * Resolve the selected image format to an enumerated type.
	 * 
	 * @param ordinal
	 *            the selected format in the pulldown
	 * @return the image format enumerated type
	 */
	public static ImageFileFormat resolveImageFormat(final int ordinal) {
		return SAFE_VALUES[ordinal];
	}

	/**
	 * Resolve the selected image format to an enumerated type.
	 * 
	 * @param imageFormat
	 *            the selected format.
	 * @return the image format enumerated type
	 */
	public static ImageFileFormat resolveImageFormat(final String imageFormat) {
		for (ImageFileFormat element : SAFE_VALUES) {
			if (element.getName().toLowerCase().equals(imageFormat.toLowerCase())) {
				return element;
			}
		}
		return getDefaultImageFormat();
	}

	/**
	 * Retrieves the default image format.
	 * 
	 * @return the default image format.
	 */
	public static ImageFileFormat getDefaultImageFormat() {
		return ImageFileFormat.JPG;
	}

	/**
	 * Returns height and width data in a GridData for the button that was passed in. You can call
	 * button.setLayoutData with the returned data.
	 * 
	 * @param button
	 *            which has already been made. We'll be making the GridData for this button, so be sure that
	 *            the text has already been set.
	 * @return GridData for this button with the suggested height and width
	 */
	public static GridData makeButtonData(Button button) {
		GC gc = new GC(button);
		gc.setFont(button.getFont());

		GridData data = new GridData();
		data.heightHint = Dialog.convertVerticalDLUsToPixels(gc.getFontMetrics(), HEIGHT_BROWSE_BUTTON);
		data.widthHint = Math.max(
				Dialog.convertHorizontalDLUsToPixels(gc.getFontMetrics(), IDialogConstants.BUTTON_WIDTH),
				button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);

		gc.dispose();

		return data;
	}

	/**
	 * Returns the output path.
	 * 
	 * @return folder
	 */
	public IPath getOutputPath() {
		return new Path(folder);
	}

	/**
	 * Returns the destination image file format selected by the user.
	 * 
	 * @return the selected image file format.
	 */
	public ImageFileFormat getImageFormat() {
		return imageFormat;
	}

	/**
	 * Retrieves the persistent settings for this dialog.
	 * 
	 * @return the persistent settings for this dialog.
	 */
	protected IDialogSettings getDialogSettings() {
		IDialogSettings settings = CompareDiagramIDEUISiriusPlugin.getDefault().getDialogSettings();
		settings = settings.getSection(DIALOG_SETTINGS_ID);
		if (settings == null) {
			settings = CompareDiagramIDEUISiriusPlugin.getDefault().getDialogSettings()
					.addNewSection(DIALOG_SETTINGS_ID);
		}
		return settings;
	}

	/**
	 * Initialize the settings for this dialog.
	 */
	protected void initDialogSettings() {
		final IDialogSettings dialogSettings = getDialogSettings();
		folder = "/"; //$NON-NLS-1$
		String workspace = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
		if (workspace != null && !workspace.equals(EMPTY_STRING)) {
			folder = workspace;
		}

		final String persistentFolder = dialogSettings.get(DIALOG_SETTINGS_FOLDER);
		if (persistentFolder != null) {
			folder = persistentFolder;
		}

		final String persistentImageFormat = dialogSettings.get(DIALOG_SETTINGS_IMAGE_FORMAT);
		if (persistentImageFormat == null) {
			imageFormat = getDefaultImageFormat();
		} else {
			imageFormat = resolveImageFormat(persistentImageFormat);
		}
	}

	/**
	 * Retrieves the persistent settings for this dialog.
	 */
	protected void saveDialogSettings() {
		final IDialogSettings dialogSettings = getDialogSettings();
		if (dialogSettings != null) {
			String[] directoryNames = dialogSettings.getArray(DIALOG_SETTINGS_FOLDER);
			if (directoryNames == null || directoryNames.length == 0) {
				directoryNames = new String[1];
				directoryNames[0] = folder;
			}
			directoryNames = addToHistory(directoryNames, folder);
			dialogSettings.put(DIALOG_SETTINGS_FOLDER, directoryNames);
			dialogSettings.put(DIALOG_SETTINGS_IMAGE_FORMAT, imageFormat.getName());
		}
	}

	@Override
	protected void okPressed() {
		imageFormat = resolveImageFormat(imageFormatCombo.getSelectionIndex());
		super.okPressed();
		saveDialogSettings();
	}

	/**
	 * Handle a browse button pressed selection.
	 */
	protected void handleBrowseButtonPressed() {
		final DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell());
		dialog.setMessage(DIRECTORY_DIALOG_MESSAGE);
		dialog.setText(DIRECTORY_DIALOG_TEXT);

		final String dirName = folderText.getText();
		if (!dirName.equals(EMPTY_STRING)) {
			final File path = new File(dirName);
			if (path.exists()) {
				dialog.setFilterPath(new Path(dirName).toOSString());
			}
		}

		final String selectedDirectory = dialog.open();
		if (selectedDirectory != null) {
			folderText.setText(selectedDirectory);
		}
	}

	/**
	 * Validate the folder text field.
	 */
	protected void validateFolderText() {

		if (folderText.getText().equals(EMPTY_STRING)) {

			setDialogErrorState("Folder cannot be blank");

		} else {

			IPath path = new Path(EMPTY_STRING);
			if (!path.isValidPath(folderText.getText())) {
				setDialogErrorState("Folder is not a valid path");
				return;
			}

			final File file = new File(folderText.getText());
			final File directory = file.getParentFile();

			if (file.exists()) {
				if (!file.canWrite()) {
					setDialogErrorState(ACCES_ERROR_MESSAGE);
					return;
				}
			} else if (directory != null && !directory.canWrite()) {
				setDialogErrorState(ACCES_ERROR_MESSAGE);
				return;
			}
			folder = folderText.getText();
			setDialogOKState();
		}
	}

	/**
	 * Set the dialog into error state mode. The error image (x) label and error label are made visible and
	 * the ok button is disabled.
	 * 
	 * @param message
	 *            the error message
	 */
	protected void setDialogErrorState(final String message) {
		messageLabel.setText(message);
		messageImageLabel.setVisible(true);
		messageLabel.setVisible(true);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		getButton(IDialogConstants.CANCEL_ID).getShell()
				.setDefaultButton(getButton(IDialogConstants.CANCEL_ID));
	}

	/**
	 * Get the supported image formats from the enumerated type.
	 * 
	 * @return array of supported image formats.
	 */
	protected String[] getImageSafeFormatItems() {
		final String[] items = new String[SAFE_VALUES.length];
		for (int i = 0; i < SAFE_VALUES.length; i++) {
			items[i] = SAFE_VALUES[i].getName();
		}
		return items;
	}

	@Override
	protected Control createContents(Composite parent) {
		Control result = super.createContents(parent);
		validateFolderText();
		return result;
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite composite = (Composite)super.createDialogArea(parent);
		createFolderGroup(composite);
		createImageFormatGroup(composite);
		createMessageGroup(composite);
		initListeners();
		return composite;
	}

	/**
	 * Create the folder group in the dialog.
	 * 
	 * @param parent
	 *            the parent widget
	 */
	protected void createFolderGroup(final Composite parent) {
		final Composite composite = SWTUtil.createCompositeHorizontalFill(parent, 3, false);
		SWTUtil.createLabel(composite, TO_DIRECTORY);

		folderText = new Combo(composite, SWT.SINGLE | SWT.BORDER);
		restoreWidgetValues();
		folderText.setText(folder);
		final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = WIDTH_FOLDER_TEXT;
		folderText.setLayoutData(gridData);

		final Button button = new Button(composite, SWT.PUSH);
		button.setText(BROWSE_LABEL);
		button.setLayoutData(makeButtonData(button));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				handleBrowseButtonPressed();
			}
		});
	}

	/**
	 * Create the image format group in the dialog.
	 * 
	 * @param parent
	 *            the parent widget
	 */
	protected void createImageFormatGroup(final Composite parent) {
		final Composite composite = SWTUtil.createCompositeHorizontalFill(parent, 2, false);
		SWTUtil.createLabel(composite, IMAGE_FORMAT_LABEL);

		String[] imageSafeFormatItems = getImageSafeFormatItems();

		Control imageFormatControl;
		if (imageSafeFormatItems != null && imageSafeFormatItems.length > 0) {

			if (imageSafeFormatItems.length == 1) {
				imageFormatControl = SWTUtil.createLabel(composite, imageSafeFormatItems[0]);

			} else {
				imageFormatCombo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
				imageFormatCombo.setItems(imageSafeFormatItems);
				imageFormatCombo.setText(imageFormat.getName());
				imageFormatControl = imageFormatCombo;
			}
		} else {
			imageFormatControl = SWTUtil.createLabel(composite, DEFAULT_VALUE.getName());
		}
		final GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, true);
		imageFormatControl.setLayoutData(gridData);

	}

	/**
	 * {@inheritDoc} Configures the shell in preparation for opening this window in it.
	 *
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(final Shell shell) {
		super.configureShell(shell);
		shell.setText(DIALOG_TITLE);
	}

	/**
	 * Hook method for restoring widget values to the values that they held last time this wizard was used to
	 * completion.
	 */
	protected void restoreWidgetValues() {
		IDialogSettings settings = getDialogSettings();
		if (settings != null) {
			String[] directoryNames = settings.getArray(DIALOG_SETTINGS_FOLDER);
			if (directoryNames == null || directoryNames.length == 0) {
				return;
			}
			folder = directoryNames[0];
			for (String directoryName : directoryNames) {
				folderText.add(directoryName);
			}
		}
	}

	/**
	 * Create the message group in the dialog used to display error messages.
	 * 
	 * @param parent
	 *            the parent widget
	 */
	private void createMessageGroup(final Composite parent) {
		final Composite composite = SWTUtil.createCompositeHorizontalFill(parent, 2, false);

		messageImageLabel = new Label(composite, SWT.NONE);
		messageImageLabel.setImage(JFaceResources.getImage(DLG_IMG_MESSAGE_ERROR));
		messageImageLabel.setVisible(false);

		messageLabel = new Label(composite, SWT.NONE);
		final GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_HORIZONTAL);
		gridData.widthHint = WIDTH_MESSAGE;
		messageLabel.setLayoutData(gridData);
		messageLabel.setVisible(false);
	}

	/**
	 * Set the dialog into ok state mode. The error image (x) label and error label and made not visible and
	 * the ok button is enabled.
	 */
	private void setDialogOKState() {
		messageImageLabel.setVisible(false);
		messageLabel.setVisible(false);
		getButton(IDialogConstants.OK_ID).setEnabled(true);
		getButton(IDialogConstants.OK_ID).getShell().setDefaultButton(getButton(IDialogConstants.OK_ID));
	}

	/**
	 * Adds an entry to a history, while taking care of duplicate history items and excessively long
	 * histories. The assumption is made that all histories should be of length
	 * <code>WizardDataTransferPage.COMBO_HISTORY_LENGTH</code>.
	 * 
	 * @param history
	 *            the current history
	 * @param newEntry
	 *            the entry to add to the history
	 * @return String[] tab of history
	 */
	protected String[] addToHistory(String[] history, String newEntry) {
		java.util.ArrayList<String> l = new java.util.ArrayList<String>(Arrays.asList(history));
		addToHistory(l, newEntry);
		String[] r = new String[l.size()];
		l.toArray(r);
		return r;
	}

	/**
	 * Adds an entry to a history, while taking care of duplicate history items and excessively long
	 * histories. The assumption is made that all histories should be of length
	 * <code>WizardDataTransferPage.COMBO_HISTORY_LENGTH</code>.
	 * 
	 * @param history
	 *            the current history
	 * @param newEntry
	 *            the entry to add to the history
	 */
	protected void addToHistory(List<String> history, String newEntry) {
		history.remove(newEntry);
		history.add(0, newEntry);
		if (history.size() > COMBO_HISTORY_LENGTH) {
			history.remove(COMBO_HISTORY_LENGTH);
		}
	}

	/**
	 * Initialize the field listeners when all field are created.
	 */
	protected void initListeners() {
		folderText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				validateFolderText();
			}
		});
		imageFormatCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				imageFormat = resolveImageFormat(imageFormatCombo.getSelectionIndex());
			}
		});
	}
}

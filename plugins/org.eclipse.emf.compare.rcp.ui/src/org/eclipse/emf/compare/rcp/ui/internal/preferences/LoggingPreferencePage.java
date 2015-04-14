/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.preferences;

import static org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin.LOGGER;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Level;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference page used to configure logging in EMFCompare.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class LoggingPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	public static final String EMFC_APPENDER_NAME = "EMFCFile"; //$NON-NLS-1$

	private Combo levelCombo;

	private Text fileField;

	private Text maxSizeField;

	private Text maxBackupField;

	private final String[] LOG_LEVELS = new String[] {"OFF", "ERROR", "INFO", "DEBUG" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 

	public static final String LOG_FILENAME_KEY = "org.eclipse.emf.compare.log.file.name"; //$NON-NLS-1$

	public static final String LOG_LEVEL_KEY = "org.eclipse.emf.compare.log.level"; //$NON-NLS-1$

	public static final String LOG_BACKUP_COUNT_KEY = "org.eclipse.emf.compare.log.backup.count"; //$NON-NLS-1$

	public static final String LOG_FILE_MAX_SIZE_KEY = "org.eclipse.emf.compare.log.file.max.size"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */
	public LoggingPreferencePage() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param title
	 *            .
	 * @param image
	 *            .
	 */
	public LoggingPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	/**
	 * Constructor.
	 * 
	 * @param title
	 *            .
	 */
	public LoggingPreferencePage(String title) {
		super(title);
	}

	/**
	 * {@inheritDoc}
	 */
	public void init(IWorkbench workbench) {
		// Nothing to do
	}

	@Override
	protected IPreferenceStore doGetPreferenceStore() {
		return EMFCompareRCPUIPlugin.getDefault().getPreferenceStore();
	}

	@Override
	protected Control createContents(Composite parent) {
		Group group = new Group(parent, SWT.NULL);
		group.setLayout(new GridLayout(3, false));
		group.setText(EMFCompareRCPUIMessages.getString("LoggingPreferencePage.preferencePage.description")); //$NON-NLS-1$

		new Label(group, SWT.LEAD).setText(EMFCompareRCPUIMessages
				.getString("LoggingPreferencePage.log.level")); //$NON-NLS-1$
		levelCombo = new Combo(group, SWT.DROP_DOWN);
		levelCombo.setItems(LOG_LEVELS);
		levelCombo.setLayoutData(getDefaultFieldGridData(40));

		Label fileLabel = new Label(group, SWT.LEAD);
		fileLabel.setText(EMFCompareRCPUIMessages.getString("LoggingPreferencePage.log.file")); //$NON-NLS-1$
		fileField = new Text(group, SWT.BORDER | SWT.SINGLE);
		fileField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		Button fileButton = new Button(group, SWT.PUSH);
		fileButton.setText(EMFCompareRCPUIMessages.getString("LoggingPreferencePage.filebutton.label")); //$NON-NLS-1$
		fileButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog(getShell(), SWT.SAVE);
				File file = new File(fileField.getText());
				dlg.setFileName(file.getName());
				dlg.setFilterPath(file.getParent());
				String fileName = dlg.open();
				if (fileName != null) {
					fileField.setText(fileName);
				}
			}
		});

		Label maxSizeLabel = new Label(group, SWT.LEAD);
		maxSizeLabel.setText(EMFCompareRCPUIMessages.getString("LoggingPreferencePage.log.file.size")); //$NON-NLS-1$
		maxSizeField = new Text(group, SWT.BORDER | SWT.SINGLE);
		maxSizeField.setLayoutData(getDefaultFieldGridData(80));
		maxSizeField.addVerifyListener(new VerifyIntegerListener());

		Label maxBackupLabel = new Label(group, SWT.LEAD);
		maxBackupLabel.setText(EMFCompareRCPUIMessages.getString("LoggingPreferencePage.log.backup.count")); //$NON-NLS-1$
		maxBackupField = new Text(group, SWT.BORDER | SWT.SINGLE);
		maxBackupField.setLayoutData(getDefaultFieldGridData(80));
		maxBackupField.addVerifyListener(new VerifyIntegerListener());

		refreshWidgets();

		return group;
	}

	protected GridData getDefaultFieldGridData(int width) {
		GridData gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1);
		gd.widthHint = width;
		return gd;
	}

	protected void savePreferences() {
		getPreferenceStore().setValue(LOG_FILENAME_KEY, fileField.getText());
		getPreferenceStore().setValue(LOG_LEVEL_KEY, levelCombo.getText());
		getPreferenceStore().setValue(LOG_BACKUP_COUNT_KEY, maxBackupField.getText());
		getPreferenceStore().setValue(LOG_FILE_MAX_SIZE_KEY, maxSizeField.getText());
	}

	protected void resetPreferences() {
		getPreferenceStore().setToDefault(LOG_FILENAME_KEY);
		getPreferenceStore().setToDefault(LOG_LEVEL_KEY);
		getPreferenceStore().setToDefault(LOG_BACKUP_COUNT_KEY);
		getPreferenceStore().setToDefault(LOG_FILE_MAX_SIZE_KEY);
	}

	protected void refreshWidgets() {
		String fileName = getPreferenceStore().getString(LOG_FILENAME_KEY);
		String level = getPreferenceStore().getString(LOG_LEVEL_KEY);
		int maxBackupCount = getPreferenceStore().getInt(LOG_BACKUP_COUNT_KEY);
		int maxSizeInMB = getPreferenceStore().getInt(LOG_FILE_MAX_SIZE_KEY);
		levelCombo.select(Arrays.asList(LOG_LEVELS).indexOf(level));
		levelCombo.pack();
		fileField.setText(fileName);
		maxBackupField.setText(Integer.toString(maxBackupCount));
		maxSizeField.setText(Integer.toString(maxSizeInMB));
	}

	@Override
	public boolean performOk() {
		String item = levelCombo.getItem(levelCombo.getSelectionIndex());
		LOGGER.setLevel(Level.toLevel(item));
		savePreferences();
		refreshWidgets();
		return super.performOk();
	}

	@Override
	protected void performDefaults() {
		resetPreferences();
		refreshWidgets();
		super.performDefaults();
	}

	/**
	 * Only allows to enter valid digits in numeric fields.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static class VerifyIntegerListener implements VerifyListener {
		public void verifyText(VerifyEvent evt) {
			try {
				Integer.parseInt(evt.text);
			} catch (NumberFormatException e) {
				evt.doit = false;
			}
		}
	}
}

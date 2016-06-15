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

import static org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences.LOG_BACKUP_COUNT_KEY;
import static org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences.LOG_BACKUP_DEFAULT;
import static org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences.LOG_FILENAME_KEY;
import static org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences.LOG_FILE_DEFAULT;
import static org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences.LOG_FILE_MAX_SIZE_KEY;
import static org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences.LOG_FILE_SIZE_DEFAULT;
import static org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences.LOG_LEVEL_DEFAULT;
import static org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences.LOG_LEVEL_KEY;

import java.io.File;
import java.util.Arrays;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
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
import org.osgi.service.prefs.BackingStoreException;

/**
 * Preference page used to configure logging in EMFCompare.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class LoggingPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private Combo levelCombo;

	private Text fileField;

	private Text maxSizeField;

	private Text maxBackupField;

	private final String[] LOG_LEVELS = new String[] {"OFF", "ERROR", "INFO", "DEBUG" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

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
	protected Control createContents(Composite parent) {
		Group group = new Group(parent, SWT.NULL);
		group.setLayout(new GridLayout(3, false));
		group.setText(EMFCompareRCPUIMessages.getString("LoggingPreferencePage.preferencePage.description")); //$NON-NLS-1$

		new Label(group, SWT.LEAD)
				.setText(EMFCompareRCPUIMessages.getString("LoggingPreferencePage.log.level")); //$NON-NLS-1$
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

	protected void savePreferences() throws BackingStoreException {
		IEclipsePreferences prefs = EMFCompareRCPPlugin.getDefault().getEMFComparePreferences();
		prefs.put(LOG_FILENAME_KEY, fileField.getText());
		String item = levelCombo.getItem(levelCombo.getSelectionIndex());
		prefs.put(LOG_LEVEL_KEY, item);
		prefs.put(LOG_BACKUP_COUNT_KEY, maxBackupField.getText());
		prefs.put(LOG_FILE_MAX_SIZE_KEY, maxSizeField.getText());
		prefs.flush();
	}

	protected void resetPreferences() {
		IEclipsePreferences prefs = EMFCompareRCPPlugin.getDefault().getEMFComparePreferences();
		prefs.put(LOG_FILENAME_KEY, ""); //$NON-NLS-1$
		prefs.put(LOG_LEVEL_KEY, "OFF"); //$NON-NLS-1$
		prefs.putInt(LOG_BACKUP_COUNT_KEY, LOG_BACKUP_DEFAULT);
		prefs.putInt(LOG_FILE_MAX_SIZE_KEY, LOG_FILE_SIZE_DEFAULT);
	}

	protected void refreshWidgets() {
		IEclipsePreferences prefs = EMFCompareRCPPlugin.getDefault().getEMFComparePreferences();
		String fileName = prefs.get(LOG_FILENAME_KEY, LOG_FILE_DEFAULT);
		String level = prefs.get(LOG_LEVEL_KEY, LOG_LEVEL_DEFAULT);
		int maxBackupCount = prefs.getInt(LOG_BACKUP_COUNT_KEY, LOG_BACKUP_DEFAULT);
		int maxSizeInMB = prefs.getInt(LOG_FILE_MAX_SIZE_KEY, LOG_FILE_SIZE_DEFAULT);
		levelCombo.select(Arrays.asList(LOG_LEVELS).indexOf(level));
		levelCombo.pack();
		fileField.setText(fileName);
		maxBackupField.setText(Integer.toString(maxBackupCount));
		maxSizeField.setText(Integer.toString(maxSizeInMB));
	}

	@Override
	public boolean performOk() {
		try {
			savePreferences();
			refreshWidgets();
			return super.performOk();
		} catch (BackingStoreException e) {
			return false;
		}
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

/*******************************************************************************
 * Copyright (c) 2006, Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.preferences;

import org.eclipse.emf.compare.ui.EMFCompareUIPlugin;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference page used for <b>EMFCompare</b>, it allows the user to define 
 * which files to compare with <b>EMFCompare</b> and the colors to use for 
 * the differences' highlighting.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class EMFComparePreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	/**
	 * Adds our page to the default preferences dialog.
	 */
	public EMFComparePreferencesPage() {
		super(GRID);
		setPreferenceStore(EMFCompareUIPlugin.getDefault().getPreferenceStore());
		setDescription("EMFCompare preferences"); //$NON-NLS-1$
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see FieldEditorPreferencePage#createFieldEditors()
	 */
	public void createFieldEditors() {
		final Group colorGroup = new Group(getFieldEditorParent(), SWT.SHADOW_ETCHED_IN);
		colorGroup.setText("colors"); //$NON-NLS-1$
		addField(new ColorFieldEditor(
				EMFCompareConstants.PREFERENCES_KEY_HIGHLIGHT_COLOR,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_HIGHLIGHT_COLOR,
				colorGroup));
		addField(new ColorFieldEditor(
				EMFCompareConstants.PREFERENCES_KEY_CHANGED_COLOR,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_CHANGED_COLOR,
				colorGroup));
		addField(new ColorFieldEditor(
				EMFCompareConstants.PREFERENCES_KEY_ADDED_COLOR,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_ADDED_COLOR,
				colorGroup));
		addField(new ColorFieldEditor(
				EMFCompareConstants.PREFERENCES_KEY_REMOVED_COLOR,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_REMOVED_COLOR,
				colorGroup));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IWorkbenchPreferencePage#init(IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
}
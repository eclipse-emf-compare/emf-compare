/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.preferences;

import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference page for EMFCompare merge.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class MergePreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private BooleanFieldEditor preMergeOnConflict;

	public void init(IWorkbench workbench) {
		setPreferenceStore(EMFCompareIDEUIPlugin.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		preMergeOnConflict = new BooleanFieldEditor(EMFCompareUIPreferences.PRE_MERGE_MODELS_WHEN_CONFLICT,
				EMFCompareIDEUIMessages.getString("MergePreferencesPage.preMergeOnConflict"), //$NON-NLS-1$
				getFieldEditorParent());
		addField(preMergeOnConflict);
	}

}

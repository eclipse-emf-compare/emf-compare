/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.preferences;

import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.CompareColorImpl;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.PreferenceLinkArea;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

/**
 * Preference page for Editor preferences
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class EditorPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	public EditorPreferencePage() {
	}

	public EditorPreferencePage(String title) {
		super(title);
	}

	public EditorPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	public void init(IWorkbench workbench) {

	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).applyTo(container);

		// Link to color tab
		PreferenceLinkArea fileEditorsArea = new PreferenceLinkArea(container, SWT.NONE,
				"org.eclipse.ui.preferencePages.ColorsAndFonts", //$NON-NLS-1$
				EMFCompareRCPUIMessages.getString("EditorPreferencePage.mainTab.colorHyperLink.label"), //$NON-NLS-1$
				(IWorkbenchPreferenceContainer)getContainer(), "selectColor:" //$NON-NLS-1$
						+ CompareColorImpl.CONFLICTING_CHANGE_COLOR_THEME_KEY);

		GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		fileEditorsArea.getControl().setLayoutData(data);

		return container;
	}

}

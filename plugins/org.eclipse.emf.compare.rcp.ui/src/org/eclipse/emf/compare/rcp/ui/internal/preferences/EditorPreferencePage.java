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
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
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
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);

		createContentInMainTab(tabFolder);

		createContentInFiltersTab(tabFolder);

		createContentInGroupsTab(tabFolder);

		return container;
	}

	private void createContentInGroupsTab(TabFolder tabFolder) {
		TabItem tbtmGroups = new TabItem(tabFolder, SWT.NONE);
		tbtmGroups.setText(EMFCompareRCPUIMessages.getString("EditorPreferencePage.groupsTab.label")); //$NON-NLS-1$
	}

	private void createContentInFiltersTab(TabFolder tabFolder) {
		TabItem tbtmFilters = new TabItem(tabFolder, SWT.NONE);
		tbtmFilters.setText(EMFCompareRCPUIMessages.getString("EditorPreferencePage.filtersTab.label")); //$NON-NLS-1$
	}

	private void createContentInMainTab(TabFolder tabFolder) {
		TabItem tbtmMain = new TabItem(tabFolder, SWT.NONE);
		tbtmMain.setText(EMFCompareRCPUIMessages.getString("EditorPreferencePage.mainTab.label")); //$NON-NLS-1$

		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		tbtmMain.setControl(composite);
		// Link to color tab
		PreferenceLinkArea fileEditorsArea = new PreferenceLinkArea(
				composite,
				SWT.NONE,
				"org.eclipse.ui.preferencePages.ColorsAndFonts", EMFCompareRCPUIMessages.getString("EditorPreferencePage.mainTab.colorHyperLink.label"),//$NON-NLS-1$ //$NON-NLS-2$
				(IWorkbenchPreferenceContainer)getContainer(), "selectColor:" //$NON-NLS-1$
						+ CompareColorImpl.CONFLICTING_CHANGE_COLOR_THEME_KEY);

		GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		fileEditorsArea.getControl().setLayoutData(data);
	}

}

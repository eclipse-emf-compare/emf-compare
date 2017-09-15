/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *      Philip Langer - bug 522372
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.preferences;

import static org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages.getString;
import static org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences.EDITOR_TREE_AUTO_EXPAND_LEVEL;
import static org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences.EDITOR_TREE_AUTO_SELECT_FIRST_CHANGE;
import static org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences.EDITOR_TREE_EXPAND_TIMEOUT;
import static org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences.EDITOR_TREE_HIGHLIGHT_RELATED_CHANGES;

import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.CompareColorImpl;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.PreferenceLinkArea;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

/**
 * Preference page for UI settings related to the comparison editor.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
@SuppressWarnings("restriction")
public class EditorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/** Preference page ID. */
	public static final String PAGE_ID = "org.eclipse.emf.compare.ide.ui.preferencepage.editor"; //$NON-NLS-1$

	/** Tab folder for all tabs in this page. */
	protected TabFolder tabFolder;

	/** Color tab. */
	protected Composite colorTab;

	/** Tree tab. */
	protected Composite treeTab;

	/** Link to color preference page. */
	protected PreferenceLinkArea colorsAndFontsLink;

	/** Editor for preference {@link EMFCompareUIPreferences#EDITOR_TREE_AUTO_SELECT_FIRST_CHANGE}. */
	protected BooleanFieldEditor treeAutoSelectFirstChange;

	/** Editor for preference {@link EMFCompareUIPreferences#EDITOR_TREE_AUTO_EXPAND_LEVEL}. */
	protected IntegerFieldEditor treeAutoExpandLevel;

	/** Editor for preference {@link EMFCompareUIPreferences#EDITOR_TREE_EXPAND_TIMEOUT}. */
	protected IntegerFieldEditor treeExpandTimeout;

	/** Editor for preference {@link EMFCompareUIPreferences#EDITOR_TREE_HIGHLIGHT_RELATED_CHANGES}. */
	protected BooleanFieldEditor treeHighlightRelatedChanges;

	/** Editor for preference {@link EMFCompareUIPreferences#SELECT_NEXT_UNRESOLVED_DIFF}. */
	private BooleanFieldEditor selectNextUnresolvedDiff;

	public EditorPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(EMFCompareIDEUIPlugin.getDefault().getPreferenceStore());
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		tabFolder = new TabFolder(container, SWT.NONE);
		createFieldEditors();
		initialize();
		checkState();
		return container;
	}

	/**
	 * Create a skeleton of a tab.
	 * 
	 * @param tabLabel
	 *            tab label
	 * @return main composite of the tab
	 */
	protected Composite createTabSkeleton(String tabLabel) {
		return createTabSkeleton(tabLabel, null);
	}

	/**
	 * Create a skeleton of a tab.
	 * 
	 * @param tabLabel
	 *            tab label
	 * @param introText
	 *            Text use as description a tab (may be null)
	 * @return main composite of the tab
	 */
	protected Composite createTabSkeleton(String tabLabel, String introText) {
		TabItem tbtmMain = new TabItem(tabFolder, SWT.NONE);
		tbtmMain.setText(tabLabel);
		Composite tabComposite = new Composite(tabFolder, SWT.NONE);
		GridDataFactory.fillDefaults().applyTo(tabComposite);
		if (introText != null) {
			Label descriptionText = new Label(tabComposite, SWT.WRAP);
			GridDataFactory.fillDefaults().span(2, 0).applyTo(descriptionText);
			descriptionText.setText(introText);
		}
		tbtmMain.setControl(tabComposite);
		return tabComposite;
	}

	/**
	 * Returns the lazily created color tab.
	 * 
	 * @return color tab
	 */
	protected Composite getColorTab() {
		if (colorTab == null) {
			colorTab = createTabSkeleton(getString("EditorPreferencesPage.colorTab.label")); //$NON-NLS-1$
		}
		return colorTab;
	}

	/**
	 * Returns the lazily created tree tab.
	 * 
	 * @return color tab
	 */
	protected Composite getTreeTab() {
		if (treeTab == null) {
			treeTab = createTabSkeleton(getString("EditorPreferencesPage.treeTab.label")); //$NON-NLS-1$
		}
		return treeTab;
	}

	@Override
	protected void createFieldEditors() {
		createColorTabContent();
		createTreeTabContent();
	}

	/**
	 * Creates and adds the content of the color tab.
	 */
	protected void createColorTabContent() {
		createColorsAndFontsLink(getColorTab());
		GridLayoutFactory.swtDefaults().applyTo(getColorTab());
	}

	/**
	 * Creates and adds the content of the tree tab.
	 */
	protected void createTreeTabContent() {
		createAutoExpandTreeLevel(getTreeTab());
		createExpandTreeTimeout(getTreeTab());
		createAutoSelectFirstChange(getTreeTab());
		createHighlightRelatedChanges(getTreeTab());
		createSelectNextUnresolvedDiff(getTreeTab());

		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(getTreeTab());
		treeAutoSelectFirstChange.fillIntoGrid(getTreeTab(), 2);
		treeExpandTimeout.fillIntoGrid(getTreeTab(), 2);
		treeHighlightRelatedChanges.fillIntoGrid(getTreeTab(), 2);
		selectNextUnresolvedDiff.fillIntoGrid(getTreeTab(), 2);
	}

	/**
	 * Creates a link to the preference page setting the colors and fonts for the Compare Editor.
	 * 
	 * @param parent
	 *            link parent
	 * @return link
	 */
	protected PreferenceLinkArea createColorsAndFontsLink(Composite parent) {
		colorsAndFontsLink = new PreferenceLinkArea(parent, SWT.NONE,
				"org.eclipse.ui.preferencePages.ColorsAndFonts", //$NON-NLS-1$
				getString("EditorPreferencesPage.colorTab.preferenceHyperLink"), //$NON-NLS-1$
				(IWorkbenchPreferenceContainer)getContainer(), "selectColor:" //$NON-NLS-1$
						+ CompareColorImpl.CONFLICTING_CHANGE_COLOR_THEME_KEY);
		return colorsAndFontsLink;
	}

	/**
	 * Creates an editor for the {@link EMFCompareUIPreferences#EDITOR_TREE_AUTO_EXPAND_LEVEL} preference.
	 * 
	 * @param parent
	 *            editor parent
	 * @return editor
	 */
	protected IntegerFieldEditor createAutoExpandTreeLevel(Composite parent) {
		treeAutoExpandLevel = new IntegerFieldEditor(EDITOR_TREE_AUTO_EXPAND_LEVEL,
				getString("EditorPreferencesPage.treeTab.autoExpandTreeLevel"), //$NON-NLS-1$
				parent, 3);
		treeAutoExpandLevel.setValidRange(-1, 999);
		addField(treeAutoExpandLevel);
		return treeAutoExpandLevel;
	}

	/**
	 * Creates an editor for the {@link EMFCompareUIPreferences#EDITOR_TREE_EXPAND_TIMEOUT} preference.
	 * 
	 * @param parent
	 *            editor parent
	 * @return editor
	 */
	protected IntegerFieldEditor createExpandTreeTimeout(Composite parent) {
		treeExpandTimeout = new IntegerFieldEditor(EDITOR_TREE_EXPAND_TIMEOUT,
				getString("EditorPreferencesPage.treeTab.expandTreeTimeout"), //$NON-NLS-1$
				parent, 3);
		treeExpandTimeout.setValidRange(1, 999);
		addField(treeExpandTimeout);
		return treeExpandTimeout;
	}

	/**
	 * Creates an editor for the {@link EMFCompareUIPreferences#EDITOR_TREE_AUTO_SELECT_FIRST_CHANGE}
	 * preference.
	 * 
	 * @param parent
	 *            editor parent
	 * @return editor
	 */
	protected BooleanFieldEditor createAutoSelectFirstChange(Composite parent) {
		treeAutoSelectFirstChange = new BooleanFieldEditor(EDITOR_TREE_AUTO_SELECT_FIRST_CHANGE,
				getString("EditorPreferencesPage.treeTab.autoSelectFirstChange"), //$NON-NLS-1$
				BooleanFieldEditor.DEFAULT, parent);
		addField(treeAutoSelectFirstChange);
		return treeAutoSelectFirstChange;
	}

	/**
	 * Creates an editor for the {@link EMFCompareUIPreferences#EDITOR_TREE_HIGHLIGHT_RELATED_CHANGES}
	 * preference.
	 * 
	 * @param parent
	 *            editor parent
	 * @return editor
	 */
	protected BooleanFieldEditor createHighlightRelatedChanges(Composite parent) {
		treeHighlightRelatedChanges = new BooleanFieldEditor(EDITOR_TREE_HIGHLIGHT_RELATED_CHANGES,
				getString("EditorPreferencesPage.treeTab.highlight"), //$NON-NLS-1$
				BooleanFieldEditor.DEFAULT, parent);
		addField(treeHighlightRelatedChanges);
		return treeHighlightRelatedChanges;
	}

	/**
	 * Creates an editor for the {@link EMFCompareUIPreferences#EDITOR_TREE_HIGHLIGHT_RELATED_CHANGES}
	 * preference.
	 * 
	 * @param parent
	 *            editor parent
	 * @return editor
	 */
	protected BooleanFieldEditor createSelectNextUnresolvedDiff(Composite parent) {
		selectNextUnresolvedDiff = new BooleanFieldEditor(EMFCompareUIPreferences.SELECT_NEXT_UNRESOLVED_DIFF,
				EMFCompareIDEUIMessages.getString("MergePreferencesPage.selectNextUnresolvedDiff"), //$NON-NLS-1$
				BooleanFieldEditor.DEFAULT, parent);
		addField(selectNextUnresolvedDiff);
		return selectNextUnresolvedDiff;
	}
}

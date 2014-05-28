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
package org.eclipse.emf.compare.ide.ui.internal.preferences;

import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference page for the model resolution, allowing users to disable the resolution altogether or to
 * configure how EMF Compare will handle it.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ModelResolutionPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	private static final String[][] scopeNamesAndValues = new String[][] {
			{
					EMFCompareIDEUIMessages
							.getString("ModelResolutionPreferencesPage.resolutionScope.workspace.label"), //$NON-NLS-1$
					CrossReferenceResolutionScope.WORKSPACE.name(), },
			{
					EMFCompareIDEUIMessages
							.getString("ModelResolutionPreferencesPage.resolutionScope.project.label"), //$NON-NLS-1$
					CrossReferenceResolutionScope.PROJECT.name(), },
			{
					EMFCompareIDEUIMessages
							.getString("ModelResolutionPreferencesPage.resolutionScope.container.label"), //$NON-NLS-1$
					CrossReferenceResolutionScope.CONTAINER.name(), },
			{
					EMFCompareIDEUIMessages
							.getString("ModelResolutionPreferencesPage.resolutionScope.outgoing.label"), //$NON-NLS-1$
					CrossReferenceResolutionScope.OUTGOING.name(), }, };

	private BooleanFieldEditor disableResolvers;

	private BooleanFieldEditor useThreads;

	private ComboFieldEditor resolutionScope;

	private Label resolutionScopeMainDescription;

	private Label resolutionScopeDescription;

	private Composite resolutionScopeComboComposite;

	public ModelResolutionPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		setPreferenceStore(EMFCompareIDEUIPlugin.getDefault().getPreferenceStore());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createFieldEditors() {
		disableResolvers = new BooleanFieldEditor(EMFCompareUIPreferences.DISABLE_RESOLVERS_PREFERENCE,
				EMFCompareIDEUIMessages.getString("ModelResolutionPreferencesPage.disableResolvers"), //$NON-NLS-1$
				getFieldEditorParent());
		addField(disableResolvers);
		useThreads = new BooleanFieldEditor(EMFCompareUIPreferences.DISABLE_THREADING_PREFERENCE,
				EMFCompareIDEUIMessages.getString("ModelResolutionPreferencesPage.disableThreading"), //$NON-NLS-1$
				getFieldEditorParent());
		addField(useThreads);

		final Composite resolutionScopeComposite = new Composite(getFieldEditorParent(), SWT.BORDER);
		resolutionScopeComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		resolutionScopeComposite.setLayout(new GridLayout(2, false));

		resolutionScopeMainDescription = new Label(resolutionScopeComposite, SWT.WRAP);
		resolutionScopeMainDescription.setText(EMFCompareIDEUIMessages
				.getString("ModelResolutionPreferencesPage.resolutionScope.description")); //$NON-NLS-1$
		final GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.widthHint = 400;
		layoutData.horizontalSpan = 2;
		resolutionScopeMainDescription.setLayoutData(layoutData);

		// Use a composite for the combo editor field alone, to prevent it from grabbing all horizontal space.
		resolutionScopeComboComposite = new Composite(resolutionScopeComposite, SWT.NONE);
		resolutionScopeComboComposite.setLayout(new GridLayout(2, false));
		resolutionScope = new ComboFieldEditor(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE,
				EMFCompareIDEUIMessages.getString("ModelResolutionPreferencesPage.resolutionScope"), //$NON-NLS-1$
				scopeNamesAndValues, resolutionScopeComboComposite);
		addField(resolutionScope);
		resolutionScopeComboComposite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

		resolutionScopeDescription = new Label(resolutionScopeComposite, SWT.WRAP | SWT.BORDER);
		final GridData descriptionData = new GridData(SWT.FILL, SWT.FILL, true, false);
		descriptionData.widthHint = 200;
		resolutionScopeDescription.setLayoutData(descriptionData);

		updateFieldEnablement(getPreferenceStore().getBoolean(
				EMFCompareUIPreferences.DISABLE_RESOLVERS_PREFERENCE));
		updateScopeDescription(getPreferenceStore().getString(
				EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE));
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource() == disableResolvers) {
			updateFieldEnablement(disableResolvers.getBooleanValue());
		} else if (event.getSource() == resolutionScope) {
			updateScopeDescription((String)event.getNewValue());
		}
		super.propertyChange(event);
	}

	private void updateFieldEnablement(boolean disabled) {
		useThreads.setEnabled(!disabled, getFieldEditorParent());
		resolutionScopeMainDescription.setEnabled(!disabled);
		resolutionScope.setEnabled(!disabled, resolutionScopeComboComposite);
		resolutionScopeDescription.setEnabled(!disabled);
	}

	private void updateScopeDescription(String scopeValue) {
		final CrossReferenceResolutionScope scope;
		if (scopeValue != null && scopeValue.length() > 0) {
			scope = CrossReferenceResolutionScope.valueOf(scopeValue);
		} else {
			scope = CrossReferenceResolutionScope.WORKSPACE;
		}
		switch (scope) {
			case WORKSPACE:
				resolutionScopeDescription.setText(EMFCompareIDEUIMessages
						.getString("ModelResolutionPreferencesPage.resolutionScope.workspace.description")); //$NON-NLS-1$
				break;
			case PROJECT:
				resolutionScopeDescription.setText(EMFCompareIDEUIMessages
						.getString("ModelResolutionPreferencesPage.resolutionScope.project.description")); //$NON-NLS-1$
				break;
			case CONTAINER:
				resolutionScopeDescription.setText(EMFCompareIDEUIMessages
						.getString("ModelResolutionPreferencesPage.resolutionScope.container.description")); //$NON-NLS-1$
				break;
			case OUTGOING:
				resolutionScopeDescription.setText(EMFCompareIDEUIMessages
						.getString("ModelResolutionPreferencesPage.resolutionScope.outgoing.description")); //$NON-NLS-1$
				break;
			default:
				// Shouldn't happen
				resolutionScopeDescription.setText(EMFCompareIDEUIMessages
						.getString("ModelResolutionPreferencesPage.resolutionScope.invalid")); //$NON-NLS-1$
				break;
		}
		getFieldEditorParent().layout();
	}

	@Override
	protected void performDefaults() {
		super.performDefaults();
		updateFieldEnablement(getPreferenceStore().getBoolean(
				EMFCompareUIPreferences.DISABLE_RESOLVERS_PREFERENCE));
		updateScopeDescription(getPreferenceStore().getString(
				EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE));
	}

	public void init(IWorkbench workbench) {
		// Empty implementation
	}
}

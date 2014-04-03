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

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.ModelResolverDescriptor;
import org.eclipse.emf.compare.ide.ui.internal.logical.ModelResolverManager;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference page for "Model resolution" preference.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class ModelResolutionPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/** Label for default value of "forced resolver" preference. */
	private static final String DEFAULT_LABEL_VALUE = EMFCompareIDEUIMessages
			.getString("ModelResolutionPreferencePage.forcedresolver.default.value.label"); //$NON-NLS-1$

	/** Description for default value of "forced resolver" preference. */
	private static final String DEFAULT_DESCRIPTION_VALUE = EMFCompareIDEUIMessages
			.getString("ModelResolutionPreferencePage.forcedresolver.default.value.description"); //$NON-NLS-1$

	/** Index for default value of "forced resolver" preference. */
	private static final int DEFAULT_VALUE_INDEX = 0;

	/** {@link ModelResolverManager} */
	private final ModelResolverManager modelResolverManager = EMFCompareIDEUIPlugin.getDefault()
			.getModelResolverManager();

	/** User selected resolver. This should be null if the default strategy is chosen. */
	private ModelResolverDescriptor userSelectedResolver;

	/** Is set to true if the "Model resolution" should be enabled. */
	private boolean isEnabled;

	private Button enableModelResolutionButton;

	/**
	 * List of element that need to be enabled or disabled depending on state of
	 * {@link ModelResolutionPreferencePage#isEnabled}
	 */
	private ArrayList<Control> activableElements;

	/** Forced resolver combo chooser. */
	private Combo resolverCombo;

	/** Label displaying the description of {@link ModelResolutionPreferencePage#userSelectedResolver} */
	private Label descriptionLabel;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createContents(Composite parent) {
		activableElements = Lists.newArrayList();
		// Init data value
		isEnabled = modelResolverManager.isResolutionEnabled();
		userSelectedResolver = modelResolverManager.getUserSelectedResolver();

		Composite mainContainer = new Composite(parent, SWT.NONE);
		mainContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		mainContainer.setLayout(new GridLayout(1, false));

		createEnablementCheckBox(mainContainer);

		Composite resolutionStrategyComposite = createModelResolverComposite(mainContainer);

		createCombo(resolutionStrategyComposite);

		createDescription(resolutionStrategyComposite);

		updateDescription();
		updateWidgetEnablement();

		return mainContainer;
	}

	/**
	 * Create main composite for the preferences.
	 * 
	 * @param mainContainer
	 * @return {@link Composite}
	 */
	private Composite createModelResolverComposite(Composite mainContainer) {
		Composite resolutionStrategyComposite = new Composite(mainContainer, SWT.BORDER);
		GridLayout groupLayout = new GridLayout(2, false);
		resolutionStrategyComposite.setLayout(groupLayout);
		resolutionStrategyComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		Label introductionLabel = new Label(resolutionStrategyComposite, SWT.NONE);
		GridData introLabelData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
		introLabelData.horizontalSpan = 2;
		introductionLabel.setLayoutData(introLabelData);
		introductionLabel.setText(EMFCompareIDEUIMessages
				.getString("ModelResolutionPreferencePage.introduction.label")); //$NON-NLS-1$
		activableElements.add(introductionLabel);
		return resolutionStrategyComposite;
	}

	/**
	 * Enable/Disable all widget from activableElements depending on the state of isEnabled.
	 */
	private void updateWidgetEnablement() {
		enableModelResolutionButton.setSelection(isEnabled);
		for (Control controlToDisable : activableElements) {
			if (!controlToDisable.isDisposed()) {
				controlToDisable.setEnabled(isEnabled);
			}
		}
	}

	/**
	 * Update the description field using {@link ModelResolutionPreferencePage#userSelectedResolver} as input.
	 */
	private void updateDescription() {
		if (descriptionLabel != null && !descriptionLabel.isDisposed()) {
			if (userSelectedResolver != null) {
				descriptionLabel.setText(userSelectedResolver.getDescription());
			} else {
				descriptionLabel.setText(DEFAULT_DESCRIPTION_VALUE);
			}
		}
	}

	/**
	 * Create the description field.
	 * 
	 * @param resolutionStrategyComposite
	 */
	private void createDescription(Composite resolutionStrategyComposite) {
		Group descriptionGroup = new Group(resolutionStrategyComposite, SWT.NONE);
		descriptionGroup.setText(EMFCompareIDEUIMessages
				.getString("ModelResolutionPreferencePage.description.label")); //$NON-NLS-1$
		GridData groupLayoutData = new GridData(SWT.FILL, SWT.TOP, true, false);
		descriptionGroup.setLayoutData(groupLayoutData);
		GridLayout descriptionGroupLayout = new GridLayout(1, false);
		activableElements.add(descriptionGroup);

		descriptionGroup.setLayout(descriptionGroupLayout);
		descriptionLabel = new Label(descriptionGroup, SWT.WRAP);
		GridData descriptionLayoutData = new GridData(SWT.FILL, SWT.TOP, true, true);
		descriptionLayoutData.minimumHeight = 50;
		descriptionLayoutData.minimumWidth = 500;
		descriptionLabel.setLayoutData(descriptionLayoutData);
		activableElements.add(descriptionLabel);

	}

	/**
	 * Create the checkbox in charge of enabling/disabling the model resolution mechanism.
	 * 
	 * @param resolutionStrategyComposite
	 */
	private void createEnablementCheckBox(Composite resolutionStrategyComposite) {
		enableModelResolutionButton = new Button(resolutionStrategyComposite, SWT.CHECK);
		enableModelResolutionButton.setText(EMFCompareIDEUIMessages
				.getString("ModelResolutionPreferencePage.enable.resolution.checkbox")); //$NON-NLS-1$
		enableModelResolutionButton.setSelection(isEnabled);
		GridData checkBoxGridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		enableModelResolutionButton.setLayoutData(checkBoxGridData);
		enableModelResolutionButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				isEnabled = enableModelResolutionButton.getSelection();
				updateWidgetEnablement();
			}

		});
	}

	/**
	 * Create the combo used to choose {@link ModelResolutionPreferencePage#userSelectedResolver}
	 * 
	 * @param resolutionStrategyComposite
	 */
	private void createCombo(Composite resolutionStrategyComposite) {
		ArrayList<ModelResolverDescriptor> resolvers = Lists.newArrayList(modelResolverManager
				.getAllResolver());
		Collections.sort(resolvers);
		String[] comboLabels = new String[resolvers.size() + 1];
		final ModelResolverDescriptor[] comboValues = new ModelResolverDescriptor[resolvers.size() + 1];

		int forcedResolverInitialValueIndex = 0;

		comboLabels[DEFAULT_VALUE_INDEX] = DEFAULT_LABEL_VALUE;
		comboValues[DEFAULT_VALUE_INDEX] = null;

		for (int index = 1; index <= resolvers.size(); index++) {
			ModelResolverDescriptor modelResolverDescriptor = resolvers.get(index - 1);
			comboLabels[index] = modelResolverDescriptor.getLabel();
			comboValues[index] = modelResolverDescriptor;
			if (userSelectedResolver == modelResolverDescriptor) {
				forcedResolverInitialValueIndex = index;
			}
		}
		resolverCombo = new Combo(resolutionStrategyComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
		resolverCombo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		resolverCombo.setItems(comboLabels);
		resolverCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				int selectionIndex = resolverCombo.getSelectionIndex();
				if (DEFAULT_VALUE_INDEX == selectionIndex) {
					userSelectedResolver = null;
				} else {
					String currentValue = comboValues[selectionIndex].getId();
					userSelectedResolver = modelResolverManager.getDescriptor(currentValue);
				}
				updateDescription();
			}
		});
		resolverCombo.select(forcedResolverInitialValueIndex);
		activableElements.add(resolverCombo);
	}

	@Override
	public boolean performOk() {
		modelResolverManager.setUserSelectedResolver(userSelectedResolver);
		modelResolverManager.setResolution(isEnabled);
		return super.performOk();
	}

	@Override
	protected void performDefaults() {
		userSelectedResolver = null;
		resolverCombo.select(DEFAULT_VALUE_INDEX);
		isEnabled = true;
		updateDescription();
		updateWidgetEnablement();
		super.performDefaults();
	}

	public void init(IWorkbench workbench) {
	}
}

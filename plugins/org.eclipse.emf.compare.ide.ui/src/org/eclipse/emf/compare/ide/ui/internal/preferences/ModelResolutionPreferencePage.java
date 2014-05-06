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
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.registry.ModelResolverDescriptor;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.registry.ModelResolverRegistry;
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
	/** Label for default value of the "selected resolver" preference. */
	private static final String DEFAULT_LABEL_VALUE = EMFCompareIDEUIMessages
			.getString("ModelResolutionPreferencePage.selectedresolver.default.value.label"); //$NON-NLS-1$

	/** Description for default value of "selected resolver" preference. */
	private static final String DEFAULT_DESCRIPTION_VALUE = EMFCompareIDEUIMessages
			.getString("ModelResolutionPreferencePage.selectedresolver.default.value.description"); //$NON-NLS-1$

	private final ModelResolverRegistry modelResolverRegistry;

	private Button disableModelResolutionButton;

	/**
	 * List of element that need to be enabled or disabled depending on state of
	 * {@link ModelResolutionPreferencePage#isEnabled}
	 */
	private ArrayList<Control> activableElements;

	/** Forced resolver combo chooser. */
	private Combo resolverCombo;

	/** Values of the resolver combo, in displayed order. */
	private ModelResolverDescriptor[] comboValues;

	/** Label displaying the description of {@link ModelResolutionPreferencePage#userSelectedResolver} */
	private Label descriptionLabel;

	public ModelResolutionPreferencePage() {
		this.modelResolverRegistry = EMFCompareIDEUIPlugin.getDefault().getModelResolverRegistry();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createContents(Composite parent) {
		activableElements = Lists.newArrayList();

		Composite mainContainer = new Composite(parent, SWT.NONE);
		mainContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		mainContainer.setLayout(new GridLayout(1, false));

		createEnablementCheckBox(mainContainer);

		Composite resolutionStrategyComposite = createModelResolverComposite(mainContainer);

		createCombo(resolutionStrategyComposite);

		createDescription(resolutionStrategyComposite);

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
	 * Enable/Disable all widget from activableElements depending on whether the user accepts model
	 * resolutions or disabled them.
	 */
	private void updateWidgetEnablement() {
		final boolean resolutionEnabled = !disableModelResolutionButton.getSelection();
		disableModelResolutionButton.setSelection(!resolutionEnabled);
		for (Control controlToDisable : activableElements) {
			if (!controlToDisable.isDisposed()) {
				controlToDisable.setEnabled(resolutionEnabled);
			}
		}
	}

	/**
	 * Update the description field.
	 */
	private void updateDescription(String selectedDescription) {
		if (descriptionLabel != null && !descriptionLabel.isDisposed()) {
			descriptionLabel.setText(selectedDescription);
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
		disableModelResolutionButton = new Button(resolutionStrategyComposite, SWT.CHECK);
		disableModelResolutionButton.setText(EMFCompareIDEUIMessages
				.getString("ModelResolutionPreferencePage.disable.resolution.checkbox")); //$NON-NLS-1$
		disableModelResolutionButton.setSelection(!modelResolverRegistry.isEnabled());
		GridData checkBoxGridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		disableModelResolutionButton.setLayoutData(checkBoxGridData);
		disableModelResolutionButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateWidgetEnablement();
			}
		});
	}

	/**
	 * Create the combo used to choose a resolver.
	 * 
	 * @param resolutionStrategyComposite
	 */
	private void createCombo(Composite resolutionStrategyComposite) {
		List<ModelResolverDescriptor> resolvers = modelResolverRegistry.getRegisteredDescriptors();
		Collections.sort(resolvers, new Comparator<ModelResolverDescriptor>() {
			public int compare(ModelResolverDescriptor o1, ModelResolverDescriptor o2) {
				return o2.getRanking() - o1.getRanking();
			}
		});

		final String[] comboLabels = new String[resolvers.size() + 1];
		comboValues = new ModelResolverDescriptor[resolvers.size() + 1];
		comboLabels[0] = DEFAULT_LABEL_VALUE;
		comboValues[0] = null;

		final ModelResolverDescriptor selected = modelResolverRegistry.getSelectedResolver();

		int initialValueIndex = 0;
		for (int index = 1; index <= resolvers.size(); index++) {
			final ModelResolverDescriptor descriptor = resolvers.get(index - 1);
			comboLabels[index] = descriptor.getLabel();
			comboValues[index] = descriptor;
			if (selected == descriptor) {
				initialValueIndex = index;
			}
		}

		resolverCombo = new Combo(resolutionStrategyComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
		resolverCombo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		resolverCombo.setItems(comboLabels);
		resolverCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final ModelResolverDescriptor selection = comboValues[resolverCombo.getSelectionIndex()];
				if (selection == null) {
					updateDescription(DEFAULT_DESCRIPTION_VALUE);
				} else {
					updateDescription(selection.getDescription());
				}
			}
		});
		resolverCombo.select(initialValueIndex);
		final ModelResolverDescriptor initialValue = comboValues[initialValueIndex];
		if (initialValue == null) {
			updateDescription(DEFAULT_DESCRIPTION_VALUE);
		} else {
			updateDescription(initialValue.getDescription());
		}
		activableElements.add(resolverCombo);
	}

	@Override
	public boolean performOk() {
		final boolean resolutionEnabled = !disableModelResolutionButton.getSelection();
		modelResolverRegistry.toggleEnablement(resolutionEnabled);
		if (resolutionEnabled) {
			final ModelResolverDescriptor resolver = comboValues[resolverCombo.getSelectionIndex()];
			if (resolver == null) {
				modelResolverRegistry.setSelectedResolver(null);
			} else {
				modelResolverRegistry.setSelectedResolver(resolver.getClassName());
			}
		}
		return super.performOk();
	}

	@Override
	protected void performDefaults() {
		resolverCombo.select(0);
		disableModelResolutionButton.setSelection(false);
		updateDescription(DEFAULT_DESCRIPTION_VALUE);
		updateWidgetEnablement();
		super.performDefaults();
	}

	public void init(IWorkbench workbench) {
	}
}

/*******************************************************************************
 * Copyright (c) 2014, 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.preferences;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor;
import org.eclipse.emf.compare.rcp.internal.tracer.TracingConstant;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.DifferenceFilterManager;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.jface.databinding.viewers.IViewerObservableSet;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * Preference page for {@link IDifferenceFilter}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class FiltersPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/** Preference page ID. */
	public static final String PAGE_ID = "org.eclipse.emf.compare.rcp.ui.preferencePage.filters"; //$NON-NLS-1$

	/** Preference key holding synchronization behavior value. */
	public static final String SYNCHRONIZATION_BEHAVIOR = "org.eclipse.emf.compare.rcp.ui.filters.syncbehavior"; //$NON-NLS-1$

	/** Values used for the combobox. */
	private static final List<String> comboValues = Lists.newArrayList(MessageDialogWithToggle.ALWAYS,
			MessageDialogWithToggle.NEVER, MessageDialogWithToggle.PROMPT);

	/** Filter manager. Used to retrieve current and default configuration. */
	private DifferenceFilterManager filterManager = null;

	/** Interactive content holding UI components for enabled/disabled filters. */
	private InteractiveFilterUIContent defaultFilterInteractiveContent;

	/** Interactive content holding UI components for activated/deactivated filters. */
	private InteractiveFilterUIContent activateFilterInteractiveContent;

	/** The tab used to choose enabled filters. */
	private Composite enabledFilterTabComposite;

	/** The tab used to choose active filters. */
	private Composite activateFilterTabComposite;

	public void init(IWorkbench workbench) {
		// Do not use InstanceScope.Instance to be compatible with Helios.
		@SuppressWarnings("deprecation")
		ScopedPreferenceStore store = new ScopedPreferenceStore(new InstanceScope(),
				EMFCompareRCPUIPlugin.PLUGIN_ID);
		setPreferenceStore(store);

	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);

		// Create tab to choose filters that are enabled by default
		createDefaultEnabledFilterTab(tabFolder);
		// Create tab to activate or deactivate totally filters
		createActivateFilterTab(tabFolder);
		return container;
	}

	/**
	 * Create a tab to choose which filters to enable by default.
	 * 
	 * @param tabFolder
	 */
	private void createDefaultEnabledFilterTab(TabFolder tabFolder) {
		enabledFilterTabComposite = createTabSkeleton(tabFolder,
				EMFCompareRCPUIMessages.getString("FiltersPreferencePage.select.tab.label"), //$NON-NLS-1$
				EMFCompareRCPUIMessages.getString("FiltersPreferencePage.selectIntro.text")); //$NON-NLS-1$
		if (filterManager == null) {
			filterManager = EMFCompareRCPUIPlugin.getDefault().getDifferenceFilterManager();
		}
		defaultFilterInteractiveContent = new InteractiveFilterUIContent(enabledFilterTabComposite,
				filterManager.getAllFilters(), filterManager.getCurrentByDefaultFilters(), false);
		defaultFilterInteractiveContent.setComboInput(getCurrentSynchronizationBehavior());
	}

	/**
	 * Create a tab to select which filters to activate or deactivate.
	 * 
	 * @param tabFolder
	 */
	private void createActivateFilterTab(TabFolder tabFolder) {
		activateFilterTabComposite = createTabSkeleton(tabFolder,
				EMFCompareRCPUIMessages.getString("FiltersPreferencePage.activate.tab.label"), //$NON-NLS-1$
				EMFCompareRCPUIMessages.getString("FiltersPreferencePage.activateIntro.text")); //$NON-NLS-1$
		if (filterManager == null) {
			filterManager = EMFCompareRCPUIPlugin.getDefault().getDifferenceFilterManager();
		}
		activateFilterInteractiveContent = new InteractiveFilterUIContent(activateFilterTabComposite,
				filterManager.getAllFilters(), filterManager.getCurrentInactiveFilters(), true);
	}

	/**
	 * Create skeleton of a tab.
	 * 
	 * @param tabFolder
	 * @param tabLabel
	 * @param introText
	 *            Text use as description a tab
	 * @return Main composite of the tab
	 */
	private Composite createTabSkeleton(TabFolder tabFolder, String tabLabel, String introText) {
		TabItem tbtmMain = new TabItem(tabFolder, SWT.NONE);
		tbtmMain.setText(tabLabel);
		Composite tabComposite = new Composite(tabFolder, SWT.NONE);
		tbtmMain.setControl(tabComposite);
		tabComposite.setLayout(new GridLayout(1, true));
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		tabComposite.setLayoutData(layoutData);
		// Description text
		Label introductionText = new Label(tabComposite, SWT.WRAP);
		introductionText.setText(introText);
		return tabComposite;
	}

	/**
	 * Gets the current value of the filter synchronization behavior.
	 * <p>
	 * This value can only be one of the following:
	 * <ul>
	 * <li>{@link MessageDialogWithToggle#PROMPT}</li>
	 * <li>{@link MessageDialogWithToggle#ALWAYS}</li>
	 * <li>{@link MessageDialogWithToggle#NEVER}</li>
	 * </ul>
	 * </p>
	 * 
	 * @return String.
	 */
	public String getCurrentSynchronizationBehavior() {
		String value = getPreferenceStore().getString(SYNCHRONIZATION_BEHAVIOR);
		if (value == null || !comboValues.contains(value)) {
			value = getDefaultSynchronizationBehavior();
		}
		return value;
	}

	/**
	 * @return The default value of filter synchronization behavior.
	 */
	public String getDefaultSynchronizationBehavior() {
		return MessageDialogWithToggle.PROMPT;
	}

	/**
	 * Set the current value of the filter synchronization behavior.
	 * 
	 * @param newBehavior
	 *            New value.
	 */
	public void setCurrentSynchronizationBehavior(String newBehavior) {
		if (getDefaultSynchronizationBehavior().equals(newBehavior)) {
			getPreferenceStore().setToDefault(SYNCHRONIZATION_BEHAVIOR);
		} else {
			getPreferenceStore().setValue(SYNCHRONIZATION_BEHAVIOR, newBehavior);
		}
		// Trace preferences values
		if (TracingConstant.CONFIGURATION_TRACING_ACTIVATED) {
			StringBuilder builder = new StringBuilder();
			// Print each preferences
			builder.append("Preference ").append(SYNCHRONIZATION_BEHAVIOR).append(":\n"); //$NON-NLS-1$ //$NON-NLS-2$
			String preferenceValue = getPreferenceStore().getString(SYNCHRONIZATION_BEHAVIOR);
			builder.append(preferenceValue);
			EMFCompareRCPUIPlugin.getDefault().log(IStatus.INFO, builder.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean performOk() {
		filterManager.setCurrentByDefaultFilters(defaultFilterInteractiveContent.getCheckedFilter());
		filterManager.setCurrentActiveFilters(activateFilterInteractiveContent.getCheckedFilter());
		setCurrentSynchronizationBehavior(defaultFilterInteractiveContent.getSynchronizationBehavior());
		return super.performOk();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performDefaults() {
		if (activateFilterTabComposite.isVisible()) {
			activateFilterInteractiveContent.checkElements(filterManager.getAllFilters());
		}
		if (enabledFilterTabComposite.isVisible()) {
			defaultFilterInteractiveContent.checkElements(filterManager.getInitialByDefaultFilters());
			defaultFilterInteractiveContent.setComboInput(getDefaultSynchronizationBehavior());
		}
		super.performDefaults();
	}

	/**
	 * Interactive UI for filter preference page.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static class InteractiveFilterUIContent {

		/** Text that will be updated with the description of the viewer. */
		private final Label descriptionText;

		/** Viewer of {@link IDifferenceFilter}. */
		private CheckboxTableViewer viewer;

		/** DataHolder for enabled/disabled {@link IDifferenceFilter}. */
		private FilterDataHolder dataHolder = new FilterDataHolder();

		/** DataHolder for activated/deactivated {@link IDifferenceFilter}. */
		private FilterDataHolder allFilters = new FilterDataHolder();

		/** Combo holding synchronization behavior preferences. */
		private Combo combo;

		/** Field holding {@link SynchronizationBehavior} */
		private String synchronizationBehaviorValue;

		private InteractiveFilterUIContent(Composite parent, Collection<? extends IDifferenceFilter> filters,
				Collection<? extends IDifferenceFilter> defaultCheck, boolean isDeactivateTab) {
			super();
			Composite contentComposite = new Composite(parent, SWT.BORDER);
			contentComposite.setLayout(new GridLayout(1, true));
			contentComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

			Label introductionText = new Label(contentComposite, SWT.WRAP);
			if (isDeactivateTab) {
				introductionText.setText(
						EMFCompareRCPUIMessages.getString("FiltersPreferencePage.INTRO_DEACTIVATE_TEXT")); //$NON-NLS-1$
			} else {
				introductionText.setText(
						EMFCompareRCPUIMessages.getString("FiltersPreferencePage.INTRO_SELECT_TEXT")); //$NON-NLS-1$
			}
			introductionText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
			// Engine chooser composite
			Composite viewerComposite = new Composite(contentComposite, SWT.NONE);
			viewerComposite.setLayout(new GridLayout(1, true));
			viewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			viewer = createViewer(viewerComposite);
			// Descriptor engine Text
			this.descriptionText = createDescriptionComposite(contentComposite);
			if (!isDeactivateTab) {
				createSynchronizationBehaviorContent(parent);
			}
			setViewerInput(Lists.newArrayList(filters));
			if (isDeactivateTab) {
				SetView<IDifferenceFilter> activatedFilters = Sets.difference(Sets.newLinkedHashSet(filters),
						Sets.newLinkedHashSet(defaultCheck));
				bindAndInit(activatedFilters);
			} else {
				bindAndInit(Sets.newLinkedHashSet(defaultCheck));
			}
			allFilters.setFilters(Sets.newLinkedHashSet(filters));
		}

		/**
		 * Content for synchronization behavior preferences.
		 * 
		 * @param parent
		 */
		private void createSynchronizationBehaviorContent(Composite parent) {
			Group synchronizationGroup = new Group(parent, SWT.NONE);
			GridData layoutData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
			GridLayout layout = new GridLayout(2, false);
			synchronizationGroup.setLayout(layout);
			synchronizationGroup.setLayoutData(layoutData);
			synchronizationGroup.setText(EMFCompareRCPUIMessages
					.getString("InteractiveFilterUIContent.sync.behavior.group.label")); //$NON-NLS-1$
			Label label = new Label(synchronizationGroup, SWT.WRAP);
			label.setText(
					EMFCompareRCPUIMessages.getString("InteractiveFilterUIContent.sync.behavior.label")); //$NON-NLS-1$
			label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
			combo = new Combo(synchronizationGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
			for (String comboLabel : comboValues) {
				combo.add(comboLabel);
			}
			combo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true));
			combo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (combo.equals(e.getSource())) {
						synchronizationBehaviorValue = combo.getItem(combo.getSelectionIndex());
					}
				}

			});
		}

		/**
		 * @return The state of the group synchronization behavior field.
		 */
		public String getSynchronizationBehavior() {
			return synchronizationBehaviorValue;
		}

		/**
		 * @return All checked {@link IDifferenceFilter}.
		 */
		public Set<IDifferenceFilter> getCheckedFilter() {
			return dataHolder.getFilters();
		}

		private void setViewerInput(List<IDifferenceFilter> filters) {
			Collections.sort(filters, new Comparator<IDifferenceFilter>() {

				public int compare(IDifferenceFilter o1, IDifferenceFilter o2) {
					if (o1 == o2) {
						return 0;
					} else if (o1 == null || o1.getLabel() == null) {
						return -1;
					} else if (o2 == null || o2.getLabel() == null) {
						return 1;
					}
					return o1.getLabel().compareTo(o2.getLabel());
				}
			});
			viewer.setInput(filters);
			select(filters.iterator().next());
		}

		private CheckboxTableViewer createViewer(Composite viewerCompsite) {
			CheckboxTableViewer descriptorViewer = CheckboxTableViewer.newCheckList(viewerCompsite,
					SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
			descriptorViewer.setContentProvider(ArrayContentProvider.getInstance());
			descriptorViewer.setLabelProvider(new FilterLabelProvider());
			GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			descriptorViewer.getControl().setLayoutData(gd);
			setViewer(descriptorViewer);
			return descriptorViewer;
		}

		private void bindAndInit(Set<IDifferenceFilter> defaultCheck) {
			if (dataHolder != null) {
				if (defaultCheck != null) {
					dataHolder.setFilters(defaultCheck);
				}
				// Bind data
				bindMultipleData(viewer, dataHolder);
			}
		}

		/**
		 * Bind UI to data object.
		 * 
		 * @param engineBindingProperty
		 * @param descriptorViewer
		 * @param dataObject
		 */
		private void bindMultipleData(CheckboxTableViewer descriptorViewer, FilterDataHolder dataObject) {
			DataBindingContext ctx = new DataBindingContext();
			// Bind the button with the corresponding field in data
			IViewerObservableSet target = ViewersObservables.observeCheckedElements(descriptorViewer,
					IDifferenceFilter.class);
			IObservableSet model = PojoProperties.set(FilterDataHolder.class, FilterDataHolder.FIELD_NAME)
					.observe(dataObject);

			ctx.bindSet(target, model);
		}

		/**
		 * Check element in the viewer.
		 * 
		 * @param checkedFilter
		 *            Element to check.
		 */
		public void checkElements(Set<IDifferenceFilter> checkedFilter) {
			viewer.setCheckedElements(checkedFilter.toArray());
			dataHolder.setFilters(checkedFilter);
		}

		/**
		 * Composite for description. This composite hold the text widget that will update with the current
		 * selection
		 * 
		 * @param composite
		 * @return
		 */
		private Label createDescriptionComposite(Composite composite) {
			Group descriptionComposite = new Group(composite, SWT.NONE);
			descriptionComposite.setText(
					EMFCompareRCPUIMessages.getString("InteractiveUIContent.descriptionComposite.label")); //$NON-NLS-1$
			descriptionComposite.setLayout(new GridLayout(1, false));
			descriptionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
			Label engineDescriptionLabel = new Label(descriptionComposite, SWT.WRAP);
			engineDescriptionLabel
					.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
			GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			layoutData.widthHint = 400;
			layoutData.heightHint = 50;
			engineDescriptionLabel.setLayoutData(layoutData);
			return engineDescriptionLabel;
		}

		/**
		 * Handle a selection in the viewer. Update related components.
		 * 
		 * @param descriptor
		 */
		public void select(IDifferenceFilter descriptor) {
			// Update viewer
			viewer.setSelection(new StructuredSelection(descriptor), true);
			String description = descriptor.getDescription();
			if (description != null) {
				descriptionText.setText(description);
			} else {
				descriptionText.setText(""); //$NON-NLS-1$
			}

		}

		/**
		 * @param viewer
		 *            A {@link StructuredViewer} of {@link IItemDescriptor}
		 */
		public void setViewer(CheckboxTableViewer inputViewer) {
			this.viewer = inputViewer;
			viewer.addSelectionChangedListener(new DescriptionListener());
		}

		/**
		 * Select the correct behavior in the interactive UI.
		 * 
		 * @param behavior
		 */
		public void setComboInput(String behavior) {
			int index = 0;
			for (String value : comboValues) {
				if (value.equals(behavior)) {
					combo.select(index);
					synchronizationBehaviorValue = behavior;
				}
				index++;
			}
		}

		/**
		 * Listener to update description text
		 * 
		 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
		 */
		private final class DescriptionListener implements ISelectionChangedListener {

			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					IStructuredSelection structSelection = (IStructuredSelection)selection;
					Object selected = structSelection.getFirstElement();
					if (selected instanceof IDifferenceFilter) {
						IDifferenceFilter desc = (IDifferenceFilter)selected;
						String description = desc.getDescription();
						if (description != null) {
							descriptionText.setText(description);
						} else {
							descriptionText.setText(""); //$NON-NLS-1$
						}
					}
				}

			}
		}

		/**
		 * Label provider for {@link IDifferenceFilter}.
		 * 
		 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
		 */
		private static final class FilterLabelProvider extends LabelProvider {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public String getText(Object element) {
				if (element instanceof IDifferenceFilter) {
					return ((IDifferenceFilter)element).getLabel();
				}
				return super.getText(element);
			}
		}

		/**
		 * Data holder for checked {@link IDifferenceFilter}.
		 * 
		 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
		 */
		private static final class FilterDataHolder {

			private static final String FIELD_NAME = "filters"; //$NON-NLS-1$

			private Set<IDifferenceFilter> filters;

			public Set<IDifferenceFilter> getFilters() {
				return filters;
			}

			public void setFilters(Set<IDifferenceFilter> filters) {
				this.filters = filters;
			}

		}
	}

}

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

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.compare.conflict.IConflictDetector;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.equi.IEquiEngine;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine.Factory;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryImpl;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.internal.engine.IItemDescriptor;
import org.eclipse.emf.compare.rcp.internal.engine.IItemRegistry;
import org.eclipse.emf.compare.rcp.internal.engine.impl.ItemUtil;
import org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences;
import org.eclipse.emf.compare.rcp.internal.tracer.TracingConstant;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.emf.compare.req.IReqEngine;
import org.eclipse.jface.databinding.viewers.IViewerObservableSet;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * Preference page for engines preferences
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class EnginesPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/** Label of the group holding the text description */
	private static final String DESCRIPTION_Label = "Description"; //$NON-NLS-1$

	/** Key used to map button to the {@link Text} widget that display the description */
	private static final String DESCRIPTION_TEXT_DATA_KEY = "descriptionText"; //$NON-NLS-1$

	/** Label provider for {@link IItemDescriptor} */
	private final EngineDescriptorLabelProvider descriptorLabelProvider = new EngineDescriptorLabelProvider();

	/** ID in the preference store when no engine has been set */
	private static final String DEFAULT_ENGINE_ID = ""; //$NON-NLS-1$

	/** Pointer to list viewers for each tab */
	private Map<String, CheckboxTableViewer> viewerFromTabs = new HashMap<String, CheckboxTableViewer>();

	/** Label of the Diff engine tab */
	private static final String DIFFERENCES_ENGINE_TAB_LABEL = EMFCompareRCPUIMessages
			.getString("EnginesPreferencePage.DIFFERENCES_ENGINE_TAB_LABEL"); //$NON-NLS-1$

	/** Label of the Equi engine tab */
	private static final String EQUIVALENCES_ENGINE_TAB_LABEL = EMFCompareRCPUIMessages
			.getString("EnginesPreferencePage.EQUIVALENCES_ENGINE_TAB_LABEL"); //$NON-NLS-1$

	/** Label of the Req engine tab */
	private static final String REQUIREMENT_ENGINE_TAB_LABEL = EMFCompareRCPUIMessages
			.getString("EnginesPreferencePage.REQUIREMENT_ENGINE_TAB_LABEL"); //$NON-NLS-1$

	/** Label of the Conflict detector tab */
	private static final String CONFLICT_DETECTOR_TAB_LABEL = EMFCompareRCPUIMessages
			.getString("EnginesPreferencePage.CONFLICT_DETECTOR_TAB_LABEL"); //$NON-NLS-1$

	/** Label of the Conflict detector tab */
	private static final String MATCH_ENGINE_TAB_LABEL = EMFCompareRCPUIMessages
			.getString("EnginesPreferencePage.MATCH_ENGINE_TAB_LABEL"); //$NON-NLS-1$

	/** Match Engine tab descriptor. */
	private static final String MATCH_ENGINE_INTRO_TEXT = EMFCompareRCPUIMessages
			.getString("EnginesPreferencePagestatic.MATCH_ENGINE_INTRO_TEXT"); //$NON-NLS-1$

	/** Diff Engine tab descritpor. */
	private static final String DIFF_ENGINE_INTRO_TEXT = EMFCompareRCPUIMessages
			.getString("EnginesPreferencePagestatic.DIFF_ENGINE_INTRO_TEXT"); //$NON-NLS-1$

	/** Equi Engine tab descriptor. */
	private static final String EQUI_ENGINE_INTRO_TEXT = EMFCompareRCPUIMessages
			.getString("EnginesPreferencePagestatic.EQUI_ENGINE_INTRO_TEXT"); //$NON-NLS-1$

	/** Req Engine tab descriptor. */
	private static final String REQ_ENGINE_INTRO_TEXT = EMFCompareRCPUIMessages
			.getString("EnginesPreferencePagestatic.REQ_ENGINE_INTRO_TEXT"); //$NON-NLS-1$

	private static final String INCORRECT_SELECTION_TITLE = EMFCompareRCPUIMessages
			.getString("EnginesPreferencePagestatic.INCORRECT_SELECTION_TITLE");//$NON-NLS-1$

	/** Conflict Detector tab Descriptor. */
	private static final String CONFLICT_DETECTOR_INTRO_TEXT = EMFCompareRCPUIMessages
			.getString("EnginesPreferencePagestatic.CONFLICT_DETECTOR_INTRO_TEXT"); //$NON-NLS-1$

	private static final String INCORRECT_SELECTION_MESSAGE = EMFCompareRCPUIMessages
			.getString("EnginesPreferencePagestatic.INCORRECT_SELECTION_MESSAGE"); //$NON-NLS-1$

	private static final String DATA_FIELD_NAME = "currentSelection"; //$NON-NLS-1$

	/** Data regarding the Difference selected engine */
	private SingleValueHolder<IDiffEngine> diffEngineData = new SingleValueHolder<IDiffEngine>();

	/** Data regarding the Equivalence selected engine */
	private SingleValueHolder<IEquiEngine> equiEngineData = new SingleValueHolder<IEquiEngine>();

	/** Data regarding the Requirement selected engine */
	private SingleValueHolder<IReqEngine> reqEngineData = new SingleValueHolder<IReqEngine>();

	/** Data regarding the Conflicts detector selected engine */
	private SingleValueHolder<IConflictDetector> conflictsDetectorData = new SingleValueHolder<IConflictDetector>();

	/** Data regarding the selected match engine factories. */
	private MultipleValueHolder<IMatchEngine.Factory> matchEnginesData = new MultipleValueHolder<IMatchEngine.Factory>();

	public EnginesPreferencePage() {
		super();
	}

	public EnginesPreferencePage(String title) {
		super(title);
	}

	public EnginesPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	public void init(IWorkbench workbench) {
		// The preferences shall be stored under EMF Compare RCP Plugin.
		// Do not use InstanceScope.Instance to be compatible with Helios.
		ScopedPreferenceStore store = new ScopedPreferenceStore(new InstanceScope(),
				EMFCompareRCPPlugin.PLUGIN_ID);
		setPreferenceStore(store);
	}

	@Override
	protected Control createContents(Composite parent) {

		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		// Create match engine tab
		IItemRegistry<Factory> matchEngineFactoryDescriptorRegistry = EMFCompareRCPPlugin.getDefault()
				.getMatchEngineFactoryDescriptorRegistry();
		IItemDescriptor<Factory> defaultMatchEngineDescriptor = matchEngineFactoryDescriptorRegistry
				.getItemDescriptor(MatchEngineFactoryImpl.class.getCanonicalName());
		createMultipleValueSelectorTab(tabFolder, MATCH_ENGINE_TAB_LABEL, MATCH_ENGINE_INTRO_TEXT,
				matchEngineFactoryDescriptorRegistry, DATA_FIELD_NAME,
				EMFComparePreferences.MATCH_ENGINE_DISABLE_ENGINES, matchEnginesData,
				defaultMatchEngineDescriptor);
		// Create diff engine tab
		IItemRegistry<IDiffEngine> diffEngineDescriptorRegistry = EMFCompareRCPPlugin.getDefault()
				.getDiffEngineDescriptorRegistry();
		createSingleValueSelectorTab(tabFolder, DIFFERENCES_ENGINE_TAB_LABEL, DIFF_ENGINE_INTRO_TEXT,
				diffEngineDescriptorRegistry, EMFComparePreferences.DIFF_ENGINES, diffEngineData);
		// Create equi engine tab
		IItemRegistry<IEquiEngine> equiEngineDescriptorRegistry = EMFCompareRCPPlugin.getDefault()
				.getEquiEngineDescriptorRegistry();
		createSingleValueSelectorTab(tabFolder, EQUIVALENCES_ENGINE_TAB_LABEL, EQUI_ENGINE_INTRO_TEXT,
				equiEngineDescriptorRegistry, EMFComparePreferences.EQUI_ENGINES, equiEngineData);
		// Create req engine tab
		IItemRegistry<IReqEngine> reqEngineDescriptorRegistry = EMFCompareRCPPlugin.getDefault()
				.getReqEngineDescriptorRegistry();
		createSingleValueSelectorTab(tabFolder, REQUIREMENT_ENGINE_TAB_LABEL, REQ_ENGINE_INTRO_TEXT,
				reqEngineDescriptorRegistry, EMFComparePreferences.REQ_ENGINES, reqEngineData);
		// Create conflicts detectors tab
		IItemRegistry<IConflictDetector> conflictDetectorDescriptorRegistry = EMFCompareRCPPlugin
				.getDefault().getConflictDetectorDescriptorRegistry();
		createSingleValueSelectorTab(tabFolder, CONFLICT_DETECTOR_TAB_LABEL, CONFLICT_DETECTOR_INTRO_TEXT,
				conflictDetectorDescriptorRegistry, EMFComparePreferences.CONFLICTS_DETECTOR,
				conflictsDetectorData);

		return container;
	}

	/**
	 * Create a tab using an {@link IItemRegistry} for a single value selection
	 * 
	 * @param tabFolder
	 *            Holder tab folder
	 * @param label
	 *            Label of the new tab
	 * @param tabDescriptor
	 *            Tab descriptor label
	 * @param registry
	 *            {@link IItemRegistry} used to fill the tab
	 * @param preferenceKey
	 *            The preference key of the engine type
	 * @param dataObject
	 *            Object that hold the UI data
	 * @param <T>
	 *            Type of descriptor
	 */
	private <T> void createSingleValueSelectorTab(TabFolder tabFolder, String label, String tabDescriptor,
			IItemRegistry<T> registry, String preferenceKey, SingleValueHolder<T> dataObject) {
		TabItem tbtmMain = new TabItem(tabFolder, SWT.NONE);
		tbtmMain.setText(label);
		// Parent container
		Composite tabComposite = new Composite(tabFolder, SWT.NONE);
		tabComposite.setLayout(new GridLayout(1, true));
		tbtmMain.setControl(tabComposite);
		// Introduction text
		Label introductionText = new Label(tabComposite, SWT.WRAP);
		introductionText.setText(tabDescriptor);
		// Selector composite
		Composite selectorComposite = new Composite(tabComposite, SWT.NONE);
		selectorComposite.setLayout(new GridLayout(2, true));
		selectorComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		// Item chooser composite
		Composite comboBoxCompsite = new Composite(selectorComposite, SWT.NONE);
		comboBoxCompsite.setLayout(new GridLayout(1, false));
		comboBoxCompsite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		// Descriptor item Text
		Text engineDescriptionText = createDescriptionComposite(selectorComposite);

		fillEngineComposite(registry, comboBoxCompsite, engineDescriptionText, preferenceKey, dataObject);

	}

	/**
	 * Create a tab using an {@link IItemRegistry} for multiple value selection
	 * 
	 * @param tabFolder
	 *            Holding tab
	 * @param tabLabel
	 *            Tab label
	 * @param introText
	 *            Description of the tab content
	 * @param registry
	 *            Registry of item use as input
	 * @param bindingProperty
	 *            Property name use to bind data to dataObject
	 * @param preferenceKey
	 *            Preference key for this tab
	 * @param dataObject
	 *            Data object use to hold information
	 * @param defaultSelection
	 *            Default selection.
	 */
	private <T> void createMultipleValueSelectorTab(TabFolder tabFolder, String tabLabel, String introText,
			IItemRegistry<T> registry, String bindingProperty, String preferenceKey,
			MultipleValueHolder<T> dataObject, IItemDescriptor<T> defaultSelection) {
		TabItem tbtmMain = new TabItem(tabFolder, SWT.NONE);
		tbtmMain.setText(tabLabel);
		Composite tabComposite = new Composite(tabFolder, SWT.NONE);
		tbtmMain.setControl(tabComposite);
		Label introductionText = new Label(tabComposite, SWT.WRAP);
		introductionText.setText(introText);
		tabComposite.setLayout(new GridLayout(1, true));
		// Parent container
		Composite composite = new Composite(tabComposite, SWT.NONE);
		composite.setLayout(new GridLayout(2, true));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		// Engine chooser composite
		Composite viewerComposite = new Composite(composite, SWT.NONE);
		viewerComposite.setLayout(new GridLayout(1, false));
		viewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		// Descriptor engine Text
		Text engineDescriptionText = createDescriptionComposite(composite);

		fillMatchEngineFactoryComposite(registry, bindingProperty, viewerComposite, engineDescriptionText,
				preferenceKey, dataObject, defaultSelection);

	}

	/**
	 * Composite for description. This composite hold the text field that will update with the current
	 * selection
	 * 
	 * @param composite
	 * @return
	 */
	private Text createDescriptionComposite(Composite composite) {
		Group descriptionComposite = new Group(composite, SWT.BORDER);
		descriptionComposite.setText(DESCRIPTION_Label);
		descriptionComposite.setLayout(new GridLayout(1, false));
		descriptionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		Text engineDescriptionText = new Text(descriptionComposite, SWT.WRAP | SWT.MULTI);
		engineDescriptionText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		layoutData.widthHint = 300;
		engineDescriptionText.setLayoutData(layoutData);
		engineDescriptionText.setEditable(false);
		return engineDescriptionText;
	}

	/**
	 * Fill the composite with list of engine from a registry.
	 * 
	 * @param registry
	 *            Registry containing data
	 * @param engineBindingProperty
	 *            Name of the field of {@link SingleValueHolder} that reflect the selection of an engine (use
	 *            for binding)
	 * @param comboBoxComposite
	 *            Parent composite
	 * @param descriptionText
	 *            Text that need to be update on selection representing the description of an engine
	 * @param dataObject
	 *            Model object holding the information selected by the user
	 * @param preferenceKey
	 *            Preference key link to this tab
	 */
	private <T> void fillEngineComposite(IItemRegistry<T> registry, Composite comboBoxComposite,
			final Text descriptionText, String preferenceKey, final SingleValueHolder<T> dataObject) {

		final CheckboxTableViewer descriptorViewer = CheckboxTableViewer.newCheckList(comboBoxComposite,
				SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.SINGLE);
		descriptorViewer.addSelectionChangedListener(new DescriptionListener(descriptionText));
		descriptorViewer.setData(DESCRIPTION_TEXT_DATA_KEY, descriptionText);
		descriptorViewer.setContentProvider(ArrayContentProvider.getInstance());
		descriptorViewer.setLabelProvider(descriptorLabelProvider);
		GridData gd = new GridData(GridData.FILL_BOTH);
		descriptorViewer.getControl().setLayoutData(gd);
		// Only one check at a time
		descriptorViewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				Object element = event.getElement();
				if (event.getChecked()) {
					if (element instanceof IItemDescriptor<?>) {
						@SuppressWarnings("unchecked")
						IItemDescriptor<T> descriptor = (IItemDescriptor<T>)element;
						dataObject.setCurrentSelection(descriptor);
					}
					descriptorViewer.setCheckedElements(new Object[] {element });
				} else {
					// Prevent from nothing checked
					if (descriptorViewer.getCheckedElements().length == 0) {
						descriptorViewer.setCheckedElements(new Object[] {element });
						MessageDialog.openWarning(getShell(), INCORRECT_SELECTION_TITLE,
								INCORRECT_SELECTION_MESSAGE);
					}
				}

			}
		});
		// Save for reset default
		viewerFromTabs.put(preferenceKey, descriptorViewer);
		List<IItemDescriptor<T>> itemDescriptors = registry.getItemDescriptors();
		Collections.sort(itemDescriptors, Collections.reverseOrder());
		descriptorViewer.setInput(itemDescriptors);
		// Init default value
		IItemDescriptor<T> defaultEngine = ItemUtil.getDefaultItemDescriptor(registry, preferenceKey,
				EMFCompareRCPPlugin.getDefault().getEMFComparePreferences());
		descriptorViewer.setSelection(new StructuredSelection(defaultEngine), true);
		descriptorViewer.setCheckedElements(new Object[] {defaultEngine });
		dataObject.setCurrentSelection(defaultEngine);
		descriptionText.setText(defaultEngine.getDescription());

	}

	private <T> void fillMatchEngineFactoryComposite(IItemRegistry<T> registry, String engineBindingProperty,
			Composite comboBoxComposite, final Text descriptionText, String preferenceKey,
			MultipleValueHolder<T> dataObject, IItemDescriptor<T> defaultDescriptor) {
		CheckboxTableViewer descriptorViewer = CheckboxTableViewer.newCheckList(comboBoxComposite, SWT.BORDER
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		descriptorViewer.addSelectionChangedListener(new DescriptionListener(descriptionText));
		descriptorViewer.setData(DESCRIPTION_TEXT_DATA_KEY, descriptionText);
		descriptorViewer.setContentProvider(ArrayContentProvider.getInstance());
		descriptorViewer.setLabelProvider(descriptorLabelProvider);
		GridData gd = new GridData(GridData.FILL_BOTH);
		descriptorViewer.getControl().setLayoutData(gd);
		// Save for reset default
		viewerFromTabs.put(preferenceKey, descriptorViewer);
		// Filter input with input with higher rank than default item descriptor
		List<IItemDescriptor<T>> itemDescriptors = registry.getItemDescriptors();
		Collections.sort(itemDescriptors, Collections.reverseOrder());
		descriptorViewer.setInput(itemDescriptors);

		descriptorViewer.setSelection(new StructuredSelection(defaultDescriptor));
		dataObject.setCurrentSelection(getActiveFactory(registry, preferenceKey));

		bindMultipleData(engineBindingProperty, descriptorViewer, dataObject);

	}

	private <T> Set<IItemDescriptor<T>> getActiveFactory(IItemRegistry<T> registry, String preferenceKey) {
		List<IItemDescriptor<T>> itemsDescriptor = ItemUtil.getItemsDescriptor(registry, preferenceKey,
				EMFCompareRCPPlugin.getDefault().getEMFComparePreferences());

		if (itemsDescriptor == null) {
			itemsDescriptor = Collections.emptyList();
		}
		Set<IItemDescriptor<T>> disableFactories = Sets.newHashSet(itemsDescriptor);
		Set<IItemDescriptor<T>> allFactories = Sets.newHashSet(registry.getItemDescriptors());
		Set<IItemDescriptor<T>> activeFactory = Sets.difference(allFactories, disableFactories);

		return activeFactory;
	}

	private <T> void bindMultipleData(String engineBindingProperty, CheckboxTableViewer descriptorViewer,
			final MultipleValueHolder<T> dataObject) {
		DataBindingContext ctx = new DataBindingContext();
		// Bind the button with the corresponding field in data
		IViewerObservableSet target = ViewersObservables.observeCheckedElements(descriptorViewer,
				IItemDescriptor.class);
		// IObservableValue target = WidgetProperties.selection().observe(viewer);
		IObservableSet model = PojoProperties.set(MultipleValueHolder.class, engineBindingProperty).observe(
				dataObject);

		ctx.bindSet(target, model);

	}

	@Override
	public boolean performOk() {

		// Update preferences preferences
		setEnginePreferences(EMFComparePreferences.DIFF_ENGINES, diffEngineData.getCurrentSelection(),
				EMFCompareRCPPlugin.getDefault().getDiffEngineDescriptorRegistry()
						.getHighestRankingDescriptor());
		setEnginePreferences(EMFComparePreferences.EQUI_ENGINES, equiEngineData.getCurrentSelection(),
				EMFCompareRCPPlugin.getDefault().getEquiEngineDescriptorRegistry()
						.getHighestRankingDescriptor());
		setEnginePreferences(EMFComparePreferences.REQ_ENGINES, reqEngineData.getCurrentSelection(),
				EMFCompareRCPPlugin.getDefault().getReqEngineDescriptorRegistry()
						.getHighestRankingDescriptor());
		setEnginePreferences(EMFComparePreferences.CONFLICTS_DETECTOR, conflictsDetectorData
				.getCurrentSelection(), EMFCompareRCPPlugin.getDefault()
				.getConflictDetectorDescriptorRegistry().getHighestRankingDescriptor());

		// Set match engine to disable
		Set<IItemDescriptor<Factory>> matchEngineRegsitry = Sets.newHashSet(EMFCompareRCPPlugin.getDefault()
				.getMatchEngineFactoryDescriptorRegistry().getItemDescriptors());
		Set<IItemDescriptor<Factory>> matchingEngineToDisable = Sets.difference(matchEngineRegsitry,
				matchEnginesData.getCurrentSelection());
		setEnginePreferences(EMFComparePreferences.MATCH_ENGINE_DISABLE_ENGINES, matchingEngineToDisable,
				new ArrayList<IItemDescriptor<IMatchEngine.Factory>>());

		if (TracingConstant.CONFIGURATION_TRACING_ACTIVATED) {
			StringBuilder traceMessage = new StringBuilder("Preference serialization:\n"); //$NON-NLS-1$
			String prefDelimiter = " :\n"; //$NON-NLS-1$
			String new_line = "\n"; //$NON-NLS-1$
			traceMessage.append(EMFComparePreferences.DIFF_ENGINES).append(prefDelimiter).append(
					getPreferenceStore().getString(EMFComparePreferences.DIFF_ENGINES)).append(new_line);
			traceMessage.append(EMFComparePreferences.EQUI_ENGINES).append(prefDelimiter).append(
					getPreferenceStore().getString(EMFComparePreferences.EQUI_ENGINES)).append(new_line);
			traceMessage.append(EMFComparePreferences.REQ_ENGINES).append(prefDelimiter).append(
					getPreferenceStore().getString(EMFComparePreferences.REQ_ENGINES)).append(new_line);
			traceMessage.append(EMFComparePreferences.CONFLICTS_DETECTOR).append(prefDelimiter).append(
					getPreferenceStore().getString(EMFComparePreferences.CONFLICTS_DETECTOR))
					.append(new_line);
			traceMessage.append(EMFComparePreferences.MATCH_ENGINE_DISABLE_ENGINES).append(prefDelimiter)
					.append(getPreferenceStore()
							.getString(EMFComparePreferences.MATCH_ENGINE_DISABLE_ENGINES)).append(new_line);

			EMFCompareRCPPlugin.getDefault().log(IStatus.INFO, traceMessage.toString());
		}
		return super.performOk();
	}

	@Override
	protected void performDefaults() {
		resetDefaultPreferencesToHighestRank(EMFCompareRCPPlugin.getDefault()
				.getDiffEngineDescriptorRegistry(), EMFComparePreferences.DIFF_ENGINES, diffEngineData);
		resetDefaultPreferencesToHighestRank(EMFCompareRCPPlugin.getDefault()
				.getReqEngineDescriptorRegistry(), EMFComparePreferences.REQ_ENGINES, reqEngineData);
		resetDefaultPreferencesToHighestRank(EMFCompareRCPPlugin.getDefault()
				.getEquiEngineDescriptorRegistry(), EMFComparePreferences.EQUI_ENGINES, equiEngineData);
		resetDefaultPreferencesToHighestRank(EMFCompareRCPPlugin.getDefault()
				.getConflictDetectorDescriptorRegistry(), EMFComparePreferences.CONFLICTS_DETECTOR,
				conflictsDetectorData);
		resetDefaultPreferencesToAll(EMFCompareRCPPlugin.getDefault()
				.getMatchEngineFactoryDescriptorRegistry(),
				EMFComparePreferences.MATCH_ENGINE_DISABLE_ENGINES, matchEnginesData);
		super.performDefaults();
	}

	/**
	 * Reset preference to default
	 * 
	 * @param registry
	 * @param preferenceKey
	 */
	private <T> void resetDefaultPreferencesToHighestRank(IItemRegistry<T> registry, String preferenceKey,
			SingleValueHolder<T> dataObject) {
		CheckboxTableViewer descriptorViewer = viewerFromTabs.get(preferenceKey);
		if (descriptorViewer != null) {
			Object _descriptionText = descriptorViewer.getData(DESCRIPTION_TEXT_DATA_KEY);
			if (_descriptionText instanceof Text) {
				Text descriptionText = (Text)_descriptionText;
				IItemDescriptor<T> defaultEngine = registry.getHighestRankingDescriptor();
				descriptorViewer.setSelection(new StructuredSelection(defaultEngine), true);
				descriptorViewer.setCheckedElements(new Object[] {defaultEngine });
				descriptionText.setText(defaultEngine.getDescription());
				dataObject.setCurrentSelection(defaultEngine);
			}
		}
	}

	private <T> void resetDefaultPreferencesToAll(IItemRegistry<T> registry, String preferenceKey,
			MultipleValueHolder<T> dataObject) {
		StructuredViewer descriptorViewer = viewerFromTabs.get(preferenceKey);
		if (descriptorViewer instanceof CheckboxTableViewer) {
			CheckboxTableViewer checkBoxViewer = (CheckboxTableViewer)descriptorViewer;
			Object _descriptionText = descriptorViewer.getData(DESCRIPTION_TEXT_DATA_KEY);
			if (_descriptionText instanceof Text) {
				Text descriptionText = (Text)_descriptionText;
				IItemDescriptor<T> defaultEngine = registry.getHighestRankingDescriptor();
				descriptorViewer.setSelection(new StructuredSelection(defaultEngine), true);
				descriptionText.setText(defaultEngine.getDescription());
				// dataObject.getCurrentSelection().addAll(registry.getEngineDescriptors());
				List<IItemDescriptor<T>> itemDescriptors = registry.getItemDescriptors();
				dataObject.setCurrentSelection(Sets.newHashSet(itemDescriptors));
				checkBoxViewer.setCheckedElements(itemDescriptors.toArray(new IItemDescriptor[itemDescriptors
						.size()]));
			}
		}
	}

	private <T> void setEnginePreferences(String preferenceKey, IItemDescriptor<T> currentSelectedEngine,
			IItemDescriptor<T> defaultConf) {
		if (currentSelectedEngine != null && !currentSelectedEngine.equals(defaultConf)) {
			getPreferenceStore().setValue(preferenceKey, currentSelectedEngine.getID());
		} else {
			getPreferenceStore().setValue(preferenceKey, DEFAULT_ENGINE_ID);
		}
	}

	private <T> void setEnginePreferences(String preferenceKey,
			Set<IItemDescriptor<T>> currentSelectedEngine, Collection<IItemDescriptor<T>> defaultConf) {
		if (currentSelectedEngine != null && !currentSelectedEngine.contains(defaultConf)) {
			StringBuilder descriptorsKey = new StringBuilder();
			for (Iterator<IItemDescriptor<T>> iterator = currentSelectedEngine.iterator(); iterator.hasNext();) {
				IItemDescriptor<T> iItemDescriptor = iterator.next();
				descriptorsKey.append(iItemDescriptor.getID());
				if (iterator.hasNext()) {
					descriptorsKey.append(ItemUtil.PREFFERENCE_DELIMITER);
				}
			}
			getPreferenceStore().setValue(preferenceKey, descriptorsKey.toString());
		} else {
			getPreferenceStore().setValue(preferenceKey, DEFAULT_ENGINE_ID);
		}
	}

	/**
	 * Label provider for {@link IItemDescriptor}
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static final class EngineDescriptorLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof IItemDescriptor<?>) {
				IItemDescriptor<?> desc = (IItemDescriptor<?>)element;
				return desc.getLabel();
			}
			return super.getText(element);
		}
	}

	/**
	 * Listener to update description text
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static final class DescriptionListener implements ISelectionChangedListener {
		private final Text descriptionText;

		private DescriptionListener(Text descriptionText) {
			this.descriptionText = descriptionText;
		}

		public void selectionChanged(SelectionChangedEvent event) {
			ISelection selection = event.getSelection();
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structSelection = (IStructuredSelection)selection;
				Object selected = structSelection.getFirstElement();
				if (selected instanceof IItemDescriptor<?>) {
					IItemDescriptor<?> desc = (IItemDescriptor<?>)selected;
					String description = desc.getDescription();
					descriptionText.setText(description);
				}
			}

		}
	}

	/**
	 * Data object use to store a simple value data
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static class SingleValueHolder<T> {

		/** Current value */
		public IItemDescriptor<T> currentSelection;

		public IItemDescriptor<T> getCurrentSelection() {
			return currentSelection;
		}

		public void setCurrentSelection(IItemDescriptor<T> currentSelection) {
			this.currentSelection = currentSelection;
		}

	}

	/**
	 * Data object use to store multiple values data
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 * @param <T>
	 */
	private class MultipleValueHolder<T> {

		public Set<IItemDescriptor<T>> currentSelection = new HashSet<IItemDescriptor<T>>();

		public Set<IItemDescriptor<T>> getCurrentSelection() {
			return currentSelection;
		}

		public void setCurrentSelection(Set<IItemDescriptor<T>> currentSelection) {
			this.currentSelection = currentSelection;
		}
	}

}

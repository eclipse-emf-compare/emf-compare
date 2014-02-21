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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.internal.engine.IItemDescriptor;
import org.eclipse.emf.compare.rcp.internal.engine.IItemRegistry;
import org.eclipse.emf.compare.rcp.internal.engine.impl.ItemUtil;
import org.eclipse.emf.compare.rcp.internal.match.DefaultRCPMatchEngineFactory;
import org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences;
import org.eclipse.emf.compare.rcp.internal.tracer.TracingConstant;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.ui.AbstractConfigurationUI;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.ui.IConfigurationUIFactory;
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
import org.eclipse.swt.custom.StackLayout;
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
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * Preference page for engines preferences
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class EnginesPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/** Label provider for {@link IItemDescriptor} */
	private final EngineDescriptorLabelProvider descriptorLabelProvider = new EngineDescriptorLabelProvider();

	/** Pointer to all {@link InteractiveUIContent} of each tab */
	private final Map<String, InteractiveUIContent> interactiveUis = new HashMap<String, InteractiveUIContent>();

	/** Data regarding the Difference selected engine */
	private final SingleValueHolder<IDiffEngine> diffEngineData = new SingleValueHolder<IDiffEngine>();

	/** Data regarding the Equivalence selected engine */
	private final SingleValueHolder<IEquiEngine> equiEngineData = new SingleValueHolder<IEquiEngine>();

	/** Data regarding the Requirement selected engine */
	private final SingleValueHolder<IReqEngine> reqEngineData = new SingleValueHolder<IReqEngine>();

	/** Data regarding the Conflicts detector selected engine */
	private final SingleValueHolder<IConflictDetector> conflictsDetectorData = new SingleValueHolder<IConflictDetector>();

	/** Data regarding the selected match engine factories. */
	private final MultipleValueHolder<IMatchEngine.Factory> matchEnginesData = new MultipleValueHolder<IMatchEngine.Factory>();

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
		createMatchEngineTab(tabFolder);
		// Create diff engine tab
		createDiffEngineTab(tabFolder);
		// Create equi engine tab
		createEquiEngineTab(tabFolder);
		// Create req engine tab
		createReqEngineTab(tabFolder);
		// Create conflicts detectors tab
		createConflictDetectorTab(tabFolder);

		return container;
	}

	/**
	 * Create a tab to select one Conflict Detector.
	 * 
	 * @param tabFolder
	 */
	private void createConflictDetectorTab(TabFolder tabFolder) {
		IItemRegistry<IConflictDetector> conflictDetectorDescriptorRegistry = EMFCompareRCPPlugin
				.getDefault().getConflictDetectorDescriptorRegistry();
		// Create tab structure
		Composite tabComposite = createTabSkeleton(tabFolder, EMFCompareRCPUIMessages
				.getString("EnginesPreferencePage.CONFLICT_DETECTOR_TAB_LABEL"),//$NON-NLS-1$
				EMFCompareRCPUIMessages.getString("EnginesPreferencePagestatic.CONFLICT_DETECTOR_INTRO_TEXT"));//$NON-NLS-1$
		// Create main content structure
		InteractiveUIContent contentStructure = createContentSkeleton(tabComposite);

		setUpUniqueCheckViewer(conflictDetectorDescriptorRegistry, contentStructure,
				EMFComparePreferences.CONFLICTS_DETECTOR, conflictsDetectorData);

		// Save for reset default
		interactiveUis.put(EMFComparePreferences.CONFLICTS_DETECTOR, contentStructure);
	}

	/**
	 * Create a tab to select one Requirement Engine.
	 * 
	 * @param tabFolder
	 */
	private void createReqEngineTab(TabFolder tabFolder) {
		IItemRegistry<IReqEngine> reqEngineDescriptorRegistry = EMFCompareRCPPlugin.getDefault()
				.getReqEngineDescriptorRegistry();
		// Create tab structure
		Composite tabComposite = createTabSkeleton(tabFolder, EMFCompareRCPUIMessages
				.getString("EnginesPreferencePage.REQUIREMENT_ENGINE_TAB_LABEL"), //$NON-NLS-1$
				EMFCompareRCPUIMessages.getString("EnginesPreferencePagestatic.REQ_ENGINE_INTRO_TEXT")); //$NON-NLS-1$
		// Create main content structure
		InteractiveUIContent contentStructure = createContentSkeleton(tabComposite);

		setUpUniqueCheckViewer(reqEngineDescriptorRegistry, contentStructure,
				EMFComparePreferences.REQ_ENGINES, reqEngineData);

		// Save for reset default
		interactiveUis.put(EMFComparePreferences.REQ_ENGINES, contentStructure);
	}

	/**
	 * Create a tab to select one Equivalence Engine.
	 * 
	 * @param tabFolder
	 */
	private void createEquiEngineTab(TabFolder tabFolder) {
		IItemRegistry<IEquiEngine> equiEngineDescriptorRegistry = EMFCompareRCPPlugin.getDefault()
				.getEquiEngineDescriptorRegistry();
		// Create tab structure
		Composite tabComposite = createTabSkeleton(tabFolder, EMFCompareRCPUIMessages
				.getString("EnginesPreferencePage.EQUIVALENCES_ENGINE_TAB_LABEL"), //$NON-NLS-1$
				EMFCompareRCPUIMessages.getString("EnginesPreferencePagestatic.EQUI_ENGINE_INTRO_TEXT")); //$NON-NLS-1$
		// Create main content structure
		InteractiveUIContent contentStructure = createContentSkeleton(tabComposite);

		setUpUniqueCheckViewer(equiEngineDescriptorRegistry, contentStructure,
				EMFComparePreferences.EQUI_ENGINES, equiEngineData);

		// Save for reset default
		interactiveUis.put(EMFComparePreferences.EQUI_ENGINES, contentStructure);
	}

	/**
	 * Create a tab to select one Difference Engine.
	 * 
	 * @param tabFolder
	 */
	private void createDiffEngineTab(TabFolder tabFolder) {
		IItemRegistry<IDiffEngine> diffEngineDescriptorRegistry = EMFCompareRCPPlugin.getDefault()
				.getDiffEngineDescriptorRegistry();
		// Create tab structure
		Composite tabComposite = createTabSkeleton(tabFolder, EMFCompareRCPUIMessages
				.getString("EnginesPreferencePage.DIFFERENCES_ENGINE_TAB_LABEL"), EMFCompareRCPUIMessages //$NON-NLS-1$
				.getString("EnginesPreferencePagestatic.DIFF_ENGINE_INTRO_TEXT")); //$NON-NLS-1$
		// Create main content structure
		InteractiveUIContent contentStructure = createContentSkeleton(tabComposite);

		setUpUniqueCheckViewer(diffEngineDescriptorRegistry, contentStructure,
				EMFComparePreferences.DIFF_ENGINES, diffEngineData);

		// Save for reset default
		interactiveUis.put(EMFComparePreferences.DIFF_ENGINES, contentStructure);

	}

	/**
	 * Create a tab to disable/enable Match Engines.
	 * 
	 * @param tabFolder
	 */
	private void createMatchEngineTab(TabFolder tabFolder) {
		IItemRegistry<Factory> matchEngineFactoryDescriptorRegistry = EMFCompareRCPPlugin.getDefault()
				.getMatchEngineFactoryDescriptorRegistry();
		IItemDescriptor<Factory> defaultMatchEngineDescriptor = matchEngineFactoryDescriptorRegistry
				.getItemDescriptor(DefaultRCPMatchEngineFactory.class.getCanonicalName());
		Composite tabComposite = createTabSkeleton(tabFolder, EMFCompareRCPUIMessages
				.getString("EnginesPreferencePage.MATCH_ENGINE_TAB_LABEL"), EMFCompareRCPUIMessages //$NON-NLS-1$
				.getString("EnginesPreferencePagestatic.MATCH_ENGINE_INTRO_TEXT")); //$NON-NLS-1$
		// Create main content structure
		InteractiveUIContent interactiveUI = createContentSkeleton(tabComposite);
		// Create viewer
		Map<String, IConfigurationUIFactory> configuratorUIRegistry = EMFCompareRCPUIPlugin.getDefault()
				.getMatchEngineConfiguratorRegistry();
		String matchEnginePreferenceKey = EMFComparePreferences.MATCH_ENGINE_DISABLE_ENGINES;
		setUpMultipleCheckViewer(matchEngineFactoryDescriptorRegistry, interactiveUI,
				matchEnginePreferenceKey, configuratorUIRegistry);

		// Save for reset default
		interactiveUis.put(matchEnginePreferenceKey, interactiveUI);
		// Init default
		interactiveUI.select(defaultMatchEngineDescriptor);
		matchEnginesData.setCurrentSelection(getActiveItems(matchEngineFactoryDescriptorRegistry,
				matchEnginePreferenceKey));
		bindMultipleData(MultipleValueHolder.DATA_FIELD_NAME, interactiveUI.getViewer(), matchEnginesData);
	}

	/**
	 * Create the skeleton of a tab. All needed composites and layout.
	 * 
	 * @param tabComposite
	 * @return
	 */
	private InteractiveUIContent createContentSkeleton(Composite tabComposite) {
		Composite contentComposite = new Composite(tabComposite, SWT.NONE);
		contentComposite.setLayout(new GridLayout(2, true));
		contentComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		// Engine chooser composite
		Composite viewerComposite = new Composite(contentComposite, SWT.NONE);
		viewerComposite.setLayout(new GridLayout(1, true));
		viewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		// Config composite
		Group configComposite = createConfigComposite(contentComposite);
		// Descriptor engine Text
		Text descriptionText = createDescriptionComposite(tabComposite);
		return new InteractiveUIContent(descriptionText, viewerComposite, configComposite);
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
	 * Composite for description. This composite hold the text widget that will update with the current
	 * selection
	 * 
	 * @param composite
	 * @return
	 */
	private Text createDescriptionComposite(Composite composite) {
		Group descriptionComposite = new Group(composite, SWT.BORDER);
		descriptionComposite.setText(EMFCompareRCPUIMessages
				.getString("EnginesPreferencePagestatic.DESCRIPTION_COMPOSITE_LABEL")); //$NON-NLS-1$
		descriptionComposite.setLayout(new GridLayout(1, false));
		descriptionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		Text engineDescriptionText = new Text(descriptionComposite, SWT.WRAP | SWT.MULTI);
		engineDescriptionText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		layoutData.heightHint = 50;
		engineDescriptionText.setLayoutData(layoutData);
		engineDescriptionText.setEditable(false);
		return engineDescriptionText;
	}

	/**
	 * Create the composite that will hold all configurations for a tab.
	 * 
	 * @param composite
	 * @return
	 */
	private Group createConfigComposite(Composite composite) {
		Group configurationComposite = new Group(composite, SWT.BORDER);
		configurationComposite.setText(EMFCompareRCPUIMessages
				.getString("EnginesPreferencePagestatic.CONFIGURATION_COMPOSITE_LABEL")); //$NON-NLS-1$
		StackLayout layout = new StackLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		configurationComposite.setLayout(layout);
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		configurationComposite.setLayoutData(layoutData);
		return configurationComposite;
	}

	/**
	 * Set up a {@link CheckboxTableViewer} which accept only one element checked at a time
	 * 
	 * @param registry
	 * @param interactiveUI
	 * @param preferenceKey
	 * @param dataObject
	 * @param configuratorRegistry
	 */
	private <T> void setUpUniqueCheckViewer(IItemRegistry<T> registry,
			final InteractiveUIContent interactiveUI, String preferenceKey,
			final SingleValueHolder<T> dataObject) {

		final CheckboxTableViewer descriptorViewer = CheckboxTableViewer.newCheckList(interactiveUI
				.getViewerComposite(), SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.SINGLE);
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
						MessageDialog
								.openWarning(
										getShell(),
										EMFCompareRCPUIMessages
												.getString("EnginesPreferencePagestatic.INCORRECT_SELECTION_TITLE"), //$NON-NLS-1$
										EMFCompareRCPUIMessages
												.getString("EnginesPreferencePagestatic.INCORRECT_SELECTION_MESSAGE")); //$NON-NLS-1$
					}
				}

			}
		});

		List<IItemDescriptor<T>> itemDescriptors = registry.getItemDescriptors();
		Collections.sort(itemDescriptors, Collections.reverseOrder());
		descriptorViewer.setInput(itemDescriptors);

		interactiveUI.setViewer(descriptorViewer);

		// Init default value
		IItemDescriptor<T> defaultEngine = ItemUtil.getDefaultItemDescriptor(registry, preferenceKey,
				EMFCompareRCPPlugin.getDefault().getEMFComparePreferences());
		interactiveUI.select(defaultEngine);
		interactiveUI.checkElement(defaultEngine);
		dataObject.setCurrentSelection(defaultEngine);

	}

	/**
	 * Set up a {@link CheckboxTableViewer} which accept multiple checked element.
	 * 
	 * @param registry
	 * @param interactiveUI
	 * @param preferenceKey
	 * @param configuratorRegistry
	 */
	private <T> void setUpMultipleCheckViewer(IItemRegistry<T> registry,
			final InteractiveUIContent interactiveUI, String preferenceKey,
			Map<String, IConfigurationUIFactory> configuratorUIRegistry) {
		CheckboxTableViewer descriptorViewer = CheckboxTableViewer.newCheckList(interactiveUI
				.getViewerComposite(), SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
		interactiveUI.setViewer(descriptorViewer);
		descriptorViewer.setContentProvider(ArrayContentProvider.getInstance());
		descriptorViewer.setLabelProvider(descriptorLabelProvider);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		descriptorViewer.getControl().setLayoutData(gd);

		// Init configuration elements
		for (IItemDescriptor<T> item : registry.getItemDescriptors()) {
			String itemId = item.getID();
			IConfigurationUIFactory configuratorFactory = configuratorUIRegistry.get(itemId);
			if (configuratorFactory != null) {
				Preferences pref = ItemUtil.getConfigurationPreferenceNode(preferenceKey, itemId);
				interactiveUI.addConfigurator(itemId, configuratorFactory, pref);
			}
		}
		// Filter input with input with higher rank than default item descriptor
		List<IItemDescriptor<T>> itemDescriptors = registry.getItemDescriptors();
		Collections.sort(itemDescriptors, Collections.reverseOrder());
		descriptorViewer.setInput(itemDescriptors);

	}

	/**
	 * Get all active item from a registry. (Filter out all disable element stored in preferences)
	 * 
	 * @param registry
	 * @param preferenceKey
	 *            Preference key where are stored disabled items.
	 * @return
	 */
	private <T> Set<IItemDescriptor<T>> getActiveItems(IItemRegistry<T> registry, String preferenceKey) {
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

	/**
	 * Bind UI to data object.
	 * 
	 * @param engineBindingProperty
	 * @param descriptorViewer
	 * @param dataObject
	 */
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

		setEnginesPreferences();

		storeConfigurations();

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

	/**
	 * Set all engines preferences.
	 */
	private void setEnginesPreferences() {
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
	}

	/**
	 * Store the value of the configuration of each engine into preferences.
	 */
	private void storeConfigurations() {
		for (Entry<String, InteractiveUIContent> interactiveContentEntry : interactiveUis.entrySet()) {
			for (Entry<String, AbstractConfigurationUI> configuratorEntry : interactiveContentEntry
					.getValue().getConfigurators().entrySet()) {
				AbstractConfigurationUI configurator = configuratorEntry.getValue();
				configurator.storeConfiguration();
			}
		}

		if (TracingConstant.CONFIGURATION_TRACING_ACTIVATED) {
			StringBuilder traceMessage = new StringBuilder("Configuration serialization:\n"); //$NON-NLS-1$
			String prefDelimiter = " :\n"; //$NON-NLS-1$
			String new_line = "\n"; //$NON-NLS-1$
			String node_Label = "Node "; //$NON-NLS-1$
			String double_dot_label = " : "; //$NON-NLS-1$
			String empty_label = "EMPTY"; //$NON-NLS-1$
			for (Entry<String, InteractiveUIContent> interactiveContentEntry : interactiveUis.entrySet()) {
				String itemTypeId = interactiveContentEntry.getKey();
				for (Entry<String, AbstractConfigurationUI> configuratorEntry : interactiveContentEntry
						.getValue().getConfigurators().entrySet()) {
					String itemToConfigureId = configuratorEntry.getKey();
					Preferences storeNode = ItemUtil.getConfigurationPreferenceNode(itemTypeId,
							itemToConfigureId);
					traceMessage.append(node_Label).append(storeNode.absolutePath()).append(prefDelimiter);
					try {
						for (String propertyKey : storeNode.keys()) {
							traceMessage.append(propertyKey).append(double_dot_label).append(
									storeNode.get(propertyKey, empty_label)).append(new_line);
						}
					} catch (BackingStoreException e) {
						e.printStackTrace();
						traceMessage.append("Error in tracing ").append(storeNode.absolutePath()); //$NON-NLS-1$
					}
				}
			}

			EMFCompareRCPPlugin.getDefault().log(IStatus.INFO, traceMessage.toString());
		}

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

		resetConfigurations();

		super.performDefaults();
	}

	/**
	 * Reset all configuration of each engine to its default value.
	 */
	private void resetConfigurations() {
		for (Entry<String, InteractiveUIContent> interactiveContentEntry : interactiveUis.entrySet()) {
			for (AbstractConfigurationUI configurator : interactiveContentEntry.getValue().getConfigurators()
					.values()) {
				configurator.resetDefault();
			}
		}
	}

	/**
	 * Reset engine preference to default using highest rank strategy.
	 * 
	 * @param registry
	 * @param preferenceKey
	 */
	private <T> void resetDefaultPreferencesToHighestRank(IItemRegistry<T> registry, String preferenceKey,
			SingleValueHolder<T> dataObject) {
		InteractiveUIContent interactiveContent = interactiveUis.get(preferenceKey);
		if (interactiveContent != null) {
			IItemDescriptor<T> defaultEngine = registry.getHighestRankingDescriptor();
			interactiveContent.select(defaultEngine);
			interactiveContent.checkElement(defaultEngine);
			dataObject.setCurrentSelection(defaultEngine);
		}
	}

	/**
	 * Reset to default for a collection (using all is default).
	 * 
	 * @param registry
	 * @param preferenceKey
	 * @param dataObject
	 */
	private <T> void resetDefaultPreferencesToAll(IItemRegistry<T> registry, String preferenceKey,
			MultipleValueHolder<T> dataObject) {
		InteractiveUIContent interactiveContent = interactiveUis.get(preferenceKey);
		if (interactiveContent != null) {
			IItemDescriptor<T> defaultEngine = registry.getHighestRankingDescriptor();
			interactiveContent.select(defaultEngine);
			List<IItemDescriptor<T>> itemDescriptors = registry.getItemDescriptors();
			interactiveContent.checkElements(itemDescriptors.toArray(new IItemDescriptor[itemDescriptors
					.size()]));
			dataObject.setCurrentSelection(Sets.newHashSet(itemDescriptors));
		}
	}

	/**
	 * Set an engine preferences into the preferences.
	 * 
	 * @param preferenceKey
	 * @param currentSelectedEngine
	 * @param defaultConf
	 */
	private <T> void setEnginePreferences(String preferenceKey, IItemDescriptor<T> currentSelectedEngine,
			IItemDescriptor<T> defaultConf) {
		if (currentSelectedEngine != null && !currentSelectedEngine.equals(defaultConf)) {
			getPreferenceStore().setValue(preferenceKey, currentSelectedEngine.getID());
		} else {
			getPreferenceStore().setToDefault(preferenceKey);
		}
	}

	/**
	 * Set an engine preferences into the preferences (for a collection).
	 * 
	 * @param preferenceKey
	 * @param currentSelectedEngine
	 * @param defaultConf
	 */
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
			getPreferenceStore().setToDefault(preferenceKey);
		}
	}

	/**
	 * Label provider for {@link ILabeledItemDescriptor}
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static final class EngineDescriptorLabelProvider extends LabelProvider {
		/**
		 * {@inheritDoc}
		 */
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

		private static final String DATA_FIELD_NAME = "currentSelection"; //$NON-NLS-1$

		public Set<IItemDescriptor<T>> currentSelection = new HashSet<IItemDescriptor<T>>();

		public Set<IItemDescriptor<T>> getCurrentSelection() {
			return currentSelection;
		}

		public void setCurrentSelection(Set<IItemDescriptor<T>> currentSelection) {
			this.currentSelection = currentSelection;
		}
	}

	/**
	 * Structure that handle UI interactive content (Description, configuration and viewer).
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static class InteractiveUIContent {
		/** Text that shall be update with the description of the viewer. */
		private final Text descriptionText;

		/** Composite holding the viewer. */
		private final Composite viewerCompsite;

		/**
		 * Composite holding the configuration. This shall react to a selection in the viewer.
		 */
		private final Composite configurationComposite;

		/** Composite that is used when the selection has no registered configuration. */
		private final Composite defaultComposite;

		/** Viewer of {@link IItemDescriptor}. */
		private CheckboxTableViewer viewer;

		/** List of all {@link AbstractConfigurationUI} that is linked to this viewer. */
		private final Map<String, AbstractConfigurationUI> configurators = new HashMap<String, AbstractConfigurationUI>();

		/**
		 * Constructor.
		 * 
		 * @param descriptionText
		 *            {@link InteractiveUIContent#descriptionText}
		 * @param viewerCompsite
		 *            {@link InteractiveUIContent#viewerCompsite}
		 * @param configurationComposite
		 *            {@link InteractiveUIContent#configurationComposite}
		 */
		public InteractiveUIContent(Text descriptionText, Composite viewerCompsite,
				Composite configurationComposite) {
			super();
			this.descriptionText = descriptionText;
			this.viewerCompsite = viewerCompsite;
			this.configurationComposite = configurationComposite;
			// Init default composite.
			defaultComposite = new Composite(configurationComposite, SWT.NONE);
			defaultComposite.setLayout(new GridLayout(1, true));
			Label text = new Label(defaultComposite, SWT.WRAP);
			text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
			text.setText(EMFCompareRCPUIMessages
					.getString("InteractiveUIContent.DEFAULT_CONFIGURATION_LABEL")); //$NON-NLS-1$
		}

		/**
		 * Add a configuration to this Interactive content.
		 * 
		 * @param id
		 *            Id of the item to configure
		 * @param configuratorfactory
		 *            Factory for the configuration
		 */
		public void addConfigurator(String id, IConfigurationUIFactory configuratorfactory, Preferences pref) {
			AbstractConfigurationUI configurator = configuratorfactory.createUI(configurationComposite,
					SWT.NONE, pref);
			configurators.put(id, configurator);
		}

		/**
		 * Check one element in the viewer
		 * 
		 * @param descriptor
		 */
		public void checkElement(IItemDescriptor<?> descriptor) {
			viewer.setCheckedElements(new Object[] {descriptor });
		}

		/**
		 * Check multiple element in the viewer. (Only use if multiple selection is allowed)
		 * 
		 * @param descriptors
		 */
		public void checkElements(IItemDescriptor<?>[] descriptors) {
			viewer.setCheckedElements(descriptors);
		}

		/**
		 * @param viewer
		 *            A {@link StructuredViewer} of {@link IItemDescriptor}
		 */
		public void setViewer(CheckboxTableViewer inputViewer) {
			this.viewer = inputViewer;
			viewer.addSelectionChangedListener(new ConfigurationListener());
			viewer.addSelectionChangedListener(new DescriptionListener());
		}

		/**
		 * @return A map of all configuration.
		 */
		public Map<String, AbstractConfigurationUI> getConfigurators() {
			return ImmutableMap.copyOf(configurators);
		}

		/**
		 * Handle a selection in the viewer. Update related components.
		 * 
		 * @param descriptor
		 */
		public void select(IItemDescriptor<?> descriptor) {
			// Update viewer
			viewer.setSelection(new StructuredSelection(descriptor), true);
			updateLinkedElements(descriptor);
		}

		/**
		 * Update linked element in
		 * 
		 * @param descriptor
		 */
		private void updateLinkedElements(IItemDescriptor<?> descriptor) {
			// Update description
			descriptionText.setText(descriptor.getDescription());

			StackLayout stackLayout = (StackLayout)configurationComposite.getLayout();
			if (configurators.containsKey(descriptor.getID())) {
				stackLayout.topControl = configurators.get(descriptor.getID());
			} else {
				stackLayout.topControl = defaultComposite;
			}
			configurationComposite.layout();
		}

		/**
		 * @return The composite holding the viewer.
		 */
		public Composite getViewerComposite() {
			return viewerCompsite;
		}

		/**
		 * @return The viewer.
		 */
		public CheckboxTableViewer getViewer() {
			return viewer;
		}

		/**
		 * Listener in charge of updating the the configuration composite.
		 * 
		 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
		 */
		private final class ConfigurationListener implements ISelectionChangedListener {
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					IStructuredSelection structSelection = (IStructuredSelection)selection;
					Object selected = structSelection.getFirstElement();
					if (selected instanceof IItemDescriptor<?>) {
						updateLinkedElements(((IItemDescriptor<?>)selected));
					}
				}
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
					if (selected instanceof IItemDescriptor<?>) {
						IItemDescriptor<?> desc = (IItemDescriptor<?>)selected;
						String description = desc.getDescription();
						descriptionText.setText(description);
					}
				}

			}
		}

	}

}
